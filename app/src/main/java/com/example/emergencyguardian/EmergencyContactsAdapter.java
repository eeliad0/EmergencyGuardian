package com.example.emergencyguardian;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
public class EmergencyContactsAdapter extends RecyclerView.Adapter<EmergencyContactsAdapter.ViewHolder> {
    private OnContactListener onContactListener;

    public EmergencyContactsAdapter(List<EmergencyContactClass> emergencyContacts, OnContactListener onContactListener) {
        this.emergencyContacts = emergencyContacts;
        this.onContactListener = onContactListener;
    }
    private List<EmergencyContactClass> emergencyContacts;

    public EmergencyContactsAdapter(List<EmergencyContactClass> emergencyContacts) {
        this.emergencyContacts = emergencyContacts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emergency_contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EmergencyContactClass contact = emergencyContacts.get(position);
        holder.nameTextView.setText(contact.getName());
        holder.phoneTextView.setText(contact.getPhone());
        holder.relationshipTextView.setText(contact.getRelationship());
    }

    @Override
    public int getItemCount() {
        return emergencyContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView phoneTextView;
        public TextView relationshipTextView;
        public Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.contact_name_text);
            phoneTextView = itemView.findViewById(R.id.contact_phone_text);
            relationshipTextView = itemView.findViewById(R.id.contact_relationship_text);
            deleteButton = itemView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String id = emergencyContacts.get(position).getId();
                        onContactListener.onContactDelete(id);
                    }
                }
            });
        }

    }
    public interface OnContactListener {
        void onContactDelete(String id);
    }
}



