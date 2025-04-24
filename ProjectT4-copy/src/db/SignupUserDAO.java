package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;


public class SignupUserDAO {
	private Connection con; 
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	// 패턴 선언
	Pattern pattern_nameEn = Pattern.compile("^[a-zA-Z]*$"); // 이름 패턴 : 영문으로만 또는 한글로만 [길이제한없음]
	Pattern pattern_nameKo = Pattern.compile("^[ㄱ-ㅎ가-힣]*$"); 
	Pattern pattern_tel = Pattern.compile("^(\\d{2,3})(\\d{3,4})(\\d{4})$"); // 연락처 패턴 : 숫자만 [10~]자
	Pattern pattern_id = Pattern.compile("^(?=.*[a-zA-Z]).{4,15}$"); // id 패턴 : 영문 필수 [4~15]자
	Pattern pattern_pw = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,20}$"); // pw 패턴 : 영문+숫자+특문 필수 [8~20]자

	public SignupUserDAO() throws ClassNotFoundException, SQLException { // DAO 생성자
		new DBConn();
		con = DBConn.getConnection(); // 객체.메소드()
	}

	// 0) user_acc테이블에 (id, pw, 연락처, 이름) INSERT 메소드
	public void insertUseracc(SignupUserVO signup_info) {
		try {
			pstmt = con.prepareStatement("INSERT INTO user_acc "
					+ "VALUES(?,?,?,?)");
			pstmt.setString(1, signup_info.getId());
			pstmt.setString(2, signup_info.getPw());
			pstmt.setString(3, signup_info.getName());
			pstmt.setString(4, telTransform(signup_info.getTel())); // user_phone칼럼값은 형식을 변환하여 저장  예) "01012345678" ->  "010-1234-5678"

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("user_acc 테이블 삽입 에러: " + e.getMessage());
		}
	}

	// 예외 처리
	// 1) 유저id가 DB에 이미 존재하는 유저id인지 확인하는 메소드
	public boolean isExist(String id) {
		try {
			pstmt = con.prepareStatement("SELECT * "
					+ "FROM user_acc "
					+ "WHERE user_id = ?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) { // id가 중복이면 (select문의 결과가 나오면) true반환
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// 2) pw와 pw재입력 값이 서로 다른지 확인하는 함수
	public boolean isPwIncorrect(String pw, String pw_re) {
		if (!pw_re.equals(pw)) {
			return true; // 다르면 true 반환
		}
		return false;
	}

	// 3) id, pw, 이름, 연락처 입력값이 규정에 맞는지 확인하는 함수
//	3-1) 이름 입력값이 이름 규정에 주합하는지 확인하는 함수
	public boolean isNameMatch(String name) {
		if (pattern_nameEn.matcher(name).matches() || pattern_nameKo.matcher(name).matches()) {
			return true; // 이름이 규정(;오직 영문/한글로만)에 부합하면 true반환
		}
		return false;
	}

//	3-2) 연락처 입력값이 연락처 규정에 부합한지 확인하는 함수
	public boolean isTelMatch(String tel) {
		if (pattern_tel.matcher(tel).matches()) {
			return true; // 연락처가 규정에 부합하면 true반환
		}
		return false;
	}

//	3-2-1) 연락처("01012345678") 에 -> 하이픈삽입하여("010-1234-5678") 반환하는 함수
	public String telTransform(String tel) {
		if (tel.substring(0, 2).contains("02")) {
			if(tel.length() == 9) {
				return tel.replaceAll("(\\d{2})(\\d{3})(\\d{4})", "$1-$2-$3"); // 반환 02-xxx-xxxx		
			}
			if(tel.length() == 10){
				return tel.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "$1-$2-$3"); // 반환 02-xxxx-xxxx	
			}
		}
		
		return tel.replaceAll("(\\d{2,3})(\\d{3,4})(\\d{4})", "$1-$2-$3"); // 반환 0xx-xxx-xxxx 또는 0xx-xxxx-xxxx	
	}

//  3-3) id입력값이 id규정에 부합한지 확인하는 함수
	public boolean isIdMatch(String id) {
		if (pattern_id.matcher(id).matches()) { // id가 규정에 부합하면 true반환
			return true;
		}
		return false;
	}

//	3-4) pw 입력값이 pw규정에 부합한지 확인하는 함수
	public boolean isPwMatch(String pw) {
		if (pattern_pw.matcher(pw).matches()) { // pw가 규정에 부합하면 true반환
			return true;
		}
		return false;
	}

}
