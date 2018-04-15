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
import eneityDAO.areaDao;
import eneityDAO.eventRecordDao;
import entity.Area;
import entity.EventRecord;
import net.sf.json.JSONObject;


//按标题 查找用户自己管理的下级区域的隐患事件
@WebServlet("/workOfEvent/searchForUser")
public class searchForUser extends HttpServlet {
	java.sql.Connection connection=null;
	JSONObject json=null; 
	areaDao areaDao=new areaDao();
	eventRecordDao eventRecordDao=new eventRecordDao();
	String sql="";
	private static final long serialVersionUID = 1L;
	
       
    
    public searchForUser() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("search");
		String uId=request.getParameter("uId");
		String condition=request.getParameter("condition");
		
		List<Area> areIdList=new ArrayList<Area>();
		List<Area> NextAreaIdList=new ArrayList<Area>();
		List<EventRecord>  eventList=new ArrayList<EventRecord>();
		json=new JSONObject();
		PrintWriter out=response.getWriter();
		try {
			connection=JDBCTools.getConnection();
			//查询自己负责的所有区域的id
			areIdList=searchMyArea(connection,uId);
			if(areIdList.size()==0){
				
			}else{
				//查询出自己负责的所有下一级区域
				NextAreaIdList=searchNextArea(areIdList,connection);
				
				if(NextAreaIdList.size()==0){
					
				}else{
					//找出自己负责的子区域上报的隐患
					eventList=searchAllEvent(NextAreaIdList,connection,condition);
				}
				
				
			}
			
			
			
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
	
	//查询自己负责的所有区域的id
	private List<Area> searchMyArea(java.sql.Connection connection2,String uId){
		sql="SELECT * FROM area WHERE uId=?";
		try {
			return areaDao.getForList(connection2, sql, uId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//查询出自己负责的所有子区域
	private  List<Area> searchNextArea(List<Area> list,java.sql.Connection connection2){
		sql="SELECT * FROM area WHERE parAreaId=?";
		List<Area> NextAreaIdList=new ArrayList<Area>();
		try {
		for(Area area:list){
			NextAreaIdList.addAll(areaDao.getForList(connection2, sql, area.getAreId()));
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return NextAreaIdList;
	}
	
	//找出自己负责的子区域上报的隐患
	private List<EventRecord> searchAllEvent(List<Area> list,java.sql.Connection connection2,String condition){
		String areaId="";
		int i=1;
		for(Area area:list){
			areaId+="areId = '"+area.getAreId()+"'";
			
			if(i!=list.size()){
				i++;
				areaId+=" or ";
			}
			}
		sql="SELECT * FROM eventRecord WHERE ("+areaId+") AND title LIKE ? order by replyTime desc";
		List<EventRecord>  eventList=new ArrayList<EventRecord>();
		condition="%"+condition+"%";
		try {
			eventList=eventRecordDao.getForList(connection2, sql,condition);
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return eventList;
	}
}
