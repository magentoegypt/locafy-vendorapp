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

class ShippingMethodsController: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate {
    
    
    @IBOutlet weak var shippingMethodTableView: UITableView!
    
    @IBOutlet weak var topWrapperView: UIView!
    @IBOutlet weak var leftButton: UIButton!
    @IBOutlet weak var rightButton: UIButton!
    @IBOutlet weak var rightButtonWidth: NSLayoutConstraint!
    
    
    let screenSize: CGRect = UIScreen.main.bounds;
    
    var baseURL = String();
    
    var valuesArray = [JSON]();
    var shippingMethodsFields = [String: [[String:Any]]]();
    
    var heightToUse = CGFloat(0);
    let dropDownSectionHeight : CGFloat = 70;
    let checkboxHeight : CGFloat = 30;
    let padding : CGFloat = 5;
    
    var dropDownValuesArray = [String]();
    var dropDownInfoArray = [String:String]();
    
    var activeShippingMethodsValueArray = [String]();
    var shippingMethodsArray = [String:String]();
    
    var upsActive = "";
    var selectedSelectArray = [Int:[String:String]]()
    var makeFieldsArray = [Int:[String:Any]]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let tlabel = UILabel(frame: CGRect(x: 0, y: 0, width: 200, height: 40));
        self.title = NSLocalizedString("Shipping Methods", comment: "")
        tlabel.text = self.title
        tlabel.textColor = UIColor.white
        tlabel.font = UIFont(name: "Roboto-Bold", size: 21.0)
        tlabel.backgroundColor = UIColor.clear
        tlabel.adjustsFontSizeToFitWidth = true
        self.navigationItem.titleView = tlabel
        
        // Do any additional setup after loading the view.
        
