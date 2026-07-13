//
//  cedAssignedQuotations.swift
//  VenderApp
//
//  Created by cedcoss on 16/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedAssignedQuotations: ced_VendorBaseClass, UITableViewDataSource, UITableViewDelegate, UIGestureRecognizerDelegate {

    @IBOutlet weak var headingLabel: UILabel!
    @IBOutlet weak var filterButton: UIButton!
    
    @IBOutlet weak var mainTable: UITableView!
    
    var filter = String();
    
    var quotationData = [[String:String]]();
    var loading = true;
    var requestId = "";
    var customerName = "";
    var email = "";
    var productName = "";
    var requestedQty = "";
    var storeUrl = "";
    var CatId = "";
    var requestedPrice = "";
    var estimatedBudgetQty = "";
    var approveQty = "";
    var status = "";
    var page = 1;
    var dropDownData = [String]()
    var dropDownkey = [String]()
    var statusArray = [String:String]()
    var tempView1=UIView()
    override func viewDidLoad() {
        super.viewDidLoad()
        headingLabel.backgroundColor = DynamicColor.secondaryColor
        headingLabel.text = " Assigned Quotations"
        headingLabel.font = UIFont.systemFont(ofSize: 16, weight: .semibold)
        mainTable.delegate = self;
        mainTable.dataSource = self;
        filterButton.addTarget(self, action: #selector(filterButtonPressed(_:)), for: .touchUpInside)
        loadData();
        // Do any additional setup after loading the view.
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
        // self.sendRequest(url: "vpoapi/assign/item", parameters: postString)
        self.sendPORequest(url: "vpoapi/getAssignedQuoteList", params: postData)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json);
            if(requestUrl == "vpoapi/getAssignedQuoteList")
            {
                if(json["vendor_data"]["success"].stringValue == "true")
                {
                    for index in json["vendor_data"]["assigned_quote_list"].arrayValue
                    {
                        quotationData.append(["product_name":index["product_name"].stringValue,"proposed_qty":index["proposed_qty"].stringValue,"request_id":index["request_id"].stringValue,"preferred_price_per_qty":index["preferred_price_per_qty"].stringValue,"category_id":index["category_id"].stringValue,"negotiated_final_qty":index["negotiated_final_qty"].stringValue,"negotiated_final_price":index["negotiated_final_price"].stringValue,"status":index["status"].stringValue,"id":index["id"].stringValue,"customer_email":index["customer_email"].stringValue])
                    }
                    headingLabel.text = " Assigned Quotations (\(quotationData.count))"
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
                       // Alert_File.showValidationError(self,title:"Error",message:json["vendor_data"]["message"].stringValue);
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
    
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return quotationData.count;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if let cell = tableView.dequeueReusableCell(withIdentifier: "cedAssignedQuotationsCell", for: indexPath) as? cedAssignedQuotationsCell
        {
            
            let data = quotationData[indexPath.row];
            cell.idLabel.text = "ID : \(data["id"] ?? "")"  ;
            cell.idLabel.backgroundColor = DynamicColor.secondaryColor
            cell.idLabel.layer.cornerRadius = 8.0
            cell.emailLabel.text = data["customer_email"];
            cell.productNameLabel.text = data["product_name"];
            cell.requestedQtyLabel.text = data["requested_qty"];
            cell.approvedQty.text = data["negotiated_final_qty"];
            cell.categoryIdLabel.text = data["category_id"];
            cell.estimatedBudgetLbl.text = data["preferred_price_per_qty"]
            cell.approvedEstimated.text = data["negotiated_final_price"]
            cell.requestedQtyLabel.text = data["proposed_qty"]
//            cell.invoiceStatusLabel.text = data["invoice_status"];
//            cell.offerStatusLabel.text = data["invoice_accepted"];
//            cell.requestedPriceLabel.text = data["requested_price"];
            cell.statusLabel.text = data["status"];
            cell.cellView.cardView();
            return cell;
        }
        return UITableViewCell();
    }
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
//        if let vc = self.storyboard?.instantiateViewController(withIdentifier: "cedAssignedQuotationView") as? cedAssignedQuotationView{
//            vc.quoteId = quotationData[indexPath.row]["id"]!;
//            self.navigationController?.pushViewController(vc, animated: true);
//        }
    }
    
    
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 300;
    }
    
    @objc func filterButtonPressed(_ sender:UIButton)
    {
        print("filterButtonPressed");
        /* background view */
        
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        
        let bgCView = UIView();
        
        tempView1 = bgCView
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let dealFilterView = AssignedQuotationFilterView();
        dealFilterView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width - 20, height: 480);
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
        dealFilterView.requestedId.text = requestId;
     //  dealFilterView.customerNameTextField.text = customerName;
        dealFilterView.customerEmail.text = email;
        dealFilterView.requestedProductName.text = productName;
        dealFilterView.categoryId.text = CatId;
        dealFilterView.estimatedBudgetPerQty.text = estimatedBudgetQty;
        dealFilterView.approveQty.text = approveQty;
        dealFilterView.requestedQty.text = requestedQty;
       // dealFilterView.status.currentTitle = status;
        
