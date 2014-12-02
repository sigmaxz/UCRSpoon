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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class Odering extends Activity {

	String whereClause;
	String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_odering);
		
		Backendless.setUrl( Defaults.SERVER_URL ); // in case you didn't already do the init
		Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );		

		//Retrieve extras
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			name = extras.getString("name");
		}
		
		whereClause = "Rname = '"+name+ "'"; Log.v("ssdf",whereClause);
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
					ListView list = (ListView)findViewById(R.id.list_TableLoc); Log.v("d","f");
					list.setAdapter(adapter);

				  
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
					EditText partySize = (EditText)findViewById(R.id.input_partySize);
					
					Intent intent = new Intent(v.getContext(), Ordering2.class);
					intent.putExtra("time", "");
					intent.putExtra("partySize", partySize.getText());
					intent.putExtra("tableLocation", "");
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
}
