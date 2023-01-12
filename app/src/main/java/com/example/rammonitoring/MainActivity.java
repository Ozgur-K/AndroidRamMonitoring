package com.example.rammonitoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

//Icons https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html
//notification https://github.com/nadinCodeHat/Android-Notifications.git

public class MainActivity extends AppCompatActivity {


    public Context context;
    NotificationManagerCompat notificationManagerCompat;
    NotificationCompat.Builder builder1;
    final int NOTIFICATION_ID = 001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        Repeat();
        Notification();

    }

    public void Notification(){
        final String CHANNEL_ID1 = "basic notification channel";
        createNotificationChannel(CHANNEL_ID1);
        builder1 = new NotificationCompat.Builder(context, CHANNEL_ID1);
        notificationManagerCompat = NotificationManagerCompat.from(context);

        builder1.setPriority(NotificationCompat.PRIORITY_LOW);


    }

    private void createNotificationChannel(String CHANNEL) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_LOW; //LOW for mute

            NotificationChannel channel = new NotificationChannel(CHANNEL, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void RamInfo(){

        //region Getting RAM Info
        ActivityManager.MemoryInfo memoryInfo1 = new ActivityManager.MemoryInfo();
        ActivityManager activityManager1 = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        activityManager1.getMemoryInfo(memoryInfo1);
        double availableMegs = memoryInfo1.availMem / 0x100000L;

        String aMem = Double.toString(availableMegs);

        //Percentage can be calculated for API 16+
        double percentAvail = memoryInfo1.availMem / (double)memoryInfo1.totalMem * 100.0;
        //endregion

        //region Total Memory
        DecimalFormat decimalFormat = new DecimalFormat("0");
        String totalMem = decimalFormat.format((memoryInfo1.totalMem));
        totalMem = totalMem.substring(0, totalMem.length() - 6);
        totalMem = "Total Memory: " +  totalMem.concat("MB");

        //Log.d("tag",decimalFormat.format((memoryInfo1.totalMem)) );
        //Log.d("tag", totalMem );
        //endregion

        //region Available Memory
        String availMem = decimalFormat.format((memoryInfo1.availMem));
        availMem = availMem.substring(0, availMem.length() - 6);
        availMem = "Available Memory: " +  availMem.concat("MB");
        //endregion

        //region Percent Memory
        long l = Math.round(percentAvail);
        l = 100 - l; //l is now free mem percentage
        String percentMem = Long.toString(l);
        percentMem = "Memory in Use: " + percentMem.concat("%");
        //endregion

        //region Print
        TextView textView = findViewById(R.id.HelloWorldID);

        textView.setText(totalMem);
        textView.append("\n");
        textView.append(availMem);
        textView.append("\n");
        textView.append(percentMem);
        //endregion

        //region Notify

        if(l < 20){
            builder1.setSmallIcon(R.drawable.ic_baseline_filter_1_24);
        }else if((l >= 20) && (l < 30)){
            builder1.setSmallIcon(R.drawable.ic_baseline_filter_2_24);
        }else if((l >= 30) && (l < 40)){
            builder1.setSmallIcon(R.drawable.ic_baseline_filter_3_24);
        }else if((l >= 40) && (l < 50)){
            builder1.setSmallIcon(R.drawable.ic_baseline_filter_4_24);
        }else if((l >= 50) && (l < 60)){
            builder1.setSmallIcon(R.drawable.ic_baseline_filter_5_24);
        }else if((l >= 60) && (l < 70)){
            builder1.setSmallIcon(R.drawable.ic_baseline_filter_6_24);
        }else if((l >= 70) && (l < 80)){
            builder1.setSmallIcon(R.drawable.ic_baseline_filter_7_24);
        }else if((l >= 80) && (l < 90)){
            builder1.setSmallIcon(R.drawable.ic_baseline_filter_8_24);
        }else if(l >= 90){
            builder1.setSmallIcon(R.drawable.ic_baseline_filter_9_24);
        }else {
            Log.e("Notification", "Error on Notify Region");
        }


        builder1.setContentTitle(percentMem);
        builder1.setContentText(availMem);

        notificationManagerCompat.notify(NOTIFICATION_ID, builder1.build());
        //endregion

    }

    public void Repeat(){
        Timer timer1;
        final Handler handler1 = new Handler();
        timer1 = new Timer();

        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                //https://stackoverflow.com/questions/6313986/android-timer-timertask-causing-my-app-to-crash
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(context, "test", Toast.LENGTH_LONG).show();

                        RamInfo();
                    }
                });
            }
        }, 3000,1000);
    }
}