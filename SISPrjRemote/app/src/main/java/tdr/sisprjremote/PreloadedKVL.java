package tdr.sisprjremote;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Ethan Pavolik on 2/27/2017.
 */

public class PreloadedKVL {
    public static KeyValueList getFirst() {
        KeyValueList ret = new KeyValueList();
        ret.putPair("Scope", "SIS.Scope1");
        ret.putPair("MessageType", "Reading");
        ret.putPair("Sender", "PrjRemote");
        ret.putPair("Receiver", "InputProcessor");
        ret.putPair("VoterID", "7247718112");
        ret.putPair("PosterID", "22");

        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static KeyValueList getRand() {
        long votid = ThreadLocalRandom.current().nextLong(1000000000L, 9999999999L);
        int postid = ThreadLocalRandom.current().nextInt(2, 50);
        KeyValueList ret = new KeyValueList();
        ret.putPair("Scope", "SIS.Scope1");
        ret.putPair("MsgID", "701");
        ret.putPair("MessageType", "Reading");
        ret.putPair("Sender", "PrjRemote");
        ret.putPair("Receiver", "InputProcessor");
        ret.putPair("VoterID", votid+"");
        ret.putPair("PosterID", postid+"");

        return ret;
    }

    public static KeyValueList get701A() {
        KeyValueList ret = new KeyValueList();
        ret.putPair("Scope", "SIS.Scope1");
        ret.putPair("MsgID", "701");
        ret.putPair("MessageType", "Reading");
        ret.putPair("Sender", "PrjRemote");
        ret.putPair("Receiver", "InputProcessor");
        ret.putPair("VoterID", "7247718112");
        ret.putPair("PosterID", "2");

        return ret;
    }

    public static KeyValueList get701B() {
        KeyValueList ret = new KeyValueList();
        ret.putPair("Scope", "SIS.Scope1");
        ret.putPair("MsgID", "701");
        ret.putPair("MessageType", "Reading");
        ret.putPair("Sender", "PrjRemote");
        ret.putPair("Receiver", "InputProcessor");
        ret.putPair("VoterID", "7247718113");
        ret.putPair("PosterID", "5");

        return ret;
    }

    public static KeyValueList get701C() {
        KeyValueList ret = new KeyValueList();
        ret.putPair("Scope", "SIS.Scope1");
        ret.putPair("MsgID", "701");
        ret.putPair("MessageType", "Reading");
        ret.putPair("Sender", "PrjRemote");
        ret.putPair("Receiver", "InputProcessor");
        ret.putPair("VoterID", "7247718112");
        ret.putPair("PosterID", "3");

        return ret;
    }

    public static KeyValueList get702() {
        KeyValueList ret = new KeyValueList();
        ret.putPair("Scope", "SIS.Scope1");
        ret.putPair("MsgID", "702");
        ret.putPair("MessageType", "Reading");
        ret.putPair("Sender", "PrjRemote");
        ret.putPair("Receiver", "InputProcessor");
        ret.putPair("Passcode", "1234");
        ret.putPair("N", "2");
        return ret;
    }

    public static KeyValueList get703() {
        KeyValueList ret = new KeyValueList();
        ret.putPair("Scope", "SIS.Scope1");
        ret.putPair("MsgID", "703");
        ret.putPair("MessageType", "Reading");
        ret.putPair("Sender", "PrjRemote");
        ret.putPair("Receiver", "InputProcessor");
        ret.putPair("CandidateList", "2;3;4");
        ret.putPair("Passcode", "1234");

        return ret;
    }

    public static KeyValueList getRegister() {
        KeyValueList ret = new KeyValueList();
        ret.putPair("Scope", "SIS.Scope1");
        ret.putPair("MessageType", "Register");
        ret.putPair("Sender", "PrjRemote");
        return ret;
    }

    public static KeyValueList getConnect() {
        KeyValueList ret = new KeyValueList();
        ret.putPair("Scope", "SIS.Scope1");
        ret.putPair("MessageType", "Connect");
        ret.putPair("Sender", "PrjRemote");
        return ret;
    }
}
