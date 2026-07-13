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

class ced_transactData: UITableViewCell {

    @IBOutlet weak var createdTitle: UILabel!
    @IBOutlet weak var transactionIDTitle: UILabel!
    @IBOutlet weak var amountTitle: UILabel!
    @IBOutlet weak var adjAmountTitle: UILabel!
    @IBOutlet weak var netAmountTitle: UILabel!
    @IBOutlet weak var payModeTitle: UILabel!
    @IBOutlet weak var actionTitle: UILabel!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var CreatedAt: UILabel!
    @IBOutlet weak var transactionId: UILabel!
    @IBOutlet weak var amount: UILabel!
    @IBOutlet weak var adjustmentAmount: UILabel!    
    @IBOutlet weak var netAmount: UILabel!
    @IBOutlet weak var paymentMode: UILabel!
    
    @IBOutlet weak var actionButton: UIButton!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        actionTitle.text="  Action".localized
        payModeTitle.text=" Payment Mode".localized
        netAmountTitle.text=" Net Amount".localized
        adjAmountTitle.text=" Adjustment Amount".localized
        amountTitle.text=" Amount".localized
        transactionIDTitle.text=" Transaction Id".localized
        createdTitle.text=" Created At".localized
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
