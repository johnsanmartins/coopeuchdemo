package com.example.springboot.Repository;

import com.emma.store.application.dao.customer.mapper.Mapper;
import com.emma.store.application.service.EntityDDD;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * This interface contains convenience methods and constants to do deal with retrieval/persistence operation using JDBC template.
 *
 * @author UNICOMER
 */
public interface TemplateJDBC {

    /**
     * Template for SQL-SELECT query that yield single row.
     */
    public final String SINGLE_SELECT = "SELECT %s FROM %s ";

    /**
     * Template for SQL-SELECT query that yield multiple rows.
     */
    public final String SELECT_QUERY = "SELECT %s FROM %s WHERE 1=1 %s ;";

    /**
     * Template for SQL-INSERT queries which return a new primary (BIGINT) key.
     */
    public final String INSERT_QUERY = "INSERT INTO %s (%s) VALUES(%s) %s %s;";

    /**
     * Template for SQL-UPDATE queries.
     */
    public final String UPDATE_QUERY = "UPDATE %s SET %s WHERE 1 = 1 %s;";

    /**
     * Template for SQL-DELETE queries
     */
    public final String DELETE_QUERY = "DELETE FROM %s WHERE %s";

    String DELETE_BY_ID_QUERY = "DELETE FROM %s WHERE %s = ?;";

    String INSERT_CREATED_DATE = "UPDATE %s SET created_at = now() WHERE %s = %d;";

    String INSERT_UPDATE_DATE = "UPDATE %s SET updated_at = now() WHERE %s = %d;";

    /**
     * @param ob
     * @param obj
     * @return Can't grasp what's this method is used for.
     */
    default public Object[] argument(Object ob, Object... obj) {
        Object[] argument = new Object[obj.length + 1];
        for (int i = 0; i < obj.length; i++) {
            argument[i] = obj[i];
        }
        argument[obj.length] = ob;
        return argument;
    }

    /**
     * @param ob
     * @param obj
     * @return Can't grasp what's this method is used for.
     */
    default public Object[] argument(Object[] ob, Object[] obj) {
        Object[] argument = new Object[ob.length + obj.length];
        int i = 0;
        for (; i < ob.length; i++) {
            argument[i] = ob[i];
        }
        for (int j=0; j < obj.length; j++, i++) {
            argument[i] = obj[j];
        }
        return argument;
    }


    /**
     * Runs a query and returns the 1st resulting row.
     *
     * @param <T>        Java bean class
     * @param template   Search in spring javadocs for more info.
     * @param sqlText    SELECT-SQL query
     * @param parameters Query parameters (empty array if no parameters are required).
     * @param clazz      Java bean class that will store retrieved values.
     * @return A new java bean containing result's fields, or null if the query yielded no rows.
     */
    @SuppressWarnings("deprecation")
    public static <T> T queryForObject(JdbcTemplate template, String sqlText, Object parameters[], Class<T> clazz) {

        List<T> result = template.query(sqlText, parameters, new Mapper<T>(clazz));

        if (result.size() > 0) {
            return result.iterator().next();
        }

        return null;
    }

    /**
     * @param <T>      Java bean class mapped to a postgres table via annotations defined in <code>com.emma.store.application.entity.anotation</code> package.
     * @param template Search in spring javadocs for more info.
     * @param bean     Instance of generic type <b>T</b>.
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public static <T> void insertReturnNoKey(JdbcTemplate template, T bean) {
        Class<T> clazz = (Class<T>) bean.getClass();
        EntityDDD<T> entityDML = new EntityDDD<>(bean, clazz);
        String insertSQL = String.format(INSERT_QUERY, entityDML.tableName(), entityDML.camposInsertName(), entityDML.valuesInsertName(), ' ', ' ');


        template.update(insertSQL, entityDML.insertValues());
    }

    /**
     * Deletes a record from database.
     *
     * @param <T>      Java bean class mapped to a postgres table via annotations defined in <code>com.emma.store.application.entity.anotation</code> package.
     * @param template Search in spring javadocs for more info.
     * @param clazz    Typed class.
     * @param id       Atomic (integer, string, date) id.
     * @throws DataAccessException
     */
    public static <T> void deleteByAtomicId(JdbcTemplate template, Class<T> clazz, Object id) throws DataAccessException {
        EntityDDD<T> entityDML;

        try {
            entityDML = new EntityDDD<>(clazz.getDeclaredConstructor().newInstance(), clazz);
            String idName = entityDML.idName();
            String deleteSQL = String.format(DELETE_QUERY, entityDML.tableName(), ' ' + idName + "= ?");
            template.update(deleteSQL, new Object[]{id});
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new DAOException(String.format("Can't delete instance of %s using id=%s", clazz.getName(), id), e);
        }
    }
}
