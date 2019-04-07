package br.edu.ifpb.pweb1.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Part;

import br.edu.ifpb.pweb1.model.dao.impdb.GrupoDaoImpl;
import br.edu.ifpb.pweb1.model.dao.impdb.UsuarioDaoImpl;
import br.edu.ifpb.pweb1.model.domain.FeedGrupo;
import br.edu.ifpb.pweb1.model.domain.Grupo;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.util.JsfUtil;

@ManagedBean(name = "grupoBean")
@ViewScoped
public class GrupoMB {
	
	private List<FeedGrupo> Feedgrupos;
	private Usuario usuario;
	private Grupo grupo;
	private String emailUsuario;
	private Part imagem;
	private GrupoDaoImpl grupoDao;
	
	@ManagedProperty("#{loginBean}")
	private LoginMB loginMb;
	
	@PostConstruct
	public void inicial() {
		grupo = new Grupo();
		grupoDao = new GrupoDaoImpl();
		
	}
	
	public String mudarFotoGrupo() {
		try {
			grupo.setFoto(GerirArquivos.salvarArquivoPasta(imagem, loginMb.getPathServImagem()));						
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String paginaGrupo() {		
		loginMb.setPaginaAtual("grupo");
		loginMb.carrgarGrupos();
		return "";
	}
	
	public String criarGrupo() {
		grupo = new Grupo();
		loginMb.setPaginaAtual("criarGrupo");
		return "";
	}
	
	public String editarGrupo() {
		if(grupo == null)
			loginMb.setPaginaAtual("grupo");
		else
			loginMb.setPaginaAtual("editarGrupo");
		return "";
	}
	
	public String salvarGrupo() {		
		switch(loginMb.getPaginaAtual()){
		case "criarGrupo":{
			try {									
				grupoDao.criar(grupo);
				grupoDao.adicionarUsuario(grupo.getId(), loginMb.getUsuarioLogado().getId());
				grupoDao.adicionarAdministrador(loginMb.getUsuarioLogado().getId(), grupo.getId());
				loginMb.carrgarGrupos();
			}catch (Exception e) {
				JsfUtil.addErrorMessage("Impossível salvar o grupo");
			}
			break;
		}
		case "editarGrupo":{
			try {
				grupoDao.editar(grupo);
			}catch (Exception e) {
				JsfUtil.addErrorMessage("Impossível editar o grupo");
			}
			break;
		}
		}	
		loginMb.carrgarGrupos();
		loginMb.setPaginaAtual("grupo");
		return null;
	}
	
	
	public String removerUsuario() {
		try {
			new GrupoDaoImpl().removerUsuario(grupo.getId(), usuario.getId());
			
			emailUsuario = "";
			grupo = null;
			loginMb.carrgarGrupos();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String excluirGrupo() {
		try {
			new GrupoDaoImpl().excluir(grupo.getId());
			
			emailUsuario = "";
			grupo = null;
			loginMb.carrgarGrupos();
		} catch (Exception e) {		
			e.printStackTrace();
		}		
		return "";
	}
	
	public String adicionarUsuario() {
		
		try {
			Usuario usuario = new UsuarioDaoImpl().buscarPorEmail(emailUsuario);
			if(usuario == null)
				return "";
			new GrupoDaoImpl().adicionarUsuario(grupo.getId(), usuario.getId());
			grupo = null;
			loginMb.carrgarGrupos();
			loginMb.carrgarGrupos();
			grupo = null;
			loginMb.carrgarGrupos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	

	public LoginMB getLoginMb() {
		return loginMb;
	}

	public void setLoginMb(LoginMB loginMb) {
		this.loginMb = loginMb;
	}

	public List<FeedGrupo> getFeedgrupos() {
		return Feedgrupos;
	}

	public void setFeedgrupos(List<FeedGrupo> feedgrupos) {
		Feedgrupos = feedgrupos;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public Part getImagem() {
		return imagem;
	}

	public void setImagem(Part imagem) {
		this.imagem = imagem;
	}
	

}
