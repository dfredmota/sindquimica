package br.developersd3.sindquimica.models;

import java.util.Date;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

public class Usuario {

	private Integer id;

	private String nome;

	private Date dtNascimento;

	@OneToOne
	@JoinColumn(name = "endereco_id")
	private Endereco endereco;

	private String email;

	private List<Telefone> listaDeTelefones;

	private String site;

	@OneToOne
	@JoinColumn(name = "sindicato_id")
	private Sindicato sindicato;

	private Boolean status;

	@OneToOne
	@JoinColumn(name = "empresa_id")
	private EmpresaAssociada empresa;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(Date dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Telefone> getListaDeTelefones() {
		return listaDeTelefones;
	}

	public void setListaDeTelefones(List<Telefone> listaDeTelefones) {
		this.listaDeTelefones = listaDeTelefones;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public EmpresaAssociada getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaAssociada empresa) {
		this.empresa = empresa;
	}

}
