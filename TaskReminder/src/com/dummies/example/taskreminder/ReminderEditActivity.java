package com.dummies.example.taskreminder;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.widget.Toast;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

@SuppressLint("SimpleDateFormat")
public class ReminderEditActivity extends Activity {

	private Button mDateButton;
	private EditText mTitleText;
	private EditText mBodyText;
	private Button mTimeButton;
	private Button Save;
	RemindersDbAdapter mDbHelper;
	ReminderManagaer rmr;
	public Calendar mCalender;
	private Long mRId;
	private static final int DATE_PICKER_DIALOG=0;
	private static final int TIME_PICKER_DIALOG=1;
	@SuppressWarnings("unused")
	private static final String DATE_FORMAT="yyyy-MM-dd";
	@SuppressWarnings("unused")
	private static final String TIME_FORMAT="kk:mm";
	public static final String DATE_TIME_FORMAT="yyyy-MM-dd kk:mm:ss";
		
		@SuppressWarnings("deprecation")
		@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//you were not calling setContentView();
		setContentView(R.layout.reminder_edit);
		
		
		
//		your mistake
//		RemindersDbAdapter mDbHelper = new RemindersDbAdapter(this);
		//mDbHelper is already declared as an instance variable, the above code was declaring another local variable with the same name.
		
		//corrected (online instantiate mDbHelper, it has already been declared as a global (instance) variable)
		mDbHelper = new RemindersDbAdapter(this);
		rmr = new ReminderManagaer(this);
		
		
		setContentView(R.layout.reminder_edit);
		mDateButton=(Button)findViewById(R.id.reminder_date);
		mTimeButton=(Button)findViewById(R.id.reminder_time);
		Save=(Button)findViewById(R.id.confirm);
		
		/*
		(your mistake) Calendar mCalender =Calendar.getInstance();
		because of this you were getting null pointer exception on 
		String dateForButton=(String) dF.format(mCalender.getTime());
		I have changed that code, you may again use it!
		 * 
		 * */
		 
		mCalender =Calendar.getInstance();
		mCalender.setTimeInMillis(System.currentTimeMillis());
		Log.w("Testing testing", (new Date(mCalender.getTimeInMillis())).toGMTString());
		
		
		
