package a415;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import db.SignupUserDAO;
import db.SignupUserVO;


public class C01_SignupUserMain extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static final int WIDTH = 480;
	public static final int HEIGHT = 640;
	private JTextField textField_name;
	private JTextField textField_tel;
	private JTextField textField_id;
	private JPasswordField PasswordField_pw;
	private JPasswordField PasswordField_pw_re;
	
	// SignupUserVO 객체 선언
	SignupUserVO signup_info; 
	// SignupUserDAO 객체 선언
	SignupUserDAO signup_dao;

	/**
	 * Launch the application.
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					C01_SignupUserMain frame_signupuser = new C01_SignupUserMain();
					frame_signupuser.setVisible(true); // 창 보이게 하기
					frame_signupuser.setSize(WIDTH, HEIGHT); // 창 크기 설정
					frame_signupuser.setLocationRelativeTo(null); // 화면의 가운데에 창 띄우기
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	// "회원 가입" 창 설계
	public C01_SignupUserMain() {
		setTitle("회원가입");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, WIDTH, HEIGHT);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		// 창 제목
		JLabel lbl_title = new JLabel("일반손님 회원가입");
		lbl_title.setFont(new Font("굴림", Font.BOLD, 18));
		lbl_title.setBounds(146, 40, 176, 25);
		lbl_title.setHorizontalAlignment(JLabel.CENTER);
		contentPane.add(lbl_title);

		// "이름" 입력칸, 라벨
		textField_name = new JTextField();
		textField_name.setBounds(100, 100, 280, 35);
		contentPane.add(textField_name);
		textField_name.setColumns(10);

		JLabel lbl_name = new JLabel("이름 :");
		lbl_name.setLabelFor(textField_name);
		lbl_name.setFont(new Font("굴림", Font.BOLD, 14));
		lbl_name.setBounds(31, 105, 57, 25);
		contentPane.add(lbl_name);

		// "연락처" 입력칸, 라벨
		textField_tel = new JTextField();
		textField_tel.setColumns(10);
		textField_tel.setBounds(100, 170, 280, 35);
		contentPane.add(textField_tel);

		JLabel lbl_tel = new JLabel("연락처 :");
		lbl_tel.setLabelFor(textField_tel);
		lbl_tel.setFont(new Font("굴림", Font.BOLD, 14));
		lbl_tel.setBounds(22, 177, 57, 25);
		contentPane.add(lbl_tel);

		// "ID" 입력칸, 라벨
		textField_id = new JTextField();
		textField_id.setColumns(10);
		textField_id.setBounds(100, 240, 280, 35);
		contentPane.add(textField_id);

		JLabel lbl_id = new JLabel("ID :");
		lbl_id.setLabelFor(textField_id);
		lbl_id.setFont(new Font("굴림", Font.BOLD, 14));
		lbl_id.setBounds(49, 245, 57, 25);
		contentPane.add(lbl_id);

		// "PW" 입력칸, 라벨
		PasswordField_pw = new JPasswordField();
		PasswordField_pw.setBounds(100, 310, 280, 35);
		contentPane.add(PasswordField_pw);

		JLabel lbl_password = new JLabel("PW :");
		lbl_password.setLabelFor(PasswordField_pw);
		lbl_password.setFont(new Font("굴림", Font.BOLD, 14));
		lbl_password.setBounds(49, 315, 57, 25);
		contentPane.add(lbl_password);

		// "PW재입력" 입력칸, 라벨
		PasswordField_pw_re = new JPasswordField();
		PasswordField_pw_re.setBounds(100, 380, 280, 35);
		contentPane.add(PasswordField_pw_re);

		JLabel lbl_password_re = new JLabel("PW 재입력 :");
		lbl_password_re.setLabelFor(PasswordField_pw_re);
		lbl_password_re.setFont(new Font("굴림", Font.BOLD, 14));
		lbl_password_re.setBounds(12, 385, 94, 25);
		contentPane.add(lbl_password_re);

		// "가입하기" 버튼
		JButton btn_signup = new JButton("가입하기");
		btn_signup.setFont(new Font("굴림", Font.BOLD, 14));
		btn_signup.setBounds(159, 486, 140, 35);
		contentPane.add(btn_signup);

		// "가입하기" 버튼 action
//		정상적으로 회원 정보가 입력되고 "가입하기"버튼을 누르면 DB 의 user_acc테이블에 insert
		btn_signup.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				String name_user = textField_name.getText(); // "이름" 상자에서 꺼내오기
				String tel_user = textField_tel.getText(); // "연락처" 상자에서 꺼내오기
				String id_user = textField_id.getText(); // "id" 상자에서 꺼내오기
				String pw_user = new String(PasswordField_pw.getPassword()); // pw 상자에서 꺼내오기
				// * JPasswordField의 반환값이 char[]이기 때문에 String으로 변환.
				String pw_re_user = new String(PasswordField_pw_re.getPassword()); // "pw재입력" 상자에서 꺼내오기

				signup_info = new SignupUserVO(name_user, tel_user, id_user, pw_user);

				try {
					signup_dao = new SignupUserDAO();

					/*
					 * 예외 상황 처리 우선순위 : id중복 > 이름규정 > 연락처규정 > id규정 > pw규정 > pw불일치
					 */
					if (signup_dao.isExist(id_user)) { // id 중복 확인
						// **user_id가 이미 테이블의 PK이기 때문에 id중복현상이 일어나면 자동으로 insertion error 뜨긴 한다.

						JOptionPane.showMessageDialog(null, "이미 존재하는 ID입니다.", "경고", JOptionPane.WARNING_MESSAGE);
					} else if (!signup_dao.isNameMatch(name_user)) { // 이름 규정 확인
						JOptionPane.showMessageDialog(null, "이름은 한글 또는 영문 조합으로 입력해주세요.", "경고",
								JOptionPane.WARNING_MESSAGE);
					} else if (!signup_dao.isTelMatch(tel_user)) { // 연락처 규정 확인
						JOptionPane.showMessageDialog(null, "연락처를 알맞게 입력해주세요.\n(예: '-' 제외)", "경고",
								JOptionPane.WARNING_MESSAGE);
					} else if (!signup_dao.isIdMatch(id_user)) { // id 규정 확인
						JOptionPane.showMessageDialog(null, "아이디는 영문포함 4~15 자로 입력해주세요.", "경고",
								JOptionPane.WARNING_MESSAGE);
					} else if (!signup_dao.isPwMatch(pw_user)) { // pw 규정 확인
						JOptionPane.showMessageDialog(null, "비밀번호는 영문+숫자+특문 조합 8~20 자로 입력해주세요.", "경고",
								JOptionPane.WARNING_MESSAGE);
					} else if (signup_dao.isPwIncorrect(pw_user, pw_re_user)) { // 비밀번호 불일치 확인
						JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.", "경고", JOptionPane.WARNING_MESSAGE);
					} else { // 입력값 모두 정상
						// SignupUserMain -> SignupUserComplete
						dispose(); // 개발자가 직접 메모리 해제 (frame 닫기)
						setVisible(false); // "회원가입"창 닫기
						C02_SignupComplete frame_signupcpl = new C02_SignupComplete(signup_info, signup_dao);
						frame_signupcpl.setVisible(true); // "회원가입완료"창의 *객체를 생성* 후 보이게
						frame_signupcpl.setLocationRelativeTo(null); // 창이 화면 가운데에 표시

					}
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

	}
}
