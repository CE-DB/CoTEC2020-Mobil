package com.example.cotec_2020_app24.patients;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cotec_2020_app24.R;
import com.example.cotec_2020_app24.models.ContactVisit;

import java.util.List;

public class PatientContactsAdapter extends RecyclerView.Adapter<PatientContactsAdapter.ContactsViewHolder> {


    private List<ContactVisit> visits;

    public PatientContactsAdapter(List<ContactVisit> visits) {
        this.visits = visits;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.patients_contacts_list_item, parent, false);
        return new ContactsViewHolder(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        final ContactVisit p = visits.get(position);

        holder.state.setText("State: ".concat(p.getState()));
        holder.name.setText("Name: ".concat(p.getFullName()));

        if (p.getState().equals("UPDATED")){

            if (p.isContactIDModified()) {
                holder.id.setText("ID: " + p.getOldContactID()
                        .concat("\n(New: ")
                        .concat(p.getContactID())
                        .concat(")"));
            } else {
                holder.id.setText("ID: ".concat(p.getContactID()));
            }

            if (p.isContactVisitDateModified()) {
                holder.date.setText("Date: " + p.getOldDate()
                        .concat("\n(New: ")
                        .concat(p.getDate())
                        .concat(")"));
            } else {
                holder.date.setText("Date: ".concat(p.getDate()));
            }

        } else {
            holder.id.setText("ID: ".concat(p.getContactID()));
            holder.date.setText("Date: ".concat(p.getDate()));
        }
    }


    @Override
    public int getItemCount() {
        return visits.size();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView id;
        public TextView date;
        public TextView state;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            this.name = itemView.findViewById(R.id.contactPatientName);
            this.id = itemView.findViewById(R.id.contactPatientId);
            this.date = itemView.findViewById(R.id.contactPatientMeetDate);
            this.state = itemView.findViewById(R.id.contactPatientState);
        }
    }
}
