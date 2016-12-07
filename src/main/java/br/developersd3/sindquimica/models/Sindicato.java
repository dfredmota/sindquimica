package br.developersd3.sindquimica.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class Sindicato {
	
	@Id
	@GeneratedValue(generator = "sindicato_id_seq")
	@SequenceGenerator(name = "sindicato_id_seq", sequenceName = "sindicato_id_seq", allocationSize = 1)
	private Integer id;
	
	private String descricao;
	
	private String ramo;
		
	private String presidente;
	
	@Column(name="dados_institucionais")
	private String dadosInstitucional;
	
	private String telefone;
	
    @OneToOne
    @JoinColumn(name="endereco_id")
	private Endereco endereco;	
		
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRamo() {
		return ramo;
	}
	public void setRamo(String ramo) {
		this.ramo = ramo;
	}
	public String getPresidente() {
		return presidente;
	}
	public void setPresidente(String presidente) {
		this.presidente = presidente;
	}
	public String getDadosInstitucional() {
		return dadosInstitucional;
	}
	public void setDadosInstitucional(String dadosInstitucional) {
		this.dadosInstitucional = dadosInstitucional;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	
}
