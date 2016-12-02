package com.lombardrisk.utils.fileService;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.Iterator;
import java.util.List;

public class XMLUtil {
    private final static Logger logger = LoggerFactory.getLogger(XMLUtil.class);
    long starttime;
    long endtime;

    public static String getcellValueFromVanilla(String xmlFile, String specElenentName, String specAttributeName) throws DocumentException {
        String value = null;
        File txtFile = new File("c:\\tmp\\QueryFromXML.txt");
        try {
            if (specAttributeName != null) {
                if (!specAttributeName.endsWith(","))
                    specAttributeName = specAttributeName + ",";
            }

            if (txtFile.exists())
                txtFile.delete();

            SAXReader reader = new SAXReader();
            Document document = reader.read(xmlFile);
            Element root = document.getRootElement();
            getNodes(root, specElenentName, specAttributeName);
            if (txtFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(txtFile));
                value = br.readLine();
                br.close();
            }


        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (txtFile.exists())
                txtFile.delete();
        }

        return value;
    }


    @SuppressWarnings("unchecked")
    public static void getNodes(Element node, String elementName, String attributeName) throws IOException {
    	File txtFileDir = new File("c:\\tmp");
        if(!txtFileDir.exists())
        	txtFileDir.mkdir();
        FileWriter fileWriter = new FileWriter("c:\\tmp\\QueryFromXML.txt", true);

        String cellName = null;
        String instance = null;
        String rowKey = null;
        try {
            if (attributeName != null) {
                cellName = attributeName.split(",")[0];
            }

            if (attributeName.split(",").length == 2) {
                instance = attributeName.split(",")[1];
            }


            if (attributeName.split(",").length == 3) {
                instance = attributeName.split(",")[1];
                rowKey = attributeName.split(",")[2];
            }
        } catch (Exception e) {
        }

        if (attributeName != null) {
            if (rowKey != null) {
                elementName = "tableCell";
            } else {
                elementName = "item";
            }
        }

        if (node.getName().equals(elementName)) {
            if (instance != null) {
                List<Attribute> listAttr = node.attributes();
                if (rowKey == null) {
                    if (listAttr.get(0).getName().equalsIgnoreCase("itemCode")) {
                        if (listAttr.get(0).getValue().equalsIgnoreCase(cellName) && listAttr.get(5).getValue().equalsIgnoreCase(instance)) {
                            String result = node.getData().toString();
                            fileWriter.write(result);
                            fileWriter.flush();
                            fileWriter.close();

                        }
                    }
                } else {
                    if (listAttr.get(0).getName().equalsIgnoreCase("xOrd")) {
                        if (listAttr.get(0).getValue().equalsIgnoreCase(cellName) && listAttr.get(1).getValue().equalsIgnoreCase(rowKey) && listAttr.get(6).getValue().equalsIgnoreCase(instance)) {
                            String result = node.getData().toString();
                            fileWriter.write(result);
                            fileWriter.flush();
                            fileWriter.close();

                        }
                    }
                }
            } else {
                String result = node.getData().toString();
                fileWriter.write(result);
                fileWriter.flush();
                fileWriter.close();

            }
        }

        List<Element> listElement = node.elements();
        for (Element e : listElement) {
            getNodes(e, elementName, attributeName);
        }
        fileWriter.close();
    }


    public static String getElementContentFromXML(String file, String nodeName) {
        String nodeValue = null;

        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(file);

            NodeList elementList = ((org.w3c.dom.Document) doc).getElementsByTagName(nodeName);
            for (int i = 0; i < elementList.getLength(); i++) {
                Node elem = elementList.item(i);
                for (Node node = elem.getFirstChild(); node != null; node = node.getNextSibling()) {

                    if (node.getNodeType() == Node.TEXT_NODE) {
                        nodeValue = node.getTextContent();
                    }
                    if (node.getNodeType() == Node.ELEMENT_NODE) {

                        nodeValue = node.getNodeValue();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nodeValue;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String getAdminTestDataFromXML(String testData, String testClass, String testCase, String attributeName) {
        String attributeValue = null;
        boolean find = false;
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(testData));
            Element root = document.getRootElement();
            for (Iterator it = root.elementIterator(); it.hasNext(); ) {
                Element element = (Element) it.next();
                if (element.getName().equals(testClass)) {
                    for (Iterator it2 = element.elementIterator(); it2.hasNext(); ) {
                        Element element2 = (Element) it2.next();
                        if (element2.getName().equals(testCase)) {
                            List<Attribute> listAttr = element2.attributes();
                            for (Attribute attr : listAttr) {
                                if (attr.getName().equals(attributeName)) {
                                    attributeValue = attr.getText();
                                    find = true;
                                    break;
                                }


                            }

                        }
                    }

                }
                if (find) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attributeValue;
    }


    public static boolean XMLCompare(String file1, String file2) {
        logger.info("Begin compare two xbrl files");
        boolean Identical = false;
        try {
            Reader r1 = new FileReader(file1);
            Reader r2 = new FileReader(file2);

            Diff diff = new Diff(r1, r2);
            Identical = diff.identical();
            logger.info("Similar? " + diff.similar());
            logger.info("Identical? " + diff.identical());

            DetailedDiff detDiff = new DetailedDiff(diff);
            @SuppressWarnings("unchecked")
            List<String> differences = detDiff.getAllDifferences();
            for (Object object : differences) {
                Difference difference = (Difference) object;
                logger.info("***********************");
                logger.info(difference.toString());
                logger.info("***********************");
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Identical;

    }

    public static String getElementValueFromXML(String xmlFile, String ChildNode, String elementName) throws DocumentException {
        String value = null;
        boolean find = false;
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(xmlFile));
        Element root = document.getRootElement();
        Iterator it = root.elementIterator();
        while (it.hasNext() && !find) {

            Element element = (Element) it.next();
            if (element.getName().equals(ChildNode)) {
                Iterator eleIt = element.elementIterator();
                while (eleIt.hasNext()) {
                    Element e = (Element) eleIt.next();
                    if (e.getName().equals(elementName)) {
                        value = e.getText();
                        find = true;
                        break;
                    }
                }
                break;
            } else {
                Iterator it2 = element.elementIterator();
                while (it2.hasNext()) {

                    Element element2 = (Element) it2.next();
                    if (element2.getName().equals(ChildNode)) {
                        Iterator eleIt = element2.elementIterator();
                        while (eleIt.hasNext()) {
                            Element e = (Element) eleIt.next();
                            if (e.getName().equals(elementName)) {
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


    public static String getElementAttributeFromXML(String xmlFile, String ChildNode, String elementName, String attr) throws DocumentException {
        String value = null;
        boolean find = false;
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(xmlFile));
        Element root = document.getRootElement();
        Iterator it = root.elementIterator();
        while (it.hasNext() && !find) {

            Element element = (Element) it.next();
            if (element.getName().equals(ChildNode)) {
                Iterator eleIt = element.elementIterator();
                while (eleIt.hasNext()) {
                    Element e = (Element) eleIt.next();
                    if (e.getName().equals(elementName)) {
                        List attrList = e.attributes();
                        for (int i = 0; i < attrList.size(); i++) {
                            Attribute item = (Attribute) attrList.get(i);
                            if (item.getName().equals(attr)) {
                                value = item.getValue();
                                find = true;
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            } else {
                Iterator it2 = element.elementIterator();
                while (it2.hasNext()) {

                    Element element2 = (Element) it2.next();
                    if (element2.getName().equals(ChildNode)) {
                        Iterator eleIt = element2.elementIterator();
                        while (eleIt.hasNext()) {
                            Element e = (Element) eleIt.next();
                            if (e.getName().equals(elementName)) {
                                List attrList = e.attributes();
                                for (int i = 0; i < attrList.size(); i++) {
                                    Attribute item = (Attribute) attrList.get(i);
                                    if (item.getName().equals(attr)) {
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
}
