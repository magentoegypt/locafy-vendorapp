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
// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func < <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l < r
  case (nil, _?):
    return true
  default:
    return false
  }
}

// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func > <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l > r
  default:
    return rhs < lhs
  }
}


class ced_navigationBarController: UINavigationController,UIViewControllerTransitioningDelegate {

     var mytitle = "Dashboard"
    
    let defaults = UserDefaults.standard
    var str = String()
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        self.navigationBar.tintColor = UIColor.black;
        // Do any additional setup after loading the view.
        
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        self.navigationBar.barTintColor = Ced_CommonVendor.UIColorFromRGB(colorString)
        
        if #available(iOS 13.0, *) {
            let appearance = UINavigationBarAppearance()
            appearance.configureWithOpaqueBackground()
            appearance.backgroundColor = UIColor.white//Ced_CommonVendor.UIColorFromRGB(colorString)
            let textAttributes = [NSAttributedString.Key.foregroundColor:UIColor.black]
            appearance.titleTextAttributes = textAttributes
            navigationBar.standardAppearance = appearance;
            navigationBar.scrollEdgeAppearance = navigationBar.standardAppearance
        }
    }

    override func viewWillAppear(_ animated: Bool) {
        self.navigationBar.tintColor = UIColor.black
        self.navigationItem.hidesBackButton = true
        self.navigationBar.backItem?.hidesBackButton = true
        self.navigationItem.backBarButtonItem?.isEnabled = false
    }
    
    @objc func dissmissView(_ sender:UIButton){
        self.dismiss(animated: true, completion: nil)
    }

    @objc func showNotification(_ sender:UIButton){
        let storyBoard = UIStoryboard(name: "Main", bundle: nil)
        let viewControl = storyBoard.instantiateViewController(withIdentifier: "ced_vendorNotification") as! ced_vendorNotification
        let navigation = self.sideDrawerViewController?.mainViewController as! ced_navigationBarController
        if let viewcontr = navigation.viewControllers.last{
            if !viewcontr.isKind(of: ced_vendorNotification.self){
                 navigation.pushViewController(viewControl, animated: true)
            }
            
        }
    
       
    }
    
    @objc func showProfile(_ sender:UIButton){
        let storyBoard = UIStoryboard(name: "Main", bundle: nil)
        let viewControl = storyBoard.instantiateViewController(withIdentifier: "profileStatusViewController") as! ProfileStatusViewController
         let navigation = self.sideDrawerViewController?.mainViewController as! ced_navigationBarController
        if let viewcontr = navigation.viewControllers.last{
            if !viewcontr.isKind(of: ProfileStatusViewController.self){
                navigation.pushViewController(viewControl, animated: true)
            }
            
        }
       
    }
    
    @objc func addNavButton(_ view:UIViewController,str:String){
        
        
        //Make toggle buuton
        let toglebut = UIButton()
        toglebut.frame.size = CGSize(width: 20, height: 20)
        toglebut.setImage(UIImage(named: "hamp")?.withRenderingMode(.alwaysTemplate), for: UIControl.State())
        toglebut.imageView?.contentMode = .scaleAspectFit;
        toglebut.tintColor = UIColor.black;
        toglebut.addTarget(self, action: #selector(toggleDrawer), for: UIControl.Event.touchUpInside)
        let togglebutton = UIBarButtonItem(customView: toglebut)
        let backButton = UIButton(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        backButton.addTarget(self, action: #selector(ced_navigationBarController.back(_:)), for: .touchUpInside)
        
        
        if ced_storeVC.selectLangauge == "en"{
            backButton.setImage(UIImage(named: "backbutton")?.withRenderingMode(.alwaysTemplate), for: UIControl.State())
        }else{
            backButton.setImage(UIImage(named: "backFlip")?.withRenderingMode(.alwaysTemplate), for: UIControl.State())
        }
       
        
        let backBarButton = UIBarButtonItem(customView: backButton)
        if let me = view as? ced_vendordashboard  {
            me.navigationItem.leftBarButtonItem = togglebutton
            
        } else if view.navigationController?.viewControllers.count >  1 {
            view.navigationItem.leftBarButtonItems = [backBarButton,togglebutton]
        }else{
            view.navigationItem.leftBarButtonItems = [togglebutton]
        }
        
        
        //profile button
        let profil = UIButton()
        profil.frame.size = CGSize(width: 30, height: 35)
        profil.setImage(UIImage(named: "profilemenuicon")?.withRenderingMode(.alwaysTemplate), for: UIControl.State())
        profil.contentMode = .scaleAspectFit
        profil.addTarget(self, action: #selector(ced_navigationBarController.showProfile(_:)), for: UIControl.Event.touchUpInside)
        let prof = UILabel()
        prof.frame = CGRect(x: 10, y: -1, width: 20, height: 18)
        prof.backgroundColor = Ced_CommonVendor.UIColorFromRGB("#2e8b57")
        prof.textColor = UIColor.white
        prof.tag = 32456
        prof.clipsToBounds = true
        prof.text = "1"
        prof.textAlignment = .center
        prof.layer.cornerRadius = 5
        prof.font = .systemFont(ofSize: 10)
        profil.addSubview(prof)
        
        
        let profile = UIBarButtonItem(customView: profil)
        
        
        
        //Notification button
        let notific = UIButton()
        notific.frame.size = CGSize(width: 30,height: 35)
        notific.setImage(UIImage(named: "ic_action_name")?.withRenderingMode(.alwaysTemplate), for: UIControl.State())
        notific.imageView?.contentMode = .scaleAspectFit;
        notific.addTarget(self, action: #selector(ced_navigationBarController.showNotification(_:)), for: UIControl.Event.touchUpInside)
        let profiln = UILabel()
        profiln.frame = CGRect(x: 10 , y: -1, width: 20, height: 18)
        profiln.backgroundColor = UIColor.init(hexString: "#e14040")
        profiln.textColor = UIColor.white
        
        if let userData = defaults.object(forKey: "userInfoDict") as? Dictionary<String,Any>
        {
            print(userData)
            profiln.text = userData["valerts"] as? String
        }
        profiln.clipsToBounds = true
        profiln.font = .systemFont(ofSize: 10)
        profiln.textAlignment = .center
        profiln.layer.cornerRadius = 5
        notific.addSubview(profiln)
        let notif = UIBarButtonItem(customView: notific)
        
        //logoutbutton
        let log = UIButton()
        log.frame.size = CGSize(width: 30, height: 30)
        log.setBackgroundImage(UIImage(named: "logout-64")?.withRenderingMode(.alwaysTemplate), for: UIControl.State())
        log.addTarget(self, action: #selector(ced_navigationBarController.doLogout(_:)), for: UIControl.Event.touchUpInside)
        let logout = UIBarButtonItem(customView: log)
        NotificationCenter.default.addObserver(self, selector: #selector(updateNotification(_:)),name:NSNotification.Name(rawValue: "refreshNotification"), object: nil)
        
        
        //        view.navigationController?.navigationBar.backIndicatorImage = UIImage(named: "backbutton")
        //        view.navigationController?.navigationBar.backIndicatorTransitionMaskImage = UIImage(named: "backbutton")
        
        view.navigationItem.backBarButtonItem = UIBarButtonItem(title:"", style:.plain, target:nil, action:nil)
        
        view.navigationItem.rightBarButtonItems = [logout,notif] //profile
        
        let titleView = UIView()
        titleView.frame = CGRect(x: 0, y: 0, width: 180, height: 40)
        let navigationTitle = UIImageView(frame: CGRect(x: 0, y: 0, width: 180, height: 40))
        navigationTitle.center = titleView.center
        navigationTitle.image = UIImage(named: "header")//?.withRenderingMode(.alwaysTemplate)
        //        navigationTitle.tintColor = Settings.barItemColor
        navigationTitle.contentMode = .scaleAspectFit
        titleView.addSubview(navigationTitle)
        view.navigationItem.titleView = titleView
    }
    
    @objc func updateNotification(_ notification: NSNotification)
    {
        if let alerttag = self.view.viewWithTag(32456) as? UILabel
        {
            if let userData = defaults.object(forKey: "userInfoDict") as? Dictionary<String,Any>
            {
                print(userData)
                alerttag.text = userData["valerts"] as? String
            }
        }
    }
    
    @objc func back(_ sender:UIButton){
        let mainview = self.sideDrawerViewController?.mainViewController  as? ced_navigationBarController
        _ =  mainview?.popViewController(animated: true)
      
    }
    
    @objc func doLogout(_ sender:UIButton){
        let confirmationAlert = UIAlertController(title: "Confirmation".localized, message: "Do you want to logout".localized, preferredStyle: UIAlertController.Style.alert)
        confirmationAlert.addAction(UIAlertAction(title: "Yes".localized, style: .default, handler: { (action: UIAlertAction!) in
            (UIApplication.shared.delegate as! AppDelegate).removeFCMTokentoServer()
            self.defaults.set(false, forKey: "isLogin")
            self.defaults.removeObject(forKey: "userInfoDict")
            self.defaults.removeObject(forKey: "cedNavTransact")
            self.defaults.removeObject(forKey: "cedNavReport")
            self.defaults.removeObject(forKey: "cedNavOrder")
            self.defaults.removeObject(forKey: "cedNavMemberShipPlans")
            self.defaults.removeObject(forKey: "cedNaveitems")
            let storyboard = UIStoryboard(name: "ced_vendorLogin", bundle: nil)
            /*let viewControl = storyboard.instantiateViewController(withIdentifier: "rootbeforelogin") as? UINavigationController
            self.present(viewControl!, animated: true, completion: nil)*/
            (UIApplication.shared.delegate as! AppDelegate).changeModules()
            
        }));
        
        confirmationAlert.addAction(UIAlertAction(title: "No".localized, style: .default, handler: { (action: UIAlertAction!) in
            //print("Handle Cancel Logic here")
        }));
        present(confirmationAlert, animated: true, completion: nil)
        
    }
    
    
    @objc func toggleDrawer() {
        if let sideDrawerViewController = self.sideDrawerViewController {
            sideDrawerViewController.toggleDrawer()
        }
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
