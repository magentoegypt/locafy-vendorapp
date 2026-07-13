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

class ConfigProductSelectionController: ced_VendorBaseClass ,UITableViewDataSource,UITableViewDelegate,UIGestureRecognizerDelegate {
    
    //edit time
    var isEditCase = false;
    //edit time
    
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
    
    
    //filtersection
    var value11 = ""
    var value12  = ""
    var value2 = ""
    var value3  = ""
    var value4 = ""
    var value5  = ""
    var value6 = ""
    var filter = String();
    let productInventory = ["Enable":"1","Disable":"0"];
    var sets = [String:String]();
    //
    
    
    //var productTypeID = String();
    
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var groupedProductTableView: UITableView!
    @IBOutlet weak var topView: UIView!
    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var filterButton: UIButton!
    
    @IBOutlet weak var setPriceButton: UIButton!
    var currentSenderTag = 0;
    
    
    
    let priceTypeArray = [NSLocalizedString("Fixed", comment: ""), NSLocalizedString("Percentage", comment: "")];
    let userDefinedQtyArray = [NSLocalizedString("Yes", comment: ""), NSLocalizedString("No", comment: "")];
    
    var baseURL = String();
    let screenSize: CGRect = UIScreen.main.bounds;
    
    let relatedProCellHeight = CGFloat(190);
    
    
    var heightToUse = CGFloat(0);
    
    //var to use in this view only
    var products = [[String:String]]();
    var selectedProductsIds = [Int]();
    
    var necesssaryInfoForUpdate = [Int:[[String:String]]]();
    
    var selectedCombination = [Int:String]();//use to match and avoid more selection of same type
    
    var attributesPriceArray = [String:[String:[String:String]]](); // imp array to update price of attributes
    var attributesPriceArrayHelper = [String:[String:String]]();
    var options = [String:[String:String]]();// use in edit case mainly
    var page = 1;
    var loading = true;
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        //print("ConfigProductSelectionController");
        
