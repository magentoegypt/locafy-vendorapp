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

class ced_viewFilter: UIView,UITextFieldDelegate{
    

    @IBOutlet weak var t1: UILabel!
    @IBOutlet weak var adjAmountTitle: UILabel!
  
    @IBOutlet weak var tID: UILabel!
    @IBOutlet weak var paymentModeTitle: UILabel!

    @IBOutlet weak var netamountTitle: UILabel!
    @IBOutlet weak var amountTitle: UILabel!
    @IBOutlet weak var toplabel: UILabel!
    @IBOutlet weak var createdAtfrom: UITextField!
    
    var view: UIView!
    @IBOutlet weak var createdTo: UITextField!
    @IBOutlet weak var paymentMode: UITextField!
    @IBOutlet weak var trasactionId: UITextField!
    @IBOutlet weak var amountFrom: UITextField!
    @IBOutlet weak var amountTo: UITextField!
    
    @IBOutlet weak var adjustmentAmount: UITextField!
    
    @IBOutlet weak var adjustmentAmountTo: UITextField!
    
    @IBOutlet weak var netamountfrom: UITextField!
    
    @IBOutlet weak var netAmountTo: UITextField!
    
    @IBOutlet weak var applyFilters: UIButton!
    @IBOutlet weak var resetFilters: UIButton!
    
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
       // toplabel.backgroundColor = color
        applyFilters.backgroundColor = color
        resetFilters.backgroundColor = color
        t1.text="Created At".localized
        paymentModeTitle.text="Payment Mode".localized
        tID.text="TransactionId".localized
        amountTitle.text="Amount".localized
        adjAmountTitle.text="Adjustment Amount".localized
        netamountTitle.text="Net Amount".localized
        applyFilters.setTitle("Filters".localized, for: .normal)
        resetFilters.setTitle("Reset Filters".localized, for: .normal)
//        createdAtfrom.delegate = self
//        createdTo.delegate = self
        amountFrom.placeholder="From".localized
        amountTo.placeholder="To".localized
        netamountfrom.placeholder="From".localized
        netAmountTo.placeholder="To".localized
        createdTo.placeholder="To".localized
        createdAtfrom.placeholder="From".localized
        adjustmentAmountTo.placeholder="To".localized
        adjustmentAmount.placeholder="From".localized
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "viewFilter", bundle: bundle)
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
    
    
    /*
    // Only override drawRect: if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func drawRect(rect: CGRect) {
    // Drawing code
    }
    */
//    @objc func textFieldDidBeginEditing(_ textField: UITextField) {
//        let dateFormatter = DateFormatter()
//        dateFormatter.dateStyle = DateFormatter.Style.short
//        dateFormatter.timeStyle = DateFormatter.Style.none
//        
//        DatePickerDialog().show("Select Date", doneButtonTitle: "Done", cancelButtonTitle: "Cancel", datePickerMode: UIDatePicker.Mode.date) {
//            (date) -> Void in
//            textField.text = dateFormatter.string(from: date);
//        }
//    }

    
}
