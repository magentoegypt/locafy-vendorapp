package magentoegypt.locafy.manage_ticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Ticket_Data_Model {

    @SerializedName("ticket_id")
    @Expose
    private String ticket_id;

    @SerializedName("entity_id")
    @Expose
    private String entity_id;

    @SerializedName("created_time")
    @Expose
    private String created_time;

    @SerializedName("customer_name")
    @Expose
    private String customer_name;

    @SerializedName("customer_email")
    @Expose
    private String customer_email;

    @SerializedName("department")
    @Expose
    private String department;

    @SerializedName("subject")
    @Expose
    private String subject;

    @SerializedName("num_msg")
    @Expose
    private String num_msg;

    @SerializedName("order_id")
    @Expose
    private String order_id;

    @SerializedName("priority")
    @Expose
    private String priority;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("ticket_messages")
    @Expose
    private String ticket_messages;


    public String getTicket_id() {
        return ticket_id;
    }

    public String getEntity_id() {
        return entity_id;
    }

    public String getCreated_time() {
        return created_time;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public String getDepartment() {
        return department;
    }

    public String getSubject() {
        return subject;
    }

    public String getNum_msg() {
        return num_msg;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getTicket_messages() {
        return ticket_messages;
    }
}
