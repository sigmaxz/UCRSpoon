package com.backendless.ucrspoon.login;

import java.util.List;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.ucrspoon.data.Restaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

public class RestaurantPage extends Activity {
String R_id;
String sname = new String("Rname");
String sdescription;
String scuisinetype;
String savgPrice;
String whereClause;
int sid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_page);
	
		Backendless.setUrl( Defaults.SERVER_URL ); // in case you didn't already do the init
		Backendless.initApp( RestaurantPage.this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );
		//Retrieve extras
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			R_id = extras.getString("R_id");
		}
		//System.out.println("~~~~~~~~~~~~:::::"+R_id);
		Button order = (Button)findViewById(R.id.button_Order2);  
        order.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BackendlessUser user = Backendless.UserService.CurrentUser();
				if (user == null)
				{
					Toast invalid = Toast.makeText(RestaurantPage.this, "You must be logged in to order", Toast.LENGTH_SHORT);
					invalid.show();
				}
				else
				{
					Intent intent = new Intent(v.getContext(), Odering.class);
					intent.putExtra("R_id", R_id);
					startActivity(intent);
				}
				
			}
		}); 
        Button review_button = (Button)findViewById(R.id.acReview);
        review_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			if(! Backendless.UserService.isValidLogin())
			{
				showToast("Login required");
				return;
			}
			Intent i = new Intent(v.getContext(), ReviewActivity.class);
			i.putExtra("sid", sid);
			startActivity (i);
			finish();
				// TODO Auto-generated method stub
				
			}
		});
        
        Button gallery_button = (Button)findViewById(R.id.gal);
        gallery_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent i = new Intent(v.getContext(), GalleryActivity.class);
			i.putExtra("sid", sid);
			startActivity (i);
				// TODO Auto-generated method stub
				
			}
		});
        
        Button rate_button = (Button)findViewById(R.id.rateB);
        rate_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			if(! Backendless.UserService.isValidLogin())
			{
				showToast("Login required");
				return;
			}
			Intent i = new Intent(v.getContext(), RateActivity.class);
			i.putExtra("sid", sid);
			startActivity (i);
				// TODO Auto-generated method stub
				
			}
		});   
        /*
        Button message_button = (Button)findViewById(R.id.messageRestaurant);
        message_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			if(! Backendless.UserService.isValidLogin())
			{
				showToast("Login required");
				return;
			}
			R_id = ;
			Intent i = new Intent(v.getContext(), MessageActivity.class);
			
			startActivity (i);
				// TODO Auto-generated method stub
				
			}
		});*/
		
				whereClause = "R_id = '" +R_id+ "'" ; 
				BackendlessDataQuery dataQuery = new BackendlessDataQuery();
				dataQuery.setWhereClause( whereClause );
				Restaurant.findAsync( dataQuery, new AsyncCallback<BackendlessCollection<Restaurant>>() // async call
				{		
					  @Override
					  public void handleResponse(BackendlessCollection<Restaurant> response )
					  {
						  List<Restaurant> lr = response.getData(); 
						  
						  if(lr.size() < 1){
							  return;
						  }
						  //System.out.println("RRRRRREEEESSSSSSTTTTTTTTAAAAARRRRRRRRAAUUUTTTTT");
						   Restaurant restaurant = response.getCurrentPage().get( 0 );
							TextView name = (TextView)findViewById(R.id.title);
							TextView description = (TextView)findViewById(R.id.description);
							TextView cuisinetype = (TextView)findViewById(R.id.cuisinetype);
							TextView avgPrice = (TextView)findViewById(R.id.avgPrice);
							name.setText(restaurant.getRname());
							description.setText("Description: " + restaurant.getDescription());
							cuisinetype.setText("Cuisine Type: " +restaurant.getCuisineType());
							avgPrice.setText("Avg. Price: " +String.format("%.2f",restaurant.getAvgPrice()));
							sid = restaurant.getR_id();
							
							RatingBar rtb = (RatingBar)findViewById(R.id.ratingBar1);
							rtb.setRating(restaurant.getRating());

						    
						   return;
								
					}
					@Override
					public void handleFault(BackendlessFault fault) { // does nothing but auto override 
						// TODO Auto-generated method stub

						  return;
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.restaurant_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showToast(String string) {
		// TODO Auto-generated method stub
	    Toast.makeText( this, string, Toast.LENGTH_SHORT ).show();
	}
}
