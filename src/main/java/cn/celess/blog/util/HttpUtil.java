package cn.celess.blog.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Objects;

/**
 * @Author: 小海
 * @Date: 2020-04-23 15:51
 * @Desc:
 */
public class HttpUtil {
    private static final OkHttpClient CLIENT = new OkHttpClient();


    /*public static String get(String urlStr) throws IOException {

        StringBuffer sb = new StringBuffer();

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlStr);

            //打开http
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            try (
                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
            ) {
                //将bufferReader的值给放到buffer里
                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                    sb.append(str);
                }
            }
        } finally {
            //断开连接
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return sb.toString();
    }
*/
    public static String get(String urlStr) {
        Request request = new Request.Builder()
                .url(urlStr)
                .get()
                .build();
        try (Response response = CLIENT.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            return null;
        }
    }


    public static String getAfterRendering(String url) {
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
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
