//
//  cedDealProducts.swift
//  VenderApp
//
//  Created by cedcoss on 05/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedDealProducts: ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource, UIGestureRecognizerDelegate {
    
    @IBOutlet weak var topheaderLable: UILabel!
    @IBOutlet weak var filterButton: UIButton!
    @IBOutlet weak var viewAllDeals: UIButton!
    var loading = true;
    var dropDown = DropDown();
    @IBOutlet weak var mainTable: UITableView!
    var page = 1;
    var id = "";
    var name = "";
    var type = "";
    var price = "";
    var qty = "";
    var status = "";
    var dealProducts = [[String:String]]()
    var filter = String();
    var productStatus = [String:String]();
    var productType = [String:String]();
    var deal_status = [String:String]()
    override func viewDidLoad() {
        super.viewDidLoad()
        mainTable.delegate = self;
        mainTable.dataSource = self;
        topheaderLable.setThemeColor()
        topheaderLable.textColor = .white
        topheaderLable.text = "  Create Vendor Deals".localized
        
        viewAllDeals.addTarget(self, action: #selector(viewAllDealsClicked(_:)), for: .touchUpInside);
        filterButton.addTarget(self, action: #selector(filterButtonPressed(_:)), for: .touchUpInside)
        viewAllDeals.setThemeColor()
        viewAllDeals.setTitle("View All Deals".localized, for: .normal)
        filterButton.setTitle("Filter".localized, for: .normal)
        filterButton.setThemeColor()
        loadProducts();
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
        self.sendRequest(url: "vdealapi/deal/listing", parameters: postString)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if(page == 1)
            {
                for index in json["data"]["type"].arrayValue
                {
                    productType[index["value"].stringValue] = index["key"].stringValue;
                }
                for index in json["data"]["status"].arrayValue
                {
                    productStatus[index["value"].stringValue] = index["key"].stringValue;
                }
                for index in json["data"]["deal_status"].arrayValue
                {
                    deal_status[index["value"].stringValue] = index["key"].stringValue;
                }
                
            }
            if(json["data"]["success"].stringValue == "true")
            {
                for product in json["data"]["vendordeal_productlist"].arrayValue
                {
                    dealProducts.append(["product_id":product["product_id"].stringValue,"product_name":product["product_name"].stringValue,"type":product["type"].stringValue,"deal_price":product["price"].stringValue,"qty":product["qty"].stringValue,"check_status":product["check_status"].stringValue])
                }
            }
            else
            {
                if(page == 1)
                {
                    dealProducts.removeAll()
                    mainTable.reloadData()
                  //  Alert_File.showValidationError(self,title:"Error".localized,message:json["data"]["message"].stringValue);
                    self.mainTable.setEmptyMessage(json["data"]["message"].stringValue)
                }
                loading = false;
                return
            }
            mainTable.restore()
            mainTable.reloadData()
            if(page == 1)
            {
                mainTable.scrollsToTop = true;
            }
        }
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1;
    }

    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dealProducts.count;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if let cell = tableView.dequeueReusableCell(withIdentifier: "cedDealListCell", for: indexPath) as? cedDealListCell
        {
            cell.prodIdLbl.text = "Product ID".localized
            cell.nameLbl.text = "Name".localized
            cell.typeLbl.text = "Type".localized
            cell.qtyLbl.text = "Qty".localized
            cell.statusLbl.text = "Status".localized
            cell.createDeal.setTitle("Create Deal".localized, for: .normal)
            
            let prod = dealProducts[indexPath.row]
            cell.productId.text = prod["product_id"];
            cell.name.text = prod["product_name"];
            cell.price.text = prod["deal_price"];
            cell.qty.text = prod["qty"];
            cell.type.text = prod["type"];
            
            cell.status.text = self.productStatus.someKey(forValue: prod["check_status"] ?? "")
            
            cell.createDeal.tag = indexPath.row;
            cell.createDeal.addTarget(self, action: #selector(addDealClicked(_:)), for: .touchUpInside);
            cell.createDeal.setThemeColor();
            cell.cellView.cardView()
            return cell
        }
        return UITableViewCell();
    }
    
    @objc func addDealClicked(_ sender: UIButton)
    {
        let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedCreateDeal") as! cedCreateDeal
        viewControl.productData = dealProducts[sender.tag];
        viewControl.productStatus = deal_status
        
        self.navigationController?.pushViewController(viewControl, animated: true)
    }
    
    @objc func viewAllDealsClicked(_ sender: UIButton)
    {
        if let vc = self.storyboard?.instantiateViewController(withIdentifier: "cedDealListing") as? cedDealListing
        {
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 280;
    }
    
    @objc func filterButtonPressed(_ sender:UIButton)
    {
        /* background view */
        
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let dealFilterView = DealFilterView();
        dealFilterView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width - 20, height: 300);
        dealFilterView.center.x = self.view.center.x;
        dealFilterView.center.y = self.view.center.y+20;
        
        self.addgesturesTohideView(self.view);
        dealFilterView.headingLabel.setThemeColor()
        dealFilterView.filterButton.setThemeColor()
        dealFilterView.resetButton.setThemeColor();
        dealFilterView.filterButton.setTitle("Filter", for: UIControl.State());
        dealFilterView.filterButton.addTarget(self, action: #selector(applyFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        
        dealFilterView.tag = 181;
        dealFilterView.productId.text = id;
        dealFilterView.productName.text = name;
        dealFilterView.productPrice.text = price;
        dealFilterView.productQty.text = qty;
        
          dealFilterView.productPrice.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
        dealFilterView.productId.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
        dealFilterView.productQty.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
        
        if(status != "")
        {
            dealFilterView.productStatus.setTitle(status, for: .normal)
        }
        if(type != "")
        {
            dealFilterView.productType.setTitle(type, for: .normal)
        }
        dealFilterView.resetButton.setTitle("Reset", for: UIControl.State());
        dealFilterView.resetButton.addTarget(self, action: #selector(resetFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        dealFilterView.productStatus.addTarget(self, action: #selector(productStatusDropDownClicked(_:)), for: .touchUpInside)
        dealFilterView.productType.addTarget(self, action: #selector(productTypeDropDownClicked(_:)), for: .touchUpInside)
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
        
        if let view = self.view.viewWithTag(181) as? DealFilterView
        {
            var statusCode = "";
            var types = "";
            if(view.productId.text != nil){
                id = view.productId.text!;
            }
            if(view.productName.text != nil){
                name = view.productName.text!;
            }
            if(view.productPrice.text != nil){
                price = view.productPrice.text!;
            }
            if(view.productQty.text != nil){
                qty = view.productQty.text!;
            }
            if(view.productType.currentTitle != "-----Select-----".localized){
                type = view.productType.currentTitle!;
                types = productType[type]!;
            }
            if(view.productStatus.currentTitle != "-----Select-----".localized)
            {
                status = view.productStatus.currentTitle!;
                statusCode = productStatus[status]!;
            }
            
            
            filter = "";
            filter += "{";
            filter += "\""+"qty"+"\":\""+qty+"\",";
            filter += "\""+"entity_id"+"\":\""+id+"\",";
            filter += "\""+"sku"+"\":\""+name+"\",";
            filter += "\""+"price"+"\":\""+price+"\",";
            filter += "\""+"status"+"\":\""+statusCode+"\",";
            filter += "\""+"type_id"+"\":\""+types+"\"}";
            print(filter);
            self.view.viewWithTag(151)?.removeFromSuperview();
            self.page = 1;
            dealProducts = [[String:String]]();
            self.loadProducts();
            
        }
        
    }
    
    @objc func resetFilterTapped(_ sender:UIButton)
    {
        print("resetFilterTapped");
        id = "";
        name = "";
        type = "";
        price = "";
        qty = "";
        status = "";
        filter = String();
        self.page = 1;
        dealProducts = [[String:String]]();
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.loadProducts();
    }
    
    @objc func productStatusDropDownClicked(_ sender: UIButton)
    {
        var tempDataSource = Array(productStatus.keys);
        print(tempDataSource);
        tempDataSource = tempDataSource.sorted { $0.localizedCaseInsensitiveCompare($1) == ComparisonResult.orderedAscending }
        dropDown.dataSource = tempDataSource;
        
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
        }
        
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            let _ = dropDown.show();
        } else {
            dropDown.hide();
        }
        
        
    }
    @objc func productTypeDropDownClicked(_ sender: UIButton)
    {
        var tempDataSource = Array(productType.keys);
        print(tempDataSource);
        tempDataSource = tempDataSource.sorted { $0.localizedCaseInsensitiveCompare($1) == ComparisonResult.orderedAscending }
        dropDown.dataSource = tempDataSource;
        
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
        }
        
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            let _ = dropDown.show();
        } else {
            dropDown.hide();
        }
        
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
                
                self.page += 1;
                self.loadProducts()
                //mainTable.reloadData();
                
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
        if let innerView = self.view.viewWithTag(181) as? DealFilterView {
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
