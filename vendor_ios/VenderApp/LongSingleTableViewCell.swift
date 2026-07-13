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

class LongSingleTableViewCell: UITableViewCell {

    var dataLoaded = false;
    @IBOutlet weak var mainWrapperView: UIView!
    @IBOutlet weak var separatorView: UIView!
    @IBOutlet weak var verticalStackView: UIStackView!
    @IBOutlet weak var wrapperScrollView: UIScrollView!
    
    
    @IBOutlet weak var mainStack: UIStackView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func setViews() {
        
        for i in 0...6 {
            let v = UIView()
            v.translatesAutoresizingMaskIntoConstraints = false
            
            v.backgroundColor = .red
            v.heightAnchor.constraint(equalToConstant: 50).isActive = true
            mainStack.addArrangedSubview(v)
        }
        
    }
}
