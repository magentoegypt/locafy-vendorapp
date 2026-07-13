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

class AddProductAddonFirstPage: ced_VendorBaseClass {
    
    @IBOutlet weak var productTypeTitle: UILabel!

    var categoryJSON: JSON!;
    //check if app is basic or platinum
    var isBasicApp = false;
    
    @IBOutlet weak var topMarginOfContinueButton: NSLayoutConstraint!
    @IBOutlet weak var attributeSetLabel: UILabel!
    @IBOutlet weak var mainViewHeight: NSLayoutConstraint!
    @IBOutlet weak var topButton: UIButton!
    @IBOutlet weak var continueButton: UIButton!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var selectProductButton: UIButton!
    @IBOutlet weak var selectAttributeButton: UIButton!
    
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
    
    
    var baseURL = String();
    
    var config = [[String:String]]();// will be get only in case of config product
    
    var allowed_pro_type = [String:String]();
    var allowed_attribute_set = [String:String]();
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("Product addon------");
        // Do any additional setup after loading the view.
        productTypeTitle.text="Product Type :".localized
        attributeSetLabel.text="Attribute Set  :".localized
        continueButton.setTitle("Continue".localized, for: .normal)
        self.view.backgroundColor = DynamicColor.ViewBackgroundColor
        
        
        if(!Ced_CommonVendor().checkModule("Ced_VProductApi")){
            isBasicApp = true;
        }
        
        
        if(isBasicApp)
        {
            self.attributeSetLabel.isHidden = true;
            self.selectAttributeButton.isHidden = true;
            self.mainViewHeight.constant = 210;
            self.topMarginOfContinueButton.constant = 10;
        }
        
        
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        
        self.topButton.setTitle(NSLocalizedString("Select Product Type".localized, comment: ""), for: UIControl.State());
        self.topButton.backgroundColor = DynamicColor.secondaryColor;
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.3);
        continueButton.addTarget(self, action: #selector(AddProductAddonFirstPage.continueTapped(_:)), for: UIControl.Event.touchUpInside);
        
        ced_navigationBarController().addNavButton(self,str:"no".localized)
        continueButton.backgroundColor = DynamicColor.secondaryColor
        
       selectProductButton.addTarget(self, action: #selector(AddProductAddonFirstPage.selectProductType(_:)), for: UIControl.Event.touchUpInside);
        selectAttributeButton.addTarget(self, action: #selector(AddProductAddonFirstPage.selectAttributeSet(_:)), for: UIControl.Event.touchUpInside)
        
        containerView.backgroundColor = DynamicColor.secondaryViewBackground
      //  selectProductButton.backgroundColor = DynamicColor.tertiaryViewBackground
        selectAttributeButton.backgroundColor = DynamicColor.tertiaryViewBackground
        selectProductButton.setTitle("SimpleProduct".localized, for: .normal)
        self.fetchRequiredInfo();
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func continueTapped(_ sender:UIButton){
        let selectedProductTypeId = self.allowed_pro_type[self.selectedProductTypeId];//"simple"//
        let selectedAttributeSetId = self.allowed_attribute_set[self.selectedAttributeSetId];
        
        
        print(selectedProductTypeId);
        print(selectedAttributeSetId);
        
        self.fetchNextStepsInfo(selectedProductTypeId!,selectedAttributeSetId: selectedAttributeSetId!);
        
    }
    
    @objc func selectProductType(_ sender:UIButton){
        
        let dropDown = DropDown();
        dropDown.dataSource = Array(self.allowed_pro_type.keys).reversed();
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            self.selectedProductTypeId = item;
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
        
    }
    
    @objc func selectAttributeSet(_ sender:UIButton){
        
        let dropDown = DropDown();
        dropDown.dataSource = Array(self.allowed_attribute_set.keys).reversed();
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            self.selectedAttributeSetId = item;
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
        
    }
    
    /** @objc function to fecth product types from API **/
    @objc func fetchRequiredInfo()
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        
        var baseURL = "vproductapi/vproducts/alloweddata/";
        
        print(baseURL);
        
        let request = NSMutableURLRequest(url: URL(string: baseURL)!);
        request.httpMethod = "POST";
        let postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        print(postString)
        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Please Wait...".localized, comment: ""))
        sendRequest(url: baseURL, parameters: postString);
    }
    
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        guard let jsonData = try? JSON(data: data!) else {return}
        print("jsonData \(requestUrl)");
        
        print(NSString(data: data!, encoding: String.Encoding.utf8.rawValue));
        Alert_File.removeLoadingIndicator(self);
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let customer_id = userData["customerId"] as! String;
        let vendor_name = userData["vendorName"] as! String;
        let profile_picture = userData["profilePicUrl"] as! String;
        let profile_complete = userData["profile_complete"] as! String
        let valerts = jsonData["data"]["valerts"].stringValue
        //saving value in NSUserDefault to use later on :: start
        let dict = ["customerId": customer_id, "hashKey": hashKey,"vendorName":vendor_name,"vendorId":vendorId,"profilePicUrl":profile_picture,"valerts":valerts,"profile_complete":profile_complete];
        self.defaults.set(dict, forKey: "userInfoDict");
        
        if(jsonData["data"]["success"].stringValue != "true")
        {
            let title = "";
            let message = jsonData["data"]["message"].stringValue;
            Alert_File.showValidationError(self,title:title,message:message);
            return;
        }
        
        /** fetching product types **/
        var firstButtonText = String();
        var counter = 0;
        for (_,val) in jsonData["data"]["allowed_pro_type"]
        {
            if( counter == 0 && val["label"].stringValue != "" )
            {
                counter += 1;
                firstButtonText = val["label"].stringValue;
                self.selectedProductTypeId = val["label"].stringValue;
            }
            self.allowed_pro_type[val["label"].stringValue] = val["value"].stringValue;
        }
        if(jsonData["data"]["category"].exists())
        {
            self.fectchParentAndChildCategories(jsonData["data"]["category"]);
        }
        self.allowed_pro_type.removeValue(forKey: "");
        /** fetching stock values **/
        /*for (key,val) in jsonData["data"]["stock"]
        {
            self.stock[val.stringValue] = key;
        }
        
        /** fetching stock values **/
        for (key,val) in jsonData["data"]["taxes"]
        {
            self.taxes[val.stringValue] = key;
        }*/
        
        
        print("-----fetching----")
        /** fetching websites data **/
        for (key,val) in jsonData["data"]["storeViews"]
        {
            var temp = [String:[String]]();
            for (inrKey,inrVal) in val
            {
                temp[inrKey] = inrVal.arrayObject as? [String];
            }
            
            self.storeViews[key] = temp;
            
        }
        
        /** fetching websites **/
        for (key,val) in jsonData["data"]["websites"]
        {
            self.websites[val.stringValue] = key;
        }
      //  self.selectProductButton.setTitle(firstButtonText, for: UIControl.State());
        
        /** fetching attributes set **/
        firstButtonText = String();
        counter = 0;
        for (_,val) in jsonData["data"]["allowed_attribute_set"]
        {
            if( counter == 0 && val["label"].stringValue != "" )
            {
                counter += 1;
                firstButtonText = val["label"].stringValue;
                self.selectedAttributeSetId = val["label"].stringValue;
            }
            self.allowed_attribute_set[val["label"].stringValue] = val["value"].stringValue;
        }
        self.allowed_attribute_set.removeValue(forKey: "");
        self.selectAttributeButton.setTitle(firstButtonText, for: UIControl.State());
        
    }
    
    @objc func fetchNextStepsInfo(_ selectedProductTypeId:String,selectedAttributeSetId:String)
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
        baseURL = settings.baseUrl
        baseURL += "vproductapi/vproducts/allowedAttribute";
        print(baseURL);
        let request = NSMutableURLRequest(url: URL(string: baseURL)!);
        
        request.httpMethod = "POST";
        var postString = "vendor_id="+vendorId+"&hashkey="+hashKey;
        postString += "&type="+selectedProductTypeId;
        postString += "&set="+selectedAttributeSetId;
        
        print(postString);
        request.httpBody = postString.data(using: String.Encoding.utf8);
        
        
        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Please Wait...".localized, comment: ""))
        
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request as URLRequest) { (data, response, error) in
            guard error == nil && data != nil else
            {
                print("error=\(error)");
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self);
                    let title = NSLocalizedString("Error".localized, comment: "");
                    let message = NSLocalizedString("Some Network Error Occured!".localized, comment: "");
                        Alert_File.showNetworkIssue(self,title:title,message:message);
                }
                return;
            }
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                print("response = \(response)");
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self);
                        
                        if(httpStatus.statusCode == -1009){
                            ced_showError.showNoNetWork(self)
                        }else{
                            ced_showError.showNoModule(self)
                        }
                }
                return;
            }
            
            // code to fetch values from response :: start
            guard let jsonData = try? JSON(data: data!) else {return}
            DispatchQueue.main.async
                {
                    print("---jsonData----");
                    print(jsonData);
                    Alert_File.removeLoadingIndicator(self);
                    print("-------")
                    /*if(jsonData["data"]["success"].stringValue != "true")
                    {
                        let title = "";
                        let message = jsonData["data"]["message"].stringValue;
                        Alert_File.showValidationError(self,title:title,message:message);
                        return;
                    }*/
                    
                    
                    
                    
                    /** fetching tabs **/
                    for (key,val) in jsonData["data"]["tabs"]
                    {
                        self.tabs[key] = val.stringValue;
                    }
                    
                    print("self.tabs");
                    print(self.tabs);
                    
                    print("COOONFFFIGGGG");
                    self.config.removeAll()
                    if(jsonData["data"]["config"] != nil)
                    {
                        for (val) in jsonData["data"]["config"].arrayValue
                        {
                            var temp = [String:String]();
                            for(inrKey,inrVal) in val
                            {
                                temp[inrKey] = inrVal.stringValue;
                            }
                            self.config.append(temp);
                            print(temp);
                        }
                        print(self.config);
                        
                        
//                        if(selectedProductTypeId.lowercased() == "configurable".lowercased())
//                        {
//                            
//                            let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "configAttributeSelectionViewController") as! ConfigAttributeSelectionViewController;
//                            
//                            
//                            viewcontrollor.config = self.config;
//                            viewcontrollor.selectedProductTypeId = selectedProductTypeId;
//                            viewcontrollor.selectedAttributeSetId = selectedAttributeSetId;
//                            viewcontrollor.stock = self.stock;
//                            viewcontrollor.taxes = self.taxes;
//                            viewcontrollor.tabs = self.tabs;
//                            viewcontrollor.storeViews = self.storeViews;
//                            viewcontrollor.websites = self.websites;
//                            viewcontrollor.categoriesList = self.categoriesList;
//                            viewcontrollor.attributes = jsonData["data"]["attributes"];
//                            viewcontrollor.categoryJson = self.categoryJSON
//                            
//                            
//                            self.navigationController?.pushViewController(viewcontrollor, animated: true)
//                            
//                            return;
//                        }
                        
                    }
                    /*let viewcontrollor = CategoryTreeViewController()
                    viewcontrollor.categoriesList = self.categoriesList
                    viewcontrollor.categoryJson = self.categoryJSON;
                    viewcontrollor.config = self.config
                    /*viewcontrollor.subcategoriesData = subcategoriesData;
                    
                    //viewcontrollor.isEditCase = isEditCase;
                    viewcontrollor.selectedFiltersIds = productSelectedCategories;
                    viewcontrollor.selectedWebsitesIds = productSelectedWebsites;
                    //edit time
                    viewcontrollor.subcategoriesData = subcategoriesData;*/
                    
                    viewcontrollor.selectedProductTypeId = selectedProductTypeId;
                    viewcontrollor.selectedAttributeSetId = selectedAttributeSetId;
                    viewcontrollor.stock = self.stock;
                    viewcontrollor.taxes = self.taxes;
                    viewcontrollor.tabs = self.tabs;
                    viewcontrollor.storeViews = self.storeViews;
                    viewcontrollor.websites = self.websites;
                    viewcontrollor.categoriesList = self.categoriesList;
                    viewcontrollor.attributes = jsonData["data"]["attributes"];
                    viewcontrollor.product_id = self.product_id;
                    viewcontrollor.configSelected = self.configSelected;
                    self.navigationController?.pushViewController(viewcontrollor, animated: true);
                    
                    print("--out--")*/
                    let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "productDetailsViewController") as! ProductDetailsViewController;
                    viewcontrollor.config = self.config;
                    viewcontrollor.selectedProductTypeId = selectedProductTypeId;
                    viewcontrollor.selectedAttributeSetId = selectedAttributeSetId;
                    viewcontrollor.stock = self.stock;
                    viewcontrollor.taxes = self.taxes;
                    viewcontrollor.tabs = self.tabs;
                    viewcontrollor.storeViews = self.storeViews;
                    viewcontrollor.websites = self.websites;
                    viewcontrollor.categoriesList = self.categoriesList;
                    viewcontrollor.attributes = jsonData["data"]["attributes"];
                    viewcontrollor.categoryJson = self.categoryJSON;
                    
                    self.navigationController?.pushViewController(viewcontrollor, animated: true)
                    
                    //self.navigationController?.pushViewController(viewcontrollor, animated: true)
                    
                    
            }
        }
        
        task.resume();
    }
    
    func fectchParentAndChildCategories(_ browseByJSON:JSON)
    {
        print("hello")
        categoryJSON = browseByJSON;
        print(browseByJSON);
        for (_,val) in browseByJSON
        {
            print("---------\(val)")
            if val["sub_categories"] != nil
            {
                var temp = [String]();
                for (_,inrVal) in val["sub_categories"]
                {
                    temp.append(inrVal["main_category"].stringValue);
                }
                self.categoriesList[val["main_category"].stringValue] = temp;
                print("--")
            }
        }
        print("---")
        //return;
        
    }
    
    
}
