package com.raithumitra.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.raithumitra.data.local.AppDatabase;
import com.raithumitra.data.local.dao.LaborerDao;
import com.raithumitra.data.local.entity.Laborer;
import com.raithumitra.data.remote.ApiService;
import com.raithumitra.data.remote.RetrofitClient;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaborRepository {
    private LaborerDao laborerDao;
    private LiveData<List<Laborer>> allLaborers;
    private ApiService apiService;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private MutableLiveData<String> networkError = new MutableLiveData<>();

    public LaborRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        laborerDao = db.laborerDao();
        allLaborers = laborerDao.getAllLaborers();
        apiService = RetrofitClient.getApiService(application);

        refreshLaborers();
    }

    public LiveData<List<Laborer>> getAllLaborers() {
        return allLaborers;
    }

    public LiveData<String> getNetworkError() {
        return networkError;
    }

    public void refreshLaborers() {
        apiService.getLaborers().enqueue(new Callback<List<Laborer>>() {
            @Override
            public void onResponse(Call<List<Laborer>> call, Response<List<Laborer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executor.execute(() -> {
                        laborerDao.deleteAll();
                        laborerDao.insertAll(response.body());
                    });
                    networkError.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Laborer>> call, Throwable t) {
                networkError.postValue("Offline Mode: Showing cached data");
            }
        });
    }

    public void hireLaborer(com.raithumitra.data.local.entity.LaborHiring hiring, RepositoryCallback callback) {
        apiService.hireLaborer(hiring).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Failed to hire laborer");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Network Error");
            }
        });
    }

    public void createLaborerProfile(Laborer laborer, RepositoryCallback callback) {
        apiService.createLaborerProfile(laborer).enqueue(new Callback<Laborer>() {
            @Override
            public void onResponse(Call<Laborer> call, Response<Laborer> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Failed to update profile");
                }
            }

            @Override
            public void onFailure(Call<Laborer> call, Throwable t) {
                callback.onError("Network Error");
            }
        });
    }

    public interface RepositoryCallback {
        void onSuccess();

        void onError(String message);
    }

    private MutableLiveData<List<com.raithumitra.data.remote.model.LaborHiringResponse>> receivedLaborRequests;

    public LiveData<List<com.raithumitra.data.remote.model.LaborHiringResponse>> getReceivedLaborRequests() {
        if (receivedLaborRequests == null) {
            receivedLaborRequests = new MutableLiveData<>();
            refreshReceivedLaborRequests();
        }
        return receivedLaborRequests;
    }

    public void refreshReceivedLaborRequests() {
        if (receivedLaborRequests == null) {
            receivedLaborRequests = new MutableLiveData<>();
        }
        apiService.getReceivedLaborRequests()
                .enqueue(new Callback<List<com.raithumitra.data.remote.model.LaborHiringResponse>>() {
                    @Override
                    public void onResponse(Call<List<com.raithumitra.data.remote.model.LaborHiringResponse>> call,
                            Response<List<com.raithumitra.data.remote.model.LaborHiringResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            receivedLaborRequests.setValue(response.body());
                        } else {
                            receivedLaborRequests.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<com.raithumitra.data.remote.model.LaborHiringResponse>> call,
                            Throwable t) {
                        receivedLaborRequests.setValue(null);
                    }
                });
    }

    public void updateLaborRequestStatus(int hiringId, String status, RepositoryCallback callback) {
        apiService.updateLaborRequestStatus(hiringId, status).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Failed to update status");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
