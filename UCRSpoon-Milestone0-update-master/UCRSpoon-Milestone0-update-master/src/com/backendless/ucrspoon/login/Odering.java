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
import android.widget.Toast;

public class Odering extends Activity {

	String whereClause;
	String R_id;
	String tableLocation = "No Preference";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_odering);
		
		Backendless.setUrl( Defaults.SERVER_URL ); // in case you didn't already do the init
		//Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );		

		//Retrieve extras
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			R_id = extras.getString("R_id");
		}
		
		//Set
		EditText input_partySize = (EditText)findViewById(R.id.input_partySize);
		input_partySize.setText("1");
		
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
				 if(TableLocations_unparsed.isEmpty())
				 {
					 //Do Nothing
				 }
				 else
				 {
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

				 }
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
					//Format minute so that it displays a 0 before values less than 10. Example 09 instead of 9.
					String minute;
					if(time.getCurrentMinute() < 10)
					{
						minute = "0" + String.valueOf(time.getCurrentMinute());
					}
					else
					{
						minute = String.valueOf(time.getCurrentMinute());
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
					
					String partySize;
					EditText partySizeTmp = (EditText)findViewById(R.id.input_partySize);
					if(partySizeTmp.getText().toString().isEmpty() || partySizeTmp.getText().toString() == "0")
					{
						Toast alert = Toast.makeText(Odering.this, "Party size is defaulted to 1", Toast.LENGTH_LONG);
						alert.show();
						partySize = new String("1");
					}
					else
					{
						partySize = partySizeTmp.getText().toString();
					}
					
					Intent intent = new Intent(v.getContext(), Ordering2.class);
					Bundle bundle = new Bundle();
					intent.putExtra("time", hour + ":" + minute + " " + am_or_pm );
					intent.putExtra("partySize", partySize);
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
