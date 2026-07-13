//
//  ced_creditmemoListing.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 30/06/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
import RxSwift
import RxCocoa

class ced_creditmemoListing: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate {
    

    @IBOutlet weak var toplsbel: UILabel!
    @IBOutlet weak var topContainer: UIView!
    @IBOutlet weak var showFilter: UIButton!
    @IBOutlet weak var creditmemoTable: UITableView!
    var creditMemo  = [[String:String]]()
    let refreshControl = UIRefreshControl()
    var disposeBag = DisposeBag()
    let filterbyView = ced_creditmemofilter();
    
    var filterString = String()
    var applyFilter = false
    var filterPostData = NSDictionary()
    var page = 1
    var loading = true
    var status = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        filterbyView.status.setTitle("", for: .normal)
        toplsbel.text="Credit Memo List ".localized
        creditmemoTable.delegate = self
        creditmemoTable.dataSource = self
        creditmemoTable.estimatedRowHeight = 270
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let bodyString = Ced_CommonVendor.getInfoPlist("bodyColor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        topContainer.backgroundColor = color
        creditmemoTable.backgroundColor = Ced_CommonVendor.UIColorFromRGB(bodyString!)
        showFilter.addTarget(self, action: #selector(ced_creditmemoListing.showFilterPopup(_:)), for: UIControl.Event.touchUpInside)
        ced_navigationBarController().addNavButton(self,str:"no".localized)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        self.loadData(page)
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        creditmemoTable.refreshControl = refreshControl
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func refresh(_ sender: AnyObject) {
        creditMemo.removeAll()
        self.creditmemoTable.reloadData()
        self.page = 1
        self.loadData(self.page)
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return creditMemo.count
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = creditmemoTable.dequeueReusableCell(withIdentifier: "ced_creditmemoList") as? ced_creditmemocell
        cell?.t1.text=" Credit Memo #".localized
        cell?.t2.text = " " + "Created At".localized
        cell?.t3.text="" //"Bill to Name".localized
        cell?.t4.text=" Order #".localized
        cell?.t5.text=" Order Date".localized
        cell?.t6.text=" "+"Status".localized
        cell?.t7.text="Refunded".localized
        cell?.creditmemo.text = creditMemo[indexPath.row]["creditmemoID"]
        cell?.createdAt.text = creditMemo[indexPath.row]["creditmemoDate"]
        cell?.orderId.text = creditMemo[indexPath.row]["order_increment_id"]
        cell?.orderDate.text = creditMemo[indexPath.row]["orderDate"]
        cell?.billtoName.text = ""//creditMemo[indexPath.row]["billing_name"]
        cell?.status.text  = creditMemo[indexPath.row]["status"]
        cell?.refunded.text   = creditMemo[indexPath.row]["amount"]
        return cell!
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 270
    }
    
    @objc func loadData(_ page:Int){
        var baseUrl = "";
        baseUrl += "vorderapi/vcreditmemo/item/page/"+"\(page)"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        if(applyFilter){
            postString += "&filter=" + filterString//.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!
        }
        sendRequest(url: baseUrl, parameters: postString)
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
                self.loading = false
                let msg = json["data"]["message"].stringValue
                if(page == 1)
                {
                    creditMemo.removeAll()
                   // self.view.showToastMsg(msg)
                    self.creditmemoTable.setEmptyMessage(msg)
                }
            }
            self.creditmemoTable.reloadData()
        }
        
    }
    
    
    @objc func statusClicked(_ sender: UIButton)
    {
        let dropDown = DropDown();
        let statusArray = ["Pending_status".localized,"Refunded Status".localized,"Canceled".localized];
        let statusArrayValue = ["Pending","Refunded","Canceled"];
        var dropDownToShow = statusArray;
       // dropDownToShow = dropDownToShow.sorted();
        
        dropDown.dataSource = dropDownToShow;
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            self.status = statusArrayValue[index]
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            _ = dropDown.show();
        } else {
            _ = dropDown.hide();
        }
    }
    
    fileprivate func parseData(_ json:JSON){
        for result in json["data"]["creditmemo"].arrayValue{
            var data = [String:String]()
            data["status"] = result["state"].stringValue
            data["creditmemoID"] = result["increment_id"].stringValue
            data["order_increment_id"]  = result["order_increment_id"].stringValue
            data["billing_name"]  = result["billing_name"].stringValue
            data["amount"]       = result["vendor_payment"].stringValue
            data["creditmemoDate"]  = result["created_at"].stringValue
            data["orderDate"]    = result["order_created_at"].stringValue
            self.creditMemo.append(data)
        }
    }

    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let data = creditMemo[indexPath.row]
        let view = storyboard?.instantiateViewController(withIdentifier: "creditmemoView") as! ced_creditmemoView
        view.creditMemoId = data["creditmemoID"]!
        
      self.navigationController?.pushViewController(view, animated: true)
    }

    
    //Filters  start
    //SHOW FILTERS
    
    
    @objc func showFilterPopup(_ sender:UIButton){
        /* background view */
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
       self.addgesturesTohideView(self.view)
//        if(filterPostData.count != 0){
//            filterbyView.status.setTitle(filterPostData.object(forKey: "state") as? String, for: UIControl.State())
//            filterbyView.billingName.text = filterPostData.object(forKey: "billing_name") as? String
//            filterbyView.orderId.text = filterPostData.object(forKey: "order_increment_id") as? String
//            filterbyView.creditmemoId.text = filterPostData.object(forKey: "increment_id") as? String
//            filterbyView.orderDateFrom.text = (filterPostData.object(forKey: "order_created_at") as AnyObject).object(forKey: "from") as? String
//            
//            filterbyView.orderDateTo.text = (filterPostData.object(forKey: "order_created_at") as AnyObject).object(forKey: "to") as? String
//            
//            filterbyView.creditDateFrom.text = (filterPostData.object(forKey: "created_at") as AnyObject).object(forKey: "from") as? String
//            filterbyView.creditDateTo.text = (filterPostData.object(forKey: "created_at") as AnyObject).object(forKey: "to") as? String
//            
//            filterbyView.amountFrom.text = (filterPostData.object(forKey: "grand_total") as AnyObject).object(forKey: "from") as? String
//            filterbyView.amountTo.text = (filterPostData.object(forKey: "grand_total") as AnyObject).object(forKey: "to") as? String
//            if let title = filterPostData.object(forKey: "status") as? String
//            {
//                filterbyView.status.setTitle(title, for: .normal)
//            }
//            
//            
//        }
        filterbyView.status.addTarget(self, action: #selector(statusClicked(_:)), for: .touchUpInside)

        filterbyView.filter.addTarget(self, action: #selector(ced_creditmemoListing.applyFilters(_:)), for: UIControl.Event.touchUpInside)
        filterbyView.ResetFilter.addTarget(self, action: #selector(ced_creditmemoListing.resetFilters(_:)), for: UIControl.Event.touchUpInside)
        
        filterbyView.orderDateFrom.tag = 7776
        filterbyView.orderDateTo.tag = 7777
        filterbyView.creditDateFrom.tag = 7774
        filterbyView.creditDateTo.tag = 7775
        filterbyView.orderDateTo.delegate = self
        filterbyView.orderDateFrom.delegate = self
        filterbyView.creditDateFrom.delegate = self
        filterbyView.creditDateTo.delegate  = self
        
        filterbyView.tag = 181;
        filterbyView.backgroundColor = UIColor.black;
        filterbyView.frame = CGRect(x: 25, y: 100, width: self.view.frame.width - 50, height: 305)
        filterbyView.center = self.view.center
        bgCView.addSubview(filterbyView)
        self.view.addSubview(bgCView);
        
    }
    @objc func applyFilters(_ sender:UIButton){
        creditMemo = [[String:String]]()
        self.creditmemoTable.reloadData()
        self.applyFilter = true
        
        var createdAtfr = ""
        if(!filterbyView.orderDateFrom.text!.isEmpty){
            createdAtfr =  filterbyView.orderDateFrom.text!
        }
        var createdAtTo  = ""
        if(!filterbyView.orderDateTo.text!.isEmpty){
            createdAtTo = filterbyView.orderDateTo.text!
        }
        var billingName = ""
        if(!filterbyView.billingName.text!.isEmpty){
            billingName = filterbyView.billingName.text!
        }
        var invoiceId = ""
        if(!filterbyView.creditmemoId.text!.isEmpty){
            invoiceId = filterbyView.creditmemoId.text!
        }
        var orderId = ""
        if(!filterbyView.orderId.text!.isEmpty){
            orderId = filterbyView.orderId.text!
        }
        
        var amountFro  = ""
        if(!filterbyView.amountFrom.text!.isEmpty){
            amountFro = filterbyView.amountFrom.text!
        }
        var amountTo = ""
        if(!filterbyView.amountTo.text!.isEmpty){
            amountTo = filterbyView.amountTo.text!
        }
        var invoiceCreatefr = ""
        if(!filterbyView.creditDateFrom.text!.isEmpty){
            invoiceCreatefr = filterbyView.creditDateFrom.text!
        }
        
        var invoiceCreateto  = ""
        if(!filterbyView.creditDateTo.text!.isEmpty){
            invoiceCreateto = filterbyView.creditDateTo.text!
        }
       
        let createdAt = ["to":createdAtTo,"from":createdAtfr]
        let amount = ["to":amountTo,"from":amountFro]
        let ordercreatew = ["to":invoiceCreateto,"from":invoiceCreatefr]
        
        let postdata = ["grand_total":amount,"created_at":ordercreatew,"order_increment_id":orderId,"increment_id":invoiceId,"order_created_at":createdAt,"billing_name":billingName,"state":status] as [String : Any]
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
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.loading = true;
        self.page = 1;
        self.loadData(page)
    }
    
    @objc func resetFilters(_ sender:UIButton){
        self.applyFilter = false
        filterPostData = NSDictionary()
        self.view.viewWithTag(151)?.removeFromSuperview();
        creditMemo = [[String:String]]()
        self.filterString = String()
        self.loading = true;
        self.page = 1;
        self.creditmemoTable.reloadData()
        self.loadData(page)
        
        filterbyView.status.setTitle("", for: UIControl.State())
        filterbyView.billingName.text = ""
        filterbyView.orderId.text = ""
        filterbyView.creditmemoId.text = ""
        filterbyView.orderDateFrom.text = ""
        filterbyView.orderDateTo.text = ""
        filterbyView.creditDateFrom.text = ""
        filterbyView.creditDateTo.text = ""
        filterbyView.amountFrom.text = ""
        filterbyView.amountTo.text = ""
        filterbyView.status.setTitle("", for: .normal)
    }
    
    //@objc function to dismiss the custom popover
    @objc func hideView(_ recognizer: UITapGestureRecognizer)
    {
        self.view.viewWithTag(151)?.removeFromSuperview();
    }
    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(ced_creditmemoListing.hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_creditmemoListing.hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_creditmemoListing.hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_creditmemoListing.hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_creditmemoListing.hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    //delegate @objc function to handle touch events on the custom popover
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        if(touch.view!.isDescendant(of: creditmemoTable)){
            return false
        }
        if let innerView = self.view.viewWithTag(181) as? ced_creditmemofilter {
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
                Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
                self.loadData(page);
                
            }
            
        }
        
    }
    

}

extension ced_creditmemoListing:UITextFieldDelegate{
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
