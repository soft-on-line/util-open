package org.open.mining;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.open.db.Transaction;

public class MysqlMining implements Mining
{
	//写日志.
	private static final Log log = LogFactory.getLog(MysqlMining.class);
	
	public MysqlMining(String user,String passwd)
	{
		new MysqlMining("127.0.0.1",user,passwd);
    }
	
	public MysqlMining(String ip,String user,String passwd)
	{
		try{
	        Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
			Properties info = new Properties();
			info.setProperty("proxool.maximum-connection-count", "50");
			info.setProperty("proxool.minimum-connection-count", "30");
			info.setProperty("proxool.house-keeping-test-sql", "select CURRENT_DATE");
			info.setProperty("user", user);
			info.setProperty("password", passwd);
			info.setProperty("useUnicode", Boolean.TRUE.toString());
			info.setProperty("characterEncoding", "gbk");
			String alias = "mysqlMiningPool";
			String driverClass = "com.mysql.jdbc.Driver";
			String driverUrl = "jdbc:mysql://"+ip+":3306/mining";
			String url = "proxool." + alias + ":" + driverClass + ":" + driverUrl;
			ProxoolFacade.registerConnectionPool(url, info);
		}catch(Exception e){
			log.error("MysqlMining construct error!", e);
		}
    }
	
	public Transaction getTransaction()
	{
		try {
			return new Transaction(getConnection());
		} catch (Exception e) {
			log.error("MysqlMining getTransaction() error!", e);
			return null;
		}
	}
	
	private Connection getConnection() throws ClassNotFoundException, SQLException, ProxoolException
	{
		//return DriverManager.getConnection("proxool.mysqlMiningPool:org.mysqlMining.jdbcDriver:jdbc:mysqlMining:"+dbFile, user, passwd);
		return DriverManager.getConnection("proxool.mysqlMiningPool");
	}
	
	public boolean update(String sql)
	{
		Connection connection = null;
		Statement statement = null;
		try{
			connection = getConnection();
			statement = connection.createStatement();
		    return statement.execute(sql);
		}catch(Exception e){
			log.error("MysqlMining update("+sql+") error!", e);
			return false;
		}finally{
			try {
				if(statement!=null){
					statement.close();
				}
				
				if(connection!=null){
					connection.close();
				}
			} catch (SQLException e) {
				log.error("MysqlMining update("+sql+") error!", e);
			}
		}
	}
	
	public int update(String sql,String[] parameters)
	{
		Connection connection = null;
		PreparedStatement ps = null;
		try{
			connection = getConnection();
			ps = connection.prepareStatement(sql);
			for(int i=1;i<=parameters.length;i++)
			{
				ps.setString(i, parameters[i-1]);
			}
			return ps.executeUpdate();
		}catch(Exception e){
			log.error("MysqlMining query("+sql+") error!", e);
			return -1;
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
				if(connection!=null){
					connection.close();
				}
			} catch (SQLException e) {
				log.error("MysqlMining query("+sql+") error!", e);
			}
		}
	}
	
	public <T> List<T> query(String sql,Class<T> clazz)
	{
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try{
			connection = getConnection();
			statement = connection.createStatement();
	        rs = statement.executeQuery(sql);
	        
	        List<T> list = new ArrayList<T>();
	        while(rs.next())
	        {
	        	T bean = clazz.newInstance();
	        	Method[] methods = clazz.getMethods();
	        	for(int i=0;i<methods.length;i++)
	        	{
	        		String methodName = methods[i].getName();
	        		if(0 == methodName.indexOf("set")){
	        			methods[i].invoke(bean, rs.getObject(methodName.substring(3)));
	        		}
	        	}
	        	
	        	list.add(bean);
	        }
	        return list;
		}catch(Exception e){
			log.error("MysqlMining query("+sql+") error!", e);
			return new ArrayList<T>();
		}finally{
			try {
				if(rs!=null){
					rs.close();
				}
				
				if(statement!=null){
					statement.close();
				}
				
				if(connection!=null){
					connection.close();
				}
			} catch (SQLException e) {
				log.error("MysqlMining query("+sql+") error!", e);
			}
		}
	}
	
	public org.open.db.ResultSet queryResultSet(String sql,String[] parameters)
	{
		try{
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			
			for(int i=1;i<=parameters.length;i++)
			{
				ps.setString(i, parameters[i-1]);
			}
			
			ResultSet rs = ps.executeQuery();
					
			return new org.open.db.ResultSet(conn,null,ps,rs);
		}catch(Exception e){
			log.error("MysqlMining query("+sql+") error!", e);
			return null;
		}
	}
	
	public org.open.db.ResultSet queryResultSet(String sql)
	{
		try{
			Connection conn = getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
					
			return new org.open.db.ResultSet(conn,stmt,null,rs);
		}catch(Exception e){
			log.error("MysqlMining query("+sql+") error!", e);
			return null;
		}
	}
	
	public <T> List<T> query(String sql,String[] parameters,Class<T> clazz)
	{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			connection = getConnection();
			ps = connection.prepareStatement(sql);
			for(int i=1;i<=parameters.length;i++)
			{
				ps.setString(i, parameters[i-1]);
			}
			
			List<T> list = new ArrayList<T>();
			rs = ps.executeQuery();
			while(rs.next())
			{
				T bean = clazz.newInstance();
	        	Method[] methods = clazz.getMethods();
	        	for(int i=0;i<methods.length;i++)
	        	{
	        		String methodName = methods[i].getName();
	        		if(0 == methodName.indexOf("set")){
	        			methods[i].invoke(bean, rs.getObject(methodName.substring(3)));
	        		}
	        	}
				
				list.add(bean);
			}
			return list;
		}catch(Exception e){
			log.error("MysqlMining query("+sql+") error!", e);
			return new ArrayList<T>();
		}finally{
			try {
				if(rs!=null){
					rs.close();
				}
				
				if(ps!=null){
					ps.close();
				}
				
				if(connection!=null){
					connection.close();
				}
			} catch (SQLException e) {
				log.error("MysqlMining query("+sql+") error!", e);
			}
		}
	}
	
	public <T> boolean queryHave(String sql,String[] parameters,Class<T> clazz)
	{
		return !query(sql,parameters,clazz).isEmpty();
	}
	
	public Boolean queryResultSetHave(String sql,String[] parameters)
	{
		org.open.db.ResultSet rs = null;
		try {
			rs = queryResultSet(sql,parameters);
			
			rs.next();
			
			return Integer.valueOf(rs.getString(1)) > 0;
		} catch (Exception e) {
			log.error("MysqlMining queryResultSetHave(...) error!", e);
			return null;
		}finally{
			if(rs!=null){
				rs.close();
			}
		}
	}
	
}
