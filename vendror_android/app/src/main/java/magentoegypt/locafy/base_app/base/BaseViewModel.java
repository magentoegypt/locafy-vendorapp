package magentoegypt.locafy.base_app.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel<E extends RequestWatcher> extends ViewModel {
    E repo;
    public void setRepo(@NonNull E repo) {
        this.repo = repo;
        loading = repo._loading;
        error = repo._error;
    }
    public LiveData<Boolean> loading;
    public LiveData<String> error;
    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }
}
