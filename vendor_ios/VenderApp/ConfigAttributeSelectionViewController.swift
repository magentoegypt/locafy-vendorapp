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
import Combine

class ConfigAttributeSelectionViewController: ced_VendorBaseClass,CustomCheckboxDelegate,UITableViewDataSource,UITableViewDelegate {
    
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var groupedProductTableView: UITableView!
    @IBOutlet weak var topView: UIView!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var nextButton: UIButton!
    
    var handler: (([AttributeModelConfig],[AttributeModelConfig])->())?
    let imagePicker = ImagePickerManager()
    var cancellables = Set<AnyCancellable>()
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

    var thirdtabList:[String:[String]] = ["Images".localized:["Apply single set of images to all SKUs".localized,"Apply unique images by attribute to each SKU".localized,"Skip image uploading at this time".localized],"Price".localized:["Apply single price to all SKUs".localized,"Apply unique prices by attribute to each SKU".localized,"Skip price at this time".localized],"Quantity".localized:["Apply single quantity to each SKUs".localized,"Apply unique quantity by attribute to each SKU".localized,"Skip quantity at this time".localized]];
    var thirdtabListBool:[Int:[Bool]] = [0:[false,false,true],1:[false,false,true],2:[false,false,true]];
    //
    var categoryJson: JSON!;
    var selectedFiltersIdsString = "";
    var selectedWebsitesIdsString = "";
    var config = [[String:String]]();//received from previous view
    let checkboxHeight : CGFloat = 30;
    var checkboxTagCounter = 10;
    let StringToRenderOnTop = "Only attributes with scope \"Global\", input type \"Dropdown\" and Use To Create Configurable Product \"Yes\" are available.";
    
    var selectedSetId = "";
    
    var selectedstock = "";
    var selectedtaxes = "";
    
    
    let screenSize: CGRect = UIScreen.main.bounds;
    let relatedProCellHeight = CGFloat(180);
    var heightToUse = CGFloat(0);
    let padding = CGFloat(5);
    
    var baseURL = String();
    var productTypeID = String();
    
    var products = [[String:String]]();
    var selectedProductsIds = [Int]();
    var stepCount:Int = 0
    var attributeModelList:[AttributeModelConfig] = [AttributeModelConfig]()
    var attributeModelListSelected:[AttributeModelConfig] = [AttributeModelConfig]()
    var attributeModelListFinal:[AttributeModelConfig] = [AttributeModelConfig]()
    var attributeModelEditListFinal:[AttributeModelConfig] = [AttributeModelConfig]()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        topLabel.layer.cornerRadius = 5
        topLabel.clipsToBounds = true
        print("selectedProductTypeId");
        print(selectedProductTypeId);
        if(ced_storeVC.selectLangauge == "ar"){
            topLabel.textAlignment = .right
        }else{
            topLabel.textAlignment = .left
        }
        //ced_navigationBarController().addNavButton(self,str:"no")
        
        self.groupedProductTableView.delegate = self;
        self.groupedProductTableView.dataSource = self;
        // Swift 4.2 onwards
//        groupedProductTableView.rowHeight = UITableView.automaticDimension
//        groupedProductTableView.estimatedRowHeight = 200
        
