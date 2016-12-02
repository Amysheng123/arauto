package com.lombardrisk.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Create by Leo Tu on Sep 18, 2015
 */
public class DateTime
{
	public static String getCurrentDateTime()
	{
		SimpleDateFormat dfs = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		return dfs.format(now);
	}

	public static long getDiffTwoDate(String date1, String date2)
	{
		long between = 0;
		try
		{
			SimpleDateFormat dfs = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

			java.util.Date begin = dfs.parse(date1);

			java.util.Date end = dfs.parse(date2);

			between = (Math.abs(end.getTime() - begin.getTime())) / 1000;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return between;
	}

	public static long getDiffTwoDate(String date1, String date2, String flag)
	{
		long between = 0;
		int i = 1;
		if (flag.equals("m"))
			i = 60;
		else if (flag.equals("H"))
			i = 60 * 60;
		else if (flag.equals("d"))
			i = 60 * 60 * 24;

		try
		{
			SimpleDateFormat dfs = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

			java.util.Date begin = dfs.parse(date1);

			java.util.Date end = dfs.parse(date2);

			between = (Math.abs(end.getTime() - begin.getTime())) / (1000 * i);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return between;
	}

}
