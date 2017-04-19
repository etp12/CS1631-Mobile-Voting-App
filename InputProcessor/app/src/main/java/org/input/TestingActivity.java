package org.input;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.tdr.R;
import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ethan Pavolik on 4/19/2017.
 */

public class TestingActivity extends Activity {
     Button btn701, btn702, btn703, btnRunAll;
     TextView testsConsole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_tests);

        //grab ui
        btn701 = (Button) findViewById(R.id.btn701);
        btn702 = (Button) findViewById(R.id.btn702);
        btn703 = (Button) findViewById(R.id.btn703);
        btnRunAll = (Button) findViewById(R.id.runTestsBtn);
        testsConsole = (TextView) findViewById(R.id.testsConsole);
        testsConsole.setMovementMethod(new ScrollingMovementMethod());

        //set button handlers
        btn701.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.client.setMessage(PreloadedKVL.get701A());
            }
        });
        btn702.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn703.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        //resultsText.setText(resString);
    }

}
