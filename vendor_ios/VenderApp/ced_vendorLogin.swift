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
import AuthenticationServices
class ced_vendorLogin: UIViewController, UITextFieldDelegate {
    
    //Declare Connection from storyboard
    
    @IBOutlet weak var regButton: UIButton!
    //    @IBOutlet weak var facebookLogin: FBSDKLoginButton!
    
    @IBOutlet weak var socialLoginStack: UIStackView!
//    @IBOutlet weak var facebookLogin: UIButton!
//    @IBOutlet weak var googleButton: UIButton!
    //    @IBOutlet weak var googleLogin: GIDSignInButton!
    @IBOutlet weak var vendoremail: UITextField!
    @IBOutlet weak var vendorPassword: UITextField!
    @IBOutlet weak var showHideBtn: UIButton!
    @IBOutlet weak var submitButton: UIButton!
    @IBOutlet weak var loginWithOTPbtn: SSRadioButton!
    @IBOutlet weak var loginWithPasswordbtn: SSRadioButton!
    @IBOutlet private weak var verifyphoneNumberView: JNPhoneNumberView!
    @IBOutlet weak var passwordHeight: NSLayoutConstraint!
    @IBOutlet weak var passwordTop: NSLayoutConstraint!
    
    
    
    let defaults = UserDefaults.standard
    var otpText = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        Util.deactivateRTL(of: self.verifyphoneNumberView)
        self.verifyphoneNumberView.delegate = self
       // self.verifyphoneNumberView.setViewConfiguration(self.getConfigration())
        self.verifyphoneNumberView.setPhoneNumber("")
        verifyphoneNumberView.setDefaultCountryCode("eg")
        verifyphoneNumberView.layer.cornerRadius = 1
        verifyphoneNumberView.layer.borderColor = UIColor.lightGray.cgColor
        verifyphoneNumberView.layer.borderWidth = 1
        vendoremail.delegate = self;
        vendorPassword.delegate = self;
        showHideBtn.setImage(UIImage(named: "eye_hide"), for: .normal)
        vendoremail.addTarget(self, action: #selector(textfieldDidChange(_:)), for: .editingChanged)
        vendorPassword.addTarget(self, action: #selector(textfieldDidChange(_:)), for: .editingChanged)
       // vendoremail.text = "sajidnawaz992@gmail.com"
      //  vendorPassword.text = "12345678"
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        /*if(Locale.current.languageCode == "ar")
         {
         let buttonText = NSAttributedString(string: NSLocalizedString("Log in with Facebook", comment: ""))
         facebookLogin.setAttributedTitle(buttonText, for: .normal)
         }*/
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        submitButton.backgroundColor = color
        //regButton.backgroundColor = color;
        //forgotPasswordButton.setTitleColor(color, for: .normal);
        self.navigationController?.navigationBar.barTintColor = color
        self.navigationController?.navigationBar.tintColor = UIColor.white
        submitButton.addTarget(self, action: #selector(ced_vendorLogin.vendorLogin(_:)), for: UIControl.Event.touchUpInside)
//                facebookLogin.delegate = self
//                facebookLogin.permissions = ["public_profile", "email"]
        
//        let googlesignin = GIDSignIn.sharedInstance()
//        googlesignin?.delegate = self
//
//        googlesignin?.clientID = "294520065742-lq1l0ptccc54le7oakqhde3qg6294pll.apps.googleusercontent.com"
//        googlesignin?.scopes.append("https://www.googleapis.com/auth/plus.login")
//        googlesignin?.scopes.append("https://www.googleapis.com/auth/plus.me")
//        googlesignin?.uiDelegate = self
//        googlesignin?.shouldFetchBasicProfile = true
        if let regEnabled = defaults.value(forKey: "cedReg") as? String{
            if(regEnabled != "1"){
                regButton.isHidden = true
            }else{
                regButton.isHidden = false
            }
        }
        else
        {
            regButton.isHidden = false
        }
        print("----social----")
        socialLoginStack.isHidden = false;
        print(Ced_CommonVendor().checkModule("Ced_VSocialLoginApi"))
        //        if(Ced_CommonVendor().checkModule("Ced_VSocialLoginApi")){
        ////            SocialLoginView.isHidden = false;
        //        }
        //        else
        //        {
        ////            SocialLoginView.isHidden = true;
        //        }
        if #available(iOS 13.0, *) {
            let appearance = UINavigationBarAppearance()
            appearance.configureWithOpaqueBackground()
            appearance.backgroundColor = Ced_CommonVendor.UIColorFromRGB(colorString ?? "")
            let textAttributes = [NSAttributedString.Key.foregroundColor:UIColor.white]
            appearance.titleTextAttributes = textAttributes
            self.navigationController?.navigationBar.standardAppearance = appearance;
            self.navigationController?.navigationBar.scrollEdgeAppearance = self.navigationController?.navigationBar.standardAppearance
        }
        submitButton.setTitle("GENERATE OTP".localized, for: .normal)
        
//        let dict = ["customerId": "", "hashKey": "OHyLbQRZrTGrkxZ6SQVxBKb1xUpzM5UV","vendorName":"vendor_name","vendorId":"207","profilePicUrl":"profile_picture","valerts":"valerts","profile_complete":"profile_complete"];
//        UserDefaults.standard.set(dict, forKey: "userInfoDict");
//        let storyBoard = UIStoryboard(name: "ProductAddon", bundle: nil)
//        let viewControl = storyBoard.instantiateViewController(withIdentifier: "addProductAddonFirstPage") as! AddProductAddonFirstPage
//        navigationController!.pushViewController(viewControl, animated: true)
    }
    
    func randomNumber(digits:Int) -> String {
        var number = String()
        for _ in 1...digits {
           number += "\(Int.random(in: 1...9))"
        }
        return number
    }
    
    @objc func textfieldDidChange(_ textfield: UITextField)
    {
//        if(textfield == vendoremail)
//        {
//            if(textfield.text!.characters.count>25)
//            {
//                self.view.showToastMsg(NSLocalizedString("Email cannot be greater than 25 characters".localized, comment: ""))
//                textfield.text = String(textfield.text!.prefix(25))
//            }
//        }
//        else if(textfield == vendorPassword)
//        {
//            if(textfield.text!.characters.count>25)
//            {
//                self.view.showToastMsg(NSLocalizedString("Password cannot be greater than 25 characters".localized, comment: ""))
//                textfield.text = String(textfield.text!.prefix(25))
//            }
//        }
//
        
    }
    
    override func viewWillAppear(_ animated: Bool)
    {
       // vendoremail.text = "";
      //  vendorPassword.text = "";
        self.navigationController?.navigationBar.isHidden = true
        //vendoremail.becomeFirstResponder();
    }
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        self.navigationController?.navigationBar.isHidden = false
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func loginWithOTPbtntpd(_ sender: Any) {
        passwordHeight.constant = 0
        passwordTop.constant = 0
        showHideBtn.isHidden = true
        loginWithPasswordbtn.isSelected = false
        loginWithOTPbtn.isSelected = true
        verifyphoneNumberView.isHidden = false
        vendoremail.isHidden = true
        submitButton.setTitle("GENERATE OTP".localized, for: .normal)
    }
    
    @IBAction func loginWithPasswordbtntpd(_ sender: Any) {
        passwordHeight.constant = 38
        passwordTop.constant = 32
        showHideBtn.isHidden = false
        loginWithPasswordbtn.isSelected = true
        loginWithOTPbtn.isSelected = false
        verifyphoneNumberView.isHidden = true
        vendoremail.isHidden = false
        submitButton.setTitle("logintext".localized, for: .normal)
    }
    
    @IBAction func showHideClicked(sender: UIButton)
    {
        if sender.currentImage == UIImage(named: "eye_hide")
        {
            vendorPassword.isSecureTextEntry = false;
            sender.setImage(UIImage(named: "eye"), for: .normal)
        }
        else
        {
            sender.setImage(UIImage(named: "eye_hide"), for: .normal)
            vendorPassword.isSecureTextEntry = true;
        }
    }
    
    //do login event
    @objc func vendorLogin(_ sendor:UIButton){
        
        let email = vendoremail.text?.trim() ?? ""
        if(email ==  ""){
            if(self.loginWithPasswordbtn.isSelected){
                self.view.showToastMsg(NSLocalizedString("Email is Required.", comment: ""))
                return
            }
        }
        let password = vendorPassword.text?.trim()
        if(password == "" && self.loginWithPasswordbtn.isSelected)
        {
            self.view.showToastMsg(NSLocalizedString("Password is Required.", comment: ""))
            return
        }
        if(self.loginWithPasswordbtn.isSelected){
            let postString = "email=" + email.replacingOccurrences(of: "&", with: "%26") + "&password=" + password!.replacingOccurrences(of: "&", with: "%26")
            self.loginApiCall(postString)
        }else{
            let phonNumber = self.verifyphoneNumberView.getNationalPhoneNumber()
            if (phonNumber.isEmpty){
               self.view.makeToast("Please enter the number".localized, duration: 2.0, position: .center)
               return
           }else if (phonNumber.first == "0"){
                self.view.makeToast("Please enter the number without 0".localized, duration: 2.0, position: .center)
                return
            }else if (phonNumber.count != 10){
                self.view.makeToast("Please enter the valid number".localized, duration: 2.0, position: .center)
                return
            }
            var phoneNumber = self.verifyphoneNumberView.getPhoneNumber()
            if(phoneNumber.contains("+")){
                phoneNumber = phoneNumber.replacingOccurrences(of: "+", with: "")
            }
            self.sendOTP(toNumber: phoneNumber,isResend: false)
//            self.verify(number: phoneNumber) { [weak self] success in
//                if success {
//                    
//                }
//            }
        }
    }
    
    private func sendOTP(toNumber number: String,isResend:Bool ) {
        otpText = randomNumber(digits: 4)
        let parameters :[String:Any] = ["type": "text", "text": otpText]
        let bodycomponents :[String:Any] = ["type": "body", "parameters": [parameters]]
        let buttoncomponents :[String:Any] = ["type": "button","sub_type": "url","index": "0", "parameters": [parameters]]
        let language :[String:Any] = ["policy": "deterministic", "code": "ar"]
        let template :[String:Any] = ["language": language,"name": "otp_new", "components": [bodycomponents,buttoncomponents]]
        let mainBody :[String:Any] = ["messaging_product": "whatsapp","recipient_type": "individual","to": number,"type": "template", "template": template]
       
        ApiHandler.handle.requestDict(with: "https://graph.facebook.com/v20.0/122095038332010689/messages", params: mainBody, requestType: .POST, controller: self) { [weak self] data, _ in
            DispatchQueue.main.async
                {
                    if(self != nil){
                        Alert_File.removeLoadingIndicator(self!)
                    }
            }
            guard let data = data else { return }
            guard let json = try? JSON(data: data) else { return }
            print(json)
            let message = json["error"]["message"].stringValue
            if(!message.isEmpty){
                
                DispatchQueue.main.async {
                    self?.view.makeToast(message)
                }
            }
            if(!isResend){
                DispatchQueue.main.async {
                    self?.view.makeToast(json["error"]["message"].stringValue)
                    // if OTP is successfully sent show the OTP popup to verify OTP
                    let popup        = AuthenticationView(otp: self?.otpText ?? "1234")
                    guard let window = UIApplication.shared.keyWindow else { return }
                    window.addSubview(popup)
                    popup.frame = window.bounds
                    
                    // Handle the success state of OTP validation
                    popup.onSuccess = { otp in
                        self?.loginApiCall(number)
                    }
                    
                    // Handle the failure state of OTP validation
                    popup.onFailure = { message in
                        self?.view.makeToast(message)
                        return
                    }
                    
                    // Handle the resend OTP Action
                    popup.onResend = {
                        self?.sendOTP(toNumber: number,isResend: true)
                        popup.otpCode = self?.otpText ?? "1234"
                    }
                }
            }
        }
    }
    
    private func verify(number: String, completion:@escaping (Bool) -> Void) {
        let countryID: String = Locale.current.regionCode ?? ""
        ApiHandler.handle.request(with: "vsignupapi/validateNumber", params: ["mobile": number, "country_id": "IN"], requestType: .POST, controller: self) { [weak self] data, error in
            guard let data = data else { return }
            guard let json = try? JSON(data: data) else { return }
            print(json)
            DispatchQueue.main.async {
                self?.view.makeToast(json[0]["message"].stringValue)
            }
            if json[0]["status"].stringValue == "true"{
                completion(true)
            }else{
                completion(false)
            }
            
        }
    }
    
    func loginApiCall(_ postString:String){
        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Loading Please Wait...", comment: ""))
        var baseUrl = settings.baseUrl
        if(self.loginWithPasswordbtn.isSelected){
            baseUrl += "vendorapi/index/login"
        }else{
            let numberWithoutSpace = postString.replacingOccurrences(of: " ", with: "")
            baseUrl += "wapplogin/index/login?mobile=\(numberWithoutSpace)"
        }
        print(baseUrl)
        print(postString)
        var request =  URLRequest(url: URL(string:baseUrl)!)
        if(self.loginWithPasswordbtn.isSelected){
            request.httpMethod = "POST"
            request.httpBody = postString.data(using: String.Encoding.utf8)
        }
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        sessionConfig.urlCache = URLCache(memoryCapacity: 0, diskCapacity: 0, diskPath: nil)
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request, completionHandler: {
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
                        
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        print("response = \(response)")
                }
                return;
            }
           guard let json = try? JSON(data: data!) else {return}
            
            print(NSString(data: data!, encoding: String.Encoding.utf8.rawValue))
            
            DispatchQueue.main.async
                {
                    Alert_File.removeLoadingIndicator(self)
                    
                    print(json)
                    if(json["data"]["customer"][0]["success"].stringValue.lowercased() == "true"){
                     //   self.parseData(json, "directlogin")
                        let multistep_done = json["data"]["customer"][0]["multistep_done"].stringValue;
                        let customer_id = json["data"]["customer"][0]["customer_id"].stringValue;
                        let hashKey = json["data"]["customer"][0]["hashkey"].stringValue;
                        let vendor_id = json["data"]["customer"][0]["vendor_id"].stringValue;
                        let vendor_name = json["data"]["customer"][0]["vendor_name"].stringValue;
                        let profile_picture = json["data"]["customer"][0]["profile_picture"].stringValue;
                        let valerts = json["data"]["customer"][0]["valerts"].stringValue
                        
                        let profile_complete = json["data"]["customer"][0]["profile_complete"].stringValue
                        //saving value in NSUserDefault to use later on :: start
                        
                        if(multistep_done == "0"){
                            let storyboard = UIStoryboard(name: "ced_vendorLogin", bundle: nil)
                            let viewControl = storyboard.instantiateViewController(withIdentifier: "ced_vendorRegistration") as? ced_vendorRegistration
                            viewControl?.vendor_id = vendor_id
                            viewControl?.hashKey = hashKey
                            viewControl?.loginJson = json
                            viewControl?.step = "2"
                            self.navigationController?.pushViewController(viewControl!, animated: true)
                        }else{
                            let dict = ["customerId": customer_id, "hashKey": hashKey,"vendorName":vendor_name,"vendorId":vendor_id,"profilePicUrl":profile_picture,"valerts":valerts,"profile_complete":profile_complete]
                            self.defaults.set(true, forKey: "isLogin")
                            self.defaults.set(dict, forKey: "userInfoDict");
                            //print(self.defaults.dictionary(forKey: "userInfoDict"))
                            self.view.showToastMsg(NSLocalizedString("Login Successful", comment: ""))
                            (UIApplication.shared.delegate as! AppDelegate).changeModules()
                        }
                    }else{
//                        if(json["data"]["customer"][0]["message"].stringValue.lowercased() == "create account".lowercased()){
//                            
//                            let storyboard = UIStoryboard(name: "ced_vendorLogin", bundle: nil)
//                            let viewControl = storyboard.instantiateViewController(withIdentifier: "vendorCreateAcc") as? ced_vendorCreateAccountAl
//                            viewControl?.customerId = json["data"]["customer"][0]["is_vendor"].stringValue
//                            self.navigationController?.pushViewController(viewControl!, animated: true)
//                            
//                        }else{
//                            
//                            self.view.showToastMsg(json["data"]["customer"][0]["message"].stringValue)
//                            
//                        }
                        var message = json["data"]["customer"][0]["message"].stringValue
                        let email = "v-relations@locafy.market"
                        let newEmail = "v\u{2060}-\u{2060}relations@\u{2060}locafy.\u{2060}market"
                        message = message.replacingOccurrences(of: email, with: newEmail)
                        Alert_File.showOkAlert(self, "Message".localized, message) { index in
                            self.modalPresentationStyle = .fullScreen
                            self.navigationController?.popToRootViewController(animated: true)
                        }
                    }
            }
            
        })
        task.resume()
    }
    
    
    func parseData(_ json:JSON, _ loginType: String){
        var options = [String]()
        var OrderData = [String]()
        var Transactionsett = [String]()
        var ReportSett = [String]()
        var Settingsdat = [String]()
        var attrData   = [String]()
        var arrData = [JSON]()
        if loginType == "sociallogin"{
            arrData = json["vendor_data"]["vendor_info"]["vendor_link"].arrayValue
        }else{
            arrData = json["data"]["customer"][0]["vendor_link"].arrayValue
        }
        
        // for data in json["data"]["customer"][0]["vendor_link"].arrayValue {
        for data in arrData
        {
            if(data.stringValue == "New Product"){
               // options.append(data.stringValue)
            }else if(data.stringValue == "Vendor Profile"){
                options.append("Dashboard Menu".localized)
                options.append(data.stringValue)
            }else if(data.stringValue == "Vendor Social Post"){
              //  options.append(data.stringValue)
            }else if(data.stringValue == "Manage Products"){
              //  options.append(data.stringValue)
            }else if(data.stringValue == "Manage Attribute"){
                attrData.append(data.stringValue)
            }else if(data.stringValue == "Manage Attribute Set"){
                attrData.append(data.stringValue)
            }
            else if(data.stringValue == "Manage Orders"){
                OrderData.append(data.stringValue.localized)
                OrderData.append("Cancel Orders".localized)
            }else if(data.stringValue == "Manage Invoice"){
                OrderData.append(data.stringValue)
            }else  if(data.stringValue == "Manage Shipment"){
                OrderData.append(data.stringValue)
            }else  if(data.stringValue == "Manage Credit Memo"){
                OrderData.append(data.stringValue)
            }else    if(data.stringValue == "Request Payments"){
                Transactionsett.append(data.stringValue)
            }else if(data.stringValue == "View Transactions"){
                Transactionsett.append(data.stringValue)
            }else  if(data.stringValue == "Order Reports"){
                ReportSett.append(data.stringValue)
            }else
                if(data.stringValue == "Product Reports"){
                ReportSett.append(data.stringValue)
            }else  if(data.stringValue == "Transaction Settings"){
                Settingsdat.append(data.stringValue)
            }else    if(data.stringValue == "Shipping Settings"){
                Settingsdat.append(data.stringValue)
            }else  if(data.stringValue == "Shipping Methods"){
                Settingsdat.append(data.stringValue)
            }
            
            
        }
        OrderData.append("Manage RMA Request".localized)
        defaults.set(attrData, forKey: "cedAttrData")
        defaults.set(options, forKey: "cedNaveitems")
        defaults.set(Settingsdat, forKey: "cedNavSett")
        defaults.set(OrderData, forKey: "cedNavOrder")
        defaults.set(ReportSett, forKey: "cedNavReport")
        defaults.set(Transactionsett, forKey: "cedNavTransact")
        
        return
    }
    
    func doLOGIn(_ data:[String:String]){
        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Loading Please Wait..", comment: ""))
        print(data)
        var params = Dictionary<String,String>()
        var baseUrl = settings.baseUrl
        baseUrl += "rest/V1/"
        // baseUrl += "vsocialloginapi/customer/register"
        baseUrl += "vsocialloginapi/create"
        var request = URLRequest(url: URL(string: baseUrl)!);
        let url = "vsocialloginapi/create";
        request.httpMethod = "POST";
        //        var postString = "firstname="+data["firstname"]!+"&lastname="+data["lastname"]!;
        //        postString += "&email="+data["email"]!;
        //        postString += "&type="+data["type"]!;
        //        postString += "&token="+data["token"]!
        params["firstname"] = data["firstname"]!
        params["lastname"] = data["lastname"]!
        params["email"]=data["email"]!;
        params["type"]=data["type"]!
        params["token"]=data["token"]!
        var postString=Dictionary<String,Dictionary<String,String>>()
        var postString1=""
        print(baseUrl)
        var makeRequest = URLRequest(url: URL(string: baseUrl)!)
        
        
        makeRequest.httpMethod = "POST"
        
        postString=["data":[:]]
        if params.count > 0 {
            for (key,value) in params
            {
                _ = postString["data"]?.updateValue(value, forKey:key)
            }
        }
        postString=["data":[:]]
        if params.count > 0 {
            for (key,value) in params
            {
                _ = postString["data"]?.updateValue(value, forKey:key)
            }
        }
        postString1=postString.convtToJson() as String
        makeRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
        makeRequest.httpBody = postString1.data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))
        print(postString1)
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: makeRequest, completionHandler: {data,response,error in
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse , httpStatus.statusCode != 200
            {
                
                DispatchQueue.main.async
                {
                    Alert_File.removeLoadingIndicator(self)
                    
                    if let data = data{
                        print(NSString(data: data, encoding: String.Encoding.utf8.rawValue) ?? "")
                    }
                    
                    print("statusCode should be 200, but is \(httpStatus.statusCode)")
                    
                    print("response = \(response)")
                    //self.renderNoDataImage(view: self, imageName: "no_module")
                    
                }
                return;
            }
            
            // code to fetch values from response :: start
            
            
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                {
                    
                    Alert_File.removeLoadingIndicator(self)
                    ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                    
                    print("error=\(error)")
                    self.recieveResponse(data: data, requestUrl: url, response: response)
                }
                return ;
            }
            
            DispatchQueue.main.async
            {
                Alert_File.removeLoadingIndicator(self)
                
                self.recieveResponse(data: data, requestUrl: url, response: response)
                
            }
        })
        
        task.resume()
    }
    
    func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        print("response")
        guard let data = data else {return}
        guard let json = try? JSON(data:data) else {return}
        print(json)
        if json["vendor_data"]["vendor_info"]["vendor_id"].stringValue != "" {
          //  self.parseData(json, "sociallogin")
            let customer_id = json["vendor_data"]["vendor_info"]["customer_id"].stringValue;
            let hashKey = json["vendor_data"]["vendor_info"]["hashkey"].stringValue;
            let vendor_id = json["vendor_data"]["vendor_info"]["vendor_id"].stringValue;
            let vendor_name = json["vendor_data"]["vendor_info"]["vendor_name"].stringValue;
            let profile_picture = json["vendor_data"]["vendor_info"]["profile_picture"].stringValue;
            let valerts = json["vendor_data"]["vendor_info"]["valerts"].stringValue
            
            let profile_complete = json["vendor_data"]["vendor_info"]["profile_complete"].stringValue
            //saving value in NSUserDefault to use later on :: start
            let dict = ["customerId": customer_id, "hashKey": hashKey,"vendorName":vendor_name,"vendorId":vendor_id,"profilePicUrl":profile_picture,"valerts":valerts,"profile_complete":profile_complete];
            
            self.defaults.set(true, forKey: "isLogin")
            self.defaults.set(dict, forKey: "userInfoDict");
            self.view.showToastMsg("Login Successful")
            (UIApplication.shared.delegate as! AppDelegate).changeModules()
            
        }
        else {
            let storyboard = UIStoryboard(name: "ced_vendorLogin", bundle: nil)
            let viewControl = storyboard.instantiateViewController(withIdentifier: "vendorCreateAcc") as? ced_vendorCreateAccountAl
            viewControl?.customerId = json["vendor_data"]["vendor_info"]["is_vendor"].stringValue
            self.navigationController?.pushViewController(viewControl!, animated: true)
        }
    }
}

