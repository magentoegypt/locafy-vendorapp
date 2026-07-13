//
//  bidViewController.swift
//  VenderApp
//
//  Created by MacMini on 23/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class bidViewController: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource, UIGestureRecognizerDelegate,UITextFieldDelegate {
    
    @IBOutlet weak var topLabel: UILabel!
    
    @IBOutlet weak var filterButton: UIButton!
    
    @IBOutlet weak var tableView: UITableView!
    
    var bid_detail=[[String:JSON]]()
    var auction_id=""
    var productId=""
    var pageNo=1
    override func viewDidLoad() {
        super.viewDidLoad()
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        topLabel.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        loadBids()
        filterButton.addTarget(self, action: #selector(filterButtonPressed(_:)), for: .touchUpInside)
        tableView.dataSource=self
        tableView.delegate=self
        // Do any additional setup after loading the view.
    }
    
    var flagForBids=true
    var bidsView=bidsFilterView()
    let bgCView=UIView()
    @objc func filterButtonPressed(_ sender: UIButton){
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        //bidsView=bidsFilterView()
        bidsView.frame=CGRect(x: 0, y: 0, width: bgCView.frame.width-30, height: 377)
        bidsView.center=bgCView.center
        bidsView.tag=121;
        
        
        bidsView.bidPrice.addTarget(self, action:#selector(isValidNmber(_:)),for: .editingDidEnd)
        bidsView.productId.addTarget(self, action:#selector(isValidNmber(_:)),for: .editingDidEnd)
        bidsView.customerId.addTarget(self, action:#selector(isValidNmber(_:)),for: .editingDidEnd)
        bidsView.customerName.addTarget(self, action:#selector(isValidNme(_:)),for: .editingDidEnd)
        
        bidsView.bidDate.delegate=self
        bidsView.applyButton.addTarget(self, action: #selector(applyButtonPressed(_:)), for: .touchUpInside)
        bidsView.clearButton.addTarget(self, action: #selector(clearButtonPressed(_:)), for: .touchUpInside)
        self.addgesturesTohideView(self.view)
        
        bgCView.addSubview(bidsView)
        self.view.addSubview(bgCView)
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
    var filter=""
    @objc func applyButtonPressed(_ sender: UIButton){
        
        
        filter = "";
        filter += "{";
        filter += "\""+"product_id"+"\":\""+bidsView.productId.text!+"\",";
        filter += "\""+"customer_id"+"\":\""+bidsView.customerId.text!+"\",";
        filter += "\""+"customer_name"+"\":\""+bidsView.customerName.text!+"\",";
        filter += "\""+"bid_price"+"\":\""+bidsView.bidPrice.text!+"\",";
        filter += "\""+"bid_time"+"\":\""+bidsView.bidTime.text!+"\",";
        filter += "\""+"winner"+"\":\""+bidsView.winner.text!+"\",";
        filter += "\""+"bid_date"+"\":\""+bidsView.bidDate.text!.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!+"\",";
        
        filter += "\""+"status"+"\":\""+bidsView.status.text!+"\"}";
        pageNo=1
        bid_detail=[[String:JSON]]()
        flagForBids=true
        loadBids()
        self.view.viewWithTag(121)?.removeFromSuperview()
        self.view.viewWithTag(151)?.removeFromSuperview();
        
    }
    
    
    
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        print("TOUCHED")
        if touch.view!.isDescendant(of: bgCView)
        {
            return false;
        }
        
        if(touch.view!.isDescendant(of: bidsView) )
        {
            return false;
        }
        return true;
        if let innerView = self.view.viewWithTag(181) as? bidsFilterView {
            if(touch.view!.isDescendant(of: innerView) )
            {
                return false;
            }
            return true;
        }
        
    }
    @objc func clearButtonPressed(_ sender: UIButton){
        bidsView.id.text=""
        bidsView.productId.text=""
        bidsView.customerId.text=""
        bidsView.customerName.text=""
        bidsView.bidPrice.text=""
        bidsView.bidDate.text=""
        bidsView.bidTime.text=""
        bidsView.winner.text=""
        bidsView.status.text=""
        filter = ""
        pageNo=1
        bid_detail=[[String:JSON]]()
        flagForBids=true
        loadBids()
        self.view.viewWithTag(121)?.removeFromSuperview()
        self.view.viewWithTag(151)?.removeFromSuperview();
    }
    var datePickerView = UIDatePicker();
    let screenSize: CGRect = UIScreen.main.bounds;
    
    var textFieldForDate=UITextField()
    @objc func textFieldDidBeginEditing(_ textField: UITextField)
    {
        textField.resignFirstResponder();
        textFieldForDate=textField
        
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
        
        textFieldForDate.resignFirstResponder();
        
        self.view.viewWithTag(161)?.removeFromSuperview();
    }
    
    
    @objc func pickDateFromDatePickerView(_ sender:UIButton)
    {
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = DateFormatter.Style.short
        dateFormatter.timeStyle = DateFormatter.Style.none
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd";
        textFieldForDate.text = dateFormatter.string(from: datePickerView.date);
        textFieldForDate.resignFirstResponder();
        
        self.view.viewWithTag(161)?.removeFromSuperview();
    }
    @objc func loadBids(){
        if flagForBids{
            let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&auction_id="+auction_id+"&product_id="+productId+"&page=\(pageNo)&filter="+filter
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
            self.sendRequest(url: "vauctionapi/bid/view", parameters: postString)
            //pageNo+=1
        }
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            if requestUrl=="vauctionapi/bid/view"{
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if json["data"]["success"].stringValue=="true"{
                    
                    if let bidDetail=json["data"]["bid_detail"].array{
                        for item in bidDetail{
                            bid_detail.append(item.dictionary!)
                        }
                        pageNo += 1;
                        tableView.restore()
                        tableView.reloadData()
                    }
                }
                else
                {
                    flagForBids=false
                    if(pageNo == 1)
                    {
                      //  self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                        self.tableView.setEmptyMessage(json["data"]["message"].stringValue)
                        
                    }
                    //ShowSimpleAlert.showSimpleAlert(self, showTitle: "", showMsg: json["data"]["message"].stringValue)
                    
                }
            }
            else if requestUrl=="vauctionapi/bid/approved" || requestUrl=="vauctionapi/bid/disapproved"{
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if json["data"]["success"].stringValue=="true"{
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                    Ced_CommonVendor.delay(2.0, closure: {
                        self.pageNo=1
                        self.bid_detail=[[String:JSON]]()
                        self.flagForBids=true
                        self.loadBids()
                    })
                }
            }
        }
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return bid_detail.count
    }
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell=tableView.dequeueReusableCell(withIdentifier: "bidCell") as! bidCell
        let data=bid_detail[indexPath.row]
        cell.productId.text=data["product_id"]?.stringValue
        cell.customerId.text=data["customer_id"]?.stringValue
        cell.customerName.text=data["customer_name"]?.stringValue
        cell.bidPrice.text=data["bid_price"]?.stringValue
        cell.bidDate.text=data["bid_date"]?.stringValue
        cell.bidTime.text=data["bid_time"]?.stringValue
        cell.winner.text=data["winner"]?.stringValue
        cell.status.text=data["status"]?.stringValue
        cell.approvedButton.tag=Int((data["id"]?.stringValue)!)!
        cell.disapprovedButton.tag=Int((data["id"]?.stringValue)!)!
        cell.approvedButton.addTarget(self, action: #selector(approvedButtonPressed(_:)), for: .touchUpInside)
        cell.disapprovedButton.addTarget(self, action: #selector(disapprovedButtonPressed(_:)), for: .touchUpInside)
        cell.viewForCard.cardView()
        if indexPath.row==bid_detail.count-1{
            
        }
        
        return cell
    }
    @objc func approvedButtonPressed(_ sender: UIButton){
        //vauctionapi/bid/approved
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&bid_id=\(sender.tag)"
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: "vauctionapi/bid/approved", parameters: postString)
        
        
    }
    @objc func disapprovedButtonPressed(_ sender: UIButton){
        //vauctionapi/bid/disapproved
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&bid_id=\(sender.tag)"
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: "vauctionapi/bid/disapproved", parameters: postString)
    }
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 400
    }
    
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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

