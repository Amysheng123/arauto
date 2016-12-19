package com.lombardrisk.utils.fileService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtil
{
	private final static Logger logger = LoggerFactory.getLogger(XMLUtil.class);

	public static String getcellValueFromVanilla(String xmlFile, String specElementName, String specAttributeName) throws DocumentException
	{
		String value = null;
		File txtFile = new File("c:/tmp/QueryFromXML.txt");
		String rowKey = null;
		try
		{
			if (specAttributeName != null)
			{
				if (!specAttributeName.endsWith(","))
					specAttributeName = specAttributeName + ",";
				if (specAttributeName.split(",").length == 3)
				{
					rowKey = specAttributeName.split(",")[2];
				}
			}

			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Element root = document.getRootElement();
			getNodes(root, specElementName, specAttributeName);
			if (rowKey == null)
			{
				value = FileUtils.readLines(txtFile).get(0);
			}
			else
			{
				value = FileUtils.readLines(txtFile).get(Integer.parseInt(rowKey) - 1);
			}
		}
		catch (Exception e)
		{
			logger.info("error", e);
			// e.printStackTrace();
		}
		finally
		{
			if (txtFile.exists())
				txtFile.delete();
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public static void getNodes(Element node, String elementName, String attributeName) throws IOException
	{
		File txtFileDir = new File("c:/tmp");
		if (!txtFileDir.exists())
			txtFileDir.mkdir();
		File txtFile = new File("c:/tmp/QueryFromXML.txt");
		if (txtFile.exists())
			txtFile.delete();
		FileWriter fileWriter = new FileWriter("c:/tmp/QueryFromXML.txt", true);
		String cellName = null, rowKey = null, instance = null, result;
		try
		{
			if (attributeName != null)
			{
				cellName = attributeName.split(",")[0];
				if (attributeName.split(",").length == 2)
				{
					instance = attributeName.split(",")[1];
				}

				if (attributeName.split(",").length == 3)
				{
					instance = attributeName.split(",")[1];
					rowKey = attributeName.split(",")[2];
				}
			}
		}
		catch (Exception e)
		{
			// e.printStackTrace();
			logger.info("error", e);
		}

		if (attributeName != null)
		{
			if (rowKey != null)
			{
				elementName = "tableCell";
			}
			else
			{
				elementName = "item";
			}
		}
		if (node.getName().equals(elementName))
		{
			if (instance != null)
			{
				List<Attribute> listAttr = node.attributes();
				if (rowKey == null)
				{
					if (listAttr.get(0).getName().equalsIgnoreCase("itemCode"))
					{
						if (listAttr.get(0).getValue().equalsIgnoreCase(cellName) && listAttr.get(5).getValue().equalsIgnoreCase(instance))
						{
							result = node.getData().toString();
							fileWriter.write(result + "\r\n");
							fileWriter.flush();
							fileWriter.close();
						}
					}
				}
				else
				{
					if (listAttr.get(0).getName().equalsIgnoreCase("xOrd"))
					{
						if (listAttr.get(0).getValue().equalsIgnoreCase(cellName) && listAttr.get(6).getValue().equalsIgnoreCase(instance))
						{
							result = node.getData().toString();
							fileWriter.write(result + "\r\n");
							fileWriter.flush();
							fileWriter.close();
						}
					}
				}
			}
			else
			{
				result = node.getData().toString();
				fileWriter.write(result + "\r\n");
				fileWriter.flush();
				fileWriter.close();
			}
		}

		List<Element> listElement = node.elements();
		for (Element e : listElement)
		{
			getNodes(e, elementName, attributeName);
		}
		fileWriter.close();
	}

	public static String getElementContentFromXML(String file, String nodeName)
	{
		String nodeValue = null;
		try
		{
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(file);

			NodeList elementList = ((org.w3c.dom.Document) doc).getElementsByTagName(nodeName);
			for (int i = 0; i < elementList.getLength(); i++)
			{
				Node elem = elementList.item(i);
				for (Node node = elem.getFirstChild(); node != null; node = node.getNextSibling())
				{

					if (node.getNodeType() == Node.TEXT_NODE)
					{
						nodeValue = node.getTextContent();
					}
					if (node.getNodeType() == Node.ELEMENT_NODE)
					{
						nodeValue = node.getNodeValue();
					}
				}
			}
		}
		catch (Exception e)
		{
			// e.printStackTrace();
		}
		return nodeValue;
	}

	public static List<String> getElements(String xmlFile, String node) throws Exception
	{
		List<String> elements = new ArrayList<String>();
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(xmlFile));
		Element root = document.getRootElement();
		Iterator it = root.elementIterator();
		while (it.hasNext())
		{
			Element element = (Element) it.next();
			if (element.getName().equals(node))
			{
				Iterator eleIt = element.elementIterator();
				while (eleIt.hasNext())
				{
					Element e = (Element) eleIt.next();
					elements.add(e.getName());
				}
				break;
			}
		}
		return elements;
	}

	public static String getElementValueFromXML(String xmlFile, String ChildNode, String elementName) throws DocumentException
	{
		String value = null;
		boolean find = false;
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(xmlFile));
		Element root = document.getRootElement();
		Iterator it = root.elementIterator();
		while (it.hasNext() && !find)
		{
			Element element = (Element) it.next();
			if (element.getName().equals(ChildNode))
			{
				Iterator eleIt = element.elementIterator();
				while (eleIt.hasNext())
				{
					Element e = (Element) eleIt.next();
					if (e.getName().equals(elementName))
					{
						value = e.getText();
						find = true;
						break;
					}
				}
				break;
			}
			else
			{
				Iterator it2 = element.elementIterator();
				while (it2.hasNext())
				{

					Element element2 = (Element) it2.next();
					if (element2.getName().equals(ChildNode))
					{
						Iterator eleIt = element2.elementIterator();
						while (eleIt.hasNext())
						{
							Element e = (Element) eleIt.next();
							if (e.getName().equals(elementName))
							{
								value = e.getText();
								find = true;
								break;
							}
						}
						find = true;
						break;
					}
				}
			}
		}
		return value;

	}

	public static String getElementAttributeFromXML(String xmlFile, String ChildNode, String elementName, String attr) throws DocumentException
	{
		String value = null;
		boolean find = false;
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(xmlFile));
		Element root = document.getRootElement();
		Iterator it = root.elementIterator();
		while (it.hasNext() && !find)
		{
			Element element = (Element) it.next();
			if (element.getName().equals(ChildNode))
			{
				Iterator eleIt = element.elementIterator();
				while (eleIt.hasNext())
				{
					Element e = (Element) eleIt.next();
					if (e.getName().equals(elementName))
					{
						List attrList = e.attributes();
						for (int i = 0; i < attrList.size(); i++)
						{
							Attribute item = (Attribute) attrList.get(i);
							if (item.getName().equals(attr))
							{
								value = item.getValue();
								find = true;
								break;
							}
						}
						break;
					}
				}
				break;
			}
			else
			{
				Iterator it2 = element.elementIterator();
				while (it2.hasNext())
				{
					Element element2 = (Element) it2.next();
					if (element2.getName().equals(ChildNode))
					{
						Iterator eleIt = element2.elementIterator();
						while (eleIt.hasNext())
						{
							Element e = (Element) eleIt.next();
							if (e.getName().equals(elementName))
							{
								List attrList = e.attributes();
								for (int i = 0; i < attrList.size(); i++)
								{
									Attribute item = (Attribute) attrList.get(i);
									if (item.getName().equals(attr))
									{
										value = item.getValue();
										find = true;
										break;
									}
								}
								break;
							}
						}
						find = true;
						break;
					}
				}
			}
		}
		return value;
	}

	public static String updateXMLFile(String xmlFile, String node, String text) throws Exception
	{
		SAXReader reader = new SAXReader();
		reader.setEncoding("UTF-8");
		Document doc = reader.read(xmlFile);
		Element rootElement = doc.getRootElement();
		String rootName = rootElement.getName();
		List<Element> eleList = doc.selectNodes(rootName + "/*");
		// List<Element> eleList =
		// doc.selectNodes(rootElement.getName()+"/"+node);
		Iterator<Element> eleIter = eleList.iterator();
		while (eleIter.hasNext())
		{
			Element element = eleIter.next();
			if (element.getName().equalsIgnoreCase(node))
			{
				element.setText(text);
				break;
			}
		}
		// OutputFormat format = OutputFormat.createPrettyPrint();
		// format.setEncoding("UTF-8");
		File xml = new File(xmlFile);
		String path = xml.getPath();
		String newXmlFile = path + "newXml.xml";
		FileOutputStream output = new FileOutputStream(new File(newXmlFile));
		XMLWriter writer = new XMLWriter(output);
		writer.write(doc);
		writer.flush();
		writer.close();
		return newXmlFile;
	}

	public org.dom4j.Document getDocument(String fileName)
	{
		SAXReader sr = new SAXReader();
		org.dom4j.Document doc = null;
		try
		{

			doc = sr.read(new File(fileName));

		}
		catch (Exception e)
		{

			// e.printStackTrace();
		}
		return doc;
	}
}