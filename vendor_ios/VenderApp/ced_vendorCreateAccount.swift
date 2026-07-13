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

class ced_vendorCreateAccount: UIViewController {
    
    //Vendor Personal View start
    @IBOutlet weak var mainScrollView: UIScrollView!
    @IBOutlet weak var vendorPersonalView: UIView!
    @IBOutlet weak var vendorFirstName: UITextField!
    @IBOutlet weak var vendorEmailAddress: UITextField!
    @IBOutlet weak var SignUpNewslater: DLRadioButton!
    @IBOutlet weak var vendorLastName: UITextField!
    //end:
    
    //vendor information view
    @IBOutlet weak var vendorInformation: UIView!
    @IBOutlet weak var vendorPublicName: UITextField!
    @IBOutlet weak var vendorShopUrl: UITextField!
    //end:
    
    //vendor Login Info
    @IBOutlet weak var vendorLoginInfo: UIView!
    @IBOutlet weak var vendorPassword: UITextField!
    @IBOutlet weak var vendorConfirmPassword: UITextField!
    //end:
    var newslettorsign = false
    var postString :String?
    let defaults   = UserDefaults.standard
    @IBOutlet weak var vendorSubmitButton: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        vendorPersonalView.makeCard(vendorPersonalView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        vendorPersonalView.makeCard(vendorInformation, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        vendorPersonalView.makeCard(vendorLoginInfo, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        vendorSubmitButton.addTarget(self, action: #selector(ced_vendorCreateAccount.createVendorAccount(_:)), for: UIControl.Event.touchUpInside)
        if let value = UserDefaults.standard.value(forKey: "currentCedAppLanguage") as? String
        {
            if value=="ar" || value=="ur"
            {
                SignUpNewslater.contentHorizontalAlignment = .right
            }
            else
            {
                SignUpNewslater.contentHorizontalAlignment = .left
            }
        }
        SignUpNewslater.isMultipleSelectionEnabled = true
        SignUpNewslater.addTarget(self, action: #selector(ced_vendorCreateAccount.vendorSignupNews(_:)), for: UIControl.Event.touchUpInside)
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        vendorSubmitButton.backgroundColor = color
        //vendorFirstName.becomeFirstResponder();
        
    }
    
    override func viewWillAppear(_ animated: Bool)
    {
        
        self.navigationItem.backBarButtonItem?.title = ""
        
        //mainScrollView.scrollToTop()
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func vendorSignupNews(_ sender:UIButton){
        if(!newslettorsign){
            newslettorsign = true
        }else{
            newslettorsign = false
        }
    }
    
    @objc func createVendorAccount(_ sender:UIButton){
        
        let vendrFirstName = vendorFirstName.text!.trim()
        let vendrlastName  = vendorLastName.text!.trim()
        let email = vendorEmailAddress.text!.trim()
        
        let vendorPublicNam = vendorPublicName.text!.trim()
        let vendorShopUl   = vendorShopUrl.text!.trim()
        let password = vendorPassword.text!
        let confirmpassword = vendorConfirmPassword.text!
        if(vendrFirstName == "")
        {
            self.view.showToastMsg(NSLocalizedString("Please enter first name", comment: ""))
            return
        }
        if(vendrlastName == "")
        {
            self.view.showToastMsg(NSLocalizedString("Please enter last name", comment: ""))
            return
        }
        let characterset = CharacterSet(charactersIn: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
        if vendrFirstName.rangeOfCharacter(from: characterset.inverted) != nil {
            self.view.showToastMsg(NSLocalizedString("Please enter only valid characters[a-z] or [A-Z] in first name", comment: ""))
            return
        }
        if vendrlastName.rangeOfCharacter(from: characterset.inverted) != nil {
            self.view.showToastMsg(NSLocalizedString("Please enter only valid characters[a-z] or [A-Z] in last name", comment: ""))
            return
        }
        let shopUrlCharacterset = CharacterSet(charactersIn: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-")
        if vendorShopUl.rangeOfCharacter(from: shopUrlCharacterset.inverted) != nil {
            self.view.showToastMsg(NSLocalizedString("Please enter only valid characters [a-z][A-Z]- in shop url", comment: ""))
            return
        }
        let publicNameCharacterset = CharacterSet(charactersIn: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
        if vendorPublicNam.rangeOfCharacter(from: publicNameCharacterset.inverted) != nil {
            self.view.showToastMsg(NSLocalizedString("Please enter only valid characters [a-z][A-Z] in public name", comment: ""))
            return
        }
        if(email ==  ""){
            //Alert_File.removeLoadingIndicator(self)
            self.view.showToastMsg(NSLocalizedString("Email is Required.", comment: ""))
            return
        }
        if(vendorPublicNam ==  ""){
            //Alert_File.removeLoadingIndicator(self)
            self.view.showToastMsg(NSLocalizedString("Public name is Required.", comment: ""))
            return
        }
        if(vendorShopUl ==  ""){
            //Alert_File.removeLoadingIndicator(self)
            self.view.showToastMsg(NSLocalizedString("Shop url is Required.", comment: ""))
            return
        }
        /*if(!Ced_CommonVendor.isValidEmail(testStr: email))
        {
            //Alert_File.removeLoadingIndicator(self)
            self.view.showToastMsg(NSLocalizedString("Email is not valid", comment: ""))
            return
        }*/
        if(password.characters.count < 8){
            //Alert_File.removeLoadingIndicator(self)
            if(password.characters.count == 0){
                self.view.showToastMsg(NSLocalizedString("Password is Required.", comment: ""))
                return
            }
            self.view.showToastMsg(NSLocalizedString("Password should be greater than 8 characters.", comment: ""))
            return
        }
        if(confirmpassword.lowercased() == "")
        {
            self.view.showToastMsg(NSLocalizedString("Please enter confirm password", comment: ""))
            return
        }
        if(password.lowercased() != confirmpassword.lowercased()){
            //Alert_File.removeLoadingIndicator(self)
            self.view.showToastMsg(NSLocalizedString("Password did not match.", comment: ""))
            return
        }
        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Loading Please Wait...", comment: ""))
        let postdata = ["lastname":vendrlastName,"is_subscribed":newslettorsign,"firstname":vendrFirstName,"password":password,"email":email,"vendor":[["value":vendorPublicNam,"key":"public_name"],["value":vendorShopUl,"key":"shop_url"]]] as [String : Any]
        
        do{
            let theJSONData = try JSONSerialization.data(
                withJSONObject: postdata ,
                options: JSONSerialization.WritingOptions(rawValue: 0))
            postString = NSString(data: theJSONData,
                                  encoding: String.Encoding.utf8.rawValue)! as String
            
        }
        catch{
            print("error in data encoding")
        }
        
        
        var baseUrl = settings.baseUrl
        baseUrl += "vendorapi/index/create"
        let dataPosted = "createaccount=" + postString!
        print(dataPosted)
        
        var request =  URLRequest(url: URL(string:baseUrl)!)
        request.httpMethod = "POST"
        request.httpBody = dataPosted.data(using: String.Encoding.utf8)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task =  session.dataTask(with: request, completionHandler: {
            data,response,error in
            DispatchQueue.main.async
                {
                    Alert_File.removeLoadingIndicator(self)
            }
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self)
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: NSLocalizedString("Error", comment: ""), showMsg: error!.localizedDescription)
                }
                print("error=\(error)")
                return;
            }
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                DispatchQueue.main.async
                    {
                        if(httpStatus.statusCode == -1009){
                            ced_showError.showNoNetWork(self)
                        }else{
                            ced_showError.showNoModule(self)
                        }
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        print("response = \(response)")
                }
                return;
            }
            guard let json = try? JSON(data: data!) else {return}
            DispatchQueue.main.async
                {
                    
                    print(NSString(data: data!, encoding: String.Encoding.utf8.rawValue))
                    
                    print(json)
                    if(json["data"]["customer"][0]["success"].stringValue.lowercased() == "true"){
                        if(json["data"]["customer"][0]["isConfirmationRequired"].stringValue.lowercased() == "NO" ) {
                            self.view.showToastMsg(json["data"]["customer"][0]["message"].stringValue)
                            let customer_id = json["data"]["customer"][0]["customer_id"].stringValue;
                            let hashKey = json["data"]["customer"][0]["hashkey"].stringValue;
                            let vendor_id = json["data"]["customer"][0]["vendor_id"].stringValue;
                            let vendor_name = json["data"]["customer"][0]["vendor_name"].stringValue;
                            let profile_picture = json["data"]["customer"][0]["profile_picture"].stringValue;
                            //saving value in NSUserDefault to use later on :: start
                            let dict = ["customerId": customer_id, "hashKey": hashKey,"vendorName":vendor_name,"vendorId":vendor_id,"profilePicUrl":profile_picture];
                            print(json["data"]["customer"][0]["isConfirmationRequired"].stringValue)
                            if(json["data"]["customer"][0]["isConfirmationRequired"].stringValue.lowercased() == "YES".lowercased()){
                                
                                
                            }else{
                                self.defaults.set(true, forKey: "isLogin")
                                self.defaults.set(dict, forKey: "userInfoDict");
                            }
                            
                        }
                        else{
                            self.view.showToastMsg(json["data"]["customer"][0]["message"].stringValue + " Your account is under admin review")
                            Ced_CommonVendor.delay(4, closure: {
                                self.navigationController?.popToRootViewController(animated: true)
                            })
                        }
                    }else{
                        self.view.showToastMsg(json["data"]["customer"][0]["message"].stringValue)
                    }
            }
        })
        task.resume()
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
/*extension String
{
    func trim() -> String
    {
        return self.trimmingCharacters(in: CharacterSet.whitespaces)
    }
}*/
extension UIScrollView {
    func scrollToTop() {
        let desiredOffset = CGPoint(x: 0, y: -contentInset.top)
        setContentOffset(desiredOffset, animated: true)
    }
}
