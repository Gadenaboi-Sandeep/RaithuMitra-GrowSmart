package com.raithumitra.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.raithumitra.data.local.SessionManager;
import com.raithumitra.data.remote.model.AuthResponse;
import com.raithumitra.data.repository.AuthRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private AuthRepository authRepository;
    private SessionManager sessionManager;
    private MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LoginViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        sessionManager = new SessionManager(application);
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void login(String email, String password) {
        LiveData<String> result = authRepository.login(email, password);
        result.observeForever(status -> {
            if ("SUCCESS".equals(status)) {
                loginSuccess.postValue(true);
            } else {
                errorMessage.postValue(status);
            }
        });
    }
}
