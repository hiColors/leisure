package com.github.lifelab.leisure.common.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 异步HTTP CLIENT工具类
 */
public class AsyncHttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(AsyncHttpUtil.class);

    private static final ThreadLocal<Exception> EXCEPTION = new ThreadLocal<>();

    private static final ThreadLocal<String> RESP_CONTENT_TYPE = new ThreadLocal<>();

    private static int MAX_TIMEOUT = 6000;

    private static int MAX_POOL_COUNT = 3;

    private static PoolingNHttpClientConnectionManager connMgr;

    private static ConnectingIOReactor ioReactor;

    private static CloseableHttpAsyncClient httpClient;

    static {
        try {
            if (LocalCacheUtils.getValue("http.async.timeout") != null) {
                MAX_TIMEOUT = Integer.valueOf(LocalCacheUtils.getValue("http.async.timeout").toString());
            }
            // 设置协议http和https对应的处理socket链接工厂的对象
            Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder.<SchemeIOSessionStrategy>create()
                    .register("http", NoopIOSessionStrategy.INSTANCE)
                    .register("https", new SSLIOSessionStrategy(createIgnoreVerifySSL()))
                    .build();
            IOReactorConfig config = IOReactorConfig.custom()
                    .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                    .setConnectTimeout(MAX_TIMEOUT).setSoTimeout(MAX_TIMEOUT)
                    .build();
            ioReactor = new DefaultConnectingIOReactor(config);
            // 设置连接池
            connMgr = new PoolingNHttpClientConnectionManager(ioReactor, null, sessionStrategyRegistry, null);
            // 设置连接池大小
            connMgr.setMaxTotal(LocalCacheUtils.getValue("http.async.pool.max") != null ?
                    Integer.valueOf(LocalCacheUtils.getValue("http.async.pool.max").toString()) : MAX_POOL_COUNT);
            connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
            httpClient = HttpAsyncClients.custom().setConnectionManager(connMgr).build();
        } catch (IOReactorException e) {
            logger.error("初始化 HttpAsyncClients 连接池异常：", e);
        }
    }


    public static Map<String, String> doOptions(String url) {
        return doOptions(url, null, null);
    }

    public static Map<String, String> doOptions(String url, Map<String, String> headerParams) {
        return doOptions(url, null, headerParams);
    }

    /**
     * 发送options请求
     *
     * @param url
     * @param futureCallback
     * @param headerParams
     * @return
     */
    public static Map<String, String> doOptions(String url,
                                                FutureCallback<HttpResponse> futureCallback,
                                                Map<String, String> headerParams) {
        Map<String, String> resultHeaders = new HashMap<>();
        EXCEPTION.remove();
        try {
            if (!httpClient.isRunning()) {
                httpClient.start();
            }
            HttpOptions httpOptions = new HttpOptions(url);
            handleHeader(headerParams, httpOptions);
            Future<HttpResponse> future = httpClient.execute(httpOptions, futureCallback);
            if (futureCallback == null) {
                HttpResponse response = future.get(MAX_TIMEOUT, TimeUnit.MILLISECONDS);
                if (response.getFirstHeader("content-type") != null) {
                    RESP_CONTENT_TYPE.set(response.getFirstHeader("content-type").getValue());
                }
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    logger.warn("http请求返回值异常 >>> [{}]:{}", url, statusCode);
                    EXCEPTION.set(new Exception("reqest error, status code:" + statusCode));
                    return resultHeaders;
                }
                for (Header header : response.getAllHeaders()) {
                    resultHeaders.put(header.getName(), header.getValue());
                }
            }
        } catch (Exception e) {
            logger.error("ERROR::", e);
            EXCEPTION.set(e);
        }
        return resultHeaders;
    }

    /**
     * 发送 GET 请求（HTTP），不带输入数据
     *
     * @param url
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, new HashMap<>(), null, null);
    }

    public static String doGet(String url, Map<String, Object> params) {
        return doGet(url, params, null, null);
    }

    public static String doGet(String url, FutureCallback<HttpResponse> futureCallback) {
        return doGet(url, new HashMap<>(), futureCallback, null);
    }

    public static String doGet(String url, FutureCallback<HttpResponse> futureCallback,
                               Map<String, String> headerParams) {
        return doGet(url, new HashMap<>(), futureCallback, headerParams);
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     *
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, Object> params, FutureCallback<HttpResponse> futureCallback,
                               Map<String, String> headerParams) {
        EXCEPTION.remove();
        String apiUrl = url;
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0) {
                param.append("?");
            } else {
                param.append("&");
            }
            param.append(key).append("=").append(params.get(key));
            i++;
        }
        apiUrl += param;
        String result = null;
        try {
            if (!httpClient.isRunning()) {
                httpClient.start();
            }
            HttpGet httpGet = new HttpGet(apiUrl);
            // 设定头信息
            handleHeader(headerParams, httpGet);
            Future<HttpResponse> future = httpClient.execute(httpGet, futureCallback);
            if (futureCallback == null) {
                HttpResponse response = future.get(MAX_TIMEOUT, TimeUnit.MILLISECONDS);
                if (response.getFirstHeader("content-type") != null) {
                    RESP_CONTENT_TYPE.set(response.getFirstHeader("content-type").getValue());
                }
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    logger.warn("http 请求返回值异常 >>> [{}]:{}", url, statusCode);
                    EXCEPTION.set(new Exception("reqest error, status code:" + statusCode));
                    return null;
                }
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    result = IOUtils.toString(instream, "UTF-8");
                }
            }
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            logger.error("ERROR：httpClient 请求异常 >>> {}", e);
            EXCEPTION.set(e);
        }
        return result;
    }

    /**
     * 发送 POST 请求（HTTP），不带输入数据
     *
     * @param apiUrl
     * @return
     */
    public static String doPost(String apiUrl) {
        return doPost(apiUrl, new HashMap<String, Object>(), null, null);
    }

    public static String doPost(String apiUrl, Map<String, Object> params) {
        return doPost(apiUrl, params, null, null);
    }

    public static String doPost(String apiUrl, Map<String, Object> params,
                                FutureCallback<HttpResponse> futureCallback) {
        return doPost(apiUrl, params, futureCallback, null);
    }

    public static String doPost(String apiUrl, Map<String, Object> params, Map<String, String> headerParams) {
        return doPost(apiUrl, params, null, headerParams);
    }

    /**
     * 发送 POST 请求（HTTP），K-V形式
     *
     * @param apiUrl    API接口URL
     * @param reqParams 参数map
     * @return
     */
    public static String doPost(String apiUrl,
                                Map<String, Object> reqParams,
                                FutureCallback<HttpResponse> futureCallback,
                                Map<String, String> headerParams) {
        EXCEPTION.remove();
        String httpStr = null;
        try {
            if (!httpClient.isRunning()) {
                httpClient.start();
            }
            HttpPost httpPost = new HttpPost(apiUrl);
            // 遍历参数，查找是否有文件类型数据(byte[])
            Map<String, List<HttpClientFileUploadBean>> fileMap = findFileParams(reqParams);

            if (fileMap.size() > 0) {
                // 请求参数中，存在文件上传
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                // 文件参数
                for (Map.Entry<String, List<HttpClientFileUploadBean>> entry : fileMap.entrySet()) {
                    for (HttpClientFileUploadBean bean : entry.getValue()) {
                        ContentBody contentBody = null;
                        if (bean.getFileObj() instanceof InputStream) {
                            contentBody = new InputStreamBody((InputStream) bean.getFileObj(), bean.getFileName());
                        } else if (bean.getFileObj() instanceof byte[]) {
                            contentBody = new ByteArrayBody((byte[]) bean.getFileObj(), bean.getFileName());
                        } else {
                            // 默认JDK对象序列化
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(baos);
                            oos.writeObject(bean.getFileObj());
                            contentBody = new ByteArrayBody(baos.toByteArray(), bean.getFileName());
                        }
                        builder.addPart(entry.getKey(), contentBody);
                    }
                }
                // 普通参数
                for (Map.Entry<String, Object> entry : reqParams.entrySet()) {
                    builder.addTextBody(entry.getKey(), entry.getValue().toString());
                }
                httpPost.setEntity(builder.build());
            } else {
                List<NameValuePair> pairList = new ArrayList<>(reqParams.size());
                for (Map.Entry<String, Object> entry : reqParams.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                    pairList.add(pair);
                }
                httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            }

            // 设定头信息
            handleHeader(headerParams, httpPost);

            Future<HttpResponse> future = httpClient.execute(httpPost, futureCallback);
            if (futureCallback == null) {
                HttpResponse response = future.get(MAX_TIMEOUT, TimeUnit.MILLISECONDS);
                if (response.getFirstHeader("content-type") != null) {
                    RESP_CONTENT_TYPE.set(response.getFirstHeader("content-type").getValue());
                }

                try {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode != HttpStatus.SC_OK) {
                        EXCEPTION.set(new Exception("reqest error, status code:" + statusCode));
                        return null;
                    }
                    HttpEntity entity = response.getEntity();
                    httpStr = EntityUtils.toString(entity, "UTF-8");
                } finally {
                    EntityUtils.consume(response.getEntity());
                }
            }
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            logger.error("ERROR:", e);
            EXCEPTION.set(e);
        }
        return httpStr;
    }

    public static String doPost(String apiUrl, String jsonStr) {
        return doPost(apiUrl, jsonStr, null, null, null);
    }

    public static String doPost(String apiUrl, String jsonStr, Map<String, Object> params) {
        return doPost(apiUrl, jsonStr, params, null, null);
    }

    public static String doPost(String apiUrl, String jsonStr, FutureCallback<HttpResponse> futureCallback) {
        return doPost(apiUrl, jsonStr, null, futureCallback, null);
    }

    public static String doPost(String apiUrl, String jsonStr, Map<String, Object> params,
                                FutureCallback<HttpResponse> futureCallback) {
        return doPost(apiUrl, jsonStr, params, futureCallback, null);
    }

    public static String doPost(String apiUrl, String jsonStr,
                                FutureCallback<HttpResponse> futureCallback,
                                Map<String, String> headerParams) {
        return doPost(apiUrl, jsonStr, null, futureCallback, headerParams);
    }

    /**
     * 发送 POST 请求（HTTP），JSON形式
     *
     * @param apiUrl
     * @param jsonStr json对象
     * @return
     */
    public static String doPost(String apiUrl,
                                String jsonStr,
                                Map<String, Object> params,
                                FutureCallback<HttpResponse> futureCallback,
                                Map<String, String> headerParams) {
        EXCEPTION.remove();
        String httpStr = null;
        try {
            if (!httpClient.isRunning()) {
                httpClient.start();
            }
            HttpPost httpPost = new HttpPost(apiUrl);
            if (params != null && params.size() > 0) {
                List<NameValuePair> pairList = new ArrayList<>(params.size());
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                    pairList.add(pair);
                }
                httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            }
            StringEntity stringEntity = new StringEntity(jsonStr, "UTF-8");// 解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);

            // 设定头信息
            handleHeader(headerParams, httpPost);
            Future<HttpResponse> future = httpClient.execute(httpPost, futureCallback);
            if (futureCallback == null) {
                HttpResponse response = future.get(MAX_TIMEOUT, TimeUnit.MILLISECONDS);
                if (response.getFirstHeader("content-type") != null) {
                    RESP_CONTENT_TYPE.set(response.getFirstHeader("content-type").getValue());
                }
                try {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (!com.github.lifelab.leisure.common.exception.consts.HttpStatus.valueOf(statusCode).is2xxSuccessful()) {
                        logger.info("==> api:[{}] ==> status:[{}]", apiUrl, statusCode);
                        EXCEPTION.set(new Exception("reqest error, status code:" + statusCode));
                        return null;
                    }
                    HttpEntity entity = response.getEntity();
                    httpStr = EntityUtils.toString(entity, "UTF-8");
                } finally {
                    EntityUtils.consume(response.getEntity());
                }
            }
        } catch (Exception e) {
            logger.error("ERROR::", e);
            EXCEPTION.set(e);
        }
        return httpStr;
    }

    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSLv3");
            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                               String paramString) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                               String paramString) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sc.init(null, new TrustManager[]{trustManager}, null);
        } catch (Exception e) {
            logger.error("ERROR::", e);
        }

        return sc;
    }

    /**
     * 从请求参数中查找文件类型参数 如果类型为HttpClientFileUploadBean 的则认为是文件上传的
     *
     * @param params
     * @return
     */
    private static Map<String, List<HttpClientFileUploadBean>> findFileParams(Map<String, Object> params) {
        Map<String, List<HttpClientFileUploadBean>> retMap = new HashMap<>();
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            Object value = params.get(key);
            if (value instanceof HttpClientFileUploadBean) {
                List<HttpClientFileUploadBean> list = new ArrayList<>();
                list.add((HttpClientFileUploadBean) value);
                retMap.put(key, list);
                params.remove(key);
            } else if (value instanceof List) {
                for (Object obj : (List<Object>) value) {
                    if (obj instanceof HttpClientFileUploadBean) {
                        retMap.put(key, (List<HttpClientFileUploadBean>) value);
                        params.remove(key);
                        break;
                    }
                }
            }
        }
        return retMap;
    }

    private static void handleHeader(Map<String, String> headerParams, HttpRequestBase request) {
        if (headerParams != null && headerParams.size() > 0) {
            Iterator<String> iter = headerParams.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                request.addHeader(key, headerParams.get(key));
            }
        }
    }

    public static Exception hasException() {
        return EXCEPTION.get();
    }

    public static String getRespContentType() {
        return RESP_CONTENT_TYPE.get();
    }

}