package com.backendless.ucrspoon.login;


import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainPage extends Activity {
		
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main_pages);
			Backendless.setUrl( Defaults.SERVER_URL ); // in case you didn't already do the init
			
			if(Backendless.UserService.CurrentUser() == null)  // if its already logged in
			{
			    Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );
			}
			
			

	        Button login_button = (Button)findViewById(R.id.login_button);    // only records 
	        login_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {	
					if(Backendless.UserService.CurrentUser() != null)  // if its already logged in
					{
						Intent i = new Intent(v.getContext(), UserPage.class);
						startActivity(i);
						
					}
					else
					{
						startActivity (new Intent(v.getContext(), LoginActivity.class));   // login is pressed 
						
					}
				}
			}); // Register the onClick listener with the implementation above
	         	       
	         Button register_button = (Button)findViewById(R.id.register_button);    // only records 
	         register_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {				
				startActivity (new Intent(v.getContext(), RegisterActivity.class));   // goes to MyCamera.java
				// TODO Auto-generated method stub
				}
			});
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
		 }
		 private void onSearchButtonClicked()
	     {
	     	Intent searchIntent = new Intent(MainPage.this, SearchInput.class);
	     	startActivity(searchIntent);	     	
	     }
}