extension UIColor {
    public convenience init?(hexString: String) {
        var cString:String = hexString.trimmingCharacters(in: NSCharacterSet.whitespacesAndNewlines as NSCharacterSet as CharacterSet).uppercased();
        if (cString.hasPrefix("#")) {
            cString = cString.substring(from: cString.index(cString.startIndex, offsetBy: 1))
        }
        if ((cString.characters.count) != 6) {
            return nil;
        }
        var rgbValue:UInt32 = 0
        Scanner(string: cString).scanHexInt32(&rgbValue)
        let r = CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0;
        let g = CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0;
        let b = CGFloat(rgbValue & 0x0000FF) / 255.0;
        let a =  CGFloat(1.0);
        self.init(red: r, green: g, blue: b, alpha: a)
        return
    }
}


extension ced_vendorLogin: JNPhoneNumberViewDelegate {
    
    /**
     Get presenter view controller
     - Parameter phoneNumberView: Phone number view
     - Returns: presenter view controller
     */
    func phoneNumberView(getPresenterViewControllerFor phoneNumberView: JNPhoneNumberView) -> UIViewController {
        return self
    }
    
    /**
     Get country code picker attributes
     - Parameter phoneNumberView: Phone number view
     */
    func phoneNumberView(getCountryPickerAttributesFor phoneNumberView: JNPhoneNumberView) -> JNCountryPickerConfiguration {
        let configuration = JNCountryPickerConfiguration()
        configuration.pickerLanguage = .en
        configuration.tableCellInsets = UIEdgeInsets(top: 10.0, left: 10.0, bottom: 10.0, right: 10.0)
        configuration.viewBackgroundColor = UIColor.lightGray
        configuration.tableCellBackgroundColor = UIColor.white
        
        return configuration
    }
    
