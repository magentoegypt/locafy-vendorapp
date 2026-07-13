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

class ced_attrfilterView: UIView {

    @IBOutlet weak var atrrCodeTitle: UILabel!
    @IBOutlet weak var atrrLabelTitle: UILabel!
    @IBOutlet weak var requiredTitle: UILabel!
    @IBOutlet weak var systemTitle: UILabel!
    @IBOutlet weak var visibleTitle: UILabel!
    @IBOutlet weak var scopeTitle: UILabel!
    @IBOutlet weak var searchableTitle: UILabel!
    @IBOutlet weak var layeredTtle: UILabel!
    @IBOutlet weak var comparableTitle: UILabel!
    
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var atributecode: UITextField!
    @IBOutlet weak var attributelabel: UITextField!
    @IBOutlet weak var required: UIButton!
    @IBOutlet weak var visible: UIButton!
    @IBOutlet weak var system: UIButton!
    @IBOutlet weak var searchable: UIButton!
    @IBOutlet weak var useinlayered: UIButton!
    @IBOutlet weak var scope: UIButton!
    @IBOutlet weak var resetFilter: UIButton!
    @IBOutlet weak var Filter: UIButton!
    @IBOutlet weak var comparable: UIButton!
    
    // Our custom view from the XIB file
    var view: UIView!
   
    override init(frame: CGRect) {
        // 1. setup any properties here
        
        // 2. call super.init(frame:)
        super.init(frame: frame)
        
        // 3. Setup view from .xib file
        xibSetup()
    }
    
    required init?(coder aDecoder: NSCoder) {
        // 1. setup any properties here
        
        // 2. call super.init(coder:)
        super.init(coder: aDecoder)
        
        // 3. Setup view from .xib file
        xibSetup()
    }
    
    @objc func xibSetup() {
        view = loadViewFromNib()
        
        // use bounds not frame or it'll be offset
        view.frame = bounds
        
        // Make the view stretch with containing view
        view.autoresizingMask = [UIView.AutoresizingMask.flexibleWidth, UIView.AutoresizingMask.flexibleHeight]
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        Filter.backgroundColor = color
        resetFilter.backgroundColor = color
    //    topLabel.backgroundColor = color
        atrrCodeTitle.text=" Attribute Code".localized
        atrrLabelTitle.text=" Attribute Label".localized
        requiredTitle.text=" Required".localized
        systemTitle.text=" System".localized
        visibleTitle.text=" Visible".localized
        scopeTitle.text=" Scope".localized
        searchableTitle.text=" Searchable".localized
        layeredTtle.text=" Use in Layered navigation".localized
        comparableTitle.text=" Comparable".localized
        Filter.setTitle("Filter".localized, for: .normal)
        resetFilter.setTitle("Reset Filters".localized, for: .normal)
        searchable.addTarget(self, action: #selector(ced_attrfilterView.selectFromDropdown(_:)), for: UIControl.Event.touchUpInside)
        visible.addTarget(self, action: #selector(ced_attrfilterView.selectFromDropdown(_:)), for: UIControl.Event.touchUpInside)
        system.addTarget(self, action: #selector(ced_attrfilterView.selectFromDropdown(_:)), for: UIControl.Event.touchUpInside)
        useinlayered.addTarget(self, action: #selector(ced_attrfilterView.selectFromDropdown(_:)), for: UIControl.Event.touchUpInside)
        scope.addTarget(self, action: #selector(ced_attrfilterView.selectFromDropdown(_:)), for: UIControl.Event.touchUpInside)
        comparable.addTarget(self, action: #selector(ced_attrfilterView.selectFromDropdown(_:)), for: UIControl.Event.touchUpInside)
        required.addTarget(self, action: #selector(ced_attrfilterView.selectFromDropdown(_:)), for: UIControl.Event.touchUpInside)
//        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
//        applyFilter.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
//        clearButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    @objc func selectFromDropdown(_ sender:UIButton){
        let dropDown = DropDown();
        if(sender.titleLabel?.text?.lowercased() == "Use in layered navigation".lowercased()){
            dropDown.dataSource = ["Select","Filterable(with results)","Filterable(no results)","No"];
        }else if(sender.titleLabel?.text == "Scope"){
            dropDown.dataSource = ["Select","Store View","Website","Global"];
        }
        else{
            dropDown.dataSource = ["Select","Yes","No"];
        }
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
    
    
    @objc func loadViewFromNib() -> UIView {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "ced_attrfilterView", bundle: bundle)
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
    
}
