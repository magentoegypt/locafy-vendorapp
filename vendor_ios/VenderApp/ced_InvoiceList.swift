//
//  ced_InvoiceList.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 30/06/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
import RxSwift
import RxCocoa

class ced_InvoiceList: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate {
    
    @IBOutlet weak var InvoiceListTable: UITableView!
    var invoiceData = [[String:String]]()
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var showFilter: UIButton!
    @IBOutlet weak var topLabel: UILabel!
    let refreshControl = UIRefreshControl()
    let disposeBag = DisposeBag()
    var orderId = String()
    var filterString = String()
    var applyFilter  = false
    var filterPostData = NSDictionary()
    var page = 1
    var loading = true
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        InvoiceListTable.delegate = self
        InvoiceListTable.dataSource = self
//        InvoiceListTable.estimatedRowHeight = 300
        showFilter.addTarget(self, action: #selector(ced_InvoiceList.showFilterPopup(_:)), for: UIControl.Event.touchUpInside)
        self.title = "Invoice List".localized
        topLabel.text="Invoice List".localized
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        containerView.backgroundColor = color
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        ced_navigationBarController().addNavButton(self,str:"no".localized)
        self.loadData(self.page)
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        InvoiceListTable.refreshControl = refreshControl
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func refresh(_ sender: AnyObject) {
        invoiceData = [[String:String]]()
        InvoiceListTable.reloadData()
        self.filterString = String()
        self.page = 1
        self.loadData(self.page)
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return invoiceData.count
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = InvoiceListTable.dequeueReusableCell(withIdentifier: "ced_invoiceList") as? ced_invoicelistCell
        cell?.invoice.text = invoiceData[indexPath.row]["invoiceID"]
        cell?.invoiceDate.text = invoiceData[indexPath.row]["invoiceDate"]
        cell?.order.text = invoiceData[indexPath.row]["order_increment_id"]
        cell?.orderdate.text = invoiceData[indexPath.row]["orderDate"]
        cell?.Billtoname.text = invoiceData[indexPath.row]["billToname"]
        cell?.status.text  = invoiceData[indexPath.row]["status"]
        cell?.amount.text   = invoiceData[indexPath.row]["amount"]
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        cell?.view1.backgroundColor = DynamicColor.secondaryColor//Ced_CommonVendor.UIColorFromRGB(colorString)
        cell?.view2.backgroundColor = DynamicColor.secondaryColor//Ced_CommonVendor.UIColorFromRGB(colorString)
        return cell!
    }
    
    
    @objc func loadData(_ page:Int){
        var baseUrl = "";
        if(orderId != ""){
            baseUrl += "vorderapi/vorders/viewinvoice/page/"+"\(page)"
        }else{
            baseUrl += "vorderapi/vinvoice/item/page/"+"\(page)"
        }
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        if(orderId != ""){
            postString += "&order_id="+orderId
        }
        if(applyFilter){
            postString += "&filter=" + filterString//.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!
        }
        print(postString)
        sendRequest(url: baseUrl, parameters: postString);
    }
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        refreshControl.endRefreshing()
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if(json["data"]["success"].stringValue == "true"){
                self.parseData(json)
               
                print(json)
            }else{
                let msg = json["data"]["message"].stringValue
                loading = false
                if(self.page == 1)
                {
                   // self.view.showToastMsg(msg)
                    self.InvoiceListTable.setEmptyMessage(json["data"]["message"].stringValue)
                }
                
            }
            self.InvoiceListTable.reloadData()
        }
    }
    fileprivate func parseData(_ json:JSON){
        for result in json["data"]["invoicedata"].arrayValue{
            var data = [String:String]()
            data["status"] = result["state"].stringValue
            data["invoiceID"] = result["increment_id"].stringValue
            data["order_increment_id"] = result["order_increment_id"].stringValue
            data["billToname"] = result["billing_name"].stringValue
            data["amount"] = result["vendor_payment"].stringValue
            data["invoiceDate"] = result["created_at"].stringValue
            data["orderDate"] = result["order_created_at"].stringValue
            self.invoiceData.append(data)
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 220
    }
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let data = invoiceData[indexPath.row]
        let view = storyboard?.instantiateViewController(withIdentifier: "invoiceView") as! ced_invoiceView
        view.invoiceId = data["invoiceID"]!
        self.navigationController?.pushViewController(view, animated: true)
    }
    //SHOW FILTERS
    @objc func showFilterPopup(_ sender:UIButton){
        /* background view */
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let filterbyView = ced_invoiceFilter();
        self.addgesturesTohideView(self.view)
        //        if(filterPostData.count != 0){
        //        }
        
        filterbyView.billingName.addTarget(self, action:#selector(isValidNme(_:)), for: .editingDidEnd);
        filterbyView.invoiceId.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
        filterbyView.orderId.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
        
        filterbyView.amountFrom.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
        
        filterbyView.amountTo.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
        
        filterbyView.filter.addTarget(self, action: #selector(ced_InvoiceList.applyFilters(_:)), for: UIControl.Event.touchUpInside)
        filterbyView.ResetFilter.addTarget(self, action: #selector(ced_InvoiceList.resetFilters(_:)), for: UIControl.Event.touchUpInside)
        filterbyView.tag = 181;
        
        filterbyView.orderDateFrom.delegate = self
        filterbyView.orderDateTo.delegate = self
        filterbyView.orderDateFrom.tag = 7776;
        filterbyView.orderDateTo.tag = 7777;
        filterbyView.invoiceDateFrom.delegate = self
        filterbyView.invoiceDateTo.delegate   = self
        filterbyView.invoiceDateFrom.tag = 7774;
        filterbyView.invoiceDateTo.tag = 7775;
        
        filterbyView.backgroundColor = UIColor.black;
        filterbyView.frame = CGRect(x: 25, y: 100, width: self.view.frame.width - 50, height: 400)
        filterbyView.center = self.view.center
        bgCView.addSubview(filterbyView)
        self.view.addSubview(bgCView);
    }
    @objc func applyFilters(_ sender:UIButton){
        invoiceData = [[String:String]]()
        InvoiceListTable.reloadData()
        self.applyFilter = true
        let view = self.view.viewWithTag(181) as! ced_invoiceFilter
        var createdAtfr = ""
        if(view.orderDateFrom.text != nil){
            createdAtfr =  view.orderDateFrom.text!
        }
        var createdAtTo  = ""
        if(view.orderDateTo.text != nil){
            createdAtTo = view.orderDateTo.text!
        }
        var billingName = ""
        if(view.billingName.text != nil){
            billingName = view.billingName.text!
        }
        var invoiceId = ""
        if(view.invoiceId.text != nil){
            invoiceId = view.invoiceId.text!
        }
        var orderId = ""
        if(view.orderId.text != nil){
            orderId = view.orderId.text!
        }
        
        var amountFro  = ""
        if(view.amountFrom.text != nil){
            amountFro = view.amountFrom.text!
        }
        var amountTo = ""
        if(view.amountTo.text != nil){
            amountTo = view.amountTo.text!
        }
        var invoiceCreatefr = ""
        if(view.invoiceDateFrom.text != nil){
            invoiceCreatefr = view.invoiceDateFrom.text!
        }
        
        var invoiceCreateto  = ""
        if(view.invoiceDateTo.text != nil){
            invoiceCreateto = view.invoiceDateTo.text!
        }
        
       
        let createdAt = ["to":createdAtTo,"from":createdAtfr]
        let amount = ["to":amountTo,"from":amountFro]
        let ordercreatew = ["to":invoiceCreateto,"from":invoiceCreatefr]
        let postdata = ["grand_total":amount,"created_at":createdAt,"order_increment_id":orderId,"increment_id":invoiceId,"order_created_at":ordercreatew,"billing_name":billingName,"state":view.statusValue] as [String : Any]
        filterPostData = postdata as NSDictionary
        do{
            let JSONData = try JSONSerialization.data(
                withJSONObject: postdata ,
                options: JSONSerialization.WritingOptions(rawValue: 0))
            filterString = NSString(data: JSONData,
                                    encoding: String.Encoding.utf8.rawValue)! as String
            
        }
        catch{
            print("error in data encoding")
        }
        self.view.viewWithTag(151)?.removeFromSuperview();
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        self.page = 1;
        self.loadData(page)
        
    }
    
    @objc func resetFilters(_ sender:UIButton){
        self.applyFilter = false
        self.view.viewWithTag(151)?.removeFromSuperview();
        invoiceData = [[String:String]]()
        InvoiceListTable.reloadData()
        self.filterString = String()
        filterPostData = NSDictionary()
        self.page = 1;
        self.loadData(1)
    }
    
    //@objc function to dismiss the custom popover
    @objc func hideView(_ recognizer: UITapGestureRecognizer)
    {
        self.view.viewWithTag(151)?.removeFromSuperview();
    }
    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(ced_InvoiceList.hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_InvoiceList.hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_InvoiceList.hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_InvoiceList.hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_InvoiceList.hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    //delegate @objc function to handle touch events on the custom popover
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        if(touch.view!.isDescendant(of: InvoiceListTable))
        {
            return false;
        }
        if let innerView = self.view.viewWithTag(181) as? ced_invoiceFilter {
            if(touch.view!.isDescendant(of: innerView))
            {
                return false;
            }
            return true;
        }
        return true;
    }
    
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading && !self.refreshControl.isRefreshing{
                self.page += 1;
                Alert_File.addLoadingIndicator(self, msg: "Loading...".localized)
                self.loadData(page);
            }
        }
    }
}
extension ced_InvoiceList:UITextFieldDelegate{
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
