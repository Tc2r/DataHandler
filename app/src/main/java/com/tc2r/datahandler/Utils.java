package com.tc2r.datahandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by Tc2r on 8/13/2017.
 * <p>
 * Description:
 */

public class Utils {

	private Target target;
	public Utils() {
	}

	// 1st, Attempt to load data from Picasso's Cache, which should be the fastest way.
	// 2nd, Attempt to load data from External Url, save to Internal Storage.
	// 3rd, Attempt to load data from Internal Storage if External Url fails.
	public void loadImage(final Context context, final String imageUrl, final ImageView imageView){

		final String fileName = URLUtil.guessFileName(imageUrl, null, null);

		target= new Target() {
			@Override
			public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
				Log.i("Picasso", "OnBitmapLoaded");


				if (context == null){
					return;
				}

				FileOutputStream fileOutputStream = null;

				try {
					fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);

				} catch (FileNotFoundException e) {
					Log.e(TAG, e.getMessage());
				}

				// Save Image To HD
				Drawable drawImage = new BitmapDrawable(context.getResources(), bitmap);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fileOutputStream);
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					IOUtils.closeQuietly(fileOutputStream);
				}

				//Set Image to View
				imageView.setImageBitmap(bitmap);
			}

			@Override
			public void onBitmapFailed(Drawable errorDrawable) {
				Log.i("Picasso", "OnBitmapFailed");

				// check for local storage of file, load.
				File file = new File(context.getFilesDir(), fileName);
				Picasso.with(context).load(file).into(imageView);
			}

			@Override
			public void onPrepareLoad(Drawable placeHolderDrawable) {
			}
		};
		Picasso.with(context)
						.load(fileName)
						.networkPolicy(NetworkPolicy.OFFLINE)
						.into(imageView, new Callback() {
							@Override
							public void onSuccess() {
								Log.i(TAG, "Offline Loading Successful - " + fileName);
							}
							@Override
							public void onError() {
								Log.w(TAG, "Offline Loading Error -" + fileName);

								Picasso.with(context).load(imageUrl).into(imageView, new Callback() {
									@Override
									public void onSuccess() {
										Log.i(TAG, "Internet Loading Successful - " + imageUrl);
										if (context == null){
											return;
										}

										Drawable drawImage = imageView.getDrawable();
										FileOutputStream fileOutputStream = null;

										try {
											fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
										} catch (FileNotFoundException e) {
											Log.e("Load image Error", e.getMessage());
										}

										// Save Image To HD
										Bitmap bitmap = ((BitmapDrawable)drawImage).getBitmap();
										bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fileOutputStream);
										try {
											fileOutputStream.close();
										} catch (IOException e) {
											Log.e("Load image IO Error", e.getMessage());

										}finally {
											IOUtils.closeQuietly(fileOutputStream);
										}
									}

									@Override
									public void onError() {
										Log.i(TAG, "Internet Loading Failure - " + imageUrl);

										// Convert the string and file path
										File file = new File(context.getFilesDir(), fileName);

										if (file.exists()) {
											Log.i(TAG, "Internal Loading Successful - " + file.toString());
											// set imageview using backed up file.
											Picasso.with(context).load(file).into(imageView);
										} else {
											Log.i(TAG, "Internal Loading Failed - " + file.toString());
										}
									}
								});
							}
						});
	}
}
