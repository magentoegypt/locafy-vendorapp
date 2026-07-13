//
//  cedVendorTableRateShipping.swift
//  VenderApp
//
//  Created by cedcoss on 22/12/17.
//  Copyright © 2017 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedVendorTableRateShipping: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource, UIGestureRecognizerDelegate {

    @IBOutlet weak var addRateButton: UIButton!
    @IBOutlet weak var filterButton: UIButton!
    @IBOutlet weak var tableView: UITableView!
    var shippingData = [[String:String]]()
    var id = "";
    var destCountryId = "";
    var destZipCode = "";
    var conditionName = "";
    var conditionValue = "";
    var price = "";
    var filter = "";
    var page = 1;
    var loading = true;
    
    override func viewDidLoad() {
        super.viewDidLoad()
        filterButton.addTarget(self, action: #selector(filterButtonPressed(_:)), for: .touchUpInside);
           ced_navigationBarController().addNavButton(self,str:"no")
        addRateButton.addTarget(self, action: #selector(addRateClicked(_:)), for: .touchUpInside);
        tableView.delegate = self
        tableView.dataSource = self
        self.fetchTableRateShipping()
    }
    
    @objc func fetchTableRateShipping(){
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
       
        let baseURL = "vendorapi/tablerate/item";
 
        var postStringToSend = "vendor_id="+vendorId+"&hashkey="+hashKey+"&page=\(page)";
        if(filter != "")
        {
            filter = filter.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!;
            postStringToSend += "&filter=\(filter)";
        }
          Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: baseURL, parameters: postStringToSend)
    }

   override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data {
            var json = try! JSON(data:data)
            Alert_File.removeLoadingIndicator(self)
            print(json)
            json = json["data"];
            if json["success"].stringValue == "true" {
                for rate in json["rate_list"].arrayValue {
                    var temp = [String:String]()
                    temp["id"] = rate["id"].stringValue
                    temp["vendor_id"] = rate["vendor_id"].stringValue
                    temp["dest_region"] = rate["dest_region"].stringValue
                    temp["dest_zip"] = rate["dest_zip"].stringValue
                    temp["dest_country"] = rate["dest_country"].stringValue
                    temp["website_id"] = rate["website_id"].stringValue
                    
                    temp["price"] = rate["price"].stringValue
                    temp["condition_name"] = rate["condition_name"].stringValue
                    temp["dest_region_id"] = rate["dest_region_id"].stringValue
                    temp["dest_country_id"] = rate["dest_country_id"].stringValue
                    temp["condition_value"] = rate["condition_value"].stringValue
                    temp["cost"] = rate["cost"].stringValue
                    
                    shippingData.append(temp)
                }
                tableView.restore()
                tableView.reloadData()
                
            }else{
                if(page != 1)
                {
                    loading = false;
                    return;
                }
              //  ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: json["message"].stringValue)
                self.tableView.setEmptyMessage(json["message"].stringValue)
            }
        }
    }
    
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return shippingData.count
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if let cell = tableView.dequeueReusableCell(withIdentifier: "vendorTable") as? cedVendorTableRateCell {
            let rate = shippingData[indexPath.row]
            cell.id.text = rate["id"]
            
             cell.price.text = rate["price"]
             cell.zipCode.text = rate["dest_zip"]
             cell.conditionCode.text = rate["condition_name"]
             cell.conditionValue.text = rate["condition_value"]
             cell.destinationcountryId.text = rate["dest_country_id"]
             cell.destinationreregionId.text = rate["dest_region_id"]
            cell.makeCard(cell, cornerRadius: 2, color: .black, shadowOpacity: 0.4)
            return cell
        }
        return UITableViewCell()
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
        let dealFilterView = TableRateFilterView()
        dealFilterView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width - 20, height: 300);
        dealFilterView.center.x = self.view.center.x;
        dealFilterView.center.y = self.view.center.y+20;
        dealFilterView.tag = 181;
        self.addgesturesTohideView(self.view);
        dealFilterView.applyButton.setTitle("Filter", for: UIControl.State());
        dealFilterView.applyButton.addTarget(self, action: #selector(applyFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        
        dealFilterView.idLabel.text = id;
        dealFilterView.destinationCountryIdLabel.text = destCountryId;
        dealFilterView.destinationZipCodeLabel.text = destZipCode
        dealFilterView.conditionNameLabel.text = conditionName;
        dealFilterView.conditionValueLabel.text = conditionValue;
        dealFilterView.priceLabel.text = price;
        dealFilterView.clearButton.setTitle("Reset", for: UIControl.State());
        dealFilterView.clearButton.addTarget(self, action: #selector(resetFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        dealFilterView.clearButton.backgroundColor = color;
        dealFilterView.applyButton.backgroundColor = color;
        
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
        
        if let view = self.view.viewWithTag(181) as? TableRateFilterView
        {
            if(view.idLabel.text != nil){
                id = view.idLabel.text!;
            }
            if(view.destinationCountryIdLabel.text != nil){
                destCountryId = view.destinationCountryIdLabel.text!;
            }
            if(view.destinationZipCodeLabel.text != nil){
                destZipCode = view.destinationZipCodeLabel.text!;
            }
            if(view.conditionNameLabel.text != nil){
                conditionName = view.conditionNameLabel.text!;
            }
            if(view.conditionValueLabel.text != nil){
                conditionValue = view.conditionValueLabel.text!;
            }
            if(view.priceLabel.text != nil){
                price = view.priceLabel.text!;
            }
            filter = "";
            filter += "{";
            filter += "\""+"id"+"\":\""+id+"\",";
            filter += "\""+"condition_name"+"\":\""+conditionName+"\",";
            filter += "\""+"condition_value"+"\":\""+conditionValue+"\",";
            filter += "\""+"price"+"\":\""+price+"\",";
            filter += "\""+"dest_zip"+"\":\""+destZipCode+"\",";
            filter += "\""+"dest_country_id"+"\":\""+destCountryId+"\"}";
            
            print(filter);
            self.view.viewWithTag(151)?.removeFromSuperview();
            self.page = 1;
            shippingData = [[String:String]]();
            fetchTableRateShipping()
            
        }
        
    }
    
    @objc func resetFilterTapped(_ sender:UIButton)
    {
        print("resetFilterTapped");
        id="";
        conditionName="";
        conditionValue="";
        destCountryId="";
        destZipCode="";
        filter = String();
        self.page = 1;
        shippingData = [[String:String]]();
        self.view.viewWithTag(151)?.removeFromSuperview();
         fetchTableRateShipping()
    }
    
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let rate = shippingData[indexPath.row]["id"]
        if let vc = self.storyboard?.instantiateViewController(withIdentifier: "AddTableRate") as? AddTableRate
        {
            vc.id = rate!;
            self.navigationController?.pushViewController(vc, animated: true);
        }
    }
    
    @objc func addRateClicked(_ sender: UIButton)
    {
        if let vc = self.storyboard?.instantiateViewController(withIdentifier: "AddTableRate") as? AddTableRate
        {
            self.navigationController?.pushViewController(vc, animated: true);
        }
    }
    
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
                
                self.page += 1;
                fetchTableRateShipping()
                //mainTable.reloadData();
                
            }
            
        }
    }
    
    
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        //gestureRecognizer.delegate = self;
        if touch.view!.isDescendant(of: tableView)
        {
            return false;
        }
        if let innerView = self.view.viewWithTag(181) as? TableRateFilterView {
            if(touch.view!.isDescendant(of: innerView) )
            {
                return false;
            }
            return true;
        }
        return true;
    }

}
