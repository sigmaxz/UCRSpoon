package com.backendless.ucrspoon.login;

import com.backendless.Backendless;
import com.backendless.ucrspoon.data.crud.common.Defaults;

import android.app.Activity;
import android.os.Bundle;

public class Recommandation extends Activity {
	 public void onCreate( Bundle savedInstanceState )
	  {
	    super.onCreate( savedInstanceState );
	    setContentView(R.layout.search_list);
	    
	    Backendless.setUrl( Defaults.SERVER_URL );
	    //Backendless.initApp( getBaseContext(), Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );
	
	
	  }

}
