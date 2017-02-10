package br.developersd3.sindquimica.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cnaes")
public class Cnae implements Serializable{
	
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 7619930467191699624L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String codigo;
	
	private String descricao;
	
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return this.codigo + " - "+descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	

}
