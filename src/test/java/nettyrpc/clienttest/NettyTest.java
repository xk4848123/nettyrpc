
package nettyrpc.clienttest;
 
import java.util.concurrent.TimeUnit;

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
    	Bootstrap bootstrap = new Bootstrap();
    	new Thread(()->{
			try {
				JpushReq jpushReq = new JpushReq();
				jpushReq.setMessage("hello world");
				jpushReq.setUserId(6290);
				DataFuther.sendData(jpushReq);
				TimeUnit.SECONDS.sleep(3);
				bootstrap.stop();
			} catch (Exception e) {
				System.out.println("netty error:" + e.getMessage());
			}
		}).start();
       
        bootstrap.startDefault();
    }
    
    //with resp
    @Test
    public void nettytest2() throws Exception {
    	Bootstrap bootstrap = new Bootstrap();
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
				TimeUnit.SECONDS.sleep(3);
				// close after three seconds 
				bootstrap.stop();
			} catch (Exception e) {
				System.out.println("netty error:" + e.getMessage());
			}
		}).start();
    	 bootstrap.startDefault();
    }
 
}