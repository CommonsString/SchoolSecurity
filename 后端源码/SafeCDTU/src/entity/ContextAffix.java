package entity;

public class ContextAffix {

	private String aId;//附近id
	@Override
	public String toString() {
		return "ContextAffix [aId=" + aId + ", txtId=" + txtId + ", title=" + title + ", url=" + url + "]";
	}
	private String txtId;//附件在哪里文章里
	private String title;//附件名
	private String url;//文件的链接
	public String getaId() {
		return aId;
	}
	public void setaId(String aId) {
		this.aId = aId;
	}
	public String getTxtId() {
		return txtId;
	}
	public void setTxtId(String txtId) {
		this.txtId = txtId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
