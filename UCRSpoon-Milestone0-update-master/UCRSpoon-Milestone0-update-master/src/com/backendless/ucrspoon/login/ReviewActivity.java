package com.backendless.ucrspoon.login;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.persistence.*;
import com.backendless.ucrspoon.data.Pictures;
import com.backendless.ucrspoon.data.Restaurant;
import com.backendless.ucrspoon.data.Dish;
import com.backendless.ucrspoon.data.Review;

public class ReviewActivity extends Activity{

	private static final String NULL = null;
	private EditText DishField;
	
	private Button Submit;
	private Button up;
	private Button take;
	
	private String dish;
	private Intent i;
	
	private String name;
	private Pictures bpic;
	private int flag = 0;
	
	
	
	public void onCreate( Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review);
	    Backendless.setUrl( Defaults.SERVER_URL ); 
		Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );

		initUI();
		
	}
	
	private void initUI()
	  {
		DishField = (EditText) findViewById( R.id.dishField );

	    up = (Button) findViewById( R.id.upButton );

	    up.setOnClickListener( new View.OnClickListener()
	    {
	      @Override
	      public void onClick( View view )
	      {
	    	  flag++;
	    	  Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
	    	  photoPickerIntent.setType("image/*");
	    	  startActivityForResult(photoPickerIntent,1);
	    	 
	    	  
	      }
	    } );
	    take = (Button) findViewById( R.id.takep );

	    take.setOnClickListener( new View.OnClickListener()
	    {
	      @Override
	      public void onClick( View view )
	      {
	    	  flag++;
	    	  Intent photoTake = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	    	  startActivityForResult(photoTake, 101);
	    	 
	    	  
	      }
	    } );
	  
	    Submit = (Button) findViewById( R.id.review_check );

	    Submit.setOnClickListener( new View.OnClickListener()
	    {
	      @Override
	      public void onClick( View view )
	      {
			i = new Intent(view.getContext(), Review2Activity.class);
	        onReviewButtonClicked(view);
			//finish();
	      }
	    } );
	    
	    
	  }
	
	private void onReviewButtonClicked(View view) {
		// TODO Auto-generated method stub
		 String DishText = DishField.getText().toString().trim();
		 
		 if(DishText != NULL && !DishText.contentEquals("") )
		 {
			 dish = DishText.substring(0,1).toUpperCase() + DishText.substring(1).toLowerCase();
			 BackendlessDataQuery dq = new BackendlessDataQuery();
			 String wc = "R_id = '" + Integer.toString(getIntent().getIntExtra("sid",0)) + "'";
			 wc = wc + " And Name = '" + dish + "'";
			 dq.setWhereClause(wc);
			 //look in Dish Table of database
			 Dish.findAsync( dq, new AsyncCallback<BackendlessCollection<Dish>>()
					 {
				  @Override
				  public void handleResponse( BackendlessCollection<Dish> response )
				  {
					  List<Dish> lr = response.getData();
					  if(lr.size() < 1){
						  
						  Dish d = new Dish();
						  d.setName(dish);
						  d.setR_id( Integer.toString(getIntent().getIntExtra("sid",0)) );
						  
						  d.saveAsync( new AsyncCallback<Dish>()
									{

										@Override
										public void handleFault(BackendlessFault arg0) {
											// TODO Auto-generated method stub
										}

										@Override
										public void handleResponse(Dish arg0) {
											// TODO Auto-generated method stub
											  showToast( " Dish added." );
										}
		 
									});
					  }
					  //extra not needed
					  /*else 
					  {
						  Dish firstDish = response.getCurrentPage().get( 0 );
						  dish = firstDish.getName();
					  }*/
					  
				  }
				@Override
				public void handleFault(BackendlessFault fault) { 
					// TODO Auto-generated method stub
					  return;
				}
			});
		 }
		 else
		 {
			 dish = "";
		 }
		 String whereClause = "R_id = '"+ Integer.toString(getIntent().getIntExtra("sid", 0)) + "'"; 

		 BackendlessDataQuery dataQuery = new BackendlessDataQuery();
		 dataQuery.setWhereClause( whereClause );
		 Restaurant.findAsync( dataQuery, new AsyncCallback<BackendlessCollection<Restaurant>>() 
		{
			  @Override
			  public void handleResponse( BackendlessCollection<Restaurant> response )
			  {
				  List<Restaurant> lr = response.getData();
				  if(lr.size() < 1){
					  showToast( " Restaurant not found." );
					  return;
				  }
				  Restaurant firstRestaurant = response.getCurrentPage().get( 0 );
				  showToast(" Restaurant Experience");
				  i.putExtra("restaurant", firstRestaurant.getR_id().toString());
				  i.putExtra("rnm", firstRestaurant.getRname());
				  i.putExtra("dish", dish);
				  i.putExtra("nor", firstRestaurant.getNumOfReviews());
				  i.putExtra("avgp", firstRestaurant.getAvgPrice());
				  startActivity (i);
				  finish();
				  
			  }
			@Override
			public void handleFault(BackendlessFault fault) { 
				// TODO Auto-generated method stub
				  return;
			}
		});
		 
		 
		 
	}

	
	 @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data)
	  {
		  if(requestCode == 1 || requestCode == 101)
		  {
			  if(resultCode == RESULT_OK)
			  {
				  //save pic here
				  Uri pic = data.getData();
				  //random name for file
				  name = UUID.randomUUID().toString() + ".jpeg";
				  //convert file into bitmap
				  Bitmap bitmap = null;
				try {
					bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pic);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				  try
				  {
					  Backendless.Files.Android.upload(bitmap, Bitmap.CompressFormat.JPEG, 100 , name , "Pictures", new AsyncCallback<BackendlessFile>()
							  {
						    @Override
						    public void handleResponse( final BackendlessFile backendlessFile )
						    {
						    	//save to table Pictures
						    	bpic = new Pictures();
						    	bpic.setFileLocation(backendlessFile.getFileURL());
						    	bpic.setUser(Backendless.UserService.CurrentUser().getEmail());
						    	bpic.setR_id(Integer.toString(getIntent().getIntExtra("sid", 0)));
						    	
						    	//prompt for description
						    	AlertDialog.Builder alert = new AlertDialog.Builder(ReviewActivity.this);

						    	alert.setTitle("Picture Description");
						    	alert.setMessage("Describe the picture you are uploading");

						    	// Set an EditText view to get user input 
						    	final EditText input = new EditText(ReviewActivity.this);
						    	alert.setView(input);

						    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						    	public void onClick(DialogInterface dialog, int whichButton) {
						    	  String value = input.getText().toString();
						    	  // Do something with value!
						    	    bpic.setDescription(value);
							    	//save now
							    	bpic.saveAsync( new AsyncCallback<Pictures>()
											{

												@Override
												public void handleFault(BackendlessFault arg0) {
													// TODO Auto-generated method stub
												}

												@Override
												public void handleResponse(Pictures arg0) {
													// TODO Auto-generated method stub
													  showToast( " Picture added." );
												}
				 
											});
						    	  }
						    	});

						    	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						    	  public void onClick(DialogInterface dialog, int whichButton) {
						    	    // Canceled.
						    		  return;
						    	  }
						    	});

						    	alert.show();
						    
						    	
						    }

						    @Override
						    public void handleFault( BackendlessFault backendlessFault )
						    {
						    	showToast(backendlessFault.toString());
						    }
						    } );
				  }
				  catch(Exception e)
				  {
					  showToast("e thrown");
					  showToast(e.toString());
				  }
				  
				  
			  }
		  }
	  }
	 
	private void showToast(String string) {
		// TODO Auto-generated method stub
	    Toast.makeText( this, string, Toast.LENGTH_SHORT ).show();
	}
}
