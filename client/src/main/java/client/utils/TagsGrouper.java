package client.utils;

import client.scenes.AddTagCtrl;
import client.scenes.ManageTagsCtrl;
import javafx.scene.Parent;
import javafx.util.Pair;

public record TagsGrouper(
    Pair<AddTagCtrl, Parent> addTag,
    Pair<ManageTagsCtrl, Parent> manageTag
    ) {

}
