package com.pedromassango.programmers.mvp.conversations.chat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.models.Contact;
import com.pedromassango.programmers.mvp.profile.profile.ProfileActivity;

import static com.pedromassango.programmers.extras.Constants.EXTRA_USER_ID;

/**
 * Created by JANU on 23/05/2017.
 */

public class Presenter implements Contract.Presenter {

    private Contract.View view;
    private Context context;
    private static String loggedUserId;

    public Presenter(Contract.View view, Context context) {
        this.view = view;
        this.context = context;
        loggedUserId = PrefsUtil.getId(getContext());
    }

    @Override
    public Context getContext() {

        return context;
    }

    @Override
    public boolean isTheCurrentUser(String userId) {

        return userId.equals(loggedUserId);
    }

    @Override
    public void onContactClicked(Contact contact) {

        Bundle b = new Bundle();

        String contactUserId = contact.getUserId();
        if (isTheCurrentUser(contactUserId)) {
            b.putString(EXTRA_USER_ID, contactUserId);

            view.startProfileActivity(b);
            return;
        }

        b.putParcelable(Constants.EXTRA_FRIEND_CONTACT, contact);

        view.startMessageActivity(b);
    }

    @Override
    public void onImageUserClicked(String imageUrl) {

        Bundle b = new Bundle();
        b.putString(Constants.EXTRA_IMAGE_URL, imageUrl);

        view.startViewImageActivity(b);
    }
}
