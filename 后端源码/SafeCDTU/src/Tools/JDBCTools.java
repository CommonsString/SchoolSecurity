package Tools;
import java.sql.Connection;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.sql.Statement;  
import javax.sql.DataSource;  
import com.mchange.v2.c3p0.ComboPooledDataSource;  
public class JDBCTools {  
    //数据库连接池应该只被初始化一次（多个项目共享一个连接池就够了）  
        private static DataSource dataSource=null;  
        static{  
            dataSource=new ComboPooledDataSource("helloC3P0");  
        }  
    //获取数据库连接的方法  
        public static Connection getConnection() throws Exception{  
            return dataSource.getConnection();  
        }  
          
          
    /** 
     * 2、关闭资源 
     * 关闭资源关闭 Statement 和 Connection和ResultSet 
     * @param rs 
     * @param statement 
     * @param conn 
     */  
    public static void release(ResultSet rs, Statement statement, Connection conn) {  
        if(rs != null){  
            try {  
                rs.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
          
          
        if (statement != null) {  
            try {  
                statement.close();  
            } catch (Exception e2) {  
                e2.printStackTrace();  
            }  
        }  
  
        if (conn != null) {  
            try {  
                conn.close();  
            } catch (Exception e2) {  
                e2.printStackTrace();  
            }  
        }  
    }  
      
    /** 
     * 关闭 Statement 和 Connection 
     * @param statement 
     * @param conn 
     */  
    public static void release(Statement statement, Connection conn) {  
        if (statement != null) {  
            try {  
                statement.close();  
            } catch (Exception e2) {  
                e2.printStackTrace();  
            }  
        }  
  
        if (conn != null) {  
            try {  
                conn.close();  
            } catch (Exception e2) {  
                e2.printStackTrace();  
            }  
        }  
    }  
  
    public static void beginThing(java.sql.Connection connection)
	{
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void commitThing(java.sql.Connection connection)
	{
		try {
			connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public static void rollbackThing(Connection connection)
	{
		try {
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
      
  
}  
