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
 * 超管查找发出去的定点通知
 * */
@WebServlet("/context/searchPotSendContent")
public class searchPotSendContent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	contextDao contextDao=new contextDao();
	PrintWriter out=null;
       
    public searchPotSendContent() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("searchPotSendContent");
		json=new JSONObject();
		out=response.getWriter();
		String title=request.getParameter("title");
		String uId=request.getParameter("uId");
		try {
			connection=JDBCTools.getConnection();
			List<Context> contextList=new ArrayList<Context>();
			String sql="SELECT * FROM context where title like ? and  needConfig= 'a'  order by time desc ";
			
			title="%"+title+"%";
			contextList=contextDao.getForList(connection, sql, title);
			for(Context c:contextList){
				System.out.println(c.toString());
			}
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
