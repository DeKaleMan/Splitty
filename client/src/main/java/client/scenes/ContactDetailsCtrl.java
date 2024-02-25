package client.scenes;

import client.utils.ServerUtils;

import javax.inject.Inject;

public class ContactDetailsCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    @Inject
    public ContactDetailsCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }
}
