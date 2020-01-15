package hch.socket.unbrella.netty.demo5;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.net.SocketAddress;
import java.time.LocalDateTime;

public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "连上了" + LocalDateTime.now());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务端读取通道。。。。。。");

        SocketAddress socketAddress = ctx.channel().remoteAddress();
        String ipAndPort = String.valueOf(socketAddress);
        int index = ipAndPort.indexOf("/");

        System.out.println("建立用户:" + ipAndPort + "与通道" + ctx.channel().id() + "的关联");
        System.err.println("读取信息-------->" + msg);

        try {
            if (msg instanceof Boolean) {
                //借伞协议
            }
            String message = null;
            message = String.valueOf(msg);

            UmbrellaMessage umbrellaMessage = null;

            System.err.println("index--->" + message.indexOf("::HTTP/1.1 200 OK"));
            if (message.indexOf("::HTTP/1.1 200 OK") != -1) {
                //借伞协议
                System.err.println("借伞---->" + message);
                Channel channel = ChannelMap.get("heart");
                ChannelMap.print();
                System.out.println("heart ====>" + channel);
                channel.writeAndFlush(message);
            } else {
                if (message.indexOf("Keep-Alive") == -1 || message.indexOf("User-Agent") == -1) {
                    if (message.isEmpty() || message.indexOf("u") == -1 || message.indexOf("s") == -1) {
                        //丢包重发
                        return;
                    }
                    umbrellaMessage = JSON.parseObject(message, UmbrellaMessage.class);
                } else {
                    return;
                }
                switch (umbrellaMessage.getU()) {
                    case "4":
                        System.out.println("心跳协议.........");
                        JSONObject jsonObject4 = new JSONObject();
                        jsonObject4.put("s", umbrellaMessage.getS());
                        jsonObject4.put("ok", "4");
                        if (index != -1) {
                            ipAndPort = ipAndPort.substring(index + 1);
                            ChannelMap.put("heart", ctx.channel());
                        } else {
                            ChannelMap.put("heart", ctx.channel());
                        }
                        ctx.channel().writeAndFlush(jsonObject4.toJSONString());
                        break;
                    case "11":
                        JSONObject jsonObject11 = new JSONObject();
                        jsonObject11.put("s", umbrellaMessage.getS());
                        jsonObject11.put("ok", "11");
                        ctx.channel().writeAndFlush(jsonObject11.toJSONString());
                        System.out.println("接收成功协议........");
                        break;
                    case "1":
                        System.out.println("借伞成功协议........");
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("s", umbrellaMessage.getS());
                        jsonObject1.put("ok", "1");
                        ctx.channel().writeAndFlush(jsonObject1.toJSONString());
                        break;
                    case "5":
                        JSONObject jsonObject5 = new JSONObject();
                        jsonObject5.put("s", umbrellaMessage.getS());
                        jsonObject5.put("ok", "5");
                        ctx.channel().writeAndFlush(jsonObject5.toJSONString());
                        System.out.println("借伞失败协议........");
                    case "0":
                        System.out.println("设备无伞协议.......");
                        JSONObject jsonObject0 = new JSONObject();
                        jsonObject0.put("s", umbrellaMessage.getS());
                        jsonObject0.put("ok", "5");
                        ctx.channel().writeAndFlush(jsonObject0.toJSONString());
                        break;
                    case "mac":
                        System.out.println("蓝牙地址协议.......");
                        break;
                    case "13794648081":
                        System.out.println("蓝牙借伞协议.......");
                        break;
                    default:
                        break;
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ChannelMap.print();
        System.out.println(ctx.channel().remoteAddress() + "-->异常---->" + cause.getMessage());
        ChannelMap.removeByChannelId(ctx.channel().id().asLongText());
        ChannelMap.print();
        ctx.close();
    }

}
