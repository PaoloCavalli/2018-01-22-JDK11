package it.polito.tdp.SerieA.EsameSerieA.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.SerieA.EsameSerieA.model.Match;
import it.polito.tdp.SerieA.EsameSerieA.model.Season;
import it.polito.tdp.SerieA.EsameSerieA.model.Team;



public class SerieADAO {

	public List<Season> listAllSeasons() {
		String sql = "SELECT season as s, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				
					
				Season s =new Season(res.getInt("s"), res.getString("description"));
				
				result.add(s);
				}
				
				
			

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
public List<Match> listMatchesForTeam(Team team,Map <Integer,Season> idMap,Map <String, Team> idMapTeam){
	String sql ="SELECT match_id, Season, `Div`,`date`, HomeTeam, AwayTeam, FTHG, FTAG, FTR " + 
			"FROM matches m1 " + 
			"WHERE  HomeTeam=? OR AwayTeam =?" ;
	
	List<Match> result = new ArrayList<>();

	try {
		Connection conn = DBConnect.getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		
		
		st.setString(1, team.getTeam());
		st.setString(2, team.getTeam());
		
		ResultSet res = st.executeQuery();
		
		while(res.next()) {
			Match match = new Match(
					res.getInt("match_id"),
					idMap.get(res.getInt("Season")),
					res.getString("Div"),
					res.getDate("date").toLocalDate(),
					idMapTeam.get(res.getString("HomeTeam")),
					idMapTeam.get(res.getString("AwayTeam")),
					res.getInt("FTHG"),
					res.getInt("FTAG"), res.getString("FTR"));
		result.add(match);
		}
		conn.close();
		return result;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
}
}
