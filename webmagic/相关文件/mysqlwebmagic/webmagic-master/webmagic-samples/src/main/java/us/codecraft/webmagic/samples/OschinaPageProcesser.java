package us.codecraft.webmagic.samples;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 */
public class OschinaPageProcesser implements PageProcessor {

    @Override
    public void process(Page page) {
        List<String> strings = page.getHtml().regex("<a[^<>]*href=[\"']{1}(http://www\\.oschina\\.net/question/[\\w]+)[\"']{1}").all();
        page.addTargetRequests(strings);
        page.putField("title", page.getHtml().xpath("//div[@class='QTitle']/h1/a"));
        page.putField("content", page.getHtml().xpath("//div[@class='Question']//div[@class='Content']/div[@class='detail']"));
    }

    @Override
    public Site getSite() {
        return Site.me().setDomain("www.oschina.net").addStartUrl("http://www.oschina.net/").
                setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
    }
}
