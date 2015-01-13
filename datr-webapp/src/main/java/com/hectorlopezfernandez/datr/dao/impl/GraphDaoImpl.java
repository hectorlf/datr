package com.hectorlopezfernandez.datr.dao.impl;

import org.springframework.stereotype.Repository;

import com.hectorlopezfernandez.datr.dao.GraphDao;

@Repository
public class GraphDaoImpl extends BaseGraphDao implements GraphDao {

	@Override
	public boolean doesUserALikeUserB(Long aId, Long bId) {
		return isARelatedToB(aId, bId);
	}

}
