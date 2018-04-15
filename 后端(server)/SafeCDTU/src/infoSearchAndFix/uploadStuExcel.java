package infoSearchAndFix;

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
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import Tools.JDBCTools;
import eneityDAO.evePicDao;
import eneityDAO.eventRecordDao;
import eneityDAO.userDao;
import net.sf.json.JSONObject;

@WebServlet("/infoSearchAndFix/uploadStuExcel")
public class uploadStuExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	   // private String uploadPath = "D:\\temp"; // 上传文件的目录
	   // private String tempPath = "d:\\temp\\buffer\\"; // 临时文件目录
	    
		//设置图片存放的路径
	    private String uploadPath = "D:\\"; // 上传文件的目录
	    private String tempPath = "d:\\buffer\\"; // 临时文件目录
	    //D:\eclipse\project\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\SafeCDTU
	   
	    File tempPathFile; 
	    PrintWriter out=null;
		
    public uploadStuExcel() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       try {
    	   out=response.getWriter();
    	   
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
              if(fi.isFormField()){
            	  
              }else{//如果是文件域
            	  String fileName = fi.getName();
            	  if (!fileName.equals("")) {
            	      File fullFile = new File(fi.getName());
            	      File savedFile = new File(uploadPath, fullFile.getName());
            	      //把文件存到服务器
            	      fi.write(savedFile);
            	      
            	      
            	  	
            	  }
              }
             
           }
			
		   out.println("上传成功");
           out.println("<a href=\"http://localhost:8080/eventIncre\"><input type=\"button\" value=\"返回\"></input></a>");
           //response.sendRedirect("http://localhost:8999/");重定向
          
       } catch (Exception e) {
           // 可以跳转出错页面
    	   out.println("系统错误,上传失败");
    	   out.println("<a href=\"http://localhost:8080/eventIncre\"><input type=\"button\" value=\"返回\"></input></a>");
           e.printStackTrace();
       }finally {

    	   out.close();
    	   out.flush();
       }
    }
   

	public void init() throws ServletException {
        File uploadFile = new File(uploadPath);
        if (!uploadFile.exists()) {
            uploadFile.mkdirs();
        }
        File tempPathFile = new File(tempPath);
         if (!tempPathFile.exists()) {
            tempPathFile.mkdirs();
        }
     }
	
    
   
     

}
