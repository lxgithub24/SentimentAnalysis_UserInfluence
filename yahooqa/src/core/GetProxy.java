package core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Templates;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import net.sf.json.JSONArray;
/**
 * @since 2016年3月30日
 * @author HuangDC
 * @desc 爬取单个实体的信息
 */
public class GetProxy {
		private Date date=new Date();
	    private int status=-1;
		private String title="";
		
	    private static int index;
	   	static String user_agent="";
	    static List<String> user_agents=new ArrayList<>(); 
	    static int useragent_len=0;
	    static String baikePrefix="http://baike.baidu.com";
		
	    
	   	/**
	   	 * 读取爬取数据时需要用来伪装的客户端
	   	 */
	    static{
	    	boolean flag=true;
	    	int i=0;
	    	while(flag){
	    		try {
	    			flag=false;
	    		} catch (Exception e) {
	    			i++;
	    			if(i==3){
	    				System.err.println("can not access internet.");
	    				System.exit(-1);
	    			}
	    		}
	    	}
	    	try (
	    		    InputStream fis = new FileInputStream("lib/user_agents");
	    		    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
	    		    BufferedReader br = new BufferedReader(isr);
	    		) {
	    			String line="";
	    		    while ((line = br.readLine()) != null) {
	    		        user_agents.add(line);
	    		    }
	    		} catch (IOException e) {
					e.printStackTrace();
				}
	    	useragent_len=user_agents.size();
	    }
	    
	    /**
	     * 判断输入的input是否存在于数据库中的索引表里，若存在则返回对应的实体，若不存在则爬取网页
	     * @param collection_index 数据库的索引表
	     * @param setEntity 存储对应的实体信息
	     * @param input:可以是输入的关键词，也可以是百科的网址
	     */
	    public static List<IPPort> GetAllProxy(int pages) throws UnsupportedEncodingException{
		    List<IPPort> ret= new ArrayList<IPPort>();
		    Document content = null;
	    		index=new Random().nextInt(useragent_len)%(useragent_len+1);
		    	user_agent=user_agents.get(index);
		    	String url = "http://www.xicidaili.com/nn/";
		    	for(int i=1;i<pages;i++){
			    	content=getProxyWeb(url+i);
			    	ret.addAll(getIPPort(content));
		    	}
		    	/*
		    	for(IPPort ip:ret){
		    		System.out.println(ip.getIp()+" "+ip.getPort());
		    	}
		    	*/
		    	System.out.println(ret.size());
		    	return ret; 
		    	
	    }
	    
	    /**
	     * 判断输入是否为网址
	     * @param str
	     * @return
	     */
	    private boolean isURL(String str) {
	    	str=str.replaceAll("^((https|http|ftp|rtsp|mms)?://)[^\\s]+", "");
	    	if(str.isEmpty()){
	    		return true;
	    	}
			return false;
		}
	    /**
	     * 爬取网页信息
	     * @param url
	     * @return
	     */
	    private static Document crawlWebContent(String url) {
	    	int i=0;
	    	boolean flag=true;
	    	Document content = null;
	    	while(flag){
	    		try {
	    			content=Jsoup.connect(url).userAgent(user_agent).get();
	    			flag=false;
	    		} catch (Exception e) {
	    			i++;
	    			try {
	    				int outtime=new Random().nextInt(3000);
	    				outtime = outtime + 2000;
						Thread.sleep(outtime);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
	    			if(i==4){
	    				System.out.println("failed to connect "+url);
	    				return null;
	    			}
	    		}
	    	}
			return content;
		}
	    /**
	     * 获取百科整个网页，并初始化status（-1表示不存在，1表示存在异义项，5表示存在唯一实体）
	     * @param tempurl
	     * @return
	     */
	    private static Document getProxyWeb(String tempurl) {
	    	Document webpage=crawlWebContent(tempurl);
	    	if(webpage==null||webpage.toString().length()<1000){
	    		return null;
	    	}
	        return webpage;
		}
	    /**
	     * 获取title
	     * @return
	     */
	    /**
	     * 获取网页正文
	     * @return
	     */
	    @SuppressWarnings("finally")
		private static List<IPPort> getIPPort(Document content){
		    List<IPPort> ret= new ArrayList<IPPort>();
		    if(content==null){
			    return null;
			    }
		    try{
				//Elements bb=content.getElementById("wgt-list").getElementsByClass("dl");//select("div[list-inner]");//dt class="dt mb-4 line
				Elements all_row=content.getElementById("ip_list").getElementsByTag("tr");
				//得到正文，然后做预处理（去除标点符号，繁转简体）
				for(Element e:all_row){
					    String str = e.text();  
					    String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3} \\d{1,5}";  
					    Pattern p = Pattern.compile(regex);  
					    Matcher m = p.matcher(str);  
					    while (m.find()) {  
					        String[] ipport = m.group().split(" ");
					        ret.add(new IPPort(ipport[0],Integer.parseInt(ipport[1])));
					    }  
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				return ret;
			}
		}
	    /**
	     * 获取URL
	     * @return
	     */
	    /**
	     * 获取当前word的状态（-1为没找到，1为存在异义项，5为唯一实体）
	     * @return
	     */
	    public int getStatus(){
	    	return status;
	    }
		/**
		 * 获取时间
		 * @return
		 */
		public Date getDate() {
			return date;
		}
	    
	    public static void main(String args[]) throws Exception{
	    	long start=System.currentTimeMillis();
	    	GetProxy extract=new GetProxy();
	    	List<IPPort> ips = extract.GetAllProxy(10);
	    	for(int i=0;i<ips.size();i++){
	    		System.out.println(ips.get(i).getIp()+"\t"+String.valueOf(ips.get(i).getPort()));
	    	}
	    	//System.out.println("keywords:"+extract.getKeyWords());
	    	System.out.println("time:"+extract.getDate());
	    	System.out.println("status:"+extract.getStatus());
	    //	System.out.println("desc:"+extract.getDesc());
	    	//System.out.println("synonym:"+extract.getAlias());
	    //	System.out.println("summary:"+extract.getSummary());
	    	//System.out.println("label:"+extract.getLabel());
	    	//System.out.println("infobox:"+extract.getInforBox());
	    	//System.out.println("polysemy:"+extract.getPoly().size()+" "+extract.getPoly().toString());
	    	long middle=System.currentTimeMillis();
	    	System.out.println("time:"+(middle-start));
	    }
}
