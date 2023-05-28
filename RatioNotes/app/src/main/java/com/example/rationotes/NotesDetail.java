package com.example.rationotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;




public class NotesDetail extends AppCompatActivity {
EditText titleET,ContentET;
ImageButton saveNoteBtn;
TextView pageTitletextView;
String title,content,docId;
boolean isEditMode =false;
TextView deletNotetextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_detail);

        titleET=findViewById(R.id.notestitle);
        ContentET=findViewById(R.id.contentEt);
        saveNoteBtn=findViewById(R.id.savenote);
        pageTitletextView=findViewById(R.id.page_Title);
        deletNotetextView=findViewById(R.id.delete_note_text_view_btn);

        //Receive data from the selected note
        title =getIntent().getStringExtra("title");
        content =getIntent().getStringExtra("content");
        docId =getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode=true;
        }
        titleET.setText(title);
        ContentET.setText(content);
        if(isEditMode){
            pageTitletextView.setText("Edit Notes");
            deletNotetextView.setVisibility(View.VISIBLE);
        }




        saveNoteBtn.setOnClickListener((v)->SaveNote());
        deletNotetextView.setOnClickListener(v-> deletNoteFromFirebase());

    }
    void SaveNote(){
        String noteTitle=titleET.getText().toString();
        String noteContent= ContentET.getText().toString();
        if(noteTitle==null || noteTitle.isEmpty()){
            titleET.setError("Title is Required");
            return;
        }
        Note note=new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());
        saveNoteToFirebase(note);

    }
    void saveNoteToFirebase(Note note){

        DocumentReference documentReference;
        if(isEditMode){
            //Updating existing data
            documentReference =utility.getCollectionReferenceForNotes().document(docId);
        }else {
            //Create new data
            documentReference =utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Note added to database
                    utility.showToast(NotesDetail.this,"Note added succesfully");
                    finish();

                }else{
                    //Notes can't be added to database
                    utility.showToast(NotesDetail.this,"Failed while adding note");

                }
            }
        });

    }
    void deletNoteFromFirebase(){
        DocumentReference documentReference;
        documentReference =utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Note added to database
                    utility.showToast(NotesDetail.this,"Note deleted succesfully");
                    finish();

                }else{
                    //Notes can't be added to database
                    utility.showToast(NotesDetail.this,"Failed to delete note");

                }
            }
        });
    }
}


