package com.lombardrisk.utils.fileService;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.List;

public class XBRLUtil {
    private final static Logger logger = LoggerFactory.getLogger(XBRLUtil.class);

    public static String getCellValueFromXBRL(File fileName, String cellName,
                                              String rowID, String instance) {
        String rowInfo = null;
        String result = null;

        List<String> strList = TxtUtil.getFileContent(fileName);
        for (int i = 0; i <= strList.size(); i++) {
            if (rowID.equals("")) {
                if (strList.get(i).contains(cellName)) {
                    rowInfo = strList.get(i + 1);
                    break;
                }
            } else {
                int rowKey = Integer.parseInt(rowID);
                int init = 0;
                while (init < rowKey) {
                    if (strList.get(i).contains(cellName)) {
                        init++;
                    }
                    if (init == rowKey) {
                        rowInfo = strList.get(i + 1);
                        break;
                    }
                }
            }
        }
        result = splitRowInfo(rowInfo);

        return result;
    }

    public static String splitRowInfo(String rowInfo) {
        int decimal = 0;
        String result = null;
        try {
            Double cellValue = Double.parseDouble(rowInfo.substring(
                    rowInfo.indexOf(">") + 1, rowInfo.lastIndexOf("<")));
            String left = rowInfo.substring(1, rowInfo.indexOf(">"));
            for (String unit : left.split(" ")) {
                if (unit.startsWith("decimals")) {
                    decimal = Integer.parseInt(unit.split("=")[1].replace("\"",
                            ""));
                    break;
                }
            }
            if (decimal > 0)
                cellValue = cellValue * Math.pow(10, decimal);
            else if (decimal < 0)
                cellValue = cellValue / Math.pow(10, Math.abs(decimal));

            result = String.valueOf(cellValue);
        } catch (Exception e) {
            result = rowInfo.substring(rowInfo.indexOf(">") + 1,
                    rowInfo.lastIndexOf("<"));
        }

        return result;

    }

    public static boolean XBRLCompare(String file1, String file2) {
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

}