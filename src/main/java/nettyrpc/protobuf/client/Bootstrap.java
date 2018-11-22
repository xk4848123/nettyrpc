package nettyrpc.protobuf.client;

import nettyrpc.protobuf.reqtype.ReqHandleMapping;
import nettyrpc.protobuf.resptype.RespHandleMapping;
import nettyrpc.protobuf.client.NettyConstant;
import nettyrpc.protobuf.client.SubReqProClient;

public class Bootstrap {

	public void startAll() throws InterruptedException {
		DataFuther.handleMapping = new ReqHandleMapping();
		SubReqProHandler.respHandleMapping = new RespHandleMapping();
		SubReqProClient.subReqProClient = new SubReqProClient();
		SubReqProClient.subReqProClient.initConnect(NettyConstant.port, NettyConstant.host);
	}

	public void startAll(String host, int port) throws InterruptedException {
		DataFuther.handleMapping = new ReqHandleMapping();
		SubReqProHandler.respHandleMapping = new RespHandleMapping();
		SubReqProClient.subReqProClient = new SubReqProClient();
		SubReqProClient.subReqProClient.initConnect(port, host);
	}

	public void startAll(String host, int port, ReqHandleMapping reqhandleMapping,RespHandleMapping respHandleMapping) throws InterruptedException {
		DataFuther.handleMapping = reqhandleMapping;
		SubReqProHandler.respHandleMapping = respHandleMapping;
		SubReqProClient.subReqProClient = new SubReqProClient();
		SubReqProClient.subReqProClient.initConnect(port, host);
	}
	public void stop(){
		SubReqProClient.subReqProClient.stopConnect();
	}
}
