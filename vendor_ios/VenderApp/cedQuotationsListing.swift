//
//  cedQuotationsListing.swift
//  VenderApp
//
//  Created by cedcoss on 16/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedQuotationsListing: ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource, UIGestureRecognizerDelegate {
    
    @IBOutlet weak var headingLabel: UILabel!
    @IBOutlet weak var mainTable: UITableView!
    var loading = true;
    @IBOutlet weak var filterButton: UIButton!
    var quotationData = [[String:String]]()
    var filter = String();
    var id = "";
    var estimatedQty = "";
    var email = "";
    var productName = "";
    var requestedQty = "";
    var storeUrl = "";
    var itemUrl = "";
    var requestedPrice = "";
    var page = 1;
    var status = ""
    var dropDownData = [String]()
    var dropDownkey = [String]()
    var statusArray = [String:String]()
    var tempView1=UIView()
    override func viewDidLoad() {
        super.viewDidLoad()
        headingLabel.backgroundColor = DynamicColor.secondaryColor//setThemeColor()
        headingLabel.text = "Quotations List"
        headingLabel.font = UIFont.systemFont(ofSize: 16, weight: .semibold)
        mainTable.delegate = self;
        mainTable.dataSource = self;
        filterButton.addTarget(self, action: #selector(filterButtonPressed(_:)), for: .touchUpInside);
        loadData();
    }

    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json);
            if(requestUrl == "vpoapi/getQuoteList")//"vpoapi/quotations/item")
            {
                if(json["vendor_data"]["success"].stringValue == "true")
                {
                    for index in json["vendor_data"]["quote_list"].arrayValue{
                        quotationData.append(["id":index["id"].stringValue, "request_id":index["request_id"].stringValue,"customer_name":index["customer_name"].stringValue,"customer_email":index["customer_email"].stringValue,"product_name":index["product_name"].stringValue,"requested_qty":index["requested_qty"].stringValue,"store_url":index["store_url"].stringValue,"item_url":index["item_url"].stringValue,"requested_price":index["requested_price"].stringValue,"status":index["status"].stringValue,"estimated_quantity":index["estimated_quantity"].stringValue,"price_per_quantity":index["price_per_quantity"].stringValue,"action":index["action"].stringValue])
                    }
                    headingLabel.text = " Quotations List (\(quotationData.count))"
                    self.statusArray.removeAll()
                    self.dropDownkey.removeAll()
                    self.dropDownData.removeAll()
                    for values in json["vendor_data"]["status_label"].arrayValue
                    {
                        self.dropDownData.append(values["label"].stringValue)
                        self.dropDownkey.append(values["value"].stringValue)
                        self.statusArray[values["value"].stringValue] = values["label"].stringValue
                    }
                    print(self.statusArray)
                }
                else
                {
                    if(page == 1)
                    {
                       // Alert_File.showValidationError(self,title:"",message:json["vendor_data"]["message"].stringValue);
                        self.mainTable.setEmptyMessage(json["vendor_data"]["message"].stringValue)
                    }
                    loading = false;
                    return
                }
                mainTable.restore()
                mainTable.reloadData();
            }
        }
    }
    
    
    @objc func loadData()
    {
        var postData = [String:String]()
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
//        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey + "&page=\(page)";
//        if(filter != "")
//        {
//            postString += "&filter="+filter;
//        }
        postData["vendor_id"] =  vendorId
        //postData["hashkey"] = hashKey
        postData["page"] = "\(page)"
        if(filter != "")
                {
                   postData["filter"] = filter;
                }
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
       // self.sendRequest(url: "vpoapi/getQuoteList", parameters: postString)//"vpoapi/quotations/item"
        self.sendPORequest(url: "vpoapi/getQuoteList", params: postData)
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return quotationData.count;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        if let cell = tableView.dequeueReusableCell(withIdentifier: "cedQuotationsListCell", for: indexPath) as? cedQuotationsListCell
        {
            let data = quotationData[indexPath.row];
            cell.idLabel.text = "ID : "+data["request_id"]!;
            cell.idLabel.backgroundColor = DynamicColor.secondaryColor
            cell.emailLabel.text = data["customer_email"];
            cell.productNameLabel.text = data["product_name"];
            cell.requestedQtyLabel.text = data["estimated_quantity"];
            cell.statusLabel.text = data["status"];
            cell.actionbtn.setTitle(data["action"], for: .normal)
            cell.estimatedBudgetPerQty.text = data["price_per_quantity"];
            cell.actionbtn.tag = indexPath.row
            cell.actionbtn.addTarget(self, action: #selector(editButtonTapped(_:)), for: .touchUpInside)
            cell.cellView.cardView();
            return cell;
        }
        return UITableViewCell();
    }
    
    @objc func editButtonTapped(_ sender:UIButton){
//        if let vc = self.storyboard?.instantiateViewController(withIdentifier: "cedPurchaseViewQuotations") as? cedPurchaseViewQuotations
//        {
//            vc.quoteId = quotationData[sender.tag]["id"]!;
//        }
        self.navigationController?.pushViewController(ced_QuotationViewController(requestId: quotationData[sender.tag]["request_id"]!), animated: true);
    }
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 310;
    }
    
    @objc func filterButtonPressed(_ sender:UIButton)
    {
        print("filterButtonPressed");
        /* background view */
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let dealFilterView = QuotationListingFilterView();
        dealFilterView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width - 20, height: 370);
        dealFilterView.center.x = self.view.center.x;
        dealFilterView.center.y = self.view.center.y+20;
        dealFilterView.tag = 181;
        self.addgesturesTohideView(self.view);
        dealFilterView.headingLabel.backgroundColor = color;
        dealFilterView.filterButton.backgroundColor = color;
        dealFilterView.clearButton.backgroundColor = color;
        dealFilterView.filterButton.setTitle("Filter", for: UIControl.State());
        dealFilterView.status.addTarget(self, action: #selector(self.showstatus(sender:)), for: .touchUpInside)
        
        dealFilterView.filterButton.addTarget(self, action: #selector(applyFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        dealFilterView.idLabel.text = id;
        dealFilterView.estimatedBudgetLabel.text = estimatedQty;
        dealFilterView.emailLabel.text = email;
        dealFilterView.productNameLabel.text = productName;
        
        //dealFilterView.statusLabel.text = status;
        
        dealFilterView.requestedQtyLabel.text = requestedQty;
        //dealFilterView.requestedPriceLabel.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
        dealFilterView.requestedQtyLabel.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
        dealFilterView.emailLabel.addTarget(self, action:#selector(isValidEmil(_:)), for: .editingDidEnd);
        dealFilterView.clearButton.setTitle("Reset", for: UIControl.State());
        dealFilterView.clearButton.addTarget(self, action: #selector(resetFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        bgCView.addSubview(dealFilterView)
        self.view.addSubview(bgCView);
    }
    @objc func showstatus(sender:UIButton){
        let dropDown = DropDown();
        dropDown.dataSource = dropDownData
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: .normal);
            
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    @objc func hideView(recognizer: UITapGestureRecognizer)
    {
        self.view.viewWithTag(151)?.removeFromSuperview();
        tempView1.removeFromSuperview()
    }
    

    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    @objc func hideView(_ recognizer: UITapGestureRecognizer){
        self.view.viewWithTag(121)?.removeFromSuperview()
        self.view.viewWithTag(151)?.removeFromSuperview()
    }
    
    @objc func applyFilterTapped(_ sender:UIButton)
    {
        print("applyFilterTapped");
        if let view = self.view.viewWithTag(181) as? QuotationListingFilterView
        {
            if(view.idLabel.text != nil){
                id = view.idLabel.text!;
            }
            if(view.productNameLabel.text != nil){
                productName = view.productNameLabel.text!;
            }
            if(view.emailLabel.text != nil){
                email = view.emailLabel.text!;
            }
            if(view.estimatedBudgetLabel.text != nil){
                estimatedQty = view.estimatedBudgetLabel.text!;
            }
            if(view.requestedQtyLabel.text != nil){
                requestedQty = view.requestedQtyLabel.text!;
            }
           // if(view.requestedPriceLabel.text != nil){
//                requestedPrice = view.requestedPriceLabel.text!;
//            }
            if(view.status.currentTitle != nil)
            {
                status = calculate_Status_Stringvalue(status: view.status.currentTitle!) ;
            }
           // if(view.itemUrlLabel.text != nil){
//                itemUrl = view.itemUrlLabel.text!;
//            }
            filter = "";
            filter += "{";
            filter += "\""+"customer_email"+"\":\""+email+"\",";
            filter += "\""+"product_name"+"\":\""+productName+"\",";
            filter += "\""+"estimated_quantity"+"\":\""+requestedQty+"\",";
            filter += "\""+"status"+"\":\""+status+"\",";
            //filter += "\""+"item_url"+"\":\""+itemUrl+"\",";
            filter += "\""+"price_per_quantity"+"\":\""+estimatedQty+"\",";
            filter += "\""+"request_id"+"\":\""+id+"\"}";//"\",";
            //filter += "\""+"price"+"\":\""+requestedPrice+;
            print(filter);
            self.view.viewWithTag(151)?.removeFromSuperview();
            quotationData = [[String:String]]();
            self.loadData();
        }
    }
    @objc func calculate_Status_Stringvalue(status:String) -> String
    {
        var valuestatus = ""
        print(status)
        for item in 0 ..< dropDownData.count
        {
            if dropDownData[item] == status
            {
                valuestatus = dropDownkey[item]
                print(valuestatus)
                return valuestatus
            }
        }
        return ""
    }
    @objc func resetFilterTapped(_ sender:UIButton){
        filter = String();
        id = "";
        estimatedQty = "";
        email = "";
        productName = "";
        requestedQty = "";
        storeUrl = "";
        itemUrl = "";
        requestedPrice = "";
        page = 1;
        status = ""
        quotationData = [[String:String]]();
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.loadData();
    }
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
                self.page += 1;
                self.loadData()
                mainTable.reloadData();
            }
        }
    }
    
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        if touch.view!.isDescendant(of: mainTable)
        {
            return false;
        }
        if let innerView = self.view.viewWithTag(181) as? QuotationListingFilterView {
            if(touch.view!.isDescendant(of: innerView) )
            {
                return false;
            }
            return true;
        }
        return true;
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}
