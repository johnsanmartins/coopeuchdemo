package com.example.springboot.Repository;

import java.text.MessageFormat;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.springboot.Mapper;
import com.example.springboot.Service.EntityDDD;

public abstract class GenericJDBC<T, ID> implements GenericDAO<T, ID>, TemplateJDBC {

	private final Logger logger;
	private final EntityDDD<T> ddd;
	private final JdbcTemplate jdbc;
	private final Mapper mapper;

	public GenericJDBC(JdbcTemplate jdbc, Logger logger, Class<T> clase) {
		this.logger = logger;
		this.jdbc = jdbc;
		this.ddd = new EntityDDD<T>(null, clase);
		this.mapper = new Mapper<>(clase);
	}

	@Override
	public List<T> all() {
		String sql = String.format(SELECT_QUERY, getDdd().camposName(), getDdd().tableName(), "");
		logger.info("Query exec: \"{}\"", sql);
		List<T> all = jdbc.query(sql, getMapper());
		return all;
	}


	@Override
	public T save(T data) {
		getDdd().setVal(data);
		String sql = String.format(INSERT_QUERY, getDdd().tableName(), getDdd().camposInsertName(), getDdd().valuesInsertName(), "RETURNING", ddd.idName());
		logger.info("Query exec: \"{}\"", sql);
		Integer id = jdbc.queryForObject(sql, Integer.class, ddd.camposInsert());
		data = getDdd().setID(id, data);
		logger.info("respuesta de la ejecucion de la query save: {} ", id);
		return data;
	}

	
	public String where(String...where) {
		StringBuilder sb = new StringBuilder();
		for (String string : where) {
			sb.append(MessageFormat.format(" AND {} = ?", string));
		}
		return sb.toString();
	}


	public Logger getLogger() {
		return logger;
	}

	public EntityDDD<T> getDdd() {
		return ddd;
	}

	public JdbcTemplate getJdbc() {
		return jdbc;
	}

	public Mapper<T> getMapper() {
		return mapper;
	}

}
