package com.raithumitra.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.raithumitra.data.local.SessionManager;
import com.raithumitra.data.remote.ApiService;
import com.raithumitra.data.remote.RetrofitClient;
import com.raithumitra.data.remote.model.AuthRequest;
import com.raithumitra.data.remote.model.AuthResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private ApiService apiService;
    private SessionManager sessionManager;

    public AuthRepository(Context context) {
        apiService = RetrofitClient.getApiService(context);
        sessionManager = new SessionManager(context);
    }

    public LiveData<String> login(String phoneNumber, String password) {
        MutableLiveData<String> result = new MutableLiveData<>();

        AuthRequest request = new AuthRequest(phoneNumber, password);
        apiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sessionManager.saveAuthToken(response.body().getToken());
                    sessionManager.saveUserRole(response.body().getRole());
                    sessionManager.saveUserProfile(
                            response.body().getFullName(),
                            response.body().getPhoneNumber(),
                            response.body().getAddress());
                    result.setValue("SUCCESS");
                } else {
                    result.setValue("Login Failed");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                result.setValue("Network Error: " + t.getMessage());
            }
        });

        return result;
    }

    public LiveData<String> signup(String fullName, String phoneNumber, String password, String role, String address) {
        MutableLiveData<String> result = new MutableLiveData<>();

        AuthRequest request = new AuthRequest(fullName, phoneNumber, password, role, address);
        apiService.signup(request).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    result.setValue("SUCCESS");
                } else {
                    try {
                        String errorMsg = response.errorBody() != null ? response.errorBody().string()
                                : "Signup Failed";
                        result.setValue(errorMsg);
                    } catch (Exception e) {
                        result.setValue("Signup Failed");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                result.setValue("Network Error: " + t.getMessage()); // Will show "connect failed..."
            }
        });

        return result;
    }
}
