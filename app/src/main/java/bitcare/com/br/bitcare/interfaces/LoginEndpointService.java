package bitcare.com.br.bitcare.interfaces;

import bitcare.com.br.bitcare.models.CloudantViewContainerDTO;
import bitcare.com.br.bitcare.models.LoginResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Felipe on 09/10/2016.
 */

public interface LoginEndpointService {

    @GET("/bitcare_db/_design/usuarios/_view/autenticacao?")
    Call<CloudantViewContainerDTO<LoginResponse>> logar(@Query("key") String loginKey);

}
