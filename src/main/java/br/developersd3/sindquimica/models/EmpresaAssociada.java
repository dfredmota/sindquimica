package br.developersd3.sindquimica.models;

import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

public class EmpresaAssociada {
	
	private Integer        id;
	
	private String         cnpj;
	
	private String         razaoSocial;
	
	private String         nomeFantasia;
	
	@OneToOne
    @JoinColumn(name="endereco_id")
	private Endereco       endereco;
	
	private List<Telefone> listaDeTelefones;
	
	private String         responsavel;
	
	private String         email;
	
	private String         site;
	
	@OneToOne
    @JoinColumn(name="sindicato_id")
	private Sindicato       sindicato;
	
	private Boolean         status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<Telefone> getListaDeTelefones() {
		return listaDeTelefones;
	}

	public void setListaDeTelefones(List<Telefone> listaDeTelefones) {
		this.listaDeTelefones = listaDeTelefones;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

}
