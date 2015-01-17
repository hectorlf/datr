package com.hectorlopezfernandez.datr.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hectorlopezfernandez.datr.dao.GraphDao;

@Repository
public class GraphDaoImpl extends BaseGraphDao implements GraphDao {

	@Override
	public boolean doesUserALikeUserB(Long aId, Long bId) {
		return isARelatedToB(aId, bId);
	}

	@Override
	public boolean doesUsersAAndBLikeEachOther(Long aId, Long bId) {
		return isAReflexivelyRelatedToB(aId, bId);
	}

	@Override
	public List<Long> findSimilarUsersTo(Long id, int maxUsers) {
		return findSimilarlyRelatedTo(id, maxUsers);
	}

}
