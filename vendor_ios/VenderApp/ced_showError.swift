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

class ced_showError: NSObject {
    class func showNoModule(_ selfRef: UIViewController)
    {
        let errorView = UIView();
        errorView.frame = CGRect(x: 0, y: 0, width: selfRef.view.frame.width, height: selfRef.view.frame.height);
        errorView.backgroundColor = UIColor.white;
        errorView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight,UIView.AutoresizingMask.flexibleWidth]
        
        
        let imgView = UIImageView();
        imgView.frame = CGRect(x: 0, y: 20, width: selfRef.view.frame.width, height: selfRef.view.frame.height);
        imgView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight,UIView.AutoresizingMask.flexibleWidth]
        imgView.image =  UIImage(named: "no_module");
        imgView.contentMode = .scaleAspectFit
        
        
        errorView.addSubview(imgView);
        
        selfRef.view.addSubview(errorView);
    }
    
    class func showNoNetWork(_ selfRef: UIViewController)
    {
        let errorView = UIView();
        errorView.frame = CGRect(x: 0, y: 0, width: selfRef.view.frame.width, height: selfRef.view.frame.height);
        errorView.backgroundColor = UIColor.white;
        errorView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight,UIView.AutoresizingMask.flexibleWidth]
        
        
        let imgView = UIImageView();
        imgView.frame = CGRect(x: 0, y: 40, width: selfRef.view.frame.width, height: selfRef.view.frame.height);
        imgView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight,UIView.AutoresizingMask.flexibleWidth]
        imgView.image =  UIImage(named: "nointernet");
        imgView.contentMode = .scaleAspectFit
        
        
        errorView.addSubview(imgView);
        
        selfRef.view.addSubview(errorView);
    }

}
