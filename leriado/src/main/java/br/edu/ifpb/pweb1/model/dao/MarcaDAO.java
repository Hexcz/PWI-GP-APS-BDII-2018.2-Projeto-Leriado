package br.edu.ifpb.pweb1.model.dao;

import java.util.List;

import br.edu.ifpb.pweb1.model.domain.Marca;
import br.edu.ifpb.pweb1.model.domain.Texto;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

public interface MarcaDAO {
	
	void cria(Marca marca) throws DataAccessException;
	void exclui(Marca marca) throws DataAccessException;
	boolean existe(int textoId, int usuarioId) throws DataAccessException;
	List<Marca> listaMarca(Texto texto) throws DataAccessException;
}
