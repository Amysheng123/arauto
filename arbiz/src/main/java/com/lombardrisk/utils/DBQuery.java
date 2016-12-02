package com.lombardrisk.utils;

import java.util.List;

import org.yiwan.webcore.util.PropHelper;

/**
 * Create by Leo Tu on Jun 26, 2015
 */
public class DBQuery{

    static String USERNAME = PropHelper.getProperty("rp.user").trim();
    static String DBType = PropHelper.getProperty("db.type").trim();
    static String connectedDB = PropHelper.getProperty("db.connectedDB").trim();
    static String serverName = PropHelper.getProperty("db.sqlserverName").trim();
    static String ip = PropHelper.getProperty("db.ip").trim();
    static String sid = PropHelper.getProperty("db.sid").trim();
    static String user = PropHelper.getProperty("db.apDBName").trim().toUpperCase();
    static String ToolsetDB = PropHelper.getProperty("db.toolsetDBName").trim().toUpperCase();
    static String password = "password";

    public static String queryRecord(String sql) {
        DBHelper dh = null;
        if (DBType.equalsIgnoreCase("oracle")) {
            if (connectedDB.equalsIgnoreCase("ar"))
                dh = new DBHelper("oracle", ip, sid, user);
            else
                dh = new DBHelper("oracle", ip, sid, ToolsetDB);
        } else {
            if (connectedDB.equalsIgnoreCase("ar"))
                dh = new DBHelper("mssql", serverName, user);
            else
                dh = new DBHelper("mssql", serverName, ToolsetDB);
        }
        dh.connect();
        String rst = dh.query(sql);
        dh.close();
        return rst;
    }

    public static String queryRecordSpecDB(String dbName, String sql) {
        DBHelper dh = null;
        if (DBType.equalsIgnoreCase("oracle")) {
            dh = new DBHelper("oracle", ip, sid, dbName);
        } else {
            dh = new DBHelper("mssql", serverName, dbName);
        }
        dh.connect();
        String rst = dh.query(sql);
        dh.close();
        return rst;
    }

    public static List<String> queryRecords(String sql) {
        DBHelper dh = null;
        if (DBType.equalsIgnoreCase("oracle")) {
            if (connectedDB.equalsIgnoreCase("ar"))
                dh = new DBHelper("oracle", ip, sid, user);
            else
                dh = new DBHelper("oracle", ip, sid, ToolsetDB);
        } else {
            if (connectedDB.equalsIgnoreCase("ar"))
                dh = new DBHelper("mssql", serverName, user);
            else
                dh = new DBHelper("mssql", serverName, ToolsetDB);
        }
        dh.connect();
        List<String> rst = dh.queryRecords(sql);
        dh.close();
        return rst;
    }


    public static int update(String sql) {
        DBHelper dh = null;
        if (DBType.equalsIgnoreCase("oracle")) {
            if (connectedDB.equalsIgnoreCase("ar"))
                dh = new DBHelper("oracle", ip, sid, user);
            else
                dh = new DBHelper("oracle", ip, sid, ToolsetDB);
        } else {
            if (connectedDB.equalsIgnoreCase("ar"))
                dh = new DBHelper("mssql", serverName, user);
            else
                dh = new DBHelper("mssql", serverName, ToolsetDB);
        }
        dh.connect();
        int rst = dh.update(sql);
        return rst;
    }


