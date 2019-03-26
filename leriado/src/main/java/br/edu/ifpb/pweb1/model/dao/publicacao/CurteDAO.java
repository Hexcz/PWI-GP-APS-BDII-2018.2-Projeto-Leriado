package br.edu.ifpb.pweb1.model.dao.publicacao;

import java.util.List;

import br.edu.ifpb.pweb1.model.domain.publicacao.Curte;
import br.edu.ifpb.pweb1.model.domain.publicacao.Texto;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

public interface CurteDAO {
	
	void cria(Curte curte) throws DataAccessException;
	void cria(int textoId, int usuarioId) throws DataAccessException;
	void exclui(Curte curte) throws DataAccessException;
	void exclui(int textoId, int usuarioId) throws DataAccessException;
	int quant(Texto texto) throws DataAccessException;
	boolean existe(int textoId, int usuarioId) throws DataAccessException;
	List<Curte> lista(Texto texto) throws DataAccessException;
}
