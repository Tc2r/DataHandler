package com.tc2r.datahandler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

	private final static String imageUrl = "http://p1.i.ntere.st/c85e85c55b96fb1b391ae678eb9bf427_480.jpg";
	private Target target;
	private String fileName;
	private FileOutputStream fileOutputStream;
	private FileInputStream fileInputStream;
	private ImageView imageView;
	private File file;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_main);

		Picasso picasso = Picasso.with(this);
		picasso.setIndicatorsEnabled(true);
		imageView = (ImageView) findViewById(R.id.iv_test);


		Utils utils = new Utils();

		utils.loadImage(this, imageUrl, imageView);


		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final String fileName = URLUtil.guessFileName(imageUrl, null, null);
				Intent intent = new Intent(MainActivity.this, readActivity.class);
				intent.putExtra("savedImage", fileName);
				startActivity(intent);
			}
		});
	}
}
