package bitcare.com.br.bitcare.interfaces;

import java.util.List;

import bitcare.com.br.bitcare.models.PulsacaoDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Rafael on 09/10/2016.
 */

public interface PulsacaoEndpointService {

    @POST("/pulsacoes")
    Call<Void> registrar(@Body PulsacaoDTO user);

    @GET("/pulsacoes?")
    Call<List<PulsacaoDTO>> buscar(@Query("login") String login,
                                   @Query("quantidade") Long quantidade);

}
