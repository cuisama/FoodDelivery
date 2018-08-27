package com.iss.framework;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

@Repository(value = "realtimeDao")
@SuppressWarnings("rawtypes")
public class RealtimeDao {

	@Resource
	private SqliteUtil sqliteUtil;

	/**
	 * 保存一个对象
	 * @param obj
	 * @throws Exception
	 */
	public void saveObject(Object obj) throws Exception {
		Map<String, List<String>> tablesMap = getFieldNamesByClass(obj.getClass());
		Map<String, String> paramMap = getFieldValuesByClass(obj);
		StringBuilder sql = new StringBuilder();
		StringBuilder value = new StringBuilder();
		Connection conn = sqliteUtil.getConnection();
		Statement stmt = conn.createStatement();
		// 组装每个table的字段
		for (String table : tablesMap.keySet()) {
			String sqlStr = this.buildSaveSql(table, tablesMap, paramMap, sql, value);
			stmt.addBatch(sqlStr);
			sql.delete(0, sql.length());
			value.delete(0, value.length());
		}
		stmt.executeBatch();
		conn.commit();
		sqliteUtil.close(conn, stmt, null);
	}

	/**
	 * 保存一组对象
	 * @param objList
	 * @throws Exception
	 */
	public void saveObjectList(List<?> objList) throws Exception {
		int listSize = objList.size() - 1;
		int batchFlag = 0;
		Map<String, List<String>> tablesMap = getFieldNamesByClass(objList.get(0).getClass());
		Map<String, String> paramMap;
		List<String> sqlList = new ArrayList<String>();
		StringBuilder sql = new StringBuilder();
		StringBuilder value = new StringBuilder();

		Connection conn = sqliteUtil.getConnection();
		Statement stmt = conn.createStatement();
		for (int i = 0; i <= listSize; i++) {
			Object obj = objList.remove(0);
			paramMap = getFieldValuesByClass(obj);
			// 组装每个table的字段
			for (String table : tablesMap.keySet()) {
				String sqlStr = this.buildSaveSql(table, tablesMap, paramMap, sql, value);
				stmt.addBatch(sqlStr);
				sql.delete(0, sql.length());
				value.delete(0, value.length());
				if (batchFlag == 5000 || i == listSize) {
					stmt.executeBatch();
					batchFlag++;
				}
			}
		}
		conn.commit();
		sqliteUtil.close(conn, stmt, null);
	}

	/**
	 * 查询一条数据
	 * @param termSn
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> findOneObject(String termSn, Class clazz) throws Exception {
		Map<String, List<String>> tablesMap = getFieldNamesByClass(clazz);
		StringBuilder sql = new StringBuilder();
		StringBuilder field = new StringBuilder();
		int tableSize = tablesMap.keySet().size();

		int index = 0;
		for (String table : tablesMap.keySet()) {
			if (index != 0) {
				sql.append(" LEFT JOIN ").append("'").append(table).append("' t").append(index).append(" ON ")
						.append(" t0.termSn=t").append(index).append(".termSn");
			} else {
				sql.append(" FROM ").append("'").append(table).append("' t").append(index);
			}
			field.append(" t").append(index).append(".*").append(",");
			index++;
		}
		sql.append(" WHERE t0.termSn='").append(termSn).append("'");// .append("'
																	// LIMIT
																	// 0,1");
		field.delete(field.lastIndexOf(","), field.lastIndexOf(",") + 1);
		sql.insert(0, field);
		sql.insert(0, "SELECT ");
		field = null;

		Connection conn = sqliteUtil.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql.toString());
		Map<String, String> valueMap = new HashMap<String, String>();
		if (rs.next()) {
			ResultSetMetaData data = rs.getMetaData();
			int columnSize = data.getColumnCount() - 1;
			for (int i = 1; i <= columnSize; i++) {
				valueMap.put(data.getColumnName(i), rs.getString(data.getColumnName(i)));
			}
		}
		return valueMap;
	}

	/**
	 * 组装多个表的sql
	 * @param table
	 * @param tablesMap
	 * @param paramMap
	 * @param sql
	 * @param values
	 * @return
	 */
	private String buildSaveSql(String table, Map<String, List<String>> tablesMap, Map<String, String> paramMap,
			StringBuilder sql, StringBuilder values) {
		sql.append("REPLACE INTO ");
		sql.append("'").append(table).append("'");
		sql.append("(");
		values.append(" VALUES(");
		// 组装字段值
		for (String column : tablesMap.get(table)) {
			if (paramMap.containsKey(column)) {
				sql.append(column).append(",");
				values.append("'").append(paramMap.get(column)).append("',");
			}
		}
		int indexFlag = values.lastIndexOf(",");
		values.delete(indexFlag, indexFlag + 1);
		values.append(")");

		indexFlag = sql.lastIndexOf(",");
		sql.delete(indexFlag, indexFlag + 1);
		sql.append(")").append(values).append(";");
		return sql.toString();
	}

	/**
	 * 根据一个类，获取当前类及子类的字段列表
	 * 
	 * @param clazz
	 * @return
	 * @throws Exception
	 */

	private Map<String, List<String>> getFieldNamesByClass(Class clazz) throws Exception {
		Map<String, List<String>> paramMap = new HashMap<String, List<String>>();
		String className = clazz.getName();
		paramMap.put(className, new ArrayList<String>());

		Field[] fs = clazz.getDeclaredFields();
		List<Field> list = new ArrayList<Field>();
		list.addAll(Arrays.asList(fs));
		for (Field field : list) {
			Class classes = field.getType();
			if (!classes.getName().contains("com.ithings.")) {
				paramMap.get(className).add(field.getName());
			} else {
				// 自定义类型
				Map<String, List<String>> childMap = getFieldNamesByClass(classes);
				paramMap.putAll(childMap);
			}
		}
		return paramMap;
	}

	/**
	 * 获取一个对象及子对象的字段值(属性名/属性值)
	 */
	private Map<String, String> getFieldValuesByClass(Object obj) throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		// String className = obj.getClass().getName();

		Field[] fs = obj.getClass().getDeclaredFields();
		List<Field> list = new ArrayList<Field>();
		list.addAll(Arrays.asList(fs));
		for (Field field : list) {
			field.setAccessible(true);
			Object fdValue = field.get(obj);
			if (null == fdValue) {
				continue;
			}
			Class classes = field.getType();
			if (!classes.getName().contains("com.iss.item.")) {
				paramMap.put(field.getName(), fdValue.toString());
			} else {
				// 自定义类型
				Map<String, String> childMap = getFieldValuesByClass(fdValue);
				paramMap.putAll(childMap);
			}
		}
		return paramMap;
	}
}
