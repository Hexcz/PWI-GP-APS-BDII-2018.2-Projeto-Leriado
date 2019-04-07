package br.edu.ifpb.pweb1.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Part;
import javax.servlet.jsp.el.ELException;

import br.edu.ifpb.pweb1.model.dao.impdb.GrupoDaoImpl;
import br.edu.ifpb.pweb1.model.dao.impdb.UsuarioDaoImpl;
import br.edu.ifpb.pweb1.model.domain.FeedGrupo;
import br.edu.ifpb.pweb1.model.domain.Grupo;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;
import br.edu.ifpb.pweb1.util.JsfUtil;

@ManagedBean(name = "grupoBean")
@ViewScoped
public class GrupoMB {
	
	private List<FeedGrupo> Feedgrupos;
	private Usuario usuario;
	private Grupo grupo;	
	private Part imagem;
	private GrupoDaoImpl grupoDao;
	private int grupoId;
	private String email;
	
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
				return null;
			}
			break;
		}
		case "editarGrupo":{
			try {
				grupoDao.editar(grupo);
			}catch (Exception e) {
				JsfUtil.addErrorMessage("Impossível editar o grupo");
				return null;
			}
			break;
		}
		}	
		loginMb.carrgarGrupos();
		loginMb.setPaginaAtual("grupo");
		return "";
	}
	
	public String sairGrupo() {
		try {
			grupoDao.removerUsuario(grupoId, loginMb.getUsuarioLogado().getId());
			loginMb.carrgarGrupos();
		} catch (DataAccessException e) {
			JsfUtil.addErrorMessage("Falha ao remover usuário do grupo");
			e.printStackTrace();
			return null;			
		}
		return "";
	}
	
	
	public String removerUsuario() {
		try {
			new GrupoDaoImpl().removerUsuario(grupo.getId(), usuario.getId());
			grupo = null;
			loginMb.carrgarGrupos();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String excluirGrupo() {
		try {
			grupo = grupoDao.busca(grupoId);
			if(grupo==null)
				throw new Exception();
			grupoDao.excluir(grupo.getId());
			grupo = null;
			loginMb.carrgarGrupos();
		} catch (Exception e) {		
			JsfUtil.addErrorMessage("Falha ao excluir grupo");
			e.printStackTrace();
			return null;
		}		
		return "";
	}
	
	public String adicionarUsuario() {
		try {
			System.out.println(email);
			System.out.println(grupoId);
			Usuario usuario = new UsuarioDaoImpl().buscarPorEmail(email);
			grupo = grupoDao.busca(grupoId);
			if((usuario == null) || (grupo == null))
				throw new Exception();
			new GrupoDaoImpl().adicionarUsuario(grupo.getId(), usuario.getId());
			grupo = null;
			loginMb.carrgarGrupos();
			loginMb.carrgarGrupos();
			grupo = null;
			loginMb.carrgarGrupos();
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Falha ao adicionar usuário");
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

	public Part getImagem() {
		return imagem;
	}

	public void setImagem(Part imagem) {
		this.imagem = imagem;
	}

	public int getGrupoId() {
		return grupoId;
	}

	public void setGrupoId(int grupoId) {
		this.grupoId = grupoId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String emailNova) {
		this.email = emailNova;
	}


	

}
