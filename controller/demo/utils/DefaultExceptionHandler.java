package com.example.demo.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errorMessages = new LinkedList<>();
		ex.getBindingResult().getAllErrors().forEach(err -> errorMessages.add(err.getDefaultMessage()));
		List<ApplicationException> exceptions = errorMessages.stream().map(ApplicationException::new).
				collect(Collectors.toList());
		return new ResponseEntity<>(exceptions, status);
	}
	
	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ApplicationException> handleApplicationException(ApplicationException e){
		return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
	}
}
