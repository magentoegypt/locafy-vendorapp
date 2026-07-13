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

class ced_creationOfinvoice: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UITextFieldDelegate,UITextViewDelegate {
    
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
    var Floatbutton = UIButton()
    
    var vc = UIViewController()
    
    var qtyToinvoiced:String? //qty to invoiced textfield
    var counter = 0 //counter for added carriers
    var enabled = false
    var items = [[String:String]]()
    var CommentsText = String()
    var carriersData = [[String:String]]()
    
    var appendComments = false //variable for comments append
    var emailCopy      = false
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        topLabel.font = .systemFont(ofSize: 15, weight: .semibold)
        tableView.register(newOrderSubtotalCell.self, forCellReuseIdentifier: newOrderSubtotalCell.reuseId)
        tableView.delegate = self
        tableView.dataSource = self
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        ced_navigationBarController().addNavButton(self,str:"back")
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
            return 3
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
//                cell.topLabel.backgroundColor = DynamicColor.themeColor
                cell.orderDate.text = invoiceData["orderStoreDate"]
                cell.orderStatus.text = invoiceData["statusLabel"]
                cell.purchasedFrom.text = invoiceData["storeName"]
                return cell
            }else if(indexPath.row == 1){
                let cell = tableView.dequeueReusableCell(withIdentifier: "invoiceAccountInfo") as! ced_invoiceAccountInfo
//                cell.backgroundColor = DynamicColor.themeColor
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
                cell.texView.text! += invoiceData["bill_region"]! + "\n" + invoiceData["bill_postcode"]! + "\n" + invoiceData["bill_telephone"]!
                return cell
            }else if(indexPath.row == 3){
                let cell = tableView.dequeueReusableCell(withIdentifier: "invoiceshippingbill") as! ced_billingShipping
                cell.label.text = "Shipping Address"
                cell.texView.text = invoiceData["name"]! + "\n" + invoiceData["street"]! + invoiceData["city"]! + "\n"
                
                cell.texView.text! += invoiceData["region"]! + "\n" + invoiceData["postcode"]! + "\n" + invoiceData["telephone"]!
                return cell
            }else if(indexPath.row == 4){
                let cell = tableView.dequeueReusableCell(withIdentifier: "payinfo") as! ced_payinfo
                cell.TopLabel.text = "Payment Information"
                cell.cashOndel.text = invoiceData["payment_method_title"]
                cell.value.text    = invoiceData["message_at_last"]
                return cell
            }else if(indexPath.row == 5){
                let cell = tableView.dequeueReusableCell(withIdentifier: "ced_shipment") as! ced_shippingCreationCell
//                cell.topLabel.backgroundColor = DynamicColor.themeColor
                cell.shippingInfo.text = invoiceData["shipping_method"]
                cell.createShipment.contentHorizontalAlignment = .leading
                cell.createShipment.titleEdgeInsets = .init(top: 0, left: 8, bottom: 0, right: 0)
                cell.value.text    = "Total Shipping Charges:" + invoiceData["shipping_price"]!
                if(invoiceData["create_shipment"] == "false"){
                    cell.createShipment.isHidden = true
                    
                }else{
                    cell.createShipment.isHidden = false
                    cell.createShipment.addTarget(self, action: #selector(ced_creationOfinvoice.createShipment(_:)), for: UIControl.Event.touchUpInside)
                }
                let bounds = UIScreen.main.bounds
                let height:CGFloat = 235
                var counter:CGFloat = 0
                for data in carriersData {
                    let view = ced_shippingCarier()
                    view.frame = CGRect(x: cell.frame.origin.x + 5,y: height+counter*170,width: cell.frame.width - 10,height: 170)
                    view.carrierButton.setTitle(data["carrier_code"], for: UIControl.State())
                    view.title.text = data["carrier_code"]
                    view.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                    view.number.text = data["number"]
                    view.submitButton.setTitle("DELETE", for: UIControl.State())
                    view.submitButton.tag = Int(counter)
                    view.submitButton.addTarget(self, action: #selector(ced_creationOfinvoice.deleteCarrier(_:)), for: UIControl.Event.touchUpInside)
                    
                    cell.contentView.addSubview(view)
                    counter += 1
                }
                
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
                    view.submitButton.addTarget(self, action: #selector(ced_creationOfinvoice.deleteCarrier(_:)), for: UIControl.Event.touchUpInside)
                    
                    cell!.contentView.addSubview(view)
                    counter += 1
                }
                
                return cell!
            }
        }else if(indexPath.section == 1){
            let cell = tableView.dequeueReusableCell(withIdentifier: "productinvoice") as? ced_invoiceproduct
            cell?.qtyToInvoice.delegate = self
            cell?.qtyToInvoice.tag = indexPath.row + 200
            if productItems[indexPath.row]["qty_invoiced"] != "" {
                let leftqty =  Int(productItems[indexPath.row]["qty_ordered"]!)! - Int(productItems[indexPath.row]["qty_invoiced"]!)!
                cell?.qtyToInvoice.text = String(leftqty)
            }
            cell?.rowTotal.text =  productItems[indexPath.row]["row_total"]
            cell?.productName.text = productItems[indexPath.row]["name"]
            cell?.price.text = productItems[indexPath.row]["price"]
            cell?.qty.text   = productItems[indexPath.row]["qty_ordered"]
            cell?.sku.text   = productItems[indexPath.row]["sku"]
            cell?.subtotal.text = productItems[indexPath.row]["subtotal"]
            cell?.taxamount.text = productItems[indexPath.row]["tax_amount"]
            cell?.discountAmount.text = productItems[indexPath.row]["discount_amount"]
            
            return cell!
        }else{
            if(indexPath.row == 0){
                let cell = tableView.dequeueReusableCell(withIdentifier: "ced_comments") as? ced_comments
                cell?.commenttextView.delegate = self
                
                return cell!
            }else if indexPath.row == 1{
                let cell = tableView.dequeueReusableCell(withIdentifier: newOrderSubtotalCell.reuseId, for: indexPath) as! newOrderSubtotalCell
                cell.populate(totalInvoiceCreateData: invoiceData)
                return cell
            }else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "invoice") as! ced_invoice
                
                cell.saveButton.addTarget(self, action: #selector(ced_creationOfinvoice.CreateInvoice(_:)), for: UIControl.Event.touchUpInside)
                cell.saveButton.backgroundColor = DynamicColor.themeColor
                return cell
            }
            
        }
    }
    
    
    //***** create shipmentTag  *******///
    
    @objc @IBAction fileprivate func createShipment(_ sender:DLRadioButton){
        let cell = tableView.cellForRow(at: IndexPath(row: 5, section: 0)) as? ced_shippingCreationCell
        if(enabled){
            enabled = false
            cell?.AddTrackingNumber.isHidden = true
        }else{
            enabled = true
            cell?.AddTrackingNumber.addTarget(self, action: #selector(ced_creationOfinvoice.ShowTracking(_:)), for: UIControl.Event.touchUpInside)
            cell?.AddTrackingNumber.isHidden = false
        }
        
    }
    
    //Mark show tracking number
    
    @objc func ShowTracking(_ sender:UIButton){
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        let view = ced_shippingCarier()
        view.tag = 399
        view.frame.size = CGSize(width: 300,height: 170)
        view.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        view.carrierButton.addTarget(self, action: #selector(ced_creationOfinvoice.showdropdown(_:)), for: UIControl.Event.touchUpInside)
        view.submitButton.setTitle("Add", for: UIControl.State())
        view.submitButton.tag = Int(counter)
        view.submitButton.addTarget(self, action: #selector(ced_creationOfinvoice.AddCarrier(_:)), for: UIControl.Event.touchUpInside)
        view.backgroundColor = UIColor.black;
        bgCView.addSubview(view)
        view.center = self.view.center
        self.view.addSubview(bgCView)
    }
    
    //Mark Add Carrier
    @objc func AddCarrier(_ sender:UIButton){
        var data = [String:String]()
        let control = self.view.viewWithTag(399) as! ced_shippingCarier
        data["title"] = control.title.text
        data["number"] = control.number.text
        data["carrier_code"]  = control.carrierButton.titleLabel?.text
        carriersData.append(data)
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.tableView.reloadRows(at: [IndexPath(row: 5, section: 0)], with: UITableView.RowAnimation.automatic)
    }
    
    //Mark : Delete Carrier
    
    @objc func deleteCarrier(_ sender:UIButton){
        let tag = sender.tag
        self.carriersData.remove(at: tag)
        self.tableView.reloadRows(at: [IndexPath(row: 5, section: 0)], with: UITableView.RowAnimation.automatic)
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
    
    /** Mark data loading*/
    @objc func loadData(){
        var baseUrl = settings.baseUrl
        baseUrl += "vorderapi/vorders/create"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString    += "&order_id="+invoiceId+"&type=invoice"
        var request =  URLRequest(url: URL(string:baseUrl)!)
        request.httpMethod = "POST"
        request.httpBody = postString.data(using: String.Encoding.utf8)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        print(baseUrl)
        print(postString)
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request, completionHandler: {
            data,response,error in

            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self)
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                }
                print("error=\(error)")
                return;
            }
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                DispatchQueue.main.async
                    {
                        self.tableView.isHidden = true
                        if(httpStatus.statusCode == -1009){
                            ced_showError.showNoNetWork(self)
                        }else{
                            ced_showError.showNoModule(self)
                        }
                }
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                print("response = \(String(describing: response))")
                return;
            }
            DispatchQueue.main.async
                {
                    if let data = data{
                       guard let json = try? JSON(data: data) else {return}
                        print(json)
                        if(json["data"]["success"].stringValue == "true"){
                            self.parseData(json)
                            self.topLabel.text = self.invoiceData["header_meassage"]
                            Alert_File.removeLoadingIndicator(self)
                            self.tableView.reloadData()
                            
                        }else{
                            self.view.showToastMsg("Problem in loading Data")
                        }
                    }
                    
            }
        })
        task.resume()
        
        
    }
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.section == 1){
            return 330
        }
        if(indexPath.section == 2){
            switch indexPath.row{
            case 0:
                return 150
            case 1:
                return UITableView.automaticDimension
            default:
                return 150
            }
            
        }
        if(indexPath.section == 0){
            if(indexPath.row == 0){
                return 190
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
            if(indexPath.row == 2 || indexPath.row == 3 ){
              return 0//180
          }
        }
        
        return 300
    }
    
    @objc func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
    
    @objc func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if(section == 0){
            return nil
        }
        else if(section == 1){
            return "Items Invoiced"
        }else {
            return "Invoice History"
        }
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
            invoicedata["order_shipping"] = data["order_shipping"].stringValue
            invoicedata["header_meassage"] = data["header_meassage"].stringValue
            invoicedata["bill_name"] = data["bill_name"].stringValue
            invoicedata["last_trans_id"] = data["last_trans_id"].stringValue
            invoicedata["cc_owner"] = data["cc_owner"].stringValue
            invoicedata["name"] = data["name"].stringValue
            invoicedata["orderStoreDate"] = data["orderStoreDate"].stringValue
            invoicedata["bill_telephone"] = data["bill_telephone"].stringValue
