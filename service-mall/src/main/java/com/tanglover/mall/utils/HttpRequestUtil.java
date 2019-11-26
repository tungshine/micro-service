package com.tanglover.mall.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author TangXu
 * @description HttpRequest工具类
 * @date 2019/11/22 14:12
 */
public class HttpRequestUtil {

    static PoolingHttpClientConnectionManager cm = null;

    static {
        init();
    }

    public static void init() {
        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
        cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);
    }

    public static CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        /*
         * CloseableHttpClient httpClient =
         * HttpClients.createDefault();//如果不采用连接池就是这种方式获取连接
         */
        return httpClient;
    }

    /**
     * http Get方式
     *
     * @param url     路径
     * @param headers 头部信息
     * @param charset 编码
     * @return
     */
    public static String doGet(String url, Map<String, String> headers, String charset) {
        HttpClient httpClient = null;
        HttpGet get = null;
        String result = null;
        try {
            httpClient = getHttpClient();
            get = new HttpGet(url);
            if (headers != null) {
                for (Entry<String, String> h : headers.entrySet()) {
                    get.addHeader(h.getKey(), h.getValue());
                }
            }
            HttpResponse response = httpClient.execute(get);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * http GET方式
     *
     * @param url     路径
     * @param charset 编码
     * @return
     */
    public static String doGet(String url, String charset) {
        return doGet(url, null, charset);
    }

    /**
     * http GET方式
     *
     * @param url 路径
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, "UTF-8");
    }

    /**
     * http Post方式
     *
     * @param url         路径
     * @param headers     头部信息
     * @param bodyContent 内容
     * @param charset     编码
     * @return
     */
    public static String doPost(String url, Map<String, String> headers, String bodyContent, String charset) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = getHttpClient();
            httpPost = new HttpPost(url);
            if (headers != null) {
                for (Entry<String, String> h : headers.entrySet()) {
                    httpPost.addHeader(h.getKey(), h.getValue());
                }
            }
            if (StringUtils.isNotBlank(bodyContent)) {
                httpPost.setEntity(new StringEntity(bodyContent, charset));
            }
            HttpEntity entity = httpPost.getEntity();
            byte[] bytes = IOUtils.toByteArray(entity.getContent());
            System.out.println(new String(bytes, "utf-8"));
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * http Post方式
     *
     * @param url         路径
     * @param headers     头部信息
     * @param bodyContent 内容
     * @param charset     编码
     * @return
     */
    public static String doPost(String url, Map<String, String> headers, byte[] bodyContent, String charset) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = getHttpClient();
            httpPost = new HttpPost(url);
            if (headers != null) {
                for (Entry<String, String> h : headers.entrySet()) {
                    httpPost.addHeader(h.getKey(), h.getValue());
                }
            }
            if (bodyContent != null && bodyContent.length > 0) {
                httpPost.setEntity(new ByteArrayEntity(bodyContent));
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * http Post方式
     *
     * @param url         路径
     * @param bodyContent 内容
     * @param charset     编码
     * @return
     */
    public static String doPost(String url, String bodyContent, String charset) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        return doPost(url, headers, bodyContent, charset);
    }

    /**
     * http Post方式
     *
     * @param url         路径
     * @param bodyContent 内容
     * @param charset     编码
     * @return
     */
    public static String doPost(String url, byte[] bodyContent, String charset) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        return doPost(url, headers, bodyContent, charset);
    }

    /**
     * http Post方式
     *
     * @param url         路径
     * @param bodyContent 内容
     * @return
     */
    public static String doPost(String url, String bodyContent) {
        return doPost(url, bodyContent, "UTF-8");
    }

    /**
     * http Post方式
     *
     * @param url         路径
     * @param bodyContent 内容
     * @return
     */
    public static String doPost(String url, byte[] bodyContent) {
        return doPost(url, bodyContent, "UTF-8");
    }

    /**
     * 模拟客服端数据生成
     *
     * @param appId
     * @param version
     * @param bodyContent
     * @return byte[]
     */
    public static byte[] generateClientPackage(String appId, String version, String bodyContent) {
        // 结果
        byte[] res = null;
        try {
            // 协议头部
            JSONObject json = new JSONObject();
            json.put("app_id", appId);
            json.put("version", version);
            byte[] head = json.toString().getBytes("UTF-8");

            // 内容加密
            byte[] key = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 0, 0, 0, 0, 0, 0};
            byte[] data = XXTeaUtil.encrypt(bodyContent.getBytes("UTF-8"), key);

            res = new byte[head.length + 1 + data.length];

            System.arraycopy(head, 0, res, 0, head.length);
            res[head.length] = (byte) 0;
            System.arraycopy(data, 0, res, head.length + 1, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
