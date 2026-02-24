package yonsei_church.yonsei.tv.api;

import android.content.Context;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import yonsei_church.yonsei.tv.app.AppConstant;

public class ApiProvider {
    public static final int REQ_TIMEOUT = 50000;

    public static <S> S createService(Class<S> cls, Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(new Interceptor() {
            public Response intercept(Interceptor.Chain chain) throws IOException {
                return chain.proceed(chain.request().newBuilder().build());
            }
        });
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.connectTimeout(50000, TimeUnit.MILLISECONDS);
        builder.networkInterceptors().add(httpLoggingInterceptor);
        builder.build();
        return new Retrofit.Builder().baseUrl(AppConstant.SERVER_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build().create(cls);
    }
}