       // saveButton.setTitle(NSLocalizedString("Continue", comment: ""), for: UIControl.State());
      //  saveButton.addTarget(self, action: #selector(ConfigAttributeSelectionViewController.continuePressed(_:)), for: UIControl.Event.touchUpInside);
        backButton.layer.cornerRadius = 10
        nextButton.layer.cornerRadius = 10
        //["DamagedCondition","NewCondition","RefurbishedCondition","UsedCondition"]
        self.topLabel.text = "Step 1: Select Attributes".localized
        for index in 0..<config.count
        {
            let val = self.config[index]
            let attribute_code = val["attribute_code"]
            let value = self.attributeModelListSelected.first { attributeModelConfig in
                attributeModelConfig.attribute_code == attribute_code
            }
            if(value != nil){
                attributeModelList.append(value!)
            }else{
                let attributeModelConfig = AttributeModelConfig()
                attributeModelConfig.title = val["title"]
                attributeModelConfig.attribute_code = attribute_code
                attributeModelConfig.attribute_Id = val["value"]
                attributeModelConfig.attribute_Label = val["label"]
                for attributeArray in attributes.arrayValue {
                    if(attributeArray["name"].stringValue == attributeModelConfig.attribute_code)
                    {
                        for value in attributeArray["values"].arrayValue{
                            let attributeChildModelConfig = AttributeModelConfig()
                            if(!value["label"].stringValue.isEmpty && !value["value"].stringValue.isEmpty){
                                attributeChildModelConfig.label = value["label"].stringValue
                                attributeChildModelConfig.value = value["value"].stringValue
                                attributeChildModelConfig.title = attributeModelConfig.title
                                attributeChildModelConfig.attribute_code = attributeModelConfig.attribute_code
                                attributeChildModelConfig.attribute_Id = attributeModelConfig.attribute_Id
                                attributeChildModelConfig.attribute_Label = attributeModelConfig.attribute_Label
                                attributeModelConfig.attributeArray.append(attributeChildModelConfig)
                            }
                        }
                    }
                }
                attributeModelList.append(attributeModelConfig)
            }
            attributeModelList = attributeModelList.reversed()
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    
    func didTapEdit(_ checkbox: CustomCheckbox) {
            print("Edit tapped for: \(checkbox.title)")
        let attribute = self.attributeModelListSelected[checkbox.tag].attributeArray[checkbox.index]
        self.showInputDialog(title: "Vendor App".localized,
                             subtitle: "Please enter the new value.".localized,
                             actionTitle: "Add".localized,
                             cancelTitle: "Cancel".localized,
                             inputPlaceholder: "New Attribute Value".localized,
                             inputValue: attribute.label,
                             inputKeyboardType: .default, actionHandler:
                                { (input:String?) in
            self.attributeModelListSelected[checkbox.tag].attributeArray[checkbox.index].label = input ?? ""
            self.saveEditAttribute(self.attributeModelListSelected[checkbox.tag].attributeArray[checkbox.index],checkbox.tag,checkbox.index, -1)
            self.groupedProductTableView.reloadData()
        })
    }

        func didTapDelete(_ checkbox: CustomCheckbox) {
            self.saveEditAttribute(self.attributeModelListSelected[checkbox.tag].attributeArray[checkbox.index], checkbox.tag, checkbox.index,checkbox.index)
        }
    
    func checkboxChanged(_ checkbox: CustomCheckbox) {
        if(stepCount == 0){
            self.attributeModelList[checkbox.index].isChecked = checkbox.isChecked
        }else if(stepCount == 1){
            self.attributeModelListSelected[checkbox.tag].attributeArray[checkbox.index].isChecked = checkbox.isChecked
        }
        print("Delete tapped for: \(checkbox.title)")
    }
    
    @IBAction func backbtntpd(_ sender: Any) {
        if(stepCount > 0){
            stepCount -= 1
            self.groupedProductTableView.reloadData()
        }
    }
    
    func generateCombinations<T>(_ matrix: [[T]]) -> [[T]] {
        guard !matrix.isEmpty else { return [] }

        return matrix.reduce([[]]) { acc, curr in
            var result: [[T]] = []
            for a in acc {
                for c in curr {
                    result.append(a + [c])
                }
            }
            return result
        }
    }
    
    func generateAttributeWithValueCombinations(from matrix: [[AttributeModelConfig]], current: [String] = [], index: Int = 0) -> [String] {
        guard index < matrix.count else {
            return [current.joined(separator: "::!")]
        }
        
        var results = [String]()
        for item in matrix[index] {
            let newCombination = current + ["\(item.attribute_code ?? ""):;\(item.value ?? "")"]
            results += generateAttributeWithValueCombinations(from: matrix, current: newCombination, index: index + 1)
        }
        
        return results
    }
    
    func generateCombinations(from matrix: [[AttributeModelConfig]], current: [String] = [], index: Int = 0) -> [String] {
        guard index < matrix.count else {
            return [current.joined(separator: "::! ")]
        }
    
        var results = [String]()
        for item in matrix[index] {
            let newCombination = current + ["\(item.title ?? ""):;\(item.label ?? "")"]
            results += generateCombinations(from: matrix, current: newCombination, index: index + 1)
        }
        
        return results
    }
    
    func generatePriceCombinations(from matrix: [[AttributeModelConfig]], current: [String] = [], index: Int = 0) -> [String] {
        guard index < matrix.count else {
            return current
        }
        var results = [String]()
        for item in matrix[index] {
            var newCombination = current
            if(!(item.price ?? "").isEmpty && self.attributeModelListSelected.first?.thirdtabPriceOption == "ApplyAttribute" && self.attributeModelListSelected.first?.thirdtabPriceAttributeIndex == item.thirdtabPriceAttributeIndex){
                newCombination = [item.price ?? ""]
            }else if(self.attributeModelListSelected.first?.thirdtabPriceOption == "Single"){
                newCombination = [self.attributeModelListSelected.first?.price ?? ""]
            }
            results += generatePriceCombinations(from: matrix, current: newCombination, index: index + 1)
        }
        return results.filter { name in
            !name.isEmpty
        }
    }
    
    func generateQuantiyCombinations(from matrix: [[AttributeModelConfig]], current: [String] = [], index: Int = 0) -> [String] {
        guard index < matrix.count else {
            return current
        }
        var results = [String]()
        for item in matrix[index] {
            var newCombination = current
            if(!(item.quantity ?? "").isEmpty && self.attributeModelListSelected.first?.thirdtabQuantityOption == "ApplyAttribute" && self.attributeModelListSelected.first?.thirdtabQuantityAttributeIndex == item.thirdtabQuantityAttributeIndex){
                newCombination = [item.quantity ?? ""]
            }else if(self.attributeModelListSelected.first?.thirdtabQuantityOption == "Single"){
                newCombination = [self.attributeModelListSelected.first?.quantity ?? ""]
            }
            results += generateQuantiyCombinations(from: matrix, current: newCombination, index: index + 1)
        }
        return results.filter { name in
            !name.isEmpty
        }
    }
    
    func generateImageCombinations(from matrix: [[AttributeModelConfig]], current: [UIImage?] = [], index: Int = 0) -> [UIImage?] {
        guard index < matrix.count else {
            return current
        }
        var results = [UIImage?]()
        for item in matrix[index] {
            var newCombination = current
            if( item.image != nil  && self.attributeModelListSelected.first?.thirdtabImageOption == "ApplyAttribute" && self.attributeModelListSelected.first?.thirdtabImageAttributeIndex == item.thirdtabImageAttributeIndex){
                newCombination = [item.image]
            }else if(self.attributeModelListSelected.first?.thirdtabImageOption == "Single"){
                newCombination = [self.attributeModelListSelected.first?.image]
            }
            results += generateImageCombinations(from: matrix, current: newCombination, index: index + 1)
        }
        return results.filter { image in
            image != nil
        }
    }
    
    
    @IBAction func nextbtnp(_ sender: Any) {
        if(stepCount < 3){
            if(stepCount <= 0){
                attributeModelListSelected = self.attributeModelList.filter({ attributeModelConfig in
                    attributeModelConfig.isChecked
                })
            }
            if(attributeModelListSelected.count == 0)
            {
                self.view.showToastMsg(NSLocalizedString("Please Select Attribute First!", comment: ""));
                return
            }else if(stepCount == 1){
                for attributeModelListSelect in attributeModelListSelected {
                    let arr = attributeModelListSelect.attributeArray.filter { attributeModelConfig in
                                                attributeModelConfig.isChecked
                                            }
                    if(arr.count <= 0){
                        self.view.showToastMsg(NSLocalizedString("Select options for all attributes or remove unused attributes.", comment: ""));
                        return
                    }
                }
            }
            if(stepCount == 2){
                for attributeModelListSelect in attributeModelListSelected {
                    if(attributeModelListSelect.thirdtabImageOption  == "Single"){
                        if(self.attributeModelListSelected.first?.image == nil && self.attributeModelListSelected.first?.imageUrl == nil){
                            self.view.showToastMsg(NSLocalizedString("Please choose image.", comment: ""));
                            return
                        }
                    }else if(attributeModelListSelect.thirdtabImageOption  == "ApplyAttribute"){
                        if(self.attributeModelListSelected.first?.thirdtabImageAttributeIndex == 0){
                            self.view.showToastMsg(NSLocalizedString("Please select attribute for images section.", comment: ""));
                            return
                        }else{
                            let arr = self.attributeModelListSelected[(self.attributeModelListSelected.first?.thirdtabImageAttributeIndex ?? 0)-1].attributeArray.filter { attributeModelConfig in
                                attributeModelConfig.image == nil && attributeModelConfig.imageUrl == nil && attributeModelConfig.isChecked
                            }
                            if(arr.count>0){
                                self.view.showToastMsg(NSLocalizedString("Please select images for your attribute.", comment: ""));
                                return
                            }
                        }
                    }
                    
                    if(attributeModelListSelect.thirdtabPriceOption  == "Single"){
                        if(self.attributeModelListSelected.first?.price == nil){
                            self.view.showToastMsg(NSLocalizedString("Please fill in the values for price section.", comment: ""));
                            return
                        }
                    }else if(attributeModelListSelect.thirdtabPriceOption  == "ApplyAttribute"){
                        if(self.attributeModelListSelected.first?.thirdtabPriceAttributeIndex == 0){
                            self.view.showToastMsg(NSLocalizedString("Please select attribute for price section.", comment: ""));
                            return
                        }else{
                            let arr = self.attributeModelListSelected[(self.attributeModelListSelected.first?.thirdtabPriceAttributeIndex ?? 0)-1].attributeArray.filter { attributeModelConfig in
                                attributeModelConfig.price == nil && attributeModelConfig.isChecked
                            }
                            if(arr.count>0){
                                self.view.showToastMsg(NSLocalizedString("Please fill-in correct values.", comment: ""));
                                return
                            }
                        }
                    }
                    
                    if(attributeModelListSelect.thirdtabQuantityOption  == "Single"){
                        if(self.attributeModelListSelected.first?.quantity == nil){
                            self.view.showToastMsg(NSLocalizedString("Please fill in the values for quantity section.", comment: ""));
                            return
                        }
                    }else if(attributeModelListSelect.thirdtabQuantityOption  == "ApplyAttribute"){
                        if(self.attributeModelListSelected.first?.thirdtabQuantityAttributeIndex == 0){
                            self.view.showToastMsg(NSLocalizedString("Please select attribute for quantity section.", comment: ""));
                            return
                        }else{
                            let arr = self.attributeModelListSelected[(self.attributeModelListSelected.first?.thirdtabQuantityAttributeIndex ?? 0)-1].attributeArray.filter { attributeModelConfig in
                                attributeModelConfig.quantity == nil && attributeModelConfig.isChecked
                            }
                            if(arr.count>0){
                                self.view.showToastMsg(NSLocalizedString("Please fill-in correct values.", comment: ""));
                                return
                            }
                        }
                    }
                }
            }
            stepCount += 1
            if(stepCount == 3){
//                let matrix = [
//                    ["DamagedCondition", "Red"],
//                    ["Blue", "Red"],
//                    ["3 mth", "6 mth"]
//                ]
                var matrix:[[AttributeModelConfig]] = [[AttributeModelConfig]]()
                for attributeModelListSelect in attributeModelListSelected {
                    let arr = attributeModelListSelect.attributeArray.filter { attributeModelConfig in
                                                attributeModelConfig.isChecked
                                            }
                    matrix.append(arr)
                }
                self.attributeModelListFinal.removeAll()
                let combinationsAttributeWithValue = generateAttributeWithValueCombinations(from: matrix)
                let combinationsAttribute = generateCombinations(from: matrix)
                let combinationsPrice = generatePriceCombinations(from: matrix)
                let combinationsQuantity = generateQuantiyCombinations(from: matrix)
                let combinationsImage = generateImageCombinations(from: matrix)
                for index in 0..<combinationsAttribute.count{
                    let finalModel = AttributeModelConfig()
                    print(combinationsAttributeWithValue[index])
                    finalModel.attributeWithValue = combinationsAttributeWithValue[index]
                    var title = combinationsAttribute[index]
                    var skuLabel:String = ""
                    let arr = title.components(separatedBy:"::! ")
                    arr.forEach { part in
                        let subArr = part.components(separatedBy: ":;")
                        if(subArr.count>1){
                            if(skuLabel.isEmpty){
                                skuLabel = subArr[1].replacingOccurrences(of: " ", with: "-")
                            }else{
                                skuLabel = "\(skuLabel)-\(subArr[1].replacingOccurrences(of: " ", with: "-"))"
                            }
                        }
                    }
                    finalModel.namelabel = skuLabel
                    finalModel.skulabel = skuLabel
                    title = title.replacingOccurrences(of: "::! ", with: " ")
                    title = title.replacingOccurrences(of: ":;", with: ":")
                    finalModel.title = title
                    if(index < combinationsPrice.count){
                        finalModel.price = combinationsPrice[index]
                    }
                    if(index < combinationsQuantity.count){
                        finalModel.quantity = combinationsQuantity[index]
                    }
                    if(index < combinationsImage.count){
                        finalModel.image = combinationsImage[index]
                    }
                    let attributeValueArr = (finalModel.attributeWithValue ?? "").components(separatedBy: "::!")
                    var attributeIdsArr: [String] = [String]()
                    attributeValueArr.forEach { attribute in
                        let subArr = attribute.components(separatedBy: ":;")
                        if(subArr.count>1){
                            attributeIdsArr.append(subArr[1])
                        }
                    }
                    self.attributeModelEditListFinal.forEach { attributeModelConfig in
                        let isEqual = Set(attributeIdsArr) == Set(attributeModelConfig.attributeIdsArr)
                        if(isEqual){
                            finalModel.skulabel = attributeModelConfig.assicoiatProduct_sku
                            finalModel.namelabel = attributeModelConfig.namelabel
                            finalModel.assicoiatProduct_sku = attributeModelConfig.assicoiatProduct_sku
                            finalModel.assicoiatProduct_id = attributeModelConfig.assicoiatProduct_id
                            finalModel.imageUrl = attributeModelConfig.imageUrl
                            if(finalModel.price == nil){
                                finalModel.price = attributeModelConfig.price
                            }
                            if(finalModel.quantity == nil){
                                finalModel.quantity = attributeModelConfig.quantity
                            }
                            if(finalModel.weight == nil){
                                finalModel.weight = attributeModelConfig.weight
                            }
                        }
                    }
                    
                    self.attributeModelListFinal.append(finalModel)
                }

            }
            self.groupedProductTableView.reloadData()
        }else if(stepCount >= 3){
            self.handler?(self.attributeModelListSelected,self.attributeModelListFinal)
            self.navigationController?.popViewController(animated: true)
        }
    }
    
    @objc func continuePressed(_ sender:UIButton)
    {
        if(configSelected.count == 0)
        {
            self.view.showToastMsg(NSLocalizedString("Please Select Attribute First!", comment: ""));
            return;
        }
        print("savePressed");
      //  let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "productDetailsViewController") as! ProductDetailsViewController;
        
        let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "configProductSelectionController") as! ConfigProductSelectionController;
       
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
        viewcontrollor.configSelected = self.configSelected;//imp//unique
//        viewcontrollor.categoryJson = self.categoryJson
//        if(selectedFiltersIdsString != "")
//        {
//            viewcontrollor.selectedFiltersIdsString = selectedFiltersIdsString;
//        }
//        if(selectedWebsitesIdsString != "")
//        {
//            viewcontrollor.selectedWebsitesIdsString = selectedWebsitesIdsString;
//        }
        self.navigationController?.setViewControllers([viewcontrollor], animated: true)
    }
    
