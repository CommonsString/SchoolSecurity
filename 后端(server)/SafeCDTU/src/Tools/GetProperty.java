package Tools;

public class GetProperty {
	public GetProperty() {  
        super();  
    }  
  
    // 方法一：通过java.util.ResourceBundle读取资源属性文件    
    public static String getPropertyByName(String path, String name) {    
        String result = "";    
    
        try {    
            // 方法一：通过java.util.ResourceBundle读取资源属性文件    
            result = java.util.ResourceBundle.getBundle(path).getString(name);    
            //System.out.println("name:" + result);    
        } catch (Exception e) {    
            System.out.println("getPropertyByName2 error:" + name);    
        }    
        return result;    
    }    
    public static void main(String[] args) {  
//        System.out.println("=="+GetProperty.getPropertyByName("OnlinePreviewConfig","swftools.url"));    
//        System.out.println("==>>"+GetProperty.getPropertyByName2("OnlinePreviewConfig.properties","port"));    
    }  
}
