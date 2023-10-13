package com.example.springboot.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.Dto.TareaDto;
import com.example.springboot.Request.TareaRequest;
import com.example.springboot.Service.TareaService;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;

@RestController("RecordCreditControllerV6")
@Api(tags = "DemoTarea")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST })
@AllArgsConstructor
public class TareaController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final TareaService service;

	@GetMapping("tareas")
	public ResponseEntity<List<TareaDto>> listarTareas() {
		logger.info("iniciando el listado de tareas ");
		List<TareaDto> response = service.listarTareas();
		logger.info("finalizando el proceso de registro ");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping("tareas")
	public ResponseEntity<TareaDto> saveTareas(
			@RequestBody TareaRequest tareaRequest) {
		logger.info(" finalizando el proceso de registro ");
		TareaDto response = service.saveTareas(tareaRequest);
		logger.info(" finalizando el proceso de registro ");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	
	@PutMapping("tareas")
	public ResponseEntity<TareaDto> updateTareas(
			@RequestBody TareaRequest tareaRequest) {
		TareaDto response = service.updateTareas(tareaRequest);
		logger.info("finalizando el proceso de actualizacion ");
		logger.info(response.toString());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PutMapping("tareas")
	public ResponseEntity<TareaDto> updateTareas(
			@RequestBody Integer id) {
		TareaDto response = service.deleteTareas(id);
		logger.info("finalizando el proceso de eliminación ");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
