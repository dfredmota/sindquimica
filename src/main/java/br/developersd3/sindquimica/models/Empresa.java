package br.developersd3.sindquimica.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@SQLDelete(sql="UPDATE empresa set deleted_at = now() WHERE id = ?")
@Where(clause="deleted_at is null")
public class Empresa  implements Serializable{

	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 6369752214780023704L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String  cnpj;
	
	@Column(name="razao_social")
	private String  razaoSocial;
	
	@Column(name="nome_fantasia")
	private String  nomeFantasia;
	
	private String  responsavel;
	
	private String  email;
	
	private String  site;
	
	private Boolean status;
	
	private String  telefones;

	@OneToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "endereco_id")
	private Endereco endereco;
	
	@Column(name="created_at", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@OneToMany
	@JoinTable(name="cnae_empresa",
            joinColumns={@JoinColumn(name="empresa_id",  
             referencedColumnName="id")},  
            inverseJoinColumns={@JoinColumn(name="cnae_id",   
             referencedColumnName="id")}) 
	private List<Cnae> cnaes;
	
	@PrePersist
	protected void onCreate() {
	    createdAt = new Date();
	}
	
	@Column(name="deleted_at", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedAt;

	public List<Cnae> getCnaes() {
		return cnaes;
	}

	public void setCnaes(List<Cnae> cnaes) {
		this.cnaes = cnaes;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getTelefones() {
		return telefones;
	}

	public void setTelefones(String telefones) {
		this.telefones = telefones;
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
	
	public String getAtivo(){
		if(this.status)
			return "Ativa";
		else
			return "Inativa";
	}
	
}
