package com.ifpb.edu.model.dao.publicacao.impdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ifpb.edu.model.dao.publicacao.PublicacaoDAO;
import com.ifpb.edu.model.domain.publicacao.Publicacao;
import com.ifpb.edu.model.jdbc.ConnectionFactory;
import com.ifpb.edu.model.jdbc.DataAccessException;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

public class PublicacaoDAOImpDB implements PublicacaoDAO {

	private MongoDatabase dataBase;
	private MongoCollection<Publicacao> collection;

	public PublicacaoDAOImpDB() {
		dataBase = ConnectionFactory.getInstance().getMongoDataBase();
		collection = dataBase.getCollection("Publicacao", Publicacao.class);
	}

	@Override
	public void cria(Publicacao publicacao) throws DataAccessException {
		collection.insertOne(publicacao);
	}

	@Override
	public void edita(Publicacao publicacao) throws DataAccessException {
		BasicDBObject updBasicDBObject = new BasicDBObject();
		updBasicDBObject.append("_id", publicacao.getTextoId());
		UpdateResult rs = collection.replaceOne(updBasicDBObject, publicacao);
		if(rs.getModifiedCount() == 0) {
			throw new DataAccessException("Falha ao editar publicação");
		}
	}

	@Override
	public void exclui(Publicacao publicacao) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Publicacao buscar(int id) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buscar(Publicacao publicacao) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buscar(int id, Publicacao publicacao) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int quant() throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Publicacao> lista() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Publicacao> lista(int inicio, int quant) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
