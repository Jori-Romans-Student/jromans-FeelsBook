package com.example.android.jromans_feelsbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {

    private static final String FILENAME = "file.sav"; // File of where to store feeling information
    private ListView feelingsList; // Feelings list variable
    private Feeling[] feelings = new Feeling[1000]; //class that stores the feelings
    private List<String> infoForListView = new ArrayList<>(); // contains information for listview
    private ArrayAdapter<String> adapter;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); // format used for the date

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button love = (Button) findViewById(R.id.love); // Button to add the feeling
        Button joy = (Button) findViewById(R.id.joy); // Button to add the feeling
        Button suprise = (Button) findViewById(R.id.suprise); // Button to add the feeling
        Button anger = (Button) findViewById(R.id.anger); // Button to add the feeling
        Button sadness = (Button) findViewById(R.id.sadness); // Button to add the feeling
        Button fear = (Button) findViewById(R.id.fear); // Button to add the feeling
        feelingsList = (ListView) findViewById(R.id.oldFeelingsList); // List of feelings already entered

        love.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                newFeeling(true, "Love", df.format(new Date()).toString());
            }
        });

        joy.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                newFeeling(true, "Joy", df.format(new Date()).toString());
            }
        });

        suprise.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                newFeeling(true, "Suprise", df.format(new Date()).toString());
            }
        });

        anger.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                newFeeling(true, "Anger", df.format(new Date()).toString());
            }
        });

        sadness.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                newFeeling(true, "Sadness", df.format(new Date()).toString());
            }
        });

        fear.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                newFeeling(true, "Fear", df.format(new Date()).toString());
            }
        });

    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        String[] feelingsWithDateAndComment = loadFromFile(); // load each value by date

        String[] parsedString = new String[5];
        int iterator = 0;
        for (String feel: feelingsWithDateAndComment) {
            parsedString = feel.split("\\|", 5);
            feelings[iterator] = new Feeling(parsedString[0], parsedString[1], parsedString[2]); // store information in feelings app
            infoForListView.add(parsedString[0] + '\n' + parsedString[1]);
            iterator++;
        }
        // Init list view
        adapter = new ArrayAdapter<String>(this,
                R.layout.feelings_button, infoForListView);
        feelingsList.setAdapter(adapter);
    }

    /* Code that is ran when a new feeling button is pressed (one of the emotions). It creates a pop up window
        for the request, and when the user is done saves the information nad stores it in the list view.

        isNew - Indicates if the feeling is new (one of the feeling buttons was pressed), or if one of the
                list view feelings is being edited
        emotion - The feeling being called, dependant on what was pressed
        date - The date of what was called, dependant on what was pressed
     */
    public void newFeeling(boolean isNew, final String emotion, final String date) {

        String comment = "";

        // If not new, then it will have a comment. So search through the Feeling class array and find the
            //one associated with the view
        if(!isNew) {
            for(Feeling feeling: feelings) {
                // If the feeling in the array matches the list view object selected
                if (date.equals(feeling.getDate()) && emotion.equals(feeling.getEmotion())) {
                    comment = feeling.getComment();
                }
            }
        }

        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_add_a_feeling, null); // inflate the view for the pop up

        // Set qualities in view
        final EditText userInput = (EditText) view.findViewById(R.id.userInput); // Input of feeling user will enter
        TextView feeling = (TextView) view.findViewById(R.id.emotion); // Create an indication of which feeling it is on the pop up
        TextView textDate = (TextView) view.findViewById(R.id.date); // Create an indication of which date it is on the pop up
        userInput.setText(comment); // Where you can add the comment
        feeling.setText(emotion); // set
        textDate.setText(date); // set

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this); // init pop up
        alertBuilder.setView(view); // set pop up view to add_a_feeling

        // When you hit the save button....
        alertBuilder.setCancelable(true).setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_OK);
                String newComment = userInput.getText().toString(); // put comment into string
                saveInFile(emotion, date, newComment); // immediately save it to the file
                infoForListView.add(emotion + "\n " + date); // Add to the list view element
                feelings[infoForListView.size() - 1] = new Feeling(emotion, date, newComment); // Add to class array for future modification
                adapter.notifyDataSetChanged(); // Update the lsit view
                //finish();

            }});

        Dialog dialog = alertBuilder.create(); // Create the pop up
        dialog.show();
    }

    /* Loads feeling information from file, which is written in the form of feeling | date | comment\n
     */
    private String[] loadFromFile() {
        ArrayList<String> feelings = new ArrayList<String>();
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            String line = in.readLine();
            while (line != null) {
                feelings.add(line);
                line = in.readLine();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return feelings.toArray(new String[feelings.size()]);
    }

    /* Saves feeling information to file, which is written in the form of feeling | date | comment\n */
    private void saveInFile(String feeling, String date, String comment) {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_APPEND);
            fos.write(new String(feeling + " | " + date + " | " + comment + "\n")
                    .getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

