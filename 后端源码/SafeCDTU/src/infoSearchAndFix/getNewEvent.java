package infoSearchAndFix;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;

import Tools.JDBCTools;
import eneityDAO.areaDao;
import eneityDAO.eventRecordDao;
import entity.Area;
import entity.EventRecord;
import entity.User;
import net.sf.json.JSONObject;

//得到最新10条事件   在安全工作台调用
@WebServlet("/infoSearchAndFix/getNewEvent")
public class getNewEvent extends HttpServlet {
	java.sql.Connection connection=null;
	java.sql.Connection connection2=null;
	java.sql.Connection connection3=null;
	JSONObject json=null; 
	areaDao areaDao=new areaDao();
	eventRecordDao eventRecordDao=new eventRecordDao();
	String sql="";
	private static final long serialVersionUID = 1L;
       
    
    public getNewEvent() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	//	System.out.println("getNewEvent");
		String uId=request.getParameter("uId");
//		System.out.println("getNewEvent  uId:"+uId);
		List<Area> areIdList=new ArrayList<Area>();
		List<Area> NextAreaIdList=new ArrayList<Area>();
		List<EventRecord>  eventList=new ArrayList<EventRecord>();
		json=new JSONObject();
		PrintWriter out=response.getWriter();
		try {
			connection=JDBCTools.getConnection();
			connection2=JDBCTools.getConnection();
			connection3=JDBCTools.getConnection();
			//查询自己负责的所有区域的id
			areIdList=searchMyArea(connection,uId);
//			System.out.println("areIdList:"+areIdList.toString());
//			for(Area a:areIdList){
//				System.out.println("areIdList:"+a.toString());
//			}
			
			if(areIdList.size()==0){
				
			}else{
				//查询出自己负责的所有下一级区域
				NextAreaIdList=searchNextArea(areIdList,connection2);
//				for(Area a:NextAreaIdList){
//					System.out.println("NextAreaIdList:"+a.toString());
//				}
				
				if(NextAreaIdList.size()==0){
					
				}else{
					//找出自己负责的子区域上报的隐患
					eventList=searchAllEvent(NextAreaIdList,connection3);
//					for(EventRecord a:eventList){
//						System.out.println("eventList:"+a.toString());
//					}
				}
				
				
				
			}
			
			json.put("eventList", eventList);
			out.write(json.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            JDBCTools.release(null, null, connection2);
            JDBCTools.release(null, null, connection3);
            out.flush();
            out.close();
        }  
		
		
	}
	private List<Area> searchMyArea(java.sql.Connection connection2,String uId){
		sql="SELECT * FROM area WHERE uId= ? ";
		try {
			List<Area> list=new ArrayList<Area>();
			list=areaDao.getForList(connection2, sql, uId);
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//查询出自己负责的所有子区域
	private  List<Area> searchNextArea(List<Area> list,java.sql.Connection connection2){
		sql="SELECT * FROM area WHERE parAreaId= ? ";
		List<Area> NextAreaIdList=new ArrayList<Area>();
		String areId="";
		try {
		for(Area area:list){
			areId=area.getAreId();
			list=areaDao.getForList(connection2, sql, areId);
			NextAreaIdList.addAll(list);
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return NextAreaIdList;
	}
	private List<EventRecord> searchAllEvent(List<Area> list,java.sql.Connection connection2){
		String areaId="";
		int i=1;
		for(Area area:list){
//			System.out.println("abc:");
			areaId+="areId = '"+area.getAreId()+"'";
			
			if(i!=list.size()){
				i++;
				areaId+=" or ";
			}
			}
//		System.out.println("areaId:"+areaId);
		sql="SELECT * FROM eventRecord WHERE "+areaId+" order by startTime desc  limit 0,10";
//		System.out.println(sql);
		List<EventRecord>  eventList=new ArrayList<EventRecord>();
		
		try {
			eventList=eventRecordDao.getForList(connection2, sql);
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return eventList;
	}

}
