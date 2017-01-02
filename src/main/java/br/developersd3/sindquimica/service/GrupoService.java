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
	public List<Grupo> all(Integer empresaSistema) {
		return dao.findAll(empresaSistema);
	}

	@Override
	public Grupo getById(Integer id,Integer empresaSistema) {
		return dao.getById(Grupo.class,id,empresaSistema);
	}

	@Override
	public Grupo create(Grupo entity,Integer empresaSistema) throws GenericException {
		entity.setEmpresaSistema(empresaSistema);		
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