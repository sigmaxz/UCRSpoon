package com.backendless.ucrspoon.login;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.ucrspoon.data.UsersName;
import com.backendless.ucrspoon.data.UsersPopulation;
import com.backendless.ucrspoon.data.crud.common.DefaultCallback;

public class FriendList extends Activity{
	
	//private List<Restaurant> result= new ArrayList<Restaurant>();
    private List<String> StringArray = new ArrayList<String>();
    private List<String> NameArray = new ArrayList<String>();
    String friends_id, user_name;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_list);
		
		
	    
	    search_friends();
	    
	}
	
	private void search_friends()
	{
		BackendlessUser user = Backendless.UserService.CurrentUser();
		friends_id = (String) user.getProperty("Friends");
		user_name = (String) user.getProperty("name");
		if(friends_id == null)
		{
			Toast.makeText(getApplicationContext(),"friends not found",
   				 Toast.LENGTH_LONG).show();
			finish();
		}
		else
		{
			System.out.println("1##########################"+friends_id);
			StringArray = Arrays.asList(friends_id.split(","));
			
			BackendlessDataQuery query = new BackendlessDataQuery();
				//query.setWhereClause("ID = "+s);
				//System.out.println("##########################"+s);
			UsersName.findAsync( query, new DefaultCallback<BackendlessCollection<UsersName>>( FriendList.this ){
				@Override
				public void handleResponse( BackendlessCollection<UsersName> response )
				{
					super.handleResponse( response );
				    List<UsersName>lr = response.getCurrentPage();
				    if(lr.size()>0)
				    {
				    	for(int i = 0; i < lr.size(); i++)
				    	{
				    		for(int j = 0; j < StringArray.size(); j++)
				    		{
				    			if(lr.get(i).getID() == Integer.parseInt(StringArray.get(j)))
				    			{
				    				NameArray.add(lr.get(i).getName());
				    			}
				    		}
				    	}
				    }
				    else
				    {
				    	Toast.makeText(getApplicationContext(),"friends not found",
			    				 Toast.LENGTH_LONG).show();
				    }
				    
				    if(NameArray.size() > 0)
				    {
				    	displayList();
				    }
				    
				    
				    
				}
				@Override
				public void handleFault(BackendlessFault fault) { 
					// TODO Auto-generated method stub
					  return;
				}
			});
			
			displayList();
		}
		
	}
	private void displayList()
	{
		ArrayAdapter<String> adapter = 
				new ArrayAdapter<String>(FriendList.this,		 			//COntext for this activity
										 R.layout.friend_item, //Layout to be use(create)
										 NameArray);
		ListView view = (ListView)findViewById(R.id.friendListView);
		view.setAdapter(adapter);
		active_list_button();
	}
	
	private void active_list_button()
	{
		ListView list = (ListView)findViewById(R.id.friendListView);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id){
				TextView text1 = (TextView)viewClicked;
				//Toast.makeText(getApplicationContext(),StringArray.get(position),
	    		//		 Toast.LENGTH_LONG).show();
				
				Intent intent = new Intent(FriendList.this, MessageActivity.class);
				String taken = text1.getText().toString();
				intent.putExtra("name",user_name);
				intent.putExtra("recipient", taken);
				//Toast.makeText(NearbyRestaurant.this, separated[2], Toast.LENGTH_LONG).show();
				startActivity (intent);
					
				
			}
		});
	}
}
	