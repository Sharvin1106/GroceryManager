package com.example.grocerymanager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Notification_ON_time extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification__o_n_time);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Breakfast_Time();
                Lunch_Time();
                Dinner_Time();
                Exp_Date();
            }

            private void Breakfast_Time(){
                createNotificationChannel1();
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR_OF_DAY, 7);
                calendar1.set(Calendar.MINUTE, 0);
                calendar1.set(Calendar.SECOND, 0);

                Intent intent = new Intent(getApplicationContext(),Notification_receiver_break.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication(),100,intent,0);
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar1.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
            }

            private void Lunch_Time(){
                createNotificationChannel2();
                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(Calendar.HOUR_OF_DAY, 11);
                calendar2.set(Calendar.MINUTE, 0);
                calendar2.set(Calendar.SECOND, 0);
                Intent intent1 = new Intent(getApplicationContext(),Notification_receiver_lunch.class);
                PendingIntent pendingIntent1= PendingIntent.getBroadcast(getApplication(),200,intent1,0);
                AlarmManager alarmManager1 = (AlarmManager)getSystemService(ALARM_SERVICE);
                alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP,calendar2.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent1);
            }

            private void Dinner_Time(){
                createNotificationChannel3();
                Calendar calendar3 = Calendar.getInstance();
                calendar3.set(Calendar.HOUR_OF_DAY, 18);
                calendar3.set(Calendar.MINUTE, 0);
                calendar3.set(Calendar.SECOND, 0);
                Intent intent2 = new Intent(getApplicationContext(),Notification_receiver_Dinner.class);
                PendingIntent pendingIntent2= PendingIntent.getBroadcast(getApplication(),300,intent2,0);
                AlarmManager alarmManager2 = (AlarmManager)getSystemService(ALARM_SERVICE);
                alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP,calendar3.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent2);
            }

            private void Exp_Date(){
                final ArrayList<String> list = new ArrayList();
                final ArrayList<String> expiry = new ArrayList();
                String pattern = "mm/dd/yyyy";
                @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat(pattern);
                Date today = Calendar.getInstance().getTime();
                String tod = df.format(today);


                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,0);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);

                Intent intent3 = new Intent(getApplicationContext(),Notification_receiver_exp.class);
                PendingIntent pendingIntent3 = PendingIntent.getBroadcast(getApplication(),400,intent3,0);
                AlarmManager alarmManager3 = (AlarmManager)getSystemService(ALARM_SERVICE);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("grocery_list");
                ref.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            final grocery grocery_list = dataSnapshot.getValue(grocery.class);
                            final String exp = grocery_list.getDate();
                            expiry.add(exp);

                            if(exp==tod){
                                createNotificationChannel();
                                alarmManager3.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent3);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

            }

            private  void createNotificationChannel(){

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    CharSequence name = "Notification expiry ";
                    String description = "Expiry reminder channel";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel("expiry", name, importance);
                    channel.setDescription(description);

                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }
            }
            private void createNotificationChannel1(){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    CharSequence name = "break";
                    String desciption = "breakfast Time";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel("break",name,importance);
                    channel.setDescription(desciption);

                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }
            }
            private void createNotificationChannel2(){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    CharSequence name = "lunch";
                    String desciption = "Lunch Time";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel("lunch",name,importance);
                    channel.setDescription(desciption);

                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }
            }
            private void createNotificationChannel3(){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    CharSequence name = "dinner";
                    String desciption = "Dinner Time";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel("dinner",name,importance);
                    channel.setDescription(desciption);

                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }
            }
        });
    }

}