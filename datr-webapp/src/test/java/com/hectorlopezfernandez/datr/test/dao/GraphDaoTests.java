package com.hectorlopezfernandez.datr.test.dao;

import org.junit.Test;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.hectorlopezfernandez.datr.dao.GraphDao;
import com.hectorlopezfernandez.datr.integration.GraphServiceConfigurer;
import com.hectorlopezfernandez.datr.test.BaseGraphDaoTest;

public class GraphDaoTests extends BaseGraphDaoTest {

	@Autowired
	private GraphDao graphDao;
	@Autowired
	private GraphServiceConfigurer graphServiceConfigurer;

	@Test
	public void testLiker() {
		Transaction tx = graphServiceConfigurer.get().beginTx();
		Assert.isTrue(!graphDao.doesUserALikeUserB(Long.MAX_VALUE, Long.MIN_VALUE));
		tx.failure();
		tx.close();
	}

}
