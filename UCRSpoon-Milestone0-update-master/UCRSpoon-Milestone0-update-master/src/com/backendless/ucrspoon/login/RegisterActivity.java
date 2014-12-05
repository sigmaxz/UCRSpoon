package com.backendless.ucrspoon.login;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.ucrspoon.data.UsersName;
import com.backendless.ucrspoon.data.UsersPopulation;
import com.backendless.ucrspoon.data.crud.common.DefaultCallback;
import com.backendless.ucrspoon.data.crud.common.Defaults;

public class RegisterActivity extends Activity
{
  private final static java.text.SimpleDateFormat SIMPLE_DATE_FORMAT = new java.text.SimpleDateFormat( "yyyy/MM/dd" );

  private EditText emailField;
  private EditText nameField;
  private EditText passwordField;

  private Button registerButton;

  private String email;
  private String name;
  private String password;
  private Integer max_id = -1;
  private UCRSpoonUser user;
  private UsersPopulation usersNumber;
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.register );
    //
    Backendless.setUrl( Defaults.SERVER_URL );
    //Backendless.initApp( getBaseContext(), Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );
	 BackendlessDataQuery query = new BackendlessDataQuery();	 
	 UsersPopulation.findAsync( query, new DefaultCallback<BackendlessCollection<UsersPopulation>>( RegisterActivity.this )
	 {
	   @Override
	   public void handleResponse( BackendlessCollection<UsersPopulation> response )
	   {
	     super.handleResponse( response );
	     List<UsersPopulation>lr = response.getCurrentPage();
	     System.out.println(lr.size());
	     if(lr.size()<=0)
	     {
	    	return;
	     }
	     usersNumber = response.getCurrentPage().get( 0 );
	     System.out.println("####################################################");
		 max_id = usersNumber.getPopulation();
	     System.out.println("*****************************************"+max_id);
	     /*
	     usersNumber.setPopulation(max_id+1);
	     usersNumber.saveAsync(new DefaultCallback<UsersPopulation>(RegisterActivity.this)
		{
				@Override
			    public void handleResponse( UsersPopulation response )
				 {
					 showToast("rating updated");
					 //startActivity(i);
					 finish();
				 }
		 } );*/
	 
	     initUI();
	   }
	 } );
    //
  }


  private void initUI()
  {
	
	emailField = (EditText) findViewById( R.id.emailField );nameField = (EditText) findViewById( R.id.nameField );passwordField = (EditText) findViewById( R.id.passwordField );

    registerButton = (Button) findViewById( R.id.registerButton );

    registerButton.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        onRegisterButtonClicked();
      }
    } );
  }

  private void onRegisterButtonClicked()
  {
    String emailText = emailField.getText().toString().trim();
    String nameText = nameField.getText().toString().trim();
    String passwordText = passwordField.getText().toString().trim();

    if ( emailText.isEmpty() )
    {
      showToast( "Field 'email' cannot be empty." );
      return;
    }

    if ( passwordText.isEmpty() )
    {
      showToast( "Field 'password' cannot be empty." );
      return;
    }

    if( !emailText.isEmpty() )
    {
      email = emailText;
    }

    if( !nameText.isEmpty() )
    {
      name = nameText;
    }

    if( !passwordText.isEmpty() )
    {
      password = passwordText;
    }

    user = new UCRSpoonUser();

    if( email != null )
    {
      user.setEmail( email );
    }

    if( name != null )
    {
      user.setName( name );
    }

    if( password != null )
    {
      user.setPassword( password );
    }
    if(max_id != -1)
    {
    	user.setUserID(max_id);
    }
    Backendless.Persistence.save( usersNumber, new AsyncCallback<UsersPopulation>() {
	    public void handleResponse( UsersPopulation response )
	    {
	      response.setPopulation(max_id+1);
	 
	      Backendless.Persistence.save( response, new AsyncCallback<UsersPopulation>() {
	        @Override
	        public void handleResponse( UsersPopulation response )
	        {
	          // Contact instance has been updated
	        }
	        @Override
	        public void handleFault( BackendlessFault fault )
	        {
	          // an error has occurred, the error code can be retrieved with fault.getCode()
	        }
	      } );
	    }
	    @Override
	    public void handleFault( BackendlessFault fault )
	    {
	      // an error has occurred, the error code can be retrieved with fault.getCode()
	    }
	  });
    UsersName newUser = new UsersName();
    newUser.setEmail(email);
    newUser.setID(max_id);
    newUser.setName(name);
    Backendless.Persistence.save( newUser, new AsyncCallback<UsersName>() {
        public void handleResponse( UsersName response )
        {
        	// new Contact instance has been saved
        }
        
        public void handleFault( BackendlessFault fault )
        {
          // an error has occurred, the error code can be retrieved with fault.getCode()
        }
      });
    Backendless.UserService.register( user, new DefaultCallback<BackendlessUser>( RegisterActivity.this )
    {
      @Override
      public void handleResponse( BackendlessUser response )
      {
        super.handleResponse( response );
        Intent intent = new Intent(RegisterActivity.this, RegistrationSuccessActivity.class);
		intent.putExtra("max_id",(Integer)max_id);
        startActivity( intent);
        finish();
      }
    } );
  }

  private void showToast( String msg )
  {
    Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
  }
}
