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
			kvl.putPair("MsgID", "22");
			kvl.putPair("Name", "InputProcessor");

			MsgEncoder encoder = new MsgEncoder(sock.getOutputStream());
			encoder.sendMsg(kvl);

			kvl = new KeyValueList();
			kvl.putPair("MsgID", "702");
			kvl.putPair("test", "test");
			encoder.sendMsg(kvl);
			sock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
