package org.open.mining;

import java.util.List;

import org.open.db.ResultSet;
import org.open.db.Transaction;

public interface Mining
{
	public Transaction getTransaction();
	
	public boolean update(String sql);
	
	public int update(String sql,String[] parameters);
	
	public ResultSet queryResultSet(String sql,String[] parameters);
	
	public ResultSet queryResultSet(String sql);
	
	public Boolean queryResultSetHave(String sql,String[] parameters);
	
	public <T> List<T> query(String sql,Class<T> clazz);
	
	public <T> List<T> query(String sql,String[] parameters,Class<T> clazz);
	
	public <T> boolean queryHave(String sql,String[] parameters,Class<T> clazz);
	
}
