/**
* CedCommerce
*
* NOTICE OF LICENSE
*
* This source file is subject to the End User License Agreement (EULA)
* that is bundled with this package in the file LICENSE.txt.
* It is also available through the world-wide-web at this URL:
* http://cedcommerce.com/license-agreement.txt
*
* @category  Ced
* @package   MageNative MultiVendor
* @author    CedCommerce Core Team <connect@cedcommerce.com >
* @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
* @license      http://cedcommerce.com/license-agreement.txt
*/

import UIKit

class BundleProductSelectionController: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate,UIGestureRecognizerDelegate {
    
    @IBOutlet weak var groupedProductTableView: UITableView!
    @IBOutlet weak var topView: UIView!
    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var filterButton: UIButton!
    
    //edit time
    var isEditCase = false;
    //edit time
    var dynamicPriceType = "1"
    var dynamicTypesArray = [String:String]()
    
    //filtersection
    var value11 = ""
    var value12  = ""
    var secondVal = ""
    var value2 = ""
    var value3  = ""
    var value4 = ""
    var filter = String();
    //
    
    //
    var product_id = "";
    var selectedProductTypeId = "";
    var selectedAttributeSetId = "";
    var stock = [String:String]();
    var taxes = [String:String]();
    var tabs = [String:String]();
    var storeViews = [String:[String:[String]]]();
    var websites = [String:String]();
    var categoriesList = [String:[String]]();
    var attributes : JSON!;
    var configSelected = [String:String]();
    //

    
    
    var currentSenderTag = 0;
    
   
    
    let priceTypeArray = ["Fixed", "Percentage"];
    let userDefinedQtyArray = ["Yes", "No"];
    
    
    let screenSize: CGRect = UIScreen.main.bounds;
    let relatedProCellHeight = CGFloat(280);
    var heightToUse = CGFloat(0);
    
    var baseURL = String();
    var productTypeID = String();
    
    var products = [[String:String]]();
    var selectedProductsIds = [Int]();
    var selectProExtraImpInfo = [String:[String:String]]();//imp and for use in this page only
    
    var pageNumber = 1
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        print("selectProExtraImpInfo##");
        print(selectProExtraImpInfo);
        print("##selectProExtraImpInfo");
        print(selectedProductsIds);
        print("##selectProExtraImpInfo");
        
        self.groupedProductTableView.delegate = self;
        self.groupedProductTableView.dataSource = self;
        
        ced_navigationBarController().addNavButton(self,str:"no")
        
