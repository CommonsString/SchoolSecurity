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
import entity.EventRecord;
import net.sf.json.JSONObject;
//按标题查找自己上报的隐患
@WebServlet("/workOfEvent/searchOwnSubEvent")
public class searchOwnSubEvent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	areaDao areaDao=new areaDao();
	eventRecordDao eventRecordDao=new eventRecordDao();
    public searchOwnSubEvent() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("searchOwnSubEvent");
		String uId=request.getParameter("uId");
		String condition=request.getParameter("condition");
//		System.out.println("uId:"+uId);
//		System.out.println("condition:"+condition);
		List<EventRecord>  eventList=new ArrayList<EventRecord>();
		json=new JSONObject();
		PrintWriter out=response.getWriter();
		
		try {
			connection=JDBCTools.getConnection();
			String sql="SELECT * FROM eventRecord WHERE submitUserId= ? AND title LIKE ? order by replyTime desc";
			condition="%"+condition+"%";
			eventList=eventRecordDao.getForList(connection, sql, uId,condition);
			
//			for(EventRecord e:eventList){
//				System.out.println(e.toString());
//			}
			int total=eventList.size();
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

}
