package com.backendless.ucrspoon.login;

import java.net.URL;
import java.util.List;

import android.app.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.*;
import com.backendless.ucrspoon.data.Restaurant;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;



public class NearbyRestaurant extends Activity {
	
	double longitude;
	double latitude;
	double longitude_max;
	double longitude_min;
	double latitude_max;
	double latitude_min;
	String[] restaurantlist;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby_restaurant);
		
		Backendless.setUrl( Defaults.SERVER_URL ); // in case you didn't already do the init
		Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );
	
		
		 LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		  LocationListener ll = new mylocationlistener(); 
		  lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll); // THE FUNCTION THAT UPDATES
		  
			Query query = new Query();
			query.execute();
 
	}
	private class Query extends AsyncTask<Void,Void,Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			for(int i = 0; i < 1; i++)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.interrupted();
				}
			}
			longitude_max = longitude + .1;
			longitude_min = longitude - .1;
			latitude_max = latitude +.1;
			latitude_min = latitude -.1;
			
			String whereClause = "Longitude < " +longitude_max + " AND Longitude > " +longitude_min+" AND "+
			"Latitude < " + latitude_max + " AND Latitude > " + longitude_min; 
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
					  
					   Restaurant firstRestaurant = response.getCurrentPage().get( 0 );

					   restaurantlist = new String[lr.size()];
					  
					   for(int i = 0; i < lr.size();i++)
					  {
						  	restaurantlist[i] = lr.get(i).getRname() + "\n\n" +
						  						"Description: " + lr.get(i).getDescription() + "\n" +
						  						"Cuisine Type: " +lr.get(i).getCuisineType() +"\n"+
						  						"Ratings: " + lr.get(i).getRating() + "\n" +
						  						"Avg. Price: " + lr.get(i).getAvgPrice() + "\n";
					  }
						//Print out list
						populateListView();
							
				}
				@Override
				public void handleFault(BackendlessFault fault) { // does nothing but auto override 
					// TODO Auto-generated method stub

					  return;
				}
			});

			
			return null;
		}
	}
	
	private void populateListView(){
		ArrayAdapter<String> adapter;
		adapter = new ArrayAdapter<String>(
				NearbyRestaurant.this,		 			//COntext for this activity
				R.layout.restaurat_list, //Layout to be use(create)
				restaurantlist);				//Items to be displayed
		ListView list = (ListView)findViewById(R.id.restaurant_list);
		list.setAdapter(adapter);
		registerClickCallback();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nearby_restaurant, menu);
		return true;
	}
	
	private void registerClickCallback() {
		ListView list = (ListView)findViewById(R.id.restaurant_list);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {
					TextView textView=(TextView)viewClicked;
					
					Intent intent = new Intent(NearbyRestaurant.this, RestaurantPage.class);
					String[] separated = textView.getText().toString().split("\n");
					intent.putExtra("Rname",separated[0]);
					//Toast.makeText(NearbyRestaurant.this, separated[2], Toast.LENGTH_LONG).show();
					startActivity (intent);
					
				// TODO Auto-generated method stub
				
			}
		
		});
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
	
	private class mylocationlistener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if (location != null)
			{
				longitude = location.getLongitude();
				latitude = location.getLatitude();
				  //Log.d("Longitude",String.format("%.2f", longitude));
				  //Log.d("Latitude",String.format("%.2f", latitude));	
			}
		}

		@Override
		public void onStatusChanged(String provider, int status,
				Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		  
	  }
}
