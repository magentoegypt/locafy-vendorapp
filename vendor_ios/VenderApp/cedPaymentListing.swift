//
//  cedPaymentListing.swift
//  VenderApp
//
//  Created by cedcoss on 05/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedPaymentListing: ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate, UIGestureRecognizerDelegate {

    @IBOutlet weak var headingLabel: UILabel!
    let refreshControl = UIRefreshControl()
    let dealFilterView = PaymentReportFilterView();
    var datePickerView = UIDatePicker();
    let screenSize: CGRect = UIScreen.main.bounds;
    
    var heightToUse : CGFloat = 0;
    var globalTextFieldTag = 0;
    @IBOutlet weak var filterButton: UIButton!
    var loading = true;
    var page = 1;
    var filter = String();
    var orderDescription = "";
    var dateFrom = "";
    var dateTo = "";
    var transactionId = "";
    var amountFrom = "";
    var amountTo = "";
    @IBOutlet weak var mainTable: UITableView!
    var paymentData = [[String:String]]()
    var payment_status = [String:String]()
    override func viewDidLoad() {
        super.viewDidLoad()
        headingLabel.setThemeColor()
        headingLabel.text = "Payment Report".localized
        mainTable.register(newPaymentreportTc.self, forCellReuseIdentifier: newPaymentreportTc.reuseId)
        mainTable.estimatedRowHeight = 200
        mainTable.delegate = self;
        mainTable.dataSource = self;
        filterButton.addTarget(self, action: #selector(filterButtonPressed(_:)), for: .touchUpInside)
        loadProducts();
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        mainTable.refreshControl = refreshControl
        // Do any additional setup after loading the view.
    }
    
    @objc func refresh(_ sender: AnyObject) {
        self.page = 1
        self.paymentData.removeAll()
        mainTable.reloadData()
        loadProducts()
    }

    @objc func loadProducts()
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey + "&page=\(page)";
        if(filter != "")
        {
            postString += "&filter="+filter;
        }
        print(postString)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        //self.sendRequest(url: "vreportapi/product/payment", parameters: postString)
        var param = [String:String]()//["vendor_id":vendorId,"hashkey":hashKey,"page":"\(page)"]
        param["vendor_id"] = vendorId
        param["hashkey"] = hashKey
        param["page"] = "\(page)"
        if(filter != "")
        {
            param["filter"] = filter;
        }
        self.sendRequestWithData(url: "rest/V1/vreport/getPayment", params: param)

    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        refreshControl.endRefreshing()
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if(json["vendor_data"]["success"].stringValue == "true")
            {
                
                json["vendor_data"]["payment_status"].arrayValue.forEach{self.payment_status[$0["value"].stringValue] = $0["label"].stringValue }
                for payment in json["vendor_data"]["product"].arrayValue
                {
                    paymentData.append(["Order Description":payment["order_description"].stringValue,"Transaction Date":payment["transaction_date"].stringValue,"Transaction Id":payment["transaction_id"].stringValue,"Amount":payment["amount"].stringValue,"Status":payment["status"].stringValue]);
                }
                
            }
            else
            {
                if(page == 1)
                {
                    paymentData.removeAll()
                    mainTable.reloadData()
                   // Alert_File.showValidationError(self,title:"Error".localized,message:json["vendor_data"]["message"].stringValue);
                    self.mainTable.setEmptyMessage(json["vendor_data"]["message"].stringValue)
                }
                loading = false;
                return
            }
            mainTable.restore()
            mainTable.reloadData()
        }
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return paymentData.count
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
//        if let cell = tableView.dequeueReusableCell(withIdentifier: "cedPaymentListCell", for: indexPath) as? cedPaymentListCell{
            let payment = paymentData[indexPath.row];
