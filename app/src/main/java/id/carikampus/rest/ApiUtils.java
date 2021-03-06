package id.carikampus.rest;

import id.carikampus.model.KampusFavorit;
import id.carikampus.service.FotoKampusService;
import id.carikampus.service.KampusFavoritService;
import id.carikampus.service.KampusService;
import id.carikampus.service.KomentarService;
import id.carikampus.service.ProdiFavoritService;
import id.carikampus.service.ProdiService;
import id.carikampus.service.UserLoginService;

public class ApiUtils {

    // This is your server API server ip Address
    // If you use localhost, look for your computer IP Address
    public static final String API_URL = "http://192.168.100.140:8080/";

    // If you use emulator / AVD use this:
    // public static final String API_URL = "http://10.0.2.2:8080";

    private ApiUtils() {

    }

    public static ProdiService getProdiService() {
        return RetrofitClient.getClient(API_URL).create(ProdiService.class);
    }

    public static KampusService getKampusService() {
        return RetrofitClient.getClient(API_URL).create(KampusService.class);
    }

    public static FotoKampusService getFotoKampusService() {
        return RetrofitClient.getClient(API_URL).create(FotoKampusService.class);
    }

    public static UserLoginService getUserLoginService() {
        return RetrofitClient.getClient(API_URL).create(UserLoginService.class);
    }

    public static KampusFavoritService getKampusFavoritService() {
        return RetrofitClient.getClient(API_URL).create(KampusFavoritService.class);
    }

    public static ProdiFavoritService getProdiFavoritService() {
        return RetrofitClient.getClient(API_URL).create(ProdiFavoritService.class);
    }

    public static KomentarService getKomentarService() {
        return RetrofitClient.getClient(API_URL).create(KomentarService.class);
    }
}
