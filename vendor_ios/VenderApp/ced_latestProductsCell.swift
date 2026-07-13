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

class ced_latestProductsCell: UICollectionViewCell {
    
    @IBOutlet weak var orderId: UILabel!
    @IBOutlet weak var orderIDTitle: UILabel!
    @IBOutlet weak var netEarnedTitle: UILabel!
    @IBOutlet weak var statusTitle: UILabel!
    @IBOutlet weak var billingTitle: UILabel!
    @IBOutlet weak var purchasedTitle: UILabel!
    
    @IBOutlet weak var netEarned: UILabel!
    
    @IBOutlet weak var orderStatus: UILabel!
    
    @IBOutlet weak var purchasedOn: UILabel!
    @IBOutlet weak var billingName: UILabel!
}
