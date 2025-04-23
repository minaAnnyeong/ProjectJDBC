package a415;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;
//import javax.swing.JTextPane;
//import java.awt.Panel;

import db.SignupUserDAO;
import db.SignupUserVO;

public class C02_SignupComplete extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static final int WIDTH = 480;
	public static final int HEIGHT = 300;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					C02_SignupComplete frame_signupcpl = new C02_SignupComplete();
					frame_signupcpl.setVisible(true); // 화면 보이게 하기
					frame_signupcpl.setSize(WIDTH, HEIGHT); // 창 크기 설정
					frame_signupcpl.setLocationRelativeTo(null); // 화면의 가운데에 창 띄우기
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public C02_SignupComplete() {
	}

	public C02_SignupComplete(SignupUserVO signup_info, SignupUserDAO signup_dao) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		// "인삿말" 라벨
		JLabel lbl_done = new JLabel("회원 가입 하시겠습니까?");
		lbl_done.setBackground(new Color(255, 255, 128));
		lbl_done.setFont(new Font("굴림", Font.BOLD, 18));
		lbl_done.setBounds(127, 23, 209, 48);
		lbl_done.setHorizontalAlignment(JLabel.CENTER);
		contentPane.add(lbl_done);

		// 회원가입 정보 라벨
		JLabel lbl_userinfo = new JLabel("");
		lbl_userinfo.setText("<html>이름 : " + signup_info.getName() + "<br>" + "연락처 : " + signup_info.getTel() + "<br>"
				+ "ID : " + signup_info.getId() + "</html>");
		lbl_userinfo.setFont(new Font("굴림", Font.BOLD, 14));
		lbl_userinfo.setBounds(80, 73, 311, 108);
		lbl_userinfo.setHorizontalAlignment(JLabel.CENTER);
		contentPane.add(lbl_userinfo);

		// "확인" 버튼
		JButton btn_confirm = new JButton("확인");
		btn_confirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// "확인" 버튼 누르면 최종적으로 회원가입 정보를 DB에 insert
				signup_dao.insertUseracc(signup_info);
				// 창 닫기
				dispose(); // 개발자가 직접 메모리 해제 (frame 닫기)
				setVisible(false); // "회원가입완료"창 닫기
				JOptionPane.showMessageDialog(null, 
						"가입되었습니다.", 
						"가입완료", 
						JOptionPane.PLAIN_MESSAGE);
			}
		});
		btn_confirm.setFont(new Font("굴림", Font.BOLD, 14));
		btn_confirm.setBounds(80, 200, 97, 35);
		contentPane.add(btn_confirm);

		// "돌아가기" 버튼
		JButton btn_back = new JButton("돌아가기");
		btn_back.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// "돌아가기" 버튼 누르면 새 "일반손님 회원가입" 창으로 돌아간다.
				dispose(); // 개발자가 직접 메모리 해제 (frame 닫기)
				setVisible(false); // "회원가입 확인"창 닫기
				C01_SignupUserMain frame_signupuser = new C01_SignupUserMain();
				frame_signupuser.setVisible(true); // "회원가입완료"창의 *객체를 생성* 후 보이게
				frame_signupuser.setLocationRelativeTo(null); // 창이 화면 가운데에 표시
			}
		});
		btn_back.setFont(new Font("굴림", Font.BOLD, 14));
		btn_back.setBounds(295, 200, 97, 35);
		contentPane.add(btn_back);
	}
}
