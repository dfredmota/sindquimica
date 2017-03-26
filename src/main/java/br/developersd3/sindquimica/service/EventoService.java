package br.developersd3.sindquimica.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.daos.EventoDao;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Evento;

@Named("eventoService")
public class EventoService implements GenericService<Evento> {

	@Inject
	@Named("eventoDao")
	private EventoDao dao;

	@Override
	public List<Evento> all(Integer empresaSistema) {
		return dao.findAll(empresaSistema);
	}

	@Override
	public Evento getById(Integer id,Integer empresaSistema) {
		return dao.getById(Evento.class,id,empresaSistema);
	}

	@Override
	public Evento create(Evento entity,Integer empresaSistema) throws GenericException {
		entity.setEmpresaSistema(empresaSistema);
		return dao.save(entity);
	}

	@Override
	public Evento update(Evento entity) throws GenericException {
		return dao.update(entity);
	}

	@Override
	public void delete(Evento entity) throws GenericException {
		dao.delete(entity);
	}

	@Override
	public List<Evento> searchByFilters(Evento entity,String field) {
		// TODO Auto-generated method stub
		return null;
	}

}