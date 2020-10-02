package com.encycode.onetimecontact.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.encycode.onetimecontact.MainActivity;
import com.encycode.onetimecontact.R;
import com.encycode.onetimecontact.entity.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    private List<Contact> contacts = new ArrayList<>();

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item,parent,false);
        return new ContactHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactHolder holder, int position) {
        final Contact currentContact = contacts.get(position);
        holder.fullName.setText(new StringBuilder().append(currentContact.getFirstName()).append(" ").append(currentContact.getLastName()).toString());
        holder.mobileNumber.setText(currentContact.getMobileNumber());

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = currentContact.getMobileNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public Contact getContactAt(int pos)
    {
        return contacts.get(pos);
    }

    public void setContacts(List<Contact> contacts){
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    class ContactHolder extends RecyclerView.ViewHolder{
        private TextView fullName;
        private TextView mobileNumber;
        private ImageButton call;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName);
            mobileNumber = itemView.findViewById(R.id.mobileNumber);
            call = itemView.findViewById(R.id.callButton);
        }
    }
}
