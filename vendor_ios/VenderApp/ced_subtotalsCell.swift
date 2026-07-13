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

class ced_subtotalsCell: UITableViewCell {

    @IBOutlet weak var t1: UILabel!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var t2: UILabel!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var subTotal: UILabel!
    @IBOutlet weak var t3: UILabel!
    @IBOutlet weak var shippinghandling: UILabel!
    @IBOutlet weak var t4: UILabel!
    @IBOutlet weak var grandTotalEarned: UILabel!
    @IBOutlet weak var commissionFee: UILabel!
    @IBOutlet weak var netEarned: UILabel!
    @IBOutlet weak var t5: UILabel!
    
    @IBOutlet weak var orderTax: UILabel!
    @IBOutlet weak var Grandtotal: UILabel!
    
    @IBOutlet weak var taxAmount: UILabel!
    
    @IBOutlet weak var discountLabel: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        if(containerView != nil){
            containerView.makeCard(containerView, cornerRadius: 1, color: UIColor.black, shadowOpacity: 0.4)
        }
        if(topLabel != nil){
            let color = Ced_CommonVendor.getInfoPlist("themecolor") as? String
            topLabel.backgroundColor = Ced_CommonVendor.UIColorFromRGB(color!)
        }
    
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