//        dealFilterView.requestedPriceTextField.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
        
        dealFilterView.requestedQty.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
        
          dealFilterView.customerEmail.addTarget(self, action:#selector(isValidEmil(_:)), for: .editingDidEnd);
        
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
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    @objc func hideView(_ recognizer: UITapGestureRecognizer)
    {
        self.view.viewWithTag(121)?.removeFromSuperview()
        self.view.viewWithTag(151)?.removeFromSuperview();
        
    }
    
    @objc func applyFilterTapped(_ sender:UIButton)
    {
        print("applyFilterTapped");
        
        if let view = self.view.viewWithTag(181) as? AssignedQuotationFilterView
        {
            if(view.requestedId.text != nil){
                requestId = view.requestedId.text!;
            }
            if(view.requestedProductName.text != nil){
                productName = view.requestedProductName.text!;
            }
            if(view.customerEmail.text != nil){
                email = view.customerEmail.text!;
            }
            
            if(view.requestedQty.text != nil){
                requestedQty = view.requestedQty.text!;
            }
            if(view.estimatedBudgetPerQty.text != nil){
                estimatedBudgetQty = view.estimatedBudgetPerQty.text!;
            }
            if(view.approveQty.text != nil){
                approveQty = view.approveQty.text!;
            }
            if view.categoryId.text != nil{
                CatId = view.categoryId.text!;
            }
            if(view.status.currentTitle != nil)
            {
                status = calculate_Status_Stringvalue(status: view.status.currentTitle!) ;
            }
            filter = "";
            filter += "{";
            filter += "\""+"customer_email"+"\":\""+email+"\",";
            filter += "\""+"product_name"+"\":\""+productName+"\",";
            filter += "\""+"proposed_qty"+"\":\""+requestedQty+"\",";
            filter += "\""+"negotiated_final_qty"+"\":\""+approveQty+"\",";
            filter += "\""+"preferred_price_per_qty"+"\":\""+estimatedBudgetQty+"\",";
            filter += "\""+"request_id"+"\":\""+requestId+"\",";
            filter += "\""+"status"+"\":\""+status+"\",";
            filter += "\""+"category_id"+"\":\""+CatId+"\"}";
            
            print(filter);
            self.view.viewWithTag(151)?.removeFromSuperview();
            //self.page = 1;
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
    @objc func resetFilterTapped(_ sender:UIButton)
    {
        print("resetFilterTapped");
        filter = String();
        requestId = "";
       // customerName = "";
        email = "";
        productName = "";
        storeUrl = "";
        requestedQty = "";
        requestedPrice = "";
        estimatedBudgetQty = "";
        approveQty = "";
        CatId = ""
        status = "";
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
        //gestureRecognizer.delegate = self;
        if touch.view!.isDescendant(of: mainTable)
        {
            return false;
        }
        if let innerView = self.view.viewWithTag(181) as? AssignedQuotationFilterView {
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
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
