package org.input;

/**
 * Created by Ethan Pavolik on 2/27/2017.
 */
import java.util.*;

//keeps track of votes
public class TallyTable {
    private Map<String, Integer> table;
    private Set<String> posterSet;

    public TallyTable() {
        table = new HashMap<>();
    }

    //-1 poster does not exist, 0 duplicate vote, 1 valid
    public int addVote(String voterID, int posterID) {
        if (posterSet != null) {
            if (!posterSet.contains(posterID+""))
                return -1;
        }

        if (table.containsKey(voterID))
                return 0;

        table.put(voterID, posterID);
        return 1;
    }

    public void setCandidates(String[] posters) {
        if (posterSet == null)
            posterSet = new HashSet<>();

        for (String s : posters)
            posterSet.add(s);
    }

    public VoteEntry[] getResults() {
        TallyResult res;
        HashMap<Integer, Integer> results = new HashMap<>();


        int i = 0;
        for (Map.Entry<String, Integer> e : table.entrySet()) {
            int posterID = e.getValue();
            if (results.containsKey(posterID)) {
                int currentVotes = results.get(posterID);
                currentVotes++;
                results.put(posterID, currentVotes);
            }
            else {
                results.put(posterID, 1);
            }
        }
       // res = new TallyResult(results.size());
        res = new TallyResult(posterSet.size());
        for (Map.Entry<Integer, Integer> e : results.entrySet()) {
            res.addResult(e.getKey(), e.getValue());
        }

        if (posterSet != null) {
            for (String s : posterSet) {
                if (!res.contains(s))
                    res.addResult(Integer.parseInt(s), 0);
            }
        }

        res.sort();
        return res.getResults();
    }
}
