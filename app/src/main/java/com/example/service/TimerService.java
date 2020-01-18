package com.example.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TimerService extends Service {
    private static final String TAG = "TimerService";
    public static final String ACTION_CLOSE = "TIMER_SERVICE_ACTION_CLOSE";
    public static int NOT_ID=1;
    private CountDownTimer countDownTimer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        notificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");

        if(ACTION_CLOSE.equals(intent.getAction())){
            stopSelf();
        } else{
            startCountDown(1000 * 10L, 1000);
            startForeground(NOT_ID, createNotification(1000));

        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        stopCountDownTimer();
        super.onDestroy();
    }

        private void startCountDown(long time, long period){
        countDownTimer = new CountDownTimer(time, period) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick() called with: millisUntilFinished = [" + millisec(millisUntilFinished) + "]");

                startForeground(NOT_ID++ , createNotification(millisec(millisUntilFinished)));

//                    updateNotification(createNotification(millisec(millisUntilFinished)));
            }


            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish() called");
                stopSelf();
            }

        };
        countDownTimer.start();
        }

        private void stopCountDownTimer(){
            if(countDownTimer != null){
                countDownTimer.cancel();
                countDownTimer = null;
            }

        }
        private long millisec(long time){
            return time/1000;
        }
        private Notification createNotification(long countTime){

        Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            Intent intentCloseService = new Intent(this, TimerService.class);
            intentCloseService.setAction(ACTION_CLOSE);
            PendingIntent pendingIntentClose = PendingIntent.getService(this,0,intentCloseService, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"1");
            builder.setContentTitle("Privki service")
                    .setContentText("Timer" + countTime)
                    .setOnlyAlertOnce(true)
                    .addAction(0,"close", pendingIntentClose)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent);

        return builder.build();
        }
        private void updateNotification(Notification not){
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(NOT_ID, not);
        }
        private void notificationChannel(){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                CharSequence name = "chanel";
                String description = "description";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("1", name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

        }
}
