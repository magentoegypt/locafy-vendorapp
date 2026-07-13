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

//@available(iOS 9.0, *)

class BundleItemsViewController: ced_VendorBaseClass {
    
    @IBOutlet weak var topWrapperView: UIView!
    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var addOptionButton: UIButton!
    @IBOutlet weak var shipBundleLabel: UILabel!
    @IBOutlet weak var shipBundleDropdownButton: UIButton!
    @IBOutlet weak var mainScrollView: UIScrollView!
    
    @IBOutlet weak var stackView: UIStackView!
    
    var dynamicPriceType = "1"
    var dynamicTypesArray = [String:String]()
    
    //edit time
    var isEditCase = false;
    var bundle_data = [String:[String:String]]();
    var tempDataToUse = [Int:[String:[String:String]]]();//use to fecth data initialy and transfer to actual variable
    var tempIdsArrayToUse = [Int:[Int]]();
    
    var previousBlockTagArray = [Int]();
    var savePreviousOptionIds = [String]();
    //edit time
    
    
    
    var baseURL = String();
    var shipBundleArray = [String:String]();
    
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
    
    
    var selectedBundleProductsIds = [Int:[Int]]();
    var selectProExtraImpInfo = [Int:[String:[String:String]]]();//give and take with dependent view
    
    var inputTypeDropdownSelectArray = [String:String]();
    let isRequiredDropdownSelectArray = ["Yes", "No"];
    var bundle_options_data = "";//used at saving and update time
    var bundle_selections_data = "";//used at saving and update time
    var prebundle_options_data = "";
    var prebundle_selections_data = "";
    
    var x : CGFloat = 0;
    var y : CGFloat = 0;
    var width : CGFloat = 0;
    let sub : CGFloat = 0;
    
    let screenSize: CGRect = UIScreen.main.bounds;
    let bundleItemViewHeight = CGFloat(200);
    var bundleItemViewCounter = 0;
    var bundleItemViewTagCounter = 20;
    
    var heightToUse = CGFloat(0);
    
