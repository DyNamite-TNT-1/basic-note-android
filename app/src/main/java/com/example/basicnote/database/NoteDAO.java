package com.example.basicnote.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.basicnote.models.Note;

import java.util.List;

@Dao
public interface NoteDAO {
    @Insert
    void insertNote(Note note);

    @Query("SELECT * FROM note")
    List<Note> getListNote();

    @Query("SELECT * FROM note where `title`= :title and `desc`= :desc")
    List<Note> checkNote(String title, String desc);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM note")
    void deleteAllNote();
    @Query("SELECT * FROM note where title LIKE '%' || :title || '%'")
    List<Note> searchNote (String title);
}
