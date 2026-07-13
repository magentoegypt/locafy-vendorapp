//
//  ced_QuotationViewController.swift
//  VenderApp
//
//  Created by cedcoss on 07/07/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
struct QuoteInfoModel {
    var status_id : String
    var negotiation_detail : NegotiationDetail
    var quotations_detail : QuotationDetail
    var product_names : [ProductNameModel]
    var chat_history : [ChatHistoryModel]
    var show_comments : String
    var save_button : String
    var decline_button : String
    var accept_button : String
    
    init(json:JSON) {
        status_id = json["status_id"].stringValue
        negotiation_detail = NegotiationDetail(json: json["negotiation_detail"])
        quotations_detail =  QuotationDetail(json: json["quotations_detail"])
        product_names = json["product_names"].arrayValue.map({ProductNameModel(json: $0)})
        chat_history = json["chat_history"].arrayValue.map({ChatHistoryModel(json: $0)})
        show_comments = json["show_comments"].stringValue
        save_button = json["save_button"].stringValue
        decline_button = json["decline_button"].stringValue
        accept_button = json["accept_button"].stringValue
    }
}
struct NegotiationDetail {
    var nqty : String
    var product_id : String
    var nprice : String
    init(json:JSON) {
        nqty=json["nqty"].stringValue
        product_id=json["product_id"].stringValue
        nprice=json["nprice"].stringValue
    }
}
    struct QuotationDetail {
        var comment : String
        var customer_id : String
        var product_name : String
        var documents_file : String
        var estimated_quantity : String
        var category_name : String
        var requested_quantity : String
        init(json:JSON) {
            comment = json["comment"].stringValue
            customer_id = json["customer_id"].stringValue
            product_name = json["product_name"].stringValue
            documents_file = json["documents_file"].stringValue
            estimated_quantity = json["estimated_quantity"].stringValue
            category_name = json["category_name"].stringValue
            requested_quantity = json["requested_quantity"].stringValue
        }
    }
    struct  ProductNameModel {
        var value : String
        var label : String
        init(json:JSON) {
            value = json["value"].stringValue
            label = json["label"].stringValue
        }
    }
    struct  ChatHistoryModel {
        var comment : String
        var sender_name : String
        var negotiation_price : String
        var created_at : String
        var negotiation_qty : String
        
        init(json:JSON) {
            comment = json["comment"].stringValue
            sender_name = json["sender_name"].stringValue
            negotiation_price = json["negotiation_price"].stringValue
            created_at = json["created_at"].stringValue
            negotiation_qty = json["negotiation_qty"].stringValue
        }
    }

class ced_QuotationViewController: ced_VendorBaseClass {
    var quotationViewData : QuoteInfoModel?
    var productNameDropDown = [String:String]()
    var requestID = String()
    lazy var quoteIdHeading: UILabel = {
        let label = UILabel()
        label.text = "Quote id".uppercased()
        label.backgroundColor = DynamicColor.secondaryColor
        label.textColor = .white
        label.layer.cornerRadius = 5.0
        label.textAlignment = .center
        return label
    }()
    lazy var acceptBtn: UIButton = {
        let btn = UIButton()
        btn.backgroundColor = UIColor.init(hexString: "#4d994d")
        btn.setTitleColor(.white, for: .normal)
        btn.setTitle("ACCEPT", for: .normal)
        btn.layer.cornerRadius = 5.0
        return btn
    }()
    lazy var declineBtn: UIButton = {
        let btn = UIButton()
        btn.backgroundColor = UIColor.init(hexString: "#ff444f")
        btn.setTitleColor(.white, for: .normal)
        btn.setTitle("DECLINE", for: .normal)
        btn.layer.cornerRadius = 5.0
        return btn
    }()
    
