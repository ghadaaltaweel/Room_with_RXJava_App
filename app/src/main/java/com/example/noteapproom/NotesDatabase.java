package com.example.noteapproom;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Notes.class,version = 1)
public abstract class NotesDatabase extends RoomDatabase {
    private static NotesDatabase instance;
    private static String DATABASE_NAME="notes_database" ;
    public abstract NotesDao notesDao();



    public static NotesDatabase getInstance(Context context) {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context,NotesDatabase.class,DATABASE_NAME)
                    .fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }
}
