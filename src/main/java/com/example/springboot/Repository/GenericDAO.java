package com.example.springboot.Repository;

import java.net.ConnectException;
import java.util.List;

import org.springframework.dao.QueryTimeoutException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.example.springboot.Dto.TareaDto;


public interface GenericDAO<T,ID>{

	
	@Retryable(value = {QueryTimeoutException.class, ConnectException.class}, maxAttemptsExpression =  "${retry.max}", backoff = @Backoff(delayExpression = "${retry.time}"))
	public List<T> all();

	public T save(T data);

	public T update(TareaDto tareaDto, ID id);

	public Boolean deleteByID(ID id);


}
