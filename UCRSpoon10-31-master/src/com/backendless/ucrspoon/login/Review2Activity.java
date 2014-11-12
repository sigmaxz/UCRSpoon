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
import com.backendless.ucrspoon.data.Review;
import com.backendless.ucrspoon.data.Restaurant;


public class Review2Activity extends Activity{

	private static final String NULL = null;
	private EditText CostField;
	private EditText EnvonField;
	private EditText ServiceField;
	
	private Button Submit2;
	
	private String Cost;
	private String Envon;
	private String Service;
	private String Rn;
	private Intent i;
	
	private Review review;
	
	public void onCreate( Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    Backendless.setUrl( Defaults.SERVER_URL ); 
		Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );
		setContentView(R.layout.review_valid);
		
		initUI();
	}
	
	private void initUI()
	  {CostField = (EditText) findViewById( R.id.costField );EnvonField = (EditText) findViewById( R.id.envonField );
	  	ServiceField = (EditText) findViewById( R.id.serviceField);

	    Submit2 = (Button) findViewById( R.id.submit_review );

	    Submit2.setOnClickListener( new View.OnClickListener()
	    {
	      @Override
	      public void onClick( View view )
	      {
	        onReview2ButtonClicked(view);
	      }
	    } );
	  }
	private void onReview2ButtonClicked(View view) {
		// TODO Auto-generated method stub
		 String CostText = CostField.getText().toString().trim();
		 String EnvonText = EnvonField.getText().toString().trim();
		 String ServiceText = ServiceField.getText().toString().trim();
		 i = new Intent(view.getContext(), ReviewSuccess.class);

		 
		 if ( CostText.isEmpty() )
		    {
		      showToast( "Field 'Cost' cannot be empty." );
		      return;
		    }
		 if ( EnvonText.isEmpty() )
		    {
		      showToast( "Field 'Envon' cannot be empty." );
		      return;
		    }
		 if ( ServiceText.isEmpty() )
		    {
		      showToast( "Field 'Service' cannot be empty." );
		      return;
		    }
		 
		 if(CostText != NULL)
		 {
			 Cost = CostText;
		 }
		 
		 if(EnvonText != NULL)
		 {
			 Envon = EnvonText;
		 }
		 if(ServiceText != NULL)
		 {
			 Service = ServiceText;
		 }
		 
		 review = new Review();
		 
		 review.setR_id( getIntent().getStringExtra("restaurant"));
		 review.setD_id( getIntent().getStringExtra("dish"));
		 
		 
		 if(Cost != NULL)
		 {
			 review.setDiningCost(Cost);
		 }
		 
		 if(Envon != NULL)
		 {
			 review.setEnviornment(Envon);
		 }
		 
		 if(Service != NULL)
		 {
			 review.setService(Service);
		 }

		 review.saveAsync( new AsyncCallback<Review>()
		{

			@Override
			public void handleFault(BackendlessFault arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void handleResponse(Review arg0) {
				// TODO Auto-generated method stub
			}
			 
		});
		 Rn = getIntent().getStringExtra("rnm");
		 String whereClause = "Rname = '"+ Rn + "'"; 

		 BackendlessDataQuery dataQuery = new BackendlessDataQuery();
		 dataQuery.setWhereClause( whereClause );
		 Restaurant.findAsync( dataQuery, new AsyncCallback<BackendlessCollection<Restaurant>>() 
		{
			  @Override
			  public void handleResponse( BackendlessCollection<Restaurant> response )
			  {
				  List<Restaurant> lr = response.getData();
				  if(lr.size() < 1){
					  showToast( " Restaurant not found for update." );
					  return;
				  }
				  Restaurant firstRestaurant = response.getCurrentPage().get( 0 );
				  if( !Rn.equals(firstRestaurant.getRname()))
				  {
					  showToast( " update Restaurant not found." );
					  return;
				  }
				  else
				  {
					  showToast(" Restaurant updated ");
					  
					  double avgp =  getIntent().getDoubleExtra("avgp", 0);
					  int nor = getIntent().getIntExtra("nor", 0);
					  double rtotal = avgp * nor;
					  firstRestaurant.setAvgPrice((rtotal + Integer.parseInt(Cost))/( nor + 1) );
					  firstRestaurant.setNumOfReviews(nor +1);
					  firstRestaurant.saveAsync(new DefaultCallback<Restaurant>(Review2Activity.this)
					  {
				          @Override
				          public void handleResponse( Restaurant response )
				          {
				        	  showToast("avg updated");
				        	  startActivity(i);
				        	  finish();
				          }
				        } );
					  
				  }
			  }
			@Override
			public void handleFault(BackendlessFault fault) { 
				// TODO Auto-generated method stub
				  return;
			}
		}); 
		 
		//startActivity (new Intent(view.getContext(), ReviewSuccess.class));
		//finish();

		 
		 }

	private void showToast(String string) {
		// TODO Auto-generated method stub
	    Toast.makeText( this, string, Toast.LENGTH_SHORT ).show();
	}
}

