package com.example.hvalfisk.notsosuckyhangman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class InstructionsActivity extends AppCompatActivity {

    View instructionWords;
    View instructionGuessing;
    View instructionWinning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        instructionWords = findViewById(R.id.instructionWords);
        instructionGuessing = findViewById(R.id.instructionGuessing);
        instructionWinning = findViewById(R.id.instructionWinning);

        insertText(instructionWords, "words");
        insertText(instructionGuessing, "guessing");
        insertText(instructionWinning, "winning");
    }

    private void insertText(View instruction, String instructionKey) {
        TextView header = (TextView) instruction.findViewById(R.id.instructionHeadline);
        TextView body = (TextView) instruction.findViewById(R.id.instructionBody);

        switch(instructionKey) {
            case "words":
                header.setText(getResources().getString(R.string.instruction_header_words));
                body.setText(getResources().getString(R.string.instruction_words));
                break;
            case "guessing":
                header.setText(getResources().getString(R.string.instruction_header_guessing));
                body.setText(getResources().getString(R.string.instruction_guessing));
                break;
            case "winning":
                header.setText(getResources().getString(R.string.instruction_header_winning));
                body.setText(getResources().getString(R.string.instruction_winning));
                break;

        }
    }
}
