package a415;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
////////////
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
////////////
import db.DatabaseHelper;
import db.UserRsvMenuVO;

public class A05_ReservationTime extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static final int WIDTH = 480;
	public static final int HEIGHT = 640;
	
	private LocalDate today = LocalDate.now();
	private String dateSelected = "";
	private String timeSelected = "";
	private String[] timeList = { "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00",
			"16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30" };


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					A05_ReservationTime frame_revtime = new A05_ReservationTime();
					frame_revtime.setVisible(true);
					frame_revtime.setResizable(false); // 화면 크기 조정 가능 여부
					frame_revtime.setSize(WIDTH, HEIGHT); // 창 크기 설정
					frame_revtime.setLocationRelativeTo(null); // 화면의 가운데에 창 띄우기
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

	public A05_ReservationTime() {
		setTitle("예약 시간 선택");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, WIDTH, HEIGHT);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// 예약 날짜 선택- Datepicker 객체
		UtilDateModel model = new UtilDateModel();
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
		getContentPane().add(datePicker);
		datePicker.setBounds(59, 10, 366, 25);
		datePicker.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		model.setDate(today.getYear(), 
				today.getMonthValue() - 1, 
				today.getDayOfMonth()); // 초기- system상 오늘 날짜
		dateSelected = model.getYear() 
				+ "-" + (model.getMonth() + 1) 
				+ "-" + model.getDay(); // 초기 - 오늘날짜
		
		model.setSelected(true);
		model.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("value".equals(evt.getPropertyName())) {
					dateSelected = model.getYear() 
							+ "-" + (model.getMonth() + 1) 
							+ "-" + model.getDay();
				}
				

			}
		});
		// Datepicker의 헤더
		JFormattedTextField datePickerHeader = datePicker.getJFormattedTextField();
		datePickerHeader.setFont(new Font("굴림", Font.BOLD, 18));
		datePickerHeader.setText("예약 날짜");
		datePickerHeader.setHorizontalAlignment(JLabel.CENTER);
		datePickerHeader.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		// 예약 시간 선택 리스트
		JList<String> list = new JList<String>(timeList);
		list.setFont(new Font("굴림", Font.BOLD, 18));
		list.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFixedCellHeight(50);

		// 리스트에 스트롤바 추가
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(59, 294, 366, 168);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(list);

		// 리스트 헤더 라벨
		JLabel lblListHeader = new JLabel("예약 시간");
		lblListHeader.setBackground(Color.WHITE);
		lblListHeader.setFont(new Font("굴림", Font.BOLD, 18));
		lblListHeader.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		lblListHeader.setHorizontalAlignment(JLabel.CENTER);
		scrollPane.setColumnHeaderView(lblListHeader);

		// 리스트 선택 action
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				timeSelected = (String) list.getSelectedValue();

			}
		});

		// "예약하기" 버튼
		JButton btnRsv = new JButton("예약하기");
		btnRsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 버튼 누르면 timeSelected가 rev_time에 저장
				// 선택하지 않았으면 -> 경고 팝업창

				// StringBuffer 확인
				System.out.println("timeSelected" + timeSelected);

				if(dateSelected.equals("")) {
					JOptionPane.showMessageDialog(null, "날짜 선택해 주세요.", "경고", JOptionPane.WARNING_MESSAGE);
				}
				else if (timeSelected.equals("")) {
					JOptionPane.showMessageDialog(null, "시간을 선택해 주세요.", "경고", JOptionPane.WARNING_MESSAGE);
				} else { // 선택한 시간이 있으면 예약하시겠습니까? 창으로 넘어감
					int result = JOptionPane.showConfirmDialog(null, "예약하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);

					if (result == JOptionPane.YES_OPTION) { // 유저가 YES를 누른경우
						DatabaseHelper.userRsvVO.setRsvTime(dateSelected + " " + timeSelected);
						// 콘솔확인용 //////////////////
						System.out.println("<ReservationForm PrintOut>");
						System.out.println("UserId: " + DatabaseHelper.userRsvVO.getUserId());
						System.out.println("RestId: " + DatabaseHelper.userRsvVO.getRestId());
						for (UserRsvMenuVO VOItem : DatabaseHelper.urmVOList) {
							System.out.println(
									"menuId: " + VOItem.getMenuId() + " / " + "menuCount: " + VOItem.getMenuCount());
						}
						System.out.println("UserCount: " + DatabaseHelper.userRsvVO.getUserCount());
						System.out.println("RsvTime: " + DatabaseHelper.userRsvVO.getRsvTime());
						System.out.println("UserRsvId: " + DatabaseHelper.userRsvVO.getUserRsvId());
						////////////////////////
						
						try {
		                	if(DatabaseHelper.insertUserReservation(DatabaseHelper.userRsvVO)) {
		                		// 헬퍼의 메소드로 rest_name, user_name, rsv_time 가져오기
		                		String[] rsvInfo = DatabaseHelper.getRsvInfo(DatabaseHelper.userRsvVO.getRestId(), DatabaseHelper.userRsvVO.getUserId());
		                		JOptionPane.showMessageDialog(null,
										"<html>예약이 확정되었습니다.<br>"
										+ "식당명: " + rsvInfo[0] + "<br>"
										+ "예약자명: " + rsvInfo[1] + "<br>"
										+ "예약시간: " + DatabaseHelper.userRsvVO.getRsvTime() + "</html>",
										"", JOptionPane.CLOSED_OPTION);
		                	}
		                	
		                	for(UserRsvMenuVO VOItem : DatabaseHelper.urmVOList) {
		                		DatabaseHelper.insertUserRsvMenu(DatabaseHelper.userRsvVO, VOItem);
		                	}
		                	
		                } catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		                
					} else if (result == JOptionPane.NO_OPTION) { // 유저가 NO를 누른경우
						// 그냥 팝업창 닫음

					} else { // 유저가 그냥 닫기를 한 경우
								// 그냥 팝업창 닫음

					}
				}
			}
		});
		btnRsv.setFont(new Font("굴림", Font.BOLD, 18));
		btnRsv.setBounds(160, 492, 156, 65);
		contentPane.add(btnRsv);

	}
}
