package a415;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import db.DatabaseHelper;
import db.UserRsvMenuVO;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class A03_MenuForm extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textA;
	private Map<String, Integer> clickCountMap = new HashMap<>();
	private Map<Integer, Integer> menuIdMenuCountMap = new HashMap<>(); //////////

	// 메뉴판 정보 List
	private ArrayList<String> foodNames = new ArrayList<>();
	private ArrayList<Integer> foodPrices = new ArrayList<>();
	private ArrayList<String> foodImages = new ArrayList<>();

	// map<menuId, ArrayList>의 values 로 foodNames, foodPrices, foodImages 리스트 생성
	LinkedHashMap<Integer, db.MenuInfo> map = DatabaseHelper.getRestMenu(DatabaseHelper.userRsvVO.getRestId());
	// ^^ map을 menu_id순서대로 정렬

	public static void setList(HashMap<Integer, db.MenuInfo> map, ArrayList<String> foodNames,
			ArrayList<Integer> foodPrices, ArrayList<String> foodImages) {
		for (int key : map.keySet()) {
			foodNames.add(map.get(key).getMenuName());
			foodPrices.add(map.get(key).getMenuPrice());
			foodImages.add(map.get(key).getImgPath());
		}
		// 콘솔확인용 ////////////
//		 for loop (keySet())
//		System.out.println("[map key & value] ");
//		Set<Integer> keySet = map.keySet();
//		for (Integer key : keySet) {
//			System.out.println(key + " : " + map.get(key));
//		}
//		System.out.println("map 크기: " + map.size());
	}

	/*
	 * public A03_MenuForm(int restId) { this.restId = restId; initializeUI(); }
	 */

	public A03_MenuForm() {
		initializeUI();
	}

	private void initializeUI() {
		setList(map, foodNames, foodPrices, foodImages);

		setTitle("메뉴 선택창");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 640);
		contentPane = new JPanel();
		contentPane.setBackground(Color.ORANGE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// "선택" 버튼
		JButton selectButton = new JButton("선택");
		selectButton.setFont(new Font("굴림", Font.PLAIN, 18));
		selectButton.setBounds(140, 554, 159, 37);
		selectButton.addActionListener(e -> {
			// 버튼 누르면 예약 확인 창으로 넘어감

			// menuIdMenuCountMap에 있는 <menuId, menuCount> 쌍 모두 꺼내서 VO에
			menuIdMenuCountMap.forEach((menuId, menuCount) -> {
				// UserRsvMenuVO < 메뉴 하나에 대한 정보
				DatabaseHelper.urmVOList.add(new UserRsvMenuVO(-1, DatabaseHelper.userRsvVO.getUserRsvId(), menuId, menuCount)); 
			});

			// 콘솔확인용 //////////////
//			System.out.println("<MenuForm PrintOut>");
//			System.out.println("UserId: " + DatabaseHelper.userRsvVO.getUserId());
//			System.out.println("RestId: " + DatabaseHelper.userRsvVO.getRestId());
//			for (UserRsvMenuVO VOItem : DatabaseHelper.urmVOList) {
//				System.out.println("menuId: " + VOItem.getMenuId() + " / " + "menuCount: " + VOItem.getMenuCount());
//			}

			dispose();
			new A04_ReserveForm().setVisible(true);
		});

		contentPane.add(selectButton);

		// "초기화" 버튼
		JButton btnNewButton = new JButton("초기화");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 누르면 선택한 메뉴 전체 초기화
				// 1) menuIdMenuCountMap, clickCountMap 초기화
				menuIdMenuCountMap.clear();
				clickCountMap.clear();	////////////////
				// 2) 선택한 메뉴 창 택스트필드 문구 초기화
				textA.setText("");
			}
		});
		btnNewButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		btnNewButton.setBounds(363, 474, 91, 23);
		contentPane.add(btnNewButton);
		
		
		textField = new JTextField("선택한 메뉴 확인 창");
		textField.setBounds(0, 471, 464, 30);
		textField.setEditable(false); // 텍스트 편집 불가
		contentPane.add(textField);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 498, 464, 46);
		contentPane.add(scrollPane);

		textA = new JTextField();
		textA.setEditable(false);
		scrollPane.setViewportView(textA);
		
		

		createFoodButtons();
	}

	// 버튼 생성
	private void createFoodButtons() {

		JButton[] buttons = new JButton[map.size()]; ////////////////
		int index = 0;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 4; col++) {
				if (index >= foodNames.size())
					break;

				int x = col * 116;
				int y = row * 157;
				
				////////////////
				// 이미지 버튼
				buttons[index] = new JButton();
				buttons[index].setIcon(new ImageIcon(A03_MenuForm.class.getResource(foodImages.get(index).toString())));
				buttons[index].setBounds(x, y, 116, 130);
				final String food = foodNames.get(index);
				final int price = foodPrices.get(index);
				buttons[index].addActionListener(
						createFoodButtonListener(index, DatabaseHelper.userRsvVO.getRestId(), food, price));

				contentPane.add(buttons[index]);


				// 메뉴 이름 텍스트 필드
				JTextField foodLabel = new JTextField(food);
				foodLabel.setHorizontalAlignment(SwingConstants.CENTER);
				foodLabel.setBounds(x, y + 130, 65, 30);
				foodLabel.setEditable(false);
				contentPane.add(foodLabel);
				
				// 메뉴 가격 텍스트 필드
				JTextField priceLabel = new JTextField(price+"원");
				priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
				priceLabel.setBounds(x + 65, y + 130, 116-65, 30);
				priceLabel.setEditable(false);
				contentPane.add(priceLabel);

				index++;
			}
		}
	}

	private ActionListener createFoodButtonListener(int index, int restId, String food, int price) {
		return e -> handleFoodClick(index, restId, food, price);
	}

	private void handleFoodClick(int index, int restId, String food, int price) {
		// 클릭된 메뉴의 수량 업데이트
		int count = clickCountMap.getOrDefault(food, 0) + 1;
		count = clickCountMap.getOrDefault(food, 0) + 1;
		clickCountMap.put(food, count);
		// <메뉴 아이디, 수량> 추가 >> DB 저장용
		menuIdMenuCountMap.put(index + 12 * (restId - 1) + 1, count); 

		// 화면에 표시할 텍스트 업데이트
		String displayText = (count > 1) ? food 
				+ " (" + count + ") - " 
				+ price + "원" 
				: food + " - " 
				+ price + "원";
		String currentText = textA.getText().trim();
		StringBuilder updatedText = new StringBuilder();
		
		boolean updated = false;
		for (String item : currentText.split(",\\s*")) {
			if (!item.isBlank()) {
				String name = item.split(" \\(")[0].split(" - ")[0].trim();
				if (name.equals(food)) {
					updatedText.append(displayText);
					updated = true;
				} else {
					updatedText.append(item);
				}
				updatedText.append(", ");
			}
		}

		if (!updated) {
			if (updatedText.length() > 0)
				updatedText.append(" ");
			updatedText.append(displayText);
		}

		textA.setText(updatedText.toString().replaceAll(",\\s*$", ""));

	}
}
