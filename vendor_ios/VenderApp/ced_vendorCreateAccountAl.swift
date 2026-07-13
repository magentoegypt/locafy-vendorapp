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
import RxCocoa
import RxSwift

class ced_vendorCreateAccountAl: UIViewController {
    
    
    @IBOutlet weak var wrapperView: UIView!
    @IBOutlet weak var publicName: UITextField!
    @IBOutlet weak var shopUrl: UITextField!
    @IBOutlet weak var SubmitButton: UIButton!
    
    var disposeBag = DisposeBag()
    let mobilePopup = mobileSignupFieldPopup()
    
    var customerId = String()
    let defaults = UserDefaults.standard
    var otpEnabled = false
    override func viewDidLoad() {
        super.viewDidLoad()
        getMobileLoginSettings()
        wrapperView.makeCard(wrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        // Do any additional setup after loading the view.
        SubmitButton.addTarget(self, action: #selector(ced_vendorCreateAccountAl.createAccount(_:)), for: UIControl.Event.touchUpInside)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func getMobileLoginSettings(){
        ApiHandler.handle.request(with: "vsignupapi/setting", params: [:], requestType: .GET, controller: self) { data, error in
            guard let data = data else {return}
            guard let json = try? JSON(data:data) else {return}
            print(json)
            if json[0]["status"].stringValue == "true"{
                self.otpEnabled = true
            }else {
                self.otpEnabled = false
            }
        }
    }
    
    
    @objc func createAccount(_ sender:UIButton){
//        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Please Wait Loading....", comment: ""))
        let publicame = publicName.text
        let shopUr  = shopUrl.text
        if(publicame! ==  "" || shopUr! == ""){
            ShowSimpleAlert.showSimpleAlert(self, showTitle: NSLocalizedString("Alert", comment: ""), showMsg:NSLocalizedString("All Fields Are Required".localized, comment: ""))
            Alert_File.removeLoadingIndicator(self)
            return
        }
        let data = [["value":publicame!,"key":"public_name"],["value":shopUr!,"key":"shop_url"]]
        
        //MARK: - add mobile number popup
        
        let bgView = UIView()
        bgView.frame = view.bounds
        view.addSubview(bgView)
        bgView.backgroundColor = UIColor.black.withAlphaComponent(0.5)
        bgView.tag = 1898
        self.addgesturesTohideView(self.view)
        
        
        mobilePopup.backgroundColor = .white
        bgView.addSubview(mobilePopup)
        mobilePopup.center(inView: bgView)
        mobilePopup.anchor(left: bgView.leadingAnchor, right: bgView.trailingAnchor, paddingLeft: 20, paddingRight: 20,height: 220)

        mobilePopup.continueBtn.rx.tap.bind{
            if self.mobilePopup.mobileField.text == ""{
                self.view.makeToast("Enter mobile number")
                return
            }
            let mobile = self.mobilePopup.mobileField.text ?? ""
            if self.otpEnabled{
                bgView.removeFromSuperview()
                self.sendOTP(toNumber: mobile, param: (data))
//                self.verify(number: mobile) { [weak self] success in
//                    if success {
//                        
//                    }
//                }
            }else{
                self.registerUser(with: data,nummber: mobile)
            }
        }.disposed(by: disposeBag)
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
    
    private func sendOTP(toNumber number: String,param:[[String:String]] ) {
        let countryID: String = Locale.current.regionCode ?? ""
        ApiHandler.handle.request(with: "vsignupapi/sendOtp", params: ["mobile": number, "country_id": "IN"], requestType: .POST, controller: self) { [weak self] data, _ in
            guard let data = data else { return }
            guard let json = try? JSON(data: data) else { return }
            print(json)
            
            self?.view.makeToast(json[0]["message"].stringValue)
            // if OTP is successfully sent show the OTP popup to verify OTP
            if json[0]["status"].stringValue == "true" {
                
                let popup        = AuthenticationView(otp: "1234")
                guard let window = UIApplication.shared.keyWindow else { return }
                window.addSubview(popup)
                popup.frame = window.bounds
                
                // Handle the success state of OTP validation
                popup.onSuccess = { otp in
                    self?.verifyOTP(on: number, otp: otp, postData: param)
                }
                
                // Handle the failure state of OTP validation
                popup.onFailure = { message in
                    self?.view.makeToast(message)
                    return
                }
                
                // Handle the resend OTP Action
                popup.onResend = {
                    self?.sendOTP(toNumber: number, param: param)
                }
            }
        }
    }
    
    private func verifyOTP(on number: String, otp: String,postData:[[String:String]]) {
        let countryID: String = Locale.current.regionCode ?? ""
        let param = ["mobile": number, "country_id": "IN", "otp": otp]
        ApiHandler.handle.request(with: "vsignupapi/verifyOtp", params: param, requestType: .POST, controller: self) { [weak self] data, _ in
            guard let data = data else { return }
            guard let json = try? JSON(data: data) else { return }
            print(json)
            
            self?.view.makeToast(json[0]["data"]["customer"][0]["message"].stringValue)
            // if OTP is successfully verified
            if json[0]["data"]["customer"][0]["status"].boolValue  {
                self?.registerUser(with: postData,nummber: number)
            }
        }
    }
    
    func registerUser(with postData:[[String:String]],nummber:String){
        var postDataArr = postData
        postDataArr.append(["key":"mobile_number","value":nummber])
        
        let finaPostData = ["vendor":postDataArr]
        var itemString = String()
        do{
            let theJSONData = try JSONSerialization.data(
                withJSONObject: finaPostData ,
                options: JSONSerialization.WritingOptions(rawValue: 0))
            itemString = NSString(data: theJSONData,
                                  encoding: String.Encoding.utf8.rawValue)! as String
            
        }
        catch{
            print("error in data encoding")
        }
        
        print(itemString)
        var baseUrl = settings.baseUrl
        baseUrl += "vendorapi/index/approvalPost"
        let dataPosted = "customer_id="+customerId+"&approveaccount=" + itemString
        
        print("DEBUG:\(baseUrl ?? "")")
         print("DEBUG:\(dataPosted)")
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
                            self.view.showToastMsg(json["data"]["customer"][0]["message"].stringValue)
                            Ced_CommonVendor.delay(2, closure: {
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
//MARK: - UIGesture
extension ced_vendorCreateAccountAl:UIGestureRecognizerDelegate{
    
    @objc func addgesturesTohideView(_ v:UIView){
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(self.hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(self.hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(self.hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(self.hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(self.hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    @objc func hideView(_ recognizer: UITapGestureRecognizer){
        self.view.viewWithTag(1898)?.removeFromSuperview()
    }
    
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool {
        let view = touch.view ?? UIView()
        return !view.isDescendant(of: mobilePopup)
    }
}
