//
//  cedProductReturn.swift
//  VenderApp
//
//  Created by cedcoss on 05/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedProductReturn: ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate, UIGestureRecognizerDelegate {

    @IBOutlet weak var headingLabel: UILabel!
    let refreshControl = UIRefreshControl()
    var datePickerView = UIDatePicker();
    let screenSize: CGRect = UIScreen.main.bounds;
    let statusDropdownData = ["Pending_status".localized:"Pending","Approved".localized:"Approved","Cancelled".localized:"Cancelled","Complete".localized:"Complete"]
    
    @IBOutlet weak var filterButton: UIButton!
    var heightToUse : CGFloat = 0;
    var globalTextFieldTag = 0;
    var page = 1;
    var loading = true;
    var filter = String();
    var orderId = "";
    var orderDate = "";
    var orderStatus = "";
    @IBOutlet weak var mainTable: UITableView!
    var productData = [[String:String]]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        headingLabel.setThemeColor()
        headingLabel.text = "Return Report".localized
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
        self.productData.removeAll()
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
      //  self.sendRequest(url: "vreportapi/product/returnproduct", parameters: postString)
        
        
        var param = [String:String]()//["vendor_id":vendorId,"hashkey":hashKey,"page":"\(page)"]
        param["vendor_id"] = vendorId
        param["hashkey"] = hashKey
        param["page"] = "\(page)"
        if(filter != "")
        {
            param["filter"] = filter;
        }
        self.sendRequestWithData(url: "rest/V1/vreport/getReturnProduct", params: param)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        refreshControl.endRefreshing()
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            guard let json = try? JSON(data:data) else {return}
            print(json)
            if(json["vendor_data"]["success"].stringValue == "true")
            {
                for product in json["vendor_data"]["product"].arrayValue
                {
                    productData.append(["Order Id":product["order_id"].stringValue,"Order Date":product["order_date"].stringValue,"Status":product["status"].stringValue]);
                }
                
            }
            else
            {
                if(page == 1)
                {
                    productData.removeAll()
                    mainTable.reloadData();
                    //Alert_File.showValidationError(self,title:"Error".localized,message:json["vendor_data"]["message"].stringValue);
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
        return productData.count;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if let cell = tableView.dequeueReusableCell(withIdentifier: "cedProductReturnCell", for: indexPath) as? cedProductReturnCell{
            let product = productData[indexPath.row];
            cell.orderDate.text = product["Order Date"]?.dateConvertToCurrentTimezone("yyyy-MM-dd HH:mm:ss")
            cell.orderId.text = product["Order Id"]
            cell.orderStatus.text = product["Status"]
            cell.cellView.cardView();
            return cell;
        }
        return UITableViewCell();
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 130;
    }

    @objc func filterButtonPressed(_ sender:UIButton)
    {
        print("filterButtonPressed");
        /* background view */
        
        
        
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let dealFilterView = ReturnReportFilterView();
        dealFilterView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width - 20, height: 200);
        dealFilterView.center.x = self.view.center.x;
        dealFilterView.center.y = self.view.center.y+20;
        dealFilterView.tag = 181;
        dealFilterView.orderDate.tag = 88;
        
        dealFilterView.orderDate.delegate = self;
       
        dealFilterView.orderId.text = orderId;
        dealFilterView.status.addTarget(self, action: #selector(self.showstatus(sender:)), for: .touchUpInside)
        dealFilterView.orderDate.text = orderDate;
        self.addgesturesTohideView(self.view);
        dealFilterView.applyFilter.setTitle("Filter".localized, for: UIControl.State());
        dealFilterView.applyFilter.addTarget(self, action: #selector(applyFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        
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
        
        if let view = self.view.viewWithTag(181) as? ReturnReportFilterView
        {
            if(view.orderDate.text != nil)
            {
                orderDate = view.orderDate.text!;
            }
            if(view.orderId.text != nil)
            {
                orderId = view.orderId.text!;
            }
            if(view.status.titleLabel!.text != nil){
                if view.status.titleLabel!.text!.lowercased() != "Select Status".localized.lowercased() {
                    orderStatus = statusDropdownData[view.status.titleLabel!.text!] ?? ""
                    print(orderStatus)
                }
            }
            filter = "";
            filter += "{";
            filter += "\""+"order_id"+"\":\""+orderId+"\",";
            filter += "\""+"created_at"+"\":\""+orderDate.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!+"\",";
            
            filter += "\""+"status"+"\":\""+orderStatus+"\"}";
            filter = "{\"order_id\":\"\(orderId)\",\"created_at\":{\"from\":\"\(orderDate)\",\"to\":\"\(orderDate)\"},\"status\":\"\(orderStatus)\"}"
            print(filter);
            self.view.viewWithTag(151)?.removeFromSuperview();
            self.page = 1;
            productData = [[String:String]]();
            self.loadProducts();
            
        }
        
    }
    
    @objc func resetFilterTapped(_ sender:UIButton)
    {
        print("resetFilterTapped");
        orderStatus = "";
        orderDate = "";
        orderId = "";
        filter = String();
        self.page = 1;
        productData = [[String:String]]();
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.loadProducts();
    }
    
    @objc func showstatus(sender:UIButton){
        let dropDown = DropDown();
        dropDown.dataSource = Array(statusDropdownData.keys)
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
        
        datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
        datePickerView.maximumDate = Date();
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
        //dateFormatter.dateStyle = DateFormatter
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
        if let innerView = self.view.viewWithTag(181) as? ReturnReportFilterView {
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
