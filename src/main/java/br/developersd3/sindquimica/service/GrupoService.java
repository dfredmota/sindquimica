package br.developersd3.sindquimica.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.daos.GrupoDao;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Grupo;

@Named("grupoService")
public class GrupoService implements GenericService<Grupo> {

	@Inject
	@Named("grupoDao")
	private GrupoDao dao;

	@Override
	public List<Grupo> all() {
		return dao.findAll();
	}

	@Override
	public Grupo getById(Integer id) {
		return dao.getById(Grupo.class,id);
	}

	@Override
	public Grupo create(Grupo entity) throws GenericException {
		return dao.save(entity);
	}

	@Override
	public Grupo update(Grupo entity) throws GenericException {
		return dao.update(entity);
	}

	@Override
	public void delete(Grupo entity) throws GenericException {
		dao.delete(entity);
	}

}