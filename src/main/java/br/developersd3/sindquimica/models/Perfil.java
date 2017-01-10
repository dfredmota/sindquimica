package br.developersd3.sindquimica.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name="perfil")
@SQLDelete(sql = "UPDATE perfil set deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
public class Perfil implements Serializable{
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -2018604330153180240L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String  descricao;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
