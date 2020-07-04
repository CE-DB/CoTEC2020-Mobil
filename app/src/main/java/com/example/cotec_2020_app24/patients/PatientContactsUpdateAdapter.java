package com.example.cotec_2020_app24.patients;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cotec_2020_app24.GlobalVars;
import com.example.cotec_2020_app24.R;
import com.example.cotec_2020_app24.models.ContactVisit;

import java.util.Calendar;
import java.util.List;

public class PatientContactsUpdateAdapter extends RecyclerView.Adapter<PatientContactsUpdateAdapter.ContactsViewHolder> {
    private final Context context;
    private List<ContactVisit> visits;
    DatePickerDialog picker;

    public PatientContactsUpdateAdapter(List<ContactVisit> visits, Context context) {
        this.visits = visits;
        this.context = context;
    }

    @NonNull
    @Override
    public PatientContactsUpdateAdapter.ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.patients_contacts_update_list_item, parent, false);
        return new PatientContactsUpdateAdapter.ContactsViewHolder(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PatientContactsUpdateAdapter.ContactsViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final ContactVisit p = visits.get(position);

        holder.identification.setText(p.getContactID());
        holder.date.setText(p.getDate());
        holder.state.setText("State: ".concat(p.getState()));

        if (p.getState().equals("DELETED")) {
            holder.identification.setEnabled(false);
            holder.date.setEnabled(false);
            holder.delete.setText("UNDELETE");
        }

        holder.date.setOnClickListener(v -> {

            int day;
            int month;
            int year;

            if (((EditText) v).getText().toString().isEmpty()) {
                Calendar c = Calendar.getInstance();
                day = c.get(Calendar.DAY_OF_MONTH);
                month = c.get(Calendar.MONTH);
                year = c.get(Calendar.YEAR);
            } else {
                day = Integer.parseInt(((EditText) v).getText().toString().split("-")[2]);
                month = Integer.parseInt(((EditText) v).getText().toString().split("-")[1]);
                year = Integer.parseInt(((EditText) v).getText().toString().split("-")[0]);
            }

            picker = new DatePickerDialog(context,
                    (view, year1, month1, dayOfMonth) -> {

                        holder.date.setText(year1 + "-" + (month1 < 10 ? "0": "") + month1 + "-" + (dayOfMonth < 10 ? "0": "") + dayOfMonth);

                    }, year, month, day);

            picker.show();

        });

        holder.identification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                p.setContactID(s.toString());
                p.setContactIDModified(true);

                if (p.getState().equals("NEW") || p.getState().equals("NEWUPDATED")) {
                    if (p.getOldContactID().isEmpty() && p.getOldDate().isEmpty()){
                        p.setState("NEW");
                    } else {
                        p.setState("NEWUPDATED");
                    }
                } else {
                    p.setState("UPDATED");
                }

                Log.d("CONTACTS", "Old: ".concat(p.getOldContactID())
                .concat(" New: ")
                .concat(s.toString()));
                holder.state.setText("State: ".concat(p.getState()));

                GlobalVars.getInstance().getTempPatientHolderForView()
                        .setContactsModified(true);
            }
        });

        holder.date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                p.setDate(s.toString());
                p.setContactVisitDateModified(true);
                if(p.getState().equals("NEW") || p.getState().equals("NEWUPDATED")) {
                    if (p.getOldContactID().isEmpty() && p.getOldDate().isEmpty()){
                        p.setState("NEW");
                    } else {
                        p.setState("NEWUPDATED");
                    }
                } else {
                    p.setState("UPDATED");
                }
                holder.state.setText("State: ".concat(p.getState()));
                GlobalVars.getInstance().getTempPatientHolderForView()
                        .setContactsModified(true);
            }
        });

        holder.delete.setOnClickListener(v -> {

            if (p.getState().equals("ONLINE")) {
                holder.identification.setEnabled(false);
                holder.date.setEnabled(false);
                holder.delete.setText("UNDELETE");
                p.setState("DELETED");
                holder.state.setText("State: ".concat(p.getState()));
            } else if (p.getState().equals("UPDATED")) {
                p.setContactID(p.getOldContactID());
                p.setDate(p.getOldDate());
                p.setContactIDModified(false);
                p.setContactVisitDateModified(false);
                p.setState("DELETED");
                holder.identification.setText(p.getContactID());
                holder.date.setText(p.getDate());
                holder.identification.setEnabled(false);
                holder.date.setEnabled(false);
                holder.delete.setText("UNDELETE");
                holder.state.setText("State: ".concat(p.getState()));
            } else if (p.getState().equals("DELETED")) {
                holder.delete.setText("DELETE");
                holder.identification.setEnabled(true);
                holder.date.setEnabled(true);
                p.setState("ONLINE");
                holder.state.setText("State: ".concat(p.getState()));
            } else if (p.getState().equals("NEW") || (p.getState().equals("NEWUPDATED"))) {

                if (p.getOldContactID().isEmpty() && p.getOldDate().isEmpty()) {
                    visits.remove(position);
                    notifyDataSetChanged();
                } else {
                    holder.identification.setEnabled(false);
                    holder.date.setEnabled(false);
                    p.setState("NEWDELETED");
                    holder.state.setText("State: ".concat(p.getState()));
                }
            }

            GlobalVars.getInstance().getTempPatientHolderForView()
                    .setContactsModified(true);

        });
    }


    @Override
    public int getItemCount() {
        return visits.size();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder{

        public EditText identification;
        public EditText date;
        public Button delete;
        public TextView state;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            this.identification = itemView.findViewById(R.id.patientContactsIdUpdateEditText);
            this.date = itemView.findViewById(R.id.editTextPatientContactsVisitDateUpdated);
            this.delete = itemView.findViewById(R.id.patientContactsUpdateDeleteButton);
            this.state = itemView.findViewById(R.id.textViewPatientContactUpdateState);

        }
    }
}
