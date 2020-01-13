package hch.socket.unbrella.netty.demo4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.time.LocalDateTime;

public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {
    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat", CharsetUtil.UTF_8));
    private static final int TRY_TIMES = 3;
    private int currentTime = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("激活时间是：" + LocalDateTime.now());
        System.out.println("客户端活着................");
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("停止时间是：" + LocalDateTime.now());
        System.out.println("HeartBeatClientHandler channelInactive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message =String.valueOf(msg);
        System.out.println("message--->"+message);
        if ("Heartbeat".equals(message)){
            ctx.writeAndFlush("有数据从服务端过来......");
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("循环触发时间："+ LocalDateTime.now());
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event =(IdleStateEvent)evt;
            if (event.state()== IdleState.WRITER_IDLE){
                if (currentTime<=TRY_TIMES){
                    System.out.println("currentTime: "+currentTime);
                    currentTime++;
                    ctx.channel().writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
                }
            }
        }

       /* String message = String.valueOf(msg);
        System.out.println("message-->" + message);
        if ("Heartbeat".equals(message)) {
            ctx.writeAndFlush("从服务端读取信息");
        }
        ReferenceCountUtil.release(msg);*/
    }

}
