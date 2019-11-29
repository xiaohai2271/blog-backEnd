package cn.celess.blog.util;

import cn.celess.blog.entity.Article;
import cn.celess.blog.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 小海
 * @Date： 2019/07/30 17:29
 * @Description：
 */
@Component
public class SitemapGenerateUtil {

    @Value("${sitemap.path}")
    private String path;
    private Map<String, String> urlList;

    @Autowired
    ArticleMapper articleMapper;

    @Async
    public void createSitemap() {
        initList();
        if ("".equals(path) || "classpath".equals(path)) {
            path = System.getProperty("user.dir")+"/sitemap.xml";
        }
        File file = new File(path);
        try {
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        DocumentBuilder db = getDocumentBuilder();
        Document document = db.newDocument();
        document.setXmlVersion("1.0");
        document.setXmlStandalone(true);
        Element urlset = document.createElement("urlset");
        urlset.setAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");
        // 创建url 结点
        urlList.forEach((s, s2) -> {
            Element url = document.createElement("url");
            Element loc = document.createElement("loc");
            Element lastmod = document.createElement("lastmod");
            loc.setTextContent(s);
            lastmod.setTextContent(s2);
            url.appendChild(loc);
            url.appendChild(lastmod);
            urlset.appendChild(url);
        });
        document.appendChild(urlset);
        try {
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.transform(new DOMSource(document), new StreamResult(file));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private void initList() {
        urlList = new HashMap<>();
        urlList.put("https://www.celess.cn", DateFormatUtil.getForXmlDate(new Date()));
        urlList.put("https://www.celess.cn/links", DateFormatUtil.getForXmlDate(new Date()));
        urlList.put("https://www.celess.cn/leaveMsg", DateFormatUtil.getForXmlDate(new Date()));
        List<Article> articles = articleMapper.findAll();
        articles.forEach(article -> {
            urlList.put("https://www.celess.cn/article/" + article.getId(), DateFormatUtil.getForXmlDate(
                    article.getUpdateDate() == null ? article.getPublishDate() : article.getUpdateDate()));
        });
    }


    private static DocumentBuilder getDocumentBuilder() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return db;
    }

}

