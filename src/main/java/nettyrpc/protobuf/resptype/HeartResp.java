package nettyrpc.protobuf.resptype;

public class HeartResp implements Resp {
	
	String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
