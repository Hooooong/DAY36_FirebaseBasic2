package com.hooooong.firebasebasic2;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Android Hong on 2017-11-01.
 */

public interface IRetro {
    // Return Type  FunctionName(인자)
    // @Body : http Body 에 담겨서 넘어간다.
    // @Query : GET 일 때 ? 뒤에 Query 형식으로 들어간다.
    // ex) @GET("sendNotification), @Query String post_data
    // sendNotification?post_data;
    @POST("sendNotification")
    Call<ResponseBody> sendNotification(@Body RequestBody post_data);
}
