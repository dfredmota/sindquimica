package br.developersd3.sindquimica.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.daos.DocumentoDao;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Documento;

@Named("documentoService")
public class DocumentoService implements GenericService<Documento> {

	@Inject
	@Named("documentoDao")
	private DocumentoDao dao;

	@Override
	public List<Documento> all() {
		return dao.findAll();
	}
	
	public List<Documento> findAllByUsuario(Integer usuario) {
		return dao.findAllByUsuario(usuario);
	}

	@Override
	public Documento getById(Integer id) {
		return dao.getById(Documento.class,id);
	}

	@Override
	public Documento create(Documento entity) throws GenericException {
		return dao.save(entity);
	}

	@Override
	public Documento update(Documento entity) throws GenericException {
		return dao.update(entity);
	}

	@Override
	public void delete(Documento entity) throws GenericException {
		dao.delete(entity);
	}

}