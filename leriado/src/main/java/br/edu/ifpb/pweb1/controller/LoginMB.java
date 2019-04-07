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
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import br.edu.ifpb.pweb1.model.dao.impdb.FeedGrupoDAOImpDB;
import br.edu.ifpb.pweb1.model.dao.impdb.TextoDAOImpDB;
import br.edu.ifpb.pweb1.model.dao.impdb.UsuarioDaoImpl;
import br.edu.ifpb.pweb1.model.domain.Arquivo;
import br.edu.ifpb.pweb1.model.domain.FeedGrupo;
import br.edu.ifpb.pweb1.model.domain.Publicacao;
import br.edu.ifpb.pweb1.model.domain.Texto;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;
import br.edu.ifpb.pweb1.util.JsfUtil;

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
	private int quantGruposParticipa;
	private List<FeedGrupo> seusGrupos;
	private Part imagem;
	private String paginaAtual;
	
	@ManagedProperty("#{usuariosOnline}")
	private UsuariosOnline usuariosOnlineMB;


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
		System.out.println("Instância de Login");
	}
	
	public String home() {
		paginaAtual = "feed";
		if (usuarioLogado != null)
			return "doHome";
		return "goLogin";
	}
	
	public String efetuarLogin() {		
		try {
			if (usuarioDao.login(email, senha)) {
				usuarioLogado = usuarioDao.buscarPorEmail(email);
				carregarPerfil();
				paginaAtual = "feed";
//				ADICIONA O NOVO USUÁRIO A LISTA DE USUARIOS ONLINE
				usuariosOnlineMB.adicionarUsuarioOnline(usuarioLogado);
				return "sucesso";
			}			
					
			
		} catch (Exception e) {			
			e.printStackTrace();
		}				
		JsfUtil.addErrorMessage("Falha ao efetuar login. Usuário ou senha inválido");
		
		return null;
	}
	
	public String carrgarGrupos() {
		try {
			FeedGrupoDAOImpDB feedGrupoDao = new FeedGrupoDAOImpDB(usuarioLogado);
			seusGrupos = feedGrupoDao.buscarGruposUsuarioParticipa(usuarioLogado.getId());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String carregarPerfil() {
		try {
			
			usuarioDao.buscar(usuarioLogado);
			usuarioDao.carregarFotoPerfil(usuarioLogado); //<=== CARREGAR A FOTO DO PERFIL
			usuarioLogado.setSenha("");
			senha = "";			
			carrgarGrupos();
			quantGruposParticipa = seusGrupos.size();
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
				usuarioLogado.setArquivoFoto(nomeArquivo);
				carregarPerfil();
			} catch (DataAccessException e) {
				e.printStackTrace();
			}	
		return "";
	}
	
	public String removerConta() {
		try {
			int usuarioId = usuarioLogado.getId();
			usuarioLogado = null;
			usuarioDao.remover(usuarioId);
			return "removido";
		}catch (Exception e) {
			e.printStackTrace();
		}		
		return "goLogin";
	}
	
	public String logout() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
//		REMOVE O USUÁRIO LOGADO DA LISTA DE ATIVOS
		usuariosOnlineMB.removerUsuarioOnline(usuarioLogado);
		System.out.println("Removendo Usuário ativo");
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

	public String getPaginaAtual() {
		return paginaAtual;
	}

	public void setPaginaAtual(String paginaAtual) {
		this.paginaAtual = paginaAtual;
	}

	public int getQuantGruposParticipa() {
		return quantGruposParticipa;
	}

	public void setQuantGruposParticipa(int quantGruposParticipa) {
		this.quantGruposParticipa = quantGruposParticipa;
	}

	public String getPathServImagem() {
		return pathServImagem;
	}

	public void setPathServImagem(String pathServImagem) {
		this.pathServImagem = pathServImagem;
	}

	public List<FeedGrupo> getSeusGrupos() {
		return seusGrupos;
	}

	public void setSeusGrupos(List<FeedGrupo> seusGrupos) {
		this.seusGrupos = seusGrupos;
	}

	public UsuariosOnline getUsuariosOnlineMB() {
		return usuariosOnlineMB;
	}

	public void setUsuariosOnlineMB(UsuariosOnline usuariosOnlineMB) {
		this.usuariosOnlineMB = usuariosOnlineMB;
	}
}
