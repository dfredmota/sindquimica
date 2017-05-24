package br.developersd3.sindquimica.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.daos.CnaeDao;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Cnae;

@Named("cnaeService")
public class CnaeService implements GenericService<Cnae> {

	@Inject
	@Named("cnaeDao")
	private CnaeDao dao;

	@Override
	public List<Cnae> all(Integer empresaSistema) {
		return dao.findAll(empresaSistema);
	}

	@Override
	public Cnae getById(Integer id,Integer empresaSistema) {
		return dao.getById(Cnae.class,id,empresaSistema);
	}

	@Override
	public Cnae create(Cnae entity,Integer empresaSistema) throws GenericException {
		entity.setEmpresaSistema(empresaSistema);
		return dao.save(entity);
	}

	@Override
	public Cnae update(Cnae entity) throws GenericException {
		return dao.update(entity);
	}

	@Override
	public void delete(Cnae entity) throws GenericException {
		dao.delete(entity);
	}

	@Override
	public List<Cnae> searchByFilters(Cnae entity,String field) {

		return null;
	}

}