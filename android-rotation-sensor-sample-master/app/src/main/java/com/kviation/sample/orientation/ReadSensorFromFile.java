package com.kviation.sample.orientation;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ReadSensorFromFile extends AppCompatActivity {
    File temp = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"Raw_sensor_data.txt");
    TextView text;
    StringBuilder Outputtext = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_sensor_from_file);
        text = (TextView) findViewById(R.id.textView);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(temp));
            String line;
            while ((line = br.readLine()) != null) {
                Outputtext.append(line);
                Outputtext.append('\n');
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        text.setText(Outputtext);


    }

}
