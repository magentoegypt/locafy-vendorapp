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

class ced_benficarydetail: UITableViewCell {

    @IBOutlet weak var venderNameTitle: UILabel!
    @IBOutlet weak var paymentMethodTitle: UILabel!
    @IBOutlet weak var beneficaryDetailsTitle: UILabel!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var containerView: UIView!
    
    @IBOutlet weak var payMethod: UILabel!
    @IBOutlet weak var vendorName: UILabel!
    
    @IBOutlet weak var beneficaryDetails: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        topLabel.backgroundColor = color
        topLabel.text="Beneficiary Details".localized
        venderNameTitle.text = " Vendor Name".localized
        paymentMethodTitle.text=" Payment Method "
            .localized
        beneficaryDetailsTitle.text=" Beneficiary Details".localized
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
