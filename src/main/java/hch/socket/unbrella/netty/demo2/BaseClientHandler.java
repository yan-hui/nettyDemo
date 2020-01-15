package hch.socket.unbrella.netty.demo2;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class BaseClientHandler extends ChannelInboundHandlerAdapter {
    private byte[] req;

    public BaseClientHandler() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("s","060101190101013699");
        jsonObject.put("u","4");
        System.out.println(jsonObject.toJSONString());
        req = jsonObject.toJSONString().getBytes();
      /*  req = ("In this chapter you general, we recommend Java Concurrency in Practice by Brian Goetz. His book w"
                + "ill give We’ve reached an exciting point—in the next chapter we’ll discuss bootstrapping, the process "
                + "of configuring and connecting all of Netty’s components to bring your learned about threading models in ge"
                + "neral and Netty’s threading model in particular, whose performance and consistency advantages we discuss"
                + "ed in detail In this chapter you general, we recommend Java Concurrency in Practice by Brian Goetz. Hi"
                + "s book will give We’ve reached an exciting point—in the next chapter we’ll discuss bootstrapping, the"
                + " process of configuring and connecting all of Netty’s components to bring your learned about threading "
                + "models in general and Netty’s threading model in particular, whose performance and consistency advantag"
                + "es we discussed in detailIn this chapter you general, we recommend Java Concurrency in Practice by Bri"
                + "an Goetz. His book will give We’ve reached an exciting point—in the next chapter;the counter is: 1 2222"
                + "sdsa ddasd asdsadas dsadasdas"+System.getProperty("line.separator")).getBytes();*/
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ByteBuf message = null;
//        for (int i=0;i<100;i++){
//            message = Unpooled.buffer(req.length);
//            message.writeBytes(req);
//            ctx.writeAndFlush(message);
//        }
        String message = "060101190101013704::HTTP/1.1 200 OK\n" +
                "Date:Wed, 15 Jan 2020 07:04:39 GMT\n" +
                "Content-Length:59\n";
        ctx.writeAndFlush(message);
    /*    message = Unpooled.buffer(req.length);
        message.writeBytes(req);
        ctx.writeAndFlush(message);
        message = Unpooled.buffer(req.length);
        message.writeBytes(req);
        ctx.writeAndFlush(message);*/
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String buf = String.valueOf(msg);
        System.out.println("NOW is:" + buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
