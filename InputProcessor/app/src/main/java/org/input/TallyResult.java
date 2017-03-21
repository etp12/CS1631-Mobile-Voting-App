package org.input;

import android.util.Log;

import java.util.Arrays;

/**
 * Created by Ethan Pavolik on 2/27/2017.
 */

class VoteEntry implements Comparable<VoteEntry> {
    int posterID;
    int votes;

    public VoteEntry(int p , int v) {
        posterID = p;
        votes = v;
    }

    @Override
    public int compareTo(VoteEntry voteEntry) {
        if (votes > voteEntry.votes) return -1;
        if (votes < voteEntry.votes) return 1;
        else return 0;
    }
}

public class TallyResult {

    int num;
    private VoteEntry[] results;
    public TallyResult(int capacity) {
        num = 0;
        if (capacity == 0)
            results = new VoteEntry[5];
        else
            results = new VoteEntry[capacity];
    }
    public TallyResult() {
        num = 0;
        results = new VoteEntry[5];
    }

    private void ensureCapacity() {
        if (num == results.length) {
            results = Arrays.copyOf(results, results.length * 2);
        }
    }

    public boolean contains(String s) {
        for(VoteEntry v : results) {
            if (v != null) {
                if (s.equals(v.posterID + "")) {
                    Log.d("TallyResult", "true in contains(): " + s + " " + v.posterID);
                    return true;
                }
            }
        }
        return false;
    }

    public void addResult(int posterID, int votes) {
        ensureCapacity();
        results[num] = new VoteEntry(posterID, votes);
        num++;
    }

    public void sort() {
        Arrays.sort(results, 0, num-1);
        //Arrays.sort(results);
    }

    public VoteEntry[] getResults() {
        return results;
    }

}
