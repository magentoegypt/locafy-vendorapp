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

class ced_productInformation: UITableViewCell {

    @IBOutlet weak var containerView: UIView!
    
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var productLabel: UILabel!
    
    @IBOutlet weak var productVal: UILabel!
    
    @IBOutlet weak var productSku: UILabel!
    
    @IBOutlet weak var productskuval: UILabel!
    
    @IBOutlet weak var productPrice: UILabel!
    
    @IBOutlet weak var priceval: UILabel!
    
    @IBOutlet weak var productqty: UILabel!
    
    @IBOutlet weak var productqtyval: UILabel!
    
    @IBOutlet weak var productsbtotal: UILabel!
    
    @IBOutlet weak var productTotalval: UILabel!
    
    
    @IBOutlet weak var taxAmount: UILabel!
    
    @IBOutlet weak var taxamountval: UILabel!
    
    @IBOutlet weak var discount: UILabel!
    
    @IBOutlet weak var discountVal: UILabel!
    
    @IBOutlet weak var RawTotal: UILabel!
    
    @IBOutlet weak var rawTotalVal: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
