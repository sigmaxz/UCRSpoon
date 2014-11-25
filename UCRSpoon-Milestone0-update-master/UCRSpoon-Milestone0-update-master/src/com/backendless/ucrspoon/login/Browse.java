package com.backendless.ucrspoon.login;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class Browse extends Activity
{
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.browse );

    initUI();
  }

  private void initUI()
  {
	  
	  AlertDialog.Builder builder = new AlertDialog.Builder( Browse.this );
      builder.setTitle( "Browse by:" );
      final String[] properties = { "All Restaurants","CuisineType", "Rating","AvgPrice", "Location","Review","Dish" };
      builder.setItems( properties, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick( DialogInterface dialogInterface, int i )
        {
          Intent nextIntent = new Intent(Browse.this, SelectBrowseOperationActivity.class );
          nextIntent.putExtra( "property", properties[ i ] );
          startActivity( nextIntent );
          dialogInterface.cancel();
        }
      } );
      builder.create().show();
  }

}