package com.example.campnewsadmin;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
public class NoteAdapter extends FirestoreRecyclerAdapter<Notes,NoteAdapter.NoteHolder> {
    private OnItemClickListener listener;



    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Notes> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteAdapter.NoteHolder noteHolder, int i, @NonNull Notes notes) {

        noteHolder.mail.setText(notes.getMail());
        noteHolder.name.setText(notes.getName());
        noteHolder.roll.setText(notes.getRoll());

        Uri imageUri = Uri.parse(notes.getProfilepic());
        try {
            Picasso.get().load(imageUri).placeholder(R.drawable.edit_image_logo).into(noteHolder.pic);
        }
        catch (Exception e) {

        }




    }

    @NonNull
    @Override
    public NoteAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list,parent,false);
        return new NoteHolder(v);
    }


     class NoteHolder extends RecyclerView.ViewHolder {

        CircleImageView pic;
        TextView name;
        TextView mail;
        TextView roll;

        public NoteHolder(View v) {
            super(v);
            pic=v.findViewById(R.id.profile);
            name=v.findViewById(R.id.name);
            mail=v.findViewById(R.id.mail);
            roll=v.findViewById(R.id.roll);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener!=null ){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot,int position);


    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;

    }


}
