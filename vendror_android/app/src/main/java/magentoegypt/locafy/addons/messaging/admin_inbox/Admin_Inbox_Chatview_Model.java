package magentoegypt.locafy.addons.messaging.admin_inbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Admin_Inbox_Chatview_Model  {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("sender_name")
    @Expose
    private String sender_name;
    @SerializedName("image")
    @Expose
    private List<String> image;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("sender")
    @Expose
    private String sender;

    public String getMessage() {
        if (message!=null)
            return message;
        else
            return "";
    }

    public String getSender_name() {
        return sender_name;
    }

    public List<String> getImage() {
        return image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getSender() {
        if (sender!=null)
            return sender;
        else
            return "";
    }

}
