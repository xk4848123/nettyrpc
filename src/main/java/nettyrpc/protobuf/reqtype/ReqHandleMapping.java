package nettyrpc.protobuf.reqtype;

import java.util.HashMap;

public class ReqHandleMapping {
	private final String GETRIDOF = "getRidOf";
	private final String JPUSH = "Jpush";
	private final String SHAREBENIT = "withresp_shareBenit";
	private final String PRESENT = "withresp_present";
	private final String Heart = "withresp_heart";
	
	private HashMap<Class<?>, String> map = null;
	
	public ReqHandleMapping() {
		initHandleMapping();
	}
	
    private void initHandleMapping(){
    	map = new HashMap<>();
    	map.put(GetRidOfReq.class, GETRIDOF);
    	map.put(JpushReq.class, JPUSH);
    	map.put(ShareBenitReq.class, SHAREBENIT);
    	map.put(PresentReq.class, PRESENT);
    	map.put(HeartReq.class, Heart);
    }
    
    public String transferToString(Class<?> type){
		return map.get(type);
    }
    
    public void add(Class<?> type,String header){
		map.put(type, header);
    }
}
