package bitcare.com.br.bitcare.interfaces;

import bitcare.com.br.bitcare.models.LoginRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Felipe on 09/10/2016.
 */

public interface LoginEndpointService {

    @POST("login/autenticacao")
    Call<Void> logar(@Body LoginRequest loginRequest);

}
