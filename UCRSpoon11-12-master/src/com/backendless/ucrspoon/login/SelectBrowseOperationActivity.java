package com.backendless.ucrspoon.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.ucrspoon.data.*;
import com.backendless.ucrspoon.data.crud.common.DataApplication;
import com.backendless.ucrspoon.data.crud.common.DefaultCallback;
import com.backendless.ucrspoon.data.crud.common.Defaults;
import com.backendless.ucrspoon.data.crud.retrieve.RetrieveRecordActivity;
import com.backendless.ucrspoon.data.crud.retrieve.ShowByPropertyActivity;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SelectBrowseOperationActivity extends Activity
{

  private String property;
  private BackendlessCollection collection;
  private int currentPage;
  private String[] items = new String[10];

 // private ListView RView;
  private TextView RView_1;
  private TextView RView_2;
  private TextView RView_3;
  private TextView RView_4;
  
  
  
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.browse_template );
    
    Button go_back_button = (Button)findViewById(R.id.GoBackButton);    // only records 
    go_back_button.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {	
			if(Backendless.UserService.CurrentUser() != null)  // if User logged in
			{
				startActivity (new Intent(v.getContext(), UserPage.class));   // goes to MainPage
			}
			else 
			{
				startActivity (new Intent(v.getContext(), MainPage.class));   // goes UserPage
			}
		// TODO Auto-generated method stub
		}
	}); // Register the onClick listener with the implementation above
    

    Backendless.setUrl( Defaults.SERVER_URL );
    Backendless.initApp( getBaseContext(), Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );
//	DataApplication dataApplication = (DataApplication) getApplication();
 //   table = dataApplication.getChosenTable();


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
      //went here...
    BackendlessDataQuery query = new BackendlessDataQuery();
    query.setWhereClause( "created > " + String.valueOf( oneDayAgo.getTime() ) );
    
    RView_1 = (TextView) findViewById( R.id.RText_1 );
    RView_2 = (TextView) findViewById( R.id.RText_2 );
    RView_3 = (TextView) findViewById( R.id.RText_3 );
    RView_4 = (TextView) findViewById( R.id.RText_4 );
    
    
    
    Intent intent = getIntent();
    property = intent.getStringExtra( "property" );

    //went here.....
    
    //items = new String[ collection.getCurrentPage().size() ];
    
    //Restaurant.findAsync( query, new DefaultCallback<BackendlessCollection<Restaurant>>( SelectBrowseOperationActivity.this )
    Backendless.Persistence.of( Restaurant.class).find( new AsyncCallback<BackendlessCollection<Restaurant>>()
    		{
      @Override
      public void handleResponse(BackendlessCollection<Restaurant> response)
      {
        //super.handleResponse( response ); 
        // **********  things i added  **********
        collection = RetrieveRecordActivity.getResultCollection();
        currentPage = 1;
        
        Restaurant firstRestaurant = response.getCurrentPage().get( 0 );
        Restaurant secondRestaurant = response.getCurrentPage().get( 1 );
        Restaurant thirdRestaurant = response.getCurrentPage().get( 2 );
        Restaurant forthRestaurant = response.getCurrentPage().get(3);
        Restaurant fifthRestaurant = response.getCurrentPage().get(4);



//***************************/***************************/***************************
        List<Restaurant> lr = response.getData();
        
        if(lr.size() < 1){     
        	Toast.makeText(getApplicationContext(), "No restaurants", Toast.LENGTH_LONG).show();
         	finish();
        }

        ////////
        
        if( property.equals( "CuisineType" ) )
        {

          for( int i = 0; i < lr.size(); i++ )
          {

            items[ i ] = String.valueOf( ((Restaurant) response.getCurrentPage().get( i )).getCuisineType() );
          }
        }
     
        else if( property.equals( "Restaurant name" ) )
        {

          for( int i = 0; i < lr.size(); i++ )
          {
            items[ i ] = String.valueOf( ((Restaurant) response.getCurrentPage().get( i )).getRname() );
          }
        } 
       
        else if( property.equals( "Rating" ) )
        {


          for( int i = 0; i < lr.size(); i++ )
          {
            items[ i ] = String.valueOf( ((Restaurant) response.getCurrentPage().get( i )).getRating() );
          }
        }

        else if( property.equals( "AvgPrice" ) )
        {

          for( int i = 0; i < lr.size(); i++ )
          {
            items[ i ] = String.valueOf( ((Restaurant) response.getCurrentPage().get( i )).getAvgPrice() );
          }
        }
        
        if(!property.equals("Location"))
        {
        	RView_1.setText( property + ": "+ items[0] + "\n" + "Name: "+firstRestaurant.getRname().toString()+
					  "\n"+"Cuisine Type: "+firstRestaurant.getCuisineType().toString()+"\n"+
					 "Description: " + firstRestaurant.getDescription() +  "\n" );

        	RView_2.setText( property + ": "+ items[1] + "\n" + "Name: "+secondRestaurant.getRname().toString()+
					 "\n"+"Cuisine Type: "+secondRestaurant.getCuisineType().toString()+"\n"+
					 "Description: " + secondRestaurant.getDescription() +  "\n" );
	
        	RView_3.setText( property + ": "+ items[2] + "\n" + "Name: "+thirdRestaurant.getRname().toString()+
					 "\n"+"Cuisine Type: "+thirdRestaurant.getCuisineType().toString()+"\n"+
					 "Description: " + thirdRestaurant.getDescription() +  "\n" );
        	
        	RView_4.setText( property + ": "+ items[3] + "\n" + "Name: "+forthRestaurant.getRname().toString()+
					 "\n"+"Cuisine Type: "+forthRestaurant.getCuisineType().toString()+"\n"+
					 "Description: " + forthRestaurant.getDescription() +  "\n" );
        	
        }
        else if ( property.equals( "Location" ) )
        {
        	items[ 0 ] = firstRestaurant.getLatitude().toString() ;
        	RView_1.setText("Location: "+ items[0] + "\n"  +
        					"Name: "+firstRestaurant.getRname().toString()+"\n"+
        					"Cuisine Type: "+firstRestaurant.getCuisineType().toString()+"\n"+
        					"Description: " + firstRestaurant.getDescription() +  "\n"  +
        					"Rating: "+ firstRestaurant.getRating().toString());

        	items[ 1 ] = secondRestaurant.getLatitude().toString() ;
        	RView_2.setText( "Location: "+ items[1] + "\n"  +
							 "Name: "+secondRestaurant.getRname().toString()+"\n"+
							 "Cuisine Type: "+secondRestaurant.getCuisineType().toString()+"\n"+
							 "Description: " + secondRestaurant.getDescription() +  "\n"  +
							 "Rating: "+ secondRestaurant.getRating().toString());
        	
        	items[ 2 ] = thirdRestaurant.getLatitude().toString();
        	RView_3.setText( "Location: "+ items[2] + "\n"  +
							 "Name: "+thirdRestaurant.getRname().toString()+"\n"+
							 "Cuisine Type: "+thirdRestaurant.getCuisineType().toString()+"\n"+
							 "Description: " + thirdRestaurant.getDescription() +  "\n"  +
							 "Rating: "+ thirdRestaurant.getRating().toString());
        	
        	items[ 4 ] = fifthRestaurant.getLatitude().toString();
        	RView_4.setText( "Location: "+ items[4] + "\n"  +
							 "Name: "+fifthRestaurant.getRname().toString()+"\n"+
							 "Cuisine Type: "+fifthRestaurant.getCuisineType().toString()+"\n"+
							 "Description: " + fifthRestaurant.getDescription() +  "\n"  +
							 "Rating: "+ fifthRestaurant.getRating().toString());
        	
        	
          //for( int i = 0; i < collection.getCurrentPage().size(); i++ )
          //{
            //items[ i ] = String.valueOf( ((Restaurant) collection.getCurrentPage().get( i )).getLatitude() );
          //}
        }

     }

	@Override
	public void handleFault(BackendlessFault arg0) {
		// TODO Auto-generated method stub
		
	}

    } );
        /////// displays results in a list template... 
	//	ListAdapter adapter = new ArrayAdapter(getBaseContext(), R.layout.browse_template, R.id.RList, items);
       // RView.setAdapter( adapter );
    }
    
  
}