    public static String getCellValeFromDB(String Regulator, String formCode, String version, String processDate, String Entity, String instance, String cellId, boolean isExtendCell, int rowKey) {
        String value = null;
        if (connectedDB.equalsIgnoreCase("ar")) {
            String month = processDate.substring(3, 5);
            String day = processDate.substring(0, 2);
            String year = processDate.substring(8, 10);
            switch (month) {
                case "01":
                    month = "JAN";
                    break;
                case "02":
                    month = "FED";
                    break;
                case "03":
                    month = "MAR";
                    break;
                case "04":
                    month = "APR";
                    break;
                case "05":
                    month = "MAY";
                    break;
                case "06":
                    month = "JUN";
                    break;
                case "07":
                    month = "JUL";
                    break;
                case "08":
                    month = "AUG";
                    break;
                case "09":
                    month = "SEP";
                    break;
                case "10":
                    month = "OCT";
                    break;
                case "11":
                    month = "NOV";
                    break;
                case "12":
                    month = "DEC";
                    break;
            }

            if (month.startsWith("0"))
                month = month.substring(1);
            if (day.startsWith("0"))
                day = day.substring(1);


            //String REFERENCE_DATE=month+"-"+day+"-"+year+" 12.00.00.000000 AM";
            String REFERENCE_DATE = day + "-" + month + "-" + year + " 12.00.00.000000 AM";


            String SQL = "SELECT \"PREFIX\" FROM \"CFG_INSTALLED_CONFIGURATIONS\" WHERE \"DESCRIPTION\"='" + Regulator + "' AND \"STATUS\"='A' ";
            String prefix = queryRecordSpecDB(user, SQL);

            SQL = "SELECT \"ID\" FROM \"FIN_FORM_INSTANCE\" WHERE \"CONFIG_PREFIX\"='" + prefix + "' AND \"EDITION_STATUS\"='ACTIVE' AND \"LAST_EDITOR\"='" + (USERNAME).toUpperCase() + "' AND \"FORM_CODE\"='" + formCode + "' AND \"FORM_VERSION\"='" + version + "' AND \"REFERENCE_DATE\"='" + REFERENCE_DATE + "' ";
            String formID = queryRecord(SQL);

            SQL = "SELECT \"ID_RANGE_START\" FROM \"CFG_INSTALLED_CONFIGURATIONS\" WHERE \"DESCRIPTION\"='" + Regulator + "'  AND \"STATUS\"='A' ";
            String startID = queryRecord(SQL);

            SQL = "SELECT \"ID_RANGE_END\" FROM \"CFG_INSTALLED_CONFIGURATIONS\" WHERE \"DESCRIPTION\"='" + Regulator + "' AND \"STATUS\"='A'  ";
            String endID = queryRecord(SQL);

            SQL = "SELECT \"ReturnId\" FROM \"CFG_RPT_Rets\" WHERE \"ID\">='" + startID + "' AND \"ID\"<='" + endID + "' AND \"Return\"='" + formCode + "' AND \"Version\"=" + version + "  ";
            String returnId = queryRecord(SQL);

            if (!instance.matches("[0-9]+")) {
                SQL = "SELECT \"InstPageInst\" FROM \"CFG_RPT_Instances\" WHERE \"ID\">='" + startID + "' AND \"ID\"<='" + endID + "' AND \"InstDescription\"='" + instance + "'";
                instance = queryRecord(SQL);
            }
            if (!isExtendCell)
                SQL = "SELECT \"Type\" FROM \"CFG_RPT_Ref\" WHERE \"ID\">='" + startID + "' AND \"ID\"<='" + endID + "' AND  \"Item\"='" + cellId + "' and \"ReturnId\"=" + returnId + "";
            else
                SQL = "SELECT \"Type\" FROM \"CFG_RPT_GridRef\" WHERE \"ID\">='" + startID + "' AND \"ID\"<='" + endID + "' AND  \"Item\"='" + cellId + "' and \"ReturnId\"=" + returnId + "";
            String type = queryRecord(SQL);

            String queryItem = null;
            if (type.equalsIgnoreCase("D"))
                queryItem = "DATE_VALUE";
            else if (type.equalsIgnoreCase("C"))
                queryItem = "CHAR_VALUE";
            else
                queryItem = "NUMBER_VALUE";

            if (!isExtendCell) {
                SQL = "SELECT \"" + queryItem + "\" FROM \"FIN_CELL_INSTANCE\" WHERE \"FORM_INSTANCE_ID\"='" + formID + "' AND \"ITEM_CODE\"='" + cellId + "' AND \"VERSION\"=0 and \"Z_AXIS_ORDINATE\"=" + instance + "";
                value = queryRecord(SQL);
            } else {
                SQL = "SELECT \"" + queryItem + "\" FROM \"FIN_CELL_INSTANCE\" WHERE \"FORM_INSTANCE_ID\"='" + formID + "' AND \"X_AXIS_ORDINATE\"='" + cellId + "' AND \"Y_AXIS_ORDINATE\" IS NOT NULL AND \"VERSION\"=0 and \"Z_AXIS_ORDINATE\"=" + instance + "";
                List<String> values = queryRecords(SQL);
                if (rowKey > 0)
                    value = values.get(rowKey - 1);
                else
                    value = values.get(0);
            }


        } else {
            String SQL = "SELECT \"ID_RANGE_START\" FROM \"CFG_INSTALLED_CONFIGURATIONS\" WHERE \"DESCRIPTION\"='" + Regulator + "'  AND \"STATUS\"='A' ";
            String RegPrefix = queryRecordSpecDB(user, SQL);
            SQL = "SELECT \"ReturnId\" FROM \"" + RegPrefix + "Rets\" WHERE \"Return\"='" + formCode + "' and \"Version\"='" + version + "'";
            String rerurnID = queryRecord(SQL);
            SQL = "SELECT \"Page\" FROM \"" + RegPrefix + "List\" WHERE \"ReturnId\"='" + rerurnID + "'";
            String page = (SQL);
            SQL = "SELECT \"EntityId\" FROM \"" + RegPrefix + "Grps\" WHERE \"Name\"='" + Entity + "'";
            String entityID = (SQL);
            SQL = "SELECT \"InstPageInst\" FROM \"" + RegPrefix + "Instances\" WHERE \"InstDescription\"='" + instance + "'";
            String pageInstance = queryRecord(SQL);

            if (!isExtendCell) {
                SQL = "SELECT \"" + cellId.toUpperCase() + "\" FROM \"" + page + "\" WHERE \"STBStatus\"='A' and \"EntityId\"='" + entityID + "' and \"PageInst\"='" + pageInstance + "'";
            } else {
                SQL = "SELECT \"" + cellId.toUpperCase() + "\" FROM \"" + page + "\" WHERE \"STBStatus\"='A' and \"EntityId\"='" + entityID + "' and \"PageInst\"='" + pageInstance + "' AND \"" + formCode + "INDEX\"='" + rowKey + "'";
            }

            value = queryRecord(SQL);
        }

        return value;

    }


}
   