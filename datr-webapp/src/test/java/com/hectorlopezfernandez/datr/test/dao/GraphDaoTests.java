package com.hectorlopezfernandez.datr.test.dao;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.hectorlopezfernandez.datr.dao.GraphDao;
import com.hectorlopezfernandez.datr.integration.GraphServiceConfigurer;
import com.hectorlopezfernandez.datr.model.NodeTypes;
import com.hectorlopezfernandez.datr.model.RelationTypes;
import com.hectorlopezfernandez.datr.test.BaseGraphDaoTest;

public class GraphDaoTests extends BaseGraphDaoTest {

	@Autowired
	private GraphDao graphDao;
	@Autowired
	private GraphServiceConfigurer graphServiceConfigurer;

	@Test
	public void testNonexistentUsers() {
		GraphDatabaseService gdbs = graphServiceConfigurer.get();
		try (Transaction tx = gdbs.beginTx()) {
			Assert.isTrue(!graphDao.doesUserALikeUserB(Long.MAX_VALUE, Long.MIN_VALUE));
		}
	}

	@Test
	public void testSuccessfulLiker() {
		GraphDatabaseService gdbs = graphServiceConfigurer.get();
		try (Transaction tx = gdbs.beginTx()) {
			Long aId = Long.valueOf(1);
			Long bId = Long.valueOf(2);
			Node a = gdbs.createNode(NodeTypes.USER);
			a.setProperty("id", aId);
			Node b = gdbs.createNode(NodeTypes.USER);
			b.setProperty("id", bId);
			a.createRelationshipTo(b, RelationTypes.LIKES);
			
			Assert.isTrue(graphDao.doesUserALikeUserB(aId, bId));
		}
	}

	@Test
	public void testFailedLiker() {
		GraphDatabaseService gdbs = graphServiceConfigurer.get();
		try (Transaction tx = gdbs.beginTx()) {
			Long aId = Long.valueOf(1);
			Long bId = Long.valueOf(2);
			Node a = gdbs.createNode(NodeTypes.USER);
			a.setProperty("id", aId);
			Node b = gdbs.createNode(NodeTypes.USER);
			b.setProperty("id", bId);
			a.createRelationshipTo(b, RelationTypes.LIKES);
			
			Assert.isTrue(!graphDao.doesUserALikeUserB(bId, aId));
		}
	}

}
