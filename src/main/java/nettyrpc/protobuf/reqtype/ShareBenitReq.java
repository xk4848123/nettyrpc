package nettyrpc.protobuf.reqtype;

import java.math.BigDecimal;

import nettyrpc.protobuf.annotation.withResp;

@withResp
public class ShareBenitReq implements Req {

	private int type;
	
	private int masterUserId;
	
	private String memo;
	
	private BigDecimal ElecNum;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getMasterUserId() {
		return masterUserId;
	}

	public void setMasterUserId(int masterUserId) {
		this.masterUserId = masterUserId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public BigDecimal getElecNum() {
		return ElecNum;
	}

	public void setElecNum(BigDecimal elecNum) {
		ElecNum = elecNum;
	}

}