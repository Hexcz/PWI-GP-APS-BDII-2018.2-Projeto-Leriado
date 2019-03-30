package br.edu.ifpb.pweb1.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.edu.ifpb.pweb1.model.dao.publicacao.impdb.PublicacaoDAOImpDB;
import br.edu.ifpb.pweb1.model.domain.publicacao.Publicacao;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

@ManagedBean(name="feedBean")
@RequestScoped
public class FeedMB {
	
	private PublicacaoDAOImpDB publicacaoDao;
	private int grupoId;	
	private int paginaAtual;
	private int qtdPaginas;
	private List<Publicacao> publicacoes;
	
	@PostConstruct
	private void inicio() {
		publicacaoDao = new PublicacaoDAOImpDB();		
	}
	
	public String listarPublicacoes(){
		try {
			publicacoes = publicacaoDao.lista();
		} catch (DataAccessException e) {		
			e.printStackTrace();
		}
		return "";
	}
	
	
	public int getGrupoId() {
		return grupoId;
	}
	public void setGrupoId(int grupoId) {
		this.grupoId = grupoId;
	}
	public int getPaginaAtual() {
		return paginaAtual;
	}
	public void setPaginaAtual(int paginaAtual) {
		this.paginaAtual = paginaAtual;
	}
	public int getQtdPaginas() {
		return qtdPaginas;
	}
	public void setQtdPaginas(int qtdPaginas) {
		this.qtdPaginas = qtdPaginas;
	}
	public List<Publicacao> getPublicacoes() {
		return publicacoes;
	}
	public void setPublicacoes(List<Publicacao> publicacoes) {
		this.publicacoes = publicacoes;
	}
	
	

	
	
}
