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

class ced_frontendproperty: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate {
    
    @IBOutlet weak var continueButton: UIButton!
    @IBOutlet weak var frontendPropertyTable: UITableView!
    var optionsData = [JSON]()
    var prevdata = [String:String!]()
    var store_label_Data = [String:String]()
    var selectedCate = [String:String]()
    var editAttrData = [String:String]()
    var AdminOption  = [[String:String]]()
    var attributeSets = [[String:String]]()
    var attributeId = String()
    let screenSize: CGRect = UIScreen.main.bounds;
    var heightToUse = CGFloat(0);
    let dropDownSectionHeight : CGFloat = 70;
    let checkboxHeight : CGFloat = 30;
    let padding : CGFloat = 5;
    var storeDropdownDic = [String:String]()
    
    var yesNoDropdownArray = [String]()
    var scopeDropdownArray = [String]()
    var frontendDropdownArray = [String]()
    var inputTypeDropdownArray = [String]()
    var dropdownValueLabel = [String:String]()
    var dropdownLabelvalue = [String:String]()
    var storeDropdownArray = [String]()
    
    var activeAttrValueArray = [String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        frontendPropertyTable.delegate = self
        frontendPropertyTable.dataSource = self
        print("ammanub\(store_label_Data)")
        self.title = "Frontend Properties".localized
        // Do any additional setup after loading the view.
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        continueButton.backgroundColor = color
        ced_navigationBarController().addNavButton(self,str:"back")
        continueButton.setTitle("Continue".localized, for: .normal)
        continueButton.addTarget(self, action: #selector(ced_frontendproperty.gotoToNextview(_:)), for: UIControl.Event.touchUpInside)
        print(attributeSets)
        if editAttrData.count == 0{
                self.fetchDataAttributeSet()
        }else{
            for data in attributeSets {
                if data["selected"] == "true"{
                    activeAttrValueArray.append(data["set_id"]!)
                }
            }
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(prevdata.count == 0){
            return 0
        }
//        if  self.attributeSets.count > 0 || editAttrData.count > 0 {
//            return 1
//        }
        return 1
        
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = frontendPropertyTable.dequeueReusableCell(withIdentifier: "frontendprop") as! ced_frontendpropertycell
        cell.prevData = prevdata["frontend_input"]! ?? ""
        cell.parent = self
        cell.yesNoArray = self.yesNoDropdownArray
        if(prevdata["frontend_input"] == "Text Area" || prevdata["frontend_input"] == "textarea"){
            cell.usedinlayerednav.isEnabled = false
            cell.usedinlayerednav.alpha = 0.4
            cell.position.isEditable = true
//            cell.position.alpha = 0.4
            cell.usedinsearchlayerednav.isEnabled = false
            cell.usedinsearchlayerednav.alpha = 0.4
            cell.usedinproductlisting.alpha = 0.4
            cell.usedinproductlisting.isEnabled = false
            
        }else if(prevdata["frontend_input"] == "Text Field" || prevdata["frontend_input"] == "text"){
            cell.usedinlayerednav.isEnabled = false
            cell.usedinlayerednav.alpha = 0.4
            cell.position.isEditable = true
//            cell.position.alpha = 0.4
            cell.usedinsearchlayerednav.isEnabled = false
            cell.usedinsearchlayerednav.alpha = 0.4
            cell.usedinproductlisting.alpha = 0.4
            cell.usedinproductlisting.isEnabled = false
            cell.enableWYSIWYG.isEnabled = false
            cell.enableWYSIWYG.alpha = 0.4
            
            
        }else  if(prevdata["frontend_input"]??.lowercased() == "Date".lowercased()){
            cell.usedinlayerednav.isEnabled = false
            cell.usedinlayerednav.alpha = 0.4
            cell.position.isEditable = true
//            cell.position.alpha = 0.4
            cell.usedinsearchlayerednav.isEnabled = false
            cell.usedinsearchlayerednav.alpha = 0.4
            cell.usedinproductlisting.alpha = 0.4
            cell.usedinproductlisting.isEnabled = false
            cell.enableWYSIWYG.isEnabled = false
            cell.enableWYSIWYG.alpha = 0.4
            
            
        }else  if(prevdata["frontend_input"]??.lowercased() == "Yes/No".lowercased() || prevdata["frontend_input"] == "boolean"){
            cell.usedinlayerednav.isEnabled = false
            cell.usedinlayerednav.alpha = 0.4
            cell.position.isEditable = true
//            cell.position.alpha = 0.4
            cell.usedinsearchlayerednav.isEnabled = false
            cell.usedinsearchlayerednav.alpha = 0.4
            
            cell.enableWYSIWYG.isEnabled = false
            cell.enableWYSIWYG.alpha = 0.4
            
        }
        else  if(prevdata["frontend_input"] == "Multiple Select" || prevdata["frontend_input"] == "multiselect"){
            cell.position.isEditable = true
//            cell.position.alpha = 0.4
            cell.usedinproductlisting.alpha = 0.4
            cell.usedinproductlisting.isEnabled = false
            cell.enableWYSIWYG.isEnabled = false
            cell.enableWYSIWYG.alpha = 0.4
            
        }
        else  if(prevdata["frontend_input"] == "Dropdown" || prevdata["frontend_input"] == "select"){
            cell.position.isEditable = true
//            cell.position.alpha = 0.4
            cell.enableWYSIWYG.isEnabled = false
            cell.enableWYSIWYG.alpha = 0.4
            
        }
        else  if(prevdata["frontend_input"]??.lowercased() == "Price".lowercased()){
            cell.position.isEditable = true
//            cell.position.alpha = 0.4
            
            cell.enableWYSIWYG.isEnabled = false
            cell.enableWYSIWYG.alpha = 0.4
            
            
        }
        
        if(editAttrData.count != 0){
            cell.usedforpromorules.setTitle(dropdownValueLabel[editAttrData["is_used_for_promo_rules"] ?? ""] ?? "", for: UIControl.State())
            cell.usedinattrbuteset.setTitle(dropdownValueLabel[editAttrData["include_in_attribute_set"] ?? ""] ?? "", for: UIControl.State())
            cell.usedinlayerednav.setTitle(editAttrData["is_filterable"], for: UIControl.State())
            
            cell.usedinproductlisting.setTitle(dropdownValueLabel[editAttrData["used_in_product_listing"] ?? ""] ?? "", for: UIControl.State())
            cell.usedinsearchlayerednav.setTitle(dropdownValueLabel[editAttrData["is_filterable_in_search"] ?? ""] ?? "", for: UIControl.State())
            cell.usedinsorting.setTitle(dropdownValueLabel[editAttrData["used_for_sort_by"] ?? ""] ?? "", for: UIControl.State())
            cell.usedproductfrontend.setTitle(dropdownValueLabel[editAttrData["is_visible_on_front"] ?? ""] ?? "", for: UIControl.State())
            cell.useinadvancedsearch.setTitle(dropdownValueLabel[editAttrData["is_visible_in_advanced_search"] ?? "" ] ?? "", for: UIControl.State())
            cell.useinquicksearch.setTitle(dropdownValueLabel[editAttrData["is_searchable"] ?? ""] ?? "", for: UIControl.State())
            cell.allowhtmlfrontend.setTitle(dropdownValueLabel[editAttrData["is_html_allowed_on_front"] ?? ""] ?? "", for: UIControl.State())
            cell.comparabonfrontend.setTitle(dropdownValueLabel[editAttrData["is_comparable"] ?? ""] ?? "", for: UIControl.State())
            cell.enableWYSIWYG.setTitle(dropdownValueLabel[editAttrData["is_wysiwyg_enabled"] ?? "" ] ?? "", for: UIControl.State())
            cell.position.text = editAttrData["sort_order"]
        }else{
            cell.usedforpromorules.setTitle(yesNoDropdownArray.first, for: UIControl.State())
            cell.usedinattrbuteset.setTitle(yesNoDropdownArray.first, for: UIControl.State())
            cell.usedinproductlisting.setTitle(yesNoDropdownArray.first, for: UIControl.State())
            cell.usedinsearchlayerednav.setTitle(yesNoDropdownArray.first, for: UIControl.State())
            cell.usedinsorting.setTitle(yesNoDropdownArray.first, for: UIControl.State())
            cell.usedproductfrontend.setTitle(yesNoDropdownArray.first, for: UIControl.State())
            cell.useinadvancedsearch.setTitle(yesNoDropdownArray.first, for: UIControl.State())
            cell.useinquicksearch.setTitle(yesNoDropdownArray.first, for: UIControl.State())
            cell.allowhtmlfrontend.setTitle(yesNoDropdownArray.first, for: UIControl.State())
            cell.comparabonfrontend.setTitle(yesNoDropdownArray.first, for: UIControl.State())
            cell.enableWYSIWYG.setTitle(yesNoDropdownArray.first, for: UIControl.State())
        }
        //print(prevdata)
        heightToUse += CGFloat(5);
        let view = UIView();
        let topPoint = heightToUse;
        var viewHeight = CGFloat(0);
        let labelView = LabelView();
        labelView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        labelView.frame = CGRect(x: 0, y: viewHeight, width: screenSize.width-30,height: checkboxHeight);
        labelView.center.x = self.view.center.x;
        heightToUse += checkboxHeight;
        heightToUse += padding;
        
        viewHeight += (checkboxHeight+padding);
        
        labelView.label.text = " "+("Select Attribute Set".uppercased());
        
        
        
        labelView.label.textColor = UIColor.black;
        
        
        view.addSubview(labelView);
        view.frame = CGRect(x: 5, y: 565, width: screenSize.width-10, height: viewHeight);
        
        //  view.makeCard(view, cornerRadius: 2, color: UIColor.blackColor(), shadowOpacity: 0.4)
        
        //view.layer.borderWidth = 1;
        //view.layer.borderColor = UIColor.grayColor().CGColor;
        cell.addSubview(view);
        viewHeight += 575
        for attribute in attributeSets {
            
            let subCategoryView = SubCategoryView();
            subCategoryView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            subCategoryView.frame = CGRect(x: 0, y: viewHeight, width: screenSize.width-30,height: checkboxHeight);
            subCategoryView.center.x = self.view.center.x;
            heightToUse += checkboxHeight;
            heightToUse += padding;
            
            viewHeight += (checkboxHeight+padding);
            
            subCategoryView.childFilterButton.setTitle(attribute["set_code"], for: UIControl.State());
            if  let set_id = attribute["set_id"] {
                if let intval = Int(set_id) {
                    subCategoryView.childFilterButton.tag = intval
                }
            }
            
            if activeAttrValueArray.contains(attribute["set_id"]!) {
                subCategoryView.childFilterButton.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
            }
            else
            {
                subCategoryView.childFilterButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
            }
            
            subCategoryView.childFilterButton.addTarget(self, action: #selector(ced_frontendproperty.checkboxButtonClicked(_:)), for: UIControl.Event.touchUpInside);
            cell.addSubview(subCategoryView);
            
            
        }
        
        cell.tableView = frontendPropertyTable
        return cell
        
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        return 560 + heightToUse + 20
        
    }
    
    @objc func checkboxButtonClicked(_ sender:UIButton)
    {
        
         let key = String(sender.tag)
            
            if(sender.imageView!.image == UIImage(named:"unchecked_checkbox"))
            {
                sender.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
                self.activeAttrValueArray.append(key);
            }
            else
            {
                sender.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
                if let indexToRemove = activeAttrValueArray.index(of: key) {
                    self.activeAttrValueArray.remove(at: indexToRemove);
                }
            }
        print(activeAttrValueArray)
        
        
    }
    
    
    @objc func gotoToNextview(_ sender:UIButton){
        let cell = frontendPropertyTable.cellForRow(at: IndexPath(row: 0, section: 0)) as! ced_frontendpropertycell
        let useinlayered = cell.usedinlayerednav.titleLabel!.text
        
        var is_searchable = cell.useinquicksearch.titleLabel?.text ?? ""
        is_searchable = dropdownLabelvalue[is_searchable] ?? ""
        var is_visible_in_advanced_search = cell.useinadvancedsearch.titleLabel?.text ?? ""
        is_visible_in_advanced_search = dropdownLabelvalue[is_visible_in_advanced_search] ?? ""
        var comparable = cell.comparabonfrontend.titleLabel?.text ?? ""
        comparable = dropdownLabelvalue[comparable] ?? ""
        var useinsearchlayer = cell.usedinsearchlayerednav.titleLabel?.text ?? ""
        useinsearchlayer = dropdownLabelvalue[useinsearchlayer] ?? ""
        var usepromorules = cell.usedforpromorules.titleLabel?.text ?? ""
        usepromorules = dropdownLabelvalue[usepromorules] ?? ""
        var allowhtmltag = cell.allowhtmlfrontend.titleLabel?.text ?? ""
        allowhtmltag = dropdownLabelvalue[allowhtmltag] ?? ""
        var usedattrset = cell.usedinattrbuteset.titleLabel?.text ?? ""
        usedattrset = dropdownLabelvalue[usedattrset] ?? ""
        var visibleproduct = cell.usedproductfrontend.titleLabel?.text ?? ""
        visibleproduct = dropdownLabelvalue[visibleproduct] ?? ""
        var usedinproductlistin = cell.usedinproductlisting.titleLabel?.text ?? ""
        usedinproductlistin = dropdownLabelvalue[usedinproductlistin] ?? ""
        var usedinsorting = cell.usedinsorting.titleLabel?.text ?? ""
        usedinsorting = dropdownLabelvalue[usedinsorting] ?? ""
        var enableWYSIWYG  = cell.enableWYSIWYG.titleLabel?.text ?? ""
        enableWYSIWYG = dropdownLabelvalue[enableWYSIWYG] ?? ""
        
        prevdata["is_searchable"] = is_searchable
        prevdata["is_visible_in_advanced_search"] = is_visible_in_advanced_search
        prevdata["is_comparable"] = comparable
        prevdata["is_filterable"] = useinlayered
        prevdata["is_filterable_in_search"] = useinsearchlayer
        prevdata["is_used_for_promo_rules"] = usepromorules
        prevdata["is_html_allowed_on_front"] = allowhtmltag
        prevdata["include_in_attribute_set"] = usedattrset
        prevdata["is_visible_on_front"] = visibleproduct
        prevdata["used_in_product_listing"] = usedinproductlistin
        prevdata["used_for_sort_by"] = usedinsorting
        prevdata["is_wysiwyg_enabled"] = enableWYSIWYG
        var position = ""
        if(cell.position.text != nil){
            position = cell.position.text
        }
        prevdata["sort_order"] = position
        //print(prevdata)
        let nextView = ced_newManageLabelsOptions()// UIStoryboard(name: "ProductAttributes", bundle: nil).instantiateViewController(withIdentifier: "productattrsave") as! ced_manageLabelsAttr
        let data = NSMutableDictionary()
        data.setDictionary(prevdata)
        if(selectedCate.count == 0){
//            data["apply_to"] = "All Product Types"
        }else{
            var itemsString = String()
            do{
                let JSONData = try JSONSerialization.data(
                    withJSONObject: selectedCate ,
                    options: JSONSerialization.WritingOptions(rawValue: 0))
                itemsString = NSString(data: JSONData,
                    encoding: String.Encoding.utf8.rawValue)! as String
                
            }
            catch{
                print("error in data encoding")
                
            }
//            data["apply_to"] = itemsString
        }
        print(data)
        if(editAttrData.count != 0){
            nextView.editAttrData = editAttrData
            nextView.attributeId = attributeId
            nextView.store_label_Data = store_label_Data
        }
        
//        nextView.dropdownValueLabel = dropdownValueLabel
//        nextView.dropdownLabelvalue = dropdownLabelvalue
        nextView.storeDropdownArray = storeDropdownArray
        nextView.storeDropdownDic = storeDropdownDic
        nextView.finaldata = data
         nextView.finaldata["attribute_set_ids"] = activeAttrValueArray
        nextView.optionsData = optionsData
        self.navigationController?.pushViewController(nextView, animated: true)
        print(nextView.finaldata)
        
    }
    
    
    @objc func fetchDataAttributeSet(){
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        //var baseUrl = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
        let baseUrl = "vproductattributeapi/set/info"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        
        self.sendRequest(url: baseUrl, parameters: postString)
        
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if(json["data"]["success"].stringValue == "true"){
                for data in json["data"]["attribute"].arrayValue {
                    self.attributeSets.append(["set_code":data["set_code"].stringValue,"set_id":data["set_id"].stringValue])
                }
//                if json["data"]["attribute"].arrayValue.count == 0{
//                    continueButton.isHidden = true
//                    ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: "Create attribute sets first to continue")
//                    return
//                }
                
                self.frontendPropertyTable.reloadData()
                
            }else{
               // ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: json["data"]["message"].stringValue)
                self.frontendPropertyTable.setEmptyMessage(json["data"]["message"].stringValue)
            }
            
        }
        
    }
    /*
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
    // Get the new view controller using segue.destinationViewController.
    // Pass the selected object to the new view controller.
    }
    */
    
}
