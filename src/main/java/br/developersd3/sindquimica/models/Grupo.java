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
	
	@OneToMany
	@JoinTable(name="grupo_empresa_associadas",
            joinColumns={@JoinColumn(name="grupo_id",  
             referencedColumnName="id")},  
            inverseJoinColumns={@JoinColumn(name="empresa_associada_id",   
             referencedColumnName="id")}) 
	private List<EmpresaAssociada> empresaAssociada;
	
	@OneToMany
	@JoinTable(name="grupo_segmento",
            joinColumns={@JoinColumn(name="grupo_id",  
             referencedColumnName="id")},  
            inverseJoinColumns={@JoinColumn(name="segmento_id",   
             referencedColumnName="id")}) 
	private List<Segmento> segmentos;
	
	@Transient
	private Integer participantes;
	
	@Column(name = "empresa_sistema_id")
	private Integer empresaSistema;
	
	public List<Segmento> getSegmentos() {
		return segmentos;
	}

	public void setSegmentos(List<Segmento> segmentos) {
		this.segmentos = segmentos;
	}

	public List<EmpresaAssociada> getEmpresaAssociada() {
		return empresaAssociada;
	}

	public void setEmpresaAssociada(List<EmpresaAssociada> empresaAssociada) {
		this.empresaAssociada = empresaAssociada;
	}

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
		
		Integer numParticipantes = 0;
		
		if(this.usuarios != null)
			numParticipantes = numParticipantes + this.usuarios.size();
		
		if(this.empresaAssociada != null)
			numParticipantes = numParticipantes + this.empresaAssociada.size();
		
		return numParticipantes;
		
	}

	public void setParticipantes(Integer participantes) {
		this.participantes = participantes;
	}
		
	
}
