package com.example.fileexternaldemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String FILENAME = "data.txt";
    private static final int READ_BLOCK_SIZE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText inputText = (EditText) findViewById(R.id.input_text);
        final TextView outputText = (TextView) findViewById(R.id.output_text);
        final Button btnSave = (Button) findViewById(R.id.save_button);
        final Button btnLoad = (Button) findViewById(R.id.load_button);
        
        final File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        final File file = new File(dir, FILENAME);
        
        btnSave.setOnClickListener(new View.OnClickListener() {
        
            @Override
            public void onClick(View v) {
                if (!isExternalStorageAvailable()) {
                    String msg = "External storage not available!";
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    return;
                }
                
                if (!dir.exists()) {
                    dir.mkdir();
                }

                String data = inputText.getText().toString();
                try {
                    //FileOutputStream fOut = openFileOutput(FILENAME, MODE_PRIVATE);

                    FileOutputStream fOut = new FileOutputStream(file);
                    OutputStreamWriter writer = new OutputStreamWriter(fOut);
                    
                    writer.write(data);
                    writer.flush();
                    writer.close();
        
                    inputText.setText("");
                    Toast.makeText(MainActivity.this,
                            "File saved successfully!", Toast.LENGTH_SHORT)
                            .show();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
        
        btnLoad.setOnClickListener(new View.OnClickListener() {
        
            @Override
            public void onClick(View v) {
                if (!isExternalStorageAvailable()) {
                    String msg = "External storage not available!";
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    //FileInputStream fIn = openFileInput(FILENAME);
                    
                    FileInputStream fIn = new FileInputStream(file);
                    InputStreamReader reader = new InputStreamReader(fIn);
                    
                    char[] buffer = new char[READ_BLOCK_SIZE];
                    String data = "";
                    int charReadCount;
                    while ((charReadCount = reader.read(buffer)) > 0) {
                        String readString = String.copyValueOf(buffer, 0,
                                charReadCount);
                        data += readString;
                        buffer = new char[READ_BLOCK_SIZE];
                    }
                    reader.close();
                    
                    outputText.setText(data);
                    Toast.makeText(MainActivity.this,
                            "File loaded successfully!", Toast.LENGTH_SHORT)
                            .show();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
    }
    
    public boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    
}
