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
import eneityDAO.contextDao;
import entity.Area;
import net.sf.json.JSONObject;


//找出最顶级的区域
@WebServlet("/infoSearchAndFix/loadTopArea")
public class loadTopArea extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	areaDao areaDao=new areaDao();
    public loadTopArea() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("aaa");
		json=new JSONObject();
		PrintWriter out=response.getWriter();
		List<Area> areaList=new ArrayList<Area>();
		
		//顶级区域的id是0
		String sql="select * from area where areId = '0'";
		try {
			connection=JDBCTools.getConnection();
			areaList=areaDao.getForList(connection, sql);
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
