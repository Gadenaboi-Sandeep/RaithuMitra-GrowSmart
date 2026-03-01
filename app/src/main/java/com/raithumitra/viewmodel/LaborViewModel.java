package com.raithumitra.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.raithumitra.data.local.entity.Laborer;
import com.raithumitra.data.repository.LaborRepository;
import java.util.List;

public class LaborViewModel extends AndroidViewModel {

    private LaborRepository repository;
    private LiveData<List<Laborer>> allLaborers;
    private LiveData<String> networkError;

    public LaborViewModel(Application application) {
        super(application);
        repository = new LaborRepository(application);
        allLaborers = repository.getAllLaborers();
        networkError = repository.getNetworkError();
    }

    public LiveData<List<Laborer>> getAllLaborers() {
        return allLaborers;
    }

    public LiveData<String> getNetworkError() {
        return networkError;
    }

}