    /**
     Did change text
     - Parameter nationalNumber: National phone number
     - Parameter country: Number country info
     - Parameter phoneNumberView: Phone number view
     */
    func phoneNumberView(didChangeText nationalNumber: String, country: JNCountry, forPhoneNumberView phoneNumberView: JNPhoneNumberView) {
      //  self.phoneNumberLabel.text = "International Phone Number: \n \(self.phoneNumberView.getDialCode() + nationalNumber)"
       // self.verifyphoneNumberView.setViewConfiguration(self.getConfigration())
    }
    
    /**
     Did end editing
     - Parameter nationalNumber: National phone number
     - Parameter country: Number country info
     - Parameter isValidPhoneNumber:  Is valid phone number flag as bool
     - Parameter phoneNumberView: Phone number view
     */
    func phoneNumberView(didEndEditing nationalNumber: String, country: JNCountry, isValidPhoneNumber: Bool, forPhoneNumberView phoneNumberView: JNPhoneNumberView) {
        let validationMessage = isValidPhoneNumber ? "Valid Phone Number" : "Invalid Phone Number"
       // self.phoneNumberLabel.text = "International Phone Number: \n \(self.phoneNumberView.getDialCode() + nationalNumber) \n \(validationMessage)"
        
       // self.phoneNumberLabel.textColor = isValidPhoneNumber ? UIColor.blue : UIColor.red
    }
    
    /**
     Country Did Changed
     - Parameter country: New Selected Country
     - Parameter isValidPhoneNumber: Is valid phone number flag as bool
     - Parameter phoneNumberView: Phone number view
     */
    func phoneNumberView(countryDidChanged country: JNCountry, isValidPhoneNumber: Bool, forPhoneNumberView phoneNumberView: JNPhoneNumberView) {
        let validationMessage = isValidPhoneNumber ? "Valid Phone Number" : "Invalid Phone Number"
       // self.phoneNumberLabel.text = "International Phone Number: \n \(self.phoneNumberView.getPhoneNumber()) \n \(validationMessage)"
       // self.phoneNumberLabel.textColor = isValidPhoneNumber ? UIColor.blue : UIColor.red
    }
    
    private func getConfigration() -> JNPhoneNumberViewConfiguration {
        let configrartion = JNPhoneNumberViewConfiguration()
        configrartion.phoneNumberTitleColor = UIColor.white
        configrartion.countryDialCodeTitleColor = UIColor.white
        configrartion.phoneNumberTitleFont = UIFont.systemFont(ofSize: 15.0)
        configrartion.countryDialCodeTitleFont = UIFont.systemFont(ofSize: 20.0)
        return configrartion
    }
}
