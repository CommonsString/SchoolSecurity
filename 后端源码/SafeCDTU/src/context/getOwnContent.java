package context;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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
 * 用户得到定点发送给自己的通知
 * */
@WebServlet("/context/getOwnContent")
public class getOwnContent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	java.sql.Connection connection3=null;
	JSONObject json=null; 
	contextDao contextDao=new contextDao();
	PrintWriter out=null;
	
    public getOwnContent() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		json=new JSONObject();
		out=response.getWriter();
		String uId=request.getParameter("uId");
		String total;
		//System.out.println("getOneContent");
		//System.out.println("getOneContent   txtId:"+txtId);
		try {
			List<Context> contextList=new ArrayList<Context>();
			connection=JDBCTools.getConnection();
			connection3=JDBCTools.getConnection();
			//得到定向发给自己的所有文章
			String sql="select * from context where depId IN(select areId from area where uId= ? ) AND needConfig= 'a' ";
			
			contextList=contextDao.getForList(connection, sql, uId);
			total=getTotal(connection3,uId);
			
			json.put("contextList", contextList);
			json.put("total", total);
			
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
	
	
	private String getTotal(java.sql.Connection connection2,String uId){
		Object object=new Object();
		try {
			
			String sql="select COUNT(*) from context where depId IN(select areId from area where uId= ? )";
				
				object=contextDao.getForValue(connection2, sql, uId);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object.toString();
		
	}

}
