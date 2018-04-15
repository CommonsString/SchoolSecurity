package workOfEvent;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import Tools.GetProperty;
import Tools.JDBCTools;
import eneityDAO.contextAffixDao;
import eneityDAO.evePicDao;
import eneityDAO.eventRecordDao;
import eneityDAO.userDao;
import net.sf.json.JSONObject;

//添加新的事件
@WebServlet("/workOfEvent/addEvent")
public class addEvent extends HttpServlet {
	private static final long serialVersionUID = 1L;
   // private String uploadPath = "D:\\temp"; // 上传文件的目录
   // private String tempPath = "d:\\temp\\buffer\\"; // 临时文件目录
    
	
	String forwardPort="";
	String myPot ="";
	String serviceIP="";
	String imageURL="";
	//设置图片存放的路径
    private String uploadPath;
    //= "D:\\eclipse\\project\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\SafeCDTU\\eventPic"; // 上传文件的目录
    private String tempPath;
    //= "d:\\eclipse\\project\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\SafeCDTU\\eventPic\\buffer\\"; // 临时文件目录
    //D:\eclipse\project\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\SafeCDTU
    String url;
    String title;
    File tempPathFile; 
    String picName;
   
    java.sql.Connection connection=null;
	JSONObject json=null; 
	evePicDao evePicDao=new evePicDao();
	eventRecordDao eventRecordDao=new eventRecordDao();
	userDao userDao=new userDao();
	PrintWriter out=null;
    public addEvent() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

