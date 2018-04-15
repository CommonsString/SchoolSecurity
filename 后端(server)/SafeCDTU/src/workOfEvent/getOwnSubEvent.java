package workOfEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
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
import entity.Area;
import entity.EventRecord;
import net.sf.json.JSONObject;
//得到自己上报的隐患
@WebServlet("/workOfEvent/getOwnSubEvent")
public class getOwnSubEvent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	areaDao areaDao=new areaDao();
	eventRecordDao eventRecordDao=new eventRecordDao();
	PrintWriter out=null;   
	int startPage=0;
    public getOwnSubEvent() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uId=request.getParameter("uId");
		String page=request.getParameter("page");
		String total="";
//		System.out.println("getOwnSubEvent:uId:"+uId);
		try {
			json=new JSONObject();
			out=response.getWriter();
			connection=JDBCTools.getConnection();
			
			startPage=Integer.valueOf(page).intValue();
			startPage=(startPage-1)*10;
			
			List<EventRecord> eventRecordList=new ArrayList<EventRecord>();
			//String sql="SELECT * FROM eventrecord WHERE submitUserId= ? order by replyTime desc  limit ?,10";
			String sql="SELECT * FROM eventrecord WHERE areId IN(SELECT areId FROM area WHERE uId = ?) order by replyTime desc  limit ?,10";
			eventRecordList=eventRecordDao.getForList(connection, sql, uId,startPage);
			
			total=getTotal(connection,uId);;
			json.put("eventRecordList", eventRecordList);
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
	
	private String getTotal( Connection connection2,String uId ) {

	
		Object object=new Object();
	
	
		String sql="SELECT COUNT(*) FROM eventrecord WHERE areId IN(SELECT areId FROM area WHERE uId = ?) ";
		try {
			object=eventRecordDao.getForValue(connection2, sql,uId);
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return object.toString();
	
	}

}
