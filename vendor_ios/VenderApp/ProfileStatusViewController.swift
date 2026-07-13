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

class ProfileStatusViewController: UIViewController
{

    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var leftLabel: UILabel!
    @IBOutlet weak var rightLabel: UILabel!
    @IBOutlet weak var profileProgressView: UIProgressView!
    let defaults = UserDefaults.standard
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        // Do any additional setup after loading the view.
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        
        let valert = userData["profile_complete"] as! String
        let progress = Float(Int(valert) ?? 1)/100
        print(progress)
        self.leftLabel.text = "Your Profile Is".localized;
        self.rightLabel.text = " \(valert)% "+"Completed".localized;
        self.profileProgressView.progressTintColor = UIColor.yellow;
        self.profileProgressView.layer.cornerRadius = 50;
        self.profileProgressView.transform = CGAffineTransform(scaleX: 1, y: 10)
        self.title = "Profile Status".localized
       ced_navigationBarController().addNavButton(self,str:"no")
        self.profileProgressView.setProgress(progress, animated: true);
    }

    override func didReceiveMemoryWarning()
    {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

   
}
