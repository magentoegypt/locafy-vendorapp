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

class UpSellProductsOptionsController: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate,UIGestureRecognizerDelegate {

    //edit time
    var isEditCase = false;
    //edit time
    
    
    @IBOutlet weak var showPageDropDown: UIButton!
    @IBOutlet weak var totalProductsNo: UILabel!
    
    @IBOutlet weak var toplabel: UILabel!
    @IBOutlet weak var topWrapperView: UIView!
    @IBOutlet weak var leftButton: UIButton!
    @IBOutlet weak var middleButton: UIButton!
    @IBOutlet weak var rightButton: UIButton!
    
    //filtersection
    var value11 = ""
    var value12  = ""
    var value2 = ""
    var value3  = ""
    var value4 = ""
    var value5  = ""
    var value6 = ""
    var value7 = ""
    var filter = String();
    let productStatus = ["Enabled", "Disabled"];
    let productType = ["Simple", "Virtual", "Grouped", "Bundle", "Configurable"];
    var sets = [String:String]();
    //
    var pages = 1;
    var proceeds: Bool = false;
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
    
    var attributeSetId = "";
    
    var selectedFiltersIds = [Int]();
    var proDetailArray = [[String:String]]();
    
    @IBOutlet weak var upsellProductTableView: UITableView!
    
    var baseURL = String();
    let screenSize: CGRect = UIScreen.main.bounds;
    var relatedProCellHeight = CGFloat(235);
    var heightToUse = CGFloat(0);
    var padding = CGFloat(5);
    var loading = true;
    
    
    //var for this view only
    var products = [[String:String]]();
    var selectedProductsIds = [Int]();
    var strToPost = String();
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if(selectedProductTypeId == "configurable"){
            leftButton.isHidden = true
        }
        
