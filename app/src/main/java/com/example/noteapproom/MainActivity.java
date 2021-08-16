package com.example.noteapproom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    EditText body_txt;
    Button bt_add,bt_reset;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Notes> notesList = new ArrayList<>();
    NotesDatabase database;
    NotesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        body_txt = findViewById(R.id.body);
        bt_add = findViewById(R.id.bt_add);
        bt_reset = findViewById(R.id.bt_reset);
        recyclerView = findViewById(R.id.recycler_view);

        //initialize database
        database = NotesDatabase.getInstance(this);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NotesAdapter(notesList,MainActivity.this);
        recyclerView.setAdapter(adapter);

        /***********get all data************/
        database.notesDao().getAll()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Notes>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Notes> posts) {
                        adapter.setList((ArrayList<Notes>) posts);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("error in get = ",e.getMessage()+"");

                    }
                });
        /*******************************/









        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sbody = body_txt.getText().toString().trim();
                Log.d(TAG,"sbody="+sbody);
                if(!sbody.equals(""))
                {
                    Log.d(TAG,"in in");
                    //when text is not empty
                    //initialize Notes model
                    Notes notes = new Notes(sbody);
                    notes.setBody(sbody);
                    notesList.add(notes);
//                    database.notesDao().insert(notes);
                    Log.d(TAG,"txt="+notes.getBody().toString());
                    body_txt.setText("");
                    adapter.notifyDataSetChanged();
                    database.notesDao().insert(new Notes(sbody))
                            .subscribeOn(Schedulers.computation())
                            .subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onComplete() {
                                    Log.d(TAG,"completed");
                                    Log.d(TAG,"current thread="+Thread.currentThread().getName());

                                    /***********get all data************/
                                    database.notesDao().getAll()
                                            .subscribeOn(Schedulers.computation())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new SingleObserver<List<Notes>>() {
                                                @Override
                                                public void onSubscribe(@NonNull Disposable d) {

                                                }

                                                @Override
                                                public void onSuccess(@NonNull List<Notes> posts) {
                                                    adapter.setList((ArrayList<Notes>) posts);
                                                    adapter.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onError(@NonNull Throwable e) {
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

            }
        });


        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesList.clear();
                database.notesDao().reset()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG,"delete completed");
                                /***********get all data************/
                                database.notesDao().getAll()
                                        .subscribeOn(Schedulers.computation())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new SingleObserver<List<Notes>>() {
                                            @Override
                                            public void onSubscribe(@NonNull Disposable d) {

                                            }

                                            @Override
                                            public void onSuccess(@NonNull List<Notes> posts) {
                                                adapter.setList((ArrayList<Notes>) posts);
                                                adapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onError(@NonNull Throwable e) {
                                                Log.d("error in get = ",e.getMessage()+"");

                                            }
                                        });
                                /*******************************/

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }
                        });
                adapter.notifyDataSetChanged();

            }
        });
    }
}