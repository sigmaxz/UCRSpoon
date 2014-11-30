package com.backendless.ucrspoon.login;

import java.util.List;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.*;
import com.backendless.ucrspoon.data.Restaurant;
import com.backendless.ucrspoon.data.Dish;
import com.backendless.ucrspoon.data.Review;

public class ReviewActivity extends Activity{

	private static final String NULL = null;
	private EditText DishField;
	
	private Button Submit;
	private Button up;
	
	private String dish;
	private Intent i;
	
	
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
			//i = new Intent(view.getContext(), Review2Activity.class);
	        //onReviewButtonClicked(view);
			//finish();
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
			 dish = DishText;
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

	private void showToast(String string) {
		// TODO Auto-generated method stub
	    Toast.makeText( this, string, Toast.LENGTH_SHORT ).show();
	}
}
