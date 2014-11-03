package com.backendless.ucrspoon.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.backendless.Backendless;

public class LoginSuccessActivity extends Activity
{
  private Button backButton;

  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.login_success );

    initUI();
  }

  private void initUI()
  {
    backButton = (Button) findViewById( R.id.backButton );

    backButton.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        onLogoutButtonClicked();
      }
    } );
  }

  private void onLogoutButtonClicked()
  {
    Backendless.UserService.logout( new DefaultCallback<Void>( this )
    {
      @Override
      public void handleResponse( Void response )
      {
        startActivity( new Intent( LoginSuccessActivity.this, MainPage.class ) );
        finish();
      }
    } );

  }
}