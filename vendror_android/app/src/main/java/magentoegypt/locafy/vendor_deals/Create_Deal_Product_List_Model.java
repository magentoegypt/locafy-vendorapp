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

public class Create_Deal_Product_List_Model {

    public String product_id_s;
    public String product_name_s;
    public String type_s;
    public String price_s;
    public String qty_s;
    public String check_status;



    public  Create_Deal_Product_List_Model(
            String product_id,
            String product_name,
            String type,
            String price,
            String qty,
            String check_status
    ) {
        this.product_id_s = product_id;
        this.product_name_s = product_name;
        this.type_s = type;
        this.price_s = price;
        this.qty_s = qty;
        this.check_status = check_status;
    }

    public String getProduct_id_s() {
        return product_id_s;
    }

    public void setProduct_id_s(String product_id_s) {
        this.product_id_s = product_id_s;
    }

    public String getProduct_name_s() {
        return product_name_s;
    }

    public void setProduct_name_s(String product_name_s) {
        this.product_name_s = product_name_s;
    }

    public String getType_s() {
        return type_s;
    }

    public void setType_s(String type_s) {
        this.type_s = type_s;
    }

    public String getPrice_s() {
        return price_s;
    }

    public void setPrice_s(String price_s) {
        this.price_s = price_s;
    }

    public String getQty_s() {
        return qty_s;
    }

    public void setQty_s(String qty_s) {
        this.qty_s = qty_s;
    }

    public String getCheck_status() {
        return check_status;
    }

    public void setCheck_status(String check_status) {
        this.check_status = check_status;
    }
}

