package infoSearchAndFix;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tools.JDBCTools;
import eneityDAO.contextDao;
import entity.Context;
import net.sf.json.JSONObject;

//得到最新10条文章   在安全工作台调用
@WebServlet("/infoSearchAndFix/getNewContext")
public class getNewContext extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	contextDao contextDao=new contextDao();
       
    public getNewContext() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String depId=request.getParameter("depId");
//		System.out.println("getNewContext depId:"+depId);
	
		json=new JSONObject();
		PrintWriter out=response.getWriter();
		List<Context> contextList=new ArrayList<Context>();
		try {
			connection=JDBCTools.getConnection();
			
			if(depId.equals("0")){
				//超管
//				System.out.println("bb");
				 String sql="SELECT * FROM context WHERE   needConfig= 'b'  order by time desc  limit 0,10";
				contextList=contextDao.getForList(connection, sql);
//				System.out.println("getNewContext 超管");
//				for(Context c:contextList){
//					System.out.println(c.toString());
//				}
			}else if(depId.equals("")){
				
			}
			else{
				//一般用户
//				System.out.println("bbb");
				String sql="SELECT * FROM context WHERE depId like ?   AND needConfig= 'b' order by time desc  limit 0,10";
				depId="%"+depId+"%";
				contextList=contextDao.getForList(connection, sql, depId);
//				System.out.println("getNewContext 一般用户");
//				for(Context c:contextList){
//					System.out.println(c.toString());
//				}
			}
			
			json.put("contextList", contextList);
			out.write(json.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
	}

}
