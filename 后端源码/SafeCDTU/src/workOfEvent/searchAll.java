package workOfEvent;

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
import eneityDAO.areaDao;
import eneityDAO.eventRecordDao;
import entity.Context;
import entity.EventRecord;
import net.sf.json.JSONObject;

//通过标题查找全部的事件
@WebServlet("/workOfEvent/searchAll")
public class searchAll extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	eventRecordDao eventRecordDao=new eventRecordDao();
	
	
    public searchAll() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String condition=request.getParameter("condition");
//		System.out.println("condition:"+condition);
		json=new JSONObject();
		PrintWriter out=response.getWriter();
		try {
			connection=JDBCTools.getConnection();
			String sql="SELECT * FROM eventrecord WHERE title LIKE ? order by replyTime desc";
			condition="%"+condition+"%";
			
			List<EventRecord> eventList=new ArrayList<EventRecord>();
			eventList=eventRecordDao.getForList(connection, sql, condition);
			String total=""+eventList.size();
			json.put("eventList", eventList);
			json.put("total", total);
			out.write(json.toString());
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
	}

}
