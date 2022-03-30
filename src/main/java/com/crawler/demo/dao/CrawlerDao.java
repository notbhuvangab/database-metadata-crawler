package com.crawler.demo.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.crawler.demo.model.ConnectionEstablish;
import com.crawler.demo.model.Profiler;

@Repository
public class CrawlerDao {

	public List<String> getSchemas(ConnectionEstablish c) {
		String url = "jdbc:mysql://" + c.getHostId() + "/";
		String user = c.getUser();
		String pwd = c.getPassword();

		Connection con = prepareConn(url, user, pwd);

		String sql = "show schemas;";
		try {
			Statement st = con.createStatement();
			ResultSet schemas = st.executeQuery(sql);

			List<String> schemaList = new ArrayList<String>();
			while (schemas.next()) {
				schemaList.add(schemas.getString("Database"));
			}
			return schemaList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Connection prepareConn(String url, String user, String pwd) {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, user, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public List<String> getTables(String schema, ConnectionEstablish c) {
		String url = "jdbc:mysql://" + c.getHostId() + "/" + schema;
		String user = c.getUser();
		String pwd = c.getPassword();

		Connection con = prepareConn(url, user, pwd);
		try {

			DatabaseMetaData databaseMetaData = con.getMetaData();
			ResultSet tables = databaseMetaData.getTables(schema, null, null, new String[] { "TABLE" });

			List<String> tableList = new ArrayList<String>();
			while (tables.next()) {
				tableList.add(tables.getString("TABLE_NAME"));
			}
			return tableList;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public List<String> getColumns(String schema, String table, ConnectionEstablish c) {

		String url = "jdbc:mysql://" + c.getHostId() + "/" + schema;
		String user = c.getUser();
		String pwd = c.getPassword();

		Connection con = prepareConn(url, user, pwd);

		try {
			DatabaseMetaData databaseMetaData = con.getMetaData();
			ResultSet columns = databaseMetaData.getColumns(schema, null, table, null);

			List<String> columnList = new ArrayList<String>();
			while (columns.next()) {
				columnList.add(columns.getString("COLUMN_NAME"));
			}
			return columnList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, Profiler> getProfiler(String schema, String table, ConnectionEstablish c, int limit) {

		String url = "jdbc:mysql://" + c.getHostId() + "/" + schema;
		String user = c.getUser();
		String pwd = c.getPassword();

		List<String> columns = getColumns(schema, table, c);
		Map<String, Profiler> tableProfile = new LinkedHashMap<String, Profiler>();

		String sqlRowCount;
		String sqlNullCount;
		String sqlDistinctCount;
		String sqlMin;
		String sqlMax;
		
		Connection con = prepareConn(url, user, pwd);
		
		for (String str : columns) {
			try {
				
				if (limit < 0) {
					sqlRowCount = "select count("+str+") as 'row_count' from " + table;
					sqlNullCount = "select sum(case when "+str+" is null then 1 else 0 end) as 'null_count' from " + table;
					sqlDistinctCount = "select count(distinct "+str+") as 'distinct_count' from " + table ;
					sqlMin = "select min("+str+") as 'minimum_value' from " + table;
					sqlMax = "select max("+str+") as 'maximum_value' from " + table;
				} else {
					sqlRowCount = "select count("+str+") as 'row_count' from (select * from " + table + " limit ?) temp";
					sqlNullCount = "select count(case when "+str+" is null then 1 else 0 end) as 'null_count' from (select * from "+table+" limit ?) temp  ";
					sqlDistinctCount = "select count(distinct "+str+") as 'distinct_count' from (select * from "+table+" limit ?) temp ";
					sqlMin = "select min("+str+") as 'minimum_value' from (select * from "+table+" limit ?) temp";
					sqlMax = "select max("+str+") as 'maximum_value' from (select * from "+table+" limit ?) temp";
				}
				PreparedStatement ps = con.prepareStatement(sqlRowCount);
				if(limit >= 0) {
					ps.setInt(1, limit);
				}
				ResultSet rs1 = ps.executeQuery();
				rs1.next();
				ps = con.prepareStatement(sqlNullCount);
				if(limit >= 0) {
					ps.setInt(1, limit);
				}
				ResultSet rs2 = ps.executeQuery();
				rs2.next();
				ps = con.prepareStatement(sqlDistinctCount);
				if(limit >= 0) {
					ps.setInt(1, limit);
				}
				ResultSet rs3 = ps.executeQuery();
				rs3.next();
				ps = con.prepareStatement(sqlMin);
				if(limit >= 0) {
					ps.setInt(1, limit);
				}
				ResultSet rs4 = ps.executeQuery();
				rs4.next();
				ps = con.prepareStatement(sqlMax);
				if(limit >= 0) {
					ps.setInt(1, limit);
				}
				ResultSet rs5 = ps.executeQuery();
				rs5.next();

				tableProfile.put(str, new Profiler(rs1.getString(1), rs2.getString(1), rs3.getString(1),
						rs4.getString(1), rs5.getString(1)));

				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return tableProfile;
	}
}
