package com.raithumitra.ui.equipment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.raithumitra.R;
import com.raithumitra.data.remote.model.EquipmentBookingResponse;
import com.raithumitra.data.repository.BookingRepository;
import java.util.List;

public class RequestsBottomSheet extends BottomSheetDialogFragment {

    private RecyclerView rvRequests;
    private RequestsAdapter adapter;
    private BookingRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests_bottom_sheet, container, false);

        rvRequests = view.findViewById(R.id.rvRequests);
        rvRequests.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RequestsAdapter();
        rvRequests.setAdapter(adapter);

        repository = new BookingRepository(requireContext());

        loadRequests();

        adapter.setOnRequestActionClickListener(new RequestsAdapter.OnRequestActionClickListener() {
            @Override
            public void onAccept(EquipmentBookingResponse request) {
                updateStatus(request, "CONFIRMED");
            }

            @Override
            public void onReject(EquipmentBookingResponse request) {
                updateStatus(request, "REJECTED");
            }
        });

        return view;
    }

    private void loadRequests() {
        repository.getReceivedBookings().observe(getViewLifecycleOwner(), bookings -> {
            if (bookings != null) {
                adapter.setRequests(bookings);
            } else {
                Toast.makeText(getContext(), "అభ్యర్థనలు లోడ్ చేయడం విఫలం (Failed to load requests)",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatus(EquipmentBookingResponse request, String status) {
        repository.updateBookingStatus(request.getBookingId(), status, new BookingRepository.StatusCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "అభ్యర్థన (Request) " + status, Toast.LENGTH_SHORT).show();
                loadRequests(); // Refresh list
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "లోపం (Error): " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
