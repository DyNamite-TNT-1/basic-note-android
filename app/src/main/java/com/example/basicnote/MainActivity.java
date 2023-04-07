package com.example.basicnote;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.basicnote.models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ListView lvNote;

    FloatingActionButton fab;
    ArrayList<Note> arrayList = new ArrayList<>();
    NoteAdapter arrayAdapter;

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
                            String idStr = intent.getStringExtra("id");
                            String title = intent.getStringExtra("title");
                            String desc = intent.getStringExtra("desc");
                            Boolean isDone = intent.getBooleanExtra("done", false);
                            if (idStr != null) {
                                int position = Integer.parseInt(idStr);
                                arrayList.set(position, new Note(idStr, title, desc, isDone));
                            } else {
                                int length = arrayList.size();
                                arrayList.add(new Note(Integer.toString(length), title, desc, isDone));
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

        setControl();
        fakeData();
        arrayAdapter = new NoteAdapter(this, arrayList);
        lvNote.setAdapter(arrayAdapter);

        lvNote.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note noteItem = arrayList.get(i);
                System.out.println(noteItem.getTitle());
                System.out.println(noteItem.getDesc());
                Intent intent = new Intent(MainActivity.this, AddNewNote.class);
                intent.putExtra("id", noteItem.getId());
                intent.putExtra("title", noteItem.getTitle());
                intent.putExtra("desc", noteItem.getDesc());
                intent.putExtra("done", noteItem.getDone());
                activityLauncher.launch(intent);
            }
        }));

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddNewNote.class);
            activityLauncher.launch(intent);
        });
    }

    private void setControl() {
        lvNote = findViewById(R.id.lvNote);
        fab = findViewById(R.id.fabAdd);
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
    }
}