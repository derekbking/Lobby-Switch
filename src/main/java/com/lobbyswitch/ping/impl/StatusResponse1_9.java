package com.lobbyswitch.ping.impl;

import com.lobbyswitch.ping.StatusResponse;

/**
 * Created by derek on 6/9/2016.
 */
public class StatusResponse1_9 extends StatusResponse {

    private Description description;

    @Override
    public String getDescription() {
        return description.getText();
    }

    public class Description {
        private String text;

        public String getText() {
            return text;
        }
    }
}
