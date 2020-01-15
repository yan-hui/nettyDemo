package hch.socket.unbrella.netty.demo5;

import io.netty.channel.Channel;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 建立用户ip端口与通道的关联
 */
public class ChannelMap {
    // 用户保存用户ip端口与通道的Map对象
    private static Map<String, Channel> channelMap;

    static {
        channelMap = new HashMap<String, Channel>();
    }

    /**
     * 添加用户ip与channel的关联
     * @param ipAndPort
     * @param channel
     */
    public static void put(String ipAndPort, Channel channel) {
        channelMap.put(ipAndPort, channel);
    }

    /**
     * 根据用户ip移除用户ip与channel的关联
     * @param ipAndPort
     */
    public static void remove(String ipAndPort) {
        channelMap.remove(ipAndPort);
    }

    /**
     * 根据通道id移除用户与channel的关联
     * @param channelId 通道的id
     */
    public static void removeByChannelId(String channelId) {
        if(!StringUtils.isNotBlank(channelId)) {
            return;
        }

        for (String s : channelMap.keySet()) {
            Channel channel = channelMap.get(s);
            if(channelId.equals(channel.id().asLongText())) {
                System.out.println("客户端连接断开,取消用户" + s + "与通道" + channelId + "的关联");
                channelMap.remove(s);
                break;
            }
        }
    }


    // 打印所有的用户与通道的关联数据
    public static void print() {
        System.out.println("通道数量---->"+channelMap.size());
        for (String s : channelMap.keySet()) {
            System.out.println("ip:端口:" + s + " 通道:" + channelMap.get(s).id());
        }
    }

    /**
     * 根据用户ip 端口获取对应的通道
     * @param ipAndPort ip+端口
     * @return Netty通道
     */
    public static Channel get(String ipAndPort) {
        return channelMap.get(ipAndPort);
    }

}
