//
//  ced_vendorRegistration.swift
//  VenderApp
//
//  Created by cedcoss on 3/3/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
import MobileCoreServices
import RxSwift
import RxCocoa
import Photos
import QuickLookThumbnailing
import CoreImage
import IQKeyboardManager

class ced_vendorRegistration: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate, UITextFieldDelegate, UIDocumentPickerDelegate, UIDocumentMenuDelegate, UITextViewDelegate
{
    
    @IBOutlet weak var signUpStackview: UIStackView!
    @IBOutlet weak var weblinkbtn: UIButton!
    
    @IBOutlet weak var signUpStackviewHeight: NSLayoutConstraint!
    var stateDropDownButton:UIButton?
    var cityDropDownButton:UIButton?
    let mobileNumber = JNPhoneNumberView()
    var signUpfields = [[String:String]]()
    var dropDownOptions = [String: [String]]()
    var index = 200
    var countryCode = [String]()
    var dropDownWholeData = [String: [String:String]]()
    var stateDropDown = [String: [String]]()
    var cityDropDown = [String: [String]]()
    var dropDown = DropDown();
    let toolBar = UIToolbar()
    let datePicker = UIDatePicker()
    var dateBtn = UIButton()
    var dateBtnArray = [UIButton]()
    var selectedImage = UIImage()
    var allVideo = [String]()
    var allImages = [String]()
    var alldocuments = [String]()
    var DocsAttached = [Int:String]()
    var postString : String?
    let defaults   = UserDefaults.standard
    var passwordString = ""
    var cpasswordString = ""
    var isSecuredText = true;
    var otpText = ""
    var step = "1"
    var vendor_id:String = ""
    var hashKey:String = ""
    var loginJson:JSON?
    
     var fromSocialLogin = Bool()
    var socialLoginData = [String:String]()
    
