package com.hectorlopezfernandez.datr.test.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.hectorlopezfernandez.datr.dao.UserDao;
import com.hectorlopezfernandez.datr.test.BaseDaoTest;

public class UserDaoTests extends BaseDaoTest {

	@Autowired
	private UserDao userDao;

	@Test
	public void testFindAllUsers() {
		Assert.notEmpty(userDao.findAllUsers());
	}

	@Test
	public void testGetUserBy() {
		Assert.notNull(userDao.getByUsername("test"));
		Assert.isNull(userDao.getByUsername("nonexistentuser"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalArgumentException1() {
		userDao.getByUsername(null);
	}

}
