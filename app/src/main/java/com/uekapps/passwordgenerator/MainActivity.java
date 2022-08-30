package com.uekapps.passwordgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton fab;
    Adapter adapter;
    List<Model> passwordList;
    DatabaseClass databaseClass;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        recyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fab);
        coordinatorLayout = findViewById(R.id.layout_main);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPasswordActivity.class);
                startActivity(intent);
            }
        });

        passwordList = new ArrayList<>();
        databaseClass = new DatabaseClass(this);
        fetchAllPasswordsFromDatabase();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, MainActivity.this, passwordList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    private void fetchAllPasswordsFromDatabase() {
        Cursor cursor = databaseClass.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                passwordList.add(new Model(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchBar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search passwords here");

        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        };

        searchView.setOnQueryTextListener(listener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_passwords) {
            deleteAllPasswords();
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllPasswords() {
        DatabaseClass db = new DatabaseClass(MainActivity.this);
        db.deleteAllPasswords();
        recreate();
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Model item = adapter.getList().get(position);

            adapter.removeItem(viewHolder.getAdapterPosition());

            @SuppressLint("ShowToast") Snackbar snackbar = Snackbar.make(coordinatorLayout, "Item deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            adapter.restoreItem(item, position);
                            recyclerView.scrollToPosition(position);
                        }
                    }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);

                            if (!(event == DISMISS_EVENT_ACTION)) {
                                DatabaseClass db = new DatabaseClass(MainActivity.this);
                                db.deleteSingleItem(item.getId());
                            }
                        }
                    });
        }
    };
}