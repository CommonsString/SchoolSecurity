package context;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tools.JDBCTools;
import eneityDAO.contextDao;
import eneityDAO.eventRecordDao;
import net.sf.json.JSONObject;

//定点通知的用户查看了文章
@WebServlet("/context/seeContent")
public class seeContent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	eventRecordDao eventRecordDao=new eventRecordDao();
	PrintWriter out=null;
	contextDao contextDao=new contextDao();
	
    public seeContent() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String txtId=request.getParameter("txtId");
		try {
			connection=JDBCTools.getConnection();
			json=new JSONObject();
			out=response.getWriter();
			String sql="UPDATE context SET isSee='1' WHERE txtId= ? ";
			contextDao.update(connection, sql, txtId);
			json.put("state", true);
			out.write(json.toString());
			
		} catch (Exception e) {
			json.put("state", false);
			json.put("message", "系统错误,请重新查看一次通知");
			out.write(json.toString());
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
		
		
	}

}
