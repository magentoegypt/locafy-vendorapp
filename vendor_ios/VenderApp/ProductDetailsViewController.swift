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
import RATreeView
import Combine

class ProductDetailsViewController: ced_VendorBaseClass,UIImagePickerControllerDelegate,UINavigationControllerDelegate,UITextFieldDelegate {
    
    
    @IBOutlet weak var topWrapperView: UIView!
    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var proceedButton: UIButton!
    @IBOutlet weak var mainScrollView: UIScrollView!
    var weight:UITextField?
    
    var treeView:RATreeView!
    var skuToUseUpload = ""
    var datePickerView = UIDatePicker();
    let screenSize: CGRect = UIScreen.main.bounds;
    var baseURL = settings.baseUrl
    var heightToUse : CGFloat = 0;
    var globalTextFieldTag = 0;
    var imageCounter:Int = 0
    
    var previousImagesTag = [Int]();
    var previousImages = [Int:[String:String]]();
    var config = [[String:String]]();
    var categoryJson: JSON!
    var productJsonArr: [JSON] = [JSON]()
    var product_brand = [Int]();
    var multiSelect = [Int:[Dictionary<String,String>]]()
    var selectedMultiSelect = [Int:[String]]()
    var isHaveToProceed = false
    var is_default_check:Bool = false
    var is_API_Call:Bool = false
    var fieldsArray = [Int:Dictionary<String,Any>]();
    //product edit case
    var isEditCase = false;
    var media_image:JSON!;
    var productBasicData = [String:String]();
    var productSelectedCategories = [Int]();
    var newImagesTag = [Int]();
    var productSelectedWebsites = [Int]();
    var categoryData : [DataObject] = []
    //product edit case
    //added
    var defautImageTag = 0;
    var store_id:String = "0"
    var product_id = "";// most imp :: product ID that we will get after sending details to server, when a new product is created
    var attribute_set_id = "";
    var proDetailArray = [[String:String]]();
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
    var subcategoriesData = [String: [String]]();
    var selectedstock = "";
    var selectedtaxes = "";
    var requiredCheck = [String:String]()
    var associated_product_idsList:[String] = [String]()
    var localDynamic = [String:String]();
    var dynamicDataInfo = [String:[String:String]]();
    var mainValuesArray = [String:[String:String]]();
    var namesArray = [String]();
    var attribute_IdArray:[String] = [String]()
    var attribute_codeArray:[String] = [String]()
    var config_selected_attr:[String] = [String]()
    var valueToSendArray = [String:String]();//for dropdown
    var valuesToSendMultiselect = [String:[String]]();// for multiselect
    var addonTag = 500;//tag to be used for addon views
    var dropDownArray = [Int: Dictionary<String,String>]()
    var tagToBeUsedForDatePicker = 0;
    var attributeModelListSelected:[AttributeModelConfig] = [AttributeModelConfig]()
    var attributeModelListFinal:[AttributeModelConfig] = [AttributeModelConfig]()
    var attributeModelEditListFinal:[AttributeModelConfig] = [AttributeModelConfig]()
    let configView = LabelButtonView()
    //let colorString = mobi_common.getInfoPlist("theme_color") as! String;
    var cancellables = Set<AnyCancellable>()
    
    var heightOfImageSection : CGFloat = 0;
    let heightOfImageView : CGFloat = 100;
    let fixedHeight : CGFloat = 70;
    let fixedlongHeight : CGFloat = 120;
    let buttonHeight : CGFloat = 40;
    
    var padding : CGFloat = 5;
    var selectedFiltersIdsString = "";
    var selectedWebsitesIdsString = "";
    var oldProductName:String = ""
    var oldProductPrice:String = ""
    var oldProductWieght:String = ""
    
    var tagCounter = 50;//tag to be used for fixed normal views
    let imagePicker = ImagePickerManager()
    var staticAddonViewTag = 20;//tag to be used for static addon normal views for specific product type
    
    var heightToAppend : CGFloat = 0;
    var startHeightForImageView : CGFloat = 0;
    
    var create_prod = false
    var isParse:Bool = false
    
    var x : CGFloat = 0;
    var y : CGFloat = 0;
    var width : CGFloat = 0;
    var imgViewTagCounter = 200;//tag to be used for images upload views
    var imgUploadViewCounter = 0;
    var currentImgTag = 0;
    @IBOutlet weak var mainHeight: NSLayoutConstraint!
    
    @IBOutlet weak var mainStackView: UIStackView!
    @IBOutlet weak var mainWidth: NSLayoutConstraint!
    
    @IBOutlet weak var mainView: UIView!
    var tags = 100;
    var productJson: JSON!;
    
    override func viewDidLoad() {
        super.viewDidLoad()
//        if !isEditCase{
//            saveButton.isHidden = true
//        }else{
            saveButton.isHidden = true
//        }
        
        if isEditCase{
            selectedProductTypeId = productBasicData["type_id"] ?? ""
        }
        // Do any additional setup after loading the view.
        mainHeight.constant = 0;
        mainWidth.constant = self.view.frame.width;
        
        print("-----configSelected-----");
        print(configSelected);
        print("productBasicData \(productBasicData)")
        self.skuToUseUpload = productBasicData["sku"] ?? ""
        ced_navigationBarController().addNavButton(self, str: "no")
        //let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String;
        
        
        proceedButton.setTitle("Save".localized, for: UIControl.State());
        proceedButton.addTarget(self, action: #selector(ProductDetailsViewController.continueClicked(_:)), for: UIControl.Event.touchUpInside);
        proceedButton.backgroundColor = DynamicColor.themeColor
        saveButton.setTitle("Save".localized, for: UIControl.State());
        saveButton.addTarget(self, action: #selector(ProductDetailsViewController.saveClicked(_:)), for: UIControl.Event.touchUpInside);
        saveButton.backgroundColor = DynamicColor.saveButtonColor//UIColor.init(hexString: DynamicColor.saveButtonColor)//setThemeColor();
        if selectedProductTypeId == "configurable" && isEditCase{
            self.loadConfigInfo(selectedProductTypeId, selectedAttributeSetId: selectedAttributeSetId)
        }
        self.loadData();
        self.view.backgroundColor = DynamicColor.ViewBackgroundColor
    }
    
    @objc func loadData()
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        var params = "vendor_id="
        params += userData["vendorId"] as! String;
        if selectedAttributeSetId != ""{
            params += "&set=\(selectedAttributeSetId)"
        }
        if(isEditCase)
        {
            params += "&product_id=\(product_id)";
            params += "&store_id="+store_id;
        }
        else{
            params += "&type=\(selectedProductTypeId)";
            if(selectedFiltersIdsString != "")
            {
                let selectedFilters = selectedFiltersIdsString.components(separatedBy: ",");
                let lastCategoryId = selectedFilters.last!.replacingOccurrences(of: "]", with: "").replacingOccurrences(of: "[", with: "").replacingOccurrences(of: "\"", with: "")
                params += "&last_category="+lastCategoryId;
            }
        }
        Alert_File.addLoadingIndicator(self, msg: "Please Wait...".localized)
        self.sendRequest(url: "vproductapi/vproducts/productform", parameters: params)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print("---json---\(json)")
            self.productJson = json[0];
            self.parseData();
            self.isParse = true
            if(self.attributeModelListFinal.count > 0){
                self.setupAttributeView()
            }
        }
    }
    
    var configView_heightConstrainbox:NSLayoutConstraint?
    @objc func setConfiguration() {
        makeLabel(labelText: "Configurations".localized)
        
        configView.label.text = "ConfigLabel".localized
        configView.button.setTitle("Create Configurations", for: .normal)
        configView.button.add(for:.touchUpInside) {
            let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "configAttributeSelectionViewController") as! ConfigAttributeSelectionViewController;
            viewcontrollor.config = self.config;
            viewcontrollor.selectedProductTypeId = self.selectedProductTypeId;
            viewcontrollor.selectedAttributeSetId = self.selectedAttributeSetId;
            viewcontrollor.stock = self.stock;
            viewcontrollor.taxes = self.taxes;
            viewcontrollor.tabs = self.tabs;
            viewcontrollor.storeViews = self.storeViews;
            viewcontrollor.websites = self.websites;
            viewcontrollor.categoriesList = self.categoriesList;
            viewcontrollor.attributes = self.attributes
            viewcontrollor.categoryJson = self.categoryJson
            viewcontrollor.attributeModelEditListFinal = self.attributeModelEditListFinal
            viewcontrollor.attributeModelListSelected = self.attributeModelListSelected
            viewcontrollor.handler = { attributeModelListSelected,attributeModelListFinal in
                let totalheight = CGFloat(self.attributeModelListFinal.count * 450)
                self.mainHeight.constant -= totalheight
                self.configView_heightConstrainbox?.constant -= totalheight
                self.attributeModelListSelected = attributeModelListSelected
                self.attribute_IdArray.removeAll()
                self.attribute_codeArray.removeAll()
                self.attributeModelListSelected.forEach { attributeModelConfig in
                    self.attribute_IdArray.append(attributeModelConfig.attribute_Id ?? "")
                    self.attribute_codeArray.append(attributeModelConfig.attribute_code ?? "")
                }
                self.attributeModelListFinal = attributeModelListFinal
                self.setupAttributeView()
            }
            self.navigationController?.pushViewController(viewcontrollor, animated: true)
        }
       // customView.translatesAutoresizingMaskIntoConstraints = false
        
