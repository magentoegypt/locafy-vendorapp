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
import SwiftyJSON

class ced_addATTrbutenew: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource {
    @IBOutlet weak var con_width: NSLayoutConstraint!
    @IBOutlet weak var continueTapped: UIButton!
    @IBOutlet weak var propertiesTable: UITableView!
    var textareaSelected = ""
    var textViewHeight = CGFloat()
    var selectedcatalog = String()
    var applyTo  = String()
    var postion  = String()
    var store_label = [String:String]()
    //dropdown datasources
    
    var storeDropdownArray = [String]()
    var yesNoDropdownArray = [String]()
    var scopeDropdownArray = [String]()
    var frontendDropdownArray = [String]()
    var inputTypeDropdownArray = [String]()
    var dropdownValueLabel = [String:String]()
    var dropdownLabelvalue = [String:String]()
    var scopeDropdowndict = [String:String]()
    var frontendDropdowndic = [String:String]()
    var storeDropdownDic = [String:String]()
    var inputTypeDropdowndic = [String:String]()
    
    var edit = false
    var attributeId = ""
    var editAttrData = [String:String]()
    var AdminOption  = [[String:String]]()
    var AddAttrData = [[String:String]]()
    var SAttrData = [[String:String]]()
    
    var optionsData = [JSON]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        propertiesTable.backgroundColor = DynamicColor.ViewBackgroundColor
        self.propertiesTable.delegate = self
        self.propertiesTable.dataSource = self
        self.title = "Properties".localized
        print("AFTERSENDINGATRRIBUTEID---\(attributeId)")
        // Do any additional setup after loading the view.
        continueTapped.setTitle("Continue".localized, for: .normal)
        continueTapped.addTarget(self, action: #selector(ced_addATTrbutenew.continueTonextView(_:)), for: UIControl.Event.touchUpInside)
        ced_navigationBarController().addNavButton(self,str:"back")
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        continueTapped.backgroundColor = color
        print("EDIT----\(edit)")
        self.requestDropdownData()
        if(edit){
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
            self.loadEditdata()
        }
        else{
            //con_width.constant=self.view.bounds.width/2
        }
        
    }
    
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(section == 1){
          if(applyTo == "seletedcat"){
             return 1
          }
            return 0
        }
        print("INSIDEROW")
        return 1
    }
