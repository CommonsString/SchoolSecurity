package test;

import static org.junit.Assert.*;

import java.awt.event.MouseWheelEvent;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;  
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.rules.DisableOnDebug;

import com.eaio.uuid.UUIDTest;

import Tools.DAO;
import Tools.JDBCTools;
import eneityDAO.studentDao;
import eneityDAO.userDao;
import entity.User;

  
public class testDao {  
     
    userDao dao =new userDao();
	//userDao<user> dao = new userDao<user>();
    @Test  
    public void testBatch() {  
         
    }  
    
    @Test
  public void UUIDTest(){
	  String idString=UUID.randomUUID().toString().replaceAll("\\-", "");
	  System.out.println(idString);
  }
    @Test  
    public void testGetForValue() {  
    	
    	    	Connection connection=null;  
    	        
    	        try {  
    	            connection=JDBCTools.getConnection();  
    	            String sql="SELECT name " +  
    	                    "FROM users " +  
    	                    "WHERE name = ?";  
    	             String name=dao.getForValue(connection ,sql,"王");
    	            //user user1=dao.get(connection, sql);
    	             System.out.println(name);  
    	        } catch (Exception e) {  
    	            e.printStackTrace();  
    	        }finally{  
    	            JDBCTools.release(null, null, connection);  
    	        }  
    	    
    }  
  
    @Test  
    public void testGetForList() {  
    	Connection connection=null;  
        
        try {  
            connection=JDBCTools.getConnection();  
            String sql="SELECT * " +  
                    "FROM user ";  
             ArrayList list = (ArrayList) dao.getForList(connection, sql);
            //user user1=dao.get(connection, sql);
             System.out.println(list.toString());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }finally{  
            JDBCTools.release(null, null, connection);  
        }  
    }  
  
    @Test  
    public void testGet() throws SQLException {  
        Connection connection=null;  
          
        try {  
            connection=JDBCTools.getConnection();  
            String sql="SELECT * " +  
                    "FROM user " +  
                    "WHERE name = ?";  
             User user1=dao.get(connection, sql, "金");
            //user user1=dao.get(connection, sql);
             System.out.println(user1.toString());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }finally{  
            JDBCTools.release(null, null, connection);  
        }  
    }  
  
    @Test  
    public void testUpdate() {  
    	 Connection connection=null;
    	 try {
    	 // 对读取Excel表格内容测试
        Tools.ExcelReader excelReader = new Tools.ExcelReader();
        InputStream is2 = new FileInputStream("d:\\student3.xls");
        Map<Integer, String> map = excelReader.readExcelContent(is2);
        System.out.println("获得Excel表格的内容:");
       
        String[][] students=mapToArray(map);
    	
         
        
            connection=JDBCTools.getConnection();  
            String sql="insert into student (stuNumber,clasz,name,tel,college) values (?,?,?,?,?)"; 
            studentDao studentDao=new studentDao();
            studentDao.batch(connection, sql, students);
           
        } catch (Exception e) {  
            e.printStackTrace();  
        }finally{  
            JDBCTools.release(null, null, connection);  
        }    
    }  
    private static String[][] mapToArray(Map<Integer, String> map){
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
