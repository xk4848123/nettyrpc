package nettyrpc.protobuf.resptype;

import java.util.HashMap;

public class RespHandleMapping {

	private final String SHAREBENIT = "withresp_shareBenitresp";

	private final String PRESENT = "withresp_presentresp";

	private HashMap<String, Class<?>> map = null;

	public RespHandleMapping() {
		initHandleMapping();
	}

	private void initHandleMapping() {
		map = new HashMap<>();
		map.put(SHAREBENIT, ShareBenitResp.class);
		map.put(PRESENT, PresentResp.class);
	}

	public Class<?> transferToClass(String header) {
		return map.get(header);
	}

	public void add(String header, Class<?> type) {
		map.put(header, type);
	}
}
