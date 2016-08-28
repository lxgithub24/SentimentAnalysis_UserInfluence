package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import yahoo.spride;

class yahoo_user {
	 

    
	public static  void UserInfo( ) throws Exception {
		FileReader fr=new FileReader(new File("data/userurl.txt"));
//		FileReader fr=new FileReader(new File("data/userurl"));
    	BufferedReader br=new BufferedReader(fr);
    	String line=br.readLine();
    	 int	Count =1;
    	while (line !=null) {
    	 
    		String Url = "https://answers.yahoo.com"+line.replace("questions", "answers")+"&view=best";
    		System.out.println(Url);
    		boolean flag=true;
    		Document doc=null;
     			while(flag)
    		    {
    		    	try {
    		    		//user_angent模拟浏览器
	    				int outtime=new Random().nextInt(10000);
	    				outtime = outtime + 10000;
						Thread.sleep(outtime);
    		    		    System.out.println("#######");
    		    			String user_Agent = user_angent.getRandomAgent();
    		    			
    		    			//模拟IP
    		    			String IP=user_angent.getRandomIP();
    		     			String[] ipinfo=IP.split("\t");
    		     			System.getProperties().setProperty("proxySet", "true");
    		 		        System.getProperties().setProperty("http.proxyHost",ipinfo[0]);
    		 		        System.getProperties().setProperty("http.proxyPort", ipinfo[1]);
    		    			
    		    			System.out.println(user_Agent);
    		    			doc = Jsoup.connect(Url).userAgent(user_Agent).get();   				
    		  				//获取用户等级
    		    			 String level= doc.getElementById("ya-membercard").getElementById("member-details").getElementById("level-text").select("p").text();
    		    			 System.out.println("level:"+level);
    		    			//得到用户的point,bestAnswers_percent,answer_num,question_num
    		    			 Element UserStatus = doc.getElementById("ya-membercard").getElementById("yan-mystatus");
    		    			 String point = UserStatus.getElementById("div-pt").select("span").first().text();
    		    			 System.out.println("point:"+point);
    		      
    		    			 String  bestAnswers_percent = UserStatus.getElementById("div-ba").select("span").first().text();
    		    			 System.out.println("bestAnswers_percent:"+bestAnswers_percent);
    		      
    		    			 String   answer_num = UserStatus.getElementById("div-answ").select("span").first().text();
    		    			 System.out.println("answer_num:"+answer_num);
    		      
    		    			 String   question_num = UserStatus.getElementById("div-ques").select("span").first().text();
    		    			 System.out.println("question_num:"+question_num);
    		    			 Element questions_answers = doc.getElementById("ya-center-rail").getElementById("ya-tabs-main");

    		    			 String  sql = "INSERT INTO user(AID,level,points,BArate,a_num,q_num)"
    	    	        		       +"VALUES ('"+line+"','"+level+"','"+point+"','"+bestAnswers_percent+"','"+answer_num+"','"+question_num+"')";
    		    			MysqlDB.insertuser(sql);
    		    			System.out.println("userurl用户信息");
    		    			writeTXT.writeuserinfo(line+"\t"+level+"\t"+point+"\t"+bestAnswers_percent+"\t"+answer_num+"\t"+question_num);
    		    			System.out.println("userurl用户信息");
    		      	        //用户回答的所有答案
    		    			
    		      	         flag=false;
    		      	         //用户回答的所有问题的网页链接----其下面的子连接有用户回答的最佳答案
    		      	         spride2.SprideBestAnswer(doc);
    		      	         System.out.println("爬取最佳答案");
    			
    		    	  } catch (Exception e) {
    				  		//System.out.println("3访问失败");
    				  		System.out.println(e.toString());
    					// TODO: handle exception
    		    		}
    		    	System.out.println("*********(**");
    	     		System.out.println(Count);
    	     		System.out.println("*********(**");
    	     		Count++;
    	    		line=br.readLine();
    		       } 
    			
     		}	
     		
		}
		
		
 		
 
			
		
	    
		    
	    

	public static void main(String[] args) throws Exception{
        
		UserInfo();
	}
}

