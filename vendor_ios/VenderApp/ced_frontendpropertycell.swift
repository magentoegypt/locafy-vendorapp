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

class ced_frontendpropertycell: UITableViewCell {
    
    @IBOutlet weak var l1: UILabel!
    @IBOutlet weak var l2: UILabel!
    @IBOutlet weak var l3: UILabel!
    @IBOutlet weak var l4: UILabel!
    @IBOutlet weak var l5: UILabel!
    @IBOutlet weak var l6: UILabel!
    
    @IBOutlet weak var l7: UILabel!
    @IBOutlet weak var l8: UILabel!
    @IBOutlet weak var l9: UILabel!
    @IBOutlet weak var l10: UILabel!
    @IBOutlet weak var l11: UILabel!
    @IBOutlet weak var l12: UILabel!
    @IBOutlet weak var l13: UILabel!
    
    
  
    @IBOutlet weak var useinquicksearch: UIButton!
    @IBOutlet weak var useinadvancedsearch: UIButton!
    @IBOutlet weak var comparabonfrontend: UIButton!
    @IBOutlet weak var usedinlayerednav: UIButton!
    @IBOutlet weak var usedinsearchlayerednav: UIButton!
    @IBOutlet weak var usedforpromorules: UIButton!
    @IBOutlet weak var position: UITextView!
    @IBOutlet weak var allowhtmlfrontend: UIButton!
    @IBOutlet weak var usedinattrbuteset: UIButton!
    @IBOutlet weak var usedproductfrontend: UIButton!
    @IBOutlet weak var usedinproductlisting: UIButton!
     @IBOutlet weak var usedinsorting: UIButton!
    
