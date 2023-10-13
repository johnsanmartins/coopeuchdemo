package com.example.springboot.Request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TareaRequest {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer id;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String descripcion;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date fechaCreacion;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private boolean vigente;

}
