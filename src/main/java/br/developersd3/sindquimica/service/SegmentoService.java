package br.developersd3.sindquimica.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.daos.SegmentoDao;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Segmento;

@Named("segmentoService")
public class SegmentoService implements GenericService<Segmento> {

	@Inject
	@Named("segmentoDao")
	private SegmentoDao dao;

	@Override
	public List<Segmento> all(Integer empresaSistema) {
		return dao.findAllSemEmpresa();
	}

	@Override
	public Segmento getById(Integer id,Integer empresaSistema) {
		return dao.getByIdEmpresa(Segmento.class, id);
	}

	@Override
	public Segmento create(Segmento entity,Integer empresaSistema) throws GenericException {
		return dao.save(entity);
	}

	@Override
	public Segmento update(Segmento entity) throws GenericException {
		return dao.update(entity);
	}

	@Override
	public void delete(Segmento entity) throws GenericException {
		dao.delete(entity);
	}

	@Override
	public List<Segmento> searchByFilters(Segmento entity,String field){

		return dao.searchByFilters(entity);

	}

	
	
	

}