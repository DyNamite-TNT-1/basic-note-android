package com.example.basicnote;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.basicnote.models.Note;
import com.example.basicnote.my_interface.IClickItemNoteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView rvNote;

    FloatingActionButton fab;
    ArrayList<Note> arrayList = new ArrayList<>();
    NoteRvAdapter arrayAdapter;

    int id;

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "onActivityResult: ");

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            //Extract data here
                            int positionInt = intent.getIntExtra("position", -1);
                            String idStr = intent.getStringExtra("id");
                            String title = intent.getStringExtra("title");
                            String desc = intent.getStringExtra("desc");
                            Boolean isDone = intent.getBooleanExtra("done", false);
                            if (positionInt != -1) {
                                int position = positionInt;
                                System.out.println(position);
                                System.out.println(arrayList.size());
                                arrayList.set(position, new Note(idStr, title, desc, isDone));
                            } else {
                                arrayList.add(new Note(Integer.toString(id++), title, desc, isDone));
                            }
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = 0;

        fakeData();

        fab = findViewById(R.id.fabAdd);
        rvNote = findViewById(R.id.rvNote);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        rvNote.setLayoutManager(layoutManager);

        arrayAdapter = new NoteRvAdapter(arrayList, new IClickItemNoteListener() {
            @Override
            public void onClickItemNote(Note note) {
                Intent intent = new Intent(MainActivity.this, AddNewNote.class);
                intent.putExtra("position", arrayList.indexOf(note));
                intent.putExtra("id", note.getId());
                intent.putExtra("title", note.getTitle());
                intent.putExtra("desc", note.getDesc());
                intent.putExtra("done", note.getDone());
                activityLauncher.launch(intent);
            }

            @Override
            public void onDeleteItemNote(Note note) {
                arrayList.remove(note);
                arrayAdapter.notifyDataSetChanged();
            }
        });

        rvNote.setAdapter(arrayAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                arrayList.remove(position);
                arrayAdapter.notifyDataSetChanged();
            }
        });

        itemTouchHelper.attachToRecyclerView(rvNote);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddNewNote.class);
            activityLauncher.launch(intent);
        });
    }

    private void setControl() {
        fab = findViewById(R.id.fabAdd);
        rvNote = findViewById(R.id.rvNote);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        rvNote.setLayoutManager(layoutManager);

        rvNote.setAdapter(arrayAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvNote.addItemDecoration(itemDecoration);
    }

    public void fakeData() {
        ArrayList<String> fakeTitle;
        ArrayList<Boolean> fakeDone;
        fakeTitle = new ArrayList<>(Arrays.asList("Do task 1", "Do research 1", "Check task 2", "Finish task 3", "Review task 4", "Do research 2", "Create task 5", "Do task 5"));
        fakeDone = new ArrayList<>(Arrays.asList(true, true, false, true, false, false, true, true));
        String desc = "This is note description of task ";

        for (int i = 0; i < fakeTitle.size(); i++) {
            arrayList.add(new Note(Integer.toString(i), fakeTitle.get(i), desc.concat(Integer.toString(i)), fakeDone.get(i)));
        }
        id = arrayList.size();
    }
}