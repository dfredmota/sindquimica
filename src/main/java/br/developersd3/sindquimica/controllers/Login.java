package br.developersd3.sindquimica.controllers;

import java.io.Serializable;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import br.developersd3.sindquimica.daos.LoginDAO;
import br.developersd3.sindquimica.models.Perfil;
import br.developersd3.sindquimica.models.Usuario;
import br.developersd3.sindquimica.service.UsuarioService;
import br.developersd3.sindquimica.util.SessionUtils;
import br.developersd3.sindquimica.util.Validations;

//          <!--  <c:if test = "${login.perfilUsuario == 'ADM'}"/>-->
@ManagedBean(name = "login")
@SessionScoped
public class Login implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	private String pwd;
	private String msg;
	private String user;

	private String email;
	private String usuario;

	@ManagedProperty(name = "usuarioService", value = "#{usuarioService}")
	private UsuarioService usuarioService;

	public String sendPasswordToEmail() {

		boolean isValidEmail = Validations.isValidEmailAddress(email.trim());
		
		if(!isValidEmail){
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Email Inválido!","");
			FacesContext.getCurrentInstance().addMessage(null, msg);			
			
			return null;
			
		}
		
		
		final String username = "2biportal@gmail.com";
		final String password = "eaccsl2017";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Usuario usuario = usuarioService.getByEmail(email);

		if (usuario != null) {

			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			Message message = new MimeMessage(session);
			try {
				message.setFrom(new InternetAddress("2biportal@gmail.com"));
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				message.setSubject("Reenvio de Senha - Sindquimica");
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				message.setText("Olá ," + usuario.getNome() + "\n\n Segue sua senha para acesso ao sistema:"
						+ "\n\n Senha:" + usuario.getPassword() + " \n\n\n Atenciosamente, Equipe Sindiquimica.");
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				Transport.send(message);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Sua senha foi enviada pro seu email!"));
		}

		return "/login.xhtml?redirect=true";
	}

	// validate login
	public String validateUsernamePassword() {

		Integer[] valid = LoginDAO.validate(user, pwd);

		if (valid != null && valid.length > 0) {
			HttpSession session = SessionUtils.getSession();
			session.setAttribute("userId", valid[0]);
			session.setAttribute("empresaSistemaId", valid[1]);

			// seta o perfil do mesmo na aplicação
			Usuario usuario = usuarioService.getById(valid[0], valid[1]);

			if (usuario != null) {

				this.usuario = usuario.getNome();

				Perfil perfil = usuario.getPerfil();

				if (perfil.getDescricao().equalsIgnoreCase("ADM")) {
					session.setAttribute("perfil", "ADM");
				} else {
					session.setAttribute("perfil", "ADMSINDICATO");
				}
			}

			return "admin";
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Login e/ou Senha Inválidos!"));

			return "login";
		}
	}

	// logout event, invalidate session
	public String logout() {
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "login";
	}

	public String getPerfilUsuario() {
		HttpSession session = SessionUtils.getSession();
		String perfil = (String) session.getAttribute("perfil");

		return perfil;
	}

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

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}
