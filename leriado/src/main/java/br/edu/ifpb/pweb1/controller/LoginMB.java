package br.edu.ifpb.pweb1.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import br.edu.ifpb.pweb1.model.dao.impdb.CompartilhaDAOImpDB;
import br.edu.ifpb.pweb1.model.dao.impdb.GrupoDaoImpl;
import br.edu.ifpb.pweb1.model.dao.impdb.TextoDAOImpDB;
import br.edu.ifpb.pweb1.model.dao.impdb.UsuarioDaoImpl;
import br.edu.ifpb.pweb1.model.domain.Arquivo;
import br.edu.ifpb.pweb1.model.domain.Compartilha;
import br.edu.ifpb.pweb1.model.domain.Grupo;
import br.edu.ifpb.pweb1.model.domain.Publicacao;
import br.edu.ifpb.pweb1.model.domain.Texto;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

/**
 * @author isleimar
 *
 */
@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginMB {

	private Usuario usuarioLogado = null;

	private String email;
	private String senha;	
	private String pathUrlImagem;
	private String pathServImagem;
	private UsuarioDaoImpl usuarioDao;	
	private List<String> seusGrupos;
	private Part imagem;


	@PostConstruct
	public void inicial() {
		seusGrupos = new ArrayList<>();
		usuarioDao = new UsuarioDaoImpl();
		try{
			Properties properties = new Properties();	
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			properties.load(inputStream);
			inputStream.close();			
			pathUrlImagem = properties.getProperty("path.url.imagem");
			pathServImagem = properties.getProperty("path.serv.imagem");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String efetuarLogin() {		
		try {
			if (usuarioDao.login(email, senha)) {
				usuarioLogado = usuarioDao.buscarPorEmail(email);
				carregarPerfil();				
				return "sucesso";
			}			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Falha ao efetura login", "Falha ao efetuar login, usuário ou senha inválidos"));
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return "falha";
	}
	
	public String carregarPerfil() {
		try {
			usuarioDao.buscar(usuarioLogado);
			usuarioDao.carregarFotoPerfil(usuarioLogado); //<=== CARREGAR A FOTO DO PERFIL
			usuarioLogado.setSenha("");
			senha = "";
			seusGrupos = new GrupoDaoImpl().buscarGruposUsuarioParticipa(usuarioLogado.getId());
			System.out.println("Perfil carregado");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String mudarFotoPerfil() {
		if(imagem != null)
			try {
				TextoDAOImpDB textoDao = new TextoDAOImpDB();
				Publicacao publicacao = new Publicacao();				
				Texto texto = new Texto();		
				List<Arquivo> imagens = new ArrayList<>();		
				/*Salvando o arquivo na pasta do servidor*/
				String nomeArquivo = GerirArquivos.salvarArquivoPasta(imagem, pathServImagem);
				
				imagens.add(new Arquivo(nomeArquivo, imagem.getSubmittedFileName(), imagem.getSize(), "Nenhuma", true));			
						
				publicacao.setTitulo("Mudança da foto do perfil");
				publicacao.setConteudo(usuarioLogado.getNome()+" modificou a foto do perfil.");
				
				publicacao.setArquivos(imagens);
				
				texto.setAtivo(true);
				texto.setDatahora(LocalDateTime.now());
				texto.setUsuario(usuarioLogado);
				texto.setPublicacao(publicacao);
				texto.setQtdCurtidas(0);
				texto.setQtdComentarios(0);
			
				textoDao.cria(texto);	
				usuarioDao.mudarFotoPerfil(usuarioLogado, texto);
				carregarPerfil();
			} catch (DataAccessException e) {
				e.printStackTrace();
			}	
		return "";
	}
	
	public String logout() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		request.getSession(false).invalidate();
		return "goLogin";
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getPathUrlImagem() {
		return pathUrlImagem;
	}

	public void setPathUrlImagem(String pathUrlImagem) {
		this.pathUrlImagem = pathUrlImagem;
	}
	
	public Part getImagem() {
		return imagem;
	}

	public void setImagem(Part imagem) {
		this.imagem = imagem;
	}

	public List<String> getSeusGrupos() {
		return seusGrupos;
	}

	public void setSeusGrupos(List<String> seusGrupos) {
		this.seusGrupos = seusGrupos;
	}

}
