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

class Website_Category_ViewController: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate {
    
    //check if app is basic or platinum
    var isBasicApp = false;
    
    var create_prod = false
    
    
    //edit time
    var isEditCase = false;
    //edit time
    
    var attributeSetId = String();
    var productType = String();
    var subcategoriesData = [String:[String]]();
    @IBOutlet weak var topWrapperView: UIView!
    @IBOutlet weak var web_cat_table_view: UITableView!
    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var proceedButton: UIButton!
    
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
    var proceeds: Bool = false;
    
    var proDetailArray = [[String:String]]();
    
    var baseURL = String();
    
    
    let screenSize: CGRect = UIScreen.main.bounds;
    let sub = CGFloat(20);
    //let colorString = mobi_common.getInfoPlist("theme_color") as! String;
    
    
    var selectedFiltersIds = [Int]();//use also at edit time
    var selectedWebsitesIds = [Int]();//use also at edit time
    var selectedFiltersIdsString = String();
    var selectedWebsitesIdsString = String();
    
    var config = [[String:String]]();
    var heightToUse1 = CGFloat(0);
    var heightToUse2 = CGFloat(0);
    let fixedHeight = CGFloat(30);
    let padding = CGFloat(5);
    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        if(selectedProductTypeId == "configurable"){
            saveButton.isHidden = true
        }
        
        print("------webtree-------")
        print(configSelected)
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        self.title = "Select Website/Categories"
        //let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB("#2abb9c");
        
        // Do any additional setup after loading the view.
        
        if(!Ced_CommonVendor().checkModule("Ced_VProductApi")){
            isBasicApp = true;
        }
        
      
        
        self.web_cat_table_view.delegate = self;
        self.web_cat_table_view.dataSource = self;
        
