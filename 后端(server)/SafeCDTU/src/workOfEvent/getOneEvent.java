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

import org.apache.commons.lang.ObjectUtils.Null;

import com.mysql.jdbc.Connection;

import Tools.JDBCTools;
import eneityDAO.areaDao;
import eneityDAO.evePicDao;
import eneityDAO.eventRecordDao;
import eneityDAO.userDao;
import entity.Area;
import entity.EvePic;
import entity.EventRecord;
import entity.User;
import infoSearchAndFix.getUserList;
import net.sf.json.JSONObject;

//得到单条的事件
@WebServlet("/workOfEvent/getOneEvent")
public class getOneEvent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	java.sql.Connection connection2=null;
	java.sql.Connection connection3=null;
	java.sql.Connection connection4=null;
	java.sql.Connection connection5=null;
	JSONObject json=null; 
	eventRecordDao eventRecordDao=new eventRecordDao();
	evePicDao evePicDao=new evePicDao();
	areaDao areaDao=new areaDao();
	userDao userDao=new userDao();
	PrintWriter out=null;
	int sub=0,exe=0,areaa=0;
	
    public getOneEvent() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		try {
			json=new JSONObject();
			out=response.getWriter();
			
			String eveId=request.getParameter("eveId");
//			System.out.println("eveId:"+eveId);
			connection=JDBCTools.getConnection();
			connection2=JDBCTools.getConnection();
			connection3=JDBCTools.getConnection();
			connection4=JDBCTools.getConnection();
			connection5=JDBCTools.getConnection();
			//找出单个隐患事件的内容
			EventRecord eventRecord=new EventRecord();
			//找出该隐患的图片
			List<EvePic> picList=new ArrayList<EvePic>();
			//区域信息
			Area area=new Area();
			Area newarea=new Area();
			//上报人信息
			User subUser=new User();
			//处理人信息
			User exeUser=new User();
			
			//上报人信息
			User newsubUser=new User();
			//处理人信息
			User newexeUser=new User();
			
			//得到单条事件
			eventRecord=getEvent(connection,eveId);
//			System.out.println("eventRecord:"+eventRecord);
			//得到事件的图片
			picList=getAllPic(connection2,eveId);
//			System.out.println("picList:"+picList);
			
			String areId=eventRecord.getAreId();
			String subId=eventRecord.getSubmitUserId();
			String exeId=eventRecord.getExecuteUserId();
			
			area=getArea(connection3, areId);
			if(area==null){
				newarea.setName("该区域已被删除");
				areaa=1;
				
			}
			//System.out.println("area:"+area.toString());
			subUser=getUser(connection4, subId);
			if(subUser==null){
				newsubUser.setName("该用户已被删除");
				newsubUser.setTel("");
				sub=1;
			}
			
//			if(exeId.equals("")||exeId==null){
			if(exeId==null){
				exeUser.setName("无");
				exeUser.setTel("");
				
			}
			else{
				exeUser=getUser(connection5, exeId);
				if(exeUser==null){
					
					newexeUser.setName("该用户已被删除");
					newexeUser.setTel("");
					exe=1;
				}
			}
			
			json.put("eventRecord", eventRecord);
			json.put("picList", picList);
			
			if(areaa==0){
				json.put("area",area );
			}else{
				json.put("area",newarea );
			}
			if(sub==0){
				json.put("submitUser",subUser );
			}else{
				json.put("submitUser",newsubUser );
			}
			if(exe==0){
				json.put("executeUser",exeUser );
			}else{
				json.put("executeUser",newexeUser );
			}
			
			//System.out.println("area:"+area.getName());
//			System.out.println("submitUser:"+subUser.toString());
//			System.out.println("executeUser:"+exeUser.toString());
			out.write(json.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            JDBCTools.release(null, null, connection2);
            JDBCTools.release(null, null, connection3);
            JDBCTools.release(null, null, connection4);
            JDBCTools.release(null, null, connection5);
            out.flush();
            out.close();
        }  
		
	}
	private User getUser(java.sql.Connection connection2, String userId) {
		String sql="SELECT * FROM users  WHERE uId= ? ";
		try {
//			System.out.println("user:"+userDao.get(connection2, sql, userId));
			User user=new User();
			user=userDao.get(connection2, sql, userId);
			return user;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private EventRecord getEvent(java.sql.Connection connection2 ,String areId){
		String sql="SELECT * FROM eventrecord WHERE eveId= ? ";
		try {
			EventRecord eventRecord=new EventRecord();
			eventRecord=eventRecordDao.get(connection2, sql, areId);
			return eventRecord;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	//得到所有的图片
	private List<EvePic> getAllPic(java.sql.Connection connection2 ,String eveId){
		String sql="SELECT * FROM evepic WHERE eveId= ? ";
		try {
			return evePicDao.getForList(connection2, sql, eveId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	//
	private Area getArea(java.sql.Connection connection2 ,String areId){
		String sql="SELECT * FROM area WHERE areId= ? ";
		try {
			return areaDao.get(connection2, sql, areId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

}
