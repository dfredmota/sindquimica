package br.developersd3.sindquimica.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.daos.SindicatoDao;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Sindicato;

@Named("sindicatoService")
public class SindicatoService implements GenericService<Sindicato> {

	@Inject
	@Named("sindicatoDao")
	private SindicatoDao dao;

	@Override
	public List<Sindicato> all() {
		return dao.findAll();
	}

	@Override
	public Sindicato getById(Integer id) {
		return dao.getById(Sindicato.class,id);
	}

	@Override
	public Sindicato create(Sindicato entity) throws GenericException {
		return dao.save(entity);
	}

	@Override
	public Sindicato update(Sindicato entity) throws GenericException {
		return dao.update(entity);
	}

	@Override
	public void delete(Sindicato entity) throws GenericException {
		dao.delete(entity);
	}

}