package com.raithumitra.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.raithumitra.data.local.AppDatabase;
import com.raithumitra.data.local.dao.ContractDao;
import com.raithumitra.data.local.entity.Contract;
import com.raithumitra.data.remote.ApiService;
import com.raithumitra.data.remote.RetrofitClient;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContractRepository {
    private ContractDao contractDao;
    private LiveData<List<Contract>> allContracts;
    private ApiService apiService;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private MutableLiveData<String> networkError = new MutableLiveData<>();

    public ContractRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        contractDao = db.contractDao();
        allContracts = contractDao.getAllContracts();
        apiService = RetrofitClient.getApiService(application);

        refreshContracts();
    }

    public LiveData<List<Contract>> getAllContracts() {
        return allContracts;
    }

    public LiveData<String> getNetworkError() {
        return networkError;
    }

    public void refreshContracts() {
        apiService.getContracts().enqueue(new Callback<List<Contract>>() {
            @Override
            public void onResponse(Call<List<Contract>> call, Response<List<Contract>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executor.execute(() -> {
                        contractDao.deleteAll();
                        contractDao.insertAll(response.body());
                    });
                    networkError.postValue(null); // Clear error
                }
            }

            @Override
            public void onFailure(Call<List<Contract>> call, Throwable t) {
                networkError.postValue("Offline Mode: Showing cached data");
            }
        });
    }

    public void createContract(Contract contract, RepositoryCallback callback) {
        apiService.createContract(contract).enqueue(new Callback<Contract>() {
            @Override
            public void onResponse(Call<Contract> call, Response<Contract> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                    refreshContracts(); // Refresh list after creation
                } else {
                    callback.onError("Failed to create contract");
                }
            }

            @Override
            public void onFailure(Call<Contract> call, Throwable t) {
                callback.onError("Network Error");
            }
        });
    }

    public void signContract(int id, RepositoryCallback callback) {
        apiService.signContract(id).enqueue(new Callback<Contract>() {
            @Override
            public void onResponse(Call<Contract> call, Response<Contract> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                    refreshContracts();
                } else {
                    callback.onError("Failed to sign contract");
                }
            }

            @Override
            public void onFailure(Call<Contract> call, Throwable t) {
                callback.onError("Network Error");
            }
        });
    }

    public interface RepositoryCallback {
        void onSuccess();

        void onError(String message);
    }
}
