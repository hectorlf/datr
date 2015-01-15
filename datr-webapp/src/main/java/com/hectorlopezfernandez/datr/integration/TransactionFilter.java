package com.hectorlopezfernandez.datr.integration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.web.context.ContextLoaderListener;

public class TransactionFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(TransactionFilter.class);

	private static final String TRANSACTION_FILTER_MARKER_ATTRIBUTE = "requestContextTransactionMarker";
	
    public TransactionFilter() {
    }

	public void init(FilterConfig fConfig) throws ServletException {
		logger.debug("Initializing TransactionFilter...");
	}
	public void destroy() {
		logger.debug("Destroying TransactionFilter...");
	}


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request.getAttribute(TRANSACTION_FILTER_MARKER_ATTRIBUTE) != null) {
			doRecurringFilter(request, response, chain);
		} else {
			doFirstTimeFilter(request, response, chain);
		}
	}

	private void doFirstTimeFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setAttribute(TRANSACTION_FILTER_MARKER_ATTRIBUTE, TRANSACTION_FILTER_MARKER_ATTRIBUTE);
		// JPA transactionmanager
		ApplicationContext ctx = ContextLoaderListener.getCurrentWebApplicationContext();
		PlatformTransactionManager txManager = (PlatformTransactionManager)ctx.getBean("transactionManager");
		TransactionAttribute def = new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus tx = null;
		// neo4j graphdatabaseservice
		GraphDatabaseService graphDb = ((GraphServiceConfigurer)ctx.getBean("graphServiceConfigurer")).get();
		Transaction n4jTx = null;
		// start transactions
		try {
			tx = txManager.getTransaction(def);
			n4jTx = graphDb.beginTx();
		} catch(Exception e) {
			logger.error("FATAL: not able to start the transactions to service this request. {} - {}", e.getClass().getSimpleName(), e.getMessage());
			try { if (tx != null) { tx.setRollbackOnly(); txManager.rollback(tx); } } catch(Exception nte) {}
			if (n4jTx != null) n4jTx.close(); // neo4j tx.close() never throws exceptions
			throw new ServletException(e);
		}
		try {
			// go on with the request
			chain.doFilter(request, response);
			// try to flush jpa so it triggers possible errors early
			// neo4j can't be flushed, it throws exceptions as soon as they happen
			tx.flush();
		} catch(Exception e) {
			logger.error("Exception occurred servicing this request, rolling back the current transactions: {} - {}", e.getClass().getSimpleName(), e.getMessage());
			tx.setRollbackOnly();
			n4jTx.failure();
			throw new ServletException(e);
		} finally {
			// this should not happen
			if (tx.isCompleted()) {
				logger.warn("JPA tx was completed before this filter, this should NOT happen");
				if (tx.isRollbackOnly()) n4jTx.failure();
				else n4jTx.success();
				n4jTx.close(); // neo4j tx.close() never throws exceptions
			} else {
				// commit
				try {
					if (!tx.isRollbackOnly()) {
						txManager.commit(tx);
						n4jTx.success();
					} else {
						txManager.rollback(tx);
						n4jTx.failure();
					}
					n4jTx.close();
				} catch(Exception e) {
					// not much we can do here
					logger.error("Exception occurred committing or rolling back the current transactions: {} - {}", e.getClass().getSimpleName(), e.getMessage());
					try { txManager.rollback(tx); } catch(Exception nte) {}
					n4jTx.failure(); n4jTx.close(); // neo4j tx.close() never throws exceptions
					throw new ServletException(e);
				}
			}
		}
	}
	private void doRecurringFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
	}

}
