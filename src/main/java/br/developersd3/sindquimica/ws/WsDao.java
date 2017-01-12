package br.developersd3.sindquimica.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.martinlabs.commons.OpResponse;
import br.developersd3.sindquimica.models.Usuario;
import br.developersd3.sindquimica.util.DataConnect;


public class WsDao {


	 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	 

	
	public static OpResponse<Usuario> loginApp(String dir,String user, String password,String token) {
		Connection con = null;
		PreparedStatement ps = null;
		br.developersd3.sindquimica.ws.Usuario usuario = null;

		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement("Select * from usuario where login = ? and password = ?");
			ps.setString(1, user);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
								
				usuario = new br.developersd3.sindquimica.ws.Usuario();
				
				usuario.setEndereco(new Endereco());
				usuario.setEmpresa(new EmpresaAssociada());
				usuario.setPerfil(new Perfil());				
				
				usuario.setId(rs.getInt("id"));
				usuario.setNome(rs.getString("nome"));
				usuario.setDtNascimento(rs.getDate("data_nascimento"));
				usuario.setEmail(rs.getString("email"));
				usuario.setTelefones(rs.getString("telefones"));
				usuario.setSite(rs.getString("site"));
				usuario.getEndereco().setId(rs.getInt("endereco_id"));
				usuario.getEmpresa().setId(rs.getInt("empresa_associada_id"));
				usuario.setStatus(rs.getBoolean("status"));
				usuario.getPerfil().setId(rs.getInt("perfil_id"));
				usuario.setEmpresaSistema(rs.getInt("empresa_sistema_id"));
				usuario.setImagemPath(rs.getString("imagem_path"));

			}
			
			// caso haja recupera imagem
			
			if(usuario.getImagemPath() != null && !usuario.getImagemPath().isEmpty()){
				
				byte[] photo = readImage(dir, usuario);
				
				if(photo != null)
					usuario.setImagem(photo);
				
			}
			
