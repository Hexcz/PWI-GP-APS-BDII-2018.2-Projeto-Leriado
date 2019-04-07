package br.edu.ifpb.pweb1.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.edu.ifpb.pweb1.model.dao.FeedPublicacaoDAO;
import br.edu.ifpb.pweb1.model.dao.impdb.CurteDAOImpDB;
import br.edu.ifpb.pweb1.model.dao.impdb.FeedPublicacaoDAOImpDB;
import br.edu.ifpb.pweb1.model.dao.impdb.TextoDAOImpDB;
import br.edu.ifpb.pweb1.model.domain.FeedPublicacao;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

@ManagedBean(name="feedBean")
@ViewScoped
public class FeedMB {
	
	
	private int pagAtual;	
	private int pagQuant;
	private int feedQuant;
	private int feedPorPag;
	private FeedPublicacao publicacao;


	private List<Integer> paginacao;
	
	private List<FeedPublicacao> feedPublicacoes;
	private  FeedPublicacaoDAO feedPublDao;
	
	@ManagedProperty("#{loginBean}")
	private LoginMB loginMb;
	
	@PostConstruct
	private void inicio() {
		feedPublDao = new FeedPublicacaoDAOImpDB(loginMb.getUsuarioLogado());
		feedPorPag = 5;
		pagAtual = 1;
		paginacao = new ArrayList<>();
		listarPublicacoes();
	}
	
	private String listarPublicacoes(){
		String pag = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pag");		
		if(pag != null && !pag.isEmpty()) {
			try {
				pagAtual = Integer.parseInt(pag);
			}catch (Exception e) {
				pagAtual = 1;
			}
		} else {
			pagAtual = 1;
		}
		try {
			feedQuant = feedPublDao.quantFeed();
			pagQuant =  (int) Math.ceil((double)feedQuant / (double)feedPorPag); 
			feedPublicacoes = feedPublDao.listaFeed((pagAtual -1) * feedPorPag, feedPorPag);
			paginacao.clear();
			for (int i = 0; i < pagQuant; i++) {
				paginacao.add(i +1);
			}
			
		} catch (DataAccessException e) {		
			e.printStackTrace();
		}	
		return "";
	}
	
	public String curtir() {
		CurteDAOImpDB curteDao = new CurteDAOImpDB();
		int textoId = publicacao.getCompartilha().getTexto().getId();
		int usuarioId = loginMb.getUsuarioLogado().getId();
		try {
			int qtdCurtidas = publicacao.getCompartilha().getTexto().getQtdCurtidas();
			boolean curtido = publicacao.isCurtido();
			if (curtido) {
				curteDao.exclui(textoId, usuarioId);				
				qtdCurtidas --;
			}else {
				curteDao.cria(textoId, usuarioId);
				qtdCurtidas ++;				
			}
			publicacao.setCurtido(!curtido);
			publicacao.getCompartilha().getTexto().setQtdCurtidas(qtdCurtidas);
			new TextoDAOImpDB().atualizeContagem(publicacao.getCompartilha().getTexto());
		} catch (DataAccessException e) {			
			e.printStackTrace();
		}
		return "";
	}
	
	public FeedPublicacao getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(FeedPublicacao publicacao) {
		this.publicacao = publicacao;
	}

	public int getPagAtual() {
		return pagAtual;
	}

	public void setPagAtual(int pagAtual) {
		this.pagAtual = pagAtual;
	}

	public int getPagQuant() {
		return pagQuant;
	}

	public void setPagQuant(int pagQuant) {
		this.pagQuant = pagQuant;
	}

	public int getFeedQuant() {
		return feedQuant;
	}

	public void setFeedQuant(int feedQuant) {
		this.feedQuant = feedQuant;
	}

	public int getFeedPorPag() {
		return feedPorPag;
	}

	public void setFeedPorPag(int feedPorPag) {
		this.feedPorPag = feedPorPag;
	}

	public List<FeedPublicacao> getFeedPublicacoes() {
		return feedPublicacoes;
	}

	public void setFeedPublicacoes(List<FeedPublicacao> feedPublicacoes) {
		this.feedPublicacoes = feedPublicacoes;
	}

	public FeedPublicacaoDAO getFeedPublDao() {
		return feedPublDao;
	}

	public void setFeedPublDao(FeedPublicacaoDAO feedPublDao) {
		this.feedPublDao = feedPublDao;
	}

	public LoginMB getLoginMb() {
		return loginMb;
	}

	public void setLoginMb(LoginMB loginMb) {
		this.loginMb = loginMb;
	}
	
	
	public List<Integer> getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(List<Integer> paginacao) {
		this.paginacao = paginacao;
	}
	
}
