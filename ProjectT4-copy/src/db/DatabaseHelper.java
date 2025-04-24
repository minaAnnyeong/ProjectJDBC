package db;

import java.sql.Connection;
//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DatabaseHelper {
	PreparedStatement pstmt = null;
	private static ResultSet rs = null;
	
	public static UserRsvVO userRsvVO = new UserRsvVO();

	public static UserRsvMenuVO urmVO = new UserRsvMenuVO(); // 메뉴 하나의 정보 {메뉴아이디, 메뉴개수}

	public static ArrayList<UserRsvMenuVO> urmVOList = new ArrayList<UserRsvMenuVO>(); // 위 메뉴정보의 List
	

	// 예약 정보를 데이터베이스에 삽입하는 메소드
	public static boolean insertUserReservation(UserRsvVO userRsvVO) throws ClassNotFoundException {
		String sql = "INSERT INTO user_rsv (user_rsv_id, user_id, rest_id, user_count, rev_time) "
				+ "VALUES (user_rsv_seq.NEXTVAL, ?, ?, ?, ?)";

		try (Connection conn = DBConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, userRsvVO.getUserId());
			stmt.setInt(2, userRsvVO.getRestId());
			stmt.setInt(3, userRsvVO.getUserCount());
			stmt.setString(4, userRsvVO.getRsvTime());

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("예약 정보 삽입 오류: " + e.getMessage());
			return false;
		}
	}

	/*
	 * // 특정 사용자의 예약 목록을 조회하는 메소드 public static void getUserReservations(String
	 * userId) { String sql = "SELECT * FROM user_rsv WHERE user_id = ?";
	 * 
	 * try (Connection conn = DBConn.getConnection(); PreparedStatement stmt =
	 * conn.prepareStatement(sql)) {
	 * 
	 * stmt.setString(1, userId); // 사용자의 ID로 예약 정보 조회
	 * 
	 * try (ResultSet rs = stmt.executeQuery()) { while (rs.next()) { int userRsvId
	 * = rs.getInt("user_rsv_id"); int restId = rs.getInt("rest_id"); int userCount
	 * = rs.getInt("user_count"); java.sql.Timestamp revTime =
	 * rs.getTimestamp("rev_time");
	 * 
	 * // 예약 정보 출력 System.out.println("Reservation ID: " + userRsvId +
	 * ", Restaurant ID: " + restId + ", User Count: " + userCount +
	 * ", Reservation Time: " + revTime); } }
	 * 
	 * } catch (SQLException e) { System.err.println("사용자 예약 정보 조회 오류: " +
	 * e.getMessage()); } }
	 */


	// 메뉴와 예약 정보를 `user_rsv_menu` 테이블에 저장하는 메소드
	public static boolean insertUserRsvMenu(UserRsvVO userRsvVO, UserRsvMenuVO urmVO) {
		String sql = "INSERT INTO user_rsv_menu (user_rsv_menu_id, user_rsv_id, menu_id, menu_count) "
				+ "VALUES (user_rsv_menu_seq.NEXTVAL, ?, ?, ?)";

		String sqlGetSeq = "SELECT user_rsv_id FROM user_rsv WHERE user_id = ? ORDER BY user_rsv_id DESC FETCH FIRST 1 ROWS ONLY";
		int userRsvId = -1;
		try (Connection conn = DBConn.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlGetSeq);
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, userRsvVO.getUserId());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				userRsvId = rs.getInt("user_rsv_id");
			}

			stmt.setInt(1, userRsvId);
			stmt.setInt(2, urmVO.getMenuId());
			stmt.setInt(3, urmVO.getMenuCount());

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("메뉴 예약 정보 삽입 오류: " + e.getMessage());
			return false;
		}

	}
	

	// 메뉴 ID를 조회하는 메소드
	public static int getMenuIdFromFood(String food) {
		String sql = "SELECT menu_id FROM menu WHERE menu_name = ?";
		try (Connection conn = DBConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, food);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("menu_id");
				}
			}

		} catch (SQLException e) {
			System.err.println("메뉴 ID 조회 오류: " + e.getMessage());
		}
		return -1; // 메뉴가 없을 경우 -1 반환
	}
	
	// 식당 아이디(int), 유저 아이디(String)파라미터로 받아서
	// 예약정보 String[] result = {식당명, 예약자명} 반환하는 메소드
	public static String[] getRsvInfo(int restId, String userId) {
		String[] result = new String[2];
		String sqlRestName = "SELECT rest_name FROM resta_se WHERE rest_id = ?";
		
		try (Connection conn = DBConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlRestName)) {

			stmt.setInt(1, restId);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					result[0] = rs.getString("rest_name");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		String sqlUserName = "SELECT user_name FROM user_acc WHERE user_id = ?";
		try (Connection conn = DBConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlUserName)) {

			stmt.setString(1, userId);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					result[1] = rs.getString("user_name");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	// user_id를 파라미터로 받아 해당 유저의 예약정보 반환하는 메소드
	// String[] info 는 {식당명, 예약시간} 순
	// **한 유저가 여러 식당에 예약한 경우를 고려, 
	// 예약들어간 식당 별 예약정보info 들을 ArrayList result에 저장하여 반환
	public static ArrayList<String[]> getAllRsvInfo(String userId) {
		ArrayList<String[]> result = new ArrayList<>(); // info들의 List
		String[] info = new String[2]; // {식당명, 예약시간}
		// 식당명(info value)은 getRsvInfo메소드를 일부 이용
		String sqlGetRestName = "SELECT rest_id FROM user_rsv "
								+ "WHERE user_id = ? "
								+ "ORDER BY rev_time";
	
		ArrayList<Integer> restIdList = new ArrayList<>();
		
		try (Connection conn = DBConn.getConnection(); 
				PreparedStatement stmt1 = conn.prepareStatement(sqlGetRestName)) {

			stmt1.setString(1, userId);

			try (ResultSet rs = stmt1.executeQuery()) {
				while (rs.next()) {
					restIdList.add(rs.getInt("rest_id"));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String sqlGetRevTime = "SELECT rev_time FROM user_rsv "
				+ "WHERE user_id = ? "
				+ "ORDER BY rev_time";
		
		try (Connection conn = DBConn.getConnection(); 
				PreparedStatement stmt2 = conn.prepareStatement(sqlGetRevTime)) {

			stmt2.setString(1, userId);

			try (ResultSet rs = stmt2.executeQuery()) {
				for(int i=0; rs.next(); i++) {
					info[0] = getRsvInfo(restIdList.get(i), userId)[0]; // {식당명}
					info[1] = rs.getString("rev_time"); // {예약시간}
					
					result.add(info);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return result; // 유저가 예약한 정보가 없으면 빈 ArrayList가 반환될 것임.
	}

	public static class UserDAO {
		public static boolean login(String userId, String password) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				conn = DBConn.getConnection();
				String sql = "SELECT * FROM user_acc WHERE USER_ID = ? AND USER_PW = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userId);
				pstmt.setString(2, password);
				rs = pstmt.executeQuery();

				return rs.next(); // 결과가 있으면 로그인 성공
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			} finally {
				DBConn.closeConnection();
			}
		}
	}

	public static class CancelDAO {

		// 예약 취소 메서드 (user_id로 예약 삭제)
		public static boolean cancelReservationByUserId(String userId) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				conn = DBConn.getConnection(); // DB 연결

				String getRsvSql = "SELECT user_rsv_id FROM user_rsv WHERE user_id = ?";
				pstmt = conn.prepareStatement(getRsvSql);
				pstmt.setString(1, userId);
				rs = pstmt.executeQuery();

				ArrayList<Integer> rsvIds = new ArrayList<>();
				while (rs.next()) {
					rsvIds.add(rs.getInt("user_rsv_id"));
				}
				rs.close();
				pstmt.close();

				// 2. 연결된 테이블 먼저 삭제
				for (int rsvId : rsvIds) {
					// user_rsv_menu 삭제
					pstmt = conn.prepareStatement("DELETE "
							+ "FROM user_rsv_menu "
							+ "WHERE user_rsv_id = ?");
					pstmt.setInt(1, rsvId);
					pstmt.executeUpdate();
					pstmt.close();

					// admin_rsv 삭제
					/*
					 * pstmt = conn.prepareStatement("DELETE FROM admin_rsv WHERE user_rsv_id = ?");
					 * pstmt.setInt(1, rsvId); pstmt.executeUpdate(); pstmt.close();
					 */
				}

				// 3. user_rsv 삭제
				pstmt = conn.prepareStatement("DELETE "
						+ "FROM user_rsv "
						+ "WHERE user_id = ?");
				pstmt.setString(1, userId);
				int result = pstmt.executeUpdate(); // 성공한 행의 수 반환

				return result > 0; // 삭제 성공 시 true 반환

			} catch (SQLException e) {
				e.printStackTrace();
				return false; // 오류 발생 시 false 반환
			} finally {
				// DB 연결 해제
				DBConn.closeConnection();
			}
		}
	}
	
	public static class ShowUserRsvDAO {

		// 유저의 예약 목록을 반환 {식당명, 인원 수, 예약 시간} 하는 메소드
		public static ArrayList<ArrayList<String>> getUserRsvList(String userId) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			ArrayList<ArrayList<String>> result = new ArrayList<>();
			
			try {
				conn = DBConn.getConnection();
				String sql = "SELECT rs.rest_name, "
						+ "ur.user_count, "
						+ "ur.rev_time "
						+ "FROM user_rsv ur, resta_se rs "
						+ "WHERE user_id = ? "
						+ "AND ur.rest_id = rs.rest_id";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userId);
				rs = pstmt.executeQuery();

				while(rs.next()) {
					ArrayList<String> item = new ArrayList<>();
					item.add(rs.getString(1));
					item.add(rs.getString(2));
					item.add(rs.getString(3));
					result.add(item);
				}
				rs.close();
				pstmt.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBConn.closeConnection();
			}
			return result;
		}
	}
	

	// 유저가 선택한 식당의 메뉴판에 대한 정보를 반환하는 메소드
	// 파라미터로 준 restId로 menu테이블에서
	// 해당 식당아이디의 메뉴 튜플들을 select하여 메뉴이름s, 메뉴가격s, 메뉴이미지경로s 가져와 Map에 add.
	// > (1) String[] foodNames , (2) int[] foodPrices , (3) String[] foodImages 
	public static LinkedHashMap<Integer, MenuInfo> getRestMenu(int restId) {
		LinkedHashMap<Integer, MenuInfo> map = new LinkedHashMap<Integer, MenuInfo>(); // 반환할 HashMap; 메뉴판 정
		String sqlGetMenu = "SELECT menu_id, menu_name, menu_price, img_path " + "FROM menu " + "WHERE rest_id = ? "
							+ "ORDER BY 1"; // *이미 menu_id가 PK라 order by 없이도 순서대로 나옴
		try (Connection conn = DBConn.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sqlGetMenu)) {

			pstmt.setInt(1, restId);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					int menuId = rs.getInt("menu_id");

					String menuName = rs.getString("menu_name");
					int menuPrice = rs.getInt("menu_price");
					String imgPath = rs.getString("img_path");

					MenuInfo mInfo = new MenuInfo(menuName, menuPrice, imgPath);

					map.put(menuId, mInfo);
				}

			}

		} catch (SQLException e) {
			System.err.println("메뉴판 불러오기 오류: " + e.getMessage());
		}

		return map;
	}
	
	// A04_ReserveForm 창에 표시할 예약자명, 연락처 Getter
	public static String[] getNameTel(UserRsvVO userRsvVO) {
		String[] result = new String[2];
		String sqlGetNameTel = "SELECT user_name, user_phone " 
								+ "FROM user_acc " 
								+ "WHERE user_id = ? ";
		try (Connection conn = DBConn.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sqlGetNameTel)) {

			pstmt.setString(1, userRsvVO.getUserId());

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					result[0] = rs.getString("user_name");
					result[1] = rs.getString("user_phone");
				}

			}

		} catch (SQLException e) {
			System.err.println("메뉴판 불러오기 오류: " + e.getMessage());
		}

		return result;
	}

	
}