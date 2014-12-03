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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

public class Odering extends Activity {

	String whereClause;
	String R_id;
	String tableLocation = "No Preference";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_odering);
		
		Backendless.setUrl( Defaults.SERVER_URL ); // in case you didn't already do the init
		Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );		

		//Retrieve extras
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			R_id = extras.getString("R_id");
		}
		
		whereClause = "R_id = '"+R_id+ "'";
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

				 String TableLocations_unparsed = lr.get(0).getTableLocations();
				 
					String[] tableLocations = TableLocations_unparsed.split(";");
				  // tableLocations = 
				   for (int i = 0; i < tableLocations.length; i++)
				   {
					   tableLocations[i] = "\n" + tableLocations[i] +"\n";
				   }
					 
				    ArrayAdapter<String> adapter;
					adapter = new ArrayAdapter<String>(
							Odering.this,		 			//COntext for this activity
							R.layout.restaurat_list, //Layout to be use(create)
							tableLocations);				//Items to be displayed
					ListView list = (ListView)findViewById(R.id.list_TableLoc); 
					list.setAdapter(adapter);
					registerClickCallback();

				  
				   return;
						
			}
			@Override
			public void handleFault(BackendlessFault fault) { // does nothing but auto override 
				// TODO Auto-generated method stub
				  return;
			}
		});
		
		 Button buttonNext = (Button)findViewById(R.id.button_Next);
	        buttonNext.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					TimePicker time = (TimePicker)findViewById(R.id.timePicker_diningTime);
					//Check if time entered is valid
					//Get Hour
					String hour;
					if(time.getCurrentHour() % 12 == 0)
					{
						hour = "12";
					}
					else
					{
						hour = String.valueOf(time.getCurrentHour() % 12);
					}
					//Get am or pm
					String am_or_pm;
					if(time.getCurrentHour() >= 12)
					{
						am_or_pm = "pm";
					}
					else
					{
						am_or_pm = "am";
					}
					
					EditText partySize = (EditText)findViewById(R.id.input_partySize);
					
					Intent intent = new Intent(v.getContext(), Ordering2.class);
					Bundle bundle = new Bundle();
					intent.putExtra("time", hour + ":" + time.getCurrentMinute().toString() + " " + am_or_pm );
					intent.putExtra("partySize", partySize.getText().toString());
					intent.putExtra("tableLocation", tableLocation);
					intent.putExtra("R_id", R_id);
					startActivity(intent);
					// TODO Auto-generated method stub
					
				}
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.odering, menu);
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
	
	private void registerClickCallback() {
		final ListView list = (ListView)findViewById(R.id.list_TableLoc);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {
					tableLocation = (String)(list.getItemAtPosition(position));
					// TODO Auto-generated method stub		
			}
		
		});
	}
}
