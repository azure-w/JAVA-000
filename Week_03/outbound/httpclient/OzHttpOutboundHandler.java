package com.oz.week03.outbound.httpclient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OzHttpOutboundHandler {

    private String backendUrl;

    public OzHttpOutboundHandler(String backendUrl) {
        this.backendUrl = backendUrl.endsWith("/") ? backendUrl.substring(0, backendUrl.length() - 1) : backendUrl;
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) throws Exception {
        final String url = this.backendUrl + fullRequest.uri();
        OzHttpClient.get(fullRequest, ctx, url, headerOps(), Collections.emptyMap(), false);
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
}