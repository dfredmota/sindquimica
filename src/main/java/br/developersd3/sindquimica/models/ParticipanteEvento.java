package br.developersd3.sindquimica.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name="participantes")
@SQLDelete(sql="UPDATE participantes set deleted_at = now() WHERE id = ?")
@Where(clause="deleted_at is null")
public class ParticipanteEvento  implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8439628861178687181L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String  nome;
	
	private String  email;
	
	@Column(name="created_at", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name="deleted_at", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedAt;
	
	@Column(name = "empresa_sistema_id")
	private Integer empresaSistema;

	public Integer getEmpresaSistema() {
		return empresaSistema;
	}

	public void setEmpresaSistema(Integer empresaSistema) {
		this.empresaSistema = empresaSistema;
	}
	
	@PrePersist
	protected void onCreate() {
	    createdAt = new Date();
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

}
