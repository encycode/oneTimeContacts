package com.encycode.onetimecontact.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.encycode.onetimecontact.MainActivity;
import com.encycode.onetimecontact.R;
import com.encycode.onetimecontact.entity.Contact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> implements Filterable {

    private List<Contact> contacts = new ArrayList<>();
    private List<Contact> contactsfull;
    private OnItemClickListener listener;
    Context context;

    public ContactAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ContactHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactHolder holder, int position) {
        final Contact currentContact = contacts.get(position);
        holder.fullName.setText(new StringBuilder().append(currentContact.getFirstName()).append(" ").append(currentContact.getLastName()).toString());
        holder.mobileNumber.setText(currentContact.getMobileNumber());

        holder.anim.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));

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

    public Contact getContactAt(int pos) {
        return contacts.get(pos);
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        this.contactsfull = new ArrayList<>(contacts);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Contact> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(contactsfull);
            } else {
                for (Contact item : contactsfull) {
                    if (item.getFirstName().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            item.getLastName().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            item.getCompanyName().contains(constraint.toString().toLowerCase()) ||
                            item.getJobTitle().contains(constraint.toString().toLowerCase()) ||
                            item.getMobileNumber().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contacts.clear();
            contacts.addAll((Collection<? extends Contact>) results.values);
            notifyDataSetChanged();
        }
    };

    class ContactHolder extends RecyclerView.ViewHolder {
        private TextView fullName;
        private TextView mobileNumber;
        private LinearLayout anim;
        private ImageButton call;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName);
            mobileNumber = itemView.findViewById(R.id.mobileNumber);
            call = itemView.findViewById(R.id.callButton);
            anim = itemView.findViewById(R.id.animLinear);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(contacts.get(pos));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Contact contact);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
