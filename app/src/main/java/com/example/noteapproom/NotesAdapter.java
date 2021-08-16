package com.example.noteapproom;


import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;


import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private static final String TAG = "NotesAdapter";
    private ArrayList<Notes> notesList = new ArrayList<>();
    private Context context;
    NotesDatabase database;


    public NotesAdapter(ArrayList<Notes> notes, Context context) {
        this.context = context;
        this.notesList = notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        database = NotesDatabase.getInstance(context);
        Notes notes = notesList.get(position);
        holder.body.setText(notes.getBody());
        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //notify when data is deleted
                int position = holder.getAdapterPosition();
                Notes d = notesList.get(position);
                int sID = d.getId();
                database.notesDao().delete(sID)
                        .subscribeOn(Schedulers.computation())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG,"completed");
                                /***********get all data************/
                                database.notesDao().getAll()
                                        .subscribeOn(Schedulers.computation())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new SingleObserver<List<Notes>>() {
                                            @Override
                                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                                            }

                                            @Override
                                            public void onSuccess(@io.reactivex.annotations.NonNull List<Notes> posts) {
                                                setList((ArrayList<Notes>) posts);
                                                notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                                Log.d("error in get = ",e.getMessage()+"");

                                            }
                                        });
                                /*******************************/
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }
                        });


            }
        });
        holder.bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //initialize main data
                Notes d =notesList.get(holder.getAdapterPosition());
                //get id;
                int sID = d.getId();
                //get text
                String sBody = d.getBody();

                //create dialog

                Dialog dialog = new Dialog(context);
                //set content view
                dialog.setContentView(R.layout.dialog_update);

                //initialize width
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                //initialize height
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                //set Layout
                dialog.getWindow().setLayout(width,height);
                //show dialog
                dialog.show();

                //initialize and assign variable
                EditText editText = dialog.findViewById(R.id.edit_text);
                Button btUpdate = dialog.findViewById(R.id.bt_update);

                //set text on edit text
                editText.setText(sBody);
                btUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String uBody = editText.getText().toString().trim();
                        notesList.clear();
                        Notes notes1 = new Notes();
                        database.notesDao().update(sID,uBody)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        notes1.setBody(uBody);
                                        notesList.add(notes1);
                                        holder.body.setText(uBody);
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {

                                    }
                                });





                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void setList(ArrayList<Notes> notesList) {
        this.notesList = notesList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView body;
        ImageView bt_delete,bt_edit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.text_view);
            bt_delete = itemView.findViewById(R.id.bt_delete);
            bt_edit = itemView.findViewById(R.id.bt_edit);


        }
    }
}

