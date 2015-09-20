package com.lobbyswitch;

/**
 * Created by derek on 9/19/2015.
 */
public interface Callback<V> {

    void onSuccess(V value);
}
