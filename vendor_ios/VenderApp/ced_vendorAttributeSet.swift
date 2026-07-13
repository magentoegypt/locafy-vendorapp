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

class ced_vendorAttributeSet: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource {
    
    
    @IBOutlet weak var tableView: UITableView!
  
    var baseURL = String();
    var filterString = String()
    var applyFilter = false
    var attrSetData = [[String:String]]()
     var Floatbutton = UIButton()
        var del_button_tag:Int = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        tableView.delegate = self
        tableView.dataSource = self
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
           loadAttributeData(1)
        self.addFloatingbutton()
    }
    
    @objc func addFloatingbutton(){
        
        let bounds = self.view.bounds
        Floatbutton = UIButton(frame: CGRect(x: bounds.width-100, y: bounds.height-150, width: 50, height: 50))
        let imageview = UIImageView(frame: CGRect(x: bounds.width-100, y: bounds.height-150, width: 50, height: 50))
        imageview.image = UIImage(named: "add_attribute")
        imageview.layer.cornerRadius = imageview.frame.width/2
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        Floatbutton.backgroundColor = UIColor.clear
        imageview.backgroundColor = color
        imageview.contentMode = .center
        Floatbutton.addTarget(self, action: #selector(ced_vendorAttributeSet.addAttrSet(_:)), for: UIControl.Event.touchUpInside)
        self.view.insertSubview(imageview, aboveSubview: self.tableView)
        self.view.insertSubview(Floatbutton, aboveSubview: self.tableView)
    }
    
    @objc func addAttrSet(_ sender:UIButton){
        let story = UIStoryboard(name: "ProductAttributes", bundle: nil)
        let View  = story.instantiateViewController(withIdentifier: "createAttr") as! ced_vendorCreateAttr
        self.navigationController?.pushViewController(View, animated: true)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return attrSetData.count
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 100;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
         let cell=tableView.dequeueReusableCell(withIdentifier: "attrSet") as! ced_attrTableViewCell
            if let label = cell.viewWithTag(1250) as? UILabel{
            cell.attrValue.text = attrSetData[indexPath.row]["set_code"]
            cell.delete.addTarget(self, action: #selector(deleteProductRequestConfirmation(_:)), for: .touchUpInside)
            cell.delete.setThemeColor()
                cell.delete.tag = indexPath.row
                cell.delete.setTitle("Delete".localized, for: UIControl.State())
            cell.delete.tintColor=UIColor.white
            label.makeCard(label, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.3)
            }
            return cell
        }
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let attrSet = attrSetData[indexPath.row]
        print(attrSet)
        let story = UIStoryboard(name: "ProductAttributes", bundle: nil)
        let View  = story.instantiateViewController(withIdentifier: "createAttr") as! ced_vendorCreateAttr
        View.selectData = attrSet
        self.navigationController?.pushViewController(View, animated: true)
    }
    
    @objc func deleteProductRequestConfirmation(_ sender:UIButton)
    {
        let showTitle = "Confirmation".localized;
        let showMsg = "Are You Sure You Want To Delete It?".localized;
        
        if #available(iOS 8.0, *) {
            let confirmationAlert = UIAlertController(title: showTitle, message: showMsg, preferredStyle: UIAlertController.Style.alert)
            confirmationAlert.addAction(UIAlertAction(title: "Yes".localized, style: .default, handler: { (action: UIAlertAction!) in
                
                self.DeleteButtonClicked(sender);
                
            }));
            
            confirmationAlert.addAction(UIAlertAction(title: "No".localized, style: .default, handler: { (action: UIAlertAction!) in
                //print("Handle Cancel Logic here")
            }));
            
            present(confirmationAlert, animated: true, completion: nil)
        } else {
            // Fallback on earlier versions
        }
        
        
    }
    
    @objc func DeleteButtonClicked(_ sender:UIButton)
    {
      
        let page_id = attrSetData[sender.tag]["set_id"]!
        print(page_id)
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
        baseURL = "vproductattributeapi/set/delete"
        let postString1 = "id="+page_id+"&vendor_id="+vendorId+"&hashkey="+hashKey
        del_button_tag=sender.tag
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        self.sendRequest(url: baseURL, parameters: postString1)
//        if isLoadedFrom
//        {
//            let page_id = cmsData[sender.tag]["page_id"]!
//            baseURL = "vcmsapi/vcmspage/delete"
//            let postString1 = "page_id="+page_id+"&vendor_id="+vendorId+"&hashkey="+hashKey
//            del_button_tag=sender.tag
//            Alert_File.addLoadingIndicator(self, msg: "Please Wait...");
//            self.sendRequest(url: baseURL, parameters: postString1)
//        }
    }
    
    @objc func loadAttributeData(_ page:Int){
        attrSetData.removeAll()
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        //var baseUrl = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
        let baseUrl = "vproductattributeapi/set/info/page/\(page)"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        if(applyFilter){
            postString += "&filter=" + filterString.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!
        }
        self.sendRequest(url: baseUrl, parameters: postString)
        
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            if(requestUrl=="vproductattributeapi/set/delete")
            {
               guard let json = try? JSON(data: data) else {return}
                if(json["data"]["success"].stringValue.lowercased() == "false"){
                    self.view.showToastMsg(json["data"]["message"].stringValue)
                    
                }else if(json["data"]["success"].stringValue.lowercased() == "true"){
                    self.view.showToastMsg(json["data"]["message"].stringValue)
                    let indexPath = IndexPath(item: del_button_tag, section: 0)
                    self.attrSetData.remove(at: del_button_tag)
                    Alert_File.removeLoadingIndicator(self);
                    self.tableView.deleteRows(at: [indexPath], with: UITableView.RowAnimation.right)
                    self.tableView.reloadData()
                }
                
            }
            else
            {
               guard let json = try? JSON(data: data) else {return}
                if json["data"]["success"].stringValue.lowercased() == "true".lowercased(){
                    if json["data"]["attribute"].count > 0{
                        for attribute in json["data"]["attribute"].arrayValue{
                            self.attrSetData.append(["set_code":attribute["set_code"].stringValue,"set_id":attribute["set_id"].stringValue])
                        }
                        self.tableView.reloadData()
                    }else{
                        print("Attribute Set Is Empty")
                    }
                }else{
                   // self.view.makeToast( json["data"]["message"].stringValue)
                    self.tableView.setEmptyMessage(json["data"]["message"].stringValue)
                }
                print(json)
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
