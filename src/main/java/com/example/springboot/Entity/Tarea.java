package com.example.springboot.Entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class Tarea {
	
	private Integer id;
	
	private String descripcion;
	
	private Date fechaCreacion ;
	
	private boolean vigente;
	
	

}
