package org.input;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.security.Key;

public class SmsReceiver extends BroadcastReceiver {


    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // get sms objects
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus.length == 0) {
                    return;
                }
                // large message might be broken into many
                SmsMessage[] messages = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sb.append(messages[i].getMessageBody());
                }
                String sender = messages[0].getOriginatingAddress();
                String message = sb.toString();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                try {
                    //make sure we can parse a poster ID before sending a 701
                    Integer.parseInt(message);
                } catch(Exception e) {
                    return;
                }
                KeyValueList temp = new KeyValueList();
                temp.putPair("MsgID", "701");
                temp.putPair("VoterID", sender);
                temp.putPair("PosterID", message);
                Log.d("Sms Received", temp.toString());
                MainActivity.parseVote(temp, sender);

                // prevent any other broadcast receivers from receiving broadcast
                // abortBroadcast();
            }
        }
    }
}