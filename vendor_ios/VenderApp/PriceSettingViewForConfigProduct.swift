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

class PriceSettingViewForConfigProduct: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate {
    
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var configApllyPriceTableView: UITableView!
    @IBOutlet weak var applyPriceButton: UIButton!
    
    
    var attributesPriceArray = [String:[String:[String:String]]](); // imp array to update price of attributes
    var attributesPriceArrayHelper = [String:[String:String]]();
    
    let screenSize: CGRect = UIScreen.main.bounds;
    let heightOfElement = CGFloat(35);
    var heightToUseGlobal = CGFloat(10);
    let padding = CGFloat(5);
    let sub = CGFloat(20);
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        self.configApllyPriceTableView.delegate = self;
        self.configApllyPriceTableView.dataSource = self;
        
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String;
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!);
        
        self.topLabel.text = "";
        self.topLabel.backgroundColor = color;
        
        self.applyPriceButton.setTitle("Apply Price", for: UIControl.State());
        self.applyPriceButton.addTarget(self, action: #selector(PriceSettingViewForConfigProduct.goBackToParentView(_:)), for: UIControl.Event.touchUpInside);
        self.applyPriceButton.backgroundColor = color;
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    // MARK: - Table view data source
    
    @objc func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return 1;
    }
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String;
        let theme_color = Ced_CommonVendor.UIColorFromRGB(colorString!);
        
        
        let cell = configApllyPriceTableView.dequeueReusableCell(withIdentifier: "longSingleCell", for: indexPath) as! LongSingleTableViewCell;
        if(cell.dataLoaded)
        {
            return cell;
        }
        cell.dataLoaded = true;
        
        for (key,val) in attributesPriceArray
        {
            var heightToUse = CGFloat(0);
            
            let tempOuterView = UIView();
            tempOuterView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            tempOuterView.backgroundColor = UIColor.white;
            
            let attributeNameLabelView = AttributeNameLabelView();
            attributeNameLabelView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            attributeNameLabelView.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-sub,height: heightOfElement);
            attributeNameLabelView.center.x = cell.mainWrapperView.center.x;
            
            attributeNameLabelView.attributeNamelabel.text = self.attributesPriceArrayHelper[key]!["attribute_label"]!;
            
            attributeNameLabelView.attributeNamelabel.textColor = UIColor.white;
            attributeNameLabelView.attributeNamelabel.backgroundColor = theme_color;
            
            
            heightToUse += heightOfElement;
            heightToUse += padding;
            tempOuterView.addSubview(attributeNameLabelView);
            
            for (inrKey,inrVal) in val
            {
                let priceAdditionView = PriceAdditionView();
                if let intVal = Int(inrKey){
                    priceAdditionView.tag = intVal;
                }
                
                priceAdditionView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                priceAdditionView.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-sub,height: heightOfElement);
                priceAdditionView.center.x = cell.mainWrapperView.center.x;
                
                priceAdditionView.attributeTermlabel.text = inrVal["value_label"]!;
                
                priceAdditionView.attributePrice.text = inrVal["pricing_value"]!;
                
                priceAdditionView.priceType.setTitle("Fixed", for: UIControl.State());
                if( inrVal["is_percent"]! == "1")
                {
                    priceAdditionView.priceType.setTitle("Percent", for: UIControl.State());
                }
                priceAdditionView.priceType.addTarget(self, action: #selector(PriceSettingViewForConfigProduct.showPriceTypeDropdown(_:)), for: UIControl.Event.touchUpInside);
                
                heightToUse += heightOfElement;
                heightToUse += padding;
                tempOuterView.addSubview(priceAdditionView);
                
            }
            
            tempOuterView.frame = CGRect(x: 0, y: heightToUseGlobal, width: screenSize.width-sub,height: heightToUse);
            tempOuterView.center.x = cell.mainWrapperView.center.x;
            tempOuterView.makeCard(tempOuterView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
            cell.mainWrapperView.addSubview(tempOuterView);
            heightToUseGlobal += heightToUse;
            heightToUseGlobal += (padding*2);
        }
        
        return cell;
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return heightToUseGlobal;
    }
    
    @objc func showPriceTypeDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = ["Fixed", "Percent"];
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
    
    @objc func goBackToParentView(_ sender:UIButton)
    {
        for (key,val) in self.attributesPriceArray
        {
            for (inrKey,_) in val
            {
                if let tag = Int(inrKey){
                    if let priceAdditionView = self.view.viewWithTag(tag) as? PriceAdditionView
                    {
                        let pricing_value = priceAdditionView.attributePrice.text;
                        self.attributesPriceArray[key]![inrKey]!["pricing_value"] = pricing_value;
                        
                        let is_percent = priceAdditionView.priceType.titleLabel?.text;
                        if(is_percent == "Fixed")
                        {
                            self.attributesPriceArray[key]![inrKey]!["is_percent"] = "0";
                        }
                        else
                        {
                            self.attributesPriceArray[key]![inrKey]!["is_percent"] = "1";
                        }
                        
                    }
                }
            }
        }
        
        print(attributesPriceArray);
        
        defaults.removeObject(forKey: "attributesPriceArray");
        defaults.set(self.attributesPriceArray, forKey: "attributesPriceArray");NotificationCenter.default.post(name: Notification.Name(rawValue: "updateAttributesPriceArrayFuncID"), object: nil);
        
        self.dismiss(animated: true, completion: {});
    }
    
}
