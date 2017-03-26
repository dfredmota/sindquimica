package br.developersd3.sindquimica.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.daos.EmpresaDao;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Empresa;


@Named("empresaService")
public class EmpresaService implements GenericService<Empresa> {

	@Inject
	@Named("empresaDao")
	private EmpresaDao dao;

	@Override
	public List<Empresa> all(Integer empresaSistema) {
		return dao.findAllSemEmpresa();
	}

	@Override
	public Empresa getById(Integer id,Integer empresaSistema) {
		return dao.getByIdEmpresa(Empresa.class,id);
	}

	@Override
	public Empresa create(Empresa entity,Integer empresaSistema) throws GenericException {
		return dao.save(entity);
	}

	@Override
	public Empresa update(Empresa entity) throws GenericException {
		return dao.update(entity);
	}

	@Override
	public void delete(Empresa entity) throws GenericException {
		dao.delete(entity);
	}

	@Override
	public List<Empresa> searchByFilters(Empresa entity,String field) {
		return dao.searchByFilters(entity,field);
	}
	
	

}