package com.photoandroidapp;

import java.util.List;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<Details> {

	private List<Details> list;
	private LayoutInflater inflater;
	private Picasso picasso;
	public static int count;

	public CustomListAdapter(Context context, int resource,
			List<Details> objects, Picasso picasso) {
		super(context, resource, objects);
		list = objects;
		inflater = LayoutInflater.from(context);
		this.picasso = picasso;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Details getItem(int position) {
		return list.get(position);
	}

	@Override
	public int getPosition(Details item) {
		return list.indexOf(item);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		Details details=getItem(position);
		
		
		
		 ViewHolder holder;
		    if (view == null) {
		      view = inflater.inflate(R.layout.list_single, parent, false);
		      holder = new ViewHolder();
		      holder.image = (ImageView) view.findViewById(R.id.img);
		      holder.textView =(TextView) view.findViewById(R.id.txt);
		      view.setTag(holder);
		    } else {
		      holder = (ViewHolder) view.getTag();
		    }
		    if(details.getUserHashtag()!=null){
		    	holder.textView.setText("tags : "+details.getUserHashtag());
		    }else{
		    	holder.textView.setText("tags : Nil");
		    }
		    //ordering the image big, small, small. 
		    count=position;
		    if(count%3==0){
		    	LayoutParams layoutParams=new LayoutParams(-1, -2);
		    	holder.image.setLayoutParams(layoutParams);
		    	picasso.load(details.getImagePathBig()).into(holder.image);
	    		count++;
		    }else if(count%3==1){
		    	LayoutParams layoutParams=new LayoutParams(310, 200);
		    	holder.image.setLayoutParams(layoutParams);
		    	picasso.load(details.getImagePath()).into(holder.image);
	    		count++;
		    }else if(count%3==2){
		    	LayoutParams layoutParams=new LayoutParams(310, 200);
		    	holder.image.setLayoutParams(layoutParams);
		    	picasso.load(details.getImagePath()).into(holder.image);
	    		count++;
		    }
		return view;
	}
	

	public static class ViewHolder {
		ImageView image;
		TextView textView;

	}

}
