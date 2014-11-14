package com.backendless.ucrspoon.login;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.ucrspoon.data.Restaurant;
import com.backendless.ucrspoon.data.crud.common.DefaultCallback;
import com.backendless.ucrspoon.data.crud.common.Defaults;

public class Browse extends Activity
{

  private static BackendlessCollection resultCollection;
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.browse );

    Backendless.setUrl( Defaults.SERVER_URL );
    Backendless.initApp( getBaseContext(), Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );

    initUI();
  }

  private void initUI()
  {
	  
	  retrieveBasicRestaurantRecord();
  }


  private void retrieveBasicRestaurantRecord()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime( new Date() );
    calendar.add( Calendar.DAY_OF_YEAR, -1 );
    Date oneDayAgo = calendar.getTime();
    BackendlessDataQuery query = new BackendlessDataQuery();
    query.setWhereClause( "created > " + String.valueOf( oneDayAgo.getTime() ) );
    Restaurant.findAsync( query, new DefaultCallback<BackendlessCollection<Restaurant>>( Browse.this )
    {
      @Override
      public void handleResponse( BackendlessCollection<Restaurant> response )
      {
        super.handleResponse( response );

        resultCollection = response;

        AlertDialog.Builder builder = new AlertDialog.Builder( Browse.this );
        builder.setTitle( "Browse by:" );
        final String[] properties = { "CuisineType","Restaurant name", "Rating","AvgPrice", "Location" };
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
    } );
  }
	  

	  
	  /*
    tablesListView = (ListView) findViewById( R.id.tablesList );

    String[] tables = new String[] { "Restaurant", "Rate", "Review", "Dish"};

    ArrayAdapter adapter = new ArrayAdapter<String>( this, R.layout.list_item_with_arrow, R.id.itemName, tables );
    tablesListView.setAdapter( adapter );

    tablesListView.setOnItemClickListener( new AdapterView.OnItemClickListener()
    {
      @Override
      public void onItemClick( AdapterView<?> adapterView, View view, int i, long l )
      {
        TextView tableNameView = (TextView) view.findViewById( R.id.itemName );
        DataApplication dataApplication = (DataApplication) getApplication();
        dataApplication.setChosenTable( tableNameView.getText().toString() );
        
        String chosenOperation = tableNameView.getText().toString();      
        if( chosenOperation.equals( "Restaurants" ) )
        {
          startActivity( new Intent(Browse.this, MainPage.class ) );
        }
        else if( chosenOperation.equals( "Rate" ) )
        {
          startActivity( new Intent(Browse.this, SelectBrowseOperationActivity.class ) );
        }
        else if( chosenOperation.equals( "Review" ) )
        {
          startActivity( new Intent(Browse.this, RetrieveRecordActivity.class ) );
        }
        else if( chosenOperation.equals( "Dishes" ) )
        {
          startActivity( new Intent(Browse.this, SelectRetrievalTypeActivity.class ) );
        }

      }
    } );  
    } */
}