    @SuppressWarnings("unchecked")
    public void doPost(HttpServletRequest request, HttpServletResponse response)
           throws IOException, ServletException {
    	
//    	System.out.println("addEvent");
//    	String content=request.getParameter("content");
//    	System.out.println(content);
    
    	 myPot =GetProperty.getPropertyByName("proper","myPort");//后端端口
    	 forwardPort =GetProperty.getPropertyByName("proper","forwardPort");//前端端口
    	serviceIP=GetProperty.getPropertyByName("proper","serviceIP");//服务器ip
    	imageURL=GetProperty.getPropertyByName("proper","imageUrl");//图片存放路径
    	System.out.println("imageURL"+imageURL);
    	 
    	String areId=null;
    	String parAreaId="";
    	String submitUserId=null;
    	String title=null;
    	String discribe=null;
    	String eveId=UUID.randomUUID().toString().replaceAll("\\-", "");
    	int process=0;
//    	json=new JSONObject();
//		PrintWriter out=response.getWriter();
       try {
    	   out=response.getWriter();
    	   connection=JDBCTools.getConnection();
    	   JDBCTools.beginThing(connection);
           // Create a factory for disk-based file items
           DiskFileItemFactory factory = new DiskFileItemFactory();
 
           // Set factory constraints
           factory.setSizeThreshold(4096); // 设置缓冲区大小，这里是4kb
           factory.setRepository(tempPathFile);// 设置缓冲区目录
 
           // Create a new file upload handler
           ServletFileUpload upload = new ServletFileUpload(factory);
 
           // Set overall request size constraint
           upload.setSizeMax(4194304); // 设置最大文件尺寸，这里是4MB
 
           List  items = upload.parseRequest(request);// 得到所有的文件
           Iterator i = items.iterator();
           
           while (i.hasNext()) {
              FileItem fi = (FileItem) i.next();
              if(fi.isFormField()){  //此处是判断非文件域，即不是<inputtype="file"/>的标签
                  String nameOfForm=fi.getFieldName(); //获取form表单中name的id
                 if("areId".equals(nameOfForm))  {
                	 areId=fi.getString("utf-8"); //item是指定id的value值，此处用      item.getString("utf-8")是把item用utf-8解析，根据你的需要可以用其他的，如：gbk；
//                	 System.out.println("areId:"+areId);
                 }else if("parAreaId".equals(nameOfForm))  {
                	 parAreaId=fi.getString("utf-8"); 
//                	 System.out.println("submitUserId:"+submitUserId);
                 }else if("submitUserId".equals(nameOfForm))  {
                	 submitUserId=fi.getString("utf-8"); 
//                	 System.out.println("submitUserId:"+submitUserId);
                 }else if("title".equals(nameOfForm))  {
                	 title=fi.getString("utf-8"); 
//                	 System.out.println("title:"+title);
                 }else if("discribe".equals(nameOfForm))  {
                	 discribe=fi.getString("utf-8"); 
//                	 System.out.println("discribe:"+discribe);
                 }
                
              }else{//如果是文件域
            	  String fileName = fi.getName();
            	  if (!fileName.equals("")) {
            	      File fullFile = new File(fi.getName());
            	      File savedFile = new File(uploadPath, fullFile.getName());
            	      //把文件存到服务器
            	      fi.write(savedFile);
            	      String name=fullFile.getName();
            	     // title=name;图片或者文件的名字  加上后缀的
            	      //换文件的名字
            	      name=changeName(name,UUID.randomUUID().toString().replaceAll("\\-", ""));
            	      File newFile=new File(uploadPath,name);
            	      savedFile.renameTo(newFile);
            	      
            	      String picId=UUID.randomUUID().toString().replaceAll("\\-", "");
            	      
            	      String src=serviceIP+myPot+"/SafeCDTU/eventPic/"+picName;
            	      
//            	  	  System.out.println("url:"+url);    
//            	  	  System.out.println("title:"+title);
            	  	 
            	      saveEvePic(picId,eveId,src,connection,request);
            	  	
            	  }
              }
             
           }
           Date nowTime=new Date();
           int re=0;
           String sql2="insert  into eventrecord(eveId,title,startTime,replyTime,discribe,process,areId,submitUserId,isNewTopReply,isNewSubReply)values(?,?,?,?,?,?,?,?,?,?)";
			eventRecordDao.update(connection, sql2, eveId,title,nowTime,nowTime,discribe,process,areId,submitUserId,re,re);
			//修改上级用户的isNewEveNotice，表示有新的事件
			updateUserIsNewEveNotice(connection,areId);
			JDBCTools.commitThing(connection);
			
		   out.println("上传成功");
           out.println("<a href=\""
           		+ serviceIP+forwardPort+"/eventIncre\"><input type=\"button\" value=\"返回\"></input></a>");
           //response.sendRedirect("http://localhost:8999/");重定向
          
       } catch (Exception e) {
           // 可以跳转出错页面
    	 JDBCTools.rollbackThing(connection);
    	   out.println("系统错误,或者文件大于了4M,上传失败");
    	   out.println("<a href=\"+"
    	   		+ serviceIP+forwardPort+"/eventIncre\"><input type=\"button\" value=\"返回\"></input></a>");
           e.printStackTrace();
       }finally {
    	   JDBCTools.release(null, null, connection);
    	   out.close();
    	   out.flush();
	}
    }
    private void updateUserIsNewEveNotice(Connection connection2, String areId) {
    	
		 String uId="";
		try {
			System.out.println("areIdadd:"+areId);
		//查找出上级用户的id
		String sql="SELECT uId FROM area WHERE areId IN(SELECT parAreaId FROM area WHERE areId= ? )";
		uId=userDao.getForValue(connection2, sql,areId);
		
		System.out.println("uIdadd:"+uId);
		//修改上级用户的isNewEveNotice
		String sql2="UPDATE users SET isNewEveNotice='1' WHERE uId= ? ";
		userDao.update(connection2, sql2,uId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    private void saveEvePic(String picId, String eveId, String src, Connection connection2,HttpServletRequest request) {
    	String sql="insert into evepic values(?,?,?)";
    	try {
    		evePicDao.update(connection2, sql,picId,eveId,src);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.println("系统错误,或者文件大于了4M,上传失败");
			out.println("<a href=\"+"
					+ serviceIP+forwardPort+"/eventIncre\"><input type=\"button\" value=\"返回\"></input></a>");
			e.printStackTrace();
		}
		
	}

	public void init() throws ServletException {
		String proClassPath = this.getClass().getResource("").getPath();
		//路径改成服务器存放图片的路径
		uploadPath=this.getServletContext().getRealPath("/")+"eventPic";
		tempPath=this.getServletContext().getRealPath("/")+"eventPic\\buffer\\";
//    	System.out.println("uploadPath:"+uploadPath);
//    	System.out.println("uploadPath:"+uploadPath);
        File uploadFile = new File(uploadPath);
        if (!uploadFile.exists()) {
            uploadFile.mkdirs();
        }
        File tempPathFile = new File(tempPath);
         if (!tempPathFile.exists()) {
            tempPathFile.mkdirs();
        }
     }
	//修改图片的名字，防止重复
    private String changeName(String oldName,String newName){
    	int i=oldName.lastIndexOf("\\");
    	int j=oldName.lastIndexOf(".");
    	oldName= oldName.substring(0,i+1)+newName+oldName.substring(j);
    	url=uploadPath+"\\"+oldName;
    	picName=oldName;
//    	System.out.println(oldName);
    	return oldName;
    	
    }
    
    private String getUrl(String proClassPath ){
    	String[] url=proClassPath.split("/");
    	String path = "";
    	for(String s:url){
    		if(!s.trim().equals("")){
    			path=path+s+"\\";
    		}
    		if(s.toString().equals("SafeCDTU")){
    			break;
    		}
    	}
		return path;
    	
    }
   
     

}
