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

class CustomOptionView: UIView {

    // Our custom view from the XIB file
    var view: UIView!
    
    //outlets
    
    
    @IBOutlet weak var coreWrapperView: UIView!
    @IBOutlet weak var topDeleteButton: UIButton!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var tittleValue: UITextView!
    @IBOutlet weak var inputTypeLabel: UILabel!
    @IBOutlet weak var inputTypeButton: UIButton!
    @IBOutlet weak var isRequiredLabel: UILabel!
    @IBOutlet weak var isRequiredButton: UIButton!
    @IBOutlet weak var sortOrderLabel: UILabel!
    @IBOutlet weak var sortOrderValue: UITextView!
    
    
    @IBOutlet weak var bottomButton: UIButton!
    override init(frame: CGRect)
    {
        // 1. setup any properties here
        
        // 2. call super.init(frame:)
        super.init(frame: frame)
        
        // 3. Setup view from .xib file
        xibSetup()
    }
    
    required init?(coder aDecoder: NSCoder)
    {
        // 1. setup any properties here
        
        // 2. call super.init(coder:)
        super.init(coder: aDecoder)
        
        // 3. Setup view from .xib file
        xibSetup()
    }
    
    @objc func xibSetup()
    {
        view = loadViewFromNib()
        
        // use bounds not frame or it'll be offset
        view.frame = bounds
        
        // Make the view stretch with containing view
        view.autoresizingMask = [UIView.AutoresizingMask.flexibleWidth, UIView.AutoresizingMask.flexibleHeight]
        
        //designing code
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String;
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!);
        topDeleteButton.backgroundColor = color;
        bottomButton.backgroundColor = color;
        PopupLookImprovement.designTextView(tittleValue);
        PopupLookImprovement.designTextView(sortOrderValue);
        PopupLookImprovement.designButtonAsTextView(inputTypeButton);
        PopupLookImprovement.designButtonAsTextView(isRequiredButton);
        //designing code
        
        
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "CustomOptionView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
    


}
