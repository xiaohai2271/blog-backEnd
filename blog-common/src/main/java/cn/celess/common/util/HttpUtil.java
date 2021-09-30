package cn.celess.common.util;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

/**
 * @Author: 小海
 * @Date: 2020-04-23 15:51
 * @Desc:
 */
public class HttpUtil {
    /**
     * FIXME
     */
    public static String get(String urlStr) {
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setDownloadImages(false);
            webClient.getOptions().setActiveXNative(false);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            Page clientPage = webClient.getPage(urlStr);
            if (clientPage.isHtmlPage()) {
                return clientPage.toString();
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * FIXME
     */
    public static String getAfterRendering(String url) {
        try (final WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setDownloadImages(false);
            webClient.getOptions().setActiveXNative(false);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            final HtmlPage page = webClient.getPage(url);
            return page.asXml();
        } catch (IOException e) {
            return null;
        }
    }
}
