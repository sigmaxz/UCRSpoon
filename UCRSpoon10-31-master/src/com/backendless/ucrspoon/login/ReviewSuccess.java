package com.backendless.ucrspoon.login;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReviewSuccess extends Activity
{
  private TextView messageView;
  private Button backButton;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.review_success );

    initUI();
  }

  private void initUI()
  {
    messageView = (TextView) findViewById( R.id.rsview );
    backButton = (Button) findViewById( R.id.backButton );
                                                    

    Resources resources = getResources();
    String message = String.format( resources.getString( R.string.review_success ), resources.getString( R.string.app_name ) );
    messageView.setText( message );

    backButton.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        onBackButtonClicked();
      }
    } );
  }

  public void onBackButtonClicked()
  {
    //startActivity( new Intent( this, LoginActivity.class ) );
    finish();
  }
}
