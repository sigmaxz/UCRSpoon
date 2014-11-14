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
	
	private TextView text_based_search;
	  private EditText search_field;
	  private Button searchButton, logout_button, browse_button;

	 public void onCreate( Bundle savedInstanceState )
	  {
	    super.onCreate( savedInstanceState );
	    setContentView( R.layout.user_page);
	    

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
     
	  }   
	  
        public void onLogoutButtonClicked()   // log out button clicked
        {
          Backendless.UserService.logout( new DefaultCallback<Void>( this )
          {
            @Override
            public void handleResponse( Void response )
            {
              startActivity( new Intent(UserPage.this, MainPage.class ) );
              finish();
            }
          } );    
        }
	    
	    private void onBrowseButtonClicked()   // browse button clicked
	    {
	    	//went here 
				startActivity (new Intent(UserPage.this, Browse.class)); 
				finish();	
				// TODO Auto-generated method stub
	    }
	    
	    // more buttons to implement
        
    
        
        
}
