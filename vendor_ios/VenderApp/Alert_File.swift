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
class Alert_File: NSObject
{
    class func showNetworkIssue(_ me:UIViewController,title:String,message:String)
    {
        if #available(iOS 8.0, *) {
            let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertController.Style.alert)
            alert.addAction(UIAlertAction(title: "OK".localized, style: UIAlertAction.Style.default, handler: nil));
            me.present(alert, animated: true, completion: {
                me.viewDidLoad();
            });
        } else {
            // Fallback on earlier versions
        };
        
    }
    
    class func showOkAlert(_ view: UIViewController,_ title: String?,_ message: String?, onTapAction : ((Int)->Void)?){
        let alertController = UIAlertController(title: title, message: message, preferredStyle: .alert)
        let action = UIAlertAction(title: NSLocalizedString("Ok".localized, comment: ""), style: .default, handler: { (action2) in
            onTapAction?(1)
        })
        alertController.addAction(action)
        view.present(alertController, animated: true, completion: nil)
    }
    
    class func showValidationError(_ me:UIViewController,title:String,message:String)
    {
        if #available(iOS 8.0, *) {
            let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertController.Style.alert)
            alert.addAction(UIAlertAction(title: "Ok".localized, style: UIAlertAction.Style.default, handler: nil));
            me.present(alert, animated: true, completion: nil);
        } else {
            // Fallback on earlier versions
        };
       
    }
    
    class func addLoadingIndicator(_ me:UIViewController,msg:String)
    {
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        let view = NVActivityIndicatorView(frame: CGRect(x: 0, y: 0, width: 70, height: 70), type: .ballSpinFadeLoader, color: UIColor(hexString: colorString), padding: 5);
        view.tag=123321123;
        view.center = me.view.center;
        view.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight, UIView.AutoresizingMask.flexibleWidth];
        view.startAnimating()
        //view.backgroundColor = UIColor(red:255, green:255, blue:255, alpha:0.9)
        me.view.addSubview(view);
        me.view.isUserInteractionEnabled = false
        //view.showLoadingMsg("");
        //view.showLoadingMsg(msg);
    }
    
    
    class func removeLoadingIndicator(_ me:UIViewController)
    {
        
        if let vc = me.view.viewWithTag(123321123) as? NVActivityIndicatorView
        {
            vc.stopAnimating();
            vc.removeFromSuperview();
            me.view.isUserInteractionEnabled = true
        }
        
    }
    
    class func networkErrorMessage(_ me:UIViewController)
    {
        if #available(iOS 8.0, *) {
            let alert = UIAlertController(title: "Alert", message: "No Internet Connection Available", preferredStyle: UIAlertController.Style.alert)
            alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
            me.present(alert, animated: true, completion: nil);
        } else {
            // Fallback on earlier versions
        }
       
    }
    
    class func networkErrorImage(_ me:UIViewController)
    {
        let imgView=UIImageView(image: UIImage(named: "server_maintainance"));
        imgView.tag=1233211134;
        let frame=UIScreen.main.bounds;
        imgView.frame=CGRect(x: (frame.width/2)-50+15,y: 130,width: 70,height: 70);
        imgView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin, UIView.AutoresizingMask.flexibleRightMargin];
        
        let label=UILabel(frame: CGRect(x: (frame.width/2)-125,y: 180+20,width: 250,height: 15));
        label.tag=1233211122;
        label.text="Unable To Fetch Data!!";
        label.font = UIFont(name:"HelveticaNeue-Bold", size: 20)
        label.textAlignment=NSTextAlignment.center;
        label.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin, UIView.AutoresizingMask.flexibleRightMargin];
        me.view.addSubview(imgView);
        me.view.addSubview(label);
    }
    
    class func removeNetworkErrorImage(_ me:UIViewController)
    {
        me.view.viewWithTag(1233211134)?.removeFromSuperview();
        me.view.viewWithTag(1233211122)?.removeFromSuperview();
    }
    
    
    class func noDataImage(_ me:UIViewController)
    {
        let imgView=UIImageView(image: UIImage(named: "cart"));
        let frame=UIScreen.main.bounds;
        imgView.frame=CGRect(x: (frame.width/2)-50+15,y: 130,width: 70,height: 70);
        imgView.tag=12332111;
        imgView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin, UIView.AutoresizingMask.flexibleRightMargin];
        
        let label=UILabel(frame: CGRect(x: (frame.width/2)-125,y: 180+20,width: 250,height: 25));
        label.tag=123321112;
        label.text="Your Cart is Empty!".capitalized;
        label.font = UIFont(name:"HelveticaNeue-Bold", size: 20)
        label.textAlignment=NSTextAlignment.center;
        label.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin, UIView.AutoresizingMask.flexibleRightMargin];
        me.view.addSubview(imgView);
        me.view.addSubview(label);
    }
    
    class func removeNoDataImage(_ me:UIViewController)
    {
        me.view.viewWithTag(12332111)?.removeFromSuperview();
        me.view.viewWithTag(123321112)?.removeFromSuperview();
    }

    class func loadingpost(_ me:UIViewController){
        let view=UIView();
        view.tag=898989;
        view.frame = UIScreen.main.bounds
        view.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight, UIView.AutoresizingMask.flexibleWidth];
      //  view.backgroundColor = UIColor.whiteColor()
        //view.alpha = 0.5
        me.view.addSubview(view);
        view.showLoadingpost()
    }
    class func removeLoadingpost(_ me:UIViewController)
    {
        me.view.viewWithTag(898989)?.removeFromSuperview();
    }

}
