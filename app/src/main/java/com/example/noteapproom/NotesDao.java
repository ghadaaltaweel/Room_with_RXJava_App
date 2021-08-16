package com.example.noteapproom;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface NotesDao {
    @Insert
    Completable insert(Notes notes);

    @Query("SELECT * FROM notes_table")
    Single<List<Notes>> getAll();


    @Query("DELETE  FROM notes_table WHERE id = :sID")
    Completable delete(int sID);
//
    //delete all queries
    @Query("DELETE  FROM notes_table")
    Completable reset();

    @Query("UPDATE notes_table SET body = :sBody WHERE id = :sID")
    Completable update(int sID,String sBody);
}
