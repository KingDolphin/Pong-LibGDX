package com.leonmontealegre.pong.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_screen);

        Intent i = getIntent();
        int winner = i.getIntExtra("winner", -1);

        TextView winnerText = (TextView)findViewById(R.id.winner_text);
        String defaultText = getResources().getText(R.string.default_winner_text).toString();
        winnerText.setText(defaultText.replace("X", ""+winner));

        // Set start button to load the LibGDX application
        Button startGameButton = (Button)findViewById(R.id.back_to_menu_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
