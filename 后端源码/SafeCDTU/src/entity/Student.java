package entity;


public class Student {
	
	private String stuNumber; 
	private String clasz; //关键字，小写
	private String name;
	private String tel;
	private String college;
	private String depId;
	//以下属性待定....
	
	@Override
	public String toString() {
		return "学号：-->  " + this.stuNumber + "班级-->  " 
	+ this.clasz + "姓名-->  " + 
				this.name + "电话号码-->  " + this.tel + "学院-->  " + this.college;
	}
	
	
	
	public String getDepId() {
		return depId;
	}


	public void setDepId(String depId) {
		this.depId = depId;
	}

	public String getStuNumber() {
		return stuNumber;
	}
	public void setStuNumber(String stuNumber) {
		this.stuNumber = stuNumber;
	}
	public String getClasz() {
		return clasz;
	}
	public void setClasz(String clasz) {
		this.clasz = clasz;
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
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
}
