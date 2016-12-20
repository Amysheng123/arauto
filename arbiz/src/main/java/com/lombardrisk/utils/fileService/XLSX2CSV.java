package com.lombardrisk.utils.fileService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XLSX2CSV extends DefaultHandler
{
	private final static String targetDataFolder = System.getProperty("user.dir") + "/target/result/data/";
	private final OPCPackage xlsxPackage;
	/**
	 * Number of columns to read starting with leftmost
	 */
	private final int minColumns;

	// /////////////////////////////////////
	/**
	 * Destination for data
	 */
	private final PrintStream output;
	File txtFile_iFile = new File(targetDataFolder + "worksheet.txt");

	/**
	 * Creates a new XLSX -> CSV converter
	 *
	 * @param pkg
	 *            The XLSX package to process
	 * @param output
	 *            The PrintStream to output the CSV to
	 * @param minColumns
	 *            The minimum number of columns to output, or -1 for no minimum
	 */
	public XLSX2CSV(OPCPackage pkg, PrintStream output, int minColumns)
	{
		this.xlsxPackage = pkg;
		this.output = output;
		this.minColumns = minColumns;
	}

	/**
	 * Parses and shows the content of one sheet using the specified styles and
	 * shared-strings tables.
	 *
	 * @param styles
	 * @param strings
	 * @param sheetInputStream
	 */
	public void processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetHandler, InputStream sheetInputStream)
			throws IOException, ParserConfigurationException, SAXException
	{
		DataFormatter formatter = new DataFormatter();
		InputSource sheetSource = new InputSource(sheetInputStream);
		try
		{
			XMLReader sheetParser = SAXHelper.newXMLReader();
			ContentHandler handler = new XSSFSheetXMLHandler(styles, null, strings, sheetHandler, formatter, false);
			sheetParser.setContentHandler(handler);
			sheetParser.parse(sheetSource);
		}
		catch (ParserConfigurationException e)
		{
			throw new RuntimeException("SAX parser appears to be broken - " + e);
		}
	}

	/**
	 * Initiates the processing of the XLS workbook file to CSV.
	 *
	 * @throws IOException
	 * @throws OpenXML4JException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void process(String sheetName) throws IOException, OpenXML4JException, ParserConfigurationException, SAXException
	{
		if (txtFile_iFile.exists())
			txtFile_iFile.delete();
		ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
		XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
		StylesTable styles = xssfReader.getStylesTable();
		XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
		// int index = 0;
		while (iter.hasNext())
		{
			InputStream stream = iter.next();
			String curSheetName = iter.getSheetName();
			if (curSheetName.equals(sheetName))
			{
				// this.output.println();
				// this.output.println(curSheetName + " [index=" + index +
				// "]:");
				processSheet(styles, strings, new SheetToCSV(), stream);
				stream.close();
				break;
			}
			// ++index;
		}
	}

	/**
	 * Uses the XSSF Event SAX helpers to do most of the work of parsing the
	 * Sheet XML, and outputs the contents as a (basic) CSV.
	 */
	private class SheetToCSV implements SheetContentsHandler
	{
		StringBuilder sbr = new StringBuilder("");
		private boolean firstCellOfRow = false;
		private int currentRow = -1;
		private int currentCol = -1;

		private void outputMissingRows(int number)
		{
			for (int i = 0; i < number; i++)
			{
				for (int j = 0; j < minColumns; j++)
				{
					sbr.append('~');
				}
				sbr.append('\n');
			}
		}

		@Override
		public void startRow(int rowNum)
		{
			// If there were gaps, output the missing rows
			outputMissingRows(rowNum - currentRow - 1);
			// Prepare for this row
			firstCellOfRow = true;
			currentRow = rowNum;
			currentCol = -1;
		}

		@Override
		public void endRow(int rowNum)
		{
			// Ensure the minimum number of columns
			for (int i = currentCol; i < minColumns; i++)
			{
				sbr.append('~');
			}
			// sbr.append('\n');
			// System.out.println(sbr);

			try
			{
				TxtUtil.writeToTxt(txtFile_iFile, sbr);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
			sbr = new StringBuilder("");
		}

		@Override
		public void cell(String cellReference, String formattedValue, XSSFComment comment)
		{
			if (firstCellOfRow)
			{
				firstCellOfRow = false;
			}
			else
			{
				sbr.append('~');
			}

			// gracefully handle missing CellRef here in a similar way as
			// XSSFCell does
			if (cellReference == null)
			{
				cellReference = new CellAddress(currentRow, currentCol).formatAsString();
			}

			// Did we miss any cells?
			int thisCol = (new CellReference(cellReference)).getCol();
			int missedCols = thisCol - currentCol - 1;
			for (int i = 0; i < missedCols; i++)
			{
				sbr.append('~');
			}
			currentCol = thisCol;

			// Number or string?
			try
			{
				Double.parseDouble(formattedValue);
				sbr.append(formattedValue);
			}
			catch (NumberFormatException e)
			{
				// sbr.append('"');
				sbr.append(formattedValue);
				// sbr.append('"');
			}
		}

		@Override
		public void headerFooter(String text, boolean isHeader, String tagName)
		{
			// Skip, no headers or footers in CSV
		}
	}
}