package core;

import java.sql.SQLException;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AnswerLinkInfo {
	
	public static void  AnswerInfo(String link) throws ClassNotFoundException, SQLException{
		
		boolean row = MysqlDB.select(link);
		if (!row){
			String Url = "https://answers.yahoo.com"+link;
			System.out.println(Url);
			boolean flag=true;
			Document doc=null;
	 			while(flag)
			    {
			    	try {
			    			int outtime=new Random().nextInt(10000);
	    					outtime = outtime + 10000;
						  	Thread.sleep(outtime);
    		    		    System.out.println("#######");
    		    			String user_Agent = user_angent.getRandomAgent();
			    			System.out.println(user_Agent);
			    			String IP=user_angent.getRandomIP();
			    			String[] ipinfo=IP.split("\t");
			    			System.getProperties().setProperty("proxySet", "true");
		    		        System.getProperties().setProperty("http.proxyHost",ipinfo[0]);
		    		        System.getProperties().setProperty("http.proxyPort", ipinfo[1]);
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
		    	        		       +"VALUES ('"+link+"','"+level+"','"+point+"','"+bestAnswers_percent+"','"+answer_num+"','"+question_num+"')";
			    			MysqlDB.insertuser(sql);
			    			writeTXT.writeuserinfo(link+"\t"+level+"\t"+point+"\t"+bestAnswers_percent+"\t"+answer_num+"\t"+question_num);
			    			System.out.println("回答者个人信息输出：");
			    			System.out.println(link+"\t"+level+"\t"+point+"\t"+bestAnswers_percent+"\t"+answer_num+"\t"+question_num);
			      	         flag=false;
//			      	         spride2.sprideAnswer(answer_url);
//			      	          	     int row = MysqlDB.select(url);
			      	         
				
			    	  } catch (Exception e) {
					  		System.out.println("3访问失败");
						// TODO: handle exception
			    		}

			       } 
		}
		
	}

}
