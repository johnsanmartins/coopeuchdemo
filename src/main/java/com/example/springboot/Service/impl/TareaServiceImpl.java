package com.example.springboot.Service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Service;

import com.example.springboot.Dto.TareaDto;
import com.example.springboot.Entity.Tarea;
import com.example.springboot.Repository.tareas.TareaRepository;
import com.example.springboot.Request.TareaRequest;
import com.example.springboot.Service.TareaService;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class TareaServiceImpl implements TareaService{

	
	private final TareaRepository repository;
	private final ObjectMapper mapper;
	private final ModelMapper model;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final ConfigurableEnvironment env;
	
	
	public TareaServiceImpl(ObjectMapper mapper, ModelMapper model, TareaRepository repository,  ConfigurableEnvironment env) {
		super();
		this.model = model;
		this.repository = repository;
		this.mapper = mapper;
		this.env = env;
	}
	
	@Override
	public List<TareaDto> listarTareas() {
		List<Tarea> all = this.repository.all();
		List<TareaDto> responst = model.map(Optional.ofNullable(all).orElse(new ArrayList<>()),
				new TypeToken<List<TareaDto>>() {
				}.getType());
		return responst;
	}

	@Override
	public TareaDto saveTareas(TareaRequest tareaRequest) {
		logger.info("Guardando registro");
		Tarea tarea = model.map(tareaRequest, Tarea.class);
		tarea = repository.save(tarea);
		TareaDto response = model.map(tarea, TareaDto.class);
		return response;
	}

	@Override
	public TareaDto updateTareas(TareaRequest tareaRequest) {
		Tarea tarea = null;
		
		tarea = repository.update(tarea);
		TareaDto response = model.map(tarea, TareaDto.class);
		return response;
	}

	@Override
	public TareaDto deleteTareas(Integer id) {
		Boolean tarea = null;
		tarea = repository.deleteByID(id);
		return model.map(TareaDto.class, null);
	}



}
