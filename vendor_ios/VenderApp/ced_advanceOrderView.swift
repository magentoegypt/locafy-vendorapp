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

class ced_advanceOrderView: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource {

    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var advanceOrderView: UITableView!
    let refreshControl = UIRefreshControl()
    var accountData = [[String:String]]()
    var OrderViewData = [String:String]()
    var productItems = [[String:String]]()
    var additionalOptions = [[String:String]]()
    var comments = [[String:String]]()
    var Floatbutton = UIButton()
    var orderId = String()
    override func viewDidLoad() {
        super.viewDidLoad()
        advanceOrderView.register(newOrderSubtotalCell.self, forCellReuseIdentifier: newOrderSubtotalCell.reuseId)
        advanceOrderView.estimatedRowHeight = 190
        // Do any additional setup after loading the view.
        advanceOrderView.delegate = self
        advanceOrderView.dataSource = self
       // advanceOrderView.estimatedRowHeight = 300
       
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        ced_navigationBarController().addNavButton(self,str:"back")
        self.loadData()
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        advanceOrderView.refreshControl = refreshControl
       // self.addFloatingbutton()
    }
    
    @objc func refresh(_ sender: AnyObject) {
        self.comments.removeAll()
        self.productItems.removeAll()
        self.OrderViewData.removeAll()
        advanceOrderView.reloadData()
        self.loadData()
    }
    
