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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql="UPDATE mensagem set deleted_at = now() WHERE id = ?")
@Where(clause="deleted_at is null")
//ORDER BY created_at USING >
public class Mensagem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8959809172509370857L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String  conteudo;
	
	@Column(name="created_at", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name="deleted_at", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedAt;
	
	@OneToMany
	@JoinTable(name="mensagem_usuario",
            joinColumns={@JoinColumn(name="mensagem_id",  
             referencedColumnName="id")},  
            inverseJoinColumns={@JoinColumn(name="usuario_id",   
             referencedColumnName="id")}) 
	private List<Usuario> usuarios;
	
	@OneToMany
	@JoinTable(name="mensagem_grupo",
            joinColumns={@JoinColumn(name="mensagem_id",  
             referencedColumnName="id")},  
            inverseJoinColumns={@JoinColumn(name="grupo_id",   
             referencedColumnName="id")}) 
	private List<Grupo> grupos;
	
	@OneToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	@Column(name="file_name")
	private String  fileName;
	
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getConteudo() {
		return conteudo;
	}


	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
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


	public List<Grupo> getGrupos() {
		return grupos;
	}


	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}


	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}	

}