        configView_heightConstrainbox = NSLayoutConstraint(item: configView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 80)
        configView.addConstraint(configView_heightConstrainbox!)
        mainHeight.constant += 80
        mainStackView.addArrangedSubview(configView)
    }
    
    func setupAttributeView(){
        var totalheight:CGFloat = 0
        configView.stackView.removeAllArrangedSubviews()
        for index in 0..<attributeModelListFinal.count
        {
            let val = self.attributeModelListFinal[index]
            let configSummaryView: ConfigSummaryView = ConfigSummaryView(frame: CGRect(x: 0, y: 0, width: screenSize.width,height: 450))
//                configSummaryView.layer.cornerRadius = 10
//                configSummaryView.layer.borderWidth = 1
//                configSummaryView.layer.borderColor = UIColor.lightGray.cgColor
            if((self.attributeModelListFinal[index].weight ?? "").isEmpty){
                self.attributeModelListFinal[index].weight = self.weight?.text
            }
            configSummaryView.skuTxtLabel.isHidden = true
            configSummaryView.nameTxtField.text = val.namelabel ?? ""
            configSummaryView.skuTxtField.text = val.skulabel ?? ""
            configSummaryView.attributeLabel.text = val.title ?? ""
            configSummaryView.attributeLabel.numberOfLines = 2
            configSummaryView.weightTxtField.text = self.attributeModelListFinal[index].weight
            configSummaryView.quantityTxtField.text = val.quantity ?? ""
            configSummaryView.priceTxtField.text = val.price ?? ""
            configSummaryView.statusLabel.text = val.isEnable ? "Enabled".localized:"Disabled".localized
            configSummaryView.selectButton.add(for: UIControlEvents.touchUpInside) {
                self.showAlertWithCallback(on: self,title: "",message: "", isEnable: val.isEnable) { caseString in
                    if(caseString == "remove"){
                        self.attributeModelListFinal.remove(at: index)
                        configSummaryView.removeFromSuperview()
                        self.mainHeight.constant -= 450
                        self.configView_heightConstrainbox?.constant -= 450
                    }else{
                        self.attributeModelListFinal[index].isEnable = !self.attributeModelListFinal[index].isEnable
                        configSummaryView.statusLabel.text = val.isEnable ? "Enabled".localized:"Disabled".localized
                    }
                }
            }
            configSummaryView.photobtn.add(for: .touchUpInside) {
                self.imagePicker.presentImagePicker(from: self, sourceType: .photoLibrary) { image in
                            if let image = image {
                                configSummaryView.imageView.image = image
                                self.attributeModelListFinal[index].image = image
                            } else {
                                print("No image selected")
                            }
                        }
            }
            if(val.image != nil){
                configSummaryView.imageView.image = val.image
            }else if(val.imageUrl != nil){
                configSummaryView.imageView.sd_setImage(with: URL(string: val.imageUrl ?? ""), placeholderImage: UIImage(named: "placeholder"))
            }
            NotificationCenter.default.publisher(for: UITextField.textDidChangeNotification, object: configSummaryView.nameTxtField)
                       .compactMap { ($0.object as? UITextField)?.text }
                       .sink { text in
                           self.attributeModelListFinal[index].namelabel = text
                       }
                       .store(in: &cancellables)
            NotificationCenter.default.publisher(for: UITextField.textDidChangeNotification, object: configSummaryView.skuTxtField)
                       .compactMap { ($0.object as? UITextField)?.text }
                       .sink { text in
                           self.attributeModelListFinal[index].skulabel = text
                       }
                       .store(in: &cancellables)
            NotificationCenter.default.publisher(for: UITextField.textDidChangeNotification, object: configSummaryView.weightTxtField)
                       .compactMap { ($0.object as? UITextField)?.text }
                       .sink { text in
                           self.attributeModelListFinal[index].weight = text
                       }
                       .store(in: &cancellables)
            NotificationCenter.default.publisher(for: UITextField.textDidChangeNotification, object: configSummaryView.quantityTxtField)
                       .compactMap { ($0.object as? UITextField)?.text }
                       .sink { text in
                           self.attributeModelListFinal[index].quantity = text
                       }
                       .store(in: &cancellables)
            NotificationCenter.default.publisher(for: UITextField.textDidChangeNotification, object: configSummaryView.priceTxtField)
                       .compactMap { ($0.object as? UITextField)?.text }
                       .sink { text in
                           self.attributeModelListFinal[index].price = text
                       }
                       .store(in: &cancellables)
            
            let width = NSLayoutConstraint(item: configSummaryView, attribute: .width, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: screenSize.width)
            configSummaryView.addConstraint(width)
            configSummaryView.layoutSubviews()
            configSummaryView.layoutIfNeeded()
            totalheight += configSummaryView.bounds.height
            configView.stackView.addArrangedSubview(configSummaryView)
        }
        configView_heightConstrainbox?.constant += totalheight
        mainHeight.constant += totalheight
    }
    
    @objc func setupTreeView() {
        makeLabel(labelText: "Select Categories".localized)
        var dataSource=[DataObject]()
        let maincatename="Default Category".localized
        let maincateimage="unchecked"
        let maincateId="10000000"
        let headerView = HeaderView()
        headerView.topLabel.text = "   " + "Default Category".localized
        headerView.topLabel.textColor = .black
        if(ced_storeVC.selectLangauge == "ar"){
            headerView.topLabel.textAlignment = .right
        }else{
            headerView.topLabel.textAlignment = .left
        }
        headerView.topLabel.font = UIFont.systemFont(ofSize: 17)
        let heightConstrainbox = NSLayoutConstraint(item: headerView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 25)
        headerView.addConstraint(heightConstrainbox)
        mainHeight.constant += 25
        mainStackView.addArrangedSubview(headerView)
        
        var subDataSource=[DataObject]()
        subDataSource=addDataToDataSource(value: categoryJson, cateName: maincatename, cateId: maincateId, cateImage: maincateimage)
        dataSource=subDataSource
        self.categoryData=dataSource
        treeView = RATreeView(frame: CGRect(x: 0, y: 0, width: self.view.frame.width, height: 300))
        treeView.register(UINib(nibName: String(describing: CategoryTreeTableViewCell.self), bundle: nil), forCellReuseIdentifier: String(describing: CategoryTreeTableViewCell.self))
        treeView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        treeView.delegate = self;
        treeView.dataSource = self;
        treeView.treeFooterView = UIView()
        treeView.backgroundColor = .clear
        self.mainStackView.addArrangedSubview(treeView)
        treeView.translatesAutoresizingMaskIntoConstraints = false
        let heightConstrain = NSLayoutConstraint(item: treeView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 300)
        treeView.addConstraint(heightConstrain)
        mainHeight.constant += 300;
        if(categoryData.count>0){
            treeView.expandRow(forItem: categoryData[0])
        }
    }
    
    func addDataToDataSource(value:JSON, cateName: String, cateId: String, cateImage: String)->[DataObject]{
        
        var cellDataSource=[DataObject]();
        let data=DataObject(name: cateName,id: cateId,image:cateImage)
       // cellDataSource.append(data)
        for index in value.arrayValue
        {
            
            let mainName = index["main_category"].stringValue.components(separatedBy: "#").filter { !$0.isEmpty }
            let cateName=mainName[1]
            let cateImage="unchecked"
            let cateId=mainName[0]
            var hasChildd="false"
            if(index["sub_categories"].exists())
            {
                hasChildd="true"
            }
            if hasChildd=="true"{
                let dataSource=addDataToDataSource(value: index["sub_categories"], cateName: cateName, cateId: cateId, cateImage: cateImage)
                
                let data=DataObject(name: cateName, children: dataSource ,id: cateId,image:cateImage)
                cellDataSource.append(data)
            }
            else
            {
                let data=DataObject(name: cateName,id: cateId,image:cateImage)
                cellDataSource.append(data)
            }
        }
        return cellDataSource
        
    }
    
    func setUpWebsite(){
        makeLabel(labelText: "Select Websites".localized)
        for (key,val) in websites
        {
            /** Checkbox Button **/
            let checkboxButtonView = SubCategoryView();
            checkboxButtonView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            checkboxButtonView.frame = CGRect(x: 0, y: 0, width: screenSize.width-20,height: 30)
            if let _ = productSelectedWebsites.index(of: Int(val)!) {
                checkboxButtonView.childFilterButton.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
            }
            else
            {
                checkboxButtonView.childFilterButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
            }
            checkboxButtonView.childFilterButton.setTitle(key, for: UIControl.State());
            checkboxButtonView.childFilterButton.tag = Int(val)!;
            checkboxButtonView.childFilterButton.addTarget(self, action: #selector(selectWebsites(_:)), for: UIControl.Event.touchUpInside);
            self.mainStackView.addArrangedSubview(checkboxButtonView)
            checkboxButtonView.translatesAutoresizingMaskIntoConstraints = false
            let heightConstrain = NSLayoutConstraint(item: checkboxButtonView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 30)
            checkboxButtonView.addConstraint(heightConstrain)
            mainHeight.constant += 30
        }
    }
    
    @objc func selectWebsites(_ sender:UIButton)
    {
        let value = self.websites[(sender.titleLabel?.text)!];
        if(sender.imageView!.image == UIImage(named:"unchecked_checkbox"))
        {
            sender.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
            self.productSelectedWebsites.append(Int(value!)!);
        }
        else
        {
            sender.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
            if let indexToRemove = productSelectedWebsites.index(of: Int(value!)!) {
                self.productSelectedWebsites.remove(at: indexToRemove);
            }
        }
        print(productSelectedWebsites);
    }
    
    
    
    func parseData()
    {
        let generalKeys = ["#General","#عام"]
       // let attributeSetKeys = ["#Attribute Set","#أباجورات و لمباديرات"]
        let advancedPricingKeys = ["#Advanced Pricing","#التسعير المتقدم"]
        let contentKeys = ["#Content","#المحتوى"]
        let seoKeys = ["#Search Engine Optimization","#تحسين محرك البحث"]
        let eliminateKeys = ["#Subscriptions by Stripe","#Facebook Attribute Group","#Subscriptions by APS"]
        generalKeys.forEach { valuestr in
            popuLateForm(valuestr, value: productJson[valuestr].arrayValue)
        }
//        attributeSetKeys.forEach { valuestr in
//            popuLateForm(valuestr, value: productJson[valuestr].arrayValue)
//        }
        if let productJson = productJson
        {
            //makeCategoryView(labelText: productJson["category_tree"].stringValue)
          //  let arrKeysEn = ["#General","#Attribute Set","#Advanced Pricing","#Content","#Search Engine Optimization"]
         //   let arrKeysAr = ["#عام","","#Advances pricing","#Content","#التسعير المتقدم#","","#المحتوى#","","تحسين محرك البحث"]
            for(key,value) in productJson
            {
                if(generalKeys.contains(key) || advancedPricingKeys.contains(key) || contentKeys.contains(key) || seoKeys.contains(key) || eliminateKeys.contains(key)){
                    continue
                }
                if(key != "category_tree")
                {
                    print(value.type)
                    popuLateForm(key, value: value.arrayValue)
                }
            }
        }
//        advancedPricingKeys.forEach { valuestr in
//            popuLateForm(valuestr, value: productJson[valuestr].arrayValue)
//        }
        contentKeys.forEach { valuestr in
            popuLateForm(valuestr, value: productJson[valuestr].arrayValue)
        }
//        seoKeys.forEach { valuestr in
//            popuLateForm(valuestr, value: productJson[valuestr].arrayValue)
//        }
        if(selectedProductTypeId.lowercased() == "configurable".lowercased())
        {
            setConfiguration()
        }
        setUpWebsite()
        setupTreeView()
        self.makeUploadImagesSection()
    }
    
    func popuLateForm(_ key:String,value:[JSON]){
        if(value.count == 0){
            return
        }
        makeLabel(labelText: key)
        for index in value
        {
            if(index["attribute_code"].stringValue == "admin_sku" || index["attribute_code"].stringValue == "price"
               || index["attribute_code"].stringValue == "status" || index["attribute_code"].stringValue == "ced_rpoint"
               || index["attribute_code"].stringValue == "is_featured" || index["attribute_code"].stringValue == "tax_class_id"
               || index["attribute_code"].stringValue == "visibility" || index["attribute_code"].stringValue == "clothes_color"
            || index["attribute_code"].stringValue == "vendor_id" || index["attribute_code"].stringValue == "Wood_Color"
               || index["attribute_code"].stringValue == "attr_5b419564" || index["attribute_code"].stringValue == "Wood_Type"
               || index["attribute_code"].stringValue == "deposit_percent" || index["attribute_code"].stringValue == "meta_title" || index["attribute_code"].stringValue == "meta_keyword" || index["attribute_code"].stringValue == "meta_description" || index["attribute_code"].stringValue == "m_seo_canonical" || index["attribute_code"].stringValue == "description" || index["attribute_code"].stringValue == "delivery_time" || index["attribute_code"].stringValue == "special_price"){
                continue
            }
           
            if(index["attribute_code"].stringValue == "name" && !index["saved_value"].stringValue.isEmpty){
                oldProductName = index["saved_value"].stringValue
            }else if(index["attribute_code"].stringValue == "vendor_price" && !index["saved_value"].stringValue.isEmpty){
                oldProductPrice = index["saved_value"].stringValue
            }else if(index["attribute_code"].stringValue == "weight" && !index["saved_value"].stringValue.isEmpty){
                oldProductWieght = index["saved_value"].stringValue
            }
            switch(index["frontend_input"].stringValue)
            {
            case "text":
                makeTextFeild(set: index["name"].stringValue, textFeildText: index["saved_value"].stringValue, index: tags, type: index["input_type"].stringValue,fieldIndex: index,isRequired:index["is_required"].stringValue);
                tags += 1;
            case "textarea":
                maketextView(set: index["name"].stringValue, textFeildText: index["saved_value"].stringValue.html2String, index: tags,fieldIndex: index, isRequired: index["is_required"].stringValue)
                tags += 1;
            case "date":
                makeDatePickeView(set: index["name"].stringValue, textFeildText: index["saved_value"].stringValue, index: tags,fieldIndex: index, isRequired: index["is_required"].stringValue)
                tags += 1;
            case "select":
                if index["attribute_code"].stringValue == "tax_class_id"{
                    makeDropDown(set: index["name"].stringValue, textFeildText: productBasicData["tax_class_id"] ?? "", index: tags, options: index["options"],fieldIndex: index, req: index["is_required"].stringValue)
                }else{
                    makeDropDown(set: index["name"].stringValue, textFeildText: index["saved_value"].stringValue, index: tags, options: index["options"],fieldIndex: index, req: index["is_required"].stringValue)
                }
                
                tags += 1;
            case "multiselect":
                makeMultipleSelectView(set: index["name"].stringValue , textFeildText:index["saved_value"].stringValue, index: tags,options: index["options"],fieldIndex: index, req: index["is_required"].stringValue)
                tags += 1;
            case "boolean":
                makeBooleanTextField(set: index["name"].stringValue, textFeildText:index["saved_value"].stringValue,index :tags,fieldIndex: index)
                tags += 1;
            case "image":
//                            if(key == "Product Details")
//                            {
                    var i = 0;
                    for images in index["saved_value"].arrayValue
                    {
                        previousImages[i]=["image_path":images["image_path"].stringValue,"image_name":images["image_name"].stringValue,"default_image":images["default_image"].stringValue,"image_id":images["image_id"].stringValue]
                        i += 1;
                    }
//                                makeUploadImagesSection()
//                            }
                
            default:
                print("dsfdsf")
            }
        }
    }
    
    @objc func makeUploadImagesSection()
    {
        /** Dynamic Img Uplaod Section Code **/
        if #available(iOS 9.0, *) {
            let button_mainStackView_Combo_View = Button_StackView_Combo_View()
            
            button_mainStackView_Combo_View.tag = 5555;
            button_mainStackView_Combo_View.topButton.setTitle("Add Product Images".localized.uppercased(), for: UIControl.State());
            let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
            let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
            button_mainStackView_Combo_View.topButton.addTarget(self, action: #selector(ProductDetailsViewController.renderImgUploadView(_:)), for: UIControl.Event.touchUpInside);
            button_mainStackView_Combo_View.topButton.backgroundColor = color;
            
            print("---weheight---")
            print(mainHeight.constant);
            button_mainStackView_Combo_View.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            button_mainStackView_Combo_View.frame = CGRect(x: 0, y: 0, width: mainWidth.constant,height: 40);
            
            mainHeight.constant += 60;
            
            mainStackView.addArrangedSubview(button_mainStackView_Combo_View);
            mainStackView.tag = 21;
            if(isEditCase && previousImages.count > 0)
            {
                self.renderPreviousImgs();
            }
        } else {
            // Fallback on earlier versions
        };
    }
    
    
    @objc func saveClicked( _ sender: UIButton)
    {
        
        saveProduct(proceed: false);
    }
    
    @objc func continueClicked(_ sender: UIButton)
    {
        saveProduct(proceed: false)
    }
    
    @objc func saveProduct(proceed: Bool)
    {
        var params = "";
        var mainProductName = ""
        var mainProductPrice = ""
        var mainProductWeight = ""
        var postDic:[String:Any] = [:]
        for(_,value) in fieldsArray{
            if let index = value["fieldIndex"] as? JSON
            {
                if(self.attribute_codeArray.contains(index["attribute_code"].stringValue)){
                    continue
                }
                
                if(index["frontend_input"].stringValue == "text" || index["frontend_input"].stringValue == "date")
                {
                    if let field = value["field"] as? UITextField
                    {
                        if(index["is_required"].stringValue == "1")
                        {
                            if(field.text == "" || field.text == nil)
                            {
                                if index["attribute_code"].stringValue != "price"{
                                    self.view.makeToast("\(index["name"].stringValue) "+"cannot be blank".localized, duration: 2.0, position: .center);
                                    return;
                                }
                                
                            }
                            
                        }
                        if(index["attribute_code"].stringValue == "name"){
                            mainProductName = field.text ?? ""
                        }else if(index["attribute_code"].stringValue == "vendor_price"){
                            mainProductPrice = field.text ?? ""
                        }else if(index["attribute_code"].stringValue == "weight"){
                            mainProductWeight = field.text ?? ""
                        }
//                        if(index["attribute_code"].stringValue == "name"){
//                            productname = field.text ?? ""
//                        }else if(index["attribute_code"].stringValue == "sku"){
//                            vendorSku = field.text ?? ""
//                        }
//                        if(!productname.isEmpty && !vendorSku.isEmpty && productname == vendorSku){
//                            self.view.makeToast("Product name and vendor sku could not be same.".localized, duration: 2.0, position: .center);
//                            return
//                        }
                        params += "&\(index["attribute_code"].stringValue)=\(field.text ?? "")";
                        postDic[index["attribute_code"].stringValue] = field.text ?? ""
                    }
                }
                else if(index["frontend_input"].stringValue == "textarea")
                {
                    if let field = value["field"] as? UITextView
                    {
                        if(index["is_required"].stringValue == "1")
                        {
                            if(field.text == "" || field.text == nil)
                            {
                                self.view.makeToast("\(index["name"].stringValue) "+"cannot be blank".localized, duration: 2.0, position: .center);
                                return;
                            }
                            
                        }
                        params += "&\(index["attribute_code"].stringValue)=\(field.text ?? "")";
                        postDic[index["attribute_code"].stringValue] = field.text ?? ""
                    }
                }
                else if(index["frontend_input"].stringValue == "select")
                {
                    if let field = value["field"] as? Label_DropDown_Combo_View
                    {
                        if(index["is_required"].stringValue == "1")
                        {
                            if(field.dropDownButton.currentTitle == "" || field.dropDownButton.currentTitle == nil)
                            {
                                self.view.makeToast("Please select".localized + " \(index["name"].stringValue)", duration: 2.0, position: .center);
                                return;
                            }
                            
                            
                        }
                        for code in index["options"].arrayValue
                        {
                            if(code["label"].stringValue == field.dropDownButton.currentTitle)
                            {
                                params += "&\(index["attribute_code"].stringValue)=\(code["value"].stringValue)";
                                postDic[index["attribute_code"].stringValue] = code["value"].stringValue
                            }
                            
                        }
                    }
                }
                else if(index["frontend_input"].stringValue == "boolean")
                {
                    if let field = value["field"] as? UIButton
                    {
                        if(index["is_required"].stringValue == "1")
                        {
//                            if(field.currentImage == UIImage(named: "toggleOff"))
//                            {
//                                self.view.makeToast("Please enable".localized + " \(index["name"].stringValue)", duration: 2.0, position: .center);
//                                return;
//                            }
                            
                            
                        }
                        if(field.currentImage == UIImage(named: "toggleOff"))
                        {
                            params += "&\(index["attribute_code"].stringValue)=1";
                            postDic[index["attribute_code"].stringValue] = "1"
                        }
                        else
                        {
                            params += "&\(index["attribute_code"].stringValue)=0";
                            postDic[index["attribute_code"].stringValue] = "0"
                        }
                        
                    }
                }
                else if(index["frontend_input"].stringValue == "multiselect")
                {
                    if let field = value["field"] as? collectionView_Label
                    {
                        if let multi = selectedMultiSelect[field.tag]
                        {
                            if(index["is_required"].stringValue == "1")
                            {
                                if(multi.isEmpty)
                                {
                                    self.view.makeToast("Please select".localized + " \(index["name"].stringValue)", duration: 2.0, position: .center);
                                    return;
                                }
                                
                                
                            }
                            var multiselectarray = "[";
                            for x in multi{
                                for v in index["options"].arrayValue
                                {
                                    if(v["label"].stringValue == x)
                                    {
                                        multiselectarray += "\"\(v["value"].stringValue)\",";
                                    }
                                }
                            }
                            if(multiselectarray != "[")
                            {
                                multiselectarray = multiselectarray.substring(to: multiselectarray.characters.index(before: multiselectarray.endIndex));
                                multiselectarray += "]";
                            }
                            else{
                                multiselectarray = "";
                            }
                            params += "&\(index["attribute_code"].stringValue)=\(multiselectarray)";
                            postDic[index["attribute_code"].stringValue] = multiselectarray
                        }
                    }
                }
            }
        }
        imageCounter = 0
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        
        baseURL = settings.baseUrl
        if(isEditCase)
        {
            baseURL += "vendorapi/vproducts/update/";
        }
        else
        {
            baseURL += "vproductapi/vproducts/createproduct";
        }
        print(baseURL);
        
        if(productSelectedCategories.count > 0)
        {
            selectedFiltersIdsString = "[";
            for (val) in productSelectedCategories
            {
                selectedFiltersIdsString += "\""+String(val)+"\",";
            }
            selectedFiltersIdsString = selectedFiltersIdsString.substring(to: selectedFiltersIdsString.characters.index(before: selectedFiltersIdsString.endIndex));
            selectedFiltersIdsString += "]";
        }
        
        if(productSelectedWebsites.count > 0)
        {
            selectedWebsitesIdsString = "[";
            for (val) in productSelectedWebsites
            {
                selectedWebsitesIdsString += "\""+String(val)+"\",";
            }
            selectedWebsitesIdsString = selectedWebsitesIdsString.substring(to: selectedWebsitesIdsString.characters.index(before: selectedWebsitesIdsString.endIndex));
            selectedWebsitesIdsString += "]";
        }
        if(isEditCase == false)
        {
            if(selectedFiltersIdsString != "")
            {
                params += "&category="+selectedFiltersIdsString;
                postDic["category"] = selectedFiltersIdsString
            }
            if(selectedWebsitesIdsString != "")
            {
                params += "&websites="+selectedWebsitesIdsString;
                postDic["websites"] = selectedWebsitesIdsString
            }
            params += "&type="+selectedProductTypeId;
            params += "&set="+selectedAttributeSetId;
            postDic["type"] = "simple"
            postDic["set"] = selectedAttributeSetId
            if(selectedFiltersIdsString != "")
            {
                let selectedFilters = selectedFiltersIdsString.components(separatedBy: ",");
                let lastCategoryId = selectedFilters.last!.replacingOccurrences(of: "]", with: "").replacingOccurrences(of: "[", with: "").replacingOccurrences(of: "\"", with: "")
                params += "&last_category="+lastCategoryId;
            }
        }else{
            if(selectedFiltersIdsString != "")
            {
                params += "&category="+selectedFiltersIdsString;
                postDic["category"] = selectedFiltersIdsString
            }else{
                params += "&delete_category=true"
                postDic["delete_category"] = true
            }
            if(selectedWebsitesIdsString != "")
            {
                params += "&websites="+selectedWebsitesIdsString;
                postDic["websites"] = selectedWebsitesIdsString
            }
            params += "&type="+selectedProductTypeId;
            params += "&set="+selectedAttributeSetId;
        }
        params += "&vendor_id="+vendor_id+"&hashkey="+hashkey;
        if(isEditCase)
        {
            params += "&product_id="+product_id;
            params += "&store_id="+store_id;
        }
            
        //make default img request
        if(self.defautImageTag != 0){
            if let index = self.previousImagesTag.index(of: self.defautImageTag) {
                
                print(self.defautImageTag);
                print(self.previousImages);
                
                let imgNameToDelete = self.previousImages[index]!["image_name"]!;
                params += "&defaultimage="+imgNameToDelete
            }
        }
    
        var parameters: [String: Any] = [:]
        if(selectedProductTypeId.lowercased() == "configurable".lowercased())
        {
            let filter_attributeModelList = self.attributeModelListFinal.filter { attributeModelConfig in
                attributeModelConfig.assicoiatProduct_id == nil
            }
            let filter_attributeModelListIds = self.attributeModelListFinal.filter { attributeModelConfig in
                attributeModelConfig.assicoiatProduct_id != nil
            }
            for index in 0..<filter_attributeModelList.count{
                for (key,value) in postDic{
                    if (key != "sku" && key != "qty" && key != "name" && key != "status" && key != "weight" && key != "vendor_price" && !(filter_attributeModelList[index].attributeWithValue ?? "").contains(key)){
                        parameters["product[\(index)][\(key)]"] = value
                    }
                }
                parameters["product[\(index)][sku]"] = filter_attributeModelList[index].skulabel ?? ""
                parameters["product[\(index)][qty]"] = filter_attributeModelList[index].quantity ?? "0"
                parameters["product[\(index)][name]"] = "\(mainProductName)-\(filter_attributeModelList[index].namelabel ?? "")"
                parameters["product[\(index)][weight]"] = filter_attributeModelList[index].weight ?? "1"
                parameters["product[\(index)][vendor_price]"] = filter_attributeModelList[index].price ?? "0"
                parameters["product[\(index)][status]"] = filter_attributeModelList[index].isEnable ? "1":"0"
                parameters["product[\(index)][type]"] = "simple"
                parameters["product[\(index)][set]"] = selectedAttributeSetId
                let arr = (filter_attributeModelList[index].attributeWithValue ?? "").components(separatedBy: "::!")
                arr.forEach { attribute in
                    let subArr = attribute.components(separatedBy: ":;")
                    if(subArr.count>1){
                        parameters["product[\(index)][\(subArr[0])]"] = subArr[1]
                    }
                }
            }
            parameters["vendor_id"] = vendor_id
            parameters["hashkey"] = hashkey
            //configurable_attributes_data have selected attribute ids with its positions
            
          //  associated_product_id have all created simple product ids
//            {
//              "configurable_attributes_data": [
//                {
//                  "443": 0
//                }
//              ],
//              "associated_product_id": [
//                1123,
//                1134
//              ]
//            }
            
            print(urlEncodedString(from: parameters))
            
                                    
            Alert_File.addLoadingIndicator(self, msg: "Please Wait...".localized)
            var newPrice = ""
            var newWeight = ""
            let doubleOldPrice = Double(oldProductPrice) ?? 0
            let doubleNewPrice = Double(mainProductPrice) ?? 0
            let doubleOldWeight = Double(oldProductWieght) ?? 0
            let doubleNewWeight = Double(mainProductWeight) ?? 0
            if(doubleOldPrice != doubleNewPrice){
                newPrice = mainProductPrice
            }
            if(doubleOldWeight != doubleNewWeight){
                newWeight = mainProductWeight
            }
            var associated_product_idsArr:[String] = [String]()
            if(filter_attributeModelList.count > 0){
                print("\(settings.baseUrl)vproductapi/vproducts/associatedproduct")
                let request = NSMutableURLRequest(url: URL(string: "\(settings.baseUrl)vproductapi/vproducts/associatedproduct")!);
                request.httpMethod = "POST";
                request.httpBody = urlEncodedString(from: parameters).data(using: String.Encoding.utf8);
                let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
                request.setValue(requestHeader, forHTTPHeaderField: "User-Agent")
                request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
                let sessionConfig = URLSessionConfiguration.default
//                sessionConfig.httpAdditionalHeaders = [
//                    "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//                ]
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
                            let title = "Error".localized;
                            let message = "Some Network Error Occured!".localized;
                                Alert_File.showNetworkIssue(self,title:title,message:message);
                        }
                        return;
                    }
                    
                    // check for http errors
                    if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
                    {
                        print("statusCode should be 200, but is \(httpStatus.statusCode) \n response = \(response)")
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
                            print("jsonData \(jsonData)");
                            Alert_File.removeLoadingIndicator(self);
                            let message = jsonData["data"]["message"].stringValue
                            if(jsonData["data"]["success"].stringValue != "true")
                            {
                                let title = "";
                                Alert_File.showValidationError(self,title:title,message:message);
                                return;
                            }
                           // self.product_id = jsonData["data"]["product_id"].stringValue
                            let associated_product_ids = jsonData["data"]["associated_product_ids"].arrayValue
                            for index in 0..<associated_product_ids.count{
                                associated_product_idsArr.append(associated_product_ids[index].stringValue)
                                if(index < filter_attributeModelList.count){
                                    filter_attributeModelList[index].assicoiatProduct_id = associated_product_ids[index].stringValue
                                    let product = jsonData["data"]["products"][associated_product_ids[index].stringValue]
                                    filter_attributeModelList[index].assicoiatProduct_sku = product["sku"].stringValue
                                }
                            }
                            filter_attributeModelListIds.forEach { attributeModelConfig in
                                associated_product_idsArr.append(attributeModelConfig.assicoiatProduct_id ?? "")
                            }
                            self.callNextProcessApi(params, filter_attributeModelList, filter_attributeModelListIds, associated_product_idsArr, proceed,postDic,productName: mainProductName,productWeight: newWeight,productPrice: newPrice)
                    }
                }
                task.resume();
            }else{
                filter_attributeModelListIds.forEach { attributeModelConfig in
                    associated_product_idsArr.append(attributeModelConfig.assicoiatProduct_id ?? "")
                }
                self.callNextProcessApi(params, filter_attributeModelList, filter_attributeModelListIds, associated_product_idsArr, proceed,postDic,productName: mainProductName,productWeight: newWeight,productPrice: newPrice)
            }
            
        }else if(!self.is_API_Call){
            self.callSimpleProductApi(params, proceed)
        }
    }
    
    func callNextProcessApi(_ postString:String,_ filter_attributeModelList:[AttributeModelConfig],_ filter_attributeModelListIds:[AttributeModelConfig],_ associated_product_idsArr:[String],_ proceed:Bool,_ postDic:[String:Any],productName:String,productWeight:String,productPrice:String){
        
        var update_parameters: [String: Any] = [:]
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String
        let hashkey    = userData["hashKey"] as! String
        for index in 0..<filter_attributeModelListIds.count{
            for (key,value) in postDic{
                if (key != "sku" && key != "qty" && key != "name" && key != "status" && key != "weight" && key != "vendor_price" && !(filter_attributeModelListIds[index].attributeWithValue ?? "").contains(key)){
                    update_parameters[key] = value
                }
            }
            if(filter_attributeModelListIds[index].defaultimage != nil){
                update_parameters["defaultimage"] = filter_attributeModelListIds[index].defaultimage
            }
            update_parameters["sku"] = filter_attributeModelListIds[index].skulabel ?? ""
            update_parameters["qty"] = filter_attributeModelListIds[index].quantity ?? "0"
            var originalNameString = "\(filter_attributeModelListIds[index].namelabel ?? "")"
            if let range = originalNameString.range(of: oldProductName) {
                originalNameString.replaceSubrange(range, with: productName)
            }else{
                originalNameString = "\(productName)-\(originalNameString)"
            }
            update_parameters["name"] = originalNameString
            let doubleOldPrice = Double(filter_attributeModelListIds[index].oldprice ?? "0.0") ?? 0
            let doubleNewPrice = Double(filter_attributeModelListIds[index].price ?? "0") ?? 0
            let doubleOldWeight = Double(filter_attributeModelListIds[index].oldweight ?? "1.0") ?? 0
            let doubleNewWeight = Double(filter_attributeModelListIds[index].weight ?? "1.0") ?? 0
            if(doubleOldWeight != doubleNewWeight){
                update_parameters["weight"] = filter_attributeModelListIds[index].weight ?? "1"
            }else if(!productWeight.isEmpty){
                update_parameters["weight"] = productWeight
            }else{
                update_parameters["weight"] = filter_attributeModelListIds[index].weight ?? "1"
            }
            if(doubleOldPrice != doubleNewPrice){
                update_parameters["vendor_price"] = filter_attributeModelListIds[index].price ?? "0"
            }else if(!productPrice.isEmpty){
                update_parameters["vendor_price"] = productPrice
            }else{
                update_parameters["vendor_price"] = filter_attributeModelListIds[index].price ?? "0"
            }
            
            update_parameters["status"] = filter_attributeModelListIds[index].isEnable ? "1":"0"
            update_parameters["type"] = "simple"
            update_parameters["set"] = selectedAttributeSetId
            let arr = (filter_attributeModelListIds[index].attributeWithValue ?? "").components(separatedBy: "::!")
            arr.forEach { attribute in
                let subArr = attribute.components(separatedBy: ":;")
                if(subArr.count>1){
                    update_parameters[subArr[0]] = subArr[1]
                }
            }
            update_parameters["store_id"] = store_id;
            update_parameters["product_id"] = filter_attributeModelListIds[index].assicoiatProduct_id
            update_parameters["vendor_id"] = vendor_id
            update_parameters["hashkey"] = hashkey
            self.callSimpleIndividualUpdateProductApi(self.urlEncodedString(from: update_parameters))
        }
        // Build JSON structure
        var params = postString
       // params += "&associated_product_id=\(associated_product_ids)"
       // var config_attribute = "associated_product_id=\(associated_product_ids)"
        var configurable_attributes_data:[String: Any] = [String: Any]()
        for index in 0..<self.attribute_IdArray.count{
            configurable_attributes_data[self.attribute_IdArray[index]] = index
          //  config_attribute += "&configurable_attributes_data[\(index)][\(self.attributeModelListSelected[index].attribute_Id ?? "")]=\(index)"
        }
        let jsonObject: [String: Any] = [
            "configurable_attributes_data": configurable_attributes_data,
            "associated_product_ids": associated_product_idsArr
        ]
        if let jsonData = try? JSONSerialization.data(withJSONObject: jsonObject, options: [.prettyPrinted]),
           let config_attribute = String(data: jsonData, encoding: .utf8) {
            print(config_attribute)
            params += "&config_attribute=\(config_attribute)"
        }
        
        filter_attributeModelList.forEach { attributeModelConfig in
            if(attributeModelConfig.image != nil){
                self.configImageUpload(attributeModelConfig.image!, attributeModelConfig.assicoiatProduct_sku ?? "",params, proceed)
            }
        }
        filter_attributeModelListIds.forEach { attributeModelConfig in
            if(attributeModelConfig.image != nil){
                self.configImageUpload(attributeModelConfig.image!, attributeModelConfig.assicoiatProduct_sku ?? "",params, proceed)
            }
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {
            if self.imageCounter <= 0 && !self.is_API_Call{
                self.callSimpleProductApi(params, proceed)
            }
        }
    }
    
    func callSimpleIndividualUpdateProductApi(_ params:String){
        print(params)
        Alert_File.addLoadingIndicator(self, msg: "Please Wait...".localized)
        let request = NSMutableURLRequest(url: URL(string: baseURL)!);
        request.httpMethod = "POST";
//        params = params.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!;
        
        request.httpBody = params.data(using: String.Encoding.utf8);
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "User-Agent");
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request as URLRequest) { (data, response, error) in
            
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                print("statusCode should be 200, but is \(httpStatus.statusCode) \n response = \(response)")
                DispatchQueue.main.async
                    {
                        if(httpStatus.statusCode == -1009){
                            ced_showError.showNoNetWork(self)
                        }else{
                            ced_showError.showNoModule(self)
                        }
                }
            }
            // code to fetch values from response :: start
            guard let jsonData = try? JSON(data: data!) else {return}
            DispatchQueue.main.async
                {
                    print("jsonData \(jsonData)");
                    let message = jsonData["data"]["message"].stringValue
                    if(jsonData["data"]["success"].stringValue != "true")
                    {
                        let title = "";
                        Alert_File.showValidationError(self,title:title,message:message);
                    }
            }
        }
        task.resume();
    }
    
    func callSimpleProductApi(_ params:String,_ proceed:Bool){
        print("----postString------------  \(params) ------------");
        self.is_API_Call = true
        Alert_File.addLoadingIndicator(self, msg: "Please Wait...".localized)
        let request = NSMutableURLRequest(url: URL(string: baseURL)!);
        request.httpMethod = "POST";
//        params = params.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!;
        
        request.httpBody = params.data(using: String.Encoding.utf8);
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "User-Agent");
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
                    let title = "Error".localized;
                    let message = "Some Network Error Occured!".localized;
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
                    if let jhs = data {
                        print(NSString(data: jhs, encoding: String.Encoding.utf8.rawValue))
                    }
                    print("jsonData");
                    print(jsonData);
                    
                    
                    Alert_File.removeLoadingIndicator(self);
                    let message = jsonData["data"]["message"].stringValue
                    if(jsonData["data"]["success"].stringValue != "true")
                    {
                        let title = "";
                        Alert_File.showValidationError(self,title:title,message:message);
                        return;
                    }
                    
                    self.product_id = jsonData["data"]["product_id"].stringValue
                    self.skuToUseUpload = jsonData["data"]["sku"].stringValue
                    self.isHaveToProceed = proceed;
                    if(self.newImagesTag.count > 0){
                        self.doImageUpload(proceed,message);
                    }
                    else{
                        if(proceed)
                        {
                            self.switchToNextView();
                        }else{
                            if(jsonData["data"]["product_id"].stringValue != ""){
                                print(jsonData["data"]["product_id"])
                                let view = UIStoryboard(name: "ProductAddon", bundle: nil).instantiateViewController(withIdentifier: "manageProductViewController") as! ManageProductViewController
                                self.navigationController?.setViewControllers([view], animated: true)
                                
                            }
                            
                        }
                    }
            }
            
        }
        task.resume();
    }
    
    @objc func renderPreviousImgs()
    {
        /*for (key,val) in media_image
        {
            var temp = [String:String]();
            print(key);
            print(val);
            for (k,v) in val
            {
                temp[k] = v.stringValue;
            }
            previousImages[Int(key)!] = temp;
        }*/
        
        
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        
        print("renderImgUploadView");
        
        for counter in 0..<previousImages.count{
            
            imgUploadViewCounter += 1;
            let imgInfo = previousImages[counter]!;
            if(imgInfo["image_path"] == "")
            {
                return;
            }
            
            if #available(iOS 9.0, *) {
                if let button_mainStackView_Combo_View = self.view.viewWithTag(5555) as? Button_StackView_Combo_View
                {
                    let uploadImageView = UploadImageView();
                    uploadImageView.tag = imgViewTagCounter;
                    self.previousImagesTag.append(imgViewTagCounter);
                    
                    PopupLookImprovement.designImageView(uploadImageView.imgView);
                    
                    uploadImageView.delButton.addTarget(self, action: #selector(ProductDetailsViewController.makeImgDeleteConfirmation(_:)), for: UIControl.Event.touchUpInside);
                    uploadImageView.delButton.backgroundColor = color;
                    uploadImageView.delButton.tag = imgViewTagCounter;
                    uploadImageView.browseButton.isHidden = true
                    let imgRequest = NSMutableURLRequest(url: URL(string: imgInfo["image_path"]!)!);
                    let sessionConfig = URLSessionConfiguration.default
//                    sessionConfig.httpAdditionalHeaders = [
//                        "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//                    ]
                    sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
                    sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
                    let session = URLSession(configuration: sessionConfig)
                    let task = session.dataTask(with: imgRequest as URLRequest) { (data, response, error) in
                        
                        guard error == nil && data != nil else
                        {
                            print("error=\(error)")
                            return;
                        }
                        
                        // check for http errors
                        if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
                        {
                            print("statusCode should be 200, but is \(httpStatus.statusCode)")
                            print("response = \(response)")
                            return;
                        }
                        
                        // Convert the downloaded data in to a UIImage object
                        let image = UIImage(data: data!)
                        // Store the image in to our cache
                        //self.imageCache[urlString] = image
                        // Update the cell
                        DispatchQueue.main.async
                            {
                                uploadImageView.imgView.image = image;
                        }
                        
                        
                    }
                    
                    task.resume();
                    
                    button_mainStackView_Combo_View.mainView.addArrangedSubview(uploadImageView);
                    
                    //added
                    button_mainStackView_Combo_View.mainView.spacing   = 10.0
                    
                    uploadImageView.makeDefaultButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
                    uploadImageView.makeDefaultButton.setTitle("Default".localized, for: UIControl.State())
                    uploadImageView.makeDefaultButton.tag = imgViewTagCounter
                    uploadImageView.makeDefaultButton.addTarget(self, action: #selector(ProductDetailsViewController.makeDefaultFunction(_:)), for: UIControl.Event.touchUpInside);
                    if( imgInfo["default_image"] != "" )
                    {
                        defautImageTag = imgViewTagCounter
                        uploadImageView.makeDefaultButton.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
                    }
                    
                    
                    PopupLookImprovement.designButton(uploadImageView.browseButton);
                    PopupLookImprovement.designButton(uploadImageView.delButton);
                    
                    uploadImageView.makeCard(uploadImageView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                    mainHeight.constant += 110;
                    let heightConstrain = NSLayoutConstraint(item: uploadImageView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 100)
                    uploadImageView.addConstraint(heightConstrain)
                    button_mainStackView_Combo_View.frame = CGRect(x: 0, y: 0, width: width, height: CGFloat(100*imgUploadViewCounter)+40);
                    
                    imgViewTagCounter += 1;
                }
            } else {
                // Fallback on earlier versions
            }
        }
        
    }
    
    
    @objc func makeImgDeleteConfirmation(_ sender:UIButton)
    {
        let showTitle = "Confirmation".localized;
        let showMsg = "Do You Want To Delete This Image".localized;
        
        if #available(iOS 8.0, *) {
            let confirmationAlert = UIAlertController(title: showTitle, message: showMsg, preferredStyle: UIAlertController.Style.alert)
            confirmationAlert.addAction(UIAlertAction(title: "Yes".localized, style: .default, handler: { (action: UIAlertAction!) in
                
                self.deleteProductImageRequest(sender);
                
            }));
            
            confirmationAlert.addAction(UIAlertAction(title: "No".localized, style: .default, handler: { (action: UIAlertAction!) in
                //print("Handle Cancel Logic here")
            }));
            
            present(confirmationAlert, animated: true, completion: nil)
        } else {
            // Fallback on earlier versions
        }
        
        
    }
    
    @objc func deleteProductImageRequest(_ sender:UIButton)
    {
        let index = sender.tag
        var imgNameToDelete = ""
        if let idx = self.previousImagesTag.index(of: index) {
            imgNameToDelete = self.previousImages[idx]!["image_id"]!;
        }
        var postdata = ["image_ids":imgNameToDelete,"sku":skuToUseUpload]
        if let idx = self.previousImagesTag.index(of: index) {
            if self.previousImages[idx]!["default_image"]! == "true"{
                postdata["is_default"] = "true"
                self.is_default_check = true
            }
        }
        ApiHandler.handle.requestDict(with: "vproducts/media/delete", params: ["parameters":postdata], requestType: .POST, controller: self) { data, error in
            guard let data = data else {return}
            guard let json = try? JSON(data: data) else {return}
            print(json)
            DispatchQueue.main.async {
                Alert_File.removeLoadingIndicator(self);
                if let idx = self.previousImages.index(forKey: index) {
                    self.previousImages.remove(at: idx)
                }
                print(self.previousImages.count)
                if(json[0]["success"].stringValue != "true"){
                    let title = "Error";
                    let message = json[0]["message"].stringValue;
                    Alert_File.showValidationError(self,title:title,message:message);
                    return;
                }
                if(self.defautImageTag == sender.tag){
                    self.defautImageTag = 0;
                }
                // image delete successfully
                self.removeimgUploadView(sender);
              //  self.view.showToastMsg("Image Deleted Successfully!");

            }
        }
        
//        let task = URLSession.shared.dataTask(with: request as URLRequest) { (data, response, error) in
//            guard error == nil && data != nil else
//            {
//                print("error=\(error)");
//                DispatchQueue.main.async
//                    {
//                        Alert_File.removeLoadingIndicator(self);
//                        let title = "Error";
//                        let message = "Some Network Error Occured!";
//                        Alert_File.showNetworkIssue(self,title:title,message:message);
//                }
//                return;
//            }
//
//            // check for http errors
//            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
//            {
//                print("statusCode should be 200, but is \(httpStatus.statusCode)")
//                print("response = \(response)");
//                DispatchQueue.main.async
//                    {
//                        Alert_File.removeLoadingIndicator(self);
//
//                        if(httpStatus.statusCode == -1009){
//                            ced_showError.showNoNetWork(self)
//                        }else{
//                            ced_showError.showNoModule(self)
//                        }
//                }
//                return;
//            }
//
//            // code to fetch values from response :: start
//            guard let jsonData = try? JSON(data: data!) else {return}
//            DispatchQueue.main.async
//
//        }
//
//        task.resume();
    }
    
    @objc func loadConfigInfo(_ selectedProductTypeId:String,selectedAttributeSetId:String)
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
                   
                    self.config.removeAll()
                    var keysName:[String] = [String]()
                    if(jsonData["data"]["config"] != nil)
                    {
                        for (val) in jsonData["data"]["config"].arrayValue
                        {
                            var temp = [String:String]();
                            for(inrKey,inrVal) in val
                            {
                                temp[inrKey] = inrVal.stringValue;
                                if(inrKey == "attribute_code"){
                                    keysName.append(inrVal.stringValue)
                                }
                            }
                            self.config.append(temp);
                            print(temp);
                        }
                        print(self.config);
                    }
                    self.attributes = jsonData["data"]["attributes"];
                    self.productJsonArr.forEach { json in
                        let finalModel = AttributeModelConfig()
                        finalModel.assicoiatProduct_id = json["entity_id"].stringValue
                        finalModel.assicoiatProduct_sku = json["sku"].stringValue
                        finalModel.skulabel = json["sku"].stringValue
                        finalModel.isEnable = json["status"].boolValue
                        finalModel.oldprice = json["vendor_price"].stringValue
                        finalModel.price = json["vendor_price"].stringValue
                        finalModel.quantity = json["stock_item"][0]["qty"].stringValue
                        finalModel.oldweight = json["weight"].stringValue
                        finalModel.weight = json["weight"].stringValue
                        finalModel.namelabel = json["name"].stringValue
                        if(json["media_image"].arrayValue.count > 0){
                            finalModel.imageUrl = json["media_image"][0]["image_path"].stringValue
                        }
                        for images in json["media_image"].arrayValue
                        {
                            if(images["default_image"].stringValue == "true"){
                                finalModel.defaultimage = images["image_name"].stringValue
                            }
                        }
                       // self.setupAttributeView()
                        var title = ""
                        var attributeWithValue = ""
                        for attributeArray in self.attributes.arrayValue  {
                            let attribute_code = attributeArray["name"].stringValue
                            let attribute_id = attributeArray["attribute_id"].stringValue
                            
                            if(!json[attribute_code].stringValue.isEmpty && keysName.contains(attribute_code) && self.config_selected_attr.contains(attribute_id)){
                                if(!self.attribute_IdArray.contains(attributeArray["attribute_id"].stringValue)){
                                    self.attribute_IdArray.append(attributeArray["attribute_id"].stringValue)
                                    self.attribute_codeArray.append(attribute_code)
                                }
                                if(title.isEmpty){
                                    title = attributeArray["label"].stringValue
                                    attributeWithValue = attributeArray["label"].stringValue
                                }else{
                                    title = "\(title) \(attributeArray["label"].stringValue)"
                                    attributeWithValue = "\(attributeWithValue)::!\(attributeArray["label"].stringValue)"
                                }
                                for value in attributeArray["values"].arrayValue{
                                    if(value["value"].stringValue == json[attribute_code].stringValue){
                                        title = "\(title):\(value["label"].stringValue)"
                                        attributeWithValue = "\(attributeWithValue):;\(value["value"].stringValue)"
                                    }
                                }
                                finalModel.attributeIdsArr.append(json[attribute_code].stringValue)
                            }
                        }
                        finalModel.attributeWithValue = attributeWithValue
                        finalModel.title = title
                        self.attributeModelListFinal.append(finalModel)
                        self.attributeModelEditListFinal.append(finalModel)
                    }
                    if(self.isParse){
                        self.setupAttributeView()
                    }
            }
        }
        
        task.resume();
    }
    
    @objc func renderImgUploadView(_ sender:UIButton)
    {
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        
        print("renderImgUploadView");
        imgUploadViewCounter += 1;
        if #available(iOS 9.0, *) {
            if let button_mainStackView_Combo_View = self.view.viewWithTag(5555) as? Button_StackView_Combo_View
            {
                let uploadImageView = UploadImageView();
                uploadImageView.tag = imgViewTagCounter;
                
                PopupLookImprovement.designImageView(uploadImageView.imgView);
                self.newImagesTag.append(imgViewTagCounter)
                uploadImageView.delButton.addTarget(self, action: #selector(ProductDetailsViewController.removeimgUploadView(_:)), for: UIControl.Event.touchUpInside);
                uploadImageView.delButton.backgroundColor = color;
                uploadImageView.delButton.tag = imgViewTagCounter;
                uploadImageView.browseButton.addTarget(self, action: #selector(ProductDetailsViewController.browseImageToUplaod(_:)), for: UIControl.Event.touchUpInside);
                uploadImageView.browseButton.backgroundColor = color;
                uploadImageView.browseButton.tag = imgViewTagCounter;
                uploadImageView.takephotobtn.addTarget(self, action: #selector(ProductDetailsViewController.cameraImageToUplaod(_:)), for: UIControl.Event.touchUpInside);
                uploadImageView.takephotobtn.backgroundColor = color;
                uploadImageView.takephotobtn.tag = imgViewTagCounter;
                uploadImageView.takephotobtn.isHidden = false
                uploadImageView.imgView.tag = imgViewTagCounter
                let heightConstrain = NSLayoutConstraint(item: uploadImageView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 100)
                uploadImageView.addConstraint(heightConstrain)
                button_mainStackView_Combo_View.mainView.addArrangedSubview(uploadImageView);
                //added
                button_mainStackView_Combo_View.mainView.spacing   = 10.0
                uploadImageView.makeDefaultButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
                uploadImageView.makeDefaultButton.setTitle("Default".localized, for: UIControl.State())
                uploadImageView.makeDefaultButton.tag = imgViewTagCounter
                uploadImageView.makeDefaultButton.addTarget(self, action: #selector(ProductDetailsViewController.makeDefaultFunction(_:)), for: UIControl.Event.touchUpInside);
                
                PopupLookImprovement.designButton(uploadImageView.browseButton);
                PopupLookImprovement.designButton(uploadImageView.delButton);
                
                uploadImageView.makeCard(uploadImageView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                
                mainHeight.constant += 110;
                button_mainStackView_Combo_View.frame = CGRect(x: 0, y: 0, width: width, height: CGFloat(100*imgUploadViewCounter)+40);
                imgViewTagCounter += 1;
            }
        } else {
            // Fallback on earlier versions
        }
    }
    
    @objc func refactorImgUploadViewSection()
    {
        if #available(iOS 9.0, *) {
            if let button_mainStackView_Combo_View = self.view.viewWithTag(5555) as? Button_StackView_Combo_View
            {
                button_mainStackView_Combo_View.frame = CGRect(x: x, y: y, width: width, height: CGFloat(120*imgUploadViewCounter)+40);
            }
        } else {
            // Fallback on earlier versions
        }
    }
    
    @objc func removeimgUploadView(_ sender:UIButton)
    {
        if let uploadImageView = self.view.viewWithTag(sender.tag) as? UploadImageView
        {
            imgUploadViewCounter -= 1;
            uploadImageView.removeFromSuperview();
            refactorImgUploadViewSection();
            if(sender.tag == defautImageTag){
                defautImageTag = 0
            }
            self.newImagesTag.removeAll { tag in
                tag == sender.tag
            }
        }
    }
    
    @objc func makeDefaultFunction(_ sender:UIButton)
    {
        print(sender.tag);
        if(sender.imageView!.image == UIImage(named:"unchecked_checkbox"))
        {
            if(defautImageTag == 0)
            {
                sender.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
                self.defautImageTag = sender.tag;
            }
            else
            {
                self.view.showToastMsg("Only one image can be made default".localized);
            }
        }
        else
        {
            sender.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
            if(defautImageTag == sender.tag)
            {
                self.defautImageTag = 0;
            }
            
        }
    }
    
    
    
    
    @objc func browseImageToUplaod( _ sender:UIButton )
    {
        print("browseImageToUplaod");
        print(type(of: sender));
        self.currentImgTag = sender.tag;
        let ImagePicker = UIImagePickerController();
        ImagePicker.delegate = self;
        ImagePicker.sourceType = UIImagePickerController.SourceType.photoLibrary;
        
        self.present(ImagePicker, animated: true, completion: nil);
    }
    
    @objc func cameraImageToUplaod( _ sender:UIButton )
    {
        print("cameraImageToUplaod");
        print(type(of: sender));
        self.currentImgTag = sender.tag;
        let ImagePicker = UIImagePickerController();
        ImagePicker.delegate = self;
        ImagePicker.sourceType = UIImagePickerController.SourceType.camera;
        
        self.present(ImagePicker, animated: true, completion: nil);
    }
    
    @objc func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
// Local variable inserted by Swift 4.2 migrator.
let info = convertFromUIImagePickerControllerInfoKeyDictionary(info)

        let v = self.view.viewWithTag(self.currentImgTag);
        if let uploadImageView = self.view.viewWithTag(self.currentImgTag) as? UploadImageView
        {
            print("imagePickerController");
//            if let imageData = (info[convertFromUIImagePickerControllerInfoKey(UIImagePickerController.InfoKey.originalImage)] as? UIImage)!.pngData() {
//                let bytes = imageData.count
//                let kB = Double(bytes) / 1000.0 // Note the difference
//                if(kB > 5500)
//                {
//                    self.dismiss(animated: true, completion: nil);
//                    self.view.makeToast("Please Select Image Of Size Less Than 5 Mb", duration: 2.0, position: .center);
//                    return;
//                }
//            }
            uploadImageView.imgView.image = info[convertFromUIImagePickerControllerInfoKey(UIImagePickerController.InfoKey.originalImage)] as? UIImage
            self.dismiss(animated: true, completion: nil);
        }
        
    }
    
    
    @objc func makeCategoryView(labelText: String){
        let labelView = LabelView()
        labelView.label.text = labelText;
        labelView.label.font = UIFont(name: "Roboto-Bold", size: 17)
        labelView.label.numberOfLines = 10
        self.mainStackView.addArrangedSubview(labelView);
        mainHeight.constant = mainHeight.constant + 80
        labelView.translatesAutoresizingMaskIntoConstraints = false
        let heightConstrain = NSLayoutConstraint(item: labelView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 60)
        labelView.addConstraint(heightConstrain)
    }
    
    @objc func makeLabel(labelText: String)
    {
        let headerView = HeaderView()
        headerView.topLabel.text = labelText
        headerView.topLabel.textColor = .white
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        headerView.topLabel.backgroundColor = DynamicColor.secondaryColor
        
        let heightConstrain = NSLayoutConstraint(item: headerView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 59)
        headerView.addConstraint(heightConstrain)
        mainHeight.constant += 69
        mainStackView.addArrangedSubview(headerView)
        /*let labelView = LabelView()
        labelView.label.text = labelText;
        labelView.label.font = UIFont(name: "Roboto-Bold", size: 17)
        self.mainStackView.addArrangedSubview(labelView);
        mainHeight.constant = mainHeight.constant + 80
        labelView.translatesAutoresizingMaskIntoConstraints = false
        let heightConstrain = NSLayoutConstraint(item: labelView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 60)
        labelView.addConstraint(heightConstrain)*/
    }
    
    func makeDropDown(set labelText:String , textFeildText:String,index IndexWithWholeData :Int, options: JSON,fieldIndex: JSON,req:String){
        let viewForTextFeild = Label_DropDown_Combo_View()
       if req == "1"{
           viewForTextFeild.entityNameLabel.text = labelText + "*"
       }else{
           viewForTextFeild.entityNameLabel.text = labelText
       }
       
//        viewForTextFeild.entityNameLabel.text = labelText
        viewForTextFeild.dropDownButton.layer.borderWidth = 1.0;
        viewForTextFeild.dropDownButton.layer.borderColor = UIColor.black.cgColor;
    
        viewForTextFeild.dropDownButton.tag = IndexWithWholeData
        viewForTextFeild.dropDownButton.addTarget(self, action: #selector(showDropDown(_:)), for: UIControl.Event.touchUpInside);
        var dict = Dictionary<String,String>();
        for index in options.arrayValue{
            dict[index["label"].stringValue] = index["value"].stringValue
            
            if(index["value"].stringValue == textFeildText)
            {
                viewForTextFeild.dropDownButton.setTitle(index["label"].stringValue, for: .normal);
            }
        }
        if(!isEditCase){
            if let dic = dict.first{
                viewForTextFeild.dropDownButton.setTitle(dict.keys.first, for: .normal);
            }
        }
        self.dropDownArray[IndexWithWholeData] = dict;
        fieldsArray[IndexWithWholeData] = ["field":viewForTextFeild,"fieldIndex":fieldIndex];
        self.mainStackView.addArrangedSubview(viewForTextFeild)
        viewForTextFeild.tag = IndexWithWholeData
        mainHeight.constant = mainHeight.constant + 90
        viewForTextFeild.translatesAutoresizingMaskIntoConstraints = false
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 75)
        viewForTextFeild.addConstraint(heightConstrain)
        
        if fieldIndex["attribute_code"].stringValue == "product_has_weight"{
            if let weightView = self.view.viewWithTag(1234590) as? Label_TextFieldComboView{
                weightView.textLabel.isEnabled = false
            }
            
            viewForTextFeild.dropDownButton.isEnabled = false
            options.arrayValue.forEach{optn in
                if optn["value"].stringValue == "0"{
                    viewForTextFeild.dropDownButton.setTitle(optn["label"].stringValue, for: .normal)
                }
            }
            
        }
        
    }
    
    func makeBooleanTextField(set labelText:String, textFeildText:String,index IndexWithWholeData :Int,fieldIndex: JSON)
    {
        let labelView = ToggleView()
        if(textFeildText == "1")
        {
            labelView.toggleButton.setImage(UIImage(named: "toggleOff"), for: .normal)
        }
        else
        {
            labelView.toggleButton.setImage(UIImage(named: "toggleOn"), for: .normal)
        }
        labelView.toggleButton.addTarget(self, action: #selector(toggleClicked(_:)), for: .touchUpInside)
        labelView.toggleButton.imageView?.contentMode = .scaleAspectFit;
        labelView.toggleButton.setTitle(labelText, for: .normal)
        fieldsArray[IndexWithWholeData] = ["field":labelView.toggleButton,"fieldIndex":fieldIndex];
        labelView.toggleButton.tag = IndexWithWholeData;
        labelView.toggleButton.titleLabel?.font = UIFont(name: "Roboto-Bold", size: 17)
        self.mainStackView.addArrangedSubview(labelView);
        mainHeight.constant = mainHeight.constant + 40
        labelView.translatesAutoresizingMaskIntoConstraints = false
        let heightConstrain = NSLayoutConstraint(item: labelView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 30)
        labelView.addConstraint(heightConstrain)
        
        if isEditCase{
            if productBasicData["type_id"] == "bundle"{
                if fieldIndex["attribute_code"].stringValue == "price_type"{
                    labelView.toggleButton.isEnabled = false
                }
            }
        }
        
        if fieldIndex["attribute_code"].stringValue == "price_type"{
            labelView.toggleButton.tag = 829286
            if(textFeildText == "1"){
                if let priceView = self.view.viewWithTag(1234591) as? Label_TextFieldComboView{
                    priceView.textLabel.isEnabled = true
                }
            }else{
                if let priceView = self.view.viewWithTag(1234591) as? Label_TextFieldComboView{
                    priceView.textLabel.isEnabled = false
                }
            }
        }
        
        if fieldIndex["attribute_code"].stringValue == "weight_type"{
            labelView.toggleButton.tag = 829287
            
            if(textFeildText == "1"){
                if let weightView = self.view.viewWithTag(1234590) as? Label_TextFieldComboView{
                    weightView.textLabel.isEnabled = true
                }
            }else{
                if let weightView = self.view.viewWithTag(1234590) as? Label_TextFieldComboView{
                    weightView.textLabel.isEnabled = false
                }
            }
            
        }
    }
    
    @objc func toggleClicked(_ sender: UIButton){
        if(sender.currentImage == UIImage(named: "toggleOn")){
            sender.setImage(UIImage(named: "toggleOff"), for: .normal);
            if sender.tag == 829286{
                if let priceView = self.view.viewWithTag(1234591) as? Label_TextFieldComboView{
                    priceView.textLabel.isEnabled = true
                }
            }
            
            if sender.tag == 829287{
                if let weightView = self.view.viewWithTag(1234590) as? Label_TextFieldComboView{
                    weightView.textLabel.isEnabled = true
                }
            }
        }else{
            sender.setImage(UIImage(named: "toggleOn"), for: .normal);
            if sender.tag == 829286{
                if let priceView = self.view.viewWithTag(1234591) as? Label_TextFieldComboView{
                    priceView.textLabel.isEnabled = false
                    priceView.textLabel.text = ""
                }
            }
            
            if sender.tag == 829287{
                if let weightView = self.view.viewWithTag(1234590) as? Label_TextFieldComboView{
                    weightView.textLabel.isEnabled = false
                    weightView.textLabel.text = ""
                }
            }
        }
    }
    
    func makeMultipleSelectView(set labelText:String , textFeildText:String,index IndexWithWholeData :Int,options:JSON,fieldIndex: JSON,req:String){
        let viewForTextFeild = collectionView_Label()
        if req == "1"{
            viewForTextFeild.topLabel.text = labelText + "*"
        }else{
            viewForTextFeild.topLabel.text = labelText;
        }
//        viewForTextFeild.topLabel.text = labelText;
        let nibCell = UINib(nibName: "MultiSelectCollectionViewCell", bundle: nil)
        viewForTextFeild.collectionView.register(nibCell, forCellWithReuseIdentifier: "MultiSelectCollectionViewCell")
        var val = [[String:String]]()
        var saved = textFeildText.replacingOccurrences(of: "\"", with: "").replacingOccurrences(of: "[", with: "").replacingOccurrences(of: "]", with: "").components(separatedBy: ",")
        var savedArray = [String]()
        for items in options.arrayValue{
            var str = [String:String]()
            for(key,value) in items
            {
                str[key] = value.stringValue;
            }
            val.append(str)
            if(saved.contains(items["value"].stringValue))
            {
                savedArray.append(items["label"].stringValue);
            }
        }
        
        
        selectedMultiSelect[IndexWithWholeData] = savedArray;
        fieldsArray[IndexWithWholeData] = ["field":viewForTextFeild,"fieldIndex":fieldIndex];
        multiSelect.updateValue(val, forKey: IndexWithWholeData)
        viewForTextFeild.collectionView.tag = IndexWithWholeData
        viewForTextFeild.collectionView.delegate = self
        viewForTextFeild.collectionView.dataSource = self
        mainHeight.constant = mainHeight.constant + 200
        mainStackView.addArrangedSubview(viewForTextFeild)
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 180)
        viewForTextFeild.addConstraint(heightConstrain)
        viewForTextFeild.tag = IndexWithWholeData
    }
    
    func makeDatePickeView(set labelText:String , textFeildText:String,index IndexWithWholeData :Int,fieldIndex: JSON,isRequired:String){
        let viewForTextFeild = Label_TextFieldComboView()
        if isRequired == "1"{
            viewForTextFeild.headingLabel.text = labelText + "*"
        }else{
            viewForTextFeild.headingLabel.text = labelText
        }
        //        viewForTextFeild.backgroundColor = .red
//        viewForTextFeild.headingLabel.text = labelText
        
        viewForTextFeild.textLabel.text = textFeildText
        viewForTextFeild.textLabel.isEnabled = true
        mainHeight.constant = mainHeight.constant + 90
        mainStackView.addArrangedSubview(viewForTextFeild)
        viewForTextFeild.textLabel.delegate = self;
        viewForTextFeild.textLabel.tag = 10
        fieldsArray[IndexWithWholeData] = ["field":viewForTextFeild.textLabel,"fieldIndex":fieldIndex];
        viewForTextFeild.translatesAutoresizingMaskIntoConstraints = false
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 75)
        viewForTextFeild.addConstraint(heightConstrain)
        viewForTextFeild.textLabel.tag = IndexWithWholeData + 9999
        viewForTextFeild.tag = IndexWithWholeData
    }
    
    //Textfield delegates
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool { // return NO to not change text
        if(textField.tag != 11){
            return true
        }
        guard let oldText = textField.text, let r = Range(range, in: oldText) else {
                return true
            }

            let newText = oldText.replacingCharacters(in: r, with: string)
            let isNumeric = newText.isEmpty || (Double(newText) != nil)
            let numberOfDots = newText.components(separatedBy: ".").count - 1

            let numberOfDecimalDigits: Int
            if let dotIndex = newText.index(of: ".") {
                numberOfDecimalDigits = newText.distance(from: dotIndex, to: newText.endIndex) - 1
            } else {
                numberOfDecimalDigits = 0
            }

            return isNumeric && numberOfDots <= 1 && numberOfDecimalDigits <= 2
//           switch string {
//           case "0","1","2","3","4","5","6","7","8","9":
//               if(textField.text!.contains(".")){
//                   let arr = textField.text!.split(separator: ".")
//                   if(arr.count>1){
//                       let array = Array(arr[1])
//                       if(array.count>=2){
//                          return false
//                       }
//                   }
//               }
//               return true
//           case ".":
//               let array = textField.text!.characters.map { String($0) }
//               var decimalCount = 0
//               for character in array {
//                   if character == "." {
//                       decimalCount += 1
//                   }
//               }
//               if decimalCount == 1 {
//                   return false
//               } else {
//                   return true
//               }
//           default:
//               let array = Array(string)
//               if array.count == 0 {
//                   return true
//               }
//               return false
//           }
       }
    
    @objc func textFieldDidBeginEditing(_ textField: UITextField) {
        if(textField.tag == 11){
            return
        }
        self.tagToBeUsedForDatePicker = textField.tag;
//        textField.resignFirstResponder()
        
        /* background view */
        let bgCView = UIView();
        bgCView.tag=800;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        
        
        let tempInrView = UIView();
        tempInrView.tag=801;
        tempInrView.frame = CGRect(x: 0, y: CGFloat(0), width: screenSize.width-50,height: CGFloat(240));
        tempInrView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        tempInrView.backgroundColor = UIColor.white;
        tempInrView.center = CGPoint(x: bgCView.frame.size.width  / 2,
                                     y: bgCView.frame.size.height / 2);
        
        datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
        datePickerView.datePickerMode = UIDatePicker.Mode.date;
        datePickerView.setValue(UIColor.black, forKeyPath: "textColor");
        datePickerView.minimumDate = Date()
        datePickerView.backgroundColor = UIColor.white;
        datePickerView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        datePickerView.frame = CGRect(x: 0, y: CGFloat(0), width: tempInrView.frame.width,height: CGFloat(200));
        tempInrView.addSubview(datePickerView);
        
        
        let twoButtonView = TwoButtonView();
        twoButtonView.frame = CGRect(x: 0, y: CGFloat(200), width: tempInrView.frame.width,height: CGFloat(40));
        twoButtonView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        tempInrView.addSubview(twoButtonView);
        
        twoButtonView.cancelButton.addTarget(self, action: #selector(ProductDetailsViewController.cancelDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.doneButton.addTarget(self, action: #selector(ProductDetailsViewController.pickDateFromDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        
        
        bgCView.addSubview(tempInrView);
        self.view.addSubview(bgCView);
    }
    
    
    
    @objc func cancelDatePickerView(_ sender:UIButton)
    {
        if let textView = self.view.viewWithTag(tagToBeUsedForDatePicker) as? UITextField
        {
            textView.resignFirstResponder();
        }
        self.view.viewWithTag(800)?.removeFromSuperview();
    }
    
    
    @objc func pickDateFromDatePickerView(_ sender:UIButton)
    {
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = DateFormatter.Style.short
        dateFormatter.timeStyle = DateFormatter.Style.none
        if let textView = self.view.viewWithTag(tagToBeUsedForDatePicker) as? UITextField
        {
            dateFormatter.dateFormat = "yyyy-MM-dd";
            dateFormatter.locale = Locale(identifier: "en_US_POSIX")
            textView.text = dateFormatter.string(from: datePickerView.date);
            textView.resignFirstResponder();
        }
        self.view.viewWithTag(800)?.removeFromSuperview();
    }
    
    func makePassword(set labelText:String , textFeildText:String,index IndexWithWholeData :Int,fieldIndex: JSON){
        let viewForTextFeild = Label_TextFieldComboView()
        //        viewForTextFeild.backgroundColor = .red
        viewForTextFeild.headingLabel.text = labelText
        viewForTextFeild.textLabel.text = ""
        viewForTextFeild.textLabel.isEnabled = true
        viewForTextFeild.textLabel.isSecureTextEntry = true
        mainHeight.constant = mainHeight.constant + 90
        mainStackView.addArrangedSubview(viewForTextFeild)
        fieldsArray[IndexWithWholeData] = ["field":viewForTextFeild.textLabel,"fieldIndex":fieldIndex];
        viewForTextFeild.translatesAutoresizingMaskIntoConstraints = false
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 75)
        viewForTextFeild.addConstraint(heightConstrain)
        viewForTextFeild.tag = IndexWithWholeData
    }
   
    //    Label_TextView_Combo_View
    
    func maketextView(set labelText:String , textFeildText:String,index IndexWithWholeData :Int,fieldIndex: JSON,isRequired:String){
        let viewForTextFeild=Label_TextView_Combo_View()
        if isRequired == "1"{
            viewForTextFeild.entityNameLabel.text = labelText + "*"
        }else{
            viewForTextFeild.entityNameLabel.text = labelText
        }
//        viewForTextFeild.entityNameLabel.text = labelText
        viewForTextFeild.entityValueTxtField.text = textFeildText;
        
        mainHeight.constant = mainHeight.constant + 140
        
        mainStackView.addArrangedSubview(viewForTextFeild)
        if(fieldIndex["input_type"].stringValue == "int")
        {
            viewForTextFeild.entityValueTxtField.keyboardType = .numberPad;
        }
        else if(fieldIndex["input_type"].stringValue == "decimal")
        {
            viewForTextFeild.entityValueTxtField.keyboardType = .numberPad;
            if let doubleValue = Double(textFeildText) {
                let intValue = Int(doubleValue)
                viewForTextFeild.entityValueTxtField.text = "\(intValue)"   // Output: 123 (decimal part truncated)
            }
        }
        fieldsArray[IndexWithWholeData] = ["field":viewForTextFeild.entityValueTxtField,"fieldIndex":fieldIndex];
        viewForTextFeild.translatesAutoresizingMaskIntoConstraints = false
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 120)
        viewForTextFeild.addConstraint(heightConstrain)
        print("---height---")
        print(mainHeight.constant);
        viewForTextFeild.tag = IndexWithWholeData
    }
    
    func makeTextFeild(set labelText:String , textFeildText:String,index IndexWithWholeData :Int, type: String,fieldIndex: JSON,isRequired:String){
        let viewForTextFeild = Label_TextFieldComboView()
        //        viewForTextFeild.backgroundColor = .red
        if isRequired == "1"{
            viewForTextFeild.headingLabel.text = labelText + "*"
        }else{
            viewForTextFeild.headingLabel.text = labelText
        }
        
        viewForTextFeild.textLabel.text = textFeildText
        viewForTextFeild.textLabel.isEnabled = true
        if(fieldIndex["attribute_code"].stringValue == "sku"){
            viewForTextFeild.textLabel.isEnabled = false
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
            let vendor_id = userData["vendorId"] as? String ?? "";
            if(!isEditCase){
               // viewForTextFeild.textLabel.text = "\(vendor_id)_\(Int(Date().timeIntervalSince1970))"
            }
        }else if(fieldIndex["attribute_code"].stringValue == "url_key"){
            viewForTextFeild.textLabel.isEnabled = false
        }else if(fieldIndex["attribute_code"].stringValue == "weight"){
            self.weight = viewForTextFeild.textLabel
        }
        mainHeight.constant = mainHeight.constant + 90
        mainStackView.addArrangedSubview(viewForTextFeild)
        if(fieldIndex["input_type"].stringValue == "int")
        {
            viewForTextFeild.textLabel.keyboardType = .numberPad;
        }
        else if(fieldIndex["input_type"].stringValue == "decimal")
        {
            viewForTextFeild.textLabel.keyboardType = .numberPad;
            viewForTextFeild.textLabel.delegate = self
            viewForTextFeild.textLabel.tag = 11
            if let doubleValue = Double(textFeildText) {
                let intValue = Int(doubleValue)
                viewForTextFeild.textLabel.text = "\(intValue)"   // Output: 123 (decimal part truncated)
            }
        }
        fieldsArray[IndexWithWholeData] = ["field":viewForTextFeild.textLabel,"fieldIndex":fieldIndex];
        viewForTextFeild.translatesAutoresizingMaskIntoConstraints = false
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 75)
        viewForTextFeild.addConstraint(heightConstrain)
        viewForTextFeild.tag = IndexWithWholeData
        print(labelText)
        if (fieldIndex["attribute_code"].stringValue == "name")
        {
            if !isEditCase{
                viewForTextFeild.textLabel.addTarget(self, action: #selector(textFieldDataChanged(_:)), for: .editingChanged)
            }
        }
        
        if fieldIndex["attribute_code"].stringValue == "qty"{
            if selectedProductTypeId == "grouped"{
                viewForTextFeild.textLabel.isEnabled = false
            }
            if isEditCase{
                if productBasicData["type_id"] == "grouped"{
                    viewForTextFeild.textLabel.isEnabled = false
                }
            }
            
            if selectedProductTypeId == "configurable"{
                viewForTextFeild.textLabel.text = "0"
                viewForTextFeild.textLabel.isEnabled = false
            }
        }
        
        if fieldIndex["attribute_code"].stringValue == "sku"
        {
            viewForTextFeild.tag = 2323
        }
        if fieldIndex["attribute_code"].stringValue == "url_key"
        {
            viewForTextFeild.tag = 12563
        }
        if fieldIndex["attribute_code"].stringValue == "meta_title"
        {
            viewForTextFeild.tag = 123456
        }
        
        if fieldIndex["attribute_code"].stringValue == "weight"{
            print("weight")
            viewForTextFeild.tag = 1234590
        }
        
        if fieldIndex["attribute_code"].stringValue == "price"{
            print("price")
            viewForTextFeild.tag = 1234591
            if selectedProductTypeId == "configurable"{
                viewForTextFeild.textLabel.text = "0"
                viewForTextFeild.textLabel.isEnabled = false
            }
        }
    }
    
    @objc func showDropDown(_ sender:UIButton){
        let tag = sender.tag
        let dropDown = DropDown();
        let datasource = dropDownArray[tag];
        dropDown.dataSource = Array((datasource?.keys)!);
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            _ = dropDown.show()
        } else {
            dropDown.hide();
        }
       
        
    }
    
    @objc func switchToNextView()
    {
        print("switchToNextView");
        print(self.tabs);
        print(self.tabs["websites"])
        if(websites != [String:String]() || categoryJson != nil)
         {
         print("switchToNextView");
         print("-----------------");
         print(productSelectedCategories)
         let viewcontrollor = CategoryTreeViewController()
         viewcontrollor.categoriesList = categoriesList
         viewcontrollor.subcategoriesData = subcategoriesData;
         viewcontrollor.categoryJson = categoryJson;
         viewcontrollor.isEditCase = isEditCase;
         viewcontrollor.selectedFiltersIds = productSelectedCategories;
         viewcontrollor.selectedWebsitesIds = productSelectedWebsites;
         //edit time
         viewcontrollor.subcategoriesData = subcategoriesData;
         
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
            if !isEditCase{
                viewcontrollor.create_prod = true
            }
         self.navigationController?.pushViewController(viewcontrollor, animated: true);
         
         
         /*let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "website_Category_ViewController") as! Website_Category_ViewController;
         
         //edit time
         viewcontrollor.isEditCase = isEditCase;
         viewcontrollor.selectedFiltersIds = productSelectedCategories;
         viewcontrollor.selectedWebsitesIds = productSelectedWebsites;
         //edit time
         viewcontrollor.subcategoriesData = subcategoriesData;
         
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
         
         
         
         self.navigationController?.setViewControllers([viewcontrollor], animated: true)*/
         return;
         }
        
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
        
        
    }
    
    @objc func textFieldDataChanged(_ sender:UITextField)
    {
        
        if let sku = self.view.viewWithTag(2323) as? Label_TextFieldComboView
        {
            sku.textLabel.text = sender.text
        }
        
        if let urlkey = self.view.viewWithTag(12563) as? Label_TextFieldComboView
        {
            urlkey.textLabel.text = sender.text
        }
        if let metaTitle = self.view.viewWithTag(123456) as? Label_TextFieldComboView {
            metaTitle.textLabel.text = sender.text
        }
    }
    
    func resizeImage(image: UIImage, newWidth: CGFloat) -> UIImage? {

       let scale = newWidth / image.size.width
       let newHeight = image.size.height * scale
       UIGraphicsBeginImageContext(CGSizeMake(newWidth, newHeight))
       image.draw(in: CGRectMake(0, 0, newWidth, newHeight))
       let newImage = UIGraphicsGetImageFromCurrentImageContext()
       UIGraphicsEndImageContext()
       return newImage
   }
    
    @objc func doImageUpload(_ proceed:Bool,_ message:String){
        var postData = [String:Any]()
        var imageArray = [[String:Any]]()
        newImagesTag.forEach { tag in
            if let uploadImageView = self.view.viewWithTag(tag) as? UploadImageView
            {
                let imageView = uploadImageView.imgView
                if(imageView?.image != nil){
                    var isDefault = false;
                    if(tag == defautImageTag)
                    {
                        isDefault = true
                    }
//                    if(self.is_default_check && defautImageTag == 0){
//                        isDefault = true
//                        self.is_default_check = false
//                    }
                    let image = resizeImage(image: imageView!.image!, newWidth: 300)
                    let imageData = image?.jpegData(compressionQuality: 0.2)!
                    if(imageData != nil){
                    let onlyExtension = imageData!.format
                    let ext = onlyExtension.replacingOccurrences(of: ".", with: "")
                    let base64_encoded_data=(imageData!.base64EncodedString())
                    let dateFormatter: DateFormatter = DateFormatter()
                    dateFormatter.locale = Locale(identifier: "en_US_POSIX")
                    dateFormatter.dateFormat = "yyyyMMddHHmmssSSS"
                    let selectedDate: String = "\(dateFormatter.string(from: Date()))\(tag)"
                    let fullNumber = Int(Date().timeIntervalSince1970 * 1000) + tag
                    let fullNumberString = String(fullNumber)
                    // Ensure it safely gets the last 9 digits (or fewer if shorter)
                    let safeLast9 = String(fullNumberString.suffix(min(9, fullNumberString.count)))
                    let imagePosition = Int(safeLast9) ?? tag
                    var dictionary:[String:Any] = ["content":["name":"name\(selectedDate)\(imageData!.format)","type":"image/\(ext)","base64_encoded_data":base64_encoded_data],"label":"true","media_type":"image","position":imagePosition]
                    if isDefault{
                        dictionary["is_default"]="true";
                    }
                    //                let convertedstring=convertDicTostring(str: dictionary)
                    imageArray.append(dictionary)
                }
                    //myImageUploadRequest(imgView);
                }
            }
        }
        
        if imageArray.isEmpty{
            self.switchToNextView()
            return
        }
        
        postData["parameters"] = ["images":imageArray,"sku":skuToUseUpload]
        if let jsonData = try? JSONSerialization.data(withJSONObject: postData, options: []),
           let jsonString = String(data: jsonData, encoding: .utf8) {
            print("JSON String: \(jsonString)")
        } else {
            print("Failed to convert to JSON string.")
        }
        ApiHandler.handle.requestDict(with: "vproducts/media", params: postData, requestType: .POST, controller: self) { data, error in
            guard let data = data else {return}
            guard let json = try? JSON(data: data) else {return}
            print(json)
            DispatchQueue.main.async {
                if json[0]["success"].stringValue != "true"{
                    self.view.showToastMsg(json["message"].stringValue)
                    return;
                }
                else{
                    self.view.showToastMsg(json[0]["message"].stringValue)
                    
                }
                Ced_CommonVendor.delay(2.0, closure: {
                    if(proceed)
                    {
                        self.switchToNextView();
                    }else{
                        self.view.showToastMsg(message)
                        Ced_CommonVendor.delay(2.0, closure: {
                                let view = UIStoryboard(name: "ProductAddon", bundle: nil).instantiateViewController(withIdentifier: "manageProductViewController") as! ManageProductViewController
                                self.navigationController?.setViewControllers([view], animated: true)
                        })
                        
                        
                    }
                })
                
            }
        }
    }
    
    @objc func configImageUpload(_ image:UIImage,_ skuProduct:String,_ params:String,_ proceed:Bool){
        var postData = [String:Any]()
        var imageArray = [[String:Any]]()
        
        let imageData = image.jpegData(compressionQuality: 0.2)!
        let onlyExtension = imageData.format
        let ext = onlyExtension.replacingOccurrences(of: ".", with: "")
        let base64_encoded_data=(imageData.base64EncodedString())
        let dateFormatter: DateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyyMMddHHmmssSSS"
        let tag:Int = Int.random(in: 1...1000)
        let selectedDate: String = "\(dateFormatter.string(from: Date()))\(tag)"
        let fullNumber = Int(Date().timeIntervalSince1970 * 1000) + tag
        let fullNumberString = String(fullNumber)
        // Ensure it safely gets the last 9 digits (or fewer if shorter)
        let safeLast9 = String(fullNumberString.suffix(min(9, fullNumberString.count)))
        let imagePosition = Int(safeLast9) ?? tag
        var dictionary:[String:Any] = ["content":["name":"name\(selectedDate)\(imageData.format)","type":"image/\(ext)","base64_encoded_data":base64_encoded_data],"label":"true","media_type":"image","position":imagePosition]
        dictionary["is_default"]="true"
        imageArray.append(dictionary)
        if imageArray.isEmpty{
            self.switchToNextView()
            return
        }
        self.imageCounter += 1
        postData["parameters"] = ["images":imageArray,"sku":skuProduct]
        
        ApiHandler.handle.requestDict(with: "vproducts/media", params: postData, requestType: .POST, controller: self) { data, error in
            guard let data = data else {return}
            guard let json = try? JSON(data: data) else {return}
            print(json)
            DispatchQueue.main.async {
                self.imageCounter -= 1
                if json[0]["success"].stringValue != "true"{
                    self.view.showToastMsg(json["message"].stringValue)
                  //  return;
                }
                else{
                    self.view.showToastMsg(json[0]["message"].stringValue)
                }
                if self.imageCounter <= 0 && !self.is_API_Call{
                    self.callSimpleProductApi(params, proceed)
                }
            }
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
  
}

extension ProductDetailsViewController:RATreeViewDelegate, RATreeViewDataSource{
    @objc func treeView(_ treeView: RATreeView, numberOfChildrenOfItem item: Any?) -> Int {
        if let item = item as? DataObject {
            return item.children.count
        } else {
            return self.categoryData.count
        }
    }
    
    @objc func treeView(_ treeView: RATreeView, child index: Int, ofItem item: Any?) -> Any {
        if let item = item as? DataObject {
            return item.children[index]
        } else {
            return categoryData[index] as AnyObject
        }
    }

    @objc func treeView(_ treeView: RATreeView, cellForItem item: Any?) -> UITableViewCell {
        let cell = treeView.dequeueReusableCell(withIdentifier: String(describing: CategoryTreeTableViewCell.self)) as! CategoryTreeTableViewCell
        let item = item as! DataObject
        cell.tag = Int(item.id)!
            if let index = productSelectedCategories.index(of: Int(item.id)!) {
                item.image="checked";
            }
            else
            {
                item.image="unchecked";
            }
        //sub_categories
        if(item.children.count > 0){
            cell.rightImage.image = UIImage(named: "IQButtonBarArrowRight")
            cell.rightImage.isHidden = false
        }else{
            cell.rightImage.image = UIImage()
            cell.rightImage.isHidden = true
        }
        
        cell.rightImage.tag = Int(item.id)!
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.handleTap(_:)))
        cell.rightImage.addGestureRecognizer(tap)
        cell.img.isHidden = false
        if(item.name.lowercased() == "Default Category".lowercased() || item.name.lowercased() == "صنف المنتج".lowercased()){
            item.image="unchecked";
            cell.img.isHidden = true
            cell.rightImage.isHidden = true
        }
        let level = treeView.levelForCell(forItem: item)
        let detailsText = "Number of children \(item.children.count)"
        cell.selectionStyle = .none
        
        cell.setup(withTitle: item.name, detailsText: detailsText, level: level, additionalButtonHidden: false,img: item.image)
        return cell
    }
    
    @objc func handleTap(_ sender: UITapGestureRecognizer? = nil) {
        // handling code
        print(sender?.view?.tag)
        let id = sender?.view?.tag ?? 0
        if let index = categoryData.firstIndex(where: { dataObject in
            dataObject.id == "\(id)"
        }) {
            let item = categoryData[index]
            if let cell=treeView.cell(forItem: item) as? CategoryTreeTableViewCell{
                if(treeView.isCellExpanded(cell)){
                    treeView.collapseRow(forItem: item)
                    cell.rightImage.image=UIImage(named: "IQButtonBarArrowRight")
                }else{
                    treeView.expandRow(forItem: item)
                    cell.rightImage.image=UIImage(named: "down-arrow")
                }
            }
        }else if let index = categoryData.first?.children.firstIndex(where: { dataObject in
            dataObject.id == "\(id)"
        }) {
            let item = categoryData.first?.children[index]
            if let cell=treeView.cell(forItem: item!) as? CategoryTreeTableViewCell{
                if(treeView.isCellExpanded(cell)){
                    treeView.collapseRow(forItem: item)
                    cell.rightImage.image=UIImage(named: "IQButtonBarArrowRight")
                }else{
                    treeView.expandRow(forItem: item)
                    cell.rightImage.image=UIImage(named: "down-arrow")
                }
            }
           // treeView.expandRow(forItem: item)
        }
        
    }
    
    func treeView(_ treeView: RATreeView, didExpandRowForItem item: Any) {
        if let cell=treeView.cell(forItem: item) as? CategoryTreeTableViewCell{
            cell.rightImage.image=UIImage(named: "down-arrow")
        }
    }
    
    func treeView(_ treeView: RATreeView, didCollapseRowForItem item: Any) {
        if let cell=treeView.cell(forItem: item) as? CategoryTreeTableViewCell{
            cell.rightImage.image=UIImage(named: "IQButtonBarArrowRight")
        }
    }
    @objc func treeView(_ treeView: RATreeView, didSelectRowForItem item: Any) {
       
        if let cell=treeView.cell(forItem: item) as? CategoryTreeTableViewCell{
            let image=cell.rightImage.image
            let checkImage=UIImage(named: "IQButtonBarArrowRight")
            let currentImage = cell.img.image
            let checkboxImage = UIImage(named: "unchecked")
            let tag = cell.tag;
            let item = item as! DataObject
            print("--select tag-- \(item.name)")
            print(tag)
            if(currentImage == checkboxImage)
            {
                if(productSelectedCategories.count >= 3){
                    self.view.showToastMsg("You cannot select categories more than 3".localized)
                }else{
                    cell.img.image = UIImage(named: "checked")
                    productSelectedCategories.append(tag)
                }
                /*if(!selectedCategories.contains(item.name))
                {
                    selectedCategories.append(item.name)
                }*/
            }
            else
            {
                cell.img.image = UIImage(named: "unchecked")
                if let indexToRemove = productSelectedCategories.index(of: tag) {
                    self.productSelectedCategories.remove(at: indexToRemove);
                }
                /*if let categSelected = selectedCategories.index(of: item.name)
                {
                    self.selectedCategories.remove(at: categSelected);
                }*/
            }
//            if image==checkImage{
//                cell.rightImage.image=UIImage(named: "down-arrow")
//            }
//            else
//            {
//                cell.rightImage.image=UIImage(named: "IQButtonBarArrowRight")
//                
//            }
            if(item.name.lowercased() == "Default Category".lowercased()){
                cell.rightImage.image=nil
                cell.img.image = UIImage(named: "unchecked")
            }
            if let data=item as? DataObject{
                if data.children.count == 0{
                    cell.rightImage.image=nil
                }
            }
            cell.rightImage.tintColor=UIColor.black
        }
    }
    
}

