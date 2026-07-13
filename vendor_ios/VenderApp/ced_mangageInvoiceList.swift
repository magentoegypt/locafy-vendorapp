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

class ced_mangageInvoiceList: UITableViewCell {

    @IBOutlet weak var cotainerView: UIView!
    @IBOutlet weak var invoiceId: UILabel!
    @IBOutlet weak var invoiceDate: UILabel!
    @IBOutlet weak var billingName: UILabel!
    @IBOutlet weak var amount: UILabel!    
    @IBOutlet weak var status: UILabel!
    @IBOutlet weak var invoiceIdLabel: UILabel!
    @IBOutlet weak var InvoiceDateLabel: UILabel!
    @IBOutlet weak var statuslabel: UILabel!
    @IBOutlet weak var amountLabel: UILabel!
    @IBOutlet weak var billingNameLabel: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        if(cotainerView != nil){
            cotainerView.makeCard(cotainerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        }
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
