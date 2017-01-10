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
@WebServlet("/SalvaMensagens")
public class SalvaMensagensServlet extends ServletWrapper {

	
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8887832520699913768L;

	@Override
    protected Object process(ServletContent content) throws RespException {
		RequestBody reqBody = content.getBody(RequestBody.class);
    	return new WsDao().salvarMensagem(reqBody.msg);
    }

	private class RequestBody {
        public Mensagem msg;  
    }
    
}
