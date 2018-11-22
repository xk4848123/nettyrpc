
package nettyrpc.clienttest;
 
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import nettyrpc.protobuf.client.Bootstrap;
import nettyrpc.protobuf.client.DataFuther;
import nettyrpc.protobuf.reqtype.JpushReq;
import nettyrpc.protobuf.reqtype.PresentReq;
import nettyrpc.protobuf.resptype.NullResp;
import nettyrpc.protobuf.resptype.PresentResp;
import nettyrpc.protobuf.resptype.Resp;

 
/**
 * Created by Hebbe on 2018/1/13.
 */
public class NettyTest {
    @BeforeClass
    public  static void start() throws  Exception {
    }
 
    @AfterClass
    public  static  void  end() throws Exception {
    }
 
    @Before
    public  void before() throws Exception{
    }
 
    @After
    public void  after() throws Exception {
    }
    
   //no resp
    @Test
    public void nettytest() throws Exception {
    	new Thread(()->{
			try {
				JpushReq jpushReq = new JpushReq();
				jpushReq.setMessage("hello world");
				jpushReq.setUserId(6290);
				DataFuther.sendData(jpushReq);
			} catch (Exception e) {
				System.out.println("netty error:" + e.getMessage());
			}
		}).start();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.startAll();
    }
    
    //with resp
    @Test
    public void nettytest2() throws Exception {
    	new Thread(()->{
			try {
				PresentReq req = new PresentReq();
				req.setType(1);
				req.setUserId(6290);
				DataFuther.sendData(req);
				Resp resp = DataFuther.getData();
				if (resp instanceof NullResp || resp == null) {
					System.out.println("no data return!");
				}else{
					PresentResp presentResp = (PresentResp)resp;
					System.out.println(presentResp.getStatus());
				}
			} catch (Exception e) {
				System.out.println("netty error:" + e.getMessage());
			}
		}).start();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.startAll();
    }
 
}