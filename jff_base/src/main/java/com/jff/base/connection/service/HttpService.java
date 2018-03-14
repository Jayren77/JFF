package com.jff.base.connection.service;

import com.jff.base.connection.entity.JffHttpResponse;
import com.jff.base.util.GsonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpService http请求服务
 */
@Service
public class HttpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpService.class);

    private  PoolingHttpClientConnectionManager connectionManager;

    private  RequestConfig requestConfig;

    private  final int MAX_TIMEOUT = 7000;

    private CloseableHttpClient httpClient;

    public HttpService() {
        LOGGER.info("httpClient 初始化开始");
        // 设置连接池
        connectionManager = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(connectionManager.getMaxTotal());
        // 设置重新验证通路的时间
        connectionManager.setValidateAfterInactivity(MAX_TIMEOUT);
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        requestConfig = configBuilder.build();
        //初始化
        httpClient= wrapClient();
        if (null != httpClient){
            LOGGER.info("httpClient 初始化完成");
        }

    }

    /**
     * GET无参请求
     * @param url
     * @return
     */
    public JffHttpResponse doGet(String url){
        return doGet(url,new HashMap<String, Object>());
    }

    /**
     * 通过GET方法发送带参数请求
     * @param apiurl
     * @param params
     * @return
     */
    public JffHttpResponse doGet(String apiurl, Map<String,Object> params){
        JffHttpResponse response = null;
        //构造get请求参数
        StringBuffer paramBuffer = new StringBuffer(apiurl);
        if(!params.isEmpty()){
            paramBuffer.append("?");
            for(final String key:params.keySet()){
                // key=value&
                paramBuffer.append(key).append("=").append(params.get(key)).append("&");
            }
            if(paramBuffer.lastIndexOf("&") == paramBuffer.length()-1){
                paramBuffer.deleteCharAt(paramBuffer.length()-1);
            }
        }
        LOGGER.debug("GET发送请求："+paramBuffer.toString());
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(paramBuffer.toString());
            //执行
            HttpResponse httpResponse = httpClient.execute(httpGet);
            response = createJffHttpResponse(httpResponse);
        } catch (IOException e) {
           LOGGER.error("执行GET请求失败",e);
        }finally {
            releaseConnection(httpGet,httpClient);
        }
        return response;
    }

    /**
     * Post方法发送带参数请求
     * @param url
     * @param params
     * @return JffHttpResponse
     */
    public JffHttpResponse doPost(String url,Map<String,Object> params){
        JffHttpResponse jffHttpResponse = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        // 将传过来的参数添加到List<NameValuePair>中
        List pairList = new ArrayList<NameValuePair>(params.size());
        if(null != params && !params.isEmpty()){
            for(Map.Entry<String,Object> entry:params.entrySet()){
                pairList.add(new BasicNameValuePair(entry.getKey(), GsonUtils.toJson(entry.getValue())));
            }
        }
        HttpEntity httpEntity = null;
        HttpResponse response = null;

        try {
            httpEntity = new UrlEncodedFormEntity(pairList);
            httpPost.setEntity(httpEntity);
            //发送封装好的POST请求
            response = httpClient.execute(httpPost);
            jffHttpResponse = createJffHttpResponse(response);
        } catch (IOException e) {
           LOGGER.error("POST请求失败",e);
        }finally {
            releaseConnection(httpPost,httpClient);
        }

        return jffHttpResponse;
    }

    /**
     * 生成JffHttpResponse
     * @return
     */
    private JffHttpResponse createJffHttpResponse(HttpResponse httpResponse) throws IOException{
        JffHttpResponse jffHttpResponse = new JffHttpResponse();
        //服务连接超时时，httpResponse == null,无返回结果
        if(null == httpResponse){
            jffHttpResponse.setMsg("连接失败，目标服务无返回结果");
            jffHttpResponse.setStatusCode(0);
            return jffHttpResponse;
        }

        HttpEntity httpEntity = httpResponse.getEntity();
        if(null != httpEntity){
            String msg = IOUtils.toString(httpEntity.getContent(),"UTF-8");
            jffHttpResponse.setMsg(msg);
        }
        jffHttpResponse.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        return jffHttpResponse;
    }

    /**
     * 释放资源
     * @param requestBase
     * @param httpClient
     */
    private void releaseConnection(HttpRequestBase requestBase, CloseableHttpClient httpClient){
        try {
            if (requestBase != null) {
                requestBase.releaseConnection();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            LOGGER.error("当前{0}请求释放资源失败",e,new String[]{requestBase.getURI().toString()});
        }
    }

    /**
     * @Description 创建一个不进行正式验证的请求客户端对象 不用导入SSL证书
     * @return HttpClient
     */
    private  CloseableHttpClient wrapClient() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslContext.init(null,new TrustManager[]{trustManager},null);
            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(
                    sslContext, NoopHostnameVerifier.INSTANCE);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(ssf).build();
            return httpclient;
        } catch (Exception e) {
            return HttpClients.createDefault();
        }
    }

}
