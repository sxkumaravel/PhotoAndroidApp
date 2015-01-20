package com.photoandroidapp;


import org.json.JSONObject;

public interface OnNetWorkResponse {

	public void onSuccessResponse(String xmlStreamSource);
	
	public void onSuccessResponse(JSONObject responseObject);

	public void onError(String error);

}
