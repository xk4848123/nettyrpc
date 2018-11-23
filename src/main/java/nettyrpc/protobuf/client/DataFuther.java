package nettyrpc.protobuf.client;

import java.util.Map.Entry;
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

	static volatile ConcurrentHashMap<Integer, RespPack> data = new ConcurrentHashMap<>();

	static NullResp nullResp = new NullResp();

	static volatile Boolean nettyStart = false;


	public static void sendData(Req req) {
		waitForNetty();
		if (nettyStart) {
			// check and release
			if (data.size() > 1) {
				System.out.println("dataFuther is two big ,wait for netty release dataFuther");
				release();
			}
			SubscribeReqProto.SubscribeReq.Builder subscribeReq = SubscribeReqProto.SubscribeReq.newBuilder();
			subscribeReq.setHeader(handleMapping.transferToString(req.getClass()));
			int subId = SubReqProHandler.subReq.getAndIncrement();
			subscribeReq.setSubReqID(subId);
			subscribeReq.setMsg(ByteString.copyFrom(GsonUtil.GsonString(req).getBytes()));
			SubscribeReq sendData = subscribeReq.build();
			if (req.getClass().isAnnotationPresent(withResp.class)) {
				data.put(subId, new RespPack(nullResp, System.currentTimeMillis()));
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
				while (!timeOut && data.get(subId).getResp() instanceof NullResp) {
					Thread.sleep(500);
					long nowTime = System.currentTimeMillis();
					if ((nowTime - startTime) / 1000 == 5 || (nowTime - startTime) / 1000 > 5) {
						timeOut = true;
						System.out.println("Time out! Can not get the data");
					}
				}
				 Resp resp = data.get(subId).getResp();
				 data.remove(subId);
				 return resp;
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
				while (!timeOut && data.get(subId).getResp() instanceof NullResp) {
					Thread.sleep(500);
					long nowTime = System.currentTimeMillis();
					if ((nowTime - startTime) / 1000 == seconds || (nowTime - startTime) / 1000 > seconds) {
						timeOut = true;
						System.out.println("Time out! Can not get the data");
					}
				}
				 Resp resp = data.get(subId).getResp();
				 data.remove(subId);
				 return resp;
			} else {
				return nullResp;
			}
		} finally {
			RESOURCE.remove();
		}
	}

	private static void waitForNetty() {
		waitForNetty(10);
	}

	private static void waitForNetty(long seconds) {
		boolean timeOut = false;
		long startTime = System.currentTimeMillis();
		while (!nettyStart && !timeOut) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
			}
			long nowTime = System.currentTimeMillis();
			if ((nowTime - startTime) / 1000 == seconds || (nowTime - startTime) / 1000 > seconds) {
				timeOut = true;
			} else {
				System.out.println("wait for netty start!");
			}
		}
		if (timeOut == true) {
			System.out.println("netty start timeout!");
		}
	}
	
	static class RespPack{
		
		Resp resp;
		
		Long storeTime;

		public Resp getResp() {
			return resp;
		}

		public Long getStoreTime() {
			return storeTime;
		}
		public RespPack(Resp resp, Long storeTime) {
			super();
			this.resp = resp;
			this.storeTime = storeTime;
		}
	}
	
	private static void release(){
		long nowTime = System.currentTimeMillis();
		for (Entry<Integer, RespPack> entry : data.entrySet()) {
			Integer subId = entry.getKey();
			RespPack respPack = entry.getValue();
			long storeTime = respPack.getStoreTime();
			if((nowTime - storeTime) / 1000 == 60 || (nowTime - storeTime) / 1000 > 60){
				data.remove(subId);
			}
	}
	}
}