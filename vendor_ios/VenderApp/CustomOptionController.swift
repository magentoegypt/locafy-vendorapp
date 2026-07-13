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

class CustomOptionController: ced_VendorBaseClass {
    
    //edit time
    var isEditCase = false;
    var custom_option = [String:[String:String]]();
    var deletedoptions = [String]();//views ids to be deleted
    var deletedoptiontypes = [String](); // dynamic add option delete-ids in case of dropdown
    var tempDataToUse = [String:[String:[String:String]]]();//use to fecth data initialy and transfer to actual variable
    var previousOptionIdsForDropdown = [Int:String]();// var to save option_ids at edit time::very imp
    //edit time
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
    var finalStringToPost = String();
    var selectedFiltersIds = [Int]();
    var proDetailArray = [[String:String]]();
    
    
    // variables for sending server request
    var baseURL = String();
    var attributeSetId = String();
    
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var topWrapperView: UIView!
    @IBOutlet weak var leftButton: UIButton!
    @IBOutlet weak var middleButton: UIButton!
    @IBOutlet weak var rightButton: UIButton!
    @IBOutlet weak var mainScrollView: UIScrollView!
    
    var additionalDataFromNextPage = [Int:[String:String]]();
    
    var dataToSendArrayDropDownCase = [Int:[String:[String:String]]]();
    
    var varToDecideView2Show = "";
    
    var inputTypeDropDownArray = [String:String]()//["Field", "Area", "File", "Drop-Down", "Radio Buttons", "Checkbox", "Multiple Select", "Date", "Date & Time", "Time"];
    let isRequiredDropDownArray = ["Yes", "No"];
    
    var priceTypeDropdownArray = [String:String]()
    
    var currentSenderTag = 0;
    var x : CGFloat = 0;
    var y : CGFloat = 0;
    var width : CGFloat = 0;
    let sub : CGFloat = 0;
    
    let screenSize: CGRect = UIScreen.main.bounds;
    let bundleItemViewHeight = CGFloat(210);
    var bundleItemViewCounter = 0;
    var bundleItemViewTagCounter = 20;
    
