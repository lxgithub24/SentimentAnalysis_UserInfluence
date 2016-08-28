package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBInsert {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException{
		   
		 DBInsert in= new DBInsert();
		 MysqlDB  MysqlDB=new MysqlDB();
	       //System.out.println(slem.lemmatize(text));
	        
//	       //连接数据库
//	        Class.forName("com.mysql.jdbc.Driver");
//			String DB_URL="jdbc:mysql://localhost:3306/yahootrec?useUnicode=true&characterEncoding=UTF-8";
//			String USER = "root";
//			String PASS = "123";
//			System.out.println("Connecting to database...");
//			Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
			
	        BufferedReader br = new BufferedReader(new FileReader(new File("E:/data/trec/user.txt")));
	        String line;
	        while ((line=br.readLine())!=null) {
	        	String[] qa=line.split("\t");
	        	String link = qa[0];
	        	String level = qa[1];
	        	String point=qa[2];
	        	String bestAnswers_percent=qa[3];
	        	String answer_num=qa[4];
	        	String question_num=qa[5];
//	        	String AID=qa[6];
//	        	String category=qa[7];
//	        	String thump_up=qa[8];
//	        	String thump_down=qa[9];
//	        	 String sql = "INSERT INTO behaviour(qID, query, body,answer, bestanswer, time,AID,categeory,thump_up,thump_down)"    
//			                + " VALUES ('"+qID+"','"+query+"','"+body+"','"+answer+  
//			                "','"+bestanswer+"','"+time+"','"+AID+"','"+category+"','"+  
//			                thump_up+"','"+thump_down+"')";
	        	String  sql = "INSERT INTO user(AID,level,points,BArate,a_num,q_num)"
	        		       +"VALUES ('"+link+"','"+level+"','"+point+"','"+bestAnswers_percent+"','"+answer_num+"','"+question_num+"')";
 			MysqlDB.insertuser(sql);
	        	
	        	
	}

}

}
