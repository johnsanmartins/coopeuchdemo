package com.example.springboot;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.emma.store.application.entity.anotation.Colum;

public class Mapper<T> implements RowMapper<T> {

	private final Logger logger;
	private final Class<T> clase;

	public Mapper(Class<T> clase) {
		this.clase = clase;
		this.logger = LoggerFactory.getLogger("com.emma.store.application.dao.backend.customer.mappe.Mapper" + clase.getSimpleName());
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T maper = newInstance();
		logger.debug("mappeo N:{}, instancia creada: {} ", rowNum, !Objects.isNull(maper));
		ResultSetMetaData md = rs.getMetaData();
		if (Objects.isNull(maper)) {
			return maper;
		}
		Field[] file = clase.getDeclaredFields();
		for (Field field : file) {
			Colum colum = field.getAnnotation(Colum.class);
			Object value = null;
			if (!Objects.isNull(colum)) {
				String name = nameColum(md, colum);
				if(!name.isEmpty()) {
					if(!name.isBlank()) {
						value =runGet(colum, rs, name);
					} else{
						logger.info("name not valid: {}", field.getName());
					}
				} else {
					logger.info("name not valid: {}", field.getName());
				}
				runSetter(field, value, maper);
			}
		}
					
		return maper;
	}
	
	private Object runGet(Colum colum, ResultSet rs, String name) throws SQLException {
		Object value = null;
		switch (colum.type()) {
		case DOUBLE:
			value  = rs.getDouble(name);
			break;
		default:
			value = rs.getObject(name);
			break;
		}
		return value;
	}
	
	private String nameColum(ResultSetMetaData md, Colum colum) throws SQLException {
		String name = "";
		for (int i=1; i<=md.getColumnCount(); i++) {
			String tableColumnName =md.getColumnLabel(i);
			String columnName = colum.name();
			if(tableColumnName.equalsIgnoreCase(columnName.replaceAll("\"", ""))) {
				name  = tableColumnName;
			}
		}
		return name;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private T newInstance() {
		T result = null;
		try {
			Constructor[] cons = clase.getConstructors();
			for (Constructor<T> constructor : cons) {
				if (constructor.getGenericParameterTypes().length == 0) {
					result = constructor.newInstance();
				}
			}
		} catch (InstantiationException e) {
			logger.warn("new instance; InstantiationException: {}", e.getMessage());
		} catch (IllegalAccessException e) {
			logger.warn("new instance; IllegalAccessException: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.warn("new instance; IllegalArgumentException: {}", e.getMessage());
		} catch (InvocationTargetException e) {
			logger.warn("new instance; InvocationTargetException: {}", e.getMessage());
		} catch (SecurityException e) {
			logger.warn("new instance; SecurityException: {}", e.getMessage());
		}
		return result;
	}

	private void runSetter(Field field, Object object, T instance) {
		for (Method method : clase.getMethods()) {
			if ((method.getName().startsWith("set")) && (method.getName().length() == field.getName().length() + 3)) {
				if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
					try {
						method.invoke(instance, object);
					} catch (IllegalAccessException e) {
						logger.warn("run setter; IllegalAccessException: {}, {}, {}", e.getMessage(), field.getName(), object);
					} catch (IllegalArgumentException e) {
						logger.warn("run setter; IllegalArgumentException: {}, {}, {}", e.getMessage(), field.getName(), object);
					} catch (InvocationTargetException e) {
						logger.warn("run setter; InvocationTargetException: {}, {}, {}", e.getMessage(), field.getName(), object);
					}
				}
			}
		}
	}

}
