package br.developersd3.sindquimica.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.daos.UsuarioDao;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Usuario;

@Named("usuarioService")
public class UsuarioService implements GenericService<Usuario> {

	@Inject
	@Named("usuarioDao")
	private UsuarioDao dao;

	@Override
	public List<Usuario> all(Integer empresaSistema) {
		return dao.all(empresaSistema);
	}

	@Override
	public Usuario getById(Integer id,Integer empresaSistema) {
		return dao.getById(Usuario.class,id,empresaSistema);
	}

	public Usuario getByEmail(String email) {
		return dao.getByEmail(Usuario.class,email);
	}

	@Override
	public Usuario create(Usuario entity,Integer empresaSistema) throws GenericException {
		entity.setEmpresaSistema(empresaSistema);
		return dao.save(entity);
	}

	@Override
	public Usuario update(Usuario entity) throws GenericException {
		return dao.update(entity);
	}

	@Override
	public void delete(Usuario entity) throws GenericException {
		dao.delete(entity);
	}
	
	@Override
	public List<Usuario> searchByFilters(Usuario entity,String field){

		return dao.searchByFilters(entity,field);

	}
	
	public List<Usuario> getAllByEmpresa(Integer empresa,Integer empresaSistema) {
		
		return dao.getAllByEmpresa(empresa, empresaSistema);
	}

}