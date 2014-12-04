package com.backendless.ucrspoon.login;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.ucrspoon.data.crud.common.Defaults;

public class FriendList extends Activity{
	
	//private List<Restaurant> result= new ArrayList<Restaurant>();
    private List<String> StringArray = new ArrayList<String>();
    String friends_id;
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
		if(friends_id == null)
		{
			Toast.makeText(getApplicationContext(),"friends not found",
   				 Toast.LENGTH_LONG).show();
			return;
		}
		System.out.println("1##########################"+friends_id);
		StringArray = Arrays.asList(friends_id.split(","));
		displayList();
		
		
	}
	private void displayList()
	{
		ArrayAdapter<String> adapter = 
				new ArrayAdapter<String>(FriendList.this,		 			//COntext for this activity
										 R.layout.friend_item, //Layout to be use(create)
										 StringArray);
		ListView view = (ListView)findViewById(R.id.friendListView);
		view.setAdapter(adapter);
		//active_list_button();
	}
}
	