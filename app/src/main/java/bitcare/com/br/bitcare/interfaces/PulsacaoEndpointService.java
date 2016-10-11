package bitcare.com.br.bitcare.interfaces;

import bitcare.com.br.bitcare.models.PulsacaoRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Rafael on 09/10/2016.
 */

public interface PulsacaoEndpointService {

    @POST("/pulsacoes")
    Call<Void> registrar(@Body PulsacaoRequest user);

}
