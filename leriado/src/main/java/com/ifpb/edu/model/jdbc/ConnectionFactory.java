package com.ifpb.edu.model.jdbc;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.Arrays;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
	private static String user;
	private static String password;
	private static String url;
	private static String driver;
	private static String mongoUrl;
	private static String mongoBase;
	private static ConnectionFactory instance = null;
	private static Connection connection = null;
	
	private MongoClient mongoClient = null;
	private CodecRegistry pojoCodecRegistry = null;
	
	
	

	
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
	
	static {
		Properties properties = new Properties();
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
		try {
			properties.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		url = properties.getProperty("database.url");
		user = properties.getProperty("database.user");
		password = properties.getProperty("database.password");
		driver = properties.getProperty("database.driver");
		mongoUrl = properties.getProperty("database.mongo.url");
		mongoBase = properties.getProperty("database.mongo.base");
				
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection(){
		return connection;
	}
	
	public MongoDatabase getMongoDataBase() {
		return  mongoClient.getDatabase(mongoBase)
				.withCodecRegistry(pojoCodecRegistry);
	}

}
