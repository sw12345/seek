package com.compgc02.team26.seek;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.compgc02.samsudin.seek.R;

public class Message extends SherlockFragmentActivity {

	private EditText subject;
	private EditText message;
	private Spinner type;
	private String sub;
	private String msg;
	private String msgtype;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		subject = (EditText) findViewById(R.id.subject);
		message = (EditText) findViewById(R.id.msg_content);
		type = (Spinner) findViewById(R.id.msg_type);

		//button click send msg
		final Button sendBtn = (Button) findViewById(R.id.send);
		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick (View v) {
				sub = subject.getText().toString();
				msg = message.getText().toString();
				msgtype = type.getSelectedItem().toString();
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/email");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "captain.bomi@gmail.com" });
				intent.putExtra(Intent.EXTRA_SUBJECT, "(" + msgtype + ") " + sub);
				intent.putExtra(Intent.EXTRA_TEXT, msg);
				startActivity(Intent.createChooser(intent, "Send message to UnLtd..."));

			}
		});



	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}        
}
