package org.input;

/**
 * Created by Ethan Pavolik on 4/19/2017.
 */

public class PreloadedKVL {
    public static KeyValueList get701A() {
        KeyValueList ret = new KeyValueList();
        ret.putPair("Scope", "SIS.Scope1");
        ret.putPair("MsgID", "701");
        ret.putPair("MessageType", "Reading");
        ret.putPair("VoterID", "7247718112");
        ret.putPair("PosterID", "3");
        return ret;
    }

    public static KeyValueList get701B() {
        KeyValueList ret = new KeyValueList();
        ret.putPair("Scope", "SIS.Scope1");
        ret.putPair("MsgID", "701");
        ret.putPair("MessageType", "Reading");
        ret.putPair("VoterID", "7247718112");
        ret.putPair("PosterID", "99");
        return ret;
    }

    public static KeyValueList get702A() {
        KeyValueList ret = new KeyValueList();
        ret.putPair("Scope", "SIS.Scope1");
        ret.putPair("MsgID", "702");
        ret.putPair("MessageType", "Reading");
        ret.putPair("Passcode", "1234");
        ret.putPair("N", "2");
        return ret;
    }

    public static KeyValueList get702B() {
        KeyValueList ret = new KeyValueList();
        ret.putPair("Scope", "SIS.Scope1");
        ret.putPair("MsgID", "702");
        ret.putPair("MessageType", "Reading");
        ret.putPair("Passcode", "9999");
        ret.putPair("N", "2");
        return ret;
    }

    public static KeyValueList get703A() {
        KeyValueList ret = new KeyValueList();
        ret.putPair("Scope", "SIS.Scope1");
        ret.putPair("MsgID", "703");
        ret.putPair("MessageType", "Reading");
        //ret.putPair("Sender", "VotingComponent");
        ret.putPair("CandidateList", "2;3;4");
        ret.putPair("Passcode", "1234");

        return ret;
    }

    public static KeyValueList get999() {
        KeyValueList ret = new KeyValueList();
        ret.putPair("Scope", "SIS.Scope1");
        ret.putPair("MsgID", "999");
        ret.putPair("MessageType", "Reading");

        return ret;
    }
}
