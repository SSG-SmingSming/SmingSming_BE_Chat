//package com.smingsming.chat.global.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.EnableMongoAuditing;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
//
//@Configuration
//@EnableMongoAuditing(modifyOnCreate=false)
//public class MongoConfig extends AbstractMongoConfiguration  {
//    @Value("${spring.data.mongodb.database}")
//    private String dbName;
//
//    @Value("${spring.data.mongodb.host}")
//    private String mongoHost;
//
//    @Value("${spring.data.mongodb.port}")
//    private int mongoPort;
//
//    @Value("${spring.data.mongodb.username}")
//    private String username;
//
//    @Value("${spring.data.mongodb.password}")
//    private String password;
//
//    @Override
//    protected String getDatabaseName() {
//        return this.dbName;
//    }
//}
