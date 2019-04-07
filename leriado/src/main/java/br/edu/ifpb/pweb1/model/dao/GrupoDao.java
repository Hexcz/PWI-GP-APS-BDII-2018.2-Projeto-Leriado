package br.edu.ifpb.pweb1.model.dao;

import java.util.List;

import br.edu.ifpb.pweb1.model.domain.Grupo;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

public interface GrupoDao {
	public void criar(Grupo novoGrupo) throws DataAccessException;
	public void excluir(int idGrupo) throws DataAccessException;
	public void editar(Grupo grupo) throws DataAccessException;
	boolean participa(int usuarioId, int grupoId)throws DataAccessException;
	boolean eAdministrador(int usuarioId, int grupoId)throws DataAccessException;
	int qtdParticipantes(int grupoId) throws DataAccessException;
	void adicionarUsuario(int idGrupo, int idUsuario) throws DataAccessException;
	void removerUsuario(int idGrupo, int idUsuario) throws DataAccessException;
	Grupo busca(int id)throws DataAccessException;
	public int buscaIdPorNome(String nome) throws DataAccessException;
	public List<Grupo> buscarGruposUsuarioParticipa(int idUsuario) throws DataAccessException;	
	public List<Grupo> admsGrupo(int idUsuario) throws DataAccessException;	
	public void adicionarAdministrador(int idUsuario,int idGrupo) throws DataAccessException;	
 }

