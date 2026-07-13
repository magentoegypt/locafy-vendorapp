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

class ced_vendorForgotpass: UIViewController {
    
    @IBOutlet weak var vedoremail: UITextField!
    @IBOutlet private weak var verifyphoneNumberView: JNPhoneNumberView!
    @IBOutlet weak var forgotSubmit: UIButton!
    @IBOutlet weak var forgotWithOTPbtn: SSRadioButton!
    @IBOutlet weak var forgotWithPasswordbtn: SSRadioButton!
    
    var otpText = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        Util.deactivateRTL(of: self.verifyphoneNumberView)
        self.verifyphoneNumberView.delegate = self
       // self.verifyphoneNumberView.setViewConfiguration(self.getConfigration())
        self.verifyphoneNumberView.setPhoneNumber("")
        verifyphoneNumberView.layer.cornerRadius = 1
        verifyphoneNumberView.layer.borderColor = UIColor.lightGray.cgColor
        verifyphoneNumberView.layer.borderWidth = 1
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        forgotSubmit.backgroundColor = color
        vedoremail.becomeFirstResponder()
        forgotSubmit.addTarget(self, action: #selector(ced_vendorForgotpass.vendorForgotpass(_:)), for: UIControl.Event.touchUpInside)
        
        
    }
    
    func randomNumber(digits:Int) -> String {
        var number = String()
        for _ in 1...digits {
           number += "\(Int.random(in: 1...9))"
        }
        return number
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func forgotWithOTPbtntpd(_ sender: Any) {
        forgotWithPasswordbtn.isSelected = false
        forgotWithOTPbtn.isSelected = true
        vedoremail.isHidden = true
        verifyphoneNumberView.isHidden = false
        forgotSubmit.setTitle("GENERATE OTP".localized, for: .normal)
    }
    
    @IBAction func forgotWithPasswordbtntpd(_ sender: Any) {
        forgotWithPasswordbtn.isSelected = true
        forgotWithOTPbtn.isSelected = false
        vedoremail.isHidden = false
        verifyphoneNumberView.isHidden = true
        forgotSubmit.setTitle("submittext".localized, for: .normal)
    }
    
    @objc func vendorForgotpass(_ sender:UIButton){
        
        let email = vedoremail.text?.trim() ?? ""
        if(email ==  ""){
            if(self.forgotWithPasswordbtn.isSelected){
                self.view.showToastMsg(NSLocalizedString("Email is Required.", comment: ""))
                return
            }
        }
        if(self.forgotWithPasswordbtn.isSelected){
            let postString = "email=" + email
            self.forgotPasswordApi(postString)
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
        /*if(!Ced_CommonVendor.isValidEmail(testStr: email!))
        {
            Alert_File.removeLoadingIndicator(self)
            self.view.showToastMsg(NSLocalizedString("Email is not valid", comment: ""))
            return
        }*/
        
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
                self?.view.makeToast(json[0]["message"].stringValue)
                // if OTP is successfully sent show the OTP popup to verify OTP
                let popup        = AuthenticationView(otp: self?.otpText ?? "1234")
                guard let window = UIApplication.shared.keyWindow else { return }
                window.addSubview(popup)
                popup.frame = window.bounds
                
                // Handle the success state of OTP validation
                popup.onSuccess = { otp in
                    self?.forgotPasswordApi(number)
                   // self?.forgotPasswordApi("918200449200")
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
    
    func forgotPasswordApi(_ postString:String){
        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Loading Please Wait...", comment: ""))
        var baseUrl = settings.baseUrl
        if(self.forgotWithPasswordbtn.isSelected){
            baseUrl += "vendorapi/index/forgotPassword"
        }else{
            let secondsStamp = Int(Date().timeIntervalSince1970)
            baseUrl += "wapplogin/index/forgotPassword?mobile=\(postString)&_t=\(secondsStamp)"
        }
        print(baseUrl)
        var request =  URLRequest(url: URL(string:baseUrl)!)
        if(self.forgotWithPasswordbtn.isSelected){
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
                    Alert_File.removeLoadingIndicator(self)
                    print(json)
                    if(json["data"]["customer"][0]["success"].stringValue.lowercased() == "true"){
                        if(self.forgotWithPasswordbtn.isSelected){
                            let alertController = UIAlertController(title: "Message".localized, message: json["data"]["customer"][0]["message"].stringValue, preferredStyle: .alert)
                            let action = UIAlertAction(title: NSLocalizedString("Ok".localized, comment: ""), style: .default, handler: { (action2) in
                                self.navigationController?.popViewController(animated: true)
                            })
                            alertController.addAction(action)
                            self.present(alertController, animated: true, completion: nil)
                        }else{
                            let reset_url = json["data"]["customer"][0]["reset_url"].stringValue
                            if let url = URL(string: reset_url) {
                                UIApplication.shared.open(url, options: [:]) { success in
                                    if success {
                                        print("App opened successfully")
                                    } else {
                                        print("Failed to open app")
                                    }
                                }
                            }
                        }
                    }else{
                        self.view.showToastMsg(json["data"]["customer"][0]["message"].stringValue)
                    }
            }
        })
        task.resume()
    }
    
}

extension ced_vendorForgotpass: JNPhoneNumberViewDelegate {
    
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
