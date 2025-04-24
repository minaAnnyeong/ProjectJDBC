package a415;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import db.DBConn;
//import db.UserRsvVO;
import db.DatabaseHelper;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class A01_LoginUser extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField IDField;
	private JPasswordField PWField;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				A01_LoginUser frame = new A01_LoginUser();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	
	public A01_LoginUser() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitle = new JLabel("(일반손님) 로그인");
		lblTitle.setFont(new Font("굴림", Font.PLAIN, 20));
		lblTitle.setBounds(231, 29, 182, 43);
		contentPane.add(lblTitle);

		JLabel lblID = new JLabel("ID : ");
		lblID.setFont(new Font("굴림", Font.PLAIN, 20));
		lblID.setBounds(140, 118, 57, 32);
		contentPane.add(lblID);

		JLabel lblPW = new JLabel("PW :");
		lblPW.setFont(new Font("굴림", Font.PLAIN, 20));
		lblPW.setBounds(140, 218, 57, 32);
		contentPane.add(lblPW);

		IDField = new JTextField();
		IDField.setBounds(209, 111, 189, 51);
		contentPane.add(IDField);
		IDField.setColumns(10);

		JButton Login_Button = new JButton("로그인");
		Login_Button.setFont(new Font("굴림", Font.PLAIN, 20));
		Login_Button.setBounds(231, 313, 167, 43);
		contentPane.add(Login_Button);

		PWField = new JPasswordField();
		PWField.setBounds(209, 211, 189, 51);
		contentPane.add(PWField);
		PWField.setColumns(10);
		PWField.addActionListener(e -> Login_Button.doClick());

		// 기존 btnNewButton 제거하고
		// 새로운 JCheckBox 생성해서 PWField 아래에 배치
		JCheckBox showPassword = new JCheckBox("비밀번호 보기");
		showPassword.setFont(new Font("굴림", Font.PLAIN, 16));
		showPassword.setBounds(410, 220, 150, 30);
		contentPane.add(showPassword);

		// 체크박스 이벤트 처리
		showPassword.addActionListener(e -> {
			if (showPassword.isSelected()) {
				PWField.setEchoChar((char) 0); // 보이게
			} else {
				PWField.setEchoChar('•'); // 다시 가리기
			}
		});

		// 로그인 버튼 이벤트
		Login_Button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String id = IDField.getText().trim();
				String password = new String(PWField.getPassword());

				if (id.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(null, "아이디와 비밀번호를 모두 입력해주세요.");
					return;
				}

				try {
					Connection conn = DBConn.getConnection();
					PreparedStatement pstmt = conn
							.prepareStatement("SELECT * FROM user_acc WHERE user_id = ? AND user_pw = ?");

					pstmt.setString(1, id);
					pstmt.setString(2, password);

					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {
						JOptionPane.showMessageDialog(null, "로그인 성공!");

						// 로그인 성공하면 VO객체 rsv_info에 유저id set
						DatabaseHelper.userRsvVO.setUserId(id);
						
						// 콘솔 확인용 //////////
//						System.out.println("<LoginUser PrintOut>");
//						System.out.println("UserId: " + DatabaseHelper.userRsvVO.getUserId());

						// 로그인 성공 후 로그인 화면 종료하고 SearchResta 화면으로 이동
						dispose();
						new A02_SearchResta().setVisible(true); // SearchResta 화면을 새로 엶

					} else {
						JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호가 올바르지 않습니다.");
					}

					rs.close();
					pstmt.close();
					conn.close();

				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "오류 발생: " + ex.getMessage());
				}

			}
		});
	}
}
