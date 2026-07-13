/**
* CedCommerce
*
* NOTICE OF LICENSE
*
* This source file is subject to the End User License Agreement (EULA)
* that is bundled with this package in the file LICENSE.txt.
* It is also available through the world-wide-web at this URL:
* http://cedcommerce.com/license-agreement.txt
*
* @category  Ced
* @package   MageNative MultiVendor
* @author    CedCommerce Core Team <connect@cedcommerce.com >
* @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
* @license      http://cedcommerce.com/license-agreement.txt
*/

import UIKit

class ced_vendorSingleorder: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource {
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var vendorSingleOrder: UITableView!
    
    var orderId = String()
    var productsData = [[String:String]]()
    var shippingAddress = [String:String]()
    var billing2Address = [String:String]()
    var OrderInfo    =  [String:String]()
    override func viewDidLoad() {
        super.viewDidLoad()
        print("inside ced_vendorSingleOrder")
        vendorSingleOrder.delegate = self
        vendorSingleOrder.dataSource = self
        self.topLabel.makeCard(self.view, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.5)
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String        
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        topLabel.backgroundColor = color
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.title = "View Order"
        
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        ced_navigationBarController().addNavButton(self,str:"back")
        self.loadvendororder()
        
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(section == 0){
            if(productsData.count != 0){
                return 5
            }else {
                return 0
            }
            
        }
        if(productsData.count != 0){
                return productsData.count + 1
        }else {
            return 0
        }
     
       
    }
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if(indexPath.section == 0){
        if(indexPath.row == 0){
            let cell = vendorSingleOrder.dequeueReusableCell(withIdentifier: "orderInfo") as! ced_vendorOrderinfoCell
            cell.orderdateval.text = OrderInfo["orderdate"]
            cell.orderStatusval.text =  OrderInfo["order_status"]
            cell.purchasefromval.text = OrderInfo["purchasedfrom"]
            return cell
        }else if(indexPath.row == 1){
           
            let cell = vendorSingleOrder.dequeueReusableCell(withIdentifier: "billingAddress") as! billingAddress
            cell.textView.text = shippingAddress["ship_to"]! + "\n" + shippingAddress["shipStreet"]! + shippingAddress["ship_city"]! + "\n"
            cell.topLabel.text = "Shipping Information"
            cell.textView.text! += shippingAddress["shipstate"]! + "\n" + shippingAddress["pincode"]! + "\n" + shippingAddress["ship_mobile"]!
            
            return cell
        }
        else if(indexPath.row == 2){
            let cell = vendorSingleOrder.dequeueReusableCell(withIdentifier: "billingAddress") as! billingAddress
            cell.textView.text = billing2Address["bill_to"]! + "\n" + billing2Address["bill_street"]! + billing2Address["bill_city"]! + "\n"
            cell.topLabel.text = "Billing Information"
            cell.textView.text! += billing2Address["bill_state"]! + "\n" + billing2Address["bill_pincode"]! + "\n" + billing2Address["bill_mobile"]!
            
            return cell
        }
        else if(indexPath.row == 3){
            let cell = vendorSingleOrder.dequeueReusableCell(withIdentifier: "paymentMethod") as! ced_paymethods
            cell.topLabel.text = "SHIPPING METHOD"
            cell.valueLabel.text = shippingAddress["shipping_method"]
            return cell
        }
        else if(indexPath.row == 4){
            let cell = vendorSingleOrder.dequeueReusableCell(withIdentifier: "paymentMethod") as! ced_paymethods
            cell.topLabel.text = "PAYMENT METHOD"
            cell.valueLabel.text = OrderInfo["method_title"]
            return cell
        }
        else{
            let cell = vendorSingleOrder.dequeueReusableCell(withIdentifier: "orderInfo") as! ced_vendorOrderinfoCell
            return cell
        }
        }else{
    
            if(indexPath.row < productsData.count){
            let cell = vendorSingleOrder.dequeueReusableCell(withIdentifier: "productInfo") as! ced_productInformation
            
            cell.productLabel.text = "Product"
            cell.productVal.text   = productsData[indexPath.row]["productName"]
            cell.productSku.text = "Product SKU"
            cell.productskuval.text = productsData[indexPath.row]["product_sku"]
            cell.productPrice.text  = "Product Price"
            cell.priceval.text   = productsData[indexPath.row]["product_price"]
            cell.productqty.text = "Quantity"
            cell.productqtyval.text =  productsData[indexPath.row]["product_qty"]
            
            cell.taxAmount.text = "Tax Amount"
             cell.taxamountval.text = productsData[indexPath.row]["tax_amount"]
            cell.discount.text = "Discount"
            cell.discountVal.text = productsData[indexPath.row]["discount_ammount"]
            cell.productsbtotal.text = "Subtotal"
            cell.productTotalval.text = productsData[indexPath.row]["rowsubtotal"]
            cell.RawTotal.text = "Row Total"
            cell.rawTotalVal.text = productsData[indexPath.row]["row_total"]
            return cell
        }
            else{
                
                let cell = vendorSingleOrder.dequeueReusableCell(withIdentifier: "productInfo") as! ced_productInformation
                
                cell.productLabel.text = "Subtotal"
                cell.productVal.text   = OrderInfo["subtotal"]
                cell.productSku.text = "Discount"
                cell.productskuval.text = OrderInfo["discount_ammount"]
                cell.productPrice.text  = "Grand Total"
                cell.priceval.text   = OrderInfo["grandtotal"]
                cell.productqty.text = "Grand Total(Earned)"
                cell.productqtyval.text = OrderInfo["grandtotal_earned"]
                
                cell.taxAmount.text = "Tax Amount"
                cell.taxamountval.text = OrderInfo["tax_amount"]
                cell.discount.text = "Commission Fee"
                cell.discountVal.text = OrderInfo["commision_fee"]
                cell.productsbtotal.text = "Net Earned"
                cell.productTotalval.text = OrderInfo["net_earned"]
                cell.RawTotal.isHidden = true
                cell.rawTotalVal.isHidden = true
                
                return cell
            }
        }
        
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.section == 0){
            return 200
        }else{
           return 320
        }
    }
    
    @objc func loadvendororder(){
        
        //var baseUrl = Ced_CommonVendor.getInfoPlist("baseUrl") as? String
        let baseUrl = "vorderapi/vorders/viewinitOrder"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey + "&order_id=" + orderId
        
        sendRequest(url: baseUrl, parameters: postString)
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            self.parseOrderData(json)
            self.vendorSingleOrder.reloadData()
        }
        
    }
    func parseOrderData(_ json:JSON){
        for result in json["data"]["ordetail"].arrayValue{
            let shipping = result["shipping"].stringValue
            let shipping_method = result["shipping_method"].stringValue
            let purchasedfrom = result["purchasedfrom"].stringValue
            let shipStreet = result["street"].stringValue
            let pincode    = result["pincode"].stringValue
            let shipcountry = result["country"].stringValue
            let shipstate   = result["state"].stringValue
            let ship_to   = result["ship_to"].stringValue
            let ship_city  = result["city"].stringValue
            let ship_mobile = result["mobile"].stringValue
            let credit_card_number = result["credit_card_number"].stringValue
            let credit_card_type = result["credit_card_type"].stringValue
            let name_on_card     = result["name_on_card"].stringValue
            
            let subtotal  = result["subtotal"].stringValue
            let net_earned = result["net_earned"].stringValue
            let grandtotal  = result["grandtotal"].stringValue
            let orderdate = result["orderdate"].stringValue
            let ordertitle = result["ordertitle"].stringValue
            let order_status = result["order_status"].stringValue
            let tax_amount = result["tax_amount"].stringValue
            let method_code  = result["method_code"].stringValue
            let method_title = result["method_title"].stringValue
            let commision_fee = result["commision_fee"].stringValue
            let grandtotal_earned = result["grandtotal_earned"].stringValue
            let discount      = result["discount"].stringValue
            let bill_city    = result["bill_city"].stringValue
            let bill_pincode = result["bill_pincode"].stringValue
            let bill_mobile  = result["bill_mobile"].stringValue
            let bill_country = result["bill_country"].stringValue
            let bill_to = result["bill_to"].stringValue
            let bill_state = result["bill_state"].stringValue
            let bill_street = result["bill_street"].stringValue
            shippingAddress = ["shipping":shipping,"shipping_method":shipping_method,"shipStreet":shipStreet,"pincode":pincode,"shipcountry":shipcountry,"shipstate":shipstate,"ship_to":ship_to,"ship_city":ship_city,"ship_mobile":ship_mobile]
            billing2Address = ["bill_city":bill_city,"bill_pincode":bill_pincode,"bill_mobile":bill_mobile,"bill_country":bill_country,"bill_to":bill_to,"bill_state":bill_state,"bill_street":bill_street]
            
            OrderInfo = ["purchasedfrom":purchasedfrom,"credit_card_number":credit_card_number,"name_on_card":name_on_card,"credit_card_type":credit_card_type,"subtotal":subtotal,"net_earned":net_earned,"grandtotal":grandtotal,"orderdate":orderdate,"ordertitle":ordertitle,"tax_amount":tax_amount,"order_status":order_status,"method_code":method_code,"method_title":method_title,"commision_fee":commision_fee,"grandtotal_earned":grandtotal_earned,"discount":discount]
            
            self.topLabel.text = "Order #\(orderId)"
            for productData in result["ordered_items"].arrayValue{
                let product_id = productData["product_id"].stringValue
                let product_qty = productData["product_qty"].stringValue
                let product_sku = productData["product_sku"].stringValue
                let original_price = productData["original_price"].stringValue
                let tax_ammount  = productData["tax_ammount"].stringValue
                let product_price = productData["product_price"].stringValue
                let product_image = productData["product_image"].stringValue
                let rowsubtotal = productData["rowsubtotal"].stringValue
                let row_total = productData["row_total"].stringValue
                let discount_ammount = productData["discount_amount"].stringValue
                let productName = productData["product_name"].stringValue
                
                let product = ["product_id":product_id,"product_qty":product_qty,"product_sku":product_sku,"original_price":original_price,"tax_ammount":tax_ammount,"product_price":product_price,"product_image":product_image,"rowsubtotal":rowsubtotal,"row_total":row_total,"discount_ammount":discount_ammount,"productName":productName]
                self.productsData.append(product)
            }
            
        }
    }
    

}
