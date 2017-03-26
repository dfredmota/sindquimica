package br.developersd3.sindquimica.service;
import java.util.List;

import br.developersd3.sindquimica.exception.GenericException;


public interface GenericService<T> {

	public List<T> all(Integer empresaSistema);

	public T getById(Integer id,Integer empresaSistema);

	public T create(T entity,Integer empresaSistema) throws GenericException;

	public T update(T entity) throws GenericException;

	public void delete(T entity) throws GenericException;
	
	public List<T> searchByFilters(T entity,String field);

}