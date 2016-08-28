package us.codecraft.webmagic.selector;

import org.apache.log4j.Logger;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import us.codecraft.webmagic.utils.Experimental;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Extract the text content of html.<br>
 * Using Readability algorithm: find parents of all p tags.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
@Experimental
public class SmartContentSelector implements Selector {

    private Logger logger = Logger.getLogger(getClass());

    public SmartContentSelector() {
    }

    @Override
    public String select(String text) {
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode tagNode = htmlCleaner.clean(text);
        if (tagNode == null) {
            return null;
        }
        TagNode[] nodes = tagNode.getElementsByName("p", true);
        TagNode[] pres = tagNode.getElementsByName("pre", true);
        Map<TagNode, Double> pDensityCountMap = new HashMap<TagNode, Double>();
        countPdensity(nodes, pDensityCountMap);
        countPdensity(pres, pDensityCountMap);
        for (TagNode pre : pres) {
            addCounter(pre, pDensityCountMap, 2);
        }
        List<Map.Entry<TagNode, Double>> sortList = new ArrayList<Map.Entry<TagNode, Double>>();
        if (pDensityCountMap.size() == 0) {
            return null;
        }
        for (Map.Entry<TagNode, Double> entry : pDensityCountMap.entrySet()) {
//            if (logger.isDebugEnabled()) {
//                logger.debug("p\t" + entry.getKey().getName() + "#" + entry.getKey().getAttributeByName("id") +
//                        "@" + entry.getKey().getAttributeByName("class") + ":" + entry.getValue());
//            }
            sortList.add(entry);
        }

        Collections.sort(sortList, new Comparator<Map.Entry<TagNode, Double>>() {
            @Override
            public int compare(Map.Entry<TagNode, Double> o1, Map.Entry<TagNode, Double> o2) {
                Double d1 = o1.getValue();
                Double d2 = o2.getValue();
                return -d1.compareTo(d2);
            }
        });
        TagNode contentNode = sortList.get(0).getKey();
        if (logger.isDebugEnabled()) {
            logger.debug("p\t" + contentNode.getName() + "#" + contentNode.getAttributeByName("id") +
                    "@" + contentNode.getAttributeByName("class"));
        }
        return htmlCleaner.getInnerHtml(contentNode);
    }

    private void addCounter(TagNode node, Map<TagNode, Double> countMap, double delta) {
        Double counter = countMap.get(node);
        if (counter == null) {
            countMap.put(node, delta);
        } else {
            countMap.put(node, counter + delta);
        }
    }

    private static final double parentWeight = 0.7;

    private void countPdensity(TagNode[] nodes, Map<TagNode, Double> pDensityCountMap) {
        for (TagNode node : nodes) {
            if (node == null) {
                continue;
            }
            TagNode parent = node.getParent();
            double pDensity = 1;
            while (parent != null) {
                addCounter(parent, pDensityCountMap, pDensity);
                parent = parent.getParent();
                pDensity = pDensity * parentWeight;
            }
        }
    }

    private TagNode findLowestCommonParent(List<TagNode> tagNodes, int maxMargin, Map<TagNode, AtomicInteger> countMap) {
        TagNode contentNode = tagNodes.get(0);
        return contentNode;
    }

    @Override
    public List<String> selectList(String text) {
        throw new UnsupportedOperationException();
    }
}
