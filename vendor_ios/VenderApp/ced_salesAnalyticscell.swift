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

class ced_salesAnalyticscell: UITableViewCell {

    @IBOutlet weak var headerView: UIView!
    @IBOutlet weak var imgicon: UIImageView!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var ChangeAction: UIButton!
    var data = true
 
    
    @IBOutlet weak var LineView: ScrollableGraphView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        ChangeAction.backgroundColor = color
        topLabel.backgroundColor = color
        topLabel.text="Sales Analytics".localized
        headerView.backgroundColor = color
        imgicon.backgroundColor = color
        LineView.backgroundColor = DynamicColor.ViewBackgroundColor
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
