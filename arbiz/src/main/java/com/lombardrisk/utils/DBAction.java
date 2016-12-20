package com.lombardrisk.utils;

import java.util.List;

/**
 * Created by leo tu on 12/7/2016.
 */
public class DBAction extends TestTemplate
{

	private static DBHelper getDBHelpInstance(String DBType, String Server, String DB) throws Exception
	{
		DBHelper dh;
		if (DBType.equalsIgnoreCase("oracle"))
		{
			String ip = Server.split("@")[0];
			String sid = Server.split("@")[1];
			dh = new DBHelper("oracle", ip, sid, DB);
		}
		else
			dh = new DBHelper("sqlServer", Server, DB);

		return dh;
	}

	public static List<List<String>> queryRecord(String DBType, String Server, String DB, String SQL) throws Exception
	{
		DBHelper dh = getDBHelpInstance(DBType, Server, DB);
		dh.connect();
		List<List<String>> rst = dh.queryRecord(SQL);
		dh.close();
		return rst;
	}
}
