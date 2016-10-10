package bitcare.com.br.bitcare;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Rafael on 09/10/2016.
 */

public interface PulsacaoService {

    @POST("/pulsacoes")
    Call<Void> registrar(@Body PulsacaoDTO user);

}
