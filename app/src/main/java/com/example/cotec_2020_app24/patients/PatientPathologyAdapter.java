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

public class PatientPathologyAdapter extends RecyclerView.Adapter<PatientPathologyAdapter.PathologyViewHolder> {


    private List<PatientsQuery.Pathology> pathologies;

    public PatientPathologyAdapter(List<PatientsQuery.Pathology> pathologies) {
        this.pathologies = pathologies;
    }

    @NonNull
    @Override
    public PathologyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.patients_pathologies_list_item, parent, false);
        return new PatientPathologyAdapter.PathologyViewHolder(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PathologyViewHolder holder, int position) {
        final PatientsQuery.Pathology p = pathologies.get(position);

        holder.name.setText("Name: " + p.name());
        holder.description.setText("Description: " + p.description());
        holder.treatment.setText("Treatment: " + p.treatment());

        StringBuilder sympList = new StringBuilder();

        for (String symp: p.symptoms()) {
            if(p.symptoms().indexOf(symp) == 0){
                if(p.symptoms().size() == 1){
                    sympList.append(symp);
                } else {
                    sympList.append(symp + ", ");
                }
            } else if (p.symptoms().indexOf(symp) == p.symptoms().size()-1){
                sympList.append(symp);
            } else {
                sympList.append(symp + ", ");
            }
        }

        holder.symptoms.setText("Symptoms: " + sympList.toString());

    }


    @Override
    public int getItemCount() {
        return pathologies.size();
    }

    public static class PathologyViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView description;
        public TextView symptoms;
        public TextView treatment;

        public PathologyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.name = itemView.findViewById(R.id.pathologyPatientName);
            this.description = itemView.findViewById(R.id.pathologyPatientDescription);
            this.symptoms = itemView.findViewById(R.id.pathologyPatientSymptoms);
            this.treatment = itemView.findViewById(R.id.pathologyPatientTreatment);
        }
    }
}
