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

class ced_manageLabelsAttr: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UITextViewDelegate {

    @IBOutlet weak var frontEndField: UITextField!
    @IBOutlet weak var frontEndLabel: UILabel!
    @IBOutlet weak var saveAttribute: UIButton!
    @IBOutlet weak var manageLabeltable: UITableView!
    var finaldata = NSMutableDictionary()
    var count:CGFloat = 0
    
    
    var dropdownValueLabel = [String:String]()
    var dropdownLabelvalue = [String:String]()
    var storeDropdownArray = [String]()
    var storeDropdownDic = [String:String]()
    
    var attributeId = String()
    var editAttrData = [String:String]()
//    var options = [String]()
//    var AdminOption  = [[String:String]]()
    var store_label_Data = [String:String]()
    var defaultOption = [String]()
    var deleteItems = [String]()
    override func viewDidLoad() {
        super.viewDidLoad()
        manageLabeltable.delegate = self
        manageLabeltable.dataSource = self
        self.title = "Manage Label/Options".localized
        print("ammanub\(store_label_Data)")
        // Do any additional setup after loading the view.
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        saveAttribute.backgroundColor = color
//        for option in AdminOption{
//            if(option["checked"] == "checked"){
//                defaultOption = [option["option_name"]!]
//            }
//            options.append(option["option_name"]!)
//        }
        ced_navigationBarController().addNavButton(self,str:"back")
        saveAttribute.setTitle("Save Attribute".localized, for: .normal)
        saveAttribute.addTarget(self, action: #selector(ced_manageLabelsAttr.SaveATTr(_:)), for: UIControl.Event.touchUpInside)
        frontEndLabel.text = "Default Label(*)".localized
        frontEndField.text = editAttrData["frontend_label"]
    }

    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ced_newstoreLabelCellTableViewCell", for: indexPath) as! ced_newstoreLabelCellTableViewCell
        cell.topLabel.font = .systemFont(ofSize: 17, weight: .semibold)
        cell.topLabel.backgroundColor = DynamicColor.themeColor
        cell.topLabel.textColor = DynamicColor.headingTextColor
        cell.topLabel.text = "Manage Labels".localized
        
        let storeArrayToBeUsed = self.storeDropdownArray.filter { $0 != "Admin" }
        
        for items in storeArrayToBeUsed{
            let view = storeLabelView()
            cell.stackView.addArrangedSubview(view)
            view.storeName.text = items
            let value = storeDropdownDic[items] ?? ""
            view.value = value
            if let text = self.store_label_Data[value]{
                view.textViewForText.text = text
            }
        }
        
        
        
//        let cell = manageLabeltable.dequeueReusableCell(withIdentifier: "manageLabel2") as! ced_editAdminOptions
//        print(AdminOption)
//        if(options.count != 0){
//            var counter:CGFloat = 0
//            let bounds = UIScreen.main.bounds
//            let height:CGFloat = 306
//            for option in options{
//            let view = ced_showOption()
//                view.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
//            view.frame = CGRect(x: bounds.origin.x, y: height+counter*60, width: bounds.width, height: 60)
//                if(self.defaultOption.count != 0){
//                    if(option == self.defaultOption[0]){
//                        view.makeDefaultButton.setSelected(true)
//
//                    }
//                }
//               // view.makeDefaultButton.enabled = false
//                view.textView.delegate = self
//                view.makeDefaultButton.addTarget(self, action: #selector(ced_manageLabelsAttr.makeDefault(_:)), for: UIControl.Event.touchUpInside)
//                view.makeDefaultButton.tag = Int(counter + 2000)
//                view.DeleteOption.addTarget(self, action: #selector(ced_manageLabelsAttr.deleteOption(_:)), for: UIControl.Event.touchUpInside)
//                view.DeleteOption.tag = Int(counter)
//                view.textView.tag = Int(counter + 1000)
//                view.textView.text = option
//                cell.addSubview(view)
//                counter += 1
//            }
//        }
//
//        if(editAttrData.count != 0){
//          cell.textView.text = editAttrData["frontend_label"]
//        }
//        let catalog = finaldata["frontend_input"] as! String
//        if(catalog == "Multiple Select" || catalog == "Dropdown"){
//          cell.addAdminOptionButton.isHidden = false
//            cell.addAdminOptionButton.addTarget(self, action: #selector(ced_manageLabelsAttr.addOptions(_:)), for: UIControl.Event.touchUpInside)
//
//        }
//
        
        
        return cell
    }
    
//    @objc func makeDefault(_ sender:UIButton){
//        let button = sender as? DLRadioButton
//        if(defaultOption.count != 0){
//            if(options[sender.tag-2000] == defaultOption[0]){
//                defaultOption.removeAll()
//                return
//            }
//            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Alert", showMsg: "You Can Have Only One DefaultValue.")
//            button?.setSelected(false)
//            return
//        }else{
//            defaultOption.append(options[sender.tag-2000])
//
//        }
//    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
//    @objc func deleteOption(_ sender:UIButton){
//        if(editAttrData.count != 0){
//            for i in 0..<AdminOption.count{
//                if(self.options[sender.tag] == AdminOption[i]["option_name"]!){
//                    deleteItems.append(AdminOption[i]["id"]!)
//                    AdminOption.remove(at: i)
//                }
//            }
//
//        }
//        if(self.defaultOption.count != 0){
//       if self.options[sender.tag] == self.defaultOption[0]{
//           self.defaultOption.removeAll()
//        }
//        }
//        print("After Deletion")
//        print(AdminOption)
//         print("After DeletionENND")
//        self.options.remove(at: sender.tag)
//         manageLabeltable.reloadSections(IndexSet(integer: 0), with: UITableView.RowAnimation.fade)
//
//    }
//
//    @objc func textViewDidEndEditing(_ textView: UITextView) {
//        if(!options.indices.contains(textView.tag-1000)){
//            return
//        }
//        if(editAttrData.count != 0){
//            print(AdminOption)
//            print(options)
//            for i in 0..<AdminOption.count{
//                if(AdminOption[i]["option_name"] == options[textView.tag-1000]){
//                    AdminOption[i]["option_name"] = textView.text
//                }
//            }
//            return
//        }
//        self.options[textView.tag-1000] = textView.text
//    }
//    @objc func addOptions(_ sender:UIButton){
//        let bgCView = UIView();
//        bgCView.tag=151;
//        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
//        bgCView.frame = UIScreen.main.bounds;
//        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
//        let view = ced_attradminOption()
//        view.tag = 399
//        view.frame.size = CGSize(width: 300,height: 185)
//        view.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
//        view.AddOption.addTarget(self, action: #selector(ced_manageLabelsAttr.AddOpt(_:)), for: UIControl.Event.touchUpInside)
//        view.backgroundColor = UIColor.black;
//        bgCView.addSubview(view)
//        view.center = self.view.center
//        self.view.addSubview(bgCView)
//    }
//
//    @objc func AddOpt(_ sender:UIButton){
//        let view = self.view.viewWithTag(399) as? ced_attradminOption
//        let text = view?.textView.text
//        let defaultd = view?.makeDefaultButton.isSelected
//
//        if(editAttrData.count != 0){
//            AdminOption.append(["newValue":text!,"id":""])
//        }
//        if(text == ""){
//            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Alert", showMsg: "Please Enter Some Text.")
//            return
//        }
//        if(defaultd!){
//            if(defaultOption.count != 0){
//                ShowSimpleAlert.showSimpleAlert(self, showTitle: "Alert", showMsg: "You Can Have Only One DefaultValue.")
//                return
//            }
//           self.defaultOption = ["\(text!)"]
//        }
//        self.options.append("\(text!)")
//        self.view.viewWithTag(151)?.removeFromSuperview()
//        manageLabeltable.reloadSections(IndexSet(integer: 0), with: UITableView.RowAnimation.fade)
//    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
       // return 306+CGFloat(60*options.count)
        return CGFloat(40 + self.storeDropdownArray.count*70)
    }
    
    @objc func SaveATTr(_ sender:UIButton){
        
        if self.frontEndField.text == "" || self.frontEndField.text == nil{
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error".localized, showMsg: "Default Label Is Required.".localized)
            return
        }
        
        let cell = manageLabeltable.cellForRow(at: IndexPath(row: 0, section: 0)) as! ced_newstoreLabelCellTableViewCell
        var store_FrontendLabel = [[String:String]]()
        cell.stackView.subviews.forEach { view in
            if let view = view as? storeLabelView{
               
                let key = view.value
                let label = view.textViewForText.text ?? ""
                print("gotcha \(key)\(label)")
                store_FrontendLabel.append(["key":key,"label":label])
            }
        }
        finaldata["store_frontend_label"] = store_FrontendLabel
        finaldata["frontend_label"] = [self.frontEndField.text ?? ""]
            
//        let cell = manageLabeltable.cellForRow(at: IndexPath(row: 0, section: 0)) as! ced_editAdminOptions
//        if(cell.textView.text == nil){
//            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: "Admin Label Is Required.")
//            return
//        }
//        var labelData = [String]()
//        if let frontendlabel = cell.textView.text {
//            labelData.append(frontendlabel)
//        }
//
//
//        if let defaultStoreval = cell.defaultStoreView.text {
//            labelData.append(defaultStoreval)
//        }
//        finaldata["frontend_label"] = labelData
//        if(editAttrData.count != 0){
//            var editedOptions = [String:String]()
//
//            print(AdminOption)
//            var i = 0
//            for editOption in AdminOption {
//                if(editOption["id"] != ""){
//                    let id = editOption["id"]!
//                   editedOptions["\(id)"] = editOption["option_name"]
//                }else{
//                   editedOptions["option_\(i)"] = (editOption["newValue"]!)
//                }
//                i += 1
//            }
//            var editOptionsData = String()
//            do{
//                let JSONData = try JSONSerialization.data(
//                    withJSONObject: editedOptions ,
//                    options: JSONSerialization.WritingOptions(rawValue: 0))
//                editOptionsData = NSString(data: JSONData,
//                    encoding: String.Encoding.ascii.rawValue)! as String
//
//            }
//            catch{
//                print("error in data encoding")
//
//            }
//            finaldata["option"] = editOptionsData
//        }else{
//        finaldata["option"] = options.description
//        }
//        finaldata["default"] = defaultOption.description
//        if(editAttrData.count != 0){
//            if(self.deleteItems.count != 0){
//                finaldata["delete_item"] = deleteItems.description
//            }
//        }
        print(finaldata)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        var itemsString = String()
        do{
            let JSONData = try JSONSerialization.data(
                withJSONObject: finaldata ,
                options: JSONSerialization.WritingOptions(rawValue: 0))
            itemsString = NSString(data: JSONData,
                encoding: String.Encoding.utf8.rawValue)! as String
            
        }
        catch{
            print("error in data encoding")
            
        }
        print(itemsString)
        //var url = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
        let url = "vproductattributeapi/index/saveAttribute"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(itemsString.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString  += "&attribute_data="+itemsString//.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!
        if(editAttrData.count != 0){
           postString  += "&attribute_id=" + attributeId
        }
        print(postString)
        self.sendRequest(url: url, parameters: postString)
       /* var request =  URLRequest(url: URL(string:url)!)

        request.httpMethod = "POST"
        request.httpBody = postString.data(using: String.Encoding.utf8)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        let task =  let sessionConfig = URLSessionConfiguration.default
            sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
            sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
            let session = URLSession(configuration: sessionConfig)
            session.dataTask(with: request, completionHandler: {
            data,response,error in
            DispatchQueue.main.async
                {
                    Alert_File.removeLoadingIndicator(self)
            }
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
            DispatchQueue.main.async
                {
                    let json = JSON(data: data!)
                    print(json)
                    if(json["data"]["success"].stringValue == "true"){
                        
                       
                        if let viewControl = UIStoryboard(name: "ProductAttributes", bundle: nil).instantiateViewController(withIdentifier: "vendor_attributeListing") as? ced_vendorProductAttributesListing {
                            
                            if let viewC = self.navigationController?.viewControllers.index(of: viewControl) {
                               if let viewpop = self.navigationController?.viewControllers[viewC] {
                                self.navigationController?.popToViewController(viewpop, animated: true)
                                }
                            }
                        }
                    }else{
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: json["data"]["message"].stringValue)
                    }
                    
            }
            
            
        })
        task.resume()*/
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if(json["data"]["success"].stringValue == "true"){
                
                self.view.makeToast(json["data"]["message"].stringValue)
                if let viewControl = UIStoryboard(name: "ProductAttributes", bundle: nil).instantiateViewController(withIdentifier: "vendor_attributeListing") as? ced_vendorProductAttributesListing {
                    
                    if let viewC = self.navigationController?.viewControllers.index(of: viewControl) {
                        if let viewpop = self.navigationController?.viewControllers[viewC] {
                            self.navigationController?.popToViewController(viewpop, animated: true)
                        }
                    }
                }
            }else{
                ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error".localized, showMsg: json["data"]["message"].stringValue)
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
