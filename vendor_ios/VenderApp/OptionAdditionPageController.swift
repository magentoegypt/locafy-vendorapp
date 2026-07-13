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

class OptionAdditionPageController: ced_VendorBaseClass {

    var errorOccured = false;
    
    //edit case
    var isEditCase = false;
    var deletedoptiontypes = [String](); // dynamic add option delete-ids in case of dropdown
    var optionIdsArrayTracking = [Int:String]();//for use in this page only
    //edit case
    
    var currentSenderTag = 0;
    var varToDecideView2Show = "";
    
    var dataToSendArray = [String:String]();
    var dataToSendArrayDropDownCase = [String:[String:String]]();
    
    let dynamicHeight1 = CGFloat(200);
    let dynamicHeight2 = CGFloat(240);
    let dynamicHeight3 = CGFloat(155);
    let extraViewHeight = CGFloat(285);
    
   
    
    @IBOutlet weak var topWrapperView1: UIView!
    @IBOutlet weak var button1: UIButton!
    @IBOutlet weak var mainScrollView: UIScrollView!
    
    
    var x : CGFloat = 0;
    var y : CGFloat = 0;
    var width : CGFloat = 0;
    let sub : CGFloat = 0;
    
    let screenSize: CGRect = UIScreen.main.bounds;
    let bundleItemViewHeight = CGFloat(245);
    var bundleItemViewCounter = 0;
    var bundleItemViewTagCounter = 20;
    
    var heightToUse = CGFloat(0);
    var priceTypeDropdownArray = [String:String]()
    var hasOptionError = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        mainScrollView.isDirectionalLockEnabled = true;
        
