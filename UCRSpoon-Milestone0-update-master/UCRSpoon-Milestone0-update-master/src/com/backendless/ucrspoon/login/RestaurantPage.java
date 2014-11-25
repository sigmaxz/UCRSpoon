package com.backendless.ucrspoon.login;

import java.util.List;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.ucrspoon.data.Restaurant;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

		

		
		Bundle extras = getIntent().getExtras();
		Log.v("SDF","O");//			name.setText(extras.getString("Rname"));
		//description.setText(extras.getString("Description"));

		if(extras != null) {
			Log.v("SDF","DDDO");
			Rname = extras.getString("Rname");
		}
		
				whereClause = "Rname = '" +Rname+ "'" ; 
				Log.v("HOOO",whereClause);
				BackendlessDataQuery dataQuery = new BackendlessDataQuery();
				dataQuery.setWhereClause( whereClause );
				Log.v("OSSS","THERE");
				Restaurant.findAsync( dataQuery, new AsyncCallback<BackendlessCollection<Restaurant>>() // async call
				{		
					//resultCollection = response;
					 //two overides 

					  @Override
					  public void handleResponse(BackendlessCollection<Restaurant> response )
					  {
						  List<Restaurant> lr = response.getData(); 
						  
						  if(lr.size() < 1){
							  Log.d("DDD","AAAAAA");
							  //showToast( " Restaurant not found." );
							  return;
						  }
						  
						   Restaurant restaurant = response.getCurrentPage().get( 0 );
						   Log.v("TEST222",restaurant.getRname());
						   Log.v("DDDSS",restaurant.getRname());
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
						Log.v("CHEES","GOAAA");
						Log.v("ERROR", fault.getCode());
						//Log.v("ERROR", fault.getDetail());
						Log.v("ERROR", fault.getMessage());
						Log.v("ERROR", fault.toString());


						  return;
					}
				});
				//Log.v("DDDSS", sname);
				//final String sname2 = sname;
				//Log.v("SSS",sname);
				//Log.v("YU",sname);
				if(sname == null)
				{
					Log.v("ki","jj");
				}
				//Toast.makeText(RestaurantPage.this, sname, Toast.LENGTH_LONG).show();
	
		
		
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
