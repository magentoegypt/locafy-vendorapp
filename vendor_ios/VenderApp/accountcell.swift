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
 * @package   MageNative
 * @author    CedCommerce Core Team <connect@cedcommerce.com >
 * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 * @license      http://cedcommerce.com/license-agreement.txt
 */


import UIKit

class accountcell: UITableViewCell {

    @IBOutlet weak var MYAccountLable: UILabel!
    @IBOutlet weak var circleView: UIView!
    @IBOutlet weak var rightView:UIImageView!
    @IBOutlet weak var lineView: UIImageView!
    @IBOutlet weak var ImageView: UIImageView!
    @IBOutlet weak var cell_view: UIView!
    @IBOutlet weak var label: UILabel!
    @IBOutlet weak var editProfile: UIButton!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
//        label.fontColorTool()
        
//        editProfile.fontColorTool()
      
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
