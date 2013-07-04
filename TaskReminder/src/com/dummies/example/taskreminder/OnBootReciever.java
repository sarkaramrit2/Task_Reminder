package com.dummies.example.taskreminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.ParseException;
import android.util.Log;

public class OnBootReciever extends BroadcastReceiver{



@Override
public void onReceive(Context context, Intent intent) {
	// TODO Auto-generated method stub
	ReminderManagaer rmgr= new ReminderManagaer(context);
	RemindersDbAdapter db= new RemindersDbAdapter(context);
	try {
		db.open();
	} catch (SQLiteException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (java.text.ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	Cursor csr =db.fetchAllReminders();
	if(csr!=null)
	{
	csr.moveToFirst();

	int rowIdColumnIndex =csr.getColumnIndex(RemindersDbAdapter.KEY_ROWID);
	int dateTimeColumnIndex = csr.getColumnIndex(RemindersDbAdapter.KEY_DATE_TIME);

	while(csr.isAfterLast()==false){
	Long rid = csr.getLong(rowIdColumnIndex);
	Log.d("OnBootReciever","Adding alarm from boot");
	String dateime = csr.getString(dateTimeColumnIndex);
	Log.d("OnBootReciever","Row Id Column Index -" + rowIdColumnIndex);
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat dmt = new SimpleDateFormat(ReminderEditActivity.DATE_TIME_FORMAT);
	try{
	java.util.Date date = dmt.parse(dateime, null);
	cal.setTime(date);

	rmgr.setReminder(rid,cal);

	} catch(ParseException e){
	Log.e("OnBootReciever",e.getMessage(),e);

	}
	csr.moveToNext();


	}
	csr.close();

	}
	db.close();

	}
	
}



