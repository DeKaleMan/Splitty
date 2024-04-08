package client.scenes;

import client.utils.ServerUtils;

import javax.inject.Inject;

public class AddTagCtrl {
    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;

    @Inject
    public AddTagCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }
}
