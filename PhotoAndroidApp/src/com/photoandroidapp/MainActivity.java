package com.photoandroidapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.dina.oauth.instagram.InstagramApp;
import br.com.dina.oauth.instagram.InstagramApp.OAuthAuthenticationListener;

public class MainActivity extends Activity {

	private InstagramApp mApp;
	private Button btnConnect;
	private TextView tvSummary;
	private AppVolleyApiManager apiManager;
	// private OAuthAuthenticationListener listener;

	private LinearLayout loadingView;
	private ListView detailListView;
	private Picasso picasso;
	private List<Details> list;
	private ListView listView;
	private CustomListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		loadingView = (LinearLayout) findViewById(R.id.loadingView);
		detailListView = (ListView) findViewById(R.id.list);
		updateUIState(View.VISIBLE, View.GONE);

		Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
		builder.downloader(new OkHttpDownloader((getApplicationContext())));
		picasso = builder.build();

		list = new ArrayList<Details>();
		adapter = new CustomListAdapter(MainActivity.this, 0, list, picasso);
		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);

		apiManager = AppVolleyApiManager.instance();
		apiManager.initVolley(getApplicationContext());

		loadData();

	}

	private void loadData() {
		mApp = new InstagramApp(this, ApplicationData.CLIENT_ID,
				ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
		mApp.setListener(new OAuthAuthenticationListener() {
			public void onSuccess() {
				// listener.onSuccess();
				if (mApp.hasAccessToken()) {
					mApp.getAccessToken();
					mApp.getId();
					tvSummary.setText("Connected as " + mApp.getUserName());
					btnConnect.setText("Disconnect");
					loadSelfMedia();
					Toast.makeText(getApplicationContext(),
							"Connection success", Toast.LENGTH_LONG).show();
					updateUIState(View.GONE, View.VISIBLE);
				}
			}

			public void onFail(String error) {
				// listener.onFail(error);

			}
		});

		tvSummary = (TextView) findViewById(R.id.tvSummary);

		btnConnect = (Button) findViewById(R.id.btnConnect);
		btnConnect.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				if (mApp.hasAccessToken()) {
					final AlertDialog.Builder builder = new AlertDialog.Builder(
							MainActivity.this);
					builder.setMessage("Disconnect from Instagram?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											mApp.resetAccessToken();
											btnConnect.setText("Connect");
											tvSummary.setText("Not connected");
										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					final AlertDialog alert = builder.create();
					alert.show();
				} else {
					mApp.authorize();
				}
			}
		});

		if (mApp.hasAccessToken()) {
			mApp.getAccessToken();
			mApp.getId();
			tvSummary.setText("Connected as " + mApp.getUserName());
			btnConnect.setText("Disconnect");
		}
	}

	protected void loadSelfMedia() {
		apiManager.getJsonResponse(
				getApplicationContext(),
				"https://api.instagram.com/v1/users/" + mApp.getId()
						+ "/media/recent/?access_token="
						+ mApp.getAccessToken(), new OnNetWorkResponse() {

					public void onSuccessResponse(JSONObject responseObject) {
						Log.v("","User Info Self Media : "+ responseObject.toString());
						
						if (responseObject != null){
							JSONArray instaGramData = responseObject.optJSONArray("data");
							
							if (instaGramData != null && instaGramData.length() > 0){
								for (int i = 0; i < instaGramData.length(); i++){
									JSONObject jsonObject = instaGramData.optJSONObject(i);
									JSONObject captionObj = jsonObject.optJSONObject("caption");
									//user hash tag
									String hashTag=null;
									if(captionObj!=null){
										hashTag = captionObj.optString("text", "no details posted");
									}
									
									JSONObject imageObject = jsonObject.optJSONObject("images");
									JSONObject imageObjectSmall = imageObject.optJSONObject("low_resolution");
									String imagePathSmall = imageObjectSmall.optString(
											"url", "no image url");
									JSONObject imageObjectBig = imageObject.optJSONObject("standard_resolution");
									String imagePathBig = imageObjectBig.optString(
											"url", "no image url");
									
									list.add(new Details(imagePathSmall, hashTag,imagePathBig));
									
								}
								
							}adapter.notifyDataSetChanged();
						}
					}

					public void onSuccessResponse(String xmlStreamSource) {
						// TODO Auto-generated method stub

					}

					public void onError(String error) {
						Log.v("", "User Info Self Media : " + error.toString());

					}
				});

	}

	private void updateUIState(int v1, int v2) {

		loadingView.setVisibility(v1);
		detailListView.setVisibility(v2);

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mApp.resetAccessToken();
	}
	
}