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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.ucrspoon.data.UsersName;
import com.backendless.ucrspoon.data.crud.common.DefaultCallback;

public class FriendList extends Activity{
	
	//private List<Restaurant> result= new ArrayList<Restaurant>();
    private List<String> StringArray = new ArrayList<String>();
    private List<String> NameArray = new ArrayList<String>();
    private List<UsersName>lr = new ArrayList<UsersName>();
    String friends_id, user_name;
    private EditText friendNameField;
    private BackendlessUser user;
    private String new_friends_id;
    private Integer current_user_id;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_list);
		
		
	    
	    search_friends();
	    
	}
	
	private void search_friends()
	{
		user = Backendless.UserService.CurrentUser();
		friends_id = (String) user.getProperty("Friends");
		user_name = (String) user.getProperty("name");
		current_user_id = (Integer)user.getProperty("userID");
		if(friends_id != null)
		{
			StringArray = Arrays.asList(friends_id.split(","));
			
			BackendlessDataQuery query = new BackendlessDataQuery();
				//query.setWhereClause("ID = "+s);
			System.out.println("##########################");
			UsersName.findAsync( query, new DefaultCallback<BackendlessCollection<UsersName>>( FriendList.this ){
				@Override
				public void handleResponse( BackendlessCollection<UsersName> response )
				{
					super.handleResponse( response );
				    lr = response.getCurrentPage();
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
				    
				    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   1");
				    displayList();
				    
				    
				    
				    
				}
				@Override
				public void handleFault(BackendlessFault fault) { 
					// TODO Auto-generated method stub
					  return;
				}
			});
		}
		else
		{
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   1");
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
		Button AddFriendButton = (Button)findViewById(R.id.add);
		   AddFriendButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onAddFriendClicked();
				}	
			});
	        
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
	public void onAddFriendClicked()
	  {
		friendNameField = (EditText)findViewById(R.id.addFriendField);
		String Name = friendNameField.getText().toString().trim();
		if(Name.isEmpty())
		{
			Toast.makeText(getApplicationContext(),"input field cannot be empty",
   				 Toast.LENGTH_LONG).show();
		    return;
		}
		
		String whereClause = "name = '"+ Name + "'"; 
		 BackendlessDataQuery dataQuery = new BackendlessDataQuery();
		 dataQuery.setWhereClause( whereClause );
		 UsersName.findAsync( dataQuery, new AsyncCallback<BackendlessCollection<UsersName>>() 
		 {
				@Override
				public void handleResponse( BackendlessCollection<UsersName> response )
				{
					System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   2");
					List<UsersName> lr = response.getData();
					if(lr.size() < 1){
						Toast.makeText(getApplicationContext(),"name not found",
				   				 Toast.LENGTH_LONG).show();
						  return;
					  }
					UsersName friendName = response.getCurrentPage().get( 0 );
					if(friendName.getName().isEmpty())
					{
						Toast.makeText(getApplicationContext(),"name not found",
				   				 Toast.LENGTH_LONG).show();
						return;
					}
					else
					{
						if(current_user_id == (Integer)friendName.getID())
						{
							Toast.makeText(getApplicationContext(),"you cant add youself",
					   				 Toast.LENGTH_LONG).show();
							return;
						}
						for(int i = 0; i < StringArray.size(); i++)
						{
							if(Integer.parseInt(StringArray.get(i)) == (Integer)friendName.getID())
							{
								Toast.makeText(getApplicationContext(),"already in friends list",
						   				 Toast.LENGTH_LONG).show();
								return;
							}
							
							
						}
						
						if(friends_id == null)
						{System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   5");
							new_friends_id = friendName.getID().toString();
							
						}
						else
						{System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   6");
							new_friends_id = friends_id.trim() + "," + friendName.getID();
							
						}
						user.setProperty("Friends", new_friends_id);
						Backendless.UserService.update( user, new AsyncCallback<BackendlessUser>() 
						{
							public void handleResponse( BackendlessUser user )
							{
								Toast.makeText(getApplicationContext(),"add friend successfully",
						   				 Toast.LENGTH_LONG).show();
								       // user has been updated
							}

							public void handleFault( BackendlessFault fault )
							{
								Toast.makeText(getApplicationContext(),"fail to add friend",
						   				 Toast.LENGTH_LONG).show();
								       // user update failed, to get the error code call fault.getCode()
							 }
						});
						
					}
					
				}
				@Override
				public void handleFault(BackendlessFault fault) { 
					// TODO Auto-generated method stub
					  return;
				}
				
		 });
		 
						
	  }
}
	