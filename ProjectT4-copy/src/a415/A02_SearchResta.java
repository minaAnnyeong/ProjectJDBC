package a415;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import db.DBConn;
import db.DatabaseHelper;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class A02_SearchResta extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    private JComboBox<String> comboBox;
    private JLabel resultLabel;
    private JTextField textField;
    private JButton btnNewButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                A02_SearchResta frame = new A02_SearchResta();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public A02_SearchResta() {
        setTitle("레스토랑 검색");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 640, 480);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("레스토랑 검색");
        lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 20));
        lblNewLabel.setBounds(261, 45, 99, 27);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("레스토랑 이름");
        lblNewLabel_1.setFont(new Font("굴림", Font.BOLD, 20));
        lblNewLabel_1.setBounds(21, 121, 144, 42);
        contentPane.add(lblNewLabel_1);

        // 콤보박스 설정
        comboBox = new JComboBox<>();
        comboBox.setBounds(177, 184, 275, 55);
        contentPane.add(comboBox);

        comboBox.addActionListener(e -> {
            String selected = (String) comboBox.getSelectedItem();
            if (selected != null && !selected.equals("레스토랑을 선택해주세요")) {
                btnNewButton.setEnabled(true);
                btnNewButton.setOpaque(true);
                btnNewButton.setBackground(new Color(0, 120, 215));
                resultLabel.setText("검색 결과: " + selected);
            } else {
                btnNewButton.setEnabled(false);
                btnNewButton.setOpaque(false);
                btnNewButton.setBackground(new Color(169, 169, 169));
                resultLabel.setText("검색 결과:");
            }
        });

        // 텍스트 필드
        textField = new JTextField();
        textField.setBounds(177, 121, 275, 48);
        contentPane.add(textField);
        textField.setColumns(10);

        // 검색 결과 레이블
        resultLabel = new JLabel("검색 결과:");
        resultLabel.setFont(new Font("굴림", Font.BOLD, 14));
        resultLabel.setBounds(60, 255, 300, 30);
        contentPane.add(resultLabel);

        // 버튼 설정
        btnNewButton = new JButton("검색");
        btnNewButton.setFont(new Font("굴림", Font.BOLD, 20));
        btnNewButton.setBounds(263, 295, 104, 42);
        contentPane.add(btnNewButton);
        btnNewButton.setEnabled(false);

        // 버튼 클릭 시 이벤트 처리
        btnNewButton.addActionListener(e -> {
            if (!btnNewButton.isEnabled()) return;

            int selectedRestId = getSelectedRestId(); // 선택된 레스토랑 ID 가져오기
            
            if (selectedRestId == -1) {
                JOptionPane.showMessageDialog(null, "레스토랑을 선택해주세요.");
                return;
            }
            DatabaseHelper.userRsvVO.setRestId(selectedRestId);
            System.out.println("<SearchRest PrintOut>");
            System.out.println("UserId: " + DatabaseHelper.userRsvVO.getUserId());
			System.out.println("RestId: " + DatabaseHelper.userRsvVO.getRestId());
            dispose(); // 현재 창 닫기
            new A03_MenuForm().setVisible(true); // 새로운 메뉴 창 열기
        });

        // 키 이벤트 리스너
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String keyword = textField.getText().trim();

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchRestaurant(keyword);
                } else {
                    if (!keyword.isEmpty()) {
                        updateComboBoxBasedOnKeyword(keyword);
                    } else {
                        loadRestaurantsFromDB(); // 모든 레스토랑 로드
                        resultLabel.setText("검색 결과:");
                        btnNewButton.setEnabled(false);
                        btnNewButton.setOpaque(false);
                        btnNewButton.setBackground(new Color(169, 169, 169));
                    }
                }
            }
        });

        loadRestaurantsFromDB(); // 초기 데이터 로드
    }

    // DB에서 레스토랑 정보 로드
    private void loadRestaurantsFromDB() {
        try (Connection conn = DBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT rest_name FROM resta_se")) {

            comboBox.removeAllItems();
            comboBox.addItem("레스토랑을 선택해주세요");

            while (rs.next()) {
                comboBox.addItem(rs.getString("rest_name"));
            }

            comboBox.setSelectedIndex(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 키워드 기반 콤보박스 업데이트
    private void updateComboBoxBasedOnKeyword(String keyword) {
        try (Connection conn = DBConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT rest_name FROM resta_se WHERE rest_name LIKE ?")) {

            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            comboBox.removeAllItems();
            comboBox.addItem("레스토랑을 선택해주세요");

            boolean found = false;
            while (rs.next()) {
                found = true;
                comboBox.addItem(rs.getString("rest_name"));
            }

            comboBox.setSelectedIndex(0);

            if (!found) {
                resultLabel.setText("검색 결과: 없음");
                btnNewButton.setEnabled(false);
                btnNewButton.setOpaque(false);
                btnNewButton.setBackground(new Color(169, 169, 169));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 레스토랑 ID 검색
    private int getSelectedRestId() {
        String selectedName = (String) comboBox.getSelectedItem();

        if (selectedName == null || selectedName.equals("레스토랑을 선택해주세요")) {
            return -1;
        }

        int restId = -1;
        try (Connection conn = DBConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT rest_id FROM resta_se WHERE rest_name = ?")) {

            pstmt.setString(1, selectedName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                restId = rs.getInt("rest_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return restId;
    }

    // 키워드로 레스토랑 검색
    private void searchRestaurant(String keyword) {
        if (keyword.isEmpty()) {
            resultLabel.setText("검색 결과:");
            btnNewButton.setEnabled(false);
            btnNewButton.setOpaque(false);
            btnNewButton.setBackground(new Color(169, 169, 169));
            return;
        }

        try (Connection conn = DBConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT rest_name FROM resta_se WHERE rest_name LIKE ?")) {

            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String result = rs.getString("rest_name");
                resultLabel.setText("검색 결과: " + result);

                for (int i = 0; i < comboBox.getItemCount(); i++) {
                    if (comboBox.getItemAt(i).equalsIgnoreCase(result)) {
                        comboBox.setSelectedIndex(i);
                        break;
                    }
                }

                btnNewButton.setEnabled(true);
                btnNewButton.setOpaque(true);
                btnNewButton.setBackground(new Color(0, 120, 215));

            } else {
                resultLabel.setText("검색 결과: 없음");
                btnNewButton.setEnabled(false);
                btnNewButton.setOpaque(false);
                btnNewButton.setBackground(new Color(169, 169, 169));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}