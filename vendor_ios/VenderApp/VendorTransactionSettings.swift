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

class VendorTransactionSettings: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate
{
    @IBOutlet weak var transactionSettingTableView: UITableView!
    @IBOutlet weak var topWrapperView: UIView!
    @IBOutlet weak var leftButton: UIButton!
    @IBOutlet weak var rightButton: UIButton!
    @IBOutlet weak var rightButtonWidth: NSLayoutConstraint!
    
    let refreshControl = UIRefreshControl()
    let screenSize: CGRect = UIScreen.main.bounds;
    
    var baseURL = String();
    var tagsArray = [Int:String]();
    var globalCounter = 0;
    var globalTag = 21;
    var heightToUse : CGFloat = 0;
    var fixedHeight : CGFloat = 60;
    var padding : CGFloat = 5;
    var valuesArray = [JSON]();
    var dropdownValues = [Int:String]();
    var helperArray = [String:[Int]]();
    var idsCombo = [String:[Int]]();
    let labelHeight : CGFloat = 30;
    let dropdownHeight : CGFloat = 40;
    let txtViewHeight : CGFloat = 45;
    let smallPaddingHeight : CGFloat = 5;
    let largePaddingHeight : CGFloat = 10;
    
    //var dataInJSONForm :JSON();
    
