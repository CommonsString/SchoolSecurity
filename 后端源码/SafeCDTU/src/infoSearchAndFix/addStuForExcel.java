package infoSearchAndFix;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tools.JDBCTools;
import eneityDAO.studentDao;
import eneityDAO.userDao;
import net.sf.json.JSONObject;

@WebServlet("/infoSearchAndFix/addStuForExcel")
public class addStuForExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	userDao dao=new userDao();
	java.sql.Connection connection=null;
	JSONObject json=null;   
	 studentDao studentDao=new studentDao();
		PrintWriter out=null;
    public addStuForExcel() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   	 try {
//         // 对读取Excel表格标题测试
//         InputStream is = new FileInputStream("d:\\student3.xls");
//         ExcelReader excelReader = new ExcelReader();
////         String[] title = excelReader.readExcelTitle(is);
////         System.out.println("获得Excel表格的标题:");
////         for (String s : title) {
////             System.out.print(s + " ");
////         }
   		 
   		 
   		 
   		json=new JSONObject();
		out=response.getWriter();
		 connection=JDBCTools.getConnection();  
   	 // 对读取Excel表格内容测试
       Tools.ExcelReader excelReader = new Tools.ExcelReader();
       InputStream is2 = new FileInputStream("d:\\student3.xls");
       Map<Integer, String> map = excelReader.readExcelContent(is2);
       
      // System.out.println("获得Excel表格的内容:");
      
       String[][] students=mapToArray(map);
       
       JDBCTools.beginThing(connection);
       String sql="insert into student (stuNumber,clasz,name,tel,college) values (?,?,?,?,?)"; 
          
       studentDao.batch(connection, sql, students);
       JDBCTools.commitThing(connection);
          
       } catch (Exception e) {  
    	   JDBCTools.rollbackThing(connection);
           e.printStackTrace();  
       }finally{  
           JDBCTools.release(null, null, connection);  
       }    
		
	}
	private  String[][] mapToArray(Map<Integer, String> map){
		 //得到行数
      int mapSize=map.size();
      //得到列数
      String[] colStrings=map.get(1).split(",");
      int col=colStrings.length;
      
      //目标数组
      String[][] students;
      students= new String[mapSize][col];
      
      //将值复制给students
      int row=0;
      for (int i = 1; i <= map.size(); i++) {
          String[] bStrings=map.get(i).split(",");
          
          for(int j=0;j<bStrings.length;j++){
         	
         	 students[row][j]=bStrings[j];
          }
          row++;
      }
		return students;
		
	}

}
