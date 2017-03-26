package br.developersd3.sindquimica.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.daos.PerfilDao;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Perfil;

@Named("perfilService")
public class PerfilService implements GenericService<Perfil> {

	@Inject
	@Named("perfilDao")
	private PerfilDao dao;

	@Override
	public List<Perfil> all(Integer empresaSistema) {
		return dao.findAllSemEmpresa();
	}

	@Override
	public Perfil getById(Integer id,Integer empresaSistema) {
		return dao.getById(Perfil.class,id,empresaSistema);
	}

	public Perfil create(Perfil entity) throws GenericException {
		return dao.save(entity);
	}

	@Override
	public Perfil update(Perfil entity) throws GenericException {
		return dao.update(entity);
	}

	@Override
	public void delete(Perfil entity) throws GenericException {
		dao.delete(entity);
	}

	@Override
	public Perfil create(Perfil entity, Integer empresaSistema) throws GenericException {
		return null;
	}

	@Override
	public List<Perfil> searchByFilters(Perfil entity,String field) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}