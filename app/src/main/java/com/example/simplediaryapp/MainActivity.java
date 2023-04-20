package com.example.simplediaryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private String stringDateSelected;
    private CalendarView calendarView;
    Button writeBtn;
    ImageButton menuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        writeBtn = findViewById(R.id.writeBtn);
        menuBtn = findViewById(R.id.menu_btn);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                stringDateSelected = Integer.toString(i2) +" - "+ Integer.toString(i1+1) +" - "+ Integer.toString(i);

                Intent intent = new Intent(MainActivity.this,DiaryDetailsActivity.class);
                intent.putExtra("date",stringDateSelected);
                startActivity(intent);
            }
        });

        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringDateSelected = todaysDate();
                Intent intent = new Intent(MainActivity.this,DiaryDetailsActivity.class);
                intent.putExtra("date",stringDateSelected);
                startActivity(intent);
            }
        });
        menuBtn.setOnClickListener((v)->showMenu());

    }
    void showMenu(){
        //Disply Menu
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
    public String todaysDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String stringDate = Integer.toString(day) +" - "+ Integer.toString(month) +" - "+ Integer.toString(year);
        return stringDate;
    }


}