extension UIViewController
{
    @objc func convertDicTostring(str:Dictionary<String, String>) -> String
    {
        let dictionary = str
        let jsonData = try? JSONSerialization.data(withJSONObject: dictionary, options: [])
        let jsonString = String(data: jsonData!, encoding: .utf8)
        return jsonString!
    }
    @objc func convertAnyDicTostring(str:Dictionary<String, Any>) -> String
    {
        let dictionary = str
        let jsonData = try? JSONSerialization.data(withJSONObject: dictionary, options: [])
        let jsonString = String(data: jsonData!, encoding: .utf8)
        return jsonString!
    }
    // MARK: - Time management Stuff

    @objc func convertParamsToJSON(params:[[String : String]])->String{
     var jsonString = String()
     if let theJSONData = try? JSONSerialization.data(
     withJSONObject: params,
     options: [.prettyPrinted]) {
     let theJSONText = String(data: theJSONData,
     encoding: .ascii)
     print("JSON string = \(theJSONText!)")
     guard let theJSONTextFinal = theJSONText else {return ""}
     jsonString = theJSONTextFinal
     }
     return jsonString
     }
    
    @objc func convertAnyParamsToJSON(params:[[String : Any]])->String{
      //  var jsonString = String()
        if let theJSONData = try? JSONSerialization.data(
            withJSONObject: params, options: []){
            let theJSONText = String(data: theJSONData,
                                     encoding: .ascii)
            
            //      options: [.prettyPrinted]) {
            //      let theJSONText = String(data: theJSONData,
            //      encoding: .ascii)
            print("JSON string = \(theJSONText!)")
            //      guard let theJSONTextFinal = theJSONText else {return ""}
            //      jsonString = theJSONTextFinal
            return theJSONText ?? ""
        }
        return ""
    }
      
}
extension Data {
    var format: String {
        let array = [UInt8](self)
        let ext: String
        print("----type----")
        print(array[0])
        switch (array[0]) {
        case 0xFF:
            ext = ".jpeg"
        case 0x89:
            ext = ".png"
        case 0x47:
            ext = ".gif"
        case 0x49, 0x4D :
            ext = ".tiff"
        default:
            ext = ".unknown"
        }
        return ext
    }
    func byRemovingEXIF() -> Data? {
            guard let source = CGImageSourceCreateWithData(self as NSData, nil),
                  let type = CGImageSourceGetType(source) else
            {
                return nil
            }

            let count = CGImageSourceGetCount(source)
            let mutableData = NSMutableData()
            guard let destination = CGImageDestinationCreateWithData(mutableData, type, count, nil) else {
                return nil
            }

            let exifToRemove: CFDictionary = [
                kCGImagePropertyExifDictionary: kCFNull,
                kCGImagePropertyGPSDictionary: kCFNull,
            ] as CFDictionary

            for index in 0 ..< count {
                CGImageDestinationAddImageFromSource(destination, source, index, exifToRemove)
                if !CGImageDestinationFinalize(destination) {
                    print("Failed to finalize")
                }
            }

            return mutableData as Data
        }
}
extension Date {
    func toMillis() -> Int64! {
        return Int64(self.timeIntervalSince1970 * 1000)
    }
}
extension String
{
    func trim() -> String
    {
        return self.trimmingCharacters(in: CharacterSet.whitespaces)
    }
}

