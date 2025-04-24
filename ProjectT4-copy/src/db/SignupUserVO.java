package db;

public class SignupUserVO {
	private String name;
	private String tel;
	private String id;
	private String pw;
	
	public SignupUserVO() { }

	public SignupUserVO(String name, String tel, String id, String pw) {
		super();
		this.name = name;
		this.tel = tel;
		this.id = id;
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
	
	
	
	
}
