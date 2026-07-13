package magentoegypt.locafy.addons.faq.viewModel;

import androidx.lifecycle.LiveData;

import magentoegypt.locafy.addons.faq.repo.FaqRepo;
import magentoegypt.locafy.base_app.base.BaseViewModel;

import java.util.Map;

public class FaqViewModel extends BaseViewModel<FaqRepo> {
    FaqRepo repo;

    public FaqViewModel() {
        repo = new FaqRepo();
        setRepo(repo);
    }

    public LiveData<Object> observeFaqData() {
        return repo.getFaqData();
    }

    public void loadFaqData(Map<String, Object> postData) {
        repo.loadFaqData(postData);
    }

    public void deleteFAQ(Map<String, Object> deleteMap) {
        repo.deleteFAQ(deleteMap);
    }

    public LiveData<Object> deleteObserver() {
        return repo.deleteFaqObserver();
    }

    public void editFAQ() {
    }

    public LiveData<Object> editFaq(Map<String, Object> objectMap) {
        return repo.editFaq(objectMap);
    }


    public LiveData<Object> saveFaqData(Map<String, Object> objectMap) {
        return repo.addFaqObserver(objectMap);
    }

    public void addFaq(Map<String, Object> objectMap) {
        repo.addFaq(objectMap);

    }

    public void update(Map<String, Object> objectMap) {
        repo.addFaq(objectMap);
    }

    public LiveData<Object> fetchFields(Map<String, Object> objectMap) {
        return repo.fetchFields(objectMap);
    }

    public LiveData<Object> observeQuestionList(Map<String, Object> parameters) {
        return repo.observeQuestionList(parameters);
    }
}
