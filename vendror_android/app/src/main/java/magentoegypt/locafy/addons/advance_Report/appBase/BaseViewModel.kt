package magentoegypt.locafy.addons.advance_Report.appBase

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel


abstract class BaseViewModel<T : BaseAppRepo?> : ViewModel() {
    private var repo: T? = null
    var error: LiveData<String>? = null
    var loading: LiveData<Boolean>? = null
    fun setRepo(repo: T) {
        this.repo = repo
        error = repo!!._error
        loading = repo._loading
    }
}
