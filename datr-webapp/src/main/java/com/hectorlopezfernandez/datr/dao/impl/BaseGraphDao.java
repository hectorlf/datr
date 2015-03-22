package com.hectorlopezfernandez.datr.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;

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
		assert aId != null && bId != null;
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

	protected final boolean isAReflexivelyRelatedToB(Long aId, Long bId) {
		assert aId != null && bId != null;
		Node a = gdbs.findNode(NodeTypes.USER, "id", aId);
		if (a == null) return false;
		Node b = gdbs.findNode(NodeTypes.USER, "id", bId);
		if (b == null) return false;

		Iterator<Relationship> aRels = a.getRelationships(RelationTypes.LIKES, Direction.OUTGOING).iterator();
		while (aRels.hasNext()) {
			Relationship r = aRels.next();
			Node n = r.getEndNode();
			if (n.getId() == b.getId()) {
				Iterator<Relationship> bRels = b.getRelationships(RelationTypes.LIKES, Direction.OUTGOING).iterator();
				while (bRels.hasNext()) {
					Relationship r2 = bRels.next();
					Node m = r2.getEndNode();
					if (m.getId() == a.getId()) return true;
				}
				return false;
			}
		}
		return false;
	}

	protected final List<Long> findSimilarlyRelatedTo(Long id, int maxNodes) {
		assert id != null;
		Map<String,Object> params = new HashMap<String,Object>(1);
		params.put("userId", id);
		params.put("maxNodes", maxNodes);
		Result result = gdbs.execute("match (n:user{id:{userId}})<-[r:LIKES]-(m:user)-[r2:LIKES]->(t:user) where t <> n return distinct t, count(t) as num order by num desc limit {maxNodes}", params );
		ResourceIterator<Node> nodes = result.columnAs("t");
		if (!nodes.hasNext()) {
			return Collections.emptyList();
		}
		List<Long> ids = new ArrayList<Long>(maxNodes);
		while (nodes.hasNext()) {
			Node n = nodes.next();
			ids.add((Long)n.getProperty("id"));
		}
		return ids;
	}

}
