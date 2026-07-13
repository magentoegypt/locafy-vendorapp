package magentoegypt.locafy.addons.advance_Report.appBase

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import magentoegypt.locafy.R
import magentoegypt.locafy.addons.advance_Report.appBase.AppUtils.Companion.hideDialog
import magentoegypt.locafy.addons.advance_Report.appBase.AppUtils.Companion.showRequestDialog


abstract class BaseActivityWithViewModel<T : BaseViewModel<BaseAppRepo>> :
    AppCompatActivity(), FragmentManager.OnBackStackChangedListener {
    var viewModel: T? = null
    var view: View? = null
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = setView()
        viewModel = setVModel()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navAdvanceReport) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        val toolbar: Toolbar = findViewById(R.id.topAppBar)
        NavigationUI.setupWithNavController(
            toolbar, navController, appBarConfiguration
        )

        supportFragmentManager.addOnBackStackChangedListener(this);

        init()
        registerApiObserver()


    }



    protected abstract fun setView(): View?
    private fun registerApiObserver() {
        viewModel!!.error!!.observe(
            this
        ) { data: String? ->
            showDialogOnApiFailure(
                data,
                "Failed to load !"
            )
        }
        viewModel!!.loading!!.observe(
            this
        ) { isLoading: Boolean ->
            updateLoading(
                isLoading
            )
        }
    }

    private fun showDialogOnApiFailure(msg: String?, title: String?) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .setIcon(R.drawable.ic_launcher_foreground)
            .setPositiveButton(
                "Retry"
            ) { dialog: DialogInterface, id: Int ->
                dialog.cancel()
                onRetryClick()
            }
            .setNegativeButton(
                "Close"
            ) { dialogInterface: DialogInterface?, i: Int -> finish() }
            .setCancelable(false)
            .show()
    }

    private fun updateLoading(isLoading: Boolean) {
        if (isLoading) showRequestDialog(this) else hideDialog()
    }

    fun showSnackBar(msg: String?) {
        val snack = Snackbar.make(view!!, msg!!, Snackbar.LENGTH_LONG)
        val view = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.BOTTOM
        view.layoutParams = params
        snack.show()
        snack.setAction("Retry") { view1: View? -> onRetryClick() }
    }

    abstract fun init()
    abstract fun setVModel(): T
    fun onRetryClick() {}

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "BaseActivity"

    }

    protected fun setUpTopAppBar(topAppBar: MaterialToolbar) {
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.filterBottomScreen -> {
                    navController.navigate(R.id.filterBottomScreen)
                    true
                }
                R.id.search -> {
                    showToast("clicked on search")
                    true
                }
                else -> false
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onBackStackChanged() {
        val canGoBack = supportFragmentManager.backStackEntryCount > 0
        Log.d("canGoBack", "onBackStackChanged: $canGoBack")
    }
}
