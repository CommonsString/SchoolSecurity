package infoSearchAndFix;

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
import entity.Area;
import net.sf.json.JSONObject;

//查找出自己管理的区域
@WebServlet("/infoSearchAndFix/loadManageArea")
public class loadManageArea extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	areaDao areaDao=new areaDao();
	
    public loadManageArea() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("loadManageArea");

		json=new JSONObject();
		PrintWriter out=response.getWriter();
		List<Area> areaList=new ArrayList<Area>();
		String uId=request.getParameter("uId");
//		System.out.println("loaduId:"+uId);
		
		//通过uId查找出自己管理的区域
		String sql="select * from area where uId= ? ";
		try {
			connection=JDBCTools.getConnection();
			areaList=areaDao.getForList(connection, sql,uId);
//			for(Area a:areaList){
//				System.out.println(a.toString());
//			}
			json.put("areaList", areaList);
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
