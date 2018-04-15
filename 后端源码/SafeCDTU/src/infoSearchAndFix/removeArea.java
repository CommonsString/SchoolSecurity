package infoSearchAndFix;

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

import org.omg.CORBA.PRIVATE_MEMBER;

import com.mysql.jdbc.Connection;

import Tools.JDBCTools;
import eneityDAO.areaDao;
import eneityDAO.departmentDao;
import eneityDAO.eventRecordDao;
import eneityDAO.userDao;
import entity.Area;
import net.sf.json.JSONObject;


//删除区域
@WebServlet("/infoSearchAndFix/removeArea")
public class removeArea extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	areaDao areaDao=new areaDao();
	userDao userDao=new userDao();
	eventRecordDao eventRecordDao=new eventRecordDao();
	departmentDao departmentDao=new departmentDao();
	boolean isOne=false;
    public removeArea() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		isOne=false;
		json=new JSONObject();
		PrintWriter out=response.getWriter();
		String areId=request.getParameter("areId");
//		System.out.println("removeArea AreId:"+areId);
		try {
			connection=JDBCTools.getConnection();
			JDBCTools.beginThing(connection);
			
			//如果删除的区域有子区域  则无法删除
			if(hasSubArea(areId,connection)){
				json.put("state", false);
				json.put("message", "此区域有子区域，无法删除，请先删除子区域");
				out.write(json.toString());
				JDBCTools.commitThing(connection);
			}
			else{
				//如果是一级区域    一级区域和部门有关联所以要将相关联的部门一起删除
				if(getParAreaId(areId,connection).equals("0")){
					//删除一级区域，清空该区域原人员的depId
					String sql="UPDATE users SET depId='' WHERE depId= ? ";
					userDao.update(connection, sql, areId);
					
					//删除一级区域对应的部门
					String sql0="delete from department  where depId = ? ";
					departmentDao.update(connection, sql0, areId);
					isOne=true;
				}
				//删除该区域
				String sql="delete from area  where areId = ? ";
				areaDao.update(connection, sql, areId);
				
				
				//将该区域上报的隐患的区域  全部换成最高级    防止数据无法查看  只有超管可以查看
				String sql2="UPDATE eventrecord SET areId='0' WHERE areId = ? ";
				eventRecordDao.update(connection, sql2, areId);
				
				
				json.put("state", true);
				if(isOne){
					json.put("message", "删除成功,删除的区域是一级区域，请通知原区域所属人员重新选择部门");
				}else{
					json.put("message", "删除成功");
				} 
				
				out.write(json.toString());
				JDBCTools.commitThing(connection);
			}
			
		} catch (Exception e) {
			JDBCTools.rollbackThing(connection);
			json.put("state", false);
			json.put("message", "删除失败");
			out.write(json.toString());
			e.printStackTrace();
		}finally{  
            JDBCTools.release(null, null, connection);
            out.flush();
            out.close();
        }  
		
	}
	private boolean hasSubArea(String areId,java.sql.Connection connection2){
		String sql="SELECT * from area  where parAreaId = ? ";
		List<Area> areaList=new ArrayList<Area>();
		try {
			areaList=areaDao.getForList(connection2, sql, areId);
			if(areaList.size()==0){
				return false;
			}
			else{
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	private String getParAreaId(String areId,java.sql.Connection connection2){
		String sql="SELECT parAreaId from area  where areId = ? ";
		try {
			return areaDao.getForValue(connection2, sql, areId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}

}
