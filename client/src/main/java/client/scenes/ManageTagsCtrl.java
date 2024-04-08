package client.scenes;

import client.utils.ServerUtils;

import javax.inject.Inject;

public class ManageTagsCtrl {

    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;

    @Inject
    public ManageTagsCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    public void refreshList() {
    }
}
