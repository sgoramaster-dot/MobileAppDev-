package com.example.mycontactlist;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private final List<Contact> contacts = new ArrayList<>();
    private final LayoutInflater inflater;
    private final OnContactClickListener listener;

    public interface OnContactClickListener {
        void onClick(Contact contact);
    }

    public ContactAdapter(Context context, OnContactClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public void submitList(List<Contact> newContacts) {
        contacts.clear();
        if (newContacts != null) {
            contacts.addAll(newContacts);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final ImageView photoView;
        private final TextView nameText;
        private final TextView phoneText;

        ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.contactPhoto);
            nameText = itemView.findViewById(R.id.contactName);
            phoneText = itemView.findViewById(R.id.contactPhone);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onClick(contacts.get(position));
                }
            });
        }

        void bind(Contact contact) {
            nameText.setText(contact.getFullName());
            phoneText.setText(contact.getPhone());

            String photoUri = contact.getPhotoUri();
            if (!TextUtils.isEmpty(photoUri)) {
                try {
                    photoView.setImageURI(Uri.parse(photoUri));
                } catch (Exception ignored) {
                    photoView.setImageResource(R.drawable.ic_contact_placeholder);
                }
            } else {
                photoView.setImageResource(R.drawable.ic_contact_placeholder);
            }
        }
    }
}
