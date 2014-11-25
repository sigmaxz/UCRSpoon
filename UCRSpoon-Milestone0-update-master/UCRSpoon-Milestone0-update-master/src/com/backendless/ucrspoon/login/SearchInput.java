package com.backendless.ucrspoon.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class SearchInput extends Activity {
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_input_page);
		
		Button searchButton = (Button)findViewById(R.id.search_button2);
 	    searchButton.setOnClickListener(new View.OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				onSearchButtonClicked();
 			}	
 		});
	}
	
	void onSearchButtonClicked()
	{
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.search_radio_group1); 
		int checkedRadioButton = radioGroup.getCheckedRadioButtonId();
		String radioButtonSelected = ""; 
		switch (checkedRadioButton) {
		  case R.id.search_radio0 : radioButtonSelected = "search_radio0";
		                   	                break;
		  case R.id.search_radio1 : radioButtonSelected = "search_radio1";
		  									break;
		  case R.id.search_radio2 : radioButtonSelected = "search_radio2";
             								break;
		}
		
		EditText search_field = (EditText)findViewById(R.id.input_field);
		String value = search_field.getText().toString();
		Intent searchIntent = new Intent(SearchInput.this, SearchPage.class);
     	searchIntent.putExtra("input", value);
     	searchIntent.putExtra("radio", radioButtonSelected);
     	startActivity(searchIntent);
	}
}