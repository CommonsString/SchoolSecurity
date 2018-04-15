package entity;

//部门类
public class Department {
	private String depId;//部门id
	private String number;//编号
	private String name;//部门名
	private String shortName;//简称
	public String getDepId() {
		return depId;
	}
	public void setDepId(String depId) {
		this.depId = depId;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	@Override
	public String toString() {
		return "Department [depId=" + depId + ", number=" + number + ", name=" + name + ", shortName=" + shortName
				+ "]";
	}
	
}
