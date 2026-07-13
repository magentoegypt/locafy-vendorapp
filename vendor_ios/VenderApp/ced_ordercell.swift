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

class ced_ordercell: UITableViewCell {

    @IBOutlet weak var ordercellContainer: UIView!
    
    @IBOutlet weak var shippingNoticeLabel: UILabel!
    @IBOutlet weak var shippTitle: UILabel!
    
    @IBOutlet weak var remainingTimeLabel: UILabel!
    @IBOutlet weak var remainTitle: UILabel!
    
    @IBOutlet weak var penaltyView: UIView!
    @IBOutlet weak var billingView: UIView!
    @IBOutlet weak var orderIdlbl: UILabel!
    @IBOutlet weak var billiNameLbl: UILabel!
    @IBOutlet weak var billingName: UILabel!
    @IBOutlet weak var orderIdval: UILabel!
    
    @IBOutlet weak var priceView: UIView!
    @IBOutlet weak var grandTotallabel: UILabel!
    @IBOutlet weak var grandTotal: UILabel!
    @IBOutlet weak var commissionLabel: UILabel!
    @IBOutlet weak var commissionval: UILabel!
    @IBOutlet weak var netEarnedlabel: UILabel!
    @IBOutlet weak var netearnedval: UILabel!
    
    
    
    var dataCheck = false;
    
    @IBOutlet weak var orderStatusView: UIView!
    @IBOutlet weak var orderpaymentstslbl: UILabel!
    @IBOutlet weak var vendorpaylabel: UILabel!
    @IBOutlet weak var orderStatus: UILabel!
    @IBOutlet weak var vendorstatus: UILabel!
    
    @IBOutlet weak var purchasedView: UIView!
    @IBOutlet weak var purchaselabl: UILabel!
    @IBOutlet weak var puchaseddate: UILabel!
    var dealtime = 100
    var timer = Timer()
    var startTime = TimeInterval()
    var timerStatus = String()
    var parent = UIViewController();
    var indexPath = 0;
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        ordercellContainer.makeCard(ordercellContainer, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.2)
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        priceView.layer.borderColor = Ced_CommonVendor.UIColorFromRGB(colorString!).cgColor
        priceView.layer.borderWidth = 2
        priceView.layer.cornerRadius = 4
        
        billingView.setThemeColor()//Ced_CommonVendor.UIColorFromRGB(colorString!)
        billingView.layer.borderColor = Ced_CommonVendor.UIColorFromRGB(colorString!).cgColor
        billingView.layer.borderWidth = 2
        billingView.layer.cornerRadius = 4
        
        orderStatusView.setThemeColor()//.backgroundColor = Ced_CommonVendor.UIColorFromRGB(colorString!)
        orderStatusView.layer.borderColor = Ced_CommonVendor.UIColorFromRGB(colorString!).cgColor
        orderStatusView.layer.borderWidth = 2
        orderStatusView.layer.cornerRadius = 4
        
        penaltyView.setThemeColor()//.backgroundColor = Ced_CommonVendor.UIColorFromRGB(colorString!)
        penaltyView.layer.borderColor = Ced_CommonVendor.UIColorFromRGB(colorString!).cgColor
        penaltyView.layer.borderWidth = 2
        penaltyView.layer.cornerRadius = 4
        
        purchasedView.layer.borderColor = Ced_CommonVendor.UIColorFromRGB(colorString!).cgColor
        purchasedView.layer.borderWidth = 2
        purchasedView.layer.cornerRadius = 4
        
        billiNameLbl.text="Billing Name :".localized
        orderIdlbl.text="Order ID :".localized
        grandTotallabel.text="Grand Total Order:".localized
       // netEarnedlabel.text="Net Earned:".localized
      //  commissionLabel.text="Commission Fee:".localized
        orderpaymentstslbl.text="Order Payment Status:".localized
        vendorpaylabel.text="Vendor Payment Status:".localized
        purchaselabl.text="Purchased On :".localized
        shippTitle.text="Shipping Notice:".localized
        remainTitle.text="Remaining Shipping Time:".localized
    }
    
    func reload()
    {
        Timer.scheduledTimer(timeInterval: 1, target: self, selector: #selector(update), userInfo: nil, repeats: true);
    }

    @objc func update() {
        //print(startTime)
        if(startTime>0){
            if let vc = parent as? ced_vendorOrderpannel
            {
                vc.timerCountArray[indexPath] = startTime;
            }
            var time = NSInteger(startTime)
            let seconds = time % 60
            let minutes = (time / 60) % 60
            let hours = (time%(24*3600) / 3600)
            let days = (time/(24*3600))
            
            remainingTimeLabel.text =  String(format: "%0.2d D:%0.2d H:%0.2d M:%0.2d S",days,hours,minutes,seconds)
            startTime = startTime - 1
        }
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }

}
