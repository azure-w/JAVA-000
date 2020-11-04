package com.oz.week03.inbound;

import com.oz.week03.filter.HttpRequestFilter;
import com.oz.week03.filter.OzHttpRequestFilter;
import com.oz.week03.outbound.httpclient.OzHttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private final String proxyServer;

    private OzHttpOutboundHandler handler;

    private HttpRequestFilter filter;

    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
        handler = new OzHttpOutboundHandler(this.proxyServer);
        filter = new OzHttpRequestFilter();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            filter.filter(fullRequest, ctx);
            handler.handle(fullRequest, ctx);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
