package com.backendless.ucrspoon.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.ucrspoon.data.Restaurant;
import com.backendless.ucrspoon.data.Orders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LoggedIn_Restaurant extends Activity {

	String R_id;
	HashMap<String, List<String>> Movies_category;
	List<String> Movies_list;
	ExpandableListView Exp_list;
	String name = "Restaurant Unavailable";
	TextView title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logged_in__restaurant);
		
		Backendless.setUrl( Defaults.SERVER_URL ); 
		Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );

		//Retrieve extras
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			R_id = extras.getString("R_id");
		}
		
		//Title of the list
		title = (TextView)findViewById(R.id.listTitle);
		
		
		//Initialize UI
		initUI();
		
		//Lists the Orders if order button is pressed
		 Button orders= (Button)findViewById(R.id.button_Orders);    
	     orders.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {	
					title.setText("Orders");
					
					String whereClause = "R_id = '" + R_id + "'";
					BackendlessDataQuery dataQuery = new BackendlessDataQuery();
					dataQuery.setWhereClause( whereClause );
					Orders.findAsync( dataQuery, new AsyncCallback<BackendlessCollection<Orders>>(){		
							@Override
							 public void handleResponse(BackendlessCollection<Orders> response )
							 { 
								  List<Orders> lr = response.getData();
								  if(lr.size() < 1){ 
									  return;
								  }
								  
								  //Initialize the list of Orders
								  String[] listOrders = new String[lr.size()];
								  
								  //Format the recived orders from backendless to a more readable format
								  for(int i = 0, size = lr.size(); i < size; i++)
								  {
									 String[] format = lr.get(i).getOrder().split(";");
									 listOrders[i] = "Name: " + format[0] + "\n" +
											 		 "Time: " + format[1] + "\n" +
											 		 "Party Size: " + format[2] + "\n" +
											 		 "Table Location: " + format[3] + "\n\n";
									 //Get Dishes
									 for(int j = 4, size2 = format.length-1; j < size2; j++)
									 {
										 listOrders[i]+= format[j] + "\n"; 
									 }
									 listOrders[i] += "\nTotal: $" + format[format.length-1]; 
								  }
								  
								  //Display orders
								  ArrayAdapter<String> adapter;
								  adapter = new ArrayAdapter<String>(
										  LoggedIn_Restaurant.this,		 //COntext for this activity
										  R.layout.restaurat_list, 		//Layout to be use(create)
										  listOrders);					//Items to be displayed
								  ListView list = (ListView)findViewById(R.id.list_DishCategory);
								  list.setAdapter(adapter);
							 }
							@Override
							public void handleFault(BackendlessFault fault) { // does nothing but auto override 
								// TODO Auto-generated method stub
								  return;
							}
					});
				}
	      });
	      
	      Button MSGs = (Button)findViewById(R.id.button_MSGs);    
	        MSGs.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {	
					title.setText("MSGs");
				}
				});
	        
	       Button Menu = (Button)findViewById(R.id.button3);     
	       Menu.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), SetMenu.class);
					intent.putExtra("name", name);
					startActivity(intent);
				}
				});
		
		
		//registerClickCallback();
	}

	private void initUI() {
		title.setText("Orders");
		
		String whereClause = "R_id = '" + R_id + "'";
		BackendlessDataQuery dataQuery = new BackendlessDataQuery();
		dataQuery.setWhereClause( whereClause );
		Orders.findAsync( dataQuery, new AsyncCallback<BackendlessCollection<Orders>>(){		
				@Override
				 public void handleResponse(BackendlessCollection<Orders> response )
				 { 
					  List<Orders> lr = response.getData();
					  if(lr.size() < 1){ 
						  return;
					  }
					  
					  //Initialize the list of Orders
					  String[] listOrders = new String[lr.size()];
					  
					  //Format the recived orders from backendless to a more readable format
					  for(int i = 0, size = lr.size(); i < size; i++)
					  {
						 String[] format = lr.get(i).getOrder().split(";");
						 listOrders[i] = "Name: " + format[0] + "\n" +
								 		 "Time: " + format[1] + "\n" +
								 		 "Party Size: " + format[2] + "\n" +
								 		 "Table Location: " + format[3] + "\n\n";
						 //Get Dishes
						 for(int j = 4, size2 = format.length-1; j < size2; j++)
						 {
							 listOrders[i]+= format[j] + "\n"; 
						 }
						 listOrders[i] += "\nTotal: $" + format[format.length-1]; 
					  }
					  
					  //Display orders
					  ArrayAdapter<String> adapter;
					  adapter = new ArrayAdapter<String>(
							  LoggedIn_Restaurant.this,		 //COntext for this activity
							  R.layout.restaurat_list, 		//Layout to be use(create)
							  listOrders);					//Items to be displayed
					  ListView list = (ListView)findViewById(R.id.list_DishCategory);
					  list.setAdapter(adapter);
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
		getMenuInflater().inflate(R.menu.logged_in__restaurant, menu);
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

