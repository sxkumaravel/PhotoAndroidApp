package com.photoandroidapp;


import java.util.Date;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class AppVolleyApiManager {

	private RequestQueue requestQueue;
	private boolean isLiveFeedReq;
	public static AppVolleyApiManager volleyApiManager;
	
	public static AppVolleyApiManager instance() {
		
		if(volleyApiManager==null){
			volleyApiManager =new AppVolleyApiManager();
		}
		
		return volleyApiManager;
	}

	public synchronized  void initVolley(Context context) {
		if (requestQueue == null) {
			requestQueue = Volley.newRequestQueue(context);
		}
	}

	public synchronized RequestQueue getRequestQueue() {
		return requestQueue;
	}

	public void clearCache() {
		if (requestQueue != null) {
			requestQueue.getCache().clear();
		}
	}

	public void clearCache(String key) {
		if (requestQueue != null) {
			requestQueue.getCache().remove(key);
		}
	}

	public void cancelAllRequests() {
		if (requestQueue != null) {
			requestQueue.cancelAll(new RequestQueue.RequestFilter() {
				
				public boolean apply(Request<?> request) {
					// TODO Auto-generated method stub
					return false;
				}
			});
		}
	}

	public void cancelRequest(String tag) {
		if (requestQueue != null) {
			requestQueue.cancelAll(tag);
		}
	}

	public void getXmlStreamResponse(Context context, String url,
			final OnNetWorkResponse netWorkResponse) {

		StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {

			public void onResponse(String response) {
				// TODO Auto-generated method stub
				
			}
		},new Response.ErrorListener() {

			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				
			}
		});

		if (getRequestQueue() == null) {
			initVolley(context);
		}
		stringRequest.setCacheEntry(parseIgnoreCacheHeaders());
		stringRequest.setTag(url);
		getRequestQueue().add(stringRequest);
	}
	
	public void getJsonResponse(Context context,String url,final OnNetWorkResponse netWorkResponse){
		JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

			public void onResponse(JSONObject response) {
				netWorkResponse.onSuccessResponse(response);
				
			}
		},new Response.ErrorListener() {

			public void onErrorResponse(VolleyError error) {
				netWorkResponse.onError(error.getMessage());
				
			}
		});
		if(getRequestQueue()==null){
			initVolley(context);
		}
		jsonObjRequest.setCacheEntry(parseIgnoreCacheHeaders());
		jsonObjRequest.setTag(url);
		getRequestQueue().add(jsonObjRequest);
	} 
	
	public Cache.Entry parseIgnoreCacheHeaders() {
	    long now = System.currentTimeMillis();
	    long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
	    long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
	    long softExpire = now + cacheHitButRefreshed;
	    long ttl = now + cacheExpired;
	    if(isLiveFeedReq){
	    	softExpire=0;
	    	ttl=0;
	    }
	    
	    Cache.Entry entry = new Cache.Entry();
	    entry.softTtl = softExpire;
	    entry.ttl = ttl;
	    entry.serverDate = new Date().getTime();

	    return entry;
	}
	
	public boolean isLiveFeedReq() {
		return isLiveFeedReq;
	}

	public void setLiveFeedReq(boolean isLiveFeedReq) {
		this.isLiveFeedReq = isLiveFeedReq;
	}
}