        // Do any additional setup after loading the view.
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        toplabel.text = "UPSELL Products".localized
        leftButton.setTitle("Save".localized, for: UIControl.State());
        leftButton.addTarget(self, action: #selector(UpSellProductsOptionsController.saveFunction(_:)), for: UIControl.Event.touchUpInside);
        
        middleButton.addTarget(self, action: #selector(UpSellProductsOptionsController.filterButtonPressed(_:)), for: UIControl.Event.touchUpInside);
        
        rightButton.setTitle("Continue".localized, for: UIControl.State());
        rightButton.addTarget(self, action: #selector(UpSellProductsOptionsController.continueFunction(_:)), for: UIControl.Event.touchUpInside);
        
        let color = Ced_CommonVendor.UIColorFromRGB("#2abb9c");
        leftButton.backgroundColor = DynamicColor.themeColor;
        rightButton.backgroundColor = color
        
        
        ced_navigationBarController().addNavButton(self, str: "no");

        
        self.upsellProductTableView.delegate = self;
        self.upsellProductTableView.dataSource = self;
        //showPageDropDown.addTarget(self, action: #selector(UpSellProductsOptionsController.showPageNation(_:)), for: .touchUpInside)
        self.fetchRelatedProductListing(pages);
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    //filter @objc functions
    @objc func filterButtonPressed(_ sender:UIButton)
    {
        print("filterButtonPressed");
        /* background view */
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let longFilterPopupView = ProductFilterPopupView();
        self.addgesturesTohideView(self.view);
        longFilterPopupView.tag = 181;
        longFilterPopupView.backgroundColor = UIColor.black;
        longFilterPopupView.frame = CGRect(x: 25, y: 25, width: self.view.frame.width - 20, height: 340);
        longFilterPopupView.center.x = self.view.center.x;
        longFilterPopupView.center.y = self.view.center.y;
        
        longFilterPopupView.topLabel.text = "Seller App";
        longFilterPopupView.label1.text = "Product Price";
        longFilterPopupView.label2.text = "Product Name";
        longFilterPopupView.label3.text = "Product SKU";
        longFilterPopupView.label4.text = "Product ID";
        longFilterPopupView.label5.text = "Product Status";
        longFilterPopupView.label6.text = "Product Type";
        longFilterPopupView.label7.text = "Attribute Set";
        
        
        longFilterPopupView.value5.setTitle(value5, for: UIControl.State());
        longFilterPopupView.value5.addTarget(self, action: #selector(UpSellProductsOptionsController.showProductStatusDropdown(_:)), for: UIControl.Event.touchUpInside);
        
        longFilterPopupView.value6.setTitle(value6, for: UIControl.State());
        longFilterPopupView.value6.addTarget(self, action: #selector(UpSellProductsOptionsController.showProductTypeDropdown(_:)), for: UIControl.Event.touchUpInside);
        
        longFilterPopupView.value7.setTitle(value7, for: UIControl.State());
        longFilterPopupView.value7.addTarget(self, action: #selector(UpSellProductsOptionsController.showAttributeSetDropdown(_:)), for: UIControl.Event.touchUpInside);
        
        longFilterPopupView.value11.text = value11;
        longFilterPopupView.value12.text = value12;
        longFilterPopupView.value2.text = value2;
        longFilterPopupView.value3.text = value3;
        longFilterPopupView.value4.text = value4;
        
        
        longFilterPopupView.leftButton.setTitle("Filter", for: UIControl.State());
        longFilterPopupView.leftButton.addTarget(self, action: #selector(UpSellProductsOptionsController.applyFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        
        longFilterPopupView.rightButton.setTitle("Reset", for: UIControl.State());
        longFilterPopupView.rightButton.addTarget(self, action: #selector(UpSellProductsOptionsController.resetFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        
        
        bgCView.addSubview(longFilterPopupView)
        self.view.addSubview(bgCView);
    }
    
    @objc func showProductStatusDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = self.productStatus;
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
    
    @objc func showProductTypeDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = self.productType;
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
    
    @objc func showAttributeSetDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = Array(self.sets.keys);
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
    
    
    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(UpSellProductsOptionsController.hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(UpSellProductsOptionsController.hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(UpSellProductsOptionsController.hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(UpSellProductsOptionsController.hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(UpSellProductsOptionsController.hideView(_:)));
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
        if let innerView = self.view.viewWithTag(181) as? ProductFilterPopupView {
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
        
        if let view = self.view.viewWithTag(181) as? ProductFilterPopupView
        {
            if(view.value11.text != nil){
                value11 =  view.value11.text!
            }
            if(view.value12.text != nil){
                value12 = view.value12.text!
            }
            if(view.value2.text != nil){
                value2 = view.value2.text!
            }
            if(view.value3.text != nil){
                value3 = view.value3.text!
            }
            if(view.value4.text != nil){
                value4 = view.value4.text!
            }
            if(view.value5.titleLabel!.text != nil){
                value5 = view.value5.titleLabel!.text!
            }
            if(view.value6.titleLabel!.text != nil){
                value6 = view.value6.titleLabel!.text!
            }
            var val_value7 = "";
            if(view.value7.titleLabel!.text != nil){
                value7 = view.value7.titleLabel!.text!;
                val_value7 = self.sets[view.value7.titleLabel!.text!]!;
            }
            
            filter = "";
            filter += "{";
            filter += "\""+"price"+"\":{\"from\":\""+value11+"\",\"to\":\""+value12+"\"},";
            filter += "\""+"name"+"\":\""+value2+"\",";
            filter += "\""+"sku"+"\":\""+value3+"\",";
            filter += "\""+"entity_id"+"\":\""+value4+"\",";
            filter += "\""+"status"+"\":\""+value5+"\",";
            filter += "\""+"type_id"+"\":\""+value6+"\",";
            filter += "\""+"attribute_set_id"+"\":\""+val_value7+"\"}";
            
            print(filter);
            self.view.viewWithTag(151)?.removeFromSuperview();
            products = [[String:String]]();
            heightToUse = CGFloat(0);
            pages = 1
            loading = true
            self.fetchRelatedProductListing(5);
            
        }
        
        
        
    }
    
    @objc func resetFilterTapped(_ sender:UIButton)
    {
        print("resetFilterTapped");
        value11 = ""
        value12  = ""
        value2 = ""
        value3  = ""
        value4 = ""
        value5  = ""
        value6 = ""
        value7 = ""
        filter = String();
        products = [[String:String]]();
        pages = 1
        loading = true
        self.upsellProductTableView.reloadData()
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.fetchRelatedProductListing(5);
    }
    
    //filter @objc functions
    
    
    
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
        let cell = upsellProductTableView.dequeueReusableCell(withIdentifier: "longSingleCell", for: indexPath) as! LongSingleTableViewCell;
        //cell.mainWrapperView.backgroundColor = UIColor.redColor();
        
        for subview in cell.mainWrapperView.subviews{
            subview.removeFromSuperview();
        }
         relatedProCellHeight = CGFloat(235);
         heightToUse = CGFloat(0);
         padding = CGFloat(5);
        
        for (val) in products
        {
            let relatedProductView = RelatedProductView();
            relatedProductView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            relatedProductView.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: relatedProCellHeight);
            heightToUse += relatedProCellHeight;
            heightToUse += padding;
            
            if(val["selected"] == "false")
            {
                relatedProductView.selectionButton.setImage(UIImage(named: "unchecked_checkbox_white"), for: UIControl.State());
            }
            else
            {
                relatedProductView.selectionButton.setImage(UIImage(named: "checked_checkbox_white"), for: UIControl.State());
                self.selectedProductsIds.append(Int(val["product_id"]!)!);
            }
            
             relatedProductView.selectionButton.setTitle("#"+val["product_name"]!, for: UIControl.State());
            
            relatedProductView.selectionButton.addTarget(self, action: #selector(UpSellProductsOptionsController.makeProductSelected(_:)), for: UIControl.Event.touchUpInside);
            relatedProductView.selectionButton.tag = Int(val["product_id"]!)!;
            
            relatedProductView.label1.text = "Product ID";
            relatedProductView.value1.text = val["product_id"];
            
            relatedProductView.label2.text = "Product SKU";
            relatedProductView.value2.text = val["sku"];
            
            relatedProductView.label3.text = "Product Type";
            relatedProductView.value3.text = val["type"];
            
            relatedProductView.label4.text = "Product Price";
            relatedProductView.value4.text = val["regular_price"];
            
            relatedProductView.label5.text = "Product Name";
            relatedProductView.value5.text = val["product_name"];
            
            relatedProductView.label6.text = "Product Status";
            relatedProductView.value6.text = val["status"];
            
            relatedProductView.label7.text = "Product Atribute Set";
            relatedProductView.value7.text = val["set_name"];
            
            relatedProductView.wrapperView.makeCardUsingThemeColor(relatedProductView.wrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
            
            cell.mainWrapperView.addSubview(relatedProductView);
        }
        
        return cell;
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return heightToUse;
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
    @objc func fetchRelatedProductListing(_ page:Int)
    {
        pages=page;
        print("fetchRelatedProductListing");
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        
        let baseURL = "vproductapi/vproducts/upSell/page/\(page)";
        
        print(baseURL);
        
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&product_id="+product_id;
        if(filter != "")
        {
            postString += "&filter="+filter;
        }
        print(postString);
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        sendRequest(url: baseURL, parameters: postString);
    }
    
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            if(requestUrl == "vendorapi/vproducts/update/")
            {
                guard let jsonData = try? JSON(data: data) else {return}
                print(jsonData);
                if(jsonData["data"]["success"].stringValue != "true")
                {
                    print("something went wrong");
                    return;
                }
                
                if(proceeds)
                {
                    self.switchToNextView();
                    proceeds = false;
                }
                else{
                    let view = UIStoryboard(name: "ProductAddon", bundle: nil).instantiateViewController(withIdentifier: "manageProductViewController") as! ManageProductViewController
                    self.navigationController?.setViewControllers([view], animated: true)
                }
            }else{
                
                guard let jsonData = try? JSON(data: data) else {return}
                print(jsonData);
                for (_,val) in jsonData["data"]["sets"][0]
                {
                    self.sets[val["label"].stringValue] = val["value"].stringValue;
                }
                if(jsonData["data"]["success"].stringValue != "true")
                {
                    loading = false;
                    self.view.makeToast(jsonData["data"]["message"].stringValue)
                    if pages == 1{
                        if jsonData["data"]["products"].arrayValue.count == 0{
                            self.products.removeAll()
                        }
                        self.upsellProductTableView.setEmptyMessage(jsonData["data"]["message"].stringValue)
                    }
                    
                    self.upsellProductTableView.reloadData();
                    return;
                }
                
                
//                self.totalProductsNo.text = "Total Products : " + jsonData["data"]["count"].stringValue
                
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
                self.upsellProductTableView.restore()
                self.upsellProductTableView.reloadData();
            }
        }
    }
    
    @objc func saveFunction(_ sender:UIButton)
    {
        print("saveFunction");
        self.fetchValues();
        self.updateProductInfo(false);
    }
    
    @objc func continueFunction(_ sender:UIButton)
    {
        print("continueFunction");
        self.fetchValues();
        self.updateProductInfo(true);
    }
    
    
    
    @objc func fetchValues()
    {
        print("fetchValues");
        strToPost = "{\"links\":{\"related\":\"";
        for (val) in selectedProductsIds
        {
            strToPost += "#"+String(val);
        }
        strToPost += "\"}}";
        
        print(strToPost);
    }
    
    @objc func updateProductInfo(_ proceed:Bool)
    {
        proceeds=proceed;
        print("updateProductInfo");
        strToPost = strToPost.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!;
        
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        let baseURL = "vendorapi/vproducts/update/";
        
        print(baseURL);
        
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&product_id="+product_id;
        if let selectedWebsite = UserDefaults.standard.value(forKey: "selectedWebsite") as? String{
            if selectedWebsite != ""{
                postString += "&websites="+selectedWebsite;
            }
        }
        if(attributeSetId != "")
        {
            postString += "&attribute_set="+attributeSetId;
        }
        if(strToPost != "")
        {
            postString += "&upsellproductids="+strToPost;
        }
        print(postString);
        
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        sendRequest(url: baseURL, parameters: postString);
    }
    
    @objc func showPageNation(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = ["5","10","15","All"];
        dropDown.selectionAction = {(index, item) in
            
            sender.setTitle(item, for: UIControl.State());
            if let intd = Int(item) {
                self.products = [[String:String]]();
                self.fetchRelatedProductListing(intd);
            }
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    
    
    @objc func switchToNextView()
    {
        print("switchToNextView");
        
        if(self.tabs["crosssell"] != nil)
        {
            let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "crossCellProductsOptionsController") as! CrossCellProductsOptionsController;
            
            //edit time
            viewcontrollor.isEditCase = isEditCase;
            //edit time
            
            
            viewcontrollor.selectedProductTypeId = selectedProductTypeId;
            viewcontrollor.selectedAttributeSetId = selectedAttributeSetId;
            viewcontrollor.stock = self.stock;
            viewcontrollor.taxes = self.taxes;
            viewcontrollor.tabs = self.tabs;
            viewcontrollor.storeViews = self.storeViews;
            viewcontrollor.websites = self.websites;
            viewcontrollor.categoriesList = self.categoriesList;
            viewcontrollor.attributes = attributes;
            viewcontrollor.product_id = self.product_id;
            viewcontrollor.configSelected = self.configSelected;
            
            self.navigationController?.setViewControllers([viewcontrollor], animated: true)
            return;
        }
        
        
        if(self.tabs["customer_options"] != nil)
        {
            let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "customOptionController") as! CustomOptionController;
            
            //edit time
            viewcontrollor.isEditCase = isEditCase;
            //edit time
            
            
            viewcontrollor.selectedProductTypeId = selectedProductTypeId;
            viewcontrollor.selectedAttributeSetId = selectedAttributeSetId;
            viewcontrollor.stock = self.stock;
            viewcontrollor.taxes = self.taxes;
            viewcontrollor.tabs = self.tabs;
            viewcontrollor.storeViews = self.storeViews;
            viewcontrollor.websites = self.websites;
            viewcontrollor.categoriesList = self.categoriesList;
            viewcontrollor.attributes = attributes;
            viewcontrollor.product_id = self.product_id;
            viewcontrollor.configSelected = self.configSelected;
            
                self.navigationController?.setViewControllers([viewcontrollor], animated: true)
            return;
        }
        
        if(selectedProductTypeId == "simple" || selectedProductTypeId == "virtual")
        {
            print("GO TO Manage Products Listing");
            let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "manageProductViewController") as! ManageProductViewController;
            
                 self.navigationController?.setViewControllers([viewcontrollor], animated: true)
            return;
        }
        
        
        if(selectedProductTypeId == "grouped")
        {
            let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "groupedProductSelectionController") as! GroupedProductSelectionController;
            
            //edit time
            viewcontrollor.isEditCase = isEditCase;
            //edit time
            
            
            viewcontrollor.selectedProductTypeId = selectedProductTypeId;
            viewcontrollor.selectedAttributeSetId = selectedAttributeSetId;
            viewcontrollor.stock = self.stock;
            viewcontrollor.taxes = self.taxes;
            viewcontrollor.tabs = self.tabs;
            viewcontrollor.storeViews = self.storeViews;
            viewcontrollor.websites = self.websites;
            viewcontrollor.categoriesList = self.categoriesList;
            viewcontrollor.attributes = attributes;
            viewcontrollor.product_id = self.product_id;
            viewcontrollor.configSelected = self.configSelected;
            
               self.navigationController?.setViewControllers([viewcontrollor], animated: true)
            return;
        }
        
        if(selectedProductTypeId == "configurable")
        {
            let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "configProductSelectionController") as! ConfigProductSelectionController;
            
            //edit time
            viewcontrollor.isEditCase = isEditCase;
            //edit time
            
            
            viewcontrollor.selectedProductTypeId = selectedProductTypeId;
            viewcontrollor.selectedAttributeSetId = selectedAttributeSetId;
            viewcontrollor.stock = self.stock;
            viewcontrollor.taxes = self.taxes;
            viewcontrollor.tabs = self.tabs;
            viewcontrollor.storeViews = self.storeViews;
            viewcontrollor.websites = self.websites;
            viewcontrollor.categoriesList = self.categoriesList;
            viewcontrollor.attributes = attributes;
            viewcontrollor.product_id = self.product_id;
            viewcontrollor.configSelected = self.configSelected;
            
                self.navigationController?.setViewControllers([viewcontrollor], animated: true)
            return;
        }
        
        if(selectedProductTypeId == "bundle")
        {
            let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "bundleItemsViewController") as! BundleItemsViewController;
            
            //edit time
            viewcontrollor.isEditCase = isEditCase;
            //edit time
            
            
            viewcontrollor.selectedProductTypeId = selectedProductTypeId;
            viewcontrollor.selectedAttributeSetId = selectedAttributeSetId;
            viewcontrollor.stock = self.stock;
            viewcontrollor.taxes = self.taxes;
            viewcontrollor.tabs = self.tabs;
            viewcontrollor.storeViews = self.storeViews;
            viewcontrollor.websites = self.websites;
            viewcontrollor.categoriesList = self.categoriesList;
            viewcontrollor.attributes = attributes;
            viewcontrollor.product_id = self.product_id;
            viewcontrollor.configSelected = self.configSelected;
            
                self.navigationController?.setViewControllers([viewcontrollor], animated: true)
            return;
        }
        
        
    }
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
                
                self.pages += 1;
                self.fetchRelatedProductListing(self.pages);
                
            }
            
        }
        
    }
    
}
