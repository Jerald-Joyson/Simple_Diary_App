package com.example.simplediaryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class DiaryDetailsActivity extends AppCompatActivity {

    EditText diarycontentEditText;
    ImageButton saveNoteBtn;
    TextView dateTextView;
    TextView deleteNoteTextViewBtn;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_details);

        diarycontentEditText = findViewById(R.id.diary_content_text);
        dateTextView = findViewById(R.id.diary_date_text);

        date = getIntent().getStringExtra("date");
        dateTextView.setText(date);



    }
}