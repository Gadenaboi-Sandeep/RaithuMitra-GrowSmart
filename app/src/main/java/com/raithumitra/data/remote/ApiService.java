package com.raithumitra.data.remote;

import com.raithumitra.data.local.entity.Contract;
import com.raithumitra.data.local.entity.Equipment;
import com.raithumitra.data.local.entity.Laborer;
import com.raithumitra.data.remote.model.AuthRequest;
import com.raithumitra.data.remote.model.AuthResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/auth/signup")
    Call<String> signup(@Body AuthRequest request);

    @POST("api/auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @GET("api/contracts")
    Call<List<Contract>> getContracts();

    @POST("api/contracts")
    Call<Contract> createContract(@Body Contract contract);

    @retrofit2.http.PUT("api/contracts/{id}/sign")
    Call<Contract> signContract(@retrofit2.http.Path("id") int id);

    @GET("api/equipment")
    Call<List<Equipment>> getEquipment();

    @POST("api/equipment")
    Call<Equipment> addEquipment(@Body Equipment equipment);

    @POST("api/equipment/book")
    Call<Void> bookEquipment(@Body com.raithumitra.data.local.entity.EquipmentBooking booking);

    @GET("api/labor")
    Call<List<Laborer>> getLaborers();

    @POST("api/labor/hire")
    Call<Void> hireLaborer(@Body com.raithumitra.data.local.entity.LaborHiring hiring);

    @POST("api/labor/profile")
    Call<Laborer> createLaborerProfile(@Body Laborer laborer);

    @retrofit2.http.Multipart
    @POST("api/files/upload")
    Call<java.util.Map<String, String>> uploadFile(@retrofit2.http.Part okhttp3.MultipartBody.Part file);

    @retrofit2.http.DELETE("api/equipment/{id}")
    Call<Void> deleteEquipment(@retrofit2.http.Path("id") int id);

    @GET("api/bookings/received")
    Call<List<com.raithumitra.data.remote.model.EquipmentBookingResponse>> getReceivedBookings();

    @retrofit2.http.PUT("api/bookings/{id}/status")
    Call<Void> updateBookingStatus(@retrofit2.http.Path("id") int id, @Body String status);

    @GET("api/labor/received")
    Call<List<com.raithumitra.data.remote.model.LaborHiringResponse>> getReceivedLaborRequests();

    @retrofit2.http.PUT("api/labor/{id}/status")
    Call<Void> updateLaborRequestStatus(@retrofit2.http.Path("id") int id, @Body String status);
}
