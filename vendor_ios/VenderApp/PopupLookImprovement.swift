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

class PopupLookImprovement: NSObject {
    
    class func designTextView(_ textView:UITextView)
    {
        textView.layer.borderColor = UIColor(red: 0.9, green: 0.9, blue: 0.9, alpha: 1.0).cgColor;
        textView.layer.borderWidth = 1.0;
        textView.layer.cornerRadius = 5;
    }
    
    class func designImageView(_ imgView:UIImageView)
    {
        imgView.layer.borderColor = UIColor(red: 0.9, green: 0.9, blue: 0.9, alpha: 1.0).cgColor
        imgView.layer.borderWidth = 1.0
        imgView.layer.cornerRadius = 0
    }
    
    class func designButton(_ button:UIButton)
    {
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        button.backgroundColor = color;
        button.layer.borderColor = color.cgColor;
        button.layer.borderWidth = 1.0;
        button.layer.cornerRadius = 5;
    }
    
    class func designButtonAsTextView(_ button:UIButton)
    {
        button.layer.borderColor = UIColor(red: 0.9, green: 0.9, blue: 0.9, alpha: 1.0).cgColor;
        button.layer.borderWidth = 1.0;
        button.layer.cornerRadius = 5;
    }
    
    
    class func designLabel(_ label:UILabel)
    {
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        label.layer.borderColor = color.cgColor;
        label.layer.borderWidth = 1.0;
        label.layer.cornerRadius = 10;
    }
    
  
}
