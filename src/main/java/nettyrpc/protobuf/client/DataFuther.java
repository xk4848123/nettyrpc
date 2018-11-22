package nettyrpc.protobuf.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.google.protobuf.ByteString;

import nettyrpc.protobuf.reqtype.ReqHandleMapping;
import nettyrpc.protobuf.reqtype.Req;
import nettyrpc.protobuf.resptype.NullResp;
import nettyrpc.protobuf.resptype.Resp;
import nettyrpc.util.GsonUtil;
import nettyrpc.protobuf.SubscribeReqProto;
import nettyrpc.protobuf.SubscribeReqProto.SubscribeReq;
import nettyrpc.protobuf.annotation.withResp;

public class DataFuther {

	static ReqHandleMapping handleMapping = null;

	private final static ThreadLocal<Integer> RESOURCE = new ThreadLocal<Integer>();

	static volatile ConcurrentHashMap<Integer, Resp> data = new ConcurrentHashMap<>();

	static NullResp nullResp = new NullResp();

	static volatile Boolean nettyStart = false;
	
	public static void sendData(Req req) {
		waitForNetty();
		if (nettyStart) {
			//check and release
			if (data.size() > 100) {
				System.out.println("wait for data release!");
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
				}
				data = new ConcurrentHashMap<>();
			}
			SubscribeReqProto.SubscribeReq.Builder subscribeReq = SubscribeReqProto.SubscribeReq.newBuilder();
			subscribeReq.setHeader(handleMapping.transferToString(req.getClass()));
			int subId = SubReqProHandler.subReq.getAndIncrement();
			subscribeReq.setSubReqID(subId);
			subscribeReq.setMsg(ByteString.copyFrom(GsonUtil.GsonString(req).getBytes()));
			SubscribeReq sendData = subscribeReq.build();
			if(req.getClass().isAnnotationPresent(withResp.class)){
				data.put(subId, nullResp);
				RESOURCE.set(subId);
			}
			SubReqProClient.subReqProClient.fixedChannel.writeAndFlush(sendData);
		} else {
			System.out.println("netty error: please start the netty!");
		}
	}

	public static Resp getData() throws InterruptedException {
		try {
			waitForNetty();
			if (nettyStart) {
				Integer subId = RESOURCE.get();
				if (subId == null) {
					return nullResp;
				}
				boolean timeOut = false;
				long startTime = System.currentTimeMillis();
				while (!timeOut && data.get(subId) instanceof NullResp) {
					Thread.sleep(500);
					long nowTime = System.currentTimeMillis();
					if ((nowTime - startTime) / 1000 == 5 || (nowTime - startTime) / 1000 > 5) {
						timeOut = true;
						System.out.println("Time out! Can not get the data");
					}
				}
				return data.get(subId);
			} else {
				return nullResp;
			}
		} finally {
			RESOURCE.remove();
		}
	}

	public static Resp getData(long seconds) throws InterruptedException {
		try {
			waitForNetty();
			if (nettyStart) {
				Integer subId = RESOURCE.get();
				if (subId == null) {
					return nullResp;
				}
				boolean timeOut = false;
				long startTime = System.currentTimeMillis();
				while (!timeOut && data.get(subId) instanceof NullResp) {
					Thread.sleep(500);
					long nowTime = System.currentTimeMillis();
					if ((nowTime - startTime) / 1000 == seconds || (nowTime - startTime) / 1000 > seconds) {
						timeOut = true;
						System.out.println("Time out! Can not get the data");
					}
				}
				return data.get(subId);
			} else {
				return nullResp;
			}
		} finally {
			RESOURCE.remove();
		}
	}
   private static void waitForNetty(){
	   while (!nettyStart) {
		    try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
			}
			System.out.println("wait for netty start!");
		}
   }
}