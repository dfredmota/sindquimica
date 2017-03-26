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
	public List<TipoDocumento> all(Integer empresaSistema) {
		return dao.findAll(empresaSistema);
	}

	@Override
	public TipoDocumento getById(Integer id,Integer empresaSistema) {
		return dao.getById(TipoDocumento.class,id,empresaSistema);
	}

	@Override
	public TipoDocumento create(TipoDocumento entity,Integer empresaSistema) throws GenericException {
		entity.setEmpresaSistema(empresaSistema);
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

	@Override
	public List<TipoDocumento> searchByFilters(TipoDocumento entity,String field) {
		
		return dao.searchByFilters(entity);
	}
	

	

}