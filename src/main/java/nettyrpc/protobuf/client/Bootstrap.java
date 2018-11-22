package nettyrpc.protobuf.client;

import nettyrpc.protobuf.reqtype.ReqHandleMapping;
import nettyrpc.protobuf.resptype.RespHandleMapping;
import nettyrpc.protobuf.client.NettyConstant;
import nettyrpc.protobuf.client.SubReqProClient;

public class Bootstrap {

	public void startAll() throws InterruptedException {
		DataFuther.handleMapping = new ReqHandleMapping();
		SubReqProHandler.respHandleMapping = new RespHandleMapping();
		SubReqProClient.subReqProClient.initConnect(NettyConstant.port, NettyConstant.host);
	}

	public void startAll(String host, int port) throws InterruptedException {
		DataFuther.handleMapping = new ReqHandleMapping();
		SubReqProHandler.respHandleMapping = new RespHandleMapping();
		SubReqProClient.subReqProClient.initConnect(port, host);
	}

	public void startAll(String host, int port, ReqHandleMapping reqhandleMapping,RespHandleMapping respHandleMapping) throws InterruptedException {
		DataFuther.handleMapping = reqhandleMapping;
		SubReqProHandler.respHandleMapping = respHandleMapping;
		SubReqProClient.subReqProClient.initConnect(port, host);
	}
	public static void main(String[] args) throws InterruptedException {
		new Bootstrap().startAll();
	}
}
