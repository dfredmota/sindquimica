package br.developersd3.sindquimica.models;

import java.io.Serializable;

import javax.persistence.Column;

public class Telefone implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1826242546085256943L;

	private Integer id;
	
	private String  ddd;
	
	private String  numero;
	
	@Column(name = "empresa_sistema_id")
	private Integer empresaSistema;

	public Integer getEmpresaSistema() {
		return empresaSistema;
	}

	public void setEmpresaSistema(Integer empresaSistema) {
		this.empresaSistema = empresaSistema;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	

}
