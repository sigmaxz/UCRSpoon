package com.backendless.ucrspoon.login;

import java.util.ArrayList;
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
import com.backendless.ucrspoon.data.Message;
import com.backendless.ucrspoon.data.UsersName;
import com.backendless.ucrspoon.data.crud.common.DefaultCallback;

public class InboxPage extends Activity{
	
	private String user_name;
	private List<String> StringArray = new ArrayList<String>();
	private List<Message>lr = new ArrayList<Message>();
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox);
		
		
	    
	    search_messages();
	    
	}
	
	private void search_messages()
	{
		BackendlessUser user = Backendless.UserService.CurrentUser();
		user_name = (String) user.getProperty("name");
		System.out.println("##########################   :"+user_name);
		if(user_name == null)
		{
			Toast.makeText(getApplicationContext(),"error",
	   				 Toast.LENGTH_LONG).show();
				finish();
		}
		else
		{
			BackendlessDataQuery query = new BackendlessDataQuery();
		    Message.findAsync( query, new DefaultCallback<BackendlessCollection<Message>>( InboxPage.this ){
			   public void handleResponse( BackendlessCollection<Message> response )
				{
					super.handleResponse( response );
				    lr = response.getCurrentPage();
				    if(lr.size()>0)
				    {
				    	for(int i = 0; i < lr.size(); i++)
				    	{
				    		System.out.println("##########################   :"+lr.get(i).getRecipient()+"'");
				    		if(lr.get(i).getRecipient().endsWith(user_name))
				    		{
				    			System.out.println("##########################   adddddddddddddddd");
				    			StringArray.add("From: "+lr.get(i).getSender()+"\n"+
				    			                "Subject: "+lr.get(i).getSubject()+"\n"+
				    					        "Date: " + lr.get(i).getCreated().toString()+"\n"+
				    					        "Message: "+lr.get(i).getMessage()+"\n");
				    		}
				    	}
				    }
				    else
				    {
				    	Toast.makeText(getApplicationContext(),"messages not found",
			    				 Toast.LENGTH_LONG).show();
				    	finish();
				    }
				    System.out.println("##########################   :"+StringArray.size()+"'");
				    if(StringArray.size() > 0)
				    {
				    	displayList();
				    }
				    else
				    {
				    	Toast.makeText(getApplicationContext(),"messages not found",
			    				 Toast.LENGTH_LONG).show();
				    	finish();
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
	private void displayList()
		{
			ArrayAdapter<String> adapter = 
					new ArrayAdapter<String>(InboxPage.this,		 			//COntext for this activity
											 R.layout.inbox_item, //Layout to be use(create)
											 StringArray);
			ListView view = (ListView)findViewById(R.id.inboxListView);
			view.setAdapter(adapter);
		}		
	
}