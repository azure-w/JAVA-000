package com.oz.week03.outbound.httpclient;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

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
     * @author wan.chengfei@rongzer.com
     * @create 2020-10-29 15:48:07
     */
    public static HttpResponse get(final FullHttpRequest inbound, final ChannelHandlerContext ctx,
                                   String url, Map<String, String> header, Map<String, String> params, boolean isSecure) throws Exception {
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
        HttpResponse httpResponse = httpClient.execute(httpGet);
        handleResponse(inbound, ctx, httpResponse);
        return httpResponse;
    }

    /**
     * 请求超时时间设置
     *
     * @return org.apache.http.client.config.RequestConfig
     * @author wan.chengfei@rongzer.com
     * @create 2020-10-29 15:47:53
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
     * @author wan.chengfei@rongzer.com
     * @create 2020-10-29 16:03:12
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
     * @author wan.chengfei@rongzer.com
     * @create 2020-10-29 15:51:06
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

    private static void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final HttpResponse endpointResponse) throws Exception {
        FullHttpResponse response = null;
        try {
            byte[] body = EntityUtils.toByteArray(endpointResponse.getEntity());
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", Integer.parseInt(endpointResponse.getFirstHeader("Content-Length").getValue()));
            response.headers().set("Authorization", fullRequest.headers().get("Authorization"));
        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    //response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
            ctx.flush();
            //ctx.close();
        }

    }
}