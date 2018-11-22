package nettyrpc.protobuf.reqtype;

import nettyrpc.protobuf.annotation.withResp;

@withResp
public class PresentReq implements Req {

	private int type;
	private int userId;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
