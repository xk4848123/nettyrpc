package nettyrpc.protobuf.client;

import nettyrpc.protobuf.reqtype.ReqHandleMapping;
import nettyrpc.protobuf.resptype.RespHandleMapping;
import nettyrpc.protobuf.client.NettyConstant;
import nettyrpc.protobuf.client.SubReqProClient;

public class Bootstrap {

	private String host;
	
	private int port;
	
    private ReqHandleMapping reqHandleMapping;
	
	private RespHandleMapping respHandleMapping;

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setReqHandleMapping(ReqHandleMapping reqHandleMapping) {
		this.reqHandleMapping = reqHandleMapping;
	}

	public void setRespHandleMapping(RespHandleMapping respHandleMapping) {
		this.respHandleMapping = respHandleMapping;
	}

	public void startDefault() throws InterruptedException {
		DataFuther.handleMapping = new ReqHandleMapping();
		SubReqProHandler.respHandleMapping = new RespHandleMapping();
		SubReqProClient.subReqProClient = new SubReqProClient();
		SubReqProClient.subReqProClient.initConnect(NettyConstant.port, NettyConstant.host);
	}

	public void start() throws InterruptedException {
		DataFuther.handleMapping = new ReqHandleMapping();
		SubReqProHandler.respHandleMapping = new RespHandleMapping();
		SubReqProClient.subReqProClient = new SubReqProClient();
		SubReqProClient.subReqProClient.initConnect(port, host);
	}

	public void startWithHandle() throws InterruptedException {
		DataFuther.handleMapping = reqHandleMapping;
		SubReqProHandler.respHandleMapping = respHandleMapping;
		SubReqProClient.subReqProClient = new SubReqProClient();
		SubReqProClient.subReqProClient.initConnect(port, host);
	}
	public void stop(){
		SubReqProClient.subReqProClient.stopConnect();
	}
}
