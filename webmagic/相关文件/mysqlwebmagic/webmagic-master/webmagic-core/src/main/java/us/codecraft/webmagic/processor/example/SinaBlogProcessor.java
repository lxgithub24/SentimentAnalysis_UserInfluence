 package us.codecraft.webmagic.processor.example; 
 
 
 import org.jsoup.Jsoup;

import us.codecraft.webmagic.Page; 
import us.codecraft.webmagic.Site; 
import us.codecraft.webmagic.Spider; 
import us.codecraft.webmagic.pipeline.DataPipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor; 

 public class SinaBlogProcessor implements PageProcessor { 
 
 
     public static final String URL_LIST = "http://blog\\.sina\\.com\\.cn/s/articlelist_1487828712_0_\\d+\\.html"; 
 
 
     public static final String URL_POST = "http://blog\\.sina\\.com\\.cn/s/blog_\\w+\\.html"; 
 
 
     private Site site = Site 
             .me() 
             .setDomain("blog.sina.com.cn") 
             .setSleepTime(3000) ;

 
     @Override 
     public void process(Page page) { 
         //列表页 
    	 if (page.getUrl().regex(URL_LIST).match()) { 
             page.addTargetRequests(page.getHtml().xpath("//div[@class=\"articleList\"]").links().regex(URL_POST).all()); 
             page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all()); 
             //文章页 
         } else { 
             page.putField("title", page.getHtml().xpath("//div[@class='articalTitle']/h2/allText()")); 
             //page.putField("title", Jsoup.parse(page.getHtml().xpath("//div[@class='articalTitle']/h2").toString()).text());
             page.putField("content", page.getHtml().xpath("//div[@id='articlebody']//div[@class='articalContent']/allText()")); 
             page.putField("date", 
                     page.getHtml().xpath("//div[@id='articlebody']//span[@class='time SG_txtc']").regex("\\((.*)\\)")); 
         } 
     } 
 
 
     @Override 
     public Site getSite() { 
         return site; 
     } 
 
 
     public static void main(String[] args) { 
         Spider.create(new SinaBlogProcessor()).addUrl("http://blog.sina.com.cn/s/articlelist_1487828712_0_1.html").addPipeline(new DataPipeline()).run(); 
     } 
 } 
