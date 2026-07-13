//
//  ced_ManageQuotes.swift
//  VenderApp
//
//  Created by Macmini on 13/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
import RxSwift
import RxCocoa

class ced_ManageQuotes: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UITextFieldDelegate,UITextViewDelegate,UIGestureRecognizerDelegate
{
    @IBOutlet weak var topview: UILabel!
    @IBOutlet weak var filterbutton: UIButton!
    @IBOutlet weak var tableview: UITableView!
    
    
    let disposeBag = DisposeBag()
    let screenSize: CGRect = UIScreen.main.bounds;
    var datePickerView = UIDatePicker();
    var statusSelected = String();
    
    var filter=""
    var applyFilter = false
    var quote_info=[[String:String]]()
    var filterPostData = NSDictionary()
    var tempView1=UIView()
    
    var loading = true;
    
    let tempView=UIView() /////datepicke
    let pickerview = ced_datepicker()
    var from_Date_Filter = ""
    var datetextfeild = UITextField()
    var pageNo = 1
   var colorString = ""
    
    var dropDownData = [String]()
    var dropDownkey = [String]()
    var dropDownDict = [String:String]()
    
    
    var QuoteIncrementId = ""
    var createdat  = ""
    var custumeremail = ""
    var status = ""
    var QuotePricefrom  = ""
    var QuotePriceto  = ""
    var QuoteRequestQty  = ""
    var QuoteUpdatedQty  = ""
    var ShippingAmountfrom  = ""
    var ShippingAmountto  = ""
    var UpdatedPriceto = ""
    var UpdatedPricefrom = ""
    var ShippingMethod = ""
    var fromQuoteId = ""
    var toQuoteId = ""
    var fromCreatedAt = ""
    var toCreatedAt = ""
    
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        //self.getDropdowndata(url:"vrfqapi/quote")///configval
        
        filterbutton.addTarget(self, action: #selector(self.filterbuttonpressed(sender:)), for: .touchUpInside)
        colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        topview.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        topview.text = " Vendor Quote Management".localized
        
        self.loadData(page: self.pageNo)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    
    @objc func getDropdowndata(url: String)
    {
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
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
                    for values in json["data"]["status"].arrayValue
                    {
                        self.dropDownData.append(values["key"].stringValue)
                        self.dropDownkey.append(values["value"].stringValue)
                        self.dropDownDict[values["key"].stringValue] = values["value"].stringValue
                    }
                    print(self.dropDownData)
                    self.loadData(page: self.pageNo)
                }

            }
        })
        task.resume()
    }
    @objc func loadData(page:Int)
    {
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let baseUrl = "vrfqapi/quote/item"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        if(applyFilter){
            postString += "&filter=" + filter
        }
        print(postString)
        sendRequest(url: baseUrl, parameters: postString);
    }
    
    
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            if(requestUrl=="vrfqapi/quote/item")
            {
                print("HELLO JSON")
                
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    if json["data"]["count"].intValue > 0
                    {
                        self.parseProductdata(json)
                        UserDefaults.standard.set(json["data"]["currency_symbol"].stringValue, forKey: "currencySymbol")
                        print(quote_info)
                    }
                }
                else if json["data"]["success"] == false
                {
//                    Alert_File.showValidationError(self,title:"Error",message:json["data"]["message"].stringValue);
                   // self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                    self.tableview.setEmptyMessage(json["data"]["message"].stringValue)
//                    Ced_CommonVendor.delay(2.0, closure: {
//                        self.navigationController?.popViewController(animated: true)
//                    })
                    loading = false
                }
                
                if quote_info.count != 0
                {
                    tableview.delegate = self
                    tableview.dataSource = self
                    tableview.restore()
                }
                
            }
            tableview.reloadData()
        }
        
    }
    
    
    func parseProductdata(_ json:JSON){
        for values in json["data"]["status"].arrayValue
        {
            self.dropDownData.append(values["key"].stringValue)
            self.dropDownkey.append(values["value"].stringValue)
            self.dropDownDict[values["value"].stringValue] = values["label"].stringValue
        }
        print(self.dropDownDict)
        
        for result in json["data"]["quote_info"].arrayValue{
            let quote_id = result["quote_id"].stringValue
            let quote_increment_id = result["quote_increment_id"].stringValue
            let created_at = result["created_at"].stringValue
            let customer_email  = result["customer_email"].stringValue
            var status = result["status"].stringValue
            print(status)
            status = calculate_Status_Stringvalue(status:status)
            let quote_total_price = result["quote_total_price"].stringValue
            let quote_total_qty  = result["quote_total_qty"].stringValue
            let quote_updated_qty = result["quote_updated_qty"].stringValue
            let shipping_amount = result["shipping_amount"].stringValue
            let quote_updated_price = result["quote_updated_price"].stringValue
            let shipping_method = result["shipping_method"].stringValue
            let product = ["quote_id":quote_id,"quote_increment_id":quote_increment_id,"created_at":created_at,"customer_email":customer_email,"status":status,"quote_total_price":quote_total_price,"quote_total_qty":quote_total_qty,"shipping_amount":shipping_amount,"quote_updated_price":quote_updated_price,"shipping_method":shipping_method,"quote_updated_qty":quote_updated_qty]
            self.quote_info.append(product)
        }
        
       
        
        
    }
