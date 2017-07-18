package br.developersd3.sindquimica.util;

public class Guia {
	private int id;
	private String titulo;
	private String nomeIcone;
	private String pagina;
	
	public Guia(int id, String titulo, String nomeIcone, String pagina) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.nomeIcone = nomeIcone;
		this.pagina = pagina;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getNomeIcone() {
		return nomeIcone;
	}
	public void setNomeIcone(String nomeIcone) {
		this.nomeIcone = nomeIcone;
	}
	public String getPagina() {
		return pagina;
	}
	public void setPagina(String pagina) {
		this.pagina = pagina;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Guia other = (Guia) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
