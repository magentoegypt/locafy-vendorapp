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

class ced_invoice: UITableViewCell {
    
    @IBOutlet weak var createShippingLabel: DLRadioButton!
    @IBOutlet weak var appendComments: DLRadioButton!
    @IBOutlet weak var emailInvoice: DLRadioButton!
    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var containerView: UIView!
    //Mark Variables from Save Credit memos
    @IBOutlet weak var subTotal: UILabel!
    @IBOutlet weak var refundShipping: UITextField!
    @IBOutlet weak var adjustmentShipping: UITextField!
    @IBOutlet weak var adjustmentFee: UITextField!
    @IBOutlet weak var grandtotal: UILabel!
    @IBOutlet weak var subtotalExcTaxLbl: UILabel!
    @IBOutlet weak var subtotalExcTax: UILabel!
    @IBOutlet weak var subtotalIncTaxLbl: UILabel!
    @IBOutlet weak var subtotalIncTax: UILabel!
    @IBOutlet weak var orderTaxLbl: UILabel!
    @IBOutlet weak var orderTax: UILabel!
    @IBOutlet weak var grandTotalExcTaxLbl: UILabel!
    @IBOutlet weak var grandtotalExcTax: UILabel!
    @IBOutlet weak var grandtotalIncTaxLbl: UILabel!
    @IBOutlet weak var grandTotalIncTax: UILabel!
    @IBOutlet weak var subtotalExcTaxStack: UIStackView!
    @IBOutlet weak var subtotalIncTaxStack: UIStackView!
    @IBOutlet weak var subtotalStack: UIStackView!
    @IBOutlet weak var taxStack: UIStackView!
    @IBOutlet weak var grandtotalExcTaxStack: UIStackView!
    @IBOutlet weak var grandtotalIncTaxStack: UIStackView!
    @IBOutlet weak var grandtotalStaxk: UIStackView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        emailInvoice.isMultipleSelectionEnabled = true
        appendComments.isMultipleSelectionEnabled = true
        if(createShippingLabel != nil){
            createShippingLabel.isMultipleSelectionEnabled = true
            createShippingLabel.contentHorizontalAlignment = .leading
            createShippingLabel.titleEdgeInsets = .init(top: 0, left: 8, bottom: 0, right: 0)
        }
        
        if(emailInvoice != nil){
            emailInvoice.contentHorizontalAlignment = .leading
            emailInvoice.titleEdgeInsets = .init(top: 0, left: 8, bottom: 0, right: 0)
        }
        
        if(appendComments != nil){
            appendComments.contentHorizontalAlignment = .leading
            appendComments.titleEdgeInsets = .init(top: 0, left: 8, bottom: 0, right: 0)
        }
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
}
