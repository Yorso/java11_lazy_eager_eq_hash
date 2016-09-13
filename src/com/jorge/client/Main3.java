package com.jorge.client;


import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
public class Main3 {

	public static void main(String[] args) {
		BasicConfigurator.configure(); // Necessary for configure log4j. It must be the first line in main method
								       // log4j.properties must be in /src directory
		
		Logger  logger = Logger.getLogger(Main3.class.getName());
		logger.debug("log4j configured correctly and logger set");

		logger.debug("getting session factory from HibernateUtil.java");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction txn = session.getTransaction();
		
		try {
			
			logger.debug("beginning persist transaction");
			txn.begin(); 

			logger.debug("getting Student data");
			Student student = (Student)session.get(Student.class, 2L); // It loads the data for the guide object associated with it because (left outer join with student and guide tables)
																       // by default, single point associations (@OneToOne and @ManyToOne) are eagerly fetched
			
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
