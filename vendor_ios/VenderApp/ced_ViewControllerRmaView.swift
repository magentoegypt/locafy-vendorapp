//
//  ced_ViewControllerRmaView.swift
//  VenderApp
//
//  Created by Macmini on 05/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_ViewControllerRmaView: ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate, UIGestureRecognizerDelegate
{
    @IBOutlet weak var tableview: UITableView!
    @IBOutlet weak var filterbutton: UIButton!
    @IBOutlet weak var topviewlable: UILabel!
    let refreshControl = UIRefreshControl()
    let statusDropdownData = ["Pending_status".localized:"Pending","Approved".localized:"Approved","Cancelled".localized:"Cancelled","Complete".localized:"Complete"]
    
    let filterbyView = RmaFilterXibs();  // filterview xib
    var applyFilter = false
    var rmaFullData=[[String:String]]()
    var filterPostData = NSDictionary()
    var filterString = String()
    var filter=""
    var globalTextFieldTag = 0;
    let screenSize: CGRect = UIScreen.main.bounds;
    var datePickerView = UIDatePicker();
    let tempView=UIView() /////datepicke
    let pickerview = ced_datepicker()
    var from_Date_Filter = ""
    var datetextfeild = UITextField()
    var tempView1=UIView()
    var loading = true;
    var pageNo = 1
    var orderid = ""
    var rmaid  = ""
    var custumername = ""
    var custumeremail = ""
    var status = ""
    var updateto = ""
    var updatedfrom  = ""
    var resolutionRequested = ""
    var statusButton = UIButton()

    
     var colorString = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        loadData(page: pageNo)
        tableview.delegate=self
        tableview.dataSource=self
        colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        topviewlable.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        topviewlable.text = "Manage Return Request".localized
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        tableview.refreshControl = refreshControl
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @objc func refresh(_ sender: AnyObject) {
        rmaFullData = []
        tableview.reloadData()
        self.filter = String()
        self.pageNo = 1
        self.loadData(page: self.pageNo)
    }
    
    @objc func loadData(page:Int)
    {
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let baseUrl = "vrmapi/index/info"
//        let baseUrl = "vrmapi/index/info/page/\(page)"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&page=\(page)"
        if(applyFilter){
            postString += "&filter=" + filter
        }
        print(postString)
        sendRequest(url: baseUrl, parameters: postString);
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "ced_TableViewCellRMAVIEW") as! ced_TableViewCellRMAVIEW
        cell.CustomerEmail.text=rmaFullData[indexPath.row]["customer_email"]
        cell.PurchasedPoint.text=rmaFullData[indexPath.row]["purchased_point"]
        cell.OrderId.text=rmaFullData[indexPath.row]["order_id"]
        cell.RMAId.text=rmaFullData[indexPath.row]["rma_id"]
        cell.Status.text=rmaFullData[indexPath.row]["status"]
        cell.UpdatedAt.text=rmaFullData[indexPath.row]["updated_at"]
        cell.ResolutionRequested.text=rmaFullData[indexPath.row]["resolution_requested"]
        cell.CustomerName.text=rmaFullData[indexPath.row]["customer_name"]
        
        return cell
    }
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return rmaFullData.count
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return 245
    }
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("clicked")
        
        let navigation = self.sideDrawerViewController?.mainViewController as! ced_navigationBarController
        let stoy = UIStoryboard(name: "Main", bundle: nil)
        let viewControl = stoy.instantiateViewController(withIdentifier: "ced_RmaDetailsView") as! ced_RmaDetailsView
        viewControl.id = rmaFullData[indexPath.row]["id"]!
        viewControl.rma_id = rmaFullData[indexPath.row]["rma_id"]!
        navigation.pushViewController(viewControl, animated: true)
        return
    }
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        //print(response)
        refreshControl.endRefreshing()
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            if(requestUrl=="vrmapi/index/info")
            {
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    if json["data"]["count"].intValue > 0
                    {
                        self.parseProductdata(json)
                        print(self.rmaFullData)
                        
                    }
                    tableview.restore()
                    tableview.reloadData()
                }else if json["data"]["success"] == false
                {
                    loading = false
                    if(pageNo == 1)
                    {
                       // self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center);
                        self.rmaFullData.removeAll()
                        self.tableview.reloadData()
                        self.tableview.setEmptyMessage(json["data"]["message"].stringValue)
                       // return
                    }
                }
                
            }
        }
        
    }
    
    
    func parseProductdata(_ json:JSON){
        for result in json["data"]["rma_list"].arrayValue{
            let purchased_point = result["purchased_point"].stringValue
            let order_id    = result["order_id"].stringValue
            let rma_id = result["rma_id"].stringValue
            let id  = result["id"].stringValue
            var status = result["status"].stringValue
            let customer_email = result["customer_email"].stringValue
            let updated_at  = result["updated_at"].stringValue
            let resolution_requested = result["resolution_requested"].stringValue
            let customer_name = result["customer_name"].stringValue
            let product = ["purchased_point":purchased_point,"order_id":order_id,"rma_id":rma_id,"id":id,"status":status,"customer_email":customer_email,"updated_at":updated_at,"resolution_requested":resolution_requested,"customer_name":customer_name]
            self.rmaFullData.append(product)
        }
    }
    
    
   
    
    
    //    MARK: FILTER BUTTON CLICKED
    
    @IBAction @objc func filterbuttonclicked(_ sender: Any) {
     
        /* background view */
        let bgCView = UIView();
        tempView1=bgCView
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
       
        self.addgesturesTohideView(self.view)
        if(filterPostData.count != 0){
            if let st = filterPostData.object(forKey: "check_status") as? String{
                filterbyView.SelectStatus.setTitle(st, for: UIControl.State.normal)
            }
        }
        filterbyView.SelectStatus.addTarget(self, action: #selector(self.showstatus(sender:)), for: .touchUpInside)
        filterbyView.Filter.addTarget(self, action: #selector(self.applyFilters(sender:)), for: .touchUpInside)
        filterbyView.resetfilter.addTarget(self, action: #selector(self.resetFilters(sender:)), for: .touchUpInside)
        filterbyView.updateto.delegate = self;
        filterbyView.updateto.tag = 88;
        filterbyView.updatedfrom.delegate = self;
        filterbyView.updatedfrom.tag = 99;

        statusButton = filterbyView.SelectStatus
        filterbyView.orderid.text = orderid
        filterbyView.rmaid.text = rmaid
        filterbyView.custumername.text = custumername
        filterbyView.custumeremail.text = custumeremail
        filterbyView.resolutionRequested.text = resolutionRequested
        filterbyView.SelectStatus.setTitle(status.localized, for: UIControl.State())
        filterbyView.updateto.text = updateto
        filterbyView.updatedfrom.text = updatedfrom
        
       // filterbyView.orderid.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
        //filterbyView.rmaid.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
        filterbyView.custumername.addTarget(self, action:#selector(isValidNme(_:)), for: .editingDidEnd);
         filterbyView.custumeremail.addTarget(self, action:#selector(isValidEmil(_:)), for: .editingDidEnd);
        
        pickerview.date.addTarget(self, action:#selector(datePickerChanged(_:)) , for: .valueChanged)
        pickerview.done.addTarget(self, action: #selector(donebuttonclick), for: .touchUpInside)
        pickerview.done.backgroundColor = Ced_CommonVendor.UIColorFromRGB(colorString)
        filterbyView.tag = 181;
        filterbyView.backgroundColor = UIColor.black;
        filterbyView.frame = CGRect(x:25, y:100, width:self.view.frame.width - 50,height: 300)
        filterbyView.center = self.view.center
        bgCView.addSubview(filterbyView)
        self.view.addSubview(bgCView)
    }
    @objc func applyFilters(sender:UIButton)
    {
        rmaFullData = []
        self.applyFilter = true
        if let view = self.view.viewWithTag(181) as? RmaFilterXibs {
            if(view.orderid.text != nil){
                orderid = view.orderid.text!
                print(orderid)
            }
            if(view.rmaid.text != nil){
                rmaid = view.rmaid.text!
            }
            
            if(view.custumername.text != nil){
                custumername = view.custumername.text!
            }
            if(view.custumeremail.text != nil){
                custumeremail = view.custumeremail.text!
            }
            if(view.resolutionRequested.text != nil){
                resolutionRequested = view.resolutionRequested.text!
            }
            
            if(view.SelectStatus.titleLabel!.text != nil){
                if view.SelectStatus.titleLabel!.text!.lowercased() != "Select Status".localized.lowercased() {
                    status = statusDropdownData[view.SelectStatus.titleLabel!.text!] ?? ""
                    print(status)
//                    status = calculate_Status_Intvalue(status: status)
                }
            }
            
            if(view.updatedfrom.text != nil){
                updatedfrom = view.updatedfrom.text!
            }
            if(view.updateto.text != nil){
                updateto = view.updateto.text!
            }
    
            filter = "";
            filter += "{";
//            updatedfrom=updatedfrom.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!
//            updateto=updateto.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!
            filter += "\""+"updated_at"+"\":{\"from\":\""+updatedfrom+"\",\"to\":\""+updateto+"\"},";
            filter += "\""+"order_id"+"\":\""+orderid+"\",";
            filter += "\""+"rma_id"+"\":\""+rmaid+"\",";
            filter += "\""+"customer_name"+"\":\""+custumername+"\",";
            filter += "\""+"customer_email"+"\":\""+custumeremail+"\",";
            filter += "\""+"resolution_requested"+"\":\""+resolutionRequested+"\",";
            filter += "\""+"status"+"\":\""+status+"\"}";
            self.view.viewWithTag(151)?.removeFromSuperview();
//            self.view.viewWithTag(181)?.removeFromSuperview();
            self.loadData(page: 1)
        }
    }
    
    
    @objc func calculate_Status_Intvalue(status:String) -> String
    {
        var valuestatus = ""
        switch status {
        case "Pending":
            valuestatus = "0"
        case "Processing":
            valuestatus = "1"
        case "Approved":
            valuestatus = "2"
        case "Cancelled":
            valuestatus = "3"
        case "PO Created":
            valuestatus = "4"
        case "Partial PO":
            valuestatus = "5"
        case "Ordered":
            valuestatus = "6"
        case "Complete":
            valuestatus = "7"
        default:
            print("Cant Handle the request ")
        }
        return valuestatus
    }
    
    @objc func resetFilters(sender:UIButton){
        self.applyFilter = false
        filterPostData = NSDictionary()
        self.view.viewWithTag(151)?.removeFromSuperview();
        tempView1.removeFromSuperview()
        rmaFullData = []
        self.filter = String()
        self.loadData(page: 1)
        statusButton.setTitle("", for: .normal)
        orderid = ""
        rmaid  = ""
        custumername = ""
        custumeremail = ""
        status = ""
        updateto = ""
        updatedfrom  = ""
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
    @objc func hideView(recognizer: UITapGestureRecognizer)
    {
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.tempView1.removeFromSuperview()
    }
    //    MARK: Date picker for filters
    @objc func donebuttonclick()
    {
        if datetextfeild.text == ""
        {
            let currentDate = NSDate()
            let dateFormatter = DateFormatter()
            dateFormatter.locale = Locale(identifier: "en_US_POSIX")
            dateFormatter.dateFormat="yyyy-MM-dd"
            let convertedDate = dateFormatter.string(from: currentDate as Date)
            datetextfeild.text=convertedDate
        }
        tempView.removeFromSuperview()
        print(datetextfeild.text!)
    }
    
    @objc func datePickerChanged(_ sender:UIDatePicker)
    {
        let dateformatter=DateFormatter()
        dateformatter.dateFormat="yyyy-MM-dd"
        let date1=dateformatter.string(from: sender.date)
        from_Date_Filter=date1
        datetextfeild.text=date1
    }
    @objc func dateclicked1(_ sender:UITextField)
    {
        print("123456789")
        datetextfeild=sender
        pickerview.date.datePickerMode = UIDatePicker.Mode.date
        tempView.frame=CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.tempView1.frame.height)
        tempView.center = self.tempView1.center
        pickerview.view.layer.cornerRadius = 15
        pickerview.view.layer.borderWidth=0
        pickerview.frame=CGRect(x: 0, y: 0, width: self.view.frame.width - 50, height: 200)
        pickerview.center=tempView.center
        if #available(iOS 10.0, *) {
            tempView.backgroundColor = UIColor(displayP3Red: 0, green: 0, blue: 0, alpha: 0.3)
        } else {
            // Fallback on earlier versions
        }//UIColor(colorLiteralRed: 0, green: 0, blue: 0, alpha: 0.3)0.3
        if sender.tag == 11
        {
            print(sender.tag)
            let formatter1 = DateFormatter()
            formatter1.locale = Locale(identifier: "en_US_POSIX")
            formatter1.dateFormat = "yyyy-MM-dd"
            let minDate: Date? = formatter1.date(from: from_Date_Filter)
            pickerview.date.minimumDate = minDate
        }
        tempView.addSubview(pickerview)
        self.tempView1.addSubview(tempView)
    }
    
    
//    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
//    {
//        //gestureRecognizer.delegate = self;
//        if touch.view!.isDescendant(of: tableview)
//        {
//            return false;
//        }
//        if let innerView = self.view.viewWithTag(181) as? ProductFilterView {
//            if(touch.view!.isDescendant(of: innerView) )
//            {
//                return false;
//            }
//            return true;
//        }
//        return true;
//    }
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading && !self.refreshControl.isRefreshing{
                self.pageNo += 1;
                self.loadData(page: pageNo)
                tableview.reloadData();
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
        if globalTextFieldTag == 88
        {
            if   filterbyView.updatedfrom.text != ""
            {
                // print(filterView.value21.text)
                let isoDate =  filterbyView.updatedfrom.text
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "yyyy-MM-dd"
                dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
                let date = dateFormatter.date(from:isoDate!)!
                let calendar = Calendar.current
                let components = calendar.dateComponents([.year, .month, .day, .hour], from: date)
                let finalDate = calendar.date(from:components)
                datePickerView.minimumDate = finalDate
            }
            else
            {
                ShowSimpleAlert.showSimpleAlert(self, showTitle: "Information".localized, showMsg: "please select the first limit of the date".localized)
                return
            }
        }
        datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
        datePickerView.datePickerMode = UIDatePicker.Mode.date;
        datePickerView.setValue(UIColor.black, forKeyPath: "textColor");
        datePickerView.backgroundColor = UIColor.white;
        datePickerView.maximumDate = Date();
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
    
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        //gestureRecognizer.delegate = self;
        if touch.view!.isDescendant(of: tableview)
        {
            return false;
        }
        if let innerView = self.view.viewWithTag(181) as? RmaFilterXibs {
            if(touch.view!.isDescendant(of: innerView) )
            {
                return false;
            }
            return true;
        }
        return true;
    }
    
}