    var disposeBag = DisposeBag()
    let mobilePopup = mobileSignupFieldPopup()
    let imagePicker = ImagePickerManager()
    var otpEnabled = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        loadData()
        mobilePopup.vendorRegistration = self
        getMobileLoginSettings()
        if(step == "2"){
            weblinkbtn.isHidden = true
        }
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
       // self.tabBarController?.tabBar.isHidden = true
        self.navigationItem.backBarButtonItem?.title = ""
    }
    
    @IBAction func createAccounbtntpd(_ sender: Any) {
        if let url = URL(string: "https://locafy.market/create-vendor-account") {
            UIApplication.shared.open(url)
        }
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
    
    func randomNumber(digits:Int) -> String {
        var number = String()
        for _ in 1...digits {
           number += "\(Int.random(in: 1...9))"
        }
        return number
    }
    
    func loadData()
    {
        print("in loadData")
        self.getRequestFields(url: "vmultistepregapi/index/registrationFields")
    }
    
    func uploadPdfFile(with postdata:[String:Any],vendormparam:[[String:String]]){
        let boundary = "Boundary-\(UUID().uuidString)"
        var allfileUrl:[String] = [String]()
        allfileUrl.append(contentsOf: allImages)
        allfileUrl.append(contentsOf: allVideo)
        allfileUrl.append(contentsOf: alldocuments)
        ApiHandler.handle.requestDictWithForm(with: "vmultistepregapi/index/upload?vendor_id=\(vendor_id)&hashkey=\(hashKey)",boundary: boundary,allfileUrl, controller: self) { data, error in
            guard let data = data else {return}
            guard let json = try? JSON(data: data) else {return}
            print(json)
            DispatchQueue.main.async {
                if json[0]["success"].stringValue != "true"{
                    let file = json["data"]["file"].stringValue
                    self.registerUser(with: postdata, vendormparam: vendormparam, num: file)
                }
                else{
                    self.view.showToastMsg(json[0]["message"].stringValue)
                    self.registerUser(with: postdata, vendormparam: vendormparam, num: "")
                }
            }
        }
    }

    @objc func getRequestFields(url: String)
    {
      //--edited  var baseUrl = Ced_CommonVendor.getInfoPlist("baseUrl") as? String
        var baseUrl = settings.baseUrl
       
        baseUrl += url
        print(baseUrl)
        var request =  URLRequest(url: URL(string:baseUrl)!)
        
        request.httpMethod = "GET"
        //request.httpBody = parameters.data(using: String.Encoding.utf8)
        // print(parameters)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        print(request)
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
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                }
                print("error=\(error)")
                return;
            }
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                DispatchQueue.main.async
                    {
                        
                        Alert_File.removeLoadingIndicator(self)
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        print("response = \(response)")
                        print(baseUrl)
                        //self.verdorOrderTable.isHidden = true
                        if(httpStatus.statusCode == -1009){
                            ced_showError.showNoNetWork(self)
                        }else{
                            ced_showError.showNoModule(self)
                        }
                }
                return;
            }
            DispatchQueue.main.async
                {
                    //print(NSString(data: data!, encoding: String.Encoding.utf8.rawValue)!)
                    //let json = JSON(data: data!)
                    Alert_File.removeLoadingIndicator(self)
                    if let data = data{
                        guard let json = try? JSON(data: data) else {return}
                        if(json["data"].exists())
                        {
                            if(json["data"]["logout"].stringValue == "true")
                            {
                                self.defaults.set(false, forKey: "isLogin")
                                UserDefaults.standard.set(false, forKey: "cedMageStart")
                                
                                self.defaults.removeObject(forKey: "userInfoDict")
                                self.defaults.removeObject(forKey: "cedNavTransact")
                                self.defaults.removeObject(forKey: "cedNavReport")
                                self.defaults.removeObject(forKey: "cedNavOrder")
                                self.defaults.removeObject(forKey: "cedNavMemberShipPlans")
                                self.defaults.removeObject(forKey: "cedNaveitems")
                                let storyboard = UIStoryboard(name: "ced_vendorLogin", bundle: nil)
                                //changed here
                                let viewControl = storyboard.instantiateViewController(withIdentifier: "rootbeforelogin") as? ced_vendorLogin
                                self.present(viewControl!, animated: true, completion: nil)
                                return;
                            }
                        }
                    }
                    self.recieveResponse(data: data, requestUrl: url, response: response)
            }
        })
        task.resume()
    }
    
    //MARK:- Response function
    
    func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?)
    {
        print("in receive Response")
        if let data = data
        {
            guard let json = try? JSON(data: data) else {return}
            print(json)
           // var countryDic:[String:String] = [String:String]()
            var stateDic:[String:String] = [String:String]()
            var cityDic:[String:String] = [String:String]()
            if json["success"].stringValue == "true"
            {
                print("success")
                for (key,value) in  json["data"]
                {
//                    if(value["field_to_post"].stringValue == "country_id"){
//                        countryDic = ["key":key,"label":value["field_name"].stringValue,"required":value["is_required"].stringValue,"type":value["type"].stringValue, "postLabel": value["field_to_post"].stringValue,"countNo":"\(self.index)"]
//                    }else
                    if(value["field_to_post"].stringValue == "region"){
                        stateDic = ["key":key,"label":value["field_name"].stringValue,"required":value["is_required"].stringValue,"type":value["type"].stringValue, "postLabel": value["field_to_post"].stringValue,"countNo":"\(self.index)"]
                    }else if(value["field_to_post"].stringValue == "city"){
                        cityDic = ["key":key,"label":value["field_name"].stringValue,"required":value["is_required"].stringValue,"type":value["type"].stringValue, "postLabel": value["field_to_post"].stringValue,"countNo":"\(self.index)"]
                    }else{
                        self.signUpfields.append(["key":key,"label":value["field_name"].stringValue,"required":value["is_required"].stringValue,"type":value["type"].stringValue, "postLabel": value["field_to_post"].stringValue,"countNo":"\(self.index)"])
                    }
                    if(value["options"].exists())
                    {
                        var Options = [String:String]()
                        var dropDowndataSource = [String]()
                        for indexData in value["options"].arrayValue
                        {
                            print("indexData option: ")
                            print(indexData)
                            if(value["field_to_post"] == "region"){
                                Options[indexData["country_id"].stringValue] = indexData["label"].stringValue
                                var stringArr = stateDropDown[indexData["country_id"].stringValue] ?? []
                                stringArr.append(indexData["label"].stringValue)
                                stateDropDown[indexData["country_id"].stringValue] = stringArr
                            }else if(value["field_to_post"] == "city"){
                                Options[indexData["states_name"].stringValue] = indexData["label"].stringValue
                                var stringArr = cityDropDown[indexData["states_name"].stringValue] ?? []
                                stringArr.append(indexData["label"].stringValue)
                                cityDropDown[indexData["states_name"].stringValue] = stringArr
                            }else{
                                Options[indexData["value"].stringValue] = indexData["label"].stringValue
                            }
                            dropDowndataSource.append(indexData["label"].stringValue)
                        }
                        print("dropDowndataSource = \(dropDowndataSource)")
                        print("Options = \(Options)")
                        self.dropDownOptions[value["field_to_post"].stringValue] = dropDowndataSource
                        self.dropDownWholeData[value["field_to_post"].stringValue] = Options
                    }
                    self.index += 1;
                }
              //  self.signUpfields.insert(countryDic, at: 3)
                self.signUpfields.insert(stateDic, at: 5)
                self.signUpfields.insert(cityDic, at: 6)
            }
            print("signUpfields array = \(self.signUpfields)")
            print("dropdown array= \(self.dropDownOptions)")
            print("whole dropDown data = \(self.dropDownWholeData)")
            self.designFields()
        }
    }
    
    //MARK:- designFields
    func designFields()
    {
        signUpStackview.removeAllArrangedSubviews()
        signUpStackviewHeight.constant = 0
        print("inside design funtion")
        print("signUpfields data:")
        print(signUpfields)
        signUpStackview.distribution = .equalSpacing
        
        let headingLabel = UILabel()
        signUpStackview.addArrangedSubview(headingLabel)
        headingLabel.text = "SELLER REGISTRATION".localized
        headingLabel.textColor = .red
        headingLabel.textAlignment = .center
        headingLabel.font = UIFont.systemFont(ofSize: 15, weight: .regular)
        
        headingLabel.translatesAutoresizingMaskIntoConstraints = false;
        signUpStackview.addConstraint(NSLayoutConstraint(item: headingLabel, attribute: .leading, relatedBy: .equal, toItem: signUpStackview, attribute: .leading, multiplier: 1, constant: 20))
        signUpStackview.addConstraint(NSLayoutConstraint(item: headingLabel, attribute: .trailing, relatedBy: .equal, toItem: signUpStackview, attribute: .trailing, multiplier: 1, constant: -20))
        signUpStackview.addConstraint(NSLayoutConstraint(item: headingLabel, attribute: .top, relatedBy: .equal, toItem: signUpStackview, attribute: .bottom, multiplier: 1, constant: 10))
        signUpStackview.addConstraint(NSLayoutConstraint(item: headingLabel, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 40))
        signUpStackviewHeight.constant += 45
        
        if signUpfields.count > 0
        {
            for indextype in signUpfields
            {
                if(step == "1" && (indextype["postLabel"] != "shop_url" && indextype["postLabel"] != "product_sample" && indextype["postLabel"] != "regdoc")){
                    
                }else if(step == "2" && (indextype["postLabel"] == "shop_url" || indextype["postLabel"] == "product_sample")){
                    
                }else{
                    continue
                }
                if(indextype["postLabel"] == "shop_url" || indextype["postLabel"] == "is_subscribed"){
                    continue
                }
                print("indextype: \(indextype)")
                switch indextype["type"]
                {
                case "text":
                    print("text check")
                    if(indextype["postLabel"] == "contact_number"){
                        self.addMobileNumber(specificData: indextype)
                    }else{
                        self.createTextField(specificData: indextype)
                    }
                    break;
                case "select":
                    print("select check")
                    self.createSelectButton(specificData: indextype)
                    break;
                case "checkbox":
                    print("checkbox check")
                    self.createCheckBoxButton(specificData: indextype)
                    break;
                case "password":
                    print("password check")
                   // if !fromSocialLogin{
                    self.createPasswordField(specificData: indextype,false)
                   // }
                    break;
                case "image":
                    print("image check")
                    self.createImageNDocField(specificData: indextype)
                    break;
                case "file":
                    print("image check")
                    self.createImageNDocField(specificData: indextype)
                    break;
                case "date":
                    print("date check")
                    //self.createDatePicker(specificData: indextype)
                    self.DatePicker1(specificData: indextype)
                    break;
                default:
                    print("abcs")
                }
            }
            
            let RegisterBtn = UIButton()
            signUpStackview.addArrangedSubview(RegisterBtn)
            RegisterBtn.setTitle("submittext".localized, for: .normal)
            RegisterBtn.addTarget(self, action: #selector(registerButtonClicked(sender:)), for: .touchUpInside)
            let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
            //--edited RegisterBtn.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString).withAlphaComponent(0.5)
            //--edited   RegisterBtn.backgroundColor = UIColor(hexString: "#BE3C00")?.withAlphaComponent(0.65)
            RegisterBtn.backgroundColor = DynamicColor.themeColor
            //RegisterBtn.backgroundColor = UIColor.blue
            RegisterBtn.tintColor = UIColor.white
            RegisterBtn.layer.cornerRadius = 5.0
            RegisterBtn.translatesAutoresizingMaskIntoConstraints = false;
            signUpStackview.addConstraint(NSLayoutConstraint(item: RegisterBtn, attribute: .leading, relatedBy: .equal, toItem: signUpStackview, attribute: .leading, multiplier: 1, constant: 20))
            signUpStackview.addConstraint(NSLayoutConstraint(item: RegisterBtn, attribute: .trailing, relatedBy: .equal, toItem: signUpStackview, attribute: .trailing, multiplier: 1, constant: -20))
            signUpStackview.addConstraint(NSLayoutConstraint(item: RegisterBtn, attribute: .top, relatedBy: .equal, toItem: signUpStackview, attribute: .bottom, multiplier: 1, constant: 10))
            signUpStackview.addConstraint(NSLayoutConstraint(item: RegisterBtn, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 50))
            signUpStackviewHeight.constant += 55
            
            let blankView = UIView()
            signUpStackview.addArrangedSubview(blankView)
            blankView.backgroundColor = .clear
            
            blankView.translatesAutoresizingMaskIntoConstraints = false;
            signUpStackview.addConstraint(NSLayoutConstraint(item: blankView, attribute: .leading, relatedBy: .equal, toItem: signUpStackview, attribute: .leading, multiplier: 1, constant: 0))
            signUpStackview.addConstraint(NSLayoutConstraint(item: blankView, attribute: .trailing, relatedBy: .equal, toItem: signUpStackview, attribute: .trailing, multiplier: 1, constant: 0))
            signUpStackview.addConstraint(NSLayoutConstraint(item: blankView, attribute: .top, relatedBy: .equal, toItem: signUpStackview, attribute: .bottom, multiplier: 1, constant: 10))
            signUpStackview.addConstraint(NSLayoutConstraint(item: blankView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 10))
            signUpStackviewHeight.constant += 10
        }
        
    }
    
    
    //MARK:- TextField creation
    func createTextField(specificData : [String:String])
    {
        print("inside createTextField")
        print("specificData =\(specificData)")
        let textField = Label_TextFieldComboView()
        signUpStackview.addArrangedSubview(textField)
        textField.tag = Int(specificData["countNo"]!)!
        textField.view.backgroundColor = .clear
        
        textField.headingLabel.textColor = .black
        textField.textLabel.textColor = .black
        textField.textLabel.textColor = .black
       // textField.textLabel.backgroundColor = UIColor.white
        if specificData["required"] == "1"
        {
            textField.headingLabel.text = specificData["label"]! + "*"
            if specificData["label"]! == "Shop Url"{
                let myAttribute = [ NSAttributedString.Key.foregroundColor : UIColor.black ]
                let myString = NSMutableAttributedString(string: specificData["label"]! + "*", attributes: myAttribute )
                
                let attributedString = NSAttributedString(string: NSLocalizedString(" (Example: my-shop-url)", comment: ""), attributes:[
                    NSAttributedString.Key.foregroundColor : UIColor.red,
                    NSAttributedString.Key.font : UIFont.systemFont(ofSize: 13, weight: .medium)
                ])
                myString.append(attributedString)
                textField.headingLabel.attributedText = myString
                //--edited textField.headingLabel.text = specificData["label"]! + "*" + " (Example: my-shop-url)"
            }
            if specificData["label"]! == "Public Name"{
                let myAttribute = [ NSAttributedString.Key.foregroundColor : UIColor.black]
                let myString = NSMutableAttributedString(string: specificData["label"]! + "*", attributes: myAttribute )
                
                let attributedString = NSAttributedString(string: NSLocalizedString(" (Name to be displayed to customers)", comment: ""), attributes:[
                    NSAttributedString.Key.foregroundColor : UIColor.red,
                    NSAttributedString.Key.font : UIFont.systemFont(ofSize: 13, weight: .medium)
                ])
                myString.append(attributedString)
                textField.headingLabel.attributedText = myString
              //--edited  textField.headingLabel.text = specificData["label"]! + "*" + " (Name to be displayed to customers)"
            }
            if specificData["label"]! == "Email"{
                if fromSocialLogin{
                    textField.textLabel.text = socialLoginData["email"] ?? ""
                    textField.textLabel.isEnabled = false
                }
            }
            if specificData["label"]! == "First Name"{
                if fromSocialLogin{
                    textField.textLabel.text = socialLoginData["firstname"] ?? ""
                    textField.textLabel.isEnabled = false
                }
            }
            if specificData["label"]! == "Last Name"{
                if fromSocialLogin{
                    textField.textLabel.text = socialLoginData["lastname"] ?? ""
                    textField.textLabel.isEnabled = false
                }
            }
            
        }
        else
        {
            textField.headingLabel.text = specificData["label"]
            if specificData["label"]! == "Shop Url"{
                let myAttribute = [ NSAttributedString.Key.foregroundColor : UIColor.black ]
                let myString = NSMutableAttributedString(string: specificData["label"]!, attributes: myAttribute )
                
                let attributedString = NSAttributedString(string: NSLocalizedString(" (Example: my-shop-url)", comment: ""), attributes:[
                    NSAttributedString.Key.foregroundColor : UIColor.red,
                    NSAttributedString.Key.font : UIFont.systemFont(ofSize: 13, weight: .medium)
                ])
                myString.append(attributedString)
                textField.headingLabel.attributedText = myString
             //--edited   textField.headingLabel.text = specificData["label"]! + " (Example: my-shop-url)"
            }
            if specificData["label"]! == "Public Name"{
                let myAttribute = [ NSAttributedString.Key.foregroundColor : UIColor.black ]
                let myString = NSMutableAttributedString(string: specificData["label"]!, attributes: myAttribute )
                
                let attributedString = NSAttributedString(string: NSLocalizedString(" (Name to be displayed to customers)", comment: ""), attributes:[
                    NSAttributedString.Key.foregroundColor : UIColor.red,
                    NSAttributedString.Key.font : UIFont.systemFont(ofSize: 13, weight: .medium)
                ])
                myString.append(attributedString)
                textField.headingLabel.attributedText = myString
               //--edited textField.headingLabel.text = specificData["label"]! + " (Name to be displayed to customers)"
            }
        }
        //|| specificData["label"] == "Zip/Postal Code "
        if (specificData["label"] == "Contact Number")
        {
            textField.textLabel.keyboardType = .numberPad
        }
        else
        {
            textField.textLabel.keyboardType = .default
        }
        textField.textLabel.tag = Int(specificData["countNo"]!)!
        textField.translatesAutoresizingMaskIntoConstraints = false;
        signUpStackview.addConstraint(NSLayoutConstraint(item: textField, attribute: .leading, relatedBy: .equal, toItem: signUpStackview, attribute: .leading, multiplier: 1, constant: 0))
        signUpStackview.addConstraint(NSLayoutConstraint(item: textField, attribute: .trailing, relatedBy: .equal, toItem: signUpStackview, attribute: .trailing, multiplier: 1, constant: 0))
        //signUpStackview.addConstraint(NSLayoutConstraint(item: textField, attribute: .top, relatedBy: .equal, toItem: signUpStackview, attribute: .bottom, multiplier: 1, constant: 10))
        signUpStackview.addConstraint(NSLayoutConstraint(item: textField, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 80))
        signUpStackviewHeight.constant += 85
    }
    
    
    //MARK:- Select Dropdown creation
    func createSelectButton(specificData : [String:String])
    {
        print("inside createSelectButtom")
        print("specifuc data = \(specificData)")
        
        let selectDropDown = Label_DropDown_Combo_View()
        signUpStackview.addArrangedSubview(selectDropDown)
        selectDropDown.tag = Int(specificData["countNo"]!)!
        selectDropDown.view.backgroundColor = .clear
        selectDropDown.entityNameLabel.textColor = .black
        
        //selectDropDown.downArrowImg.tintColor = .white
        selectDropDown.dropDownButton.setTitleColor(.black, for: .normal)
        selectDropDown.dropDownButton.layer.borderColor = UIColor.black.cgColor
       //  selectDropDown.downArrowImg.tintColor = .black
        selectDropDown.dropDownButton.setTitleColor(.black, for: .normal)
        selectDropDown.dropDownButton.backgroundColor = .lightGray
        if specificData["required"] == "1"
        {
            selectDropDown.entityNameLabel.text = specificData["label"]! + "*"
        }
        else
        {
            selectDropDown.entityNameLabel.text = specificData["label"]
        }
        if(specificData["postLabel"] == "region"){
            var countryName:String = "EG"
            if let datasource = dropDownOptions["country_id"]{
                countryName = datasource.first ?? ""
            }
            self.sortStateCountry(countryName)
            self.stateDropDownButton = selectDropDown.dropDownButton
        }else if(specificData["postLabel"] == "city"){
            var countryName:String = ""
            if let datasource = dropDownOptions["region"]{
                countryName = datasource.first ?? ""
            }
            self.sortCityCountry(countryName)
            self.cityDropDownButton = selectDropDown.dropDownButton
        }
        if let datasource = dropDownOptions[specificData["postLabel"]!]{
            selectDropDown.dropDownButton.setTitle(datasource.first, for: .normal)
            selectDropDown.dropDownButton.isHidden = false
        }else{
            selectDropDown.dropDownButton.isHidden = true
        }
        selectDropDown.dropDownButton.tag = Int(specificData["countNo"]!)!
        selectDropDown.dropDownButton.accessibilityHint = specificData["postLabel"]!
        selectDropDown.dropDownButton.addTarget(self, action: #selector(dropDownClicked(sender:)), for: .touchUpInside)
        selectDropDown.translatesAutoresizingMaskIntoConstraints = false;
        signUpStackview.addConstraint(NSLayoutConstraint(item: selectDropDown, attribute: .leading, relatedBy: .equal, toItem: signUpStackview, attribute: .leading, multiplier: 1, constant: 0))
        signUpStackview.addConstraint(NSLayoutConstraint(item: selectDropDown, attribute: .trailing, relatedBy: .equal, toItem: signUpStackview, attribute: .trailing, multiplier: 1, constant: 0))
        //signUpStackview.addConstraint(NSLayoutConstraint(item: selectDropDown, attribute: .top, relatedBy: .equal, toItem: signUpStackview, attribute: .bottom, multiplier: 1, constant: 10))
        signUpStackview.addConstraint(NSLayoutConstraint(item: selectDropDown, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 70))
        signUpStackviewHeight.constant += 75
        
    }
    
    var countryKey:String = "EG"
    func sortStateCountry(_ countryName:String){
        
        if let datasource = dropDownWholeData["country_id"]{
            for (key,value) in datasource{
                if(value == countryName){
                    countryKey = key
                }
            }
        }
        let dropDowndataSource = stateDropDown[countryKey]
        if(dropDowndataSource?.count ?? 0 > 0){
            self.stateDropDownButton?.isHidden = false
            self.stateDropDownButton?.setTitle(dropDowndataSource?.first, for: .normal)
            self.sortCityCountry(dropDowndataSource?.first ?? "")
        }else{
            self.stateDropDownButton?.isHidden = true
        }
        self.dropDownOptions["region"] = dropDowndataSource
    }
    
    var stateKey:String = ""
    func sortCityCountry(_ stateName:String){
        stateKey = stateName
//        if let datasource = dropDownWholeData["region"]{
//            for (key,value) in datasource{
//                if(value == stateName){
//                    stateKey = key
//                }
//            }
//        }
        let dropDowndataSource = cityDropDown[stateKey]
        if(dropDowndataSource?.count ?? 0 > 0){
            self.cityDropDownButton?.isHidden = false
            self.cityDropDownButton?.setTitle(dropDowndataSource?.first, for: .normal)
        }else{
            self.cityDropDownButton?.isHidden = true
        }
        self.dropDownOptions["city"] = dropDowndataSource
    }
    
    @objc func dropDownClicked(sender: UIButton)
    {
        print("dropDownClicked")
        print("sender tag = \(sender.tag)")
        dropDown = DropDown()
        IQKeyboardManager.shared().resignFirstResponder()
        print(dropDownOptions[(sender.accessibilityHint ?? "")])
        if let datasource = dropDownOptions[(sender.accessibilityHint ?? "")]
        {
            var tempDataSource = datasource;
            print("--- tempDataSource ---")
            print(tempDataSource);
            dropDown.dataSource = tempDataSource;
            dropDown.selectionAction = {(index, item) in
                sender.setTitle(item, for: UIControl.State());
                if(sender.accessibilityHint ?? "" == "country_id"){
                    self.sortStateCountry(item)
                }else if(sender.accessibilityHint ?? "" == "region"){
                    self.sortCityCountry(item)
                }
            }
            dropDown.anchorView = sender
            dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
            if dropDown.isHidden {
                let _ = dropDown.show();
            } else {
                dropDown.hide();
            }
        }
    }
    
    
    //MARK:- Image and Document Picker Functions
    func createImageNDocField(specificData : [String:String])
    {
        print("inside createImageNDocField")
        print("specificData = \(specificData)")
        let label = UILabel()
        signUpStackview.addArrangedSubview(label)
        label.translatesAutoresizingMaskIntoConstraints = false
        label.font = .systemFont(ofSize: 15, weight: .medium)
        label.text = specificData["label"]
        
        signUpStackview.addConstraint(NSLayoutConstraint(item: label, attribute: .leading, relatedBy: .equal, toItem: signUpStackview, attribute: .leading, multiplier: 1, constant: 0))
        signUpStackview.addConstraint(NSLayoutConstraint(item: label, attribute: .trailing, relatedBy: .equal, toItem: signUpStackview, attribute: .trailing, multiplier: 1, constant: 0))
        //signUpStackview.addConstraint(NSLayoutConstraint(item: selectDropDown, attribute: .top, relatedBy: .equal, toItem: signUpStackview, attribute: .bottom, multiplier: 1, constant: 10))
        signUpStackview.addConstraint(NSLayoutConstraint(item: label, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 30))
        signUpStackviewHeight.constant += 35
        
        
        let uploadField = browseImageView()
        signUpStackview.addArrangedSubview(uploadField)
        uploadField.tag = Int(specificData["countNo"]!)!
        uploadField.view.backgroundColor = .clear
        buttonShadow(uploadField.browseButton)
        buttonShadow(uploadField.nameLabel)
        //uploadField.browseButton.setTitleColor(UIColor.blue, for: .normal)
        
        //uploadField.browseButton.addTarget(self, action: #selector(openDocMenu(sender:)), for: .touchUpInside)
        if(step == "1"){
            uploadField.browseButton.add(for: .touchUpInside) {
                self.imagePicker.presentImagePicker(from: self, sourceType: .photoLibrary) { image in
                            if let image = image {
                                uploadField.browseImage.image = image
                                uploadField.base64EncodedStringData = "image"
                            } else {
                                print("No image selected")
                            }
                        }
            }
            uploadField.nameLabel.add(for: .touchUpInside) {
                self.imagePicker.presentImagePicker(from: self, sourceType: .camera) { image in
                            if let image = image {
                                uploadField.browseImage.image = image
                                uploadField.base64EncodedStringData = "image"
                            } else {
                                print("No image selected")
                            }
                        }
            }
        }else{
            uploadField.browseImage.isHidden = true
            uploadField.browseButton.addTarget(self, action: #selector(browseButtonPressed(sender:)), for: .touchUpInside)
            uploadField.nameLabel.addTarget(self, action: #selector(cameraButtonPressed(sender:)), for: .touchUpInside)
        }
        
        /*if specificData["label"] == ": Upload Document"
        {
            uploadField.browseButton.addTarget(self, action: #selector(browseDocFun(sender:)), for: .touchUpInside)
        }
        else
        {
            uploadField.browseButton.addTarget(self, action: #selector(browseImageFunc(sender:)), for: .touchUpInside)
        }*/
        uploadField.nameLabel.setTitle("Camera".localized, for: .normal)
        uploadField.fileAttachedLabel.text = ""
        uploadField.browseButton.tag = Int(specificData["countNo"]!)!
        uploadField.browseButton.accessibilityHint = specificData["postLabel"]
        uploadField.nameLabel.tag = Int(specificData["countNo"]!)!
        uploadField.nameLabel.accessibilityHint = specificData["postLabel"]
        uploadField.fileAttachedLabel.tag = Int(specificData["countNo"]!)!
       
        uploadField.translatesAutoresizingMaskIntoConstraints = false;
        signUpStackview.addConstraint(NSLayoutConstraint(item: uploadField, attribute: .leading, relatedBy: .equal, toItem: signUpStackview, attribute: .leading, multiplier: 1, constant: 0))
        signUpStackview.addConstraint(NSLayoutConstraint(item: uploadField, attribute: .trailing, relatedBy: .equal, toItem: signUpStackview, attribute: .trailing, multiplier: 1, constant: 0))
        if(step == "1"){
            signUpStackview.addConstraint(NSLayoutConstraint(item: uploadField, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 83))
            signUpStackviewHeight.constant += 90
        }else{
            signUpStackview.addConstraint(NSLayoutConstraint(item: uploadField, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 53))
            signUpStackviewHeight.constant += 60
        }
    }
    
    func buttonShadow(_ button:UIButton){
        button.setTitleColor(UIColor.black, for: .normal)
        button.titleLabel!.layer.shadowColor = UIColor.black.cgColor
        button.titleLabel!.layer.shadowOffset = CGSize(width: 1.0, height: 2.0)
        button.titleLabel!.layer.shadowOpacity = 5.0
        button.titleLabel!.layer.shadowRadius = 5
        button.titleLabel!.layer.masksToBounds = false
        button.layer.cornerRadius = 15.0
        button.tintColor = .black
       // button.backgroundColor = UIColor.clear
        button.backgroundColor = UIColor(hexString: "32a1e8")
        button.layer.borderColor = UIColor.black.cgColor
        button.layer.borderColor = UIColor.white.cgColor
        button.layer.borderWidth = 0.5
    }
    
    @objc func browseButtonPressed(sender:UIButton)
    {
        print("browseButtonPressed")
        print(sender.tag)
        print("DocsAttached = \(DocsAttached)")
        if let superView = sender.superview?.superview as? browseImageView
        {
            print("superView found")
            superView.fileAttachedLabel.text = ""
            if DocsAttached.count > 0
            {
                DocsAttached.removeValue(forKey: sender.tag)
            }
        }
        let actionSheetControllerIOS8: UIAlertController = UIAlertController(title: "Option to select", message: nil, preferredStyle: .actionSheet)
           
        
        let selectImages = UIAlertAction(title: "Select images", style: .default)
        { _ in
            print("upload image")
          //  self.browseImageFunc(sender: sender)
            self.openOpalImagePicker()
        }
        actionSheetControllerIOS8.addAction(selectImages)
        let selectVidoe = UIAlertAction(title: "Select video", style: .default)
        { _ in
            print("upload image")
            self.browseImageFunc(isImage: false, isCamera: false)
          //  self.openOpalImagePicker()
        }
        actionSheetControllerIOS8.addAction(selectVidoe)
        let uploadpdf = UIAlertAction(title: "Select documents file", style: .default)
        { _ in
            print("Document")
            self.OpenDocumentPicker(sender: sender)
        }
        let cancelActionButton = UIAlertAction(title: "Cancel", style: .cancel) { _ in
               print("Cancel")
        }
        actionSheetControllerIOS8.addAction(cancelActionButton)
        if let popoverController = actionSheetControllerIOS8.popoverPresentationController {
            popoverController.sourceView = self.view
            popoverController.sourceRect = CGRect(x: self.view.bounds.midX, y: self.view.bounds.midY, width: 0, height: 0)
            popoverController.permittedArrowDirections = []
        }
        actionSheetControllerIOS8.addAction(uploadpdf)
       // self.present(actionSheetControllerIOS8, animated: true, completion: nil)
       // self.OpenDocumentPicker(sender: sender)
        self.openOpalImagePicker()
    }
    
    @objc func cameraButtonPressed(sender:UIButton)
    {
        if(self.allImages.count < 10){
            self.browseImageFunc(isImage: false, isCamera: true)
        }else{
            self.view.makeToast("Maximum 10 images, image size not exceeding 10 MB".localized, duration: 2.0, position: .center)
        }
    }
    
    @objc func removeimgUploadView(_ sender:UIButton)
    {
        print(sender.tag)
        if let uploadImageView = self.view.viewWithTag(sender.tag) as? UploadImageView
        {
            uploadImageView.removeFromSuperview();
            self.signUpStackviewHeight.constant -= 80;
            self.signUpStackview.setNeedsLayout()
            self.allImages.removeAll { url in
                url == uploadImageView.fileUrlStr
            }
            self.allVideo.removeAll { url in
                url == uploadImageView.fileUrlStr
            }
            self.alldocuments.removeAll { url in
                url == uploadImageView.fileUrlStr
            }
        }
    }
    
    var counterTag = 400
    @objc func renderImgUploadView(selectedImage:UIImage,name:String,urlPath:String,_ contentMode:UIView.ContentMode)
    {
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        let uploadImageView = UploadImageView()
        if(signUpStackview.subviews.count > 4){
            signUpStackview.insertArrangedSubview(uploadImageView, at: signUpStackview.subviews.count-4)
        }else{
            signUpStackview.insertArrangedSubview(uploadImageView, at: signUpStackview.subviews.count-1)
        }
        counterTag += 1
        uploadImageView.tag = counterTag
        uploadImageView.fileUrlStr = urlPath
        uploadImageView.delButton.addTarget(self, action: #selector(removeimgUploadView(_:)), for: UIControl.Event.touchUpInside);
        uploadImageView.delButton.backgroundColor = color
        uploadImageView.delButton.tag = counterTag
       
        PopupLookImprovement.designImageView(uploadImageView.imgView)
        uploadImageView.imgView.image = selectedImage
        uploadImageView.imgView.contentMode = contentMode
        uploadImageView.fileNamelbl.text = name
        uploadImageView.delButton.backgroundColor = color
        uploadImageView.browseButton.isHidden = true
        uploadImageView.takephotobtn.isHidden = true
        uploadImageView.defaultbtnHeight.constant = 0
        uploadImageView.fileNamelbl.isHidden = false
        uploadImageView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: 80);
        signUpStackview.addConstraint(NSLayoutConstraint(item: uploadImageView, attribute: .leading, relatedBy: .equal, toItem: signUpStackview, attribute: .leading, multiplier: 1, constant: 5))
        signUpStackview.addConstraint(NSLayoutConstraint(item: uploadImageView, attribute: .trailing, relatedBy: .equal, toItem: signUpStackview, attribute: .trailing, multiplier: 1, constant: -5))
        signUpStackview.addConstraint(NSLayoutConstraint(item: uploadImageView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 80))
        uploadImageView.makeCard(uploadImageView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        signUpStackviewHeight.constant += 80
    }
    
    
    func openOpalImagePicker(){
        let imagePicker = OpalImagePickerController()
        imagePicker.configuration?.okayString = "Ok".localized
        imagePicker.configuration?.maximumSelectionsAllowedMessage = "You cannot select more than 10 images. Please deselect another image before trying to select again.".localized
        let requestOptions = PHImageRequestOptions()
        //Only allow image media type assets
        imagePicker.allowedMediaTypes = Set([PHAssetMediaType.image])
//        if(self.arrMedia.count == 5)
//        {
//            self.showAlert(msg: "You can add maximum 5 images")
//            return
//        }
        if(step == "1"){
            imagePicker.maximumSelectionsAllowed = 1
        }else{
            imagePicker.maximumSelectionsAllowed = 10// - self.arrMedia.count
        }
        requestOptions.resizeMode = PHImageRequestOptionsResizeMode.exact
        requestOptions.deliveryMode = PHImageRequestOptionsDeliveryMode.highQualityFormat
        // this one is key
        requestOptions.isSynchronous = true
        presentOpalImagePickerController(imagePicker, animated: true,
                                         select: { (assets) in
                                            imagePicker.dismiss(animated: true, completion: nil)
            assets.forEach { asset in
                asset.requestContentEditingInput(with: PHContentEditingInputRequestOptions()) { (eidtingInput, info) in
                  if let input = eidtingInput, let imgURL = input.fullSizeImageURL {
                      let imageURL = imgURL
                      let imageName = imageURL.lastPathComponent
                      print(imageName)
                      print(imageURL)
                      do {
                              let imageData = try Data(contentsOf: imageURL)
                          let size = self.sizePerMB(url: imageURL)
                          if(size <= 10){
                              if(self.allImages.count < 10){
                                  self.allImages.append(imageURL.path)
                                  let pickedImage = UIImage(data: imageData)
                                  self.renderImgUploadView(selectedImage: pickedImage!, name: imageName, urlPath: imageURL.path,.scaleToFill)
                              }else{
                                  self.view.makeToast("Maximum 10 images, image size not exceeding 10 MB".localized, duration: 2.0, position: .center)
                              }
                          }else{
                              self.view.makeToast("You cannot image size gerater 10mb".localized, duration: 2.0, position: .center)
                          }
                          
                          } catch {
                              print("Error loading image : \(error)")
                          }
                  }
                }
//                PHImageManager.default().requestImage(for: asset , targetSize: PHImageManagerMaximumSize, contentMode: PHImageContentMode.default, options: requestOptions, resultHandler: { (pickedImage, info) in
//                    
//                })
            }
        }, cancel: {
            //Cancel
        })
    }
    
    func sizePerMB(url: URL?) -> Double {
        guard let filePath = url?.path else {
            return 0.0
        }
        do {
            let attribute = try FileManager.default.attributesOfItem(atPath: filePath)
            if let size = attribute[FileAttributeKey.size] as? NSNumber {
                return size.doubleValue / 1000000.0
            }
        } catch {
            print("Error: \(error)")
        }
        return 0.0
    }
    
    @objc func OpenDocumentPicker(sender: UIButton)
    {
        print("opendocumentPicker")
        let docPicker = UIDocumentPickerViewController(documentTypes: [String(kUTTypePDF)], in: .import)
        docPicker.delegate = self;
        self.present(docPicker, animated: true, completion: nil)
    }
    
    @objc func openDocMenu( sender: UIButton)
    {
        print("openDocMenu")
        print(sender.tag)
        print("DocsAttached = \(DocsAttached)")
        if let superView = sender.superview?.superview as? browseImageView
        {
            print("superView found")
            superView.fileAttachedLabel.text = ""
            if DocsAttached.count > 0
            {
                DocsAttached.removeValue(forKey: sender.tag)
            }
        }
        let menu = UIDocumentMenuViewController(documentTypes: [String(kUTTypePDF),String(kUTTypePNG),String(kUTTypeJPEG)], in: .import)
        menu.delegate = self;
        menu.popoverPresentationController?.sourceView = self.view
        menu.modalPresentationStyle = .popover
        //menu.modalPresentationStyle = .formSheet
        menu.addOption(withTitle: "Attach Image", image: nil, order: .first) {
           // self.browseImageFunc(sender: sender)
        }
        
//        menu.addOption(withTitle: "Attach Document", image: nil, order: .last) {
//
//        }
        self.present(menu, animated: true, completion: nil)
        
    }
    
    func documentMenu(_ documentMenu: UIDocumentMenuViewController, didPickDocumentPicker documentPicker: UIDocumentPickerViewController) {
        documentPicker.delegate = self;
        self.present(documentPicker, animated: true, completion: nil)
    }
    
    func documentPickerWasCancelled(_ controller: UIDocumentPickerViewController) {
        print("view was cancelled")
        self.dismiss(animated: true, completion: nil)
    }
    
    
//    @objc func browseDocFun(sender: UIButton)
//    {
//        print("inside browseDocFun")
//        print(sender.tag)
//        let docPicker = UIDocumentPickerViewController()
//        docPicker.delegate = self;
//        self.present(docPicker, animated: true, completion: nil)
//    }
    
    func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentsAt urls: [URL]) {
        guard let myURL = urls.first else
        {
            return;
        }
        print("--urls fetched--")
        print(urls)
        print(myURL)
        let name = myURL.lastPathComponent
        let image = UIImage(named: "ic_file")
        self.renderImgUploadView(selectedImage: image!, name: name, urlPath: myURL.path,.scaleAspectFit)
        alldocuments.append(myURL.path)
    }
    
    
    
//    func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentAt url: URL) {
//        let doc = url
//        print(doc)
//    }
    
    
    @objc func browseImageFunc(isImage:Bool,isCamera:Bool)
    {
        print("inside browseFunc")
        let imagePicker = UIImagePickerController()
        imagePicker.delegate = self
        if(isCamera){
            imagePicker.sourceType = .camera
        }else if(isImage){
            imagePicker.sourceType = .photoLibrary
            imagePicker.mediaTypes = ["public.image"]
        }else{
            imagePicker.sourceType = .savedPhotosAlbum
            imagePicker.mediaTypes = ["public.movie"]
        }
        imagePicker.allowsEditing = false
        self.present(imagePicker, animated: true, completion: nil)
    }

    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        print("info -- ")
        print(info)
        if (picker.sourceType == UIImagePickerController.SourceType.camera) {

            if let image = info[UIImagePickerController.InfoKey.editedImage] as? UIImage {
                self.selectedImage = image
            } else if let image = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
                self.selectedImage = image
                }
                let imgName = "\(randomNumber(digits: 10)).jpg"
                let documentDirectory = NSTemporaryDirectory()
                let localPath = documentDirectory.appending(imgName)

            let data = selectedImage.jpegData(compressionQuality: 0.2)! as NSData
                data.write(toFile: localPath, atomically: true)
                let imageURL = URL.init(fileURLWithPath: localPath)
            let size = self.sizePerMB(url: imageURL)
            if(size > 10){
                self.view.makeToast("You cannot image size gerater 10mb".localized, duration: 2.0, position: .center)
                return
            }
            self.allImages.append(imageURL.path)
            let imageName = imageURL.lastPathComponent
            renderImgUploadView(selectedImage: self.selectedImage, name: imageName, urlPath: imageURL.path,.scaleToFill)
        }else{
            let imageURL = info[.mediaURL] as! URL
            if((info[.mediaType] as! String) == "public.movie" ){
                // Video file
                let size = self.sizePerMB(url: imageURL)
                if(size > 150){
                    self.view.makeToast("You cannot image size gerater 50mb".localized, duration: 2.0, position: .center)
                    return
                }
                let asset = AVURLAsset(url: imageURL, options: nil)
                let imgGenerator = AVAssetImageGenerator(asset: asset)
                imgGenerator.appliesPreferredTrackTransform = true
                do{
                    let cgImage = try imgGenerator.copyCGImage(at: CMTimeMake(value: 0, timescale: 1), actualTime: nil)
                    let thumbnail = UIImage(cgImage: cgImage)
                    self.selectedImage = thumbnail
                    self.allVideo.append(imageURL.path)
                }catch{
                    
                }
              }
              else{
                  let size = self.sizePerMB(url: imageURL)
                  if(size > 10){
                      self.view.makeToast("You cannot image size gerater 10mb".localized, duration: 2.0, position: .center)
                      return
                  }
                // Image
                  let image = info[.originalImage]
                  self.selectedImage = image as! UIImage
                  self.allImages.append(imageURL.path)
              }
            let imageName = imageURL.lastPathComponent
            renderImgUploadView(selectedImage: self.selectedImage, name: imageName, urlPath: imageURL.path,.scaleToFill)
        }
        picker.dismiss(animated: true, completion: nil)
    }
    
    
    
    //MARK:- createCheckBoxButton Function
    func createCheckBoxButton(specificData: [String:String])
    {
        print("inside createCheckBoxButton")
        print("specific Data = \(specificData)")
        let checkBox = CheckboxButtonView()
        signUpStackview.addArrangedSubview(checkBox)
        checkBox.tag = Int(specificData["countNo"]!)!
        checkBox.view.backgroundColor = .clear
        checkBox.checkboxButton.setTitleColor(.white, for: .normal)
        checkBox.checkboxButton.tintColor = .black
        checkBox.checkboxButton.setTitleColor(.black, for: .normal)
        checkBox.checkboxButton.backgroundColor = .white
        checkBox.checkboxButton.tintColor = .black
        checkBox.checkboxButton.layer.cornerRadius = 5.0
        checkBox.checkboxButton.setImage(UIImage(named: "unchecked"), for: .normal)
        checkBox.checkboxButton.setTitle(specificData["label"], for: .normal)
        checkBox.checkboxButton.tag = Int(specificData["countNo"]!)!
        checkBox.checkboxButton.addTarget(self, action: #selector(checkboxClicked(sender:)), for: .touchUpInside)
        checkBox.translatesAutoresizingMaskIntoConstraints = false;
        signUpStackview.addConstraint(NSLayoutConstraint(item: checkBox, attribute: .leading, relatedBy: .equal, toItem: signUpStackview, attribute: .leading, multiplier: 1, constant: 0))
        signUpStackview.addConstraint(NSLayoutConstraint(item: checkBox, attribute: .trailing, relatedBy: .equal, toItem: signUpStackview, attribute: .trailing, multiplier: 1, constant: 0))
        signUpStackview.addConstraint(NSLayoutConstraint(item: checkBox, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 35))
        signUpStackviewHeight.constant += 40
    }
    
    @objc func checkboxClicked(sender: UIButton)
    {
        print("inside checkboxClicked")
        print(sender.tag)
        if sender.currentImage == UIImage(named: "unchecked")
        {
            sender.setImage(UIImage(named: "checked"), for: .normal)
        }
        else
        {
            sender.setImage(UIImage(named: "unchecked"), for: .normal)
        }
    }
    
    
    //MARK:- PasswordField
    func createPasswordField(specificData : [String:String],_ isConfirmpassword:Bool)
    {
        print("inside createPasswordField")
        print("specific data = \(specificData)")
        let pswdField = PasswordTextFieldView()
        pswdField.view.backgroundColor = .clear
        signUpStackview.addArrangedSubview(pswdField)
        pswdField.passwordTextField.layer.borderColor = UIColor.black.cgColor
        pswdField.headingLabel.textColor = .black
        pswdField.showHideBtn.tintColor = .black
        pswdField.passwordTextField.textColor = .black
        pswdField.view.backgroundColor = .clear
        pswdField.showHideBtn.isHidden = false
       pswdField.showHideBtn.tintColor = .black
        pswdField.passwordTextField.textColor = .black
        pswdField.passwordTextField.backgroundColor = .white
        
        if(isConfirmpassword){
            pswdField.headingLabel.text = "Confirm Password".localized + "*"
            pswdField.passwordTextField.placeholder = "Confirm Password".localized
        }else if specificData["required"] == "1"
        {
            pswdField.headingLabel.text = specificData["label"]! + "*"
            pswdField.passwordTextField.placeholder = specificData["label"]
        }
        else
        {
            pswdField.headingLabel.text = specificData["label"]
            pswdField.passwordTextField.placeholder = specificData["label"]
        }
       
        pswdField.passwordTextField.addTarget(self, action: #selector(passwordTextChanged(_:)), for: .editingChanged)
        pswdField.passwordTextField.delegate = self;
        if(!isConfirmpassword){
            pswdField.passwordTextField.tag = Int(specificData["countNo"]!)!
            pswdField.tag = Int(specificData["countNo"]!)!
            pswdField.showHideBtn.tag = Int(specificData["countNo"]!)!
        }else{
            pswdField.passwordTextField.tag = 10
            pswdField.tag = 10
            pswdField.showHideBtn.tag = 10
        }
        pswdField.showHideBtn.setImage(UIImage(named: "eye_hide"), for: .normal)
        pswdField.showHideBtn.contentMode = .scaleAspectFit
        pswdField.showHideBtn.addTarget(self, action: #selector(showHideClicked(sender:)), for: .touchUpInside)
        pswdField.passwordTextField.isSecureTextEntry = true;
        pswdField.translatesAutoresizingMaskIntoConstraints = false;
        signUpStackview.addConstraint(NSLayoutConstraint(item: pswdField, attribute: .leading, relatedBy: .equal, toItem: signUpStackview, attribute: .leading, multiplier: 1, constant: 0))
        signUpStackview.addConstraint(NSLayoutConstraint(item: pswdField, attribute: .trailing, relatedBy: .equal, toItem: signUpStackview, attribute: .trailing, multiplier: 1, constant: 0))
        signUpStackview.addConstraint(NSLayoutConstraint(item: pswdField, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 70))
        signUpStackviewHeight.constant += 75
        if(!isConfirmpassword){
            self.createPasswordField(specificData: specificData,true)
        }
    }
    
    func textViewDidChange(_ textView: UITextView) {
        print("textViewDidChange")
        if isSecuredText == true
        {
            textView.text = String(repeating: "*", count: (textView.text ?? "").count)
        }
        else if isSecuredText == false
        {
            textView.text = passwordString
        }
    }
    
//    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
//        print("shouldChangeTextIn")
//        passwordString = ((passwordString ?? "") as NSString).replacingCharacters(in: range, with: text)
//        print("passwordString = \(passwordString)")
//        return true
//    }
    @objc  func passwordTextChanged(_ sender: UITextField){
        if sender.placeholder == "Password".localized {
            passwordString = sender.text ?? ""
        }
        if sender.placeholder == "Confirm Password".localized{
            cpasswordString = sender.text ?? ""
        }
    }
    @objc func showHideClicked(sender: UIButton)
    {
        print("showHideClicked")
        print(sender.tag)
        print(passwordString)
        if let pswdTextField = self.view.viewWithTag((sender.superview?.superview!.tag)!) as? PasswordTextFieldView
        {
            if sender.currentImage == UIImage(named: "eye_hide")
            {
               // pswdTextField.passwordTextField.text = passwordString
                pswdTextField.passwordTextField.isSecureTextEntry = false;
                isSecuredText = false;
                sender.setImage(UIImage(named: "eye"), for: .normal)
            }
            else
            {
                sender.setImage(UIImage(named: "eye_hide"), for: .normal)
                pswdTextField.passwordTextField.text = String(repeating: "*", count: (pswdTextField.passwordTextField.text ?? "").count)
                pswdTextField.passwordTextField.isSecureTextEntry = true;
                isSecuredText = true;
            }
        }
    }
    
    //MARK:- Create Date Picker
    func addMobileNumber(specificData : [String:String])
    {
        let label = UILabel()
        if specificData["required"] == "1"
        {
            label.text = specificData["label"]! + "*"
        }else{
            label.text = specificData["label"]!
        }
        label.font = UIFont.systemFont(ofSize: 17, weight: UIFont.Weight.medium)
        label.textColor = .black
        signUpStackview.addArrangedSubview(label)
        signUpStackview.addConstraint(NSLayoutConstraint(item: label, attribute: .leading, relatedBy: .equal, toItem: signUpStackview, attribute: .leading, multiplier: 1, constant: 5))
        signUpStackview.addConstraint(NSLayoutConstraint(item: label, attribute: .trailing, relatedBy: .equal, toItem: signUpStackview, attribute: .trailing, multiplier: 1, constant: -5))
        signUpStackview.addConstraint(NSLayoutConstraint(item: label, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 25))
        signUpStackviewHeight.constant += 25
        
       // mobileNumber.delegate = self
        mobileNumber.setPhoneNumber("")
        mobileNumber.delegate = self
        mobileNumber.setDefaultCountryCode("eg")
        mobileNumber.layer.cornerRadius = 1
        mobileNumber.layer.borderColor = UIColor.lightGray.cgColor
        mobileNumber.layer.borderWidth = 1
        signUpStackview.addArrangedSubview(mobileNumber)
        Util.deactivateRTL(of: mobileNumber)
        mobileNumber.translatesAutoresizingMaskIntoConstraints = false;
        signUpStackview.addConstraint(NSLayoutConstraint(item: mobileNumber, attribute: .leading, relatedBy: .equal, toItem: signUpStackview, attribute: .leading, multiplier: 1, constant: 5))
        signUpStackview.addConstraint(NSLayoutConstraint(item: mobileNumber, attribute: .trailing, relatedBy: .equal, toItem: signUpStackview, attribute: .trailing, multiplier: 1, constant: -5))
        //signUpStackview.addConstraint(NSLayoutConstraint(item: mobileNumber, attribute: .top, relatedBy: .equal, toItem: signUpStackview, attribute: .bottom, multiplier: 1, constant: 10))
        signUpStackview.addConstraint(NSLayoutConstraint(item: mobileNumber, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 40))
        signUpStackviewHeight.constant += 40
    }
    
    
    
    //MARK:- Create Date Picker
    func DatePicker1(specificData: [String:String])
    {
        print("opening DatePicker")
        print("specific data = \(specificData)")
        self.dateBtn = UIButton()
        let selectDropDown = Label_DropDown_Combo_View()
        signUpStackview.addArrangedSubview(selectDropDown)
        if specificData["required"] == "1"
        {
            selectDropDown.entityNameLabel.text = specificData["label"]! + "*"
        }
        else
        {
            selectDropDown.entityNameLabel.text = specificData["label"]
        }
        selectDropDown.view.backgroundColor = .clear
        selectDropDown.entityNameLabel.textColor = .black
       // selectDropDown.downArrowImg.tintColor = .white
        selectDropDown.dropDownButton.setTitleColor(.white, for: .normal)
        selectDropDown.dropDownButton.layer.borderColor = UIColor.white.cgColor
        
       // selectDropDown.downArrowImg.tintColor = .black
        selectDropDown.dropDownButton.setTitleColor(.black, for: .normal)
        selectDropDown.dropDownButton.backgroundColor = .white
        selectDropDown.tag = Int(specificData["countNo"]!)!
        selectDropDown.dropDownButton.tag = Int(specificData["countNo"]!)!
        self.dateBtn = selectDropDown.dropDownButton
        self.dateBtn.tag = selectDropDown.dropDownButton.tag
        self.dateBtnArray.append(dateBtn)
        //selectDropDown.downArrowImg.isHidden = true;
        selectDropDown.dropDownButton.imageView?.isHidden = true;
        selectDropDown.dropDownButton.setTitle("", for: .normal)
        selectDropDown.dropDownButton.addTarget(self, action: #selector(dateclicked(sender:)), for: .touchUpInside)
        selectDropDown.translatesAutoresizingMaskIntoConstraints = false;
        signUpStackview.addConstraint(NSLayoutConstraint(item: selectDropDown, attribute: .leading, relatedBy: .equal, toItem: signUpStackview, attribute: .leading, multiplier: 1, constant: 0))
        signUpStackview.addConstraint(NSLayoutConstraint(item: selectDropDown, attribute: .trailing, relatedBy: .equal, toItem: signUpStackview, attribute: .trailing, multiplier: 1, constant: 0))
        //signUpStackview.addConstraint(NSLayoutConstraint(item: selectDropDown, attribute: .top, relatedBy: .equal, toItem: signUpStackview, attribute: .bottom, multiplier: 1, constant: 10))
        signUpStackview.addConstraint(NSLayoutConstraint(item: selectDropDown, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 70))
        signUpStackviewHeight.constant += 75
        
    }
 
    @objc func dateclicked(sender: UIButton)
    {
        print("dateclicked")
        print(sender.tag)
        IQKeyboardManager.shared().resignFirstResponder()
        let datePicker = ced_datepicker()
        datePicker.frame = CGRect(x: 0, y: self.view.frame.height - 300, width: signUpStackview.frame.width, height: 300)
        self.view.addSubview(datePicker)
        datePicker.date.datePickerMode = .date
        let currentDate = NSDate()
        let calendar = Calendar.init(identifier: .gregorian)
        let dateComponents = NSDateComponents()
        dateComponents.year = -30
        let minD = calendar.date(byAdding: dateComponents as DateComponents, to: currentDate as Date)
        datePicker.date.minimumDate = minD
        dateComponents.year = 30
        let maxDate = calendar.date(byAdding: dateComponents as DateComponents, to: currentDate as Date)
        datePicker.date.maximumDate = maxDate
        //datePicker.date.minimumDate = currentDate as Date
        
        //datePicker.date.minimumDate = Date()
        datePicker.tag = sender.tag
        datePicker.done.tag = sender.tag
        datePicker.done.addTarget(self, action: #selector(PickerDoneClicked(sender:)), for: .touchUpInside)
        
//        signUpStackview.addArrangedSubview(date)
//        date.translatesAutoresizingMaskIntoConstraints = false
//        signUpStackview.addConstraint(NSLayoutConstraint(item: date, attribute: .leading, relatedBy: .equal, toItem: signUpStackview, attribute: .leading, multiplier: 1, constant: 0))
//        signUpStackview.addConstraint(NSLayoutConstraint(item: date, attribute: .trailing, relatedBy: .equal, toItem: signUpStackview, attribute: .trailing, multiplier: 1, constant: 0))
//        signUpStackview.addConstraint(NSLayoutConstraint(item: date, attribute: .bottom, relatedBy: .equal, toItem: signUpStackview, attribute: .bottom, multiplier: 1, constant: 10))
//        signUpStackview.addConstraint(NSLayoutConstraint(item: date, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 300))
        
    }
    
    @objc func PickerDoneClicked(sender: UIDatePicker)
    {
        print("PickerDoneClicked")
        print(sender.tag)
        if let outerView = sender.superview?.superview as? ced_datepicker
        {
            if let view = self.view.viewWithTag(sender.tag) as? Label_DropDown_Combo_View
            {
                print(view)
                print("-- date selected---")
                print(outerView.date.date)
                let formatter = DateFormatter()
                formatter.locale = Locale(identifier: "en_US_POSIX")
                formatter.dateFormat = "dd-MM-yyyy"
                print(formatter.string(from: outerView.date.date))
                view.dropDownButton.setTitle(formatter.string(from: outerView.date.date), for: .normal)
            }
            outerView.removeFromSuperview()
        }
    }
    
    
    @objc func registerButtonClicked(sender: UIButton)
    {
        print("registerButtonClicked")
        var params = [String:String]()
        var parameters = [[String:String]]()
        var email = ""
        var password = ""
        var cpassword = ""
        var name = ""
        var checkIsSubscribed = ""

        for fields in signUpfields
        {
            print("fields = \(fields)")
            switch fields["type"]
            {
                case "text":
                    print("in case of textFields check")
                    if fields["required"] == "1"
                    {
                        if let textdata = self.view.viewWithTag(Int(fields["countNo"]!)!) as? Label_TextFieldComboView
                        {
                            let textBoxValue = textdata.textLabel.text
                            if textBoxValue == "" || cpasswordString.isEmpty
                            {
                                self.view.makeToast("Please enter some text in required fields".localized, duration: 2.0, position: .center)
                                return;
                            }
                            else
                            {
                                switch fields["postLabel"]
                                {
                                    case "email":
                                        
                                    if !self.isValidEmail(testStr: textBoxValue!.trimmingCharacters(in: .whitespaces))
                                        {
                                            self.view.makeToast("Invalid Email".localized, duration: 2.0, position: .center)
                                            return
                                        }
                                        else
                                        {
                                            email = textBoxValue!.trimmingCharacters(in: .whitespaces);
                                            //params[fields["postLabel"]!] = textBoxValue;
                                            //parameters.append(["key":fields["postLabel"]!,"value" : textBoxValue!])
                                        }
                                    break
                                case "name":
//                                    if !self.isValidName(testStr: textBoxValue!)
//                                    {
//                                        self.view.makeToast("Invalid Full Name.", duration: 2.0, position: .center)
//                                        return
//                                    }
//                                    else
//                                    {
//                                        name = textBoxValue!;
//                                        //params[fields["postLabel"]!] = textBoxValue;
//                                        //parameters.append(["key":fields["postLabel"]!,"value" : textBoxValue!])
//                                    }
                                    name = textBoxValue!;
                                    break
                                    case "public_name", "company_name":
//                                        if !self.isValidName(testStr: textBoxValue!)
//                                        {
//                                            self.view.makeToast("Invalid Name(s) Entered.", duration: 2.0, position: .center)
//                                            return
//                                        }
//                                        else
//                                        {
                                            params[fields["postLabel"]!] = textBoxValue;
                                            parameters.append(["key":fields["postLabel"]!,"value" : textBoxValue!,"type":"text"])
//                                        }
                                    break
                                    case "password":
                                        //if !fromSocialLogin{
                                        if textBoxValue!.count < 8
                                        {
                                            self.view.makeToast("your_password_must_be_at_least_8_characters_with_at_least_1_number_and_1_special_character".localized, duration: 2.0, position: .center)
                                            return;
                                        }
                                        else
                                        {
                                            password = textBoxValue!
                                            //params[fields["postLabel"]!] = textBoxValue;
                                            //parameters.append(["key":fields["postLabel"]!,"value" : textBoxValue!])
                                        }
                                    //}
                                    break
                                case "confirm_password":
                                    //if !fromSocialLogin{
                                    if textBoxValue!.count < 8
                                    {
                                        self.view.makeToast("your_password_must_be_at_least_8_characters_with_at_least_1_number_and_1_special_character".localized, duration: 2.0, position: .center)
                                        return;
                                    }
                                    else
                                    {
                                        cpassword = textBoxValue!
                                        //params[fields["postLabel"]!] = textBoxValue;
                                        //parameters.append(["key":fields["postLabel"]!,"value" : textBoxValue!])
                                    }
                                    break
                                    case "shop_url":    // -----------regular expression --------
//                                        let shopUrlCharacterset = CharacterSet(charactersIn: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-")
//                                        if textBoxValue!.rangeOfCharacter(from: shopUrlCharacterset.inverted) != nil {
//                                            self.view.makeToast("Please enter only valid characters [a-z][A-Z] in shop url. Example: my-shop-url", duration: 2.0, position: .center)
//                                            //self.view.showToastMsg(NSLocalizedString("Please enter only valid characters [a-z][A-Z] in shop url", comment: ""))
//                                            return
//                                        }
                                      
                                    if !self.isValidShopUrl(testStr: textBoxValue!)
                                 {
                                        self.view.makeToast("Enter a valid Shop Url. Example: my-shop-url".localized, duration: 2.0, position: .center)
                                  return;
                                }
                                  else
                                 {
                                    params[fields["postLabel"]!] = textBoxValue;
                                    parameters.append(["key":fields["postLabel"]!,"value" : textBoxValue!,"type":"text"])
                                    }
                                    break
                                    case "contact_number":
                                        if !isValidNumber(Number: textBoxValue!)
                                        {
                                            self.view.makeToast("Enter a valid contact number.", duration: 2.0, position: .center)
                                            return;
                                        }
                                        else
                                        {
                                            params[fields["postLabel"]!] = textBoxValue;
                                            parameters.append(["key":fields["postLabel"]!,"value" : textBoxValue!,"type":"text"])
                                        }
                                      break
                                case "country_id":
                                    if !isValidNumber(Number: textBoxValue!)
                                    {
                                        self.view.makeToast("Enter a valid contact number.", duration: 2.0, position: .center)
                                        return;
                                    }
                                    else
                                    {
                                        params[fields["postLabel"]!] = countryKey;
                                        parameters.append(["key":fields["postLabel"]!,"value" : countryKey,"type":"text"])
                                    }
                                  break
                                    /*case "Zip/Postal Code ":
                                        if !self.isValidZipCode(postalCode: textBoxValue!)  //---------- zip code ---
                                        {
                                            self.view.makeToast("Invalid Zip/ Postal Code Entered.", duration: 2.0, position: .center)
                                            //return
                                        }
                                    case "Account Number":
                                        if !self.isValidAccountNumber(Number: textBoxValue!)
                                        {
                                            self.view.makeToast("Invalid Account Number Entered.", duration: 2.0, position: .center)
                                            //return
                                        }
                                    case "IBAN":
                                        if !self.isValidIBAN(Number: textBoxValue!)
                                        {
                                            self.view.makeToast("Invalid IBAN Entered.", duration: 2.0, position: .center)
                                            //return
                                        }
                                    case "City", "State", "Pick Up Store Name", "If you sell your products online, enter website URL (Optional)", "Pick up Address":
                                        if !self.isValidName(testStr: textBoxValue!)
                                        {
                                            
                                        }*/
                                    default:
                                        print("default")
                                        params[fields["postLabel"]!] = textBoxValue;
                                        parameters.append(["key":fields["postLabel"]!,"value" : textBoxValue!,"type":"text"])
                                }
                                
                            }
                        }
                    }
                case "select":
                    print("in case of select check")
                        if let dropDownData = self.view.viewWithTag(Int(fields["countNo"]!)!) as? Label_DropDown_Combo_View
                        {
                            var dropDownDataValue = ""
                            if(dropDownData.dropDownButton.isHidden){
                                dropDownDataValue = dropDownData.textLabel.text ?? ""
                            }else{
                                dropDownDataValue = dropDownData.dropDownButton.titleLabel?.text ?? ""
                            }
                            if dropDownDataValue == ""
                            {
                                //let headingName = fields["label"]
                                if fields["required"] == "1"
                                {
                                    self.view.makeToast("Please select a category from the dropdown.", duration: 2.0, position: .center)
                                    return;
                                }
                            }
                            else
                            {
                                if fields["postLabel"] == "country_id"
                                {
                                    params[fields["postLabel"]!] = countryKey;
                                    parameters.append(["key":fields["postLabel"]!,"value" : countryKey,"type":"text"])
                                }else{
                                    params[fields["postLabel"]!] = dropDownDataValue;
                                    parameters.append(["key":fields["postLabel"]!,"value" : dropDownDataValue,"type":"text"])
                                }
                            }
                        }
                case "checkbox":
                    print("in case of checkbox check")
                    if fields["required"] == "1"
                    {
                        if let checkboxdata = self.view.viewWithTag(Int(fields["countNo"]!)!) as? CheckboxButtonView
                        {
                            let checkboxdatastate = checkboxdata.checkboxButton.currentImage
                            let checkboxdataValue = "1"
                            if checkboxdatastate == UIImage(named: "unchecked")
                            {
                                self.view.makeToast("Please check the checkbox".localized, duration: 2.0, position: .center)
                                return;
                            }
                            else
                            {
                                checkIsSubscribed = checkboxdataValue;
                                //params[fields["postLabel"]!] = checkboxdataValue;
                                //parameters.append(["key":fields["postLabel"]!,"value" : checkboxdataValue])
                            }
                        }
                    }
                    
                case "password":
                    print("in case of password check")
                   // if !fromSocialLogin{
                    if fields["required"] == "1"
                    {
                        if let pswddata = self.view.viewWithTag(Int(fields["countNo"]!)!) as? PasswordTextFieldView
                        {
                            let pswddataValue = pswddata.passwordTextField.text
                            if pswddataValue == ""
                            {
                                self.view.makeToast("Please enter a valid password.", duration: 2.0, position: .center)
                                return;
                            }
                            else if pswddataValue!.count < 8
                            {
                                self.view.makeToast("your_password_must_be_at_least_8_characters_with_at_least_1_number_and_1_special_character".localized, duration: 2.0, position: .center)
                                return;
                            }
                            else
                            {
                                password = pswddataValue!
                                //params[fields["postLabel"]!] = pswddataValue;
                                //parameters.append(["key":fields["postLabel"]!,"value" : pswddataValue!])
                            }
                        }
                 //   }
                }
                case "image":
                    print("in case of image check")
                    if fields["required"] == "1"
                    {
                        if let docdata = self.view.viewWithTag(Int(fields["countNo"]!)!) as? browseImageView
                        {
                            if(!docdata.base64EncodedStringData.isEmpty){
                                let imageData = docdata.browseImage.image?.jpegData(compressionQuality: 0.2)!
                                if(imageData != nil){
                                    let dateFormatter: DateFormatter = DateFormatter()
                                    dateFormatter.locale = Locale(identifier: "en_US_POSIX")
                                    dateFormatter.dateFormat = "yyyyMMddHHmmssSSS"
                                    let selectedDate: String = "\(dateFormatter.string(from: Date()))\(Int(fields["countNo"]!) ?? 0)"
                                    let onlyExtension = imageData!.format
                                    let ext = onlyExtension.replacingOccurrences(of: ".", with: "")
                                    let base64_encoded_data=(imageData!.base64EncodedString())
                                    var logo = [String: Any]()
                                    logo["type"] = "file"
                                    logo["name"] = "name\(selectedDate)\(imageData!.format)"
                                    logo["base64_encoded_data"] = base64_encoded_data
                                    if let logoData = try? JSONSerialization.data(withJSONObject: logo, options: []),
                                               let logoString = String(data: logoData, encoding: .utf8) {
                                        parameters.append(["key":fields["postLabel"]!,"value" : logoString])
                                    }
                                }
                            }else{
                                let docdataValue = docdata.fileAttachedLabel.text
                                if docdataValue == ""
                                {
                                    self.view.makeToast("Please select a valid document.", duration: 2.0, position: .center)
                                    return;
                                }
                                else
                                {
                                    params[fields["postLabel"]!] = docdataValue;
                                    parameters.append(["key":fields["postLabel"]!,"value" : docdataValue!])
                                }
                            }
                        }
                    }else{
                        if let docdata = self.view.viewWithTag(Int(fields["countNo"]!)!) as? browseImageView
                        {
                            if(!docdata.base64EncodedStringData.isEmpty){
                                let imageData = docdata.browseImage.image?.jpegData(compressionQuality: 0.2)!
                                if(imageData != nil){
                                    let dateFormatter: DateFormatter = DateFormatter()
                                    dateFormatter.locale = Locale(identifier: "en_US_POSIX")
                                    dateFormatter.dateFormat = "yyyyMMddHHmmssSSS"
                                    let selectedDate: String = "\(dateFormatter.string(from: Date()))\(Int(fields["countNo"]!) ?? 0)"
                                    let onlyExtension = imageData!.format
                                    let ext = onlyExtension.replacingOccurrences(of: ".", with: "")
                                    let base64_encoded_data=(imageData!.base64EncodedString())
                                    var logo = [String: Any]()
                                    logo["type"] = "file"
                                    logo["name"] = "name\(selectedDate)\(imageData!.format)"
                                    logo["base64_encoded_data"] = base64_encoded_data
                                    if let logoData = try? JSONSerialization.data(withJSONObject: logo, options: []),
                                               let logoString = String(data: logoData, encoding: .utf8) {
                                        parameters.append(["key":fields["postLabel"]!,"value" : logoString])
                                    }
                                }
                            }
                        }
                    }
                case "date":
                    print("in case of date check")
                    
                        if let dropDownData = self.view.viewWithTag(Int(fields["countNo"]!)!) as? Label_DropDown_Combo_View
                        {
                            var dropDownDataValue:String?
                            if( dropDownData.dropDownButton.isHidden){
                                dropDownDataValue = dropDownData.textLabel.text
                            }else{
                                dropDownDataValue = dropDownData.dropDownButton.titleLabel?.text
                            }
                            if fields["required"] == "1"
                            {
                                if dropDownDataValue == "" || dropDownDataValue == nil
                                {
                                    let headingName = fields["label"]
                                    self.view.makeToast("Please enter some text in \(headingName) field.", duration: 2.0, position: .center)
                                    return;
                                }
                                else
                                {
                                    params[fields["postLabel"]!] = dropDownDataValue;
                                    parameters.append(["key":fields["postLabel"]!,"value" : dropDownDataValue!,"type":"text"])
                                }
                            }else{
                                params[fields["postLabel"]!] = dropDownDataValue;
                                parameters.append(["key":fields["postLabel"]!,"value" : dropDownDataValue ?? "","type":"text"])
                        }
                    }
                   // if fromSocialLogin{
                       //  parameters.append(["key":"multistep_done","value" : "1","type":"text"])
               // }
                default:
                    print("my default case")
            }
        }
        
//        if(self.step == "1"){
//            parameters.append(["key":"shop_url","value" :"my-shop-url\(randomNumber(digits: 10))","type":"text"])
//            parameters.append(["key":"public_name","value" :"test public name","type":"text"])
//        }
                if  password != cpasswordString{
                    self.view.makeToast("Password not matched!".localized, duration: 2.0, position: .center)
                    return;
                }
        
        if(self.step == "1"){
            let postdata = ["is_subscribed":checkIsSubscribed,"password":password,"name":name,"email":email] as [String : Any]
            let phoneNumber = mobileNumber.getNationalPhoneNumber()
            if(!isValidFullName(name)){
                self.view.makeToast("The full name is incorrect".localized, duration: 2.0, position: .center)
                return
            }else if (phoneNumber.isEmpty){
               self.view.makeToast("Please enter some text in required fields".localized, duration: 2.0, position: .center)
               return
           }else if (phoneNumber.first == "0"){
                self.view.makeToast("Please enter the number without 0".localized, duration: 2.0, position: .center)
                return
            }else if (phoneNumber.count != 10){
                self.view.makeToast("Please enter the valid number".localized, duration: 2.0, position: .center)
                return
            }
            self.sendOTP(toNumber: mobileNumber.getPhoneNumber(), param: (postdata),vendormparam: parameters, isResend: false)
        }else{
            parameters.append(["key":"multistep_done","value" : "1","type":"text"])
            let postdata = [String : Any]()
            if(self.allImages.count == 0 && self.allVideo.count == 0 && self.alldocuments.count == 0){
                
                self.registerUser(with: postdata, vendormparam: parameters, num: "")
            }else{
                self.uploadPdfFile(with: postdata, vendormparam: parameters)
//                if let data = NSData(contentsOfFile: self.filePath) {
//
//                }else{
//                    Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
//                    self.registerUser(with: postdata, vendormparam: parameters, num: "")
//                }
            }
        }
       
        print(cpasswordString)
        print(passwordString)

     //   let postdata = ["lastname":lastName,"is_subscribed":checkIsSubscribed,"firstname":firstName,"password":passwordString,"email":email,"vendor": parameters] as [String : Any]
        
        print("params = \(params)")
        print("parameters = \(parameters)")
        return
    //        registerUser(with: postString!)
    //--edited    var baseUrl = Ced_CommonVendor.getInfoPlist("baseUrl") as? String
        
        //MARK: - add mobile number popup
       
        /*
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
            var phoneNumber = self.mobilePopup.verifyphoneNumberView.getPhoneNumber()
            if phoneNumber == ""{
                self.view.makeToast("Enter mobile number")
                return
            }
            if(phoneNumber.contains("+")){
                phoneNumber = phoneNumber.replacingOccurrences(of: "+", with: "")
            }
            if self.otpEnabled{
                bgView.removeFromSuperview()
                self.sendOTP(toNumber: phoneNumber, param: (postdata),isResend: false)
//                self.verify(number: mobile) { [weak self] success in
//                    if success {
//                        bgView.removeFromSuperview()
//                        self?.sendOTP(toNumber: mobile, param: (postdata),isResend: false)
//                    }
//                }
            }else{
                self.registerUser(with: postdata,num: phoneNumber)
            }
        }.disposed(by: disposeBag)
         */
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
    
    private func sendOTP(toNumber number: String,param:[String:Any],vendormparam:[[String:String]],isResend:Bool ) {
        otpText = randomNumber(digits: 4)
        let parameters :[String:Any] = ["type": "text", "text": otpText]
        let bodycomponents :[String:Any] = ["type": "body", "parameters": [parameters]]
        let buttoncomponents :[String:Any] = ["type": "button","sub_type": "url","index": "0", "parameters": [parameters]]
        let language :[String:Any] = ["policy": "deterministic", "code": "ar"]
        let template :[String:Any] = ["language": language,"name": "otp_new", "components": [bodycomponents,buttoncomponents]]
        let mainBody :[String:Any] = ["messaging_product": "whatsapp","recipient_type": "individual","to": number,"type": "template", "template": template]
       // let countryID: String = Locale.current.regionCode ?? ""
        //vsignupapi/sendOtp old end point
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
                    let popup  = AuthenticationView(otp: self?.otpText ?? "1234")
                    guard let window = UIApplication.shared.keyWindow else { return }
                    window.addSubview(popup)
                    popup.frame = window.bounds
                    
                    // Handle the success state of OTP validation
                    popup.onSuccess = { otp in
                        self?.registerUser(with: param,vendormparam: vendormparam,num: number)
                        // self?.verifyOTP(on: number, otp: otp, postData: param)
                    }
                    
                    // Handle the failure state of OTP validation
                    popup.onFailure = { message in
                        self?.view.makeToast(message)
                        return
                    }
                    
                    // Handle the resend OTP Action
                    popup.onResend = {
                        self?.sendOTP(toNumber: number, param: param,vendormparam:vendormparam,isResend: true)
                        popup.otpCode = self?.otpText ?? "1234"
                    }
                }
            }
        }
    }
    
    private func verifyOTP(on number: String, otp: String,postData:[String:Any]) {
        let param = ["mobile": number, "country_id": "IN", "otp": otp]
        ApiHandler.handle.request(with: "vsignupapi/verifyOtp", params: param, requestType: .POST, controller: self) { [weak self] data, _ in
            guard let data = data else { return }
            guard let json = try? JSON(data: data) else { return }
            print(json)
            
            self?.view.makeToast(json[0]["data"]["customer"][0]["message"].stringValue)
            // if OTP is successfully verified
            if json[0]["data"]["customer"][0]["status"].boolValue  {
               // self?.registerUser(with: postData,num: number)
            }
        }
    }
    
    func registerUser(with postdata:[String:Any],vendormparam:[[String:String]],num:String){
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        var baseUrl = settings.baseUrl
        var dataPosted = String()
        var vendormparamFinal = vendormparam
        if(self.step == "1"){
            var numberWithoutSpace = num.replacingOccurrences(of: " ", with: "")
            numberWithoutSpace = numberWithoutSpace.replacingOccurrences(of: "+", with: "")
            vendormparamFinal.append(["key":"contact_number","value" :numberWithoutSpace,"type":"text"])
        }else if(self.step == "2" && !num.isEmpty){
            vendormparamFinal.append(["key":"product_sample","value" :num,"type":"text"])
        }
        var dataToPostFinal = postdata
       // dataToPostFinal["contact_number"] = "923003377181"//num //mobile_number
        dataToPostFinal["vendor"] = vendormparamFinal
        
        do{
            let theJSONData = try JSONSerialization.data(
                withJSONObject: dataToPostFinal ,
                options: JSONSerialization.WritingOptions(rawValue: 0))
            postString = NSString(data: theJSONData,
                                  encoding: String.Encoding.utf8.rawValue)! as String
            
        }
        catch{
            print("error in data encoding")
        }
        
        guard let postString = postString else {
            return
        }
        
        
        if !fromSocialLogin{
            if(self.step == "2"){
                baseUrl += "vmultistepregapi/index/save?vendor_id=\(vendor_id)&hashkey=\(hashKey)"
            }else{
                baseUrl += "vmultistepregapi/index/create"
            }
            dataPosted = "createaccount=" + postString
            print("create account api: ")
                  
        }else{
            baseUrl += "vendorapi/index/approvalPost"
            dataPosted =  "approveaccount=" + postString +  "&customer_id=" + socialLoginData["customer_id"]!
               print("create approve api: ")
        }
      print(baseUrl)
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
            
            print("create account json:")
            print(NSString(data: data!, encoding: String.Encoding.utf8.rawValue))
            guard let data = data else {return}
            guard let json1 = try? JSON(data: data) else {return;}
            print(json1)
           
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
            guard let json = try? JSON(data: data) else {return}
            DispatchQueue.main.async
                {
                    
                    print(NSString(data: data, encoding: String.Encoding.utf8.rawValue))
                    print(json)
                    if(json["data"]["customer"][0]["success"].stringValue.lowercased() == "true"){
                        if(self.step == "2" && self.loginJson != nil){
                            let multistep_done = self.loginJson!["data"]["customer"][0]["multistep_done"].stringValue;
                            let customer_id = self.loginJson!["data"]["customer"][0]["customer_id"].stringValue;
                            let hashKey = self.loginJson!["data"]["customer"][0]["hashkey"].stringValue;
                            let vendor_id = self.loginJson!["data"]["customer"][0]["vendor_id"].stringValue;
                            let vendor_name = self.loginJson!["data"]["customer"][0]["vendor_name"].stringValue;
                            let profile_picture = self.loginJson!["data"]["customer"][0]["profile_picture"].stringValue;
                            let valerts = self.loginJson!["data"]["customer"][0]["valerts"].stringValue
                            let profile_complete = self.loginJson!["data"]["customer"][0]["profile_complete"].stringValue
                            let dict = ["customerId": customer_id, "hashKey": hashKey,"vendorName":vendor_name,"vendorId":vendor_id,"profilePicUrl":profile_picture,"valerts":valerts,"profile_complete":profile_complete]
                            self.defaults.set(true, forKey: "isLogin")
                            self.defaults.set(dict, forKey: "userInfoDict");
                            //print(self.defaults.dictionary(forKey: "userInfoDict"))
                            self.view.showToastMsg(NSLocalizedString("Login Successful", comment: ""))
                            (UIApplication.shared.delegate as! AppDelegate).changeModules()
                        }else if(json["data"]["customer"][0]["isConfirmationRequired"].stringValue.lowercased() == "NO" ) {
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
                            Alert_File.showOkAlert(self, "Message".localized, json["data"]["customer"][0]["message"].stringValue) { index in
                                self.modalPresentationStyle = .fullScreen
                                self.navigationController?.popToRootViewController(animated: true)
                            }
//                            self.view.showToastMsg(json["data"]["customer"][0]["message"].stringValue + "\n Your account is under admin review")
//                            Ced_CommonVendor.delay(4, closure: {
//                                
//                            })
                        }
                    }else{
                        self.view.showToastMsg(json["data"]["customer"][0]["message"].stringValue)
                        return;
//                        Ced_CommonVendor.delay(4, closure: {
//                            self.modalPresentationStyle = .fullScreen
//                            self.navigationController?.popToRootViewController(animated: true)
//                        })
                    }
                    
            }
        })
        task.resume()
    }
    
}
//MARK: - UIGesture
extension ced_vendorRegistration:UIGestureRecognizerDelegate{
    
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
    
    func isValidFullName(_ name: String) -> Bool {
//        let regex = "^[\\p{L}\\p{M}]+(?: [\\p{L}\\p{M}]+)$"
//        return NSPredicate(format: "SELF MATCHES %@", regex).evaluate(with: name)
        // quick nil/empty check
           let raw = name.trimmingCharacters(in: .whitespacesAndNewlines)
           if raw.isEmpty { return false }

           // normalize & clean common weird characters
           var s = (raw as NSString).precomposedStringWithCanonicalMapping
           s = s.replacingOccurrences(of: "\u{00A0}", with: " ") // NBSP -> space
           s = s.replacingOccurrences(of: "\u{200C}", with: "")  // ZWNJ remove (optional)
           s = s.replacingOccurrences(of: "\u{200D}", with: "")  // ZWJ remove (optional)

           // collapse all consecutive whitespace into single spaces and split
           let comps = s.components(separatedBy: .whitespacesAndNewlines).filter { !$0.isEmpty }
           guard comps.count == 2 else { return false }

           // each word must be only letters + combining marks
           let wordPattern = #"^[\p{L}\p{M}]+$"#
           for w in comps {
               if !NSPredicate(format: "SELF MATCHES %@", wordPattern).evaluate(with: w) {
                   return false
               }
           }
           return true
    }
}

extension ced_vendorRegistration: JNPhoneNumberViewDelegate {
    
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

extension UIStackView {
    
    func removeAllArrangedSubviews() {
        
        let removedSubviews = arrangedSubviews.reduce([]) { (allSubviews, subview) -> [UIView] in
            self.removeArrangedSubview(subview)
            return allSubviews + [subview]
        }
        
        // Deactivate all constraints
        NSLayoutConstraint.deactivate(removedSubviews.flatMap({ $0.constraints }))
        
        // Remove the views from self
        removedSubviews.forEach({ $0.removeFromSuperview() })
    }
}

//https://brassandwood.org/en/vmultistepregapi/index/save?vendor_id={{vendor_id}}&hashkey={{vtoken}}
//https://brassandwood.org/en/vmultistepregapi/index/registrationFields
//https://locafy.market/eg-en/vmultistepregapi/index/registrationFields
