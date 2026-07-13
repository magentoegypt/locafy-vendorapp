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

class ced_requestTransactFilter: UIView,UITextFieldDelegate {
       var view: UIView!
    
    @IBOutlet weak var orderId: UITextField!
    @IBOutlet weak var pendingAmountTo: UITextField!
    @IBOutlet weak var pendingAmountFrom: UITextField!
    @IBOutlet weak var orderDateTo: UITextField!
    @IBOutlet weak var orderDateFrom: UITextField!
    
    @IBOutlet weak var topLabel: UILabel!
    
    @IBOutlet weak var resetFilter: UIButton!
    @IBOutlet weak var Filter: UIButton!
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
        view.layer.borderColor = UIColor.black.cgColor
        view.layer.borderWidth = 0.3
       // topLabel.backgroundColor = color
        resetFilter.backgroundColor = color
        Filter.backgroundColor = color
        orderDateTo.delegate = self
        orderDateFrom.delegate = self
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "RequestTransactFilter", bundle: bundle)
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
    
    @objc func textFieldDidBeginEditing(_ textField: UITextField) {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateStyle = DateFormatter.Style.short
        dateFormatter.timeStyle = DateFormatter.Style.none
       
        DatePickerDialog().show("Select Date", doneButtonTitle: "Done", cancelButtonTitle: "Cancel", datePickerMode: UIDatePicker.Mode.date) {
            (date) -> Void in
             textField.text = dateFormatter.string(from: date);
        }
    }

}
