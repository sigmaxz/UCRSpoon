package com.backendless.ucrspoon.login;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.ucrspoon.data.Restaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Recommendation extends Activity {
	
	  private Integer[] counter_array; 
	  private List<String> StringArray = new ArrayList<String>();
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
		 calendar.add( Calendar.DAY_OF_YEAR, -0 );  // -0 instant..???
		 Date oneDayAgo = calendar.getTime();
		 BackendlessDataQuery query = new BackendlessDataQuery();
		 query.setWhereClause( "created < " + String.valueOf( oneDayAgo.getTime() ) );  // i swear i didnt change the inequality sign... wtf??!!!
		 
	    	Restaurant.findAsync( query, new DefaultCallback<BackendlessCollection<Restaurant>>( Recommendation.this )
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
		
	    			counter_array = new Integer[lr.size()]; 
	    			
	    			for(int i =0; i< lr.size(); ++i) // put all BrowseCounter into an array 
    				{
	    				counter_array[i] = lr.get(i).getBrowseCounter();
    				}	    			
	    			Arrays.sort(counter_array,Collections.reverseOrder());   // sorts the array in descending order
	    			
	    			//output the most popular restaurant...
	    					for(int j =0; j < lr.size(); ++j)
	    					{
	    						if(counter_array[0] == lr.get(j).getBrowseCounter())
	    						{
	    							// have duplicates 
	    							StringArray.add("* * * * * Popularity: Top "+ 1 +" * * * * *"+"\n" +
	    											lr.get(j).getRname() +"\n" +
	    											"Total number of times viewed: "+lr.get(j).getBrowseCounter()+"\n" +
	    											"Description: " + lr.get(j).getDescription() + "\n" +
	    											"Cuisine Type: "+lr.get(j).getCuisineType() + "\n" +
	    											"Rating: " + lr.get(j).getRating() + "\n"+
	    											"Avg. Price: " + lr.get(j).getAvgPrice()+"\n");
	        		    			idArray.add(lr.get(j).getR_id().toString().trim());

	    						}
	    					}		
	    			int temp =0;
	    			for(int i =1; i<lr.size(); ++i)  // starts from the second one....
	    			{
	    				if(!(counter_array[i].equals(counter_array[i-1])))   // checks for duplicated BrowseCounter...
	    				{
	    					for(int j =0; j < lr.size(); ++j)
	    					{
	    						if(counter_array[i] == lr.get(j).getBrowseCounter())
	    						{
	    							temp = i+1;
	    							StringArray.add("* * * * * Popularity: Top "+ temp +" * * * * *"+"\n" +
													//"Total number of times viewed: "+lr.get(j).getBrowseCounter()+"\n" +
													lr.get(j).getRname() +"\n" +
													"Total number of times viewed: "+lr.get(j).getBrowseCounter()+"\n" +
	    											"Description: " + lr.get(j).getDescription() + "\n" +
	    											"Cuisine Type: "+lr.get(j).getCuisineType() + "\n" +
	    											"Rating: " + lr.get(j).getRating() + "\n"+
	    											"Avg. Price: " + lr.get(j).getAvgPrice()+"\n");
	        		    			idArray.add(lr.get(j).getR_id().toString().trim());

	    						}
	    					}
	    				}
	    			}
	    			
	    			displayList();			
	    		}
	    	});

	  }
	  private void displayList()
	  {
	  	ArrayAdapter<String> adapter = 
	  			new ArrayAdapter<String>(Recommendation.this,		 			//COntext for this activity
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
					
					Intent intent = new Intent(Recommendation.this, RestaurantPage.class);
					//String[] separated = text1.getText().toString().split("\nDescription");
					intent.putExtra("R_id",idArray.get(position).toString());
					System.out.println(":::::::::::::::::::::::"+position);
					// for recommendation page: add counter to each restaurant 

					startActivity (intent);

				}
			});
		}
	}
