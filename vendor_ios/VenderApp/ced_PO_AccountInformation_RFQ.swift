//
//  ced_PO_AccountInformation_RFQ.swift
//  VenderApp
//
//  Created by Macmini on 16/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class PO_AccountInformation_RFQ: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UITextFieldDelegate,UITextViewDelegate,UIGestureRecognizerDelegate
{
    @IBOutlet weak var topheader: UILabel!
    @IBOutlet weak var tableview: UITableView!
    var po_id = ""
    
    
    var PO_info=[[String:String]]()
    var address_info = [String:String]()
    var PO_total_info = [String:String]()
    var customer_info = [String:String]()
    var shipping_info = [String:String]()
    var order_info = [String:String]()
    var chat_info = [[String:String]]()
    var item_info = [[String:String]]()
    var viewmessagehistory = false
    var message = UITextView()
    var pinfo = ""
    var from_po = false
     var colorString = ""
    var arraytextfeild = [[String:UITextField]]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        loadData()
        colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        topheader.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        topheader.text = pinfo
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    //    MARK: data get responce
    @objc func loadData(){
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        var baseUrl = ""
        var postString = ""
        baseUrl = "vrfqapi/po/viewpo"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey+"&po_id=" + po_id
        print(postString)
        sendRequest(url: baseUrl, parameters: postString);
    }
    
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            if(requestUrl=="vrfqapi/po/viewpo")
            {
                print("HELLO JSON")
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    self.parsePoData(json)
                    
                }else if json["data"]["success"] == false
                {
//                    self.view.showToastMsg(json["data"]["message"].stringValue)
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                    Ced_CommonVendor.delay(2.0, closure: {
                        self.navigationController?.popViewController(animated: true)
                    })
                }
            }
            tableview.reloadData()
        }
        
    }
    
    func parsePoData(_ json:JSON)
    {
        if json["data"]["address_info"].dictionaryValue != nil
        {
            let data = json["data"]["address_info"].dictionaryValue
            print(data)
            print(data["telephone"]!.stringValue)
            let telephone = data["telephone"]!.stringValue
            let country = data["country"]!.stringValue
            let state = data["state"]!.stringValue
            let city = data["city"]!.stringValue
            let pincode = data["pincode"]!.stringValue
            let street = data["street"]!.stringValue
            address_info = ["telephone":telephone,"country":country,"state":state,"city":city,"pincode":pincode,"street":street]
            
        }
        if json["data"]["quote_total_info"].dictionaryValue != nil
        {
            let data = json["data"]["quote_total_info"].dictionaryValue
            let grand_total_of_po = data["grand_total"]!.stringValue
            let sub_total_of_po = data["subtotal"]!.stringValue
            //let shiping_and_handling = data["shipping_handling"]!.stringValue
            PO_total_info = ["grand_total_of_po":grand_total_of_po,"sub_total_of_po":sub_total_of_po]
        }
        
        
        if json["data"]["account_info"].dictionaryValue != nil
        {
            let data = json["data"]["account_info"].dictionaryValue
            let customer_name = data["customer_name"]!.stringValue
            let customer_group = data["customer_group"]!.stringValue
            let customer_email = data["customer_email"]!.stringValue
            customer_info = ["customer_email":customer_email,"customer_name":customer_name,"customer_group":customer_group]
        }
        /*if json["data"]["shipping_info"].dictionaryValue != nil
        {
            let data = json["data"]["shipping_info"].dictionaryValue
            let shipping_charges = data["shipping_charges"]!.stringValue
            let shipping_description = data["shipping_description"]!.stringValue
            shipping_info = ["shipping_charges":shipping_charges,"shipping_description":shipping_description]
        }*/
        
        if json["data"]["po_info"].dictionaryValue != nil
        {
            let data = json["data"]["po_info"].dictionaryValue
            let po_date = data["po_date"]!.stringValue
            let po_status = data["po_status"]!.stringValue
            let po_increment_id = data["po_increment_id"]!.stringValue
            order_info = ["po_date":po_date,"po_status":po_status,"po_increment_id":po_increment_id]
            
        }
        for result in json["data"]["item_info"].arrayValue{
            let proposed_qty = result["proposed_qty"].stringValue
            let requested_price = result["requested_price"].stringValue
            let sku = result["sku"].stringValue
            let title = result["title"].stringValue
            let requested_qty = result["requested_qty"].stringValue
            let remaining_qty = result["remaining_qty"].stringValue
            let row_total = result["row_total"].stringValue
            let iteminfo = ["proposed_qty":proposed_qty,
                                "requested_price":requested_price,
                            "sku":sku,
                            "title":title,
                            "requested_qty":requested_qty,
                            "remaining_qty":remaining_qty,
                            "row_total":row_total]
            self.item_info.append(iteminfo)
        }
        print(PO_total_info)
        print(address_info)
        print(customer_info)
        print(shipping_info)
        print(chat_info)
        print(item_info)
        tableview.delegate = self
        tableview.dataSource = self
        
    }
    
    
    
    //MARK: table view delegate @objc function
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 3
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0
        {
            return 3
        }
        else if section == 1
        {
            return 1
        }
        else
        {
            return 1
        }
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.section == 0
        {
            if indexPath.row == 0
            {
                let cell = tableView.dequeueReusableCell(withIdentifier: "ced_Quote_AccountInformationCELL") as! ced_Quote_AccountInformationCELL
                
                cell.headerlable.text = "PO Information".localized
                cell.toplable_order.text = "Order Info".localized
                cell.PODateLbl.text = "PO Date".localized
                cell.postatusLbl.text = "PO Status".localized
                
                
                cell.QuoteDate.text=order_info["po_date"]
                cell.headerlable.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
                cell.ced_Quoteview.makeCard(cell.ced_Quoteview, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                if ((order_info["po_status"] as? NSNull) != nil)
                {
                    cell.CurrentQuoteStatus.text="NULL"
                }
                else
                {
                    cell.CurrentQuoteStatus.text=order_info["po_status"]
                }
                cell.QuoteCreatedFrom.text=order_info["quote_created_from"]
                cell.QuotedTotalQuantity.text=order_info["quote_total_quantity"]
                cell.QuotedTotalPrice.text=order_info["quoted_total_price"]
                return cell
            }
            else if indexPath.row == 1
            {
                let cell = tableView.dequeueReusableCell(withIdentifier: "AccountInformation") as! ced_Quote_AccountInformationCELL
                
                cell.AccountInformation_Lable.text = " Account Information".localized
                cell.customerNameLbl.text = "Customer Name".localized
                cell.EmailLbl.text = "Email".localized
                cell.customerGroupLbl.text = "Customer Group".localized
                
                
                cell.CustomerName.text = customer_info["customer_name"]
                cell.Email.text = customer_info["customer_email"]
                cell.CustomerGroup.text = customer_info["customer_group"]
                cell.AccountInformation_Lable.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
                cell.AccountInformationcell.makeCard(cell.AccountInformationcell, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                return cell
            }
            else if indexPath.row == 2
            {
                let cell = tableView.dequeueReusableCell(withIdentifier: "AddressAndShippingInformation") as! ced_Quote_AccountInformationCELL
                cell.AddressAndShippingInformationcell.makeCard(cell.AddressAndShippingInformationcell, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                cell.AddressAndShippingInformation.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
                let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
                cell.subadd1.text = userData["vendorName"] as? String
                cell.subadd2.text = address_info["street"]!+" "+address_info["state"]!+" "+address_info["pincode"]!
                cell.subadd3.text = address_info["country"]
                cell.subadd4.text = "TEl: "+address_info["telephone"]!
                cell.subadd5.text = ""
                return cell
            }else if indexPath.row == 3
            {
                let cell = tableView.dequeueReusableCell(withIdentifier: "ShippingInformation") as! ced_Quote_AccountInformationCELL
                cell.ShippingInformation.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
                
                cell.ShippingInformation.makeCard(cell.ShippingInformationCell, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                cell.emailshopinginfo.text = "Total Shipping Charges :".localized + shipping_info["shipping_charges"]!
                cell.CustomerNameShopinginfo.text = "Shippment Method Selected:".localized + shipping_info["shipping_description"]!
                return cell
            }
        }
        else if indexPath.section == 1
        {
            let cell = tableView.dequeueReusableCell(withIdentifier: "ced_PO_Detail_Cell") as! ced_PO_Detail_Cell
            cell.cellview.makeCard(cell.cellview, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
            cell.toplable.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
            cell.product.text = item_info[indexPath.row]["title"]
            cell.sku.text = item_info[indexPath.row]["sku"]
            cell.requestedQty.text = item_info[indexPath.row]["requested_qty"]
            cell.requestedPrice.text = item_info[indexPath.row]["requested_price"]
            cell.proposedQty.text = item_info[indexPath.row]["proposed_qty"]
            cell.remainingqty.text = item_info[indexPath.row]["remaining_qty"]
            cell.rowtotal.text = item_info[indexPath.row]["row_total"]!
            return cell
        }
        let cell = tableView.dequeueReusableCell(withIdentifier: "QuoteTotals") as! ced_messagecell
          cell.QuoteTotals.makeCard(cell.QuoteTotals, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        cell.Subtotal.text = PO_total_info["sub_total_of_po"]
        cell.Subtotal.isEnabled = false;
        cell.GrandTotal.isEnabled = false;
        //cell.ShippingHandling.text = PO_total_info["shiping_and_handling"]
        cell.GrandTotal.text = PO_total_info["grand_total_of_po"]
        return cell
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.section == 0
        {
            if indexPath.row == 0
            {
                return 150
            }
            else if indexPath.row == 1
            {
                return 170
            }
            else if indexPath.row == 2
            {
                return 230
            }
            else if indexPath.row == 3
            {
                return 125
            }
        }
        else if indexPath.section == 1
        {
             return 280
        }
        return 150
    }
}
