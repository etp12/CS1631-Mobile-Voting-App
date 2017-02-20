/**
* etp12
* Receives input (from SMS) and sends to PrjRemote
**/
import java.net.Socket;
public class InputProcessor {

	public static void main(String[] args) {
		try {
			Socket sock = new Socket("127.0.0.1", 53217);

			KeyValueList kvl = new KeyValueList();
			kvl.putPair("Ethan Pavolik", "123");
			kvl.putPair("Jordan Britton", "124");

			MsgEncoder encoder = new MsgEncoder(sock.getOutputStream());
			encoder.sendMsg(kvl);

			MsgDecoder decoder = new MsgDecoder(sock.getInputStream());
			KeyValueList reply = decoder.getMsg();
			if (reply != null) {
				System.out.println(reply.getValue("MsgID"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}