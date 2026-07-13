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
import MapKit

class ShippingSettingController: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate, CLLocationManagerDelegate {
    
    @IBOutlet weak var shippingSettingTableView: UITableView!
    var locationCheck = false;
    @IBOutlet weak var topWrapperView: UIView!
    @IBOutlet weak var leftButton : UIButton!
    @IBOutlet weak var rightButton: UIButton!
    @IBOutlet weak var rightButtonWidth: NSLayoutConstraint!
    let screenSize: CGRect = UIScreen.main.bounds;
    var baseURL = String();
    var valuesArray = [JSON]();
    var shippingAddressFields = [[String:String]]();
    var heightToUse : CGFloat = 0;
    var fixedHeight : CGFloat = 70;
    var padding : CGFloat = 10;
    var dataToPost = [[String:String]]();
    var dropDownArrays = [Int:[String:String]]();//var to store all values of select type
    var stateDropDownIsVisible = true;// to check dropdown is visible or textfield
    var stateSelectionViewTag = -1;//tag of the state view
    var textFieldValueOfState = "";//textfield value of state
    var dropdownValueOfState = "";//dropdown value of state
    var statesArray = [String:String]();
    var countryArray = [String:String]();
    var previousCountry = "";
    
    var countryDropDown = [String]();
    var tagsArray = [Int:String]();
    var dropdownValues = [Int:String]();
    var helperArray = [String:[Int]]();
    var idsCombo = [String:[Int]]();
    var locationManager: CLLocationManager = CLLocationManager()
    var warehouseAddressFieldTag = 0
    var cityFieldTag = 0
    var zipFieldTag = 0
    var clgeocoder: CLGeocoder = CLGeocoder()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        shippingSettingTableView.backgroundColor = DynamicColor.ViewBackgroundColor
        leftButton.backgroundColor = DynamicColor.ViewBackgroundColor
        leftButton.setTitleColor(DynamicColor.labelColor, for: .normal)
        
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        self.title = "Shipping Settings".localized
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 21)!]
        ced_navigationBarController().addNavButton(self,str:"no")
        self.leftButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 17)
        self.rightButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 17)
        self.leftButton.setTitle("Shipping Settings".localized, for: UIControl.State());
        self.rightButton.setTitle("Save".localized, for: UIControl.State());
        self.rightButton.addTarget(self, action: #selector(ShippingSettingController.saveShippingAddress(_:)), for: UIControl.Event.touchUpInside);
        self.rightButtonWidth.constant = screenSize.width/3;
        topWrapperView.makeCard(topWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        self.shippingSettingTableView.dataSource = self;
        self.shippingSettingTableView.delegate = self;
        
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        let colorGreen = Ced_CommonVendor.UIColorFromRGB("#002f63");
        rightButton.backgroundColor = colorGreen
        topWrapperView.backgroundColor = color
        self.fetchShippingAddressInfo();
        locationManager.delegate = self
        if CLLocationManager.locationServicesEnabled() {
            
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            
            if(self.locationManager.responds(to: #selector(CLLocationManager.requestAlwaysAuthorization)))
            {
                locationManager.requestAlwaysAuthorization()
            }
            else {
                
                locationManager.requestLocation()
                //startUpdatingLocation()
            }
            
        }
        
    }
    
    // MARK: - CLLocationManagerDelegate
    
    //Check authorization
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        if status == .authorizedWhenInUse || status == .authorizedAlways {
            
            //locationManager.requestLocation()
            
            startUpdatingLocation()
            
        }
    }
    
    
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        //Get last updated location(current)
        
        if let location = locations.last as? CLLocation
        {
            clgeocoder.reverseGeocodeLocation(location, completionHandler: {(placemarks, error) -> Void in
                if error == nil {
                    if let placemarksArray = placemarks{
                        if(placemarksArray.count>0)
                        {
                            let placemark = placemarks![0]
                            if(self.locationCheck == false)
                            {
                                self.locationCheck = true;
                                if(self.zipFieldTag != 0)
                                {
                                    if let field = self.view.viewWithTag(self.zipFieldTag) as? UITextView
                                    {
                                        field.text = placemark.postalCode
                                        
                                    }
                                }
                                if(self.warehouseAddressFieldTag != 0)
                                {
                                    if let field = self.view.viewWithTag(self.warehouseAddressFieldTag) as? UITextView
                                    {
                                        var address = "";
                                        address += placemark.thoroughfare ?? ""
                                        address += placemark.subThoroughfare ?? ""
                                        address += placemark.subLocality ?? ""
                                        field.text = address;
                                    }
                                }
                                if(self.cityFieldTag != 0)
                                {
                                    if let field = self.view.viewWithTag(self.cityFieldTag) as? UITextView
                                    {
                                        
                                        field.text = placemark.subAdministrativeArea;
                                    }
                                }
                            }
                            
                            
                        }
                    }
                    
                }
            })
        
            
        }
    
    }
    
    //Updated location
    
    
    
    func startUpdatingLocation() {
        
        locationManager.startUpdatingLocation()
        
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
        if(self.shippingAddressFields.count == 0)
        {
            return 0;
        }
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = shippingSettingTableView.dequeueReusableCell(withIdentifier: "longSingleCell", for: indexPath) as! LongSingleTableViewCell;
        cell.mainWrapperView.backgroundColor = DynamicColor.ViewBackgroundColor;
    
        //*** top label **//
        heightToUse += CGFloat(10);
        let ts_label_view = TS_Label_View();
        ts_label_view.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        ts_label_view.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-10,height: CGFloat(40));
        heightToUse += CGFloat(50);
        ts_label_view.center.x = cell.mainWrapperView.center.x;
        ts_label_view.entityNameLabel.font=UIFont(name: "Roboto-Regular", size: 17)
        ts_label_view.entityNameLabel.text = "Original Address Details".localized.uppercased();
        
        //design code
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        ts_label_view.backgroundColor = color
        ts_label_view.entityNameLabel.textAlignment = NSTextAlignment.center;
        ts_label_view.entityNameLabel.textColor = UIColor.white;
        ts_label_view.entityNameLabel.backgroundColor = DynamicColor.secondaryColor;
        ts_label_view.layer.borderColor = color.cgColor
        ts_label_view.layer.borderWidth = 1.0
        ts_label_view.layer.cornerRadius = 5
        
        //PopupLookImprovement.designLabel(ts_label_view.entityNameLabel);
        heightToUse += CGFloat(5);
        
        cell.mainWrapperView.addSubview(ts_label_view);
        
        var counter = 1;
        for val in shippingAddressFields
        {
            if(val["label"] == "")
            {
                print("DEKHO");
                print(val);
                print(val["value"]!);
                self.textFieldValueOfState = val["value"]!;
                print(self.textFieldValueOfState);
                //counter++;
                print("GGGG")
                continue;
            }
            if let key1 = val["tag"] {
                self.dataToPost.append([key1:""]);
            }
            
            if(val["tag"] == "region_id" || val["tag"] == "region")
            {
                let label_TextView_Combo_View = NewStatePickerView();
                label_TextView_Combo_View.tag = counter;
                label_TextView_Combo_View.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                label_TextView_Combo_View.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-20,height: fixedHeight);
                heightToUse += fixedHeight;
                heightToUse += padding;
                label_TextView_Combo_View.center.x = cell.mainWrapperView.center.x;
                label_TextView_Combo_View.entityNameLabel.font=UIFont(name: "Roboto-Regular", size: 15)
                label_TextView_Combo_View.entityNameLabel.text = val["label"];
                label_TextView_Combo_View.dropDownButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 15)
                label_TextView_Combo_View.dropDownButton.tag = counter;
                label_TextView_Combo_View.textValue.tag = counter;
                self.stateSelectionViewTag = counter;
                
                label_TextView_Combo_View.dropDownButton.addTarget(self, action: #selector(ShippingSettingController.showStatesDropdown(_:)), for: UIControl.Event.touchUpInside);
                
                
                if(val["tag"] == "region_id")
                {
                    if(val["region_id"] != "")
                    {
                        self.stateDropDownIsVisible = true;
                        label_TextView_Combo_View.textValue.isHidden = true;
                        label_TextView_Combo_View.dropDownButton.isHidden = false;
                        label_TextView_Combo_View.dropDownButton.setTitle(val["value"], for: UIControl.State());
                        self.dropdownValueOfState = val["value"]!;
                    }
                    else
                    {
                        print("self.textFieldValueOfState");
                        print(self.textFieldValueOfState);
                        self.stateDropDownIsVisible = false;
                        label_TextView_Combo_View.dropDownButton.isHidden = true;
                        label_TextView_Combo_View.textValue.isHidden = false;
                        label_TextView_Combo_View.textValue.text = val["value"];
                        if(self.textFieldValueOfState != "")
                        {
                            label_TextView_Combo_View.textValue.text = self.textFieldValueOfState;
                        }
                    }
                    
                }
                else if(val["tag"] == "region")
                {
                    self.stateDropDownIsVisible = false;
                    label_TextView_Combo_View.dropDownButton.isHidden = true;
                    label_TextView_Combo_View.textValue.isHidden = false;
                    label_TextView_Combo_View.textValue.text = val["value"];
                    if(self.textFieldValueOfState != "")
                    {
                        label_TextView_Combo_View.textValue.text = self.textFieldValueOfState;
                    }
                }
                
                label_TextView_Combo_View.makeCard(label_TextView_Combo_View, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
                
                cell.mainWrapperView.addSubview(label_TextView_Combo_View);
                counter += 1;
                continue;
            }
            
            
            if(val["type"] == "select")
            {
                var tempDropDown = [String:String]();
                var initialVal = "";
                for (_,inrVal) in self.valuesArray[counter-1]
                {
                    if( val["value"] == inrVal["value"].stringValue )
                    {
                        initialVal = inrVal["label"].stringValue;
                    }
                    tempDropDown[inrVal["label"].stringValue] = inrVal["value"].stringValue;
                    self.countryDropDown.append(inrVal["label"].stringValue);
                }
                self.dropDownArrays[counter-1] = tempDropDown;
                
                if(val["tag"] == "country_id")
                {
                    self.countryArray = tempDropDown;
                    self.previousCountry = val["value"]!;
                }
                
                let label_DropDown_Combo_View = Label_DropDown_Combo_View();
                label_DropDown_Combo_View.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                label_DropDown_Combo_View.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-20,height: fixedHeight);
                heightToUse += fixedHeight;
                heightToUse += padding;
                label_DropDown_Combo_View.center.x = cell.mainWrapperView.center.x;
                label_DropDown_Combo_View.entityNameLabel.font=UIFont(name: "Roboto-Regular", size: 15)
                label_DropDown_Combo_View.entityNameLabel.text = val["label"]?.uppercased();
                if(val["value"] != nil)
                {
                    print("Value of country")
                    print(val["value"])
                    if let code = val["value"] {
                        let country =  findcountryForCode(code,countries: countryArray)
                        label_DropDown_Combo_View.dropDownButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 15)
                        label_DropDown_Combo_View.dropDownButton.setTitle(country, for: UIControl.State());
                    }
                    
                }
                else
                {
                    print("Intial Value\(initialVal)")
                    label_DropDown_Combo_View.dropDownButton.setTitle(initialVal, for: UIControl.State());
                }
                
                label_DropDown_Combo_View.dropDownButton.addTarget(self, action: #selector(ShippingSettingController.showRequiredDropdown(_:)), for: UIControl.Event.touchUpInside);
                label_DropDown_Combo_View.dropDownButton.tag = counter;
                print(val["label"])
                print("taggg of this value \(counter)")
                label_DropDown_Combo_View.makeCard(label_DropDown_Combo_View, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                
                cell.mainWrapperView.addSubview(label_DropDown_Combo_View);
                
            }
            else if(val["type"] == "text")
            {
                let label_TextView_Combo_View = Label_TextView_Combo_View();
                label_TextView_Combo_View.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                label_TextView_Combo_View.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-20,height: fixedHeight);
                heightToUse += fixedHeight;
                heightToUse += padding;
                label_TextView_Combo_View.center.x = cell.mainWrapperView.center.x;
                label_TextView_Combo_View.entityNameLabel.font=UIFont(name: "Roboto-Regular", size: 15)
                label_TextView_Combo_View.entityNameLabel.text = val["label"];
                if(val["tag"]?.lowercased() == "phoneno" || val["label"]?.lowercased() == "postcode")
                {
                    label_TextView_Combo_View.entityValueTxtField.keyboardType = .numberPad;
                }
                if(val["tag"]?.lowercased() == "warehouseaddress")
                {
                    warehouseAddressFieldTag = counter;
                }
                else if(val["tag"]?.lowercased() == "city")
                {
                    cityFieldTag = counter;
                }
                else if(val["tag"]?.lowercased() == "postcode")
                {
                    zipFieldTag = counter;
                }
                label_TextView_Combo_View.entityValueTxtField.text = val["value"];
                label_TextView_Combo_View.entityValueTxtField.tag = counter;
                
                print(val["label"])
                print("taggg of this value \(counter)")
                
                label_TextView_Combo_View.makeCard(label_TextView_Combo_View, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
                
                cell.mainWrapperView.addSubview(label_TextView_Combo_View);
            }
            counter += 1;
        }
        
        
        if let newStatePickerView = self.view.viewWithTag(self.stateSelectionViewTag) as? NewStatePickerView
        {
            if(self.stateDropDownIsVisible == false)
            {
                newStatePickerView.dropDownButton.isHidden = true;
                newStatePickerView.textValue.isHidden = false;
                newStatePickerView.textValue.text = "";
                if(self.textFieldValueOfState != "")
                {
                    newStatePickerView.textValue.text = self.textFieldValueOfState;
                }
            }
            else
            {
                newStatePickerView.textValue.isHidden = true;
                newStatePickerView.dropDownButton.isHidden = false;
                if(self.dropdownValueOfState != "")
                {
                    newStatePickerView.dropDownButton.setTitle(self.dropdownValueOfState, for: UIControl.State());
                    print("previousCountry");
                    print(self.previousCountry);
                    
                    if let country_code = self.countryArray[self.previousCountry] {
                        self.fetchStatesInsideCountry(country_code);
                    }
                    
                    if(self.textFieldValueOfState != "")
                    {
                        newStatePickerView.textValue.text = self.textFieldValueOfState;
                        
                    }
                }
                else
                {
                    newStatePickerView.dropDownButton.setTitle("", for: UIControl.State());
                }
                
            }
            
        }
        
        return cell;
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return heightToUse;
    }
    
    
    @objc func showRequiredDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        let tempRequiredArray = self.dropDownArrays[sender.tag-1]!;
        //        /print(tempRequiredArray);
        var dropDownToShow = Array(tempRequiredArray.keys);
      //  dropDownToShow = dropDownToShow.sorted();
        if let indexToRemove = dropDownToShow.index(of: "--Please Select--") {
            dropDownToShow.remove(at: indexToRemove);
        }
        
        
        dropDown.dataSource = dropDownToShow;
        dropDown.selectionAction = {(index, item) in
            let keyToUse = tempRequiredArray[item]!;
            sender.setTitle(item, for: UIControl.State());
            self.fetchStatesInsideCountry(keyToUse);
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    
    @objc func showStatesDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        var dropDownToShow = Array(statesArray.keys);
       // dropDownToShow = dropDownToShow.sorted();
        if let indexToRemove = dropDownToShow.index(of: "--Please Select--") {
            dropDownToShow.remove(at: indexToRemove);
        }
        
        dropDown.dataSource = dropDownToShow;
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
    
    
    
    @objc func fetchStatesInsideCountry(_ item:String)
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        baseURL = "vendorapi/index/getcountry/";
        
        var postString = "vendor_id="+vendorId+"&hashkey="+hashKey;
        postString += "&country_code="+item;
        
        print(postString);
        
        self.sendRequest(url: baseURL, parameters: postString)
        
        
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            
            //baseURL = "vendorapi/vtable/address"//"vupsshippingapi/index/address/";
            baseURL = "vmultishippingApi/setting/address/vendor_id/\(vendorId)/hashkey/\(hashKey)"
            let baseURL2 = "vmultishippingApi/setting/saveShippingAddress/hashkey/\(hashKey)"
            if requestUrl=="vendorapi/index/getcountry/"
            {
                guard let jsonData = try? JSON(data: data) else {return}
                
                Alert_File.removeLoadingIndicator(self);
                
                print("jsonData[\"data\"]DDDEEEVVV");
                print(jsonData);
                
                if(jsonData["success"].stringValue == "false")
                {
                    print("Show TEXTBOX");
                    print(self.stateSelectionViewTag);
                    print(self.view.viewWithTag(self.stateSelectionViewTag));
                    if let newStatePickerView = self.view.viewWithTag(self.stateSelectionViewTag) as? NewStatePickerView
                    {
                        print("jkdfgdjksfgkdjsfg")
                        self.stateDropDownIsVisible = false;
                        newStatePickerView.dropDownButton.isHidden = true;
                        newStatePickerView.textValue.isHidden = false;
                    }
                }
                else
                {
                    print("Show DropDown");
                    if let newStatePickerView = self.view.viewWithTag(self.stateSelectionViewTag) as? NewStatePickerView
                    {
                        self.stateDropDownIsVisible = true;
                        newStatePickerView.dropDownButton.isHidden = false;
                        newStatePickerView.textValue.isHidden = true;
                    }
                    
                    
                    self.statesArray = [String:String]();
                    for (_,val) in jsonData["states"]
                    {
                        self.statesArray[val["default_name"].stringValue] = val["region_id"].stringValue;
                    }
                    print("self.statesArray");
                    print(self.statesArray);
                }
            }
            else if(requestUrl==baseURL)//ForOnlyPushCart//"vupsshippingapi/index/address/")
            {
                guard let jsonData = try? JSON(data: data) else {return}
                print(jsonData)
                Alert_File.removeLoadingIndicator(self);
                for (_,UPS_Fileds) in jsonData["data"]["address"]
                {
                    var tempArray = [String:String]();
                    for (key,val) in UPS_Fileds
                    {
                        if(key == "values")
                        {
                            self.valuesArray.append(val);
                        }
                        else
                        {
                            tempArray[key] = val.stringValue;
                        }
                    }
                    self.shippingAddressFields.append(tempArray);
                }
                self.shippingSettingTableView.reloadData();
                
            }
            else if(requestUrl==baseURL2)//"vupsshippingapi/index/SaveShippingAddress")
            {
                guard let jsonData = try? JSON(data: data) else {return}

                print(jsonData);
                Alert_File.removeLoadingIndicator(self);
                
                if(jsonData["data"]["success"].stringValue != "true")
                {
                    let title = "Error";
                    let message = "Unfortunately Data Not Saved";
                    
                    Alert_File.showValidationError(self,title:title,message:message);
                    
                    return;
                }
                
                let title = "Success";
                let message = jsonData["data"]["message"].stringValue
                Alert_File.showValidationError(self,title:title,message:message);
                return;
                
            }
        }
        
    }
    @objc func fetchShippingAddressInfo()
    {
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
        //baseURL = "vendorapi/vtable/address"//"vupsshippingapi/index/address/";
        baseURL = "vmultishippingApi/setting/address/vendor_id/\(vendorId)/hashkey/\(hashKey)"
        
        print(baseURL);
        let postString = "vendor_id="+vendorId+"&hashkey="+hashKey;
        getRequest(url: baseURL);
        
    }
    
    
    @objc func saveShippingAddress(_ sender:UIButton)
    {
        var counter = 1;
        var shipping_settings_data = "[";
        print("---")
        print(dataToPost)
        for value in dataToPost {
            for (key,val) in value
            {
                //[{"country_id":""},{"city":""},{"region":""},{"postcode":""}]
                print(key);
                print(val);
                print(counter)
                var temp = "";
                if let button = self.view.viewWithTag(counter) as? UIButton
                {
                    for index in shippingAddressFields
                    {
                        if(index["required"] == "true")
                        {
                            if(button.titleLabel?.text == "")
                            {
                                self.view.makeToast("\(key) cannot be blank", duration: 2.0, position: .center)
                                return;
                            }
                        }
                    }
                    print(button.titleLabel?.text)
                    if(button.titleLabel?.text != nil)
                    {
                        
                        temp = (button.titleLabel?.text)!;
                    }
                    else
                    {
                        temp = "";
                    }
                    
                }
                if let textView = self.view.viewWithTag(counter) as? UITextView           {
                    print(textView.text)
                    for index in shippingAddressFields
                    {
                        if(index["required"] == "true")
                        {
                            if(textView.text == "")
                            {
                                self.view.makeToast("\(key) cannot be blank", duration: 2.0, position: .center)
                                return;
                            }
                        }
                    }
                    if let val = textView.text {
                        temp = val
                    }
                }
                
                print(temp);
                
                
                if(key != "region_id")
                {
                    print(key)
                    shipping_settings_data += "{\""+key+"\":";
                    print(type(of: self.view.viewWithTag(counter)));
                    if key == "country_id"{
                        if let text = self.countryArray[temp]{
                            temp = text;
                        }
                    }
                    shipping_settings_data += "\""+temp+"\"},";
                    print(shipping_settings_data)
                }
                else
                {
                    if let newStatePickerView = self.view.viewWithTag(self.stateSelectionViewTag) as? NewStatePickerView
                    {
                        print("@#@#@");
                        print(self.stateDropDownIsVisible);
                        if(self.stateDropDownIsVisible == false)
                        {
                            for index in shippingAddressFields
                            {
                                if(index["required"] == "true")
                                {
                                    if(newStatePickerView.textValue.text == "")
                                    {
                                        self.view.makeToast("\(key) cannot be blank", duration: 2.0, position: .center)
                                        return;
                                    }
                                }
                            }
                            var txt = "";
                            print("newStatePickerView.textValue.text");
                            print(newStatePickerView.textValue.text);
                            if(newStatePickerView.textValue.text != nil)
                            {
                                txt = newStatePickerView.textValue.text;
                            }
                            shipping_settings_data += "{\""+"region"+"\":";
                            shipping_settings_data += "\""+txt+"\"},";
                            shipping_settings_data += "{\""+"region_id"+"\":";
                            shipping_settings_data += "\""+""+"\"},";
                        }
                        else
                        {
                            var txt = "";
                            for index in shippingAddressFields
                            {
                                if(index["required"] == "true")
                                {
                                    if(newStatePickerView.dropDownButton.titleLabel!.text! == "")
                                    {
                                        self.view.makeToast("\(key) cannot be blank", duration: 2.0, position: .center)
                                        return;
                                    }
                                }
                            }
                            if(newStatePickerView.dropDownButton.titleLabel!.text != nil)
                            {
                                txt = newStatePickerView.dropDownButton.titleLabel!.text!;
                                if let text = self.statesArray[txt]{
                                    txt = text;
                                }
                            }
                            print(txt)
                            shipping_settings_data += "{\""+"region"+"\":";
                            shipping_settings_data += "\""+""+"\"},";
                            shipping_settings_data += "{\""+"region_id"+"\":";
                            shipping_settings_data += "\""+txt+"\"},";
                            print(shipping_settings_data)
                        }
                        
                    }
                    
                }
                
                
            }
            counter += 1;
        }
        shipping_settings_data = shipping_settings_data.substring(to: shipping_settings_data.characters.index(before: shipping_settings_data.endIndex));
        shipping_settings_data += "]";
        
        print(shipping_settings_data);
//        shipping_settings_data = shipping_settings_data.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!;
        print(shipping_settings_data);
        print("saveShippingAddress");
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        //vstorepickupapi/setting/saveShippingAddress/hashkey/\(hashkey)
        baseURL = "vmultishippingApi/setting/saveShippingAddress/hashkey/\(hashKey)"//"vendorapi/vtable/save"//"vupsshippingapi/index/SaveShippingAddress";
        
        print(baseURL);
        
        var postString = "vendor_id="+vendorId+"&hashkey="+hashKey;
        postString += "&shipping_settings_data="+shipping_settings_data;
        
        
        print(postString);
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        sendRequest(url: baseURL, parameters: postString);
        
    }
    
    
    @objc func findcountryForCode(_ value: String, countries: [String: String]) ->String?
    {
        for (key, array) in countries
        {
            if (array.contains(value))
            {
                return key
            }
        }
        
        return nil
    }
}
