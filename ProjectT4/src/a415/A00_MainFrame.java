package a415;


import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class A00_MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					A00_MainFrame frame = new A00_MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public A00_MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 640);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("운영자 로그인");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose(); 
                new B01_LoginAdmin().setVisible(true);
			}
		});
		btnNewButton.setBounds(294, 10, 164, 63);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("환영합니다!!!");
		lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 20));
		lblNewLabel.setBounds(10, 71, 448, 92);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton_1 = new JButton("손님 로그인");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose(); 
                new a415.A01_LoginUser().setVisible(true);
			}
		});
		btnNewButton_1.setFont(new Font("굴림", Font.PLAIN, 75));
		btnNewButton_1.setBounds(10, 159, 448, 159);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_1_1 = new JButton("손님 가입");
		btnNewButton_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose(); 
                new C01_SignupUserMain().setVisible(true);
			}
		});
		btnNewButton_1_1.setFont(new Font("굴림", Font.PLAIN, 80));
		btnNewButton_1_1.setBounds(10, 328, 448, 159);
		contentPane.add(btnNewButton_1_1);
		
		// "예약 취소" 버튼
		JButton btnNewButton_2 = new JButton("예약 취소");
		btnNewButton_2.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        new B03_CancelForm(); 
		    }
		});

		btnNewButton_2.setFont(new Font("굴림", Font.PLAIN, 18));
		btnNewButton_2.setBounds(294, 528, 158, 52);
		contentPane.add(btnNewButton_2);
		
		// "내 예약 보기" 버튼
		JButton btnNewButton_3 = new JButton("내 예약 보기");
		btnNewButton_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new B04_ShowUserRsv();
			}
		});
		
		btnNewButton_3.setFont(new Font("굴림", Font.PLAIN, 18));
		btnNewButton_3.setBounds(108, 528, 158, 52);
		contentPane.add(btnNewButton_3);
		
		
		
	}
}
