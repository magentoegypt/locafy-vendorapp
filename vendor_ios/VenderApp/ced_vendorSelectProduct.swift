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

class ced_vendorSelectProduct: ced_VendorBaseClass {
    
    @IBOutlet weak var continueButton: UIButton!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var selectProductButton: UIButton!
    
    var selectedProductType = "";
    
    var baseURL = String();
    var allowed_pro_type = [String:String]();
    var stock = [String:String]();
    var taxes = [String:String]();
    var websites = [String:String]();
    var categoriesList = [String:[String]]();
    var storeViews = [String:[String:[String]]]();
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        self.fetchProductTypes();
        ced_navigationBarController().addNavButton(self,str:"no")
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.3)
        continueButton.addTarget(self, action: #selector(ced_vendorSelectProduct.continueTapped(_:)), for: UIControl.Event.touchUpInside)
        selectProductButton.addTarget(self, action: #selector(ced_vendorSelectProduct.selectProductType(_:)), for: UIControl.Event.touchUpInside)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func continueTapped(_ sender:UIButton){
        print(self.allowed_pro_type);
        print(self.selectedProductType);
        let selectedProductTypeId = self.allowed_pro_type[self.selectedProductType];
        print(selectedProductTypeId);
        
        let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "productDetailsViewController") as! ProductDetailsViewController;
        viewcontrollor.selectedProductTypeId = selectedProductTypeId!;
        viewcontrollor.stock = self.stock;
        viewcontrollor.taxes = self.taxes;
        viewcontrollor.storeViews = self.storeViews;
        viewcontrollor.websites = self.websites;
        viewcontrollor.categoriesList = self.categoriesList;
    self.navigationController?.pushViewController(viewcontrollor, animated: true)
        return;
    }
    
    @objc func selectProductType(_ sender:UIButton){
        
        let dropDown = DropDown();
        dropDown.dataSource = Array(self.allowed_pro_type.keys).reversed();
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            self.selectedProductType = item;
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
        
    }
    
    /** @objc function to fecth product types from API **/
    @objc func fetchProductTypes()
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        
        baseURL = "vendorapi/vproducts/allowedprodata/";
      
        let postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
        
        print(postString);
        sendRequest(url: baseURL, parameters: postString)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        
        if(requestUrl == "vendorapi/vproducts/allowedprodata/")
        {
            if let data = data{
                var jsonData = try! JSON(data: data);
                print("jsonData");
                print(jsonData);
                if(jsonData["data"]["success"].stringValue != "true")
                {
                    return;
                }
                
                /** fetching product types **/
                var firstButtonText = String();
                var counter = 0;
                for (_,val) in jsonData["data"]["allowed_pro_type"]
                {
                    if( counter == 0 && val["label"].stringValue != "" )
                    {
                        counter += 1;
                        firstButtonText = val["label"].stringValue;
                        self.selectedProductType = val["label"].stringValue;
                    }
                    self.allowed_pro_type[val["label"].stringValue] = val["value"].stringValue;
                }
                self.allowed_pro_type.removeValue(forKey: "");
                self.selectProductButton.setTitle(firstButtonText, for: UIControl.State());
                
                /** fetching stock values **/
                counter = 0;
                for (key,val) in jsonData["data"]["stock"]
                {
                    self.stock[val.stringValue] = key;
                }
                
                
                /** fetching stock values **/
                counter = 0;
                for (key,val) in jsonData["data"]["taxes"]
                {
                    self.taxes[val.stringValue] = key;
                }
                
                self.fectchParentAndChildCategories(jsonData["data"]["category"]);
                
                
                /** fetching websites data **/
                for (key,val) in jsonData["data"]["storeViews"]
                {
                    var temp = [String:[String]]();
                    for (inrKey,inrVal) in val
                    {
                        temp[inrKey] = inrVal.arrayObject as? [String];
                    }
                    
                    
                    self.storeViews[key] = temp;
                    
                }
                
                /** fetching websites **/
                for (key,val) in jsonData["data"]["websites"]
                {
                    self.websites[val.stringValue] = key;
                    print(key);
                    print(val);
                }
            }
        }

    }
    func fectchParentAndChildCategories(_ browseByJSON:JSON)
    {
        print(browseByJSON);
        for (_,val) in browseByJSON
        {
            var temp = [String]();
            for (_,inrVal) in val["sub_categories"]
            {
                temp.append(inrVal["main_category"].stringValue);
            }
            self.categoriesList[val["main_category"].stringValue] = temp;
        }
        print(self.categoriesList);
    }
    
}
