package com.raithumitra.ui.labor;

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
import com.raithumitra.data.repository.LaborRepository;
import com.raithumitra.data.remote.model.LaborHiringResponse;
import java.util.List;

public class LaborRequestsBottomSheet extends BottomSheetDialogFragment {

    private LaborRepository repository;
    private LaborRequestsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_labor_requests_bottom_sheet, container, false);

        repository = new LaborRepository(requireActivity().getApplication());

        RecyclerView recyclerView = view.findViewById(R.id.rvRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LaborRequestsAdapter();
        recyclerView.setAdapter(adapter);

        loadRequests();

        adapter.setOnRequestActionClickListener(new LaborRequestsAdapter.OnRequestActionClickListener() {
            @Override
            public void onAccept(LaborHiringResponse request) {
                updateStatus(request, "ACCEPTED");
            }

            @Override
            public void onReject(LaborHiringResponse request) {
                updateStatus(request, "REJECTED");
            }
        });

        return view;
    }

    private void loadRequests() {
        repository.getReceivedLaborRequests().observe(getViewLifecycleOwner(), requests -> {
            if (requests != null) {
                adapter.setRequests(requests);
            } else {
                Toast.makeText(getContext(), "అభ్యర్థనలు లోడ్ చేయడం విఫలం (Failed to load requests)",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatus(LaborHiringResponse request, String status) {
        repository.updateLaborRequestStatus(request.getHiringId(), status, new LaborRepository.RepositoryCallback() {
            @Override
            public void onSuccess() {
                if (getActivity() != null) {
                    Toast.makeText(getContext(), "అభ్యర్థన (Request) " + status, Toast.LENGTH_SHORT).show();
                    loadRequests(); // Refresh list
                }
            }

            @Override
            public void onError(String message) {
                if (getActivity() != null) {
                    Toast.makeText(getContext(), "లోపం (Error): " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
