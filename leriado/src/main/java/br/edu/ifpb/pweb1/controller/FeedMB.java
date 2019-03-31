package br.edu.ifpb.pweb1.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import br.edu.ifpb.pweb1.model.dao.FeedPublicacaoDAO;
import br.edu.ifpb.pweb1.model.dao.impdb.CurteDAOImpDB;
import br.edu.ifpb.pweb1.model.dao.impdb.FeedPublicacaoDAOImpDB;
import br.edu.ifpb.pweb1.model.domain.FeedPublicacao;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

@ManagedBean(name="feedBean")
@RequestScoped
public class FeedMB {
	
	
	private int pagAtual;	
	private int pagQuant;
	private int feedQuant;
	private int feedPorPag;
	private FeedPublicacao publicacao;
	
	
	private List<FeedPublicacao> feedPublicacoes;
	private  FeedPublicacaoDAO feedPublDao;
	
	@ManagedProperty("#{loginBean}")
	private LoginMB loginMb;
	
	@PostConstruct
	private void inicio() {
		feedPublDao = new FeedPublicacaoDAOImpDB(loginMb.getUsuarioLogado());
		feedPorPag = 5;
		pagAtual = 0;
		listarPublicacoes();
	}
	
	public String listarPublicacoes(){
		try {
			feedQuant = feedPublDao.quantFeed();
			pagQuant =  (int) Math.ceil((double)feedQuant / (double)feedPorPag); 
			feedPublicacoes = feedPublDao.listaFeed(pagAtual, feedPorPag);			
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
			if (curteDao.existe(textoId, usuarioId)) {
				curteDao.exclui(textoId, usuarioId);
			}else {
				curteDao.cria(textoId, usuarioId);
			}
		} catch (DataAccessException e) {			
			e.printStackTrace();
		}		
		listarPublicacoes();
		return "feed";
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
	
	
	
}
