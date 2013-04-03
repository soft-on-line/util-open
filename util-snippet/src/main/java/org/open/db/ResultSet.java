package org.open.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ResultSet
{
	//写日志.
	private static final Log log = LogFactory.getLog(ResultSet.class);
	
	private java.sql.ResultSet rs = null;
	
	private Connection conn = null;
	
	private Statement stmt = null;
	
	private PreparedStatement ps = null;
	
	public ResultSet(Connection conn,Statement stmt,PreparedStatement ps,java.sql.ResultSet rs) 
	{
		this.conn = conn;
		this.stmt = stmt;
		this.ps = ps;
		this.rs = rs;
	}
	
	public void close()
	{
		try{
			if(rs!=null){
				rs.close();
			}
			
			if(stmt!=null){
				stmt.close();
			}
			
			if(ps!=null){
				ps.close();
			}
			
			if(conn!=null){
				conn.close();
			}
		}catch(Exception e){
			log.error("ResultSet close() error!", e);
		}
	}
	
	public boolean next() throws SQLException{
		return rs.next();
	}
	
	public void beforeFirst() throws SQLException {
		rs.beforeFirst();
	}
	
	public void previous() throws SQLException {
		rs.previous();
	}
	
	public void afterLast() throws SQLException {
		rs.afterLast();
	}
	
	public int getRow() throws SQLException {
		return rs.getRow();
	}
	
	public boolean absolute(int row) throws SQLException{
		return rs.absolute(row);
	}
	
	public String getString(String col) throws SQLException{
		return rs.getString(col);
	}
	
	public String getString(int col) throws SQLException{
		return rs.getString(col);
	}
	
}
