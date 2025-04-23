package db;

public class UserRsvMenuVO {
	private int userRsvMenuId; // Sequence of TABLE user_rsv_menu
	private int userRsvId; // FK Sequence from TABLE user_rsv
	private int menuId; // FK from TABLE menu
	private int menuCount;
	
	public UserRsvMenuVO() { }
	
	public UserRsvMenuVO(int userRsvMenuId, int userRsvId, int menuId, int menuCount) {
		super();
		this.userRsvMenuId = userRsvMenuId;
		this.userRsvId = userRsvId;
		this.menuId = menuId;
		this.menuCount = menuCount;
	}
	public int getUserRsvMenuId() {
		return userRsvMenuId;
	}
	public void setUserRsvMenuId(int userRsvMenuId) {
		this.userRsvMenuId = userRsvMenuId;
	}
	public int getUserRsvId() {
		return userRsvId;
	}
	public void setUserRsvId(int userRsvId) {
		this.userRsvId = userRsvId;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public int getMenuCount() {
		return menuCount;
	}
	public void setMenuCount(int menuCount) {
		this.menuCount = menuCount;
	}
	
	
}
