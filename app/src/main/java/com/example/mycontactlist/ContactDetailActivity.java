package com.example.mycontactlist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ContactDetailActivity extends AppCompatActivity {
    public static final String EXTRA_CONTACT_ID = "extra_contact_id";

    private ContactRepository repository;
    private Contact contact;
    private ImageView detailPhoto;
    private TextView detailName, detailPhone, detailEmail, detailAddress, detailNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        repository = new ContactRepository(this);
        detailPhoto = findViewById(R.id.detailPhoto);
        detailName = findViewById(R.id.detailName);
        detailPhone = findViewById(R.id.detailPhone);
        detailEmail = findViewById(R.id.detailEmail);
        detailAddress = findViewById(R.id.detailAddress);
        detailNotes = findViewById(R.id.detailNotes);

        Button callButton = findViewById(R.id.callButton);
        Button emailButton = findViewById(R.id.emailButton);
        Button mapButton = findViewById(R.id.mapButton);
        Button editButton = findViewById(R.id.editButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        long contactId = getIntent().getLongExtra(EXTRA_CONTACT_ID, -1L);
        if (contactId < 0) {
            Toast.makeText(this, "Contact not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        contact = repository.getById(contactId);
        if (contact == null) {
            Toast.makeText(this, "Contact not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bindContact();
        callButton.setOnClickListener(v -> startDialer());
        emailButton.setOnClickListener(v -> startEmail());
        mapButton.setOnClickListener(v -> startMap());
        editButton.setOnClickListener(v -> openEditor());
        deleteButton.setOnClickListener(v -> showDeleteDialog());
    }

    private void bindContact() {
        detailName.setText(contact.getFullName());
        detailPhone.setText(contact.getPhone());
        detailEmail.setText(TextUtils.isEmpty(contact.getEmail()) ? "No email" : contact.getEmail());
        detailAddress.setText(TextUtils.isEmpty(contact.getAddress()) ? "No address" : contact.getAddress());
        detailNotes.setText(TextUtils.isEmpty(contact.getNotes()) ? "No notes" : contact.getNotes());

        if (!TextUtils.isEmpty(contact.getPhotoUri())) {
            detailPhoto.setImageURI(Uri.parse(contact.getPhotoUri()));
        } else {
            detailPhoto.setImageResource(R.drawable.ic_contact_placeholder);
        }
    }

    private void startDialer() {
        if (TextUtils.isEmpty(contact.getPhone())) {
            Toast.makeText(this, "No phone number available.", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = Uri.parse("tel:" + contact.getPhone().replace(" ", ""));
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No dialer app is available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startEmail() {
        if (TextUtils.isEmpty(contact.getEmail())) {
            Toast.makeText(this, "No email address available.", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = Uri.parse("mailto:" + contact.getEmail());
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No email app is available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startMap() {
        String location = TextUtils.isEmpty(contact.getAddress()) ? contact.getFullName() : contact.getAddress();
        Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
        Intent intent = new Intent(Intent.ACTION_VIEW, geoUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No map app is available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openEditor() {
        Intent intent = new Intent(this, ContactFormActivity.class);
        intent.putExtra(ContactFormActivity.EXTRA_CONTACT_ID, contact.getId());
        startActivity(intent);
        finish();
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete contact")
                .setMessage("Are you sure you want to delete this contact?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repository.deleteById(contact.getId());
                    Toast.makeText(this, "Contact deleted.", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
