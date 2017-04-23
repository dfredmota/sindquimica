package br.developersd3.sindquimica.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.daos.EmpresaAssociadaDao;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.EmpresaAssociada;


@Named("empresaAssociadaService")
public class EmpresaAssociadaService implements GenericService<EmpresaAssociada> {

	@Inject
	@Named("empresaAssociadaDao")
	private EmpresaAssociadaDao dao;

	@Override
	public List<EmpresaAssociada> all(Integer empresaSistema) {
		return dao.findAll(empresaSistema);
	}

	@Override
	public EmpresaAssociada getById(Integer id,Integer empresaSistema) {
		return dao.getById(EmpresaAssociada.class,id,empresaSistema);
	}

	@Override
	public EmpresaAssociada create(EmpresaAssociada entity,Integer empresaSistema) throws GenericException {
		entity.setEmpresaSistema(empresaSistema);
		return dao.save(entity);
	}

	@Override
	public EmpresaAssociada update(EmpresaAssociada entity) throws GenericException {
		return dao.update(entity);
	}

	@Override
	public void delete(EmpresaAssociada entity) throws GenericException {
		dao.delete(entity);
	}
	
	public List<EmpresaAssociada> findAllByCnae(Integer idCnae) {
		return dao.findAllByCnae(idCnae);		
	}
	
	public List<EmpresaAssociada> findAllBySegmento(Integer segmento) {
		return dao.findAllBySegmento(segmento);		
	}
	
	@Override
	public List<EmpresaAssociada> searchByFilters(EmpresaAssociada entity,String field) {
		return dao.searchByFilters(entity,field);
	}

}