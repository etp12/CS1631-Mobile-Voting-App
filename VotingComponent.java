/**
* etp12
* Receives message from SIS Server to record votes.
**/

import java.io.*;

public class VotingComponent implements ComponentBase{

private final int init=0;
private final int success=1;
private final int failure=2;


private int state;

public VotingComponent(){
    state=init;
}

/* just a trivial example */

private void doAuthentication(String first,String last,String passwd){
if (first != null && last != null && passwd != null) {
	if (first.equals("Ethan")&&last.equals("Pavolik")&&passwd.equals("etp12"))
    state=success;
}
else {
	System.out.println("First: " + first);
	System.out.println("Last: " + last);
	System.out.println("passwd: " + passwd);

	  state=failure;
	}
}

/* function in interface ComponentBase */

public KeyValueList processMsg(KeyValueList kvList){
    int MsgID=Integer.parseInt(kvList.getValue("MsgID"));
    if (MsgID!=702) return null;
    doAuthentication(kvList.getValue("FirstName"),kvList.getValue("LastName"),kvList.getValue("passwd"));
    KeyValueList kvResult = new KeyValueList();
    kvResult.addPair("MsgID","1");
    kvResult.addPair("Description","Authentication Result");

   switch (state) {
   case success: {
      kvResult.addPair("Authentication","success");
      break;
   }
  case failure: {
     kvResult.addPair("Authentication","failure");
     break;
   }
   }
  return kvResult;
}

}
