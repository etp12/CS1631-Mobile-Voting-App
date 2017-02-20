/**
* etp12
* Receives message from SIS Server to record votes.
**/
import java.net.Socket;
public class VotingComponent {
	public static void main(String[] args) {
		try {
			Socket sock = new Socket("127.0.0.1", 53217);
			MsgDecoder decoder = new MsgDecoder(sock.getInputStream());

			KeyValueList kvl = new KeyValueList();
			kvl.putPair("MsgID", "21");
			kvl.putPair("Name", "VotingComponent");

			MsgEncoder encoder = new MsgEncoder(sock.getOutputStream());
			encoder.sendMsg(kvl);
			KeyValueList reply = decoder.getMsg();
			if (reply != null) {
				System.out.println("Reply received..");
				System.out.println(reply.getValue("MsgID"));
			} else {
				System.out.println("No reply!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}
