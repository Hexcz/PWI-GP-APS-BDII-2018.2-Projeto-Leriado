package br.edu.ifpb.pweb1.model.dao.impdb;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.edu.ifpb.pweb1.model.dao.PublicacaoDAO;
import br.edu.ifpb.pweb1.model.domain.Publicacao;
import br.edu.ifpb.pweb1.model.jdbc.ConnectionFactory;
import br.edu.ifpb.pweb1.model.jdbc.DataAccessException;
import redis.clients.jedis.Jedis;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class PublicacaoDAOImpDB implements PublicacaoDAO {

	private MongoDatabase dataBase;
	private MongoCollection<Publicacao> collection;
	private Gson gson;
	private Jedis jedis;
	private Long jedisTimeout;
	private static String prefRedis = "publ";	
	
	private static Logger log = Logger.getLogger(PublicacaoDAOImpDB.class.getName());

	public PublicacaoDAOImpDB() {
		dataBase = ConnectionFactory.getInstance().getMongoDataBase();
		collection = dataBase.getCollection("Publicacao", Publicacao.class);
		/*Startando o redis*/
		jedis = ConnectionFactory.getInstance().getRedis();
		jedisTimeout = ConnectionFactory.getInstance().getRedisTimeout();
		gson = new Gson();
	}
	
	private void salvaRedis(Publicacao publicacao) {		
		log.info("Publicação salva no Redis");
		String json = gson.toJson(publicacao);
		jedis.psetex(prefRedis+publicacao.getTextoId(), jedisTimeout, json);		
	}	
	
	private Publicacao buscaRedis(int id) {		
		if(!jedis.exists(prefRedis+id))
			return null;
		log.info("Publicação buscada no Redis");
		String json = jedis.get(prefRedis+id);
		jedis.psetex(prefRedis+id, jedisTimeout, json);
		Publicacao publicacao = gson.fromJson(json, Publicacao.class);		
		return publicacao;
	}
	
	private void excluiRedis(int id) {
		log.info("Publicação excluída no Redis");
		jedis.del(prefRedis+id);	
		System.out.println(id);
	}

	@Override
	public void cria(Publicacao publicacao) throws DataAccessException {
		salvaRedis(publicacao);
		collection.insertOne(publicacao);				
	}

	@Override
	public void edita(Publicacao publicacao) throws DataAccessException {
		BasicDBObject updBasicDBObject = new BasicDBObject();
		salvaRedis(publicacao);
		updBasicDBObject.append("_id", publicacao.getTextoId());
		UpdateResult rs = collection.replaceOne(updBasicDBObject, publicacao);
		if(rs.getModifiedCount() == 0) {
			throw new DataAccessException("Falha ao editar publicação");
		}
		
	}

	@Override
	public void exclui(Publicacao publicacao) throws DataAccessException {
		excluiRedis(publicacao.getTextoId());
		DeleteResult rs = collection.deleteOne(eq("_id",(publicacao.getTextoId())));
		if(rs.getDeletedCount()==0)
			throw new DataAccessException("Falha ao deletar publicacao");
		
	}

	@Override
	public Publicacao buscar(int id) throws DataAccessException {
		Publicacao publicacao = buscaRedis(id);
		if (publicacao!=null) {
			return publicacao;			
		}
		publicacao = collection.find(eq("_id",id)).first();
		salvaRedis(publicacao);
		return publicacao;
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
			salvaRedis(publicacao);
		}
		return pubs;
	}

	@Override
	public List<Publicacao> lista(int inicio, int quant) throws DataAccessException {
		List<Publicacao> pubs = new ArrayList<Publicacao>();
		for (Publicacao publicacao : collection.find().skip(inicio).limit(quant)) {			
			pubs.add(publicacao);			
			salvaRedis(publicacao);
		}
		return pubs;
	}
	
}
