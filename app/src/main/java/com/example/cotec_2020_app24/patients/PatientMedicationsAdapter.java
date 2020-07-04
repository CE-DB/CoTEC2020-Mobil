package com.example.cotec_2020_app24.patients;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.PatientsQuery;
import com.example.cotec_2020_app24.R;

import java.util.List;

public class PatientMedicationsAdapter extends RecyclerView.Adapter<PatientMedicationsAdapter.MedicationsViewHolder> {


    private List<PatientsQuery.Medication> medications;

    public PatientMedicationsAdapter(List<PatientsQuery.Medication> medications) {
        this.medications = medications;
    }

    @NonNull
    @Override
    public MedicationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.patients_medications_list_item, parent, false);
        return new MedicationsViewHolder(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MedicationsViewHolder holder, int position) {
        final PatientsQuery.Medication p = medications.get(position);

        holder.name.setText("Name: " + p.name());
        holder.pharmaceutical.setText("Pharmaceutical: " + p.pharmaceutical());

    }


    @Override
    public int getItemCount() {
        return medications.size();
    }

    public static class MedicationsViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView pharmaceutical;

        public MedicationsViewHolder(@NonNull View itemView) {
            super(itemView);

            this.name = itemView.findViewById(R.id.medicationPatientName);
            this.pharmaceutical = itemView.findViewById(R.id.medicationPatientPharmeceutical);
        }
    }
}