    @objc func addFloatingbutton(){
        let bounds = self.view.bounds
        Floatbutton = UIButton(frame: CGRect(x: bounds.width-100, y: bounds.height-150, width: 50, height: 50))
        let imageview = UIImageView(frame: CGRect(x: bounds.width-100, y: bounds.height-150, width: 50, height: 50))
        imageview.image = UIImage(named: "add_comment")
        imageview.layer.cornerRadius = imageview.frame.width/2
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        
        Floatbutton.backgroundColor = UIColor.clear
        containerView.backgroundColor = color
        imageview.backgroundColor = color
        imageview.contentMode = .center
        // Floatbutton.setBackgroundImage(UIImage(named: "add_comment"), forState: UIControlState.Normal)
        
        Floatbutton.addTarget(self, action: #selector(ced_advanceOrderView.addComments(_:)), for: UIControl.Event.touchUpInside)
        
        self.view.insertSubview(imageview, aboveSubview: self.advanceOrderView)
        self.view.insertSubview(Floatbutton, aboveSubview: self.advanceOrderView)
        
    }
    
    @objc func scrollViewDidScroll(_ scrollView: UIScrollView) {
        let bounds = advanceOrderView.bounds
        let frame = Floatbutton.frame
        Floatbutton.frame.origin.y = bounds.origin.y + 400
        self.view.bringSubviewToFront(Floatbutton)
        self.Floatbutton.frame = frame;
    }
    
    @objc func addComments(_ sender:UIButton){
        let view = storyboard?.instantiateViewController(withIdentifier: "ced_addcomments") as? ced_invoiceAddcomments
        view?.prodView = "orderview"
        view?.invoiceId = orderId
        self.navigationController?.present(view!, animated: true, completion: nil)
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
            return comments.count + 1
        }
        if(OrderViewData.count == 0){
            return 0
        }
        if(OrderViewData["shipping_method"] != ""){
            return 6
        }
        return 5
        
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if(indexPath.section == 0){
            if(indexPath.row == 0){
                let cell = advanceOrderView.dequeueReusableCell(withIdentifier: "invoiceOrderInfo") as! ced_orderInfo
                cell.orderDate.text = OrderViewData["orderStoreDate"]
                cell.orderStatus.text = OrderViewData["statusLabel"]
                cell.purchasedFrom.text = OrderViewData["storeName"]
                cell.order.text = "Order Status".localized
                cell.orderDateTitle.text = "Order Date".localized
                cell.purchTitle.text = "Purchased From".localized
                cell.topLabel.text = " ORDER INFORMATION".localized
                return cell
            }else if(indexPath.row == 1){
                let cell = advanceOrderView.dequeueReusableCell(withIdentifier: "invoiceAccountInfo") as! ced_invoiceAccountInfo
                
                cell.topLabel.text = " ACCOUNT INFORMATION".localized
                cell.cName.text = "Customer Name".localized
                cell.cemail.text = "Email".localized
                cell.cgrp.text = "Customer Group".localized
                
                cell.customerName.text = OrderViewData["CustomerName"]
                cell.customergroup.text = OrderViewData["CustomerGroup"]
                cell.email.text = OrderViewData["CustomerEmail"]
                let height:CGFloat = 150
                var counter:CGFloat = 0
                let bounds = UIScreen.main.bounds
                for data in accountData {
                    let view = ced_repeatViewinvoice()
                    view.frame = CGRect(x: bounds.origin.x + 5,y: height+counter*40,width: bounds.width - 10,height: 40)
                    view.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                    view.label1.text = data["label"]
                    view.value.text = data["value"]
                    view.center.x = cell.containerView.center.x
                    cell.contentView.addSubview(view)
                    counter += 1
                }
                return cell
            }else if(indexPath.row == 2){
                let cell = advanceOrderView.dequeueReusableCell(withIdentifier: "invoiceshippingbill") as! ced_billingShipping
                cell.label.text = " Billing Address".localized.uppercased()
                cell.texView.text = OrderViewData["name"]! + "\n" + OrderViewData["street"]! + "\n" + OrderViewData["city"]! + "\n"
                cell.texView.text! += OrderViewData["region"]! + "\n" + OrderViewData["postcode"]! + "\n"
                cell.texView.text! += OrderViewData["country_id"]! + "\n" + OrderViewData["telephone"]!
                return cell
            }else if(indexPath.row == 3){
                let cell = advanceOrderView.dequeueReusableCell(withIdentifier: "invoiceshippingbill") as! ced_billingShipping
                cell.label.text = " Shipping Address".localized.uppercased()
                cell.texView.text = OrderViewData["name"]! + "\n" + OrderViewData["street"]! + "\n" + OrderViewData["city"]! + "\n"
                cell.texView.text! += OrderViewData["region"]! + "\n" + OrderViewData["postcode"]! + "\n"
                cell.texView.text! += OrderViewData["country_id"]! + "\n" + OrderViewData["telephone"]!
                return cell

            }else if(indexPath.row == 4){
                let cell = advanceOrderView.dequeueReusableCell(withIdentifier: "payinfo") as! ced_payinfo
                cell.TopLabel.text = " Payment Information".localized.uppercased()
                cell.cashOndel.text = OrderViewData["payment_method_title"]
                cell.value.text    = OrderViewData["message_at_last"]
                return cell
            }else{
                let cell = advanceOrderView.dequeueReusableCell(withIdentifier: "payinfo") as! ced_payinfo
                cell.TopLabel.text = " Shipping & Handling Information".localized.uppercased()
                cell.cashOndel.text = OrderViewData["shipping_method"]
                cell.value.text    = "Total Shipping Charges:".localized + OrderViewData["shipping_price"]!
                return cell
            }
        }else if(indexPath.section == 1){
            let cell = advanceOrderView.dequeueReusableCell(withIdentifier: "productinvoice") as! ced_invoiceproduct
            if(cell.dataCheck)
            {
                cell.dataCheck = false;
                
                cell.rowTotalLbl.text = "Row Total".localized
                cell.taxPercentLbl.text = "Tax Percentage".localized
                cell.discountLbl.text = "Discount Amount".localized
                cell.taxAmmLbl.text = "Tax Amount".localized
                cell.subtotalLbl.text = "Subtotal".localized
                cell.qtyLbl.text = "Qty".localized
                cell.skuLbl.text = "SKU".localized
                cell.priceLabel.text = "Price".localized
                cell.orignalPrice.text = "Original Price".localized
                cell.itemStatus.text = "Item Status".localized
                cell.prodNameLbl.text = "Product".localized
                
                cell.productName.text = productItems[indexPath.row]["name"]
                cell.price.text = productItems[indexPath.row]["price"]
                cell.qty.text   = productItems[indexPath.row]["qty_ordered"]
                cell.sku.text   = productItems[indexPath.row]["sku"]
                cell.subtotal.text = productItems[indexPath.row]["subtotal"]
                cell.taxamount.text = productItems[indexPath.row]["tax_amount"]
                cell.discountAmount.text = productItems[indexPath.row]["discount_amount"]
                cell.rowTotal.text = productItems[indexPath.row]["row_total"]
                cell.taxPercentLabel.text = productItems[indexPath.row]["tax_percent"]
            
                //print("---sgst-\(productItems[indexPath.row]["sgstAmt"] ?? "" + "(\(productItems[indexPath.row]["sgstRate"] ?? ""))")")
                cell.itemStatusLabel.text = productItems[indexPath.row]["item_status"];
                cell.originalPriceLabel.text = productItems[indexPath.row]["original_price"];
                if let subviews = cell.additionalInfoStackView.arrangedSubviews as? [UIView]
                {
                    for index in subviews
                    {
                        index.removeFromSuperview();
                    }
                }
                
                for index in additionalOptions
                {
                    for(key,value) in index
                    {
                        let labelView = KeyValueLabelView()
                        cell.additionalInfoStackView.addArrangedSubview(labelView);
                        labelView.keyLabel.text = key;
                        labelView.valueLabel.text = value
                        labelView.translatesAutoresizingMaskIntoConstraints = false
                        let heightConstrain = NSLayoutConstraint(item: labelView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 30)
                        labelView.addConstraint(heightConstrain)
                        cell.additionalInfoStackHeight.constant += 40;
                    }
                }
            }
            return cell
        }else{
            if(indexPath.row < comments.count ){
            let cell = advanceOrderView.dequeueReusableCell(withIdentifier: "ced_comments") as! ced_comments
            cell.commentsDate.text = comments[indexPath.row]["date_time"]
            cell.userNotified.text = comments[indexPath.row]["notified"]
            cell.comment.text   = comments[indexPath.row]["comment"]
                
                cell.t3.text = "Comment".localized
                cell.t2.text = "User Notified".localized
                cell.t1.text = "Comment Date".localized
            return cell
            }else{
                
                let cell = tableView.dequeueReusableCell(withIdentifier: newOrderSubtotalCell.reuseId) as! newOrderSubtotalCell
                cell.populate(with: OrderViewData)
//                cell.topLabel.text = "Order Totals".uppercased()
//                cell.subTotal.text = OrderViewData["order_total"]
//                cell.shippinghandling.text = OrderViewData["shipping_price"]
//                cell.grandTotalEarned.text   = OrderViewData["marketplace_fees"]
//                cell.commissionFee.text   = OrderViewData["order_commission"]
//                cell.netEarned.text   = OrderViewData["net_earn"]
//                cell.orderTax.text = OrderViewData["order_tax"]
                return cell
            }
            
        }
        
        
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.section == 1){
            if(additionalOptions.count > 0)
            {
                return CGFloat(355 + (additionalOptions[indexPath.row].count * 40)) - 50
            }
            return 355 - 50
        }
        if(indexPath.section == 2){
            if(indexPath.row == comments.count){
                return UITableView.automaticDimension
            }
             return 120
        }
        if(indexPath.section == 0){
            if(indexPath.row == 0){
                return 185
            }
            if(indexPath.row == 4){
                return 0//150
            }
            if(indexPath.row == 5){
                return 150
            }
            if(indexPath.row == 1){
                return 0//160 + CGFloat(accountData.count*40)
            }
            if(indexPath.row == 2 || indexPath.row == 3){
                return 0//230
            }
        }
        return 300
    }
    
 
    
    
    /** Mark data loading*/
    
    @objc func loadData(){
        var baseUrl = settings.baseUrl
        baseUrl += "vorderapi/vorders/view"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString    += "&order_id="+orderId
        
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

            DispatchQueue.main.async
                {
                    self.refreshControl.endRefreshing()
                    Alert_File.removeLoadingIndicator(self)
            }
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
                        self.advanceOrderView.isHidden = true
                        if(httpStatus.statusCode == -1009){
                            ced_showError.showNoNetWork(self)
                        }else{
                            ced_showError.showNoModule(self)
                        }
                }
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                print("response = \(response)")
                return;
            }
            DispatchQueue.main.async
                {
                    if let data = data{
                        guard let json = try? JSON(data: data) else {return}
                        print(json)
                        if(json["data"]["success"].stringValue == "true"){
                            self.parseData(json)
                            self.topLabel.text = self.OrderViewData["header_meassage"]
                            Alert_File.removeLoadingIndicator(self)
                            self.advanceOrderView.reloadData()
                        }else{
                           // ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: json["data"]["message"].stringValue)
                            self.advanceOrderView.setEmptyMessage(json["data"]["message"].stringValue)
                        }
                    }
                    
            }
        })
        task.resume()
        
        
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
            invoicedata["net_earn"]  = data["net_earn"].stringValue
            invoicedata["order_commission"] = data["order_commission"].stringValue
            invoicedata["order_tax"] = data["order_tax"].stringValue
            invoicedata["marketplace_fees"] = data["marketplace_fees"].stringValue
            invoicedata["service_tax"] = data["service_tax"].stringValue
            invoicedata["grand_total_earned"] = data["grand_total_earned"].stringValue
            self.OrderViewData = invoicedata
            
            for accountDa in  data["account_data"].arrayValue{
                var  acc = [String:String]()
                acc["label"] = accountDa["label"].stringValue
                acc["value"] = accountDa["value"].stringValue
                accountData.append(acc)
            }
            for product in  data["items"].arrayValue{
                print(product)
                var prdct = [String:String]()
                prdct["row_total"] = product["vendor_row_total"].stringValue
                prdct["sku"]      = product["sku"].stringValue
                prdct["price"]    = product["vendor_price"].stringValue
                prdct["tax_amount"] = product["tax_amount"].stringValue
                prdct["subtotal"]    = product["vendor_row_total"].stringValue
                prdct["qty_ordered"] = product["qty_ordered"].stringValue
                prdct["name"]         = product["name"].stringValue
                prdct["discount_amount"] = product["discount_amount"].stringValue
                var addOptions = [String:String]()
                for index in product["additonal_options"].arrayValue
                {
                    addOptions[index["label"].stringValue] = index["value"].stringValue;
                }
                
                prdct["igstAmt"] = product["igstAmt"].stringValue
                prdct["cgstAmt"] = product["cgstAmt"].stringValue
                prdct["sgstAmt"] = product["sgstAmt"].stringValue
                prdct["tax_percent"] = product["tax_percent"].stringValue
                prdct["original_price"] = product["original_price"].stringValue
                prdct["item_status"] = product["item_status"].stringValue
                prdct["igstRate"] = product["igstRate"].stringValue
                prdct["sgstRate"] = product["sgstRate"].stringValue
                prdct["cgstRate"] = product["cgstRate"].stringValue
                prdct["row_total"] = product["row_total"].stringValue
                
               // additionalOptions.append(addOptions);
                productItems.append(prdct)
            }
            for coments  in data["comments"].arrayValue{
                var  comentData = [String:String]()
                comentData["comment"] = coments["comment"].stringValue
                comentData["date_time"] = coments["date_time"].stringValue
                comentData["notified"]  = coments["notified"].stringValue
               // comments.append(comentData)
            }
          
        }
        
    }
    


}
