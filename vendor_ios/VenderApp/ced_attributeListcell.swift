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

class ced_attributeListcell: UITableViewCell {
    @IBOutlet weak var attributeTitle: UILabel!
    @IBOutlet weak var attributeLabelTitle: UILabel!
    @IBOutlet weak var requiredTitle: UILabel!
    @IBOutlet weak var systemTitle: UILabel!
    @IBOutlet weak var visibleTitle: UILabel!
    @IBOutlet weak var scopeTitle: UILabel!
    @IBOutlet weak var saerchableTitle: UILabel!
    @IBOutlet weak var layeredTitle: UILabel!
    @IBOutlet weak var comparableTitle: UILabel!
    
    @IBOutlet weak var delete_atr_cell: UIButton!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var attriButeCode: UILabel!
    @IBOutlet weak var attributeLabel: UILabel!
    @IBOutlet weak var Required: UILabel!
    @IBOutlet weak var system: UILabel!
    @IBOutlet weak var visible: UILabel!
    @IBOutlet weak var scope: UILabel!
    @IBOutlet weak var searchable: UILabel!
    @IBOutlet weak var useinLayered: UILabel!
    @IBOutlet weak var comparable: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        // containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        attributeTitle.text="Attribute Code".localized
        attributeLabelTitle.text="Attribute Label".localized
        requiredTitle.text="Required".localized
        systemTitle.text="System".localized
        visibleTitle.text="Visible".localized
        scopeTitle.text="Scope".localized
        saerchableTitle.text="Searchable".localized
        layeredTitle.text="Use in Layered Navigation".localized
        comparableTitle.text="Comparable".localized
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
