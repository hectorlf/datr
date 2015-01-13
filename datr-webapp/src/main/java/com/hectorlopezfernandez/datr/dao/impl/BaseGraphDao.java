package com.hectorlopezfernandez.datr.dao.impl;

import java.util.Iterator;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import com.hectorlopezfernandez.datr.integration.GraphServiceConfigurer;

public abstract class BaseGraphDao {

	private static enum RelTypes implements RelationshipType {
	    LIKES
	}
	private static final Label userLabel = DynamicLabel.label("user");

	private GraphDatabaseService gdbs;

	@Inject
	public void setGraphServiceConfigurer(GraphServiceConfigurer configurer) {
		this.gdbs = configurer.get();
	}

	/* Utility methods */
	
	protected final boolean isARelatedToB(Long aId, Long bId) {
		Node a = gdbs.findNode(userLabel, "id", aId);
		if (a == null) return false;
		Node b = gdbs.findNode(userLabel, "id", bId);
		if (b == null) return false;

		Iterator<Relationship> aRels = a.getRelationships(RelTypes.LIKES, Direction.OUTGOING).iterator();
		while (aRels.hasNext()) {
			Relationship r = aRels.next();
			Node n = r.getEndNode();
			if (n.getId() == b.getId()) return true;
		}
		return false;
	}

}
