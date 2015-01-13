package com.hectorlopezfernandez.datr.integration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GraphServiceConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(GraphServiceConfigurer.class);
	
	private GraphDatabaseService gdbs;
	
	public GraphDatabaseService get() {
		return gdbs;
	}

	@PostConstruct
	public void init() {
		logger.debug("Initializing Neo4j...");
		try {
			gdbs = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(Constants.NEO4J_DB_PATH).newGraphDatabase();
			if (gdbs == null) throw new IllegalStateException("Couldnt create a GraphDatabaseService, result was null");
		} catch(Exception e) {
			logger.error("Exception initializing Neo4j: {} - {}", e.getClass().getSimpleName(), e.getMessage());
			throw new IllegalStateException(e);
		}
	}

	@PreDestroy
	public void shutdown() {
		logger.debug("Shutting down Neo4j...");
		if (gdbs != null) gdbs.shutdown();
	}

}
