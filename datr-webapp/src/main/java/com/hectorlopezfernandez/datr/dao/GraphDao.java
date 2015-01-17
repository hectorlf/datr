package com.hectorlopezfernandez.datr.dao;

import java.util.List;

public interface GraphDao {

	boolean doesUserALikeUserB(Long aId, Long bId);

	boolean doesUsersAAndBLikeEachOther(Long aId, Long bId);

	List<Long> findSimilarUsersTo(Long id, int maxUsers);

}
