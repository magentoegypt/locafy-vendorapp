package magentoegypt.locafy.addons.messaging.admin_inbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Admin_Inbox_Model {

    {

    }

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("sender_name")
    @Expose
    private String sender_name;

    @SerializedName("receiver_name")
    @Expose
    private String receiver_name;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("subject")
    @Expose
    private String subject;

    @SerializedName("message_status")
    @Expose
    private String message_status;

    @SerializedName("customer_id")
    @Expose
    private String customer_id;


    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public String getSender_name() {
        return sender_name;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage_status() {
        return message_status;
    }
}
