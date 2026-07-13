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

class ced_creationOfcreditMemos: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UITextFieldDelegate,UITextViewDelegate {
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var tableView: UITableView!
    
    var invoiceId = String()
    var invoiceData = [String:String]()
    var productItems  = [[String:String]]()
    var comments    = [[String:String]]()
    var accountData = [[String:String]]()
    var carriersList = [String]()
    var shipmenttracks = [[String:String]]()
    var qtyToinvoiced:String? //qty to invoiced textfield
    var counter = 0 //counter for added carriers
    var enabled = false
    var items = [[String:String]]()
    var CommentsText = String()
    var carriersData = [[String:String]]()
    var select = false
    var appendComments = false //variable for comments append
    var emailCopy      = false
    //Specific on Credit MEMOs
    var vc = UIViewController();
    var adjustment_positive:String?
    var adjustment_negative:String?
    var shipping_amount:String?
    var returnTOstockarr = [[String:String]]()
    var inVoiceID  = String()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        tableView.estimatedRowHeight = 250
        tableView.delegate = self
        tableView.dataSource = self
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
     let color = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        containerView.backgroundColor = Ced_CommonVendor.UIColorFromRGB(color)
        topLabel.textColor = UIColor.white
    ced_navigationBarController().addNavButton(self,str:"back")
          Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.loadData()
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 3
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(section == 1){
            return productItems.count
        }
        if(section == 2){
            return 2
        }
        if(invoiceData.count == 0){
            return 0
        }
        
