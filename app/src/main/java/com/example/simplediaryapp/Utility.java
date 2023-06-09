package com.example.simplediaryapp;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;


public class Utility {
    static void showToast(Context context,String Msg){
        Toast.makeText(context,Msg,Toast.LENGTH_SHORT).show();
    }
    static CollectionReference getCollectionReferenceForNotes(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes")
                .document(currentUser.getUid()).collection("my_notes");
    }
    static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("hh:mm:ss a").format(timestamp.toDate());
    }
}
