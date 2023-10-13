package com.example.springboot.Service;

import java.util.List;

import com.example.springboot.Dto.TareaDto;
import com.example.springboot.Request.TareaRequest;

public interface TareaService {

	public List<TareaDto> listarTareas();
	
	public TareaDto saveTareas(TareaRequest tareaRequest);
	
	public TareaDto updateTareas(TareaRequest tareaRequest);
	
	public TareaDto deleteTareas(Integer id);
	
	
}
