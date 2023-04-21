package com.example.simplediaryapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private String stringDateSelected;
    private CalendarView calendarView;
    Button writeBtn;
    ImageButton menuBtn;
    boolean find = false;

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
                //checkTitleForNotes(stringDateSelected);
                Intent intent = new Intent(MainActivity.this,DiaryDetailsActivity.class);
                intent.putExtra("date",stringDateSelected);
                startActivity(intent);
//                if (find!=true){
//                    Intent intent = new Intent(MainActivity.this,DiaryDetailsActivity.class);
//                    intent.putExtra("date",stringDateSelected);
//                    startActivity(intent);
//                }
            }
        });

        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringDateSelected = todaysDate();
                Intent intent = new Intent(MainActivity.this,DiaryDetailsActivity.class);
                intent.putExtra("date",stringDateSelected);
                startActivity(intent);
//                checkTitleForNotes(stringDateSelected);
//                if (find!=true){
//                    Intent intent = new Intent(MainActivity.this,DiaryDetailsActivity.class);
//                    intent.putExtra("date",stringDateSelected);
//                    startActivity(intent);
//                }
            }
        });
        menuBtn.setOnClickListener((v)->showMenu());

    }
    void showMenu(){
        //Disply Menu
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,menuBtn);
        popupMenu.getMenu().add("View Data");
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
                if (menuItem.getTitle()=="View Data"){
                    startActivity(new Intent(MainActivity.this,RecyclerActivity.class));
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
    void searchTitleForNotes(String searchString) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference notesRef = FirebaseFirestore.getInstance()
                .collection("notes")
                .document(currentUser.getUid())
                .collection("my_notes");

        Query query = notesRef.whereEqualTo("title", searchString);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    String docId = querySnapshot.getDocuments().get(0).getId();
                    DocumentReference documentReference = notesRef.document(docId);
                    documentReference.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Intent intent = new Intent(MainActivity.this, DiaryDetailsActivity.class);
                                    intent.putExtra("title", documentSnapshot.getString("title"));
                                    intent.putExtra("content", documentSnapshot.getString("content"));
                                    intent.putExtra("docId", documentSnapshot.getId());
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "on failure", e);
                                }
                            });
                    Log.d(TAG, "String '" + searchString + " found in title field of document ID " + docId);
                } else {
                    Log.d(TAG, "String '" + searchString + " not found in title field of any note");
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }


//    void checkTitleForNotes(String searchString) {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        CollectionReference notesRef = FirebaseFirestore.getInstance()
//                .collection("notes")
//                .document(currentUser.getUid())
//                .collection("my_notes");
//
//        Query query = notesRef.whereEqualTo("title", searchString);
//
//        query.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                QuerySnapshot querySnapshot = task.getResult();
//                if (!querySnapshot.isEmpty()) {
//                    find = true;
//                    String docId = querySnapshot.getDocuments().get(0).getId();
//                    getDataForDocument(docId);
//                    Log.d(TAG, "String '" + searchString + " found in title field of document ID " + docId);
//                } else {
//                    find = false;
//                    Log.d(TAG, "String '" + searchString + " not found in title field of any note");
//                }
//            } else {
//                Log.d(TAG, "Error getting documents: ", task.getException());
//            }
//        });
//    }
//    void getDataForDocument(String docId) {
//        DocumentReference documentReference;
//        documentReference = Utility.getCollectionReferenceForNotes().document(docId);
//        documentReference.get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        Intent intent = new Intent(MainActivity.this,DiaryDetailsActivity.class);
//                        intent.putExtra("title", documentSnapshot.getString("title"));
//                        intent.putExtra("content", documentSnapshot.getString("content"));
//                        intent.putExtra("docId", documentSnapshot.getId());
//                        startActivity(intent);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "on failure", e);
//                    }
//                });
//    }


}