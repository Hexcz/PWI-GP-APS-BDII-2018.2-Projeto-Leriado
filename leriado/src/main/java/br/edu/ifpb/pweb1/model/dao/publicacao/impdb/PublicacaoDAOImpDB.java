package br.edu.ifpb.pweb1.model.dao.publicacao.impdb;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pweb1.model.dao.publicacao.PublicacaoDAO;
import br.edu.ifpb.pweb1.model.domain.publicacao.Publicacao;
import br.edu.ifpb.pweb1.model.jdbc.ConnectionFactory;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
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
		DeleteResult rs = collection.deleteOne(eq("_id",(publicacao.getTextoId())));
		if(rs.getDeletedCount()==0)
			throw new DataAccessException("Falha ao deletar publicacao");
		
	}

	@Override
	public Publicacao buscar(int id) throws DataAccessException {		 		
		return collection.find(eq("_id",id)).first();
	}

	@Override
	public void buscar(Publicacao publicacao) throws DataAccessException {
		int id = publicacao.getTextoId();
		publicacao = buscar(id);
		
	}

	@Override
	public void buscar(int id, Publicacao publicacao) throws DataAccessException {
		publicacao = buscar(id);		
	}

	@Override
	public int quant() throws DataAccessException {
		return (int) collection.countDocuments();
	}

	@Override
	public List<Publicacao> lista() throws DataAccessException {
		List<Publicacao> pubs = new ArrayList<Publicacao>();
		for (Publicacao publicacao : collection.find()) {
			pubs.add(publicacao);			
		}
		return pubs;
	}

	@Override
	public List<Publicacao> lista(int inicio, int quant) throws DataAccessException {
		List<Publicacao> pubs = new ArrayList<Publicacao>();
		for (Publicacao publicacao : collection.find().skip(inicio).limit(quant)) {
			pubs.add(publicacao);			
		}
		return pubs;
	}
	
}
