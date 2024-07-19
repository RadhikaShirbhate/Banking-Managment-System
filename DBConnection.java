package Test;

import static Test.DBInfo.*;

import java.sql.Connection;
import java.sql.DriverManager;
public class DBConnection
{
	private static Connection con;
	private DBConnection() {}
	static
	{
		try {
				Class.forName(driver);
				con = DriverManager.getConnection(dbURL,uName,pWord);
			}
		catch(Exception e) 
			{
				e.printStackTrace();
			}
	}
			public static Connection getCon()
			{
				return con;
			}
	
}