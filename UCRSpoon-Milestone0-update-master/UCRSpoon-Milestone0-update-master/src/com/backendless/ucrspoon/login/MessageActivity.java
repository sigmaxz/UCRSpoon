package com.backendless.ucrspoon.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.ucrspoon.data.Message;
import com.backendless.ucrspoon.data.Review;
import com.backendless.ucrspoon.data.UsersName;

public class MessageActivity extends Activity{
	private EditText subjectField;
	private EditText contentField;
	private Button send;
	public void onCreate( Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    
		setContentView(R.layout.message_page);
		
		initUI();
	}
	
	private void initUI()
	{
		subjectField = (EditText)findViewById(R.id.messageSubjectField);
		contentField = (EditText)findViewById(R.id.messageContentField);
		send = (Button)findViewById( R.id.sendMessage );
		
		send.setOnClickListener(new View.OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				onSendButtonClicked();
 			}	
 		});
		
		
	}
	
	private void onSendButtonClicked()
	{
		String recipient, subject, content, sender;
		subject = subjectField.getText().toString().trim();
		content = contentField.getText().toString().trim();
		Intent tempIntent = getIntent();
		sender = tempIntent.getStringExtra("name");
		recipient = tempIntent.getStringExtra("recipient");
		 if ( recipient.isEmpty() )
		 {
			 Toast.makeText(getApplicationContext(),"recipient field cannot be empty",
    				 Toast.LENGTH_LONG).show();
		     return;
		 }
		 if ( subject.isEmpty() )
		 {
			 Toast.makeText(getApplicationContext(),"subject field cannot be empty",
    				 Toast.LENGTH_LONG).show();
		     return;
		 }
		 if ( content.isEmpty() )
		 {
			 Toast.makeText(getApplicationContext(),"content field cannot be empty",
    				 Toast.LENGTH_LONG).show();
		     return;
		 }
		 if ( sender.isEmpty() )
		 {
			 Toast.makeText(getApplicationContext(),"sender no found",
    				 Toast.LENGTH_LONG).show();
		     return;
		 }
		 Message new_message = new Message();
		 new_message.setRecipient(recipient);
		 new_message.setSubject(subject);
		 new_message.setMessage(content);
		 new_message.setSender(sender);
		 
		 Backendless.Persistence.save( new_message, new AsyncCallback<Message>() {
			 public void handleResponse( Message response )
		     {
				 Toast.makeText(getApplicationContext(),"message sent",
	    				 Toast.LENGTH_LONG).show();
				 finish();
		     }
		        
		     public void handleFault( BackendlessFault fault )
		     {
		    	 Toast.makeText(getApplicationContext(),"fail to send message",
	    				 Toast.LENGTH_LONG).show();
		    	 finish();
		          // an error has occurred, the error code can be retrieved with fault.getCode()
		     }
		 });
		 
	}
}