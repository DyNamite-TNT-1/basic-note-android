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
                            int id = arrayList.size();
                            String title = intent.getStringExtra("title");
                            String desc = intent.getStringExtra("desc");
                            arrayList.add(new Note(Integer.toString(++id), title, desc, false));
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
        lvNote = findViewById(R.id.lvNote);
        FloatingActionButton fab = findViewById(R.id.fabAdd);

        fakeData();
        arrayAdapter = new NoteAdapter(this, arrayList);
        lvNote.setAdapter(arrayAdapter);

        lvNote.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note noteItem = (Note) lvNote.getSelectedItem();
                System.out.println(noteItem.getTitle());
                System.out.println(noteItem.getDesc());
            }
        }) );

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddNewNote.class);
            activityLauncher.launch(intent);
        });


    }

    public void fakeData() {
        ArrayList<String> fakeTitle;
        fakeTitle = new ArrayList<>(Arrays.asList("Do task 1", "Do research 1", "Check task 2", "Finish task 3", "Review task 4"));

        String desc = "This is description";

        for (int i = 0; i < fakeTitle.size(); i++) {
            arrayList.add(new Note(Integer.toString(i), fakeTitle.get(i), desc, false));
        }
    }
}