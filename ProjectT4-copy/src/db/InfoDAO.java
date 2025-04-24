package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class InfoDAO {
	// DAO - DB와 만나는 java 파일

	private Connection con;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public InfoDAO() throws ClassNotFoundException, SQLException {
		new DBConn();
		con = DBConn.getConnection();
	}
	
///////////////////////////////////////////////////////////
	public ArrayList<InfoVO> getAllInfo() throws SQLException {
		ArrayList<InfoVO> rev_array = new ArrayList<>();

		String sql = 
			    "SELECT user_name, rev_time, " +
			    "LISTAGG(menu_name || ' ' || menu_count, CHR(10)) " +
			    "WITHIN GROUP (ORDER BY user_rsv_menu_id) AS menu_list, " +
			    "user_phone, " +
			    "user_rsv_id " +
			    "FROM ( " +
			    "    SELECT ua.user_name, ur.rev_time, " +
			    "           m.menu_name, urm.menu_count, urm.user_rsv_menu_id, ua.user_phone " +
			    "			ur.user_rsv_id " +
			    "    FROM user_rsv ur " +
			    "    JOIN user_acc ua ON ur.user_id = ua.user_id " +
			    "    JOIN user_rsv_menu urm ON ur.user_rsv_id = urm.user_rsv_id " +
			    "    JOIN menu m ON urm.menu_id = m.menu_id " +
			    ") " +
			    "GROUP BY user_name, rev_time, user_phone, user_rsv_id  " +
			    "ORDER BY rev_time";


		pstmt = con.prepareStatement(sql);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			String name = rs.getString("user_name");
			String rsv_time = rs.getString("rev_time");
			String menu_list = rs.getString("menu_list");
			String user_phone = rs.getString("user_phone");
			int user_rsv_id = rs.getInt("user_rsv_id");

			InfoVO tv = new InfoVO(name, rsv_time, menu_list, user_phone, user_rsv_id);
			rev_array.add(tv);
		}
		return rev_array;
	}
///////////////////////////////////////////////////////
	
	
	public ArrayList<InfoVO> getAllInfo(int rest_id) throws SQLException {
		ArrayList<InfoVO> rev_array = new ArrayList<>();

		String sql = 
			    "SELECT user_name, rev_time, rest_id," +
			    "LISTAGG(menu_name || ' ' || menu_count, CHR(10)) " +
			    "WITHIN GROUP (ORDER BY user_rsv_menu_id) AS menu_list, " +
			    "user_phone, " +
			    "user_rsv_id " +
			    "FROM ( " +
			    "    SELECT ua.user_name, ur.rev_time, ur.rest_id, " +
			    "           m.menu_name, urm.menu_count, urm.user_rsv_menu_id, ua.user_phone, " +
			    "			ur.user_rsv_id " +
			    "    FROM user_rsv ur " +
			    "    JOIN user_acc ua ON ur.user_id = ua.user_id " +
			    "    JOIN user_rsv_menu urm ON ur.user_rsv_id = urm.user_rsv_id " +
			    "    JOIN menu m ON urm.menu_id = m.menu_id " +
			    ") " +
			    "	 WHERE rest_id = ? " +
			    "GROUP BY user_name, rev_time, user_phone, rest_id, user_rsv_id " +
			    "ORDER BY rev_time";


		pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, rest_id);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			String name = rs.getString("user_name");
			String rsv_time = rs.getString("rev_time");
			String menu_list = rs.getString("menu_list");
			String user_phone = rs.getString("user_phone");
			int user_rsv_id = rs.getInt("user_rsv_id");

			InfoVO tv = new InfoVO(name, rsv_time, menu_list, user_phone, user_rsv_id);
			rev_array.add(tv);
		}
		return rev_array;
	}
}
