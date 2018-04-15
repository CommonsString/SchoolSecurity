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
import entity.Area;
import net.sf.json.JSONObject;


//得到自己的区域
@WebServlet("/workOfEvent/getOwnArea")
public class getOwnArea extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	areaDao areaDao=new areaDao();
	PrintWriter out=null;
    public getOwnArea() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("getOwnArea");
		String uId=request.getParameter("uId");
//		System.out.println("uId:"+uId);
		try {
			json=new JSONObject();
			out=response.getWriter();
			connection=JDBCTools.getConnection();
			List<Area> areList=new ArrayList<Area>();
			String sql="SELECT * FROM area WHERE uId= ? ";
			areList=areaDao.getForList(connection, sql, uId);
			json.put("areList", areList);
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
