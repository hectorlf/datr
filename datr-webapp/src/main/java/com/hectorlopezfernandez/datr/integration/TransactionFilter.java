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
		// neo4j transactionmanager and threadlocal graphservice setup
		GraphDatabaseService graphDb = (GraphDatabaseService)ctx.getBean("graphDatabaseService");
		Transaction n4jTx = null;
		// start transactions
		try {
			tx = txManager.getTransaction(def);
			n4jTx = graphDb.beginTx();
		} catch(Exception e) {
			logger.error("FATAL: not able to start the transactions to service this request. {} - {}", e.getClass().getSimpleName(), e.getMessage());
			try { if (tx != null) tx.setRollbackOnly(); txManager.rollback(tx); } catch(Exception nte) {}
			try { if (n4jTx != null) n4jTx.failure(); n4jTx.close(); } catch(Exception nte) {}
			throw new ServletException(e);
		}
		try {
			chain.doFilter(request, response);
		} catch(Exception e) {
			logger.error("Exception occurred servicing this request, rolling back the current transactions...");
			tx.setRollbackOnly();
			n4jTx.failure();
			throw new ServletException(e);
		} finally {
			try {
				if (!tx.isCompleted() && !tx.isRollbackOnly()) { tx.flush(); }
				n4jTx.success();
			} catch(Exception e) {
				logger.error("Exception occurred flushing the current transactions: {} - {}", e.getClass().getSimpleName(), e.getMessage());
				tx.setRollbackOnly();
				n4jTx.failure();
				throw new ServletException(e);
			} finally {
				if (!tx.isCompleted() && tx.isRollbackOnly()) { try { txManager.rollback(tx); } catch(Exception e) {} try { n4jTx.close(); } catch(Exception e) {} }
				else if (!tx.isCompleted() && !tx.isRollbackOnly()) {
					// transactions should not fail here, but...
					try { txManager.commit(tx); n4jTx.close(); } catch(Exception e) {
						logger.error("Exception occurred committing transactions, rolling back: {} - {}", e.getClass().getSimpleName(), e.getMessage());
						try { txManager.rollback(tx); } catch(Exception nte) {}
						try { n4jTx.failure(); n4jTx.close(); } catch(Exception nte) {}
					}
				} else { try { n4jTx.close(); } catch(Exception e) { logger.error("Exception occurred closing neo4j tx while jpa tx was already completed: {} - {}", e.getClass().getSimpleName(), e.getMessage()); } }
			}
		}
	}
	private void doRecurringFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
	}

}