    // MARK: - Table view data source
    
    @objc func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if(stepCount == 0){
            return 1
        }else if(stepCount == 1){
            return self.attributeModelListSelected.count
        }else if(stepCount == 2){
            return 3
        }else if(stepCount == 3){
            return 1
        }
        return 0
    }
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = groupedProductTableView.dequeueReusableCell(withIdentifier: "longSingleCell", for: indexPath) as! LongSingleTableViewCell;
        cell.verticalStackView.removeAllArrangedSubviews()
        let heightOfTextView = self.calculateHeightForString(self.StringToRenderOnTop);
        
        let singleTextView = SingleTextView();
        singleTextView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        singleTextView.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: heightOfTextView);
        singleTextView.center.x = self.view.center.x;
        heightToUse += heightOfTextView;
        heightToUse += padding;
        
        singleTextView.infoTxtView.text = self.StringToRenderOnTop;
        
       // cell.mainWrapperView.addSubview(singleTextView);
        
        if(stepCount == 0){
            self.topLabel.text = "Step 1: Select Attributes".localized
            for index in 0..<attributeModelList.count
            {
                let val = self.attributeModelList[index]
                let checkbox: CustomCheckbox = CustomCheckbox(frame: CGRect(x: 0, y: 0, width: screenSize.width,height: 30))
                checkbox.title = val.title ?? ""
                checkbox.delegate = self
                checkbox.index = index
                checkbox.isChecked = val.isChecked
                checkbox.hideEditDeleteButtons()
                cell.verticalStackView.addArrangedSubview(checkbox)
            }
        }else if(stepCount == 1){
            self.topLabel.text = "Step 2: Attribute Values\nSelect values from each attribute to include in this product. Each unique combination of values creates a unigue product SKU.".localized
            let attribteTItleView = AttributeTitleView()
            let attributeModel = self.attributeModelListSelected[indexPath.row]
            attribteTItleView.title = attributeModel.title ?? ""
            cell.verticalStackView.addArrangedSubview(attribteTItleView)
            attribteTItleView.deleteButton.add(for: .touchUpInside) {
                self.attributeModelListSelected.remove(at: indexPath.row)
                self.groupedProductTableView.reloadData()
            }
            attribteTItleView.selectButton.add(for: .touchUpInside) {
                for index in 0..<attributeModel.attributeArray.count
                {
                    attributeModel.attributeArray[index].isChecked = true
                }
                self.groupedProductTableView.reloadData()
            }
            attribteTItleView.deSelectButton.add(for: .touchUpInside) {
                for index in 0..<attributeModel.attributeArray.count
                {
                    attributeModel.attributeArray[index].isChecked = false
                }
                self.groupedProductTableView.reloadData()
            }
            for index in 0..<attributeModel.attributeArray.count
            {
                let checkbox: CustomCheckbox = CustomCheckbox(frame: CGRect(x: 0, y: 0, width: screenSize.width,height: 30))
                if(!attributeModel.attributeArray[index].isUserCustom){
                    checkbox.hideEditDeleteButtons()
                }
                checkbox.title = attributeModel.attributeArray[index].label ?? ""
                checkbox.delegate = self
                checkbox.index = index
                checkbox.tag = indexPath.row
                checkbox.isChecked = attributeModel.attributeArray[index].isChecked
                cell.verticalStackView.addArrangedSubview(checkbox)
            }
            let addNewValue: UIButton = UIButton(type: .system)
            addNewValue.setTitleColor(.white, for: .normal)
            addNewValue.layer.cornerRadius = 5
            addNewValue.clipsToBounds = true
            addNewValue.backgroundColor = UIColor(hexString: "#002F63")
            addNewValue.setTitle("Create New Value".localized, for: .normal)
            addNewValue.add(for: .touchUpInside) {
                self.showInputDialog(title: "Vendor App".localized,
                                     subtitle: "Please enter the new value.".localized,
                                     actionTitle: "Add".localized,
                                     cancelTitle: "Cancel".localized,
                                     inputPlaceholder: "New Attribute Value".localized,
                                     inputKeyboardType: .default, actionHandler:
                                        { (input:String?) in
                    let attributeChildModelConfig = AttributeModelConfig()
                    attributeChildModelConfig.label = input
                    attributeChildModelConfig.isUserCustom = true
                    attributeChildModelConfig.attribute_Id = self.attributeModelListSelected[indexPath.row].attribute_Id
                    attributeChildModelConfig.attribute_Label = self.attributeModelListSelected[indexPath.row].attribute_Label
                    self.saveEditAttribute(attributeChildModelConfig,indexPath.row,-1,-1)
                    
                })
            }
            cell.verticalStackView.addArrangedSubview(addNewValue)
        }else if(stepCount == 2){
            self.topLabel.text = "Step 3: Bulk Images, Price and Quantity\nBased on your selections 3 new products will be created. Use this step to customize images and price for your new products.".localized
            let keys = ["Images".localized,"Price".localized,"Quantity".localized]
            let titleLabel = UILabel()
            titleLabel.font = UIFont.boldSystemFont(ofSize: 16)
            titleLabel.textColor = .black
            titleLabel.text = keys[indexPath.row]
            
            let dropdownField: DropdownTextField = DropdownTextField(frame: CGRect(x: 0, y: 0, width: screenSize.width,height: 40))
            dropdownField.layer.cornerRadius = 5
            dropdownField.layer.borderWidth = 1
            dropdownField.layer.borderColor = UIColor.lightGray.cgColor
            var optionArray:[String] = [String]()
            optionArray.append("Select".localized)
            dropdownField.text = "Select".localized
            for attributeModelListSelect in attributeModelListSelected {
                optionArray.append(attributeModelListSelect.title ?? "")
            }
            dropdownField.optionArray = optionArray
            
            if(indexPath.row == 0){
                dropdownField.text = optionArray[self.attributeModelListSelected[0].thirdtabImageAttributeIndex]
            }else if(indexPath.row == 1){
                dropdownField.text = optionArray[self.attributeModelListSelected[0].thirdtabPriceAttributeIndex]
            }else if(indexPath.row == 2){
                dropdownField.text = optionArray[self.attributeModelListSelected[0].thirdtabQuantityAttributeIndex]
            }
            dropdownField.didSelect(completion: { selectedText, indexValue, id in
                if(indexPath.row == 0){
                    for index in 0..<self.attributeModelListSelected.count
                    {
                        self.attributeModelListSelected[index].thirdtabImageAttributeIndex = indexValue
                    }
                }else if(indexPath.row == 1){
                    for index in 0..<self.attributeModelListSelected.count
                    {
                        self.attributeModelListSelected[index].thirdtabPriceAttributeIndex = indexValue
                    }
                }else if(indexPath.row == 2){
                    for index in 0..<self.attributeModelListSelected.count
                    {
                        self.attributeModelListSelected[index].thirdtabQuantityAttributeIndex = indexValue
                    }
                }
                self.groupedProductTableView.reloadData()
            })
            dropdownField.isSearchEnable = false
            let droptitleLabel = UILabel()
            droptitleLabel.font = UIFont.systemFont(ofSize: 14)
            droptitleLabel.textColor = .black
            droptitleLabel.text = "Select attribute".localized
            // StackView Layout
            let stackView = UIStackView(arrangedSubviews: [droptitleLabel,dropdownField])
            let boolvalue = self.thirdtabListBool[indexPath.row] ?? []
            let stringvalue = self.thirdtabList[keys[indexPath.row]] ?? []
            let radioButton1: RadioButtonControl = RadioButtonControl(frame: CGRect(x: 0, y: 0, width: screenSize.width,height: 30))
            radioButton1.title = stringvalue[0]
            radioButton1.isSelectedRadio = boolvalue[0]
            let radioButton2: RadioButtonControl = RadioButtonControl(frame: CGRect(x: 0, y: 0, width: screenSize.width,height: 30))
            radioButton2.title = stringvalue[1]
            radioButton2.isSelectedRadio = boolvalue[1]
            let radioButton3: RadioButtonControl = RadioButtonControl(frame: CGRect(x: 0, y: 0, width: screenSize.width,height: 30))
            radioButton3.title = stringvalue[2]
            radioButton3.isSelectedRadio = boolvalue[2]
            let uploadImageView = UploadImageView();
            radioButton1.onValueChanged = { sender in
                [radioButton1, radioButton2, radioButton3].forEach { $0?.isSelectedRadio = ($0 == sender) }
                self.thirdtabListBool[indexPath.row] = [true,false,false]
                if(indexPath.row == 0){
                    for index in 0..<self.attributeModelListSelected.count
                    {
                        self.attributeModelListSelected[index].thirdtabImageOption = "Single"
                    }
                }else if(indexPath.row == 1){
                    for index in 0..<self.attributeModelListSelected.count
                    {
                        self.attributeModelListSelected[index].thirdtabPriceOption = "Single"
                    }
                }else if(indexPath.row == 2){
                    for index in 0..<self.attributeModelListSelected.count
                    {
                        self.attributeModelListSelected[index].thirdtabQuantityOption = "Single"
                    }
                }
                tableView.reloadData()
            }
            radioButton2.onValueChanged = { sender in
                [radioButton1, radioButton2, radioButton3].forEach { $0?.isSelectedRadio = ($0 == sender) }
                stackView.isHidden = false
                self.thirdtabListBool[indexPath.row] = [false,true,false]
                if(indexPath.row == 0){
                    for index in 0..<self.attributeModelListSelected.count
                    {
                        self.attributeModelListSelected[index].thirdtabImageOption = "ApplyAttribute"
                    }
                }else if(indexPath.row == 1){
                    for index in 0..<self.attributeModelListSelected.count
                    {
                        self.attributeModelListSelected[index].thirdtabPriceOption = "ApplyAttribute"
                    }
                }else if(indexPath.row == 2){
                    for index in 0..<self.attributeModelListSelected.count
                    {
                        self.attributeModelListSelected[index].thirdtabQuantityOption = "ApplyAttribute"
                    }
                }
                tableView.reloadData()
            }
            radioButton3.onValueChanged = { sender in
                [radioButton1, radioButton2, radioButton3].forEach { $0?.isSelectedRadio = ($0 == sender) }
                uploadImageView.isHidden = true
                stackView.isHidden = true
                self.thirdtabListBool[indexPath.row] = [false,false,true]
                if(indexPath.row == 0){
                    for index in 0..<self.attributeModelListSelected.count
                    {
                        self.attributeModelListSelected[index].thirdtabImageOption = "Skip"
                    }
                }else if(indexPath.row == 1){
                    for index in 0..<self.attributeModelListSelected.count
                    {
                        self.attributeModelListSelected[index].thirdtabPriceOption = "Skip"
                    }
                }else if(indexPath.row == 2){
                    for index in 0..<self.attributeModelListSelected.count
                    {
                        self.attributeModelListSelected[index].thirdtabQuantityOption = "Skip"
                    }
                }
                tableView.reloadData()
            }
            designImageView(uploadImageView,nil)
            stackView.frame = CGRect(x: 0, y: 0, width: screenSize.width,height: 60)
            stackView.axis = .vertical
            stackView.spacing = 5
            stackView.alignment = .leading
            stackView.translatesAutoresizingMaskIntoConstraints = false
            cell.verticalStackView.addArrangedSubview(titleLabel)
            cell.verticalStackView.addArrangedSubview(radioButton1)
            cell.verticalStackView.addArrangedSubview(radioButton2)
            cell.verticalStackView.addArrangedSubview(radioButton3)
                if(radioButton2.isSelectedRadio){
                cell.verticalStackView.addArrangedSubview(stackView)
            }
            
            if(indexPath.row > 0 && radioButton1.isSelectedRadio){
                let viewForTextFeild = Label_TextFieldComboView()
                viewForTextFeild.textLabel.keyboardType = .numberPad
                if(indexPath.row == 1){
                    viewForTextFeild.textLabel.text = self.attributeModelListSelected[0].price
                }else{
                    viewForTextFeild.textLabel.text = self.attributeModelListSelected[0].quantity
                }
                viewForTextFeild.headingLabel.text = "\(keys[indexPath.row])" + "*"
                viewForTextFeild.translatesAutoresizingMaskIntoConstraints = false
                let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 75)
                viewForTextFeild.addConstraint(heightConstrain)
                cell.verticalStackView.addArrangedSubview(viewForTextFeild)
                NotificationCenter.default.publisher(for: UITextField.textDidChangeNotification, object: viewForTextFeild.textLabel)
                           .compactMap { ($0.object as? UITextField)?.text }
                           .sink { text in
                               if(indexPath.row == 1){
                                   for index in 0..<self.attributeModelListSelected.count
                                   {
                                       self.attributeModelListSelected[index].price = text
                                   }
                               }else{
                                   for index in 0..<self.attributeModelListSelected.count
                                   {
                                       self.attributeModelListSelected[index].quantity = text
                                   }
                               }
                           }
                           .store(in: &cancellables)
            }else if(radioButton1.isSelectedRadio){
                uploadImageView.imgView.image = self.attributeModelListSelected.first?.image ?? UIImage(named: "placeholder")
                cell.verticalStackView.addArrangedSubview(uploadImageView)
            }
            if(radioButton2.isSelectedRadio){
                if(indexPath.row == 0){
                    if(self.attributeModelListSelected[0].thirdtabImageAttributeIndex > 0){
                        let attributeModel = self.attributeModelListSelected[self.attributeModelListSelected[0].thirdtabImageAttributeIndex-1]
                        for attribute in attributeModel.attributeArray {
                            if(attribute.isChecked){
                                let childUploadImageView = UploadImageView();
                                designImageView(childUploadImageView,attribute)
                                attribute.thirdtabImageAttributeIndex = self.attributeModelListSelected[0].thirdtabImageAttributeIndex
                                childUploadImageView.fileNamelbl.text = attribute.label
                                childUploadImageView.fileNamelbl.isHidden = false
                                childUploadImageView.labelYAxis.constant = -30
                                cell.verticalStackView.addArrangedSubview(childUploadImageView)
                            }
                        }
                    }
                }else if(indexPath.row == 1){
                    if(self.attributeModelListSelected[0].thirdtabPriceAttributeIndex > 0){
                        let attributeModel = self.attributeModelListSelected[self.attributeModelListSelected[0].thirdtabPriceAttributeIndex-1]
                        for attribute in attributeModel.attributeArray {
                            if(attribute.isChecked){
                                attribute.thirdtabPriceAttributeIndex = self.attributeModelListSelected[0].thirdtabPriceAttributeIndex
                                let viewForTextFeild = Label_TextFieldComboView()
                                viewForTextFeild.textLabel.keyboardType = .numberPad
                                viewForTextFeild.textLabel.text = attribute.price
                                viewForTextFeild.headingLabel.text = "\(attribute.label ?? "")" + "*"
                                viewForTextFeild.translatesAutoresizingMaskIntoConstraints = false
                                let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 75)
                                viewForTextFeild.addConstraint(heightConstrain)
                                cell.verticalStackView.addArrangedSubview(viewForTextFeild)
                                NotificationCenter.default.publisher(for: UITextField.textDidChangeNotification, object: viewForTextFeild.textLabel)
                                           .compactMap { ($0.object as? UITextField)?.text }
                                           .sink { text in
                                               attribute.price = text
                                           }
                                           .store(in: &cancellables)
                            }
                        }
                    }
                }else if(indexPath.row == 2){
                    if(self.attributeModelListSelected[0].thirdtabQuantityAttributeIndex > 0){
                        let attributeModel = self.attributeModelListSelected[self.attributeModelListSelected[0].thirdtabQuantityAttributeIndex-1]
                        for attribute in attributeModel.attributeArray {
                            if(attribute.isChecked){
                                attribute.thirdtabQuantityAttributeIndex = self.attributeModelListSelected[0].thirdtabQuantityAttributeIndex
                                let viewForTextFeild = Label_TextFieldComboView()
                                viewForTextFeild.textLabel.keyboardType = .numberPad
                                viewForTextFeild.textLabel.text = attribute.quantity
                                viewForTextFeild.headingLabel.text = "\(attribute.label ?? "")" + "*"
                                viewForTextFeild.translatesAutoresizingMaskIntoConstraints = false
                                let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 75)
                                viewForTextFeild.addConstraint(heightConstrain)
                                cell.verticalStackView.addArrangedSubview(viewForTextFeild)
                                NotificationCenter.default.publisher(for: UITextField.textDidChangeNotification, object: viewForTextFeild.textLabel)
                                           .compactMap { ($0.object as? UITextField)?.text }
                                           .sink { text in
                                               attribute.quantity = text
                                           }
                                           .store(in: &cancellables)
                            }
                        }
                    }
                }
                
            }
        }else if(stepCount == 3){
            cell.separatorView.isHidden = true
            self.topLabel.text = "Step 4: Summary".localized
            for index in 0..<attributeModelListFinal.count
            {
                let val = self.attributeModelListFinal[index]
                let configSummaryView: ConfigSummaryView = ConfigSummaryView(frame: CGRect(x: 0, y: 0, width: screenSize.width,height: 400))
                configSummaryView.skuTxtField.isHidden = true
                configSummaryView.skuTxtLabel.text = val.skulabel ?? ""
                configSummaryView.attributeLabel.text = val.title ?? ""
                configSummaryView.quantityTxtField.text = val.quantity ?? ""
                configSummaryView.priceTxtField.text = val.price ?? ""
                if(val.image != nil){
                    configSummaryView.imageView.image = val.image
                }else if(val.imageUrl != nil){
                    configSummaryView.imageView.sd_setImage(with: URL(string: val.imageUrl ?? ""), placeholderImage: UIImage(named: "placeholder"))
                }
                configSummaryView.statusHeight.constant = 0
                configSummaryView.weightHeight.constant = 0
                configSummaryView.nameHeight.constant = 0
                configSummaryView.quantityTxtField.isUserInteractionEnabled = false
                configSummaryView.quantityTxtField.borderStyle = .none
                configSummaryView.priceTxtField.isUserInteractionEnabled = false
                configSummaryView.priceTxtField.borderStyle = .none
                cell.verticalStackView.addArrangedSubview(configSummaryView)
            }
        }
        
        return cell;
    }
    
    func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
        
    }
    
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
    
    @objc func checkboxButtonClicked(_ sender:UIButton)
    {
        print("checkboxButtonClicked");
        
        let key = self.config[sender.tag-10]["value"]!;
        let value = self.config[sender.tag-10]["attribute_code"]!;
        
        if(sender.imageView!.image == UIImage(named:"unchecked_checkbox"))
        {
            sender.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
            self.configSelected[key] = value
        }
        else
        {
            sender.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
            if(configSelected[key] != nil)
            {
                configSelected[key] = "";
            }
        }
        
        print(self.configSelected);
        
    }
    @objc func calculateHeightForString(_ inString:String) -> CGFloat
    {
        let messageString = inString
        //var attributes = [UIFont(): UIFont.systemFontOfSize(15.0)]
        let attrString:NSAttributedString? = NSAttributedString(string: messageString, attributes: nil)
        let rect:CGRect = attrString!.boundingRect(with: CGSize(width: 160.0,height: CGFloat.greatestFiniteMagnitude), options: NSStringDrawingOptions.usesLineFragmentOrigin, context:nil )//hear u will get nearer height not the exact value
        let requredSize:CGRect = rect
        return requredSize.height  //to include button's in your tableview
    }
    
    func showInputDialog(title:String? = nil,
                             subtitle:String? = nil,
                             actionTitle:String? = "Add",
                             cancelTitle:String? = "Cancel",
                             inputPlaceholder:String? = nil,
                             inputValue:String? = nil,
                             inputKeyboardType:UIKeyboardType = UIKeyboardType.default,
                             cancelHandler: ((UIAlertAction) -> Swift.Void)? = nil,
                             actionHandler: ((_ text: String?) -> Void)? = nil) {
            
            let alert = UIAlertController(title: title, message: subtitle, preferredStyle: .alert)
            alert.addTextField { (textField:UITextField) in
                textField.placeholder = inputPlaceholder
                textField.keyboardType = inputKeyboardType
                textField.text = inputValue
            }
            alert.addAction(UIAlertAction(title: actionTitle, style: .default, handler: { (action:UIAlertAction) in
                guard let textField =  alert.textFields?.first else {
                    actionHandler?(nil)
                    return
                }
                actionHandler?(textField.text)
            }))
            alert.addAction(UIAlertAction(title: cancelTitle, style: .cancel, handler: cancelHandler))
            
            self.present(alert, animated: true, completion: nil)
        }
    
    func designImageView(_ uploadImageView:UploadImageView,_ attributeModelConfig:AttributeModelConfig?){
        if(attributeModelConfig != nil){
            uploadImageView.imgView.image = attributeModelConfig?.image ?? UIImage(named: "placeholder")
        }
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        PopupLookImprovement.designImageView(uploadImageView.imgView);
        uploadImageView.delButton.addTarget(self, action: #selector(ProductDetailsViewController.removeimgUploadView(_:)), for: UIControl.Event.touchUpInside);
        uploadImageView.delButton.backgroundColor = color
        uploadImageView.delButton.isHidden = true;
        uploadImageView.browseButton.backgroundColor = color
        uploadImageView.takephotobtn.backgroundColor = color
        uploadImageView.takephotobtn.isHidden = false
        uploadImageView.defaultbtnHeight.constant = 0
        let heightConstrain = NSLayoutConstraint(item: uploadImageView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 100)
        uploadImageView.addConstraint(heightConstrain)
        uploadImageView.makeDefaultButton.isHidden = true
        uploadImageView.takephotobtn.removeTarget(nil, action: nil, for: UIControlEvents.allEvents)
        uploadImageView.takephotobtn.add(for: .touchUpInside) {
            self.imagePicker.presentImagePicker(from: self, sourceType: .camera) { image in
                        if let image = image {
                            uploadImageView.imgView.image = image
                            if(attributeModelConfig == nil){
                                for attributeModelListSelect in self.attributeModelListSelected {
                                    attributeModelListSelect.image = image
                                }
                            }else{
                                attributeModelConfig?.image = image
                            }
                        } else {
                            print("No image selected")
                        }
                    }
        }
        uploadImageView.browseButton.add(for: .touchUpInside) {
            self.imagePicker.presentImagePicker(from: self, sourceType: .photoLibrary) { image in
                        if let image = image {
                            uploadImageView.imgView.image = image
                            if(attributeModelConfig == nil){
                                for attributeModelListSelect in self.attributeModelListSelected {
                                    attributeModelListSelect.image = image
                                }
                            }else{
                                attributeModelConfig?.image = image
                            }
                        } else {
                            print("No image selected")
                        }
                    }
        }
                
                PopupLookImprovement.designButton(uploadImageView.browseButton);
                PopupLookImprovement.designButton(uploadImageView.delButton);
                
                uploadImageView.makeCard(uploadImageView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
    }
    
    func saveEditAttribute(_ config:AttributeModelConfig,_ mainIndex:Int,_ childIndex:Int,_ deleteIndex:Int){
       
        var optionValue = [String:Any]()
        var value = [String:[String]]()
        var OptionLabelvalue = [String]()
        optionValue["attribute_code"] = self.attributeModelListSelected[mainIndex].attribute_code
        for attribute in  self.attributeModelListSelected[mainIndex].attributeArray{
            value[attribute.value ?? ""] = [attribute.label ?? ""]
            OptionLabelvalue.append(attribute.value ?? "")
        }
        if(childIndex == -1){
            value["option_\(value.count+1)"] = [config.label ?? ""]
        }else if(deleteIndex != -1){
            optionValue["delete"] = [
                config.value ?? "":"1"
            ]
        }
        optionValue["option"] = [
            "value":value
        ]
        var itemsString = String()
        
        do{
            let JSONData = try JSONSerialization.data(
                withJSONObject: optionValue ,
                options: JSONSerialization.WritingOptions(rawValue: 0))
            itemsString = NSString(data: JSONData,
                encoding: String.Encoding.utf8.rawValue)! as String
        }
        catch{
            print("error in data encoding")
        }
        print(itemsString)
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString  += "&attribute_data="+itemsString//.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!
        postString  += "&attribute_id=" + (self.attributeModelListSelected[mainIndex].attribute_Id ?? "")
        print(postString)
        let request = NSMutableURLRequest(url: URL(string: "\(settings.baseUrl)vproductattributeapi/index/saveAttribute")!);
        request.httpMethod = "POST";
        
        request.httpBody = postString.data(using: String.Encoding.utf8);
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "User-Agent")
        request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
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
                    print("jsonData \(jsonData)");
                    Alert_File.removeLoadingIndicator(self);
                    let message = jsonData["data"]["message"].stringValue
                    if(jsonData["data"]["success"].stringValue != "true")
                    {
                        let title = "";
                        Alert_File.showValidationError(self,title:title,message:message);
                        return;
                    }
                    if(childIndex == -1){
                        let optionsArr = jsonData["data"]["option"].arrayValue
                        optionsArr.forEach { option in
                            if(!OptionLabelvalue.contains(option["value"].stringValue)){
                                config.value = option["value"].stringValue
                                self.attributeModelListSelected[mainIndex].attributeArray.append(config)
                                self.groupedProductTableView.reloadData()
                            }
                        }
                    }else if(deleteIndex != 0){
                        self.attributeModelListSelected[mainIndex].attributeArray.remove(at: deleteIndex)
                        self.groupedProductTableView.reloadData()
                    }
            }
        }
        task.resume();
    }
    
}

