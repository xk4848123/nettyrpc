package nettyrpc.protobuf.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import nettyrpc.protobuf.SubscribeReqProto;
import nettyrpc.protobuf.SubscribeRespProto;
import nettyrpc.protobuf.SubscribeReqProto.SubscribeReq;
import nettyrpc.protobuf.SubscribeRespProto.SubscribeResp;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.protobuf.ByteString;

import nettyrpc.protobuf.reqtype.HeartReq;
import nettyrpc.protobuf.resptype.Resp;
import nettyrpc.protobuf.resptype.RespHandleMapping;
import nettyrpc.util.GsonUtil;

public class SubReqProHandler extends SimpleChannelInboundHandler<SubscribeRespProto.SubscribeResp> {

	static AtomicInteger subReq = new AtomicInteger(1);

	static RespHandleMapping respHandleMapping = null;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SubscribeResp msg) throws Exception {
		Class<?> type = respHandleMapping.transferToClass(msg.getHeader());
		if (type != null) {
			Resp resp = (Resp) GsonUtil.GsonToBean(new String(msg.getMsg().toByteArray()),type);
			DataFuther.data.put(msg.getSubReqID(), resp);
		}
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		SubReqProClient.subReqProClient.fixedChannel = channel;
		DataFuther.nettyStart = true;
		System.out.println("channel " + channel.id() + " is available!");
		channel.eventLoop().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				SubscribeReq req = subReq(subReq.getAndIncrement());
				ctx.writeAndFlush(req);
			}
		}, 0, 120, TimeUnit.SECONDS);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	private SubscribeReqProto.SubscribeReq subReq(int i) {
		SubscribeReqProto.SubscribeReq.Builder req = SubscribeReqProto.SubscribeReq.newBuilder();
		req.setHeader("withresp_heart");
		req.setSubReqID(i);
		HeartReq heartReq = new HeartReq();
		heartReq.setMessage("ping");
		req.setMsg(ByteString.copyFrom(GsonUtil.GsonString(heartReq).getBytes()));
		return req.build();
	}
}
