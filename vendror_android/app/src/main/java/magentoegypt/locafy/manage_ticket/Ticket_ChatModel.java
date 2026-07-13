package magentoegypt.locafy.manage_ticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Ticket_ChatModel {
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("from")
    @Expose
    private String from;

    @SerializedName("message_time")
    @Expose
    private String message_time;

    @SerializedName("image")
    @Expose
    private String image;

    public String getMessage() {
        return message;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage_time() {
        return message_time;
    }

    public String getImage() {
        return image;
    }
}
