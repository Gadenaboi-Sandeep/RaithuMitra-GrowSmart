package com.raithumitra.ui.contract;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.raithumitra.R;
import com.raithumitra.data.local.entity.Contract;
import com.raithumitra.viewmodel.ContractViewModel;

public class ContractFragment extends Fragment {

    private ContractViewModel viewModel;
    private ContractsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rvContracts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ContractsAdapter();
        recyclerView.setAdapter(adapter);

        // Setup ViewModel
        viewModel = new ViewModelProvider(this).get(ContractViewModel.class);
        viewModel.getAllContracts().observe(getViewLifecycleOwner(), contracts -> {
            adapter.setContracts(contracts);
        });

        viewModel.getNetworkError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        // Check User Role using SessionManager
        com.raithumitra.data.local.SessionManager sessionManager = new com.raithumitra.data.local.SessionManager(
                requireContext());
        String userRole = sessionManager.getUserRole();

        com.google.android.material.floatingactionbutton.FloatingActionButton fab = view
                .findViewById(R.id.fabAddContract);
        if ("CORPORATE".equals(userRole)) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(v -> {
                startActivity(new android.content.Intent(getContext(), CreateContractActivity.class));
            });
        }

        // Handle Interactions
        adapter.setOnContractClickListener(this::showAgreementBottomSheet);

        return view;
    }

    private void showAgreementBottomSheet(Contract contract) {
        BottomSheetDialog sheet = new BottomSheetDialog(requireContext());
        View sheetView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_terms, null);

        TextView tvTitle = sheetView.findViewById(R.id.tv_terms_title);
        TextView tvBody = sheetView.findViewById(R.id.tv_terms_body);
        MaterialButton btnSign = sheetView.findViewById(R.id.btn_sign_agreement);

        tvTitle.setText("నిబంధనలు (Terms): "
                + (contract.companyName != null ? contract.companyName : "కార్పొరేట్ (Corporate)"));
        tvBody.setText(
                contract.contractDetails + "\n\nధర (Price): ₹" + contract.pricePerKg + "/క్వింటాల్ \nపరిమాణం (Qty): "
                        + contract.quantityRequired + "\nపంట (Crop): " + contract.cropName);

        // Check role to toggle Sign Button
        com.raithumitra.data.local.SessionManager sessionManager = new com.raithumitra.data.local.SessionManager(
                requireContext());
        if (!"FARMER".equals(sessionManager.getUserRole())) {
            btnSign.setVisibility(View.GONE);
        } else if ("Signed".equalsIgnoreCase(contract.status)) {
            btnSign.setText("ఇప్పటికే సంతకం చేశారు (Already Signed)");
            btnSign.setEnabled(false);
        }

        btnSign.setOnClickListener(v -> {
            // Call Repository to sign
            com.raithumitra.data.repository.ContractRepository repo = new com.raithumitra.data.repository.ContractRepository(
                    requireActivity().getApplication());
            repo.signContract(contract.contractId,
                    new com.raithumitra.data.repository.ContractRepository.RepositoryCallback() {
                        @Override
                        public void onSuccess() {
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    Toast.makeText(getContext(), "ఒప్పందం సంతకం చేయబడింది! (Agreement Signed!)",
                                            Toast.LENGTH_SHORT).show();
                                    sheet.dismiss();
                                });
                            }
                        }

                        @Override
                        public void onError(String message) {
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> Toast
                                        .makeText(getContext(), "లోపం (Error): " + message, Toast.LENGTH_SHORT).show());
                            }
                        }
                    });
        });

        sheet.setContentView(sheetView);
        sheet.show();
    }
}
