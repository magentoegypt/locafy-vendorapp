//
//  ced_shipmentView.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 01/07/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_shipmentView: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource {

    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var shipmentViewtable: UITableView!
    @IBOutlet weak var containerView: UIView!
    var shipmentId = String()
    var additionalOptions = [[String:String]]()
    var accountData = [[String:String]]()
    var creditmemoData = [String:String]()
    var productItems = [[String:String]]()
    var comments = [[String:String]]()
    var shipmenttracks = [[String:String]]()
    var Floatbutton = UIButton()
    var carriersList = [String]()
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        shipmentViewtable.delegate = self
        shipmentViewtable.dataSource = self
        shipmentViewtable.estimatedRowHeight = 300
        ced_navigationBarController().addNavButton(self,str:"back".localized)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        self.loadData()
      //  self.addFloatingbutton()
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
        Floatbutton.addTarget(self, action: #selector(ced_shipmentView.addComments(_:)), for: UIControl.Event.touchUpInside)
        self.view.insertSubview(imageview, aboveSubview: self.shipmentViewtable)
        self.view.insertSubview(Floatbutton, aboveSubview: self.shipmentViewtable)
        
    }
    
    @objc func scrollViewDidScroll(_ scrollView: UIScrollView) {
        let bounds = shipmentViewtable.bounds
        let frame = Floatbutton.frame
        Floatbutton.frame.origin.y = bounds.origin.y + 400
        self.view.bringSubviewToFront(Floatbutton)
        self.Floatbutton.frame = frame;
    }
    
    @objc func addComments(_ sender:UIButton){
        let view = storyboard?.instantiateViewController(withIdentifier: "ced_addcomments") as? ced_invoiceAddcomments
        view?.prodView = "shipment"
        view?.invoiceId = shipmentId
    self.navigationController?.present(view!, animated: true, completion: nil)
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        if(creditmemoData["shipping_method"] != ""){
            return 3
        }
        return 2
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(section == 1){
            
            return productItems.count
        }
        if(section == 2){
            return comments.count
        }
        if(creditmemoData.count == 0){
            return 0
        }
        if(creditmemoData["shipping_method"] != ""){
            return 6
        }
        return 5
        
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if(indexPath.section == 0){
            if(indexPath.row == 0){
                let cell = shipmentViewtable.dequeueReusableCell(withIdentifier: "invoiceOrderInfo") as! ced_orderInfo
                cell.topLabel.text = "Order Information ".localized
                cell.orderDate.text = creditmemoData["orderStoreDate"]
                cell.orderStatus.text = creditmemoData["statusLabel"]
                cell.purchasedFrom.text = creditmemoData["storeName"]
                cell.orderDateTitle.text=" Order Date".localized
                cell.order.text=" Order Status".localized
                cell.purchTitle.text=" Purchased From".localized
                return cell
            }else if(indexPath.row == 1){
                let cell = shipmentViewtable.dequeueReusableCell(withIdentifier: "invoiceAccountInfo") as! ced_invoiceAccountInfo
                cell.topLabel.text="  Account Information".localized
                cell.cemail.text=" Email".localized
                cell.cgrp.text=" Customer Group".localized
                cell.cName.text=" Customer Name".localized
                cell.customerName.text = creditmemoData["CustomerName"]
                cell.customergroup.text = creditmemoData["CustomerGroup"]
                cell.email.text = creditmemoData["CustomerEmail"]
                let height:CGFloat = 150
                var counter:CGFloat = 0
                let bounds = UIScreen.main.bounds
                for data in accountData {
                    let view = ced_repeatViewinvoice()
                     view.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin]
                    view.frame = CGRect(x: bounds.origin.x + 5,y: height+counter*40,width: bounds.width - 10,height: 40)
                    view.label1.text = data["label"]
                    view.value.text = data["value"]
                    view.center.x = cell.containerView.center.x
                    cell.contentView.addSubview(view)
                    counter += 1
                }
                return cell
            }else if(indexPath.row == 2){
                let cell = tableView.dequeueReusableCell(withIdentifier: "invoiceshippingbill") as! ced_billingShipping
                cell.label.text = "Billing Address".localized
                cell.texView.text = creditmemoData["bill_name"]! + "\n" + creditmemoData["bill_street"]! + "\n" + creditmemoData["bill_city"]! + "\n"
                cell.texView.text! += creditmemoData["bill_region"]! + "\n" + creditmemoData["bill_postcode"]! + "\n"
                cell.texView.text! += creditmemoData["bill_country_id"]! + "\n" + creditmemoData["bill_telephone"]!
                return cell
            }else if(indexPath.row == 3){
                let cell = tableView.dequeueReusableCell(withIdentifier: "invoiceshippingbill") as! ced_billingShipping
                cell.label.text = "Shipping Address".localized
                cell.texView.text = creditmemoData["name"]! + "\n" + creditmemoData["street"]! + "\n" + creditmemoData["city"]! + "\n"
                cell.texView.text! += creditmemoData["region"]! + "\n" + creditmemoData["postcode"]! + "\n"
                cell.texView.text! += creditmemoData["country_id"]! + "\n" + creditmemoData["telephone"]!
                return cell
            }else if(indexPath.row == 4){
                let cell = shipmentViewtable.dequeueReusableCell(withIdentifier: "payinfo") as! ced_payinfo
                cell.TopLabel.text = "Payment Information".localized
                cell.cashOndel.text = creditmemoData["payment_method_title"]
                cell.value.text    = creditmemoData["message_at_last"]
                return cell
            }else{
                let cell = shipmentViewtable.dequeueReusableCell(withIdentifier: "payinfo") as! ced_payinfo
                cell.TopLabel.text = "Shipping Information".localized
                cell.cashOndel.text = creditmemoData["shipping_method"]
                cell.value.text    = "Total Shipping Charges:".localized + creditmemoData["shipping_price"]!
                let bounds = UIScreen.main.bounds
                let height:CGFloat = 150
                var counter:CGFloat = 0
                for data in shipmenttracks {
                    let view = ced_shippingCarier()
                    view.frame = CGRect(x: cell.frame.origin.x + 20, y: height+counter*150,width: cell.frame.width - 40,height: 170)
                    view.carrierButton.setTitle(data["carrier"], for: UIControl.State())
                    view.title.text = data["carrier"]
                    view.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                    view.number.text = data["number"]
                    view.submitButton.setTitle("DELETE".localized, for: UIControl.State())
                    view.submitButton.tag = Int(counter)
                     view.submitButton.addTarget(self, action: #selector(ced_shipmentView.deleteCarrier(_:)), for: UIControl.Event.touchUpInside)
                    view.center.x = cell.contentView.center.x
                    cell.contentView.addSubview(view)
                    counter += 1
                }
                
                let view = ced_shippingCarier()
                view.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                view.frame = CGRect(x: cell.frame.origin.x + 20,y: height+counter*170,width: cell.bounds.width - 40,height: 170)
                view.carrierButton.setTitle("Select".localized, for: UIControl.State())
                view.submitButton.setTitle("ADD".localized, for: UIControl.State())
                view.carrierButton.addTarget(self, action: #selector(ced_shipmentView.showdropdown(_:)), for: UIControl.Event.touchUpInside)
                view.submitButton.tag = Int(counter)
                view.submitButton.addTarget(self, action: #selector(ced_shipmentView.addCarrier(_:)), for: UIControl.Event.touchUpInside)
                view.tag = 143
                view.center.x = cell.contentView.center.x
                cell.contentView.addSubview(view)
                counter += 1
                return cell
 
            }
        }else if(indexPath.section == 1){
            let cell = shipmentViewtable.dequeueReusableCell(withIdentifier: "productinvoice") as? ced_invoiceproduct
            cell?.productName.text = productItems[indexPath.row]["name"]
            cell?.qty.text   = productItems[indexPath.row]["qty_ordered"]
            cell?.sku.text   = productItems[indexPath.row]["sku"]
           cell?.qtyLbl.text = "Qty".localized
            cell?.skuLbl.text = "SKU".localized
            cell?.prodNameLbl.text = "Product".localized
            let additionalinfo = additionalOptions[indexPath.row]
            for(key,value) in additionalinfo
            {
                let labelView = KeyValueLabelView()
                cell?.additionalStackView.addArrangedSubview(labelView);
                labelView.keyLabel.text = key;
                labelView.valueLabel.text = value
                labelView.translatesAutoresizingMaskIntoConstraints = false
                let heightConstrain = NSLayoutConstraint(item: labelView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 30)
                labelView.addConstraint(heightConstrain)
                cell?.additionalStackHeight.constant += 40;
                
            }
            return cell!
        }else{
            if(indexPath.row < comments.count){
            let cell = shipmentViewtable.dequeueReusableCell(withIdentifier: "ced_comments") as? ced_comments
            cell?.commentsDate.text = comments[indexPath.row]["date_time"]
            cell?.userNotified.text = comments[indexPath.row]["notified"]
            cell?.comment.text   = comments[indexPath.row]["comment"]
                cell?.t1.text=" Comment Date".localized
                cell?.t2.text=" User Notified".localized
                cell?.t3.text=" Comment".localized
            return cell!
            }else{
                let cell = shipmentViewtable.dequeueReusableCell(withIdentifier: "ced_subtotals") as? ced_subtotalsCell
                cell?.t1.text=" SubTotal".localized
                cell?.t2.text=" Shipping & Handling".localized
                cell?.t3.text=" Discount".localized
                cell?.t4.text=" Tax Amount".localized
                cell?.t5.text=" Grand Total".localized
                cell?.subTotal.text = creditmemoData["order_subtotal"]
                cell?.shippinghandling.text = creditmemoData["shipping_price"]
                cell?.grandTotalEarned.text   = creditmemoData["order_total"]
                cell?.commissionFee.text   = creditmemoData["order_commission"]
                cell?.netEarned.text   = creditmemoData["net_earn"]
                return cell!
            }

            
        }
        
        
    }
   
    //Mark Add carrier
    
    @objc func addCarrier(_ sender:UIButton){
        
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
      let view = self.view.viewWithTag(143) as? ced_shippingCarier
        let number = view?.number.text
        let title = view?.title.text?.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed)
        let carrier = view?.carrierButton.titleLabel?.text!.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed)
        
        let baseUrl = "vorderapi/vshipment/addShipmentTracking"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString    += "&title="+title!+"&carrier="+carrier!+"&number="+number!+"&shipment_id="+shipmentId
        sendRequest(url: baseUrl, parameters: postString);
    }
    
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        if(requestUrl == "vorderapi/vshipment/addShipmentTracking"){
            if let data = data{
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    additionalOptions = [[String:String]]()
                    accountData = [[String:String]]()
                    creditmemoData = [String:String]()
                    productItems = [[String:String]]()
                    comments = [[String:String]]()
                    shipmenttracks = [[String:String]]()
                    carriersList = [String]()
                    loadData()
//                    self.parseData(json)
//                    self.topLabel.text = self.creditmemoData["header_meassage"]
//                    self.shipmentViewtable.reloadData()
                }else{
                    self.view.showToastMsg("Problem in loading Data".localized)
                }
            }
        }
        if requestUrl == "vorderapi/vshipment/removeShipmentTracking"{
            guard let data = data else {return}
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if json["data"]["success"].boolValue{
                self.view.makeToast(json["data"]["message"].stringValue)
                
                additionalOptions = [[String:String]]()
                accountData = [[String:String]]()
                creditmemoData = [String:String]()
                productItems = [[String:String]]()
                comments = [[String:String]]()
                shipmenttracks = [[String:String]]()
                carriersList = [String]()
                
                self.loadData()
            }
                
        }
    }
    
    @objc func deleteCarrier(_ sender:UIButton){
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let tracking = shipmenttracks[sender.tag]
        guard let trackId = tracking["track_id"] else {return}
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString    += "&shipment_id=" + shipmentId + "&track_id=" + trackId

        self.sendRequest(url: "vorderapi/vshipment/removeShipmentTracking", parameters: postString)
    }
    
    @objc func showdropdown(_ sender:UIButton){
        let dropDown = DropDown();
        let view = self.view.viewWithTag(143) as? ced_shippingCarier
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
    //end
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.section == 1){
            if(additionalOptions[indexPath.row].count > 0)
            {
                return CGFloat(120 + (additionalOptions[indexPath.row].count * 40))
            }
            return 120
        }
        if(indexPath.section == 2){
            return 120
        }
        if(indexPath.section == 0){
            if(indexPath.row == 0){
                return 185
            }
            if( indexPath.row == 5){
                return 200 + CGFloat((shipmenttracks.count+1)*170)
            }
            if(indexPath.row == 1){
                return 0//155 + CGFloat(accountData.count*40)
            }
            if(indexPath.row == 2 || indexPath.row == 3){
                return 0//180
            }
            if(indexPath.row == 4 ){
                return 0//150
            }
        }
        return UITableView.automaticDimension
    }
    
    @objc func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if(section == 0){
            return nil
        }
        else if(section == 1){
            return "Items Refunded Shipment".localized
        }else {
            return "Shipment History".localized
        }
    }
    
    /** Mark data loading*/
    @objc func loadData(){
        var baseUrl = settings.baseUrl
        baseUrl += "vorderapi/vshipment/view"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString    += "&shipment_id="+shipmentId
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

            DispatchQueue.main.async
                {
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
                        self.shipmentViewtable.isHidden = true
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
                            self.topLabel.text = self.creditmemoData["header_meassage"]
                            self.shipmentViewtable.reloadData()
                        }else{
                            self.view.showToastMsg("Problem in loading Data".localized)
                        }
                    }
                    
            }
        })
        task.resume()
        
        
    }
    
    func parseData(_ json:JSON){
        for data in json["data"]["shipmentData"].arrayValue{
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
            self.creditmemoData = invoicedata
            for accountDa in  data["account_data"].arrayValue{
                var  acc = [String:String]()
                acc["label"] = accountDa["label"].stringValue
                acc["value"] = accountDa["value"].stringValue
                accountData.append(acc)
            }
            for product in  data["items"].arrayValue{
                var prdct = [String:String]()
                prdct["row_total"] = product["row_total"].stringValue
                prdct["sku"]      = product["sku"].stringValue
                prdct["price"]    = product["price"].stringValue
                prdct["tax_amount"] = product["tax_amount"].stringValue
                prdct["subtotal"]    = product["subtotal"].stringValue
                prdct["qty_ordered"] = product["qty_shipped"].stringValue
                prdct["name"]         = product["name"].stringValue
                prdct["discount_amount"] = product["discount_amount"].stringValue
                var options = [String:String]()
                for index in product["additional_options"].arrayValue
                {
                    options[index["label"].stringValue] = index["value"].stringValue
                }
                additionalOptions.append(options)
                productItems.append(prdct)
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

}
