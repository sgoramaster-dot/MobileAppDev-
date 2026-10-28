package com.example.mycontactlist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class ContactFormActivity extends AppCompatActivity {
    public static final String EXTRA_CONTACT_ID = "extra_contact_id";

    private ContactRepository repository;
    private Contact contact;
    private long contactId = -1L;
    private Uri selectedPhotoUri;

    private TextInputEditText firstNameInput;
    private TextInputEditText lastNameInput;
    private TextInputEditText phoneInput;
    private TextInputEditText emailInput;
    private TextInputEditText addressInput;
    private TextInputEditText notesInput;
    private ImageView photoPreview;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    selectedPhotoUri = uri;
                    photoPreview.setImageURI(uri);
                    photoPreview.setVisibility(View.VISIBLE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);

        repository = new ContactRepository(this);
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        phoneInput = findViewById(R.id.phoneInput);
        emailInput = findViewById(R.id.emailInput);
        addressInput = findViewById(R.id.addressInput);
        notesInput = findViewById(R.id.notesInput);
        photoPreview = findViewById(R.id.photoPreview);
        Button choosePhotoButton = findViewById(R.id.choosePhotoButton);
        Button saveButton = findViewById(R.id.saveButton);

        contactId = getIntent().getLongExtra(EXTRA_CONTACT_ID, -1L);
        if (contactId > 0) {
            contact = repository.getById(contactId);
            if (contact != null) {
                bindValues();
            }
        } else {
            contact = new Contact();
        }

        choosePhotoButton.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        saveButton.setOnClickListener(v -> saveContact());
    }

    private void bindValues() {
        firstNameInput.setText(contact.getFirstName());
        lastNameInput.setText(contact.getLastName());
        phoneInput.setText(contact.getPhone());
        emailInput.setText(contact.getEmail());
        addressInput.setText(contact.getAddress());
        notesInput.setText(contact.getNotes());

        if (!TextUtils.isEmpty(contact.getPhotoUri())) {
            selectedPhotoUri = Uri.parse(contact.getPhotoUri());
            photoPreview.setImageURI(selectedPhotoUri);
            photoPreview.setVisibility(View.VISIBLE);
        }
    }

    private void saveContact() {
        Contact newContact = new Contact();
        newContact.setId(contactId > 0 ? contactId : 0L);
        newContact.setFirstName(firstNameInput.getText() == null ? "" : firstNameInput.getText().toString());
        newContact.setLastName(lastNameInput.getText() == null ? "" : lastNameInput.getText().toString());
        newContact.setPhone(phoneInput.getText() == null ? "" : phoneInput.getText().toString());
        newContact.setEmail(emailInput.getText() == null ? "" : emailInput.getText().toString());
        newContact.setAddress(addressInput.getText() == null ? "" : addressInput.getText().toString());
        newContact.setNotes(notesInput.getText() == null ? "" : notesInput.getText().toString());
        if (selectedPhotoUri != null) {
            newContact.setPhotoUri(selectedPhotoUri.toString());
        }

        ContactValidator.ValidationResult result = ContactValidator.validate(newContact);
        if (!result.isValid()) {
            StringBuilder message = new StringBuilder();
            for (String error : result.getErrors()) {
                message.append(error).append("\n");
            }
            Toast.makeText(this, message.toString().trim(), Toast.LENGTH_LONG).show();
            return;
        }

        if (contactId > 0) {
            newContact.setCreatedAt(contact.getCreatedAt());
            repository.update(newContact);
            Toast.makeText(this, "Contact updated.", Toast.LENGTH_SHORT).show();
        } else {
            newContact.setCreatedAt(System.currentTimeMillis());
            repository.insert(newContact);
            Toast.makeText(this, "Contact saved.", Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        finish();
    }
}
