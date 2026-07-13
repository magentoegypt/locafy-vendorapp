//
//  cedCreateDeal.swift
//  VenderApp
//
//  Created by cedcoss on 04/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedCreateDeal: ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource {

    var datePickerView = UIDatePicker();
    let screenSize: CGRect = UIScreen.main.bounds;
    var selectedDealStatus = "";
    var productData = [String:String]()
    var productStatus = [String:String]()
    var dropDown = DropDown()
    var pickerButtonTag = 0;
    var editCheck = false;
    
    @IBOutlet weak var mainTable: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        mainTable.delegate = self;
        mainTable.dataSource = self;
        
        // Do any additional setup after loading the view.
    }

    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if let cell = tableView.dequeueReusableCell(withIdentifier: "cedDealCell", for: indexPath) as? cedDealCell
        {
            if(editCheck)
            {
                cell.deleteButton.isHidden = false;
                cell.headingLabel.text = "Edit Deal";
                
                cell.createDealButton.setTitle("Update", for: .normal)
            }
            cell.headingView.setThemeColor()
            cell.deleteButton.setThemeColor()
            cell.createDealButton.setThemeColor()
            cell.backButton.setThemeColor();
            cell.createDealButton.addTarget(self, action: #selector(addDealClicked(_:)), for: .touchUpInside)
            cell.productName.text = productData["product_name"];
            cell.productId.text = productData["product_id"];
      
            if let startDate = productData["start_date"]
            {
                cell.dealFrom.setTitle(startDate, for: .normal);
            }
            if let endDate = productData["end_date"]
            {
                cell.dealTo.setTitle(endDate, for: .normal);
            }
//            cell.dealStatus.setTitle("Enabled", for: .normal);
//            if let status = productData["status"]
//            {
//                if(status != "")
//                {
//                    cell.dealStatus.setTitle(status.capitalizingFirstLetter(), for: .normal);
//                }
//            }
            if editCheck{
                if let dealPrice = productData["deal_price"],let currency = productData["currency_code"]
                {
                    cell.dealPrice.text = dealPrice.replacingOccurrences(of: currency, with: "")
                }
            }else{
                cell.dealPrice.text = ""
            }
            
            
            if !editCheck{
                cell.dealStatus.setTitle(productStatus.keys.first, for: .normal)
            }else{
                if let status = productData["status"]{
                    cell.dealStatus.setTitle(productStatus.someKey(forValue: status), for: .normal)
                }
            }
            
            cell.dealStatus.addTarget(self, action: #selector(dealStatusDropDownClicked(_:)), for: .touchUpInside)
            
            cell.dealFrom.tag = 1001;
            cell.dealTo.tag = 1002;
            cell.tag = 1000;
            cell.dealFrom.addTarget(self, action: #selector(selectDateClicked(_:)), for: .touchUpInside);
            cell.dealTo.addTarget(self, action: #selector(selectDateClicked(_:)), for: .touchUpInside);
            cell.backButton.addTarget(self, action: #selector(backButtonClicked(_:)), for: .touchUpInside)
            cell.deleteButton.addTarget(self, action: #selector(deleteDealClicked(_:)), for: .touchUpInside)
            return cell;
        }
        return UITableViewCell();
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            if(requestUrl == "vdealapi/deal/create")
            {
                if(json["data"]["success"].stringValue == "true")
                {
                    Alert_File.showValidationError(self,title:"Success",message:json["data"]["message"].stringValue);
                }
                else
                {
                    Alert_File.showValidationError(self,title:"Error",message:json["data"]["message"].stringValue);
                }
            }
            else if(requestUrl == "vdealapi/deal/delete")
            {
                if(json["data"]["success"].stringValue == "true")
                {
                    Alert_File.showValidationError(self,title:"Success",message:json["data"]["message"].stringValue);
                }
                else
                {
                    Alert_File.showValidationError(self,title:"Error",message:json["data"]["message"].stringValue);
                }
            }
            else if(requestUrl == "vdealapi/deal/update")
            {
                print(json)
                if(json["data"]["success"].stringValue == "true")
                {
                    Alert_File.showValidationError(self,title:"Success",message:json["data"]["message"].stringValue);
                }
                else
                {
                    Alert_File.showValidationError(self,title:"Error",message:json["data"]["message"].stringValue);
                }
            }
            
        }
    }
    @objc func backButtonClicked(_ sender: UIButton)
    {
        self.navigationController?.popViewController(animated: true);
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 720
    }
    
    @objc func deleteDealClicked(_ sender: UIButton)
    {
        var params = "";
        if let dealId = productData["deal_id"]
        {
            params += "deal_id="+dealId;
        }
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        params += "&vendor_id="+vendorId;
        params += "&hashkey="+hashKey;
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: "vdealapi/deal/delete", parameters: params)
    }
    
    @objc func addDealClicked(_ sender: UIButton)
    {
        if let cell = self.view.viewWithTag(1000) as? cedDealCell
        {
            let dealStatusLbl = cell.dealStatus.currentTitle ?? ""
            let dealStatus = self.productStatus[dealStatusLbl] ?? ""
            var params = "";
            params += "status="+dealStatus
            params += "&product_name="+cell.productName.text!;
            params += "&product_id="+cell.productId.text!;
            params += "&deal_price="+cell.dealPrice.text!;
            if(cell.dealFrom.currentTitle == "-----Select-----")
            {
                self.view.makeToast("Please Select Start Date", duration: 2.0, position: .center)
                return;
            }
            params += "&start_date="+cell.dealFrom.currentTitle!//.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!;
            /*else
            {
                params += "&start_date="+"";
            }*/
            if(cell.dealTo.currentTitle == "-----Select-----")
            {
                self.view.makeToast("Please Select End Date", duration: 2.0, position: .center)
                return;
                
            }
            params += "&end_date="+cell.dealTo.currentTitle!//.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!;
            /*else
            {
                params += "&end_date="+"";
            }*/
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            params += "&vendor_id="+vendorId;
            params += "&hashkey="+hashKey;
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
            if(editCheck)
            {
                if let dealId = productData["deal_id"]
                {
                    params += "&deal_id="+dealId;
                }
                print(params)
                self.sendRequest(url: "vdealapi/deal/update", parameters: params)
            }
            else
            {
                self.sendRequest(url: "vdealapi/deal/create", parameters: params)
            }
            
        }
    }
    
    @objc func dealStatusDropDownClicked(_ sender: UIButton)
    {
        var tempDataSource = Array(productStatus.keys)
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
    
    @objc func selectDateClicked(_ sender: UIButton)
    {
        pickerButtonTag = sender.tag;
        print(pickerButtonTag)
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
        
        
        if pickerButtonTag == 1002
        {
            if   sender.currentTitle != "-----Select-----"
            {
                // print(filterView.value21.text)
                if let fromDateButton = self.view.viewWithTag(1001) as? UIButton
                {
                    let isoDate = fromDateButton.currentTitle!;
                    if(isoDate != "-----Select-----")
                    {
                        let dateFormatter = DateFormatter()
                        dateFormatter.dateFormat = "yyyy-MM-dd"
                        dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
                        let date = dateFormatter.date(from:isoDate)!
                        let calendar = Calendar.current
                        let components = calendar.dateComponents([.year, .month, .day, .hour], from: date)
                        let finalDate = calendar.date(from:components)
                        print("en_US_POSIX\(finalDate)")
//                        datePickerView.minimumDate = finalDate
//                        datePickerView.maximumDate = nil
                    }
                    
                    
                }
                
            }
            
            
        }
        if pickerButtonTag == 1001
        {
            if   sender.currentTitle != "-----Select-----"
            {
                // print(filterView.value21.text)
                if let toDateButton = self.view.viewWithTag(1002) as? UIButton
                {
                    let isoDate = toDateButton.currentTitle!;
                    if(isoDate != "-----Select-----")
                    {
                        let dateFormatter = DateFormatter()
                        dateFormatter.dateFormat = "yyyy-MM-dd"
                        dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
                        let date = dateFormatter.date(from:isoDate)!
                        let calendar = Calendar.current
                        let components = calendar.dateComponents([.year, .month, .day, .hour], from: date)
                        let finalDate = calendar.date(from:components)
//                        datePickerView.maximumDate = finalDate
//                        datePickerView.minimumDate = nil;
                    }
                    
                    
                }
                
            }
            
            
        }
        
        //datePickerView.maximumDate = Date();
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
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @objc func cancelDatePickerView(_ sender:UIButton)
    {
        self.view.viewWithTag(121)?.removeFromSuperview();
    }
    
    
    @objc func pickDateFromDatePickerView(_ sender:UIButton)
    {
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = DateFormatter.Style.short
        dateFormatter.timeStyle = DateFormatter.Style.none
        if let button = self.view.viewWithTag(pickerButtonTag) as? UIButton
        {
            dateFormatter.dateFormat = "yyyy-MM-dd"
            dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
            button.setTitle(dateFormatter.string(from: datePickerView.date), for: .normal);
        }
        self.view.viewWithTag(121)?.removeFromSuperview();
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
extension String {
    func capitalizingFirstLetter() -> String {
        return prefix(1).uppercased() + dropFirst()
    }
    
    mutating func capitalizeFirstLetter() {
        self = self.capitalizingFirstLetter()
    }
}