        return 6
    }
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if(indexPath.section == 0){
            if(indexPath.row == 0){
                let cell = tableView.dequeueReusableCell(withIdentifier: "invoiceOrderInfo") as! ced_orderInfo
                cell.orderDate.text = invoiceData["orderStoreDate"]
                cell.orderStatus.text = invoiceData["statusLabel"]
                cell.purchasedFrom.text = invoiceData["storeName"]
                return cell
            }else if(indexPath.row == 1){
                let cell = tableView.dequeueReusableCell(withIdentifier: "invoiceAccountInfo") as! ced_invoiceAccountInfo
                cell.customerName.text = invoiceData["CustomerName"]
                cell.customergroup.text = invoiceData["CustomerGroup"]
                cell.email.text = invoiceData["CustomerEmail"]
                let height:CGFloat = 150
                var counter:CGFloat = 0
                let bounds = UIScreen.main.bounds
                for data in accountData {
                    let view = ced_repeatViewinvoice()
                    view.frame = CGRect(x: 5,y: height+counter*40,width: bounds.width-10,height: 40)
                    view.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                    view.label1.text = data["label"]
                    view.value.text = data["value"]
                    view.center.x = cell.containerView.center.x
                    cell.contentView.addSubview(view)
                    counter += 1
                }
                return cell
            }else if(indexPath.row == 2){
                let cell = tableView.dequeueReusableCell(withIdentifier: "invoiceshippingbill") as! ced_billingShipping
                cell.label.text = "Billing Address"
                cell.texView.text = invoiceData["bill_name"]! + "\n" + invoiceData["bill_street"]! + invoiceData["bill_city"]! + "\n"
                cell.texView.text! += invoiceData["bill_region"]! + "\n" + invoiceData["bill_postcode"]! + "\n"
                cell.texView.text! += invoiceData["bill_country_id"]! + "\n" + invoiceData["bill_telephone"]!
                return cell
            }else if(indexPath.row == 3){
                let cell = tableView.dequeueReusableCell(withIdentifier: "invoiceshippingbill") as! ced_billingShipping
                cell.label.text = "Shipping Address"
                cell.texView.text = invoiceData["name"]! + "\n" + invoiceData["street"]! + "\n" + invoiceData["city"]! + "\n"
                cell.texView.text! += invoiceData["region"]! + "\n" + invoiceData["postcode"]! + "\n"
                cell.texView.text! += invoiceData["country_id"]! + "\n" + invoiceData["telephone"]!
                return cell
            }else if(indexPath.row == 4){
                let cell = tableView.dequeueReusableCell(withIdentifier: "payinfo") as! ced_payinfo
                cell.TopLabel.text = "Payment Information"
                cell.cashOndel.text = invoiceData["payment_method_title"]
                cell.value.text    = invoiceData["message_at_last"]
                return cell
            }else if(indexPath.row == 5){
                let cell = tableView.dequeueReusableCell(withIdentifier: "ced_shipment") as! ced_shippingCreationCell
                cell.topLabel.backgroundColor = DynamicColor.themeColor
                cell.shippingInfo.text = invoiceData["shipping_method"]
                cell.value.text    = "Total Shipping Charges:" + invoiceData["order_shipping"]!
                return cell
            }else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "selectCarrier")
                let bounds = UIScreen.main.bounds
                let height:CGFloat = 300
                var counter:CGFloat = 0
                for data in carriersData {
                    let view = ced_shippingCarier()
                    view.frame = CGRect(x: bounds.origin.x + 5,y: height+counter*170,width: bounds.width - 10,height: 170)
                    view.carrierButton.setTitle(data["carrier_code"], for: UIControl.State())
                    view.title.text = data["carrier_code"]
                    view.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                    view.number.text = data["number"]
                    view.submitButton.setTitle("DELETE", for: UIControl.State())
                    view.submitButton.tag = Int(counter)
                    view.submitButton.addTarget(self, action: "deleteCarrier:", for: UIControl.Event.touchUpInside)
                    cell!.contentView.addSubview(view)
                    counter += 1
                }
                
                return cell!
            }
        }else if(indexPath.section == 1){
            let cell = tableView.dequeueReusableCell(withIdentifier: "productinvoice") as? ced_invoiceproduct
            cell?.qtyToInvoice.delegate = self
            cell?.qtyToInvoice.tag = indexPath.row + 200
            cell?.productName.text = productItems[indexPath.row]["name"]
            cell?.price.text = productItems[indexPath.row]["price"]
            cell?.qty.text   = productItems[indexPath.row]["qty_ordered"]
            cell?.sku.text   = productItems[indexPath.row]["sku"]
            cell?.subtotal.text = productItems[indexPath.row]["subtotal"]
            cell?.taxamount.text = productItems[indexPath.row]["tax_amount"]
            cell?.discountAmount.text = productItems[indexPath.row]["discount_amount"]
            cell?.inVoicedQty.text = productItems[indexPath.row]["qty_invoiced"]
            cell?.shippedQty.text = productItems[indexPath.row]["qty_shipped"]
            cell?.returnTostock.contentHorizontalAlignment = .leading
            
            if(items.count > 0){

                for i in 0..<items.count{
                    if items[i]["item_id"] == productItems[indexPath.row]["item_id"]{
                        cell?.returnTostock.setSelected(true)
                    }else{
                         cell?.returnTostock.setSelected(false)
                    }
                }
            }else{
                cell?.returnTostock.setSelected(false)
            }
            
            
            cell?.returnTostock.addTarget(self, action: #selector(ced_creationOfcreditMemos.returnTostock(_:)), for: UIControl.Event.touchUpInside)
            cell?.returnTostock.tag = indexPath.row + 300
            return cell!
        }else{
            if(indexPath.row == 0){
                let cell = tableView.dequeueReusableCell(withIdentifier: "ced_comments") as? ced_comments
                cell?.commenttextView.delegate = self
                
                return cell!
            }else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "invoice") as! ced_invoice
//                cell.subTotal.text = invoiceData["order_subtotal"]
//                cell.grandtotal.text = invoiceData["order_total"]
                cell.emailInvoice.contentHorizontalAlignment = .leading
                cell.emailInvoice.titleEdgeInsets = .init(top: 0, left: 8, bottom: 0, right: 0)
                cell.refundShipping.delegate = self
                cell.appendComments.contentHorizontalAlignment = .leading
                cell.appendComments.titleEdgeInsets = .init(top: 0, left: 8, bottom: 0, right: 0)
                
                cell.refundShipping.tag = 1001
                if let txt = self.shipping_amount{
                    cell.refundShipping.text = txt
                }else{
                    cell.refundShipping.text = invoiceData["order_shipping_without_currency"]
                    self.shipping_amount = invoiceData["order_shipping_without_currency"]
                }
//                cell.adjustmentFee.tag = 1002
//                cell.adjustmentShipping.tag = 1003
//                cell.adjustmentShipping.delegate = self
//                cell.adjustmentFee.delegate = self
                cell.emailInvoice.addTarget(self, action: #selector(ced_creationOfcreditMemos.EnableInvoice(_:)), for: UIControl.Event.touchUpInside)
                cell.appendComments.addTarget(self, action: #selector(ced_creationOfcreditMemos.appendCommentsAct(_:)), for: UIControl.Event.touchUpInside)
                cell.saveButton.addTarget(self, action: #selector(ced_creationOfcreditMemos.CreateInvoice(_:)), for: UIControl.Event.touchUpInside)
                cell.saveButton.backgroundColor = DynamicColor.themeColor
                
                if invoiceData["order_subtotal_excl"] != ""{
                    cell.subtotalExcTaxStack.isHidden = false
                    cell.subtotalExcTax.text = invoiceData["order_subtotal_excl"]
                }else{
                    cell.subtotalExcTaxStack.isHidden = true
                }
                if invoiceData["order_subtotal_incl"] != ""{
                    cell.subtotalIncTaxStack.isHidden = false
                    cell.subtotalIncTax.text = invoiceData["order_subtotal_incl"]
                }else{
                    cell.subtotalIncTaxStack.isHidden = true
                }
                if invoiceData["order_subtotal"] != ""{
                    cell.subtotalStack.isHidden = false
                    cell.subTotal.text = invoiceData["order_subtotal"]
                }else{
                    cell.subtotalStack.isHidden = true
                }
                if invoiceData["order_tax"] != ""{
                    cell.taxStack.isHidden = false
                    cell.orderTax.text = invoiceData["order_tax"]
                }else{
                    cell.taxStack.isHidden = true
                }
                if invoiceData["order_total_excl"] != ""{
                    cell.grandtotalExcTaxStack.isHidden = false
                    cell.grandtotalExcTax.text = invoiceData["order_total_excl"]
                }else{
                    cell.grandtotalExcTaxStack.isHidden = true
                }
                if invoiceData["order_total_incl"] != ""{
                    cell.grandtotalIncTaxStack.isHidden = false
                    cell.grandTotalIncTax.text = invoiceData["order_total_incl"]
                }else{
                    cell.grandtotalIncTaxStack.isHidden = true
                }
                if invoiceData["order_total"] != ""{
                    cell.grandtotalStaxk.isHidden = false
                    cell.grandtotal.text = invoiceData["order_total"]
                }else{
                    cell.grandtotalStaxk.isHidden = true
                }
                return cell
            }
            
        }
    }
    
    
    //product Cell button action
    
    @objc func returnTostock(_ sender:UIButton){
        let product = productItems[sender.tag - 300]["item_id"]
        if(items.count > 0){
            for i in 0..<items.count{
                if items[i]["item_id"] == product{
                    if items[i]["back_to_stock"] == "true"{
                        items[i]["back_to_stock"] = "false"
                    }else{
                        items[i]["back_to_stock"] = "true"
                    }
                }
            }
        }else{
            let sen = sender as? DLRadioButton
            sen?.setSelected(false)
        }
    }
    
    
    //Mark : Show carrier dropdown in popup
    @objc func showdropdown(_ sender:UIButton){
        let dropDown = DropDown();
        let view = self.view.viewWithTag(399) as? ced_shippingCarier
        dropDown.dataSource = carriersList
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            view?.title.text = item
            
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
        
        
    }
    
    //Save DATA button @objc functions
    
    @objc func EnableInvoice(_ sender:UIButton){
        let cell = tableView.cellForRow(at: IndexPath(row: 1, section: 2)) as? ced_invoice
        if(!emailCopy){
            emailCopy = true
            cell?.appendComments.isEnabled = true
        }else{
            cell?.appendComments.isEnabled = false
            emailCopy = false
        }
    }
    
    
    @objc func appendCommentsAct(_ sender:UIButton){
        if(!appendComments){
            appendComments = true
        }else{
            appendComments = false
        }
    }

    /** Mark data loading*/
    @objc func loadData(){
        let baseUrl = "vorderapi/vorders/create"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString    += "&order_id="+invoiceId+"&type=creditmemo"
        sendRequest(url: baseUrl, parameters: postString);
    }
    
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        if let data = data{
            if(requestUrl == "vorderapi/vorders/create")
            {
                Alert_File.removeLoadingIndicator(self)
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    self.parseData(json)
                    self.topLabel.text = self.invoiceData["header_meassage"]
                    
                    self.tableView.reloadData()
                    
                }else{
                    let msg = json["data"]["message"].stringValue
                    self.view.showToastMsg(msg)
                }
            }
            else if(requestUrl == "vorderapi/vcreditmemo/save")
            {
                guard let json = try? JSON(data: data) else {return}
                print(NSString(data: data, encoding: String.Encoding.utf8.rawValue))
                let msg = json["data"]["message"].stringValue
                if(json["data"]["success"].stringValue == "true"){
                    //ShowSimpleAlert.showSimpleAlert(self, showTitle: "Success", showMsg: msg)
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                    
                    Ced_CommonVendor.delay(2.0, closure: {
                        self.navigationController?.popViewController(completion: {
                            if let vc = self.vc as? ced_manageProduct{
                                vc.orderId = self.invoiceId
                                vc.initializeData();
                            }
                        })
                        /*if let vc = self.vc as? ced_manageProduct{
                            vc.orderId = self.invoiceId
                            vc.initializeData();
                        }*/
                    })
                }else{
                    
                    ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: msg)
                    
                }
                
            }
        }
        

    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.section == 1){
            return 400
        }
        if(indexPath.section == 2){
            if(indexPath.row == 1){
                return UITableView.automaticDimension
            }
            return 150
        }
        if(indexPath.section == 0){
            if(indexPath.row == 0){
                return 185
            }
            if( indexPath.row == 5){
                return 235 + CGFloat((carriersData.count)*170)
            }
            if(indexPath.row == 4 ){
                return 150
            }
            if(indexPath.row == 1){
                return 0//155 + CGFloat(accountData.count*40)
            }
            if(indexPath.row == 2 || indexPath.row == 3){
                return 0//180
            }
        }
        return 300
    }
    
    @objc func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
    
 
    func parseData(_ json:JSON){
        for data in json["data"]["ordersData"].arrayValue{
            var invoicedata = [String:String]()
            invoicedata["region"] = data["region"].stringValue
            invoicedata["bill_postcode"] = data["bill_postcode"].stringValue
            invoicedata["bill_street"] = data["bill_street"].stringValue
            invoicedata["street"] = data["street"].stringValue
            invoicedata["company"] = data["company"].stringValue
            invoicedata["payment_method_title"] = data["payment_method_title"].stringValue
            invoicedata["telephone"]  = data["telephone"].stringValue
            invoicedata["bill_city"] = data["bill_city"].stringValue
            invoicedata["country_id"] = data["country_id"].stringValue
            invoicedata["payment_method_code"] = data["payment_method_code"].stringValue
            invoicedata["bill_region"] = data["bill_region"].stringValue
            invoicedata["cc_type"] = data["cc_type"].stringValue
            invoicedata["CustomerName"] = data["CustomerName"].stringValue
            invoicedata["bill_country_id"] = data["bill_country_id"].stringValue
            invoicedata["storeName"] = data["storeName"].stringValue
            invoicedata["statusLabel"] = data["statusLabel"].stringValue
            invoicedata["order_subtotal"] = data["order_subtotal"].stringValue
            invoicedata["header_meassage"] = data["header_meassage"].stringValue
            invoicedata["bill_name"] = data["bill_name"].stringValue
            invoicedata["last_trans_id"] = data["last_trans_id"].stringValue
            invoicedata["cc_owner"] = data["cc_owner"].stringValue
            invoicedata["name"] = data["name"].stringValue
            invoicedata["orderStoreDate"] = data["orderStoreDate"].stringValue
            invoicedata["bill_telephone"] = data["bill_telephone"].stringValue
            invoicedata["shipping_price"] = data["shipping_price"].stringValue
            invoicedata["city"] = data["city"].stringValue
            invoicedata["order_total"] = data["order_total"].stringValue
            invoicedata["bill_company"] = data["bill_company"].stringValue
            invoicedata["orderAdminDate"] = data["orderAdminDate"].stringValue
            invoicedata["CustomerGroup"] = data["CustomerGroup"].stringValue
            invoicedata["postcode"] = data["postcode"].stringValue
            invoicedata["CustomerEmail"] = data["CustomerEmail"].stringValue
            invoicedata["message_at_last"] = data["message_at_last"].stringValue
            invoicedata["shipping_method"] = data["shipping_method"].stringValue
            invoicedata["shipping_price"]  = data["shipping_price"].stringValue
            invoicedata["create_shipment"] = data["create_shipment"].stringValue
            invoicedata["order_shipping"] = data["order_shipping"].stringValue
            invoicedata["net_earn"] = data["net_earn"].stringValue
            invoicedata["order_shipping_without_currency"] = data["order_shipping_without_currency"].stringValue
            
            invoicedata["order_total_incl"] = data["order_total_incl"].stringValue
            invoicedata["order_subtotal_excl"] = data["order_subtotal_excl"].stringValue
            invoicedata["order_tax"] = data["order_tax"].stringValue
            invoicedata["order_subtotal_incl"] = data["order_subtotal_incl"].stringValue
            invoicedata["order_total_excl"] = data["order_total_excl"].stringValue
            
            self.invoiceData = invoicedata
            for product in  data["items"].arrayValue{
                var prdct = [String:String]()
                prdct["row_total"] = product["row_total"].stringValue
                prdct["sku"]      = product["sku"].stringValue
                prdct["price"]    = product["price"].stringValue
                prdct["tax_amount"] = product["tax_amount"].stringValue
                prdct["subtotal"]    = product["subtotal"].stringValue
                prdct["qty_ordered"] = product["qty_ordered"].stringValue
                prdct["name"]         = product["name"].stringValue
                prdct["item_id"]  = product["item_id"].stringValue
                prdct["qty_invoiced"] = product["qty_invoiced"].stringValue
                prdct["qty_shipped"]  = product["qty_shipped"].stringValue
                prdct["back_to_stock"] = product["back_to_stock"].stringValue
                prdct["discount_amount"] = product["discount_amount"].stringValue
                productItems.append(prdct)
            }
            for accountDa in  data["account_data"].arrayValue{
                var  acc = [String:String]()
                acc["label"] = accountDa["label"].stringValue
                acc["value"] = accountDa["value"].stringValue
                accountData.append(acc)
            }
            for coments  in data["comments"].arrayValue{
                var  comentData = [String:String]()
                comentData["comment"] = coments["comment"].stringValue
                comentData["date_time"] = coments["date_time"].stringValue
                comentData["notified"]  = coments["notified"].stringValue
               // comments.append(comentData)
            }
            for coments  in data["shipment_tracks"].arrayValue{
                var  trackData = [String:String]()
                trackData["number"] = coments["number"].stringValue
                trackData["carrier"] = coments["carrier"].stringValue
                trackData["tracking_url"]  = coments["tracking_url"].stringValue
                trackData["track_id"]  = coments["track_id"].stringValue
                trackData["delete"]  = coments["delete"].stringValue
                shipmenttracks.append(trackData)
            }
            for carrier in data["carriers"].arrayValue{
                let data = carrier["value"]
                let label = carrier["label"].stringValue
                carriersList.append(label)
                
            }
        }
    }
    //Mark : textField delegates
    
    @objc func textFieldDidEndEditing(_ textField: UITextField) {
        if(textField.tag == 1001){
            shipping_amount = textField.text
            return
        }else if(textField.tag == 1002){
            adjustment_positive = textField.text
            return
        }else if(textField.tag == 1003){
            adjustment_negative = textField.text
            return
        }
        let productId = productItems[textField.tag-200]["item_id"]
        qtyToinvoiced = textField.text
        if(items.count != 0){

        for i in 0..<items.count{

            if items[i]["item_id"] == productId{
                items[i]["quantity"] = "\(qtyToinvoiced!)"
            }else{
                let string = ["item_id":"\(productId!)","quantity":"\(qtyToinvoiced!)","back_to_stock":"false"]
                items.append(string)
            }
        }
        }else{
            let string = ["item_id":"\(productId!)","quantity":"\(qtyToinvoiced!)","back_to_stock":"false"]
            items.append(string)
        }
   
       
        
    }
    
    //Mark : TextView Delegates
    
    @objc func textViewDidEndEditing(_ textView: UITextView) {
        CommentsText = textView.text
    }
    
    
    
    // Mark: Function to create invoice
    
    @objc func CreateInvoice(_ sender:UIButton){
     Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        
        var itemsString = String()
        items = [[String:String]]()
        for i in 0..<productItems.count
        {
            if let textField = self.view.viewWithTag(i+200) as? UITextField
            {
                let productId = productItems[textField.tag-200]["item_id"]
                if let qtyToinvoiced = textField.text
                {
                    if let qty = Int(qtyToinvoiced)
                    {
                        if(qty != 0)
                        {
                            if(items.count != 0){
                                
                                for i in 0..<items.count{
                                    
                                    if items[i]["item_id"] == productId{
                                        items[i]["quantity"] = "\(qtyToinvoiced)"
                                    }else{
                                        let string = ["item_id":"\(productId!)","quantity":"\(qtyToinvoiced)","back_to_stock":"false"]
                                        items.append(string)
                                    }
                                }
                            }else{
                                let string = ["item_id":"\(productId!)","quantity":"\(qtyToinvoiced)","back_to_stock":"false"]
                                items.append(string)
                            }
                        }
                        
                    }
                }
                
            }
        }
        
    
        do{
            let JSONData = try JSONSerialization.data(
                withJSONObject: items ,
                options: JSONSerialization.WritingOptions(rawValue: 0))
            itemsString = NSString(data: JSONData,
                encoding: String.Encoding.utf8.rawValue)! as String
            
        }
        catch{
            print("error in data encoding")
        }
        let baseUrl = "vorderapi/vcreditmemo/save"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        if(adjustment_positive != nil){
        postString += "&adjustment_positive="+adjustment_positive!
        }
        if(adjustment_negative != nil){
        postString += "&adjustment_negative="+adjustment_negative!
        }
        if(shipping_amount != nil){
        postString += "&shipping_amount="+shipping_amount!
            
        }
        postString += "&items="+itemsString
        postString += "&comment_customer_notify="+"\(appendComments)"+"&send_email="+"\(emailCopy)"
        postString    += "&order_id="+invoiceId
        if(inVoiceID != ""){
            postString += "&invoice_id="+inVoiceID
        }
        if CommentsText !=  "" {
            postString += "&comment_text=" + CommentsText
        }
        sendRequest(url: baseUrl, parameters: postString);
    }
    
    
}
