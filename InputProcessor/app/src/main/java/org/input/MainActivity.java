package org.input;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.tdr.R;
import org.w3c.dom.Text;

import java.security.Key;
import java.util.Timer;
import java.util.TimerTask;

/*
    A demo Activity, which shows how to build a connection with SIS server, register itself to the server,
    and send a message to the server as well.
    Edited to be our VotingComponent
 */
public class MainActivity extends Activity {

    public static final String TAG = "Voting Component";

    private static Button connectToServerButton,registerToServerButton
            ,toggleVotingButton, viewResultsButton, setPosterListButton, runTestsButton;

    private EditText serverIp,serverPort, posterList;

   public static ComponentSocket client;

    private static TextView messageReceivedListText, votingEnabledText;

    private static final String SENDER = "VotingComponent";
    private static final String REGISTERED = "Registered";
    private static final String DISCOONECTED =  "Disconnect";
    private static final String SCOPE = "SIS.Scope1";

    private KeyValueList readingMessage;

    public static final int CONNECTED = 1;
    public static final int DISCONNECTED = 2;
    public static final int MESSAGE_RECEIVED = 3;

    private static TallyTable voteTable;
    private static boolean votingEnabled;
    public static VoteEntry[] results;
    private static String passcode = "";
    private BroadcastReceiver smsReceiver;
    private static SmsManager smsManager = SmsManager.getDefault();

    String data = null;//"EMG:333ECG:111V";

