package org.input;

import android.os.Bundle;
import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.widget.EditText;
import android.widget.TextView;

import org.tdr.R;

public class ShowResults extends Activity {
    private TextView resultsText;
    private final String PRERESULTS = "PosterID | # Votes\n";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);

        resultsText = (TextView) findViewById(R.id.resultsList);
        resultsText.setMovementMethod(new ScrollingMovementMethod());
        VoteEntry[] res = MainActivity.results;
        String resString = PRERESULTS;
        if (res != null) {
            for (int i = 0; i < res.length; i++) {
                if (res[i] != null)
                    resString += (res[i].posterID + "\t | \t" + res[i].votes + "\n");
            }
        }
        resultsText.setText(resString);
    }

}
