package br.edu.ifpb.pweb1.model.dao.publicacao;

import java.util.List;
import java.util.Optional;

import br.edu.ifpb.pweb1.model.domain.publicacao.Texto;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;

public interface TextoDAO {	
	int cria(Texto texto) throws DataAccessException;
	void edita(Texto texto) throws DataAccessException;
	void exclui(Texto texto) throws DataAccessException;	
	Texto buscar(int id) throws DataAccessException;
	void buscar(Texto texto) throws DataAccessException;
	void buscar(int id, Texto texto) throws DataAccessException;
	int quant() throws DataAccessException;
	List<Texto> lista() throws DataAccessException;
	List<Texto>lista(int inicio,int quant) throws DataAccessException;
}
