package magentoegypt.locafy.addons.inventory.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InventoryApi {
    @POST("rest/V1/vinventory/Getoutofstockproducts")
    Call<Object> loadOutOfStockData(@Body Map<String, Object> params);

    @POST("rest/V1/vinventory/GetLowstockproducts")
    Call<Object> loadLowStockData(@Body Map<String, Object> params);

    @POST("rest/V1/vinventory/saveMinimumInventory")
    Call<Object> saveInventory(@Body Map<String, Object> params);

    @POST("rest/V1/vinventory/getMinimumInventory")
    Call<Object> loadMinimumInventoryData(@Body Map<String, Object> params);
}
