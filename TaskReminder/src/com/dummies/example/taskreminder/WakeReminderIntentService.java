package com.dummies.example.taskreminder;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public abstract class WakeReminderIntentService extends IntentService{

abstract void doReminderWork(Intent i);

public static final String LOCK_NAME_STATIC = "com.dummies.android.taskreminder.Static";


private static WakeLock lockStatic=null;


public static void acquireStaticLock(Context c){
getLock(c).acquire();

}

synchronized private static PowerManager.WakeLock getLock (Context c){

if(lockStatic==null){
PowerManager mgr= (PowerManager)c.getSystemService(Context.POWER_SERVICE);

lockStatic=mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,LOCK_NAME_STATIC);

lockStatic.setReferenceCounted(true);


}

return(lockStatic);

}

public WakeReminderIntentService(String name){
super(name);

}

@Override
final protected void onHandleIntent(Intent i){
try{
doReminderWork(i);
}
finally{
getLock(this).release();
}

}


}
