package com.backendless.ucrspoon.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

public class UserPage extends Activity {
	
	  private Button searchButton, logout_button, browse_button, order;
	  String name;
	 public void onCreate( Bundle savedInstanceState )
	  {
	    super.onCreate( savedInstanceState );
	    setContentView( R.layout.user_page);
	    
	    Bundle extras = getIntent().getExtras();
	   
		if(extras != null) {
			name = extras.getString("name");
		}
		TextView displayUserName = (TextView)findViewById(R.id.displayUserName);
		displayUserName.setText(name);
		
	    logout_button = (Button)findViewById(R.id.LogOutButton);    // only records 
        logout_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				onLogoutButtonClicked();
			// TODO Auto-generated method stub
			
			}
		}); // Logout the onClick listener with the implementation above
        
        browse_button = (Button)findViewById(R.id.BrowseButton);
        browse_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {	
				onBrowseButtonClicked();
			// TODO Auto-generated method stub
			
			}
		}); // Logout the onClick listener with the implementation above
   
        
        Button button1 = (Button)findViewById(R.id.NearByButton);    // only records 
        button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
			startActivity (new Intent(v.getContext(), NearbyRestaurant.class));   // goes to MyCamera.java
				// TODO Auto-generated method stub
			}
		}); // Register the onClick listener with the implementation above
        Button searchButton = (Button)findViewById(R.id.SearchButton);
 	    searchButton.setOnClickListener(new View.OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				onSearchButtonClicked();
 			}	
 		});
	  
        
	  }   
	  
        public void onLogoutButtonClicked()   // log out button clicked
        {
          Backendless.UserService.logout( new DefaultCallback<Void>( this )
          {
            @Override
            public void handleResponse( Void response )
            {
              startActivity( new Intent(UserPage.this, MainPage.class ) );
            }
          } );    
        }
	    
	    private void onBrowseButtonClicked()   // browse button clicked
	    {
				startActivity (new Intent(UserPage.this, Browse.class)); 
				// TODO Auto-generated method stub
	    }
	    
	    // more buttons to implement
	    
	    private void onSearchButtonClicked()
	    {
	    	Intent searchIntent = new Intent(UserPage.this, SearchInput.class);
	    	startActivity(searchIntent);	    	
	    }

}
