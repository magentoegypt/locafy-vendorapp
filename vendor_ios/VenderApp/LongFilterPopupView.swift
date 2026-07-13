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

class LongFilterPopupView: UIView {

    // Our custom view from the XIB file
    var view: UIView!
    
    @IBOutlet weak var topSpace: NSLayoutConstraint!
    
    @IBOutlet weak var idWidth: NSLayoutConstraint!
    @IBOutlet weak var value11width: NSLayoutConstraint!
    @IBOutlet weak var label7Height:  NSLayoutConstraint!
    @IBOutlet weak var value11Height: NSLayoutConstraint!
    //outlets
    @IBOutlet weak var ButtonTopSpace: UIView!
    
    @IBOutlet weak var value5TopHeight: NSLayoutConstraint!
    @IBOutlet weak var value5LabelHeight: NSLayoutConstraint!
    @IBOutlet weak var coreWrapperView: UIView!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var label1: UILabel!
    @IBOutlet weak var value11: UITextView!
    @IBOutlet weak var value12: UITextView!
    @IBOutlet weak var label2: UILabel!
    @IBOutlet weak var value21: UITextView!
    @IBOutlet weak var value22: UITextView!
    @IBOutlet weak var label3: UILabel!
    @IBOutlet weak var value31: UITextView!
    @IBOutlet weak var value32: UITextView!
    @IBOutlet weak var label4: UILabel!
    @IBOutlet weak var value4: UITextView!
    @IBOutlet weak var label5: UILabel!
    @IBOutlet weak var value5: UITextView!
    @IBOutlet weak var label6: UILabel!
    @IBOutlet weak var value6: UIButton!
    @IBOutlet weak var label7: UILabel!
    @IBOutlet weak var value7: UIButton!
    @IBOutlet weak var leftButton: UIButton!
    @IBOutlet weak var rightButton: UIButton!
    
    
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
        let color = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        let col = Ced_CommonVendor.UIColorFromRGB(color)
        
       // topLabel.backgroundColor = col
        leftButton.backgroundColor = col
        rightButton.backgroundColor = col
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "LongFilterPopupView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        leftButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        rightButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        return view
    }
    

}
