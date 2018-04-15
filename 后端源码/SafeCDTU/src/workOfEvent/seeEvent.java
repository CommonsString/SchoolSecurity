package workOfEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;

import Tools.JDBCTools;
import eneityDAO.areaDao;
import eneityDAO.eventRecordDao;
import entity.EventRecord;
import net.sf.json.JSONObject;


//查看单篇文章
@WebServlet("/workOfEvent/seeEvent")
public class seeEvent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	eventRecordDao eventRecordDao=new eventRecordDao();
	PrintWriter out=null;
    public seeEvent() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String eventId=request.getParameter("eventId");
//		System.out.println("eventId:"+eventId);
		json=new JSONObject();
		out=response.getWriter();
		try {
			connection=JDBCTools.getConnection();
			EventRecord eventRecord=new EventRecord();
			eventRecord=getEvent(connection,eventId);
			
			if(eventRecord.getProcess()==0){
				//将process设置为1
				setProcessToOne(connection,eventId);
				json.put("state", true);
				json.put("message", "已确认查看");
				out.write(json.toString());
			}
			else{
				//已经查看过，不需要弹出提示消息
				json.put("state", "ee");
				out.write(json.toString());
			}
			
			
		} catch (Exception e) {
			json.put("state", false);
			json.put("message", "系统错误，请重新查看");
			out.write(json.toString());
			e.printStackTrace();
		}finally {
			JDBCTools.release(null, null, connection);
			out.flush();
			out.close();
		}
		
	}
	
	//将process设置为1
	private void setProcessToOne(java.sql.Connection connection2, String eventId) {
		String sql="UPDATE eventrecord SET process='1' WHERE eveId= ? ";
		try {
			eventRecordDao.update(connection2, sql, eventId);
		} catch (SQLException e) {
			json.put("state", false);
			json.put("message", "系统错误，请重新查看");
			out.write(json.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private EventRecord getEvent(java.sql.Connection connection2,String eventId){
		String sql="SELECT * FROM eventrecord WHERE eveId= ? ";
		try {
			return eventRecordDao.get(connection2, sql, eventId);
		} catch (SQLException e) {
			json.put("state", false);
			json.put("message", "系统错误，请重新查看");
			out.write(json.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

}