        print("----configSelected new---");
        print(configSelected);
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 15)!]
        topLabel.text = "Config Products".localized
        ced_navigationBarController().addNavButton(self,str:"no")
        self.groupedProductTableView.delegate = self;
        self.groupedProductTableView.dataSource = self;
        
        defaults.removeObject(forKey: "attributesPriceArray");
        NotificationCenter.default.addObserver(self, selector: #selector(ConfigProductSelectionController.updateAttributesPriceArrayFunc(_:)),name:NSNotification.Name(rawValue: "updateAttributesPriceArrayFuncID"), object: nil);
        
        filterButton.addTarget(self, action: #selector(ConfigProductSelectionController.filterPressed(_:)), for: UIControl.Event.touchUpInside);
        
        saveButton.setTitle(NSLocalizedString("Save".localized, comment: ""), for: UIControl.State());
        saveButton.addTarget(self, action: #selector(ConfigProductSelectionController.savePressed(_:)), for: UIControl.Event.touchUpInside);
        
        setPriceButton.addTarget(self, action: #selector(ConfigProductSelectionController.setPriceButtonPressed(_:)), for: UIControl.Event.touchUpInside);
        setPriceButton.frame = CGRect(x: 160, y: 100, width: 50, height: 50);
        setPriceButton.layer.cornerRadius = 0.5 * setPriceButton.bounds.size.width;
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String;
        let theme_color = Ced_CommonVendor.UIColorFromRGB(colorString!);
        setPriceButton.backgroundColor = theme_color;
        setPriceButton.isHidden = true
        let color = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        saveButton.backgroundColor = DynamicColor.themeColor;
        
        self.fetchRelatedProductListing();
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func updateAttributesPriceArrayFunc(_ notification: Notification)
    {
        let attributesPriceArrayTemp = defaults.object(forKey: "attributesPriceArray") as! [String:[String:[String:String]]];
        self.attributesPriceArray = attributesPriceArrayTemp;
        
        //print("CCCCCC");
        self.view.showToastMsg(NSLocalizedString("Price Successfully Applied. Now Save the Changes.", comment: ""));
    }
    
    /** @objc func to calculate  **/
    @objc func calculateHeightForString(_ inString:String) -> CGFloat
    {
        let messageString = inString
        //var attributes = [UIFont(): UIFont.systemFontOfSize(15.0)]
        let attrString:NSAttributedString? = NSAttributedString(string: messageString, attributes: nil)
        let rect:CGRect = attrString!.boundingRect(with: CGSize(width: 160.0,height: CGFloat.greatestFiniteMagnitude), options: NSStringDrawingOptions.usesLineFragmentOrigin, context:nil )//hear u will get nearer height not the exact value
        let requredSize:CGRect = rect
        return requredSize.height  //to include button's in your tableview
        
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
            let heightExtra = self.calculateHeightForString(val["attribute"]!);
            
            let configItemView = ConfigItemView();
            configItemView.tag = Int(val["product_id"]!)!;
            configItemView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            configItemView.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: relatedProCellHeight+heightExtra);
            
            
            heightToUse += relatedProCellHeight+heightExtra;
            
            if(val["selected"] == "false"){
                configItemView.selectionButton.setImage(UIImage(named: "unchecked_checkbox_white"), for: UIControl.State());
            }
            else
            {
                configItemView.selectionButton.setImage(UIImage(named: "checked_checkbox_white"), for: UIControl.State());
                self.selectedProductsIds.append(Int(val["product_id"]!)!);
                self.selectedCombination[Int(val["product_id"]!)!] = (val["attribute"]!);
            }
            
            configItemView.selectionButton.setTitle("#"+val["product_name"]!, for: UIControl.State());
            
            configItemView.selectionButton.addTarget(self, action: #selector(ConfigProductSelectionController.makeProductSelected(_:)), for: UIControl.Event.touchUpInside);
            configItemView.selectionButton.tag = Int(val["product_id"]!)!;
            
            configItemView.label1.text = NSLocalizedString("Product SKU", comment: "");
            configItemView.value1.text = val["sku"];
            
            configItemView.label2.text = NSLocalizedString("Product Price", comment: "");
            configItemView.value2.text = val["regular_price"];
            
            configItemView.label3.text = NSLocalizedString("Product Status", comment: "");
            configItemView.value3.text = val["Inventory"];
            
            configItemView.label4.text = NSLocalizedString("Attribute Set", comment: "");
            configItemView.value4.text = val["set_name"];
            
            if(val["attribute"] == nil || val["attribute"] == "")
            {
                configItemView.lowerView.isHidden = true;
            }
            else
            {
                configItemView.lowerView.text = val["attribute"];
                
                configItemView.lowerView.makeCard(configItemView.lowerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
            }
            
            configItemView.coreWrapperView.makeCardUsingThemeColor(configItemView.coreWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
            
            configItemView.lowerView.makeCard(configItemView.lowerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
            
            configItemView.coreWrapperView.makeCard(configItemView.coreWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
            
            cell.mainWrapperView.addSubview(configItemView);
        }
        
        return cell;
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return heightToUse;
    }
    
    @objc func showPriceTypeDropdown(_ sender:UIButton)
    {
        //print("showPriceTypeDropdown");
        let dropDown = DropDown();
        dropDown.dataSource = self.priceTypeArray;
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
        //print("showUserDefinedQtyDropdown");
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
        //print(sender.tag);
        if(sender.imageView!.image == UIImage(named:"unchecked_checkbox_white"))
        {
            if let configItemView = self.view.viewWithTag(sender.tag) as? ConfigItemView
            {
                let textToUse = configItemView.lowerView.text!;
                for (_,v) in selectedCombination
                {
                    if(v == textToUse)
                    {
                        print("can't select sorry!");
                        self.view.showToastMsg(NSLocalizedString("You Can Select Only One Product.", comment: ""));
                        return;
                    }
                }
            }
            
            sender.setImage(UIImage(named: "checked_checkbox_white"), for: UIControl.State());
            
            self.selectedProductsIds.append(sender.tag);
            if let configItemView = self.view.viewWithTag(sender.tag) as? ConfigItemView
            {
                self.selectedCombination[sender.tag] = configItemView.lowerView.text!;
            }
        }
        else
        {
            sender.setImage(UIImage(named: "unchecked_checkbox_white"), for: UIControl.State());
            if let indexToRemove = selectedProductsIds.index(of: sender.tag) {
                self.selectedProductsIds.remove(at: indexToRemove);
                print(selectedCombination);
                self.selectedCombination[sender.tag] = "";
                print(selectedCombination);
            }
        }
        
        print(self.selectedProductsIds);
    }
    /*
     
     hashkey : FBxoRT8MjcpIyVxelRyf7p6FtmgpzK8z
     product_id : 2991
     vendor_id : 22
     
     Configurableproducts : {"links":{"related":"28442846"}}
     
     config_data : {"2846":[{"label":"SizeAug","attribute_id":"314","value_index":"297","is_percent":"0","pricing_value":" "}],"2844":[{"label":"SizeAug","attribute_id":"314","value_index":"296","is_percent":"0","pricing_value":" "}]}
     
   
     config_attribute : {"size_22":"314"}
     websites : ["1"]
     attribute_set : 128
     type=configurable
     configurable_attributes_data : [{"id":"null","label":"SizeAug","use_default":"null","position":"null","values":[{"attribute_id":"314","label":"S","value_index":"298","pricing_value":"32","is_percent":"0"},{"attribute_id":"314","label":"L","value_index":"297","pricing_value":"12","is_percent":"0"},{"attribute_id":"314","label":"M","value_index":"296","pricing_value":"43","is_percent":"0"}],"attribute_id":"314","attribute_code":"size_22","frontend_label":"SizeAug","store_label":"SizeAug","html_id":"configurableattribute_0"}]
     
     */
    
    /** @objc function to fetch related product data **/
    @objc func fetchRelatedProductListing()
    {
        var config_attribute = "{";
        for (k,v) in configSelected
        {
            config_attribute += "\""+v+"\":"+"\""+k+"\",";
        }
        if(config_attribute != "{")
        {
            config_attribute = config_attribute.substring(to: config_attribute.characters.index(before: config_attribute.endIndex));
        }
        config_attribute += "}";
        
        //print("fetchRelatedProductListing");
        //print(config_attribute)
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        baseURL = "vproductapi/vproducts/configurableGrid/";
        
        print(baseURL);
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&product_id="+product_id;
        postString += "&type="+selectedProductTypeId;
        postString += "&attribute_set="+selectedAttributeSetId;
        postString += "&page=\(page)"
        
        
        if(config_attribute != "")
        {
            postString += "&config_attribute="+config_attribute;
        }
        if !filter.isEmpty{
            postString += "&filter="+filter;
        }
        print(postString);
        
        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Please Wait...", comment: ""))
        sendRequest(url: baseURL, parameters: postString);
        
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        var jsonData = try! JSON(data: data!);
        print(jsonData)
        if(requestUrl == "vproductapi/vproducts/configurableGrid/")
        {
            
            Alert_File.removeLoadingIndicator(self);
            
            if(jsonData["data"]["success"].stringValue != "true")
            {
                loading = false;
                let title = "";
                let message = jsonData["data"]["message"].stringValue;
                Alert_File.showValidationError(self,title:title,message:message);
                return;
            }
            
            /** fetching product types **/
            
            for (_,val) in jsonData["data"]["sets"][0]
            {
                self.sets[val["value"].stringValue] = val["id"].stringValue;
            }
            
            
            /* to be used at edit time mainly */
            self.options = [String:[String:String]]();
            if(jsonData["data"]["options"] != nil)
            {
                for (_,val) in jsonData["data"]["options"]
                {
                    let key = val["value-index"].stringValue;
                    
                    var temp = [String:String]();
                    temp["value-id"] = val["value-id"].stringValue;
                    temp["product_super_attribute_id"] = val["product_super_attribute_id"].stringValue;
                    temp["price_type"] = val["price_type"].stringValue;
                    temp["option_price"] = val["option_price"].stringValue;
                    
                    self.options[key] = temp;
                }
            }
            print("options");
            print(self.options);
            print("options");
            for (val) in jsonData["data"]["products"].arrayValue
            {
                var tempArray = [String:String]();
                var tempID = 0;
                for(k,v) in val
                {
                    if(k == "product_id")
                    {
                        tempID = Int(v.stringValue)!;
                    }
                    if(k == "attribute")
                    {
                        var tempStrToUse = "";
                        var tempImpArray = [[String:String]]();
                        for (_,mainVal) in v
                        {
                            var inrTempArray = [String:String]();
                            inrTempArray["label"] = mainVal["attribute_label"].stringValue;
                            inrTempArray["attribute_id"] = mainVal["attribute_id"].stringValue;
                            inrTempArray["value_index"] = mainVal["value"].stringValue;
                            inrTempArray["is_percent"] = "0";
                            inrTempArray["pricing_value"] = " ";
                            tempImpArray.append(inrTempArray);
                            
                            tempStrToUse += mainVal["attribute_label"].stringValue+":"+mainVal["value_label"].stringValue+"\n";
                            
                            if( self.attributesPriceArrayHelper[mainVal["attribute_id"].stringValue] == nil )
                            {
                                self.attributesPriceArrayHelper[mainVal["attribute_id"].stringValue] = ["attribute_code":mainVal["attribute_code"].stringValue, "attribute_label":mainVal["attribute_label"].stringValue];
                                
                                self.attributesPriceArray[mainVal["attribute_id"].stringValue] = [String:[String:String]]();
                            }
                            print(self.attributesPriceArrayHelper);
                            
                            if( self.attributesPriceArray[mainVal["attribute_id"].stringValue]![mainVal["value"].stringValue] == nil )
                            {
                                if(self.options[mainVal["value"].stringValue] != nil)
                                {
                                    let optionsData = self.options[mainVal["value"].stringValue]!;
                                    var is_percent = "0";
                                    var pricing_value = "";
                                    
                                    if(self.isEditCase)
                                    {
                                        if(( optionsData["option_price"] ) != nil)
                                        {
                                            pricing_value =  optionsData["option_price"]!;
                                        }
                                        if(( optionsData["price_type"] ) != nil)
                                        {
                                            is_percent =  optionsData["price_type"]!;
                                        }
                                    }
                                    
                                    self.attributesPriceArray[mainVal["attribute_id"].stringValue]![mainVal["value"].stringValue] = ["value_label":mainVal["value_label"].stringValue, "is_percent":is_percent, "pricing_value":pricing_value];
                                }
                                else
                                {
                                    let is_percent = "0";
                                    let pricing_value = "";
                                    self.attributesPriceArray[mainVal["attribute_id"].stringValue]![mainVal["value"].stringValue] = ["value_label":mainVal["value_label"].stringValue, "is_percent":is_percent, "pricing_value":pricing_value];
                                }
                            }
                            print(self.attributesPriceArray);
                            
                        }
                        if let idToUSe = Int(val["product_id"].stringValue){
                            self.necesssaryInfoForUpdate[idToUSe] = tempImpArray;
                        }
                       
                        tempArray[k] = tempStrToUse;
                        if(k == "attribute")
                        {
                            print("-----attributes----")
                            print(tempStrToUse)
                        }
                        continue;
                    }
                    tempArray[k] = v.stringValue;
                    
                }
                self.products.append(tempArray);
            }
            
            //print("self.necesssaryInfoForUpdate");
            //print(self.necesssaryInfoForUpdate);
            
            //print("self.attributesPriceArrayHelper");
            //print(self.attributesPriceArrayHelper);
            
            //print("self.attributesPriceArray");
            //print(self.attributesPriceArray);
            
            self.groupedProductTableView.reloadData();
            
        }
        else if(requestUrl == "vendorapi/vproducts/update/")
        {
            print(jsonData);
            Alert_File.removeLoadingIndicator(self);
            
            if(jsonData["data"]["success"].stringValue != "true")
            {
                if(jsonData["data"]["message"][0].exists())
                {
                    if(jsonData["data"]["message"][0] == "لا يمكن حفظ المنتج.")
                    {
                        self.view.showToastMsg("The Product cannot be saved")
                        return;
                    }
                }
                let title = "";
                let message = jsonData["data"]["message"].stringValue;
                Alert_File.showValidationError(self,title:title,message:message);
                return;
            }
            self.view.showToastMsg(jsonData["data"]["message"].stringValue)
            Ced_CommonVendor.delay(2.0, closure: {
                let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "manageProductViewController") as! ManageProductViewController;
                
                self.navigationController?.setViewControllers([viewcontrollor], animated: true)
                
                return;
            })
            
            
        }
    }
    
    @objc func savePressed(_ sender:UIButton)
    {
        //print("savePressed");
        //print(selectedCombination);
        //print(selectedProductsIds);
        
        var config_data = "{";
        
        for (id) in selectedProductsIds
        {
            config_data += "\""+String(id)+"\":[";
            
            for (val) in necesssaryInfoForUpdate[id]!
            {
                print(val);
                
                print("attributesPriceArray");
                print(attributesPriceArray);
                print("attributesPriceArray");
                let tempDataToUse = attributesPriceArray[val["attribute_id"]!]![val["value_index"]!]!;
                let is_percent = tempDataToUse["is_percent"]!;
                config_data += "{";
                config_data += "\""+"label"+"\":\""+val["label"]!+"\",";
                config_data += "\""+"attribute_id"+"\":\""+val["attribute_id"]!+"\",";
                config_data += "\""+"value_index"+"\":\""+val["value_index"]!+"\",";
                config_data += "\""+"is_percent"+"\":\""+is_percent+"\",";
                config_data += "\""+"pricing_value"+"\":\""+val["pricing_value"]!+"\"";
                config_data += "},";
            }
            config_data = config_data.substring(to: config_data.characters.index(before: config_data.endIndex));
            config_data += "],";
        }
        if( config_data != "{")
        {
            config_data = config_data.substring(to: config_data.characters.index(before: config_data.endIndex));
        }
        config_data += "}";
        print("config_data");
        print(config_data);
        print("config_data");
        
        
        //apply filter section :: start
        var tempCounter = 0;
        var configurable_attributes_data = "[";
        for (key,val) in attributesPriceArray
        {
            var idToSet = "";
            
            let attribute_label = attributesPriceArrayHelper[key]!["attribute_label"]!;
            let attribute_code = attributesPriceArrayHelper[key]!["attribute_code"]!;
            
            configurable_attributes_data += "{";
            configurable_attributes_data += "\""+"label"+"\":"+"\""+attribute_label+"\",";
            configurable_attributes_data += "\""+"use_default"+"\":"+"\""+""+"\",";
            configurable_attributes_data += "\""+"position"+"\":"+"\""+""+"\",";
            
            configurable_attributes_data += "\""+"attribute_id"+"\":"+"\""+key+"\",";
            configurable_attributes_data += "\""+"attribute_code"+"\":"+"\""+attribute_code+"\",";
            configurable_attributes_data += "\""+"frontend_label"+"\":"+"\""+attribute_label+"\",";
            configurable_attributes_data += "\""+"store_label"+"\":"+"\""+attribute_label+"\",";
            configurable_attributes_data += "\""+"html_id"+"\":"+"\"configurableattribute_"+String(tempCounter)+"\",";
            tempCounter += 1;
            
            configurable_attributes_data += "\""+"values"+"\":"+"[";
            for (inrKey,inrVal) in val
            {
                let label = inrVal["value_label"]!;
                let is_percent = inrVal["is_percent"]!;
                let pricing_value = inrVal["pricing_value"]!;
                
                configurable_attributes_data += "{";
                
                if(isEditCase)
                {
                    
                    if(self.options[inrKey] != nil)
                    {
                        configurable_attributes_data += "\""+"can_edit_price"+"\":"+"\""+"true"+"\",";
                        configurable_attributes_data += "\""+"can_read_price"+"\":"+"\""+"true"+"\",";
                        let optionDataToUse =  self.options[inrKey]!;
                        idToSet = optionDataToUse["product_super_attribute_id"]!;
                        configurable_attributes_data += "\""+"product_super_attribute_id"+"\":"+"\""+idToSet+"\",";
                        configurable_attributes_data += "\""+"value-id"+"\":"+"\""+optionDataToUse["value-id"]!+"\",";
                    }
                    
                }
                
                configurable_attributes_data += "\""+"label"+"\":"+"\""+label+"\",";
                configurable_attributes_data += "\""+"attribute_id"+"\":"+"\""+key+"\",";
                configurable_attributes_data += "\""+"value_index"+"\":"+"\""+inrKey+"\",";
                configurable_attributes_data += "\""+"is_percent"+"\":"+"\""+is_percent+"\",";
                configurable_attributes_data += "\""+"pricing_value"+"\":"+"\""+pricing_value+"\"";
                configurable_attributes_data += "},";
            }
            if( configurable_attributes_data.characters.last! == "," )
            {
                configurable_attributes_data = configurable_attributes_data.substring(to: configurable_attributes_data.characters.index(before: configurable_attributes_data.endIndex));
            }
            configurable_attributes_data += "],";
            configurable_attributes_data += "\""+"id"+"\":"+"\""+idToSet+"\"";
            configurable_attributes_data += "},";
        }
        if( configurable_attributes_data.characters.last! == "," )
        {
            configurable_attributes_data = configurable_attributes_data.substring(to: configurable_attributes_data.characters.index(before: configurable_attributes_data.endIndex));
        }
        configurable_attributes_data += "]";
        //apply filter section :: end
        
        print("configurable_attributes_data");
        print(configurable_attributes_data);
        print("configurable_attributes_data");
        
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey = userData["hashKey"] as! String;
        
        baseURL = "vendorapi/vproducts/update/";
        
        print(baseURL);
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&product_id="+product_id;
        
        if(config_data != "{}")
        {
            postString += "&config_data="+config_data;
        }
        if let selectedWebsite = UserDefaults.standard.value(forKey: "selectedWebsite") as? String{
            if selectedWebsite != ""{
                postString += "&websites="+selectedWebsite;
            }
        }
        if(configurable_attributes_data != "[]")
        {
            postString += "&configurable_attributes_data="+configurable_attributes_data;
        }
        
        var config_attribute = "{";
        for (k,v) in configSelected
        {
            config_attribute += "\""+v+"\":"+"\""+k+"\",";
        }
        if(config_attribute != "{")
        {
            config_attribute = config_attribute.substring(to: config_attribute.characters.index(before: config_attribute.endIndex));
        }
        config_attribute += "}";
        postString += "&config_attribute="+config_attribute
        
       
        var configselectArr = ""
        for seleId in selectedProductsIds {
            configselectArr += "\(seleId)"
        }
        var configProducts = "{\"links\":{\"related\":\"\(configselectArr)\"}"
        configProducts += "}"
        if(configselectArr != "")
        {
            postString += "&Configurableproducts=\(configProducts)"
        }
        
        
//        postString = postString.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!;
        print("postString");
        print(postString);
        print("postString");
        /*if(self.selectedProductsIds.count > 0 && attributesPriceArray.count > 0)
         {
         self.view.showToastMsg("Config attributes are not selected")
         return;
         }*/
        
        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Please Wait...", comment: ""))
        sendRequest(url: baseURL, parameters: postString);
    }
    
    @objc func setPriceButtonPressed(_ sender:UIButton)
    {
        let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "priceSettingViewForConfigProduct") as! PriceSettingViewForConfigProduct;
        viewcontrollor.attributesPriceArrayHelper = self.attributesPriceArrayHelper;
        viewcontrollor.attributesPriceArray = self.attributesPriceArray;
        
        self.navigationController?.present(viewcontrollor, animated: true, completion: {});
        
    }
    
    @objc func filterPressed(_ sender:UIButton)
    {
        print("filterPressed");
        print("filterButtonPressed");
        /* background view */
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let longFilterPopupView = ConfigProductFilterView();
        self.addgesturesTohideView(self.view);
        longFilterPopupView.tag = 181;
        longFilterPopupView.backgroundColor = UIColor.black;
        longFilterPopupView.frame = CGRect(x: 25, y: 25, width: self.view.frame.width - 20, height: 250);
        longFilterPopupView.center.x = self.view.center.x;
        longFilterPopupView.center.y = self.view.center.y;
        
        longFilterPopupView.topLabel.text = NSLocalizedString("Seller App", comment: "");
        longFilterPopupView.label1.text = NSLocalizedString("Product Price", comment: "");
        longFilterPopupView.label2.text = NSLocalizedString("Product Name", comment: "");
        longFilterPopupView.label3.text = NSLocalizedString("Product SKU", comment: "");
        longFilterPopupView.label4.text = NSLocalizedString("Product ID", comment: "");
        longFilterPopupView.label5.text = NSLocalizedString("Status", comment: "");
//        longFilterPopupView.label6.text = NSLocalizedString("Attribute Set", comment: "");
//        longFilterPopupView.label4.isHidden = true
        longFilterPopupView.label6.isHidden = true
        longFilterPopupView.value5.setTitle(value5, for: UIControl.State());
        longFilterPopupView.value5.addTarget(self, action: #selector(ConfigProductSelectionController.showProductInventoryDropdown(_:)), for: UIControl.Event.touchUpInside);
        
        longFilterPopupView.value6.setTitle(value6, for: UIControl.State());
        longFilterPopupView.value6.addTarget(self, action: #selector(ConfigProductSelectionController.showAttributeSetDropdown(_:)), for: UIControl.Event.touchUpInside);
        
        longFilterPopupView.value11.text = value11;
        longFilterPopupView.value12.text = value12;
        longFilterPopupView.value2.text = value2;
        longFilterPopupView.value3.text = value3;
        longFilterPopupView.value4.text = value4;
//        longFilterPopupView.value4.isHidden = true
        longFilterPopupView.value6.isHidden = true
        
        longFilterPopupView.leftButton.setTitle(NSLocalizedString("Filter", comment: ""), for: UIControl.State());
        longFilterPopupView.leftButton.addTarget(self, action: #selector(ConfigProductSelectionController.applyFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        
        longFilterPopupView.rightButton.setTitle(NSLocalizedString("Reset", comment: ""), for: UIControl.State());
        longFilterPopupView.rightButton.addTarget(self, action: #selector(ConfigProductSelectionController.resetFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        
        bgCView.addSubview(longFilterPopupView)
        self.view.addSubview(bgCView);
    }
    
    
    @objc func showProductInventoryDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = Array(productInventory.keys)
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
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(ConfigProductSelectionController.hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ConfigProductSelectionController.hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ConfigProductSelectionController.hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ConfigProductSelectionController.hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ConfigProductSelectionController.hideView(_:)));
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
        if let innerView = self.view.viewWithTag(181) as? ConfigProductFilterView {
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
        
        if let view = self.view.viewWithTag(181) as? ConfigProductFilterView
        {
            var value51 = ""
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
                value51 = productInventory[view.value5.titleLabel!.text!] ?? ""
                value5 = view.value5.titleLabel!.text!
            }
            if(view.value6.titleLabel!.text != nil){
                value6 = view.value6.titleLabel!.text!
            }
            
            filter = "";
            filter += "{";
            filter += "\""+"price"+"\":{\"from\":\""+value11+"\",\"to\":\""+value12+"\"},";
            filter += "\""+"name"+"\":\""+value2+"\",";
            filter += "\""+"sku"+"\":\""+value3+"\",";
            filter += "\""+"entity_id"+"\":\""+value4+"\",";
//            filter += "\""+"status"+"\":\""+value51+"\",";
            filter += "\""+"status"+"\":\""+value51+"\"}";
            
            print(filter);
            self.view.viewWithTag(151)?.removeFromSuperview();
            products = [[String:String]]();
            page = 1
            loading = true
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
        value2 = ""
        value3  = ""
        value4 = ""
        value5  = ""
        value6 = ""
        filter = String();
        products = [[String:String]]();
        page = 1
        loading = true
        groupedProductTableView.reloadData()
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.fetchRelatedProductListing();
    }
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
                
                self.page += 1;
                self.fetchRelatedProductListing();
                
            }
            
        }
        
    }
    
}
