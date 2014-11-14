package com.backendless.ucrspoon.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;

public class LoginSuccessActivity extends Activity
{

  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.login_success );
    
    Thread timer = new Thread()  // go to UserPage after 2 sec...
    {
    	public void run()
    	{
    			try 
    			{
    				sleep(1000);
    			}
    			catch(InterruptedException e)
    			{
    				e.printStackTrace();
    			}
    			finally
    			{
    				Intent i = new Intent(getBaseContext(), UserPage.class);  
    				startActivity(i);
    				finish();
    			}
    	}	
    };
    timer.start();
  }}