package com.raithumitra.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.raithumitra.data.local.AppDatabase;
import com.raithumitra.data.local.dao.EquipmentDao;
import com.raithumitra.data.local.entity.Equipment;
import com.raithumitra.data.local.entity.EquipmentBooking;
import com.raithumitra.data.remote.ApiService;
import com.raithumitra.data.remote.RetrofitClient;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EquipmentRepository {
    private EquipmentDao equipmentDao;
    private LiveData<List<Equipment>> allEquipment;
    private ApiService apiService;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private MutableLiveData<String> networkError = new MutableLiveData<>();

    public EquipmentRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        equipmentDao = db.equipmentDao();
        allEquipment = equipmentDao.getAllEquipment();
        apiService = RetrofitClient.getApiService(application);

        refreshEquipment();
    }

    public LiveData<List<Equipment>> getAllEquipment() {
        return allEquipment;
    }

    public LiveData<String> getNetworkError() {
        return networkError;
    }

    public void refreshEquipment() {
        apiService.getEquipment().enqueue(new Callback<List<Equipment>>() {
            @Override
            public void onResponse(Call<List<Equipment>> call, Response<List<Equipment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executor.execute(() -> {
                        equipmentDao.deleteAll();
                        equipmentDao.insertAll(response.body());
                    });
                    networkError.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Equipment>> call, Throwable t) {
                networkError.postValue("Offline Mode: Showing cached data");
            }
        });
    }

    public void addEquipment(Equipment equipment, RepositoryCallback callback) {
        apiService.addEquipment(equipment).enqueue(new Callback<Equipment>() {
            @Override
            public void onResponse(Call<Equipment> call, Response<Equipment> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                    refreshEquipment();
                } else {
                    callback.onError("Failed to add equipment");
                }
            }

            @Override
            public void onFailure(Call<Equipment> call, Throwable t) {
                callback.onError("Network Error");
            }
        });
    }

    public void bookEquipment(EquipmentBooking booking, RepositoryCallback callback) {
        apiService.bookEquipment(booking).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Failed to book equipment");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Network Error");
            }
        });
    }

    public void uploadImage(okhttp3.MultipartBody.Part file, ImageUploadCallback callback) {
        apiService.uploadFile(file).enqueue(new Callback<java.util.Map<String, String>>() {
            @Override
            public void onResponse(Call<java.util.Map<String, String>> call,
                    Response<java.util.Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String fileUrl = response.body().get("fileUrl");
                    callback.onSuccess(fileUrl);
                } else {
                    callback.onError("Image upload failed");
                }
            }

            @Override
            public void onFailure(Call<java.util.Map<String, String>> call, Throwable t) {
                callback.onError("Network Error: " + t.getMessage());
            }
        });
    }

    public interface RepositoryCallback {
        void onSuccess();

        void onError(String message);
    }

    public void deleteEquipment(int equipmentId, RepositoryCallback callback) {
        apiService.deleteEquipment(equipmentId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                    refreshEquipment(); // Refresh list after deletion
                } else {
                    callback.onError("Failed to delete equipment");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Network Error: " + t.getMessage());
            }
        });
    }

    public interface ImageUploadCallback {
        void onSuccess(String imageUrl);

        void onError(String message);
    }
}
