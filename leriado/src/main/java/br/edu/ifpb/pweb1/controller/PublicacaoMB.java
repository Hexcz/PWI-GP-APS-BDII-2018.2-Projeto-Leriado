package br.edu.ifpb.pweb1.controller;

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.Part;

import br.edu.ifpb.pweb1.model.dao.GrupoDaoImpl;
import br.edu.ifpb.pweb1.model.dao.publicacao.impdb.CompartilhaDAOImpDB;
import br.edu.ifpb.pweb1.model.dao.publicacao.impdb.TextoDAOImpDB;
import br.edu.ifpb.pweb1.model.domain.Grupo;
import br.edu.ifpb.pweb1.model.domain.publicacao.Arquivo;
import br.edu.ifpb.pweb1.model.domain.publicacao.Compartilha;
import br.edu.ifpb.pweb1.model.domain.publicacao.Publicacao;
import br.edu.ifpb.pweb1.model.domain.publicacao.Texto;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

@ManagedBean(name = "publicacaoBean")
@RequestScoped
public class PublicacaoMB {
	
	private String imgSource ="/home/isleimar/Imagens/imgSite/";
	private String titulo;
	private String conteudo;
	private boolean localizacao;
	private String latitude;
	private String longitude;
	private String grupoCompartilhado;
	private Part arquivo;
	
	@ManagedProperty("#{loginBean}")
	private LoginMB loginMb;
	
	@PostConstruct
	private void iniciar() {
		localizacao = true;
	}
	
	private String md5(String texto) {
		try {

			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(texto.getBytes(), 0, texto.length());
			return new BigInteger(1, m.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
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
			
			
			System.out.println("Titulo: " + titulo);
			System.out.println("Conteúdo: " + conteudo);
			System.out.println("Localização: " + localizacao);
			System.out.println("Latitude: " + latitude);
			System.out.println("Longitude: " + longitude);
			System.out.println("Grupo Compartilhado: " + grupoCompartilhado);		
			
			if( arquivo != null) {				
				String nomeArquivo = md5(LocalDateTime.now().toString() + "-" + arquivo.getSubmittedFileName());
				System.out.println("Arquivo: " + nomeArquivo);
				try (InputStream file = arquivo.getInputStream()){
					arquivos.add(new Arquivo(nomeArquivo, arquivo.getSubmittedFileName(), arquivo.getSize(), "Nenhuma", true));
					Files.copy(file , new File(imgSource + nomeArquivo).toPath(), StandardCopyOption.REPLACE_EXISTING);			
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			publicacao.setTitulo(titulo);
			publicacao.setConteudo(conteudo);
			if(localizacao) {
				publicacao.setLatitude(latitude);
				publicacao.setLongitude(longitude);
			} else {
				publicacao.setLatitude(null);
				publicacao.setLongitude(null);
				
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
			
			System.out.println("Publicação realizada");
			
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		
		
		limpar();
		return "";
		
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



}