		mTitleText=(EditText)findViewById(R.id.title);
		mBodyText=(EditText)findViewById(R.id.body);
		 mRId= savedInstanceState!=null ? savedInstanceState.getLong(RemindersDbAdapter.KEY_ROWID) : null;
			registerButtonListenersAndSetDefaultText();
			
		}
	
		private void setRowIdFromIntent(){
			if(mRId == null){
				Bundle extras = getIntent().getExtras();
				mRId = extras!=null ? extras.getLong(RemindersDbAdapter.KEY_ROWID):null;
						
			}
		}
		
		

		public void saveState() {
			String title= mTitleText.getText().toString();
			String body = mBodyText.getText().toString();
			
			
			try{
				Date date = new Date(mCalender.getTimeInMillis());
				@SuppressWarnings("deprecation")
				String reminderDateTime = date.toGMTString();
				if(mRId==null)
				{
					long id= mDbHelper.createReminder(title,body,reminderDateTime);
					if(id>0)	mRId=id;
				}
				else	
					mDbHelper.updateReminder(mRId,title, body, reminderDateTime);
					
			}catch(Exception e)
			{
				//Log.w("message", e.getMessage());
				e.printStackTrace();
			}
			
			rmr.setReminder(mRId,mCalender);
		}
	
	
	
	private void registerButtonListenersAndSetDefaultText() {
		// TODO Auto-generated method stub
		mDateButton.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
			showDialog(DATE_PICKER_DIALOG);	
			}
		});
		mTimeButton.setOnClickListener(new View.OnClickListener() {
			
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			showDialog(TIME_PICKER_DIALOG);	
			}
		});
		
		updateDateButtonText();
		updateTimeButtonText();
		
		
		
		Save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new 

						AlertDialog.Builder(ReminderEditActivity.this);
										builder.setMessage("Are you sure you want to save the task?")
												

						.setTitle("Are you sure")
												

						.setCancelable(false)
												

						.setPositiveButton("Yes",new 

						DialogInterface.OnClickListener(){
													

						public void onClick(DialogInterface dialog,int id){
														

						saveState();
										setResult(RESULT_OK);
										

						Toast.makeText(ReminderEditActivity.this, 

						getString(R.string.task_saved_message), 

						Toast.LENGTH_SHORT).show();
										finish();
													}
												})
												

						.setNegativeButton("No",new 

						DialogInterface.OnClickListener() {
													

						public void onClick(DialogInterface dialog,int id){
														

						dialog.cancel();
													}
												});
												

						builder.create().show();
				
			}
		});
		
		
	}
	
	
	@SuppressWarnings("deprecation")
	protected Dialog onCreateDialog(int id){
		switch(id){
		case DATE_PICKER_DIALOG:
			return showDatePicker();
		case TIME_PICKER_DIALOG:
			return showTimePicker();	
		}
		return super.onCreateDialog(id);
	}
	
	private DatePickerDialog showDatePicker(){
		DatePickerDialog dP = new DatePickerDialog(ReminderEditActivity.this,new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int mon, int day) {
				// TODO Auto-generated method stub
				mCalender.set(Calendar.YEAR,year);
				mCalender.set(Calendar.MONTH,mon);
				mCalender.set(Calendar.DAY_OF_MONTH,day);
				
				updateDateButtonText();
				updateTimeButtonText();
			}
		},mCalender.get(Calendar.YEAR),mCalender.get(Calendar.MONTH),mCalender.get(Calendar.DAY_OF_MONTH));
		return dP;
	}
	private TimePickerDialog showTimePicker(){
		TimePickerDialog dP = new TimePickerDialog(this,new TimePickerDialog.OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hour, int min) {
				// TODO Auto-generated method stub
				mCalender.set(Calendar.HOUR_OF_DAY,hour);
				mCalender.set(Calendar.MINUTE,min);
				
				
				updateDateButtonText();
				updateTimeButtonText();
			}
		},mCalender.get(Calendar.HOUR_OF_DAY),mCalender.get(Calendar.MINUTE),true);
		return dP;
	}
	private void updateDateButtonText(){
		//SimpleDateFormat dF=new SimpleDateFormat(DATE_FORMAT);
		//String dateForButton=(String) dF.format(mCalender.getTime());
		String dateForButton = (new Date(mCalender.getTimeInMillis())).toString();
		mDateButton.setText(dateForButton);	
	}
	
	
	@SuppressLint("SimpleDateFormat")
	private void updateTimeButtonText(){

		Date date = new Date(mCalender.getTimeInMillis());
		@SuppressWarnings("deprecation")
		String reminderDateTime = date.toLocaleString();
		
		
		
		mTimeButton.setText(reminderDateTime);	
	}

	
	@SuppressWarnings("deprecation")
	private void populateFields() throws java.text.ParseException {
		if(mRId!=null){
			Cursor cr = mDbHelper.fetchReminder(mRId);
			startManagingCursor(cr);
			mTitleText.setText(cr.getString(cr.getColumnIndexOrThrow(RemindersDbAdapter.KEY_TITLE)));
			mBodyText.setText(cr.getString(cr.getColumnIndexOrThrow(RemindersDbAdapter.KEY_BODY)));
			
			
			//SimpleDateFormat dtf= new SimpleDateFormat(DATE_TIME_FORMAT);
			Date date=null;
			try{
				String ds = cr.getString(cr.getColumnIndexOrThrow(RemindersDbAdapter.KEY_DATE_TIME));
				date= getString(ds);
				
				mCalender.setTime(date);
			} catch(Exception e)
			{
				Log.e("ReminderEditActivity",e.getMessage(),e);
				e.printStackTrace();
			}
		
		}
		updateDateButtonText();
		updateTimeButtonText();
			
	}
	
	
	@Override 
	protected void onSaveInstanceState( Bundle outstate){
		super.onSaveInstanceState(outstate);
		outstate.putLong(RemindersDbAdapter.KEY_ROWID, mRId);
	}
	
	
	
	private Date getString(String datestring) throws java.text.ParseException{
	    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    sd.setTimeZone(TimeZone.getDefault());
	    return (Date)sd.parse(datestring);
	    
	}
	
	
	
	
	
	@Override
	protected void onPause(){
		super.onPause();
		try{
		mDbHelper.close();
		}
		catch(ParseException e){
			e.printStackTrace();
		}
	}
	@Override
	protected void onResume(){
		super.onResume();
		try{
		try {
			mDbHelper.open();
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		catch(ParseException e){
			e.printStackTrace();
		}
		setRowIdFromIntent();
		try {
			populateFields();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
}

