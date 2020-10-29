### Week_02 课后作业
#### Q2:写一段代码，使用 HttpClient 或 OkHttp 访问 http://localhost:8801，代码提交到 Github。
```java
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author wan.chengfei
 */
public class OzHttpClient {

    private static final int SOCKET_TIMEOUT = 5 * 1000;
    private static final int CONNECT_TIMEOUT = 2 * 1000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 1 * 1000;

    /**
     * Get 请求，也可拓展成其他的类型请求
     *
     * @param url      请求url
     * @param header   请求头
     * @param params   请求参数t
     * @param isSecure 是否需要安全验证
     * @return org.apache.http.HttpResponse
     * @author wan.chengfei
     */
    public static HttpResponse get(String url, Map<String, String> header, Map<String, String> params, boolean isSecure) throws Exception {
        HttpClient httpClient = getHttpClient(isSecure);
        String separator = "?";
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        StringBuilder urlBuilder = new StringBuilder(url);
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            urlBuilder
                    .append(separator)
                    .append(entry.getKey())
                    .append("=")
                    .append(entry.getValue());
            separator = "&";
        }
        url = urlBuilder.toString();
        HttpGet httpGet = new HttpGet(url);
        // 设置头信息
        for (Map.Entry<String, String> entry : header.entrySet()) {
            httpGet.addHeader(entry.getKey(), entry.getValue());
        }
        httpGet.setConfig(getRequestConfig());
        return httpClient.execute(httpGet);
    }


    /**
     * 请求超时时间设置
     *
     * @return org.apache.http.client.config.RequestConfig
     * @author wan.chengfei
     */
    protected static RequestConfig getRequestConfig() {
        return RequestConfig
                .custom()
                .setSocketTimeout(OzHttpClient.SOCKET_TIMEOUT)
                .setConnectTimeout(OzHttpClient.CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(OzHttpClient.CONNECTION_REQUEST_TIMEOUT)
                .build();
    }


    /**
     * 处理HTTP请求头
     *
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author wan.chengfei
     */
    private static Map<String, String> headerOps() {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        return header;
    }


    /**
     * 获取 HttpClient 对象
     *
     * @param isSecure 效验服务器主机名的合法性
     * @return org.apache.http.client.HttpClient
     * @author wan.chengfei
     */
    protected static HttpClient getHttpClient(boolean isSecure) throws Exception {
        if (isSecure) {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
                        throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, new SecureRandom());
            HostnameVerifier verifier = (hostname, session) -> true;
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(context, verifier);
            return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
        } else {
            return HttpClients.custom().build();
        }
    }

    public static void main(String[] args) throws Exception {
        String uri = "http://localhost:8801/";
        get(uri, headerOps(), Collections.emptyMap(), false);
    }
}
```