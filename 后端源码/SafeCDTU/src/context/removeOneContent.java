package context;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tools.GetProperty;
import Tools.JDBCTools;
import eneityDAO.contextDao;
import entity.Context;
import net.sf.json.JSONObject;

//删除单篇文章
@WebServlet("/context/removeOneContent")
public class removeOneContent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	contextDao contextDao=new contextDao();
	PrintWriter out=null;
	
    public removeOneContent() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		json=new JSONObject();
		out=response.getWriter();
		String txtId=request.getParameter("txtId");
		String everUrl=GetProperty.getPropertyByName("proper","everUrl");
		try {
			connection=JDBCTools.getConnection();
			String sql2="SELECT content FROM context WHERE txtId= ?";
			String content=contextDao.getForValue(connection, sql2, txtId);

			//删除一篇文章
			String sql="DELETE FROM context WHERE txtId= ? ";
			contextDao.update(connection, sql, txtId);
			
			//查找出文章中的图片链接http://localhost:8099/SafeCDTU/ueditor/jsp/upload/image/20170811/1502438927467018740.png
			 Pattern p = Pattern.compile("(?:src=\"?)(.*?)\"?\\s");
			 Matcher m = p.matcher(content);
			 String[] arr = new String[20];
			 int i=0;
			 while(m.find()) {
				 arr[i] = m.group(1);
				 i++;
				 }
			//获取当前项目的绝对路径
			String url=request.getSession().getServletContext().getRealPath("");
			for(int k=0;k<arr.length;k++){
				if(arr[k]!=null){
				arr[k]=arr[k].replace(everUrl, "");
				arr[k]=arr[k].replace("/", "\\");
				arr[k]=url+arr[k];
				File file=new File(arr[k]);
				file.delete();
				}
				
			}
			json.put("state", true);
			json.put("message", "删除成功");
			out.write(json.toString());
		} catch (Exception e) {
			json.put("state", false);
			json.put("message", "系统错误,删除失败");
			out.write(json.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
	
		
	}

}
