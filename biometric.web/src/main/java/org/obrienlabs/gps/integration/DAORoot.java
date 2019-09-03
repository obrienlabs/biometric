package org.obrienlabs.gps.integration;

import java.io.Serializable;

public interface DAORoot<T, ID extends Serializable> {//extends Repository<T, ID> {
 	<S extends T> S save(S entity);
 	T findOne(ID primaryKey);
	Iterable<T> findAll();
	Long count();
 	void delete(T entity);
 	Boolean exists(ID primaryKey);	
}
