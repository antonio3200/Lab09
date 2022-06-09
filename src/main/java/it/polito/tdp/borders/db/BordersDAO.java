package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.CountryMap;

public class BordersDAO {

	public List<Country> loadAllCountries(CountryMap idMap) {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		List<Country> result = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Country c = new Country(rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
				result.add(idMap.get(c));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(CountryMap idMap,int anno) {
		String sql="SELECT state1no AS s1,state2no AS s2 "
				+ "FROM contiguity c "
				+ "WHERE c.conttype=1 AND c.year<= ?";
		List<Border> result= new LinkedList<Border>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs= st.executeQuery();
			while(rs.next()) {
				int codice1=rs.getInt("s1");
				int codice2=rs.getInt("s2");
				Country c1=idMap.get(codice1);
				Country c2=idMap.get(codice2);
				if(c1!=null && c2!=null) {
					result.add(new Border(c1,c2));
				}
				else {
					System.out.println("Error skipping ");
				}
				Border b= new Border(c1,c2);
				result.add(b);
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al db",e);
		}
	}
}
