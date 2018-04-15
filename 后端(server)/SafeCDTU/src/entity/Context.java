package entity;

import java.util.Date;

public class Context {

	
	@Override
	public String toString() {
		return "Context [txtId=" + txtId + ", uId=" + uId + ", title=" + title + ", time=" + time + ", depId=" + depId
				+ ", content=" + content + ", needConfig=" + needConfig + ", isSee=" + isSee + "]";
	}
	
	private String txtId;//文章Id
	private String uId;//发布人Id
	private String title;//文章标题
	private Date time;//发布时间
	private String depId;//部门Id
	private String content;//文章内容
	private String needConfig;//是否定点发送
	private String isSee;//是否查看
	
	
	
	public String getTxtId() {
		return txtId;
	}
	public void setTxtId(String txtId) {
		this.txtId = txtId;
	}
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getDepId() {
		return depId;
	}
	public void setDepId(String depId) {
		this.depId = depId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getNeedConfig() {
		return needConfig;
	}
	public void setNeedConfig(String needConfig) {
		this.needConfig = needConfig;
	}
	public String getIsSee() {
		return isSee;
	}
	public void setIsSee(String isSee) {
		this.isSee = isSee;
	}
	
	
	
}
