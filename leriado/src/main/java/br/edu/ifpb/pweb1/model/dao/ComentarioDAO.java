package br.edu.ifpb.pweb1.model.dao;

import java.util.List;

import br.edu.ifpb.pweb1.model.domain.Texto;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

public interface ComentarioDAO {

	void cria(Texto comentario, int textoId) throws DataAccessException;
	void edita(Texto comentario) throws DataAccessException;
	void exclui(Texto comentario) throws DataAccessException;	
	int quant(int textoId) throws DataAccessException;
	List<Texto> lista(int textoId) throws DataAccessException;
	List<Texto>lista(int textoId, int inicio,int quant) throws DataAccessException;
}
