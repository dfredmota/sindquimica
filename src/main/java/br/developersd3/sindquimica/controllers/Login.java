package br.developersd3.sindquimica.controllers;

import java.io.Serializable;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import br.developersd3.sindquimica.daos.LoginDAO;
import br.developersd3.sindquimica.service.EmpresaService;
import br.developersd3.sindquimica.util.SessionUtils;


@Model
public class Login implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;
	
	private String pwd;
	private String msg;
	private String user;

    @Inject
    @Named("empresaService")
	private EmpresaService empresaService;
	
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	//validate login
	public String validateUsernamePassword() {
		
		Integer valid = LoginDAO.validate(user, pwd);
		
		if (valid != 0) {
			HttpSession session = SessionUtils.getSession();
			session.setAttribute("userId", valid);
			return "admin";
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,new FacesMessage(FacesMessage.SEVERITY_WARN,"Login e/ou Senha Inválidos","Tente Novamente!"));
					
			return "login";
		}
	}

	//logout event, invalidate session
	public String logout() {
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "login";
	}
}
