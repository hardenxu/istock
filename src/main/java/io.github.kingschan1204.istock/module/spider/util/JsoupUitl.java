package io.github.kingschan1204.istock.module.spider.util;

import io.github.kingschan1204.istock.module.spider.entity.WebPage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.Proxy;
import java.util.Map;

/**
 * jsoup 通用工具
 * @author chenguoxiang
 * @create 2019-03-28 10:24
 **/
@Slf4j
public class JsoupUitl {

    /**
     * jsoup 通用请求方法
     * @param pageUrl url
     * @param method 方法
     * @param timeOut 超时时间单位毫秒
     * @param useAgent 请求头
     * @param referer 来源url
     * @return
     */
    public static WebPage getWebPage(String pageUrl,Connection.Method method,
                                     Integer timeOut,String useAgent,String referer) {
        return getWebPage(
                pageUrl, method,
                timeOut, useAgent, referer,
                null, null,
                true, true);
    }



    /**
     * jsoup 通用请求方法
     * @param pageUrl url
     * @param method 方法
     * @param timeOut 超时时间单位毫秒
     * @param useAgent 请求头
     * @param referer 来源url
     * @param cookie cookie
     * @param proxy 是否使用代理
     * @param ignoreContentType 是否忽略内容类型
     * @param ignoreHttpErrors 是否忽略http错误
     * @return
     */
    public static WebPage getWebPage(String pageUrl,Connection.Method method,
                              Integer timeOut,String useAgent,String referer,
                              Map<String,String> cookie,Proxy proxy,
                              Boolean ignoreContentType,Boolean ignoreHttpErrors) {
        WebPage webPage = null;
        Connection connection = Jsoup.connect(pageUrl)
                .timeout(null == timeOut ? 8000 : timeOut)
                .method(null == method ? Connection.Method.GET : method);
        if(null!=useAgent){
            connection.userAgent(useAgent);
        }
        if (null != ignoreContentType) {
            connection.ignoreContentType(ignoreContentType);
        }
        if (null != ignoreHttpErrors) {
            connection.ignoreHttpErrors(ignoreHttpErrors);
        }
        if (null != referer) {
            connection.referrer(referer);
        }
        if (null != proxy) {
            connection.proxy(proxy);
        }
        if (null != cookie) {
            connection.cookies(cookie);
        }
        try {
            Long start = System.currentTimeMillis();
            log.info(pageUrl);
            Connection.Response response = connection.execute();
            Document document = response.parse();
            webPage = new WebPage(System.currentTimeMillis() - start, pageUrl, document, document.html());
            return webPage;
        } catch (IOException e) {
            log.error("crawlPage {} {}", pageUrl, e);
            e.printStackTrace();
        }
        return null;
    }

}
