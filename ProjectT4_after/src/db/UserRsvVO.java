package db;

public class UserRsvVO {
	private int userRsvId; // Sequence of TABLE user_rsv
	private String userId; // FK from TABLE user_acc
	private int restId; // FK from TABLE resta_se
	private int userCount;
	private String rsvTime;
	
	public UserRsvVO() { } 
	
	public UserRsvVO(int userRsvId, String userId, int restId, int userCount, String rsvTime) {
		super();
		this.userRsvId = userRsvId;
		this.userId = userId;
		this.restId = restId;
		this.userCount = userCount;
		this.rsvTime = rsvTime;
	}

	public int getUserRsvId() {
		return userRsvId;
	}

	public void setUserRsvId(int userRsvId) {
		this.userRsvId = userRsvId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getRestId() {
		return restId;
	}

	public void setRestId(int restId) {
		this.restId = restId;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public String getRsvTime() {
		return rsvTime;
	}

	public void setRsvTime(String rsvTime) {
		this.rsvTime = rsvTime;
	}
	
	
	
}
