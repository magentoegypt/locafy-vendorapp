package magentoegypt.locafy.addons.faq.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import magentoegypt.locafy.addons.faq.api.FaqApi;
import magentoegypt.locafy.base_app.base.RequestWatcher;
import magentoegypt.locafy.base_app.base.RetrofitInstance;

import java.util.Map;

public class FaqRepo extends RequestWatcher {
    FaqApi api;

    public FaqRepo() {
        this.api = RetrofitInstance.getInstance().create(FaqApi.class);
    }


    MutableLiveData<Object> _observeFawData = new MutableLiveData<>();

    public MutableLiveData<Object> getFaqData() {
        return _observeFawData;
    }

    public void loadFaqData(Map<String, Object> postData) {
        enqueueApi(api.loadFaqData(postData), _observeFawData);
    }

    MutableLiveData<Object> _editFaqObserver = new MutableLiveData<>();

    public LiveData<Object> editFaq(Map<String, Object> objectMap) {
        enqueueApi(api.editFaq(objectMap), _editFaqObserver);
        return _editFaqObserver;
    }

    MutableLiveData<Object> _deleteFaqObserver = new MutableLiveData<>();

    public MutableLiveData<Object> deleteFaqObserver() {
        return _deleteFaqObserver;
    }

    public void deleteFAQ(Map<String, Object> deleteMap) {
        enqueueApi(api.deleteFaq(deleteMap), _deleteFaqObserver);
    }

    MutableLiveData<Object> _addFaqObserver = new MutableLiveData<>();

    public MutableLiveData<Object> addFaqObserver(Map<String, Object> objectMap) {
        enqueueApi(api.addFaq(objectMap), _addFaqObserver);
        return _addFaqObserver;
    }

    public void addFaq(Map<String, Object> objectMap) {
        enqueueApi(api.addFaq(objectMap), _addFaqObserver);

    }

    MutableLiveData<Object> _fieldsObserver = new MutableLiveData<>();

    public LiveData<Object> fetchFields(Map<String, Object> objectMap) {
        enqueueApi(api.fetchFields(objectMap), _fieldsObserver);
        return _fieldsObserver;
    }

    MutableLiveData<Object> questionObserver = new MutableLiveData<>();

    public LiveData<Object> observeQuestionList(Map<String, Object> parameters) {
        enqueueApi(api.fetchQuestionList(parameters), questionObserver);
        return questionObserver;
    }
}
