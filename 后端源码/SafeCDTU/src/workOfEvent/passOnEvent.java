package workOfEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import Tools.JDBCTools;
import eneityDAO.areaDao;
import eneityDAO.eventRecordDao;
import eneityDAO.userDao;
import entity.EventRecord;
import entity.User;
import net.sf.json.JSONObject;

//上报隐患
@WebServlet("/workOfEvent/passOnEvent")
public class passOnEvent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	eventRecordDao eventRecordDao=new eventRecordDao();
	areaDao areaDao=new areaDao();
	userDao userDao=new userDao();
	PrintWriter out=null;
    public passOnEvent() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		json=new JSONObject();
		out=response.getWriter();
		String eveId=request.getParameter("eventId");
//		String areaId=request.getParameter("areId");
		//uId代表当前用户的上一级用户的id
		String uId=request.getParameter("uId");
		String process1=request.getParameter("process");
		String areaId="";
//		System.out.println("process1:"+process1);
		int process=0;
		try {
			connection=JDBCTools.getConnection();
			EventRecord eventRecord=new EventRecord();
			
			//得到这条事件
			eventRecord=getEvent(connection,eveId);
			
			//得到当前区域id
			String id=eventRecord.getAreId();
			
			//得到父区域id
			areaId=getParareId(connection,id);
			
			User user=new User();
			user=getUser(connection, uId);
			
			if(eventRecord.getProcess()==0){
				json.put("state", false);
				json.put("message", "上报失败,请先查看");
				out.write(json.toString());
			}
			else if(user.getLevel()==1){
				json.put("state", false);
				json.put("message", "最高权限者,无法上报");
				out.write(json.toString());
			}
			else{
				//上报
				//将区域id换成上一级区域的id   uid换成上一级的uid
				String sql="UPDATE eventrecord SET areId= ? , process= ?, submitUserId= ?  WHERE  eveId= ?";
				eventRecordDao.update(connection, sql, areaId,process,uId,eveId);
				updateUserIsNewEveNotice(connection,areaId);
				json.put("state", true);
				json.put("message", "上报成功");
				out.write(json.toString());
			}
			
		} catch (Exception e) {
			json.put("state", false);
			json.put("message", "系统错误,上报失败");
			out.write(json.toString());
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
		
	}
	
	 private void updateUserIsNewEveNotice(Connection connection2, String areId) {
	    	
		 String uId="";
		try {
		//查找出上级用户的id
		String sql="SELECT parId FROM users WHERE uId In(SELECT uId FROM area WHERE areId='"+areId+"');";
		uId=userDao.getForValue(connection2, sql);
		
		//修改上级用户的isNewEveNotice
		String sql2="UPDATE users SET isNewEveNotice='1' WHERE uId= ? ";
		userDao.update(connection2, sql2,uId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private User getUser(java.sql.Connection connection2,String uId){
		String sql="SELECT * FROM users WHERE uId= ? ";
		User user=new User();
		try {
			user= userDao.get(connection2, sql, uId);
		} catch (SQLException e) {
			json.put("state", false);
			json.put("message", "系统错误，请重新查看");
			out.write(json.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
		
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
	
	//得到父区域id
	private String getParareId(Connection connection2, String are){
		String sql="SELECT parAreaId FROM area WHERE areId= ? ";
		String areId="";
		try {
			areId=areaDao.getForValue(connection2, sql, are);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return areId;
		
	}

}
