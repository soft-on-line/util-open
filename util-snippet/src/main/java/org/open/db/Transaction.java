package org.open.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Transaction 
{
	//写日志.
	private static final Log log = LogFactory.getLog(Transaction.class);
	
	private Connection conn;
	
	public Transaction(Connection conn){
		try {
			this.conn = conn;
			this.conn.setAutoCommit(false);
		} catch (SQLException e) {
			log.error("Transaction() error!", e);
		}   
	}
	
	public void close()
	{
		try {
			conn.close();
		} catch (SQLException e) {
			log.error("Transaction close() error!", e);
		}
	}
	
	public void commit()
	{
		try {
			conn.commit();
		} catch (SQLException e) {
			log.error("Transaction commit() error!", e);
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("Transaction putPreSql(...) transction conn close error!", e);
				}
			}
		}
	}
	
	public void rollback()
	{
		try {
			conn.rollback();
		} catch (SQLException e) {
			log.error("Transaction rollback() error!", e);
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("Transaction putPreSql(...) transction conn close error!", e);
				}
			}
		}
	}
	
	public void prepareExecuteUpdate(String sql,String[] parameters) throws Exception
	{
		PreparedStatement ps = null;
		try{   
			ps = conn.prepareStatement(sql);
			for(int j=1;j<=parameters.length;j++)
			{
				ps.setString(j, parameters[j-1]);
			}
			ps.executeUpdate();
		}finally{
			if(ps != null){
				ps.close();
			}
		}
	}
	
	public Boolean prepareExecuteQueryCountHave(String sql,String[] parameters) throws Exception
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{   
			ps = conn.prepareStatement(sql);
			for(int j=1;j<=parameters.length;j++)
			{
				ps.setString(j, parameters[j-1]);
			}
			
			rs = ps.executeQuery();
			if(rs.next()){
				return ((Integer.valueOf(rs.getString(1))) > 0);
			}
			
			return null;
		}finally{
			if(rs != null){
				rs.close();
			}
			
			if(ps != null){
				ps.close();
			}
		}
	}
}
