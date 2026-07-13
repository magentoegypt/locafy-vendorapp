//
//  ced_ViewPo.swift
//  VenderApp
//
//  Created by Macmini on 15/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
import RxSwift
import RxCocoa

class ced_ViewPo: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UITextFieldDelegate,UITextViewDelegate,UIGestureRecognizerDelegate
{
    
    
    @IBOutlet weak var filterbutton: UIButton!
    @IBOutlet weak var topview: UILabel!
    @IBOutlet weak var tableview: UITableView!
    ////   FILTER VARIABLES
    ///
    let disposeBag = DisposeBag()
    var filter=""
    var applyFilter = false
    var filterPostData = NSDictionary()
    ////////
    var po_info=[[String:String]]()
    
    //////Pickerview
    var globalTextFieldTag = 0;
    let screenSize: CGRect = UIScreen.main.bounds;
    var datePickerView = UIDatePicker();
    
    let tempView=UIView() /////datepicke
    let pickerview = ced_datepicker()
    var from_Date_Filter = ""
    var datetextfeild = UITextField()
    var colorString = ""
    var loading = true
    var pageNo = 1
    var tempView1=UIView()
    
    var dropDownData = [String]()
    var dropDownkey = [String]()
    
    var POIncrementId = ""
    var QuoteId  = ""
    var status = ""
    var CreatedAtFrom  = ""
    var CreatedAtTo  = ""
    var statusArray = [String:String]()
    override func viewDidLoad() {
        super.viewDidLoad()
        
        colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        topview.backgroundColor = Ced_CommonVendor.UIColorFromRGB(colorString)
        
        topview.text = "Vendor PO Management".localized
        //  self.getRequest(url: "vrfqapi/quote/configval")
        self.loadData(page: self.pageNo)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    
    //    MARK: Dropdown Data
    @objc func getDropdowndata(url: String){
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
                print("error=\(String(describing: error))")
                return;
            }
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                DispatchQueue.main.async
                {
                    
                    Alert_File.removeLoadingIndicator(self)
                    print("statusCode should be 200, but is \(httpStatus.statusCode)")
                    print("response = \(String(describing: response))")
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
                
            }
        })
        task.resume()
    }
    
    
    @objc func loadData(page:Int)
    {
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let baseUrl = "vrfqapi/po/item"
        //      let baseUrl = "vrfqapi/po/item/page/\(page)"
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
            if(requestUrl == "vrfqapi/quote/configval")
            {
                guard let json = try? JSON(data: data) else {return}
                print(json["status"])
                for values in json["data"]["status"].arrayValue
                {
                    self.dropDownData.append(values["key"].stringValue)
                    self.dropDownkey.append(values["value"].stringValue)
                    self.statusArray[values["value"].stringValue] = values["label"].stringValue
                }
                print(self.statusArray)
                print(self.dropDownData)
                self.loadData(page: self.pageNo)
            }
            else if(requestUrl=="vrfqapi/po/item")
            {
                print("HELLO JSON")
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    if json["data"]["count"].intValue > 0
                    {
                        UserDefaults.standard.set(json["data"]["currency_symbol"].stringValue, forKey: "currencySymbol")
                        self.parseProductdata(json)
                        tableview.reloadData()
                    }
                    for values in json["data"]["status"].arrayValue
                    {
                        self.dropDownData.append(values["label"].stringValue)
                        self.dropDownkey.append(values["value"].stringValue)
                        self.statusArray[values["value"].stringValue] = values["label"].stringValue
                    }
                    print(self.statusArray)
                    print(self.dropDownData)
                }else if json["data"]["success"] == false
                {
                    
                   // self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                    self.tableview.setEmptyMessage(json["data"]["message"].stringValue)
                    //                    Ced_CommonVendor.delay(2.0, closure: {
                    //                        self.navigationController?.popViewController(animated: true)
                    //                    })
                    
                    //                    Alert_File.showValidationError(self,title:"Error",message:json["data"]["message"].stringValue);
                    loading = false
                    return
                }
                else
                {
                    self.view.showToastMsg("Problem in loading Data".localized)
                }
                tableview.restore()
                tableview.reloadData()
            }
            
            
        }
        
    }
    
    
    func parseProductdata(_ json:JSON){
        for result in json["data"]["po_info"].arrayValue{
            let po_increment_id = result["po_increment_id"].stringValue
            let quote_id = result["quote_id"].stringValue
            let po_id  = result["po_id"].stringValue
            let created_at = result["created_at"].stringValue
            var status = result["status"].stringValue
            //status = calculate_Status_Stringvalue(status:status)
            let quote_increment_id = result["quote_increment_id"].stringValue
            let product = ["po_increment_id":po_increment_id,"quote_id":quote_id,"po_id":po_id,"created_at":created_at,"status":status,"quote_increment_id":quote_increment_id]
            self.po_info.append(product)
        }
        tableview.delegate = self
        tableview.dataSource = self
        print(po_info)
    }
    
    
    
    //    MARK : table view
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ced_ViewPoCell") as! ced_ViewPoCell
        cell.po_increment_id.text = po_info[indexPath.row]["po_increment_id"]
        cell.QuoteIncrementId.text = po_info[indexPath.row]["quote_increment_id"]
        cell.CreatedAt.text = po_info[indexPath.row]["created_at"]
        cell.status.text = self.statusArray[po_info[indexPath.row]["status"] ?? ""]
        cell.QuoteId.text = po_info[indexPath.row]["quote_id"]
        return cell
    }
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return po_info.count
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return 180
    }
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let navigation = self.sideDrawerViewController?.mainViewController as! ced_navigationBarController
        let stoy = UIStoryboard(name: "Main", bundle: nil)
        let viewControl = stoy.instantiateViewController(withIdentifier: "ced_PO_AccountInformation_RFQ") as! PO_AccountInformation_RFQ
        viewControl.po_id = po_info[indexPath.row]["po_id"]!
        viewControl.pinfo = po_info[indexPath.row]["po_increment_id"]!
        viewControl.from_po = true
        navigation.pushViewController(viewControl, animated: true)
        return
    }
    
    
    
    
    //    MARK: Filter Functions
    
    @IBAction @objc func FilterButtonPressed(_ sender: Any) {
        let bgCView = UIView();
        bgCView.tag=151;
        tempView1=bgCView
        bgCView.frame = UIScreen.main.bounds;
        //bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let filterbyView = ced_PO_Filter();
        self.addgesturesTohideView(self.view)
        if(filterPostData.count != 0){
            if let st = filterPostData.object(forKey: "check_status") as? String{
                filterbyView.Status.setTitle(st, for: UIControl.State.normal)
            }
        }
        filterbyView.ResetFilter.setTitle("Reset".localized, for: .normal)
        filterbyView.Filter.setTitle("Filter".localized, for: .normal)
        filterbyView.Status.addTarget(self, action: #selector(self.showstatus(sender:)), for: .touchUpInside)
        filterbyView.Filter.addTarget(self, action: #selector(self.applyFilters(sender:)), for: .touchUpInside)
        filterbyView.ResetFilter.addTarget(self, action: #selector(self.resetFilters(sender:)), for: .touchUpInside)
        filterbyView.tag = 181;
        filterbyView.CreatedAtFrom.delegate = self
        filterbyView.createdAtTo.delegate = self
        //        filterbyView.CreatedAt.addTarget(self, action: #selector(dateclicked1(_:)), for: .allEditingEvents)
//        pickerview.date.addTarget(self, action:#selector(datePickerChanged(_:)) , for: .valueChanged)
//        pickerview.done.addTarget(self, action: #selector(donebuttonclick), for: .touchUpInside)
//        pickerview.done.backgroundColor = Ced_CommonVendor.UIColorFromRGB(colorString)
        filterbyView.backgroundColor = UIColor.black;
        
        filterbyView.frame = .init(x: 0, y: 0, width: bgCView.frame.width - 60 ,height: 250)//CGRect(x:25, y:100, width:self.view.frame.width-50 ,height: 0)
        filterbyView.center = self.view.center
        bgCView.addSubview(filterbyView)
        self.view.addSubview(bgCView);
        //        filterbyView.anchor(width: bgCView.frame.width - 32, height: 150)
        //        filterbyView.center(inView: bgCView)
        SetFilerView()
    }
    
    
    @objc func applyFilters(sender:UIButton){
        po_info = []
        self.tableview.reloadData()
        self.applyFilter = true
        if let view = self.view.viewWithTag(181) as? ced_PO_Filter {
            
            if(view.POIncrementId.text != nil){
                POIncrementId =  view.POIncrementId.text!
            }
            
            if(view.quoteID.text != nil){
                QuoteId = view.quoteID.text!
            }
            
            
            
            
            
            if(view.Status.titleLabel!.text != nil){
                if view.Status.titleLabel!.text!.lowercased() != "Select Status".localized.lowercased() {
                    status = view.Status.titleLabel!.text!
                    status = calculate_Status_Intvalue(status: status)
                    print(status)
                    //                    status = calculate_Status_Intvalue(status: status)
                }
            }
            
            
            if(view.CreatedAtFrom.text != nil){
                CreatedAtFrom = view.CreatedAtFrom.text!
            }
            if(view.createdAtTo.text != nil){
                CreatedAtTo = view.createdAtTo.text!
            }
            filter = "";
            filter += "{";
            filter += "\""+"created_at"+"\":{\"from\":\""+CreatedAtFrom+"\",\"to\":\""+CreatedAtTo+"\"},";
            filter += "\""+"po_increment_id"+"\":\""+POIncrementId+"\",";
            filter += "\""+"quote_id"+"\":\""+QuoteId+"\",";
            filter += "\""+"status"+"\":\""+status+"\"}";
            
            //            filter:{"po_increment_id":"30","po_customer_id":"","po_price":"","po_qty":"","status":"","created_at":{"from":"","to":""}}quote_increment_id
            self.view.viewWithTag(151)?.removeFromSuperview();
            self.loadData(page: 1)
        }
    }
    
    
    
    
    @objc func resetFilters(sender:UIButton){
        POIncrementId = ""
        QuoteId  = ""
        status = ""
        CreatedAtFrom  = ""
        CreatedAtTo = ""
        self.applyFilter = false
        filterPostData = NSDictionary()
        self.view.viewWithTag(151)?.removeFromSuperview();
        po_info = []
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
    
    //    MARK: Date picker for filters
    
    //    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
    //
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
    
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        //gestureRecognizer.delegate = self;
        if touch.view!.isDescendant(of: tableview)
        {
            return false;
        }
        if let innerView = self.view.viewWithTag(181) as? ced_PO_Filter {
            if(touch.view!.isDescendant(of: innerView) )
            {
                return false;
            }
            return true;
        }
        return true;
    }
    
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
        var valuestatus = ""
        print(status)
        for item in 0 ..< dropDownkey.count
        {
            if dropDownkey[item] == status
            {
                valuestatus = dropDownData[item]
                print(valuestatus)
                return valuestatus
            }
        }
        return ""
    }
    
    
    
    
    
    @objc func SetFilerView()
    {
        if let view = self.view.viewWithTag(181) as? ced_PO_Filter
        {
            
            if(view.POIncrementId.text != nil){
                view.POIncrementId.text!=POIncrementId
            }
            
            if(view.quoteID.text != nil){
                view.quoteID.text!=QuoteId
            }
            
            
            status = calculate_Status_Stringvalue(status: status)
            if(view.Status.titleLabel!.text != nil){
                view.Status.setTitle(status, for: .normal)
            }
            
            //
            //            if(view.CreatedAt.text != nil){
            //                CreatedAt = view.CreatedAt.text!
            //            }
        }
    }
    
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


