package us.codecraft.webmagic.selector;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.utils.EnvironmentUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Selectable plain text.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
public class Html extends PlainText {

    private Logger logger = Logger.getLogger(getClass());

    /**
     * Store parsed document for better performance when only one text exist.
     */
    private Document document;

    public Html(List<String> strings) {
        super(strings);
    }

    public Html(String text) {
        super(text);
        try {
            this.document = Jsoup.parse(text);
        } catch (Exception e) {
            logger.warn("parse document error ", e);
        }
    }

    public Html(Document document) {
        super(document.html());
        this.document = document;
    }

    public static Html create(String text) {
        return new Html(text);
    }

    @Override
    protected Selectable select(Selector selector, List<String> strings) {
        List<String> results = new ArrayList<String>();
        for (String string : strings) {
            String result = selector.select(string);
            if (result != null) {
                results.add(result);
            }
        }
        return new Html(results);
    }

    @Override
    protected Selectable selectList(Selector selector, List<String> strings) {
        List<String> results = new ArrayList<String>();
        for (String string : strings) {
            List<String> result = selector.selectList(string);
            results.addAll(result);
        }
        return new Html(results);
    }

    @Override
    public Selectable smartContent() {
        SmartContentSelector smartContentSelector = Selectors.smartContent();
        return select(smartContentSelector, strings);
    }

    @Override
    public Selectable links() {
        return xpath("//a/@href");
    }

    @Override
    public Selectable xpath(String xpath) {
        if (EnvironmentUtil.useXsoup()) {
            XsoupSelector xsoupSelector = new XsoupSelector(xpath);
            if (document != null) {
                return new Html(xsoupSelector.selectList(document));
            }
            return selectList(xsoupSelector, strings);
        } else {
            XpathSelector xpathSelector = new XpathSelector(xpath);
            return selectList(xpathSelector, strings);
        }
    }

    @Override
    public Selectable $(String selector) {
        CssSelector cssSelector = Selectors.$(selector);
        if (document != null) {
            return new Html(cssSelector.selectList(document));
        }
        return selectList(cssSelector, strings);
    }

    @Override
    public Selectable $(String selector, String attrName) {
        CssSelector cssSelector = Selectors.$(selector, attrName);
        if (document != null) {
            return new Html(cssSelector.selectList(document));
        }
        return selectList(cssSelector, strings);
    }

    public Document getDocument() {
        return document;
    }

    public String getText() {
        if (strings != null && strings.size() > 0) {
            return strings.get(0);
        }
        return document.html();
    }

    /**
     * @param selector
     * @return
     */
    public String selectDocument(Selector selector) {
        if (selector instanceof ElementSelector) {
            ElementSelector elementSelector = (ElementSelector) selector;
            return elementSelector.select(getDocument());
        } else {
            return selector.select(getText());
        }
    }

    public List<String> selectDocumentForList(Selector selector) {
        if (selector instanceof ElementSelector) {
            ElementSelector elementSelector = (ElementSelector) selector;
            return elementSelector.selectList(getDocument());
        } else {
            return selector.selectList(getText());
        }
    }
}