        proceedButton.setTitle("Continue".localized, for: UIControl.State());
        proceedButton.addTarget(self, action: #selector(Website_Category_ViewController.proceedToNextStep(_:)), for: UIControl.Event.touchUpInside);
        proceedButton.isHidden = true
        
        saveButton.setTitle("Save".localized, for: UIControl.State());
        saveButton.addTarget(self, action: #selector(Website_Category_ViewController.saveButtonPressed(_:)), for: UIControl.Event.touchUpInside);
        //ced_navigationBarController().addNavButton(self, str: "no")
        proceedButton.backgroundColor = color;
        saveButton.backgroundColor = DynamicColor.saveButtonColor;
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @objc func proceedToNextStep(_ sender:UIButton)
    {
        self.fetchValues();
        self.updateProductInfo(true);
    }
    
    @objc func saveButtonPressed(_ sender:UIButton)
    {
        print("saveButtonPressed");
        self.fetchValues();
        self.updateProductInfo(false);
    }
    
    func fetchValues()
    {
        if(selectedFiltersIds.count > 0)
        {
            selectedFiltersIdsString = "[";
            for (val) in selectedFiltersIds
            {
                selectedFiltersIdsString += "\""+String(val)+"\",";
            }
            selectedFiltersIdsString = selectedFiltersIdsString.substring(to: selectedFiltersIdsString.characters.index(before: selectedFiltersIdsString.endIndex));
            selectedFiltersIdsString += "]";
        }
        
        if(selectedWebsitesIds.count > 0)
        {
            selectedWebsitesIdsString = "[";
            for (val) in selectedWebsitesIds
            {
                selectedWebsitesIdsString += "\""+String(val)+"\",";
            }
            selectedWebsitesIdsString = selectedWebsitesIdsString.substring(to: selectedWebsitesIdsString.characters.index(before: selectedWebsitesIdsString.endIndex));
            selectedWebsitesIdsString += "]";
        }
    }
    
    
    func updateProductInfo(_ proceed:Bool)
    {
        proceeds=proceed;
        print("updateProductInfo");
        
        selectedFiltersIdsString = selectedFiltersIdsString.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!;
        
        selectedWebsitesIdsString = selectedWebsitesIdsString.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!;
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        
        baseURL = "vendorapi/vproducts/update/";
        
        print(baseURL);
        
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&product_id="+product_id;
        if create_prod{
            postString += "&create_prod=" + "\(create_prod)"
        }
        if(selectedFiltersIdsString != "")
        {
            postString += "&category="+selectedFiltersIdsString;
        }else{
            postString += "&delete_category=true"
        }
        if(selectedWebsitesIdsString != "")
        {
            postString += "&websites="+selectedWebsitesIdsString;
        }
        postString += "&attribute_set="+selectedAttributeSetId;
        
        print(postString);
        
        Alert_File.addLoadingIndicator(self, msg: "Please Wait...")
        sendRequest(url: baseURL, parameters: postString);
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        guard let jsonData = try? JSON(data: data!) else {return}
        print(jsonData);
        Alert_File.removeLoadingIndicator(self);
        
        if(jsonData["data"]["success"].stringValue != "true")
        {
            let title = "";
            let message = jsonData["data"]["message"].stringValue;
            Alert_File.showValidationError(self,title:title,message:message);
            return;
        }
        
        if(proceeds)
        {
            self.switchToNextView();
            proceeds=false;
        }
        else
        {
            let view = UIStoryboard(name: "ProductAddon", bundle: nil).instantiateViewController(withIdentifier: "manageProductViewController") as! ManageProductViewController
            self.navigationController?.setViewControllers([view], animated: true)
        }
    }
    
    // MARK: - Table view data source
    
    func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1;
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return 1;
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        
        if(indexPath.row == 1)
        {
            let cell = web_cat_table_view.dequeueReusableCell(withIdentifier: "toggleViewCell",for:indexPath) as! ToggleViewCell;
            
            
            //cell.makeCard(cell.wrapperView, cornerRadius: 2, color: UIColor.blackColor(), shadowOpacity: 0.4)
            
            if(cell.renderData)
            {
                return cell;
            }
            cell.renderData = true;
            
            cell.topButton.setTitle("Select Categories", for: UIControl.State());
            cell.topButton.backgroundColor = color;
            for (key,val) in categoriesList
            {
                let parentCategoryView = ParentCategoryView();
                parentCategoryView.backgroundColor = UIColor.black;
                parentCategoryView.frame = CGRect(x: 0, y: CGFloat(heightToUse2), width: screenSize.width-sub, height: fixedHeight+5);
                parentCategoryView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                
                let impArray = key.components(separatedBy: "#");
                
                parentCategoryView.parentFilterButton.setTitle(impArray.last!, for: UIControl.State());
                
                
                if let _ = selectedFiltersIds.index(of: Int(impArray.first!)!) {
                    parentCategoryView.parentFilterButton.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
                }
                else
                {
                    parentCategoryView.parentFilterButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
                }
                
                parentCategoryView.parentFilterButton.tag = Int(impArray.first!)!;
                parentCategoryView.parentFilterButton.addTarget(self, action: #selector(Website_Category_ViewController.makeFilterSelected(_:)), for: UIControl.Event.touchUpInside);
                
                cell.innerView.addSubview(parentCategoryView);
                heightToUse2 += fixedHeight+5;
                
                for inrVal in val
                {
                    let subCategoryView = SubCategoryView();
                    //subCategoryView.backgroundColor = UIColor.black;
                    subCategoryView.frame = CGRect(x: 20, y: CGFloat(heightToUse2), width: screenSize.width-sub, height: fixedHeight);
                    subCategoryView.autoresizingMask = [UIView.AutoresizingMask.flexibleRightMargin];
                    
                    let impArray = inrVal.components(separatedBy: "#");
                    
                    
                    subCategoryView.childFilterButton.setTitle(impArray.last!, for: UIControl.State());
                    
                    if let _ = selectedFiltersIds.index(of: Int(impArray.first!)!) {                        subCategoryView.childFilterButton.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
                    }
                    else
                    {
                        subCategoryView.childFilterButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
                    }
                    
                    subCategoryView.childFilterButton.tag = Int(impArray.first!)!;
                    subCategoryView.childFilterButton.addTarget(self, action: #selector(Website_Category_ViewController.makeFilterSelected(_:)), for: UIControl.Event.touchUpInside);
                    
                   // cell.innerView.addSubview(subCategoryView);
                    heightToUse2 += fixedHeight;
                    for (subkey,subvalue) in subcategoriesData
                    {
                        if(subkey == impArray.first!)
                        {
                            for(nestedinrVal) in subvalue
                            {
                                let nestedsubCategoryView = SubCategoryView();
                               // nestedsubCategoryView.backgroundColor = UIColor.black;
                                nestedsubCategoryView.frame = CGRect(x: 40, y: CGFloat(heightToUse2), width: screenSize.width-sub, height: fixedHeight);
                                nestedsubCategoryView.autoresizingMask = [UIView.AutoresizingMask.flexibleRightMargin];
                                let nestedimpArray = nestedinrVal.components(separatedBy: "#");
                                
                                
                                nestedsubCategoryView.childFilterButton.setTitle(nestedimpArray.last!, for: UIControl.State());
                                
                                if let _ = selectedFiltersIds.index(of: Int(nestedimpArray.first!)!) {                        nestedsubCategoryView.childFilterButton.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
                                }
                                else
                                {
                                    nestedsubCategoryView.childFilterButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
                                }
                                
                                nestedsubCategoryView.childFilterButton.tag = Int(nestedimpArray.first!)!;
                                nestedsubCategoryView.childFilterButton.addTarget(self, action: #selector(Website_Category_ViewController.makeFilterSelected(_:)), for: UIControl.Event.touchUpInside);
                                cell.innerView.addSubview(nestedsubCategoryView);
                                heightToUse2 += fixedHeight;
                            }
                            
                        }
                        
                    }
                }
                
            }
            
            return cell;
            
        }
        
        
        let cell = web_cat_table_view.dequeueReusableCell(withIdentifier: "toggleViewCell",for:indexPath) as! ToggleViewCell;
        //cell.makeCard(cell.wrapperView, cornerRadius: 2, color: UIColor.blackColor(), shadowOpacity: 0.4)
        if(cell.renderData)
        {
            return cell;
        }
        cell.renderData = true;
        
        cell.topButton.setTitle("Select Websites".localized, for: UIControl.State());
        cell.topButton.backgroundColor = color;
        
        for (key,val) in websites
        {
            /** Checkbox Button **/
            let checkboxButtonView = SubCategoryView();
            checkboxButtonView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            checkboxButtonView.frame = CGRect(x: 0, y: heightToUse1, width: screenSize.width-sub,height: fixedHeight);
            checkboxButtonView.center.x = cell.innerView.center.x;
            heightToUse1 += fixedHeight;
            heightToUse1 += padding;
            
           
            if let _ = selectedWebsitesIds.index(of: Int(val)!) {
                checkboxButtonView.childFilterButton.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
            }
            else
            {
                checkboxButtonView.childFilterButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
            }
            
            checkboxButtonView.childFilterButton.setTitle(key, for: UIControl.State());
            /*if(selectedWebsitesIds.count==0)
             {
             if(key.lowercased() == "main website")
             {
             checkboxButtonView.childFilterButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControlState());
             self.selectedWebsitesIds.append(Int(value)!);
             }
             }*/
            checkboxButtonView.childFilterButton.tag = Int(val)!;
            checkboxButtonView.childFilterButton.addTarget(self, action: #selector(Website_Category_ViewController.selectWebsites(_:)), for: UIControl.Event.touchUpInside);
            cell.innerView.addSubview(checkboxButtonView);
            
            
//            for (inrKey,inrVal) in val
//            {
//                /** Bold Label **/
//                let labelView = LabelView();
//                labelView.backgroundColor = DynamicColor.secondaryViewBackground
//                labelView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
//                labelView.frame = CGRect(x: 0, y: heightToUse1, width: screenSize.width-sub,height: fixedHeight);
//                labelView.center.x = cell.innerView.center.x;
//                heightToUse1 += fixedHeight;
//                heightToUse1 += padding;
//                
//                labelView.label.text = inrKey;
//                labelView.label.font = UIFont.boldSystemFont(ofSize: 15.0);
//                cell.innerView.addSubview(labelView);
//                
//                for inrMostLabel in inrVal
//                {
//                    /** Simple Label **/
//                    let labelView = LabelView();
//                    labelView.backgroundColor = DynamicColor.secondaryViewBackground
//                    labelView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
//                    labelView.frame = CGRect(x: 0, y: heightToUse1, width: screenSize.width-sub,height: fixedHeight);
//                    labelView.center.x = cell.innerView.center.x;
//                    heightToUse1 += fixedHeight;
//                    heightToUse1 += padding;
//                    
//                    labelView.label.text = "    "+inrMostLabel;
//                    print("inrMostLabel\(inrMostLabel)")
//                    print(heightToUse1)
//                    cell.innerView.addSubview(labelView);
//                }
//            }
        }
//        tableView.beginUpdates()
//        tableView.endUpdates()
        return cell;
    }
    
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        if(indexPath.row == 0)
        {
            return heightToUse1 + 40;
        }
        return heightToUse2+100;
    }
    
    @objc func selectWebsites(_ sender:UIButton)
    {
        let value = self.websites[(sender.titleLabel?.text)!];
        if(sender.imageView!.image == UIImage(named:"unchecked_checkbox"))
        {
            sender.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
            self.selectedWebsitesIds.append(Int(value!)!);
        }
        else
        {
            sender.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
            if let indexToRemove = selectedWebsitesIds.index(of: Int(value!)!) {
                self.selectedWebsitesIds.remove(at: indexToRemove);
            }
        }
        
        print(selectedWebsitesIds);
    }
    
    @objc func makeFilterSelected(_ sender:UIButton)
    {
        if(sender.imageView!.image == UIImage(named:"unchecked_checkbox"))
        {
            sender.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
            self.selectedFiltersIds.append(sender.tag);
        }
        else
        {
            sender.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
            if let indexToRemove = selectedFiltersIds.index(of: sender.tag) {
                self.selectedFiltersIds.remove(at: indexToRemove);
            }
        }
        
        //print(self.selectedFiltersIds);
        //print("");
    }
    
    func switchToNextView()
    {
        print("switchToNextView12345678910");
        print(self.tabs);
        UserDefaults.standard.set(selectedWebsitesIdsString, forKey: "selectedWebsite")
        print(UserDefaults.standard.value(forKey: "selectedWebsite") as? String)
        if(self.tabs["related"] != nil)
        {
            let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "relatedProductsOptionsController") as! RelatedProductsOptionsController;
            
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
            
            self.navigationController?.pushViewController(viewcontrollor, animated: true)//setViewControllers([viewcontrollor], animated: true)
            return;
        }
        
        if(self.tabs["upsell"] != nil)
        {
            let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "upSellProductsOptionsController") as! UpSellProductsOptionsController;
            
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
            viewcontrollor
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
        
        if(selectedProductTypeId.lowercased() == "configurable".lowercased())
        {
            
            let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "configAttributeSelectionViewController") as! ConfigAttributeSelectionViewController;
            
            
            
            viewcontrollor.config = config;
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
            if(selectedFiltersIdsString != "")
            {
                viewcontrollor.selectedFiltersIdsString = selectedFiltersIdsString;
            }
            if(selectedWebsitesIdsString != "")
            {
                viewcontrollor.selectedWebsitesIdsString = selectedWebsitesIdsString;
            }
            
            self.navigationController?.pushViewController(viewcontrollor, animated: true)
            
            return;
        }
        
        
    }
    
}


/*import UIKit

class Website_Category_ViewController: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate {

    //check if app is basic or platinum
    var isBasicApp = false;
    
    
    @IBOutlet weak var saveOnlyButton: UIButton!
    
    //edit time
    var isEditCase = false;
    //edit time
    var config = [[String:String]]();
    var attributeSetId = String();
    var productType = String();
    var subcategoriesData = [String:[String]]();
    @IBOutlet weak var topWrapperView: UIView!
    @IBOutlet weak var web_cat_table_view: UITableView!
    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var proceedButton: UIButton!
    var attributes : JSON!;
    var categoryJSON : JSON!;
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
    //var attributes : JSON!;
    var configSelected = [String:String]();
    //
    var proceeds: Bool = false;
    
    var proDetailArray = [[String:String]]();
    
    var baseURL = String();
    

    let screenSize: CGRect = UIScreen.main.bounds;
    let sub = CGFloat(20);
    //let colorString = mobi_common.getInfoPlist("theme_color") as! String;
    
    
    var selectedFiltersIds = [Int]();//use also at edit time
    var selectedWebsitesIds = [Int]();//use also at edit time
    var selectedFiltersIdsString = String();
    var selectedWebsitesIdsString = String();
    
    
    var heightToUse1 = CGFloat(0);
    var heightToUse2 = CGFloat(0);
    let fixedHeight = CGFloat(30);
    let padding = CGFloat(5);
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedStringKey.foregroundColor: UIColor.white,NSAttributedStringKey.font: UIFont(name: "Roboto-Bold", size: 18)!]
        self.title = "Select Website/Categories"
        //let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        
        // Do any additional setup after loading the view.
        
        if(!Ced_CommonVendor().checkModule("Ced_VProductApi")){
            isBasicApp = true;
        }
        
        if(self.isBasicApp)
        {
            saveOnlyButton.setTitle("Save".localized, for: UIControlState());
            saveOnlyButton.addTarget(self, action: #selector(Website_Category_ViewController.saveButtonPressed(_:)), for: UIControlEvents.touchUpInside);
            saveOnlyButton.backgroundColor = color;
        }
        else
        {
            self.saveOnlyButton.isHidden = true;
        }
        
        self.web_cat_table_view.delegate = self;
        self.web_cat_table_view.dataSource = self;
        
        proceedButton.setTitle("Continue", for: UIControlState());
        proceedButton.addTarget(self, action: #selector(Website_Category_ViewController.proceedToNextStep(_:)), for: UIControlEvents.touchUpInside);
        
        saveButton.setTitle("Save".localized, for: UIControlState());
        saveButton.addTarget(self, action: #selector(Website_Category_ViewController.saveButtonPressed(_:)), for: UIControlEvents.touchUpInside);
        ced_navigationBarController().addNavButton(self, str: "no")
        proceedButton.backgroundColor = color;
        saveButton.backgroundColor = color;
    
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @objc func proceedToNextStep(_ sender:UIButton)
    {
        self.fetchValues();
        self.updateProductInfo(true);
    }
    
    @objc func saveButtonPressed(_ sender:UIButton)
    {
        print("saveButtonPressed");
        self.fetchValues();
        self.updateProductInfo(false);
    }
    
    @objc func fetchValues()
    {
        if(selectedFiltersIds.count > 0)
        {
            selectedFiltersIdsString = "[";
            for (val) in selectedFiltersIds
            {
                selectedFiltersIdsString += "\""+String(val)+"\",";
            }
            selectedFiltersIdsString = selectedFiltersIdsString.substring(to: selectedFiltersIdsString.characters.index(before: selectedFiltersIdsString.endIndex));
            selectedFiltersIdsString += "]";
        }
        
        if(selectedWebsitesIds.count > 0)
        {
            selectedWebsitesIdsString = "[";
            for (val) in selectedWebsitesIds
            {
                selectedWebsitesIdsString += "\""+String(val)+"\",";
            }
            selectedWebsitesIdsString = selectedWebsitesIdsString.substring(to: selectedWebsitesIdsString.characters.index(before: selectedWebsitesIdsString.endIndex));
            selectedWebsitesIdsString += "]";
        }
    }
    
    
    @objc func updateProductInfo(_ proceed:Bool)
    {
        //self.switchToNextView();
        proceeds=proceed;
        print("updateProductInfo");
        
        selectedFiltersIdsString = selectedFiltersIdsString.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!;
        
        selectedWebsitesIdsString = selectedWebsitesIdsString.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!;
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        
        baseURL = "vendorapi/vproducts/update/";
        
        print(baseURL);
        
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&product_id="+product_id;
        if(selectedFiltersIdsString != "")
        {
            postString += "&category="+selectedFiltersIdsString;
        }
        if(selectedWebsitesIdsString != "")
        {
            postString += "&websites="+selectedWebsitesIdsString;
        }
        postString += "&attribute_set="+selectedAttributeSetId;
        
        print(postString);
        
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        sendRequest(url: baseURL, parameters: postString);
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            let jsonData = JSON(data: data);
            print(jsonData);
            Alert_File.removeLoadingIndicator(self);
            
            if(jsonData["data"]["success"].stringValue != "true")
            {
                let title = "Error";
                let message = "Unfortunately Some Error Occured";
                Alert_File.showValidationError(self,title:title,message:message);
                return;
            }
            
            if(proceeds)
            {
                self.switchToNextView();
                proceeds=false;
            }
            else
            {
                let view = UIStoryboard(name: "ProductAddon", bundle: nil).instantiateViewController(withIdentifier: "manageProductViewController") as! ManageProductViewController
                self.navigationController?.setViewControllers([view], animated: true)
            }
        }
        
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
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        
        if(indexPath.row == 1)
        {
            let cell = web_cat_table_view.dequeueReusableCell(withIdentifier: "toggleViewCell",for:indexPath) as! ToggleViewCell;
            
            
            //cell.makeCard(cell.wrapperView, cornerRadius: 2, color: UIColor.blackColor(), shadowOpacity: 0.4)
            
            if(cell.renderData)
            {
                return cell;
            }
            cell.renderData = true;
            
            cell.topButton.setTitle("Select Categories", for: UIControlState());
            cell.topButton.backgroundColor = color;
            for (key,val) in categoriesList
            {
                let parentCategoryView = ParentCategoryView();
                parentCategoryView.backgroundColor = UIColor.black;
                parentCategoryView.frame = CGRect(x: 0, y: CGFloat(heightToUse2), width: screenSize.width-sub, height: fixedHeight+5);
                parentCategoryView.autoresizingMask = [UIViewAutoresizing.flexibleLeftMargin,UIViewAutoresizing.flexibleRightMargin];
                
                let impArray = key.components(separatedBy: "#");
                
                parentCategoryView.parentFilterButton.setTitle(impArray.last!, for: UIControlState());


                if let _ = selectedFiltersIds.index(of: Int(impArray.first!)!) {
                    parentCategoryView.parentFilterButton.setImage(UIImage(named: "checked_checkbox"), for: UIControlState());
                }
                else
                {
                   parentCategoryView.parentFilterButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControlState());
                }
                
                parentCategoryView.parentFilterButton.tag = Int(impArray.first!)!;
                parentCategoryView.parentFilterButton.addTarget(self, action: #selector(Website_Category_ViewController.makeFilterSelected(_:)), for: UIControlEvents.touchUpInside);
                
                cell.innerView.addSubview(parentCategoryView);
                heightToUse2 += fixedHeight+5;
                
                for inrVal in val
                {
                    let subCategoryView = SubCategoryView();
                    subCategoryView.backgroundColor = UIColor.black;
                    subCategoryView.frame = CGRect(x: 20, y: CGFloat(heightToUse2), width: screenSize.width-sub, height: fixedHeight);
                    subCategoryView.autoresizingMask = [UIViewAutoresizing.flexibleRightMargin];
                    
                    let impArray = inrVal.components(separatedBy: "#");
                    
                    
                    subCategoryView.childFilterButton.setTitle(impArray.last!, for: UIControlState());
                    
                    if let _ = selectedFiltersIds.index(of: Int(impArray.first!)!) {                        subCategoryView.childFilterButton.setImage(UIImage(named: "checked_checkbox"), for: UIControlState());
                    }
                    else
                    {
                        subCategoryView.childFilterButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControlState());
                    }

                    subCategoryView.childFilterButton.tag = Int(impArray.first!)!;
                    subCategoryView.childFilterButton.addTarget(self, action: #selector(Website_Category_ViewController.makeFilterSelected(_:)), for: UIControlEvents.touchUpInside);
                    
                    cell.innerView.addSubview(subCategoryView);
                    heightToUse2 += fixedHeight;
                    for (subkey,subvalue) in subcategoriesData
                    {
                        if(subkey == impArray.first!)
                        {
                            for(nestedinrVal) in subvalue
                            {
                                let nestedsubCategoryView = SubCategoryView();
                                nestedsubCategoryView.backgroundColor = UIColor.black;
                                nestedsubCategoryView.frame = CGRect(x: 40, y: CGFloat(heightToUse2), width: screenSize.width-sub, height: fixedHeight);
                                nestedsubCategoryView.autoresizingMask = [UIViewAutoresizing.flexibleRightMargin];
                                let nestedimpArray = nestedinrVal.components(separatedBy: "#");
                                
                                
                                nestedsubCategoryView.childFilterButton.setTitle(nestedimpArray.last!, for: UIControlState());
                                
                                if let _ = selectedFiltersIds.index(of: Int(nestedimpArray.first!)!) {                        nestedsubCategoryView.childFilterButton.setImage(UIImage(named: "checked_checkbox"), for: UIControlState());
                                }
                                else
                                {
                                    nestedsubCategoryView.childFilterButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControlState());
                                }
                                
                                nestedsubCategoryView.childFilterButton.tag = Int(nestedimpArray.first!)!;
                                nestedsubCategoryView.childFilterButton.addTarget(self, action: #selector(Website_Category_ViewController.makeFilterSelected(_:)), for: UIControlEvents.touchUpInside);
                                cell.innerView.addSubview(nestedsubCategoryView);
                                heightToUse2 += fixedHeight;
                            }
                            
                        }
                        
                    }
                }
                
            }
            
            return cell;
            
        }
        
        
        let cell = web_cat_table_view.dequeueReusableCell(withIdentifier: "toggleViewCell",for:indexPath) as! ToggleViewCell;
        //cell.makeCard(cell.wrapperView, cornerRadius: 2, color: UIColor.blackColor(), shadowOpacity: 0.4)
        if(cell.renderData)
        {
            return cell;
        }
        cell.renderData = true;
        
        cell.topButton.setTitle("Select Websites", for: UIControlState());
        cell.topButton.backgroundColor = color;
        
        for (key,val) in storeViews
        {
            /** Checkbox Button **/
            let checkboxButtonView = SubCategoryView();
            checkboxButtonView.autoresizingMask = [UIViewAutoresizing.flexibleLeftMargin,UIViewAutoresizing.flexibleRightMargin];
            checkboxButtonView.frame = CGRect(x: 0, y: heightToUse1, width: screenSize.width-sub,height: fixedHeight);
            checkboxButtonView.center.x = cell.innerView.center.x;
            heightToUse1 += fixedHeight;
            heightToUse1 += padding;
            
            let value = self.websites[key]!;//getting unique id
            
            if let _ = selectedWebsitesIds.index(of: Int(value)!) {
                checkboxButtonView.childFilterButton.setImage(UIImage(named: "checked_checkbox"), for: UIControlState());
            }
            else
            {
                checkboxButtonView.childFilterButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControlState());
            }
            
