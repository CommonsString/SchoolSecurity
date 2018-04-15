package entity;

public class User {
	
	
	private String uId; //用户id
	private String account;//用户账户
	@Override
	public String toString() {
		return "User [uId=" + uId + ", account=" + account + ", pswd=" + pswd + ", name=" + name + ", sex=" + sex
				+ ", tel=" + tel + ", depId=" + depId + ", email=" + email + ", position=" + position + ", state="
				+ state + ", level=" + level + ", veCode=" + veCode + ", parId=" + parId + ", isNewReply=" + isNewReply
				+ ", isNewPotNotice=" + isNewPotNotice + ", isNewDepNotice=" + isNewDepNotice + ", isNewEveNotice="
				+ isNewEveNotice + "]";
	}
	private String pswd;//用户密码
	private String name;//用户姓名
	private String sex;//用户性别
	private String tel;//电话号码
	private String depId;//部门id
	private String email;//邮箱
	private String position;//职位
	private int state;//是否已经登录
	private int level;//用户权限等级
	private String veCode;//验证码
	private String parId;//父Id
	private int isNewReply;//是否有新的事件回复
	private int isNewPotNotice;//是否有新的定点通知
	private int isNewDepNotice;//是否有新的部门通知
	private int isNewEveNotice;//是否有新的事件
	
	public int getIsNewPotNotice() {
		return isNewPotNotice;
	}
	public void setIsNewPotNotice(int isNewPotNotice) {
		this.isNewPotNotice = isNewPotNotice;
	}
	public int getIsNewDepNotice() {
		return isNewDepNotice;
	}
	public void setIsNewDepNotice(int isNewDepNotice) {
		this.isNewDepNotice = isNewDepNotice;
	}
	public int getIsNewEveNotice() {
		return isNewEveNotice;
	}
	public void setIsNewEveNotice(int isNewEveNotice) {
		this.isNewEveNotice = isNewEveNotice;
	}
	
	public int getIsNewReply() {
		return isNewReply;
	}
	public void setIsNewReply(int isNewReply) {
		this.isNewReply = isNewReply;
	}
	///////////////////////////////////////////////////////////
	public String getParId() {
		return parId;
	}
	public void setParId(String parId) {
		this.parId = parId;
	}
	///////////////////////////////////////////////////////////
	public String getVeCode() {
		return veCode;
	}
	public void setVeCode(String veCode) {
		this.veCode = veCode;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPswd() {
		return pswd;
	}
	public void setPswd(String pswd) {
		this.pswd = pswd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getDepId() {
		return depId;
	}
	public void setDepId(String depId) {
		this.depId = depId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	
}
