package com.iss.item.cuisine.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.iss.framework.SqliteUtil;
import com.iss.item.cuisine.model.Cuisine;

@Repository
public class CuisineDao {
	
	@Resource  
    private SqliteUtil sqliteUtil; 
	
	private Connection conn = null;
	private Statement stmt = null;

	
	public List<Cuisine> list() throws Exception{
		conn = sqliteUtil.getConnection();  
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from cuisine");
        Cuisine cuisine = null;
        List<Cuisine> list = new ArrayList<>();
        while(rs.next()){
        	cuisine = new Cuisine(rs);
        	list.add(cuisine);
        }
        sqliteUtil.close(conn, stmt, rs);
        return list;
	}

}
