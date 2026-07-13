package magentoegypt.locafy.vendor_deals;
/*
 *  /**
 *      * CedCommerce
 *      *
 *      * NOTICE OF LICENSE
 *      *
 *      * This source file is subject to the End User License Agreement (EULA)
 *      * that is bundled with this package in the file LICENSE.txt.
 *      * It is also available through the world-wide-web at this URL:
 *      * http://cedcommerce.com/license-agreement.txt
 *      *
 *      * @category  Ced
 *      * @package   MageNative
 *      * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *      * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *      * @license      http://cedcommerce.com/license-agreement.txt
 *
 */

public class Deal_List_Model {

    public String deal_id_s;
    public String product_name_s;
    public String end_date_s;
    public String start_date_s;
    public String admin_status_s;
    public String status_s;
    public String deal_price_s;
    public String product_id_s;



    public Deal_List_Model(
            String deal_id,
            String product_name,
            String end_date,
            String start_date,
            String admin_status,
            String status,
            String deal_price,
            String product_id
    ) {
        this.deal_id_s = deal_id;
        this.product_name_s = product_name;
        this.end_date_s = end_date;
        this.start_date_s = start_date;
        this.admin_status_s = admin_status;
        this.status_s = status;
        this.deal_price_s = deal_price;
        this.product_id_s = product_id;
    }
    public String getProduct_id_s() {
        return product_id_s;
    }

    public void setProduct_id_s(String product_id_s) {
        this.product_id_s = product_id_s;
    }

    public String getDeal_id_s() {
        return deal_id_s;
    }

    public void setDeal_id_s(String deal_id_s) {
        this.deal_id_s = deal_id_s;
    }

    public String getProduct_name_s() {
        return product_name_s;
    }

    public void setProduct_name_s(String product_name_s) {
        this.product_name_s = product_name_s;
    }

    public String getEnd_date_s() {
        return end_date_s;
    }

    public String getStart_date_s() {
        return start_date_s;
    }

    public void setEnd_date_s(String end_date_s) {
        this.end_date_s = end_date_s;
    }

    public String getAdmin_status_s() {
        return admin_status_s;
    }

    public void setAdmin_status_s(String admin_status_s) {
        this.admin_status_s = admin_status_s;
    }

    public String getStatus_s() {
        return status_s;
    }

    public void setStatus_s(String status_s) {
        this.status_s = status_s;
    }

    public String getDeal_price_s() {
        return deal_price_s;
    }

    public void setDeal_price_s(String deal_price_s) {
        this.deal_price_s = deal_price_s;
    }
}

