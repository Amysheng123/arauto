package com.lombardrisk.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBHelper
{
	private final static Logger logger = LoggerFactory.getLogger(DBHelper.class);
	private String dbms;
	private String dbmsDriver;
	private String host;
	private String ip;
	private String sid;
	private String port;
	private String db;
	private String user = "sa";
	private String password = "password";
	private Connection conn = null;

	protected DBHelper(String dbms, String host, String db)
	{
		this.dbms = dbms;
		fillDbmsDriver(dbms);
		this.host = host;
		fillDbmsPort(dbms);
		this.db = db;
	}

	protected DBHelper(String dbms, String ip, String sid, String db)
	{
		this.dbms = dbms;
		fillDbmsDriver(dbms);
		this.ip = ip;
		this.sid = sid;
		fillDbmsPort(dbms);
		this.db = db;
	}

	protected DBHelper(String dbms, String host, String port, String db, String user, String password)
	{
		this.dbms = dbms;
		fillDbmsDriver(dbms);
		this.host = host;
		this.port = port;
		this.db = db;
		this.user = user;
		this.password = password;
	}

	private void fillDbmsDriver(String dbms)
	{
		if (dbms.equalsIgnoreCase("sqlServer"))
			dbmsDriver = "net.sourceforge.jtds.jdbc.Driver";
		else if (dbms.equalsIgnoreCase("oracle"))
			dbmsDriver = "oracle.jdbc.driver.OracleDriver";

	}

	protected void fillDbmsPort(String dbms)
	{
		if (dbms.equalsIgnoreCase("oracle"))
			port = "1521";
		else if (dbms.equalsIgnoreCase("sqlserver"))
			port = "1433";

	}

	protected void connect()
	{
		if (conn != null)
			return;

		String strConn = null;
		if (dbms.equalsIgnoreCase("oracle"))
			strConn = String.format("jdbc:oracle:thin:@%s:%s:%s", ip, port, sid);
		else if (dbms.equalsIgnoreCase("sqlserver"))
		{
			if (host.contains("\\"))
			{
				host = host.replace("\\", "#");
				strConn = String.format("jdbc:jtds:sqlserver://%s:%s/%s;instance=%s", host.split("#")[0], port, db, host.split("#")[1]);
			}
			else
			{
				strConn = String.format("jdbc:jtds:sqlserver://%s:%s/%s", host, port, db);
			}

		}
		else
		{
			strConn = String.format("jdbc:%s://%s:%s/%s", dbms, host, port, db);
		}

		DbUtils.loadDriver(dbmsDriver);
		try
		{
			if (dbms.equalsIgnoreCase("oracle"))
				conn = DriverManager.getConnection(strConn, db, password);
			else
				conn = DriverManager.getConnection(strConn, user, password);
		}
		catch (SQLException e)
		{
			logger.error("Database connection failed!");
			logger.error("error", e);
		}
	}

	protected void close()
	{
		try
		{
			DbUtils.close(conn);
			conn = null;
		}
		catch (SQLException e)
		{
			logger.error("Database close failed!");
			logger.error("error", e);
		}
	}

	protected String query(String sql) throws SQLException
	{
		if (conn == null)
			return null;
		else
		{
			String value = null;
			Statement stmt = null;
			ResultSet rs = null;
			try
			{
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stmt.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();
				while (rs.next())
				{
					String type = rsmd.getColumnClassName(1).toString();
					if (type.equals("oracle.jdbc.OracleClob"))
						value = rs.getClob(1).getSubString((long) 1, (int) rs.getClob(1).length());
					else if (type.equals("java.math.BigDecimal"))
						value = String.valueOf(rs.getBigDecimal(1));
					else
						value = rs.getString(1);
				}

			}
			catch (SQLException e)
			{
				logger.info("SQLException in [" + sql + "]");
				logger.error("error", e);
			}
			finally
			{
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			}
			return value;
		}
	}

	protected List<List<String>> queryRecord(String sql) throws SQLException
	{
		if (conn == null)
			return null;
		else
		{
			ResultSet rs = null;
			Statement stmt = null;
			List<List<String>> value = new ArrayList<>();
			try
			{
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stmt.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnNum = rsmd.getColumnCount();
				while (rs.next())
				{
					List<String> rowData = new ArrayList<>();
					for (int columnIndex = 1; columnIndex <= columnNum; columnIndex++)
					{
						String cellValue = "";
						String type = rsmd.getColumnClassName(columnIndex).toString();
						if (type.equals("oracle.jdbc.OracleClob"))
							cellValue = rs.getClob(columnIndex).getSubString((long) 1, (int) rs.getClob(columnIndex).length());
						else if (type.equals("java.math.BigDecimal"))
							cellValue = String.valueOf(rs.getBigDecimal(columnIndex));
						else
							cellValue = rs.getString(columnIndex);
						rowData.add(cellValue);
					}
					value.add(rowData);
				}

			}
			catch (SQLException e)
			{
				logger.info("SQLException in [" + sql + "]");
				logger.error("error", e);
			}
			finally
			{
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			}
			return value;
		}
	}

	protected List<String> queryRecords(String sql) throws SQLException
	{
		if (conn == null)
			return null;
		else
		{
			ArrayList<String> rst = new ArrayList<>();
			ResultSet rs = null;
			Statement stmt = null;
			try
			{
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stmt.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();
				while (rs.next())
				{
					String type = rsmd.getColumnClassName(1).toString();
					if (type.equals("oracle.jdbc.OracleClob"))
						rst.add(rs.getClob(1).getSubString((long) 1, (int) rs.getClob(1).length()));
					else if (type.equals("java.math.BigDecimal"))
						rst.add(String.valueOf(rs.getBigDecimal(1)));
					else
						rst.add(rs.getString(1));
				}
			}
			catch (SQLException e)
			{
				logger.info("SQLException in [" + sql + "]");
				logger.error("error", e);
			}
			finally
			{
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			}
			return rst;
		}
	}

	protected int update(String sql)
	{
		if (conn == null)
			return 0;
		else
		{
			QueryRunner run = new QueryRunner();
			int result = 0;

			try
			{
				result = run.update(conn, sql);
			}
			catch (SQLException e)
			{
				logger.info("SQLException in [" + sql + "]");
				logger.error("error", e);
			}
			return result;
		}
	}

	protected void setConn(Connection conn)
	{
		this.conn = conn;
	}

	@Override
	protected void finalize() throws Throwable
	{
		// TODO Auto-generated method stub
		close();
		super.finalize();
	}
}
