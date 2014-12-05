package com.backendless.ucrspoon.login;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
  private List<String> StringArray = new ArrayList<String>();
  private String[] r_name = new String[10];
  private List<String> idArray = new ArrayList<String>();
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView(R.layout.search_list);
    
    Backendless.setUrl( Defaults.SERVER_URL );
    //Backendless.initApp( getBaseContext(), Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );
  
    initUI();
  }

  private void initUI()
  {	  	
    Calendar calendar = Calendar.getInstance();
    calendar.setTime( new Date() );
    calendar.add( Calendar.DAY_OF_YEAR, -1 );  // was -5.. and it worked..
    Date oneDayAgo = calendar.getTime();
      //went here...
    BackendlessDataQuery query = new BackendlessDataQuery();
    query.setWhereClause( "created < " + String.valueOf( oneDayAgo.getTime() ) );  // i swear i didnt change the inequality sign... wtf??!!!

    Intent intent = getIntent();
    property = intent.getStringExtra( "property" );

    //went here.....
    
    if( property.equals( "Review" ) )
    { 
    	/*
    	Restaurant.findAsync( query, new DefaultCallback<BackendlessCollection<Restaurant>>( SelectBrowseOperationActivity.this )
        {
    		@Override
    		public void handleResponse( BackendlessCollection<Restaurant> response )
    		{
    			super.handleResponse( response ); 
    			List<Restaurant>r_name_list = response.getData(); 
			
    			if(r_name_list.size() <1)
    			{
    				Toast.makeText(getApplicationContext(), "No Reviews", Toast.LENGTH_LONG).show();
    				finish();
    			}
    			else
    			{
    				for(int i =0; i<r_name_list.size(); ++i)
    				{
    					List<Review> review_list  = r_name_list.get(i).getReview();
    					if(review_list.isEmpty())
    					{}
    					else
    					{
    						for(int j =0; j < review_list.size(); ++j)
    						{
    							StringArray.add(r_name_list.get(i).getRname() + "\n\n"+
    											"Environment: " + review_list.get(j).getEnviornment() + "\n" +
    											"Service: " +review_list.get(j).getService() +"\n"+
    											"Dining Cost: $" + review_list.get(j).getDiningCost() + "\n");
    						}
    					}
    				}
    				displayList();
    			}
    		}});
    		*/
    	
    	
    	Review.findAsync( query, new DefaultCallback<BackendlessCollection<Review>>( SelectBrowseOperationActivity.this )
    			{
    		@Override
    		public void handleResponse( BackendlessCollection<Review> response )
    		{
    			super.handleResponse( response ); 
    			// **********  things i added  **********
	
    			List<Review> review_list = response.getData();
    	        
    			if(review_list.size() < 1){     
    				Toast.makeText(getApplicationContext(), "No Reviews", Toast.LENGTH_LONG).show();
    				finish();
    			}
    			else
    			{
    				Restaurant r_id;  
    				String rName;

        			for(int i =0; i< review_list.size(); ++i)
        			{
/*
        				BackendlessDataQuery Newquery = new BackendlessDataQuery();
        				Newquery.setWhereClause( "R_id = '"+ r_id + "'" );
        				
        			    Restaurant.findAsync( Newquery, new DefaultCallback<BackendlessCollection<Restaurant>>( SelectBrowseOperationActivity.this )
        			    {
        			    	@Override
        		    		public void handleResponse( BackendlessCollection<Restaurant> response )
        		    		{
        		    			super.handleResponse( response ); 
        		    			List<Restaurant>r_name_list = response.getData(); 
        		    			if(r_name_list.size() <1)
        		    			{
        		    				Toast.makeText(getApplicationContext(), "No Reviews", Toast.LENGTH_LONG).show();
        		    				finish();
        		    			}
        		    			else
        		    			{
        		    					r_name[counter]=r_name_list.get(counter).getRname().toString();	
        		    					StringArray.add(r_name[counter] + "\n\n" )
        		    					
        		    			}
        		    			counter++;
        		    		}});*/
        		    		
        			    StringArray.add("temp" + "\n\n"+
		 						"Environment: " + review_list.get(i).getEnviornment() + "\n" +
		 						"Service: " +review_list.get(i).getService() +"\n"+
		 						"Dining Cost: $" + review_list.get(i).getDiningCost() + "\n");
        			    idArray.add(review_list.get(i).getR_id().trim());
        				}
        				displayList();		
    			}
    		}});
    	
    }
    
    
    else if( property.equals( "Dish" ) )
    {
    	Dish.findAsync( query, new DefaultCallback<BackendlessCollection<Dish>>( SelectBrowseOperationActivity.this )
    			{
    		public void handleResponse( BackendlessCollection<Dish> response )
    		{
    			super.handleResponse( response ); 
    			// **********  things i added  **********
    			Dish firstRestaurant = response.getCurrentPage().get( 0 );
    			
    			List<Dish> dish_list = response.getData();
    	        
    			if(dish_list.size() < 1){     
    				Toast.makeText(getApplicationContext(), "No Dishes", Toast.LENGTH_LONG).show();
    				finish();
    			}
    			for(int i =0; i< dish_list.size(); ++i)
    			{

    			    StringArray.add("Dish: "+ dish_list.get(i).getName()+"\n\n");
    			}
    				displayList();				
    		}});
    	
    }
    
    else 
    {
    	Restaurant.findAsync( query, new DefaultCallback<BackendlessCollection<Restaurant>>( SelectBrowseOperationActivity.this )
    			{
    		@Override
    		public void handleResponse( BackendlessCollection<Restaurant> response )
    		{
    			super.handleResponse( response ); 
    			// **********  things i added  **********
    			collection = RetrieveRecordActivity.getResultCollection();
    			currentPage = 1;
        
    			Restaurant firstRestaurant = response.getCurrentPage().get( 0 );

//***************************/***************************/***************************
    			List<Restaurant> lr = response.getData();
        
    			if(lr.size() < 1){     
    				Toast.makeText(getApplicationContext(), "No restaurants", Toast.LENGTH_LONG).show();
    				finish();
    			}

    			if( property.equals( "CuisineType" ) )
    			{
    				final String[] cuisine_type = {"Mexican","Pizza","American","Japanese","Fastfood"};

    				for(int i =0; i<cuisine_type.length; ++i)
    				{
    					StringArray.add("\n\nCuisine Type: " + cuisine_type[i] + "\n\n");
    					for(int j = 0; j < lr.size(); ++j)
        				{
        					if(lr.get(j).getCuisineType().equals(cuisine_type[i]))
        					{
        						StringArray.add(lr.get(j).getRname() +"\n" +
        										"Description: " + lr.get(j).getDescription() + "\n" +
        										"Ratings: " + lr.get(j).getRating() + "\n" +
        										"Avg. Price: " + lr.get(j).getAvgPrice());
        						idArray.add(lr.get(i).getR_id().toString().trim());
        					}
        				}
    				}
    			
					displayList();
    			}
     
    			else if( property.equals( "All Restaurants" ) )
    			{
    				
    				for(int i =0; i< lr.size(); ++i)
    				{
    					StringArray.add(lr.get(i).getRname() +"\n\n" +
								"Description: " + lr.get(i).getDescription() + "\n" +
								"Cuisine Type: "+lr.get(i).getCuisineType() + "\n" +
								"Rating: " + lr.get(i).getRating() + "\n"+
								"Avg. Price: " + lr.get(i).getAvgPrice());
    					idArray.add(lr.get(i).getR_id().toString().trim());
    				}
					displayList();

    		    } 
       
    			else if( property.equals( "Rating" ) )
    			{
    				final int[] rating_array = {1,2,3,4,5};
    				for (int i =0; i< rating_array.length; ++i)
    				{
    					int temp =0;
    					StringArray.add("\n\nRating: " + rating_array[i] + "\n\n");
    					for(int j =0; j< lr.size(); ++j)
        				{
    						if(lr.get(j).getRating().equals(rating_array[i]))
    						{
    							temp =1;
    							StringArray.add(lr.get(j).getRname() +"\n" +
    											"Description: " + lr.get(j).getDescription() + "\n" +
    											"Cuisine Type: "+lr.get(j).getCuisineType() + "\n" +
    											"Avg. Price: " + lr.get(j).getAvgPrice());
    							idArray.add(lr.get(i).getR_id().toString().trim());
    						}	
        				}
    					if(temp != 1)
    					{
    						StringArray.add("No Restaurants found for rating: " + rating_array[i]);
    					}
    				}	
    				displayList();
    			}
    			
    			else if( property.equals( "AvgPrice" ) )
    			{
    				final Double[] avg_price_array = {5.0,6.0,8.0,8.8,9.0,10.0,12.0};
    				for(int i =0; i< avg_price_array.length; ++i)
    				{
    					StringArray.add("\n\nAverage Price: " + avg_price_array[i] + "\n\n");
    					for(int j=0; j<lr.size(); ++j)
    					{
    						if(lr.get(j).getAvgPrice().equals(avg_price_array[i]))
    						{
    							StringArray.add(lr.get(j).getRname() +"\n" +
    											"Description: " + lr.get(j).getDescription() + "\n" +
    											"Cuisine Type: "+lr.get(j).getCuisineType() + "\n" +
    											"Rating: " + lr.get(j).getRating() + "\n");
    							idArray.add(lr.get(i).getR_id().toString().trim());
    						}
    					}		
    				}
    				displayList();
    			}    

     }} );

    }
  }
    
  private void displayList()
  {
  	ArrayAdapter<String> adapter = 
  			new ArrayAdapter<String>(SelectBrowseOperationActivity.this,		 			//COntext for this activity
  									 R.layout.search_item, //Layout to be use(create)
  									 StringArray);
  	ListView view = (ListView)findViewById(R.id.searchListView);
  	view.setAdapter(adapter);
  	active_list_button();
	}
	
	private void active_list_button()
	{
		ListView list = (ListView)findViewById(R.id.searchListView);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id){
				TextView text1 = (TextView)viewClicked;
				
				Intent intent = new Intent(SelectBrowseOperationActivity.this, RestaurantPage.class);
				//String[] separated = text1.getText().toString().split("\nDescription");
				intent.putExtra("R_id",idArray.get(position).toString());
				System.out.println(":::::::::::::::::::::::"+position);
				// for recommendation page: add counter to each restaurant 
				 

				startActivity (intent);

			}
		});
	}
}
