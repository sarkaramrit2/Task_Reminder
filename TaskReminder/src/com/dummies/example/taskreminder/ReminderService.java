package com.dummies.example.taskreminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

public class ReminderService extends WakeReminderIntentService{

public ReminderService(){
super("ReminderService");

}

@SuppressWarnings("deprecation")
void doReminderWork(Intent i){
Long rid=i.getExtras().getLong(RemindersDbAdapter.KEY_ROWID);

NotificationManager mgr= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

Intent ni= new Intent(this, ReminderEditActivity.class);
ni.putExtra(RemindersDbAdapter.KEY_ROWID,rid);

PendingIntent pi = PendingIntent.getActivity(this,0,ni,PendingIntent.FLAG_ONE_SHOT);

Notification n=new Notification(android.R.drawable.stat_sys_warning, getString(R.string.notify_new_task_message),System.currentTimeMillis());

n.setLatestEventInfo(this,getString(R.string.notify_new_task_title),getString(R.string.notify_new_task_message),pi);

n.defaults = Notification.DEFAULT_SOUND;
n.flags = Notification.FLAG_AUTO_CANCEL;

//an issue could occur if user ever enter over MAX INT VALUE
//doubt tht will happen

int id=(int)((long)rid);
mgr.notify(id,n);



}
}