//            cell.orderDescription.text = payment["Order Description"];
//            cell.creditAmount.text = payment["Amount"];
//            cell.status.text = self.payment_status[payment["Status"] ?? ""]
//            cell.transactionDate.text = payment["Transaction Date"];
//            cell.transactionId.text = payment["Transaction Id"];
//            cell.cellView.cardView();
//            return cell;
//        }
//        return UITableViewCell();
        let cell = tableView.dequeueReusableCell(withIdentifier: newPaymentreportTc.reuseId, for: indexPath) as! newPaymentreportTc
        cell.populate(with: payment, payment_status: payment_status)
        return cell
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
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
        
        dealFilterView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width - 20, height: 200);
        dealFilterView.center.x = self.view.center.x;
        dealFilterView.center.y = self.view.center.y+20;
        dealFilterView.tag = 181;
        dealFilterView.transactionDateFrom.tag = 88;
        dealFilterView.transactionTo.tag = 89;
        dealFilterView.transactionDateFrom.delegate = self;
        dealFilterView.transactionTo.delegate = self;
        self.addgesturesTohideView(self.view);
        dealFilterView.applyFilter.setTitle("Filter".localized, for: UIControl.State());
        dealFilterView.applyFilter.addTarget(self, action: #selector(applyFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        dealFilterView.creditAmountFrom.text = amountFrom;
        dealFilterView.creditAmountTo.text = amountTo;
        dealFilterView.transactionDateFrom.text = dateFrom;
        dealFilterView.transactionTo.text = dateTo;
        dealFilterView.transactionId.text = transactionId;
        dealFilterView.clearButton.setTitle("Reset".localized, for: UIControl.State());
        dealFilterView.clearButton.addTarget(self, action: #selector(resetFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        
        
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
        
        if let view = self.view.viewWithTag(181) as? PaymentReportFilterView
        {
            if(view.creditAmountFrom.text != nil)
            {
                amountFrom = view.creditAmountFrom.text!;
            }
            if(view.creditAmountTo.text != nil)
            {
                amountTo = view.creditAmountTo.text!;
            }
            if(view.transactionDateFrom.text != nil)
            {
                dateFrom = view.transactionDateFrom.text!;
            }
            if(view.transactionTo.text != nil)
            {
                dateTo = view.transactionTo.text!;
            }
            if(view.transactionId.text != nil)
            {
                transactionId = view.transactionId.text!;
            }
            
            filter = "";
            filter += "{";
            
            if((dateFrom != "" && dateTo == "") || (dateFrom == "" && dateTo != ""))
            {
                Alert_File.showValidationError(self,title:"Error".localized,message:"Please provide range for order date".localized)
                return;
            }
            else
            {
                filter += "\""+"created_at"+"\":{\"from\":\""+dateFrom.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!+"\",\"to\":\""+dateTo.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!+"\"},";
            }
            if((amountFrom != "" && amountTo == "") || (amountFrom == "" && amountTo != ""))
            {
                Alert_File.showValidationError(self,title:"Error".localized,message:"Please provide range for amount".localized)
                return;
            }
            else
            {
                filter += "\""+"amount"+"\":{\"from\":\""+amountFrom+"\",\"to\":\""+amountTo+"\"},";
            }
            filter += "\""+"transaction_id"+"\":\""+transactionId+"\"}";
            print(filter);
            self.view.viewWithTag(151)?.removeFromSuperview();
            self.page = 1;
            paymentData = [[String:String]]();
            self.loadProducts();
            
        }
        
    }
    
    @objc func resetFilterTapped(_ sender:UIButton)
    {
        print("resetFilterTapped");
        orderDescription = "";
        dateFrom = "";
        dateTo = "";
        transactionId = "";
        amountFrom = "";
        amountTo = "";
        filter = String();
        self.page = 1;
        paymentData = [[String:String]]();
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.loadProducts();
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
        
        
        
       
        if textField.tag == 89
        {
            if  dealFilterView.transactionDateFrom.text != ""
            {
               // print(filterView.value21.text)
                let isoDate = dealFilterView.transactionDateFrom.text
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "yyyy-MM-dd"
                dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
                let date = dateFormatter.date(from:isoDate!)!
                let calendar = Calendar.current
                let components = calendar.dateComponents([.year, .month, .day, .hour], from: date)
                let finalDate = calendar.date(from:components)
                datePickerView.minimumDate = finalDate
                datePickerView.maximumDate = Date();
            }
            else
            {
                ShowSimpleAlert.showSimpleAlert(self, showTitle: "Information".localized, showMsg: "please select the first limit of the date".localized)
                return
            }
        }else if textField.tag == 88
        {
            // print(filterView.value21.text)
             let isoDate = dealFilterView.transactionTo.text
             let dateFormatter = DateFormatter()
             dateFormatter.dateFormat = "yyyy-MM-dd"
             dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
             let date = dateFormatter.date(from:isoDate ?? "") ?? Date()
             let calendar = Calendar.current
             let components = calendar.dateComponents([.year, .month, .day, .hour], from: date)
             let finalDate = calendar.date(from:components)
             datePickerView.minimumDate = nil
             datePickerView.maximumDate = finalDate
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
            dateFormatter.dateFormat = "yyyy-MM-dd"
            textField.text = dateFormatter.string(from: datePickerView.date);
            textField.resignFirstResponder();
        }
        self.view.viewWithTag(121)?.removeFromSuperview();
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading && !self.refreshControl.isRefreshing{
                
                self.page += 1;
                self.loadProducts()
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
        if let innerView = self.view.viewWithTag(181) as? PaymentReportFilterView {
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
