package workOfEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tools.JDBCTools;
import eneityDAO.eventRecordDao;
import entity.EventRecord;
import net.sf.json.JSONObject;

//确认处理事件
@WebServlet("/workOfEvent/querenEvent")
public class querenEvent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	eventRecordDao eventRecordDao=new eventRecordDao();
	PrintWriter out=null;
    public querenEvent() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String eventId=request.getParameter("eventId");
		String doResult=request.getParameter("doResult");
		String executeUserId=request.getParameter("executeUserId");
//		System.out.println("eventId:"+eventId);
//		System.out.println("doResult:"+doResult);
//		System.out.println("executeUserId:"+executeUserId);
		json=new JSONObject();
		out=response.getWriter();
		try {
			connection=JDBCTools.getConnection();
			EventRecord eventRecord=new EventRecord();
			
			//通过id得到事件
			eventRecord=getEvent(connection,eventId);
			
			//通过process判断状态
			if(eventRecord.getProcess()==1){
				setProcessToOne(connection,eventId,doResult,executeUserId);
				json.put("state", true);
				json.put("message", "已成功处理");
				out.write(json.toString());
			}else if(eventRecord.getProcess()==0){
				json.put("state", false);
				json.put("message", "请先进行查看");
				out.write(json.toString());
			}
			
			else if(eventRecord.getProcess()==2){
				json.put("state", false);
				json.put("message", "已经处理过该隐患,请勿重复处理");
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
	
	//将process设置为2
	private void setProcessToOne(java.sql.Connection connection2, String eventId,String doResult,String executeUserId) {
		Date endTime=new Date();
		String sql="UPDATE eventrecord SET endTime= ? ,process='2' ,doResult= ? , executeUserId= ? WHERE eveId= ? ";
		try {
			eventRecordDao.update(connection2, sql,endTime,doResult,executeUserId, eventId);
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
