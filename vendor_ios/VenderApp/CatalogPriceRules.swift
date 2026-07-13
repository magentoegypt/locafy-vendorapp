//
//  ced_PromotionalViewControler.swift
//  VenderApp
//
//  Created by Macmini on 11/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class CatalogPriceRules: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate
{
    
    var ShoppingCartPriceRules = false
    
    @IBOutlet weak var headerlable: UILabel!
    @IBOutlet weak var applynewRuleButton: UIButton!
    @IBOutlet weak var applyrulesbutton: UIButton!
    @IBOutlet weak var tableview: UITableView!
    
    var applyFilter = false
    var CatalogRuleListing=[[String:String]]()
    var filterPostData = NSDictionary()
    var filterString = String()
    var filter=""
    let bgCView = UIView();
    let tempView=UIView() /////datepicker
    var datetextfeild = UITextField()
    let pickerview = ced_datepicker()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        loadData()
        tableview.delegate=self
        tableview.dataSource=self
        if ShoppingCartPriceRules == true
        {
            headerlable.text = "Shopping Cart Price Rules"
            applyrulesbutton.isHidden=true
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        
    }
    
    
    @IBAction @objc func FilterButtonPressed(_ sender: UIButton) {
        
        /* background view */
        
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let filterbyView = CatalogPriceRulesXib();
        self.addgesturesTohideView(self.view)
        if(filterPostData.count != 0){
            if let st = filterPostData.object(forKey: "check_status") as? String{
                filterbyView.status.setTitle(st, for: UIControl.State.normal)
            }
        }
        filterbyView.status.addTarget(self, action: #selector(self.showstatus(sender:)), for: .touchUpInside)
        filterbyView.filterbutton.addTarget(self, action: #selector(self.applyFilters(sender:)), for: .touchUpInside)
        filterbyView.resetbutton.addTarget(self, action: #selector(self.resetFilters(sender:)), for: .touchUpInside)
        filterbyView.tag = 181;
        filterbyView.backgroundColor = UIColor.black;
        filterbyView.frame = CGRect(x:25, y:100, width:self.view.frame.width - 50,height: 350)
        filterbyView.center = self.view.center
        if #available(iOS 10.0, *) {
            filterbyView.endto.addTarget(self, action: #selector(dateclicked1(_:)), for: .editingDidBegin)
        } else {
            // Fallback on earlier versions
        }
        if #available(iOS 10.0, *) {
            filterbyView.endfrom.addTarget(self, action: #selector(dateclicked1(_:)), for: .editingDidBegin)
        } else {
            // Fallback on earlier versions
        }
        if #available(iOS 10.0, *) {
            filterbyView.startfrom.addTarget(self, action: #selector(dateclicked1(_:)), for: .editingDidBegin)
        } else {
            // Fallback on earlier versions
        }
        if #available(iOS 10.0, *) {
            filterbyView.startto.addTarget(self, action: #selector(dateclicked1(_:)), for: .editingDidBegin)
        } else {
            // Fallback on earlier versions
        }
        pickerview.date.addTarget(self, action:#selector(datePickerChanged(_:)) , for: .valueChanged)
        pickerview.done.addTarget(self, action: #selector(donebuttonclick), for: .touchUpInside)
        bgCView.addSubview(filterbyView)
        self.view.addSubview(bgCView);
        
    }
    @objc func showstatus(sender:UIButton){
        let dropDown = DropDown();
        dropDown.dataSource = ["Active","Inactive"];
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
    @objc func applyFilters(sender:UIButton){
        CatalogRuleListing = []
        self.applyFilter = true
        if let view = self.view.viewWithTag(181) as? CatalogPriceRulesXib {
            
            
            
            var id = ""
            if(view.id.text != nil){
                id =  view.id.text!
            }
            var rule  = ""
            if(view.rule.text != nil){
                rule = view.rule.text!
            }
            
//            var website = ""
//            if(view.website.text != nil){
//                website = view.website.text!
//            }
            var startto = ""
            if(view.startto.text != nil){
                startto = view.startto.text!
            }
            
            var status = ""
            if(view.status.titleLabel!.text != nil){
                if view.status.titleLabel!.text!.lowercased() != "Select Status".lowercased() {
                    status = view.status.titleLabel!.text!
                    if status == "Active"
                    {
                        status = "1"
                    }
                    else
                    {
                        status = "0"
                    }
                }
            }
            
            var startfrom  = ""
            if(view.startfrom.text != nil){
                startfrom = view.startfrom.text!
            }
            var endfrom = ""
            if(view.endfrom.text != nil){
                endfrom = view.endfrom.text!
            }
            var endto = ""
            if(view.endto.text != nil){
                endto = view.endto.text!
            }
            
            
            filter = "";
            filter += "{";
            filter += "\""+"End"+"\":{\"from\":\""+startfrom+"\",\"to\":\""+startto+"\"},";
            filter += "\""+"Start"+"\":{\"from\":\""+endfrom+"\",\"to\":\""+endto+"\"},";
            filter += "\""+"Id"+"\":\""+id+"\",";
            filter += "\""+"Name"+"\":\""+rule+"\",";
            filter += "\""+"status"+"\":\""+status+"\"}";
            self.view.viewWithTag(151)?.removeFromSuperview();
            self.loadData()
        }
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
    
    @objc func hideView(recognizer: UITapGestureRecognizer)
    {
        self.view.viewWithTag(151)?.removeFromSuperview();
    }
    
    @objc func resetFilters(sender:UIButton){
        self.applyFilter = false
        filterPostData = NSDictionary()
        self.view.viewWithTag(151)?.removeFromSuperview();
        CatalogRuleListing = []
        self.filter = String()
        self.loadData()
    }
    //    MARK: all Request Responce Functions
    @objc func loadData(){
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
//        192.168.0.217/training/m2_2/vpromotionapi/promotionrules/listing
        var baseUrl = ""
        if ShoppingCartPriceRules == false
        {
            baseUrl = "vpromotionapi/promotionrules/listing"
        }
        else
        {
            baseUrl = "vpromotionapi/catalogrule/listing"
        }
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        if(applyFilter){
            postString += "&filter="+filter.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!;
        }
        print(postString)
        sendRequest(url: baseUrl, parameters: postString);
    }
    
    
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?)
    {
        print(response!)
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            if(requestUrl=="vpromotionapi/promotionrules/listing") || (requestUrl=="vpromotionapi/catalogrule/listing")
            {
                print("HELLO JSON")
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["status"].stringValue == "true"){
                    self.parseCatalogRuleListing(json)
                    print(self.CatalogRuleListing)
                    if CatalogRuleListing.count == 0
                    {
                        //self.view.showToastMsg("No Data Found")
                        self.tableview.setEmptyMessage("No Data Found".localized)
                        return
                    }
                    tableview.restore()
                    tableview.reloadData()
                }else if json["data"]["success"] == false
                {
                    self.view.showToastMsg(json["data"]["message"].stringValue)
                    self.tableview.setEmptyMessage(json["data"]["message"].stringValue)
                }
                else
                {
                    self.view.showToastMsg("Problem in loading Data")
                }
            }
        }
        
    }
    
    func parseCatalogRuleListing(_ json:JSON){
        for result in json["data"]["list"].arrayValue{
            let status = result["status"].stringValue
            let Start = result["Start"].stringValue
            let Name = result["Name"].stringValue
            let website  = result["website"][0].string
            let End = result["End"].stringValue
            let Id = result["Id"].stringValue
            var priority = ""
            var Coupon_code = ""
            if ShoppingCartPriceRules == true
            {
                priority=result["priority"].stringValue
                Coupon_code=result["Coupon_code"].stringValue
                let product = ["status":status,"Start":Start,"Name":Name,"Id":Id,"website":website,"End":End,"priority":priority,"Coupon_code":Coupon_code]
                self.CatalogRuleListing.append(product as! [String : String])
            }
            else
            {
                let product = ["status":status,"Start":Start,"Name":Name,"Id":Id,"website":website,"End":End]
                self.CatalogRuleListing.append(product as! [String : String])
            }
            
        }
    }
    
    
    //    MARK: table view Delegate
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return CatalogRuleListing.count
    }
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        if ShoppingCartPriceRules == false
        {
            return 230
        }
        else
        {
            return 310
        }
        
    }
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "CatalogPriceRulesCell") as! CatalogPriceRulesCell
        
        if ShoppingCartPriceRules == false
        {
            cell.priorityheight.constant = 0
            cell.priorityheightt.constant = 0
            cell.coupancodeheight.constant = 0
            cell.coupancodeheightt.constant = 0
        }
        else
        {
            cell.priorityheight.constant = 30
            cell.priorityheightt.constant = 30
            cell.coupancodeheight.constant = 30
            cell.coupancodeheightt.constant = 30
            cell.priority.text = CatalogRuleListing[indexPath.row]["priority"]
            cell.coupancode.text = CatalogRuleListing[indexPath.row]["Coupon_code"]
        }
        
        
        
        cell.id.text=CatalogRuleListing[indexPath.row]["Id"]
        cell.end.text=CatalogRuleListing[indexPath.row]["End"]
        
        cell.start.text=CatalogRuleListing[indexPath.row]["Start"]
        cell.rule.text=CatalogRuleListing[indexPath.row]["Name"]
        if var status = CatalogRuleListing[indexPath.row]["status"]
        {
            if status == "1"
            {
                status = "Actice"
            }
            else
            {
                status = "Inactive"
            }
            cell.status.text = status
        }
        if var website = CatalogRuleListing[indexPath.row]["website"]
        {
            if website == "1"
            {
                website = "Main Website"
            }
            else
            {
                website = "Default Website"
            }
            cell.website.text=website
        }
        return cell
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
        print(datetextfeild.text)
    }
    
    @objc func datePickerChanged(_ sender:UIDatePicker)
    {
        let dateformatter=DateFormatter()
        dateformatter.dateFormat="yyyy-MM-dd"
        //dd/MM/yyyy
        
        let date1=dateformatter.string(from: sender.date)
        
        print("abhinav"+date1)
        datetextfeild.text=date1
    }
    
    
    @available(iOS 10.0, *)
    @objc func dateclicked1(_ sender:UITextField)
    {
        datetextfeild=sender
        pickerview.date.datePickerMode = UIDatePicker.Mode.date
        pickerview.date.maximumDate = Date();
        tempView.frame=CGRect(x: 0, y: 0, width: self.view.frame.width-50, height: self.view.frame.height)
        tempView.center = self.view.center
        pickerview.view.layer.cornerRadius = 15
        pickerview.view.layer.borderWidth=0
        pickerview.frame=CGRect(x: 0, y: 0, width: 600, height: 200)
        pickerview.center=tempView.center
        tempView.backgroundColor = UIColor(displayP3Red: 0, green: 0, blue: 0, alpha: 0.3) //UIColor(colorLiteralRed: 0, green: 0, blue: 0, alpha: 0.3)
        tempView.addSubview(pickerview)
        self.bgCView.addSubview(tempView)
    }
}
