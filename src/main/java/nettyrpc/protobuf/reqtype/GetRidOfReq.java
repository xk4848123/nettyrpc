package nettyrpc.protobuf.reqtype;

public class GetRidOfReq implements Req {

	private int userId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}