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

class ced_shippingCreationCell: UITableViewCell {
    @IBOutlet weak var AddTrackingNumber: UIButton!

    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var value: UILabel!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var shippingInfo: UILabel!
    @IBOutlet weak var createShipment: DLRadioButton!
    override func awakeFromNib() {
        super.awakeFromNib()
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        // Initialization code
        if((createShipment) != nil){
        createShipment.isMultipleSelectionEnabled = true
        }
      
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
