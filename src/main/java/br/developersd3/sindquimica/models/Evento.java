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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name="evento")
@SQLDelete(sql = "UPDATE evento set deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
public class Evento implements Serializable{
	
		
    /**
	 * 
	 */
	private static final long serialVersionUID = 257290762168774044L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String  descricao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date    inicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date    fim;
	
	@Column(name = "created_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name = "empresa_sistema_id")
	private Integer empresaSistema;
	
	private String local;
	
	@OneToMany
	@JoinTable(name="evento_usuario",
            joinColumns={@JoinColumn(name="evento_id",  
             referencedColumnName="id")},  
            inverseJoinColumns={@JoinColumn(name="usuario_id",   
             referencedColumnName="id")}) 
	private List<Usuario> usuarios;
	
	@OneToMany
	@JoinTable(name="evento_grupo",
            joinColumns={@JoinColumn(name="evento_id",  
             referencedColumnName="id")},  
            inverseJoinColumns={@JoinColumn(name="grupo_id",   
             referencedColumnName="id")}) 
	private List<Grupo> grupo;
	
	@OneToMany
	@JoinTable(name="evento_participantes",
            joinColumns={@JoinColumn(name="evento_id",  
             referencedColumnName="id")},  
            inverseJoinColumns={@JoinColumn(name="participante_id",   
             referencedColumnName="id")}) 
	private List<ParticipanteEvento> participantes;
	
	private Boolean status;
	
	@Transient
	private byte[] imagem;
	
	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public List<ParticipanteEvento> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(List<ParticipanteEvento> participantes) {
		this.participantes = participantes;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Grupo> getGrupo() {
		return grupo;
	}

	public void setGrupo(List<Grupo> grupo) {
		this.grupo = grupo;
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

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

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}
	
	

}
