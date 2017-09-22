package com.owl.chatstory.data.usersource;

import android.content.Intent;

/**
 * Created by lebron on 16-11-7.
 */

public abstract class SignInAuth {

    protected ILoginData.OnAuthListener onAuthListener;

    public abstract void signIn();

    /**
     * implement logout, logout just clear user info from app and doesn`t revoke access
     */
    public abstract void signOut();

    /**
     * implement revoke method from facebook or google inside this method,
     * in callback method from API (Facebook or Google) call {@link ILoginData.OnAuthListener#onRevoke()}
     * to clear shared preferences, clear activity stack and something else
     */
    public abstract void revoke();

    public abstract void release();

    public abstract void resultLogin(int requestCode, int resultCode, Intent data);

    /**
     * you must set onAuthListener to receiver callback events
     * when login was successful, error, cancel or revoke
     *
     * @param onAuthListener
     */
    public void setOnAuthListener(ILoginData.OnAuthListener onAuthListener) {
        this.onAuthListener = onAuthListener;
    }
}
