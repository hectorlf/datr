package com.hectorlopezfernandez.datr.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hectorlopezfernandez.datr.dao.GraphDao;
import com.hectorlopezfernandez.datr.dao.impl.GraphDaoImpl;
import com.hectorlopezfernandez.datr.integration.GraphServiceConfigurer;

@Configuration
public class GraphConfig {

	// neo4j
	
	@Bean
	public GraphServiceConfigurer graphDatabaseService() {
		return new GraphServiceConfigurer();
	}

	// dao
	
	@Bean
	public GraphDao graphDao() {
		return new GraphDaoImpl();
	}

}
