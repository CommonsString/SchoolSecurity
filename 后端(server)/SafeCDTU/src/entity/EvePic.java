package entity;

public class EvePic {
	private String picId;
	@Override
	public String toString() {
		return "EvePic [picId=" + picId + ", eveId=" + eveId + ", src=" + src + "]";
	}
	private String eveId;
	private String src;//图片的链接
	public String getPicId() {
		return picId;
	}
	public void setPicId(String picId) {
		this.picId = picId;
	}
	public String getEveId() {
		return eveId;
	}
	public void setEveId(String eveId) {
		this.eveId = eveId;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	
}
