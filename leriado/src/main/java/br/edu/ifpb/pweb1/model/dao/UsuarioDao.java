package br.edu.ifpb.pweb1.model.dao;

import java.util.List;

import br.edu.ifpb.pweb1.model.domain.Texto;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

public interface UsuarioDao {
	
	void criar(Usuario usuario) throws DataAccessException;
	void atualizar(Usuario usuarioNovo, Integer idUsuario) throws DataAccessException;
	void remover(Integer idUsuario) throws DataAccessException;
	void mudarFotoPerfil(Usuario usuario, Texto texto) throws DataAccessException;
	void mudarFotoPerfil(int usuarioId, int textoId) throws DataAccessException;
	void carregarFotoPerfil(Usuario usuario) throws DataAccessException;
	void carregarFotoPerfil(List<Usuario> usuarios) throws DataAccessException;
	String carregarFotoPerfil(int usuarioId) throws DataAccessException;	
	Usuario buscarPorId(Integer id) throws DataAccessException;
	Usuario buscarPorEmail(String email) throws DataAccessException;
	void buscar(Usuario usuario)throws DataAccessException; 
	List<Usuario> buscar(String consulta) throws DataAccessException;
	List<Usuario> buscar(String consulta, int inicio, int quant) throws DataAccessException;
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
	void mudarStatus(Usuario self, Usuario usuario) throws DataAccessException;
	void mudarStatus(Usuario self, List<Usuario> usuarios) throws DataAccessException;
	
}
