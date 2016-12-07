package br.developersd3.sindquimica.service;
import java.util.List;

import br.developersd3.sindquimica.exception.GenericException;


public interface GenericService<T> {

	public List<T> all();

	public T getById(Integer id);

	public T create(T entity) throws GenericException;

	public T update(T entity) throws GenericException;

	public void delete(T entity) throws GenericException;

}