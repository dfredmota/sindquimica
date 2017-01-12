/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.developersd3.sindquimica.ws;

import javax.servlet.annotation.WebServlet;

import br.com.martinlabs.commons.ServletWrapper;
import br.com.martinlabs.commons.exceptions.RespException;
import br.developersd3.sindquimica.daos.LoginDAO;

/**
 *
 * @author developer
 */
@WebServlet("/Login")
public class LoginServlet extends ServletWrapper {

	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1816982573545190821L;

	@Override
    protected Object process(ServletContent content) throws RespException {
    	LoginRequest reqBody = content.getBody(LoginRequest.class);
		String dir = content.request.getSession().getServletContext().getRealPath("/");
    	return new WsDao().loginApp(dir,reqBody.account, reqBody.password,reqBody.token);
    }

    private class LoginRequest {
        String account;
        String password;
        String token;
    }
    
}
