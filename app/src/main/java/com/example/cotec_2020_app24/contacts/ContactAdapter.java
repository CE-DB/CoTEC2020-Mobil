package com.example.cotec_2020_app24.contacts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cotec_2020_app24.GlobalVars;
import com.example.cotec_2020_app24.R;
import com.example.cotec_2020_app24.models.ContactModel;
import com.example.cotec_2020_app24.models.PatientState;
import com.example.cotec_2020_app24.patients.SpecificPatient;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<ContactModel> contacts;

    public ContactAdapter(List<ContactModel> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.patients_list_item, parent, false);
        return new ContactViewHolder(listItem);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        final ContactModel p = contacts.get(position);

        String identification;
        String fullName;

        p.checkSyncState(GlobalVars.getInstance().getDatabase());

        if (p.getSyncState().equals(PatientState.DELETED)){
            identification = p.getIdentification();
            fullName = p.getFirstName() + " " + p.getLastName();

            holder.fullName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.identification.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        } else if (p.getSyncState().equals(PatientState.UPDATED)) {
            identification = p.getNewContact().getIdentification();
            fullName = p.getNewContact().getFirstName() + " " + p.getNewContact().getLastName();
        } else {
            identification = p.getIdentification();
            fullName = p.getFirstName() + " " + p.getLastName();

            holder.fullName.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
            holder.identification.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
        }

        holder.identification.setText("ID: " + identification);
        holder.fullName.setText("Full name: " + fullName);
        holder.state.setText("State: " + p.getSyncState().toString());

        holder.layout.setOnClickListener((v) ->{

            GlobalVars.getInstance().setTempContactHolderForView(p);
            Intent i = new Intent(v.getContext(), SpecificContact.class);
            v.getContext().startActivity(i);

        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{

        public TextView identification;
        public TextView fullName;
        public TextView state;
        public LinearLayout layout;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            this.identification = (TextView) itemView.findViewById(R.id.patientIdentification);
            this.fullName = (TextView) itemView.findViewById(R.id.patientFullname);
            this.state = (TextView) itemView.findViewById(R.id.patientState);
            this.layout = itemView.findViewById(R.id.PatientRecyclerElementLayout);
        }
    }

}