        button1.setTitle("Done", for: UIControl.State());
        button1.addTarget(self, action: #selector(OptionAdditionPageController.goBackPressed(_:)), for: UIControl.Event.touchUpInside);
        let color = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        button1.backgroundColor = color;
        
        ced_navigationBarController().addNavButton(self, str: "no")
        self.initialSetup();
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @objc func goBackPressed(_ sender:UIButton)
    {
        errorOccured = false;
        switch(varToDecideView2Show)
        {
        case "field":
            self.fetchDynamicType1View();
            break;
        case "area":
            self.fetchDynamicType1View();
            break;
        case "file":
            self.fetchDynamicType2View();
            break;
        case "date":
            self.fetchDynamicType3View();
            break;
        case "date_time":
            self.fetchDynamicType3View();
            break;
        case "time":
            self.fetchDynamicType3View();
            break;
        default:
            self.fetchAllAddedOptionValue();
            break;
        }

        
        print("goBackPressed");
        
        if(errorOccured)
        {
            self.view.showToastMsg(" All fields are Required.".localized);
            return;
        }
        
        if hasOptionError{
            self.view.showToastMsg("Please create atleast one Option!");
            return;
        }
        
        if(varToDecideView2Show == "drop_down" || varToDecideView2Show == "radio" || varToDecideView2Show == "checkbox" || varToDecideView2Show == "multiple")
        {
            defaults.removeObject(forKey: "deletedoptiontypes");
            defaults.set(self.deletedoptiontypes, forKey: "deletedoptiontypes");
            
            //print("goBackPressed###");
            defaults.removeObject(forKey: "additionalDataCustomOptionDropdown");
            //print("goBackPressed###123");
            //print(self.dataToSendArrayDropDownCase);
            defaults.set(self.dataToSendArrayDropDownCase, forKey: "additionalDataCustomOptionDropdown");
            print("goBackPressed###1230000");
            self.dismiss(animated: true, completion: {});
            print("goBackPressed###1230000");
            NotificationCenter.default.post(name: Notification.Name(rawValue: "setAdditionalDataID"), object: nil);
            print("goBackPressed###1230000");
            return;
        }
        
        //print(self.dataToSendArray);
        defaults.removeObject(forKey: "additionalDataCustomOption");
        defaults.set(self.dataToSendArray, forKey: "additionalDataCustomOption");
        print("goBackPressed###1230000DEV");
        self.dismiss(animated: true, completion: {});
        print("goBackPressed###1230000DEV");
        NotificationCenter.default.post(name: Notification.Name(rawValue: "setAdditionalDataID"), object: nil);
    }
    
    @objc func initialSetup()
    {
        if(varToDecideView2Show == "drop_down" || varToDecideView2Show == "radio" || varToDecideView2Show == "checkbox" || varToDecideView2Show == "multiple")
        {
            let topButtonWithWrapperView = TopButtonWithWrapperView();
            topButtonWithWrapperView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            topButtonWithWrapperView.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-sub,height: 40);
            topButtonWithWrapperView.center.x = self.view.center.x;
            heightToUse += 40;
            
            topButtonWithWrapperView.topButton.setTitle("Add Options", for: UIControl.State());
            topButtonWithWrapperView.topButton.addTarget(self, action: #selector(OptionAdditionPageController.addDynamicExtraView(_:)), for: UIControl.Event.touchUpInside);
            
            topButtonWithWrapperView.coreWrapperView.makeCard(topButtonWithWrapperView.coreWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)

            
            mainScrollView.addSubview(topButtonWithWrapperView);
            mainScrollView.contentSize = CGSize(width: screenSize.width, height: heightToUse);
            
        }
        
        if #available(iOS 9.0, *) {
            let stackOnlyView = UIStackOnlyView()
            stackOnlyView.tag = 11;
            stackOnlyView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            stackOnlyView.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-sub,height: 40);
            stackOnlyView.center.x = self.view.center.x;
            stackOnlyView.mainStackView.spacing   = 10.0
            x  = 0;
            y = heightToUse;
            width = screenSize.width-sub;
            heightToUse += 40;
            mainScrollView.addSubview(stackOnlyView);
            mainScrollView.contentSize = CGSize(width: screenSize.width, height: heightToUse);
        } else {
            // Fallback on earlier versions
        };
      
        
        switch(varToDecideView2Show)
        {
            case "field":
                self.renderDynamicType1View();
                break;
            case "area":
                self.renderDynamicType1View();
                break;
            case "file":
                self.renderDynamicType2View();
                break;
            case "date":
                self.renderDynamicType3View();
                break;
            case "date_time":
                self.renderDynamicType3View();
                break;
            case "time":
                self.renderDynamicType3View();
                break;
            default:
                if(varToDecideView2Show == "drop_down" || varToDecideView2Show == "radio" || varToDecideView2Show == "checkbox" || varToDecideView2Show == "multiple")
                {
                    self.showPreviousDropDownSectionData();
                }
                break;
        }
    }
    
    @objc func renderDynamicType1View()
    {
        //print("renderDynamicType1View");
        bundleItemViewCounter += 1;
        if #available(iOS 9.0, *) {
            if let stackOnlyView = self.view.viewWithTag(11) as? UIStackOnlyView
            {
                //print("renderDynamicType1View");
                
                var priceValue = "";
                var priceTypeButton = "";
                var skuValue = "";
                var maxCharValue = "";
                
                if(self.dataToSendArray["price"] != nil)
                {
                    priceValue = self.dataToSendArray["price"]!;
                }
                if(self.dataToSendArray["price_type"] != nil)
                {
                    priceTypeButton = priceTypeDropdownArray.someKey(forValue: self.dataToSendArray["price_type"] ?? "") ?? ""
                }
                if(self.dataToSendArray["sku"] != nil)
                {
                    skuValue = self.dataToSendArray["sku"]!;
                }
                if(self.dataToSendArray["max_characters"] != nil)
                {
                    maxCharValue = self.dataToSendArray["max_characters"]!;
                }
                
                
                let dynamictype1View = Dynamictype1View();
                dynamictype1View.tag = bundleItemViewTagCounter;
                
                dynamictype1View.priceLabel.text = "Price";
                dynamictype1View.priceValue.text = priceValue;
                
                dynamictype1View.priceTypeLabel.text = "Price Type";
                dynamictype1View.priceTypeButton.setTitle(priceTypeButton, for: UIControl.State());
                dynamictype1View.priceTypeButton.addTarget(self, action: #selector(OptionAdditionPageController.priceTypeDropdown(_:)), for: UIControl.Event.touchUpInside);
                
                dynamictype1View.skuLabel.text = "SKU";
                dynamictype1View.skuValue.text = skuValue;
                
                dynamictype1View.maxCharLabel.text = "Max Character";
                dynamictype1View.maxCharValue.text = maxCharValue;
                
                dynamictype1View.coreWrapperView.makeCard(dynamictype1View.coreWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                
                bundleItemViewTagCounter += 1;
                stackOnlyView.mainStackView.addArrangedSubview(dynamictype1View);
                stackOnlyView.frame = CGRect(x: x, y: y, width: width, height: dynamicHeight1*CGFloat(bundleItemViewCounter));
                
                mainScrollView.contentSize = CGSize(width: screenSize.width, height: dynamicHeight1*CGFloat(bundleItemViewCounter));
                
            }
        } else {
            // Fallback on earlier versions
        }

    }
    
    @objc func renderDynamicType2View()
    {
        bundleItemViewCounter += 1;
        if #available(iOS 9.0, *) {
            if let stackOnlyView = self.view.viewWithTag(11) as? UIStackOnlyView
            {
                var priceValue = "";
                var priceTypeButton = "";
                var skuValue = "";
                var allowedFileTypeValue = "";
                var maxImgSizeValue1 = "";
                var maxImgSizeValue2 = "";
                
                if(self.dataToSendArray["price"] != nil)
                {
                    priceValue = self.dataToSendArray["price"]!;
                }
                if(self.dataToSendArray["price_type"] != nil)
                {
                    priceTypeButton = priceTypeDropdownArray.someKey(forValue: self.dataToSendArray["price_type"] ?? "") ?? ""
                }
                if(self.dataToSendArray["sku"] != nil)
                {
                    skuValue = self.dataToSendArray["sku"]!;
                }
                if(self.dataToSendArray["file_extension"] != nil)
                {
                    allowedFileTypeValue = self.dataToSendArray["file_extension"]!;
                }
                if(self.dataToSendArray["image_size_x"] != nil)
                {
                    maxImgSizeValue1 = self.dataToSendArray["image_size_x"]!;
                }
                if(self.dataToSendArray["image_size_y"] != nil)
                {
                    maxImgSizeValue2 = self.dataToSendArray["image_size_y"]!;
                }
                
                let dynamictype1View = DynamicType2View();
                dynamictype1View.tag = bundleItemViewTagCounter;
                
                dynamictype1View.priceLabel.text = "Price";
                dynamictype1View.priceValue.text = priceValue;
                
                dynamictype1View.priceTypeLabel.text = "Price Type";
                dynamictype1View.priceTypeButton.setTitle(priceTypeButton, for: UIControl.State());
                dynamictype1View.priceTypeButton.addTarget(self, action: #selector(OptionAdditionPageController.priceTypeDropdown(_:)), for: UIControl.Event.touchUpInside);
                
                dynamictype1View.skuLabel.text = "SKU";
                dynamictype1View.skuValue.text = skuValue;
                
                dynamictype1View.allowedFileTypeLabel.text = "Allow File Type";
                dynamictype1View.allowedFileTypeValue.text = allowedFileTypeValue;
                
                dynamictype1View.maxImgSizeLabel.text = "Max Image Size";
                dynamictype1View.maxImgSizeValue1.text = maxImgSizeValue1;
                dynamictype1View.maxImgSizeValue2.text = maxImgSizeValue2;
                //dynamictype1View.maxImgSizeValue1.backgroundColor = UIColor.whiteColor();
                //dynamictype1View.maxImgSizeValue2.backgroundColor = UIColor.whiteColor();
                
                dynamictype1View.coreWrapperView.makeCard(dynamictype1View.coreWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                
                bundleItemViewTagCounter += 1;
                stackOnlyView.mainStackView.addArrangedSubview(dynamictype1View);
                stackOnlyView.frame = CGRect(x: x, y: y, width: width, height: dynamicHeight2*CGFloat(bundleItemViewCounter));
                
                mainScrollView.contentSize = CGSize(width: screenSize.width, height: dynamicHeight1*CGFloat(bundleItemViewCounter));
            }
        } else {
            // Fallback on earlier versions
        }
        
    }
    
    
    @objc func renderDynamicType3View()
    {
        bundleItemViewCounter += 1;
        if #available(iOS 9.0, *) {
            if let stackOnlyView = self.view.viewWithTag(11) as? UIStackOnlyView
            {
                
                var priceValue = "";
                var priceTypeButton = "";
                var skuValue = "";
                
                if(self.dataToSendArray["price"] != nil)
                {
                    priceValue = self.dataToSendArray["price"]!;
                }
                if(self.dataToSendArray["price_type"] != nil)
                {
                    priceTypeButton = priceTypeDropdownArray.someKey(forValue: self.dataToSendArray["price_type"] ?? "") ?? ""
                }
                if(self.dataToSendArray["sku"] != nil)
                {
                    skuValue = self.dataToSendArray["sku"]!;
                }
                
                
                let dynamictype1View = DynamicType3View();
                dynamictype1View.tag = bundleItemViewTagCounter;
                
                dynamictype1View.priceLabel.text = "Price";
                dynamictype1View.priceValue.text = priceValue;
                
                dynamictype1View.priceTypeLabel.text = "Price Type";
                dynamictype1View.priceTypeButton.setTitle(priceTypeButton, for: UIControl.State());
                dynamictype1View.priceTypeButton.addTarget(self, action: #selector(OptionAdditionPageController.priceTypeDropdown(_:)), for: UIControl.Event.touchUpInside);
                
                dynamictype1View.skuLabel.text = "SKU";
                dynamictype1View.skuValue.text = skuValue;
                
                
                dynamictype1View.coreWrapperView.makeCard(dynamictype1View.coreWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                
                
                bundleItemViewTagCounter += 1;
                stackOnlyView.mainStackView.addArrangedSubview(dynamictype1View);
                stackOnlyView.frame = CGRect(x: x, y: y, width: width, height: dynamicHeight3*CGFloat(bundleItemViewCounter));
                
                mainScrollView.contentSize = CGSize(width: screenSize.width, height: dynamicHeight1*CGFloat(bundleItemViewCounter));
            }
        } else {
            // Fallback on earlier versions
        }
        
    }
    
    
    @objc func addDynamicExtraView(_ sender:UIButton)
    {
        //print("addDynamicExtraView");
        bundleItemViewCounter += 1;
        if #available(iOS 9.0, *) {
            if let stackOnlyView = self.view.viewWithTag(11) as? UIStackOnlyView
            {
                let dynamictype1View = DynamicExtraView();
                dynamictype1View.tag = bundleItemViewTagCounter;
                
                
                dynamictype1View.deleteButton.setTitle("Delete", for: UIControl.State());
                dynamictype1View.deleteButton.tag = bundleItemViewTagCounter;
                dynamictype1View.deleteButton.addTarget(self, action: #selector(OptionAdditionPageController.removeDynamicExtraView(_:)), for: UIControl.Event.touchUpInside);
                
                dynamictype1View.titleLabel.text = "Title";
                dynamictype1View.titleValue.text = "";
                
                dynamictype1View.priceLabel.text = "Price";
                dynamictype1View.priceValue.text = "";
                
                dynamictype1View.priceTypeLabel.text = "Price Type";
                dynamictype1View.priceTypeButton.setTitle("", for: UIControl.State());
                dynamictype1View.priceTypeButton.addTarget(self, action: #selector(OptionAdditionPageController.priceTypeDropdown(_:)), for: UIControl.Event.touchUpInside);
                
                dynamictype1View.skuLabel.text = "SKU";
                dynamictype1View.skuValue.text = "";
                
                dynamictype1View.sortOrderLabel.text = "Sort Order";
                dynamictype1View.sortOrderValue.text = "";
                
                
                dynamictype1View.coreWrapperView.makeCard(dynamictype1View.coreWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                
                
                bundleItemViewTagCounter += 1;
                
                stackOnlyView.mainStackView.addArrangedSubview(dynamictype1View);
                stackOnlyView.frame = CGRect(x: x, y: y, width: width, height: extraViewHeight*CGFloat(bundleItemViewCounter));
                
                mainScrollView.contentSize = CGSize(width: screenSize.width, height: extraViewHeight*CGFloat(bundleItemViewCounter)+40);
            }
        } else {
            // Fallback on earlier versions
        }
    }
    
    @objc func addDynamicExtraViewDirectCall(_ val:[String:String])
    {
        //print("addDynamicExtraViewDirectCall");
        bundleItemViewCounter += 1;
        if #available(iOS 9.0, *) {
            if let stackOnlyView = self.view.viewWithTag(11) as? UIStackOnlyView
            {
                let titleValue = val["title"];
                let priceValue = val["price"];
                let priceTypeButton = priceTypeDropdownArray.someKey(forValue: val["price_type"] ?? "") ?? ""
                let skuValue = val["sku"];
                let sortOrderValue = val["sort_order"];
                
                let dynamictype1View = DynamicExtraView();
                dynamictype1View.tag = bundleItemViewTagCounter;
                dynamictype1View.customOptionPrevData = val
                if(self.optionIdsArrayTracking[bundleItemViewTagCounter] == nil)
                {
                    self.optionIdsArrayTracking[bundleItemViewTagCounter] = String();
                }
                self.optionIdsArrayTracking[bundleItemViewTagCounter]! = (val["option_type_id"]!);//array for tracking
                
                
                dynamictype1View.deleteButton.setTitle("Delete", for: UIControl.State());
                dynamictype1View.deleteButton.tag = bundleItemViewTagCounter;
                dynamictype1View.deleteButton.addTarget(self, action: #selector(OptionAdditionPageController.removeDynamicExtraView(_:)), for: UIControl.Event.touchUpInside);
                
                dynamictype1View.titleLabel.text = "Title";
                dynamictype1View.titleValue.text = titleValue;
                
                dynamictype1View.priceLabel.text = "Price";
                dynamictype1View.priceValue.text = priceValue;
                
                dynamictype1View.priceTypeLabel.text = "Price Type";
                dynamictype1View.priceTypeButton.setTitle(priceTypeButton, for: UIControl.State());
                dynamictype1View.priceTypeButton.addTarget(self, action: #selector(OptionAdditionPageController.priceTypeDropdown(_:)), for: UIControl.Event.touchUpInside);
                
                dynamictype1View.skuLabel.text = "SKU";
                dynamictype1View.skuValue.text = skuValue;
                
                dynamictype1View.sortOrderLabel.text = "Sort Order";
                dynamictype1View.sortOrderValue.text = sortOrderValue;
                
                bundleItemViewTagCounter += 1;
                
                dynamictype1View.coreWrapperView.makeCard(dynamictype1View.coreWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                
                stackOnlyView.mainStackView.addArrangedSubview(dynamictype1View);
                stackOnlyView.frame = CGRect(x: x, y: y, width: width, height: extraViewHeight*CGFloat(bundleItemViewCounter));
                
                mainScrollView.contentSize = CGSize(width: screenSize.width, height: extraViewHeight*CGFloat(bundleItemViewCounter)+40);
            }
        } else {
            // Fallback on earlier versions
        }
    }
    
    @objc func removeDynamicExtraView(_ sender:UIButton)
    {
        //print("removeDynamicExtraView");
        //print(sender.tag);
        if let dynamicExtraView = self.view.viewWithTag(sender.tag) as? DynamicExtraView
        {
            //print("removeDynamicExtraView11111");
            if(isEditCase)
            {
                print("IMP");
                print(self.optionIdsArrayTracking);
               
                if let idNeeded = self.optionIdsArrayTracking[sender.tag]{
                    deletedoptiontypes.append(idNeeded);
                }
                //self.deletedoptiontypes.append(idNeeded);
                print("deletedoptiontypes.append(idNeeded);");
                print(deletedoptiontypes);
            }
            
            
            bundleItemViewCounter -= 1;
            dynamicExtraView.removeFromSuperview();
            refactorDynamicExtraViewSection();
        }
    }
    @objc func refactorDynamicExtraViewSection()
    {
        if #available(iOS 9.0, *) {
            if let stackOnlyView = self.view.viewWithTag(11) as? UIStackOnlyView
            {
                stackOnlyView.frame = CGRect(x: x, y: y, width: width, height: extraViewHeight*CGFloat(bundleItemViewCounter));
                
                mainScrollView.contentSize = CGSize(width: screenSize.width, height: extraViewHeight*CGFloat(bundleItemViewCounter)+40);
            }
        } else {
            // Fallback on earlier versions
        }
    }
    
    /*** data fetching @objc functions ***/
    @objc func fetchDynamicType1View()
    {
        //print("fetchDynamicType1View");
        let tagToUse = bundleItemViewTagCounter-1;
        if let dynamicTypeView = self.view.viewWithTag(tagToUse) as? Dynamictype1View
        {
            if(dynamicTypeView.priceTypeButton.titleLabel?.text == nil)
            {
                errorOccured = true;
                return;
            }
            
            if(dynamicTypeView.priceValue.text == nil || dynamicTypeView.priceValue.text == "")
            {
                errorOccured = true;
                return;
            }
            
            if(dynamicTypeView.skuValue.text == nil || dynamicTypeView.skuValue.text == "")
            {
                errorOccured = true;
                return;
            }
            
            if(dynamicTypeView.maxCharValue.text == nil || dynamicTypeView.maxCharValue.text == "")
            {
                errorOccured = true;
                return;
            }
            
            
            let priceValue = dynamicTypeView.priceValue.text;
            let priceTypeButton = priceTypeDropdownArray[(dynamicTypeView.priceTypeButton.currentTitle ?? "")] ?? ""
            let skuValue = dynamicTypeView.skuValue.text!;
            let maxCharValue = dynamicTypeView.maxCharValue.text!;
            self.dataToSendArray["price"] = priceValue;
            self.dataToSendArray["price_type"] = priceTypeButton
            self.dataToSendArray["sku"] = skuValue;
            self.dataToSendArray["max_characters"] = maxCharValue;
            
        }
    }
    
    @objc func fetchDynamicType2View()
    {
        //print("fetchDynamicType2View");
        let tagToUse = bundleItemViewTagCounter-1;
        if let dynamicTypeView = self.view.viewWithTag(tagToUse) as? DynamicType2View
        {
            let priceValue = dynamicTypeView.priceValue.text;
            
            if(dynamicTypeView.priceTypeButton.titleLabel?.text == nil)
            {
                errorOccured = true;
                return;
            }
            
            let priceTypeButton = priceTypeDropdownArray[(dynamicTypeView.priceTypeButton.currentTitle ?? "")] ?? ""
            let skuValue = dynamicTypeView.skuValue.text!;
            let allowedFileTypeValue = dynamicTypeView.allowedFileTypeValue.text!;
            let maxImgSizeValue1 = dynamicTypeView.maxImgSizeValue1.text!;
            let maxImgSizeValue2 = dynamicTypeView.maxImgSizeValue2.text!;
            
            self.dataToSendArray["price"] = priceValue;
            self.dataToSendArray["price_type"] = priceTypeButton;
            self.dataToSendArray["sku"] = skuValue;
            self.dataToSendArray["file_extension"] = allowedFileTypeValue;
            self.dataToSendArray["image_size_x"] = maxImgSizeValue1;
            self.dataToSendArray["image_size_y"] = maxImgSizeValue2;
        }
    }

    
    
    
    @objc func fetchDynamicType3View()
    {
        //print("fetchDynamicType1View");
        let tagToUse = bundleItemViewTagCounter-1;
        if let dynamicTypeView = self.view.viewWithTag(tagToUse) as? DynamicType3View
        {
            let priceValue = dynamicTypeView.priceValue.text;
            
            if(dynamicTypeView.priceTypeButton.titleLabel?.text == nil)
            {
                errorOccured = true;
                return;
            }
            
            let priceTypeButton = priceTypeDropdownArray[(dynamicTypeView.priceTypeButton.currentTitle ?? "")] ?? ""
            let skuValue = dynamicTypeView.skuValue.text!;
            
            self.dataToSendArray["price"] = priceValue;
            self.dataToSendArray["price_type"] = priceTypeButton;
            self.dataToSendArray["sku"] = skuValue;
        }
    }
    
    @objc func fetchAllAddedOptionValue()
    {
        if(varToDecideView2Show == "drop_down" || varToDecideView2Show == "radio" || varToDecideView2Show == "checkbox" || varToDecideView2Show == "multiple")
        {
            print("Value to send to main page :: Initially");
            print(self.dataToSendArrayDropDownCase);
            
            //print("fetchAllAddedOptionValue");
            let lastTagToUse = bundleItemViewTagCounter-1;
            print("lastTagToUse");
            print(lastTagToUse);
            var tagToUse = 20;
            var counter = -1;

            for tagToUse in 20..<lastTagToUse+1
            {
                if let dynamicTypeView = self.view.viewWithTag(tagToUse) as? DynamicExtraView
                {
                    counter += 1;
                    var temp = [String:String]();
                
                    let titleValue = dynamicTypeView.titleValue.text;
                    let priceValue = dynamicTypeView.priceValue.text;
                    var priceTypeButton = "";
                    if((dynamicTypeView.priceTypeButton.titleLabel?.text) != nil)
                    {
                        priceTypeButton = priceTypeDropdownArray[(dynamicTypeView.priceTypeButton.currentTitle ?? "")] ?? ""
                    }
                    let skuValue = dynamicTypeView.skuValue.text!;
                    let sortOrderValue = dynamicTypeView.sortOrderValue.text!;
                    
                    let customOptnDataForThatView = dynamicTypeView.customOptionPrevData
                    
                    if customOptnDataForThatView["option_type_id"] == nil || customOptnDataForThatView["option_type_id"] == ""{
                        temp["option_type_id"] = "";//have to change later
                    }else{
                        print("Already Exists");
                        print(self.dataToSendArrayDropDownCase[String(counter)]);
                        temp["option_type_id"] = customOptnDataForThatView["option_type_id"] ?? ""
                    }
                    
//                    if(self.dataToSendArrayDropDownCase[String(counter)] != nil)
//                    {
//                        print("Already Exists");
//                        print(self.dataToSendArrayDropDownCase[String(counter)]);
//                        temp["option_type_id"] = self.dataToSendArrayDropDownCase[String(counter)]!["option_type_id"];//have to change later
//                    }
//                    else
//                    {
//                        temp["option_type_id"] = "";//have to change later
//                    }
                    
                    temp["title"] = titleValue;
                    temp["price"] = priceValue;
                    temp["price_type"] = priceTypeButton
                    temp["sku"] = skuValue;
                    temp["sort_order"] = sortOrderValue;
                    if((temp["is_delete"]) == nil)
                    {
                        temp["is_delete"] = "";
                    }
                    self.dataToSendArrayDropDownCase[String(counter)] = temp;
                    dynamicTypeView.customOptionPrevData = temp
                }
                
            }
            
            if counter == -1{
                hasOptionError = true
            }else{
                hasOptionError = false
            }
            
            print("Value to send to main page");
            print(self.dataToSendArrayDropDownCase);
            
        }
    }
    
    
    @objc func showPreviousDropDownSectionData()
    {
        //print("showPreviousDropDownSectionData#12345");
        //print(dataToSendArrayDropDownCase);
        print("Data Receivedd on page 2");
        print(dataToSendArrayDropDownCase);
        
        for (_,val) in dataToSendArrayDropDownCase
        {
            ////print(val);
            print("val[\"option_type_id\"]");
            print(val["option_type_id"]);
            if let _ = deletedoptiontypes.index(of: val["option_type_id"]!) {
            }
            else
            {
                self.addDynamicExtraViewDirectCall(val);
            }
        }
        
    }


    @objc func priceTypeDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = Array(priceTypeDropdownArray.keys);
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
 }


