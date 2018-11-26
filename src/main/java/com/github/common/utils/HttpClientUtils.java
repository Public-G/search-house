package com.github.common.utils;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClient连接池工具类
 *
 * @author ZEALER
 * @date 2018-11-20
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    // 连接管理器
    private final static PoolingHttpClientConnectionManager connManager;

    // request请求相关配置
    private final static RequestConfig defaultRequestConfig;

    // 重试处理器
    private final static HttpRequestRetryHandler customRetryHandler;

    private final static CloseableHttpClient httpclient;

    static {
        connManager = new PoolingHttpClientConnectionManager();
        // 最大连接数
        connManager.setMaxTotal(100);
        // 设置每个连接的路由数
        connManager.setDefaultMaxPerRoute(50);

        defaultRequestConfig = RequestConfig.custom()
                // 连接超时时间
                .setConnectTimeout(3 * 1000)
                // 读超时时间（等待数据超时时间）
                .setSocketTimeout(3 * 1000)
                // 从池中获取连接超时时间
                .setConnectionRequestTimeout(1000)
                .build();

        customRetryHandler = (exception, executionCount, context) -> {
            // 重试3次
            if (executionCount >= 3) {
                return false;
            }

            // 如果服务器丢掉了连接，那么就重试
            if (exception instanceof NoHttpResponseException) {
                return true;
            }

            // 不要重试SSL握手异常
            if (exception instanceof SSLHandshakeException) {
                return false;
            }

            //超时
            if (exception instanceof InterruptedIOException) {
                return false;
            }

            //目标服务器不可达
            if (exception instanceof UnknownHostException) {
                return false;
            }

            //ssl握手异常
            if (exception instanceof SSLException) {
                return false;
            }

            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest       request       = clientContext.getRequest();

            // HttpEntityEnclosingRequest指的是有请求体的request，比HttpRequest多一个Entity属性
            // 而常用的GET请求是没有请求体的，POST、PUT都是有请求体的
            // Rest一般用GET请求获取数据，故幂等，POST用于新增数据，故不幂等
            // 如果请求类型不是HttpEntityEnclosingRequest，被认为是幂等的，那么就重试
            return !(request instanceof HttpEntityEnclosingRequest);
        };

        httpclient = HttpClients.custom()
                .setConnectionManager(connManager) // 连接管理器
                .setDefaultRequestConfig(defaultRequestConfig) // 默认请求配置
                .setRetryHandler(customRetryHandler)  //重试策略
                .build();
    }


    /**
     * 处理HTTP GET请求
     *
     * @param url
     * @return
     */
    public static String httpGet(String url){
        // 创建一个Get请求，使用defaultRequestConfig配置
        HttpGet httpget = new HttpGet(url);

        return doExecute(httpget);
    }


    /**
     * 处理HTTP POST请求
     *
     * @param url
     * @param params
     * @return
     */
    public static String httpPost(String url, Map<String, Object> params){
        HttpPost httppost = new HttpPost(url);
        setParams(httppost, params);

        return doExecute(httppost);
    }

    /**
     * 执行HTTP请求
     *
     * @param request
     * @return
     */
    private static String doExecute(final HttpUriRequest request) {
        String result                  = null;
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(request);

            result = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (ClientProtocolException e) {
            logger.error("协议错误", e);

        } catch (ParseException e) {

            logger.error("解析错误", e);
        } catch (IOException e) {

            logger.error("IO错误", e);
        } finally {

            if (null != response) {
                try {
                    // 关闭HttpEntity输入流
                    EntityUtils.consume(response.getEntity());

                    //关闭连接(如果已经释放连接回连接池，则什么也不做)
                    response.close();
                } catch (IOException e) {
                    logger.error("释放连接错误", e);
                }
            }
        }

        return result;
    }

    /**
     * 设置请求体内容
     *
     * @param httppost
     * @param params
     */
    private static void setParams(HttpPost httppost, Map<String, Object> params) {
        if (params.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<>();

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
            }

            try {
                httppost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error("UrlEncoded错误", e);
            }
        }
    }
}
