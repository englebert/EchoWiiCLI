package com.echowii.cli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Main Output Console Screen
    ListView lvTerminalOutput;
    EditText editTextCommand;

    private ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;

    // Yellow: User
    // Blue: System
    // White: From Flight Controller
    // 8k lines of buffering...
    int[] consoleColor = new int[8192];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing
        initApplication();
    }

    private void initApplication() {
        // Adapter pointer
        arrayList = new ArrayList<String>();

        // Create an ArrayAdapter from List
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the Item from the ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tvLabel = view.findViewById(android.R.id.text1);

                // Setting the Color
                tvLabel.setTextColor(consoleColor[position]);

                // Setting font types
                tvLabel.setTypeface(Typeface.MONOSPACE);
                tvLabel.setTextSize((float)10.9);

                Log.d("ECHOWII", String.valueOf(position));

                return view;
            }
        };

        lvTerminalOutput = findViewById(R.id.lvTerminalOutput);
        lvTerminalOutput.setAdapter(adapter);

        // EditText Commander
        editTextCommand = findViewById(R.id.etCommand);

        // Button Send
        Button buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand();
            }
        });

        showWelcomeMessage();
    }

    private void showWelcomeMessage() {
        consoleColor[0] = Color.BLUE;
        String msg = "Welcome to EchoWii CLI version " + getResources().getString(R.string.version);
        adapter.add(msg);
    }

    private void sendCommand() {
        // Retrieving values
        String str_command = editTextCommand.getText().toString();

        // Skip if empty
        if(str_command.length() == 0) return;

        // Injecting to ListView Array
        // Adapter pointer
        consoleColor[arrayList.size()] = Color.YELLOW;
        adapter.add(str_command);

        // Checking command. If command is not defined will return the error.
        String[] str_command_parts = str_command.split(" ");
        String command_results = "";
        switch(str_command_parts[0]) {
            case "help":
                command_results = help();
                break;

            case "exit":
                finish();
                break;

            // Something not registered or right!!
            default:
                command_results = errorCommand();
                break;
        }

        editTextCommand.setText("");
        adapter.add(command_results);
    }

    private String errorCommand() {
        String error_command = "Command not found or not available.";
        consoleColor[arrayList.size()] = Color.RED;
        return error_command;
    }

    private String help() {
        String str_help = "Available commands: [help][exit]";
        consoleColor[arrayList.size()] = Color.CYAN;
        return str_help;
    }
}