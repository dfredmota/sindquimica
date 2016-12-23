package br.developersd3.sindquimica.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql="UPDATE grupo set deleted_at = now() WHERE id = ?")
@Where(clause="deleted_at is null")
public class Grupo implements Serializable{
	
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 7074627393779427391L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String  nome;
	
	@Column(name="created_at", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name="deleted_at", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedAt;
	
	@OneToMany
	@JoinTable(name="grupo_usuarios",
            joinColumns={@JoinColumn(name="grupo_id",  
             referencedColumnName="id")},  
            inverseJoinColumns={@JoinColumn(name="usuario_id",   
             referencedColumnName="id")}) 
	private List<Usuario> usuarios;
	
	@Transient
	private Integer participantes;

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

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	public Integer getParticipantes(){
		if(this.usuarios != null)
			return this.usuarios.size();
		else
			return new Integer(0);
	}

	public void setParticipantes(Integer participantes) {
		this.participantes = participantes;
	}
		
	
}