/*
 //                let combinations = generateCombinations(matrix)
 //
 //                for combo in combinations {
 //                    print(combo)
 //                }
                 
                 
               //  printNodeTree(attributeModelList.reversed())
                 let arr = self.attributeModelList.first?.attributeArray.filter({ attributeModelConfig in
                     attributeModelConfig.isChecked
                 }) ?? []
                // printNode(arr)
 //                for index in stride(from: attributeModelList.count-1, through: 0, by: -1) {
 //                    var attributeName = ""
 //                    for i in stride(from: 0, to: index-1, by: 1) {
 //                        let arr1 = self.attributeModelList[i].attributeArray.filter { attributeModelConfig in
 //                            attributeModelConfig.isChecked
 //                        }
 //                        if(attributeName.isEmpty){
 //                            attributeName = arr1.first?.label ?? ""
 //                        }else{
 //                            attributeName = "\(attributeName) \(arr1.first?.label ?? "")"
 //                        }
 //                    }
 //                    for i in stride(from: index, to: attributeModelList.count, by: 1) {
 //                       // print("\(index) \(i)") // 5,4,3,2,1,0
 //                        let arr1 = self.attributeModelList[i].attributeArray.filter { attributeModelConfig in
 //                            attributeModelConfig.isChecked
 //                        }
 //                        arr1.forEach { attributeModelConfig in
 //                            var finalName = ""
 //                            if(attributeName.isEmpty){
 //                                finalName = attributeModelConfig.label ?? ""
 //                            }else{
 //                                finalName = "\(attributeName) \(attributeModelConfig.label ?? "")"
 //                            }
 //                           // print(finalName)
 //                            print("\(index) \(i)  \(finalName)")
 //                        }
 ////                        if(i >= self.attributeModelList.count-1){
 ////
 ////                            let arr2 = self.attributeModelList[i].attributeArray.filter { attributeModelConfig in
 ////                                attributeModelConfig.isChecked
 ////                            }
 ////
 ////                        }else{
 ////
 ////                        }
 //                    }
 //                }
 //                for config in self.attributeModelList{
 //
 //                }
 
 func printNode(_ nodes: [AttributeModelConfig], level: Int = 0) {
     if(level < self.attributeModelList.count){
         let arr = self.attributeModelList[level].attributeArray.filter({ attributeModelConfig in
             attributeModelConfig.isChecked
         })
         arr.forEach { attributeModelConfig in
             print(attributeModelConfig.label)
         }
         printNode(arr,level: level+1)
     }
 }
 
 func printNodeTree(_ nodes: [AttributeModelConfig], level: Int = 0) {
     for node in nodes {
         let indent = String(repeating: "  ", count: level)
         
         print("\(level)- \(node.title) \(node.label)")
         printNodeTree(node.attributeArray.filter({ attributeModelConfig in
             attributeModelConfig.isChecked
         }), level: level + 1)
     }
     print("\(level)   \(nodes.count)")
 }
 */
