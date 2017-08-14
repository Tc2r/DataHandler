package com.tc2r.datahandler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class readActivity extends AppCompatActivity {

	private ImageView imageView;
	private String fileName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read);

		// Get the file from passing intent
		Intent intent = getIntent();
		fileName = intent.getStringExtra("savedImage");
		File file = new File(getFilesDir(), fileName);



		// Initialize imageview
		imageView = (ImageView) findViewById(R.id.iv_test);

		// Set imageView to bitmap returned by "getImageBackup" method.
		//imageView.setImageBitmap(getImageBackup(fileName));

		Picasso.with(this).load(file)
						.into(imageView);

	}

	// Attempts to get an image from internal memory of phone.
	public Bitmap getImageBackup(String fileName) {
		Bitmap image = null;


		File file = this.getFileStreamPath(fileName);
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			image = BitmapFactory.decodeStream(fileInputStream);
			fileInputStream.close();


		} catch (FileNotFoundException e) {
			Log.e("getImageBackup", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
}