        filterButton.addTarget(self, action: #selector(BundleProductSelectionController.filterButtonPressed(_:)), for: UIControl.Event.touchUpInside);
        
        saveButton.setTitle("Save".localized, for: UIControl.State());
        saveButton.addTarget(self, action: #selector(BundleProductSelectionController.goBackPressed(_:)), for: UIControl.Event.touchUpInside);
        let color = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        saveButton.backgroundColor = color;
        
        self.fetchRelatedProductListing();
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @objc func goBackPressed(_ sender:UIButton)
    {
        print("goBackPressed");
        
        selectProExtraImpInfo.forEach{key,val in
            if let prodId = Int(key){
                if !selectedProductsIds.contains(prodId){
                    selectProExtraImpInfo[key] = nil
                }
            }
        }
        
        
        for (id) in selectedProductsIds
        {
            
            if let bundleProductSingleView = self.view.viewWithTag(id) as? BundleProductSingleView
            {
                var tempArray = [String:String]();
                //validation code
                if(bundleProductSingleView.value5.text == nil || bundleProductSingleView.value5.text == "")
                {
                    self.view.showToastMsg("Please Fill Default Quantity!");
                    return;
                }
                if dynamicPriceType == "1"{
                    if(bundleProductSingleView.value6.text == nil || bundleProductSingleView.value6.text == "")
                    {
                        self.view.showToastMsg("Please Fill Product Price!");
                        return;
                    }
                    
                    if(bundleProductSingleView.value7.titleLabel!.text == nil)
                    {
                        self.view.showToastMsg("Please Select Price Type!");
                        return;
                    }
                }
                
                
                if(bundleProductSingleView.value8.titleLabel!.text == nil)
                {
                    self.view.showToastMsg("Please Select User Defined Qty!");
                    return;
                }
                //validation code
                
                
                print(bundleProductSingleView);
                tempArray["selection_qty"] = bundleProductSingleView.value5.text!;
                
                
                
                if bundleProductSingleView.value8.titleLabel!.text == "Yes"{
                    tempArray["selection_can_change_qty"] = "1"
                }else{
                    tempArray["selection_can_change_qty"] = "0"
                }
                tempArray["product_id"] = bundleProductSingleView.value1.text!
                if selectProExtraImpInfo[String(id)]?["product_id"] == bundleProductSingleView.value1.text{
                    tempArray["selection_id"] = selectProExtraImpInfo[String(id)]?["selection_id"] ?? ""
                    if dynamicPriceType == "0"{
                        tempArray["selection_price_type"] = selectProExtraImpInfo[String(id)]?["selection_price_type"] ?? ""
                        tempArray["selection_price_value"] = selectProExtraImpInfo[String(id)]?["selection_price_value"] ?? ""
                    }else{
                        tempArray["selection_price_type"] = dynamicTypesArray[bundleProductSingleView.value7.currentTitle ?? ""] ?? ""
                        tempArray["selection_price_value"] = bundleProductSingleView.value6.text ?? ""
                    }
                    
                }else{
                    tempArray["selection_id"] = ""
                    tempArray["selection_price_type"] = dynamicTypesArray[bundleProductSingleView.value7.currentTitle ?? ""] ?? ""
                    tempArray["selection_price_value"] = bundleProductSingleView.value6.text ?? ""
                }
                
                tempArray["delete"] = ""
                
                self.selectProExtraImpInfo[String(id)] = tempArray;
            }
            
        }
        
        
        print("self.selectProExtraImpInfo");
        print(self.selectProExtraImpInfo);
        
        defaults.removeObject(forKey: "selectedProductsIds");
        defaults.set(self.selectedProductsIds, forKey: "selectedProductsIds");
        
        defaults.removeObject(forKey: "selectProExtraImpInfo");
        defaults.set(self.selectProExtraImpInfo, forKey: "selectProExtraImpInfo");
        
        self.dismiss(animated: true, completion: {});
        NotificationCenter.default.post(name: Notification.Name(rawValue: "setTheSelectionID"), object: nil);
    }
    
    //filter section
    // filter @objc functions
    @objc func filterButtonPressed(_ sender:UIButton)
    {
        print("filterButtonPressed");
        /* background view */
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let longFilterPopupView = BundleProductFilterPopupView();
        self.addgesturesTohideView(self.view);
        longFilterPopupView.tag = 181;
        longFilterPopupView.backgroundColor = UIColor.black;
        longFilterPopupView.frame = CGRect(x: 25, y: 25, width: self.view.frame.width - 20, height: 270);
        longFilterPopupView.center.x = self.view.center.x;
        longFilterPopupView.center.y = self.view.center.y;
        
        longFilterPopupView.topLabel.text = "Seller App";
        longFilterPopupView.label1.text = "Product Price";
        longFilterPopupView.second.text = "Product Name";
        longFilterPopupView.label2.text = "Product SKU";
        longFilterPopupView.label3.text = "Product ID";
        longFilterPopupView.label4.text = "Attribute Set";
        
        
        longFilterPopupView.value11.text = value11;
        longFilterPopupView.value12.text = value12;
        longFilterPopupView.secondVal.text = secondVal;
        longFilterPopupView.value2.text = value2;
        longFilterPopupView.value3.text = value3;
        longFilterPopupView.value4.setTitle(value4, for: UIControl.State());
        
        
        
        longFilterPopupView.leftButton.setTitle("Filter", for: UIControl.State());
        longFilterPopupView.leftButton.addTarget(self, action: #selector(BundleProductSelectionController.applyFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        
        longFilterPopupView.rightButton.setTitle("Reset", for: UIControl.State());
        longFilterPopupView.rightButton.addTarget(self, action: #selector(BundleProductSelectionController.resetFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        
        
        bgCView.addSubview(longFilterPopupView)
        self.view.addSubview(bgCView);
    }
    
    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(BundleProductSelectionController.hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(BundleProductSelectionController.hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(BundleProductSelectionController.hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(BundleProductSelectionController.hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(BundleProductSelectionController.hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    //@objc function to dismiss the custom popover
    @objc func hideView(_ recognizer: UITapGestureRecognizer)
    {
        self.view.viewWithTag(151)?.removeFromSuperview();
    }
    
    //delegate @objc function to handle touch events on the custom popover
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        if let innerView = self.view.viewWithTag(181) as? GroupedProductFilterView{
            if(touch.view!.isDescendant(of: innerView))
            {
                return false;
            }
            return true;
        }
        return true;
    }
    
    @objc func applyFilterTapped(_ sender:UIButton)
    {
        print("applyFilterTapped");
        
        if let view = self.view.viewWithTag(181) as? BundleProductFilterPopupView
        {
            if(view.value11.text != nil){
                value11 =  view.value11.text!
            }
            if(view.value12.text != nil){
                value12 = view.value12.text!
            }
            if(view.secondVal.text != nil){
                secondVal = view.secondVal.text!
            }
            if(view.value2.text != nil){
                value2 = view.value2.text!
            }
            if(view.value3.text != nil){
                value3 = view.value3.text!
            }
            if(view.value4.titleLabel!.text != nil){
                value4 = view.value4.titleLabel!.text!
            }
            
            print(filter);
            self.view.viewWithTag(151)?.removeFromSuperview();
            products = [[String:String]]();
            heightToUse = CGFloat(0);
            self.fetchRelatedProductListing();
            
        }
        
        
        
    }
    
    @objc func resetFilterTapped(_ sender:UIButton)
    {
        print("resetFilterTapped");
        value11 = ""
        value12  = ""
        secondVal = ""
        value2 = ""
        value3  = ""
        value4 = ""
        filter = String();
        products = [[String:String]]();
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.fetchRelatedProductListing();
    }
    

    //filter section
    
    // MARK: - Table view data source
    
    @objc func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return 1;
    }
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = groupedProductTableView.dequeueReusableCell(withIdentifier: "longSingleCell", for: indexPath) as! LongSingleTableViewCell;
        
        for (val) in products
        {
            let bundleItemView = BundleProductSingleView();
            bundleItemView.tag = Int(val["product_id"]!)!;
            bundleItemView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            bundleItemView.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: relatedProCellHeight);
            heightToUse += relatedProCellHeight;
            
            if let _ = selectedProductsIds.index(of: Int(val["product_id"]!)!) {
                bundleItemView.selectionButton.setImage(UIImage(named: "checked_checkbox_white"), for: UIControl.State());
                let previousVal = selectProExtraImpInfo[val["product_id"]!]!;
                
                bundleItemView.value5.text = previousVal["selection_qty"]!;
                bundleItemView.value6.text = previousVal["selection_price_value"]!;
                
                
                bundleItemView.value7.setTitle(dynamicTypesArray.someKey(forValue: previousVal["selection_price_type"] ?? ""), for: UIControl.State());
                if previousVal["selection_can_change_qty"] == "1"{
                    bundleItemView.value8.setTitle("Yes", for: UIControl.State());
                }else{
                    bundleItemView.value8.setTitle("No", for: UIControl.State());
                }
                
                
            }
            else
            {
                bundleItemView.selectionButton.setImage(UIImage(named: "unchecked_checkbox_white"), for: UIControl.State());
                bundleItemView.value5.text = val["qty"];
                bundleItemView.value6.text = "";
                bundleItemView.value7.setTitle("", for: UIControl.State());
                bundleItemView.value8.setTitle("", for: UIControl.State());
                
            }
            
            bundleItemView.selectionButton.setTitle("#"+val["product_name"]!, for: UIControl.State());
            
            bundleItemView.selectionButton.addTarget(self, action: #selector(BundleProductSelectionController.makeProductSelected(_:)), for: UIControl.Event.touchUpInside);
            bundleItemView.selectionButton.tag = Int(val["product_id"]!)!;
            
            bundleItemView.label1.text = "Product ID";
            bundleItemView.value1.text = val["product_id"];
            
            bundleItemView.label2.text = "Product SKU";
            bundleItemView.value2.text = val["sku"];
            
            bundleItemView.label3.text = "Product Price";
            bundleItemView.value3.text = val["regular_price"];
            
            bundleItemView.label4.text = "Product Name";
            bundleItemView.value4.text = val["product_name"];
            
            bundleItemView.label5.text = "Default Quantity";
            
            bundleItemView.label6.text = "Price";
            
            bundleItemView.label7.text = "Price Type";
            bundleItemView.value7.addTarget(self, action: #selector(BundleProductSelectionController.showPriceTypeDropdown(_:)), for: UIControl.Event.touchUpInside);
            
            bundleItemView.label8.text = "User Defined Qty";
            bundleItemView.value8.addTarget(self, action: #selector(BundleProductSelectionController.showUserDefinedQtyDropdown(_:)), for: UIControl.Event.touchUpInside);
            
            bundleItemView.coreWrapperView.makeCardUsingThemeColor(bundleItemView.coreWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
            
            if dynamicPriceType == "0"{
                bundleItemView.label6.isHidden = true
                bundleItemView.value6.isHidden = true
                
                bundleItemView.label7.isHidden = true
                bundleItemView.value7.isHidden = true
            }else{
                bundleItemView.label6.isHidden = false
                bundleItemView.value6.isHidden = false
                
                bundleItemView.label7.isHidden = false
                bundleItemView.value7.isHidden = false
            }
            
            cell.mainWrapperView.addSubview(bundleItemView);
        }
        
        return cell;
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return heightToUse;
    }
    
    @objc func showPriceTypeDropdown(_ sender:UIButton)
    {
        print("showPriceTypeDropdown");
        let dropDown = DropDown();
        dropDown.dataSource = Array(dynamicTypesArray.keys)
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
        
    }
    
    
    @objc func showUserDefinedQtyDropdown(_ sender:UIButton)
    {
        print("showUserDefinedQtyDropdown");
        let dropDown = DropDown();
        dropDown.dataSource = self.userDefinedQtyArray;
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    
    
    @objc func makeProductSelected(_ sender:UIButton)
    {
        print(sender.tag);
        if(sender.imageView!.image == UIImage(named:"unchecked_checkbox_white"))
        {
            sender.setImage(UIImage(named: "checked_checkbox_white"), for: UIControl.State());
            self.selectedProductsIds.append(sender.tag);
        }
        else
        {
            sender.setImage(UIImage(named: "unchecked_checkbox_white"), for: UIControl.State());
            if let indexToRemove = selectedProductsIds.index(of: sender.tag) {
                self.selectedProductsIds.remove(at: indexToRemove);
            }
        }
        
        print(self.selectedProductsIds);
    }
    
    
    /** @objc function to fetch related product data **/
    @objc func fetchRelatedProductListing()
    {
        print("fetchRelatedProductListing");
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        
        
        baseURL = "vproductapi/vproducts/bundleGrid/page/\(pageNumber)";
        
        print(baseURL);
        
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&product_id="+product_id;
        
        print(postString);
        
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        sendRequest(url: baseURL, parameters: postString);
    }
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        if let data = data{
            var jsonData = try! JSON(data: data);
            
            print(jsonData)
            Alert_File.removeLoadingIndicator(self);
            
            if(jsonData["data"]["success"].stringValue != "true")
            {
                self.groupedProductTableView.reloadData();
                return
            }
            
            /** fetching product types **/
            for (_,val) in jsonData["data"]["products"]
            {
                var tempArray = [String:String]();
                for(k,v) in val
                {
                    tempArray[k] = v.stringValue;
                }
                self.products.append(tempArray);
            }
            pageNumber += 1
//            self.groupedProductTableView.reloadData();
//            self.groupedProductTableView.reloadData();
            fetchRelatedProductListing()
        }
        
    }
        
}
