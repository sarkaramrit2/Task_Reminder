package com.dummies.example.taskreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnAlarmReciever extends BroadcastReceiver {

@Override
public void onReceive(Context arg0, Intent arg1) {
	// TODO Auto-generated method stub
	long rid = arg1.getExtras().getLong(RemindersDbAdapter.KEY_ROWID);
	WakeReminderIntentService.acquireStaticLock(arg0);

	Intent in = new Intent (arg0, ReminderService.class);
	in.putExtra(RemindersDbAdapter.KEY_ROWID, rid);
	arg0.startService(in);

	
}


}