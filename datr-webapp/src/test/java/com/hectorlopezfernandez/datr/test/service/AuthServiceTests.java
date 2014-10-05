package com.hectorlopezfernandez.datr.test.service;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

import com.hectorlopezfernandez.datr.dao.AuthDao;
import com.hectorlopezfernandez.datr.model.auth.Principal;
import com.hectorlopezfernandez.datr.service.AuthService;
import com.hectorlopezfernandez.datr.service.impl.AuthServiceImpl;
import com.hectorlopezfernandez.datr.test.BaseSpringTest;

@ContextConfiguration
public class AuthServiceTests extends BaseSpringTest {

	@Configuration
	static class Config {

		@Bean
		public AuthDao authDao() {
			Principal stubUser = new Principal();
			stubUser.setUsername("test");
			AuthDao mock = mock(AuthDao.class);
			when(mock.loadUserByUsername(eq("test"))).thenReturn(stubUser);
			when(mock.loadUserByUsername(eq("doesnotexist"))).thenThrow(new UsernameNotFoundException("username not found"));
			return mock;
		}

		@Bean
		public AuthService authService() {
			return new AuthServiceImpl(authDao());
		}
		
	}

	@Autowired
	private AuthService authService;

	@Test
	public void testLoadUserByUsername1() {
		Assert.notNull(authService.loadUserByUsername("test"));
	}

	@Test(expected=UsernameNotFoundException.class)
	public void testLoadUserByUsername2() {
		authService.loadUserByUsername("doesnotexist");
	}

}
