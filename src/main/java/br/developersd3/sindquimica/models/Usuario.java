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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name="usuario")
@SQLDelete(sql = "UPDATE usuario set deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
public class Usuario implements Serializable{

	
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 3990221567540470465L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    
	private String nome;

	@Column(name="data_nascimento")
	private Date dtNascimento;

	@OneToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "endereco_id")
	private Endereco endereco;

	private String email;

	private String telefones;

	private String site;

	private Boolean status;

	@OneToOne
	@JoinColumn(name = "empresa_associada_id")
	private EmpresaAssociada empresa;
	
	@OneToMany(mappedBy="usuario")
    private List<Documento> listaDocumentos;
	
	@Column(name = "created_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	private String login;
	
	private String password;
	
	@Column(name="imagem_path")
	private String imagemPath;
	
	@Column(name="cadastro_app")
	private Boolean cadastroApp;
	
	@Column(name = "empresa_sistema_id")
	private Integer empresaSistema;
	
	@OneToOne
	@JoinColumn(name = "perfil_id")
	private Perfil perfil;
	
	@Column(name = "token_app")
	private String token;
	
	@Transient
	private Boolean confirmou;
	
	@Transient
	private Boolean visualizou;
	
	public Boolean getConfirmou() {
		return confirmou;
	}

	public void setConfirmou(Boolean confirmou) {
		this.confirmou = confirmou;
	}

	public Boolean getVisualizou() {
		return visualizou;
	}

	public void setVisualizou(Boolean visualizou) {
		this.visualizou = visualizou;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Integer getEmpresaSistema() {
		return empresaSistema;
	}

	public void setEmpresaSistema(Integer empresaSistema) {
		this.empresaSistema = empresaSistema;
	}

	public Boolean getCadastroApp() {
		return cadastroApp;
	}

	public void setCadastroApp(Boolean cadastroApp) {
		this.cadastroApp = cadastroApp;
	}

	public String getImagemPath() {
		return imagemPath;
	}

	public void setImagemPath(String imagemPath) {
		this.imagemPath = imagemPath;
	}
	
	public String getConfirmouPresenca(){
		
		if(this.confirmou != null){
			
			if(confirmou)
				return "SIM";
			else
				return "NÃO";
					
		}else{
			return "";
		}
		
	}
	
	public String getVisualizouEvento(){
		
		if(this.visualizou != null){
			
			if(visualizou)
				return "SIM";
			else
				return "NÃO";
					
		}else{
			return "";
		}
		
	}
	
	public String getAtivo(){
		if(this.status)
			return "Liberado";
		else
			return "Bloqueado";
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(Date dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getTelefones() {
		return telefones;
	}

	public void setTelefones(String telefones) {
		this.telefones = telefones;
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

	public EmpresaAssociada getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaAssociada empresa) {
		this.empresa = empresa;
	}

	public List<Documento> getListaDocumentos() {
		return listaDocumentos;
	}

	public void setListaDocumentos(List<Documento> listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
