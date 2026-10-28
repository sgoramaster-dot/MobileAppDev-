package com.example.mycontactlist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ContactRepository repository;
    private ContactAdapter adapter;
    private SharedPreferences preferences;
    private TextView emptyStateText;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        applyThemeFromSettings();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        repository = new ContactRepository(this);
        recyclerView = findViewById(R.id.contactRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);
        FloatingActionButton fab = findViewById(R.id.fabAddContact);
        com.google.android.material.textfield.TextInputEditText searchInput = findViewById(R.id.searchEditText);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapter(this, contact -> openDetails(contact.getId()));
        recyclerView.setAdapter(adapter);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                refreshList();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        fab.setOnClickListener(v -> openForm(-1L));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void applyThemeFromSettings() {
        boolean darkMode = preferences.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void refreshList() {
        String query = ((com.google.android.material.textfield.TextInputEditText) findViewById(R.id.searchEditText)).getText() == null
                ? ""
                : ((com.google.android.material.textfield.TextInputEditText) findViewById(R.id.searchEditText)).getText().toString();

        List<Contact> contacts = new ArrayList<>();
        if (TextUtils.isEmpty(query)) {
            contacts.addAll(repository.getAll());
        } else {
            contacts.addAll(repository.search(query));
        }

        int sortMode = preferences.getInt("sort_mode", ContactUtils.SORT_NAME_ASC);
        ContactUtils.sortContacts(contacts, sortMode);

        adapter.submitList(contacts);
        emptyStateText.setVisibility(contacts.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void openDetails(long contactId) {
        Intent intent = new Intent(this, ContactDetailActivity.class);
        intent.putExtra(ContactDetailActivity.EXTRA_CONTACT_ID, contactId);
        startActivity(intent);
    }

    private void openForm(long contactId) {
        Intent intent = new Intent(this, ContactFormActivity.class);
        if (contactId > 0) {
            intent.putExtra(ContactFormActivity.EXTRA_CONTACT_ID, contactId);
        }
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_sort_name_asc) {
            preferences.edit().putInt("sort_mode", ContactUtils.SORT_NAME_ASC).apply();
            refreshList();
            return true;
        }
        if (itemId == R.id.action_sort_name_desc) {
            preferences.edit().putInt("sort_mode", ContactUtils.SORT_NAME_DESC).apply();
            refreshList();
            return true;
        }
        if (itemId == R.id.action_sort_newest) {
            preferences.edit().putInt("sort_mode", ContactUtils.SORT_NEWEST).apply();
            refreshList();
            return true;
        }
        if (itemId == R.id.action_sort_oldest) {
            preferences.edit().putInt("sort_mode", ContactUtils.SORT_OLDEST).apply();
            refreshList();
            return true;
        }
        if (itemId == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
