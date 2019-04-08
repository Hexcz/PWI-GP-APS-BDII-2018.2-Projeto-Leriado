package br.edu.ifpb.pweb1.model.dao.impdb;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.*;

import br.edu.ifpb.pweb1.model.dao.MensagemDao;
import br.edu.ifpb.pweb1.model.domain.Mensagem;
import br.edu.ifpb.pweb1.model.jdbc.ConnectionFactory;

public class MensagemDaoImpl implements MensagemDao{
	MongoCollection<Mensagem> collection=null;
	MongoDatabase database = null;
	public MensagemDaoImpl(){
		database = ConnectionFactory.getInstance().getMongoDataBase();

		collection = database.getCollection("mensagens", Mensagem.class);
	}
	
	public void criar(Mensagem m) {
		if(!m.getConteudo().isEmpty() || m.getConteudo() != null) {
			m.setIdConversa(m.getIdDestinatario()+"t"+m.getIdRemetente());
			collection.insertOne(m);
		}
	}

	public List<Mensagem> recuperarMensagens(String idDest, String idRemet) {
		String idConversa1 = idDest+"t"+idRemet;
		String idConversa2 = idRemet+"t"+idDest;
		
		FindIterable<Mensagem> mensagens = collection.find(or(eq("idConversa", idConversa1), eq("idConversa", idConversa2)));
		MongoCursor<Mensagem> todasMensagens = mensagens.iterator();
		
		List<Mensagem> l = new ArrayList<>();
		todasMensagens.forEachRemaining(m->l.add(m));
		
		return l;
	}
}
