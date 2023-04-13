package com.example.basicnote;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basicnote.database.NoteDatabase;
import com.example.basicnote.models.Note;
import com.example.basicnote.my_interface.IClickItemNoteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvNote;
    private FloatingActionButton fab;
    private List<Note> listNotes;
    private NoteRvAdapter noteRvAdapter;
    private TextView tvDeleteAll;
    private EditText edtSearch;
    private SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                noteRvAdapter.notifyDataSetChanged();
                pullToRefresh.setRefreshing(false);
            }
        });


        noteRvAdapter = new NoteRvAdapter(listNotes, new IClickItemNoteListener() {
            @Override
            public void onClickItemNote(Note note) {
                Intent intent = new Intent(MainActivity.this, AddNewNote.class);
                intent.putExtra("object_note", note);
                activityLauncher.launch(intent);
            }

            @Override
            public void onDeleteItemNote(Note note) {
                clickDeleteNote(note);
            }
        });

        listNotes = new ArrayList<>();
        loadData();

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        rvNote.setLayoutManager(layoutManager);

        rvNote.setAdapter(noteRvAdapter);

//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//                listNotes.remove(position);
//                noteRvAdapter.notifyDataSetChanged();
//            }
//        });
//
//        itemTouchHelper.attachToRecyclerView(rvNote);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddNewNote.class);
            activityLauncher.launch(intent);
        });

        tvDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDeleteAllNote();
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    handleSearchNote();
                }
                return false;
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handleSearchNote();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void initUI() {
        //invisible back button
        ImageButton imageButton = findViewById(R.id.action_bar_back);
        imageButton.setVisibility(View.INVISIBLE);

        fab = findViewById(R.id.fabAdd);
        rvNote = findViewById(R.id.rvNote);
        tvDeleteAll = findViewById(R.id.tvDeleteAll);
        edtSearch = findViewById(R.id.edtSearch);
        pullToRefresh = findViewById(R.id.pullToRefresh);
    }

    public void refreshData() {
        loadData();
    }

    private void loadData() {
        listNotes = NoteDatabase.getInstance(MainActivity.this).noteDAO().getListNote();
        noteRvAdapter.setData(listNotes);
    }

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        loadData();
                    }
                }
            }
    );

    private void clickDeleteNote(Note note) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Confirm delete Note")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Delete note
                        NoteDatabase.getInstance(MainActivity.this).noteDAO().deleteNote(note);
                        Toast.makeText(MainActivity.this, "Delete note successfully", Toast.LENGTH_SHORT).show();

                        loadData();
                    }
                }).setNegativeButton("No", null).show();
    }

    private void clickDeleteAllNote() {

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Confirm delete all Note")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Delete note
                        NoteDatabase.getInstance(MainActivity.this).noteDAO().deleteAllNote();
                        Toast.makeText(MainActivity.this, "Delete all note successfully", Toast.LENGTH_SHORT).show();

                        loadData();
                    }
                }).setNegativeButton("No", null).show();
    }

    private void handleSearchNote() {
        String strKeyword = edtSearch.getText().toString().trim();
        listNotes = new ArrayList<>();
        listNotes = NoteDatabase.getInstance(MainActivity.this).noteDAO().searchNote(strKeyword);
        noteRvAdapter.setData(listNotes);
    }
}