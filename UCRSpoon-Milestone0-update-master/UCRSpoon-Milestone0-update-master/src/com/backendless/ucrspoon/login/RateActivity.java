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
import com.backendless.ucrspoon.data.Dish;
import com.backendless.ucrspoon.data.Rating;
import com.backendless.ucrspoon.data.Review;
import com.backendless.ucrspoon.data.Restaurant;

public class RateActivity extends Activity{
	
	private static final String NULL = null;
	private EditText RateField;
	
	private Button Submit;
	
	private String rate;
	private Intent i;
	private int sid;
	
	private double rating ;
	private int nor ;
	private double rtotal ;
	
	
	public void onCreate( Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rate);
	    Backendless.setUrl( Defaults.SERVER_URL ); 
		Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );

		initUI();
		
	}
	
	private void initUI()
	  {
		RateField = (EditText) findViewById( R.id.rate_val );

	  
	    Submit = (Button) findViewById( R.id.rate_submit );

	    Submit.setOnClickListener( new View.OnClickListener()
	    {
	      @Override
	      public void onClick( View view )
	      {
	        onRateButtonClicked(view);
			//finish();
	      }
	    } );
	  }
	
	private void onRateButtonClicked(View view) {
		// TODO Auto-generated method stub
		 String RateText = RateField.getText().toString().trim();
		 

		 if ( RateText.isEmpty() )
		    {
		      showToast( "Field 'Rate' cannot be empty." );
		      
		      return;
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
		 
		 sid = getIntent().getIntExtra("sid", 0);
		 String whereClause = "R_id = '"+ sid + "'"; 

		 //find the restaurant
		 BackendlessDataQuery dataQuery = new BackendlessDataQuery();
		 dataQuery.setWhereClause( whereClause );
		 Restaurant.findAsync( dataQuery, new AsyncCallback<BackendlessCollection<Restaurant>>() 
		{
			  @Override
			  public void handleResponse( BackendlessCollection<Restaurant> response )
			  {
				  List<Restaurant> lr = response.getData();
				  if(lr.size() < 1){ //should never occur
					  showToast( " Restaurant not found." );
					  return;
				  }
				  Restaurant firstRestaurant = response.getCurrentPage().get( 0 );
				  
				  rating =  firstRestaurant.getRating();
				  nor = firstRestaurant.getNumOfRate();
				  rtotal = rating * nor;
				  //find if there is an existing rating 
				  String wc = "R_id = '" + sid + "' AND User = '" + Backendless.UserService.CurrentUser().getEmail() + "'";
				  BackendlessDataQuery dq = new BackendlessDataQuery();
				  dq.setWhereClause(wc);
				  Rating.findAsync(dq, new AsyncCallback<BackendlessCollection<Rating>>()
						  {

							@Override
							public void handleFault(BackendlessFault arg0) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void handleResponse(
									BackendlessCollection<Rating> res) {
								// TODO Auto-generated method stub
								List<Rating> lr2 = res.getData();
								//occurs when user has not rated restaurant before 
								if(lr2.size() < 1)
								{
									Rating r = new Rating();
									r.setR_id(sid);
									r.setRating( Integer.parseInt(rate));
									r.setUser(Backendless.UserService.CurrentUser().getEmail());
									r.saveAsync( new AsyncCallback<Rating>()
											{

												@Override
												public void handleFault(BackendlessFault arg0) {
													// TODO Auto-generated method stub
												}

												@Override
												public void handleResponse(Rating arg0) {
													// TODO Auto-generated method stub
												}
				 
											});
								}
								else
								{
									showToast("Overwriting previous rating " + res.getCurrentPage().get(0).getRating() );
									
									//values replacement for existing rating
									nor -= 1;
									rtotal -= res.getCurrentPage().get(0).getRating();
									
									res.getCurrentPage().get(0).setRating(Integer.parseInt(rate));
									res.getCurrentPage().get(0).saveAsync( new AsyncCallback<Rating>()
											{

										@Override
										public void handleFault(BackendlessFault arg0) {
											// TODO Auto-generated method stub
										}

										@Override
										public void handleResponse(Rating arg0) {
											// TODO Auto-generated method stub
										}
		 
									});
									
									
									
								}
								
							}
						 
						  });


				  firstRestaurant.setDRating((rtotal + Integer.parseInt(rate))/( nor + 1) );
				  firstRestaurant.setRating((int)Math.floor(rtotal + Integer.parseInt(rate))/(nor+1));
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