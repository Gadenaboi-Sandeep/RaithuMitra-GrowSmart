package com.raithumitra.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.raithumitra.data.local.entity.Contract;
import com.raithumitra.data.repository.ContractRepository;
import java.util.List;

public class ContractViewModel extends AndroidViewModel {

    private ContractRepository repository;
    private LiveData<List<Contract>> allContracts;
    private LiveData<String> networkError;

    public ContractViewModel(Application application) {
        super(application);
        repository = new ContractRepository(application);
        allContracts = repository.getAllContracts();
        networkError = repository.getNetworkError();
    }

    public LiveData<List<Contract>> getAllContracts() {
        return allContracts;
    }

    public LiveData<String> getNetworkError() {
        return networkError;
    }
}
