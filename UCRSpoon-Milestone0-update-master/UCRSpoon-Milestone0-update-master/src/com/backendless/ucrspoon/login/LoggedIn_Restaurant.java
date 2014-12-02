package com.backendless.ucrspoon.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.backendless.Backendless;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

public class LoggedIn_Restaurant extends Activity {

	String name = "Restaurant Name Unavailable";
	HashMap<String, List<String>> Movies_category;
	List<String> Movies_list;
	ExpandableListView Exp_list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logged_in__restaurant);
		
		//Retrieve extras
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			name = extras.getString("name");
		}
		final TextView title = (TextView)findViewById(R.id.listTitle);
		  Button Orders= (Button)findViewById(R.id.button_Orders);    // only records 
	      Orders.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {	
					title.setText("Orders");
				}
	      });
	      Button MSGs = (Button)findViewById(R.id.button_MSGs);    // only records 
	        MSGs.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {	
					title.setText("MSGs");
				}
				});
	       Button Menu = (Button)findViewById(R.id.button3);    // only records 
	       Menu.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), SetMenu.class);
					intent.putExtra("name", name);
					startActivity(intent);
				}
				});
		//Set Title
		getActionBar().setTitle(name);
		
			String[] hi = {"OMG","2","3"};
			ArrayAdapter<String> adapter;
			adapter = new ArrayAdapter<String>(
					LoggedIn_Restaurant.this,		 			//COntext for this activity
					R.layout.restaurat_list, //Layout to be use(create)
					hi);				//Items to be displayed
			ListView list = (ListView)findViewById(R.id.list_DishCategory);
			list.setAdapter(adapter);
			//registerClickCallback();

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

