package com.example.cotec_2020_app24.patients;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cotec_2020_app24.GlobalVars;
import com.example.cotec_2020_app24.R;
import com.example.cotec_2020_app24.models.PatientModel;
import com.example.cotec_2020_app24.models.PatientState;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    private List<PatientModel> patients;
    private Context context;

    public PatientAdapter(List<PatientModel> patients, Context context) {
        this.patients = patients;
        this.context = context;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.patients_list_item, parent, false);
        return new PatientViewHolder(listItem);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        final PatientModel p = patients.get(position);

        String identification;
        String fullName;

        p.checkSyncState(GlobalVars.getInstance().getDatabase());

        if (p.getSyncState().equals(PatientState.DELETED)){
            identification = p.getIdentification();
            fullName = p.getFirstName() + " " + p.getLastName();

            holder.fullName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.identification.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else if (p.getSyncState().equals(PatientState.UPDATED)) {
            identification = p.getNewPatient().getIdentification();
            fullName = p.getNewPatient().getFirstName() + " " + p.getNewPatient().getLastName();
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

            GlobalVars.getInstance().setTempPatientHolderForView(p);
            Intent i = new Intent(v.getContext(), SpecificPatient.class);
            v.getContext().startActivity(i);

        });
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder{

        public TextView identification;
        public TextView fullName;
        public TextView state;
        public LinearLayout layout;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);

            this.identification = itemView.findViewById(R.id.patientIdentification);
            this.fullName = itemView.findViewById(R.id.patientFullname);
            this.state = itemView.findViewById(R.id.patientState);
            this.layout = itemView.findViewById(R.id.PatientRecyclerElementLayout);
        }
    }
}
