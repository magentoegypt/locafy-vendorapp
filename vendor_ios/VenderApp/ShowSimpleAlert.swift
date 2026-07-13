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

class ShowSimpleAlert:NSObject  {

    class func showSimpleAlert(_ selfRef:UIViewController,showTitle:String,showMsg:String)
    {
        if #available(iOS 8.0, *) {
            let alertController = UIAlertController(title: showTitle.capitalized, message:
                showMsg.capitalized, preferredStyle: UIAlertController.Style.alert)
            alertController.addAction(UIAlertAction(title: "Ok".localized, style: UIAlertAction.Style.default,handler: { Void in
                //selfRef.navigationController?.popViewController(animated: true)
                
            }));
            selfRef.present(alertController, animated: true, completion: nil);
        } else {
            // Fallback on earlier versions
        };
        
    }
    class func dismissSimpleAlert(_ selfRef:UIViewController,showTitle:String,showMsg:String)
    {
        if #available(iOS 8.0, *) {
            let alertController = UIAlertController(title: showTitle.capitalized, message:
                showMsg.capitalized, preferredStyle: UIAlertController.Style.alert)
            alertController.addAction(UIAlertAction(title: "Dismiss", style: UIAlertAction.Style.default,handler: { Void in
                selfRef.navigationController?.popViewController(animated: true)
                
            }));
            selfRef.present(alertController, animated: true, completion: nil);
        } else {
            // Fallback on earlier versions
        };
        
    }
    

}
