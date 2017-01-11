/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.developersd3.sindquimica.ws;

import javax.servlet.annotation.WebServlet;

import br.com.martinlabs.commons.ServletWrapper;
import br.com.martinlabs.commons.exceptions.RespException;

/**
 *
 * @author developer
 */
@WebServlet("/RegistrarUsuario")
public class RegistrarUsuarioServlet extends ServletWrapper {

	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1816982573545190821L;

	@Override
    protected Object process(ServletContent content) throws RespException {
		RequestBody reqBody = content.getBody(RequestBody.class);
		String dir = content.request.getSession().getServletContext().getRealPath("/");
    	return new WsDao().insertUsuario(dir,reqBody.usuario);
    }

	private class RequestBody {
        public Usuario usuario;  
    }
    
}
