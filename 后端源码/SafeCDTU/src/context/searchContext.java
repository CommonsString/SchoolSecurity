package context;

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


/*
*
* 超管通过标题查找所有部门通知   
* 
* 用户通过标题查找自己部门通知
* 
* 
* */
@WebServlet("/context/searchContext")
public class searchContext extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	contextDao contextDao=new contextDao();
	PrintWriter out=null;
	
    public searchContext() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		json=new JSONObject();
		out=response.getWriter();
		String title=request.getParameter("title");
		String depId=request.getParameter("depId");
//		System.out.println("searchContext title:"+title);
//		System.out.println("searchContext depId:"+depId);
		title="%"+title+"%";
		try {
			connection=JDBCTools.getConnection();
			List<Context> contextList=new ArrayList<Context>();
			
			//得到文章
			if(depId.equals("0")){
				//超管
//				System.out.println("bb");
				String sql="SELECT * FROM context where title like ? and  needConfig= 'b'  order by time desc ";
				contextList=contextDao.getForList(connection, sql,title);
			}else{
				//一般用户
//				System.out.println("bbb");
				String sql="SELECT * FROM context WHERE depId like ? and title like ? and  needConfig= 'b'  order by time desc  ";
				depId="%"+depId+"%";
				contextList=contextDao.getForList(connection, sql, depId,title);
			}
//			System.out.println("");
//			System.out.println("k");
//			for(Context con:contextList){
//				System.out.println(con.toString());
//			}
			//返回查找到的所有文章
			json.put("contextList", contextList);
			//返回文章的总条数
			json.put("total", contextList.size());
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
