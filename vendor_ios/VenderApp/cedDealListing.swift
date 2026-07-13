//
//  cedDealListing.swift
//  VenderApp
//
//  Created by cedcoss on 05/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedDealListing: ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource, UIGestureRecognizerDelegate,UITextFieldDelegate {

    @IBOutlet weak var headingLabel: UILabel!
    @IBOutlet weak var filterButton: UIButton!
    var datePickerView = UIDatePicker();
    let screenSize: CGRect = UIScreen.main.bounds;
     let dealFilterView = DealListFilterView();
    var heightToUse : CGFloat = 0;
    var globalTextFieldTag = 0;
    var dropDown = DropDown();
    var page = 1;
    var loading = true;
    var filter = String();
    var dealId = "";
    var productName = "";
    var dealPriceFrom = "";
    var dealPriceTo = "";
    var dealEndFrom = "";
    var dealEndTo = "";
    var dealStatus = "";
    var adminStatus = "";
    var dealStatusArray = [String:String]();
    var adminStatusArray = [String:String]();
    @IBOutlet weak var mainTable: UITableView!
    var dealsData = [[String:String]]();
    
    override func viewDidLoad() {
        super.viewDidLoad()
        headingLabel.setThemeColor();
        headingLabel.text = "  Vendor Deals".localized
        mainTable.delegate = self;
        mainTable.dataSource = self;
        filterButton.addTarget(self, action: #selector(filterButtonPressed(_:)), for: .touchUpInside)
        loadProducts()
        

        // Do any additional setup after loading the view.
    }

    @objc func loadProducts()
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey+"&page=\(page)";
        if(filter != "")
        {
            postString += "&filter="+filter;
        }
        print(postString)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        self.sendRequest(url: "vdealapi/deal/item", parameters: postString)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingpost(self);
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if(page == 1)
            {
                for index in json["data"]["admin_status"].arrayValue
                {
                    adminStatusArray[index["value"].stringValue] = index["key"].stringValue;
                }
                for index in json["data"]["deal_status"].arrayValue
                {
                    dealStatusArray[index["value"].stringValue] = index["key"].stringValue;
                }
            }
            if(json["data"]["success"].stringValue == "true")
            {
                for deals in json["data"]["vendordeal_list"].arrayValue
                {
                    dealsData.append(["deal_id":deals["deal_id"].stringValue,"product_id":deals["product_id"].stringValue,"product_name":deals["product_name"].stringValue,"days":deals["days"].stringValue,"specificdays":deals["specificdays"].stringValue,"start_date":deals["start_date"].stringValue,"end_date":deals["end_date"].stringValue,"vendor_id":deals["vendor_id"].stringValue,"admin_status":deals["admin_status"].stringValue,"status":deals["status"].stringValue,"deal_price":deals["deal_price"].stringValue,"currency_code":deals["currency_code"].stringValue])
                }
                
            }
            else
            {
                if(page == 1)
                {
                    dealsData.removeAll()
                    mainTable.reloadData()
                   // Alert_File.showValidationError(self,title:"Error",message:json["data"]["message"].stringValue);
                    self.mainTable.setEmptyMessage(json["data"]["message"].stringValue)
                }
                loading = false;
                return
            }
            mainTable.restore()
            mainTable.reloadData()
        }
    }
    
    @objc func editButtonClicked(_ sender: UIButton)
    {
        let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedCreateDeal") as! cedCreateDeal
        viewControl.productData = dealsData[sender.tag];
        viewControl.editCheck = true;
        viewControl.productStatus = dealStatusArray
        self.navigationController?.pushViewController(viewControl, animated: true)
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dealsData.count;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if let cell = tableView.dequeueReusableCell(withIdentifier: "cedDealListCell", for: indexPath) as? cedDealListCell
        {
            
            cell.dealIdLbl.text = "Deal Id".localized
            cell.prodNameLbl.text = "Product Name".localized
            cell.dealPriceLbl.text = "Deal Price".localized
            cell.dealEndLbl.text = "Deal End".localized
            cell.dealStatusLbl.text = "Deal Status".localized
            cell.adminStatusLbl.text = "Admin Status".localized
            cell.editButton.setTitle("Edit Deal".localized, for: .normal)
            let deal = dealsData[indexPath.row]
            cell.dealId.text = deal["deal_id"];
            cell.productName.text = deal["product_name"];
            cell.dealPrice.text = deal["deal_price"];
            cell.dealStatus.text = dealStatusArray.someKey(forValue: deal["status"] ?? "") ?? ""
            cell.dealEnd.text = deal["end_date"];
            let adminStatus = adminStatusArray.someKey(forValue: deal["admin_status"] ?? "") ?? ""
            cell.adminStatus.text = adminStatus
//            if(deal["admin_status"] == "1")
//            {
//                cell.adminStatus.text = "Approved".localized;
//            }
//            else if(deal["admin_status"] == "2")
//            {
//                cell.adminStatus.text = "Approval Pending".localized;
//            }
//            else
//            {
//                cell.adminStatus.text = "Disapproved".localized;
//            }
            cell.editButton.tag = indexPath.row;
            cell.editButton.addTarget(self, action: #selector(editButtonClicked(_:)), for: .touchUpInside)
            cell.editButton.setThemeColor();
            cell.cellView.cardView();
            return cell;
        }
        return UITableViewCell();
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 280;
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
       
        dealFilterView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width - 20, height: 300);
        dealFilterView.center.x = self.view.center.x;
        dealFilterView.center.y = self.view.center.y+20;
        dealFilterView.tag = 181;
        self.addgesturesTohideView(self.view);
        dealFilterView.dealEndFrom.tag = 100000;
        dealFilterView.dealEndTo.tag = 100001;
        dealFilterView.dealEndFrom.delegate = self;
        dealFilterView.dealEndTo.delegate = self;
        dealFilterView.dealStatus.addTarget(self, action: #selector(dealStatusDropDownClicked(_:)), for: .touchUpInside);
        dealFilterView.adminStatus.addTarget(self, action: #selector(adminStatusDropDownClicked(_:)), for: .touchUpInside);
        dealFilterView.filterButton.setTitle("Filter".localized, for: UIControl.State());
        dealFilterView.filterButton.addTarget(self, action: #selector(applyFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        dealFilterView.dealId.text = dealId;
        dealFilterView.productName.text = productName;
        dealFilterView.dealEndFrom.text = dealEndFrom;
        dealFilterView.dealEndTo.text  = dealEndTo;
        dealFilterView.dealPriceFrom.text = dealPriceFrom;
        dealFilterView.dealPriceTo.text = dealPriceTo;
        
        dealFilterView.dealPriceFrom.addTarget(self, action: #selector(isValidNmber(_:)), for: .editingDidEnd)
         dealFilterView.dealPriceFrom.addTarget(self, action: #selector(isValidNmber(_:)), for: .editingDidEnd)
        
        
        
        
        if(adminStatus != "")
        {
            dealFilterView.adminStatus.setTitle(adminStatus, for: .normal)
        }
        if(dealStatus != "")
        {
            dealFilterView.dealStatus.setTitle(dealStatus, for: .normal);
        }
        dealFilterView.resetButton.setTitle("Reset".localized, for: UIControl.State());
        dealFilterView.resetButton.addTarget(self, action: #selector(resetFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        dealFilterView.headingLabel.setThemeColor()
        dealFilterView.resetButton.setThemeColor()
        dealFilterView.filterButton.setThemeColor()
        
        bgCView.addSubview(dealFilterView)
        self.view.addSubview(bgCView);
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
        
        if let view = self.view.viewWithTag(181) as? DealListFilterView
        {
            var admStatus = "";
            var statusOfDeal = "";
            if(view.dealId.text != nil){
                dealId = view.dealId.text!;
            }
            if(view.productName.text != nil){
                productName = view.productName.text!;
            }
            if(view.dealPriceFrom.text != nil){
                dealPriceFrom = view.dealPriceFrom.text!;
            }
            if(view.dealPriceTo.text != nil){
                dealPriceTo = view.dealPriceTo.text!;
            }
            if(view.dealEndFrom.text != nil){
                dealEndFrom = view.dealEndFrom.text!;
            }
            if(view.dealEndTo.text != nil){
                dealEndTo = view.dealEndTo.text!;
            }
            if(view.dealStatus.currentTitle != "-----Select-----".localized){
                dealStatus = view.dealStatus.currentTitle!;
                statusOfDeal = dealStatusArray[dealStatus]!
            }
            if(view.adminStatus.currentTitle != "-----Select-----".localized)
            {
                adminStatus = view.adminStatus.currentTitle!;

                admStatus = adminStatusArray[adminStatus]!;
                
            }
            
            
            filter = "";
            filter += "{";
            filter += "\""+"status"+"\":\""+statusOfDeal+"\",";
            filter += "\""+"deal_price"+"\":{\"from\":\""+dealPriceFrom+"\",\"to\":\""+dealPriceTo+"\"},";
            filter += "\""+"end_date"+"\":{\"from\":\""+dealEndFrom.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!+"\",\"to\":\""+dealEndTo.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!+"\"},";
            filter += "\""+"product_name"+"\":\""+productName+"\",";
            
            filter += "\""+"admin_status"+"\":\""+admStatus+"\",";
            
            
            filter += "\""+"deal_id"+"\":\""+dealId+"\"}";
            
            print(filter);
            self.view.viewWithTag(151)?.removeFromSuperview();
            self.page = 1;
            self.dealsData = [[String:String]]()
            self.loadProducts()
            
        }
        
    }
    
    @objc func resetFilterTapped(_ sender:UIButton)
    {
        print("resetFilterTapped");
        dealStatus = "";
        dealEndTo = "";
        dealEndFrom = "";
        dealPriceTo = "";
        dealPriceFrom = "";
        dealId = "";
        productName = "";
        adminStatus = "";
        filter = String();
        self.page = 1;
        dealsData = [[String:String]]();
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.loadProducts();
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
                
                self.page += 1;
                self.loadProducts()
                mainTable.reloadData();
                
            }
            
        }
    }

    @objc func textFieldDidBeginEditing(_ textField: UITextField)
    {
        textField.resignFirstResponder();
        
        globalTextFieldTag = textField.tag;
        
        /* background view */
        let bgCView = UIView();
        bgCView.tag=121;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        
        
        let colorGreen = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        let colorRed = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String;
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!);
        
        let tempInrView = UIView();
        tempInrView.tag=131;
        tempInrView.frame = CGRect(x: 0, y: CGFloat(0), width: screenSize.width-50,height: CGFloat(270));
        tempInrView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        tempInrView.backgroundColor = UIColor.white;
        tempInrView.center = CGPoint(x: bgCView.frame.size.width  / 2,
                                     y: bgCView.frame.size.height / 2);
        
        let label = UILabel();
        label.frame = CGRect(x: 0, y: CGFloat(0), width: tempInrView.frame.width,height: CGFloat(30));
        label.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        label.text = "Select Date".localized.uppercased();
        label.textAlignment = NSTextAlignment.center;
        label.textColor = UIColor.white;
        label.backgroundColor = color;
        tempInrView.addSubview(label);
        
        if globalTextFieldTag == 100001
        {
            if   dealFilterView.dealEndFrom.text != ""
            {
                // print(filterView.value21.text)
                let isoDate =  dealFilterView.dealEndFrom.text
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "yyyy-MM-dd"
                dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
                let date = dateFormatter.date(from:isoDate!)!
                let calendar = Calendar.current
                let components = calendar.dateComponents([.year, .month, .day, .hour], from: date)
                let finalDate = calendar.date(from:components)
                datePickerView.minimumDate = finalDate
                datePickerView.maximumDate = Date()
            }
            else
            {
                ShowSimpleAlert.showSimpleAlert(self, showTitle: "Information".localized, showMsg: "Please Select From date first".localized)
                return
            }
        }else if globalTextFieldTag == 100000
        {
            let isoDate =  dealFilterView.dealEndTo.text
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "yyyy-MM-dd"
            dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
            let date = dateFormatter.date(from:isoDate ?? "") ?? Date()
            let calendar = Calendar.current
            let components = calendar.dateComponents([.year, .month, .day, .hour], from: date)
            let finalDate = calendar.date(from:components)
            datePickerView.maximumDate = finalDate
            datePickerView.minimumDate = nil
        }
        
        datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
        datePickerView.datePickerMode = UIDatePicker.Mode.date;
        datePickerView.setValue(UIColor.black, forKeyPath: "textColor");
        datePickerView.backgroundColor = UIColor.white;
        datePickerView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        datePickerView.frame = CGRect(x: 0, y: CGFloat(30), width: tempInrView.frame.width,height: CGFloat(200));
        tempInrView.addSubview(datePickerView);
        
        
        let twoButtonView = TwoButtonView();
        twoButtonView.tag=141;
        twoButtonView.frame = CGRect(x: 0, y: CGFloat(230), width: tempInrView.frame.width,height: CGFloat(40));
        twoButtonView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        tempInrView.addSubview(twoButtonView);
        
        twoButtonView.cancelButton.addTarget(self, action: #selector(cancelDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.cancelButton.backgroundColor = colorRed;
        
        
        twoButtonView.doneButton.addTarget(self, action: #selector(pickDateFromDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.doneButton.backgroundColor = colorGreen;
        
        tempInrView.makeCardUsingThemeColor(tempInrView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
        
        bgCView.addSubview(tempInrView);
        self.view.addSubview(bgCView);
        
    }
    
    @objc func cancelDatePickerView(_ sender:UIButton)
    {
        if let textField = self.view.viewWithTag(globalTextFieldTag) as? UITextField
        {
            textField.resignFirstResponder();
        }
        self.view.viewWithTag(121)?.removeFromSuperview();
    }
    
    
    @objc func pickDateFromDatePickerView(_ sender:UIButton)
    {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateStyle = DateFormatter.Style.short
        dateFormatter.timeStyle = DateFormatter.Style.none
        if let textField = self.view.viewWithTag(globalTextFieldTag) as? UITextField
        {
            dateFormatter.dateFormat = "yyyy-MM-dd";
            textField.text = dateFormatter.string(from: datePickerView.date);
            textField.resignFirstResponder();
        }
        self.view.viewWithTag(121)?.removeFromSuperview();
    }
    
    @objc func dealStatusDropDownClicked(_ sender: UIButton)
    {
        var tempDataSource = Array(dealStatusArray.keys);
        print(tempDataSource);
        tempDataSource = tempDataSource.sorted { $0.localizedCaseInsensitiveCompare($1) == ComparisonResult.orderedAscending }
        dropDown.dataSource = tempDataSource;
        
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
        }
        
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            let _ = dropDown.show();
        } else {
            dropDown.hide();
        }
        
        
    }
    
    @objc func adminStatusDropDownClicked(_ sender: UIButton)
    {
        var tempDataSource = Array(adminStatusArray.keys);
        print(tempDataSource);
        tempDataSource = tempDataSource.sorted { $0.localizedCaseInsensitiveCompare($1) == ComparisonResult.orderedAscending }
        dropDown.dataSource = tempDataSource;
        
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
        }
        
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            let _ = dropDown.show();
        } else {
            dropDown.hide();
        }
        
        
    }
    
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        //gestureRecognizer.delegate = self;
        if touch.view!.isDescendant(of: mainTable)
        {
            return false;
        }
        if let innerView = self.view.viewWithTag(181) as? DealListFilterView {
            if(touch.view!.isDescendant(of: innerView) )
            {
                return false;
            }
            return true;
        }
        return true;
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
