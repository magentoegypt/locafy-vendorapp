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

class ced_transactiondetailss: UITableViewCell {

    @IBOutlet weak var trIDTitle: UILabel!
    @IBOutlet weak var trDate: UILabel!
    @IBOutlet weak var trMode: UILabel!
    @IBOutlet weak var trType: UILabel!
    @IBOutlet weak var trShippingAmount: UILabel!
    @IBOutlet weak var trAmount: UILabel!
    @IBOutlet weak var trAdjAmount: UILabel!
    @IBOutlet weak var trNetAmount: UILabel!
    @IBOutlet weak var trNotes: UILabel!
    @IBOutlet weak var containerView: UIView!
    
    @IBOutlet weak var topLabel: UILabel!
  
    @IBOutlet weak var transactionId: UILabel!
    @IBOutlet weak var type: UILabel!
    @IBOutlet weak var mode: UILabel!
    @IBOutlet weak var transactionDate: UILabel!
    @IBOutlet weak var amount: UILabel!
    @IBOutlet weak var adjustmentAmount: UILabel!
    @IBOutlet weak var netAmount: UILabel!
    @IBOutlet weak var notes: UILabel!
    @IBOutlet weak var shippingAmount: UILabel!
    @IBOutlet weak var viewDocumentlbl: UILabel!
    @IBOutlet weak var pdfHeight: NSLayoutConstraint!
    

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        topLabel.backgroundColor = color
        topLabel.text="Transaction Details".localized
        trIDTitle.text=" Transaction ID#".localized
        trDate.text=" Transaction Date".localized
        trMode.text=" Transaction Mode".localized
        trType.text=" Transaction Type".localized
        trShippingAmount.text=" Total Shipping Amount".localized
        trAmount.text=" Amount".localized
        trAdjAmount.text=" Adjustment Amount".localized
        trNetAmount.text=" Net Amount".localized
        trNotes.text=" Notes".localized
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
