package client.utils;

import client.scenes.AdminLoginCtrl;
import javafx.scene.Parent;
import javafx.util.Pair;

public record AdminWindows(Pair<AdminLoginCtrl, Parent> adminLogin) {
}
