package magentoegypt.locafy.addons.faq.api;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FaqApi {
    @POST("rest/V1/vfaqapi/faqlist")
    Call<Object> loadFaqData(@Body Map<String, Object> postData);

    @POST("rest/V1/vfaqapi/savefaq")
    Call<Object> editFaq(@Body Map<String, Object> objectMap);

    @POST("rest/V1/vfaqapi/deletefaq")
    Call<Object> deleteFaq(@Body Map<String, Object> objectMap);

    @POST("rest/V1/vfaqapi/savefaq")
    Call<Object> addFaq(@Body Map<String, Object> objectMap);

    @POST("rest/V1/vfaqapi/getfaqattributes")
    Call<Object> fetchFields(@Body Map<String, Object> objectMap);

    @POST("rest/V1/mobifaq/faqlist")
    Call<Object> fetchQuestionList(@Body Map<String, Object> parameters);
}