            checkboxButtonView.childFilterButton.setTitle(key, for: UIControlState());
            checkboxButtonView.childFilterButton.tag = Int(value)!;
            checkboxButtonView.childFilterButton.addTarget(self, action: #selector(Website_Category_ViewController.selectWebsites(_:)), for: UIControlEvents.touchUpInside);
            cell.innerView.addSubview(checkboxButtonView);
            
            
            for (inrKey,inrVal) in val
            {
                /** Bold Label **/
                let labelView = LabelView();
                labelView.autoresizingMask = [UIViewAutoresizing.flexibleLeftMargin,UIViewAutoresizing.flexibleRightMargin];
                labelView.frame = CGRect(x: 0, y: heightToUse1, width: screenSize.width-sub,height: fixedHeight);
                labelView.center.x = cell.innerView.center.x;
                heightToUse1 += fixedHeight;
                heightToUse1 += padding;
                
                labelView.label.text = inrKey;
                labelView.label.font = UIFont.boldSystemFont(ofSize: 15.0);
                cell.innerView.addSubview(labelView);
                
                for inrMostLabel in inrVal
                {
                    /** Simple Label **/
                    let labelView = LabelView();
                    labelView.autoresizingMask = [UIViewAutoresizing.flexibleLeftMargin,UIViewAutoresizing.flexibleRightMargin];
                    labelView.frame = CGRect(x: 0, y: heightToUse1, width: screenSize.width-sub,height: fixedHeight);
                    labelView.center.x = cell.innerView.center.x;
                    heightToUse1 += fixedHeight;
                    heightToUse1 += padding;
                    
                    labelView.label.text = "    "+inrMostLabel;
                    cell.innerView.addSubview(labelView);
                }
            }
        }
        
