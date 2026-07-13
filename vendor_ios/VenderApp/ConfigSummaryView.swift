

import UIKit

class ConfigSummaryView: UIView {

    var view = UIView()
    
    
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var photobtn: UIButton!
    @IBOutlet weak var nameTxtField: UITextField!
    @IBOutlet weak var skuTxtField: UITextField!
    @IBOutlet weak var skuTxtLabel: UILabel!
    @IBOutlet weak var priceTxtField: UITextField!
    @IBOutlet weak var quantityTxtField: UITextField!
    @IBOutlet weak var weightTxtField: UITextField!
    @IBOutlet weak var statusLabel: UILabel!
    @IBOutlet weak var attributeLabel: UILabel!
    @IBOutlet weak var selectButton: UIButton!
    @IBOutlet weak var nameHeight: NSLayoutConstraint!
    @IBOutlet weak var weightHeight: NSLayoutConstraint!
    @IBOutlet weak var statusHeight: NSLayoutConstraint!
    
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
        
        //designing code
        
        
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "ConfigSummaryView", bundle: bundle)
        
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }

}
