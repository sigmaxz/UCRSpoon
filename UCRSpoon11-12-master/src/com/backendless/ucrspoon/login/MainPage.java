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
				finish();	
				// TODO Auto-generated method stub
				}
			}); // Register the onClick listener with the implementation above
	         
	         Button near_by_button = (Button)findViewById(R.id.NearByButton);    // only records 
	         near_by_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {				
				//startActivity (new Intent(v.getContext(), Browse.class));   // goes to MyCamera.java
				//finish();	
				// TODO Auto-generated method stub
				}
			}); // Register the onClick listener with the implementation above
	         
	         Button review_button = (Button)findViewById(R.id.review_button);
	         review_button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				startActivity (new Intent(v.getContext(), ReviewActivity.class));
					// TODO Auto-generated method stub
					
				}
			});
	        
	        
	        
	 }
	
	
	
	

}
