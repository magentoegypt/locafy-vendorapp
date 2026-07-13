package magentoegypt.locafy.addons.inventory.viewModels;

import androidx.lifecycle.LiveData;

import magentoegypt.locafy.addons.inventory.repo.InventoryRepo;
import magentoegypt.locafy.base_app.base.BaseViewModel;

import java.util.Map;

public class InventoryViewModel extends BaseViewModel<InventoryRepo> {
    InventoryRepo repo;

    public InventoryViewModel() {
        repo = new InventoryRepo();
        setRepo(repo);
    }

    public LiveData<Object> observeOutOfStockData() {
        return repo.getOutOfStockData();
    }

    public void loadOutOfStockData(Map<String, Object> params) {
        repo.loadOutOfStockData(params);
    }

    public LiveData<Object> observeLowStockData() {
        return repo.getLowStockData();
    }

    public void loadLowStockData(Map<String, Object> objectMap) {
        repo.loadLowStockData(objectMap);
    }

    public LiveData<Object> observeSaveInventory() {
        return repo.getSaveInventory();
    }

    public void saveInventory(Map<String, Object> objectMap) {
        repo.saveInventory(objectMap);
    }

    public LiveData<Object> observeMinimumInventory() {
        return repo.getMinimumInventoryData();
    }
    public void loadMinimumInventory(Map<String, Object> objectMap){
        repo.loadMinimumInventoryData(objectMap);
    }
}
