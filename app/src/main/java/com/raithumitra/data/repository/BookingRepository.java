package com.raithumitra.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.raithumitra.data.local.SessionManager;
import com.raithumitra.data.remote.ApiService;
import com.raithumitra.data.remote.RetrofitClient;
import com.raithumitra.data.remote.model.EquipmentBookingResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class BookingRepository {

    private ApiService apiService;
    private SessionManager sessionManager;

    public BookingRepository(Context context) {
        apiService = RetrofitClient.getApiService(context);
        sessionManager = new SessionManager(context);
    }

    private MutableLiveData<List<EquipmentBookingResponse>> receivedBookings;

    public LiveData<List<EquipmentBookingResponse>> getReceivedBookings() {
        if (receivedBookings == null) {
            receivedBookings = new MutableLiveData<>();
            refreshReceivedBookings();
        }
        return receivedBookings;
    }

    public void refreshReceivedBookings() {
        if (receivedBookings == null) {
            receivedBookings = new MutableLiveData<>();
        }
        apiService.getReceivedBookings().enqueue(new Callback<List<EquipmentBookingResponse>>() {
            @Override
            public void onResponse(Call<List<EquipmentBookingResponse>> call,
                    Response<List<EquipmentBookingResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    receivedBookings.setValue(response.body());
                } else {
                    receivedBookings.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<EquipmentBookingResponse>> call, Throwable t) {
                receivedBookings.setValue(null);
            }
        });
    }

    public void updateBookingStatus(int bookingId, String status, StatusCallback callback) {
        apiService.updateBookingStatus(bookingId, status).enqueue(new Callback<Void>() {
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

    public interface StatusCallback {
        void onSuccess();

        void onError(String message);
    }
}
