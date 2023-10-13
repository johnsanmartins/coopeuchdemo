package com.example.springboot.Repository.impl;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.emma.store.application.entity.customer.company.Stakeholder;
import com.example.springboot.Mapper;
import com.example.springboot.Entity.Tarea;
import com.example.springboot.Repository.TemplateJDBC;
import com.example.springboot.Repository.tareas.TareaRepository;
import com.example.springboot.Service.EntityDDD;




public class TareasJDBC implements TareaRepository, TemplateJDBC {
	
	 private final Logger logger = LoggerFactory.getLogger(getClass());

	    private final EntityDDD<Tarea> ddd = new EntityDDD<>(null, Tarea.class);

	    private final JdbcTemplate jdbc;

	    public TareasJDBC(@Qualifier("jdbc-backend-customer") JdbcTemplate jdbc) {
	        this.jdbc = jdbc;
	    }

		@Override
		public List<Tarea> all() {
			 String sql = String.format(SELECT_QUERY,  "");
		        List<Tarea> all = jdbc.query(sql, new Mapper<Tarea>(Tarea.class));
		        return all;
		}
		


		@Override
		public Tarea save(Tarea tarea) {
			  ddd.setVal(tarea);
		        String sql = String.format(INSERT_QUERY,tarea.getDescripcion(),tarea.getFechaCreacion(), tarea.isVigente());
		        Integer id = jdbc.queryForObject(sql, Integer.class, ddd.camposInsert());
		        tarea.setId(id);
		        logger.info("respuesta de la ejecucion de la query save: {} ", id);
		        return tarea;

		}

		@Override
		public Tarea update(Tarea tarea) {
			  ddd.setVal(tarea);
		        String sql = String.format(UPDATE_QUERY, tarea.getDescripcion(),tarea.getFechaCreacion(), tarea.isVigente());
		        int i = jdbc.update(sql, argument( ddd.camposSetUpdate()));
		        logger.info("respuesta de la ejecucion de la query update: {} ", i);
		        return tarea;
		}

		@Override
		public Boolean deleteByID(Integer id) {
			  TemplateJDBC.deleteByAtomicId(jdbc, Tarea.class, id);
		        return true;
		}


}
