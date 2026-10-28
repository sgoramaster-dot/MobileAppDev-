package com.example.mycontactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CONTACTS = "contacts";

    public ContactDbHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_CONTACTS + " ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "first_name TEXT NOT NULL, "
                + "last_name TEXT NOT NULL, "
                + "phone TEXT NOT NULL, "
                + "email TEXT, "
                + "address TEXT, "
                + "notes TEXT, "
                + "photo_uri TEXT, "
                + "created_at INTEGER NOT NULL"
                + ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public long insertContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = toContentValues(contact);
        return db.insert(TABLE_CONTACTS, null, values);
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = toContentValues(contact);
        return db.update(TABLE_CONTACTS, values, "_id = ?", new String[] {String.valueOf(contact.getId())});
    }

    public int deleteContact(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_CONTACTS, "_id = ?", new String[] {String.valueOf(id)});
    }

    public Contact getContactById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, null, "_id = ?", new String[] {String.valueOf(id)}, null, null, null);
        if (cursor == null || !cursor.moveToFirst()) {
            if (cursor != null) cursor.close();
            return null;
        }
        Contact contact = cursorToContact(cursor);
        cursor.close();
        return contact;
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, null, null, null, null, null, "created_at DESC");
        if (cursor == null) {
            return contacts;
        }
        while (cursor.moveToNext()) {
            contacts.add(cursorToContact(cursor));
        }
        cursor.close();
        return contacts;
    }

    public List<Contact> searchContacts(String query) {
        List<Contact> contacts = new ArrayList<>();
        if (TextUtils.isEmpty(query)) {
            return getAllContacts();
        }

        SQLiteDatabase db = getReadableDatabase();
        String selection = "first_name LIKE ? OR last_name LIKE ? OR phone LIKE ? OR email LIKE ? OR address LIKE ?";
        String search = "%" + query.trim() + "%";
        String[] args = new String[] {search, search, search, search, search};
        Cursor cursor = db.query(TABLE_CONTACTS, null, selection, args, null, null, "created_at DESC");
        if (cursor == null) {
            return contacts;
        }
        while (cursor.moveToNext()) {
            contacts.add(cursorToContact(cursor));
        }
        cursor.close();
        return contacts;
    }

    private ContentValues toContentValues(Contact contact) {
        ContentValues values = new ContentValues();
        values.put("first_name", contact.getFirstName().trim());
        values.put("last_name", contact.getLastName().trim());
        values.put("phone", contact.getPhone().trim());
        values.put("email", contact.getEmail().trim());
        values.put("address", contact.getAddress().trim());
        values.put("notes", contact.getNotes().trim());
        values.put("photo_uri", contact.getPhotoUri());
        values.put("created_at", contact.getCreatedAt() == 0 ? System.currentTimeMillis() : contact.getCreatedAt());
        return values;
    }

    private Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact();
        contact.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
        contact.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("first_name")));
        contact.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("last_name")));
        contact.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
        contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        contact.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
        contact.setNotes(cursor.getString(cursor.getColumnIndexOrThrow("notes")));
        contact.setPhotoUri(cursor.getString(cursor.getColumnIndexOrThrow("photo_uri")));
        contact.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow("created_at")));
        return contact;
    }
}
