package a415;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import db.DatabaseHelper.ShowUserRsvDAO;
import db.DatabaseHelper.UserDAO;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JTable;

public class B04_ShowUserRsv extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtUserId;
	private JPasswordField txtPassword;
	private JButton btnLogin;
	private JButton btnMyRsvList;

	private String loggedInUserId;
	private JTable table;
	private JScrollPane scrollPane;

	public B04_ShowUserRsv() {
		setTitle("나의 예약 확인");
		setSize(475, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JLabel label = new JLabel("ID:");
		label.setFont(new Font("굴림", Font.PLAIN, 15));
		label.setBounds(38, 61, 77, 30);
		getContentPane().add(label);
		txtUserId = new JTextField(15);
		txtUserId.setBounds(127, 62, 187, 30);
		getContentPane().add(txtUserId);

		JLabel label_1 = new JLabel("Password:");
		label_1.setFont(new Font("굴림", Font.PLAIN, 15));
		label_1.setBounds(38, 111, 77, 15);
		getContentPane().add(label_1);
		txtPassword = new JPasswordField(15);
		txtPassword.setBounds(127, 104, 187, 30);
		getContentPane().add(txtPassword);

		btnLogin = new JButton("로그인");
		btnLogin.setBounds(347, 77, 77, 39);
		getContentPane().add(btnLogin);

		btnMyRsvList = new JButton("예약 확인");
		btnMyRsvList.setFont(new Font("굴림", Font.PLAIN, 14));
		btnMyRsvList.setBounds(163, 151, 100, 30);
		btnMyRsvList.setEnabled(false); // 로그인 전엔 비활성화
		getContentPane().add(btnMyRsvList);

		// 예약 목록 보여주는 표
		table = new JTable();
		table.setEnabled(false); // 로그인 되기 전에는 disable

		// scrollPane
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(29, 205, 408, 300);
		scrollPane.setViewportView(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));

		getContentPane().add(scrollPane);

		btnLogin.addActionListener(e -> handleLogin());
		btnMyRsvList.addActionListener(e -> handleUserRsvList());

		setVisible(true);
	}

	private void handleLogin() {
		String userId = txtUserId.getText().trim();
		String password = new String(txtPassword.getPassword()).trim();

		if (userId.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "ID와 비밀번호를 입력하세요.");
			return;
		}

		boolean loginSuccess = UserDAO.login(userId, password);
		if (loginSuccess) {
			JOptionPane.showMessageDialog(this, "로그인 성공!");
			loggedInUserId = userId;
			btnMyRsvList.setEnabled(true);
			table.setEnabled(true);
		} else {
			JOptionPane.showMessageDialog(this, "로그인 실패. 다시 시도하세요.");
		}
	}

	private void handleUserRsvList() {
		// table에 getUserRsvList로 가져온 유저의 예약 목록 패딩
		String[] coulumNames = { "식당명", "인원 수", "예약시간" }; // table의 헤더 칼럼
		String[][] data = ArrListTo2dString(
				ShowUserRsvDAO.getUserRsvList(loggedInUserId));

		table = new JTable();
		// 예약 목록이 없으면 table패널 위에 "조회결과없음" 출력
		if (data.length == 0) {
			JLabel noDataLabel = new JLabel("조회 결과 없음", SwingConstants.CENTER);
			noDataLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
			noDataLabel.setForeground(Color.GRAY);
			scrollPane.setViewportView(noDataLabel); // table 대신 label 띄움
		} else {
			DefaultTableModel model = new DefaultTableModel(data, coulumNames) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false; // 모든 셀 편집 불가능
				}
			};

			table.setModel(model); // table에 model set. *행,열 높이,너비 조절은 table에 model이 set된 이후부터 유효하다.
			table.setFont(new Font("굴림", Font.PLAIN, 15));
			table.setRowHeight(50); // 각 행 높이

			JTableHeader header = table.getTableHeader();
			header.setFont(new Font("맑은 고딕", Font.BOLD, 16));
			header.setBackground(new Color(200, 221, 242)); // 연한 파랑

			TableColumnModel columnModel = table.getColumnModel();
			columnModel.getColumn(0).setPreferredWidth(40);
			columnModel.getColumn(1).setPreferredWidth(40);
			columnModel.getColumn(2).setPreferredWidth(100);

			table.setBounds(10, 80, 448, 515);
			scrollPane.setViewportView(table);

			table.setEnabled(true);
		}
	}

	// ArrayList<ArrayList<String>> -> String[][]로 변형 메소드
	private String[][] ArrListTo2dString(ArrayList<ArrayList<String>> arrayList) {
		String[][] result = new String[arrayList.size()][];

		for (int i = 0; i < arrayList.size(); i++) {
			ArrayList<String> innerList = arrayList.get(i);
			result[i] = new String[innerList.size()]; // 각 행 초기화

			for (int j = 0; j < innerList.size(); j++) {
				result[i][j] = innerList.get(j);
			}
		}

		return result;
	}
}
