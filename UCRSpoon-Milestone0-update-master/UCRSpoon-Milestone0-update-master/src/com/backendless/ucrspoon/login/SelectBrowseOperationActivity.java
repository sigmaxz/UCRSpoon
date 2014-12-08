package com.backendless.ucrspoon.login;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.ucrspoon.data.*;
import com.backendless.ucrspoon.data.crud.common.DefaultCallback;
import com.backendless.ucrspoon.data.crud.common.Defaults;
import com.backendless.ucrspoon.data.crud.retrieve.RetrieveRecordActivity;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SelectBrowseOperationActivity extends Activity
{
	  private String property;
	  private List<String> StringArray = new ArrayList<String>();
	  private Double[] avgPrice_arr;
	  private String[] rName_arr;
	  private String[] dish_arr;
	  private String[] rid_arr;
	  
	  private List<String> idArray = new ArrayList<String>();
  
	  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView(R.layout.search_list);
	Backendless.setUrl( Defaults.SERVER_URL ); // in case you didn't already do the init

    initUI();
  }

  private void initUI()
  {	  	
    Calendar calendar = Calendar.getInstance();
    calendar.setTime( new Date() );
    calendar.add( Calendar.DAY_OF_YEAR, -0 );  // was -5.. and it worked..
    Date oneDayAgo = calendar.getTime();
      //went here...
    BackendlessDataQuery query = new BackendlessDataQuery();
    query.setWhereClause( "created < " + String.valueOf( oneDayAgo.getTime() ) );  // i swear i didnt change the inequality sign... wtf??!!!

    Intent intent = getIntent();
    property = intent.getStringExtra( "property" );

    //went here.....
    
   if( property.equals( "Dish" ) )
    {
    	Dish.findAsync( query, new DefaultCallback<BackendlessCollection<Dish>>( SelectBrowseOperationActivity.this )
    			{
    		public void handleResponse( BackendlessCollection<Dish> response )
    		{
    			super.handleResponse( response ); 
    			// **********  things i added  **********    			
    			List<Dish> dish_list = response.getData();
    			dish_arr = new String[dish_list.size()];
    			
    			for(int i=0; i<dish_list.size(); ++i)   // get dish name to an array 
    			{
    				dish_arr[i] = dish_list.get(i).getName();
    			}
    			
    			Arrays.sort(dish_arr);  //sort the 	
    			
    			for(int i =0; i< dish_list.size(); ++i)
    			{
    				for(int j =0; j < dish_list.size(); ++j)
    				{
    					if(dish_arr[i].equals(dish_list.get(j).getName()))
    					{
    						StringArray.add("Dish Name: "+  dish_list.get(j).getName()+ "\n" +
    			    				"Dish Category: " + dish_list.get(j).getCategory()+ "\n" +
    			    				"Dish Price: " + dish_list.get(j).getPrice() + "\n" +  
    			    				"\n");    
    						idArray.add(null);
    					}
    				}
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

//***************************/***************************/***************************
    			List<Restaurant> lr = response.getData();
        
    			if(lr.size() < 1){     
    				Toast.makeText(getApplicationContext(), "No restaurants", Toast.LENGTH_LONG).show();
    				finish();
    			}

    			if( property.equals( "CuisineType" ) )
    			{
    				final String[] cuisine_type = {"Mexican","Pizza","American","Japanese","Fastfood"};
    				Arrays.sort(cuisine_type);

    				for(int i =0; i<cuisine_type.length; ++i)
    				{
    					StringArray.add("\n\nCuisine Type: " + cuisine_type[i] + "\n\n");
    					idArray.add(null);
    					for(int j = 0; j < lr.size(); ++j)
        				{
        					if(lr.get(j).getCuisineType().equals(cuisine_type[i]))
        					{
        						StringArray.add(lr.get(j).getRname() +"\n" +
        										"Description: " + lr.get(j).getDescription() + "\n" +
        										"Ratings: " + lr.get(j).getRating() + "\n" +
        										"Avg. Price: " + lr.get(j).getAvgPrice());
        						//
        							idArray.add(lr.get(j).getR_id().toString().trim());

        					}
        				}
    				}
					displayList();
    			}
     
    			else if( property.equals( "All Restaurants" ) )
    			{
    				rName_arr = new String[lr.size()];
    				for(int i =0; i<lr.size(); ++i)
    				{
    					rName_arr[i] = lr.get(i).getRname();
    				}
    				Arrays.sort(rName_arr);
    				
    				for(int i =0; i< lr.size(); ++i)
    				{
    					for(int j=0; j<lr.size(); ++j)
    					{
    						if(rName_arr[i].equals(lr.get(j).getRname()))
    						{
    							StringArray.add(lr.get(j).getRname() +"\n" +
    											"Description: " + lr.get(j).getDescription() + "\n" +
    											"Cuisine Type: "+lr.get(j).getCuisineType() + "\n" +
    											"Rating: " + lr.get(j).getRating() + "\n"+
    											"Avg. Price: " + lr.get(j).getAvgPrice() + "\n");
    							idArray.add(lr.get(j).getR_id().toString().trim());
    						}
    					}
    					
    					
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
    					idArray.add(null);
    					for(int j =0; j< lr.size(); ++j)
        				{
    						if(lr.get(j).getRating().equals(rating_array[i]))
    						{
    							temp =1;
    							StringArray.add(lr.get(j).getRname() +"\n" +
    											"Description: " + lr.get(j).getDescription() + "\n" +
    											"Cuisine Type: "+lr.get(j).getCuisineType() + "\n" +
    											"Avg. Price: " + lr.get(j).getAvgPrice());
    							idArray.add(lr.get(j).getR_id().toString().trim());
    						}	
        				}
    					if(temp != 1)
    					{
    						StringArray.add("No Restaurants found for rating: " + rating_array[i]);
    						idArray.add(null);
    					}
    				}	
    				displayList();
    			}
    			
    			else if( property.equals( "AvgPrice" ) )
    			{
    				
    				avgPrice_arr = new Double [lr.size()];
    				for(int i =0; i < lr.size(); ++i)  // put all avg price into an array 
    				{
    						avgPrice_arr[i] = lr.get(i).getAvgPrice();
    				}
    				Arrays.sort(avgPrice_arr);  // sort the array in ascending order
    				
    				StringArray.add("\n\nAverage Price: " + avgPrice_arr[0] + "\n\n");   // output the first one regardless
    				idArray.add(null);
    				for(int j=0; j<lr.size(); ++j)
					{
						if(lr.get(j).getAvgPrice().equals(avgPrice_arr[0]))
						{
							StringArray.add(lr.get(j).getRname() +"\n" +
											"Description: " + lr.get(j).getDescription() + "\n" +
											"Cuisine Type: "+lr.get(j).getCuisineType() + "\n" +
											"Rating: " + lr.get(j).getRating() + "\n");
							idArray.add(lr.get(j).getR_id().toString().trim());
						}
					}
    				
					for(int i =1; i < avgPrice_arr.length; ++i)
    				{
    					if(!(avgPrice_arr[i].equals(avgPrice_arr[i-1])))   //check for duplicates
    					{
    						StringArray.add("\n\nAverage Price: " + avgPrice_arr[i] + "\n\n");
    						idArray.add(null);
    						for(int j=0; j<lr.size(); ++j)
    						{
    							if(lr.get(j).getAvgPrice().equals(avgPrice_arr[i]))
    							{
    								StringArray.add(lr.get(j).getRname() +"\n" +
    												"Description: " + lr.get(j).getDescription() + "\n" +
    												"Cuisine Type: "+lr.get(j).getCuisineType() + "\n" +
    												"Rating: " + lr.get(j).getRating() + "\n");
	    							idArray.add(lr.get(j).getR_id().toString().trim());
    							}
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
				Intent intent = new Intent(SelectBrowseOperationActivity.this, RestaurantPage.class);
				//String[] separated = text1.getText().toString().split("\nDescription");
				if(idArray.get(position).toString() != null)
				{
					intent.putExtra("R_id",idArray.get(position).toString());
					System.out.println(":::::::::::::::::::::::"+position);
					// for recommendation page: add counter to each restaurant 
					startActivity (intent);
				}
				else
				{
					return;
				}
			}
		});
	}
}
