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


@WebServlet("/workOfEvent/getEvent")
public class getEvent extends HttpServlet {
	java.sql.Connection connection=null;
	java.sql.Connection connection2=null;
	java.sql.Connection connection3=null;
	java.sql.Connection connection4=null;
	JSONObject json=null; 
	areaDao areaDao=new areaDao();
	eventRecordDao eventRecordDao=new eventRecordDao();
	String sql="";
	private static final long serialVersionUID = 1L;
	int startPage=0;
       
    
    public getEvent() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uId=request.getParameter("uId");
		String page=request.getParameter("page");
//		System.out.println("getEvent");
//		System.out.println("uId:"+uId+"page:"+page);
//		
//		System.out.println("uId:"+uId+"  page:"+page);
		
		String total="";
		startPage=Integer.valueOf(page).intValue();
		startPage=(startPage-1)*10;
		
		List<Area> areIdList=new ArrayList<Area>();
		List<Area> NextAreaIdList=new ArrayList<Area>();
		List<EventRecord>  eventList=new ArrayList<EventRecord>();
		json=new JSONObject();
		PrintWriter out=response.getWriter();
		try {
			connection=JDBCTools.getConnection();
			connection2=JDBCTools.getConnection();
			connection3=JDBCTools.getConnection();
			connection4=JDBCTools.getConnection();
			
			//查询自己负责的所有区域的id
			areIdList=searchMyArea(connection,uId);
		//	System.out.println("areIdList");
//			for(Area a:areIdList){
//				System.out.println(a.toString());
//			}
			
			if(areIdList.size()==0){
				
			}else{
				//查询出自己负责的所有下一级区域
				NextAreaIdList=searchNextArea(areIdList,connection2);
//				System.out.println("NextAreaIdList");
//				for(Area a:NextAreaIdList){
//					System.out.println(a.toString());
//				}
//				
				if(NextAreaIdList.size()==0){
					
				}else{
					//找出自己负责的子区域上报的隐患
					eventList=searchAllEvent(NextAreaIdList,connection3);
//					System.out.println("eventList");
//					for(EventRecord a:eventList){
//						System.out.println(a.toString());
//					}
					
//					for(EventRecord e:eventList){
//						System.out.println("a");
//						e.toString();
//					}
					if(eventList.size()==0){
						
					}else{
						
						total=getTotal(NextAreaIdList,connection4);
					}
					
					
				}
				
			}
			
			json.put("eventList", eventList);
			json.put("total", total);
			out.write(json.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{  
			 JDBCTools.release(null, null, connection);
	            JDBCTools.release(null, null, connection2);
	            JDBCTools.release(null, null, connection3);
	            JDBCTools.release(null, null, connection4);
            out.flush();
            out.close();
        }  
		
		
	}
	private String getTotal(List<Area> nextAreaIdList, Connection connection2) {

		String areaId="";
		Object object=new Object();
		int i=1;
		for(Area area:nextAreaIdList){
			areaId+="areId = '"+area.getAreId()+"'";
			
			if(i!=nextAreaIdList.size()){
				i++;
				areaId+=" or ";
			}
			}
		sql="SELECT COUNT(*) FROM eventRecord WHERE "+areaId;
//		System.out.println("tol:"+sql);
		try {
			object=eventRecordDao.getForValue(connection2, sql);
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return object.toString();
	
	}

	private List<Area> searchMyArea(java.sql.Connection connection2,String uId){
		sql="SELECT * FROM area WHERE uId= ?";
		List<Area> areList=new ArrayList<Area>();
		try {
			areList= areaDao.getForList(connection2, sql, uId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return areList;
	}
	
	//查询出自己负责的所有子区域
	private  List<Area> searchNextArea(List<Area> list,java.sql.Connection connection2){
		sql="SELECT * FROM area WHERE parAreaId= ?";
		List<Area> NextAreaIdList=new ArrayList<Area>();
		try {
		for(Area area:list){
			NextAreaIdList.addAll(areaDao.getForList(connection2, sql, area.getAreId()));
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		return NextAreaIdList;
	}
	private List<EventRecord> searchAllEvent(List<Area> list,java.sql.Connection connection2){
		String areaId="";
		int i=1;
		//拼接sql、将所有区域Id拼接到sql上
		for(Area area:list){
			areaId+="areId = '"+area.getAreId()+"'";
			
			if(i!=list.size()){
				i++;
				areaId+=" or ";
			}
			}
		sql="SELECT * FROM eventRecord WHERE "+areaId+" order by replyTime desc  limit ?,10";
		List<EventRecord>  eventList=new ArrayList<EventRecord>();
//		System.out.println("sql:"+sql);
		try {
			eventList=eventRecordDao.getForList(connection2, sql,startPage);
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
		return eventList;
	}
}
