package magentoegypt.locafy.addons.faq;

import org.json.JSONObject;

public interface EditAndDeleteListener {
    void onClick(JSONObject obj, int type);

    void onLoadMoreClick();
}
