package br.developersd3.sindquimica.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.daos.TipoDocumentoDao;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.TipoDocumento;

@Named("tipoDocumentoService")
public class TipoDocumentoService implements GenericService<TipoDocumento> {

	@Inject
	@Named("tipoDocumentoDao")
	private TipoDocumentoDao dao;

	@Override
	public List<TipoDocumento> all() {
		return dao.findAll();
	}

	@Override
	public TipoDocumento getById(Integer id) {
		return dao.getById(TipoDocumento.class,id);
	}

	@Override
	public TipoDocumento create(TipoDocumento entity) throws GenericException {
		return dao.save(entity);
	}

	@Override
	public TipoDocumento update(TipoDocumento entity) throws GenericException {
		return dao.update(entity);
	}

	@Override
	public void delete(TipoDocumento entity) throws GenericException {
		dao.delete(entity);
	}

}