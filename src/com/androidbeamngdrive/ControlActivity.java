package com.androidbeamngdrive;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class ControlActivity extends Activity implements SensorEventListener {

	private SensorManager msensorManager;

	private float[] rotationMatrix;
	private float[] accelData;
	private float[] magnetData;
	private float[] OrientationData;

	public TextView tvSteerXZ;
	public TextView tvThrottleZY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		msensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		rotationMatrix = new float[16];
		accelData = new float[3];
		magnetData = new float[3];
		OrientationData = new float[3];

		this.tvSteerXZ = (TextView) findViewById(R.id.tvSteerXZ);
		this.tvThrottleZY = (TextView) findViewById(R.id.tvThrottleZY);

		setContentView(R.layout.activity_control);
	}

	@Override
	protected void onResume() {
		super.onResume();
		msensorManager.registerListener(this,
				msensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
		msensorManager.registerListener(this,
				msensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onPause() {
		super.onPause();
		msensorManager.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	public void onSensorChanged(SensorEvent event) {
		loadNewSensorData(event);
		SensorManager.getRotationMatrix(rotationMatrix, null, accelData,
				magnetData);
		SensorManager.getOrientation(rotationMatrix, OrientationData);

		if (this.tvSteerXZ == null || this.tvThrottleZY == null) {
			this.tvSteerXZ = (TextView) findViewById(R.id.tvSteerXZ);
			this.tvThrottleZY = (TextView) findViewById(R.id.tvThrottleZY);
		}
		double steer = Math.round(Math.toDegrees(OrientationData[1])) * 0.01;
		double throttle = (Math.round(Math.toDegrees(OrientationData[2]) + 87) * 0.01);

		this.tvSteerXZ.setText(String.valueOf(steer));
		this.tvThrottleZY.setText(String.valueOf(throttle));
	}

	private void loadNewSensorData(SensorEvent event) {

		final int type = event.sensor.getType();

		if (type == Sensor.TYPE_ACCELEROMETER) {
			accelData = event.values.clone();
		}

		if (type == Sensor.TYPE_MAGNETIC_FIELD) {
			magnetData = event.values.clone();
		}
	}

}