    lazy var buttonStack : UIStackView = {
        let stack = UIStackView(arrangedSubviews: [acceptBtn,declineBtn])
        stack.spacing = 5.0
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        return stack
    }()
    lazy var tableView:UITableView = {
        let table = UITableView()
        table.register(ced_QuotationViewTC.self, forCellReuseIdentifier: ced_QuotationViewTC.reuseID)
        table.register(ced_NegotiationViewTC.self, forCellReuseIdentifier: ced_NegotiationViewTC.reuseID)
        table.register(ced_ChatHistoryTC.self, forCellReuseIdentifier: ced_ChatHistoryTC.reusedID)
        table.register(ced_quoteCommentTC.self, forCellReuseIdentifier: ced_quoteCommentTC.reuseId)
        table.delegate = self
        table.dataSource = self
        return table
    }()
    override func viewDidLoad() {
        super.viewDidLoad()
        loadData()
        acceptBtn.isHidden = true
        declineBtn.isHidden = true
        quoteIdHeading.text = "#"+requestID
        acceptBtn.addTarget(self, action: #selector(AcceptBtnTapped(_:)), for: .touchUpInside)
        declineBtn.addTarget(self, action: #selector(declineButtonTapped(_:)), for: .touchUpInside)
    }
    init(requestId: String) {
        self.requestID = requestId
        
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    func loadData(){
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postData = ["vendor_id":vendorId, "request_id": requestID];
        print(postData)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        sendPORequest(url: "vpoapi/viewQuote", params: postData)
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if requestUrl == "vpoapi/viewQuote"
        {
            Alert_File.removeLoadingIndicator(self);
            if let data = data{
                guard let json = try? JSON(data: data) else {return}
                print(json);
                
                if(json["vendor_data"]["success"].stringValue == "true")
                {
                    self.quotationViewData = QuoteInfoModel(json: json["vendor_data"]["quote_info"])
                    
                    for data in json["vendor_data"]["quote_info"]["product_names"].arrayValue{
                        print("\(data["value"].stringValue)"+"\(data["label"].stringValue)")
                        self.productNameDropDown[data["label"].stringValue] = data["value"].stringValue
                        
                    }
                    if self.quotationViewData?.accept_button == "1"{
                        acceptBtn.isHidden = false
                    }
                    
                    if self.quotationViewData?.decline_button == "1"{
                        declineBtn.isHidden = false
                    }
                    
                    configureUI()
                    // print(self.quotationViewData)
                    tableView.delegate = self
                    tableView.dataSource = self
                    tableView.reloadData()
                    
                }
                
                else
                {
                    
                    Alert_File.showValidationError(self,title:"Error",message:json["vendor_data"]["message"].stringValue);
                    
                }
            }
        }else if requestUrl == "vpoapi/saveQuote"{
            Alert_File.removeLoadingIndicator(self);
            if let data = data{
                guard let json = try? JSON(data: data) else {return}
                print(json);
                if(json["vendor_data"]["success"].stringValue == "true")
                {
                    self.view.makeToast(json["vendor_data"]["message"].stringValue, duration: 2.0, position: .center)
                    
                    Ced_CommonVendor.delay(2.0) {
                        self.navigationController?.popViewController(animated: true)                    }
                }
                else
                {
                    Alert_File.showValidationError(self,title:"Error",message:json["vendor_data"]["message"].stringValue);
                }
            }
        }else if (requestUrl == "vpoapi/acceptQuote" || requestUrl == "vpoapi/declineQuote"){
            Alert_File.removeLoadingIndicator(self);
            if let data = data{
                guard let json = try? JSON(data: data) else {return}
                print(json);
                if(json["vendor_data"]["success"].stringValue == "true")
                {
                    self.view.makeToast(json["vendor_data"]["message"].stringValue, duration: 2.0, position: .center)
                    
                    Ced_CommonVendor.delay(2.0) {
                        self.navigationController?.popViewController(animated: true)                    }
                }
                else
                {
                    Alert_File.showValidationError(self,title:"Error",message:json["vendor_data"]["message"].stringValue);
                }
            }

        }
    }

        
    
    func configureUI() {
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        view.addSubview(quoteIdHeading)
        if #available(iOS 11.0, *) {
            quoteIdHeading.anchor(top: self.view.safeAreaLayoutGuide.topAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 5, paddingLeft: 5, paddingRight: 5, height: 40)
        } else {
            // Fallback on earlier versions
        }
        
        view.addSubview(buttonStack)
        buttonStack.anchor(top: quoteIdHeading.bottomAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 5, paddingLeft: 5, paddingRight: 5)
        if acceptBtn.isHidden == false || declineBtn.isHidden == false{
            buttonStack.anchor(height: 40)
        }
        
        view.addSubview(tableView)
        tableView.anchor(top: buttonStack.bottomAnchor, left: view.leadingAnchor, bottom: view.bottomAnchor, right: view.trailingAnchor, paddingTop: 2, paddingLeft: 2, paddingBottom: 2, paddingRight: 2)
        
    }
    
        
}
extension ced_QuotationViewController:UITableViewDelegate,UITableViewDataSource{
    func numberOfSections(in tableView: UITableView) -> Int {
        return 5
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            return 1
        case 1:
            return 1
        case 2:
            return 2
        case 3:
            return quotationViewData?.chat_history.count ?? 0
        default:
            if quotationViewData?.save_button == "1"{
            return 1
            }else{
                return 0
            }
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case 0:
            let cell = tableView.dequeueReusableCell(withIdentifier: ced_QuotationViewTC.reuseID, for: indexPath) as! ced_QuotationViewTC
            //cell.configureView()
            if self.quotationViewData?.quotations_detail != nil{
                cell.populateData(data: (self.quotationViewData?.quotations_detail)!)
            }
            cell.documentValue.addTarget(self, action: #selector(urlTapped(_:)), for: .touchUpInside)
            cell.selectionStyle = .none
            return cell
        case 1:
            
            let cell = tableView.dequeueReusableCell(withIdentifier: ced_NegotiationViewTC.reuseID, for: indexPath) as! ced_NegotiationViewTC
            cell.configureView()
            if self.quotationViewData?.negotiation_detail != nil{
                cell.populateData(data: self.quotationViewData!.negotiation_detail)
                cell.ProductRequestValue.setTitle(self.productNameDropDown.someKey(forValue: self.quotationViewData!.negotiation_detail.product_id), for: .normal)
                cell.ProductRequestValue.addTarget(self, action: #selector(openDropdown(_:)), for: .touchUpInside)
                cell.selectionStyle = .none
                return cell
            }
        case 2:
            if indexPath.row == 0{
                let cell = tableView.dequeueReusableCell(withIdentifier: ced_quoteCommentTC.reuseId, for: indexPath) as! ced_quoteCommentTC
                if quotationViewData?.show_comments == "1"{
                    cell.headingLabel.text = "Comment"
                }
                cell.configureView()
                cell.selectionStyle = .none
                return cell
            }else{
                var cell = tableView.dequeueReusableCell(withIdentifier: "cell1") as? UITableViewCell
                if( !(cell != nil))
                {
                    cell = UITableViewCell(style: UITableViewCell.CellStyle.default, reuseIdentifier: "Cell")
                }
                cell?.textLabel?.text = "Chat History"
                cell?.textLabel?.textAlignment = .center
                cell?.textLabel?.textColor = DynamicColor.labelColor
                cell?.textLabel?.font = .systemFont(ofSize: 16, weight: .semibold)
                cell?.selectionStyle = .none
                return cell ?? UITableViewCell()
            }
        case 3 :
            let cell = tableView.dequeueReusableCell(withIdentifier: ced_ChatHistoryTC.reusedID, for: indexPath) as! ced_ChatHistoryTC
            cell.configureView()
            if self.quotationViewData?.chat_history.count != 0{
                cell.populateData(data: (self.quotationViewData?.chat_history[indexPath.row])!)
            }
            cell.selectionStyle = .none
            return cell
        default:
            var cell = tableView.dequeueReusableCell(withIdentifier: "cell2") as? UITableViewCell
            if( !(cell != nil))
            {
                cell = UITableViewCell(style: UITableViewCell.CellStyle.default, reuseIdentifier: "Cell2")
            }
            let btn = UIButton(frame: CGRect(x: 0, y: 0, width: 120, height: 40))
            cell?.accessoryView = btn
            btn.center = cell!.center
            btn.setTitle("SAVE".localized, for: .normal)
            btn.setThemeColor()
            btn.layer.cornerRadius = 5.0
            btn.titleLabel?.font = .systemFont(ofSize: 16, weight: .semibold)
            btn.addTarget(self, action: #selector(SaveBtnTapped(_:)), for: .touchUpInside)
            cell?.selectionStyle = .none
            return cell ?? UITableViewCell()
        }
        return UITableViewCell()
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        switch indexPath.section {
        case 0:
            return 350.0
        case 1:
            return 160.0
        case 2:
            if indexPath.row == 0{
                if quotationViewData?.show_comments == "1"{
                    return 120
                }
                return 0.0
            }else{
                return 45
            }
        case 3:
            return 140
        default:
            if quotationViewData?.save_button == "1"{
            return 50
            }else{
                return 0
            }
        }
    }
    @objc func openDropdown(_ sender:UIButton){
        let dropDown = DropDown();
        dropDown.dataSource = Array(productNameDropDown.keys)
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: .normal)
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            _ = dropDown.show()
        } else {
            dropDown.hide();
        }
        
    }
    @objc func urlTapped(_ gesture:UIButton){
        print("Tapped")
        let view = UIStoryboard(name: "Main", bundle: nil)
        let vc = view.instantiateViewController(withIdentifier: "cmsWebView") as! cedMageCmsWebView
        var url = quotationViewData?.quotations_detail.documents_file ?? ""
        
        vc.pageUrl = url
        self.navigationController?.pushViewController(vc, animated: true)
    }
    @objc func SaveBtnTapped(_ sender:UIButton){

        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let cell1 = tableView.cellForRow(at: IndexPath(row: 0, section: 1)) as! ced_NegotiationViewTC
        let cell2 = tableView.cellForRow(at: IndexPath(row: 0, section: 2)) as! ced_quoteCommentTC
        
        if cell1.ProductRequestValue.currentTitle == "Please Select Product"{
            self.view.makeToast("Please Select Product!", duration: 2.0, position: .center)
            return
        }
        let params = ["request_id":self.requestID,"vendor_id":vendorId,"product_id":productNameDropDown[cell1.ProductRequestValue.currentTitle!] ?? "","comment":cell2.commentView.text ?? "","nqty":cell1.QuantityValue.text!,"nprice":cell1.priceValue.text!]
        print(params)
        sendPORequest(url: "vpoapi/saveQuote", params: params)
    }
    
    @objc func AcceptBtnTapped(_ sender:UIButton){
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let cell1 = tableView.cellForRow(at: IndexPath(row: 0, section: 1)) as! ced_NegotiationViewTC
        if cell1.ProductRequestValue.currentTitle!.lowercased() == "please select product"{
            self.view.makeToast("Select Product First!")
            return
        }
        let params = ["request_id":requestID,"vendor_id":vendorId,"status_id":quotationViewData?.status_id ?? "","product_id":productNameDropDown[cell1.ProductRequestValue.currentTitle!] ?? "","customer_id":quotationViewData?.quotations_detail.customer_id ?? ""]
        sendPORequest(url: "vpoapi/acceptQuote", params: params)
    }
    @objc func declineButtonTapped(_ sender: UIButton){
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let params = ["request_id":requestID,"vendor_id":vendorId]
        sendPORequest(url: "vpoapi/declineQuote", params: params)
    }

}
extension Dictionary where Value: Equatable {
    func someKey(forValue val: Value) -> Key? {
        return first(where: { $1 == val })?.key
    }
}
