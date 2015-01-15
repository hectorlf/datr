package com.hectorlopezfernandez.datr.dao.impl;

import java.util.Iterator;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import com.hectorlopezfernandez.datr.integration.GraphServiceConfigurer;
import com.hectorlopezfernandez.datr.model.NodeTypes;
import com.hectorlopezfernandez.datr.model.RelationTypes;

public abstract class BaseGraphDao {

	private GraphDatabaseService gdbs;

	@Inject
	public void setGraphServiceConfigurer(GraphServiceConfigurer configurer) {
		this.gdbs = configurer.get();
	}

	/* Utility methods */
	
	protected final boolean isARelatedToB(Long aId, Long bId) {
		Node a = gdbs.findNode(NodeTypes.USER, "id", aId);
		if (a == null) return false;
		Node b = gdbs.findNode(NodeTypes.USER, "id", bId);
		if (b == null) return false;

		Iterator<Relationship> aRels = a.getRelationships(RelationTypes.LIKES, Direction.OUTGOING).iterator();
		while (aRels.hasNext()) {
			Relationship r = aRels.next();
			Node n = r.getEndNode();
			if (n.getId() == b.getId()) return true;
		}
		return false;
	}

}
