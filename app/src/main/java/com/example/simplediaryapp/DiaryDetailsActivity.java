package com.example.simplediaryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class DiaryDetailsActivity extends AppCompatActivity {

    EditText contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    TextView dateTextView;
    TextView deleteNoteTextViewBtn;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_details);

        contentEditText = findViewById(R.id.diary_content_text);
        dateTextView = findViewById(R.id.diary_date_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewBtn = findViewById(R.id.delete_note_text_view_btn);

        date = getIntent().getStringExtra("date");
        dateTextView.setText(date);

        saveNoteBtn.setOnClickListener((v)->saveNote());

    }
    void saveNote(){
        String noteTitle = dateTextView.getText().toString();
        String noteContent = contentEditText.getText().toString();
        if (noteContent == null || noteContent.isEmpty()){
            contentEditText.setError("Content is required");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);
    }
    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
//        if (isEditMode){
//            //update the note
//            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
//        }else {
//            //create new note
//            documentReference = Utility.getCollectionReferenceForNotes().document();
//        }
        documentReference = Utility.getCollectionReferenceForNotes().document();
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //notes added
                    Utility.showToast(DiaryDetailsActivity.this,"Note added Successfully");
                    finish();
                }else{
                    Utility.showToast(DiaryDetailsActivity.this,"Failed While adding note");
                }
            }
        });
    }
}