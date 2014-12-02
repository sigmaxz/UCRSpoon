package com.backendless.ucrspoon.login;

import java.util.List;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
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
String Rname;
String sname = new String("Rname");
String sdescription;
String scuisinetype;
String savgPrice;
String whereClause;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_page);
		
		Backendless.setUrl( Defaults.SERVER_URL ); // in case you didn't already do the init
		Backendless.initApp( RestaurantPage.this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );
		
		 Button order = (Button)findViewById(R.id.button_Order2);  
         order.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				startActivity(new Intent(v.getContext(), Odering.class));
			}
		}); 
		

		//Retrieve extras
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			Rname = extras.getString("Rname");
		}
		
				whereClause = "Rname = '" +Rname+ "'" ; 
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
						  
						   Restaurant restaurant = response.getCurrentPage().get( 0 );
							TextView name = (TextView)findViewById(R.id.title);
							TextView description = (TextView)findViewById(R.id.description);
							TextView cuisinetype = (TextView)findViewById(R.id.cuisinetype);
							TextView avgPrice = (TextView)findViewById(R.id.avgPrice);
							name.setText(restaurant.getRname());
							description.setText("Description: " + restaurant.getDescription());
							cuisinetype.setText("Cuisine Type: " +restaurant.getCuisineType());
							avgPrice.setText("Avg. Price: " +restaurant.getAvgPrice().toString());
							
							RatingBar rtb = (RatingBar)findViewById(R.id.ratingBar1);
							rtb.setRating((int)Math.floor(restaurant.getRating()));

						  
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
}