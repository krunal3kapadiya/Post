/*
 *  Copyright 2017 Keval Patel.
 *
 *  Licensed under the GNU General Public License, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.common.base

import android.annotation.SuppressLint
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import butterknife.ButterKnife
import com.common.utils.ViewUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Keval on 17-Dec-16.
 * This is the root class for the activity that extends [AppCompatActivity]. Use this class instead
 * of [AppCompatActivity] through out the application.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    val disposables = CompositeDisposable()
    protected var isNetworkCallRunning = false

    protected fun networkingCallRunning() {
        isNetworkCallRunning = true
    }

    protected fun networkingCallComplete() {
        isNetworkCallRunning = false
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        //Bind butter knife
        ButterKnife.bind(this)
    }

    /**
     * Set the toolbar of the activity.

     * @param toolbarId    resource id of the toolbar
     * *
     * @param title        title of the activity
     * *
     * @param showUpButton true if toolbar should display up indicator.
     */
    fun setToolbar(toolbarId: Int,
                   @StringRes title: Int,
                   showUpButton: Boolean) {
        val toolbar = findViewById<Toolbar>(toolbarId)
        setSupportActionBar(toolbar)
        setToolbar(title, showUpButton)
    }

    /**
     * Set the toolbar of the activity.

     * @param toolbarId    resource id of the toolbar
     * *
     * @param title        title of the activity
     * *
     * @param showUpButton true if toolbar should display up indicator.
     */
    fun setToolbar(toolbarId: Int,
                   title: String?,
                   showUpButton: Boolean) {
        val toolbar = findViewById<Toolbar>(toolbarId)
        setSupportActionBar(toolbar)
        setToolbar(title, showUpButton)
    }

    /**
     * Set the toolbar.

     * @param title        Activity title string resource
     * *
     * @param showUpButton true if toolbar should display up indicator.
     */
    protected fun setToolbar(@StringRes title: Int,
                             showUpButton: Boolean) {
        setToolbar(getString(title), showUpButton)
    }

    /**
     * Set the toolbar.

     * @param title        Activity title string.
     * *
     * @param showUpButton true if toolbar should display up indicator.
     */
    @SuppressLint("RestrictedApi")
    protected fun setToolbar(title: String?,
                             showUpButton: Boolean) {
        //set the title
        supportActionBar!!.title = title ?: ""

        //Set the up indicator
        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(showUpButton)
        supportActionBar!!.setHomeButtonEnabled(showUpButton)
        supportActionBar!!.setDisplayHomeAsUpEnabled(showUpButton)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            //Hide the keyboard if any view is currently in focus.
            if (currentFocus != null) ViewUtils.hideKeyboard(currentFocus)
            supportFinishAfterTransition()
            false
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    /**
     * Add the subscription to the [CompositeDisposable].

     * @param disposable [Disposable]
     */
    fun addSubscription(disposable: Disposable?) {
        if (disposable == null) return
        disposables.add(disposable)
    }

    public override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    fun showSnackbar(@StringRes message: Int,
                     @StringRes actionName: Int,
                     onActionClick: View.OnClickListener?) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
                .setAction(actionName, onActionClick)
                .show()
    }

    fun showSnackbar(@StringRes message: Int) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }
}