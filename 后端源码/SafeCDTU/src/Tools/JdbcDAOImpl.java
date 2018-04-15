package Tools;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;  
  
/** 
 * 使用 QueryRunner 提供其具体的实现 
 * @param <T>: 子类需传入的泛型类型.  
 */  
public class JdbcDAOImpl<T> implements DAO<T> {  
    private QueryRunner queryRunner=null;  
    public Class<T> type;  
    
    
    
    
    public JdbcDAOImpl(){  
        queryRunner=new QueryRunner();  
        type=ReflectionUtils.getSuperGenericType(getClass());  
    }  
      
      
    @Override  
    public  int[] batch(Connection connection, String sql, Object[]... params)  
            throws SQLException {  
    		int[] flag = queryRunner.batch(connection, sql, params);
    		return flag;
    }  
    
   
  
    @Override  
    public <E> E getForValue(Connection connection, String sql, Object... args)  
            throws SQLException {  
        return (E)queryRunner.query(connection,sql,new ScalarHandler(),args);
      
        
    }  
    
    @Override  
    public List<T> getForList(Connection connection, String sql, Object... args)  
            throws SQLException {  
        return queryRunner.query(connection, sql, new BeanListHandler<>(type) ,args);
    }  
  
    @Override  
    public T get(Connection connection, String sql, Object... args)  
            throws SQLException {  
            return queryRunner.query(connection, sql,  
                    new BeanHandler<>(type), args);  
    }  
  
    
    @Override  
    public int update(Connection connection, String sql, Object... args)  
            throws SQLException { 
    	int flag = queryRunner.update(connection, sql, args);
    	return flag;
    }


	@Override
	public List<T> getForListForStu(Connection connection, String sql, Class type, Object... args) throws SQLException {
        return queryRunner.query(connection, sql, new BeanListHandler<>(type) ,args);
	}


}  
