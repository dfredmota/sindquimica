package br.developersd3.sindquimica.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.daos.EmpresaAssociadaDao;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.EmpresaAssociada;


@Named("empresaAssociadaService")
public class EmpresaAssociadaService implements GenericService<EmpresaAssociada> {

	@Inject
	@Named("empresaAssociadaDao")
	private EmpresaAssociadaDao dao;

	@Override
	public List<EmpresaAssociada> all() {
		return dao.findAll();
	}

	@Override
	public EmpresaAssociada getById(Integer id) {
		return dao.getById(EmpresaAssociada.class,id);
	}

	@Override
	public EmpresaAssociada create(EmpresaAssociada entity) throws GenericException {
		return dao.save(entity);
	}

	@Override
	public EmpresaAssociada update(EmpresaAssociada entity) throws GenericException {
		return dao.update(entity);
	}

	@Override
	public void delete(EmpresaAssociada entity) throws GenericException {
		dao.delete(entity);
	}

}