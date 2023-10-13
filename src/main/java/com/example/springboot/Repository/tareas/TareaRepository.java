package com.example.springboot.Repository.tareas;

import java.net.ConnectException;
import java.util.List;

import org.springframework.dao.QueryTimeoutException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.example.springboot.Entity.Tarea;

public interface TareaRepository {
	
    @Retryable(value = {QueryTimeoutException.class, ConnectException.class}, maxAttemptsExpression =  "${retry.max}", backoff = @Backoff(delayExpression = "${retry.time}"))
    public List<Tarea> all();
	
    public Tarea save(Tarea tarea);

    public Tarea update(Tarea tarea);

    public Boolean deleteByID(Integer id);

}
