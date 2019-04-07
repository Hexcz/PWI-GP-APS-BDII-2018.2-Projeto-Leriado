package br.edu.ifpb.pweb1.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.Part;

import br.edu.ifpb.pweb1.model.dao.impdb.CompartilhaDAOImpDB;
import br.edu.ifpb.pweb1.model.dao.impdb.GrupoDaoImpl;
import br.edu.ifpb.pweb1.model.dao.impdb.TextoDAOImpDB;
import br.edu.ifpb.pweb1.model.domain.Arquivo;
import br.edu.ifpb.pweb1.model.domain.Compartilha;
import br.edu.ifpb.pweb1.model.domain.Grupo;
import br.edu.ifpb.pweb1.model.domain.Publicacao;
import br.edu.ifpb.pweb1.model.domain.Texto;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;
import br.edu.ifpb.pweb1.util.JsfUtil;

@ManagedBean(name = "publicacaoBean")
@RequestScoped
public class PublicacaoMB {
	
	private String pathServImagem;
	private String titulo;
	private String conteudo;
	private boolean localizacao;
	private String latitude;
	private String longitude;
	private String endereco;
	private String grupoCompartilhado;
	private int textoId;
	private Part arquivo;
	
	@ManagedProperty("#{loginBean}")
	private LoginMB loginMb;
	
	@PostConstruct
	private void iniciar() {
		localizacao = true;
		try{
			Properties properties = new Properties();	
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			properties.load(inputStream);
			inputStream.close();
			pathServImagem = properties.getProperty("path.serv.imagem");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void limpar() {
		titulo="";
		conteudo="";
		localizacao =true;
		latitude="";
		longitude="";
		grupoCompartilhado="";		
	}
	
	public String publicar() {
		try {
			TextoDAOImpDB textoDao = new TextoDAOImpDB();
			Publicacao publicacao = new Publicacao();		
			Texto texto = new Texto();		
			List<Arquivo> arquivos = new ArrayList<>();
			GrupoDaoImpl grupoDao = new GrupoDaoImpl(); 
			Grupo grupo = grupoDao.busca(grupoDao.buscaIdPorNome(grupoCompartilhado));
			
			if (arquivo != null) {
				/*Salvando o arquivo na pasta do servidor*/
				String nomeArquivo = GerirArquivos.salvarArquivoPasta(arquivo, pathServImagem);			
				arquivos.add(new Arquivo(nomeArquivo, arquivo.getSubmittedFileName(), arquivo.getSize(), "Nenhuma", true));
			}
					
			publicacao.setTitulo(titulo);
			publicacao.setConteudo(conteudo);
			if(localizacao) {
				publicacao.setLatitude(latitude);
				publicacao.setLongitude(longitude);
				publicacao.setEndereco(endereco);
			} else {
				publicacao.setLatitude(null);
				publicacao.setLongitude(null);
				publicacao.setEndereco(null);
				
			}
			publicacao.setArquivos(arquivos);
			
			texto.setAtivo(true);
			texto.setDatahora(LocalDateTime.now());
			texto.setUsuario(loginMb.getUsuarioLogado());
			texto.setPublicacao(publicacao);
			texto.setQtdCurtidas(0);
			texto.setQtdComentarios(0);
		
			textoDao.cria(texto);
			
			new CompartilhaDAOImpDB().cria(new Compartilha(
					LocalDateTime.now(),
					loginMb.getUsuarioLogado(),
					texto,
					grupo));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		limpar();
		return "feed";		
	}
	
	public String compartilharTexto() {
		try {
			if (grupoCompartilhado == null)
				throw new Exception();
			GrupoDaoImpl grupoDao = new GrupoDaoImpl();
			Texto texto = new TextoDAOImpDB().buscar(textoId);
			Grupo grupo = grupoDao.busca(grupoDao.buscaIdPorNome(grupoCompartilhado));
			Compartilha comp = new Compartilha(
					LocalDateTime.now(),
					loginMb.getUsuarioLogado(),
					texto,
					grupo);
			new CompartilhaDAOImpDB().cria(comp);
			limpar();
			return "feed";
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Falha ao compartilhar publicação");
			return null;
		}
	}
	
	public String excluirPublicacao() {
		try {	
			TextoDAOImpDB textoDao = new TextoDAOImpDB(); 
			GrupoDaoImpl grupoDao = new GrupoDaoImpl();
			Texto texto = textoDao.buscar(textoId);
			Grupo grupo = grupoDao.busca(grupoDao.buscaIdPorNome(grupoCompartilhado));
			if ((grupo == null)||(texto == null))
				throw new Exception();
			if (texto.getUsuario().getId()==loginMb.getUsuarioLogado().getId()) {
				textoDao.exclui(textoId);
			} else {
				new CompartilhaDAOImpDB().exclui(
						loginMb.getUsuarioLogado().getId(),
						textoId,
						grupo.getId());
			}
		}catch (Exception e) {
			JsfUtil.addErrorMessage("Falha ao excluir publicação");
			return null;
		}
		limpar();
		return "feed";
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public boolean isLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(boolean localizacao) {
		this.localizacao = localizacao;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getGrupoCompartilhado() {
		return grupoCompartilhado;
	}

	public void setGrupoCompartilhado(String grupoCompartilhado) {
		this.grupoCompartilhado = grupoCompartilhado;
	}

	public Part getArquivo() {
		return arquivo;
	}

	public void setArquivo(Part arquivo) {
		this.arquivo = arquivo;
	}

	public LoginMB getLoginMb() {
		return loginMb;
	}

	public void setLoginMb(LoginMB loginMb) {
		this.loginMb = loginMb;
	}

	public int getTextoId() {
		return textoId;
	}

	public void setTextoId(int textoId) {
		this.textoId = textoId;
	}





}
