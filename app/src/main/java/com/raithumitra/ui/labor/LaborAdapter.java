package com.raithumitra.ui.labor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.raithumitra.R;
import com.raithumitra.data.local.entity.Laborer;
import java.util.ArrayList;
import java.util.List;

public class LaborAdapter extends RecyclerView.Adapter<LaborAdapter.LaborViewHolder> {

    private String currentUserRole;
    private String currentUserPhone;
    private List<Laborer> laborList = new ArrayList<>();
    private OnLaborClickListener listener;

    public void setUserContext(String role, String phone) {
        this.currentUserRole = role;
        this.currentUserPhone = phone;
        notifyDataSetChanged();
    }

    public interface OnLaborClickListener {
        void onCallClick(Laborer laborer);

        void onHireClick(Laborer laborer);

        void onEditClick(Laborer laborer);

        void onDeleteClick(Laborer laborer);
    }

    public void setOnLaborClickListener(OnLaborClickListener listener) {
        this.listener = listener;
    }

    public void setLaborList(List<Laborer> laborList) {
        this.laborList = laborList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LaborViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_labor_card, parent, false);
        return new LaborViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LaborViewHolder holder, int position) {
        Laborer laborer = laborList.get(position);
        holder.bind(laborer);
    }

    @Override
    public int getItemCount() {
        return laborList.size();
    }

    class LaborViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSkill, tvWage, tvMemberCount;
        RatingBar ratingBar;
        Button btnCall, btnHire;

        public LaborViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_labor_name);
            tvSkill = itemView.findViewById(R.id.tv_labor_skill);
            tvWage = itemView.findViewById(R.id.tv_labor_wage);
            tvMemberCount = itemView.findViewById(R.id.tv_member_count);
            ratingBar = itemView.findViewById(R.id.rating_labor);
            btnCall = itemView.findViewById(R.id.btn_call_worker);
            btnHire = itemView.findViewById(R.id.btn_hire_worker);
        }

        public void bind(Laborer laborer) {
            if (laborer.user != null) {
                tvName.setText(laborer.user.fullName);
            } else {
                tvName.setText("తెలియని కూలీ (Unknown Worker)");
            }
            tvSkill.setText(laborer.skillType);
            tvWage.setText(laborer.dailyWage);
            ratingBar.setRating(laborer.rating);

            if (laborer.memberCount > 1) {
                tvMemberCount.setText(laborer.memberCount + " సభ్యులు అందుబాటులో (Members Available)");
                tvMemberCount.setVisibility(View.VISIBLE);
            } else {
                tvMemberCount.setText("వ్యక్తిగతం (Individual)");
                tvMemberCount.setVisibility(View.VISIBLE);
            }

            // Button Logic
            boolean isFarmer = "FARMER".equals(currentUserRole);
            boolean isMyProfile = laborer.user != null && laborer.user.phoneNumber != null
                    && laborer.user.phoneNumber.equals(currentUserPhone);
            boolean isWorker = "WORKER".equals(currentUserRole);

            if (isFarmer) {
                // Formatting for Farmer: Call & Hire
                btnCall.setVisibility(View.VISIBLE);
                btnHire.setVisibility(View.VISIBLE);
                btnCall.setText("కాల్ (Call)");
                btnHire.setText("ఇప్పుడు నియమించు (Hire Now)");
                // Reset colors if needed, but XML defaults are good for Farmer
                // Reset listeners to avoid recycling issues
                btnCall.setOnClickListener(null);
                btnHire.setOnClickListener(null);

                // Re-attach listeners
                btnCall.setOnClickListener(v -> {
                    if (listener != null)
                        listener.onCallClick(laborer);
                });
                btnHire.setOnClickListener(v -> {
                    if (listener != null)
                        listener.onHireClick(laborer);
                });
            } else if (isWorker && isMyProfile) {
                // Formatting for Worker (Own Profile): Edit & Delete
                btnCall.setVisibility(View.VISIBLE);
                btnHire.setVisibility(View.VISIBLE);
                btnCall.setText("ఎడిట్ (Edit)");
                btnHire.setText("తొలగించు (Delete)");

                // Change Colors
                btnHire.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED));

                btnCall.setOnClickListener(v -> {
                    if (listener != null)
                        listener.onEditClick(laborer);
                });
                btnHire.setOnClickListener(v -> {
                    if (listener != null)
                        listener.onDeleteClick(laborer);
                });
            } else {
                // Worker viewing others: Hide Actions
                btnCall.setVisibility(View.GONE);
                btnHire.setVisibility(View.GONE);
            }
        }
    }
}
