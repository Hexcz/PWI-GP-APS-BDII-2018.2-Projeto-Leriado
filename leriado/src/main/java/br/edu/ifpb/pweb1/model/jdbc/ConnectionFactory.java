package br.edu.ifpb.pweb1.model.jdbc;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import redis.clients.jedis.Jedis;

public class ConnectionFactory {	

	/* POSTGRES*/
	private static String user;
	private static String password;
	private static String url;
	private static String driver;
	private static ConnectionFactory instance = null;
	private static Connection connection = null;
	/*MONGODB*/
	private static String mongoUrl;
	private static String mongoBase;
	private MongoClient mongoClient = null;
	private CodecRegistry pojoCodecRegistry = null;
	/*NEO4J*/
	private static String neo4jUrl;
	private static String neo4jUser;
	private static String neo4jpassword;
	private static Driver driverNeo4j = null;
	/*REDIS*/
	private static String redisUrl;
	private static Long redisTimeout;
	private static Jedis jedis;
	
	static {
		
		try {
			Properties properties = new Properties();
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			properties.load(inputStream);
			inputStream.close();	
			
			/*Carregando dados config.properties*/
			url = properties.getProperty("database.url");
			user = properties.getProperty("database.user");
			password = properties.getProperty("database.password");
			driver = properties.getProperty("database.driver");
			mongoUrl = properties.getProperty("database.mongo.url");
			mongoBase = properties.getProperty("database.mongo.base");
			neo4jUrl = properties.getProperty("database.neo4j.url");
			neo4jUser = properties.getProperty("database.neo4j.user");
			neo4jpassword = properties.getProperty("database.neo4j.password");
			redisUrl = properties.getProperty("database.redis.url");
			redisTimeout = Long.parseLong(properties.getProperty("database.redis.timeout"));
			
			/*Neo4J*/
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);			
			driverNeo4j = GraphDatabase.driver(neo4jUrl,AuthTokens.basic(neo4jUser, neo4jpassword));
			
			/*Redis*/
			
			jedis = new Jedis(redisUrl);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	
	private ConnectionFactory() {
		pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		
		mongoClient = new MongoClient();
	}
	
	public static ConnectionFactory getInstance() {
		if(instance == null) {
			synchronized (ConnectionFactory.class) {
				if(instance == null) {
					instance = new ConnectionFactory();
				}
			}
		}
		return instance;
	}
	
	
	public Connection getConnection(){
		return connection;
	}
	
	public MongoDatabase getMongoDataBase() {
		return  mongoClient.getDatabase(mongoBase)
				.withCodecRegistry(pojoCodecRegistry);
	}
	
	public Driver getDriveNeo4J() {
		return driverNeo4j;		
	}
	
	public Jedis getRedis() {
		return jedis;
	}
	
	public Long getRedisTimeout() {
		return redisTimeout;
	}

}
