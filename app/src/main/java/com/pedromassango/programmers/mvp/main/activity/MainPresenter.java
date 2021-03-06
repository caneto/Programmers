package com.pedromassango.programmers.mvp.main.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.config.Settings;
import com.pedromassango.programmers.config.SettingsPreference;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.IGetUserListener;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.services.GoogleServices;
import com.pedromassango.programmers.services.LocalBroadcast;

import static com.pedromassango.programmers.extras.Constants._DEVELOP_MODE;
import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 23-02-2017 14:58.
 */

public class MainPresenter implements Contract.Presenter, IGetUserListener, Contract.OnDialogListener {

    private Usuario usuario = null;
    private Contract.View view;
    private Model model;

    public MainPresenter(Contract.View view) {
        this.view = view;
        this.model = new Model(this);
    }

    @Override
    public Context getContext() {
        return ((Context) view);
    }

    public Contract.View getView() {
        return view;
    }

    @Override
    public void initialize(Intent intent, Bundle savedState) {
        showLog("MAINPRENSETER STARTED");

        // Check if GooglePlayServices is avaliable and Installed
        GoogleServices services = new GoogleServices((Activity) getContext());
        if (!services.isGooglePlayServicesAvailable()) {
            return;
        }

        // Check if we have an instance saved
        if (savedState != null) {
            usuario = savedState.getParcelable(Constants.EXTRA_USER);
            showLog("show info from - savedState");
            // Show Usuario data and reload and get posts
            showUserInfoAndGetAllAppData(usuario);
            return;
        }

        // Check if the Activity sender, send an data
        // And if the data sent is an Usuario data.
        if (intent != null && intent.hasExtra(Constants.EXTRA_USER)) {
            usuario = intent.getParcelableExtra(Constants.EXTRA_USER);
            showLog("show info from - intent");
            // Show Usuario data and reload and get posts
            showUserInfoAndGetAllAppData(usuario);
            return;
        }

        // Test only
        if(Constants._DEVELOP_MODE){
            view.showHeaderInfo(Util.getUser());
            // set Empty, to get all default info on  all fragments
            view.setFragmentByCategory("");
            return;
        }

        showLog("getiing info from - firebase");

        //Call server worker here
        view.showProgress(R.string.getting_user_info);
        model.getUser(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.EXTRA_USER, usuario);
    }

    @Override
    public void setFragmentByCategory(String category) {

        view.setFragmentByCategory(category);
    }

    // Show Usuario data and get all Posts
    private void showUserInfoAndGetAllAppData(Usuario mUsuario) {
        showLog("showing user info...");
        showLog("setting up all fragments...");

        view.showHeaderInfo(mUsuario);

        // set Empty, to get all default info on  all fragments
        view.setFragmentByCategory("");
    }

    @Override
    public void onLogoutClicked() {

        model.logoutUser();
    }



    //To check APP versionCode
    @Override
    public PackageInfo packageInfo() {
        PackageInfo packageInfo = null;
        try {
            String packageName = getContext().getPackageName();
            packageInfo = getContext().getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            showLog("GET PACKAGE_NAME_ERROR", e.getMessage());
        }

        return packageInfo;
    }

    @Override
    public String getSkill(int skill) {
        return String.format("%s%s", skill, "%");
    }

    /**
     * Hadle recyclerView scroll status
     * just to show FAB
     *
     * @param newState the new state of the recyclerView
     */
    @Override
    public void onScrollStateChanged(int newState) {
        if (newState != RecyclerView.SCROLL_STATE_IDLE &&
                newState != RecyclerView.SCROLL_STATE_DRAGGING) {
            view.setFABVisibility(false);
        } else {
            view.setFABVisibility(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.donate:
                view.startDonateActivity();
                break;

            case R.id.rate:
                view.startRateApp();
                break;

            case R.id.about:
                view.startAboutActivity();
                break;

            case R.id.settings:
                view.startSettingsActivity();
                break;

            case R.id.action_bug_report:
                view.startReportBugActivity();
                break;
        }
        return false;
    }

    @Override
    public void onGetUserSuccess(Usuario usuario) {
        this.usuario = usuario;

        view.dismissprogess();

        // Show Usuario data and reload and get posts
        showUserInfoAndGetAllAppData(usuario);
    }

    @Override
    public void onGetUserError(String error) {
        view.dismissprogess();

        if (!_DEVELOP_MODE) {
            view.showDialogGetUserInfoError(error, this);
        }
    }

    @Override
    public void onRetry() {

        //Call server worker here
        view.showProgress(R.string.getting_user_info);
        model.getUser(this);
    }

    @Override
    public void onQuit() {

        view.quit();
    }

    @Override
    public void leavingActivity() {

        model.onDestroy();
    }
}
