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
	public List<Usuario> all() {
		return dao.findAll();
	}

	@Override
	public Usuario getById(Integer id) {
		return dao.getById(Usuario.class,id);
	}

	@Override
	public Usuario create(Usuario entity) throws GenericException {
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

}