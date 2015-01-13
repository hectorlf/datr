package com.hectorlopezfernandez.datr.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hectorlopezfernandez.datr.dao.AuthDao;
import com.hectorlopezfernandez.datr.dao.GraphDao;
import com.hectorlopezfernandez.datr.dao.MetadataDao;
import com.hectorlopezfernandez.datr.dao.UserDao;
import com.hectorlopezfernandez.datr.dao.impl.AuthDaoImpl;
import com.hectorlopezfernandez.datr.dao.impl.GraphDaoImpl;
import com.hectorlopezfernandez.datr.dao.impl.MetadataDaoImpl;
import com.hectorlopezfernandez.datr.dao.impl.UserDaoImpl;
import com.hectorlopezfernandez.datr.service.AuthService;
import com.hectorlopezfernandez.datr.service.UserService;
import com.hectorlopezfernandez.datr.service.impl.AuthServiceImpl;
import com.hectorlopezfernandez.datr.service.impl.UserServiceImpl;

@Configuration
@EnableTransactionManagement
public class AppConfiguration {

	private static String[] messageSourceBasenames = { "applicationResources" };

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
		ms.setBasenames(messageSourceBasenames);
		ms.setFallbackToSystemLocale(false);
		return ms;
	}

	// neo4j
	
	@Bean
	public GraphServiceConfigurer graphServiceConfigurer() {
		return new GraphServiceConfigurer();
	}


	// dao layer

	@Bean
	public MetadataDao metadataDao() {
		return new MetadataDaoImpl();
	}

	@Bean
	public AuthDao authDao() {
		return new AuthDaoImpl();
	}

	@Bean
	public UserDao userDao() {
		return new UserDaoImpl();
	}
	
	@Bean
	public GraphDao graphDao() {
		return new GraphDaoImpl();
	}

	// service layer
	
	@Bean
	public AuthService authService() {
		return new AuthServiceImpl(authDao());
	}

	@Bean
	public UserService userService() {
		return new UserServiceImpl(userDao());
	}

}
