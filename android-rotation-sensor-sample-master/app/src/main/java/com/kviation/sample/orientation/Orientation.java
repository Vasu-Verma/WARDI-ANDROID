
package com.kviation.sample.orientation;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.TextView;

public class Orientation implements SensorEventListener {

    float gx;
    float gy;
    float gz;
    float ax;
    float ay;
    float az;
    static TextView GyroscopeX;
  public interface Listener {
      void onOrientationChanged(float GyroX, float GyroY, float GyroZ,float AcceleroX, float AcceleroY, float AcceleroZ);
  }

  private static final int SENSOR_DELAY_MICROS = 50 * 1000; // 50ms

  private final WindowManager mWindowManager;

  private final SensorManager mSensorManager;

    @Nullable
    public final Sensor mGyroscopeSensor;
    @Nullable
    public final Sensor mAccelerometerSensor;

  private int mLastAccuracy;
  private Listener mListener;

  public Orientation(Activity activity) {
    mWindowManager = activity.getWindow().getWindowManager();
    mSensorManager = (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);

    // Can be null if the sensor hardware is not available
    mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
  }

  public void startListening(Listener listener) {
    if (mListener == listener) {
      return;
    }
    mListener = listener;
    if (mGyroscopeSensor == null) {
      LogUtil.w("Rotation vector sensor not available; will not provide orientation data.");
      return;
    }
    mSensorManager.registerListener(this, mGyroscopeSensor, SENSOR_DELAY_MICROS);
      mSensorManager.registerListener(this,mAccelerometerSensor,SENSOR_DELAY_MICROS);
  }

  public void stopListening() {
    mSensorManager.unregisterListener(this);
    mListener = null;
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    if (mLastAccuracy != accuracy) {
      mLastAccuracy = accuracy;
    }
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    if (mListener == null) {
      return;
    }
    if (mLastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
      return;
    }
    if (event.sensor == mGyroscopeSensor) {
        gx = event.values[0];
        gy = event.values[1];
        gz = event.values[2];
        updateOrientation();
    }else if(event.sensor == mAccelerometerSensor){
        ax = event.values[0];
        ay = event.values[1];
        az = event.values[2];
        updateOrientation();
    }
  }

  @SuppressWarnings("SuspiciousNameCombination")
  private void updateOrientation() {
//      Log.d(gx, "updateOrientation: ");
    mListener.onOrientationChanged(gx,gy,gz,ax,ay,az);
  }
}