extension ProductDetailsViewController : UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout {
    @objc func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "MultiSelectCollectionViewCell", for: indexPath) as? MultiSelectCollectionViewCell
        let text = multiSelect[collectionView.tag]![indexPath.row]["label"];
        //let text = fullEditProfileData[collectionView.tag].options[indexPath.row]
        cell?.checkBoxButtonView.checkboxButton.setTitle(text, for: .normal)
        //        checking for selected data
        if let selectedata = selectedMultiSelect[collectionView.tag]{
            cell?.setdataView(dataFor: selectedata)
        }
        cell?.checkBoxButtonView.tag = collectionView.tag
        cell?.checkBoxButtonView.checkboxButton.tag = collectionView.tag
        cell?.checkBoxButtonView.checkboxButton.addTarget(self, action: #selector(selectMultiSelectbutton(_:)), for: .touchUpInside)
        
        return cell ?? UICollectionViewCell()
    }
    
    @objc func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    @objc func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return (multiSelect[collectionView.tag]?.count)!;
    }
    
    @objc func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: 180, height: 40)
    }
    
    @objc func selectMultiSelectbutton(_ sender:UIButton){
        if sender.currentImage == UIImage(named: "unchecked"){
            sender.setImage(UIImage(named: "checked"), for: .normal)
            let keyValue = sender.tag;
            var arry = selectedMultiSelect[keyValue]
            if !(arry?.contains(sender.currentTitle ?? "") ?? false){
                if arry == nil{
                    arry = [sender.currentTitle ?? ""]
                }else{
                    arry?.append(sender.currentTitle ?? "")
                }
                
            }
            selectedMultiSelect.updateValue(arry ?? [], forKey: keyValue)
        }else{
            sender.setImage(UIImage(named: "unchecked"), for: .normal)
            let keyValue = sender.tag;
            var arry = selectedMultiSelect[keyValue]
            if (arry?.contains(sender.currentTitle ?? "") ?? false){
                //                arry?.append(sender.currentTitle ?? "")
                arry = arry?.filter { $0 != sender.currentTitle ?? "" }
            }
            selectedMultiSelect.updateValue(arry ?? [], forKey: keyValue)
        }
        print(multiSelect)
    }
    
    func urlEncodedString(from parameters: [String: Any]) -> String {
        var components: [String] = []

        for (key, value) in parameters {
//            if let array = value as? [Any] {
//                for (index, element) in array.enumerated() {
//                    let encodedKey = "\(key)[\(index)]".addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
//                    let encodedValue = "\(element)".addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
//                    components.append("\(encodedKey)=\(encodedValue)")
//                }
//            } else {
//                let encodedKey = key.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
//                let encodedValue = "\(value)".addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
//                components.append("\(encodedKey)=\(encodedValue)")
//            }
            components.append("\(key)=\(value)")
        }
        return components.joined(separator: "&")
    }
    
    func showAlertWithCallback(on viewController: UIViewController, title: String, message: String,isEnable:Bool, completion: @escaping (String) -> Void) {
        let alert = UIAlertController(title: "Vendor App".localized, message: nil, preferredStyle: .actionSheet)

        let enAbleAction = UIAlertAction(title: isEnable ? "Disabled".localized:"Enabled".localized, style: .default) { _ in
            completion("enable")
        }

        let removeAction = UIAlertAction(title: "Remove Product".localized, style: .default) { _ in
            completion("remove")
        }
        
        let cancelAction = UIAlertAction(title: "Cancel".localized, style: .destructive) { _ in
            
        }

        alert.addAction(enAbleAction)
        alert.addAction(removeAction)
        alert.addAction(cancelAction)

        viewController.present(alert, animated: true, completion: nil)
    }
}

// Helper function inserted by Swift 4.2 migrator.
fileprivate func convertFromUIImagePickerControllerInfoKeyDictionary(_ input: [UIImagePickerController.InfoKey: Any]) -> [String: Any] {
	return Dictionary(uniqueKeysWithValues: input.map {key, value in (key.rawValue, value)})
}

// Helper function inserted by Swift 4.2 migrator.
fileprivate func convertFromUIImagePickerControllerInfoKey(_ input: UIImagePickerController.InfoKey) -> String {
	return input.rawValue
}
