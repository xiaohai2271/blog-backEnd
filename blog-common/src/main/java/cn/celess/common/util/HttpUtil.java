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
     * 获取http请求的响应
     *
     * @param url url链接
     * @return 请求的响应
     */
    public static String getHttpResponse(String url) {
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setDownloadImages(false);
            webClient.getOptions().setActiveXNative(false);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            Page clientPage = webClient.getPage(url);
            return clientPage.getWebResponse().getContentAsString();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取渲染后的网页数据
     *
     * @param url url链接
     * @return 经js渲染后的网页源代码
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
