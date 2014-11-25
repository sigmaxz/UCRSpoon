package com.backendless.ucrspoon.login;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.ucrspoon.data.Review;
import com.backendless.ucrspoon.data.Restaurant;

public class RateActivity extends Activity{
	
	private static final String NULL = null;
	private EditText RestaurantField;
	private EditText RateField;
	
	private Button Submit;
	
	private String restaurant;
	private String rate;
	private Intent i;
	
	
	public void onCreate( Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rate);
	    Backendless.setUrl( Defaults.SERVER_URL ); 
		Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );

		initUI();
		
	}
	
	private void initUI()
	  {RestaurantField = (EditText) findViewById( R.id.rate_rest );RateField = (EditText) findViewById( R.id.rate_val );

	  
	    Submit = (Button) findViewById( R.id.rate_submit );

	    Submit.setOnClickListener( new View.OnClickListener()
	    {
	      @Override
	      public void onClick( View view )
	      {
			//i = new Intent(view.getContext(), Review2Activity.class);
	        onRateButtonClicked(view);
			//finish();
	      }
	    } );
	  }
	
	private void onRateButtonClicked(View view) {
		// TODO Auto-generated method stub
		 String RestaurantText = RestaurantField.getText().toString().trim();
		 String RateText = RateField.getText().toString().trim();
		 
		 if ( RestaurantText.isEmpty() )
		    {
		      showToast( "Field 'Restaurant' cannot be empty." );
		      
		      return;
		    }
		 if ( RateText.isEmpty() )
		    {
		      showToast( "Field 'Rate' cannot be empty." );
		      
		      return;
		    }

		 if(RestaurantText != NULL)
		 {
			 restaurant = RestaurantText;
		 }
		 
		 if(RateText != NULL)
		 {
			 rate = RateText;
		 }
		 if( Integer.parseInt(rate) > 5 || Integer.parseInt(rate) < 0)
		{
			 showToast( "Field 'Rate' must be from 0 to 5");
			 return;
			 
		}
		 
		 
		 String whereClause = "Rname = '"+ restaurant + "'"; 

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
				  if( !restaurant.equals(firstRestaurant.getRname()))
				  {
					  showToast( " Restaurant not found." );
					  return;
				  }
				  else
				  {
					  showToast(" Restaurant found");
					  double rating =  firstRestaurant.getRating();
					  int nor = firstRestaurant.getNumOfRate();
					  double rtotal = rating * nor;
					  firstRestaurant.setRating((rtotal + Integer.parseInt(rate))/( nor + 1) );
					  firstRestaurant.setNumOfRate(nor + 1);
					  firstRestaurant.saveAsync(new DefaultCallback<Restaurant>(RateActivity.this)
					  {
				          @Override
				          public void handleResponse( Restaurant response )
				          {
				        	  showToast("rating updated");
				        	  //startActivity(i);
				        	  finish();
				          }
				        } );
					  //startActivity (i);
				  }
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