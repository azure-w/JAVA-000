package com.oz.week03.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class OzHttpRequestFilter implements HttpRequestFilter {

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        fullRequest.headers().set("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGllcyI6IiIsInN1YiI6IjkyODM0MVNBIiwiZXhwIjoxNjA0NTk2MDM3fQ.sb-TtenBdYLJJTBW1x9-8tzGRpIXX-eWEgsXBtAdHOJlyIxs8V_J9IrMynBxYQLjg96b-6yUN8hoE9NEOTEINA");
    }
}