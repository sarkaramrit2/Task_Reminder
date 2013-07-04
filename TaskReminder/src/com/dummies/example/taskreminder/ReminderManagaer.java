package com.dummies.example.taskreminder;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ReminderManagaer{

private Context C;
private AlarmManager AM;
public ReminderManagaer(Context cxt){

C=cxt;
AM=(AlarmManager)cxt.getSystemService(Context.ALARM_SERVICE);

}

public void setReminder(Long taskId, Calendar when){
Intent i = new Intent(C,OnAlarmReciever.class);
i.putExtra(RemindersDbAdapter.KEY_ROWID, (long)taskId);

PendingIntent pi = PendingIntent.getBroadcast(C,0,i,PendingIntent.FLAG_ONE_SHOT);

AM.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(),pi);

}
}