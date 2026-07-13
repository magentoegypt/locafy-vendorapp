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
import WebKit
class ced_orderDetailscell: UITableViewCell {
    
    @IBOutlet weak var orderIDTitle: UILabel!
    @IBOutlet weak var grandTotalTitle: UILabel!
    @IBOutlet weak var commisionFeeTitle: UILabel!
    @IBOutlet weak var paymentModeTitle: UILabel!
    @IBOutlet weak var totalPaymentTitle: UILabel!
    @IBOutlet weak var myWebView: WKWebView!
    
    @IBOutlet weak var orderIdButton: UIButton!
    
    @IBOutlet weak var grandTotalLabel: UILabel!
    
    @IBOutlet weak var commissionFeeLabel: UILabel!
    
    @IBOutlet weak var paymentModeLabel: UILabel!
    
    @IBOutlet weak var totalPaymentLabel: UILabel!
    
    
    @IBOutlet weak var containerView: UIView!
    
    @IBOutlet weak var topLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        //topLabel.backgroundColor = color
      
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
}

