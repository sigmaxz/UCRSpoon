package com.backendless.ucrspoon.login;


import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainPage extends Activity {
	
	//String currentUserId = Backendless.UserService.loggedInUser();   // get the current user ID 
	
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main_pages);
	        //
	        Button login_button = (Button)findViewById(R.id.login_button);    // only records 
	         login_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {	
					if(Backendless.UserService.CurrentUser() != null)  // if its already logged in
					{
						Intent i = new Intent(v.getContext(), UserPage.class);
						startActivity(i);
						//finish();
					}
					else
					{
						startActivity (new Intent(v.getContext(), LoginActivity.class));   // login is pressed 
						//finish();
					}
				}
			}); // Register the onClick listener with the implementation above
	         
	        
	         Button browse_button = (Button)findViewById(R.id.BrowseButton);    // only records 
	         browse_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {				
				startActivity (new Intent(v.getContext(), Browse.class));   // goes to MyCamera.java
				// TODO Auto-generated method stub
				}
			}); // Register the onClick listener with the implementation above
	         
       
	         Button nearbyButton = (Button)findViewById(R.id.NearByButton);    // only records 
	         nearbyButton.setOnClickListener(new View.OnClickListener() {
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
		 	    
		 	   //Temporary button to make testing for Pre-Order easier
		 	    //Orders from abc
		 	 Button button1 = (Button)findViewById(R.id.button1);
		 	    button1.setOnClickListener(new View.OnClickListener() {	
		 			@Override
		 			public void onClick(View v) {
		 				Intent i = new Intent(v.getContext(), Odering.class);
		 				i.putExtra("name", "abc" );
		 				startActivity(i);
		 			}	
		 		});
		        
		 }
		
		 private void onSearchButtonClicked()
	     {
	     	Intent searchIntent = new Intent(MainPage.this, SearchInput.class);
	     	startActivity(searchIntent);	     	
	     }
	         
                 
	 
	
	

	

}
