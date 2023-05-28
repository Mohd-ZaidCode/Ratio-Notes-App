package com.example.rationotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder>{
    Context context;


    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.titleTextview.setText(note.title);
        holder.contentTextView.setText(note.content);
        holder.timestamptextView.setText(utility.timestampToString(note.timestamp));


        holder.itemView.setOnClickListener(v ->{
            Intent intent =new Intent(context,NotesDetail.class);
            intent.putExtra("title",note.title);
            intent.putExtra("content",note.content);
            String docId= this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);


        } );

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextview,contentTextView,timestamptextView;

        public NoteViewHolder(@NonNull View itemView){
            super(itemView);
            titleTextview=itemView.findViewById(R.id.notestitleView);
            contentTextView=itemView.findViewById(R.id.notecontentView);
            timestamptextView=itemView.findViewById(R.id.timestampView);



        }
    }

}
