package br.edu.ifpb.pweb1.validator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns="/imagem")
@SuppressWarnings("serial")
public class ImagemServlet extends HttpServlet {
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String pathServImagem = "";
		String pathSemImagem = "";		
		String arquivo = "";
		try{
			Properties properties = new Properties();	
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			properties.load(inputStream);
			inputStream.close();
			pathServImagem = properties.getProperty("path.serv.imagem");
			pathSemImagem =  properties.getProperty("path.name.semimagem");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(request.getParameter("img")!=null) {
			arquivo = pathServImagem + request.getParameter("img");// "/imagem?img" <== config.properties path.url.imagem
		}else {
			arquivo = pathServImagem + pathSemImagem;
		}
		
		File imagem = new File(arquivo);
		if(!imagem.exists() || imagem.isDirectory()) {			
			imagem = new File(pathServImagem + pathSemImagem);
		}
		if(imagem.exists()) {
			response.setContentType("image/jpeg");
			response.getOutputStream().write(Files.readAllBytes(imagem.toPath()));
		}
		
	}

}
