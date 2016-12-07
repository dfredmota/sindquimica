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
	public List<Endereco> all() {
		return dao.findAll();
	}

	@Override
	public Endereco getById(Integer id) {
		return dao.getById(Endereco.class,id);
	}

	@Override
	public Endereco create(Endereco entity) throws GenericException {
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

}