    var paymentMethodsFields = [String:[[String:String]]]();
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        transactionSettingTableView.backgroundColor = DynamicColor.ViewBackgroundColor
        leftButton.backgroundColor = DynamicColor.ViewBackgroundColor
        // Do any additional setup after loading the view.
        self.title = NSLocalizedString("Transaction Settings", comment: "")
        let tlabel = UILabel(frame: CGRect(x: 0, y: 0, width: 200, height: 40));
        tlabel.text = self.title
        tlabel.textColor = UIColor.white
        tlabel.font = UIFont(name: "Roboto-Bold", size: 21.0)
        tlabel.backgroundColor = UIColor.clear
        tlabel.adjustsFontSizeToFitWidth = true
        self.navigationItem.titleView = tlabel
        self.leftButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 17)
        self.rightButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 17)
        self.leftButton.setTitle(NSLocalizedString("Transaction Settings", comment: ""), for: UIControl.State());
        self.rightButton.setTitle(NSLocalizedString("Save".localized, comment: ""), for: UIControl.State());
        self.rightButton.addTarget(self, action: #selector(VendorTransactionSettings.saveTransactionSettings(_:)), for: UIControl.Event.touchUpInside);
        self.rightButtonWidth.constant = screenSize.width/3;
        
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        let colorGreen = Ced_CommonVendor.UIColorFromRGB("#002f63");
        rightButton.backgroundColor = colorGreen
        topWrapperView.backgroundColor = color
        //leftButton.backgroundColor = color
        
        ced_navigationBarController().addNavButton(self,str:"no")
        self.transactionSettingTableView.dataSource = self;
        self.transactionSettingTableView.delegate = self;
        
        self.fetchTransactionSettingsInfo();
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        transactionSettingTableView.refreshControl = refreshControl
    }
    
    override func didReceiveMemoryWarning()
    {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func refresh(_ sender: AnyObject) {
        heightToUse = 0;
        fixedHeight = 60;
        self.paymentMethodsFields.removeAll()
        transactionSettingTableView.reloadData()
        self.fetchTransactionSettingsInfo();
    }
    
    // MARK: - Table view data source
    
    @objc func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if(paymentMethodsFields.count == 0)
        {
            return 0;
        }
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = transactionSettingTableView.dequeueReusableCell(withIdentifier: "longSingleCell", for: indexPath) as! LongSingleTableViewCell;
        
        //cell.mainWrapperView.backgroundColor = UIColor.whiteColor();
        
        /** Top View **/
        /*let profileTopView = ProfileTopView();
         profileTopView.autoresizingMask = [UIViewAutoresizing.FlexibleLeftMargin,UIViewAutoresizing.FlexibleRightMargin];
         profileTopView.frame = CGRectMake(0, heightToUse, screenSize.width-10,CGFloat(90));
         profileTopView.center.x = self.view.center.x;
         heightToUse += CGFloat(90);
         
         profileTopView.topButton.addTarget(self, action: Selector("saveTransactionSettings:"), forControlEvents: UIControlEvents.TouchUpInside);
         cell.mainWrapperView.addSubview(profileTopView);*/
        
        
        
        //*** top label **//
        heightToUse += CGFloat(10);
        let ts_label_view = TS_Label_View();
        ts_label_view.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        ts_label_view.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-10,height: CGFloat(40));
        heightToUse += CGFloat(50);
        ts_label_view.center.x = cell.mainWrapperView.center.x;
        
        ts_label_view.entityNameLabel.font=UIFont(name: "Roboto-Regular", size: 17)
        ts_label_view.entityNameLabel.text = NSLocalizedString("PAYMENT METHOD INFORMATION", comment: "")
        ts_label_view.entityNameLabel.textAlignment = NSTextAlignment.center;
        ts_label_view.entityNameLabel.textColor = UIColor.white;
        heightToUse += CGFloat(5);
        
        //design code
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        ts_label_view.backgroundColor = DynamicColor.secondaryColor
        ts_label_view.entityNameLabel.textColor = UIColor.white;
        ts_label_view.entityNameLabel.backgroundColor = DynamicColor.secondaryColor;
        ts_label_view.layer.borderColor = color.cgColor
        ts_label_view.layer.borderWidth = 1.0
        ts_label_view.layer.cornerRadius = 5
        //print("---paymentfields--\(paymentMethodsFields)");
        
        cell.mainWrapperView.addSubview(ts_label_view);
        for paymentMethods in paymentMethodsFields
        {
            let wrapperView = UIView();
            
            var inrHeightToUse = CGFloat(0);
            
            let ts_label_view = TS_Label_View();
            ts_label_view.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            ts_label_view.frame = CGRect(x: 15, y: inrHeightToUse, width: screenSize.width-20,height: 40);
            ts_label_view.center.x = cell.mainWrapperView.center.x;
            inrHeightToUse += 40;
            inrHeightToUse += padding;
            wrapperView.addSubview(ts_label_view);
            ts_label_view.entityNameLabel.font=UIFont(name: "Roboto-Regular", size: 17)
            ts_label_view.entityNameLabel.text = paymentMethods.0.localized;
            
            //design
            ts_label_view.entityNameLabel.textColor = UIColor.white;
            ts_label_view.entityNameLabel.backgroundColor = DynamicColor.secondaryColor;
            
            
            var tempIdsCombo = [Int]();
            var parentName = "";
            print("paymentMethods.1---\(paymentMethods.1)")
            for val in paymentMethods.1
            {
                
                parentName = val["name"]!;
                tempIdsCombo.append(globalTag);
                self.tagsArray[globalTag] = val["tag"]!;
                
                if(val["type"] == "text")
                {
                    let label_TextView_Combo_View = Label_TextView_Combo_View();
                    label_TextView_Combo_View.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                    label_TextView_Combo_View.frame = CGRect(x: 0, y: inrHeightToUse, width: screenSize.width-20,height: fixedHeight);
                    label_TextView_Combo_View.center.x = cell.mainWrapperView.center.x;
                    inrHeightToUse += fixedHeight;
                    inrHeightToUse += padding;
                    label_TextView_Combo_View.entityNameLabel.font=UIFont(name: "Roboto-Regular", size: 15)
                    label_TextView_Combo_View.entityNameLabel.text = val["label"];
                    label_TextView_Combo_View.entityValueTxtField.text = val["value"];
                    if(val["tag"] == "bank_account_number")
                    {
                        label_TextView_Combo_View.entityValueTxtField.keyboardType = .numberPad
                    }
                    label_TextView_Combo_View.entityValueTxtField.tag = globalTag;
                    wrapperView.addSubview(label_TextView_Combo_View);
                }
                else if(val["type"] == "select")
                {
                    print("---select---\(globalTag)");
                    var dropDownValue = "";
                    print("--valArray--\(self.valuesArray)")
                    print("---gCounter--\(globalCounter)")
                    if(globalCounter==8)
                    {
                        globalCounter=4;
                    }
                    for (inrVal) in self.valuesArray[globalCounter].arrayValue
                    {
                        print("looping---")
                        print(inrVal["value"].stringValue)
                        print("----")
                        print(val["value"])
                        if(inrVal["value"].stringValue == val["value"])
                        {
                            dropDownValue = inrVal["label"].stringValue;
                            self.dropdownValues[globalTag] = inrVal["value"].stringValue;
                            break;
                        }
                    }
                    
                    let label_DropDown_Combo_View = Label_DropDown_Combo_View();
                    label_DropDown_Combo_View.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                    label_DropDown_Combo_View.frame = CGRect(x: 0, y: inrHeightToUse, width: screenSize.width-20,height: fixedHeight);
                    label_DropDown_Combo_View.center.x = cell.mainWrapperView.center.x;
                    inrHeightToUse += fixedHeight;
                    inrHeightToUse += padding;
                    label_DropDown_Combo_View.entityNameLabel.font=UIFont(name: "Roboto-Regular",size: 15)
                    label_DropDown_Combo_View.entityNameLabel.text = val["label"];
                    label_DropDown_Combo_View.dropDownButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 15)
                    label_DropDown_Combo_View.dropDownButton.setTitle( dropDownValue, for: UIControl.State());
                    label_DropDown_Combo_View.dropDownButton.addTarget(self, action: #selector(VendorTransactionSettings.showTransactionDropdown(_:)), for: UIControl.Event.touchUpInside);
                    label_DropDown_Combo_View.dropDownButton.tag = globalTag;
                    wrapperView.addSubview(label_DropDown_Combo_View);
                    
                }
                self.globalCounter += 1;
                self.globalTag += 1;
            }
            
            self.idsCombo[parentName] = tempIdsCombo;
            
            wrapperView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            wrapperView.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-20,height: inrHeightToUse);
            wrapperView.center.x = cell.mainWrapperView.center.x;
            
            //wrapperView.makeCard(wrapperView, cornerRadius: 2, color: UIColor.redColor(), shadowOpacity: 0.3);
            
            wrapperView.backgroundColor = DynamicColor.ViewBackgroundColor//UIColor.white;
            wrapperView.makeCard(wrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
            
            
            //wrapperView.layer.borderWidth = 1;
            //wrapperView.layer.borderColor = UIColor.grayColor().CGColor;
            
            heightToUse += inrHeightToUse;
            heightToUse += 10;
            cell.mainWrapperView.addSubview(wrapperView);
        }
        
        return cell;
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return heightToUse;
    }
    
    @objc func showTransactionDropdown(_ sender:UIButton)
    {
        var arrayToLoop = valuesArray[sender.tag-21];
        if(sender.tag == 29)
        {
            arrayToLoop = valuesArray[4];
        }
        var dropDownArray = [String:String]();
        for (_,val) in arrayToLoop
        {
            dropDownArray[val["value"].stringValue] = val["label"].stringValue;
        }
        let dropDown = DropDown();
        let dataSource = [String](dropDownArray.values);
        dropDown.dataSource = dataSource;
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            let keys = dropDownArray.allKeysForValue(item);
            self.dropdownValues[sender.tag] = keys.first;
        }
        dropDown.anchorView = sender;
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
        
    }
    
    
    @objc func fetchTransactionSettingsInfo()
    {
        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Please Wait...", comment: ""));
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
        //baseURL = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
        baseURL = "vendorapi/setting/availableMethod";
        
        print(baseURL);
        
        
        //var request = URLRequest(url: URL(string: baseURL)!);
        
        let postStringToSend = "vendor_id="+vendorId+"&hashkey="+hashKey;
        self.sendRequest(url: baseURL, parameters: postStringToSend)
        
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        refreshControl.endRefreshing()
        if(requestUrl=="vendorapi/setting/availableMethod")
        {
            guard let jsonData = try? JSON(data: data!) else {return}
            print("json data---\(jsonData)");
            Alert_File.removeLoadingIndicator(self);
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            //print(userData)
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            let customer_id = userData["customerId"] as! String;
            let vendor_name = userData["vendorName"] as! String;
            let profile_picture = userData["profilePicUrl"] as! String;
            let profile_complete = userData["profile_complete"] as! String;
            let valerts = jsonData["data"]["valerts"].stringValue
            //saving value in NSUserDefault to use later on :: start
            let dict = ["customerId": customer_id, "hashKey": hashKey,"vendorName":vendor_name,"vendorId":vendorId,"profilePicUrl":profile_picture,"valerts":valerts,"profile_complete":profile_complete];
            self.defaults.set(dict, forKey: "userInfoDict");
            for paymentMethod in jsonData["data"]["fieldset"].arrayValue
            {
                for val in paymentMethod
                {
                    if(val.0 == "Check/Money Order" || val.0 == "PayPal"){
                        continue
                    }
                    print("val.1----\(val.1)")
                    var fieldInfoArrayMain = [[String:String]]();
                    for (_,fields) in val.1
                    {
                        var fieldInfoArray = [String:String]();
                        print("fields----\(fields)")
                        for (k,v) in fields
                        {
                            if( k == "values" )
                            {
                                self.valuesArray.append(v);
                                
                                
                            }
                            else
                            {
                                if( k == "tag" )
                                {
                                    //self.tagsArray.append(v.stringValue);
                                }
                                fieldInfoArray[k] = v.stringValue;
                            }
                        }
                        fieldInfoArrayMain.append(fieldInfoArray);
                        print("filedinfo----\(fieldInfoArray)")
                    }
                    self.paymentMethodsFields[val.0] = fieldInfoArrayMain;
                    print("field info array---\(fieldInfoArrayMain)");
                }
                
                print(self.paymentMethodsFields);
                
                self.transactionSettingTableView.reloadData();
            }
        }
        else
        {
            print(NSString(data: data!,
                           encoding: String.Encoding.utf8.rawValue)! as String)
            guard let jsonData = try? JSON(data: data!) else {return}

            print(jsonData);
            Alert_File.removeLoadingIndicator(self);
            
            if(jsonData["data"]["success"].stringValue != "true")
            {
                let title = NSLocalizedString("Error".localized, comment: "");
                let message = jsonData["data"]["message"].stringValue;
                Alert_File.showValidationError(self,title:title,message:message);
                return;
            }
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            //print(userData)
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            let customer_id = userData["customerId"] as! String;
            let vendor_name = userData["vendorName"] as! String;
            let profile_picture = userData["profilePicUrl"] as! String;
            let profile_complete = userData["profile_complete"] as! String;
            let valerts = jsonData["data"]["valerts"].stringValue
            //saving value in NSUserDefault to use later on :: start
            let dict = ["customerId": customer_id, "hashKey": hashKey,"vendorName":vendor_name,"vendorId":vendorId,"profilePicUrl":profile_picture,"valerts":valerts,"profile_complete":profile_complete];
            self.defaults.set(dict, forKey: "userInfoDict");
            let title = NSLocalizedString("Success", comment: "");
            let message = jsonData["data"]["message"].stringValue
            Alert_File.showValidationError(self,title:title.localized,message:message);
            return;
            
        }
    }
    
    @objc func saveTransactionSettings(_ sender:UIButton)
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
        print("self.idsCombo");
        print(self.idsCombo);
        
        var settings_data = "{";
        for (key,val) in idsCombo
        {
            var isActive = false;
            
            settings_data += "\""+key+"\":{";
            for tag in val
            {
                settings_data += "\""+tagsArray[tag]!+"\":";
                if let _ = self.view.viewWithTag(tag) as? UIButton
                {
                    var tempVal = "";
                    if(dropdownValues[tag] != nil)
                    {
                        print("Nil NOt Found");
                        tempVal = dropdownValues[tag]!;
                    }
                    if(tempVal == "1")
                    {
                        isActive = true;
                    }
                    settings_data += "\""+tempVal+"\",";
                }
                else if let label_TextView_Combo_View_name = self.view.viewWithTag(tag) as? UITextView
                {
                    if(isActive)
                    {
                        if(label_TextView_Combo_View_name.text! == "")
                        {
                            let title = NSLocalizedString("Error", comment: "");
                            let message = NSLocalizedString("Please Fill All fields Of Active Section!", comment: "");
                            Alert_File.showValidationError(self,title:title,message:message);
                            print("THIS IS IS REQUIRED NOW");
                            return;
                        }
                    }
                    settings_data += "\""+label_TextView_Combo_View_name.text!+"\",";
                }
            }
            settings_data = settings_data.substring(to: settings_data.characters.index(before: settings_data.endIndex));
            settings_data += "},";
        }
        settings_data = settings_data.substring(to: settings_data.characters.index(before: settings_data.endIndex));
        settings_data += "}";
        
        print(settings_data);
        
        
        baseURL = "vendorapi/setting/index";
        print(baseURL);
        
        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Please Wait...", comment: ""));
        //request.httpMethod = "POST";
        var postString = "vendor_id="+vendorId+"&hashkey="+hashKey;
        postString += "&settings_data="+settings_data;
        self.sendRequest(url: baseURL, parameters: postString)
    }
    
    
}

/** Imp Extn Code **/
extension Dictionary where Value : Equatable {
    func allKeysForValue(_ val : Value) -> [Key] {
        return self.filter { $1 == val }.map { $0.0 }
    }
}
