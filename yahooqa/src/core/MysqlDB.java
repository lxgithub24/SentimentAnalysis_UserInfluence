package core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;




public class MysqlDB 
{
	 static Connection conn;
	static Statement stm;
	public static  Connection connect() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		String DB_URL="jdbc:mysql://localhost:3306/trec?useUnicode=true&characterEncoding=UTF-8";
		String USER = "root";
		String PASS = "123";
		System.out.println("Connecting to database...");
		Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
		return conn;		
	}
	
	public static void insert(String sql) throws ClassNotFoundException, SQLException 
	{
		conn=connect();
		stm =(Statement) conn.createStatement();
		int rst = stm.executeUpdate(sql);
		System.out.println("插入数据"+rst+"条");
		conn.close();
	}
	//执行数据库查询语句
	public static  boolean select(String name) throws ClassNotFoundException, SQLException{
		conn=connect();
		String sql = "select * from user where AID = \""+name+"\"" ;
	    PreparedStatement stmt = conn.prepareStatement(sql);
	    ResultSet rst = stmt.executeQuery(sql);
	    rst.last();
	    int count=rst.getRow();
	    if (count==0) {
			return false;
		}else {
			return true;
		}
		
		
	}
	
	
	public static void insertuser(String sql) throws ClassNotFoundException, SQLException{
		
		conn=connect();
		stm=conn.createStatement();
		int rst = stm.executeUpdate(sql);
		System.out.println("用户信息插入成功！");
		conn.close();
		
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		MysqlDB db =new MysqlDB();
		db.connect();
//		String sql = "INSERT INTO test1(name, age)"+ " VALUES ('Tom1', '32')";
//		db.insert(sql);
		String name="/activity/questions?show=SUZ6DZ6WET6W7ZDMJUQBL6PRD4&t=g";
//		boolean row = db.select(name);
//		System.out.println(row);
//		 String sql = "insert into users(AID,level,points,BArate,question,answer)"
//				 		+"values('/activity/questions?show=SUZ6DZ6WET6W7ZDMJUQBL6PRD4&t=g',7,30,45,20,50)";
//	      db.insertuser(sql);
	}
}
