package com.kviation.sample.orientation;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;

import static android.R.attr.value;

public class MainActivity extends AppCompatActivity implements Orientation.Listener {



    private Orientation mOrientation;
//  private AttitudeIndicator mAttitudeIndicator;
    TextView GyroscopeX;
    TextView GyroscopeY;
    TextView GyroscopeZ;
    TextView AccelerometerX;
    TextView AccelerometerY;
    TextView AccelerometerZ;
    boolean Recording;
    Button RecordButton;
    Button StopButton;
    Button ReadFile;
    File temp = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"Raw_sensor_data.txt");
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
      Recording = false;
    GyroscopeX = (TextView) findViewById(R.id.GyroscopeX);
    GyroscopeY = (TextView) findViewById(R.id.GyroscopeY);
    GyroscopeZ = (TextView) findViewById(R.id.GyroscopeZ);
    AccelerometerX = (TextView) findViewById(R.id.AccelerometerX);
    AccelerometerY = (TextView) findViewById(R.id.AccelerometerY);
    AccelerometerZ = (TextView) findViewById(R.id.AccelerometerZ);
      AccelerometerX.setText("0");
      AccelerometerY.setText("0");
      AccelerometerZ.setText("0");
      GyroscopeX.setText("0");
      GyroscopeY.setText("0");
      GyroscopeZ.setText("0");
      RecordButton = (Button) findViewById(R.id.RecordButton);
      StopButton = (Button) findViewById(R.id.StopButton);
      ReadFile = (Button) findViewById(R.id.ReadFile);

      ReadFile.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent myIntent = new Intent(MainActivity.this, ReadSensorFromFile.class);
              myIntent.putExtra("key", value); //Optional parameters
              MainActivity.this.startActivity(myIntent);
          }
      });

      RecordButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Recording = true;
          }
      });

      StopButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Recording = false;
          }
      });

    mOrientation = new Orientation(this);
  }

  @Override
  protected void onStart() {
    super.onStart();
    mOrientation.startListening(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    mOrientation.stopListening();
  }

  @Override
  public void onOrientationChanged(float GX, float GY, float GZ,float AX, float AY, float AZ) {
      verifyStoragePermissions(this);

      if(Recording) {
          FileWriter f = null;
          try {

               f= new FileWriter(temp, true);


              if (Math.abs(Float.valueOf(GyroscopeX.getText().toString()) - GX) > 0.1) {
                  GyroscopeX.setText(String.valueOf(GX));

                  if (Math.abs(Float.valueOf(GyroscopeX.getText().toString()) - GY) > 0.1) {
                      GyroscopeY.setText(String.valueOf(GY));
                  }
                  if (Math.abs(Float.valueOf(GyroscopeX.getText().toString()) - GZ) > 0.1) {
                      GyroscopeZ.setText(String.valueOf(GZ));
                  }
                  if (Math.abs(Float.valueOf(AccelerometerX.getText().toString()) - AX) > 0.1) {
                      AccelerometerX.setText(String.valueOf(AX));
                  }
                  if (Math.abs(Float.valueOf(AccelerometerY.getText().toString()) - AY) > 0.1) {
                      AccelerometerY.setText(String.valueOf(AY));
                  }
                  if (Math.abs(Float.valueOf(AccelerometerZ.getText().toString()) - AZ) > 0.1) {
                      AccelerometerZ.setText(String.valueOf(AZ));
                  }
                  f.write("GYROSCOPE: X:" + GyroscopeX.getText().toString() + ",Y:" + GyroscopeY.getText().toString() + ",Z:" + GyroscopeZ.getText().toString()+" ACCELEROMETER: X:" + AccelerometerX.getText().toString() + ",Y:" + AccelerometerY.getText().toString() + ",Z:" + AccelerometerZ.getText().toString() + "\n");
                  f.close();
              }



          }catch(Exception e){
              e.printStackTrace();
          }

      }
  }


}
