package entity;

public class AreRule {

	
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	public String getAreId() {
		return areId;
	}
	public void setAreId(String areId) {
		this.areId = areId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	private String ruleId;
	@Override
	public String toString() {
		return "AreRule [ruleId=" + ruleId + ", areId=" + areId + ", content=" + content + "]";
	}
	private String areId;
	private String content;
}
