package com.jorge.client;


import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jorge.entity.Guide;
import com.jorge.entity.Student;
import com.jorge.util.HibernateUtil;

/**
 * By default, single point associations (@OneToOne and @ManyToOne) are eagerly fetched. @OneToMany and @ManyToMany are lazy
 * 
 * Lazy means that when there are relationships between different entities, which are dependent on other 
 * non-initialized with their values are stored in a database until they are explicitly accessed (read)
 * 
 * A solution to avoid sending a customer relations not explicitly initialized is defined as non-lazy (or, more correctly, Eager)
 * 
 */
public class Main1 {

	public static void main(String[] args) {
		BasicConfigurator.configure(); // Necessary for configure log4j. It must be the first line in main method
								       // log4j.properties must be in /src directory
		
		Logger  logger = Logger.getLogger(Main1.class.getName());
		logger.debug("log4j configured correctly and logger set");

		logger.debug("getting session factory from HibernateUtil.java");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction txn = session.getTransaction();
		
		try {
			
			logger.debug("beginning persist transaction");
			txn.begin(); 

			logger.debug("getting Guide data");
			Guide guide = (Guide)session.get(Guide.class, 2L);
			
			logger.debug("getting student data from Guide object");
			Set<Student> students = guide.getStudents();
			int numberOfStudents = students.size(); // By default, fetch type is lazy so it is ok (not necessary fetch=FetchType.EAGER in Guide.java class)
			
			logger.debug("making commit");
			txn.commit();
			
		} catch (Exception e) {
			if (txn != null) {
				logger.error("something was wrong, making rollback of transactions");
				txn.rollback(); // If something was wrong, we make rollback
			}
			logger.error("Exception: " + e.getMessage().toString());
		} finally {
			if (session != null) {
				logger.debug("close session");
				session.close();
			}
		}
		
	}										

}
