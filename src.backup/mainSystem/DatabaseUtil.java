/**
 * 
 */
package mainSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

/**
 * @author ac ʢ���ܣ��Ͼ����Զ
 *
 */
public class DatabaseUtil {
	private static Connection conn=null;
	private static final String driverName="com.mysql.jdbc.Driver";
	private static final String userName="SDJ";
	private static final String userPassword="sdj808545365";
	private static final String url="jdbc:mysql://127.0.0.1:3306/weathermonitor";
//	�������ݿ�õ����Ӷ���conn
	public static void getDatebase() throws Exception{
		Class.forName(driverName);
	}
	/**
	 * @return conn
	 * @throws SQLException 
	 */
	public static Connection getConn() throws SQLException {
		conn=DriverManager.getConnection(url, userName, userPassword);
		return conn;
	}
	/**
	 * @param conn Ҫ���õ� conn
	 */
	public static void setConn(Connection conn) {
		DatabaseUtil.conn = conn;
	}
//	�ر����ݿ�����
    public static void closeConnection() throws SQLException{
    	if(conn!=null&&!conn.isClosed()){
    		conn.close();
    	}
    }
}
