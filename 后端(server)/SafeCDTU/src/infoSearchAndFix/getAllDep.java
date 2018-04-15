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
import eneityDAO.departmentDao;
import entity.Department;
import net.sf.json.JSONObject;

//得到所有的部门   用于用户选择自己的部门
@WebServlet("/infoSearchAndFix/getAllDep")
public class getAllDep extends HttpServlet {
	private static final long serialVersionUID = 1L;
	java.sql.Connection connection=null;
	JSONObject json=null; 
	departmentDao departmentDao=new departmentDao();
	
    public getAllDep() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("getAllDep");
		json=new JSONObject();
		PrintWriter out=response.getWriter();
		try {
			connection=JDBCTools.getConnection();
			String sql="SELECT * FROM department WHERE depId <> '0' ";
			List<Department> depList=new ArrayList<Department>();
			depList=departmentDao.getForList(connection, sql);
//			for(Department d:depList){
//				System.out.println(d.toString());
//			}
			json.put("depList", depList);
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
