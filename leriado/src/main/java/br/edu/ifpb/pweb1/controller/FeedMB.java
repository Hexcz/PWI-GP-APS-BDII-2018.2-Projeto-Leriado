package br.edu.ifpb.pweb1.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import br.edu.ifpb.pweb1.model.dao.publicacao.FeedPublicacaoDAO;
import br.edu.ifpb.pweb1.model.dao.publicacao.impdb.FeedPublicacaoDAOImpDB;
import br.edu.ifpb.pweb1.model.domain.publicacao.FeedPublicacao;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

@ManagedBean(name="feedBean")
@RequestScoped
public class FeedMB {
	
	
	private int pagAtual;	
	private int pagQuant;
	private int feedQuant;
	private int feedPorPag;
	
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
