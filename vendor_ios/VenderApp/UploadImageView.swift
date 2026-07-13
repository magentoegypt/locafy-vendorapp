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

class UploadImageView: UIView {

    // Our custom view from the XIB file
    var view: UIView!
    
    //outlets
    
    @IBOutlet weak var imgView: UIImageView!
    @IBOutlet weak var delButton: UIButton!
    @IBOutlet weak var takephotobtn: UIButton!
    @IBOutlet weak var browseButton: UIButton!
    @IBOutlet weak var makeDefaultButton: UIButton!
    @IBOutlet weak var fileNamelbl: UILabel!
    @IBOutlet weak var defaultbtnHeight: NSLayoutConstraint!
    @IBOutlet weak var labelYAxis: NSLayoutConstraint!
    
    var fileUrlStr:String = ""
    
    override var intrinsicContentSize : CGSize {
        if isHidden {
            return CGSize.zero
        } else {
            return super.intrinsicContentSize
        }
    }
    
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
        
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        makeDefaultButton.contentHorizontalAlignment = .leading
        makeDefaultButton.titleEdgeInsets = .init(top: 0, left: 16, bottom: 0, right: 16)
        makeDefaultButton.imageEdgeInsets = .init(top: 0, left: 8, bottom: 0, right: 8)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "UploadImageView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }

}
