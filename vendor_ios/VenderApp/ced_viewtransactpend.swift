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

class ced_viewtransactpend: UITableViewCell {
    
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var totalEarnedAmount: UILabel!
    @IBOutlet weak var totalPendingAmount: UILabel!
    @IBOutlet weak var pendingTransfers: UILabel!
    
    @IBOutlet weak var pendingTransactionLabel: UILabel!
    @IBOutlet weak var totalearnedLabel: UILabel!
    
    @IBOutlet weak var transactionstatics: UILabel!
    @IBOutlet weak var totalpendingLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        totalearnedLabel.backgroundColor = color
        totalpendingLabel.backgroundColor = color
        pendingTransactionLabel.backgroundColor = color
        transactionstatics.backgroundColor = color
        transactionstatics.text=" TRANSACTIONS STATISTICS".localized
        totalearnedLabel.text="Total Earned Ammount".localized
        totalpendingLabel.text="Transaction Total Pending Amount".localized
        pendingTransactionLabel.text="Pending Transfers".localized
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
