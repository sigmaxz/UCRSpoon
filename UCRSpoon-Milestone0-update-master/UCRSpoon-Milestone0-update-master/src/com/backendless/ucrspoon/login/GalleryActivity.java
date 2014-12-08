package com.backendless.ucrspoon.login;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.ucrspoon.data.Pictures;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class GalleryActivity extends Activity{
	
	
	private ImageSwitcher imageSwitcher;
	private Gallery gallery;
	private ImageAdapter imageAdapter;
	private List<Pictures> lr;
    private ArrayList<Drawable> alld = new ArrayList<Drawable>();
    private ArrayList<String> descriptions = new ArrayList<String>();
    private TextView textView;
    
    
		 protected void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        
		        //populate images
		    	 String wc = "R_id = '" + Integer.toString(getIntent().getIntExtra("sid",0)) + "'";
				 BackendlessDataQuery dq = new BackendlessDataQuery();
				 dq.setWhereClause(wc);
				 //look for urls
				 Pictures.findAsync( dq, new AsyncCallback<BackendlessCollection<Pictures>>()
						 {
					  @Override
					  public void handleResponse( final BackendlessCollection<Pictures> response )
					  {
						  lr = response.getData();
						  if(lr.size() < 1){
							  showToast("No Pictures available ");
							  finish();
							  
						  }
							  // launch thread to get the image
							  new Thread()
							  {
							  @Override
							  public void run()
							  {
							  List<Pictures> imageEntities = response.getCurrentPage();

							  for( Pictures picture : imageEntities )
							  {
								  Message message = new Message();
								  try
								  {

									  URL url = new URL( picture.getFileLocation() );
									  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
									  connection.setDoInput( true );
									  connection.connect();
									  InputStream input = connection.getInputStream();
									  BitmapFactory.Options options = new BitmapFactory.Options();
									  options.inSampleSize = 3;
									  message.obj = BitmapFactory.decodeStream( input, null, options );
									  
								  }
								  catch( Exception e )
								  {
									  //message.obj = e;
									  showToast(e.toString());
								  }
								  imagesHandler.sendMessage( message );

							  }
							  }
							  }.start();
							  List<Pictures> ie = response.getCurrentPage();
							  for( Pictures picture: ie)
							  {
								  descriptions.add(picture.getDescription());
							  }
//
							
						  
						  
					  }
					@Override
					public void handleFault(BackendlessFault fault) { 
						// TODO Auto-generated method stub
						  return;
					}
				});
		    	
		    //    alld.add(this.getResources().getDrawable(R.drawable.pic1));
		  //      alld.add(this.getResources().getDrawable(R.drawable.pic2));
		        
		        setContentView(R.layout.gallery_page);

		        
		        // get The references
                imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
                // Set the ViewFactory of the ImageSwitcher that will create ImageView object when asked
                imageSwitcher.setFactory(new ViewFactory() {
                   
                    public View makeView() {
                        // TODO Auto-generated method stub
                       
                            // Create a new ImageView set it's properties
                            ImageView imageView = new ImageView(getApplicationContext());
                            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
                            return imageView;
                    }
                });

                // Declare the animations and initialize them
                Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
                Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);
               
                // set the animation type to imageSwitcher
                imageSwitcher.setInAnimation(in);
                imageSwitcher.setOutAnimation(out);
                
 //               imageSwitcher.setImageDrawable(alld.get(0));
                textView = (TextView) findViewById(R.id.gDes);
                textView.setText("Loading");
                /////////////////////////////////////////////////////////////////////////////////////////// 
                // Note that Gallery view is deprecated in Android 4.1---
                gallery = (Gallery) findViewById(R.id.gallery1);
                imageAdapter = new ImageAdapter(this);
                gallery.setAdapter(imageAdapter);
                gallery.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position,long id)
                {
                // display the images selected
                ImageSwitcher imageView = (ImageSwitcher) findViewById(R.id.imageSwitcher);
                imageView.setImageDrawable(alld.get(position));//imageIDs[position]);
                textView.setText(descriptions.get(position));
                }
                });
                }
                 
                public class ImageAdapter extends BaseAdapter {
                private Context context;
                private int itemBackground;
                public ImageAdapter(Context c)
                {
                context = c;
                // sets a grey background; wraps around the images
                TypedArray a =obtainStyledAttributes(R.styleable.MyGallery);
                itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
                a.recycle();
                }
                // returns the number of images
                public int getCount() {
                return alld.size();//imageIDs.length;
                }
                public void add( )
        		{
        			//images.add( bitmap );
        			notifyDataSetChanged();
        		}
                // returns the ID of an item
                public Object getItem(int position) {
                return position;
                }
                // returns the ID of an item
                public long getItemId(int position) {
                return position;
                }
                // returns an ImageView view
                public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView = new ImageView(context);
                //imageView.setImageResource(imageIDs[position]);
                imageView.setImageDrawable(alld.get(position));
                imageView.setLayoutParams(new Gallery.LayoutParams(200, 200));
                imageView.setBackgroundResource(itemBackground);
                return imageView;
                } 
                
               
			        
			 }
                private Handler imagesHandler = new Handler( new Handler.Callback()
            	{
            		@Override
            		public boolean handleMessage( Message message )
            		{
            			Object result = message.obj;
            			showToast("new picture");
            			if(message.obj instanceof Bitmap)
						  {

							  Drawable d = new BitmapDrawable(getResources(),(Bitmap)message.obj);
							  alld.add(d);
							  imageAdapter.add();
							  if(alld.size() == 1)
							  {
								  textView.setText("");
							  }
							  
						  }
/*
            			if( result instanceof Bitmap )
            				imageAdapter.add( (Bitmap) result ); */
            			else
            				Toast.makeText( GalleryActivity.this, ((Exception) result).getMessage(), Toast.LENGTH_SHORT ).show();

            			return true;
            		}
            	} );
		 
			private void showToast(String string) {
				// TODO Auto-generated method stub
			    Toast.makeText( this, string, Toast.LENGTH_SHORT ).show();
			}
			

		

}
