package nettyrpc.protobuf.client;
 

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;
import nettyrpc.protobuf.SubscribeRespProto;
 
public class SubReqProClient {
	
    static SubReqProClient subReqProClient = new SubReqProClient();
    //配置客户端NIO线程池
    private EventLoopGroup workGroup=new NioEventLoopGroup(3);
    
    Channel fixedChannel = null;
    
    private Bootstrap b = null;

    public void initConnect(int port,String host){
    	try {
    		  b=new Bootstrap();
    	        b.group(workGroup);
    	        b.channel(NioSocketChannel.class);
    	        b.option(ChannelOption.SO_KEEPALIVE, true);
    	        b.handler(new ChannelInitializer<SocketChannel>() {
    	            @Override
    	            protected void initChannel(SocketChannel socketChannel) throws Exception {
    	                socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
    	                socketChannel.pipeline().addLast(new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance()));
    	                socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
    	                socketChannel.pipeline().addLast(new ProtobufEncoder());
    	                socketChannel.pipeline().addLast("readTimeoutHandler",new ReadTimeoutHandler(150));
    	                socketChannel.pipeline().addLast(new SubReqProHandler());
    	            }
    	        });
    	        doConnect(port, host);
		} catch (Exception e) {
			System.out.println("netty error: " + e.getMessage());
		}
    }
     private void doConnect(int port,String host) throws InterruptedException{
        try{
       
          //发起异步连接操作
        	ChannelFuture f=b.connect(host,port).sync();
        	System.out.println("connet "+ host + ":" + port + " success!");
        	//等待客户端链路关闭
            f.channel().closeFuture().sync();
        }finally {
            //释放NIO 线程组
			System.out.println("channel close,wait for connet....");
			fixedChannel.eventLoop().execute(new Runnable() {
				@Override
				public void run() {
					try {
						TimeUnit.SECONDS.sleep(5);
						doConnect(port,host);
					} catch (InterruptedException e) {
						System.out.println("netty error: " + e.getMessage());
					}

				}
			});
        }
    }
}