    private var deletedPrebundleOptions = [String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        NotificationCenter.default.addObserver(self, selector: #selector(BundleItemsViewController.setTheSelection(_:)),name:NSNotification.Name(rawValue: "setTheSelectionID"), object: nil)
        ced_navigationBarController().addNavButton(self,str:"no")
        saveButton.setTitle("Save".localized, for: UIControl.State());
        saveButton.addTarget(self, action: #selector(BundleItemsViewController.fetchValuesTosend(_:)), for: UIControl.Event.touchUpInside);
        
        let color = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        saveButton.backgroundColor = color;
        
        addOptionButton.setTitle("", for: UIControl.State());
        addOptionButton.addTarget(self, action: #selector(BundleItemsViewController.renderBundleItemsView(_:)), for: UIControl.Event.touchUpInside);
        
        shipBundleLabel.text = "Ship Bundle";
        shipBundleDropdownButton.setTitle("", for: UIControl.State());
        shipBundleDropdownButton.addTarget(self, action: #selector(BundleItemsViewController.showShipBundleDropdown(_:)), for: UIControl.Event.touchUpInside);
        
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
    
    @objc func setTheSelection(_ notification: Notification)
    {
        print("setTheSelection")
        let arrayOfIdsReceived = defaults.object(forKey: "selectedProductsIds") as! [Int];
        selectedBundleProductsIds[currentSenderTag] = arrayOfIdsReceived;
        
        let selectProExtraImpInfoTemp = defaults.object(forKey: "selectProExtraImpInfo") as! [String:[String:String]];
        self.selectProExtraImpInfo[currentSenderTag] = selectProExtraImpInfoTemp;
    }
        
    @objc func initialSetup()
    {
        if #available(iOS 9.0, *) {
            let stackOnlyView = UIStackOnlyView()
            stackOnlyView.tag = 11;
            stackOnlyView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            stackOnlyView.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-sub,height: 40);
            stackOnlyView.center.x = self.view.center.x;
            
            x  = 0;
            y = heightToUse;
            width = screenSize.width-sub;
            heightToUse += 40;
            mainScrollView.addSubview(stackOnlyView);
            mainScrollView.contentSize = CGSize(width: screenSize.width, height: heightToUse);
            mainScrollView.tag = 12;

        } else {
            // Fallback on earlier versions
        };
        
    }
    
    @objc func renderBundleItemsView(_ sender:UIButton)
    {
        print("renderImgUploadView");
        bundleItemViewCounter += 1;
        if #available(iOS 9.0, *) {
            if let stackOnlyView = self.view.viewWithTag(11) as? UIStackOnlyView
            {
                let bundleItemView = BundleItemView();
                print("bundleItemViewTagCounter")
                print(bundleItemViewTagCounter)
                bundleItemView.tag = bundleItemViewTagCounter;
                
                bundleItemView.deleteButton.setTitle("", for: UIControl.State());
                bundleItemView.deleteButton.tag = bundleItemViewTagCounter;
                bundleItemView.deleteButton.addTarget(self, action: #selector(BundleItemsViewController.removeBundleItemsView(_:)), for: UIControl.Event.touchUpInside);
                
                bundleItemView.titleLabel.text = "Title";
                bundleItemView.titleValue.text = "";
                
                bundleItemView.InputTypeLabel.text = "Input Type";
                bundleItemView.inputTypeDropdown.setTitle("", for: UIControl.State());
                bundleItemView.inputTypeDropdown.addTarget(self, action: #selector(BundleItemsViewController.inputTypeDropdownSelect(_:)), for: UIControl.Event.touchUpInside);
                
                
    //            var is_option_required = "No";
    //            if(val["is_option_required"] == "1")
    //            {
    //                is_option_required = "Yes";
    //            }
                
                bundleItemView.isRequiredLabel.text = "Is Required";
                bundleItemView.isRequiredDropdown.setTitle("", for: UIControl.State());
                bundleItemView.isRequiredDropdown.addTarget(self, action: #selector(BundleItemsViewController.isRequiredDropdownSelect(_:)), for: UIControl.Event.touchUpInside);
                
//                bundleItemView.positionLabel.text = "Position";
                bundleItemView.positionValue.text = "";
                
                bundleItemView.addSelectionButton.setTitle("Add Selection", for: UIControl.State());
                bundleItemView.addSelectionButton.tag = bundleItemViewTagCounter;
                bundleItemView.addSelectionButton.addTarget(self, action: #selector(BundleItemsViewController.goToBundleProListingPage(_:)), for: UIControl.Event.touchUpInside);
                
                bundleItemView.coreWrapperView.makeCard(bundleItemView.coreWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                
                
                bundleItemViewTagCounter += 1;
                stackOnlyView.mainStackView.addArrangedSubview(bundleItemView);
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
        if let bundleItemView = self.view.viewWithTag(sender.tag) as? BundleItemView
        {
            if bundleItemView.previousBundleOptionId != ""{
                deletedPrebundleOptions.append(bundleItemView.previousBundleOptionId)
            }
            
            bundleItemViewCounter -= 1;
            bundleItemView.removeFromSuperview();
            refactorBundleItemsViewSection();
        }
    }
    
    @objc func goToBundleProListingPage(_ sender:UIButton)
    {
        print("goToBundleProListingPage");
        self.currentSenderTag = sender.tag;
        let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "bundleProductSelectionController") as! BundleProductSelectionController;
        viewcontrollor.product_id = product_id
        viewcontrollor.currentSenderTag = sender.tag;
        if(selectedBundleProductsIds[currentSenderTag] == nil)
        {
            selectedBundleProductsIds[currentSenderTag] = [Int]();
        }
        if(selectProExtraImpInfo[currentSenderTag] == nil)
        {
            selectProExtraImpInfo[currentSenderTag] = [String:[String:String]]();
        }
        viewcontrollor.selectedProductsIds = selectedBundleProductsIds[currentSenderTag]!;
        viewcontrollor.selectProExtraImpInfo = self.selectProExtraImpInfo[currentSenderTag]!;
        viewcontrollor.isEditCase = self.isEditCase;
        
        viewcontrollor.dynamicPriceType = dynamicPriceType
        viewcontrollor.dynamicTypesArray = dynamicTypesArray
        
        self.present(viewcontrollor, animated: true, completion: {});
        
        //self.navigationController?.pushViewController(viewcontrollor, animated: true)
    }
    
    @objc func showShipBundleDropdown(_ sender:UIButton)
    {
        print("showShipBundleDropdown");
        let dropDown = DropDown();
        dropDown.dataSource = Array(self.shipBundleArray.keys)
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
    
    @objc func fetchPreviousData()
    {
        //print("fetchPreviousData");
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        
        baseURL = "vproductapi/vproducts/bundleData/";
        
        //print(baseURL);
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&product_id="+product_id;

        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")

        //print(postString);
        sendRequest(url: baseURL, parameters: postString);
        
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            guard let jsonData = try? JSON(data: data) else {return}
            if(requestUrl == "vproductapi/vproducts/bundleData/")
            {
                
                print("jsonData");
                print(jsonData);
                
                self.dynamicPriceType = jsonData["data"]["dynamic_price_type"].stringValue
                
                jsonData["data"]["input_type"].arrayValue.forEach{self.inputTypeDropdownSelectArray[$0["label"].stringValue] = $0["value"].stringValue}
                jsonData["data"]["ship_bundle_items"].arrayValue.forEach{self.shipBundleArray[$0["label"].stringValue] = $0["value"].stringValue}
                jsonData["data"]["selection_price_type"].arrayValue.forEach{self.dynamicTypesArray[$0["label"].stringValue] = $0["value"].stringValue}
                
                shipBundleDropdownButton.setTitle(shipBundleArray.someKey(forValue: jsonData["data"]["shipment_type"].stringValue), for: .normal)
                shipBundleDropdownButton.setTitle(shipBundleArray.someKey(forValue: jsonData["data"]["shipment_type"].stringValue), for: .normal)
                
                if isEditCase{
                    if(jsonData["data"]["success"].stringValue != "true")
                    {
                        let title = "No Data";
                        let message = "No Bundle Option Found";
                        Alert_File.showValidationError(self,title:title,message:message);
                        return;
                    }
                    
                    var localGlobalCounter = 0;
                    for (_,val) in jsonData["data"]["bundle_data"]
                    {
                        var arrayKey = "";
                        var arrayVal = [String:String]();
                        for (k,v) in val
                        {
                            if(k == "option_id")
                            {
                                arrayKey = v.stringValue;
                            }
                            if(k == "option_selection")
                            {
                                var counterTemp = 0;
                                var tempArrayToUseLater = [String:[String:String]]();
                                var tempIdsArrayToUseLater = [Int]();
                                for (_,inrVal) in v
                                {
                                    var inrTempArray = [String:String]();
                                    var productID = "";
                                    for(ik,iv) in inrVal
                                    {
                                        if(ik == "product_id")
                                        {
                                            tempIdsArrayToUseLater.append(Int(iv.stringValue)!);
                                            productID = iv.stringValue;
                                        }
                                        if(ik == "selection_price")
                                        {
                                            inrTempArray["selection_price_value"] = iv.stringValue;
                                            continue;
                                        }
                                        if(ik == "selection_price_type")
                                        {
//                                            if(iv.stringValue == "1")
//                                            {
                                                inrTempArray[ik] = iv.stringValue;
//                                            }
//                                            else
//                                            {
//                                                inrTempArray[ik] = "Percent";
//                                            }
                                            continue;
                                        }
                                        if(ik == "user_can_change_qty")
                                        {
                                            inrTempArray["selection_can_change_qty"] = iv.stringValue
                                            continue;
                                        }
                                        if(ik == "default_qty")
                                        {
                                            inrTempArray["selection_qty"] = iv.stringValue;
                                        }
                                        inrTempArray[ik] = iv.stringValue;
                                    }
                                    tempArrayToUseLater[productID] = inrTempArray;
                                    counterTemp += 1;
                                }
                                self.tempDataToUse[localGlobalCounter] = (tempArrayToUseLater);
                                self.tempIdsArrayToUse[localGlobalCounter] = (tempIdsArrayToUseLater);
                                localGlobalCounter += 1;
                                continue;
                            }
                            arrayVal[k] = v.stringValue;
                        }
                        
                        self.bundle_data[arrayKey] = arrayVal;
                        self.savePreviousOptionIds.append(arrayKey);
                        
                    }
                    print("self.tempDataToUse");
                    print(self.tempDataToUse);
                    
                    self.showPreviousBundleData();
                }
                
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
    
    @objc func showPreviousBundleData()
    {
        var inrTempCounter = 0;
        for (optnId,val) in bundle_data
        {
            bundleItemViewCounter += 1;
            if #available(iOS 9.0, *) {
                if let stackOnlyView = self.view.viewWithTag(11) as? UIStackOnlyView
                {
                    let bundleItemView = BundleItemView();
                    bundleItemView.coreWrapperView.makeCard(bundleItemView.coreWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
                    bundleItemView.previousBundleOptionId = optnId
                    bundleItemView.tag = bundleItemViewTagCounter;
                    self.previousBlockTagArray.append(bundleItemViewTagCounter);
                    
                    self.selectProExtraImpInfo[bundleItemViewTagCounter] = tempDataToUse[inrTempCounter];
                    print("bundleItemViewTagCounter");
                    print(bundleItemViewTagCounter);
                    self.selectedBundleProductsIds[bundleItemViewTagCounter] = tempIdsArrayToUse[inrTempCounter];
                    print(selectedBundleProductsIds);
                    inrTempCounter += 1;
                    
                    bundleItemView.deleteButton.setTitle("", for: UIControl.State());
                    bundleItemView.deleteButton.tag = bundleItemViewTagCounter;
                    bundleItemView.deleteButton.addTarget(self, action: #selector(BundleItemsViewController.removeBundleItemsView(_:)), for: UIControl.Event.touchUpInside);
                    
                    bundleItemView.titleLabel.text = "Title";
                    bundleItemView.titleValue.text = val["option_title"];
                    
                    bundleItemView.InputTypeLabel.text = "Input Type";
                    if let optn = self.inputTypeDropdownSelectArray.someKey(forValue: val["option_type"] ?? ""){
                        bundleItemView.inputTypeDropdown.setTitle(optn, for: UIControl.State());
                    }else{
                        bundleItemView.inputTypeDropdown.setTitle("", for: UIControl.State());
                    }
                    
                    bundleItemView.inputTypeDropdown.addTarget(self, action: #selector(BundleItemsViewController.inputTypeDropdownSelect(_:)), for: UIControl.Event.touchUpInside);
                    
                    
                    var is_option_required = "No";
                    if(val["is_option_required"] == "1")
                    {
                        is_option_required = "Yes";
                    }
                    bundleItemView.isRequiredLabel.text = "Is Required";
                    bundleItemView.isRequiredDropdown.setTitle(is_option_required, for: UIControl.State());
                    bundleItemView.isRequiredDropdown.addTarget(self, action: #selector(BundleItemsViewController.isRequiredDropdownSelect(_:)), for: UIControl.Event.touchUpInside);
                    
//                    bundleItemView.positionLabel.text = "Position";
                    bundleItemView.positionValue.text = val["option_position"];
                    
                    bundleItemView.addSelectionButton.setTitle("Add Selection", for: UIControl.State());
                    bundleItemView.addSelectionButton.tag = bundleItemViewTagCounter;
                    bundleItemView.addSelectionButton.addTarget(self, action: #selector(BundleItemsViewController.goToBundleProListingPage(_:)), for: UIControl.Event.touchUpInside);
                    
                    bundleItemViewTagCounter += 1;
                    stackOnlyView.mainStackView.addArrangedSubview(bundleItemView);
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
        print("cdfsdfsdfs");
        print("self.selectProExtraImpInfo");
        print(self.selectProExtraImpInfo);
        print("self.selectedBundleProductsIds");
        print(self.selectedBundleProductsIds);
        
    }
    
    @objc func inputTypeDropdownSelect(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = Array(self.inputTypeDropdownSelectArray.keys)
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
    
    @objc func isRequiredDropdownSelect(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = self.isRequiredDropdownSelectArray;
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
    
    @objc func fetchValuesTosend(_ sender:UIButton)
    {
        print("fetchValuesTosend");
        
        bundle_options_data = "{";
        bundle_selections_data = "{";
        
        prebundle_options_data = "{";
        prebundle_selections_data = "{";
        var counter = 20;
        var localCount = 0;
        var localCountBundle = 0;
        for counter in 20..<bundleItemViewTagCounter
        {
            //option_id
            if let bundleItemView = self.view.viewWithTag(counter) as? BundleItemView
            {
                if let _ = previousBlockTagArray.index(of: counter)
                {
                    print(self.savePreviousOptionIds);
                    print("self.savePreviousOptionIds");
                    let option_id2 = bundleItemView.previousBundleOptionId
                    let option_id = self.savePreviousOptionIds[localCount];
                    print("option_id");
                    print("option_id");
                    print(option_id);
                    print(option_id2);
                    prebundle_options_data += "\""+String(localCount)+"\":\"{";
                    prebundle_options_data += "\\\""+"position"+"\\\":"+"\\\""+(bundleItemView.positionValue.text ?? "")+"\\\",";
                    prebundle_options_data += "\\\""+"delete"+"\\\":"+"\\\""+""+"\\\",";
                    prebundle_options_data += "\\\""+"title"+"\\\":"+"\\\""+bundleItemView.titleValue.text!+"\\\",";
                    prebundle_options_data += "\\\""+"option_id"+"\\\":"+"\\\""+option_id+"\\\",";//have to change at edit time
                    if(bundleItemView.isRequiredDropdown.titleLabel!.text == "Yes")
                    {
                        prebundle_options_data += "\\\""+"required"+"\\\":"+"\\\""+"1"+"\\\",";
                    }
                    else
                    {
                        prebundle_options_data += "\\\""+"required"+"\\\":"+"\\\""+"0"+"\\\",";
                    }
                    if let optn = inputTypeDropdownSelectArray[bundleItemView.inputTypeDropdown.titleLabel?.text ?? ""]{
                        prebundle_options_data += "\\\""+"type"+"\\\":"+"\\\""+optn+"\\\"}\",";
                    }
                    
                    
                    
                    if(self.selectProExtraImpInfo[counter] != nil)
                    {
                        let tempArrayToLoop = self.selectProExtraImpInfo[counter]!;
                        print("tempArrayToLoop");
                        print(tempArrayToLoop);
                        
                        prebundle_selections_data += "\""+String(localCount)+"\":[";
                        
                        for (key,val) in tempArrayToLoop
                        {
                            prebundle_selections_data += "{";
                            
//                            prebundle_selections_data += "\""+"product_id"+"\":"+"\""+key+"\",";
//                            prebundle_selections_data += "\""+"selection_id"+"\":"+"\""+""+"\",";
//                            prebundle_selections_data += "\""+"delete"+"\":"+"\""+""+"\",";
                            prebundle_selections_data += "\""+"option_id"+"\":"+"\""+option_id+"\",";
                            
                            for (ik,iv) in val
                            {
                                prebundle_selections_data += "\""+ik+"\":"+"\""+iv+"\",";
                            }
                            prebundle_selections_data = prebundle_selections_data.substring(to: prebundle_selections_data.characters.index(before: prebundle_selections_data.endIndex));
                            prebundle_selections_data += "},";
                        }
                        prebundle_selections_data = prebundle_selections_data.substring(to: prebundle_selections_data.characters.index(before: prebundle_selections_data.endIndex));
                        prebundle_selections_data += "],"
                    }
                }
                else
                {
                    //validation code
                    if(bundleItemView.titleValue.text == nil || bundleItemView.titleValue.text == "")
                    {
                        self.view.showToastMsg("Please Fill Tittle");
                        return;
                    }
                    if(bundleItemView.titleValue.text == nil || bundleItemView.titleValue.text == "")
                    {
                        self.view.showToastMsg("Please Fill Tittle");
                        return;
                    }
                    
                    if(bundleItemView.isRequiredDropdown.titleLabel!.text == nil)
                    {
                        self.view.showToastMsg("Please select is required");
                        return;
                    }
                    //validation code
                    
                    bundle_options_data += "\""+String(localCountBundle)+"\":\"{";
                    bundle_options_data += "\\\""+"position"+"\\\":"+"\\\""+(bundleItemView.positionValue.text ?? "")+"\\\",";
                    bundle_options_data += "\\\""+"delete"+"\\\":"+"\\\""+""+"\\\",";
                    bundle_options_data += "\\\""+"title"+"\\\":"+"\\\""+bundleItemView.titleValue.text!+"\\\",";
                    bundle_options_data += "\\\""+"option_id"+"\\\":"+"\\\""+""+"\\\",";//have to change at edit time
                    if(bundleItemView.isRequiredDropdown.titleLabel!.text == "Yes")
                    {
                        bundle_options_data += "\\\""+"required"+"\\\":"+"\\\""+"1"+"\\\",";
                    }
                    else
                    {
                        bundle_options_data += "\\\""+"required"+"\\\":"+"\\\""+"0"+"\\\",";
                    }
                    if let optn = inputTypeDropdownSelectArray[bundleItemView.inputTypeDropdown.titleLabel?.text ?? ""]{
                        bundle_options_data += "\\\""+"type"+"\\\":"+"\\\""+optn+"\\\"}\",";
                    }
                    
                    
                    
                    print("selectProExtraImpInfo");
                    print(selectProExtraImpInfo);
                    
                    //[Int:[String:[String:String]]]();
                    if(self.selectProExtraImpInfo[counter] != nil)
                    {
                        let tempArrayToLoop = self.selectProExtraImpInfo[counter]!;
                        print("tempArrayToLoop");
                        print(tempArrayToLoop);
                        
                        bundle_selections_data += "\""+String(localCountBundle)+"\":[";
                        
                        for (key,val) in tempArrayToLoop
                        {
                            bundle_selections_data += "{";
                            
//                            bundle_selections_data += "\""+"product_id"+"\":"+"\""+key+"\",";
//                            bundle_selections_data += "\""+"selection_id"+"\":"+"\""+""+"\",";
//                            bundle_selections_data += "\""+"delete"+"\":"+"\""+""+"\",";
                            bundle_selections_data += "\""+"option_id"+"\":"+"\""+""+"\",";
                            
                            for (ik,iv) in val
                            {
                                bundle_selections_data += "\""+ik+"\":"+"\""+iv+"\",";
                            }
                            bundle_selections_data = bundle_selections_data.substring(to: bundle_selections_data.characters.index(before: bundle_selections_data.endIndex));
                            bundle_selections_data += "},";
                        }
                        bundle_selections_data = bundle_selections_data.substring(to: bundle_selections_data.characters.index(before: bundle_selections_data.endIndex));
                        bundle_selections_data += "],"
                    }
                    localCountBundle += 1
                }
               
            }
            localCount += 1;
        }
        bundle_options_data = bundle_options_data.substring(to: bundle_options_data.characters.index(before: bundle_options_data.endIndex));
        bundle_options_data += "}";
        
        
        if !deletedPrebundleOptions.isEmpty{
            deletedPrebundleOptions.forEach{otpnId in
                prebundle_options_data += "\""+String(localCount)+"\":\"{";
                prebundle_options_data += "\\\""+"position"+"\\\":"+"\\\""+""+"\\\",";
                prebundle_options_data += "\\\""+"delete"+"\\\":"+"\\\""+"1"+"\\\",";
                prebundle_options_data += "\\\""+"title"+"\\\":"+"\\\""+""+"\\\",";
                prebundle_options_data += "\\\""+"option_id"+"\\\":"+"\\\""+otpnId+"\\\",";//have to change at edit time
                prebundle_options_data += "\\\""+"required"+"\\\":"+"\\\""+"0"+"\\\",";
                prebundle_options_data += "\\\""+"type"+"\\\":"+"\\\""+""+"\\\"}\",";
                
                prebundle_selections_data += "\""+String(localCount)+"\":["
                prebundle_selections_data += "],"
                
                localCount += 1
            }
        }
        
        prebundle_options_data = prebundle_options_data.substring(to: prebundle_options_data.characters.index(before: prebundle_options_data.endIndex));
        prebundle_options_data += "}";
            
        print("prebundle_options_data");
        print(prebundle_options_data);
        
        
        print("bundle_options_data");
        print(bundle_options_data);
        
        bundle_selections_data = bundle_selections_data.substring(to: bundle_selections_data.characters.index(before: bundle_selections_data.endIndex));
        bundle_selections_data += "}";
        print("bundle_selections_data");
        print(bundle_selections_data);
        
        
        prebundle_selections_data = prebundle_selections_data.substring(to: prebundle_selections_data.characters.index(before: prebundle_selections_data.endIndex));
        prebundle_selections_data += "}";
        print("prebundle_selections_data");
        print(prebundle_selections_data);
        
        self.updateProductInfo();
    }
    

    @objc func updateProductInfo()
    {
        let shipType = shipBundleArray[shipBundleDropdownButton.currentTitle ?? ""] ?? ""
        if shipType == ""{
            self.view.makeToast("Please select shipment type")
            return
        }
        
        
        print("updateProductInfo");
        //bundle_options_data = bundle_options_data.stringByAddingPercentEncodingWithAllowedCharacters(.URLHostAllowedCharacterSet())!;
        //bundle_selections_data = bundle_selections_data.stringByAddingPercentEncodingWithAllowedCharacters(.URLHostAllowedCharacterSet())!;
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        
        
        baseURL = "vendorapi/vproducts/update/";
        
        print(baseURL);
        var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        postString += "&product_id="+product_id;
        if let selectedWebsite = UserDefaults.standard.value(forKey: "selectedWebsite") as? String{
            if selectedWebsite != ""{
                postString += "&websites="+selectedWebsite;
            }
        }
        var bundle_options = "{";
        if(bundle_options_data != "}")
        {
            bundle_options += "\""+"bundle_options_data"+"\":"+bundle_options_data+",";
            //postString += "&bundle_options_data="+bundle_options_data;
        }else{
            bundle_options += "\""+"bundle_options_data"+"\":"+"{}"+",";
        }
        if(bundle_selections_data != "}")
        {
            bundle_options += "\""+"bundle_selections_data"+"\":"+bundle_selections_data+","
            //postString += "&bundle_selections_data="+bundle_selections_data;
        }else{
            bundle_options += "\""+"bundle_selections_data"+"\":"+"{}"+","
        }
        
        if prebundle_options_data != "}"{
            bundle_options += "\""+"prebundle_options_data"+"\":"+prebundle_options_data+","
        }else{
            bundle_options += "\""+"prebundle_options_data"+"\":"+"{}"+","
        }
        if prebundle_selections_data != "}"{
            bundle_options += "\""+"prebundle_selections_data"+"\":"+prebundle_selections_data;
        }else{
            bundle_options += "\""+"prebundle_selections_data"+"\":"+"{}"
        }
        bundle_options += "}";
        
        print("bundle_options");
        print(bundle_options);
        postString += "&bundle_options="+bundle_options;
        postString += "&shipment_type="+shipType;
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        sendRequest(url: baseURL, parameters: postString);
    }
    
}
