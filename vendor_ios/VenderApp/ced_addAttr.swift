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

class ced_addAttr: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource {
    @IBOutlet weak var continueTapped: UIButton!
    @IBOutlet weak var propertiesTable: UITableView!
    var textareaSelected = ""
    var textViewHeight = CGFloat()
    var selectedcatalog = String()
    var applyTo  = String()
    var postion  = String()
    var id=0;
    var edit = true
    var attributeId = ""
    var editAttrData = [String:String]()
    var AdminOption  = [[String:String]]()
    var AddAttrData = [[String:String]]()
    var SAttrData = [[String:String]]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        propertiesTable.delegate = self
        propertiesTable.dataSource = self
        self.title = "Properties"
        // Do any additional setup after loading the view.
        continueTapped.addTarget(self, action: #selector(ced_addATTrbutenew.continueTonextView(_:)), for: UIControl.Event.touchUpInside)
        ced_navigationBarController().addNavButton(self,str:"back")
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        continueTapped.backgroundColor = color
        if(edit){
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
            self.loadEditdata()
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
        return 1
    }
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if(indexPath.section == 0){
            let cell = propertiesTable.dequeueReusableCell(withIdentifier: "buttonCell") as! ced_buttoncell
            cell.parent = self
            cell.tableView = propertiesTable
            if(edit){
                cell.scope.setTitle(editAttrData["is_global"], for: UIControl.State())
                cell.defaultValue.text = editAttrData["default_value"]
                cell.attributeCode.text = editAttrData["attribute_code"]
                if(editAttrData["frontend_input"] == "text"){
                    cell.catalog.setTitle("Text Field", for: UIControl.State())
                }else if(editAttrData["frontend_input"] == "textarea"){
                    cell.catalog.setTitle("Text Area", for: UIControl.State())
                }else if(editAttrData["frontend_input"] == "date"){
                    cell.catalog.setTitle("Date", for: UIControl.State())
                }else if(editAttrData["frontend_input"] == "boolean"){
                    cell.catalog.setTitle("Yes/No", for: UIControl.State())
                }else if(editAttrData["frontend_input"] == "multiselect"){
                    cell.catalog.setTitle("Multiple Select", for: UIControl.State())
                }else if(editAttrData["frontend_input"] == "select"){
                    cell.catalog.setTitle("Dropdown", for: UIControl.State())
                }else if(editAttrData["frontend_input"] == "price"){
                    cell.catalog.setTitle("Price", for: UIControl.State())
                }else if(editAttrData["frontend_input"] == "media_image"){
                    cell.catalog.setTitle("Media Image", for: UIControl.State())
                }
                
                if(applyTo != "seletedcat"){
                    cell.applyTo.setTitle("All Product Types", for: UIControl.State())
                }else{
                    cell.applyTo.setTitle("Selected Product Types", for: UIControl.State())
                }
                cell.catalog.isEnabled = false
                cell.attributeCode.isEditable = false
                cell.catalog.backgroundColor = UIColor.lightText
                cell.attributeCode.backgroundColor = UIColor.lightText
                cell.valuesRequired.setTitle(editAttrData["is_required"], for: UIControl.State())
                cell.uniqueValue.setTitle(editAttrData["is_unique"], for: UIControl.State())
                cell.inputvalidationStore.setTitle(editAttrData["frontend_class"], for: UIControl.State())
                
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
        return 450
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
        
        let scope = cell.scope.titleLabel?.text!
        let catalog = cell.catalog.titleLabel?.text!
        let inputvalidation = cell.inputvalidationStore.titleLabel?.text!
        let uniqueval = cell.uniqueValue.titleLabel?.text!
        let valuesReq = cell.valuesRequired.titleLabel?.text!
        let config = cell.usetocreateconfig.titleLabel?.text!
        let defayesNo = cell.defaultyesNo.titleLabel?.text!
        let useInFilter = cell.useInFilterOption.titleLabel?.text
        let addColumn = cell.applyTo.titleLabel?.text
        var defaultval = ""
        if(cell.defaultValue.text != nil){
            defaultval = cell.defaultValue.text
        }
        let data = ["attribute_code":cell.attributeCode.text,"is_global":scope,"frontend_input":catalog,"frontend_class":inputvalidation,"is_unique":uniqueval,"is_required":valuesReq,"is_configurable":config,"default_value_textarea":defaultval,"default_value_date":defaultval,"default_value_text":defaultval,"default_value_yesno":defayesNo,"is_filterable_in_grid":useInFilter,"is_used_in_grid":addColumn] as [String : Any]
        
        if(catalog == "Media Image" || cell.catalog.titleLabel?.text == "media_image"){
            let nextView = UIStoryboard(name: "ProductAttributes", bundle: nil).instantiateViewController(withIdentifier: "productattrsave") as! ced_manageLabelsAttr
            let data1 = NSMutableDictionary()
            data1.setDictionary(data)
            
            print(AdminOption)
            if(editAttrData.count != 0){
                nextView.editAttrData = editAttrData
                nextView.attributeId = attributeId
//                nextView.AdminOption = AdminOption
            }
            nextView.finaldata = data1
            self.navigationController?.pushViewController(nextView, animated: true)
        }else{
            let nextView = UIStoryboard(name: "ProductAttributes", bundle: nil).instantiateViewController(withIdentifier: "frontendproperty") as! ced_frontendproperty
            nextView.prevdata = data as! [String : String?]
            
            if(edit){
                nextView.editAttrData = editAttrData
                nextView.attributeId = attributeId
                nextView.AdminOption = AdminOption
                nextView.attributeSets = SAttrData
            }
            self.navigationController?.pushViewController(nextView, animated: true)
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
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            if(requestUrl == "vproductattributeapi/index/getVProductAttributeData/")
            {
                guard let json = try? JSON(data:data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    self.parseData(json)
                }else{
                   // ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: json["data"]["message"].stringValue)
                    self.propertiesTable.setEmptyMessage(json["data"]["message"].stringValue)
                }
            }
        }
        
    }
    // select parse Edit
    
    func parseData(_ json:JSON){
        for result in json["data"]["attribute_data"].arrayValue{
            print(result)
            var data = [String:String]()
            data["is_searchable"] = result["is_searchable"].stringValue
            data["is_configurable"] = result["is_configurable"].stringValue
            data["is_comparable"] = result["is_comparable"].stringValue
            data["is_global"] = result["is_global"].stringValue
            data["is_required"]     = result["is_required"].stringValue
            data["is_user_defined"] = result["is_user_defined"].stringValue
            data["attribute_id"] = result["attribute_id"].stringValue
            data["is_unique"] = result["is_unique"].stringValue
            data["is_visible_on_front"] = result["is_visible_on_front"].stringValue
            data["is_filterable"] = result["is_filterable"].stringValue
            data["frontend_label"] = result["frontend_label"].stringValue
            data["attribute_code"] = result["attribute_code"].stringValue
            data["frontend_input"] = result["frontend_input"].stringValue
            data["frontend_model"] = result["frontend_model"].stringValue
            data["search_weight"] = result["search_weight"].stringValue
            data["frontend_class"] = result["frontend_class"].stringValue
            data["source_model"] = result["source_model"].stringValue
            data["default_value"] = result["default_value"].stringValue
            data["is_used_for_price_rules"] = result["is_used_for_price_rules"].stringValue
            data["include_in_attribute_set"] = result["include_in_attribute_set"].stringValue
            data["backend_model"] = result["backend_model"].stringValue
            data["is_used_for_promo_rules"] = result["is_used_for_promo_rules"].stringValue
            data["note"] = result["note"].stringValue
            data["is_html_allowed_on_front"] = result["is_html_allowed_on_front"].stringValue
            
            data["is_visible_in_advanced_search"] = result["is_visible_in_advanced_search"].stringValue
            data["backend_type"] = result["backend_type"].stringValue
            data["is_filterable_in_search"] = result["is_filterable_in_search"].stringValue
            data["is_filterable_in_search"] = result["is_filterable_in_search"].stringValue
            data["is_wysiwyg_enabled"] = result["is_wysiwyg_enabled"].stringValue
            data["apply_to"] = result["apply_to"].stringValue
            data["entity_type_id"] = result["entity_type_id"].stringValue
            data["frontend_input_renderer"] = result["frontend_input_renderer"].stringValue
            data["used_in_product_listing"] = result["used_in_product_listing"].stringValue
            data["used_for_sort_by"] = result["used_for_sort_by"].stringValue
            data["is_used_in_grid"]  = result["is_used_in_grid"].stringValue
            data["is_filterable_in_grid"] = result["is_filterable_in_grid"].stringValue
            print(result["option_data"])
            for attr in result["attribute_set_ids"].arrayValue{
                SAttrData.append(["set_id":attr["value"].stringValue,"set_code":attr["label"].stringValue,"selected":attr["selected"].stringValue])
            }
            
            self.editAttrData = data
            if(self.editAttrData["apply_to"]?.lowercased() != "All Product Types".lowercased()){
                applyTo = "seletedcat"
            }
            self.propertiesTable.reloadData()
        }
        for option in json["data"]["option_data"].arrayValue {
            let checked=option["checked"].stringValue
            let option_name = option["option_name"].stringValue
            let id = option["id"].stringValue
            let data = ["checked":checked,"option_name":option_name,"id":id]
            self.AdminOption.append(data)
        }
    }
    
    
}
