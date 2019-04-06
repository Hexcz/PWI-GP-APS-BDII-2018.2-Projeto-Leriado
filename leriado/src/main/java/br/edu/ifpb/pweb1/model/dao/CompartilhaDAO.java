package br.edu.ifpb.pweb1.model.dao;

import java.util.List;

import br.edu.ifpb.pweb1.model.domain.Compartilha;
import br.edu.ifpb.pweb1.model.domain.Grupo;
import br.edu.ifpb.pweb1.model.domain.Texto;
import br.edu.ifpb.pweb1.model.domain.Usuario;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

public interface CompartilhaDAO {
	
	void cria(Compartilha compartilha) throws DataAccessException;
	void cria(int usuarioId, int textoId, int grupoId)throws DataAccessException;
	void exclui(Compartilha compartilha) throws DataAccessException;
	void exclui(int usuarioId, int textoId, int grupoId ) throws DataAccessException;
	boolean existe(int usuarioId, int publicacaoId, int grupoId)throws DataAccessException;
	int quant()throws DataAccessException;
	int quant(Grupo grupo) throws DataAccessException;
	int quant(Usuario usuario) throws DataAccessException;
	int quant(Texto texto)throws DataAccessException;
	List<Compartilha> lista() throws DataAccessException;
	List<Compartilha> lista(Grupo grupo) throws DataAccessException;
	List<Compartilha> lista(Usuario usuario) throws DataAccessException;
	List<Compartilha> lista(Texto texto) throws DataAccessException;
	List<Compartilha> lista(int inicio, int quant) throws DataAccessException;
	List<Compartilha> lista(Grupo grupo, int inicio, int quant) throws DataAccessException;
	List<Compartilha> lista(Usuario usuario, int inicio, int quant) throws DataAccessException;
	List<Compartilha> lista(Texto texto, int inicio, int quant) throws DataAccessException;
	int quantFeed(Usuario usuario) throws DataAccessException;
	List<Compartilha> feed(Usuario usuario, int inicio, int quant)throws DataAccessException;
}
