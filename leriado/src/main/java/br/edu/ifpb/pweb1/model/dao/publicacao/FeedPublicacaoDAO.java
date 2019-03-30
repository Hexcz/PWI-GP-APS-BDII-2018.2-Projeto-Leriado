package br.edu.ifpb.pweb1.model.dao.publicacao;

import java.util.List;

import br.edu.ifpb.pweb1.model.domain.Grupo;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.domain.publicacao.FeedPublicacao;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

public interface FeedPublicacaoDAO {
	
	int quantFeed() throws DataAccessException;
	
	int quantFeed(Usuario usuario) throws DataAccessException;

	int quantFeed(Grupo grupo) throws DataAccessException;

	List<FeedPublicacao> listaFeed(int inicio, int quant) throws DataAccessException;

	List<FeedPublicacao> listaUsuario(Usuario usuario, int inicio, int quant) throws DataAccessException;

	List<FeedPublicacao> listaGrupo(Grupo grupo, int inicio, int quant) throws DataAccessException;

	void carregarComentarios(FeedPublicacao feedPublicacao) throws DataAccessException;

	void carregarComentarios(FeedPublicacao feedPublicacao, int inicio, int quant) throws DataAccessException;
}
