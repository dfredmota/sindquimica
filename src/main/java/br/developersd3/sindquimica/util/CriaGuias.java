package br.developersd3.sindquimica.util;



public class CriaGuias {
	
	public static Guia getGuia(int id){
		switch (id) {
			case 0: return guiaConsultarSecretaria();
			case 1: return guiaConsultarUnidade();
			default:return guiaConsultarSecretaria();
		}
	}
	
	private static Guia guiaConsultarSecretaria(){
		return new Guia(0,"Consultar Secretarias/�rg�os","glyphicon-search","esqueciSenha.xhtml");
	}
	private static Guia guiaConsultarUnidade(){
		return new Guia(1,"Consultar Unidades","glyphicon-search","login.xhtml");
	}
}
