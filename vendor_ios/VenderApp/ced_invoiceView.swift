//
//  ced_invoiceView.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 30/06/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_invoiceView: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource {
    
    @IBOutlet weak var createCreditMemo: UIButton!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var invoiceViewtable: UITableView!
   
    var invoiceId = String()
    var invoiceData = [String:String]()
    var productItems  = [[String:String]]()
    var comments    = [[String:String]]()
    var accountData = [[String:String]]()
    var Floatbutton = UIButton()
    var EnabledButtons = [String]()
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        invoiceViewtable.register(newOrderSubtotalCell.self, forCellReuseIdentifier: newOrderSubtotalCell.reuseId)
        invoiceViewtable.delegate = self
        invoiceViewtable.dataSource = self
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
       
        ced_navigationBarController().addNavButton(self,str:"back".localized)
        
    }
    override func viewWillAppear(_ animated: Bool) {
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        self.loadData()
       // self.addFloatingbutton()
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
        
        Floatbutton.addTarget(self, action: #selector(ced_invoiceView.addComments(_:)), for: UIControl.Event.touchUpInside)
        self.view.insertSubview(imageview, aboveSubview: self.invoiceViewtable)
        self.view.insertSubview(Floatbutton, aboveSubview: self.invoiceViewtable)
    }
    
    @objc func addComments(_ sender:UIButton){
        let view = storyboard?.instantiateViewController(withIdentifier: "ced_addcomments") as? ced_invoiceAddcomments
        view?.prodView = "invoice"
        view?.invoiceId = invoiceId
        self.present(view!, animated: true, completion: nil)
        
    }
    @objc func scrollViewDidScroll(_ scrollView: UIScrollView) {
        let bounds = invoiceViewtable.bounds
        let frame = Floatbutton.frame
        Floatbutton.frame.origin.y = bounds.origin.y + 400
        self.view.bringSubviewToFront(Floatbutton)
        self.Floatbutton.frame = frame;
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
        if(invoiceData.count == 0){
            return 0
        }
        return 6
    }
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if(indexPath.section == 0){
            if(indexPath.row == 0){
            let cell = invoiceViewtable.dequeueReusableCell(withIdentifier: "invoiceOrderInfo") as! ced_orderInfo
                cell.topLabel.text="Order Information".localized
                cell.orderDate.text = invoiceData["orderStoreDate"]
                cell.orderStatus.text = invoiceData["statusLabel"]
                cell.purchasedFrom.text = invoiceData["storeName"]
                cell.orderDateTitle.text=" Order Date".localized
                cell.order.text=" Order Status".localized
                cell.purchTitle.text=" Purchased From".localized
            return cell
            }else if(indexPath.row == 1){
                let cell = invoiceViewtable.dequeueReusableCell(withIdentifier: "invoiceAccountInfo") as! ced_invoiceAccountInfo
                cell.topLabel.text="  Account Information".localized
                cell.cemail.text=" Email".localized
                cell.cgrp.text=" Customer Group".localized
                cell.cName.text=" Customer Name".localized
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
                cell.label.text = "Billing Address".localized.uppercased()
                cell.texView.text = invoiceData["bill_name"]! + "\n" + invoiceData["bill_street"]! + invoiceData["bill_city"]! + "\n"
                cell.texView.text! += invoiceData["bill_region"]! + "\n" + invoiceData["bill_postcode"]! + "\n"
                cell.texView.text! += invoiceData["bill_country_id"]! + "\n" + invoiceData["bill_telephone"]!
                return cell
            }else if(indexPath.row == 3){
                let cell = tableView.dequeueReusableCell(withIdentifier: "invoiceshippingbill") as! ced_billingShipping
                cell.label.text = "Shipping Address".localized.uppercased()
                cell.texView.text = invoiceData["name"]! + "\n" + invoiceData["street"]! + "\n" + invoiceData["city"]! + "\n"
                cell.texView.text! += invoiceData["region"]! + "\n" + invoiceData["postcode"]! + "\n"
                cell.texView.text! += invoiceData["country_id"]! + "\n" + invoiceData["telephone"]!
                return cell
            }else if(indexPath.row == 4){
                let cell = invoiceViewtable.dequeueReusableCell(withIdentifier: "payinfo") as! ced_payinfo
                cell.TopLabel.text = "Payment Information".localized.uppercased()
                cell.cashOndel.text = invoiceData["payment_method_title"]
                cell.value.text    = invoiceData["message_at_last"]
                return cell
            }else{
                if invoiceData["shipment_handled"] == "vendor"{
                    let cell = invoiceViewtable.dequeueReusableCell(withIdentifier: "payinfo") as! ced_payinfo
                    cell.TopLabel.text = " Shipping Information".localized
                    cell.cashOndel.text = invoiceData["shipping_method"]
                    cell.value.text    = "Total Shipping Charges:".localized + invoiceData["shipping_price"]!
                    return cell
                }else{
                    return .init()
                }
                
                
            }
        }else if(indexPath.section == 1){
           let cell = invoiceViewtable.dequeueReusableCell(withIdentifier: "productinvoice") as? ced_invoiceproduct
            
            cell?.rowTotalLbl.text = "Row Total".localized
            cell?.discountLbl.text = "Discount Amount".localized
            cell?.taxAmmLbl.text = "Tax Amount".localized
            cell?.subtotalLbl.text = "Subtotal".localized
            cell?.qtyLbl.text = "Qty".localized
            cell?.skuLbl.text = "SKU".localized
            cell?.priceLabel.text = "Price".localized
            cell?.prodNameLbl.text = "Product".localized
            
            cell?.productName.text = productItems[indexPath.row]["name"]
            cell?.price.text = productItems[indexPath.row]["price"]
            cell?.qty.text   = productItems[indexPath.row]["qty_ordered"]
            cell?.sku.text   = productItems[indexPath.row]["sku"]
            cell?.subtotal.text = productItems[indexPath.row]["subtotal"]
            cell?.taxamount.text = productItems[indexPath.row]["tax_amount"]
            cell?.discountAmount.text = productItems[indexPath.row]["discount_amount"]
            cell?.rowTotal.text = productItems[indexPath.row]["row_total"]
//            cell?.igstLabel.text = productItems[indexPath.row]["igstAmt"];
//            cell?.cgstLabel.text = productItems[indexPath.row]["cgstAmt"];
//            cell?.sgstLabel.text = productItems[indexPath.row]["sgstAmt"];
            return cell!
        }else{
            if(indexPath.row < comments.count){
            let cell = invoiceViewtable.dequeueReusableCell(withIdentifier: "ced_comments") as? ced_comments
            cell?.commentsDate.text = comments[indexPath.row]["date_time"]
            cell?.userNotified.text = comments[indexPath.row]["notified"]
            cell?.comment.text   = comments[indexPath.row]["comment"]
                
                cell?.t3.text = "Comment".localized
                cell?.t2.text = "User Notified".localized
                cell?.t1.text = "Comment Date".localized
            return cell!
            }else{
//                let cell = invoiceViewtable.dequeueReusableCell(withIdentifier: "ced_subtotals") as? ced_subtotalsCell
//                cell?.t1.text=" SubTotal".localized
//                cell?.t2.text=" Shipping & Handling".localized
//                cell?.t3.text=" Discount".localized
//                cell?.t4.text=" Tax Amount".localized
//                cell?.t5.text=" Grand Total".localized
//                cell?.topLabel.text = "Invoice totals".localized.uppercased()
//                cell?.subTotal.text = invoiceData["order_subtotal"]
//                cell?.shippinghandling.text = invoiceData["shipping_price"]
//                cell?.Grandtotal.text   = invoiceData["order_total"]
//                //cell?.commissionFee.text   = invoiceData["order_commission"]
//                //cell?.netEarned.text   = invoiceData["net_earn"]
//                cell?.taxAmount.text = invoiceData["order_tax"];
//                cell?.discountLabel.text = invoiceData["order_discount"]
//
//                return cell!
                let cell = tableView.dequeueReusableCell(withIdentifier: newOrderSubtotalCell.reuseId, for: indexPath) as! newOrderSubtotalCell
                cell.populate(invoiceViewData: invoiceData)
                return cell
            }

        }
    }

    /** Mark data loading*/
    
    @objc func loadData(){
        let baseUrl = "vorderapi/vinvoice/view"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString    += "&invoice_id="+invoiceId
        sendRequest(url: baseUrl, parameters: postString);

    }
    
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
           guard let json = try? JSON(data: data) else {return}
            print(json)
            if(json["data"]["success"].stringValue == "true"){
                self.parseData(json)
                self.topLabel.text = self.invoiceData["header_meassage"]
                self.invoiceViewtable.restore()
                self.invoiceViewtable.reloadData()
                if(self.EnabledButtons.count == 0){
                    self.createCreditMemo.isHidden = true
                }else{
                    self.createCreditMemo.isHidden = false
                    self.createCreditMemo.addTarget(self, action: #selector(self.ShowOptions(_:)), for: UIControl.Event.touchUpInside)
                    
                }
            }else{
               // self.view.showToastMsg(json["data"]["message"].stringValue)
                self.invoiceViewtable.setEmptyMessage(json["data"]["message"].stringValue)
            }
        }
        

    }
    //SHow CReations options start
    
    @objc func ShowOptions(_ sender:UIButton){
        if #available(iOS 8.0, *) {
            let actionsheet = UIAlertController(title: "Choose Action".localized, message: nil, preferredStyle: UIAlertController.Style.actionSheet)
            for buttons in self.EnabledButtons {
                let action = UIAlertAction(title: buttons, style: UIAlertAction.Style.default, handler: {
                    void in
                    if(buttons == "invoice"){
                        let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
                        let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "invoicecreation") as! ced_creationOfinvoice
                        viewcontrol.invoiceId = self.invoiceId
                        self.navigationController?.pushViewController(viewcontrol, animated: true)
                        
                    }else if(buttons == "ship"){
                        let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
                        let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "createShipment") as! ced_creationOfshipment
                        viewcontrol.invoiceId = self.invoiceId
                        self.navigationController?.pushViewController(viewcontrol, animated: true)
                    }else if(buttons == "creditmemo"){
                        let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
                        let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "creditMemocreation") as! ced_creationOfcreditMemos
                        viewcontrol.inVoiceID = self.invoiceId
                        self.navigationController?.pushViewController(viewcontrol, animated: true)
                    }
                })
                actionsheet.addAction(action)
            }
            let cancelAct = UIAlertAction(title: "Cancel".localized, style: UIAlertAction.Style.destructive, handler: nil)
            actionsheet.addAction(cancelAct)
            self.present(actionsheet, animated: true, completion: nil)
        } else {
            // Fallback on earlier versions
        }
       
    }
    

    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.section == 1){
          return 255
        }
        if(indexPath.section == 2){
            if(indexPath.row == comments.count){
                return  UITableView.automaticDimension
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
            if(indexPath.row == 1){
                return 0//155 + CGFloat(accountData.count*40)
            }
            if(indexPath.row == 2 || indexPath.row == 3){
                return 0//180
            }
            if indexPath.row == 5{
                if invoiceData["shipment_handled"] == "vendor"{
                    return 150
                }else{
                    return 0
                }
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
            return "Items Invoiced".localized.uppercased()
        }else {
            return nil//"Invoice History".localized.uppercased()
        }
    }
    func parseData(_ json:JSON){
        
            for data in json["data"]["invoicedata"].arrayValue{
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
                invoicedata["order_total"] = data["order_subtotal"].stringValue
                invoicedata["bill_company"] = data["bill_company"].stringValue
                invoicedata["orderAdminDate"] = data["orderAdminDate"].stringValue
                invoicedata["CustomerGroup"] = data["CustomerGroup"].stringValue
                invoicedata["postcode"] = data["postcode"].stringValue
                invoicedata["CustomerEmail"] = data["CustomerEmail"].stringValue
                invoicedata["message_at_last"] = data["message_at_last"].stringValue
                invoicedata["shipping_method"] = data["shipping_method"].stringValue
                invoicedata["order_shipping"]  = data["order_shipping"].stringValue
                invoicedata["adjustment_negative"] = data["adjustment_negative"].stringValue
                invoicedata["adjustment_positive"] = data["adjustment_positive"].stringValue
                invoicedata["order_tax"] = data["order_tax"].stringValue
                invoicedata["order_discount"] = data["order_discount"].stringValue
                invoicedata["net_earn"] = data["net_earn"].stringValue
                invoicedata["shipment_handled"] = data["shipment_handled"].stringValue
                
                invoicedata["order_total_incl"] = data["order_total_incl"].stringValue
                invoicedata["order_subtotal_excl"] = data["order_subtotal_excl"].stringValue
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
                    prdct["discount_amount"] = product["discount_amount"].stringValue
                    prdct["igstAmt"] = product["igstAmt"].stringValue
                    prdct["sgstAmt"] = product["sgstAmt"].stringValue
                    prdct["cgstAmt"] = product["cgstAmt"].stringValue
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
                for buttondata in data["buttons"].arrayValue{
                    EnabledButtons.append(buttondata["label"].stringValue)
                }
        }
    }
}
