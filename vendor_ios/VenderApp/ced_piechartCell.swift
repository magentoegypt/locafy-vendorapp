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

class ced_piechartCell: UITableViewCell {

    @IBOutlet weak var headerView: UIView!
    @IBOutlet weak var viewDetails: UIButton!
   
    @IBOutlet weak var approved: UILabel!
    
    @IBOutlet weak var pending: UILabel!
    @IBOutlet weak var noDataLabel: UILabel!
    
    @IBOutlet weak var disaproved: UILabel!
    @IBOutlet weak var piechartView: PieView2!
    @IBOutlet weak var topLabelTitle: UILabel!
    var data = true
    var parent = UIViewController()
    override func awakeFromNib() {
        super.awakeFromNib()
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        viewDetails.backgroundColor = color
        headerView.backgroundColor = color
        topLabelTitle.text=" Products Status".localized
        viewDetails.setTitle("View Details".localized, for: .normal)
        viewDetails.addTarget(self, action: #selector(ced_piechartCell.viewdeatails(_:)), for: UIControl.Event.touchUpInside)
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    @objc func viewdeatails(_ sender:UIButton){
        let storyBoard = UIStoryboard(name: "ProductAddon", bundle: nil)
        let viewcontrol = storyBoard.instantiateViewController(withIdentifier: "manageProductViewController") as! ManageProductViewController
    
        self.parent.navigationController?.pushViewController(viewcontrol, animated: true)
    }
    
}
