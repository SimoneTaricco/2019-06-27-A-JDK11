package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> listAllCategories(){
		String sql = "SELECT DISTINCT offense_category_id "
				+ "FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getString("offense_category_id"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Date> listAllDate(){
		String sql = "SELECT DISTINCT DATE(reported_date) AS data "
				+ "FROM EVENTS "
				+ "ORDER BY data ASC " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Date> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getDate("data"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> listVertices(String catID, Integer anno){
		
		String sql = "SELECT DISTINCT offense_type_id "
				+ "FROM EVENTS "
				+ "WHERE YEAR(reported_date) = ? AND offense_category_id = ?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setString(2, catID);
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getString("offense_type_id"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Adiacenza> listAdiacenze(String catID, Integer anno){
		
		String sql = "SELECT e1.offense_type_id AS id1, e2.offense_type_id as id2, COUNT(DISTINCT(e1.district_id)) AS peso "
				+ "FROM EVENTS e1, EVENTS e2 "
				+ "WHERE YEAR(e1.reported_date) = ? AND e1.offense_category_id = ? "
				+ "AND e1.offense_category_id = e2.offense_category_id AND YEAR(e2.reported_date) = YEAR(e1.reported_date) "
				+ "AND e1.offense_type_id > e2.offense_type_id "
				+ "AND e1.district_id = e2.district_id "
				+ "GROUP BY e1.offense_type_id, e2.offense_type_id" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setString(2, catID);
			
			List<Adiacenza> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Adiacenza(res.getString("id1"),res.getString("id2"),res.getDouble("peso")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}

}
