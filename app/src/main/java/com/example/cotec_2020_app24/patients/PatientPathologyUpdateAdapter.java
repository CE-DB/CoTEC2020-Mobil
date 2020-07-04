package com.example.cotec_2020_app24.patients;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.PatientsQuery;
import com.example.cotec_2020_app24.R;
import com.example.cotec_2020_app24.models.PatientModel;

import java.util.List;

public class PatientPathologyUpdateAdapter extends RecyclerView.Adapter<PatientPathologyUpdateAdapter.PathologyViewHolder> {


    private List<String> pathologies;
    private PatientModel oldP;

    public PatientPathologyUpdateAdapter(List<String> pathologies, PatientModel oldP) {
        this.pathologies = pathologies;
        this.oldP = oldP;
    }

    @NonNull
    @Override
    public PathologyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.patients_pathologies_updated_list_item, parent, false);
        return new PatientPathologyUpdateAdapter.PathologyViewHolder(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PathologyViewHolder holder, int position) {
        final String p = pathologies.get(position);

        holder.pathology.setText(p);

        holder.pathology.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pathologies.set(position, s.toString());
                oldP.setPatholoigesModified(true);
            }
        });

        holder.delete.setOnClickListener(v -> {
            pathologies.remove(position);
            notifyItemRemoved(position);
            oldP.setPatholoigesModified(true);
        });

    }


    @Override
    public int getItemCount() {
        return pathologies.size();
    }

    public static class PathologyViewHolder extends RecyclerView.ViewHolder{

        public EditText pathology;
        public Button delete;

        public PathologyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.pathology = itemView.findViewById(R.id.patientPathologiesUpdateEditText);
            this.delete = itemView.findViewById(R.id.patientPathologiesUpdateDeleteButton);
        }
    }
}
