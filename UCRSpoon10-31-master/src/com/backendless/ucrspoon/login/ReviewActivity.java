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

public class ReviewActivity extends Activity{

	private static final String NULL = null;
	private EditText RestaurantField;
	private EditText DishField;
	
	private Button Submit;
	
	private String restaurant;
	private String Dish;
	private Intent i;
	
	
	public void onCreate( Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review);
	    Backendless.setUrl( Defaults.SERVER_URL ); // in case you didn't already do the init
		Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );

		initUI();
		
	}
	
	private void initUI()
	  {RestaurantField = (EditText) findViewById( R.id.RestaurantField );DishField = (EditText) findViewById( R.id.dishField );

	  
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
		 String RestaurantText = RestaurantField.getText().toString().trim();
		 String DishText = DishField.getText().toString().trim();
		 
		 if ( RestaurantText.isEmpty() )
		    {
		      showToast( "Field 'Restaurant' cannot be empty." );
		      
		      return;
		    }
		 
		 if(RestaurantText != NULL)
		 {
			 restaurant = RestaurantText;
		 }
		 
		 if(DishText != NULL)
		 {
			 Dish = DishText;
		 }
		 else
		 {
			 Dish = "";
		 }
		 String whereClause = "Rname = '"+ restaurant + "'"; // query for restaurant name

		 BackendlessDataQuery dataQuery = new BackendlessDataQuery();
		 dataQuery.setWhereClause( whereClause );
		 Restaurant.findAsync( dataQuery, new AsyncCallback<BackendlessCollection<Restaurant>>() // async call
		{
			 //two overides 
			  @Override
			  public void handleResponse( BackendlessCollection<Restaurant> response )
			  {
				  //check for size of what you got
				  List<Restaurant> lr = response.getData();
				  if(lr.size() < 1){
					  showToast( " Restaurant not found." );
					  return;
				  }
				  //since i'm looking for a name i took for instance
				  Restaurant firstRestaurant = response.getCurrentPage().get( 0 );
				  if( !restaurant.equals(firstRestaurant.getRname()))
				  {
					  showToast( " Restaurant not found." );
					  return;
				  }
				  else
				  {
					  //control flow into next Activity
					  showToast(" Restaurant found");
					  i.putExtra("restaurant", firstRestaurant.getR_id().toString());
					  i.putExtra("dish", Dish);
					  startActivity (i);
					  finish();
				  }
			  }
			@Override
			public void handleFault(BackendlessFault fault) { // does nothing but auto override 
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