//    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
//        print("hello")
//        let cell=tableView.dequeueReusableCell(withIdentifier: "buttonCell")
//        return cell!
//    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        print("hello")
        if(indexPath.section == 0){
                let cell = propertiesTable.dequeueReusableCell(withIdentifier: "buttonCell") as! ced_buttoncell
                cell.parent = self
                cell.tableView = propertiesTable
            
            cell.l1.text = "Attribute Code".localized
            cell.l2.text = "Scope".localized
            cell.l3.text = "Catalog Input Type for Store Owner".localized
            cell.l4.text = "Default Value".localized
            cell.l5.text = "Unique Value".localized
            cell.l6.text = "Values Required".localized
            cell.l7.text = "Input Validation for Store Owner".localized
            cell.l8.text = "Use To Create Configurable Product".localized
            cell.l9.text = "Add to Column Options".localized
            cell.l10.text = "Use in Filter Options".localized
            
            print(inputTypeDropdownArray)
            cell.frontendDropdownArray = self.frontendDropdownArray
            cell.storeDropdownArray = self.storeDropdownArray
            cell.scopeDropdownArray = self.scopeDropdownArray
            cell.yesNoDropdownArray = self.yesNoDropdownArray
            cell.inputTypeDropdownArray = self.inputTypeDropdownArray
            if !edit{
                cell.scope.setTitle(scopeDropdownArray.first, for: .normal)
                cell.catalog.setTitle(inputTypeDropdownArray.first, for: .normal)
                cell.applyTo.setTitle(yesNoDropdownArray.first, for: .normal)
                cell.valuesRequired.setTitle(yesNoDropdownArray.first, for: .normal)
                cell.inputvalidationStore.setTitle(frontendDropdownArray.first, for: .normal)
                cell.uniqueValue.setTitle(yesNoDropdownArray.first, for: .normal)
                cell.usetocreateconfig.setTitle(yesNoDropdownArray.first, for: .normal)
                cell.useInFilterOption.setTitle(yesNoDropdownArray.first, for: .normal)
                cell.defaultyesNo.setTitle(yesNoDropdownArray.first, for: .normal)
                if let textval = cell.catalog.currentTitle {
                    if dropdownLabelvalue[textval] == "Text Field"{
                        cell.usetocreateconfig.isEnabled = false
                        cell.usetocreateconfig.alpha = 0.5
                    }
                }
            }
            if edit{
                cell.scope.setTitle( scopeDropdowndict.someKey(forValue: editAttrData["is_global"] ?? "") ?? "", for: UIControl.State())
                if editAttrData["frontend_input"] == "select" || editAttrData["frontend_input"] == "multiselect"{
                    cell.defaultValue.isEditable = false
                    cell.defaultValue.text = ""
                }else{
                    cell.defaultValue.isEditable = true
                    cell.defaultValue.text = editAttrData["default_value"]
                }
              
               cell.attributeCode.text = editAttrData["attribute_code"]
                if let input = editAttrData["frontend_input"]{
                    cell.catalog.setTitle(frontendDropdowndic.someKey(forValue: input) ?? "", for: UIControl.State())
                }
            
                if(applyTo != "seletedcat"){
                    cell.applyTo.setTitle(dropdownValueLabel["Yes"] ?? "", for: UIControl.State())
                }else{
                    cell.applyTo.setTitle(dropdownValueLabel["No"] ?? "", for: UIControl.State())
                }
                cell.catalog.isEnabled = false
                cell.attributeCode.isEditable = false
                cell.catalog.backgroundColor = UIColor.lightText
                cell.attributeCode.backgroundColor = UIColor.lightText
                cell.valuesRequired.setTitle(dropdownValueLabel[editAttrData["is_required"] ?? ""] ?? "", for: UIControl.State())
                cell.uniqueValue.setTitle(dropdownValueLabel[editAttrData["is_unique"] ?? ""] ?? "", for: UIControl.State())
                cell.inputvalidationStore.setTitle(inputTypeDropdowndic.someKey(forValue: editAttrData["frontend_class"] ?? "") ?? "", for: UIControl.State())
                cell.usetocreateconfig.setTitle(dropdownValueLabel[editAttrData["is_configurable"] ?? ""] ?? "", for: UIControl.State())
                cell.useInFilterOption.setTitle(dropdownValueLabel[editAttrData["is_filterable_in_grid"] ?? ""] ?? "", for: UIControl.State())
//                if cell.catalog.currentTitle == "Text Field"{
//                    cell.usetocreateconfig.isEnabled = false
//                    cell.usetocreateconfig.alpha = 0.5
//                }
            }
            cell.tag = indexPath.row
                return cell
        }else{
            print("category")
            let cell = propertiesTable.dequeueReusableCell(withIdentifier: "categoryCell")
            let view = ced_categorySelector(frame: CGRect(x: cell!.frame.origin.x ,y: cell!.frame.origin.y,width: cell!.frame.width,height: 180))
            view.tag = 1234
            if(edit){
                let applyArr = editAttrData["apply_to"]?.components(separatedBy: ",")
                for data in applyArr! {
                    print(data)
                    if(data.lowercased() == "virtual"){
                        view.virtualProduct.setSelected(true)
                    }else if(data.lowercased() == "configurable".lowercased()){
                        view.configProduct.setSelected(true)
                    }else if(data.lowercased() == "grouped".lowercased()){
                        view.grouped.setSelected(true)
                    }else if(data.lowercased() == "simple".lowercased()){
                        view.simpleProduct.setSelected(true)
                    }else if(data.lowercased() == "bundle".lowercased()){
                        view.bundle.setSelected(true)
                    }else if(data.lowercased() == "Downloadable".lowercased()){
                        view.downloadable.setSelected(true)
                    }
                }

            }
            cell?.addSubview(view)
            return cell!
        }
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(applyTo == "seletedcat"){
            if(indexPath.section == 1){
               return 180
            }
        }
        return 380
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    
    @objc func continueTonextView(_ sender:UIButton){
        let cell = propertiesTable.cellForRow(at: IndexPath(row: 0, section: 0)) as! ced_buttoncell
        if(cell.attributeCode.text == ""){
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: "Attribute Code Is Required.")
            return
        }
     
        var scope = cell.scope.titleLabel?.text ?? ""
        scope = scopeDropdowndict[scope] ?? ""
        var catalog = cell.catalog.titleLabel?.text ?? ""
        catalog = frontendDropdowndic[catalog] ?? ""
        var inputvalidation = cell.inputvalidationStore.titleLabel?.text ?? ""
        inputvalidation = inputTypeDropdowndic[inputvalidation] ?? ""
        var uniqueval = cell.uniqueValue.titleLabel?.text ?? ""
        uniqueval = dropdownLabelvalue[uniqueval] ?? ""
        var valuesReq = cell.valuesRequired.titleLabel?.text ?? ""
        valuesReq = dropdownLabelvalue[valuesReq] ?? ""
        var config = cell.usetocreateconfig.titleLabel?.text ?? ""
        config = dropdownLabelvalue[config] ?? ""
        var defayesNo = cell.defaultyesNo.titleLabel?.text ?? ""
        defayesNo = dropdownLabelvalue[defayesNo] ?? ""
        var useInFilter = cell.useInFilterOption.titleLabel?.text ?? ""
        useInFilter = dropdownLabelvalue[useInFilter] ?? ""
        var addColumn = cell.applyTo.titleLabel?.text ?? ""
        addColumn = dropdownLabelvalue[addColumn] ?? ""
        let defaultval = cell.defaultValue.text ?? ""
        let data = ["attribute_code":cell.attributeCode.text,"is_global":scope ,"frontend_input":catalog,"frontend_class":inputvalidation,"is_unique":uniqueval,"is_required":valuesReq,"is_configurable":config,"default_value_textarea":defaultval,"default_value_date":defaultval,"default_value_text":defaultval,"default_value_yesno":defayesNo,"is_filterable_in_grid":useInFilter,"is_used_in_grid":addColumn] as [String : Any]
        
        if(catalog == "Media Image" || cell.catalog.titleLabel?.text == "media_image"){
            let nextView = UIStoryboard(name: "ProductAttributes", bundle: nil).instantiateViewController(withIdentifier: "productattrsave") as! ced_manageLabelsAttr
            let data1 = NSMutableDictionary()
            data1.setDictionary(data)
          
            print(AdminOption)
            if(editAttrData.count != 0){
                nextView.editAttrData = editAttrData
                nextView.attributeId = attributeId
             //   nextView.AdminOption = AdminOption
                nextView.store_label_Data = store_label
            }
            nextView.finaldata = data1
            self.navigationController?.pushViewController(nextView, animated: true)
        }else{
        let nextView = UIStoryboard(name: "ProductAttributes", bundle: nil).instantiateViewController(withIdentifier: "frontendproperty") as! ced_frontendproperty
        nextView.prevdata = data as! [String : String?]
            nextView.storeDropdownArray = storeDropdownArray
            nextView.yesNoDropdownArray = yesNoDropdownArray
            nextView.scopeDropdownArray = scopeDropdownArray
            nextView.frontendDropdownArray = frontendDropdownArray
            nextView.inputTypeDropdownArray = inputTypeDropdownArray
            nextView.dropdownValueLabel = dropdownValueLabel
            nextView.dropdownLabelvalue = dropdownLabelvalue
            nextView.storeDropdownDic = storeDropdownDic
            
        print("data being forwarded = \(data)")
        if(edit){
            nextView.editAttrData = editAttrData
            nextView.attributeId = attributeId
            nextView.AdminOption = AdminOption
            nextView.attributeSets = SAttrData
            nextView.store_label_Data = store_label
            nextView.optionsData = optionsData
        }
        self.navigationController?.pushViewController(nextView, animated: true)
        }
    }
    
    func requestDropdownData(){
        let url = "vproductattributeapi/index/getAttributeOptions"
        self.getRequest(url: url)
    }
    
    func getOptionsData(_ json:JSON){
        
        for items in json["data"]["frontend_type"].arrayValue{
            self.inputTypeDropdownArray.append(items["label"].stringValue)
            self.frontendDropdowndic[items["label"].stringValue] = items["value"].stringValue
        }
        for items in json["data"]["scope_type"].arrayValue{
            self.scopeDropdownArray.append(items["label"].stringValue)
            self.scopeDropdowndict[items["label"].stringValue] = items["value"].stringValue
        }
        for items in json["data"]["fronetend_class"].arrayValue{
            self.frontendDropdownArray.append(items["label"].stringValue)
            self.inputTypeDropdowndic[items["label"].stringValue] = items["value"].stringValue
        }
        for items in json["data"]["store_array"].arrayValue{
            self.storeDropdownArray.append(items["store_name"].stringValue)
            self.storeDropdownDic[items["store_name"].stringValue] = items["store_id"].stringValue
        }
        for items in json["data"]["yesno_type"].arrayValue{
            self.yesNoDropdownArray.append(items["label"].stringValue)
            self.dropdownLabelvalue[items["label"].stringValue] = items["value"].stringValue
            self.dropdownValueLabel[items["value"].stringValue] = items["label"].stringValue
        }
        print(inputTypeDropdownArray)
        print(scopeDropdownArray)
        print(frontendDropdownArray)
        print(storeDropdownArray)
        print(yesNoDropdownArray)
        print(dropdownLabelvalue)
        print(dropdownValueLabel)
        DispatchQueue.main.async {
            self.propertiesTable.reloadData()
        }
        
    }
    
    @objc func loadEditdata(){
        //var url = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
        let url = "vproductattributeapi/index/getVProductAttributeData/"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString  += "&attribute_id=" + attributeId
        print(postString)
        self.sendRequest(url: url, parameters: postString)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if(requestUrl == "vproductattributeapi/index/getVProductAttributeData/")
        {
            Alert_File.removeLoadingIndicator(self)
            if let data = data{
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    self.parseData(json)
                }else{
                  //  ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: json["data"]["message"].stringValue)
                    self.propertiesTable.setEmptyMessage(json["data"]["message"].stringValue)
                }
            }
        }else{
            guard let data = data else {return}
            guard let json = try? JSON(data: data) else {return}
            print("options = \(json)")
            if json["data"]["success"].stringValue.lowercased() == "true".lowercased(){
                self.getOptionsData(json)
            }
        }
    }
    // select parse Edit
    
    func parseData(_ json:JSON) {
        let result = json["data"]["attribute_data"]
            print(result)
            var data = [String:String]()
            data["is_searchable"] = result["storefront_properties"]["is_searchable"].stringValue
            data["is_configurable"] = result["is_configurable"].stringValue
            data["is_comparable"] = result["storefront_properties"]["is_comparable"].stringValue
            data["is_global"] = result["attribute_properties"]["is_global"].stringValue
            data["is_required"]     = result["attribute_properties"]["is_required"].stringValue
            data["is_user_defined"] = result["attribute_properties"]["is_user_defined"].stringValue
            data["attribute_id"] = result["attribute_properties"]["attribute_id"].stringValue
            data["is_unique"] = result["attribute_properties"]["is_unique"].stringValue
            data["is_visible_on_front"] = result["storefront_properties"]["is_visible_on_front"].stringValue
            data["is_filterable"] = result["attribute_properties"]["is_filterable"].stringValue
            data["frontend_label"] = result["attribute_properties"]["frontend_label"].stringValue
            data["attribute_code"] = result["attribute_properties"]["attribute_code"].stringValue
            data["frontend_input"] = result["attribute_properties"]["frontend_input"].stringValue
            data["frontend_model"] = result["frontend_model"].stringValue
            data["search_weight"] = result["search_weight"].stringValue
            data["frontend_class"] = result["attribute_properties"]["frontend_class"].stringValue
            data["source_model"] = result["source_model"].stringValue
            data["default_value"] = result["attribute_properties"]["default_value"].stringValue
        data["sort_order"] = result["attribute_properties"]["sort_order"].stringValue
            data["is_used_for_price_rules"] = result["is_used_for_price_rules"].stringValue
            data["include_in_attribute_set"] = result["include_in_attribute_set"].stringValue
            data["backend_model"] = result["backend_model"].stringValue
            data["is_used_for_promo_rules"] = result["storefront_properties"]["is_used_for_promo_rules"].stringValue
            data["note"] = result["note"].stringValue
            data["is_html_allowed_on_front"] = result["storefront_properties"]["is_html_allowed_on_front"].stringValue
            
            data["is_visible_in_advanced_search"] = result["storefront_properties"]["is_visible_in_advanced_search"].stringValue
            data["backend_type"] = result["backend_type"].stringValue
            data["is_filterable_in_search"] = result["is_filterable_in_search"].stringValue
            data["is_filterable_in_search"] = result["is_filterable_in_search"].stringValue
            data["is_wysiwyg_enabled"] = result["is_wysiwyg_enabled"].stringValue
            data["apply_to"] = result["apply_to"].stringValue
            data["entity_type_id"] = result["entity_type_id"].stringValue
            data["frontend_input_renderer"] = result["frontend_input_renderer"].stringValue
            data["used_in_product_listing"] = result["storefront_properties"]["used_in_product_listing"].stringValue
            data["used_for_sort_by"] = result["storefront_properties"]["used_for_sort_by"].stringValue
            data["is_used_in_grid"]  = result["is_used_in_grid"].stringValue
            data["is_filterable_in_grid"] = result["is_filterable_in_grid"].stringValue
            print(result["option_data"])
        for attr in result["attribute_properties"]["attribute_set_ids"].arrayValue{
            SAttrData.append(["set_id":attr["value"].stringValue,"set_code":attr["label"].stringValue,"selected":attr["selected"].stringValue])
        }
            
            self.editAttrData = data
            if(self.editAttrData["apply_to"]?.lowercased() != "All Product Types".lowercased()){
                applyTo = "seletedcat"
            }
            self.propertiesTable.reloadData()
        
        for option in json["data"]["option_data"].arrayValue {
            let checked=option["checked"].stringValue
            let option_name = option["option_name"].stringValue
            let id = option["id"].stringValue
            let data = ["checked":checked,"option_name":option_name,"id":id]
            self.AdminOption.append(data)
        }
        for (itm) in result["store_frontend_label"].arrayValue{
            self.store_label[itm["key"].stringValue] = itm["value"].stringValue
        }
        print("Stroie=\(store_label)")
        
        self.optionsData = result["attribute_properties"]["options"].arrayValue
    }
    

}