    @IBOutlet weak var enableWYSIWYG: UIButton!
    @IBOutlet weak var label: UILabel!
    @IBOutlet weak var Button: UIButton!
    var prevData = String()
    var tableView = UITableView()
    var parent     = UIViewController()
    var yesNoArray = [String]()
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        enableWYSIWYG.setTitleColor(.white, for: .normal)
        usedinattrbuteset.setTitleColor(.white, for: .normal)
        usedinlayerednav.setTitleColor(.white, for: .normal)
        usedinsearchlayerednav.setTitleColor(.white, for: .normal)
        
        
        l13.text = "Enable WYSIWYG".localized
        l12.text = "Used for Sorting in Product Listing".localized
        l11.text = "Used in Product Listing".localized
        l10.text = "Visible on Product View Page on Front-end".localized
        l9.text = "Include in Attribute Set".localized
        l8.text = "Allow HTML Tags on Frontend".localized
        l7.text = "Position".localized
        l6.text = "Use for Promo Rule Conditions".localized
        l5.text = "Use In Search Results Layered Navigation".localized
        l4.text = "Use In Layered Navigation".localized
        l3.text = "Comparable on Front-end".localized
        l2.text = "Use in Advanced Search".localized
        l1.text = "Use in Quick Search".localized
            
            
         useinquicksearch?.addTarget(self, action: #selector(ced_frontendpropertycell.selectOpt(_:)), for: UIControl.Event.touchUpInside)
        useinadvancedsearch?.addTarget(self, action: #selector(ced_frontendpropertycell.selectOpt(_:)), for: UIControl.Event.touchUpInside)
        comparabonfrontend?.addTarget(self, action: #selector(ced_frontendpropertycell.selectOpt(_:)), for: UIControl.Event.touchUpInside)
        usedinlayerednav?.addTarget(self, action: #selector(ced_frontendpropertycell.selectOpt(_:)), for: UIControl.Event.touchUpInside)
        usedinsearchlayerednav?.addTarget(self, action: #selector(ced_frontendpropertycell.selectOpt(_:)), for: UIControl.Event.touchUpInside)
        usedforpromorules?.addTarget(self, action: #selector(ced_frontendpropertycell.selectOpt(_:)), for: UIControl.Event.touchUpInside)
        allowhtmlfrontend?.addTarget(self, action: #selector(ced_frontendpropertycell.selectOpt(_:)), for: UIControl.Event.touchUpInside)
        usedinattrbuteset?.addTarget(self, action: #selector(ced_frontendpropertycell.selectOpt(_:)), for: UIControl.Event.touchUpInside)
        usedinproductlisting?.addTarget(self, action: #selector(ced_frontendpropertycell.selectOpt(_:)), for: UIControl.Event.touchUpInside)
        usedinsorting?.addTarget(self, action: #selector(ced_frontendpropertycell.selectOpt(_:)), for: UIControl.Event.touchUpInside)
         usedproductfrontend?.addTarget(self, action: #selector(ced_frontendpropertycell.selectOpt(_:)), for: UIControl.Event.touchUpInside)
        enableWYSIWYG?.addTarget(self, action: #selector(ced_frontendpropertycell.selectOpt(_:)), for: UIControl.Event.touchUpInside)
        
        
        
        let view2 = parent as? ced_frontendproperty
        print(prevData)
       
        if(prevData == "Text Area"){
            usedinlayerednav.isEnabled = false
            usedinlayerednav.alpha = 0.4
            position.isEditable = true
//            position.alpha = 0.4
            usedinsearchlayerednav.isEnabled = false
            usedinsearchlayerednav.alpha = 0.4
            usedinproductlisting.alpha = 0.4
            usedinproductlisting.isEnabled = false
           
            
        }else if(prevData == "Text Field"){
            usedinlayerednav.isEnabled = false
            usedinlayerednav.alpha = 0.4
            position.isEditable = true
//            position.alpha = 0.4
            usedinsearchlayerednav.isEnabled = false
            usedinsearchlayerednav.alpha = 0.4
            usedinproductlisting.alpha = 0.4
            usedinproductlisting.isEnabled = false
            enableWYSIWYG.isEnabled = false
            enableWYSIWYG.alpha = 0.4
            
            
        }else  if(prevData == "Date"){
            usedinlayerednav.isEnabled = false
            usedinlayerednav.alpha = 0.4
            position.isEditable = true
//            position.alpha = 0.4
            usedinsearchlayerednav.isEnabled = false
            usedinsearchlayerednav.alpha = 0.4
            usedinproductlisting.alpha = 0.4
            usedinproductlisting.isEnabled = false
            enableWYSIWYG.isEnabled = false
            enableWYSIWYG.alpha = 0.4
         
            
        }else  if(prevData == "Yes/No"){
            usedinlayerednav.isEnabled = false
            usedinlayerednav.alpha = 0.4
            position.isEditable = true
//            position.alpha = 0.4
            usedinsearchlayerednav.isEnabled = false
            usedinsearchlayerednav.alpha = 0.4
        
            enableWYSIWYG.isEnabled = false
            enableWYSIWYG.alpha = 0.4
            
        }
        else  if(prevData == "Multiple Select"){
          
            position.isEditable = true
//            position.alpha = 0.4
           
            usedinproductlisting.alpha = 0.4
            usedinproductlisting.isEnabled = false
            enableWYSIWYG.isEnabled = false
            enableWYSIWYG.alpha = 0.4
           
        }
        else  if(prevData == "Dropdown"){
            position.isEditable = true
//            position.alpha = 0.4
            
            enableWYSIWYG.isEnabled = false
            enableWYSIWYG.alpha = 0.4
            
        }
        else  if(prevData == "Price"){
            position.isEditable = true
//            position.alpha = 0.4
            
            enableWYSIWYG.isEnabled = false
            enableWYSIWYG.alpha = 0.4
            
            
        }
        else  if(prevData == "Media Image"){
            
        }
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
  
        // Configure the view for the selected state
    }
    
    @objc func selectOpt(_ sender:UIButton){
        let dropDown = DropDown();
        if(sender == usedinlayerednav){
            dropDown.dataSource = ["Select","Filterable(with results)","Filterable(no results)","No"];
            
        }else{
            dropDown.dataSource = yesNoArray
           // dropDown.dataSource = ["Select","Yes","No"];
        }
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            self.updateSection(item)
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
        
    }

    @objc func updateSection(_ strin:String){
        
        
       
        

        if(strin == "Filterable(with results)" || strin == "Filterable(no results)"){
            position.isEditable = true
//            position.alpha = 1
        }
        else{
            position.isEditable = true
//            position.alpha = 0.5
        }
    }

}
