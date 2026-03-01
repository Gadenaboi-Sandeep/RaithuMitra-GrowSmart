package com.raithumitra.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.raithumitra.data.local.entity.Equipment;
import com.raithumitra.data.repository.EquipmentRepository;
import java.util.List;

public class EquipmentViewModel extends AndroidViewModel {

    private EquipmentRepository repository;
    private LiveData<List<Equipment>> allEquipment;
    private LiveData<String> networkError;

    public EquipmentViewModel(Application application) {
        super(application);
        repository = new EquipmentRepository(application);
        allEquipment = repository.getAllEquipment();
        networkError = repository.getNetworkError();
    }

    public LiveData<List<Equipment>> getAllEquipment() {
        return allEquipment;
    }

    public LiveData<String> getNetworkError() {
        return networkError;
    }

    public void deleteEquipment(int equipmentId, EquipmentRepository.RepositoryCallback callback) {
        repository.deleteEquipment(equipmentId, callback);
    }
}
