package br.developersd3.sindquimica.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.developersd3.sindquimica.util.DataConnect;

/**
 * Servlet implementation class ImageServlet
 */
//http://localhost:8080/sindquimica/ImageServlet?id=1
@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		 /* this is pseudo code, retrieve the image and its metadata from database.
         * Maybe you do not want to use a parameter but a RESTful approach, e.g. '/images/image1234'.
         */
        String idEvento = request.getParameter("id");
        
        if(idEvento != null){
        
        byte[] imagemEvento = DataConnect.carregaImagem(Integer.parseInt(idEvento));

        /* you may want to support different encodings (e.g. JPG, PNG, TIFF) */
        response.setContentType("image/jpg");

        if(imagemEvento != null){
        
        /* obtain output stream and stream the bytes back to the client */
        OutputStream out = response.getOutputStream();

        /* stream it, here you have different options, finally close the stream */
        out.write(imagemEvento);
        
        }
        
        }
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
