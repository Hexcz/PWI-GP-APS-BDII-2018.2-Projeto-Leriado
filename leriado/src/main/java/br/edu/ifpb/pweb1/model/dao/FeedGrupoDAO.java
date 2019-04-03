package br.edu.ifpb.pweb1.model.dao;

import java.sql.SQLException;
import java.util.List;

import br.edu.ifpb.pweb1.model.domain.FeedGrupo;
import br.edu.ifpb.pweb1.model.domain.Grupo;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

public interface FeedGrupoDAO {
	
	public void criar(FeedGrupo novoFeedGrupo) throws DataAccessException;
	public void excluir(int idFeedGrupo) throws DataAccessException;
	void adicionarUsuario(int idFeedGrupo, int idUsuario) throws DataAccessException;
	void removerUsuario(int idFeedGrupo, int idUsuario) throws DataAccessException;
	FeedGrupo busca(int id)throws DataAccessException;
	public int buscaIdPorNome(String nome) throws DataAccessException;
	public List<FeedGrupo> buscarGruposUsuarioParticipa(int idUsuario) throws DataAccessException;
	public List<FeedGrupo> admsGrupo(int idUsuario) throws DataAccessException;
	public void adicionarAdministrador(int idUsuario,int idFeedGrupo) throws DataAccessException;

}
