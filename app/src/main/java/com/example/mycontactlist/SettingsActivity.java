package com.example.mycontactlist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setThemeBasedOnPreference();
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = new Toolbar(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.settings);
        }

        RadioGroup sortGroup = findViewById(R.id.sortRadioGroup);
        SwitchMaterial darkModeSwitch = findViewById(R.id.darkModeSwitch);

        switch (preferences.getInt("sort_mode", ContactUtils.SORT_NAME_ASC)) {
            case ContactUtils.SORT_NAME_DESC:
                sortGroup.check(R.id.radioNameDesc);
                break;
            case ContactUtils.SORT_NEWEST:
                sortGroup.check(R.id.radioNewest);
                break;
            case ContactUtils.SORT_OLDEST:
                sortGroup.check(R.id.radioOldest);
                break;
            case ContactUtils.SORT_NAME_ASC:
            default:
                sortGroup.check(R.id.radioNameAsc);
                break;
        }

        darkModeSwitch.setChecked(preferences.getBoolean("dark_mode", false));

        sortGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioNameAsc) {
                preferences.edit().putInt("sort_mode", ContactUtils.SORT_NAME_ASC).apply();
            } else if (checkedId == R.id.radioNameDesc) {
                preferences.edit().putInt("sort_mode", ContactUtils.SORT_NAME_DESC).apply();
            } else if (checkedId == R.id.radioNewest) {
                preferences.edit().putInt("sort_mode", ContactUtils.SORT_NEWEST).apply();
            } else if (checkedId == R.id.radioOldest) {
                preferences.edit().putInt("sort_mode", ContactUtils.SORT_OLDEST).apply();
            }
        });

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("dark_mode", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        });
    }

    private void setThemeBasedOnPreference() {
        boolean darkMode = preferences.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
