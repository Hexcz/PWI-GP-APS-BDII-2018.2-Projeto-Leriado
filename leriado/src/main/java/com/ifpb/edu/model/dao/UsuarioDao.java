package com.ifpb.edu.model.dao;

import java.sql.SQLException;
import java.util.List;

import com.ifpb.edu.model.domain.Usuario;
import com.ifpb.edu.model.jdbc.DataAccessException;

public interface UsuarioDao {
	
	void criar(Usuario usuario) throws DataAccessException;
	void atualizar(Usuario usuarioNovo, Integer idUsuario) throws DataAccessException;
	void remover(Integer idUsuario) throws DataAccessException;
	Usuario buscarPorId(Integer id) throws DataAccessException;
	Usuario buscarPorEmail(String email) throws DataAccessException;
	List<Usuario> buscarNome(String nome) throws DataAccessException;
	List<Usuario> buscarNome(String nome, int inicio, int quant) throws DataAccessException;
	int qtdBuscarNome(String nome) throws DataAccessException;
	boolean login(String email, String senha) throws DataAccessException;
	void seguir(Usuario seguidor, Usuario seguido) throws DataAccessException;
	void desfazerAmizade(Usuario usuario1, Usuario usuario2) throws DataAccessException;
	List<Usuario> amigos(Usuario usuario) throws DataAccessException;
	List<Usuario> amigos(Usuario usuario, int inicio, int quant) throws DataAccessException;
	int qtdAmigos(Usuario usuario) throws DataAccessException;
	List<Usuario> solicitacoesAmizades(Usuario self) throws DataAccessException;
	List<Usuario> solicitacoesAmizades(Usuario self, int inicio, int quant) throws DataAccessException;
	int qtdSolicitacoesAmizades(Usuario self) throws DataAccessException;
}
