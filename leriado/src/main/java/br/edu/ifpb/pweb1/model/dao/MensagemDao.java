package br.edu.ifpb.pweb1.model.dao;

import java.util.List;

import br.edu.ifpb.pweb1.model.domain.Mensagem;

public interface MensagemDao {
	
	public void criar(Mensagem m);
	public List<Mensagem> recuperarMensagens(String idDest, String idRemet);
}
