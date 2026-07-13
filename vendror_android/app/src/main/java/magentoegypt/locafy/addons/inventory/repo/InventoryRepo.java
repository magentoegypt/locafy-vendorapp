package magentoegypt.locafy.addons.inventory.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import magentoegypt.locafy.addons.inventory.api.InventoryApi;
import magentoegypt.locafy.base_app.base.RequestWatcher;
import magentoegypt.locafy.base_app.base.RetrofitInstance;

import java.util.Map;

public class InventoryRepo extends RequestWatcher {
    InventoryApi api;

    public InventoryRepo() {
        this.api = RetrofitInstance.getInstance().create(InventoryApi.class);
    }


    MutableLiveData<Object> _observerOutOfStockData = new MutableLiveData<>();

    public MutableLiveData<Object> getOutOfStockData() {
        return _observerOutOfStockData;
    }

    MutableLiveData<Object> _observerLowStockData = new MutableLiveData<>();

    public void loadOutOfStockData(Map<String, Object> params) {
        enqueueApi(api.loadOutOfStockData(params), _observerLowStockData);
    }

    public LiveData<Object> getLowStockData() {
        return _observerLowStockData;
    }

    public void loadLowStockData(Map<String, Object> params) {
        enqueueApi(api.loadLowStockData(params), _observerLowStockData);
    }

    MutableLiveData<Object> _observerSaveInventory = new MutableLiveData<>();

    public LiveData<Object> getSaveInventory() {
        return _observerSaveInventory;
    }

    public void saveInventory(Map<String, Object> params) {
        enqueueApi(api.saveInventory(params), _observerSaveInventory);
    }

    MutableLiveData<Object> _observerMinimumInventory = new MutableLiveData<>();

    public LiveData<Object> getMinimumInventoryData() {
        return _observerMinimumInventory;
    }
    public void loadMinimumInventoryData(Map<String, Object> params) {
        enqueueApi(api.loadMinimumInventoryData(params), _observerMinimumInventory);
    }

}
