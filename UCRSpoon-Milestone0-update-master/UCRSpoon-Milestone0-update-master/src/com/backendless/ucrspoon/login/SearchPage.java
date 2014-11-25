package com.backendless.ucrspoon.login;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.ucrspoon.data.Restaurant;
import com.backendless.ucrspoon.data.crud.common.DefaultCallback;
import com.backendless.ucrspoon.data.crud.common.Defaults;

public class SearchPage extends Activity{
	private String search_input, radio_choice;
	//private List<Restaurant> result= new ArrayList<Restaurant>();
    private List<String> StringArray = new ArrayList<String>();
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_list);
		
		Backendless.setUrl( Defaults.SERVER_URL );
	    Backendless.initApp( getBaseContext(), Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );
	    
	    searchDataBase();
	    
	}
	
	private void searchDataBase()
	{
		 Intent tempIntent = getIntent();
		 search_input = tempIntent.getStringExtra("input");
		 radio_choice = tempIntent.getStringExtra("radio");
		 
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTime( new Date() );
	     calendar.add( Calendar.DAY_OF_YEAR, -1 );
		 Date oneDayAgo = calendar.getTime();
		 BackendlessDataQuery query = new BackendlessDataQuery();
		 query.setWhereClause( "created < " + String.valueOf( oneDayAgo.getTime() ) );
		 
		 
		 Restaurant.findAsync( query, new DefaultCallback<BackendlessCollection<Restaurant>>( SearchPage.this )
		 {
		   @Override
		   public void handleResponse( BackendlessCollection<Restaurant> response )
		   {
		     super.handleResponse( response );
		     List<Restaurant>lr = response.getCurrentPage();
		     
		     if(lr.size()>0)
		     {
		    	 if(radio_choice.equals("search_radio0"))
		    	 {
		    		 Integer counter = 1;
			    	 for(int i = 0; i < lr.size(); i++)
			    	 {
			    		 if(search_input.equals(lr.get(i).getRname()))
			    		 {
			    			 //result.add(lr.get(i));
			    			 /*
			    			 StringArray.add(counter+")"+lr.get(i).getRname() + "\n\n" +
			 						"Description: " + lr.get(i).getDescription() + "\n" +
			 						"Cuisine Type: " +lr.get(i).getCuisineType() +"\n"+
			 						"Ratings: " + lr.get(i).getRating() + "\n" +
			 						"Avg. Price: " + lr.get(i).getAvgPrice() + "\n");
			 						*/
			    			 
			    			 StringArray.add(lr.get(i).getRname() + "\n\n" +
				 						"Description: " + lr.get(i).getDescription() + "\n" +
				 						"Cuisine Type: " +lr.get(i).getCuisineType() +"\n"+
				 						"Ratings: " + lr.get(i).getRating() + "\n" +
				 						"Avg. Price: " + lr.get(i).getAvgPrice() + "\n");
			    			 
			    			 
			    			 counter++;
			    		 }
			    		 else if(search_input.equals(lr.get(i).getCuisineType()))
			    		 {
			    			 //result.add(lr.get(i));
/*			    			 StringArray.add(counter+")"+lr.get(i).getRname() + "\n\n" +
			 						"Description: " + lr.get(i).getDescription() + "\n" +
			 						"Cuisine Type: " +lr.get(i).getCuisineType() +"\n"+
			 						"Ratings: " + lr.get(i).getRating() + "\n" +
			 						"Avg. Price: " + lr.get(i).getAvgPrice() + "\n");
			 						*/
			    			 
			    			 StringArray.add(lr.get(i).getRname() + "\n\n" +
				 						"Description: " + lr.get(i).getDescription() + "\n" +
				 						"Cuisine Type: " +lr.get(i).getCuisineType() +"\n"+
				 						"Ratings: " + lr.get(i).getRating() + "\n" +
				 						"Avg. Price: " + lr.get(i).getAvgPrice() + "\n");
			    			 
			    			counter++;
			    		 }
			    	 }  
		     	}
		    	 else if(radio_choice.equals("search_radio1"))
		    	 {
		    		 Integer counter = 1;
		    		 for(int i = 0; i < lr.size(); i++)
			    	 {
			    		 if((Double.valueOf(search_input)).equals(lr.get(i).getAvgPrice()))
			    		 {
			    			 //result.add(lr.get(i));
/*			    			 StringArray.add(counter+")"+lr.get(i).getRname() + "\n\n" +
			 						"Description: " + lr.get(i).getDescription() + "\n" +
			 						"Cuisine Type: " +lr.get(i).getCuisineType() +"\n"+
			 						"Ratings: " + lr.get(i).getRating() + "\n" +
			 						"Avg. Price: " + lr.get(i).getAvgPrice() + "\n");
			 						*/
			    			 
			    			 StringArray.add(lr.get(i).getRname() + "\n\n" +
				 						"Description: " + lr.get(i).getDescription() + "\n" +
				 						"Cuisine Type: " +lr.get(i).getCuisineType() +"\n"+
				 						"Ratings: " + lr.get(i).getRating() + "\n" +
				 						"Avg. Price: " + lr.get(i).getAvgPrice() + "\n");
			    			 
			    			 
			    			 counter++;
			    		 }
			    		 
			    	 }
		    	 }
		    	 else if(radio_choice.equals("search_radio2"))
		    	 {
		    		 Integer counter = 1;
		    		 for(int i = 0; i < lr.size(); i++)
			    	 {
			    		 if((Integer.valueOf(search_input)).equals(lr.get(i).getRating()))
			    		 {
			    			 //result.add(lr.get(i));
/*			    			 StringArray.add(counter+")"+lr.get(i).getRname() + "\n\n" +
			 						"Description: " + lr.get(i).getDescription() + "\n" +
			 						"Cuisine Type: " +lr.get(i).getCuisineType() +"\n"+
			 						"Ratings: " + lr.get(i).getRating() + "\n" +
			 						"Avg. Price: " + lr.get(i).getAvgPrice() + "\n");
			 						*/
			    			 
			    			 StringArray.add(lr.get(i).getRname() + "\n\n" +
				 						"Description: " + lr.get(i).getDescription() + "\n" +
				 						"Cuisine Type: " +lr.get(i).getCuisineType() +"\n"+
				 						"Ratings: " + lr.get(i).getRating() + "\n" +
				 						"Avg. Price: " + lr.get(i).getAvgPrice() + "\n");
			    			 
			    			 
			    			 
			    			 counter++;
			    		 }
			    		 
			    	 }
		    	 }
		    	 else
		    	 {
		    		 Toast.makeText(getApplicationContext(),"restaurant not found",
		    				 Toast.LENGTH_LONG).show();
		    	 }
		    	 
		    	 if(StringArray.size()>0)
		    	 {
		    		 displayList();
		    	 }
		    	 else
		    	 {
		    		 Toast.makeText(getApplicationContext(),"restaurant not found",
		    				 Toast.LENGTH_LONG).show();
		    	 }
		      }
		     else 
		     {
		    	 Toast.makeText(getApplicationContext(),"restaurant not found",
	    				 Toast.LENGTH_LONG).show();
		    	
		     }
		     
		     
		   }
		 } );
	}
	
	private void displayList()
	{
		ArrayAdapter<String> adapter = 
				new ArrayAdapter<String>(SearchPage.this,		 			//COntext for this activity
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
				//Toast.makeText(getApplicationContext(),StringArray.get(position),
	    		//		 Toast.LENGTH_LONG).show();
				
				Intent intent = new Intent(SearchPage.this, RestaurantPage.class);
				String[] separated = text1.getText().toString().split("\n");
				intent.putExtra("Rname",separated[0]);
				//Toast.makeText(NearbyRestaurant.this, separated[2], Toast.LENGTH_LONG).show();
				startActivity (intent);
					
				
			}
		});
	}
	
	
}