//            invoicedata["shipping_price"] = data["shipping_price"].stringValue
            invoicedata["city"] = data["city"].stringValue
            invoicedata["order_total"] = data["order_total"].stringValue
            invoicedata["bill_company"] = data["bill_company"].stringValue
            invoicedata["orderAdminDate"] = data["orderAdminDate"].stringValue
            invoicedata["CustomerGroup"] = data["CustomerGroup"].stringValue
            invoicedata["postcode"] = data["postcode"].stringValue
            invoicedata["CustomerEmail"] = data["CustomerEmail"].stringValue
            invoicedata["message_at_last"] = data["message_at_last"].stringValue
            invoicedata["shipping_method"] = data["shipping_method"].stringValue
            invoicedata["shipping_price"]  = data["shipping_price"].string ?? "N/A"
            invoicedata["create_shipment"] = data["create_shipment"].stringValue
            
            invoicedata["order_total_incl"] = data["order_total_incl"].stringValue
            invoicedata["order_subtotal_excl"] = data["order_subtotal_excl"].stringValue
            invoicedata["order_tax"] = data["order_tax"].stringValue
            invoicedata["order_subtotal_incl"] = data["order_subtotal_incl"].stringValue
            invoicedata["order_total_excl"] = data["order_total_excl"].stringValue
            invoicedata["order_shipping_without_currency"] = data["order_shipping_without_currency"].stringValue
            
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
                prdct["discount_amount"] = product["discount_amount"].stringValue
                prdct["qty_invoiced"] = product["qty_invoiced"].stringValue
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
                _ = carrier["value"]
                let label = carrier["label"].stringValue
                carriersList.append(label)
                
            }
        }
    }
    //Mark : textField delegates
    
    @objc func textFieldDidEndEditing(_ textField: UITextField) {
        let productId = productItems[textField.tag-200]["item_id"]
        if let totalProducts = productItems[textField.tag-200]["qty_ordered"]{
            if let qtyToinvoiced = textField.text{
                if let intVal = Int(qtyToinvoiced) {
                    if let totalProducNo = Int(totalProducts){
                        if intVal <= totalProducNo {
                        if(items.count != 0){

                            for i in 0..<items.count{

                                if items[i]["item_id"] == productId{
                                    items[i]["quantity"] = "\(qtyToinvoiced)"
                                }else{
                                    let string = ["item_id":"\(productId!)","quantity":"\(qtyToinvoiced)"]
                                    items.append(string)
                                }
                            }
                        }else{
                            let string = ["item_id":"\(productId!)","quantity":"\(qtyToinvoiced)"]
                            items.append(string)
                        }
                        }else{
                            self.view.showToastMsg("Entered Qty must be less than ordered qty.")
                        }
                    }
                }
            }
        }
        
    }
    
    //Mark : TextView Delegates
    
    @objc func textViewDidEndEditing(_ textView: UITextView) {
        CommentsText = textView.text
    }
    
    
    
    // Mark: Function to create invoice
    
    @objc func CreateInvoice(_ sender:UIButton){
        var itemsString = String()
        items = [[String:String]]()
        var carrierString = String()
        for i in 0..<productItems.count
        {
            if let textField = self.view.viewWithTag(i+200) as? UITextField
            {
                let productId = productItems[textField.tag-200]["item_id"]
                if let totalProducts = productItems[textField.tag-200]["qty_ordered"]{
                    if let qtyToinvoiced = textField.text{
                        if let intVal = Int(qtyToinvoiced) {
                            if(intVal != 0)
                            {
                                if let totalProducNo = Int(totalProducts){
                                    if intVal <= totalProducNo {
                                        if(items.count != 0){
                                            
                                            for i in 0..<items.count{
                                                
                                                if items[i]["item_id"] == productId{
                                                    items[i]["quantity"] = "\(qtyToinvoiced)"
                                                }else{
                                                    let string = ["item_id":"\(productId!)","quantity":"\(qtyToinvoiced)"]
                                                    items.append(string)
                                                }
                                            }
                                        }else{
                                            let string = ["item_id":"\(productId!)","quantity":"\(qtyToinvoiced)"]
                                            items.append(string)
                                        }
                                    }else{
                                        self.view.showToastMsg("Entered Qty must be less than ordered qty.")
                                    }
                                }
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
            let jSondata2 = try JSONSerialization.data(
                withJSONObject: carriersData ,
                options: JSONSerialization.WritingOptions(rawValue: 0))
            carrierString = NSString(data: jSondata2,
                encoding: String.Encoding.utf8.rawValue)! as String
        }
        catch{
            print("error in data encoding")
        }
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        var baseUrl = settings.baseUrl
        baseUrl += "vorderapi/vinvoice/save"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString += "&do_shipment="+"\(enabled)"
        postString += "&items="+itemsString
        postString += "&tracking="+carrierString
        postString += "&comment_customer_notify="+"\(appendComments)"+"&send_email="+"\(emailCopy)"
        postString    += "&order_id="+invoiceId
        print(baseUrl)
        print(postString)
        var request =  URLRequest(url: URL(string:baseUrl)!)
        request.httpMethod = "POST"
        request.httpBody = postString.data(using: String.Encoding.utf8)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request, completionHandler: {
            data,response,error in

            guard error == nil && data != nil else
            {
                print("error=\(String(describing: error))")
                Alert_File.removeLoadingIndicator(self)
                return;
            }
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                print("response = \(String(describing: response))")
                Alert_File.removeLoadingIndicator(self)
                return;
            }
            DispatchQueue.main.async
                {
                    Alert_File.removeLoadingIndicator(self)
                    if let data = data{
                        guard let json = try? JSON(data: data) else {return}
                        print(json)
                        if(json["data"]["success"].stringValue == "true"){
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
                            // self.topLabel.text = self.creditmemoData["header_meassage"]
                            
                        }else{
                            self.view.showToastMsg(json["data"]["message"].stringValue )
                        }
                    }
                    
            }
        })
        task.resume()
        
        
        
    }
    
    
    
    
}
