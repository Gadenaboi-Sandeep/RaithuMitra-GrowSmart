package com.raithumitra.ui.labor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.raithumitra.R;
import com.raithumitra.data.remote.model.LaborHiringResponse;
import java.util.ArrayList;
import java.util.List;

public class LaborRequestsAdapter extends RecyclerView.Adapter<LaborRequestsAdapter.RequestViewHolder> {

    private List<LaborHiringResponse> requests = new ArrayList<>();
    private OnRequestActionClickListener listener;

    public interface OnRequestActionClickListener {
        void onAccept(LaborHiringResponse request);

        void onReject(LaborHiringResponse request);
    }

    public void setRequests(List<LaborHiringResponse> requests) {
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
        LaborHiringResponse request = requests.get(position);
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

        public void bind(LaborHiringResponse request) {
            tvEquipmentName.setText("పని అభ్యర్థన (Work Request)");

            if (request.getFarmer() != null) {
                tvFarmerDetails.setText(
                        "అభ్యర్థించినది (Requested by): " + request.getFarmer().getFullName() + "\nఫోన్ (Phone): "
                                + request.getFarmer().getPhoneNumber());
            } else {
                tvFarmerDetails.setText("అభ్యర్థించినది (Requested by): తెలియని రైతు (Unknown Farmer)");
            }

            tvDate.setText("ప్రారంభ తేదీ (Start Date): " + request.getWorkStartDate() + " (" + request.getDurationDays()
                    + " రోజులు (days))");
            tvStatus.setText("స్థితి (Status): " + request.getStatus());

            if ("REQUESTED".equalsIgnoreCase(request.getStatus())) {
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
