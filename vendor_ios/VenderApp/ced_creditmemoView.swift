//
//  ced_creditmemoView.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 30/06/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_creditmemoView: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate {
    
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var creditmemoView: UITableView!
    var creditMemoId = String()
   
    var accountData = [[String:String]]()
    var creditmemoData = [String:String]()
    var productItems = [[String:String]]()
    var comments = [[String:String]]()
     var Floatbutton = UIButton()
    override func viewDidLoad() {
        super.viewDidLoad()
        creditmemoView.register(newOrderSubtotalCell.self, forCellReuseIdentifier: newOrderSubtotalCell.reuseId)
        
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        creditmemoView.delegate = self
        creditmemoView.dataSource = self
        creditmemoView.estimatedRowHeight = 300
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        ced_navigationBarController().addNavButton(self,str:"back".localized)
       // self.addFloatingbutton()
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
        
        Floatbutton.addTarget(self, action: #selector(ced_creditmemoView.addComments(_:)), for: UIControl.Event.touchUpInside)
        self.view.insertSubview(imageview, aboveSubview: self.creditmemoView)
        self.view.insertSubview(Floatbutton, aboveSubview: self.creditmemoView)
    }
    
    @objc func scrollViewDidScroll(_ scrollView: UIScrollView) {
        let bounds = creditmemoView.bounds
        let frame = Floatbutton.frame
        Floatbutton.frame.origin.y = bounds.origin.y + 400
        self.view.bringSubviewToFront(Floatbutton)
        self.Floatbutton.frame = frame;
    }
    
    @objc func addComments(_ sender:UIButton){
        let view = storyboard?.instantiateViewController(withIdentifier: "ced_addcomments") as? ced_invoiceAddcomments
        view?.prodView = "creditmemo"
        view?.invoiceId = creditMemoId
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
                let cell = creditmemoView.dequeueReusableCell(withIdentifier: "invoiceOrderInfo") as! ced_orderInfo
                cell.orderDate.text = creditmemoData["orderStoreDate"]
                cell.orderStatus.text = creditmemoData["statusLabel"]
                cell.purchasedFrom.text = creditmemoData["storeName"]
                cell.orderDateTitle.text=" Order Date".localized
                cell.order.text=" Order Status".localized
                cell.purchTitle.text=" Purchased From".localized
                return cell
            }else if(indexPath.row == 1){
                let cell = creditmemoView.dequeueReusableCell(withIdentifier: "invoiceAccountInfo") as! ced_invoiceAccountInfo
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
                let cell = tableView.dequeueReusableCell(withIdentifier: "invoiceshippingbill") as! ced_billingShipping
                cell.label.text = "Billing Address".localized
                cell.texView.text = creditmemoData["bill_name"]! + "\n" + creditmemoData["bill_street"]! + creditmemoData["bill_city"]! + "\n"
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
                let cell = creditmemoView.dequeueReusableCell(withIdentifier: "payinfo") as! ced_payinfo
                cell.TopLabel.text = " Payment Information".localized
                cell.cashOndel.text = creditmemoData["payment_method_title"]
                cell.value.text    = creditmemoData["message_at_last"]
                return cell
            }else{
                let cell = creditmemoView.dequeueReusableCell(withIdentifier: "payinfo") as! ced_payinfo
                cell.TopLabel.text = " Shipping Information".localized
                cell.cashOndel.text = creditmemoData["shipping_method"]
                cell.value.text    = "Total Shipping Charges:".localized + creditmemoData["order_shipping"]!
                return cell
            }
        }else if(indexPath.section == 1){
            let cell = creditmemoView.dequeueReusableCell(withIdentifier: "productinvoice") as? ced_invoiceproduct
            cell?.productName.text = productItems[indexPath.row]["name"]
            cell?.price.text = productItems[indexPath.row]["price"]
            cell?.qty.text   = productItems[indexPath.row]["qty_ordered"]
            cell?.sku.text   = productItems[indexPath.row]["sku"]
            cell?.subtotal.text = productItems[indexPath.row]["subtotal"]
            cell?.taxamount.text = productItems[indexPath.row]["tax_amount"]
            cell?.discountAmount.text = productItems[indexPath.row]["discount_amount"]
            cell?.rowTotal.text = productItems[indexPath.row]["row_total"]
            return cell!
        }else{
            if(indexPath.row < comments.count){
            let cell = creditmemoView.dequeueReusableCell(withIdentifier: "ced_comments") as? ced_comments
            cell?.commentsDate.text = comments[indexPath.row]["date_time"]
            cell?.userNotified.text = comments[indexPath.row]["notified"]
            cell?.comment.text   = comments[indexPath.row]["comment"]
            cell?.t1.text=" Comment Date".localized
            cell?.t2.text=" User Notified".localized
            cell?.t3.text=" Comment".localized
            return cell!
            }else{
                let cell = tableView.dequeueReusableCell(withIdentifier: newOrderSubtotalCell.reuseId, for: indexPath) as! newOrderSubtotalCell
                cell.populate(creditmemoViewData: creditmemoData)
                return cell
//                let cell = creditmemoView.dequeueReusableCell(withIdentifier: "ced_subtotals") as? ced_subtotalsCell
//                cell?.topLabel.text = " Credit Memo Totals".localized
//                cell?.t1.text=" SubTotal".localized
//                cell?.t2.text=" Shipping & Handling".localized
//                cell?.t3.text=" Discount".localized
//                cell?.t4.text=" Tax Amount".localized
//                cell?.t5.text=" Grand Total".localized
//                cell?.subTotal.text = creditmemoData["order_subtotal"]
//                cell?.shippinghandling.text = creditmemoData["shipping_price"]
//                cell?.Grandtotal.text   = creditmemoData["order_total"]
//                cell?.commissionFee.text   = creditmemoData["order_commission"]
//                cell?.netEarned.text   = creditmemoData["net_earn"]
//                cell?.netEarned.text  = creditmemoData["order_discount"]
//                cell?.grandTotalEarned.text = creditmemoData["adjustment_positive"]
//                cell?.commissionFee.text    = creditmemoData["adjustment_negative"]
//                cell?.taxAmount.text = creditmemoData["order_tax"]
//
//                return cell!
            }

            
        }
        
        
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.section == 1){
            return 245
        }
        if(indexPath.section == 2){
            print(indexPath.row)
            if(indexPath.row == comments.count){
                return UITableView.automaticDimension
            }
            return 110
        }
        if(indexPath.section == 0){
            if(indexPath.row == 0){
                return 185
            }else if(indexPath.row == 4){
                return 0//150
            }else if(indexPath.row == 5){
                return 150
            }else if(indexPath.row == 1){
                return 0//155 + CGFloat(accountData.count*40)
            }else if(indexPath.row == 2 || indexPath.row == 3){
                return 0//180
            }
        }
        return 300
    }
    
    @objc func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if(section == 0){
            return nil
        }
        else if(section == 1){
            return "Items Refunded".localized
        }else {
            if(comments.count != 0){
                return "Credit Memo History".localized
            }
            return nil
        }
    }
    
    /** Mark data loading*/
    
    @objc func loadData(){
        var baseUrl = "";
        baseUrl += "vorderapi/vcreditmemo/view"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString    += "&creditmemo_id="+creditMemoId
        sendRequest(url: baseUrl, parameters: postString);
        
    }
    
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if(json["data"]["success"].stringValue == "true"){
                self.parseData(json)
                self.topLabel.text = self.creditmemoData["header_meassage"]
                self.creditmemoView.reloadData()
            }else{
                self.view.showToastMsg("Problem in loading Data")
            }
        }
        

    }
    
    func parseData(_ json:JSON){
        for data in json["data"]["creditmemoData"].arrayValue{
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
            invoicedata["adjustment_negative"] = data["adjustment_negative"].stringValue
            invoicedata["adjustment_positive"] = data["adjustment_positive"].stringValue
            invoicedata["marketplace_fees"] = data["marketplace_fees"].stringValue
            invoicedata["shipping_refund"] = data["shipping_refund"].stringValue
            invoicedata["order_tax"] = data["order_tax"].stringValue
            invoicedata["order_discount"] = data["order_discount"].stringValue
            invoicedata["shipment_handled"] = data["shipment_handled"].stringValue
            invoicedata["order_shipping"] = data["order_shipping"].stringValue
            
            invoicedata["order_total_incl"] = data["order_total_incl"].stringValue
            invoicedata["order_subtotal_excl"] = data["order_subtotal_excl"].stringValue
            invoicedata["order_subtotal_incl"] = data["order_subtotal_incl"].stringValue
            invoicedata["order_total_excl"] = data["order_total_excl"].stringValue
            
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
                prdct["qty_ordered"] = product["qty_ordered"].stringValue
                prdct["name"]         = product["name"].stringValue
                prdct["discount_amount"] = product["discount_amount"].stringValue
                prdct["igstAmt"] = product["igstAmt"].stringValue
                prdct["cgstAmt"] = product["csgstAmt"].stringValue
                prdct["sgstAmt"] = product["sgstAmt"].stringValue
                productItems.append(prdct)
            }
            for coments  in data["comments"].arrayValue{
                var  comentData = [String:String]()
                comentData["comment"] = coments["comment"].stringValue
                comentData["date_time"] = coments["date_time"].stringValue
                comentData["notified"]  = coments["notified"].stringValue
              //  comments.append(comentData)
            }
        }
        
    }
    
    
}