        return cell;
    }
    
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        if(indexPath.row == 0)
        {
            return heightToUse1;
        }
        return heightToUse2+100;
    }
    
    @objc func selectWebsites(_ sender:UIButton)
    {
        let value = self.websites[(sender.titleLabel?.text)!];
        if(sender.imageView!.image == UIImage(named:"unchecked_checkbox"))
        {
            sender.setImage(UIImage(named: "checked_checkbox"), for: UIControlState());
            self.selectedWebsitesIds.append(Int(value!)!);
        }
        else
        {
            sender.setImage(UIImage(named: "unchecked_checkbox"), for: UIControlState());
            if let indexToRemove = selectedWebsitesIds.index(of: Int(value!)!) {
                self.selectedWebsitesIds.remove(at: indexToRemove);
            }
        }
        
        print(selectedWebsitesIds);
    }
    
    @objc func makeFilterSelected(_ sender:UIButton)
    {
        if(sender.imageView!.image == UIImage(named:"unchecked_checkbox"))
        {
            sender.setImage(UIImage(named: "checked_checkbox"), for: UIControlState());
            self.selectedFiltersIds.append(sender.tag);
        }
        else
        {
            sender.setImage(UIImage(named: "unchecked_checkbox"), for: UIControlState());
            if let indexToRemove = selectedFiltersIds.index(of: sender.tag) {
                self.selectedFiltersIds.remove(at: indexToRemove);
            }
        }
        
        //print(self.selectedFiltersIds);
        //print("");
    }
    
    @objc func switchToNextView()
    {
        print("switchToNextView12345678910");
        print(self.tabs);
        if(self.tabs["related"] != nil)
        {
            let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "relatedProductsOptionsController") as! RelatedProductsOptionsController;
            
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
        
        if(self.tabs["upsell"] != nil)
        {
            let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "upSellProductsOptionsController") as! UpSellProductsOptionsController;
            
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
        if(selectedProductTypeId.lowercased() == "configurable".lowercased())
        {
            
            let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "configAttributeSelectionViewController") as! ConfigAttributeSelectionViewController;
            
            
            
            viewcontrollor.config = config;
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
            if(selectedFiltersIdsString != "")
            {
                viewcontrollor.selectedFiltersIdsString = selectedFiltersIdsString;
            }
            if(selectedWebsitesIdsString != "")
            {
                viewcontrollor.selectedWebsitesIdsString = selectedWebsitesIdsString;
            }
            
            self.navigationController?.pushViewController(viewcontrollor, animated: true)
            
            return;
        }
        /*let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "productDetailsViewController") as! ProductDetailsViewController;
        viewcontrollor.selectedProductTypeId = selectedProductTypeId;
        viewcontrollor.selectedAttributeSetId = selectedAttributeSetId;
        viewcontrollor.stock = self.stock;
        viewcontrollor.taxes = self.taxes;
        viewcontrollor.tabs = self.tabs;
        if(selectedFiltersIdsString != "")
        {
            viewcontrollor.selectedFiltersIdsString = selectedFiltersIdsString;
        }
        if(selectedWebsitesIdsString != "")
        {
            viewcontrollor.selectedWebsitesIdsString = selectedWebsitesIdsString;
        }
        
        viewcontrollor.storeViews = self.storeViews;
        viewcontrollor.websites = self.websites;
        viewcontrollor.categoriesList = self.categoriesList;
        viewcontrollor.attributes = self.attributes;
        //viewcontrollor.categoryJson = self.categoryJSON
        viewcontrollor.categoryJson = self.categoryJSON;
        
        self.navigationController?.pushViewController(viewcontrollor, animated: true)*/


        
        
    }
  
}
*/
