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

class ced_vendorCreateAttr: ced_VendorBaseClass {

    
    @IBOutlet weak var createTitle: UILabel!
    @IBOutlet weak var nameTitle: UILabel!
    @IBOutlet weak var basedOnlabel: UILabel!
    @IBOutlet weak var name: UITextField!
    @IBOutlet weak var basedOn: UITextField!
    @IBOutlet weak var saveAttribute: UIButton!
    @IBOutlet weak var selectDropDown: UIButton!
    
    var selectData = [String:String]()
    
    var attrSetData = [[String:String]]()
    var attrVal      = [String]()
    var attrId       = [String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        // Do any additional setup after loading the view.
        saveAttribute.setTitle("Save".localized, for: .normal)
        createTitle.text="Create Attribute Set".localized
        nameTitle.text="Name".localized
        basedOnlabel.text="Based On".localized
        saveAttribute.addTarget(self, action: #selector(ced_vendorCreateAttr.createAttrSet(_:)), for: .touchUpInside)
        selectDropDown.addTarget(self, action: #selector(ced_vendorCreateAttr.showorderDrop(_:)), for: .touchUpInside)
        print(selectData)
        if selectData.count > 0{
            selectDropDown.isHidden = true
            basedOnlabel.isHidden = true
            name.text = selectData["set_code"] ?? ""
        }
        
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        saveAttribute.backgroundColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
      
        loaddefaultSet()
        
    }
    
    //Create Or update Attribute Set Button Clicked
    @objc func createAttrSet(_ sender:UIButton){
        if let name = name.text {
            if name != "" {
                if (selectDropDown.titleLabel?.text != NSLocalizedString("Select", comment: "") || selectData.count > 0){
                    let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
                    //print(userData)
                    let vendorId = userData["vendorId"] as! String
                    let hashKey    = userData["hashKey"] as! String
                    var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
                    Alert_File.addLoadingIndicator(self, msg: "")
                    //var baseUrl = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
                    var baseUrl=""
                    if selectData.count > 0 {
                        postString += "&attribute_set_name="+name+"&set_id="+selectData["set_id"]!
                        baseUrl += "vproductattributeapi/set/update"
                    }else{
                        if(selectDropDown.currentTitle == "Select")
                        {
                            self.view.showToastMsg("Please select attribute set");
                            return;
                        }
                        for index in attrSetData{
                            if(index["label"] == selectDropDown.currentTitle)
                            {
                                postString += "&attribute_set_name="+name+"&skeleton_set="+index["value"]!;
                            }
                        }
                        
                        baseUrl += "vproductattributeapi/set/create"
                    }
                    print(baseUrl)
                    print(postString)
                    self.sendRequest(url: baseUrl, parameters: postString)
                }
            }else{
                
            }
        }
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            if(requestUrl=="vproductattributeapi/set/GetParentSet")
            {
                guard let json = try? JSON(data: data) else {return}
                print(json)
                // if json["data"]["success"].stringValue.lowercased() == "true".lowercased()
                if json["data"]["attribute_set"].count > 0{
                    for attribute in json["data"]["attribute_set"].arrayValue{
                        
                        self.attrId.append(attribute["value"].stringValue)
                        self.attrVal.append(attribute["label"].stringValue)
                        self.attrSetData.append(["value":attribute["value"].stringValue,"label":attribute["label"].stringValue])
                        print(attrSetData)
                    }
                    
                }else{
                    print("Attribute Set Is Empty")
                }
                
            }
            else
            {
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if json["data"]["success"].stringValue == "true" {
                    self.view.showToastMsg( json["data"]["message"].stringValue)
                    self.navigationController?.popViewController(animated: true)
                }else{
                    self.view.showToastMsg( json["data"]["message"].stringValue)
                }
                
            }
        }
        
    }
    @objc func showorderDrop(_ sender:UIButton){
        let dropDown = DropDown();
        dropDown.dataSource = attrVal;
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

    
    @objc func loaddefaultSet() {
        //"vproductattributeapi/set/GetParentSet"
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        //var baseUrl = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
        let baseUrl = "vproductattributeapi/set/GetParentSet"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        self.sendRequest(url: baseUrl, parameters: postString)

        
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
