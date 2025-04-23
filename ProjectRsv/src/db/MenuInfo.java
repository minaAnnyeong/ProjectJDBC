package db;

public class MenuInfo {
	private String menuName;
	private int menuPrice;
	private String imgPath;
	
	public MenuInfo() { }
	
	public MenuInfo(String menuName, int menuPrice, String imgPath) {
		super();
		this.menuName = menuName;
		this.menuPrice = menuPrice;
		this.imgPath = imgPath;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public int getMenuPrice() {
		return menuPrice;
	}
	public void setMenuPrice(int menuPrice) {
		this.menuPrice = menuPrice;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	
	
}
