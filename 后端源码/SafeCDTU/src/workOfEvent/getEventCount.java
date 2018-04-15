package workOfEvent;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tools.JDBCTools;
import eneityDAO.eventRecordDao;
import net.sf.json.JSONObject;

//得到事件的总条数
@WebServlet("/workOfEvent/getEventCount")
public class getEventCount extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	eventRecordDao eventRecordDao=new eventRecordDao();
	
    public getEventCount() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		json=new JSONObject();
		PrintWriter out=response.getWriter();
		
			try {
				connection=JDBCTools.getConnection();
				String sql="SELECT COUNT(*) FROM eventrecord ";
				Object number=eventRecordDao.getForValue(connection, sql);
				String count=number.toString();
				json.put("count", count);
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
