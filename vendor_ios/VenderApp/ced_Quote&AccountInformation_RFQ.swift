//
//  ced_Quote&AccountInformation_RFQ.swift
//  VenderApp
//
//  Created by Macmini on 15/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_Quote_AccountInformation_RFQ: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource
{
    
    @IBOutlet weak var topViewHeight: NSLayoutConstraint!
    @IBOutlet weak var tableview: UITableView!
    @IBOutlet weak var qutainfo: UILabel!
    @IBOutlet weak var createpobutton: UIButton!
    
    @IBOutlet weak var acceptBtn: UIButton!
    @IBOutlet weak var declineBtn: UIButton!
    var quote_id = ""
    var filter=""
    var applyFilter = false
    var quote_info=[[String:String]]()
    var address_info = [String:String]()
    var quote_total_info = [String:String]()
    var customer_info = [String:String]()
    var shipping_info = [String:String]()
    var order_info = [String:String]()
    var chat_info = [[String:String]]()
    var item_info = [[String:String]]()
    var viewmessagehistory = false
    var message = UITextView()
    var qinfo = ""
    var selectedStatus = ""
    var from_po = false
    var data_to_send = [[String:String]]()
    var arraytextfeild = [[String:UITextField]]()
    var unit = [String]()
    var quantity = [String]()
    var sub = [String]()
    var row = [String]()
    var idArray = [String]()
    var unitString = ""
    var quantityString = ""
    var subString = ""
    var rowString = ""
    var idString = ""
    
    var currencySymbol = UserDefaults.standard.value(forKey: "currencySymbol") as! String
    
    var dropDownData = [String]()
    var dropDownkey = [String]()
    
    var colorString = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        loadData()
        qutainfo.text = " Quote # ".localized+qinfo
        colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        qutainfo.textColor = Ced_CommonVendor.UIColorFromRGB(colorString)
        createpobutton.backgroundColor = DynamicColor.secondaryColor//Ced_CommonVendor.UIColorFromRGB(colorString)
        createpobutton.layer.cornerRadius = 5.0
        acceptBtn.backgroundColor = UIColor.init(hexString: "#4d994d")
        acceptBtn.layer.cornerRadius = 5.0
        declineBtn.backgroundColor = UIColor.init(hexString:  "#FF444F")
        declineBtn.layer.cornerRadius = 5.0
        acceptBtn.setTitleColor(.white, for: .normal)
        declineBtn.setTitleColor(.white, for: .normal)
        createpobutton.setTitle("Create Po".localized.uppercased(), for: .normal)
        acceptBtn.setTitle("Accept".localized.uppercased(), for: .normal)
        declineBtn.setTitle("Decline".localized.uppercased(), for: .normal)
        acceptBtn.tag = 2
        declineBtn.tag = 3
        acceptBtn.addTarget(self, action:  #selector(SAVEQUOTE(sender:)), for: .touchUpInside)
        declineBtn.addTarget(self, action:  #selector(SAVEQUOTE(sender:)), for: .touchUpInside)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    
    //    MARK: data get responce
    @objc func loadData(){
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        var baseUrl = ""
        var postString = ""
        baseUrl = "vrfqapi/quote/viewquote"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        postString = "vendor_id=" + vendorId + "&quote_id=" + quote_id//+ "&hashkey=" + hashKey
        print(postString)
        sendRequest(url: baseUrl, parameters: postString);
    }
    
    
    
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            if(requestUrl=="vrfqapi/quote/viewquote")
            {
                print("HELLO JSON")
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    self.parseProductdata(json)
                    
                }else if json["data"]["success"] == false
                {
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                    Ced_CommonVendor.delay(2.0, closure: {
                        self.navigationController?.popViewController(animated: true)
                    })
                    
//                    Alert_File.showValidationError(self,title:"Error",message:json["data"]["message"].stringValue);
                }
                //getDropdowndata(url: "vrfqapi/quote/configval")
            }
            else if(requestUrl=="vrfqapi/quote/chat")
            {
                print("HELLO JSON")
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    self.view.showToastMsg(json["data"]["message"].stringValue)
                    
                }else if json["data"]["success"] == false
                {
//                    Alert_File.showValidationError(self,title:"Error",message:json["data"]["message"].stringValue);
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                    Ced_CommonVendor.delay(2.0, closure: {
                        self.navigationController?.popViewController(animated: true)
                    })
                }
                
            }
            else if(requestUrl=="vrfqapi/quote/update")
            {
                print("HELLO JSON")
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    self.view.showToastMsg(json["data"]["message"].stringValue)
                    Ced_CommonVendor.delay(2.0, closure: {
                       // self.navigationController?.popViewController(animated: true)
                        let stoy = UIStoryboard(name: "Main", bundle: nil)
                        let viewControl = stoy.instantiateViewController(withIdentifier: "ced_ManageQuotes") as! ced_ManageQuotes
                        self.navigationController?.setViewControllers([viewControl], animated: true)
                    })
                }else if json["data"]["success"] == false
                {
//                    Alert_File.showValidationError(self,title:"Error",message:json["data"]["message"].stringValue);
                   // self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                    self.tableview.setEmptyMessage(json["vendor_data"]["message"].stringValue)
                    return
                }
                
            }
            tableview.restore()
            tableview.delegate = self
            tableview.dataSource = self
            tableview.reloadData()
        }
        
    }
    
    
    func parseProductdata(_ json:JSON)
    {
        if json["data"]["show_po_button"] == true
        {
            createpobutton.isHidden=false
        }
        else
        {
            createpobutton.isHidden=true
        }
        if json["data"]["show_approve_button"] == "2"
        {
            acceptBtn.isHidden=false
        }
        else
        {
            acceptBtn.isHidden=true
        }
        if json["data"]["show_reject_button"] == "3"
        {
            declineBtn.isHidden=false
        }
        else
        {
            declineBtn.isHidden=true
        }
        if (acceptBtn.isHidden == false || declineBtn.isHidden == false || createpobutton.isHidden == false){
            topViewHeight.constant = 90.0
        }else{
            topViewHeight.constant = 45.0
        }
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
            let grand_total_of_po = data["grand_total_of_po"]!.stringValue
            let sub_total_of_po = data["sub_total_of_po"]!.stringValue
            //let shiping_and_handling = data["shiping_and_handling"]!.stringValue
            let total_due = data["total_due"]!.stringValue
            quote_total_info = ["grand_total_of_po":grand_total_of_po,"sub_total_of_po":sub_total_of_po,"total_due":total_due]
        }
        
        
        if json["data"]["customer_info"].dictionaryValue != nil
        {
            let data = json["data"]["customer_info"].dictionaryValue
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
        
        if json["data"]["order_info"].exists()//dictionaryValue != nil
        {
            let data = json["data"]["order_info"].dictionaryValue
            let quote_date = json["data"]["order_info"]["quote_date"].stringValue
            let quote_status = json["data"]["order_info"]["quote_status"].stringValue
            let quote_created_from = json["data"]["order_info"]["quote_created_from"].stringValue
            //let quote_total_quantity = json["data"]["order_info"]["quote_total_quantity"].stringValue
            let quote_increment_id = json["data"]["order_info"]["quote_increment_id"].stringValue
            //let quoted_total_price = json["data"]["order_info"]["quoted_total_price"].stringValue
            order_info = ["quote_date":quote_date,"quote_created_from":quote_created_from,"quote_status":quote_status,"quote_increment_id":quote_increment_id]
            
        }
        for result in json["data"]["chat_info"].arrayValue{
            let sent_by = result["sent_by"].stringValue
            let chat_date = result["chat_date"].stringValue
            let chat_message = result["chat_message"].stringValue
            let iteminfo = ["sent_by":sent_by,"chat_date":chat_date,"chat_message":chat_message]
            self.chat_info.append(iteminfo)
        }
        
        for result in json["data"]["item_info"].arrayValue{
            let row_total = result["row_total"].stringValue
            let sku = result["sku"].stringValue
            let proposedUnitPrice = result["proposed_unit_price"].stringValue
            let title = result["title"].stringValue
            let stock = result["stock"].stringValue
            let requestedUnitPrice = result["requested_unit_price"].stringValue
            let requestedQty = result["requested_qty"].stringValue
            let proposedQty = result["proposed_qty"].stringValue
            let proposalCreatedForQty = result["proposal_created_for_qty"].stringValue
            let productId = result["product_id"].stringValue
            let editable = result["editable"].stringValue
            let quote_item_id = result["quote_item_id"].stringValue
            let iteminfo = ["quote_item_id":quote_item_id,
                            "sku":sku,
                            "proposal_created_for_qty":proposalCreatedForQty,
                            "proposed_qty":proposedQty,
                            "product_id":productId,
                            "requested_qty":requestedQty,
                            "requested_unit_price":requestedUnitPrice,
                            "editable":editable,
                            "proposed_unit_price":proposedUnitPrice,
                            "row_total":row_total,
                            "title":title,
                            "stock":stock] //["row_total":row_total,"sku":sku,"quoted_price":quoted_price,"title":title,"stock":stock,"actual_price":actual_price,"currency_code":currency_code,"qty_already_poed":qty_already_poed,"quote_qty":quote_qty,"quantity_to_po":quantity_to_po,"subtotal":subtotal,"unit_price":unit_price,"id":id]
            self.item_info.append(iteminfo)
        }
        
        print(quote_total_info)
        print(address_info)
        print(customer_info)
        print(shipping_info)
        print(chat_info)
        print(item_info)
        
    }
    //MARK: table view delegate @objc function
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 4
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0
        {
            return 3
        }
        else if section == 1
        {
            return item_info.count
        }
        else if section == 2
        {
            if viewmessagehistory == false
            {
                return 2
            }
            else
            {
                return 2 + chat_info.count
            }
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
                cell.headerlable.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
                cell.headerlable.text = "QUOTE & ACCOUNT INFORMATION".localized
                cell.quoteDataLbl.text = "Quote Date".localized
                cell.currentQuoteLvl.text = "Current Quote Status".localized
                cell.quoteCreatedFromLbl.text = "Quote Created From".localized
                
                cell.ced_Quoteview.makeCard(cell.ced_Quoteview, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                cell.toplable_order.text = "Order Info #".localized+"\(order_info["quote_increment_id"] ?? "")"
                
                cell.QuoteDate.text=order_info["quote_date"]
                if ((order_info["quote_status"] as? NSNull) != nil)
                {
                    cell.CurrentQuoteStatus.text=""
                }
                else
                {
                    cell.CurrentQuoteStatus.text=order_info["quote_status"]
                }
                cell.QuoteCreatedFrom.text=order_info["quote_created_from"]
                return cell
            }
            else if indexPath.row == 1
            {
                let cell = tableView.dequeueReusableCell(withIdentifier: "AccountInformation") as! ced_Quote_AccountInformationCELL
                cell.AccountInformation_Lable.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
                cell.AccountInformation_Lable.text = "ACCOUNT INFORMATION".localized
                cell.customerNameLbl.text = "Customer Name".localized
                cell.EmailLbl.text = "Email".localized
                cell.customerGroupLbl.text = "Customer Group".localized
                
                cell.AccountInformationcell.makeCard(cell.AccountInformationcell, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                cell.CustomerName.text = customer_info["customer_name"]
                cell.Email.text = customer_info["customer_email"]
                cell.CustomerGroup.text = customer_info["customer_name"]
                return cell
            }
            else if indexPath.row == 2
            {
                let cell = tableView.dequeueReusableCell(withIdentifier: "AddressAndShippingInformation") as! ced_Quote_AccountInformationCELL
                cell.AddressAndShippingInformationcell.makeCard(cell.AddressAndShippingInformationcell, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                cell.AddressAndShippingInformation.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
                cell.AddressAndShippingInformation.text = " ADDRESS & SHIPPING INFORMATION".localized
                cell.ShippingAddress.text = "Shipping Address".localized
                
                let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
                cell.subadd1.text = userData["vendorName"] as? String
                cell.subadd2.text = address_info["street"]!+" "+address_info["state"]!+" "+address_info["pincode"]!
                cell.subadd3.text = address_info["country"]
                cell.subadd4.text = "TEl: ".localized+address_info["telephone"]!
                cell.subadd5.text = ""
                return cell
            }else if indexPath.row == 3
            {
                let cell = tableView.dequeueReusableCell(withIdentifier: "ShippingInformation") as! ced_Quote_AccountInformationCELL
               cell.ShippingInformation.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
                cell.ShippingInformation.text = "SHIPPING INFORMATION".localized
                
                cell.ShippingInformation.makeCard(cell.ShippingInformationCell, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                cell.emailshopinginfo.text = "Total Shipping Charges :".localized + shipping_info["shipping_charges"]!
                cell.CustomerNameShopinginfo.text = "Shippment Method Selected:".localized + shipping_info["shipping_description"]!
                return cell
            }
        }
        else if indexPath.section == 1
        {
            let cell = tableView.dequeueReusableCell(withIdentifier: "ced_itemOrdered") as! ced_itemOrdered
            
            cell.ced_itemOrderedCell.makeCard(cell.ced_itemOrderedCell, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
            cell.toplable.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
            cell.toplable.text = " ITEMS ORDERED".localized
            
            cell.product.text = item_info[indexPath.row]["title"]
            cell.SKU.text = item_info[indexPath.row]["sku"]
            cell.ItemStock.text = item_info[indexPath.row]["stock"]
            cell.proposalCreatedForQty.text = item_info[indexPath.row]["proposal_created_for_qty"]!
            cell.proposedQty.text = item_info[indexPath.row]["proposed_qty"]!
            cell.rowTotal.text = item_info[indexPath.row]["row_total"]!
            cell.proposedUnitPrice.text = item_info[indexPath.row]["row_total"]!
            cell.requestedQty.text = item_info[indexPath.row]["requested_qty"]!
            cell.requestedUniPrice.text=item_info[indexPath.row]["requested_unit_price"]!
            cell.requestedUniPrice.tag = indexPath.row
            cell.rowTotal.tag = indexPath.row
            cell.proposedQty.isEnabled = false
            cell.proposedUnitPrice.isEnabled = false
            if item_info[indexPath.row]["editable"]! == "true"{
                cell.proposedQty.isEnabled = true
                cell.proposedUnitPrice.isEnabled = true
                cell.proposedQty.addTarget(self, action: #selector(ProposedQty(sender:)), for: .allEditingEvents)
                cell.proposedUnitPrice.addTarget(self, action: #selector(proposedUnitPrice(sender:)), for: .allEditingEvents)
            }
//            cell.UpdatedUnitPrice.addTarget(self, action: #selector(self.UpdatedUnitPrice(sender:)), for: .allEditingEvents)
//            cell.QuantitytoPO.addTarget(self, action: #selector(self.QuantitytoPO(sender:)), for: .allEditingEvents)
//            cell.Subtotal.addTarget(self, action: #selector(self.Subtotal(sender:)), for: .allEditingEvents)
//            cell.RowTotal.addTarget(self, action: #selector(self.RowTotal(sender:)), for: .allEditingEvents)
            return cell
        }
        else if indexPath.section == 2
        {
            if indexPath.row == 0
            {
                if viewmessagehistory == false
                {
                    let cell = tableView.dequeueReusableCell(withIdentifier: "ced_messagecell") as! ced_messagecell
                    cell.messagesection.text = "Messaging section".localized
                    if chat_info.count == 0
                    {
                        cell.chathistory.text = "No chat history".localized
                    }
                    else
                    {
                        cell.chathistory.text = "Chat History".localized
                    }
                    
                    cell.ced_messagecell.makeCard(cell.ced_messagecell, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                    cell.viewmessage.addTarget(self, action: #selector(self.viewmessagepressed(sender:)), for: .touchUpInside)
                    cell.viewmessage.tag = 1
                    return cell
                }
                else
                {
                    
                    let cell = tableView.dequeueReusableCell(withIdentifier: "ced_messagecell") as! ced_messagecell
                    cell.messagesection.text = "Messaging section".localized
                    if chat_info.count == 0
                    {
                        cell.chathistory.text = "No chat history".localized
                    }
                    else
                    {
                        cell.chathistory.text = "Chat History".localized
                    }
                    cell.ced_messagecell.makeCard(cell.ced_messagecell, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                    cell.viewmessage.addTarget(self, action: #selector(self.viewmessagepressed(sender:)), for: .touchUpInside)
                    cell.viewmessage.tag = 2
                    return cell
                }
            }
            else if indexPath.row == 1
            {
                let cell = tableView.dequeueReusableCell(withIdentifier: "SendMessage") as! ced_messagecell
                cell.SendMessage.makeCard(cell.SendMessage, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                cell.sendmessage.setTitle("Send Message".localized, for: .normal)
                self.message=cell.messagetext
                cell.messagetext.layer.borderWidth = 1.0;
                cell.messagetext.layer.borderColor = UIColor.lightGray.cgColor
                cell.sendmessage.addTarget(self, action: #selector(self.sendmessage(sender:)), for: .touchUpInside)
                cell.sendmessage.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
                return cell
            }
            else
            {
                let cell = tableView.dequeueReusableCell(withIdentifier: "ced_messagecell_messages") as! ced_messagecell
                cell.ced_messagecell_messages.makeCard(cell.ced_messagecell_messages, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                cell.datamessage.text = chat_info[indexPath.row-2]["chat_date"]! + chat_info[indexPath.row-2]["sent_by"]!
                cell.messagedata.text = chat_info[indexPath.row-2]["chat_message"]
                return cell
            }
        }
        let cell = tableView.dequeueReusableCell(withIdentifier: "QuoteTotals") as! ced_messagecell
        cell.QuoteTotals.makeCard(cell.QuoteTotals, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        cell.QuoteTotalsLable.text = "Quote Totals".localized
        cell.subtotalLbl.text = "Subtotal".localized
        cell.grandTotalLbl.text = "Grand Total".localized
        cell.shippHandleLbl.text = "Shipping & Handling".localized
        cell.totalDueLbl.text = "Total Due".localized
        cell.SAVEQUOTE.setTitle("SAVE QUOTE".localized, for: .normal)
        
        cell.Subtotal.text=quote_total_info["sub_total_of_po"]
        cell.GrandTotal.text=quote_total_info["grand_total_of_po"]
      // cell.ShippingHandling.text=quote_total_info["shiping_and_handling"]
        cell.TotalDue.text=quote_total_info["total_due"]
        
        cell.Subtotal.isEnabled = false
        cell.GrandTotal.isEnabled = false
        cell.TotalDue.isEnabled = false
//        cell.Status.setTitle(order_info["quote_status"], for: .normal)
//        cell.Status.addTarget(self, action: #selector(self.showstatus(sender:)), for: .touchUpInside)
//        cell.Status.backgroundColor = UIColor.white;
//        cell.Status.titleLabel?.textColor = UIColor.black;
        cell.SAVEQUOTE.addTarget(self, action: #selector(self.SAVEQUOTE(sender:)), for: .touchUpInside)
        cell.SAVEQUOTE.tag = Int(selectedStatus) ?? 0
         cell.SAVEQUOTE.setTitleColor(UIColor.white, for: .normal)
        cell.SAVEQUOTE.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        return cell
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.section == 0
        {
            if indexPath.row == 0
            {
                return 180
            }
            else if indexPath.row == 1
            {
                return 165
            }
            else if indexPath.row == 2
            {
                return 235
            }
            else if indexPath.row == 3
            {
                return 125
            }
        }
        else if indexPath.section == 1
        {
            return 400
        }
        else if indexPath.section == 2
        {
            if indexPath.row == 0
            {
                return 140
            }
            else if indexPath.row == 1
            {
                return 200
            }
            else
            {
                return 120
            }
        }
        return 200
    }
    
    @objc func viewmessagepressed(sender:UIButton){
        if sender.tag == 1
        {
            viewmessagehistory=true
        }
        else
        {
            viewmessagehistory=false
        }
        
        tableview.reloadData()
    }
    @objc func showstatus(sender:UIButton){
        let dropDown = DropDown();
        dropDown.dataSource = dropDownData
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: .normal)
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    @objc func sendmessage(sender:UIButton){
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let baseUrl = "vrfqapi/quote/chat"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey+"&quote_id=" + quote_id+"&message="+message.text
        print(postString)
        sendRequest(url: baseUrl, parameters: postString);
    }
    @objc func SAVEQUOTE(sender:UIButton){
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let baseUrl = "vrfqapi/quote/update"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
        parameterArray()
        
        
        
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey+"&quote_id=" + quote_id
        let subPostString = "&quote_updated_qty=" + quantityString + "&unitprice=" + unitString + "&quote_item_ids=" + idString //"&unitprice="+unitString+"&quoteproducts="+quantityString+"&subtotal="+subString+"&row_total="+rowString+"&products="+idString

        let subpostString1 = "&customer_email="+customer_info["customer_email"]!+"&status=\(sender.tag)"//+"&grandtotalofpo="+quote_total_info["grand_total_of_po"]!+"&totaldue="+quote_total_info["total_due"]!+"&subtotalofpo="+quote_total_info["sub_total_of_po"]!+
        postString = postString+subPostString+subpostString1
        print(postString)
        sendRequest(url: baseUrl, parameters: postString);
    }
    
    
    
    @objc func UpdatedUnitPrice(sender:UITextField){
        item_info[sender.tag]["unit_price"]=sender.text
    }
    @objc func QuantitytoPO(sender:UITextField){
        item_info[sender.tag]["quantity_to_po"]=sender.text
    }
    @objc func Subtotal(sender:UITextField){
        item_info[sender.tag]["subtotal"]=sender.text
    }
    @objc func RowTotal(sender:UITextField){
        item_info[sender.tag]["row_total"]=sender.text
    }
    @objc func ProposedQty(sender:UITextField){
        item_info[sender.tag]["proposed_qty"]=sender.text
    }
    @objc func proposedUnitPrice(sender:UITextField){
        item_info[sender.tag]["proposed_unit_price"]=sender.text
    }
    @objc func parameterArray()
    {
        unit.removeAll()
        quantity.removeAll()
        idArray.removeAll()
        for i in 0 ..< item_info.count
        {
            unit.append(item_info[i]["proposed_unit_price"]!.replacingOccurrences(of: currencySymbol, with: "").replacingOccurrences(of: ",", with: ""))
            quantity.append(item_info[i]["proposed_qty"]!)
            idArray.append(item_info[i]["quote_item_id"]!)
        }
        unitString=JSONConvert(data: unit)
        quantityString=JSONConvert(data: quantity)
        idString = JSONConvert(data: idArray)
    }
    
    
    
    @objc func JSONConvert(data :[String]) -> String {
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
    
    
    
    
// MARK: All Working of Create PO Button
    
    
    
    @IBAction @objc func createpo_Pressed(_ sender: Any) {
        let navigation = self.sideDrawerViewController?.mainViewController as! ced_navigationBarController
        let stoy = UIStoryboard(name: "Main", bundle: nil)
        let viewControl = stoy.instantiateViewController(withIdentifier: "ced_CreatePoView") as! ced_CreatePoView
        viewControl.quote_id = self.quote_id
        navigation.pushViewController(viewControl, animated: true)
    }
    
    //    MARK: Dropdown Data
    @objc func getDropdowndata(url: String)
    {
        var baseUrl = settings.baseUrl
        baseUrl += url
        var request =  URLRequest(url: URL(string:baseUrl)!)
        
        request.httpMethod = "GET"
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
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error".localized, showMsg: error!.localizedDescription)
                }
                print("error=\(error)")
                return;
            }
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                DispatchQueue.main.async
                    {
                        
                        Alert_File.removeLoadingIndicator(self)
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        print("response = \(response)")
                        print(baseUrl)
                        //self.verdorOrderTable.isHidden = true
                        if(httpStatus.statusCode == -1009)
                        {
                            ced_showError.showNoNetWork(self)
                        }else
                        {
                            ced_showError.showNoModule(self)
                        }
                }
                return;
            }
            DispatchQueue.main.async
            {
                Alert_File.removeLoadingIndicator(self)
                print("HELLO JSON")
                if let data = data{
                    guard let json = try? JSON(data: data) else {return}
                    print(json["status"])
                    for values in json["status"].arrayValue
                    {
                        self.dropDownData.append(values["key"].stringValue)
                        self.dropDownkey.append(values["value"].stringValue)
                    }
                    print(self.dropDownData)
                }
            }
        })
        task.resume()
    }
    
}
