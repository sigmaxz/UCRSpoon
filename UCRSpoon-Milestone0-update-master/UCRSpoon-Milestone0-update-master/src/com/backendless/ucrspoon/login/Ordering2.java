package com.backendless.ucrspoon.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.ucrspoon.data.Dish;
import com.backendless.ucrspoon.data.Restaurant;
import com.backendless.ucrspoon.data.Orders;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Ordering2 extends Activity {

	String[] categories; 
	String[] items; 
	HashMap<String, Integer> Orders;
	HashMap<String, Double> Prices;
	String R_id;
	String partySize;
	String tableLocation;
	String time;
	String selectedItem;
	double totalPrice = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ordering2);
		
		Backendless.setUrl( Defaults.SERVER_URL ); // in case you didn't already do the init
		Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );
		
		//Retrieve extras
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			R_id = extras.getString("R_id");
			Log.v("SDF","SDFSDFSDF");
			time = extras.getString("time");
			partySize = extras.getString("partySize");
			tableLocation = extras.getString("tableLocation");Log.v("SDF","DSF");
			populateCategory();
		}
		
		TextView selectedCategory = (TextView)findViewById(R.id.itemPrompt);
		selectedCategory.setText("----");
		
		Prices = new HashMap<String, Double>();
		Orders = new HashMap<String, Integer>();
		
		Button orderItem = (Button)findViewById(R.id.button_orderItem);
 	    orderItem.setOnClickListener(new View.OnClickListener() {	
 			@Override
 			public void onClick(View v) {
 				Toast ordered = Toast.makeText(Ordering2.this, "You ordered " + selectedItem + "!", Toast.LENGTH_SHORT);
 				ordered.show();
 				Log.v("SDF",selectedItem);
 				if(Orders.containsKey(selectedItem))
 				{
 					Log.v("SDF","GdfsdfGGGG");
 					Orders.put(selectedItem, Orders.get(selectedItem)+1);
 					
 				}
 				else
 				{
 					Log.v("SDF","GGgggggg");
 					Orders.put(selectedItem, 1);
 				}Log.v("SDF","GGGGGG");
 				totalPrice += Prices.get(selectedItem);
 				Log.v("SDF", String.valueOf(totalPrice));

 			}	
 		});

 	   Button next= (Button)findViewById(R.id.button_toConfirmPage);
	    next.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Orders order = new Orders();
				order.setR_id(R_id);
				String orderTmp = "name" + ";" + time + ";" + partySize + ";" + tableLocation;
 				for(Map.Entry<String, Integer> entry: Orders.entrySet())
 				{
 					orderTmp += ";" + entry.getKey() + ":" + entry.getValue();
 					
 				}
 				Log.v("SDF",orderTmp);
				order.setOrder(orderTmp);
				order.saveAsync(new AsyncCallback<Orders>() {

					@Override
					public void handleFault(BackendlessFault fault) {
						Toast fail= Toast.makeText(Ordering2.this,"Ordering Failed. Please Try Again.", Toast.LENGTH_SHORT);
		 				fail.show();
						// TODO Auto-generated method stub
						
					}

					@Override
					public void handleResponse(Orders response) {
						// TODO Auto-generated method stub
						
					}
					
				});
				
			}
				 
				
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ordering2, menu);
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
	private void populateCategory(){
		String whereClause = "R_id = '" + R_id + "'";
		BackendlessDataQuery dataQuery = new BackendlessDataQuery();
		dataQuery.setWhereClause( whereClause );
		Restaurant.findAsync( dataQuery, new AsyncCallback<BackendlessCollection<Restaurant>>(){		
				@Override
				 public void handleResponse(BackendlessCollection<Restaurant> response )
				 { 
					  List<Restaurant> lr = response.getData();
					  if(lr.size() < 1){ 
						  return;
					  }
					  categories = lr.get(0).getMenuCategories().split(";");			 
					  ArrayAdapter<String> adapter;
					  adapter = new ArrayAdapter<String>(
							Ordering2.this,		 			//COntext for this activity
							R.layout.restaurat_list, //Layout to be use(create)
							categories);				//Items to be displayed
						
					  ListView list = (ListView)findViewById(R.id.list_menuCategories); 
					  list.setAdapter(adapter);
					  registerClickCallback();
					  populateItem();
				 }
				@Override
				public void handleFault(BackendlessFault fault) { // does nothing but auto override 
					// TODO Auto-generated method stub
					  return;
				}
		});
	}
	private void populateItem(){
			Log.v("f","f");
	}
	private void registerClickCallback() {
		final ListView list = (ListView)findViewById(R.id.list_menuCategories);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {
				TextView selected = (TextView)viewClicked;
				
				TextView selectedCategory = (TextView)findViewById(R.id.itemPrompt);
				selectedCategory.setText("Select an item from " + selected.getText());
				
				//Get Items corresponding to the selected Category
				String whereClause = "R_id = '" +R_id + "' AND Category = '" + selected.getText().toString() + "'";
				Log.v("SDF", whereClause);
				BackendlessDataQuery dataQuery = new BackendlessDataQuery();
				dataQuery.setWhereClause( whereClause );
				Dish.findAsync( dataQuery, new AsyncCallback<BackendlessCollection<Dish>>(){		
						@Override
						public void handleResponse(BackendlessCollection<Dish> response )
						{
							
							List<Dish> lr = response.getData();
							if(lr.size() < 1){ 
								Log.v("SDF","GGGG");
								return;
							}
							items = new String[lr.size()];
							for(int i = 0, size = lr.size(); i < size; i++)
							{
								items[i] = new String(lr.get(i).getName() + " ($"+lr.get(i).getPrice()+" )");
								Prices.put(items[i], lr.get(i).getPrice());
							}
							  ArrayAdapter<String> adapter;
							  adapter = new ArrayAdapter<String>(
									Ordering2.this,		 			//COntext for this activity
									R.layout.restaurat_list, //Layout to be use(create)
									items);				//Items to be displayed
								
							  ListView list = (ListView)findViewById(R.id.list_menuItems); 
							  list.setAdapter(adapter);
							  registerClickCallback2();
							
						}
						@Override
						public void handleFault(BackendlessFault fault) { // does nothing but auto override 
							// TODO Auto-generated method stub
							return;
						}
				});
					// TODO Auto-generated method stub		
			}
		
		});
	}
	private void registerClickCallback2() {
		ListView list = (ListView)findViewById(R.id.list_menuItems);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {
					//TextView textView=(TextView)viewClicked;
				TextView selected = (TextView)viewClicked;
				selectedItem = selected.getText().toString();
				Log.v("SDF",selectedItem);	
				// TODO Auto-generated method stub
				
			}
		
		});
	}
}
