package com.example.cotec_2020_app24.patients;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cotec_2020_app24.R;
import com.example.cotec_2020_app24.models.PatientModel;

import java.util.List;

public class PatientMedicationUpdateAdapter extends RecyclerView.Adapter<PatientMedicationUpdateAdapter.PathologyViewHolder> {


    private List<String> medications;
    private PatientModel oldP;

    public PatientMedicationUpdateAdapter(List<String> medications, PatientModel oldP) {
        this.medications = medications;
        this.oldP = oldP;
    }

    @NonNull
    @Override
    public PathologyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.patient_medications_update_list_item, parent, false);
        return new PatientMedicationUpdateAdapter.PathologyViewHolder(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PathologyViewHolder holder, int position) {
        final String p = medications.get(position);

        holder.medication.setText(p);

        holder.medication.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                medications.set(position, s.toString());
                oldP.setMedicationsModified(true);
            }
        });

        holder.delete.setOnClickListener(v -> {
            medications.remove(position);
            notifyItemRemoved(position);
            oldP.setMedicationsModified(true);
        });

    }


    @Override
    public int getItemCount() {
        return medications.size();
    }

    public static class PathologyViewHolder extends RecyclerView.ViewHolder{

        public EditText medication;
        public Button delete;

        public PathologyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.medication = itemView.findViewById(R.id.patientMedicationsUpdateEditText);
            this.delete = itemView.findViewById(R.id.patientMedicationsUpdateDeleteButton);
        }
    }
}
