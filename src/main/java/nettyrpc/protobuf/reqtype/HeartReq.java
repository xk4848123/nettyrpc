package nettyrpc.protobuf.reqtype;


public class HeartReq implements Req {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
