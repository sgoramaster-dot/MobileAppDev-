package com.example.mycontactlist;

import android.content.Context;
import java.util.List;

public class ContactRepository {
    private final ContactDbHelper dbHelper;

    public ContactRepository(Context context) {
        dbHelper = new ContactDbHelper(context.getApplicationContext());
    }

    public long insert(Contact contact) {
        return dbHelper.insertContact(contact);
    }

    public boolean update(Contact contact) {
        return dbHelper.updateContact(contact) > 0;
    }

    public boolean deleteById(long contactId) {
        return dbHelper.deleteContact(contactId) > 0;
    }

    public Contact getById(long contactId) {
        return dbHelper.getContactById(contactId);
    }

    public List<Contact> getAll() {
        return dbHelper.getAllContacts();
    }

    public List<Contact> search(String query) {
        return dbHelper.searchContacts(query);
    }
}
