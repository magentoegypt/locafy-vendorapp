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

class ced_orderFilterpopup: UIView,UITextFieldDelegate {

    // Our custom view from the XIB file
    var view: UIView!
    @IBOutlet weak var titleID: UILabel!
    @IBOutlet weak var titlePurchasedOn: UILabel!
    @IBOutlet weak var titleBilling: UILabel!
    @IBOutlet weak var titleGrand: UILabel!
    @IBOutlet weak var titleCommision: UILabel!
    @IBOutlet weak var titleNet: UILabel!
    @IBOutlet weak var titleStatus: UILabel!
    @IBOutlet weak var titlevps: UILabel!
    
    @IBOutlet weak var topLabel: UILabel!
    //outlets
    @IBOutlet weak var orderIdval: UITextField!
    @IBOutlet weak var purchaseOnfrom: UITextField!
    @IBOutlet weak var purchasedonTo: UITextField!
    @IBOutlet weak var billingNamefrom: UITextField!
    @IBOutlet weak var grandtotalFrom: UITextField!
    @IBOutlet weak var grandtotalTo: UITextField!
    @IBOutlet weak var commisionfeefrom: UITextField!
    @IBOutlet weak var commisionfeeto: UITextField!
    @IBOutlet weak var netEarnedfrom: UITextField!
    @IBOutlet weak var netEarnedTo: UITextField!
    @IBOutlet weak var orderPaymentStatusbutton: UIButton!
    @IBOutlet weak var vendorPaymentStatus: UIButton!
    @IBOutlet weak var ResetFilter: UIButton!
    @IBOutlet weak var filterButton: UIButton!
    @IBOutlet weak var filterMainview: UIScrollView!
    
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
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        //topLabel.backgroundColor = color
        filterButton.backgroundColor = color
        ResetFilter.backgroundColor = color
        // Make the view stretch with containing view
        titleID.text="Order Id#".localized
        titlePurchasedOn.text="Purchased On:".localized
        titleBilling.text="Billing Name:".localized
        titleGrand.text="Grand Total:".localized
       // titleCommision.text="Commission Fee:".localized
        titleNet.text="Net Earned:".localized
        titleStatus.text="Order Payment Status:".localized
        titlevps.text="Vendor Payment Status:".localized
        filterButton.setTitle("FILTER".localized, for: .normal)
        ResetFilter.setTitle("RESET FILTER".localized, for: .normal)
        orderPaymentStatusbutton.setTitle("Please Select Option".localized, for: .normal)
        vendorPaymentStatus.setTitle("Please Select Option".localized, for: .normal)
        purchaseOnfrom.placeholder="From".localized
        purchasedonTo.placeholder="To".localized
        grandtotalTo.placeholder="To".localized
        grandtotalFrom.placeholder="From".localized
        commisionfeeto.placeholder="To".localized
        commisionfeefrom.placeholder="From".localized
        netEarnedfrom.placeholder="From".localized
        netEarnedTo.placeholder="To".localized
        view.autoresizingMask = [UIView.AutoresizingMask.flexibleWidth, UIView.AutoresizingMask.flexibleHeight]
        view.layer.borderColor = UIColor.black.cgColor
        view.layer.borderWidth = 0.2
//        purchaseOnfrom.tag = 7776
//        purchasedonTo.tag = 7777
//        purchasedonTo.delegate = self
//        purchaseOnfrom.delegate = self
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(view)
    }
    
    @objc func loadViewFromNib() -> UIView {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "ced_orderFilter", bundle: bundle)
       
        // Assumes UIView is top level and only object in CustomView.xib file
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
}
