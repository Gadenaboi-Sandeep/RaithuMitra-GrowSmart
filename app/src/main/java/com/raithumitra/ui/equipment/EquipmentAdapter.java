package com.raithumitra.ui.equipment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.raithumitra.R;
import com.raithumitra.data.local.entity.Equipment;
import java.util.ArrayList;
import java.util.List;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder> {

    private List<Equipment> equipmentList = new ArrayList<>();
    private OnEquipmentClickListener listener;
    private String currentUserRole;
    private String currentUserPhone;

    public void setUserContext(String role, String phone) {
        this.currentUserRole = role;
        this.currentUserPhone = phone;
        notifyDataSetChanged();
    }

    public interface OnEquipmentClickListener {
        void onBookClick(Equipment equipment);

        void onDeleteClick(Equipment equipment);
    }

    public void setOnEquipmentClickListener(OnEquipmentClickListener listener) {
        this.listener = listener;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EquipmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_equipment_card, parent, false);
        return new EquipmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EquipmentViewHolder holder, int position) {
        Equipment equipment = equipmentList.get(position);
        holder.bind(equipment);
    }

    @Override
    public int getItemCount() {
        return equipmentList.size();
    }

    class EquipmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRate, tvLocation;
        android.widget.ImageView ivImage;
        Button btnBook;

        public EquipmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_equip_name);
            tvRate = itemView.findViewById(R.id.tv_equip_rate);
            tvLocation = itemView.findViewById(R.id.tv_equip_location);
            ivImage = itemView.findViewById(R.id.img_equipment);
            btnBook = itemView.findViewById(R.id.btn_action);
        }

        public void bind(Equipment equipment) {
            tvName.setText(equipment.equipmentName);
            tvRate.setText("₹" + equipment.hourlyRate + "/గం (/hr)");
            tvLocation.setText(equipment.location);

            if (equipment.imageUrl != null && !equipment.imageUrl.isEmpty()) {
                com.bumptech.glide.Glide.with(itemView.getContext())
                        .load(equipment.imageUrl)
                        .placeholder(R.mipmap.ic_launcher)
                        .listener(new com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable>() {
                            @Override
                            public boolean onLoadFailed(
                                    @androidx.annotation.Nullable com.bumptech.glide.load.engine.GlideException e,
                                    Object model,
                                    com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target,
                                    boolean isFirstResource) {
                                new android.os.Handler(android.os.Looper.getMainLooper())
                                        .post(() -> android.widget.Toast.makeText(itemView.getContext(),
                                                "చిత్రం లోడ్ విఫలం (Img Fail): " + equipment.imageUrl,
                                                android.widget.Toast.LENGTH_LONG)
                                                .show());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model,
                                    com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target,
                                    com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(ivImage);
            } else {
                ivImage.setImageResource(R.mipmap.ic_launcher);
            }

            boolean isFarmer = "FARMER".equalsIgnoreCase(currentUserRole);

            if (isFarmer) {
                btnBook.setText("బుక్ (Book)");
                btnBook.setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#4CAF50")));
                btnBook.setOnClickListener(v -> {
                    if (listener != null)
                        listener.onBookClick(equipment);
                });
                btnBook.setVisibility(View.VISIBLE);
            } else {
                // Owner / Worker
                // Only show delete if the current user is the owner of this equipment
                if (currentUserPhone != null && currentUserPhone.equals(equipment.ownerPhone)) {
                    btnBook.setText("తొలగించు (Delete)");
                    btnBook.setBackgroundTintList(
                            android.content.res.ColorStateList.valueOf(android.graphics.Color.RED));
                    btnBook.setOnClickListener(v -> {
                        if (listener != null)
                            listener.onDeleteClick(equipment);
                    });
                    btnBook.setVisibility(View.VISIBLE);
                } else {
                    btnBook.setVisibility(View.GONE);
                }
            }
        }
    }
}
