package hch.socket.unbrella.netty.demo5;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;


@ChannelHandler.Sharable
public class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if(state == IdleState.READER_IDLE) {
                System.out.println("读空闲事件触发...");
            }
            else if(state == IdleState.WRITER_IDLE) {
                System.out.println("写空闲事件触发...");
            }
            else if(state == IdleState.ALL_IDLE) {
                System.out.println("---------------");
                System.out.println("读写空闲事件触发");
                System.out.println("关闭通道资源");
                ChannelMap.removeByChannelId(ctx.channel().id().asLongText());
                ctx.channel().close();
            }

        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
