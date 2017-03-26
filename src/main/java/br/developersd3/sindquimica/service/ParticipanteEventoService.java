package br.developersd3.sindquimica.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.daos.ParticipanteEventoDao;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.ParticipanteEvento;

@Named("participanteEventoService")
public class ParticipanteEventoService implements GenericService<ParticipanteEvento> {

	@Inject
	@Named("participanteEventoDao")
	private ParticipanteEventoDao dao;

	@Override
	public List<ParticipanteEvento> all(Integer empresaSistema) {
		return dao.findAll(empresaSistema);
	}

	@Override
	public ParticipanteEvento getById(Integer id,Integer empresaSistema) {
		return dao.getById(ParticipanteEvento.class,id,empresaSistema);
	}

	@Override
	public ParticipanteEvento create(ParticipanteEvento entity,Integer empresaSistema) throws GenericException {
		entity.setEmpresaSistema(empresaSistema);
		return dao.save(entity);
	}

	@Override
	public ParticipanteEvento update(ParticipanteEvento entity) throws GenericException {
		return dao.update(entity);
	}

	@Override
	public void delete(ParticipanteEvento entity) throws GenericException {
		dao.delete(entity);
	}

	@Override
	public List<ParticipanteEvento> searchByFilters(ParticipanteEvento entity,String field) {
		// TODO Auto-generated method stub
		return null;
	}

}