			if(usuario.getId() != null)
				atualizaTokenDeUsuario(usuario.getId(),token);
			
			
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
			return new OpResponse(usuario);
		} finally {
			DataConnect.close(con);
		}
		return new OpResponse(usuario);
	}
	
	private static void atualizaTokenDeUsuario(Integer idUsuario,String token){
		
		 PreparedStatement ps = null;
		 Connection con = null;
		 ResultSet rs = null;
		 
		 try {
		 
         String sql = "update usuario set token_app='"+token+"' where id="+idUsuario;
		
         con = DataConnect.getConnection();

         ps = con.prepareStatement(sql);
         
         ps.executeUpdate();
         
         
		} catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null)
	                rs.close();
	            if (ps != null)
	                ps.close();
	            if (con != null)
	                con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	
	    }
		
		
	}
	
	public static OpResponse<Usuario> insertUsuario(String dir,br.developersd3.sindquimica.ws.Usuario usuario) {
		
		 Integer idUsuario = null;
		 PreparedStatement ps = null;
		 Connection con = null;
		 ResultSet rs = null;

	        try {

	            String sql = "insert into usuario(nome,data_nascimento,endereco_id,email,"
	            		+ "telefones,site,empresa_associada_id,created_at,status,login,password,perfil_id,"
	            		+ "empresa_sistema_id,imagem_path) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?) returning id;";

	            con = DataConnect.getConnection();

	            ps = con.prepareStatement(sql);

	            ps.setString(1,usuario.getNome());
	            ps.setDate(2, new java.sql.Date(usuario.getDtNascimento().getTime()));
	            ps.setInt(3, insertEndereco(usuario.getEndereco()));
	            ps.setString(4, usuario.getEmail());
	            ps.setString(5, usuario.getTelefones());
	            ps.setString(6,usuario.getSite());
	            ps.setInt(7,usuario.getEmpresa().getId());
	            ps.setDate(8, new java.sql.Date(new Date().getTime()));
	            ps.setBoolean(9, false);
	            ps.setString(10, usuario.getLogin());
	            ps.setString(11, usuario.getPassword());
	            ps.setInt(12, usuario.getPerfil().getId());
	            ps.setInt(13, usuario.getEmpresa().getEmpresaSistema());	
	            
	            String pathImagem = saveImage(dir,usuario);	   
	            
	            usuario.setImagemPath(pathImagem);
	            
	            ps.setString(14, usuario.getImagemPath());           
	            
	            rs = ps.executeQuery();

	            if (rs.next())
	            	idUsuario = rs.getInt("id");

	            if (idUsuario != null){
	               usuario.setId(idUsuario);	                
	            }


	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (rs != null)
	                    rs.close();
	                if (ps != null)
	                    ps.close();
	                if (con != null)
	                    con.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }

	        }

	        return new OpResponse(usuario);	
		
	}
	
	private static Integer insertEndereco(Endereco endereco){
		
		 Integer idEndereco = null;
		 PreparedStatement ps = null;
		 Connection con = null;
		 ResultSet rs = null;
		 
		 try {
		 
         String sql = "insert into endereco(logradouro,numero,bairro,cidade,"
         		+ "cep,complemento,created_at,empresa_sistema_id) values(?,?,?,?,?,?,?,?) returning id;";
         
         
         con = DataConnect.getConnection();

         ps = con.prepareStatement(sql);

         ps.setString(1,endereco.getLogradouro());
         ps.setString(2, endereco.getNumero());
         ps.setString(3, endereco.getBairro());
         ps.setString(4, endereco.getCidade());
         ps.setString(5, endereco.getCep());
         ps.setString(6,endereco.getComplemento());
         ps.setDate(7, new java.sql.Date(new Date().getTime()));
         ps.setInt(8, endereco.getEmpresaSistema());

         rs = ps.executeQuery();

         if (rs.next())
        	 idEndereco = rs.getInt("id");

         if (idEndereco != null){
            return    idEndereco;            
         }


     } catch (Exception e) {
         e.printStackTrace();
     } finally {
         try {
             if (rs != null)
                 rs.close();
             if (ps != null)
                 ps.close();
             if (con != null)
                 con.close();
         } catch (SQLException e) {
             e.printStackTrace();
         } catch (Exception e) {
             e.printStackTrace();
         }

     }
		
		return idEndereco;
	}
	
	public static OpResponse<List<br.developersd3.sindquimica.ws.EmpresaAssociada>> listaEmpresasAssociadas() {
		Connection con = null;
		PreparedStatement ps = null;
		List<br.developersd3.sindquimica.ws.EmpresaAssociada> lista = new ArrayList<EmpresaAssociada>();

		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement("Select * from empresa_associada where deleted_at is null");

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
								
				EmpresaAssociada emp = new br.developersd3.sindquimica.ws.EmpresaAssociada();
				
				emp.setId(rs.getInt("id"));
				emp.setNomeFantasia(rs.getString("nome_fantasia"));
				emp.setEmpresaSistema(rs.getInt("empresa_sistema_id"));
				
				lista.add(emp);

			}
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
			return new OpResponse(lista);
		} finally {
			DataConnect.close(con);
		}
		return new OpResponse(lista);
	}
	
	
	public static OpResponse<List<br.developersd3.sindquimica.ws.Mensagem>> 
	listaMensagensUsuario(String diretorio,br.developersd3.sindquimica.ws.Usuario usuario) {
		
		Connection con = null;
		PreparedStatement ps = null;
		List<br.developersd3.sindquimica.ws.Mensagem> lista = new ArrayList<Mensagem>();

		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement(" select msg.usuario_id as usuario_send_id, msg.conteudo,msg.id,msg.created_at "+
						" from mensagem msg,mensagem_usuario msg_user where msg_user.usuario_id ="+usuario.getId()+
						" and msg.id = msg_user.mensagem_id and msg.empresa_sistema_id="+usuario.getEmpresaSistema()+
						" ORDER BY created_at USING >");

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
								
				Mensagem msg = new br.developersd3.sindquimica.ws.Mensagem();
				
				msg.setId(rs.getInt("id"));
				msg.setConteudo(rs.getString("conteudo"));
				msg.setCreatedAt(rs.getDate("created_at"));
				
				br.developersd3.sindquimica.ws.Usuario user = getUsuario(diretorio,rs.getInt("usuario_send_id"));
				
				if(user != null){
					
					msg.setUsuario(user);
				}
				
				lista.add(msg);

			}
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
			return new OpResponse(lista);
		} finally {
			DataConnect.close(con);
		}
		return new OpResponse(lista);
	}
	
	public OpResponse<br.developersd3.sindquimica.ws.Mensagem> salvarMensagem(br.developersd3.sindquimica.ws.Mensagem msg){
		
		Integer idMensagem = insertMensagem(msg);
		
		msg.setId(idMensagem);
		
		// salva os usuarios da mensagem
		if(msg.getUsuarios() != null && !msg.getUsuarios().isEmpty()){
			
			for(br.developersd3.sindquimica.ws.Usuario user : msg.getUsuarios()){
				
				insertUsuariosMensagem(idMensagem,user);
			}			
		}
		
		// salva os grupos da mensagem
		if(msg.getGrupos() != null && !msg.getGrupos().isEmpty()){
			
			for(br.developersd3.sindquimica.ws.Grupo grupo : msg.getGrupos()){
				
				insertGrupoMensagem(idMensagem,grupo);
			}			
		}		
		
		return new OpResponse(msg);
	}
	
	private void insertGrupoMensagem(Integer idMensagem,br.developersd3.sindquimica.ws.Grupo grupo){
		
		 PreparedStatement ps = null;
		 Connection con = null;
		 ResultSet rs = null;
		 
	 try {
		 
      String sql = "insert into mensagem_grupo(grupo_id, mensagem_id,empresa_sistema_id) values(?,?,?) returning id;";
      
      con = DataConnect.getConnection();

      ps = con.prepareStatement(sql);

      ps.setInt(1,grupo.getId());
      ps.setInt(2, idMensagem);
      ps.setInt(3,grupo.getEmpresaSistema());

      rs = ps.executeQuery();

	   } catch (Exception e) {
	       e.printStackTrace();
	   } finally {
	       try {
	           if (rs != null)
	               rs.close();
	           if (ps != null)
	               ps.close();
	           if (con != null)
	               con.close();
	       } catch (SQLException e) {
	           e.printStackTrace();
	       } catch (Exception e) {
	           e.printStackTrace();
	       }

	   }

	}
	
	private void insertUsuariosMensagem(Integer idMensagem,br.developersd3.sindquimica.ws.Usuario usuario){
		
		 PreparedStatement ps = null;
		 Connection con = null;
		 ResultSet rs = null;
		 
	 try {
		 
       String sql = "insert into mensagem_usuario(usuario_id, mensagem_id,empresa_sistema_id) values(?,?,?) returning id;";
       
       con = DataConnect.getConnection();

       ps = con.prepareStatement(sql);

       ps.setInt(1,usuario.getId());
       ps.setInt(2, idMensagem);
       ps.setInt(3,usuario.getEmpresaSistema());

       rs = ps.executeQuery();


	   } catch (Exception e) {
	       e.printStackTrace();
	   } finally {
	       try {
	           if (rs != null)
	               rs.close();
	           if (ps != null)
	               ps.close();
	           if (con != null)
	               con.close();
	       } catch (SQLException e) {
	           e.printStackTrace();
	       } catch (Exception e) {
	           e.printStackTrace();
	       }

	   }

	}

	
	private static Integer insertMensagem(br.developersd3.sindquimica.ws.Mensagem mensagem){
		
		 Integer idEndereco = null;
		 PreparedStatement ps = null;
		 Connection con = null;
		 ResultSet rs = null;
		 
		 try {
		 
        String sql = "insert into mensagem(conteudo, created_at, deleted_at, usuario_id, file_name,"+ 
            "empresa_sistema_id) values(?,?,?,?,?,?,?,?) returning id;";
        
        
        con = DataConnect.getConnection();

        ps = con.prepareStatement(sql);

        ps.setString(1,mensagem.getConteudo());
        ps.setDate(2, new java.sql.Date(new Date().getTime()));
        ps.setString(3, null);
        ps.setInt(4, mensagem.getUsuario().getId());
        ps.setString(5, null);
        ps.setInt(6,mensagem.getUsuario().getEmpresaSistema());

        rs = ps.executeQuery();

        if (rs.next())
       	 idEndereco = rs.getInt("id");

        if (idEndereco != null){
           return    idEndereco;            
        }


    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
            if (con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
		
		return idEndereco;
	}
	
	
	
	public static OpResponse<List<br.developersd3.sindquimica.ws.Grupo>> 
	listaGruposUsuario(br.developersd3.sindquimica.ws.Usuario usuario) {
		
		Connection con = null;
		PreparedStatement ps = null;
		List<br.developersd3.sindquimica.ws.Grupo> lista = new ArrayList<Grupo>();

		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement(" select gp.id,gp.nome as nomeGrupo,gp.empresa_sistema_id as empresaSistemaId "+ 
									" from grupo gp,grupo_usuarios gu where gu.usuario_id = "+usuario.getId()+
									" and gu.grupo_id = gp.id and gp.empresa_sistema_id="+usuario.getEmpresaSistema());

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
								
				Grupo grupo = new br.developersd3.sindquimica.ws.Grupo();
				
				grupo.setId(rs.getInt("id"));
				grupo.setNome(rs.getString("nomegrupo"));
				grupo.setEmpresaSistema(rs.getInt("empresasistemaid"));
				
				lista.add(grupo);

			}
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
			return new OpResponse(lista);
		} finally {
			DataConnect.close(con);
		}
		return new OpResponse(lista);
	}
	
	private static String saveImage(String dir,br.developersd3.sindquimica.ws.Usuario usuario){
		
		if (usuario.getImagem() != null) {       	        	
		
			File file1 = new File(dir+"/images/",usuario.getNome()+"_app");
			usuario.setImagemPath(file1.getName());
			try {
				FileOutputStream fos = new FileOutputStream(file1);
				fos.write(usuario.getImagem());
				fos.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}else{
			usuario.setImagemPath("");
		}
		
		return usuario.getImagemPath();
		
	}
	
	private static byte[] readImage(String dir,br.developersd3.sindquimica.ws.Usuario usuario){
		
		byte fileContent[]=null;
		  	        	
		
		File file = new File(dir+"/images/", usuario.getImagemPath());
		
		FileInputStream fin = null;

			
		try {
			
			            // create FileInputStream object
			
			            fin = new FileInputStream(file);
			 
			
			            fileContent = new byte[(int)file.length()];
						             
			
			            // Reads up to certain bytes of data from this input stream into an array of bytes.
			
			            fin.read(fileContent);
			
			            //create string from byte array
			
			            String s = new String(fileContent);
			
			            System.out.println("File content: " + s);
						        }
			
			        catch (FileNotFoundException e) {
			
			            System.out.println("File not found" + e);
			
			        }
			
			        catch (IOException ioe) {
			
			            System.out.println("Exception while reading file " + ioe);
			
			        }
			
			        finally {
			
			            // close the streams using close method
			
			            try {
			
			                if (fin != null) {
			
			                    fin.close();
			
			                }
			
			            }
			
			            catch (IOException ioe) {
			
			                System.out.println("Error while closing stream: " + ioe);
			
			            }
			
			        }
		
		return fileContent;
	}
	
	private static br.developersd3.sindquimica.ws.Usuario getUsuario(String dir,Integer id) {
		Connection con = null;
		PreparedStatement ps = null;
		br.developersd3.sindquimica.ws.Usuario usuario = null;

		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement("Select * from usuario where id = ?");
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
								
				usuario = new br.developersd3.sindquimica.ws.Usuario();
				
				usuario.setEndereco(new Endereco());
				usuario.setEmpresa(new EmpresaAssociada());
				usuario.setPerfil(new Perfil());				
				
				usuario.setId(rs.getInt("id"));
				usuario.setNome(rs.getString("nome"));
				usuario.setDtNascimento(rs.getDate("data_nascimento"));
				usuario.setEmail(rs.getString("email"));
				usuario.setTelefones(rs.getString("telefones"));
				usuario.setSite(rs.getString("site"));
				usuario.getEndereco().setId(rs.getInt("endereco_id"));
				usuario.getEmpresa().setId(rs.getInt("empresa_associada_id"));
				usuario.setStatus(rs.getBoolean("status"));
				usuario.getPerfil().setId(rs.getInt("perfil_id"));
				usuario.setEmpresaSistema(rs.getInt("empresa_sistema_id"));
				usuario.setImagemPath(rs.getString("imagem_path"));

			}
			
			// caso haja recupera imagem
			
			if(usuario.getImagemPath() != null && !usuario.getImagemPath().isEmpty()){
				
				byte[] photo = readImage(dir, usuario);
				
				if(photo != null)
					usuario.setImagem(photo);
				
			}
			
			
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
			return usuario;
		} finally {
			DataConnect.close(con);
		}
		return usuario;
	}
	
}