package StarGaze;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	
	private static ConnectionManager connInstance = null;
	
	private final String USERNAME = "anko7921";
	private final String PASSWORD = "ail2ueK9Daiw";
	static final String DB_URL = "jdbc:mysql://mysql.dsv.su.se:3306/anko7921?useLegacyDatetimeCode=false&serverTimezone=UTC";
	
	private Connection conn = null;
	
	
	
	//------------------------------Contructors------------------------------------------
	
	private ConnectionManager() {
	}
	
	
	//------------------------------Methods------------------------------------------
	
	
	
	public static ConnectionManager getInstance() {
		if (connInstance == null) {
			connInstance = new ConnectionManager();
		}
		return connInstance;
	}
	
	private boolean openConnection()
	{
		try {
				conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
				return true;
		}
		catch (SQLException e) {
			System.err.println(e);
			return false;
		}

	}
	
	public Connection getConnection() {
		if (conn == null) {
			if (openConnection()) {
				System.out.println("Connection opened");
				return conn;
			} else {
				return null;
			}
		}
		return conn;
	}
	
	public void close() {
		System.out.println("Closing connection");
		try {
			conn.close();
			conn = null;
		} catch (Exception e) {
		}
	}
}
