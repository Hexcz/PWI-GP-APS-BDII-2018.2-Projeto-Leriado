package com.ifpb.edu.leriadoApp;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ifpb.edu.model.dao.publicacao.impdb.TextoDAOImpDB;
import com.ifpb.edu.model.domain.Usuario;
import com.ifpb.edu.model.domain.publicacao.Arquivo;
import com.ifpb.edu.model.domain.publicacao.Publicacao;
import com.ifpb.edu.model.domain.publicacao.Texto;
import com.ifpb.edu.model.jdbc.DataAccessException;

public class TextoTeste {
	
private Texto texto;
private Publicacao publicacao;
private Usuario usuario;
private TextoDAOImpDB textoDao;
private List<Arquivo> arquivos;
	
@Before
public void inicio() {
	arquivos = new ArrayList<Arquivo>();
	arquivos.add(new Arquivo("Nome do arquivo", "nomeOriginal", 1000, "descricao", true));
	texto = new Texto();
	publicacao = new Publicacao();
	publicacao.setArquivos(arquivos);
	publicacao.setConteudo("Conteúdo de teste de publicação");
	publicacao.setTitulo("Título de publicação");	
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
