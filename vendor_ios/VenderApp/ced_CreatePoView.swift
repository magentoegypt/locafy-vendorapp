//
//  ced_CreatePoView.swift
//  VenderApp
//
//  Created by Macmini on 18/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_CreatePoView: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UITextFieldDelegate,UITextViewDelegate,UIGestureRecognizerDelegate
{
    @IBOutlet weak var topview: UILabel!
    @IBOutlet weak var tableview: UITableView!
    var quote_id = ""
    var PO_info=[[String:String]]()
    var address_info = [String:String]()
    var quote_total_info = [String:String]()
    var customer_info = [String:String]()
    var shipping_info = [String:String]()
    var order_info = [String:String]()
    var chat_info = [[String:String]]()
    var item_info = [[String:String]]()
    var viewmessagehistory = false
    var message = UITextView()
    var pinfo = ""
    var from_po = false
    var arraytextfeild = [[String:UITextField]]()
    var arrayTextFeild_To_Chage = [UITextField]()
    var UpdatedUnitPrice = [UITextField]()
    var QuantitytoPO = [UITextField]()
    var Subtotal = [UITextField]()
    var RowTotal = [UITextField]()
    ///////////
    var sub = [String]()
    var row = [String]()
    var idArray = [String]()
    var po = [String]()
    var subString = ""
    var rowString = ""
    var quoteItemId = ""
    var quoteArr = [String]()
    var idString = ""
    var quantitytopo_string = ""
    //////////
    ///
    var colorString = ""
    var currencySymbol = UserDefaults.standard.value(forKey: "currencySymbol") as! String
    override func viewDidLoad() {
        super.viewDidLoad()
        loadData()
        colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        topview.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        topview.text = "Vendor PO Management".localized
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    
    
    
    //    MARK: data get responce
    @objc func loadData(){
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        var baseUrl = ""
        var postString = ""
        baseUrl = "vrfqapi/po/create"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey+"&quote_id=" + quote_id
        print(postString)
        sendRequest(url: baseUrl, parameters: postString);
    }
    
    
    
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            if(requestUrl=="vrfqapi/po/create")
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
                else
                {
//                    self.view.showToastMsg("Problem in loading Data")
                    self.view.makeToast("Problem in loading Data".localized, duration: 2.0, position: .center)
                    Ced_CommonVendor.delay(2.0, closure: {
                        self.navigationController?.popViewController(animated: true)
                    })
                }
            }
            else if(requestUrl=="vrfqapi/po/submitpo")
            {
                print("HELLO JSON")
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    self.view.showToastMsg(json["data"]["message"].stringValue)
                    Ced_CommonVendor.delay(2.0, closure: {
                        self.navigationController?.popViewController(animated: true)
                    })
                }else if json["data"]["success"] == false
                {
//                    self.view.showToastMsg(json["data"]["message"].stringValue)
                   // self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                    self.tableview.setEmptyMessage(json["data"]["message"].stringValue)
                   return
                }
            }
            tableview.restore()
            tableview.reloadData()
        }
    }

    func parsePoData(_ json:JSON)
    {
        if json["data"]["address_info"].dictionaryValue != nil
        {
            let data = json["data"]["address_info"].dictionaryValue
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
            let sub_total_of_po = data["sub_total"]!.stringValue
            //let shiping_and_handling = data["shiping_and_handling"]!.stringValue
            let total_due = data["total_due"]!.stringValue
            quote_total_info = ["grand_total_of_po":grand_total_of_po,"sub_total_of_po":sub_total_of_po,"total_due":total_due]
        }
        
        /*if json["data"]["shipping_info"].dictionaryValue != nil
        {
            let data = json["data"]["shipping_info"].dictionaryValue
            let shipping_charges = data["shipping_charges"]!.stringValue
            let shipping_description = data["shipping_description"]!.stringValue
            shipping_info = ["shipping_charges":shipping_charges,"shipping_description":shipping_description]
        }*/
        
        if json["data"]["customer_info"].dictionaryValue != nil
        {
            let data = json["data"]["customer_info"].dictionaryValue
            let customer_name = data["customer_name"]!.stringValue
            let customer_group = data["customer_group"]!.stringValue
            let customer_email = data["customer_email"]!.stringValue
            customer_info = ["customer_name":customer_name,"customer_group":customer_group,"customer_email":customer_email]
            
        }
        for result in json["data"]["item_info"].arrayValue{
            
            //MARK:Updated
            
            let product_id = result["product_id"].stringValue
            let proposal_created_for_qty = result["proposal_created_for_qty"].stringValue
            let quote_item_id = result["quote_item_id"].stringValue
            let sku = result["sku"].stringValue
            let requested_unit_price = result["requested_unit_price"].stringValue
            let title = result["title"].stringValue
            let proposed_qty = result["proposed_qty"].stringValue
            let proposed_unit_price = result["proposed_unit_price"].stringValue
            let subtotal = result["subtotal"].stringValue
            let iteminfo = ["product_id":product_id,
                            "proposal_created_for_qty":proposal_created_for_qty,
                            "quote_item_id":quote_item_id,
                            "sku":sku,
                            "requested_unit_price":requested_unit_price,
                            "title":title,
                            "proposed_qty":proposed_qty,
                            "proposed_unit_price":proposed_unit_price,
                            "subtotal":subtotal]
            self.item_info.append(iteminfo)
        }
        print(quote_total_info)
        print(address_info)
        print(shipping_info)
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
            return 2
        }
        else if section == 1
        {
            return item_info.count
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
                let cell = tableView.dequeueReusableCell(withIdentifier: "AccountInformation") as! ced_Quote_AccountInformationCELL
                
                cell.AccountInformation_Lable.text = "ACCOUNT INFORMATION".localized
                cell.customerNameLbl.text = "Customer Name".localized
                cell.EmailLbl.text = "Email".localized
                cell.customerGroupLbl.text = "Customer Group".localized
                
                
                cell.AccountInformation_Lable.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
                cell.AccountInformationcell.makeCard(cell.AccountInformationcell, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                cell.CustomerName.text = customer_info["customer_name"]
                cell.Email.text = customer_info["customer_email"]
                cell.CustomerGroup.text = customer_info["customer_group"]
                return cell
            }
            else if indexPath.row == 1
            {
                let cell = tableView.dequeueReusableCell(withIdentifier: "AddressAndShippingInformation") as! ced_Quote_AccountInformationCELL
                
                cell.AddressAndShippingInformation.text = " ADDRESS INFORMATION".localized
                cell.ShippingAddress.text = "Shipping Address".localized
                
                
                cell.AddressAndShippingInformationcell.makeCard(cell.AddressAndShippingInformationcell, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                cell.AddressAndShippingInformation.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
                let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
                cell.subadd1.text = userData["vendorName"] as? String
                cell.subadd2.text = address_info["street"]!+" "+address_info["state"]!+" "+address_info["pincode"]!
                cell.subadd3.text = address_info["country"]
                cell.subadd4.text = "TEl: "+address_info["telephone"]!
                cell.subadd5.text = ""
                return cell
            }else if indexPath.row == 2
            {
                let cell = tableView.dequeueReusableCell(withIdentifier: "ShippingInformation") as! ced_Quote_AccountInformationCELL
                cell.ShippingInformation.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
                cell.ShippingInformation.makeCard(cell.ShippingInformationCell, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                cell.ShippingInformation.text = "SHIPPING INFORMATION".localized
                cell.emailshopinginfo.text = "Total Shipping Charges :".localized + shipping_info["shipping_charges"]!
                cell.CustomerNameShopinginfo.text = "Shippment Method Selected:".localized + shipping_info["shipping_description"]!
                return cell
            }
        }
        else if indexPath.section == 1
        {
            let cell = tableView.dequeueReusableCell(withIdentifier: "ced_CreatePOitemCell") as! ced_CreatePOitemCell
            cell.cellview.makeCard(cell.cellview, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
            cell.headerinfo.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
            cell.headerinfo.text = "ITEMS ORDERED".localized
            
            cell.Product.text = item_info[indexPath.row]["title"]
            cell.SKU.text = item_info[indexPath.row]["sku"]
            cell.proposedQty.text = item_info[indexPath.row]["proposed_qty"]
            cell.proposedQty.tag = indexPath.row
            cell.requestedUnitPrice.text = item_info[indexPath.row]["requested_unit_price"]
            cell.proposedUnitPrice.text = item_info[indexPath.row]["proposed_unit_price"]
            cell.proposalCreatedForQty.text = item_info[indexPath.row]["proposal_created_for_qty"]
            cell.RowTotal.text = item_info[indexPath.row]["subtotal"]
            cell.proposedQty.addTarget(self, action: #selector(self.changetextfeild(sender:)), for: .editingChanged)
//            cell.QuantitytoPO.addTarget(self, action: #selector(self.changetextfeild(sender:)), for: .editingChanged)
//            cell.QuantitytoPO.tag = indexPath.row
//            cell.UpdatedUnitPrice.tag = indexPath.row
//            UpdatedUnitPrice.append(cell.UpdatedUnitPrice)
            QuantitytoPO.append(cell.proposedQty)
//            Subtotal.append(cell.Subtotal)
           // RowTotal.append(cell.RowTotal)
            return cell
        }
        let cell = tableView.dequeueReusableCell(withIdentifier: "QuoteTotals") as! ced_messagecell
        
        cell.QuoteTotalsLable.text = "QUOTE TOTALS".localized
        cell.subtotalLbl.text = "Subtotal".localized
        cell.grandTotalLbl.text = "Grand Total".localized
        cell.totalDueLbl.text = "Total Due".localized
        
        
        cell.QuoteTotals.makeCard(cell.QuoteTotals, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        cell.Subtotal.text = quote_total_info["sub_total_of_po"]
        //cell.ShippingHandling.text = quote_total_info["shiping_and_handling"]
        cell.TotalDue.text = quote_total_info["total_due"]
        cell.GrandTotal.text = quote_total_info["grand_total_of_po"]
        cell.TotalDue.isEnabled = false
        cell.GrandTotal.isEnabled = false
        cell.Subtotal.isEnabled = false
        cell.SAVEQUOTE.addTarget(self,action: #selector(SaveQuatesPressed(sender:)), for: .touchUpInside)
         cell.SAVEQUOTE.setTitleColor(UIColor.white, for: .normal)
        cell.SAVEQUOTE.setTitleColor(UIColor.white, for: .normal)
        cell.SAVEQUOTE.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
         cell.SAVEQUOTE.setTitleColor(UIColor.white, for: .normal)
//        "Submit Purchase Order"
        cell.SAVEQUOTE.setTitle("Submit Purchase Order".localized, for: .normal);
        arrayTextFeild_To_Chage.append(cell.Subtotal)
        arrayTextFeild_To_Chage.append(cell.TotalDue)
        arrayTextFeild_To_Chage.append(cell.GrandTotal)
        return cell
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.section == 0
        {
            if indexPath.row == 0
            {
                return 165
            }
            else if indexPath.row == 1
            {
                return 235
            }
            else if indexPath.row == 2
            {
                return 125
            }
        }
        else if indexPath.section == 1
        {
            return 400
        }
        return 240
    }

    
    @objc func changetextfeild(sender : UITextField)
    {
        guard let cell1 = tableview.cellForRow(at: IndexPath(row: sender.tag, section: 1)) as? ced_CreatePOitemCell else{print("cell1");return}
        
        if cell1.proposedQty.text != ""
        {
            let a = Int(sender.text ?? "") ?? 1
            let b = Double(item_info[sender.tag]["proposed_unit_price"]?.replacingOccurrences(of: currencySymbol, with: "").replacingOccurrences(of: ",", with: "") ?? "") ?? 1.0
            let rt =  Double(a) * b
            print(rt)
            let price = String(rt)
            print(price)
            let prevRT = cell1.RowTotal.text
            cell1.RowTotal.text = price
            item_info[sender.tag]["proposed_qty"] = String(a)
            let val1 = Double(quote_total_info["sub_total_of_po"]?.replacingOccurrences(of: currencySymbol, with: "").replacingOccurrences(of: ",", with: "") ?? "") ?? 1.0
            let val2 = Double(prevRT?.replacingOccurrences(of: currencySymbol, with: "").replacingOccurrences(of: ",", with: "") ?? "") ?? 1.0//
            let st = String((val1 - val2) + rt)
            print("ST:\(st)")
            
            quote_total_info["sub_total_of_po"] = st
            quote_total_info["grand_total_of_po"] = st
            quote_total_info["total_due"] = st
            guard let cell2 = tableview.cellForRow(at: IndexPath(row: 0, section: 2)) as? ced_messagecell else{print("cell2");return}
            cell2.Subtotal.text = st
            cell2.GrandTotal.text = st
            cell2.TotalDue.text = st
            
        }
    }
    @objc func SaveQuatesPressed(sender : UIButton)
    {
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let baseUrl = "vrfqapi/po/submitpo"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        parameterArray()
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey+"&quote_id=" + quote_id
        let subPostString = "&quoteproducts="+quantitytopo_string+"&quote_item_ids="+quoteItemId//"&subtotal="+subString+"&row_total="+rowString+"&products="+idString+"&noofproducts="+String(item_info.count)+
        let subpostString1 = "&customer_email="+customer_info["customer_email"]!+"&grandtotalofpo="+quote_total_info["grand_total_of_po"]!.replacingOccurrences(of: currencySymbol, with: "").replacingOccurrences(of: ",", with: "")+"&totaldue="+quote_total_info["total_due"]!.replacingOccurrences(of: currencySymbol, with: "").replacingOccurrences(of: ",", with: "")+"&subtotalofpo="+quote_total_info["sub_total_of_po"]!.replacingOccurrences(of: currencySymbol, with: "").replacingOccurrences(of: ",", with: "")
        

        postString = postString+subPostString//+subpostString1
        print(postString)
        sendRequest(url: baseUrl, parameters: postString);
    }
    @objc func parameterArray()
    {
        for i in 0 ..< item_info.count
        {
            sub.append(item_info[i]["subtotal"]!.replacingOccurrences(of: currencySymbol, with: "").replacingOccurrences(of: ",", with: ""))
            guard let cell1 = tableview.cellForRow(at: IndexPath(row: i, section: 1)) as? ced_CreatePOitemCell else{print("cell1");return}

            let a = Int(cell1.proposedQty.text ?? "") ?? 1
            let b = Double(item_info[i]["proposed_unit_price"]?.replacingOccurrences(of: currencySymbol, with: "").replacingOccurrences(of: ",", with: "") ?? "") ?? 1.0
            let rt =  Double(a) * b
            print(rt)
            row.append("\(rt)")
            idArray.append(item_info[i]["product_id"]!)
              po.append(item_info[i]["proposed_qty"]!)
            quoteArr.append(item_info[i]["quote_item_id"]!)
        }
        subString = JSONConvert(data: sub)
        rowString = JSONConvert(data: row)
        idString = JSONConvert(data: idArray)
        quantitytopo_string = JSONConvert(data: po)
        quoteItemId = JSONConvert(data: quoteArr)
    }
    
    @objc func JSONConvert(data :[String]) -> String
    {
        var ConvertData = ""
        let x = data as [String]
        do{
            let JSONData = try JSONSerialization.data(
                withJSONObject: x ,
                options: JSONSerialization.WritingOptions(rawValue: 0))
            ConvertData = NSString(data: JSONData,
                                   encoding: String.Encoding.utf8.rawValue)! as String
        }
        catch{
            print("error in data encoding")
        }
        print(ConvertData)
        return ConvertData
    }
    
}
