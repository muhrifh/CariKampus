package id.carikampus.service;

import java.util.List;

import id.carikampus.data.model.Kampus;
import id.carikampus.data.model.Prodi;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface KampusService {

    @GET("kampus")
    Call<Kampus> getKampusById(@Query("id") int id);

    @GET("list-kampus")
    Call<List<Kampus>> getListKampus();
}