//    MARK : table view
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "ced_ManageQuotesCell") as! ced_ManageQuotesCell

        cell.QuoteIncrementId.text = quote_info[indexPath.row]["quote_increment_id"]
        
        cell.CreatedAt.text = quote_info[indexPath.row]["created_at"]
        
        cell.CustomerEmail.text = quote_info[indexPath.row]["customer_email"]
        
        cell.Status.text = quote_info[indexPath.row]["status"]
        
        cell.quoteId.text = quote_info[indexPath.row]["quote_id"]
        
//        cell.QuotePrice.text = quote_info[indexPath.row]["quote_total_price"]
//
//        cell.QuoteRequestedQty.text = quote_info[indexPath.row]["quote_updated_qty"]
//
//        cell.ShippingMethod.text = ""
//
//        cell.QuoteShippingAmount.text = ""
//
//        cell.QuoteUpdatedQty.text = quote_info[indexPath.row]["quote_updated_qty"]
//
//        cell.QuoteUpdatedPrice.text = quote_info[indexPath.row]["quote_updated_price"]
//
        return cell
    }
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return quote_info.count
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return 175
    }
    
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let navigation = self.sideDrawerViewController?.mainViewController as! ced_navigationBarController
        let stoy = UIStoryboard(name: "Main", bundle: nil)
        let viewControl = stoy.instantiateViewController(withIdentifier: "ced_Quote_AccountInformation_RFQ") as! ced_Quote_AccountInformation_RFQ
        viewControl.quote_id = quote_info[indexPath.row]["quote_id"]!
        viewControl.qinfo = quote_info[indexPath.row]["quote_increment_id"]!
        viewControl.selectedStatus = dropDownDict.someKey(forValue: quote_info[indexPath.row]["status"]!) ?? ""
        navigation.pushViewController(viewControl, animated: true)
        return
    }
    
    @objc func filterbuttonpressed(sender:UIButton)
    {
        /* background view */
        let bgCView = UIView();
        bgCView.tag=151;
        tempView1=bgCView
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let filterbyView = ced_FilterXib_ManageQuotes();
        //        RmaFilterXibs
        filterbyView.frame=CGRect(x: 0, y: 0, width: self.view.frame.width - 10, height: 225);
        self.addgesturesTohideView(self.view)
        if(filterPostData.count != 0){
            if let st = filterPostData.object(forKey: "check_status") as? String{
                filterbyView.Status.setTitle(st, for: UIControl.State.normal)
            }
        }
       // filterbyView.createdat.delegate = self;
        filterbyView.toCreatedAt.delegate = self
        filterbyView.fromCreatedAt.delegate = self
        filterbyView.Status.addTarget(self, action: #selector(self.showstatus(sender:)), for: .touchUpInside)
        filterbyView.filter.addTarget(self, action: #selector(self.applyFilters(sender:)), for: .touchUpInside)
        filterbyView.resetfilter.addTarget(self, action: #selector(self.resetFilters(sender:)), for: .touchUpInside)
       // filterbyView.createdat.tag=11
//        pickerview.date.addTarget(self, action:#selector(datePickerChanged(_:)) , for: .valueChanged)
//        pickerview.done.addTarget(self, action: #selector(donebuttonclick), for: .touchUpInside)
//        pickerview.done.backgroundColor = Ced_CommonVendor.UIColorFromRGB(colorString)
        filterbyView.tag = 181;
        filterbyView.backgroundColor = UIColor.black;
        filterbyView.frame = CGRect(x:25, y:100, width:self.view.frame.width - 50,height: 325)
        filterbyView.center = self.view.center
        bgCView.addSubview(filterbyView)
        self.view.addSubview(bgCView);
        SetFilerView()
    }
    
    @objc func applyFilters(sender:UIButton){
        quote_info = []
        self.tableview.reloadData()
        self.applyFilter = true
        if let view = self.view.viewWithTag(181) as? ced_FilterXib_ManageQuotes {
            
            //
            if(view.QuoteIncrementId.text != nil){
                QuoteIncrementId =  view.QuoteIncrementId.text!
            }
             //////////
            if view.fromQuoteId.text != nil {
                fromQuoteId = view.fromQuoteId.text!
            }
            
//            if view.toQuoteId.text != nil {
//                toQuoteId = view.fromQuoteId.text!
//            }
            
            if view.fromCreatedAt.text != nil {
                fromCreatedAt = view.fromCreatedAt.text!
            }
            
            if view.toCreatedAt.text != nil {
                toCreatedAt = view.toCreatedAt.text!
            }
            
           
            if(view.custumeremail.text != nil){
                custumeremail = view.custumeremail.text!
            }
            
            
            if(view.Status.titleLabel!.text != nil){
                if view.Status.titleLabel!.text!.lowercased() != "Select Status".localized.lowercased() {
                    status = self.statusSelected
//                    status = view.Status.titleLabel!.text!
//                    status = calculate_Status_Intvalue(status: status)
//                    print(status)
                }
            }
            
           /*
            if(view.QuotePricefrom.text != nil){
                QuotePricefrom = view.QuotePricefrom.text!
            }
            
            if(view.QuotePriceto.text != nil){
                QuotePriceto = view.QuotePriceto.text!
            }
            
            if(view.QuoteRequestQty.text != nil){
                QuoteRequestQty = view.QuoteRequestQty.text!
            }
            
            if(view.QuoteUpdatedQty.text != nil){
                QuoteUpdatedQty = view.QuoteUpdatedQty.text!
            }
            
            if(view.ShippingAmountfrom.text != nil){
                ShippingAmountfrom = view.ShippingAmountfrom.text!
            }
            
            if(view.ShippingAmountto.text != nil){
                ShippingAmountto = view.ShippingAmountto.text!
            }
            
            if(view.UpdatedPriceto.text != nil){
                UpdatedPriceto = view.UpdatedPriceto.text!
            }
           
            if(view.UpdatedPricefrom.text != nil){
                UpdatedPricefrom = view.UpdatedPricefrom.text!
            }
            
            if(view.ShippingMethod.text != nil){
                ShippingMethod = view.ShippingMethod.text!
            }
            
            if validateToAndFrom(firstTextfeild: view.ShippingAmountfrom, SecoundTextfeild: view.ShippingAmountto)
            {
                return
            }
            if validateToAndFrom(firstTextfeild: view.UpdatedPricefrom, SecoundTextfeild: view.UpdatedPriceto)
            {
                return
            }
            if validateToAndFrom(firstTextfeild: view.QuotePricefrom, SecoundTextfeild: view.QuotePriceto)
            {
                return
            }
            */
            filter = "";
            filter += "{";
            filter += "\""+"quote_increment_id"+"\":\""+QuoteIncrementId+"\",";
            filter += "\""+"customer_email"+"\":\""+custumeremail+"\",";
            filter += "\""+"quote_id"+"\":\""+fromQuoteId+"\",";
            filter += "\""+"created_at"+"\":{\"from\":\""+fromCreatedAt+"\",\"to\":\""+toCreatedAt+"\"},";
         //   filter += "\""+"created_at"+"\":\""+createdat.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!+"\",";
          //  filter += "\""+"quote_total_price"+"\":{\"from\":\""+QuotePricefrom+"\",\"to\":\""+QuotePriceto+"\"},";
          //  filter += "\""+"shipping_amount"+"\":{\"from\":\""+ShippingAmountfrom+"\",\"to\":\""+ShippingAmountto+"\"},";
          //  filter += "\""+"quote_updated_price"+"\":{\"from\":\""+UpdatedPricefrom+"\",\"to\":\""+UpdatedPriceto+"\"},";
          //  filter += "\""+"quote_total_qty"+"\":\""+QuoteRequestQty+"\",";
         //   filter += "\""+"quote_updated_qty"+"\":\""+QuoteUpdatedQty+"\",";
          //  filter += "\""+"shipment_method"+"\":\""+ShippingMethod+"\",";
            filter += "\""+"status"+"\":\""+status+"\"}";
            self.view.viewWithTag(151)?.removeFromSuperview();
            self.loadData(page: 1)
        }
    }
    
    @objc func resetFilters(sender:UIButton){
        QuoteIncrementId = ""
        createdat  = ""
        custumeremail = ""
        status = ""
        QuotePricefrom  = ""
        QuotePriceto  = ""
        QuoteRequestQty  = ""
        QuoteUpdatedQty  = ""
        ShippingAmountfrom  = ""
        ShippingAmountto  = ""
        UpdatedPriceto = ""
        UpdatedPricefrom = ""
        ShippingMethod = ""
        statusSelected = ""
        fromCreatedAt = ""
        toCreatedAt = ""
        fromQuoteId = ""
        toQuoteId = ""
        self.applyFilter = false
        filterPostData = NSDictionary()
        self.view.viewWithTag(151)?.removeFromSuperview();
        
        quote_info = []
        self.tableview.reloadData()
        self.filter = String()
        self.loadData(page: 1)
    }
    
    
    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(hideView(recognizer:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(recognizer:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(recognizer:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(recognizer:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(recognizer:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    
    @objc func showstatus(sender:UIButton){
        let dropDown = DropDown();
        var dataSource = [String]()
        for (_,v) in dropDownDict {
            dataSource.append(v)
        }
        dropDown.dataSource = dataSource
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: .normal);
            for (k,v) in self.dropDownDict {
                if v==item {
                    self.statusSelected = k;
                }
            }
            
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
    
    
    
    //    MARK: Date picker for filters
//    @objc func donebuttonclick()
//    {
//        if datetextfeild.text == ""
//        {
//            let currentDate = NSDate()
//            let dateFormatter = DateFormatter()
//            dateFormatter.dateFormat="yyyy-MM-dd"
//            let convertedDate = dateFormatter.string(from: currentDate as Date)
//            datetextfeild.text=convertedDate
//        }
//        tempView.removeFromSuperview()
//        print(datetextfeild.text)
//    }
    
//    @objc func datePickerChanged(_ sender:UIDatePicker)
//    {
//
//        let dateformatter=DateFormatter()
//        dateformatter.dateFormat="yyyy-MM-dd"
//        let date1=dateformatter.string(from: sender.date)
//        from_Date_Filter=date1
//        datetextfeild.text=date1
//
//    }
//
//
//    @objc func dateclicked1(_ sender:UITextField)
//    {
//        print("123456789")
//        datetextfeild=sender
//        pickerview.date.datePickerMode = UIDatePicker.Mode.date
//        tempView.frame=CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.tempView1.frame.height)
//        tempView.center = self.tempView1.center
//        pickerview.view.layer.cornerRadius = 15
//        pickerview.view.layer.borderWidth=0
//        pickerview.frame=CGRect(x: 0, y: 0, width: self.view.frame.width - 50, height: 200)
//        pickerview.center=tempView.center
//        if #available(iOS 10.0, *) {
//            tempView.backgroundColor = UIColor(displayP3Red: 0, green: 0, blue: 0, alpha: 0.3)
//        } else {
//            // Fallback on earlier versions
//        } //UIColor(colorLiteralRed: 0, green: 0, blue: 0, alpha: 0.3)
//        tempView.addSubview(pickerview)
//        self.tempView1.addSubview(tempView)
//    }
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        //gestureRecognizer.delegate = self;
        if touch.view!.isDescendant(of: tableview)
        {
            return false;
        }
        if let innerView = self.view.viewWithTag(181) as? ced_FilterXib_ManageQuotes {
            if(touch.view!.isDescendant(of: innerView) )
            {
                return false;
            }
            return true;
        }
        return true;
    }
//    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
//        let currentOffset = scrollView.contentOffset.y
//        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
//        if (maximumOffset - currentOffset) <= 40 {
//            if loading{
//                self.pageNo += 1;
//                self.loadData(page: pageNo)
//                tableview.reloadData();
//            }
//        }
//    }
    
    @objc func calculate_Status_Intvalue(status:String) -> String
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
    
    @objc func calculate_Status_Stringvalue(status:String) -> String
    {
        //var valuestatus = ""
        print(status)
        print(dropDownDict)
        for (k,v) in dropDownDict {
            if k == status {
                return v
            }
        }
        return ""
    }
    
    
    
    
    
//
//    @objc func textFieldDidBeginEditing(_ textField: UITextField)
//    {
//        textField.resignFirstResponder();
//
//        globalTextFieldTag = textField.tag;
//
//        /* background view */
//        let bgCView = UIView();
//        bgCView.tag=121;
//        bgCView.frame = UIScreen.main.bounds;
//        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
//        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
//
//
//        let colorGreen = Ced_CommonVendor.UIColorFromRGB("#ff0000");
//        let colorRed = Ced_CommonVendor.UIColorFromRGB("#ff0000");
//        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String;
//        let color = Ced_CommonVendor.UIColorFromRGB(colorString!);
//
//        let tempInrView = UIView();
//        tempInrView.tag=131;
//        tempInrView.frame = CGRect(x: 0, y: CGFloat(0), width: screenSize.width-50,height: CGFloat(270));
//        tempInrView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
//        tempInrView.backgroundColor = UIColor.white;
//        tempInrView.center = CGPoint(x: bgCView.frame.size.width  / 2,
//                                     y: bgCView.frame.size.height / 2);
//
//        let label = UILabel();
//        label.frame = CGRect(x: 0, y: CGFloat(0), width: tempInrView.frame.width,height: CGFloat(30));
//        label.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
//        label.text = "Select Date".localized.uppercased();
//        label.textAlignment = NSTextAlignment.center;
//        label.textColor = UIColor.white;
//        label.backgroundColor = color;
//        tempInrView.addSubview(label);
//
//
//        datePickerView.datePickerMode = UIDatePicker.Mode.date;
//        datePickerView.setValue(UIColor.black, forKeyPath: "textColor");
//        datePickerView.backgroundColor = UIColor.white;
//        datePickerView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
//        datePickerView.frame = CGRect(x: 0, y: CGFloat(30), width: tempInrView.frame.width,height: CGFloat(200));
//        datePickerView.maximumDate = Date()
//        tempInrView.addSubview(datePickerView);
//
//
//        let twoButtonView = TwoButtonView();
//        twoButtonView.tag=141;
//        twoButtonView.frame = CGRect(x: 0, y: CGFloat(230), width: tempInrView.frame.width,height: CGFloat(40));
//        twoButtonView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
//        tempInrView.addSubview(twoButtonView);
//
//        twoButtonView.cancelButton.addTarget(self, action: #selector(cancelDatePickerView(_:)), for: UIControl.Event.touchUpInside);
//        twoButtonView.cancelButton.backgroundColor = colorRed;
//
//
//        twoButtonView.doneButton.addTarget(self, action: #selector(pickDateFromDatePickerView(_:)), for: UIControl.Event.touchUpInside);
//        twoButtonView.doneButton.backgroundColor = colorGreen;
//
//        tempInrView.makeCardUsingThemeColor(tempInrView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
//
//        bgCView.addSubview(tempInrView);
//        self.view.addSubview(bgCView);
//
//
//    }
//
//    @objc func cancelDatePickerView(_ sender:UIButton)
//    {
//        if let textField = self.view.viewWithTag(globalTextFieldTag) as? UITextField
//        {
//            textField.resignFirstResponder();
//        }
//        self.view.viewWithTag(121)?.removeFromSuperview();
//    }
//
//
//    @objc func pickDateFromDatePickerView(_ sender:UIButton)
//    {
//        let dateFormatter = DateFormatter()
//        //dateFormatter.dateStyle = DateFormatter
//        dateFormatter.dateStyle = DateFormatter.Style.short
//        dateFormatter.timeStyle = DateFormatter.Style.none
//        if let textField = self.view.viewWithTag(globalTextFieldTag) as? UITextField
//        {
//            dateFormatter.dateFormat = "yyyy-MM-dd";
//            textField.text = dateFormatter.string(from: datePickerView.date);
//            textField.resignFirstResponder();
//        }
//        self.view.viewWithTag(121)?.removeFromSuperview();
//    }
//
    
    @objc func SetFilerView()
    {
        if let view = self.view.viewWithTag(181) as? ced_FilterXib_ManageQuotes
        {
            if(view.QuoteIncrementId.text != nil){
                view.QuoteIncrementId.text!=QuoteIncrementId
            }
//            if(view.createdat.text != nil){
//                view.createdat.text!=createdat
//            }
            
            
            if(view.custumeremail.text != nil){
                view.custumeremail.text!=custumeremail
            }
            
            
      /*      if(view.Status.currentTitle != nil && view.Status.currentTitle?.lowercased() != "select status")
            {
                status = calculate_Status_Stringvalue(status: status)
                view.Status.setTitle(status, for: .normal)
            }
            if(view.QuotePricefrom.text != nil){
                view.QuotePricefrom.text!=QuotePricefrom
            }
            
            if(view.QuotePriceto.text != nil){
                view.QuotePriceto.text!=QuotePriceto
            }
            
            if(view.QuoteRequestQty.text != nil){
                view.QuoteRequestQty.text!=QuoteRequestQty
            }
            
            if(view.QuoteUpdatedQty.text != nil){
                view.QuoteUpdatedQty.text!=QuoteUpdatedQty
            }
            
            if(view.ShippingAmountfrom.text != nil){
                view.ShippingAmountfrom.text!=ShippingAmountfrom
            }
            
            if(view.ShippingAmountto.text != nil){
                view.ShippingAmountto.text!=ShippingAmountto
            }
            
            if(view.UpdatedPriceto.text != nil){
                view.UpdatedPriceto.text!=UpdatedPriceto
            }
            
            if(view.UpdatedPricefrom.text != nil){
                view.UpdatedPricefrom.text!=UpdatedPricefrom
            }
            
            if(view.ShippingMethod.text != nil){
                view.ShippingMethod.text!=ShippingMethod
            }  */
        }
    }
    
}
extension ced_ManageQuotes{
    @objc func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.resignFirstResponder()
 
        /* background view */
        let bgCView = UIView();
        bgCView.tag=161;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        
        
        let colorGreen = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        let colorRed = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String;
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!);
        
        let tempInrView = UIView();
        tempInrView.tag=131;
        tempInrView.frame = CGRect(x: 0, y: CGFloat(0), width: UIScreen.main.bounds.width-100,height: CGFloat(270));
        tempInrView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        tempInrView.backgroundColor = UIColor.white;
        tempInrView.center = CGPoint(x: bgCView.frame.size.width  / 2,y: bgCView.frame.size.height / 2);
        
        let label = UILabel();
        label.frame = CGRect(x: 0, y: CGFloat(0), width: tempInrView.frame.width,height: CGFloat(30));
        label.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        label.text = "Select Date".localized.uppercased();
        label.textAlignment = NSTextAlignment.center;
        label.textColor = UIColor.white;
        label.backgroundColor = color;
        tempInrView.addSubview(label);
        
        let datePickerView = UIDatePicker()
        datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
        datePickerView.datePickerMode = UIDatePicker.Mode.date;
        datePickerView.maximumDate = Date()
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
        
        
//        twoButtonView.doneButton.addTarget(self, action: #selector(pickDateFromDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.doneButton.backgroundColor = colorGreen;
        twoButtonView.doneButton.rx.tap.bind{
            let dateFormatter = DateFormatter()
            dateFormatter.locale = Locale(identifier: "en_US_POSIX")
            dateFormatter.dateStyle = DateFormatter.Style.short
            dateFormatter.timeStyle = DateFormatter.Style.none
            dateFormatter.dateFormat = "yyyy-MM-dd";
            let timeFormatter = DateFormatter()
            timeFormatter.locale = Locale(identifier: "en_US_POSIX")
            timeFormatter.dateFormat = "hh:mm:ss"
            let time = timeFormatter.string(from: Date())
            let dateDisplay = dateFormatter.string(from: datePickerView.date);
            textField.text = dateDisplay
            textField.resignFirstResponder();
            self.view.viewWithTag(161)?.removeFromSuperview();
        }.disposed(by: disposeBag)
        
        tempInrView.makeCardUsingThemeColor(tempInrView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
        
        bgCView.addSubview(tempInrView);
        self.view.addSubview(bgCView);
  
    }
  
    @objc func cancelDatePickerView(_ sender:UIButton){
        
        sender.resignFirstResponder();
        
        self.view.viewWithTag(161)?.removeFromSuperview();
    }
}

extension UIViewController
{
    @objc func validateToAndFrom(firstTextfeild:UITextField,SecoundTextfeild:UITextField) -> Bool {
        if(firstTextfeild.text != "") && (SecoundTextfeild.text == "")
        {
            Alert_File.showValidationError(self,title:"Error".localized,message:"Updated to Feild Required".localized);
            return true
        }
        if(firstTextfeild.text == "") && (SecoundTextfeild.text != "")
        {
            Alert_File.showValidationError(self,title:"Error".localized,message:"Updated From Feild Required".localized);
            return true
        }
        if (Int(firstTextfeild.text!) != nil) && (Int(SecoundTextfeild.text!) != nil)
        {
            let first = Int(firstTextfeild.text!)
            let secound = Int(SecoundTextfeild.text!)
            print(first!)
            print(secound!)
            let x = first! > secound!
            print(x)
            if (first! > secound!)
            {
                Alert_File.showValidationError(self,title:"Error".localized,message:"From Field is less than To field".localized);
                return true
            }
        }
        return false
    }
}
