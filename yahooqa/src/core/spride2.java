package core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import core.MysqlDB;

public class spride2 {
	
	public static void sprideHtml(String html) throws Exception, SQLException
	{
		boolean flag=true;
		Document doc;
		String  qID = null;
		String  question;
		String  body;
		String 	result = null;
		String  askhref;
		String  bestanswer_name = null;
		String  bestanswer_day = null;
		String  bestanswer_thumUp = null;
		String  bestanswer_thumDown = null;
		String  bestanswerLink = null;
		String publish_time=null;
		String comment=null;
		String  category;
		while (flag) {
			try {
				System.out.println(html);
				   int outtime=new Random().nextInt(10000);
				   outtime = outtime + 10000;
				   Thread.sleep(outtime);
	    		    System.out.println("#######");
	    			String user_Agent = user_angent.getRandomAgent();
	    			System.out.println(user_Agent);
	    			//模拟IP
	    			String IP=user_angent.getRandomIP();
	     			String[] ipinfo=IP.split("\t");
	     			System.getProperties().setProperty("proxySet", "true");
	 		        System.getProperties().setProperty("http.proxyHost",ipinfo[0]);
	 		        System.getProperties().setProperty("http.proxyPort", ipinfo[1]);
				   System.out.println("@@@@@@@@");
					doc=Jsoup.connect("https://answers.yahoo.com"+html).userAgent(user_Agent).get();
					
					System.out.println("网页信息");
//					System.out.println(doc.toString());
				     //问题ID
					 Element bestanswer = doc.getElementById("ya-center-rail").getElementById("ya-best-answer");
					 Element content = doc.getElementById("ya-center-rail").getElementById("ya-question-detail");
					 //抽取提问者连接，有可能为空
				      askhref = content.getElementById("yq-question-detail-profile-img").select("a").attr("href").replace("'", "LHM#");
				      System.out.println("*******************");
				      System.out.println("askhref"+askhref);
				      System.out.println(askhref.length());
				      System.out.println(askhref.isEmpty());
				      System.out.println("*******************");
				      
					  if (askhref.length()!=0){
						  AnswerLinkInfo.AnswerInfo(askhref);
					  }else {
						  System.out.println("askhref:"+askhref);
						  askhref=null;
					}
					  System.out.println("askhref:"+askhref);
					  bestanswerLink = bestanswer.getElementsByAttributeValue("class", "Fl-start Mstart-14").select("a").attr("href");
					  System.out.println("bestanswerLink:"+bestanswerLink);
					  if (bestanswerLink.length()!=0) {
						  System.out.println("bestanswerLink:"+bestanswerLink);
						
					  }else {
						  bestanswerLink=null;
					}
					  System.out.println("#########");
					  System.out.println("bestanswerLink:"+bestanswerLink);
					  if (bestanswerLink !=null) {
						  qID = doc.getElementById("ya-center-rail").select("div").attr("data-ya-question-id");
					       System.out.println("qid:"+qID);
							
					      //对相似问题具体内容的相似性进行解析 body
					       publish_time = doc.getElementsByAttributeValue("property", "og:question:published_time").attr("content");
						  question = content.select("h1").text().replace("'", "LHM#");//抽取问题
						  System.out.println("问题:"+question);
						//抽取问题的body，有可能为空
						  body=content.getElementsByAttributeValue("itemprop", "text").text().replace("'", "LHM#");
						  if (body.isEmpty()) {
							  body=null;
						  }
						  
					      //问题类别
					      category = doc.getElementById("brdCrb").text().replace("'", "LHM#");
						 		  			      
						  //bestanswer进行答案抽取，可能不存在
						 
						  if (bestanswer!=null) {
							  result = bestanswer.getElementsByAttributeValue("itemprop", "text").text().replace("'", "LHM#");
							  System.out.println("result:"+result);
							//最佳答案的人的名字
							  Elements bestanswer_Info = bestanswer.getElementsByAttributeValue("class", "Mstart-75 Pos-r");
//							  bestanswer_name = bestanswer_Info.select("div[class=Pt-15]").select("a").text().replace("'", "LHM#");//回答名字
							  bestanswer_day= bestanswer_Info.select("div[class=Pt-15]").select("span").first().text().replace("'", " ");//回答时间
							  System.out.println("bestanswer_day:"+bestanswer_day);
							  comment = bestanswer_Info.select("div[class=Pt-10]").select("div[data-ya-type=comment]").text();
							  System.out.println("comment:"+comment);
							  //最佳答案的点赞数
							  bestanswer_thumUp = bestanswer.getElementsByAttributeValue("data-ya-type", "thumbsUp").select("div[itemprop=upvoteCount]").text().replace("'", "LHM#");
							  bestanswer_thumDown = bestanswer.getElementsByAttributeValue("data-ya-type", "thumbsDown").select("div[class=D-ib Mstart-23 count]").text().replace("'", "LHM#");
							  System.out.println("bestanswer_thumUp:"+bestanswer_thumUp);
							  System.out.println("bestanswer_thumDown:"+bestanswer_thumDown);				
							  
							 
					}
					  //最佳答案的人信息链接
						  System.out.println("panduan");
						  String sql = "INSERT INTO bestanswer(qID, query, body,answer, bestanswer, time,AID,categeory,thump_up,thump_down,comment,publish_time,askhref)"    
					                + " VALUES ('"+qID+"','"+question+"','"+body+"','"+result+  
					                "','"+"0"+"','"+bestanswer_day+"','"+bestanswerLink+"','"+category+"','"+  
					                bestanswer_thumUp+"','"+bestanswer_thumDown+"','"+comment+"','"+publish_time+"','"+askhref+"')";
						     MysqlDB.insert(sql);
						     writeTXT.writespace(qID+"\t"+question+"\t"+body+"\t"+result+"\t"+0+"\t"+bestanswer_day+"\t"+bestanswerLink+"\t"+category+"\t"+bestanswer_thumUp+"\t"+bestanswer_thumDown+"\t"+comment+"\t"+publish_time+"\t"+askhref);
						     System.out.println("问答对和最佳回答者个人信息");
						     System.out.println(qID+"\t"+question+"\t"+body+"\t"+result+"\t"+0+"\t"+bestanswer_day+"\t"+bestanswerLink+"\t"+category+"\t"+bestanswer_thumUp+"\t"+bestanswer_thumDown+"\t"+comment+"\t"+publish_time+"\t"+askhref);
						     writeTXT.writeuser(bestanswerLink);
						     AnswerLinkInfo.AnswerInfo(bestanswerLink);
				}
					
					//一个问题的所有答案是否需要翻页
					String document=doc.getElementById("ya-center-rail").toString();
					if (document.contains("id=\"ya-qn-pagination\"")) {
						System.out.println("*********");
						Elements Pagelist = doc.getElementById("ya-qn-pagination").select("a");
						System.out.println("Pagelist:"+Pagelist);
						System.out.println(Pagelist.size());
							for (int i = 0; i <Pagelist.size()-1; i++) {
								String page = Pagelist.get(i).attr("href");
								System.out.println(page);
								spride2.SprideHtmlInfo(html);
							}
					}else {
						System.out.println("########");
						System.out.println("只有一个网页存在");
						spride2.SprideHtmlInfo1(doc);
					}
		  			flag=false;
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			
		}
		
		
	}
    //对雅虎问答某个具体问题-最佳答案-候选答案的爬取(问题答案网页的爬取)
	public static void SprideHtmlInfo(String similarUrl) throws Exception {
		// TODO Auto-generated method stub
		boolean flag=true;
		Document doc=null;
		String  qID = null;
		String  question;
		String  body;
		String  askhref;
		String  answer_item;
		String  answer_item_link = null;
		String  answer_name;
		String  answered_day;
		String  thumsUp;
		String  thumsDown;
		String  category;
		String publish_time=null;
		String comment=null;
	    while(flag)
	    {
	    	try {
	    		int outtime=new Random().nextInt(10000);
				outtime = outtime + 10000;
				Thread.sleep(outtime);
	    		System.out.println("#######");
	    	    String user_Agent = user_angent.getRandomAgent();
	    	    System.out.println(user_Agent);
	    	    
	    	    //模拟ip
	    	    String IP=user_angent.getRandomIP();
    			String[] ipinfo=IP.split("\t");
    			System.getProperties().setProperty("proxySet", "true");
		        System.getProperties().setProperty("http.proxyHost",ipinfo[0]);
		        System.getProperties().setProperty("http.proxyPort", ipinfo[1]);
	    		doc = Jsoup.connect("https://answers.yahoo.com"+similarUrl).userAgent(user_Agent).get();

      //问题ID
		  qID = doc.getElementById("ya-center-rail").select("div").attr("data-ya-question-id"); 
		  System.out.println("qid:"+qID);

      //对相似问题具体内容的相似性进行解析 body
		  Element content = doc.getElementById("ya-center-rail").getElementById("ya-question-detail");
		  question = content.select("h1").text().replace("'", "LHM#");//抽取问题
		  System.out.println("问题:"+question);
		  publish_time=doc.getElementsByAttributeValue("property", "og:question:published_time").attr("content");
	 //抽取问题的body，有可能为空
		  body=content.getElementsByAttributeValue("itemprop", "text").text().replace("'", "LHM#");
		  if (body.isEmpty()) {
			  body=null;
		  } 
      //问题类别
		  category = doc.getElementById("brdCrb").text().replace("'", "LHM#");
//	  //抽取提问者连接，有可能为空
		  askhref = content.getElementById("yq-question-detail-profile-img").select("a").attr("href").replace("'", "LHM#");
		  if (askhref.isEmpty()) {
			  askhref=null;
		  }	 
	 //对候选答案进行抽取  有的用户可能没有连接
		  Element candidate = doc.getElementById("ya-center-rail").getElementById("ya-qn-answers");
		  if (candidate !=null) {
			  int i=0;
			  Elements candidates = candidate.select("li");
			  for (i=0;i<candidates.size();i++) {
				  answer_item = candidates.get(i).getElementsByAttributeValue("itemprop", "text").text().replace("'", "LHM#");
				  answer_item_link= candidates.get(i).getElementsByAttributeValue("class", "Pt-15").select("a").attr("href").replace("'", "LHM#");
				  if (answer_item_link.isEmpty()) {
					  answer_item_link=null;
					  Elements Nohref = candidates.get(i).getElementsByAttributeValue("class", "Pt-15").select("span").select("span[class=Clr-88]");
					  answer_name = Nohref.get(0).text().replace("'", "LHM#");			
					  answered_day=Nohref.get(1).text().replace("'", "LHM#");	
					  comment = candidates.get(i).getElementsByAttributeValue("class", "Pt-10").select("div[data-ya-type=comment]").text();
					  thumsUp=candidates.get(i).getElementsByAttributeValue("itemprop", "upvoteCount").text().replace("'", "LHM#");
					  thumsDown=candidates.get(i).getElementsByAttributeValue("data-ya-type", "thumbsDown").select("div[class=D-ib Mstart-23 count]").text().replace("'", "LHM#");
					  String sql ="INSERT INTO bestanswer(qID, query, body,answer, bestanswer, time,AID,categeory,thump_up,thump_down,comment,publish_time,askhref)"    
							  + " VALUES ('"+qID+"','"+question+"','"+body+"','"+answer_item+  
							  "','"+String.valueOf(i+1)+"','"+answered_day+"','"+answer_item_link+"','"+category+"','"+  
							  thumsUp+"','"+thumsDown+"','"+comment+"','"+publish_time+"','"+askhref+"')";
					  MysqlDB.insert(sql);
					  writeTXT.writespace(qID+"\t"+question+"\t"+body+"\t"+answer_item+"\t"+String.valueOf(i+1)+"\t"+answered_day+"\t"+answer_item_link+"\t"+category+"\t"+thumsUp+"\t"+thumsDown+"\t"+comment+"\t"+publish_time+"\t"+askhref);
					  System.out.println("候选答案和候选答案挥着个人信息");
					  System.out.println(qID+"\t"+question+"\t"+body+"\t"+answer_item+"\t"+String.valueOf(i+1)+"\t"+answered_day+"\t"+answer_item_link+"\t"+category+"\t"+thumsUp+"\t"+thumsDown+"\t"+comment+"\t"+publish_time+"\t"+askhref);
					  System.out.println("候选答案"+i+":"+answer_item);
					  
			
				  }else {
					  answer_name= candidates.get(i).getElementsByAttributeValue("class", "Pt-15").select("a").text().replace("'", "LHM#");
					  answered_day = candidates.get(i).getElementsByAttributeValue("class", "Clr-88").text().replace("'", "LHM#");
					  comment=candidates.get(i).getElementsByAttributeValue("class", "Pt-10").select("div[data-ya-type=comment]").text();
					  thumsUp=candidates.get(i).getElementsByAttributeValue("itemprop", "upvoteCount").text().replace("'", "LHM#");
					  thumsDown=candidates.get(i).getElementsByAttributeValue("data-ya-type", "thumbsDown").select("div[class=D-ib Mstart-23 count]").text().replace("'", "LHM#");
					
					  String sql ="INSERT INTO bestanswer(qID, query, body,answer, bestanswer, time,AID,categeory,thump_up,thump_down,comment,publish_time,askhref)"    
							  + " VALUES ('"+qID+"','"+question+"','"+body+"','"+answer_item+  
							  "','"+String.valueOf(i+1)+"','"+answered_day+"','"+answer_item_link+"','"+category+"','"+  
							  thumsUp+"','"+thumsDown+"','"+comment+"','"+publish_time+"','"+askhref+"')";
					MysqlDB.insert(sql);
					writeTXT.writespace(qID+"\t"+question+"\t"+body+"\t"+answer_item+"\t"+String.valueOf(i+1)+"\t"+answered_day+"\t"+answer_item_link+"\t"+category+"\t"+thumsUp+"\t"+thumsDown+"\t"+comment+"\t"+publish_time+"\t"+askhref);
					System.out.println("候选答案和候选答案挥着个人信息");
					System.out.println(qID+"\t"+question+"\t"+body+"\t"+answer_item+"\t"+String.valueOf(i+1)+"\t"+answered_day+"\t"+answer_item_link+"\t"+category+"\t"+thumsUp+"\t"+thumsDown+"\t"+comment+"\t"+publish_time+"\t"+askhref);
					writeTXT.writeuser(answer_item_link);
					AnswerLinkInfo.AnswerInfo(answer_item_link);
					System.out.println("候选答案"+i+":"+answer_item);
			    }

		   }	

	  		}
	     flag=false;

       }catch (Exception e) {
    	   			System.out.println("2访问失败");
       			}		
    }  
}

	
    //对雅虎问答某个具体问题-最佳答案-候选答案的爬取(问题答案网页的爬取)
	public static void SprideHtmlInfo1(Document doc) throws Exception {
		// TODO Auto-generated method stub
		String  qID = null;
		String  question;
		String  body;
		String  askhref;
		String  answer_item;
		String  answer_item_link = null;
		String  answer_name;
		String  answered_day;
		String  thumsUp;
		String  thumsDown;
		String  category;
		boolean flag=true;
		String publish_time=null;
		String comment=null;
			
	    
      //问题ID
	
			  qID = doc.getElementById("ya-center-rail").select("div").attr("data-ya-question-id");
//				 
//			     
			//   
			      System.out.println("qid:"+qID);
//					
			      //对相似问题具体内容的相似性进行解析 body
				  Element content = doc.getElementById("ya-center-rail").getElementById("ya-question-detail");
				  question = content.select("h1").text().replace("'", "LHM#");//抽取问题
				  System.out.println("问题:"+question);
				  
				  publish_time=doc.getElementsByAttributeValue("property", "og:question:published_time").attr("content");
				//抽取问题的body，有可能为空
				  body=content.getElementsByAttributeValue("itemprop", "text").text().replace("'", "LHM#");
				  if (body.isEmpty()) {
					  body=null;
				  } 
			      //问题类别
			      category = doc.getElementById("brdCrb").text().replace("'", "LHM#");
//				  //抽取提问者连接，有可能为空
			      Elements askhrefYN = content.getElementById("yq-question-detail-profile-img").select("a");
			    
				  askhref = content.getElementById("yq-question-detail-profile-img").select("a").attr("href").replace("'", "LHM#");
				  if (askhref.isEmpty()) {
					  askhref=null;
				  }	 
				 //对候选答案进行抽取  有的用户可能没有连接
				  Element candidate = doc.getElementById("ya-center-rail").getElementById("ya-qn-answers");
				  if (candidate !=null) {
					  int i=0;
					 Elements candidates = candidate.select("li");
					 System.out.println(candidates.size());
					  for (i=0;i<candidates.size();i++) {
						     answer_item = candidates.get(i).getElementsByAttributeValue("itemprop", "text").text().replace("'", "LHM#");
						     answer_item_link= candidates.get(i).getElementsByAttributeValue("class", "Pt-15").select("a").attr("href").replace("'", "LHM#");
						if (answer_item_link.isEmpty()) {
							answer_item_link=null;
							Elements Nohref = candidates.get(i).getElementsByAttributeValue("class", "Pt-15").select("span").select("span[class=Clr-88]");
							answer_name = Nohref.get(0).text().replace("'", "LHM#");			
							answered_day=Nohref.get(1).text().replace("'", "LHM#");	
							comment = candidates.get(i).getElementsByAttributeValue("class", "Pt-10").select("div[data-ya-type=comment]").text();
							thumsUp=candidates.get(i).getElementsByAttributeValue("itemprop", "upvoteCount").text().replace("'", "LHM#");
							thumsDown=candidates.get(i).getElementsByAttributeValue("data-ya-type", "thumbsDown").select("div[class=D-ib Mstart-23 count]").text().replace("'", "LHM#");
							String sql ="INSERT INTO bestanswer(qID, query, body,answer, bestanswer, time,AID,categeory,thump_up,thump_down,comment,publish_time,askhref)"    
									+ " VALUES ('"+qID+"','"+question+"','"+body+"','"+answer_item+  
									"','"+String.valueOf(i+1)+"','"+answered_day+"','"+answer_item_link+"','"+category+"','"+  
									thumsUp+"','"+thumsDown+"','"+comment+"','"+publish_time+"','"+askhref+"')";
							MysqlDB.insert(sql);
							writeTXT.writespace(qID+"\t"+question+"\t"+body+"\t"+answer_item+"\t"+String.valueOf(i+1)+"\t"+answered_day+"\t"+answer_item_link+"\t"+category+"\t"+thumsUp+"\t"+thumsDown+"\t"+comment+"\t"+publish_time+"\t"+askhref);
							System.out.println("候选答案和候选答案挥着个人信息");
							System.out.println(qID+"\t"+question+"\t"+body+"\t"+answer_item+"\t"+String.valueOf(i+1)+"\t"+answered_day+"\t"+answer_item_link+"\t"+category+"\t"+thumsUp+"\t"+thumsDown+"\t"+comment+"\t"+publish_time+"\t"+askhref);
							System.out.println("候选答案"+i+":"+answer_item);
						
						}else {
//								answer_name= candidates.get(i).getElementsByAttributeValue("class", "Pt-15").select("a").text().replace("'", "LHM#");
								answered_day = candidates.get(i).getElementsByAttributeValue("class", "Clr-88").text().replace("'", "LHM#");
								comment = candidates.get(i).getElementsByAttributeValue("class", "Pt-10").select("div[data-ya-type=comment]").text();
								thumsUp=candidates.get(i).getElementsByAttributeValue("itemprop", "upvoteCount").text().replace("'", "LHM#");
								thumsDown=candidates.get(i).getElementsByAttributeValue("data-ya-type", "thumbsDown").select("div[class=D-ib Mstart-23 count]").text().replace("'", "LHM#");
								String sql ="INSERT INTO bestanswer(qID, query, body,answer, bestanswer, time,AID,categeory,thump_up,thump_down,comment,publish_time,askhref)"    
						                + " VALUES ('"+qID+"','"+question+"','"+body+"','"+answer_item+  
						                "','"+String.valueOf(i+1)+"','"+answered_day+"','"+answer_item_link+"','"+category+"','"+  
						                thumsUp+"','"+thumsDown+"','"+comment+"','"+publish_time+"','"+askhref+"')";
								MysqlDB.insert(sql);
								writeTXT.writespace(qID+"\t"+question+"\t"+body+"\t"+answer_item+"\t"+String.valueOf(i+1)+"\t"+answered_day+"\t"+answer_item_link+"\t"+category+"\t"+thumsUp+"\t"+thumsDown+"\t"+comment+"\t"+publish_time+"\t"+askhref);
								System.out.println("候选答案和候选答案挥着个人信息");
								System.out.println(qID+"\t"+question+"\t"+body+"\t"+answer_item+"\t"+String.valueOf(i+1)+"\t"+answered_day+"\t"+answer_item_link+"\t"+category+"\t"+thumsUp+"\t"+thumsDown+"\t"+comment+"\t"+publish_time+"\t"+askhref);
								writeTXT.writeuser(answer_item_link);
								AnswerLinkInfo.AnswerInfo(answer_item_link);
								System.out.println("候选答案"+i+":"+answer_item);
						    }

					   }	

				  		}

}

    //用户回答的答案为最佳答案的网页爬去
	public  static void SprideBestAnswer(Document doc) throws Exception {
		// TODO Auto-generated method stub
				System.out.println("网页信息");
//				System.out.println(doc.toString());
				Elements bestAnswer_list = doc.getElementById("ya-center-rail").getElementById("ya-myanswers").select("li");
				System.out.println(bestAnswer_list.size());
				int bestnum=0;
				if (bestAnswer_list.size()!=0) {
					
					for (int  i= 0; i <bestAnswer_list.size(); i++) {
						//找到用户回答过的问题的挥着者的数量
						String[]num = bestAnswer_list.get(i).getElementsByAttributeValue("class", "Fz-12 Clr-888").text().split(" ");
						System.out.println("num:"+num[0]);
						int answer_Num= Integer.parseInt(num[0]);
						if (bestAnswer_list.size()==1 && answer_Num<=1) {
							break;
						}else {
							if (answer_Num>1) {
								//用户回答过的问题被评为最佳答案的url
								String userAQBest_url = bestAnswer_list.get(i).getElementsByAttributeValue("class","qstn-title Fz-15 Fw-b Wow-bw").select("a").attr("href");
								System.out.println("userAQBest_url:"+userAQBest_url);
								//用户回答过的问题被评为最佳答案的标题
								String userAQBest_subjiect = bestAnswer_list.get(i).getElementsByAttributeValue("class","qstn-title Fz-15 Fw-b Wow-bw").select("a").text();
								System.out.println("userAQBest_subjiect:"+userAQBest_subjiect);
								spride2.sprideHtml(userAQBest_url);
								bestnum=bestnum+1;
								System.out.println("bestnum====>"+bestnum);
								
							}
						}
						
					
						 
						
					}
					
//					continue;
				}else {
					System.out.println("用户权限关闭或无最佳答案");
//					continue;
				}	
}

	public static void main(String[] args) throws SQLException, Exception {
		String url = "/question/index?qid=20160218040319AAjXp0U";
		///question/index?qid=20151123122634AAQQLs7
		///question/index?qid=20151107160251AAgNYVL
		spride2.sprideHtml(url);
	}
}
