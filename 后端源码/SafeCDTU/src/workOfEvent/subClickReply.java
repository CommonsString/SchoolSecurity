package workOfEvent;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tools.JDBCTools;
import eneityDAO.areaDao;
import eneityDAO.eventRecordDao;
import eneityDAO.replyDao;
import eneityDAO.userDao;
import net.sf.json.JSONObject;
//下级查看了回复
@WebServlet("/workOfEvent/subClickReply")
public class subClickReply extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	eventRecordDao eventRecordDao=new eventRecordDao();
	PrintWriter out=null;
	
    public subClickReply() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("subClickReply");
		String eveId=request.getParameter("eveId");
		try {
			
			json=new JSONObject();
			out=response.getWriter();
			connection=JDBCTools.getConnection();
			String sql="UPDATE eventrecord SET isNewTopReply='0' WHERE eveId= ? ";
			eventRecordDao.update(connection, sql, eveId);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
		
		
	}

}
