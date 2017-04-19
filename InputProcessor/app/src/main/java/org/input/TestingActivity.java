package org.input;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.tdr.R;
import org.w3c.dom.Text;

import java.security.Key;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ethan Pavolik on 4/19/2017.
 */

public class TestingActivity extends Activity {
     Button btn701, btn702, btn703, btnRunAll;
     TextView testsConsole;
    static Set<String> msgs = new HashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_tests);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //grab ui
        btn701 = (Button) findViewById(R.id.btn701);
        btn702 = (Button) findViewById(R.id.btn702);
        btn703 = (Button) findViewById(R.id.btn703);
        btnRunAll = (Button) findViewById(R.id.runAllTests);
        testsConsole = (TextView) findViewById(R.id.testsConsole);
        testsConsole.setMovementMethod(new ScrollingMovementMethod());

        //set button handlers
        btn701.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.sendMessage(PreloadedKVL.get701A());
            }
        });
        btn702.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.sendMessage(PreloadedKVL.get702A());
            }
        });
        btn703.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.sendMessage(PreloadedKVL.get703A());
            }
        });
        btnRunAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Test Cases
                testsConsole.append("Test Case \t Result\n");
                //1 Correctly corrected to SIS Server
                if (MainActivity.client.isSocketAlive()) testsConsole.append("1. Successfully Connected to SIS Server: Pass\n");
                else testsConsole.append("1. Successfully Connected to SIS Server: Fail\n");

                //2. invalid message results in no response
                MainActivity.sendMessage(PreloadedKVL.get999());
                if (msgs.isEmpty()) testsConsole.append("2. Sending Invalid Message Results in no response: Pass\n");
                else testsConsole.append("2. Sending Invalid Message Results in no response: Fail\n");

                //3. Sending 703 results in ack
                MainActivity.sendMessage((PreloadedKVL.get703A()));
                if (msgs.contains("Ack")) testsConsole.append("3. Sending 703 results in Ack: Pass\n");
                else testsConsole.append("3. Sending 703 results in Ack: Fail\n");

                //4 Sending 702 with no votes doesn't crash the program
                try {
                    MainActivity.sendMessage((PreloadedKVL.get702A()));
                    testsConsole.append("4. Sending 702 with no votes doesn't crash component: Pass\n");
                } catch (Exception e) {
                    testsConsole.append("4. Sending 702 with no votes doesn't crash component: Fail\n");
                }

                //5. Sending valid 701 results in 711
                MainActivity.sendMessage((PreloadedKVL.get701A()));
                if (msgs.contains("711")) testsConsole.append("5. Sending 701 votes results in a 711 response: Pass\n");
                else testsConsole.append("5. Sending 701 votes results in a 711 response: Fail\n");

                //6. Voting for poster not in candidate list returns invalid
                MainActivity.sendMessage(PreloadedKVL.get701B());
                if (msgs.contains("Invalid711")) testsConsole.append("6. Voting for poster not in candidate list returns invalid: Pass\n");
                else testsConsole.append("6. Voting for poster not in candidate list returns invalid: Fail\n");

                //7. Voting for duplicate poster returns invalid
                MainActivity.sendMessage(PreloadedKVL.get701A());
                if (msgs.contains("Invalid711")) testsConsole.append("7. Voting for duplicate poster returns invalid: Pass\n");
                else testsConsole.append("7. Voting for duplicate poster returns invalid: Fail\n");

                //8. Giving wrong passcode doesn't return results
                MainActivity.sendMessage(PreloadedKVL.get702B());
                if (msgs.contains("712")) testsConsole.append("8. Giving wrong passcode doesn't return results: Pass\n");
                else testsConsole.append("8. Giving wrong passcode doesn't return results: Fail\n");

                //9. Sending 702 returns results correctly
                MainActivity.sendMessage(PreloadedKVL.get702A());
                if (msgs.contains("712")) testsConsole.append("9. Sending 702 returns results correctly: Pass\n");
                else testsConsole.append("9. Sending 702 returns results correctly: Fail\n");

            }

        });


        //resultsText.setText(resString);
    }

}
