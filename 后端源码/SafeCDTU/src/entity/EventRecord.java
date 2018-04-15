package entity;

import java.util.Date;


public class EventRecord {
	
	@Override
	public String toString() {
		return "EventRecord [eveId=" + eveId + ", title=" + title + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", replyTime=" + replyTime + ", discribe=" + discribe + ", doResult=" + doResult + ", process="
				+ process + ", areId=" + areId + ", submitUserId=" + submitUserId + ", executeUserId=" + executeUserId
				+ ", isNewTopReply=" + isNewTopReply + ", isNewSubReply=" + isNewSubReply + "]";
	}
	
	public String getEveId() {
		return eveId;
	}
	public void setEveId(String eveId) {
		this.eveId = eveId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}
	public String getDiscribe() {
		return discribe;
	}
	public void setDiscribe(String discribe) {
		this.discribe = discribe;
	}
	public String getDoResult() {
		return doResult;
	}
	public void setDoResult(String doResult) {
		this.doResult = doResult;
	}
	public int getProcess() {
		return process;
	}
	public void setProcess(int process) {
		this.process = process;
	}
	public String getAreId() {
		return areId;
	}
	public void setAreId(String areId) {
		this.areId = areId;
	}
	public String getSubmitUserId() {
		return submitUserId;
	}
	public void setSubmitUserId(String submitUserId) {
		this.submitUserId = submitUserId;
	}
	public String getExecuteUserId() {
		return executeUserId;
	}
	public void setExecuteUserId(String executeUserId) {
		this.executeUserId = executeUserId;
	}
	public int getIsNewTopReply() {
		return isNewTopReply;
	}
	public void setIsNewTopReply(int isNewTopReply) {
		this.isNewTopReply = isNewTopReply;
	}
	public int getIsNewSubReply() {
		return isNewSubReply;
	}
	public void setIsNewSubReply(int isNewSubReply) {
		this.isNewSubReply = isNewSubReply;
	}
	private String eveId;//隐患Id
	private String title;//隐患标题
	private Date startTime;//开始时间
	private Date endTime;//结束时间
	private Date replyTime;//最新回复的时间
	private String discribe;//隐患描述
	private String doResult;//处理结果
	private int process;//进度
	private String areId;//隐患所在区域
	private String submitUserId;//提交人Id
	private String executeUserId;//处理人Id
	private int isNewTopReply;//是否有上级的回复
	private int isNewSubReply;//是否有下级的回复
	
	
}