    static Handler callbacks = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String str;
            String[] strs;
            switch (msg.what) {
                case CONNECTED:
                    registerToServerButton.setText(REGISTERED);
                    Log.e(TAG, "===============================================================CONNECTED" );
                    break;
                case DISCONNECTED:
                    connectToServerButton.setText("Connect");
                    Log.e(TAG, "===============================================================DISCONNECTED" );
                    break;
                case MESSAGE_RECEIVED:
                    KeyValueList recv = (KeyValueList)msg.obj;
                    str = recv.toString();
                    parseKVL(recv);
                    messageReceivedListText.append(str+"********************\n");
                    final int scrollAmount = messageReceivedListText.getLayout().getLineTop(messageReceivedListText.getLineCount()) - messageReceivedListText.getHeight();
                    if (scrollAmount > 0)
                        messageReceivedListText.scrollTo(0, scrollAmount);
                    else
                        messageReceivedListText.scrollTo(0, 0);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private static void scrollText(KeyValueList kv) {
        if (messageReceivedListText != null) {
            messageReceivedListText.append(kv + "********************\n");
            final int scrollAmount = messageReceivedListText.getLayout().getLineTop(messageReceivedListText.getLineCount()) - messageReceivedListText.getHeight();
            if (scrollAmount > 0)
                messageReceivedListText.scrollTo(0, scrollAmount);
            else
                messageReceivedListText.scrollTo(0, 0);
        }
    }

    //Called when receiving a direct SIS message
    protected static void parseKVL(KeyValueList recv) {
        if (recv.getValue("MsgID") != null) {
            if (recv.getValue("MsgID").equals("701")) {
                Log.e(TAG, "Received 701");
                parseVote(recv);
            }
            else if(recv.getValue("MsgID").equals("702")) {
                Log.e(TAG, "Received 702");
                if (!recv.getValue("Passcode").equals(passcode)) {
                    KeyValueList send = new KeyValueList();
                    send.putPair("Scope", SCOPE);
                    send.putPair("MessageType", "Reading");
                    send.putPair("Sender", "InputProcessor");
                    send.putPair("MsgID", "712");
                    send.putPair("RankedReport", "null");
                    client.setMessage(send);
                }
                else {
                    VoteEntry[] res = voteTable.getResults();
                    String resultsString = "";
                    int n = Integer.parseInt(recv.getValue("N"));

                    for (int i = 0, j = 0; i < res.length && j < n; i++) {
                        if (res[i] != null) {
                            resultsString += (res[i].posterID + "\t | \t" + res[i].votes + "\n");
                            j++;
                        }
                    }
                    KeyValueList send = new KeyValueList();
                    send.putPair("Scope", SCOPE);
                    send.putPair("Sender", "InputProcessor");
                    send.putPair("MessageType", "Reading");
                    send.putPair("MsgID", "712");
                    send.putPair("RankedReport", resultsString);
                    client.setMessage(send);
                }


            }
            else if(recv.getValue("MsgID").equals("703")) {
                Log.e(TAG, "Received 703");
                voteTable = new TallyTable();
                String posters = recv.getValue("CandidateList");
                passcode = recv.getValue("Passcode");
                String[] postersList = posters.split(";");
                voteTable.setCandidates(postersList);

                KeyValueList send = new KeyValueList();
                send.putPair("Scope", SCOPE);
                send.putPair("MessageType", "Reading");
                send.putPair("Sender", "InputProcessor");
                send.putPair("MsgID", "26");
                send.putPair("Ack", "Ack");
                client.setMessage(send);
            }
        }
    }

    //used when receving a text message
    public static void parseVote(KeyValueList recv, String sender) {
        scrollText(recv);
        if (votingEnabled) {
            if (voteTable == null)
                voteTable = new TallyTable();

            if (recv.getValue("VoterID") == null || recv.getValue("PosterID") == null
                    || recv.getValue("PosterID").equals("")) {
                KeyValueList send = new KeyValueList();

                send.putPair("Scope", SCOPE);
                send.putPair("MessageType", "Reading");
                send.putPair("Sender", "InputProcessor");
                send.putPair("MsgID", "711");
                send.putPair("Status", "Invalid");
          //      client.setMessage(send);
                smsManager.sendTextMessage(sender, null, "Vote Invalid!", null, null);
            }

            String voterID = recv.getValue("VoterID");

            int posterID;
            try {
                posterID = Integer.parseInt(recv.getValue("PosterID"));
            } catch(Exception e) {
                return;
            }
            int accepted = voteTable.addVote(voterID, posterID);
            KeyValueList send = new KeyValueList();

            send.putPair("Scope", SCOPE);
            send.putPair("MessageType", "Reading");
            send.putPair("Sender", "InputProcessor");
            send.putPair("MsgID", "711");
            if (accepted == 1) {
                send.putPair("Status", "Valid");
                smsManager.sendTextMessage(sender, null, "Vote Accepted!", null, null);
            } else if (accepted == -1){
                send.putPair("Status", "Invalid");
                smsManager.sendTextMessage(sender, null, "Vote Invalid!", null, null);
            } else if (accepted == 0) {
                send.putPair("Status", "Duplicate");
                smsManager.sendTextMessage(sender, null, "Duplicate Vote!", null, null);
            }
            Log.e(TAG, "Sending 711");
//            client.setMessage(send);

        }
    }

    //called when we receive a 701 voting message
    public static void parseVote(KeyValueList recv) {
        scrollText(recv);
        if (votingEnabled) {
            if (voteTable == null)
                voteTable = new TallyTable();

            if (recv.getValue("VoterID") == null || recv.getValue("PosterID") == null
                    || recv.getValue("PosterID").equals("")) {
                KeyValueList send = new KeyValueList();

                send.putPair("Scope", SCOPE);
                send.putPair("MessageType", "Reading");
                send.putPair("Sender", "InputProcessor");
                send.putPair("MsgID", "711");
                send.putPair("Status", "Invalid");
                client.setMessage(send);
                smsManager.sendTextMessage("+17247718112", null, "Vote Invalid!", null, null);
            }

            String voterID = recv.getValue("VoterID");
            int posterID = Integer.parseInt(recv.getValue("PosterID"));

            int accepted = voteTable.addVote(voterID, posterID);
            KeyValueList send = new KeyValueList();

            send.putPair("Scope", SCOPE);
            send.putPair("MessageType", "Reading");
            send.putPair("Sender", "InputProcessor");
            send.putPair("MsgID", "711");
            if (accepted == 1) {
                send.putPair("Status", "Valid");
            }
            else if (accepted == -1){
                send.putPair("Status", "Invalid");
            }
            else if (accepted == 0) {
                send.putPair("Status", "Duplicate");
            }
            Log.e(TAG, "Sending 711");
            client.setMessage(send);

        }

    }

    public void showResults() {
        Intent intent = new Intent(this, ShowResults.class);
        startActivity(intent);
    }

    public void runTests() {
        Intent intent = new Intent(this, TestingActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        data = this.getIntent().getStringExtra("data");
        if(data!=null){
            Log.e(TAG, "Received an intent data: "+data );
        }

        SmsReceiver rec = new SmsReceiver();
        IntentFilter intentFilter = new IntentFilter("MainActivity.intent.MAIN");

        this.registerReceiver(rec, intentFilter);

        votingEnabled = true;

        connectToServerButton = (Button) findViewById(R.id.connectToServer);
        runTestsButton = (Button) findViewById(R.id.runTestsBtn);
        registerToServerButton = (Button) findViewById(R.id.registerToServerButton);
        toggleVotingButton = (Button) findViewById(R.id.toggleVotingButton);
        viewResultsButton = (Button) findViewById(R.id.viewResultsButton);
        setPosterListButton = (Button) findViewById(R.id.setPosterList);
        posterList = (EditText) findViewById(R.id.posterList);
        serverIp = (EditText) findViewById(R.id.serverIp);
        serverPort = (EditText) findViewById(R.id.serverPort);
        votingEnabledText = (TextView) findViewById(R.id.votingEnabledText);
        votingEnabledText.setText("Voting Enabled: " + votingEnabled);

        messageReceivedListText = (TextView) findViewById(R.id.messageReceivedListText);
        messageReceivedListText.setMovementMethod(ScrollingMovementMethod.getInstance());

        registerToServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(client!=null && client.isSocketAlive() && registerToServerButton.getText().toString().equalsIgnoreCase(REGISTERED)){
                    Toast.makeText(MainActivity.this,"Already registered.",Toast.LENGTH_SHORT).show();
                }else{
                    client = new ComponentSocket(serverIp.getText().toString(), Integer.parseInt(serverPort.getText().toString()),callbacks);
                    client.start();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            KeyValueList list = generateRegisterMessage();
                            client.setMessage(list);
                        }
                    }, 500);


                }
            }
        });
        connectToServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(MainActivity.TAG, "Sending connectToServerButton.1" );
                if(connectToServerButton.getText().toString().equalsIgnoreCase(DISCOONECTED)){
                    Log.e(MainActivity.TAG, "Sending connectToServerButton.2" );
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            client.killThread();
                        }
                    }, 100);
                    connectToServerButton.setText("Connect");
                }else{
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            KeyValueList list = generateConnectMessage();
                            client.setMessage(list);
                        }
                    }, 100);
                    connectToServerButton.setText(DISCOONECTED);
                }
            }
        });
        toggleVotingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                votingEnabled = !votingEnabled;
                votingEnabledText.setText("Voting Enabled: " + votingEnabled);
            }
        });
        viewResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (voteTable != null) {
                    results = voteTable.getResults();
                    if (results != null)
                        showResults();
                }
            }
        });
        runTestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            runTests();
            }
        });
        setPosterListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (posterList != null) {
                    String[] postersList = posterList.getText().toString().split(";");
                    voteTable = new TallyTable();
                    voteTable.setCandidates(postersList);
                }
            }
        });
    }
    //Generate a test register message, please replace something of attributes with your own.
    KeyValueList generateRegisterMessage(){
        KeyValueList list = new KeyValueList();
        //Set the scope of the message
        list.putPair("Scope",SCOPE);
        //Set the message type
        list.putPair("MessageType","Register");
        //Set the sender or name of the message
        list.putPair("Sender",SENDER);
        //Set the role of the message
        list.putPair("Role","Basic");
        //Set the name of the component
        list.putPair("Name",SENDER);
        return list;
    }
    //Generate a test connect message, please replace something of attributes with your own.
    KeyValueList generateConnectMessage(){
        KeyValueList list = new KeyValueList();
        //Set the scope of the message
        list.putPair("Scope",SCOPE);
        //Set the message type
        list.putPair("MessageType","Connect");
        //Set the sender or name of the message
        list.putPair("Sender",SENDER);
        //Set the role of the message
        list.putPair("Role","Basic");
        //Set the name of the component
        list.putPair("Name",SENDER);
        return list;
    }
    //Generate a test register message, please replace something of attributes with your own.
    KeyValueList generateReadingMessage(){
        KeyValueList list = new KeyValueList();
        //Set the scope of the message
        list.putPair("Scope",SCOPE);
        //Set the message type
        list.putPair("MessageType","Reading");
        //Set the sender or name of the message
        list.putPair("Sender",SENDER);
        //Set the role of the message
        list.putPair("Role","Basic");

        list.putPair("Name", "Ethan");
        list.putPair("ContestantID", "5");

        return list;
    }

}
