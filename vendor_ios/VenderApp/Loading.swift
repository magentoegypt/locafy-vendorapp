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
    @objc func showLoadingMsg(_ message:String)
    {
        let loadingAlert=UIView();
        let bounds = UIScreen.main.bounds
        loadingAlert.frame=CGRect(x: 20, y: 0, width: bounds.width - 40,height: 60);
        loadingAlert.backgroundColor = UIColor.white
        loadingAlert.makeCard(loadingAlert, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        loadingAlert.center = CGPoint(x: self.frame.size.width  / 2,
            y: self.frame.size.height/2);
       let label = UILabel(frame: CGRect(x: 90, y: 10,  width: loadingAlert.frame.width-80, height: 40))
        label.text = "Please Wait!Loading...".capitalized;
       //label.textColor = UIColor.whiteColor()
        loadingAlert.addSubview(label)
        let imgView=UIImageView(image: UIImage(named: "loader"));  //making image view to act as custom loading indicator
        imgView.frame=CGRect(x: 20, y: 5, width: 50, height: 50);
        loadingAlert.tag = 654321
        loadingAlert.addSubview(imgView)
        self.runSpinAnimationOn(imgView, duration: 1, rotation: M_PI / 2 / 60, repeat: MAXFLOAT)       
        self.addSubview(loadingAlert);  //adding toast to view
       
    }
    
    //code to rotate Image as loading Indicator
    @objc func runSpinAnimationOn(_ view: UIView, duration: Double, rotation: Double, repeat: Float) {
        let animation = CABasicAnimation(keyPath: "transform.rotation.z")
        animation.fromValue = NSNumber(value: 0.0 as Float)
        animation.toValue = NSNumber(value: 3.14 * 2.0 as Float)
        animation.duration = duration
        animation.isCumulative = true
        animation.repeatCount = `repeat`
        animation.isRemovedOnCompletion = false
        animation.fillMode = CAMediaTimingFillMode.forwards;
        view.layer.add(animation, forKey: "rotationAnimation")
    }
    //end of code to rotate

    //loading post images 
    
    @objc func showLoadingpost()
    {
        let loadingAlert=UIView(); //making toast view
        //setting size and look of toast
        loadingAlert.frame=CGRect(x: 0, y: 0, width: 60, height: 60);
        loadingAlert.backgroundColor = UIColor.white
        loadingAlert.alpha = 0.7;
        loadingAlert.center = CGPoint(x: self.frame.size.width  / 2,
            y: self.frame.size.height/2);
        loadingAlert.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin, UIView.AutoresizingMask.flexibleRightMargin, UIView.AutoresizingMask.flexibleTopMargin, UIView.AutoresizingMask.flexibleBottomMargin]
        //loadingAlert.layer.cornerRadius=8.0;
        loadingAlert.clipsToBounds=true;
       // loadingAlert.layer.shadowColor = UIColor.whiteColor().CGColor
        loadingAlert.layer.shadowOffset = CGSize.zero
        loadingAlert.layer.shadowOpacity = 0.5
        //loadingAlert.layer.shadowRadius = 5
        //toast setting end
        self.addSubview(loadingAlert);  //adding toast to view
        let imgView=UIImageView(image: UIImage(named: "close"));  //making image view to act as custom loading indicator
        imgView.frame=CGRect(x: 0, y: 0, width: 60, height: 60);
        loadingAlert.tag = 654321
        loadingAlert.addSubview(imgView)
        self.runSpinAnimationOn(imgView, duration: 1, rotation: M_PI / 2 / 60, repeat: MAXFLOAT)
        
    }
    @objc func removeloading (){
        viewWithTag(654321)?.removeFromSuperview()
    }

    
}
