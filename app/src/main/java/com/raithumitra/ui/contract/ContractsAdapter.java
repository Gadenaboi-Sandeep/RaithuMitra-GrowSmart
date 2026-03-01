package com.raithumitra.ui.contract;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.raithumitra.R;
import com.raithumitra.data.local.entity.Contract;
import java.util.ArrayList;
import java.util.List;

public class ContractsAdapter extends RecyclerView.Adapter<ContractsAdapter.ContractViewHolder> {

    private List<Contract> contracts = new ArrayList<>();
    private OnContractClickListener listener;

    public interface OnContractClickListener {
        void onViewAgreement(Contract contract);
    }

    public void setOnContractClickListener(OnContractClickListener listener) {
        this.listener = listener;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contract, parent, false);
        return new ContractViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractViewHolder holder, int position) {
        Contract contract = contracts.get(position);
        holder.bind(contract);
    }

    @Override
    public int getItemCount() {
        return contracts.size();
    }

    class ContractViewHolder extends RecyclerView.ViewHolder {
        TextView tvCompany, tvCrop, tvPrice;
        Chip chipStatus;
        Button btnViewAgreement;

        public ContractViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCompany = itemView.findViewById(R.id.tv_company);
            tvCrop = itemView.findViewById(R.id.tv_crop);
            tvPrice = itemView.findViewById(R.id.tv_price);
            chipStatus = itemView.findViewById(R.id.chip_status);
            btnViewAgreement = itemView.findViewById(R.id.btn_view_agreement);
        }

        public void bind(Contract contract) {
            tvCompany.setText(contract.companyName != null ? contract.companyName : "కార్పొరేట్ (Corporate)");
            tvCrop.setText(contract.cropName);
            tvPrice.setText("₹" + contract.pricePerKg + "/కిలో");
            chipStatus.setText(contract.status);

            // Status Styling
            int statusColor;
            if ("Signed".equalsIgnoreCase(contract.status)) {
                statusColor = ContextCompat.getColor(itemView.getContext(), R.color.status_signed);
            } else {
                statusColor = ContextCompat.getColor(itemView.getContext(), R.color.status_open);
            }
            chipStatus.setChipBackgroundColor(ColorStateList.valueOf(statusColor));

            btnViewAgreement.setOnClickListener(v -> {
                if (listener != null)
                    listener.onViewAgreement(contract);
            });
        }
    }
}
