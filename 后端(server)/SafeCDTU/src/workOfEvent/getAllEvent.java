package workOfEvent;

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
import eneityDAO.eventRecordDao;
import entity.EventRecord;
import net.sf.json.JSONObject;

//得到某一页全部的事件,超管的方法
@WebServlet("/workOfEvent/getAllEvent")
public class getAllEvent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	eventRecordDao eventRecordDao=new eventRecordDao();
   
    public getAllEvent() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("getAllEvent");
			PrintWriter out=null;
		try {
			json=new JSONObject();
			out=response.getWriter();
			String page=request.getParameter("page");
			//System.out.println("page1:"+page);
			
			//分页
			int startPage=Integer.valueOf(page).intValue();
			startPage=(startPage-1)*10;
			//System.out.println("startPage:"+startPage);
			
			List<EventRecord> eventList=new ArrayList<EventRecord>();
			String total;
			
			connection=JDBCTools.getConnection();
			String sql="SELECT * FROM eventrecord  order by replyTime  desc  limit  ?,10";
			eventList=eventRecordDao.getForList(connection, sql, startPage);
			total=getTotal(connection);
			
			json.put("eventList", eventList);
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
	private String getTotal(java.sql.Connection connection2){
		String sql="SELECT count(*) FROM eventrecord";
		Object object=new Object();
 		try {
 			object=eventRecordDao.getForValue(connection2, sql);
			 return object.toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

}
