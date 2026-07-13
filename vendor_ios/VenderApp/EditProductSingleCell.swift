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

class EditProductSingleCell: UITableViewCell {

    @IBOutlet weak var checkedButton: UIButton!
    @IBOutlet weak var mainWrapperView: UIView!
    @IBOutlet weak var productImage: UIImageView!
    
    @IBOutlet weak var imageViewHeight: NSLayoutConstraint!
    
    @IBOutlet weak var addAuctionButton: UIButton!
    
    
    @IBOutlet weak var label1: UILabel!
    @IBOutlet weak var value1: UILabel!
    @IBOutlet weak var label2: UILabel!
    @IBOutlet weak var value2: UILabel!
    @IBOutlet weak var label3: UILabel!
    @IBOutlet weak var value3: UILabel!
    @IBOutlet weak var label4: UILabel!
    @IBOutlet weak var value4: UILabel!
    @IBOutlet weak var label5: UILabel!
    @IBOutlet weak var value5: UILabel!
    @IBOutlet weak var label6: UILabel!
    @IBOutlet weak var value6: UILabel!
    @IBOutlet weak var label7: UILabel!
    @IBOutlet weak var value7: UILabel!
    @IBOutlet weak var editButton: UIButton!
    @IBOutlet weak var deleteButton: UIButton!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
         mainWrapperView.makeCard(mainWrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        deleteButton.backgroundColor = color
        editButton.backgroundColor = color
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
       
        // Configure the view for the selected state
    }

}
