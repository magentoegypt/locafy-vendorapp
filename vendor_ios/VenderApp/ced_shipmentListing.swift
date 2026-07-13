//
//  ced_shipmentListing.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 01/07/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
import RxSwift
import RxCocoa
class ced_shipmentListing: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate {
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var showFilter: UIButton!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var shipmentTable: UITableView!
    var shipmentdata  = [[String:String]]()
    let refreshControl = UIRefreshControl()
    var disposeBag = DisposeBag()
    
    var applyFilter = false
    var filterString = String()
    var filterPostdata = NSDictionary()
    var loading = true
    var page  = 1
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        topLabel.text="Shipment List".localized
        shipmentTable.delegate = self
        shipmentTable.dataSource = self
//        shipmentTable.rowHeight = UITableView.automaticDimension
//        shipmentTable.estimatedRowHeight = 300
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        containerView.backgroundColor = color
        showFilter.addTarget(self, action: #selector(ced_shipmentListing.showFilterPopup(_:)), for: UIControl.Event.touchUpInside)
        ced_navigationBarController().addNavButton(self,str:"no".localized)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        self.loadData(1)
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        shipmentTable.refreshControl = refreshControl
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func refresh(_ sender: AnyObject) {
        filterPostdata = NSDictionary()
        shipmentdata = [[String:String]]()
        shipmentTable.reloadData()
        self.page = 1
        self.loadData(self.page)
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return shipmentdata.count
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = shipmentTable.dequeueReusableCell(withIdentifier: "ced_shipmentList") as? ced_shipmentcell
        cell?.shipmentId.text = shipmentdata[indexPath.row]["increment_id"]
        cell?.dateShipped.text = shipmentdata[indexPath.row]["created_at"]
        cell?.orderId.text = shipmentdata[indexPath.row]["order_increment_id"]
        cell?.orderDate.text = shipmentdata[indexPath.row]["order_created_at"]
        cell?.shipToname.text = shipmentdata[indexPath.row]["shipping_name"]
        cell?.shipmentQty.text = shipmentdata[indexPath.row]["total_qty"]
        return cell!
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 215
    }
    
    @objc func loadData(_ page:Int){
        let baseUrl = "vorderapi/vshipment/item/page/"+"\(page)"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        if(applyFilter){
            postString += "&filter=" + filterString//.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!
        }
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
                self.shipmentTable.reloadData()
                print(json)
            }else{
                let msg = json["data"]["message"].stringValue
               // self.view.showToastMsg(msg)
                self.shipmentTable.setEmptyMessage(json["data"]["message"].stringValue)
                loading = false
                /*let showTitle = "Error";
                let showMsg = json["data"]["message"].stringValue;
                
                let confirmationAlert = UIAlertController(title: showTitle, message: showMsg, preferredStyle: UIAlertControllerStyle.alert)
                confirmationAlert.addAction(UIAlertAction(title: "Dismiss", style: .default, handler: { (action: UIAlertAction!) in
                    self.navigationController?.popViewController(animated: true)
                    
                }));
                
                confirmationAlert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { (action: UIAlertAction!) in
                    //print("Handle Cancel Logic here")
                }))
                present(confirmationAlert, animated: true, completion: nil)*/
                self.shipmentTable.reloadData()
            }
        }
        
    }
    
    
    fileprivate func parseData(_ json:JSON){
        for result in json["data"]["shipmentdata"].arrayValue{
            var data = [String:String]()
            data["order_id"] = result["order_id"].stringValue
            data["entity_id"] = result["entity_id"].stringValue
            data["order_created_at"]  = result["order_created_at"].stringValue
            data["increment_id"]  = result["increment_id"].stringValue
            data["created_at"]    = result["created_at"].stringValue
            data["shipping_name"] = result["shipping_name"].stringValue
            data["vendor_id"]    = result["vendor_id"].stringValue
            data["total_qty"]    = result["total_qty"].stringValue
            data["shipment_status"]  = result["shipment_status"].stringValue
            data["store_id"]    = result["store_id"].stringValue
            data["order_increment_id"]    = result["order_increment_id"].stringValue
            self.shipmentdata.append(data)
        }
    }
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let data = shipmentdata[indexPath.row]
        let view = storyboard?.instantiateViewController(withIdentifier: "shipmentView") as? ced_shipmentView
        view?.shipmentId = data["increment_id"]!
    self.navigationController?.pushViewController(view!, animated: true)
    }
    
    
    //SHOW FILTERS
    @objc func showFilterPopup(_ sender:UIButton){
        /* background view */
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let filterbyView = ced_shipmentFilterView();
        self.addgesturesTohideView(self.view)
        if(filterPostdata.count != 0){
            filterbyView.dateShippedFrom?.text = (filterPostdata.object(forKey: "created_at") as AnyObject).object(forKey: "from") as? String
            filterbyView.dateShippedTo?.text = (filterPostdata.object(forKey: "created_at") as AnyObject).object(forKey: "to") as? String
            filterbyView.shippingName?.text = filterPostdata.object(forKey: "shipping_name") as? String
            filterbyView.orderDatefrom?.text = (filterPostdata.object(forKey: "order_created_at") as AnyObject).object(forKey: "from") as? String
            filterbyView.orderDateTo?.text = (filterPostdata.object(forKey: "order_created_at") as AnyObject).object(forKey: "to") as? String
            filterbyView.shipmentId?.text = filterPostdata.object(forKey: "increment_id") as? String
            filterbyView.orderId?.text = filterPostdata.object(forKey: "order_increment_id")  as? String
            filterbyView.shipmentQtyField?.text = filterPostdata.object(forKey: "order_qty")  as? String
         
        }

        filterbyView.filter.addTarget(self, action: #selector(ced_shipmentListing.applyFilters(_:)), for: UIControl.Event.touchUpInside)
        filterbyView.resetFilter.addTarget(self, action: #selector(ced_shipmentListing.resetFilters(_:)), for: UIControl.Event.touchUpInside)
        
        filterbyView.orderDateTo.delegate = self
        filterbyView.orderDatefrom.delegate = self
        filterbyView.dateShippedFrom.delegate = self
        filterbyView.dateShippedTo.delegate = self
        filterbyView.dateShippedFrom.tag = 7776
        filterbyView.dateShippedTo.tag = 7777
        filterbyView.orderDatefrom.tag = 7774
        filterbyView.orderDateTo.tag = 7775
        
        filterbyView.tag = 181;
        filterbyView.backgroundColor = UIColor.black;
        filterbyView.frame = CGRect(x: 20, y: 100, width: self.view.frame.width-40,  height: 295)
        filterbyView.center =  self.view.center
        bgCView.addSubview(filterbyView)
        self.view.addSubview(bgCView);
        
    }
    @objc func applyFilters(_ sender:UIButton){
        shipmentdata = [[String:String]]()
        self.shipmentTable.reloadData()
        
        self.applyFilter = true
        let view = self.view.viewWithTag(181) as! ced_shipmentFilterView
        var createdAtfr = ""
        if(view.orderDatefrom.text != nil){
            createdAtfr =  view.orderDatefrom.text!
        }
        var createdAtTo  = ""
        if(view.orderDateTo.text != nil){
            createdAtTo = view.orderDateTo.text!
        }
        var shipname = ""
        if(view.shippingName.text != nil){
            shipname = view.shippingName.text!
        }
        var shipmentId = ""
        if(view.shipmentId.text != nil){
            shipmentId = view.shipmentId.text!
        }
        
      
        var shipDatefrom = ""
        if(view.dateShippedFrom.text != nil){
            shipDatefrom = view.dateShippedFrom.text!
        }
        
        var shipDateto  = ""
        if(view.dateShippedTo.text != nil){
            shipDateto = view.dateShippedTo.text!
        }
        var orderId  = ""
        if(view.orderId.text != nil){
            orderId = view.orderId.text!
        }
        
        var orderQty = ""
        if(view.shipmentQtyField.text != nil)
        {
            orderQty = view.shipmentQtyField.text!;
        }
        
        let createdAt = ["to":createdAtTo,"from":createdAtfr]
        let shipdate = ["to":shipDateto,"from":shipDatefrom]
        let postdata = ["order_created_at":createdAt,"shipping_name":shipname,"created_at":shipdate,"order_increment_id":orderId,"increment_id":shipmentId, "total_qty":orderQty] as [String : Any]
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
        filterPostdata = postdata as NSDictionary
        self.view.viewWithTag(151)?.removeFromSuperview();
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        self.loadData(1)
        
    }
    
    @objc func resetFilters(_ sender:UIButton){
        applyFilter = false
        self.view.viewWithTag(151)?.removeFromSuperview();
        filterPostdata = NSDictionary()
        shipmentdata = [[String:String]]()
        self.shipmentTable.reloadData()
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
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
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(ced_shipmentListing.hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_shipmentListing.hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_shipmentListing.hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_shipmentListing.hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_shipmentListing.hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    //delegate @objc function to handle touch events on the custom popover
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        if let innerView = self.view.viewWithTag(181) as? ced_shipmentFilterView {
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
                Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
                self.loadData(page);
                
            }
            
        }
        
    }

}
extension ced_shipmentListing:UITextFieldDelegate{
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
