package com.raithumitra.ui.equipment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.raithumitra.R;
import com.raithumitra.data.remote.model.EquipmentBookingResponse;
import java.util.ArrayList;
import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {

    private List<EquipmentBookingResponse> requests = new ArrayList<>();
    private OnRequestActionClickListener listener;

    public interface OnRequestActionClickListener {
        void onAccept(EquipmentBookingResponse request);

        void onReject(EquipmentBookingResponse request);
    }

    public void setRequests(List<EquipmentBookingResponse> requests) {
        this.requests = requests;
        notifyDataSetChanged();
    }

    public void setOnRequestActionClickListener(OnRequestActionClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        EquipmentBookingResponse request = requests.get(position);
        holder.bind(request);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvEquipmentName, tvFarmerDetails, tvDate, tvStatus;
        Button btnAccept, btnReject;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEquipmentName = itemView.findViewById(R.id.tvEquipmentName);
            tvFarmerDetails = itemView.findViewById(R.id.tvFarmerDetails);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }

        public void bind(EquipmentBookingResponse request) {
            if (request.getEquipment() != null) {
                tvEquipmentName.setText(request.getEquipment().equipmentName);
            }
            if (request.getFarmer() != null) {
                tvFarmerDetails.setText(
                        "అభ్యర్థించినది (Requested by): " + request.getFarmer().getFullName() + "\nఫోన్ (Phone): "
                                + request.getFarmer().getPhoneNumber());
            }
            tvDate.setText("తేదీ (Date): " + request.getBookingDate());
            tvStatus.setText("స్థితి (Status): " + request.getStatus());

            if ("PENDING".equalsIgnoreCase(request.getStatus())) {
                btnAccept.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.VISIBLE);
                btnAccept.setOnClickListener(v -> {
                    if (listener != null)
                        listener.onAccept(request);
                });
                btnReject.setOnClickListener(v -> {
                    if (listener != null)
                        listener.onReject(request);
                });
            } else {
                btnAccept.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
            }
        }
    }
}
