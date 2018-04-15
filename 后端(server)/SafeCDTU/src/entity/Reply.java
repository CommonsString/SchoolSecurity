package entity;

import java.util.Date;

public class Reply {

   @Override
	public String toString() {
		return "Reply [repId=" + repId + ", eveId=" + eveId + ", repUserName=" + repUserName + ", repContext="
				+ repContext + ", repTime=" + repTime + "]";
	}
   
   
   private 	String repId;//回复消息的id
   private  String eveId;//回复所在事件的事件id
   private  String repUserName;//回复人的姓名
   private  String repContext;//回复内容
   private  Date repTime;//回复的时间
   
   public String getRepId() {
	return repId;
}
public void setRepId(String repId) {
	this.repId = repId;
}
public String getEveId() {
	return eveId;
}
public void setEveId(String eveId) {
	this.eveId = eveId;
}
public String getRepUserName() {
	return repUserName;
}
public void setRepUserName(String repUserName) {
	this.repUserName = repUserName;
}
public String getRepContext() {
	return repContext;
}
public void setRepContext(String repContext) {
	this.repContext = repContext;
}
public Date getRepTime() {
	return repTime;
}
public void setRepTime(Date repTime) {
	this.repTime = repTime;
}

}
