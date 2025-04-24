package a415;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
//import java.awt.FlowLayout;
//import db.DatabaseHelper;
import db.DatabaseHelper.CancelDAO;
import db.DatabaseHelper.UserDAO;

import java.awt.Font;

public class B03_CancelForm extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtUserId;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancelReservation;

    private String loggedInUserId;
    

    public B03_CancelForm() {
        setTitle("예약 취소");
        setSize(475, 262);
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

        btnCancelReservation = new JButton("예약 취소");
        btnCancelReservation.setFont(new Font("굴림", Font.PLAIN, 14));
        btnCancelReservation.setBounds(163, 151, 100, 30);
        btnCancelReservation.setEnabled(false); // 로그인 전엔 비활성화
        getContentPane().add(btnCancelReservation);

        btnLogin.addActionListener(e -> handleLogin());
        btnCancelReservation.addActionListener(e -> handleCancelReservation());

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
            btnCancelReservation.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "로그인 실패. 다시 시도하세요.");
        }
    }

    private void handleCancelReservation() {
        int confirm = JOptionPane.showConfirmDialog(this, "정말로 예약을 취소하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean result = CancelDAO.cancelReservationByUserId(loggedInUserId);
            if (result) {
                JOptionPane.showMessageDialog(this, "예약이 성공적으로 취소되었습니다.");
                dispose(); // 창 닫기
            } else {
                JOptionPane.showMessageDialog(this, "예약 취소에 실패했습니다. 예약이 없거나 오류가 발생했습니다.");
            }
        }
    }
}

