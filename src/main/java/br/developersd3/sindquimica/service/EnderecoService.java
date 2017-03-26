package br.developersd3.sindquimica.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.developersd3.sindquimica.daos.EnderecoDao;
import br.developersd3.sindquimica.exception.GenericException;
import br.developersd3.sindquimica.models.Endereco;

@Named("enderecoService")
public class EnderecoService implements GenericService<Endereco> {

	@Inject
	@Named("enderecoDao")
	private EnderecoDao dao;

	@Override
	public List<Endereco> all(Integer empresaSistema) {
		return dao.findAll(empresaSistema);
	}

	@Override
	public Endereco getById(Integer id,Integer empresaSistema) {
		return dao.getById(Endereco.class,id,empresaSistema);
	}

	@Override
	public Endereco create(Endereco entity,Integer empresaSistema) throws GenericException {
		entity.setEmpresaSistema(empresaSistema);
		return dao.save(entity);
	}

	@Override
	public Endereco update(Endereco entity) throws GenericException {
		return dao.update(entity);
	}

	@Override
	public void delete(Endereco entity) throws GenericException {
		dao.delete(entity);
	}

	@Override
	public List<Endereco> searchByFilters(Endereco entity,String field) {
		// TODO Auto-generated method stub
		return null;
	}

}