    var heightToUse = CGFloat(0);
   
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if(selectedProductTypeId == "configurable"){
            leftButton.isHidden = true
        }
        ////print("CustomOptionController");
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 19)!]
        topLabel.text = "Custom Options".localized
        // Do any additional setup after loading the view.
        ced_navigationBarController().addNavButton(self, str: "no")
        
        defaults.removeObject(forKey: "additionalDataCustomOptionDropdown");
        defaults.removeObject(forKey: "deletedoptiontypes");
        NotificationCenter.default.addObserver(self, selector: #selector(CustomOptionController.setAdditionalData(_:)),name:NSNotification.Name(rawValue: "setAdditionalDataID"), object: nil);
        
        leftButton.setTitle(NSLocalizedString("Save".localized, comment: ""), for: UIControl.State());
        leftButton.addTarget(self, action: #selector(CustomOptionController.saveFunction(_:)), for: UIControl.Event.touchUpInside);
        
        middleButton.addTarget(self, action: #selector(CustomOptionController.renderCustomOptionView(_:)), for: UIControl.Event.touchUpInside);
        
        rightButton.setTitle(NSLocalizedString("Continue".localized, comment: ""), for: UIControl.State());
        rightButton.addTarget(self, action: #selector(CustomOptionController.continueFunction(_:)), for: UIControl.Event.touchUpInside);
        
        
        let color = Ced_CommonVendor.UIColorFromRGB("#2abb9c");
        leftButton.backgroundColor = DynamicColor.themeColor;
        rightButton.backgroundColor = color
        
        if(selectedProductTypeId == "bundle"){
            self.showAlertToNoCustom()
            return
        }
        
        self.initialSetup();
        
//        if(isEditCase)
//        {
            self.fetchPreviousData();
//        }
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func showAlertToNoCustom(){
        let alert = UIAlertController(title: "Information", message: "No custom options are allowed for bundle products!", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: { _ in
            self.switchToNextView()
        }))
        self.present(alert, animated: true)
    }
    
    @objc func setAdditionalData(_ notification: Notification)
    {
        ////print("setAdditionalData")
        
        if(varToDecideView2Show == "drop_down" || varToDecideView2Show == "radio" || varToDecideView2Show == "checkbox" || varToDecideView2Show == "multiple")
        {
            let additionalDataCustomOption = defaults.object(forKey: "additionalDataCustomOptionDropdown") as! [String:[String:String]];
            print("Data Received On Main Page");
            print(additionalDataCustomOption);
            dataToSendArrayDropDownCase[currentSenderTag] = additionalDataCustomOption;
            
            ////print(dataToSendArrayDropDownCase);
            deletedoptiontypes = defaults.object(forKey: "deletedoptiontypes") as! [String];
            
            return;
        }
        let additionalDataCustomOption = defaults.object(forKey: "additionalDataCustomOption") as! [String:String];
        additionalDataFromNextPage[currentSenderTag] = additionalDataCustomOption;
        ////print(additionalDataCustomOption);
        //print(additionalDataFromNextPage);
    }
    
    
    
    
    @objc func initialSetup()
    {
        if #available(iOS 9.0, *) {
            let stackOnlyView = UIStackOnlyView()
            stackOnlyView.tag = 11;
            stackOnlyView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            stackOnlyView.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-sub,height: 40);
            stackOnlyView.center.x = self.view.center.x;
            
            stackOnlyView.backgroundColor = UIColor.gray;
            stackOnlyView.mainStackView.spacing   = 10.0
            
            x  = 0;
            y = 0;
            width = screenSize.width-sub;
            
            heightToUse += 40;
            
            mainScrollView.addSubview(stackOnlyView);
            
            mainScrollView.contentSize = CGSize(width: screenSize.width, height: heightToUse);
            mainScrollView.tag = 12;
            
            if(!isEditCase)
            {
                let title = NSLocalizedString("No Custom Options", comment: "");
                let message = NSLocalizedString("No Custom Options Added yet.", comment: "");
                Alert_File.showValidationError(self,title:title,message:message);
            }
        } else {
            // Fallback on earlier versions
        };
        
        
    }
    
    @objc func renderCustomOptionView(_ sender:UIButton)
    {
        //print("renderImgUploadView");
        bundleItemViewCounter += 1;
        if #available(iOS 9.0, *) {
            if let stackOnlyView = self.view.viewWithTag(11) as? UIStackOnlyView
            {
                let customOptionView = CustomOptionView();
                customOptionView.tag = bundleItemViewTagCounter;
                
                customOptionView.topDeleteButton.setTitle("", for: UIControl.State());
                customOptionView.topDeleteButton.tag = bundleItemViewTagCounter;
                customOptionView.topDeleteButton.addTarget(self, action: #selector(CustomOptionController.removeBundleItemsView(_:)), for: UIControl.Event.touchUpInside);
                
                customOptionView.titleLabel.text = NSLocalizedString("Title", comment: "");
                customOptionView.tittleValue.text = "";
                
                customOptionView.inputTypeLabel.text = NSLocalizedString("Input Type", comment: "");
                customOptionView.inputTypeButton.setTitle("", for: UIControl.State());
                customOptionView.inputTypeButton.tag = bundleItemViewTagCounter;
                customOptionView.inputTypeButton.addTarget(self, action: #selector(CustomOptionController.inputTypeDropDown(_:)), for: UIControl.Event.touchUpInside);
                
                customOptionView.isRequiredLabel.text = NSLocalizedString("Is Required?", comment: "");
                customOptionView.isRequiredButton.setTitle("", for: UIControl.State());
                customOptionView.isRequiredButton.tag = bundleItemViewTagCounter;
                customOptionView.isRequiredButton.addTarget(self, action: #selector(CustomOptionController.isRequiredDropDown(_:)), for: UIControl.Event.touchUpInside);
                
                
                customOptionView.sortOrderLabel.text = NSLocalizedString("Sort Order", comment: "");
                customOptionView.sortOrderValue.text = "";
                
                customOptionView.bottomButton.setTitle(NSLocalizedString("Add Selection", comment: ""), for: UIControl.State());
                customOptionView.bottomButton.tag = bundleItemViewTagCounter;
                customOptionView.bottomButton.addTarget(self, action: #selector(CustomOptionController.goToOptionAdditionPage(_:)), for: UIControl.Event.touchUpInside);
                
                customOptionView.coreWrapperView.makeCard(customOptionView.coreWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
                
                bundleItemViewTagCounter += 1;
                stackOnlyView.mainStackView.addArrangedSubview(customOptionView);
                stackOnlyView.frame = CGRect(x: x, y: y, width: width, height: bundleItemViewHeight*CGFloat(bundleItemViewCounter));
                
                if let mainWrapperView = self.view.viewWithTag(12) as? UIScrollView
                {
                    mainScrollView.contentSize = CGSize(width: mainWrapperView.bounds.width, height: bundleItemViewHeight*CGFloat(bundleItemViewCounter));
                }
            }
        } else {
            // Fallback on earlier versions
        }
    }
    
    @objc func refactorBundleItemsViewSection()
    {
        if #available(iOS 9.0, *) {
            if let stackOnlyView = self.view.viewWithTag(11) as? UIStackOnlyView
            {
                stackOnlyView.frame = CGRect(x: x, y: y, width: width, height: bundleItemViewHeight*CGFloat(bundleItemViewCounter));
                
                if let mainWrapperView = self.view.viewWithTag(12) as? UIScrollView
                {
                    mainScrollView.contentSize = CGSize(width: mainWrapperView.bounds.width, height: bundleItemViewHeight*CGFloat(bundleItemViewCounter));
                }
            }
        } else {
            // Fallback on earlier versions
        }
    }
    
    @objc func removeBundleItemsView(_ sender:UIButton)
    {
        if let customOptionView = self.view.viewWithTag(sender.tag) as? CustomOptionView
        {
            if(isEditCase)
            {
                if(additionalDataFromNextPage[sender.tag] != nil)
                {
                    if(additionalDataFromNextPage[sender.tag] != nil && additionalDataFromNextPage[sender.tag]!["option_id"] != nil)
                    {
                        let optionIDToDelete = additionalDataFromNextPage[sender.tag]!["option_id"]!;
                        self.deletedoptions.append(optionIDToDelete);
                    }
                }
            }
            
            bundleItemViewCounter -= 1;
            customOptionView.removeFromSuperview();
            refactorBundleItemsViewSection();
        }
    }
    
    @objc func goToOptionAdditionPage(_ sender:UIButton)
    {
        var varToDecideView2Show = "";
        if let customOptionView = self.view.viewWithTag(sender.tag) as? CustomOptionView
        {
            if(customOptionView.inputTypeButton.titleLabel?.text == nil)
            {
                let title = NSLocalizedString("Error", comment: "");
                let message = NSLocalizedString("Please Select Input Type!", comment: "");
                Alert_File.showValidationError(self,title:title,message:message);
                return;
            }
            varToDecideView2Show = inputTypeDropDownArray[customOptionView.inputTypeButton.currentTitle ?? ""] ?? ""
        }
        self.varToDecideView2Show = varToDecideView2Show;
        //print("goToOptionAdditionPage");
        self.currentSenderTag = sender.tag;
        
        //print(currentSenderTag);
        //print(additionalDataFromNextPage);
        
        let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "optionAdditionPageController") as! OptionAdditionPageController;
        viewcontrollor.currentSenderTag = sender.tag;
        viewcontrollor.varToDecideView2Show = varToDecideView2Show
        viewcontrollor.isEditCase = isEditCase;
        viewcontrollor.priceTypeDropdownArray = priceTypeDropdownArray
        if(varToDecideView2Show == "drop_down" || varToDecideView2Show == "radio" || varToDecideView2Show == "checkbox" || varToDecideView2Show == "multiple")
        {
            //print("varToDecideView2Show");
            //print(varToDecideView2Show);
            //print(currentSenderTag);
            //print(dataToSendArrayDropDownCase[currentSenderTag]);
            if(dataToSendArrayDropDownCase[currentSenderTag] == nil)
            {
                dataToSendArrayDropDownCase[currentSenderTag] = [String:[String:String]]();
            }
            viewcontrollor.dataToSendArrayDropDownCase = dataToSendArrayDropDownCase[currentSenderTag]!;
            
            viewcontrollor.deletedoptiontypes = self.deletedoptiontypes;
            
            print("Data Send To Page 2");
            print(dataToSendArrayDropDownCase[currentSenderTag]!);
        }
        else
        {
            if(additionalDataFromNextPage[currentSenderTag] == nil)
            {
                additionalDataFromNextPage[currentSenderTag] = [String:String]();
            }
            viewcontrollor.dataToSendArray = additionalDataFromNextPage[currentSenderTag]!;
        }
        self.navigationController?.present(viewcontrollor, animated: true, completion: {});
        //self.navigationController?.pushViewController(viewcontrollor, animated: true)
    }
    
    
    @objc func saveFunction(_ sender:UIButton)
    {
        //print("saveFunction");
        self.fetchData();
        self.sendUpdateRequestToServer(false);
    }
    
    @objc func continueFunction(_ sender:UIButton)
    {
        self.fetchData();
        self.sendUpdateRequestToServer(true);
    }
    
    @objc func fetchData()
    {
        //print("continueFunction");
        
        var counter = 20;
        var loopCtr = 0;
        var inrStrToPost = "{\"options\":{";
        for counter in 20..<bundleItemViewTagCounter
        {
            if let customOptionView = self.view.viewWithTag(counter) as? CustomOptionView
            {
                loopCtr += 1;
                
                inrStrToPost += "\""+String(loopCtr-1)+"\":{";
                let titleValue = customOptionView.tittleValue.text;
                
                var inputTypeButton = inputTypeDropDownArray[customOptionView.inputTypeButton.currentTitle ?? ""] ?? ""
                                
                var isRequiredButton = "";
                if((customOptionView.isRequiredButton.titleLabel?.text) != nil)
                {
                    isRequiredButton = (customOptionView.isRequiredButton.titleLabel?.text)!;
                }
                
                let sortOrderValue = customOptionView.sortOrderValue.text!;
                
                // common to all
                inrStrToPost += "\""+"is_delete"+"\":"+"\""+""+"\",";
                //inrStrToPost += "\""+"previous_type"+"\":"+"\""+""+"\",";
                //inrStrToPost += "\""+"previous_group"+"\":"+"\""+""+"\",";
                
                //inrStrToPost += "\""+"id"+"\":"+"\""+String(loopCtr-1)+"\",";
                
                inrStrToPost += "\""+"title"+"\":";
                inrStrToPost += "\""+titleValue!+"\",";
                inrStrToPost += "\""+"type"+"\":"+"\""+inputTypeButton+"\",";
                inrStrToPost += "\""+"sort_order"+"\":"+"\""+sortOrderValue+"\",";
                
                if(isRequiredButton == "Yes")
                {
                    inrStrToPost += "\""+"is_require"+"\":"+"\""+"1"+"\",";
                }
                else
                {
                    inrStrToPost += "\""+"is_require"+"\":"+"\""+"0"+"\",";
                }
                
                if(dataToSendArrayDropDownCase[counter] != nil)
                {
                    //print("dataToSendArrayDropDownCase");
                    //print(dataToSendArrayDropDownCase);
                    if(isEditCase)
                    {
                        if(self.previousOptionIdsForDropdown[counter] != nil)
                        {
                            inrStrToPost += "\""+"option_id"+"\":"+"\""+self.previousOptionIdsForDropdown[counter]!+"\",";// have to take care of that
                        }
                        else
                        {
                            //inrStrToPost += "\""+"option_id"+"\":"+"\""+"0"+"\",";// have to take care of that
                        }
                        
                    }
                    else
                    {
                        //inrStrToPost += "\""+"option_id"+"\":"+"\""+"0"+"\",";// have to take care of that
                    }
                    
                    var tempArray = [String:[String:String]]();
                    tempArray = dataToSendArrayDropDownCase[counter]!;
                    
                    if(tempArray.count > 0)
                    {
                        var inrStr = "{";
                        
                        for(key,val) in tempArray
                        {
                            if let optionId = val["option_type_id"]{
                                // change in technique in case of delete
                                if let _ = deletedoptiontypes.index(of: optionId)
                                {
                                    continue;
                                }
                            }
                            //print(val);
                            inrStr += "\""+key+"\":{";
                            for(k,v) in val
                            {
                                if(k != "option_type_id")
                                {
                                    inrStr += "\""+k+"\":"+"\""+v+"\",";
                                }
                                
                            }
                            inrStr = inrStr.substring(to: inrStr.characters.index(before: inrStr.endIndex));
                            inrStr += "},";
                        }
                        if(inrStr != "{")
                        {
                            inrStr = inrStr.substring(to: inrStr.characters.index(before: inrStr.endIndex));
                        }
                        inrStr += "}";
                        
                        inrStrToPost += "\""+"values"+"\":"+inrStr+",";
                    }
                    else
                    {
                        inrStrToPost += "\""+"values"+"\":"+"{}"+",";
                    }
                }
                else
                {
                    if(isEditCase)
                    {
                        if(additionalDataFromNextPage[counter] != nil && additionalDataFromNextPage[counter]!["option_id"] != nil)
                        {
                            let temp = additionalDataFromNextPage[counter]!["option_id"]!;
                            //print("temp");
                            //print(temp);
                            
                            inrStrToPost += "\""+"option_id"+"\":"+"\""+temp+"\",";// have to take care of that
                        }
                        else
                        {
                            //inrStrToPost += "\""+"option_id"+"\":"+"\""+"0"+"\",";// have to take care of that
                        }
                        
                    }
                    else
                    {
                        //inrStrToPost += "\""+"option_id"+"\":"+"\""+"0"+"\",";// have to take care of that
                    }
                    
                    var tempArray = [String:String]();
                    
                    if(additionalDataFromNextPage[counter] == nil)
                    {
                        //print("have to show alert");
                        return;
                    }
                    
                    tempArray = additionalDataFromNextPage[counter]!;
                    
                    for(key,val) in tempArray
                    {
                        if(key != "option_type_id")
                        {
                            inrStrToPost += "\""+key+"\":"+"\""+val+"\",";
                        }
                        
                    }
                }
                inrStrToPost = inrStrToPost.substring(to: inrStrToPost.characters.index(before: inrStrToPost.endIndex));
                inrStrToPost += "},";
            }
            
        }
        
        inrStrToPost = inrStrToPost.substring(to: inrStrToPost.characters.index(before: inrStrToPost.endIndex));
        inrStrToPost += "}}";
        
        self.finalStringToPost = inrStrToPost;
        //print(inrStrToPost);
        
    }
    
    
    // send custom option update request to server
    @objc func sendUpdateRequestToServer(_ proceed:Bool)
    {
        proceeds=proceed;
        let strToPost = self.finalStringToPost;
        
        print("strToPost");
        print(strToPost);
        //strToPost = strToPost.stringByAddingPercentEncodingWithAllowedCharacters(.URLHostAllowedCharacterSet())!;
        
        //print("sendUpdateRequestToServer");
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        
        let baseURL = "vendorapi/vproducts/update/";
        
        //print(baseURL);
        
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&product_id="+product_id;
        if strToPost != "{\"options\":}}"{
            postString += "&custom_options="+strToPost;
        }else{
            postString += "&custom_options={}";
//            var cstd = "{}"
//            var optnSend = [String:[String:String]]()
//            if !deletedoptions.isEmpty{
//                deletedoptions.enumerated().forEach{(indx,otn) in
//                    optnSend[String(indx)] = ["option_id":otn,"is_delete":"","title":"","type":"","sort_order":"","is_require":"","price":"","sku":"","price_type":"","max_characters":""]
//                }
//                cstd = ["options":optnSend].convtToJson() as String
//            }
//            postString += "&custom_options=" + cstd;
        }
       
        print(strToPost)
        if let selectedWebsite = UserDefaults.standard.value(forKey: "selectedWebsite") as? String{
            if selectedWebsite != ""{
                postString += "&websites="+selectedWebsite;
            }
        }
        if(attributeSetId != "")
        {
            postString += "&attribute_set="+attributeSetId;
        }
        
        var deletedoptions = "{";
        var deletedoptiontypesStr = "{";
        
        if(isEditCase)
        {
            if(self.deletedoptions.count > 0)
            {
                for (val) in self.deletedoptions
                {
                    deletedoptions += "\""+val+"\":"+"\""+"true"+"\",";
                }
                deletedoptions = deletedoptions.substring(to: deletedoptions.characters.index(before: deletedoptions.endIndex));
                deletedoptions += "}";
                
                postString += "&deletedoptions="+deletedoptions;
                
            }
            
            if(self.deletedoptiontypes.count > 0)
            {
                for (val) in self.deletedoptiontypes
                {
                    deletedoptiontypesStr += "\""+val+"\":"+"\""+"true"+"\",";
                }
                deletedoptiontypesStr = deletedoptiontypesStr.substring(to: deletedoptiontypesStr.characters.index(before: deletedoptiontypesStr.endIndex));
                deletedoptiontypesStr += "}";
                
                postString += "&deletedoptiontypes="+deletedoptiontypesStr;
                
            }
            
        }
        
        print("deletedoptiontypesStr");
        print(deletedoptiontypesStr);
        print("-----postString-----")
        print(postString)
        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Please Wait...", comment: ""))
        sendRequest(url: baseURL, parameters: postString);
    }
    
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        guard let jsonData = try? JSON(data: data!) else {return}
        print(jsonData);
        if(requestUrl == "vendorapi/vproducts/update/")
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
            else
            {
                self.view.showToastMsg(jsonData["data"]["message"].stringValue)
                
            }
            Ced_CommonVendor.delay(2.0, closure: {
                if(self.proceeds)
                {
                    self.switchToNextView();
                    self.proceeds=false;
                }else{
                    let view = UIStoryboard(name: "ProductAddon", bundle: nil).instantiateViewController(withIdentifier: "manageProductViewController") as! ManageProductViewController
                    self.navigationController?.setViewControllers([view], animated: true)
                }
            })
            
            
            
        }
        else if(requestUrl == "vproductapi/vproducts/customOptionData/")
        {
            Alert_File.removeLoadingIndicator(self);
            jsonData["data"]["option_types"].arrayValue.forEach{inputTypeDropDownArray[$0["label"].stringValue] = $0["code"].stringValue}
            jsonData["data"]["price_type"].arrayValue.forEach{priceTypeDropdownArray[$0["label"].stringValue] = $0["value"].stringValue}
            
            if(jsonData["data"]["success"].stringValue != "true")
            {
                let title = NSLocalizedString("Nothing Found", comment: "");
                let message = NSLocalizedString("No Custom Options Available", comment: "");
                Alert_File.showValidationError(self,title:title,message:message);
                return;
            }
            
            var localGlobalCounter = 0;
            for val in jsonData["data"]["custom_option"].arrayValue
            {
                var arrayKey = "";
                var arrayVal = [String:String]();
                for (k,v) in val
                {
                    if(k == "option_id")
                    {
                        arrayKey = v.stringValue;
                    }
                    if(k == "option")
                    {
                        var counterTemp = 0;
                        var tempArrayToUseLater = [String:[String:String]]();
                        for inrVal in v.arrayValue
                        {
                            var inrTempArray = [String:String]();
                            for(ik,iv) in inrVal
                            {
                                inrTempArray[ik] = iv.stringValue;
                            }
                            tempArrayToUseLater[String(counterTemp)] = inrTempArray;
                            counterTemp += 1;
                        }
                        self.tempDataToUse[val["option_id"].stringValue] = (tempArrayToUseLater);
                        localGlobalCounter += 1;
                        continue;
                    }
                    arrayVal[k] = v.stringValue;
                }
                
                self.custom_option[arrayKey] = arrayVal;
            }
            self.showPreviousCustomOptions();
        }
    }
    
    @objc func inputTypeDropDown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = Array(self.inputTypeDropDownArray.keys)
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
    
    @objc func isRequiredDropDown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = self.isRequiredDropDownArray;
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
    
    //edit time @objc function to fetch previous custom options
    @objc func fetchPreviousData()
    {
        //print("fetchPreviousData");
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        
        let baseURL = "vproductapi/vproducts/customOptionData/";
        
        print(baseURL);
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&product_id="+product_id;
        
        print(postString);
        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Please Wait...", comment: ""))
        sendRequest(url: baseURL, parameters: postString);
    }
    
    @objc func showPreviousCustomOptions()
    {
        
        var dropDownCount = 0;
        ////print("custom_option");
        ////print(custom_option);
        for (_,val) in custom_option
        {
            //print("renderImgUploadView");
            bundleItemViewCounter += 1;
                if let stackOnlyView = self.view.viewWithTag(11) as? UIStackOnlyView
                {
                    
                    let customOptionView = CustomOptionView();
                    customOptionView.tag = bundleItemViewTagCounter;
                    
                    customOptionView.coreWrapperView.makeCard(customOptionView.coreWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
                    
                    customOptionView.topDeleteButton.setTitle("", for: UIControl.State());
                    customOptionView.topDeleteButton.tag = bundleItemViewTagCounter;
                    customOptionView.topDeleteButton.addTarget(self, action: #selector(CustomOptionController.removeBundleItemsView(_:)), for: UIControl.Event.touchUpInside);
                    
                    var typeValueInitial = inputTypeDropDownArray.someKey(forValue: val["type"] ?? "")
                    
//                    //continue
//                    if(val["type"]?.lowercased() == "drop_down".lowercased())
//                    {
//                        typeValueInitial = "Drop-Down";
//                    }
//                    else if (val["type"]?.lowercased() == "multiple".lowercased())
//                    {
//                        typeValueInitial = "Multiple Select";
//                        print(typeValueInitial)
//                    }
//                    else if (val["type"]?.lowercased() == "Date_time".lowercased())
//                    {
//                        typeValueInitial = "Date & Time";
//                        print(typeValueInitial)
//                    }
//                    else if (val["type"]?.lowercased() == "radio".lowercased())
//                    {
//                        typeValueInitial = "Radio Buttons";
//                        print(typeValueInitial)
//                    }
//                    else
//                    {
//                        typeValueInitial = val["type"]!.uppercaseFirst;
//                        print(typeValueInitial)
//                    }
                    
                    customOptionView.titleLabel.text = NSLocalizedString("Title", comment: "");
                    customOptionView.tittleValue.text = val["title"];
                    
                    
                    customOptionView.inputTypeLabel.text = NSLocalizedString("Input Type", comment: "");
                    customOptionView.inputTypeButton.setTitle(typeValueInitial, for: UIControl.State());
                    customOptionView.inputTypeButton.tag = bundleItemViewTagCounter;
                    customOptionView.inputTypeButton.addTarget(self, action: #selector(CustomOptionController.inputTypeDropDown(_:)), for: UIControl.Event.touchUpInside);
                    
                    
                    customOptionView.isRequiredLabel.text = NSLocalizedString("Is Required?", comment: "");
                    var isRequired = "Yes";
                    if(val["is_require"] == "0")
                    {
                        isRequired = "No";
                    }
                    customOptionView.isRequiredButton.setTitle(isRequired, for: UIControl.State());
                    customOptionView.isRequiredButton.tag = bundleItemViewTagCounter;
                    customOptionView.isRequiredButton.addTarget(self, action: #selector(CustomOptionController.isRequiredDropDown(_:)), for: UIControl.Event.touchUpInside);
                    
                    
                    customOptionView.sortOrderLabel.text = NSLocalizedString("Sort Order", comment: "");
                    customOptionView.sortOrderValue.text = val["sort_order"];
                    
                    
                    customOptionView.bottomButton.setTitle(NSLocalizedString("Add Selection", comment: ""), for: UIControl.State());
                    customOptionView.bottomButton.tag = bundleItemViewTagCounter;
                    customOptionView.bottomButton.addTarget(self, action: #selector(CustomOptionController.goToOptionAdditionPage(_:)), for: UIControl.Event.touchUpInside);
                    
                    stackOnlyView.mainStackView.addArrangedSubview(customOptionView);
                    stackOnlyView.frame = CGRect(x: x, y: y, width: width, height: bundleItemViewHeight*CGFloat(bundleItemViewCounter));
                    
                    
                    var additionalDataCustomOption = [String:String]();
                    
                    additionalDataCustomOption["max_characters"] = val["max_characters"];
                    additionalDataCustomOption["sku"] = val["sku"];
                    additionalDataCustomOption["price_type"] = val["price_type"]!;
                    additionalDataCustomOption["price"] = val["price"];
                    
                    additionalDataCustomOption["option_id"] = val["option_id"];
                    
                    additionalDataCustomOption["file_extension"] = val["file_extension"];
                    additionalDataCustomOption["image_size_x"] = val["image_size_x"];
                    additionalDataCustomOption["image_size_y"] = val["image_size_y"];
                    additionalDataCustomOption["store_title"] = val["store_title"];
                    
                    additionalDataFromNextPage[bundleItemViewTagCounter] = additionalDataCustomOption;
                    
                    print(typeValueInitial)
                    if(val["type"] == "drop_down" || val["type"] == "radio" || val["type"] == "checkbox" || val["type"] == "multiple")
                    {
                        //print("tempDataToUse");
                        //print(tempDataToUse);
                        //print(tempDataToUse[dropDownCount]);
                        
                        self.previousOptionIdsForDropdown[bundleItemViewTagCounter] = val["option_id"];// option id is saved here :: very useful
                        
                        
                        if(self.tempDataToUse[val["option_id"] ?? ""] != nil)
                        {
                            self.dataToSendArrayDropDownCase[bundleItemViewTagCounter] = tempDataToUse[val["option_id"] ?? ""]!;
                        }
                        else
                        {
                            self.dataToSendArrayDropDownCase[bundleItemViewTagCounter] = [String:[String:String]]();
                        }
                        dropDownCount += 1;
                    }
                    
                    bundleItemViewTagCounter += 1;
                }
        }
        
        
        if let mainWrapperView = self.view.viewWithTag(12) as? UIScrollView
        {
            mainScrollView.contentSize = CGSize(width: mainWrapperView.bounds.width, height: bundleItemViewHeight*CGFloat(bundleItemViewCounter));
        }
        
    }
    
    
    @objc func switchToNextView()
    {
        print("switchToNextView");
        print(selectedProductTypeId);
        
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
    
}


extension String {
    var first: String {
        return String(characters.prefix(1))
    }
    var last: String {
        return String(characters.suffix(1))
    }
    var uppercaseFirst: String {
        return first.uppercased() + String(characters.dropFirst())
    }
}