        self.leftButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 17)
        self.rightButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 17)
        self.leftButton.setTitle(NSLocalizedString("Shipping Methods", comment: ""), for: UIControl.State());
        self.rightButton.setTitle(NSLocalizedString("Save".localized, comment: ""), for: UIControl.State());
        self.rightButton.addTarget(self, action: #selector(ShippingMethodsController.saveShippingMethods(_:)), for: UIControl.Event.touchUpInside);
        self.rightButtonWidth.constant = screenSize.width/3;
        
        ced_navigationBarController().addNavButton(self,str:"no")
        
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        let colorGreen = Ced_CommonVendor.UIColorFromRGB("#002f63");
        rightButton.backgroundColor = colorGreen
        topWrapperView.backgroundColor = color
        
        
        self.shippingMethodTableView.dataSource = self;
        self.shippingMethodTableView.delegate = self;
        
        self.fetchShippingMethodsInfo();
        
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
        if(self.shippingMethodsFields.count == 0)
        {
            return 0;
        }
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = shippingMethodTableView.dequeueReusableCell(withIdentifier: "longSingleCell", for: indexPath) as! LongSingleTableViewCell;
        cell.mainWrapperView.backgroundColor = UIColor.white;
        
        /** Top View **/
        /*let profileTopView = ProfileTopView();
         profileTopView.autoresizingMask = [UIViewAutoresizing.FlexibleLeftMargin,UIViewAutoresizing.FlexibleRightMargin];
         profileTopView.frame = CGRectMake(0, heightToUse, screenSize.width-10,CGFloat(90));
         profileTopView.center.x = self.view.center.x;
         heightToUse += CGFloat(90);
         
         profileTopView.topButton.addTarget(self, action: Selector("saveShippingMethods:"), forControlEvents: UIControlEvents.TouchUpInside);
         cell.mainWrapperView.addSubview(profileTopView);
         
         profileTopView.secondLeftLabel.text = "UPS";
         profileTopView.secondRightButton.hidden = true;*/
        
        
        
        
        var counter = 1000;
        for(key,value) in shippingMethodsFields
        {
            //*** top label **//
            heightToUse += CGFloat(10);
            let ts_label_view = TS_Label_View();
            ts_label_view.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            ts_label_view.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-10,height: CGFloat(40));
            heightToUse += CGFloat(50);
            ts_label_view.center.x = cell.mainWrapperView.center.x;
            if let keyArray = key.components(separatedBy: "#") as? [String]
            {
                ts_label_view.entityNameLabel.text = keyArray[1];
            }
            
            
            //design code
            let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
            let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
            ts_label_view.backgroundColor = color
            ts_label_view.entityNameLabel.font=UIFont(name: "Roboto-Regular",size: 15)
            ts_label_view.entityNameLabel.textAlignment = NSTextAlignment.center;
            ts_label_view.entityNameLabel.textColor = UIColor.white;
            ts_label_view.entityNameLabel.backgroundColor = color;
            ts_label_view.layer.borderColor = color.cgColor
            ts_label_view.layer.borderWidth = 1.0
            ts_label_view.layer.cornerRadius = 5
            
            
            heightToUse += CGFloat(5);
            
            cell.mainWrapperView.addSubview(ts_label_view);
            var x = 0;
            for val in value{
                
                if let type = val["type"] as? JSON{
                    if(type.stringValue == "select")
                    {
                        let label_DropDown_Combo_View = Label_DropDown_Combo_View();
                        label_DropDown_Combo_View.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                        label_DropDown_Combo_View.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-30,height: dropDownSectionHeight);
                        label_DropDown_Combo_View.center.x = cell.mainWrapperView.center.x;
                        heightToUse += dropDownSectionHeight;
                        heightToUse += padding;
                        label_DropDown_Combo_View.entityNameLabel.font=UIFont(name: "Roboto-Regular",size: 15)
                        if let label = val["label"] as? JSON{
                            label_DropDown_Combo_View.entityNameLabel.text = label.stringValue;
                        }
                        
                        label_DropDown_Combo_View.dropDownButton.titleLabel?.font=UIFont(name: "Roboto-Regular",size: 15)
                        //label_DropDown_Combo_View.dropDownButton.setTitle( upsActive, for: UIControlState());
                        label_DropDown_Combo_View.dropDownButton.addTarget(self, action: #selector(ShippingMethodsController.showRequiredDropdown(_:)), for: UIControl.Event.touchUpInside);
                        label_DropDown_Combo_View.dropDownButton.tag = counter;
                        if let values = val["values"] as? JSON{
                            var valuesArray = [String:String]();
                            for valEach in values.arrayValue
                            {
                                if let value = val["value"] as? JSON{
                                    if(value.stringValue == valEach["value"].stringValue)
                                    {
                                        label_DropDown_Combo_View.dropDownButton.setTitle(valEach["label"].stringValue, for: .normal);
                                    }
                                }
                                valuesArray[valEach["label"].stringValue] = valEach["value"].stringValue;
                            }
                            selectedSelectArray[counter] = valuesArray;
                        }
                        shippingMethodsFields[key]![x]["field"] = label_DropDown_Combo_View.dropDownButton;
                        //design
                        label_DropDown_Combo_View.makeCard(label_DropDown_Combo_View, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                        
                        cell.mainWrapperView.addSubview(label_DropDown_Combo_View);
                        
                        
                        
                        /*for (_,inrVal) in self.valuesArray[counter]
                        {
                            if( val["value"] == inrVal["value"].stringValue )
                            {
                                upsActive = inrVal["label"].stringValue;
                            }
                            self.dropDownInfoArray[inrVal["label"].stringValue] = inrVal["value"].stringValue;
                            self.dropDownValuesArray.append(inrVal["label"].stringValue);
                        }
                        
                        let label_DropDown_Combo_View = Label_DropDown_Combo_View();
                        label_DropDown_Combo_View.autoresizingMask = [UIViewAutoresizing.flexibleLeftMargin,UIViewAutoresizing.flexibleRightMargin];
                        label_DropDown_Combo_View.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-30,height: dropDownSectionHeight);
                        label_DropDown_Combo_View.center.x = cell.mainWrapperView.center.x;
                        heightToUse += dropDownSectionHeight;
                        heightToUse += padding;
                        label_DropDown_Combo_View.entityNameLabel.font=UIFont(name: "Roboto-Regular",size: 15)
                        label_DropDown_Combo_View.entityNameLabel.text = val["label"];
                        label_DropDown_Combo_View.dropDownButton.titleLabel?.font=UIFont(name: "Roboto-Regular",size: 15)
                        label_DropDown_Combo_View.dropDownButton.setTitle( upsActive, for: UIControlState());
                        label_DropDown_Combo_View.dropDownButton.addTarget(self, action: #selector(ShippingMethodsController.showRequiredDropdown(_:)), for: UIControlEvents.touchUpInside);
                        label_DropDown_Combo_View.dropDownButton.tag = 10;
                        
                        //design
                        label_DropDown_Combo_View.makeCard(label_DropDown_Combo_View, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                        
                        cell.mainWrapperView.addSubview(label_DropDown_Combo_View);*/
                    }
                    /*else if(type.stringValue == "multiselect")
                    {
                        let topPoint = heightToUse;
                        var viewHeight = CGFloat(0);
                        let valueString = val["value"];
                        let valueArray = valueString!.characters.split{$0 == ","}.map(String.init);
                        
                        print(valueArray);
                        print(type(of: valueArray));
                        
                        self.activeShippingMethodsValueArray = valueArray;
                        
                        let view = UIView();
                        view.backgroundColor = UIColor.white;
                        print(self.valuesArray[counter]);
                        
                        let labelView = LabelView();
                        labelView.autoresizingMask = [UIViewAutoresizing.flexibleLeftMargin,UIViewAutoresizing.flexibleRightMargin];
                        labelView.frame = CGRect(x: 0, y: viewHeight, width: screenSize.width-30,height: checkboxHeight);
                        labelView.center.x = self.view.center.x;
                        heightToUse += checkboxHeight;
                        heightToUse += padding;
                        
                        viewHeight += (checkboxHeight+padding);
                        labelView.label.font=UIFont(name: "Roboto-Regular", size: 15)
                        labelView.label.text = " "+(val["label"]?.uppercased())!;
                        
                        //design
                        labelView.label.textColor = UIColor.white;
                        labelView.label.backgroundColor = color;
                        labelView.label.textColor = UIColor.white;
                        
                        
                        view.addSubview(labelView);
                        
                        for (_,val) in self.valuesArray[counter]
                        {
                            self.shippingMethodsArray[val["label"].stringValue] = val["value"].stringValue;
                            
                            let subCategoryView = SubCategoryView();
                            subCategoryView.autoresizingMask = [UIViewAutoresizing.flexibleLeftMargin,UIViewAutoresizing.flexibleRightMargin];
                            subCategoryView.frame = CGRect(x: 0, y: viewHeight, width: screenSize.width-30,height: checkboxHeight);
                            subCategoryView.center.x = self.view.center.x;
                            heightToUse += checkboxHeight;
                            heightToUse += padding;
                            
                            viewHeight += (checkboxHeight+padding);
                            
                            subCategoryView.childFilterButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 15)
                            subCategoryView.childFilterButton.setTitle(val["label"].stringValue, for: UIControlState());
                            
                            if valueArray.contains(val["value"].stringValue) {
                                subCategoryView.childFilterButton.setImage(UIImage(named: "checked_checkbox"), for: UIControlState());
                            }
                            else
                            {
                                subCategoryView.childFilterButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControlState());
                            }
                            
                            
                            subCategoryView.childFilterButton.addTarget(self, action: #selector(self.checkboxButtonClicked(_:)), for: UIControlEvents.touchUpInside);
                            
                            view.addSubview(subCategoryView);
                            
                        }
                        
                        view.frame = CGRect(x: 15, y: topPoint+10, width: screenSize.width-30, height: viewHeight);
                        
                        view.makeCard(view, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                        
                        //view.layer.borderWidth = 1;
                        //view.layer.borderColor = UIColor.grayColor().CGColor;
                        cell.mainWrapperView.addSubview(view);
                    }*/
                    counter += 1;
                }
                x += 1;
            }
            
        }
        
        return cell;
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return heightToUse+20;
    }
    
    @objc func checkboxButtonClicked(_ sender:UIButton)
    {
        let key = self.shippingMethodsArray[(sender.titleLabel?.text)!]!;
        
        if(sender.imageView!.image == UIImage(named:"unchecked_checkbox"))
        {
            sender.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
            self.activeShippingMethodsValueArray.append(key);
        }
        else
        {
            sender.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
            if let indexToRemove = activeShippingMethodsValueArray.index(of: key) {
                self.activeShippingMethodsValueArray.remove(at: indexToRemove);
            }
        }
        
        print(self.activeShippingMethodsValueArray);
    }
    
    
    @objc func showRequiredDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        let tag = sender.tag;
        let dropdownArray = selectedSelectArray[tag];
        let dataSource = Array(dropdownArray!.keys);
        
        dropDown.dataSource = dataSource;
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
    
    @objc func fetchShippingMethodsInfo()
    {
        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Please Wait...", comment: ""))
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendor_id = userData["vendorId"] as! String
        let hashkey    = userData["hashKey"] as! String
        
        //baseURL = "vupsshippingapi/index/shippingMethods/";
        baseURL = "vmultishippingApi/setting/shippingMethods/vendor_id/\(vendor_id)/hashkey/\(hashkey)"
        print(baseURL);
        
        let postString = ""//"vendor_id="+vendor_id+"&hashkey="+hashkey;
        self.getRequest(url: baseURL)
        
        
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendor_id = userData["vendorId"] as! String
        let hashkey    = userData["hashKey"] as! String
        
        //baseURL = "vupsshippingapi/index/shippingMethods/";
        baseURL = "vmultishippingApi/setting/shippingMethods/vendor_id/\(vendor_id)/hashkey/\(hashkey)"
        let baseURL2 = "vmultishippingApi/setting/saveShippingMethod/hashkey/\(hashkey)"
        if(requestUrl==baseURL)
        {
            guard let jsonData = try? JSON(data: data!) else {return}
            print(jsonData)
            Alert_File.removeLoadingIndicator(self);
            for(mainkey,mainval) in jsonData["data"]
            {
                var methodArray = [[String:Any]]()
                for index in mainval.arrayValue{
                    var checkArray = [String:Any]()
                    for(key,val) in index
                    {
                        checkArray[key] = val;
                    }
                    methodArray.append(checkArray);
                    
                }
                
                /*for (_,UPS_Fileds) in mainval
                {
                    var tempArray = [String:String]();
                    for (key,val) in UPS_Fileds
                    {
                        print(key);
                        print(val);
                        
                        
                        if(key == "values")
                        {
                            self.valuesArray.append(val);
                        }
                        else
                        {
                            tempArray[key] = val.stringValue;
                        }
                    }
                    methodArray.append(tempArray);
                }*/
                shippingMethodsFields[mainkey] = methodArray;
            }
            
            
            print("*****");
            
            print(self.shippingMethodsFields);
            
            self.shippingMethodTableView.reloadData();
        }
            
        else if(requestUrl == baseURL2)
        {
            guard let jsonData = try? JSON(data: data!) else {return}
            
            if(jsonData["data"]["success"].stringValue != "true")
            {
                let title = NSLocalizedString("Error", comment: "");
                let message = NSLocalizedString("Unfortunately Data Not Saved", comment: "");
                Alert_File.showValidationError(self,title:title,message:message);
                return;
            }
            
            let title = NSLocalizedString("Success", comment: "");
            let message = jsonData["data"]["message"].stringValue
            Alert_File.showValidationError(self,title:title,message:message);
            return;
            
        }
    }
    
    @objc func saveShippingMethods(_ sender:UIButton)
    {
        var shipping_method_data = "[";
        for(key,val) in shippingMethodsFields{
            var keysVal = ""
            if let keyArray = key.components(separatedBy: "#") as? [String]
            {
                keysVal = keyArray[0]
                
            }
            shipping_method_data += "{\"\(keysVal)\":{"
            for index in val{
                if let type = index["type"] as? JSON{
                    if(type.stringValue == "select")
                    {
                        if let field = index["field"] as? UIButton{
                            if(field.currentTitle != "")
                            {
                                print(field.tag);
                                print(field.currentTitle);
                                if let postField = index["field_to_post"] as? JSON{
                                    let datatosend = selectedSelectArray[field.tag]![field.currentTitle!]!;
                                    shipping_method_data += "\"\(postField.stringValue)\":\"\(datatosend)\","
                                }
                            }
                        }
                    }
                }
            }
            if(shipping_method_data != "[{\"\(keysVal)\":{")
            {
                shipping_method_data = shipping_method_data.substring(to: shipping_method_data.characters.index(before: shipping_method_data.endIndex));
                shipping_method_data += "}},";
            }
            
        }
        shipping_method_data = shipping_method_data.substring(to: shipping_method_data.characters.index(before: shipping_method_data.endIndex));
        shipping_method_data += "]";
        /*for val in activeShippingMethodsValueArray
        {
            shipping_method_data += "\""+val+"\",";
        }
        //shipping_method_data += "\""+"groups[delhivery][active]"+"\",";
        shipping_method_data = shipping_method_data.substring(to: shipping_method_data.characters.index(before: shipping_method_data.endIndex));
        if(activeShippingMethodsValueArray.count > 0)
        {
            shipping_method_data += "]},";
        }
        else
        {
            shipping_method_data = "";
        }
        
        if let dataIn = dropDownInfoArray[upsActive] {
            shipping_method_data += "{\"active\":";
            shipping_method_data += dataIn
            shipping_method_data += "}]";
        }
       */
        
        
        print(shipping_method_data);
        
        shipping_method_data = shipping_method_data.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!;
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        
        //baseURL = "vupsshippingapi/index/saveShippingMethods/";
        baseURL = "vstorepickupapi/setting/saveShippingMethod/hashkey/\(hashkey)"
        print(baseURL);
        
        
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&shipping_method_data="+shipping_method_data;
        print(postString);
        
        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Please Wait...", comment: ""))
        sendRequest(url: baseURL, parameters: postString);
        
    }
    
    
}
