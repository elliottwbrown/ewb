package com.ewb.DB;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DBSynchronizer {

    public static String sql;

    public static String buildSQL(final ResultSet rs, String sql) throws SQLException {
        return buildSQL(rs, sql, 0, "");
    }

    public static String buildSQL(final ResultSet rs, String sql, int offset, String pkColName) throws SQLException {
        ResultSetMetaData rsm = rs.getMetaData();
        int colCount = rsm.getColumnCount();
        //System.out.println("colCount="+colCount);

        for (int c = 1; c <= colCount; c++) {
            Object val = "";
            if (c > 1) {
                sql += ",";
            }
            if (pkColName.equals(rsm.getColumnName(c))) {
                val = rs.getInt(c) + offset;
            } else {
                int colType = rsm.getColumnType(c);
                try {
                    if (colType == java.sql.Types.TIMESTAMP) {
                        if (rs.getTimestamp(c) != null) {
                            val = "to_timestamp('" + rs.getTimestamp(c) + "','YYYY-MM-DD HH24:MI:SS.FF')";
                        } else {
                            val = "null";
                        }
                    } else if (colType == java.sql.Types.INTEGER ||
                            colType == java.sql.Types.BIT ||
                            colType == java.sql.Types.BIGINT ||
                            colType == java.sql.Types.TINYINT) {
                        if (rs.getObject(c) != null) {
                            val = rs.getInt(c);
                        } else {
                            val = "0";
                        }
                    } else if (colType == java.sql.Types.DOUBLE) {
                        if (rs.getObject(c) != null) {
                            val = rs.getDouble(c);
                        } else {
                            val = "0";
                        }
                    } else if (colType == java.sql.Types.CHAR ||
                            colType == java.sql.Types.VARCHAR ||
                            colType == java.sql.Types.LONGVARCHAR) {
                        if (rs.getObject(c) != null) {
                            val = "'" + rs.getString(c).replaceAll("'", "''") + "'";
                        } else {
                            val = "''";
                        }
                    } else {
                        val = "null";
                    }
                } catch (SQLException e) {
                    System.err.println(colType);
                    System.err.println(sql);
                    throw e;
                }
            }
            sql += val;
        }
        sql += ")";
        return sql;
    }
}

