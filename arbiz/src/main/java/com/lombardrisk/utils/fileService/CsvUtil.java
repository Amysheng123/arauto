package com.lombardrisk.utils.fileService;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Created by Leo Tu on 6/17/2015.
 */

public class CsvUtil
{
	private final static Logger logger = LoggerFactory.getLogger(CsvUtil.class);
	private final List<String> list = new ArrayList<String>();
	private BufferedReader bufferedreader = null;

	public CsvUtil()
	{

	}

	public CsvUtil(final String filename) throws IOException
	{
		bufferedreader = new BufferedReader(new FileReader(filename));
		String stemp;
		while ((stemp = bufferedreader.readLine()) != null)
		{
			list.add(stemp);
		}
	}

	public static String getCellValueFromCSV(File csvFile, String cellId, String instance, String rowKey)
	{

		String inString = null;
		String cellValue = null;

		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(csvFile));

			while ((inString = reader.readLine()) != null)
			{
				String[] data = inString.replace("\"", "").split(",");
				if (data[0].equals(cellId))
				{
					if (data[1].equals(instance))
					{
						if (rowKey == null || rowKey.equals(""))
						{
							cellValue = data[2];
							break;
						}
						else
						{
							try
							{
								if (data[4].equals(rowKey))
								{
									cellValue = data[2];
									break;
								}
							}
							catch (Exception e)
							{
								continue;
							}
						}
					}
				}
			}
			reader.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return cellValue;
	}

	public static List<String> readFile(File file)
	{
		BufferedReader br = null;
		List<String> list = new ArrayList<String>();
		try
		{
			br = new BufferedReader(new FileReader(file));
			String str = "";
			while ((str = br.readLine()) != null)
			{
				/*
				 * if (str.contains("/") && str.contains(" 0:00")) { for (String
				 * column : str.split(",")) { if ((column.contains("/")) &&
				 * column.contains(" 0:00")) { String init = column; for (String
				 * part : column.split("/")) { if (part.length() == 1) column =
				 * column.replace(part, "0" + part); } column =
				 * column.replace(" 0:00", " 00:00:00"); str = str.replace(init,
				 * column); } }
				 * 
				 * }
				 */
				if (str.length() > 0)
				{
					list.add(str);
				}
			}
			br.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return list;
	}

	public static boolean compareCSV(File file1, File file2)
	{
		boolean rst = true;
		List<String> fileCon1 = readFile(file1);
		List<String> fileCon2 = readFile(file2);
		if (fileCon1.size() != fileCon2.size())
		{
			logger.error("Records amount is different");
			rst = false;
		}
		else
		{
			for (int i = 0; i < fileCon1.size(); i++)
			{
				if (!fileCon1.contains(fileCon2.get(i)))
				{
					rst = false;
					break;
				}
			}
		}
		return rst;
	}

	public static void writeFile(File file, List<String> list)
	{
		BufferedWriter bw = null;
		try
		{
			String line = System.getProperty("line.separator");
			bw = new BufferedWriter(new FileWriter(file));
			for (String str : list)
			{
				bw.write(str);
				bw.write(line);
			}
			bw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void sort(List<String> list, final int column)
	{
		try
		{
			Collections.sort(list, new Comparator<String>() {
				public int compare(String str1, String str2)
				{
					String[] str1s = str1.split(",");
					String[] str2s = str2.split(",");
					return str1s[column - 1].compareTo(str2s[column - 1]);
				}
			});
		}
		catch (Exception e)
		{

		}
	}

	public static void sortCsv(String sourceFile, String destFile, int colID)
	{
		logger.info("Begin sort csv file[" + sourceFile + "] by column " + colID);
		File file1 = new File(sourceFile);
		File file2 = new File(destFile);
		List<String> list = readFile(file1);
		String head = list.get(0);
		int tmp = list.get(0).split(",").length;
		int col = colID;
		if (col < 1 || col > tmp)
		{
			list.clear();
			System.exit(4);
		}
		sort(list, col);
		int key = list.indexOf(head);
		list.remove(key);
		list.add(0, head);
		writeFile(file2, list);

	}

	public static void writeToSpecCell(String csvFile, int col, String text) throws IOException
	{
		File file = new File(csvFile);
		CsvUtil sourceCsv = new CsvUtil(csvFile);
		int rowAmt = sourceCsv.getRowNum();
		int rowID = 0;

		for (int i = 1; i < rowAmt; i++)
		{

			List<String> rowValue = new ArrayList<String>();
			StringBuilder replaceedTxt = new StringBuilder();
			String value = sourceCsv.getRow(i);

			if (value.endsWith("\""))
			{
				value = value.replace("\"", "");

				value = value + ",";
			}
			else if (!value.endsWith(","))
				value = value + ",";

			for (String txt : value.split(","))
			{
				rowValue.add(txt);
			}

			if (rowValue.size() < col)
				rowValue.add(" ");
			if (rowValue.get(col - 1).equals(" "))
			{
				rowValue.remove(col - 1);
				rowValue.add(col - 1, text);
				rowID = i;
				for (int index = 0; index < rowValue.size(); index++)
				{
					if (!rowValue.get(index).equals(" "))
					{
						replaceedTxt.append(rowValue.get(index));
					}
					replaceedTxt.append(",");

				}
				replaceedTxt = replaceedTxt.deleteCharAt(replaceedTxt.length() - 1);
				List<String> list = readFile(file);
				list.remove(rowID);
				list.add(rowID, replaceedTxt.toString());
				CsvUtil.writeFile(file, list);
			}
		}

	}

	public final static void CsvToExcel(String csv, String excel) throws IOException
	{
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Sheet1");

		BufferedReader r = null;

		try
		{
			r = new BufferedReader(new FileReader(csv));
			int i = 0;
			while (true)
			{
				String ln = r.readLine();
				if (ln == null)
					break;

				HSSFRow row = sheet.createRow((short) i++);
				int j = 0;
				for (CSVTokenizer it = new CSVTokenizer(ln); it.hasMoreTokens();)
				{
					String val = it.nextToken();
					HSSFCell cell = row.createCell((short) j++);
					cell.setCellValue(val);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (r != null)
				r.close();
		}

		FileOutputStream fileOut = null;

		try
		{
			fileOut = new FileOutputStream(excel);
			wb.write(fileOut);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (fileOut != null)
				fileOut.close();
		}
	}

	private List<String> getList() throws IOException
	{

		return list;
	}

	private int getRowNum()
	{

		return list.size();
	}

	private int getColNum()
	{
		if (!list.toString().equals("[]"))
		{

			if (list.get(0).toString().contains(","))
			{
				return list.get(0).toString().split(",").length;
			}
			else if (list.get(0).toString().trim().length() != 0)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
		else
		{
			return 0;
		}
	}

	private String getRow(final int index)
	{

		String rowValue = null;
		if (this.list.size() != 0)
		{
			rowValue = (String) list.get(index);
		}

		return rowValue;

	}

	private String getCol(final int index)
	{

		if (this.getColNum() == 0)
		{
			return null;
		}

		StringBuffer scol = new StringBuffer();
		String temp = null;
		final int colnum = this.getColNum();

		if (colnum > 1)
		{
			for (final Iterator<String> it = list.iterator(); it.hasNext();)
			{
				temp = it.next().toString();
				scol = scol.append(temp.split(",")[index] + ",");
			}
		}
		else
		{
			for (final Iterator<String> it = list.iterator(); it.hasNext();)
			{
				temp = it.next().toString();
				scol = scol.append(temp + ",");
			}
		}
		String str = new String(scol.toString());
		str = str.substring(0, str.length() - 1);
		return str;
	}

	private String getString(final int row, final int col)
	{

		String temp = null;
		final int colnum = this.getColNum();
		if (colnum > 1)
		{
			temp = list.get(row).toString().split(",")[col];
		}
		else if (colnum == 1)
		{
			temp = list.get(row).toString();
		}
		else
		{
			temp = null;
		}
		return temp;
	}

	private void CsvClose() throws IOException
	{
		this.bufferedreader.close();
	}
}
