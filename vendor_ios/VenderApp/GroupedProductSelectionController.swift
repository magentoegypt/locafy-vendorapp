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

class GroupedProductSelectionController: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate,UIGestureRecognizerDelegate {

    //edit time
    var isEditCase = false;
    //edit time
    
    //filtersection
    var value11 = ""
    var value12  = ""
    var value21 = ""
    var value22 = ""
    var value31  = ""
    var value32  = ""
    var value4 = ""
    var value5  = ""
    var filter = String();
    //
    var currentpage = 1
    
    
    
    @IBOutlet weak var groupedProductTableView: UITableView!
    @IBOutlet weak var topView: UIView!
    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var filterButton: UIButton!
    
    
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

    let screenSize: CGRect = UIScreen.main.bounds;
    let relatedProCellHeight = CGFloat(190);
    var heightToUse = CGFloat(0);
    let padding = CGFloat(5);
    
    
    var products = [[String:String]]();
    var selectedProductsIds = [Int]();//use in this page only
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        self.groupedProductTableView.delegate = self;
        self.groupedProductTableView.dataSource = self;
        ced_navigationBarController().addNavButton(self,str:"no")
        
        filterButton.addTarget(self, action: #selector(GroupedProductSelectionController.filterButtonPressed(_:)), for: UIControl.Event.touchUpInside);
        
        saveButton.setTitle("Save".localized, for: UIControl.State());
        saveButton.addTarget(self, action: #selector(GroupedProductSelectionController.savePressed(_:)), for: UIControl.Event.touchUpInside);
        
        let color = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        saveButton.backgroundColor = color;
        
        
        self.fetchRelatedProductListing();
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
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
            let groupedProductView = GroupedProductView();
            groupedProductView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            groupedProductView.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: relatedProCellHeight);
            heightToUse += relatedProCellHeight;
            heightToUse += padding;
            
            
            groupedProductView.tag = Int(val["product_id"]!)!;
            
            
            if(val["selected"] == "false")
            {
                groupedProductView.selectionButton.setImage(UIImage(named: "unchecked_checkbox_white"), for: UIControl.State());
            }
            else
            {
                groupedProductView.selectionButton.setImage(UIImage(named: "checked_checkbox_white"), for: UIControl.State());
                self.selectedProductsIds.append(Int(val["product_id"]!)!);
            }
            
            groupedProductView.selectionButton.setTitle("#"+val["product_name"]!, for: UIControl.State());
            
            groupedProductView.selectionButton.addTarget(self, action: #selector(GroupedProductSelectionController.makeProductSelected(_:)), for: UIControl.Event.touchUpInside);
            groupedProductView.selectionButton.tag = Int(val["product_id"]!)!;
            
            groupedProductView.label1.text = "Product ID";
            groupedProductView.value1.text = val["product_id"];
            
            groupedProductView.label2.text = "Product SKU";
            groupedProductView.value2.text = val["sku"];
            
            groupedProductView.label3.text = "Product Price";
            groupedProductView.value3.text = val["regular_price"];
            
            groupedProductView.label4.text = "Product Name";
            groupedProductView.value4.text = val["product_name"];
            
            groupedProductView.label5.text = "Default Quantity";
            groupedProductView.value5.text = val["qty"];
            
            groupedProductView.coreWrapperView.makeCardUsingThemeColor(groupedProductView.coreWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);

            
            cell.mainWrapperView.addSubview(groupedProductView);
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
    @objc func fetchRelatedProductListing()
    {
        print("fetchRelatedProductListing");
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;

//        baseURL = Ced_CommonVendor.getInfoPlist("baseUrl") as! String;
        let baseURL = "vproductapi/vproducts/grouped/page/\(currentpage)";
        
        print(baseURL);
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&product_id="+product_id;
        
        if filter != ""{
            postString += "&filter=\(filter)"
        }
        
        print(postString);
        
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        sendRequest(url: baseURL, parameters: postString);
    }
    
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            var jsonData = try! JSON(data: data);
            if(requestUrl!.contains("vproductapi/vproducts/grouped/"))
            {
                print("jsonData :: Bundle");
                print(jsonData);
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
                
                currentpage += 1
                fetchRelatedProductListing()
            }
            else if(requestUrl == "vendorapi/vproducts/update/")
            {
                
                print(jsonData);
                if(jsonData["data"]["success"].stringValue != "true")
                {
                    let title = "Error";
                    let message = "Unfortunately Some Error Occured";
                    Alert_File.showValidationError(self,title:title,message:message);
                    return;
                }
                
                let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "manageProductViewController") as! ManageProductViewController;
                
                self.navigationController?.setViewControllers([viewcontrollor], animated: true)
                return;
            }
        }
        
    }
    
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
        let longFilterPopupView = GroupedProductFilterView();
        self.addgesturesTohideView(self.view);
        longFilterPopupView.tag = 181;
        longFilterPopupView.backgroundColor = UIColor.black;
        longFilterPopupView.frame = CGRect(x: 25, y: 25, width: self.view.frame.width - 20, height: 270);
        longFilterPopupView.center.x = self.view.center.x;
        longFilterPopupView.center.y = self.view.center.y;
        
        longFilterPopupView.topLabel.text = "Seller App";
        longFilterPopupView.label1.text = "Product Price";
        longFilterPopupView.label2.text = "Product Qty";
        longFilterPopupView.label3.text = "Product ID";
        longFilterPopupView.label4.text = "Product Name";
        longFilterPopupView.label5.text = "Product SKU";
        
        
        longFilterPopupView.value11.text = value11;
        longFilterPopupView.value12.text = value12;
        longFilterPopupView.value21.text = value21;
        longFilterPopupView.value22.text = value22;
        longFilterPopupView.value31.text = value31;
        longFilterPopupView.value32.text = value32;
        longFilterPopupView.value4.text = value4;
        longFilterPopupView.value5.text = value5;
        
        
        longFilterPopupView.leftButton.setTitle("Filter", for: UIControl.State());
        longFilterPopupView.leftButton.addTarget(self, action: #selector(GroupedProductSelectionController.applyFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        
        longFilterPopupView.rightButton.setTitle("Reset", for: UIControl.State());
        longFilterPopupView.rightButton.addTarget(self, action: #selector(GroupedProductSelectionController.resetFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        
        
        bgCView.addSubview(longFilterPopupView)
        self.view.addSubview(bgCView);
    }
    
    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(GroupedProductSelectionController.hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(GroupedProductSelectionController.hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(GroupedProductSelectionController.hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(GroupedProductSelectionController.hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(GroupedProductSelectionController.hideView(_:)));
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
        
        if let view = self.view.viewWithTag(181) as? GroupedProductFilterView
        {
            if(view.value11.text != nil){
                value11 =  view.value11.text!
            }
            if(view.value12.text != nil){
                value12 = view.value12.text!
            }
            if(view.value21.text != nil){
                value21 = view.value21.text!
            }
            if(view.value22.text != nil){
                value22 = view.value22.text!
            }
            if(view.value31.text != nil){
                value31 = view.value31.text!
            }
            if(view.value32.text != nil){
                value32 = view.value32.text!
            }
            if(view.value4.text != nil){
                value4 = view.value4.text!
            }
            if(view.value5.text != nil){
                value5 = view.value5.text!
            }
//            {filter={"price":{"from":"","to":""},"qty":{"from":"","to":""},"entity_id":{"from":"","to":""},"name":"best","sku":""},
            
//            filter = "";
//            filter += "{";
//            filter += "\""+"price"+"\":{\"from\":\""+value11+"\",\"to\":\""+value12+"\"},";
//            filter += "\""+"name"+"\":\""+value2+"\",";
//            filter += "\""+"sku"+"\":\""+value3+"\",";
//            filter += "\""+"id"+"\":\""+value4+"\",";
//            filter += "\""+"check_status"+"\":\""+value5+"\",";
//            filter += "\""+"type_id"+"\":\""+value6+"\",";
//            filter += "\""+"attribute_set_id"+"\":\""+val_value7+"\"}";

            let filterData:[String:Any] = ["price":["from":value11,"to":value12],"qty":["from":value21,"to":value22],"entity_id":["from":value31,"to":value32],"name":value4,"sku":value5]
            
            filter = filterData.convtToJson() as String
            
            print(filter);
            self.view.viewWithTag(151)?.removeFromSuperview();
            products = [[String:String]]();
            currentpage = 1
            groupedProductTableView.reloadData()
            heightToUse = CGFloat(0);
            self.fetchRelatedProductListing();
            
        }
    }
    
    @objc func resetFilterTapped(_ sender:UIButton)
    {
        print("resetFilterTapped");
        value11 = ""
        value12  = ""
        value21 = ""
        value22 = ""
        value31  = ""
        value32  = ""
        value4 = ""
        value5  = ""
        filter = String();
        products = [[String:String]]();
        currentpage = 1
        groupedProductTableView.reloadData()
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.fetchRelatedProductListing();
    }
    
    
    @objc func savePressed(_ sender:UIButton)
    {
        print("savePressed");
        
        var qty = "{";
        var groupedproducts = "{\"links\":{\"related\":\"";
        
        for (proID) in selectedProductsIds
        {
            if let groupedProductView = self.view.viewWithTag(proID) as? GroupedProductView
            {
                var qtyToPick = groupedProductView.value5.text!;
                if(qtyToPick == "")
                {
                    qtyToPick = "0";
                }
                qty += "\""+String(proID)+"\":"+"\""+qtyToPick+"\",";
                groupedproducts += "#"+String(proID);
            }
        }
        qty = qty.substring(to: qty.characters.index(before: qty.endIndex));
        qty += "}";
        
        groupedproducts += "\"}}";
        
        print(groupedproducts);
        print(qty);
        
        print("updateProductInfo");
        
//        groupedproducts = groupedproducts.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!;
//        qty = qty.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!;
        
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
        if(selectedAttributeSetId != "")
        {
            postString += "&attribute_set="+selectedAttributeSetId;
        }
        if(groupedproducts != "")
        {
            postString += "&groupedproducts="+groupedproducts;
        }
        if(qty != "")
        {
            postString += "&groupedqty="+qty;
        }
   
        print(postString);
        Alert_File.addLoadingIndicator(self, msg: "Please Wait...")

        sendRequest(url: baseURL, parameters: postString);
        
    }
    
}
