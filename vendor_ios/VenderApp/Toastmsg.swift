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


extension UIView
{
    @objc func showToastMsg(_ message:String)
    {
        let toast=UIView(); //making toast view
        //setting size and look of toast
        toast.frame=CGRect(x: 5, y: 0, width: UIWindow().frame.size.width - 10, height: 40);
        toast.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5)
        toast.center = CGPoint(x: self.frame.size.width  / 2,
            y: self.frame.size.height-100);
        toast.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin, UIView.AutoresizingMask.flexibleRightMargin, UIView.AutoresizingMask.flexibleTopMargin, UIView.AutoresizingMask.flexibleBottomMargin]
        toast.layer.cornerRadius=8.0;
        toast.clipsToBounds=true;
        toast.layer.shadowColor = UIColor.black.cgColor
        toast.layer.shadowOffset = CGSize.zero
        toast.layer.shadowOpacity = 0.5
        toast.layer.shadowRadius = 5
        //toast setting end
        let msg=UILabel();  //message inside toast
        //setting size and look of message
        msg.text=message.capitalized;
        msg.textAlignment=NSTextAlignment.center;
        msg.textColor=UIColor.white;
        msg.center=CGPoint(x: toast.frame.size.width  / 2,
            y: toast.frame.size.height/2);
        msg.frame=CGRect(x: 5, y: 0, width: UIWindow().frame.size.width - 10, height: 40);
        msg.numberOfLines = 0
        
        //end of message setting
        toast.addSubview(msg);  //adding message to toast
        self.addSubview(toast);  //adding toast to view
        //code to automatically remove toast with
        UIView.animate(withDuration: 1, delay:1, options:UIView.AnimationOptions.transitionFlipFromTop, animations: {
            toast.alpha = 0
            }, completion: { finished in
                toast.isHidden = true
        })
  
    }
    
}

