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

class ced_requesttransactFirst: UITableViewCell {

    @IBOutlet weak var mainContainerView: UIView!
    @IBOutlet weak var containerView: UIView!
    
    @IBOutlet weak var paymentsStatictics: UILabel!
    @IBOutlet weak var totalRequestdamountlabel: UILabel!
    @IBOutlet weak var totalPendingamountLabel: UILabel!
    @IBOutlet weak var totalPendingAmount: UILabel!
    @IBOutlet weak var totalRequestedAmount: UILabel!
    
    @IBOutlet weak var totalPaidAmount: UILabel!
    
    @IBOutlet weak var totalPaidAmountLabel: UILabel!
    
   @IBOutlet weak var massRequestButton: UIButton!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
         mainContainerView.makeCard(mainContainerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
         containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        paymentsStatictics.backgroundColor = color
        totalPendingamountLabel.backgroundColor = color
        totalRequestdamountlabel.backgroundColor = color
        totalPaidAmountLabel.backgroundColor = color
       // massRequestButton.titleLabel?.numberOfLines = 0
        //massRequestButton.layer.borderColor = UIColor.black.cgColor
        //massRequestButton.layer.borderWidth = 0.2
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        
        super.setSelected(selected, animated: animated)
        // Configure the view for the selected state
    }

}
