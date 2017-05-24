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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "empresa_associada")
@SQLDelete(sql = "UPDATE empresa_associada set deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
public class EmpresaAssociada implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4511301723598419914L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String cnpj;

	@Column(name = "razao_social")
	private String razaoSocial;

	@Column(name = "nome_fantasia")
	private String nomeFantasia;

	@OneToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "endereco_id")
	private Endereco endereco;

	private String telefones;

	private String responsavel;

	private String email;

	private String site;

	@OneToOne
	@JoinColumn(name = "empresa_id")
	private Empresa empresa;

	private Boolean status;

	@Column(name = "created_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@OneToMany
	@JoinTable(name="cnae_empresa_associada",
            joinColumns={@JoinColumn(name="empresa_associada_id",  
             referencedColumnName="id")},  
            inverseJoinColumns={@JoinColumn(name="cnae_id",   
             referencedColumnName="id")}) 
	private List<Cnae> cnaes;
	
	@Column(name = "empresa_sistema_id")
	private Integer empresaSistema;
	
	@OneToOne
	@JoinColumn(name = "segmento_id")
	private Segmento segmento;
	
	@Column(name="data_filiacao")
	private Date dataFiliacao;	
	
	@Column(name="pontos_fidelidade")
	private Integer pontosFidelidade;
	
	public Integer getPontosFidelidade() {
		return pontosFidelidade;
	}

	public void setPontosFidelidade(Integer pontosFidelidade) {
		this.pontosFidelidade = pontosFidelidade;
	}

	public Date getDataFiliacao() {
		return dataFiliacao;
	}

	public void setDataFiliacao(Date dataFiliacao) {
		this.dataFiliacao = dataFiliacao;
	}

	public Segmento getSegmento() {
		return segmento;
	}

	public void setSegmento(Segmento segmento) {
		this.segmento = segmento;
	}

	public Integer getEmpresaSistema() {
		return empresaSistema;
	}

	public void setEmpresaSistema(Integer empresaSistema) {
		this.empresaSistema = empresaSistema;
	}
	
	public String getAtivo(){
		if(this.status)
			return "Ativa";
		else
			return "Inativa";
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

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getTelefones() {
		return telefones;
	}

	public void setTelefones(String telefones) {
		this.telefones = telefones;
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public List<Cnae> getCnaes() {
		return cnaes;
	}

	public void setCnaes(List<Cnae> cnaes) {
		this.cnaes = cnaes;
	}
	

}
