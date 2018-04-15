package entity;

public class Area {

		private String areId;//区域id
		@Override
		public String toString() {
			return "Area [areId=" + areId + ", number=" + number + ", name=" + name + ", uId=" + uId + ", discribe="
					+ discribe + ", parAreaId=" + parAreaId + "]";
		}
		private String number;//区域编号
		private String name;//区域名称
		private String uId;//负责人id
		private String discribe;//区域描述
		private String parAreaId;//父区域id
		
		
		public String getAreId() {
			return areId;
		}
		public void setAreId(String areId) {
			this.areId = areId;
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
		public String getuId() {
			return uId;
		}
		public void setuId(String uId) {
			this.uId = uId;
		}
		public String getDiscribe() {
			return discribe;
		}
		public void setDiscribe(String discribe) {
			this.discribe = discribe;
		}
		public String getParAreaId() {
			return parAreaId;
		}
		public void setParAreaId(String parAreaId) {
			this.parAreaId = parAreaId;
		}
		
}
