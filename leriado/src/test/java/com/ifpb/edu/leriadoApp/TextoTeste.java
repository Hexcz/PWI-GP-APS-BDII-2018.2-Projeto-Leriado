package com.ifpb.edu.leriadoApp;

import org.junit.Before;
import org.junit.Test;

import com.ifpb.edu.model.dao.publicacao.impdb.TextoDAOImpDB;
import com.ifpb.edu.model.domain.Usuario;
import com.ifpb.edu.model.domain.publicacao.Publicacao;
import com.ifpb.edu.model.domain.publicacao.Texto;
import com.ifpb.edu.model.jdbc.DataAccessException;

public class TextoTeste {
	
private Texto texto;
private Publicacao publicacao;
private Usuario usuario;
private TextoDAOImpDB textoDao;
	
@Before
public void inicio() {
	texto = new Texto();
	publicacao = new Publicacao();
	publicacao.setConteudo("Conteúdo de teste de publicação");
	texto.setPublicacao(publicacao);	
	usuario = new Usuario();
	usuario.setId(1);
	texto.setUsuario(usuario);
	textoDao = new TextoDAOImpDB();
}

@Test
public void criaTexto() {
	try {		
		textoDao.cria(texto);
	} catch (DataAccessException e) {		
		e.printStackTrace();
	}
}
}
