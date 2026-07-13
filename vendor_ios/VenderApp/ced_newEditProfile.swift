//
//  ced_newEditProfile.swift
//  VenderApp
//
//  Created by Macmini on 18/12/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit


class EditProfileViewController: ced_VendorBaseClass,UIGestureRecognizerDelegate, UITextFieldDelegate {
    
var stateButtonIndex = Int()
    var businessStateArray = [String:String]()
    var businessCityArray = [String:String]()
    var bankStateArray = [String:String]()
    var bankCityArray = [String:String]()
    var addressStateArray = [String:String]()
    var addressCountryArray = [String:String]();
    var ImagePicker: UIImagePickerController!
    var businessStateButton: UIButton!;
    var businessCityButton: UIButton!;
    var bankStateButton: UIButton!;
    var bankCityButton: UIButton!;
    var addressCountryButton: UIButton!;
    var addressStateButton: UIButton!;
    let mobileNumber = JNPhoneNumberView()
    
    @IBOutlet weak var stackView: UIStackView!
    @IBOutlet weak var stackViewHeight: NSLayoutConstraint!
    @IBOutlet weak var stackViewWidth: NSLayoutConstraint!
    
    @IBOutlet weak var savebuttonPressed: UIButton!
    
    @IBOutlet weak var editProfileButton: UIButton!
    
    var multiSelect = [String:[String]]()
    var imagViewArray = [String:UIImageView]()
    var fileData = [String:String]()
    var currentTagForImageToUpload = Int()
    var selectedBrowseView: Label_ImageView_Combo_View!
    var passwordCheck = false
    var is_shop_url_done = false
    var passwordView: PasswordView!;
    var heightConstrain1: NSLayoutConstraint!;
    var selectedBankState = "";
    var oldMobileNumber = "";
    var otpText = ""
    var selectedBusinessState = "";
    var stateDropdownButton: UIButton?
    var countryDropdownButton: UIButton?
    var dataForOption = [String]()
                           var dropOption = [String:String]()
    var countryArray = [String:String]();
    var statesArray = [String:String]();
    struct editProFileData {
        var field_name = String()
        var saved_value = String()
        var type = String()
        var is_required = String()
        var field_to_post = String()
        var options = [String]()
        var input_type = String()
        var dropdownOptions = [String:String]()
        var location = String()
    }

     var currentLocation = [String:String]()
    
    var fullEditProfileData = [editProFileData]()
    var placeIdArray = [String:String]()
      let dropDown = DropDown();
    
    override func viewDidLoad() {
        super.viewDidLoad()
        ImagePicker = UIImagePickerController()
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        fetchCountryListing()
        let postString = "vendor_id="+vendorId+"&hashkey="+hashKey;
        sendRequest(url: "vendorapi/index/getProfileFields", parameters: postString)
        stackViewHeight.constant = 0
        savebuttonPressed.setThemeColor()
        editProfileButton.setTitle("Edit Profile".localized, for: .normal)
        savebuttonPressed.setTitle("Save".localized, for: .normal)
        savebuttonPressed.addTarget(self, action: #selector(saveButtonPressed(_:)), for: .touchUpInside)
       // addressCountryButton.addTarget(self, action: #selector(fetchCountryListing), for: .touchUpInside)
       
    }
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        stackViewWidth.constant = view.bounds.width
//        stackViewHeight.constant = 0
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            Alert_File.removeLoadingIndicator(self);
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if requestUrl=="vendorapi/index/getcountry/"
                    {
                       guard let json = try? JSON(data: data) else {return}
                        print("jsionsjisn=\(json)")
                        Alert_File.removeLoadingIndicator(self);
                        let countryListing = json["country"].arrayValue;
                        for d in 0..<countryListing.count
                        {
                            let key = countryListing[d]["value"].stringValue;
                            
                            let value = countryListing[d]["label"].stringValue;
                            
                            self.countryArray[value] = key;
                            
                            print(self.countryArray);
                        }
                        for (i,v) in countryArray {
                            print(self.countryArray[v])
                            print(self.self.countryArray[i])
                         //self.makeDropDown(set: self.countryArray[i] ?? "", textFeildText: self.countryArray[i] ?? "", index: 0)
                        }
                        print("defrfw34r342")
                        //                self.citylisting()
                        //self.editProfileTableView.reloadData();
                    }
          else  if requestUrl=="vendorapi/index/update"{
                if json["data"]["success"].stringValue == "true"{
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                    Ced_CommonVendor.delay(2.0) {
                        self.navigationController?.popToRootViewController(animated: true)
//                        let storybordOrder = UIStoryboard(name: "Main", bundle: nil)
//                        let viewControl = storybordOrder.instantiateViewController(withIdentifier: "vendorProfileViewController") as! VendorProfileViewController
//                        self.navigationController?.setViewControllers([viewControl], animated: true);
                    }
                }
                else
                {
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                }
            }else{
                if json["data"]["success"].stringValue == "true"{
                   
                    for(key,val) in json["data"]["vendor_attributes"]
                    {
                        let tempHeader = editProFileData(field_name: key, saved_value: key, type: "labels", is_required: "0", field_to_post: "labels", options: [], input_type: "String", dropdownOptions: [:])
                            fullEditProfileData.append(tempHeader)
                        for items in val.arrayValue{
                            
                            
                            if items["field_name"].stringValue == "Neighborhood"{
                                print(items)
                            }
                            if(items["field_to_post"].stringValue == "shop_url_done"){
                                if(items["saved_value"].stringValue == "1"){
                                    self.is_shop_url_done = true
                                }
                            }
                            var tempData = editProFileData(field_name: items["field_name"].stringValue, saved_value: items["saved_value"].stringValue, type: items["type"].stringValue, is_required: items["is_required"].stringValue, field_to_post: items["field_to_post"].stringValue, options: [], input_type: items["input_type"].stringValue, dropdownOptions: [:])
                            
                            
                            dropOption.removeAll()
                            if(items["type"].stringValue == "multiselect")
                            {
                                for value in items["options"].arrayValue{
                                    
                                    dataForOption.append(value["value"].stringValue)
                                }
                            }
                            else if(items["type"].stringValue == "select")
                            {
                                for value in items["options"].arrayValue{
                                    dropOption[value["label"].stringValue] = value["value"].stringValue;
                                }
                            }
                            tempData.dropdownOptions = dropOption;
                            print(dropOption)
                            tempData.options = dataForOption
                            fullEditProfileData.append(tempData)
                        }
                        
                    }
                    makeEditProfileView()
                    makePasswordCheckbox();
                    
                }else{
                    view.showToastMsg("Please Try Again".localized)
                }
            }
        }
    }
    
    private func sendOTP(toNumber number: String,parameter: String,isResend:Bool ) {
        otpText = randomNumber(digits: 4)
        let parameters :[String:Any] = ["type": "text", "text": otpText]
        let bodycomponents :[String:Any] = ["type": "body", "parameters": [parameters]]
        let buttoncomponents :[String:Any] = ["type": "button","sub_type": "url","index": "0", "parameters": [parameters]]
        let language :[String:Any] = ["policy": "deterministic", "code": "ar"]
        let template :[String:Any] = ["language": language,"name": "otp_new", "components": [bodycomponents,buttoncomponents]]
        let mainBody :[String:Any] = ["messaging_product": "whatsapp","recipient_type": "individual","to": number,"type": "template", "template": template]
        let countryID: String = Locale.current.regionCode ?? ""
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
                        self?.sendRequest(url: "vendorapi/index/update", parameters: parameter)
                        // self?.verifyOTP(on: number, otp: otp, postData: param)
                    }
                    
                    // Handle the failure state of OTP validation
                    popup.onFailure = { message in
                        self?.view.makeToast(message)
                        return
                    }
                    
                    // Handle the resend OTP Action
                    popup.onResend = {
                        self?.sendOTP(toNumber: number,parameter: parameter,isResend: true)
                        popup.otpCode = self?.otpText ?? "1234"
                    }
                }
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
    
    @objc func fetchCountryListing()
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String

        var baseURL = settings.baseUrl
        //baseURL = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
        baseURL = "vendorapi/index/getcountry/";
        
        print(baseURL);
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        //var request = URLRequest(url: URL(string: baseURL)!);
        
        //request.httpMethod = "POST";
        let postString = "vendor_id="+vendorId+"&hashkey="+hashKey;
        self.sendRequest(url: baseURL, parameters: postString)
    }
    @objc func makeEditProfileView(){
        
        for items in fullEditProfileData.indices{
            if(fullEditProfileData[items].field_to_post == "shop_url_done"){
                continue
            }
            switch fullEditProfileData[items].type {
            case "labels":
                makeHeaderLabel(itemsValue: fullEditProfileData[items].saved_value);
            case "label":
                makeLabel(set: fullEditProfileData[items].field_name, textFeildText: fullEditProfileData[items].saved_value, index: items)
            case "text":
                if(fullEditProfileData[items].field_to_post == "contact_number"){
                    self.addMobileNumber(set: fullEditProfileData[items].field_name, textFeildText: fullEditProfileData[items].saved_value, index: items, inputType: fullEditProfileData[items].input_type)
                }else{
                    makeTextFeild(set: fullEditProfileData[items].field_name,field_to_post: fullEditProfileData[items].field_to_post, textFeildText: fullEditProfileData[items].saved_value, index: items, inputType: fullEditProfileData[items].input_type)
                }
               
            case "select":
                if fullEditProfileData[items].field_name == "State" {
                    print("asdfg\(fullEditProfileData[items])")
                    var savedText = fullEditProfileData[items].saved_value
                    for (key,val) in fullEditProfileData[items].dropdownOptions{
                        if val == fullEditProfileData[items].saved_value{
                            savedText = key
                            print("savedText\(savedText)")
                        }
                    }
                    makeTextFeild(set: fullEditProfileData[items].field_name,field_to_post: fullEditProfileData[items].field_to_post, textFeildText: savedText, index: items, inputType: fullEditProfileData[items].input_type, fieldName: "State")
                } else {
                    makeDropDown(set: fullEditProfileData[items].field_name, textFeildText: fullEditProfileData[items].saved_value, index: items)
                }
                
            case "datetime":
                makeDatePickeView(set: fullEditProfileData[items].field_name, textFeildText: fullEditProfileData[items].saved_value, index: items)
            case "type":
                makePassword(set: fullEditProfileData[items].field_name, textFeildText: fullEditProfileData[items].saved_value, index: items, inputType: fullEditProfileData[items].input_type)
            case "multiselect":
                makeMultipleSelectView(set: fullEditProfileData[items].field_name, textFeildText: fullEditProfileData[items].saved_value, index: items)
            case "image":
                makeImageUpload(set: fullEditProfileData[items].field_name, textFeildText: fullEditProfileData[items].saved_value, index: items)
            case "file":
                makeDocUpload(set: fullEditProfileData[items].field_name, textFeildText: fullEditProfileData[items].saved_value, index: items)
            case "textarea":
                maketextView(set: fullEditProfileData[items].field_name, textFeildText: fullEditProfileData[items].saved_value, index: items, inputType: fullEditProfileData[items].input_type)
            case "password":
                print("11")
//            case "location":
//                getLocation(set: fullEditProfileData[items].location, textFeildText: fullEditProfileData[items].saved_value, index: items, inputType: fullEditProfileData[items].input_type)
            default:
                break
            }
        }
    }
    
    
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        print(textField.text)
        if textField != self.passwordView.newPasswordField{
            let ds =  Array(self.statesArray.keys)
            
            if ds.count > 0 {
                dropDown.dataSource = ds
                
                dropDown.selectionAction = {index,item in
                    textField.text = item
                }
                
                dropDown.anchorView = textField
                
                if dropDown.isHidden {
                    _ = dropDown.show()
                } else {
                    dropDown.hide();
                }
            }
        }
        
    }
    
    
    
    
    @objc func makePasswordCheckbox()
    {
        let headerView = CheckboxButtonView();
        headerView.checkboxButton.setTitle("Change Password".localized, for: .normal);
        let heightConstrain = NSLayoutConstraint(item: headerView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 40)
        headerView.addConstraint(heightConstrain)
        stackViewHeight.constant += 50
        stackView.addArrangedSubview(headerView)
        passwordView = PasswordView();
        heightConstrain1 = NSLayoutConstraint(item: passwordView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 0)
        passwordView.addConstraint(heightConstrain1)
        stackView.addArrangedSubview(passwordView)
        headerView.checkboxButton.addTarget(self, action: #selector(changePasswordClicked(_:)), for: .touchUpInside);
        passwordView.isHidden = true;
        passwordView.newPasswordField.delegate = self;
    }
    
    @objc func changePasswordClicked(_ sender: UIButton)
    {
        passwordCheck = !passwordCheck;
        if(passwordCheck)
        {
            sender.setImage(UIImage(named: "checked"), for: .normal);
            heightConstrain1.constant = 225
            stackViewHeight.constant += 240
            passwordView.isHidden = false;
            
        }
        else
        {
            sender.setImage(UIImage(named: "unchecked"), for: .normal);
            heightConstrain1.constant = 0;
            stackViewHeight.constant -= 240
            passwordView.isHidden = true;
        }
    }
    
    
    
    @objc func makeHeaderLabel(itemsValue: String)
    {
        let headerView = HeaderView()
        if(itemsValue == "general_information"){
            headerView.topLabel.text = "General Information".localized
        }else{
            headerView.topLabel.text = itemsValue
        }
        headerView.topLabel.backgroundColor = DynamicColor.secondaryColor
        headerView.topLabel.textColor = .white
        let heightConstrain = NSLayoutConstraint(item: headerView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 59)
        headerView.addConstraint(heightConstrain)
        stackViewHeight.constant += 69
        stackView.addArrangedSubview(headerView)
    }
    
    @objc func makeLabel(set labelText:String , textFeildText:String,index IndexWithWholeData :Int)
    {
        let headerView = LabelComboView()
        headerView.headingLabel.text = labelText
        headerView.valueLabel.text = textFeildText
        let heightConstrain = NSLayoutConstraint(item: headerView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 75)
        headerView.addConstraint(heightConstrain)
        stackViewHeight.constant += 90
        stackView.addArrangedSubview(headerView)
    }
    
    //MARK:- Create Date Picker
    func addMobileNumber(set labelText:String , textFeildText:String,index IndexWithWholeData :Int, inputType: String, fieldName:String?=nil)
    {
        oldMobileNumber = textFeildText
        let label = UILabel()
        label.text = labelText
        label.font = UIFont.systemFont(ofSize: 17, weight: UIFont.Weight.medium)
        label.textColor = .black
        stackView.addArrangedSubview(label)
        stackView.addConstraint(NSLayoutConstraint(item: label, attribute: .leading, relatedBy: .equal, toItem: stackView, attribute: .leading, multiplier: 1, constant: 5))
        stackView.addConstraint(NSLayoutConstraint(item: label, attribute: .trailing, relatedBy: .equal, toItem: stackView, attribute: .trailing, multiplier: 1, constant: -5))
        stackView.addConstraint(NSLayoutConstraint(item: label, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 25))
        stackViewHeight.constant += 25
        
       // mobileNumber.delegate = self
        mobileNumber.setPhoneNumber("")
        mobileNumber.setDefaultCountryCode("eg")
        mobileNumber.layer.cornerRadius = 1
        mobileNumber.layer.borderColor = UIColor.lightGray.cgColor
        mobileNumber.layer.borderWidth = 1
        let code = mobileNumber.getDialCode()
        if(textFeildText.count > 4){
            mobileNumber.setPhoneNumber(String(textFeildText.dropFirst(code.count-1)))
        }
        stackView.addArrangedSubview(mobileNumber)
        Util.deactivateRTL(of: mobileNumber)
        mobileNumber.translatesAutoresizingMaskIntoConstraints = false;
        stackView.addConstraint(NSLayoutConstraint(item: mobileNumber, attribute: .leading, relatedBy: .equal, toItem: stackView, attribute: .leading, multiplier: 1, constant: 5))
        stackView.addConstraint(NSLayoutConstraint(item: mobileNumber, attribute: .trailing, relatedBy: .equal, toItem: stackView, attribute: .trailing, multiplier: 1, constant: -5))
        //signUpStackview.addConstraint(NSLayoutConstraint(item: mobileNumber, attribute: .top, relatedBy: .equal, toItem: signUpStackview, attribute: .bottom, multiplier: 1, constant: 10))
        stackView.addConstraint(NSLayoutConstraint(item: mobileNumber, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 40))
        stackViewHeight.constant += 40
    }
    
    @objc func makeTextFeild(set labelText:String,field_to_post:String , textFeildText:String,index IndexWithWholeData :Int, inputType: String, fieldName:String?=nil){
        let viewForTextFeild = Label_TextFieldComboView()
        viewForTextFeild.headingLabel.text = labelText
        
        if fieldName == "State"{
            viewForTextFeild.textLabel.text = textFeildText
        }else{
            viewForTextFeild.textLabel.text = fullEditProfileData[IndexWithWholeData].saved_value
        }
        if field_to_post == "shop_url"{
            viewForTextFeild.textLabel.isUserInteractionEnabled = !self.is_shop_url_done
        }
        viewForTextFeild.textLabel.isEnabled = true
        stackViewHeight.constant = stackViewHeight.constant + 90
        stackView.addArrangedSubview(viewForTextFeild)
        if(inputType == "int")
        {
            viewForTextFeild.textLabel.keyboardType = .numberPad;
        }
        viewForTextFeild.translatesAutoresizingMaskIntoConstraints = false
        switch viewForTextFeild.headingLabel.text {
        case "Location":
//            callLocationData(viewForTextFeild.textLabel)
            viewForTextFeild.textLabel.tag = 1111
       case "Latitude":
            viewForTextFeild.textLabel.tag = 1122
           // viewForTextFeild.textLabel.isEnabled = false
        case "Longitude":
            viewForTextFeild.textLabel.tag = 1123
         //  viewForTextFeild.textLabel.isEnabled = false
        case "City":
            viewForTextFeild.textLabel.tag = 1100
        case "State":
            viewForTextFeild.textLabel.tag = 1101
        case "Country":
            viewForTextFeild.textLabel.tag = 1102
        case "Zip/Postal Code":
            viewForTextFeild.textLabel.tag = 1103
        default:
            print("")
        }
        
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 75)
        viewForTextFeild.addConstraint(heightConstrain)
        viewForTextFeild.tag = IndexWithWholeData
        
        if fieldName != nil {
//            viewForTextFeild.textLabel.text = ""
//            viewForTextFeild.textLabel.placeholder = "Select State".localized
            viewForTextFeild.textLabel.delegate = self
        } else {
            viewForTextFeild.textLabel.delegate = nil
        }
        
        
        
        
        
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        let pass = textField.text
        if pass != "" {
            let result = String(describing: passwordStgth.checPassWordStrenght(password: pass!)).uppercased()
            passwordView.passwordStrengthLabel.text = result.localized
            if(result == "strong")
            {
                passwordView.passwordStrengthLabel.backgroundColor = UIColor.white;
                passwordView.passwordStrengthLabel.textColor = UIColor.green;
            }
            else
            {
                passwordView.passwordStrengthLabel.backgroundColor = UIColor.red;
                passwordView.passwordStrengthLabel.textColor = UIColor.white;
            }
        }
        return true
    }

    @objc func makeDropDown(set labelText:String , textFeildText:String,index IndexWithWholeData :Int){
        let viewForTextFeild = Label_DropDown_Combo_View()
        viewForTextFeild.entityNameLabel.text = labelText
        print(textFeildText)
//        let cLoc = UserDefaults.standard.value(forKey: "currentLocation") as? [String:String]
//        print(cLoc)
        dropOption.removeAll()
        print(dropOption)
       
        print(labelText)
        print(IndexWithWholeData)
        
        if String(labelText) == "State" {
            self.stateDropdownButton = viewForTextFeild.dropDownButton
            self.stateButtonIndex = IndexWithWholeData
        }
       if String(labelText) == "Country" {
          self.countryDropdownButton = viewForTextFeild.dropDownButton
       }
        
        if(textFeildText != "")
        {
            let options = fullEditProfileData[IndexWithWholeData].dropdownOptions;
            for(key,value) in options
            {
                if(value == textFeildText)
                {
                    viewForTextFeild.dropDownButton.setTitle(key, for: .normal)
                    if labelText == "Country" && self.countryArray.count > 0{
                        
                        self.fetchStatesInsideCountry(key, sender: self.countryDropdownButton!)
                    }
                    break;
                }
            }
        }
        if(fullEditProfileData[IndexWithWholeData].field_to_post == "business_state")
        {
            selectedBusinessState = textFeildText;
            businessStateButton = viewForTextFeild.dropDownButton;
            fetchStatesInsideCountry(textFeildText, sender: viewForTextFeild.dropDownButton)
            
            //fetchCitiesInsideCountry(textFeildText, sender: viewForTextFeild.dropDownButton);
        }
        else if(fullEditProfileData[IndexWithWholeData].field_to_post == "bank_state")
        {
            selectedBankState = textFeildText;
            bankStateButton = viewForTextFeild.dropDownButton;
            fetchStatesInsideCountry(textFeildText, sender: viewForTextFeild.dropDownButton)
          //  fetchCitiesInsideCountry(textFeildText, sender: viewForTextFeild.dropDownButton);
        }
        else if(fullEditProfileData[IndexWithWholeData].field_to_post == "region_id")
        {
           // addressCountryButton = viewForTextFeild.dropDownButton;
         //   addressStateButton = viewForTextFeild.dropDownButton;
            //fetchStatesInsideCountry(textFeildText, sender: viewForTextFeild.dropDownButton)
            //fetchCitiesInsideCountry(textFeildText, sender: viewForTextFeild.dropDownButton);
        }
//        else if(fullEditProfileData[IndexWithWholeData].field_to_post == "bank_city")
//        {
//            bankCityButton = viewForTextFeild.dropDownButton;
//            if(selectedBankState != "")
//            {
//                fetchCitiesInsideCountry(selectedBankState, cityItem: textFeildText ,sender: viewForTextFeild.dropDownButton);
//            }
//        }
//        else if(fullEditProfileData[IndexWithWholeData].field_to_post == "business_city")
//        {
//            businessCityButton = viewForTextFeild.dropDownButton;
//            if(selectedBusinessState != "")
//            {
//                fetchCitiesInsideCountry(selectedBusinessState, cityItem: textFeildText, sender: viewForTextFeild.dropDownButton);
//            }
//        }
         viewForTextFeild.dropDownButton.tag = IndexWithWholeData
        viewForTextFeild.dropDownButton.addTarget(self, action: #selector(showDropDown(_:)), for: UIControl.Event.touchUpInside);
        self.stackView.addArrangedSubview(viewForTextFeild)
        viewForTextFeild.tag = IndexWithWholeData
        stackViewHeight.constant = stackViewHeight.constant + 90
        viewForTextFeild.translatesAutoresizingMaskIntoConstraints = false
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 75)
        viewForTextFeild.addConstraint(heightConstrain)
    }
    
    
    @objc func makeDatePickeView(set labelText:String , textFeildText:String,index IndexWithWholeData :Int){
        let viewForTextFeild = buttonLabel()
//        dataPickerArray.append(viewForTextFeild.button)
        viewForTextFeild.topLabel.text = labelText
        viewForTextFeild.datePicker.tag = IndexWithWholeData
        viewForTextFeild.datePicker.addTarget(self, action: #selector(datePickerValueChanged(_:)), for: .valueChanged)
        self.stackView.addArrangedSubview(viewForTextFeild)
        viewForTextFeild.tag = IndexWithWholeData
        stackViewHeight.constant = stackViewHeight.constant + 300
        viewForTextFeild.translatesAutoresizingMaskIntoConstraints = false
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 250)
        viewForTextFeild.addConstraint(heightConstrain)
    }
    
    @objc func makePassword(set labelText:String , textFeildText:String,index IndexWithWholeData :Int, inputType: String){
        let viewForTextFeild = Label_TextFieldComboView()
//        viewForTextFeild.backgroundColor = .red
        viewForTextFeild.headingLabel.text = labelText
        viewForTextFeild.textLabel.text = ""
        viewForTextFeild.textLabel.isEnabled = true
        viewForTextFeild.textLabel.isSecureTextEntry = true
        stackViewHeight.constant = stackViewHeight.constant + 90
        stackView.addArrangedSubview(viewForTextFeild)
        if(inputType == "int")
        {
            viewForTextFeild.textLabel.keyboardType = .numberPad;
        }
        viewForTextFeild.translatesAutoresizingMaskIntoConstraints = false
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 75)
        viewForTextFeild.addConstraint(heightConstrain)
        viewForTextFeild.tag = IndexWithWholeData
    }
    
    
    @objc func makeMultipleSelectView(set labelText:String , textFeildText:String,index IndexWithWholeData :Int){
        let viewForTextFeild = collectionView_Label()
        viewForTextFeild.topLabel.text = fullEditProfileData[IndexWithWholeData].field_name
        let nibCell = UINib(nibName: "MultiSelectCollectionViewCell", bundle: nil)
        viewForTextFeild.collectionView.register(nibCell, forCellWithReuseIdentifier: "MultiSelectCollectionViewCell")
        let array = fullEditProfileData[IndexWithWholeData].saved_value.split(separator: ",")
        var val = [String]()
        for items in array{
            val.append(String(items))
        }
        multiSelect.updateValue(val, forKey: fullEditProfileData[IndexWithWholeData].field_to_post)
        viewForTextFeild.collectionView.tag = IndexWithWholeData
        viewForTextFeild.collectionView.delegate = self
        viewForTextFeild.collectionView.dataSource = self
        stackViewHeight.constant = stackViewHeight.constant + 200
        stackView.addArrangedSubview(viewForTextFeild)
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 180)
        viewForTextFeild.addConstraint(heightConstrain)
        viewForTextFeild.tag = IndexWithWholeData
    }
    
    @objc func makeDocUpload(set labelText:String , textFeildText:String,index IndexWithWholeData :Int){
        let viewForTextFeild=Label_ImageView_Combo_View()
        viewForTextFeild.entityNameLabel.text = fullEditProfileData[IndexWithWholeData].field_name
        viewForTextFeild.browseButton.addTarget(self, action: #selector(uploadDocButtonPressed(sender:)), for: .touchUpInside)
        viewForTextFeild.browseButton.tag = IndexWithWholeData + 845
        viewForTextFeild.downloadButton.tag = IndexWithWholeData + 845
        viewForTextFeild.imgView.tag = IndexWithWholeData + 845
        viewForTextFeild.fileName.tag = IndexWithWholeData + 845
        /*if let url = URL(string: fullEditProfileData[IndexWithWholeData].saved_value){
            viewForTextFeild.imgView.sd_setImage(with: url) { (image, error, _, _) in
            }
        }*/
        if(textFeildText != "")
        {
            viewForTextFeild.downloadButton.isHidden = false;
            viewForTextFeild.downloadButton.addTarget(self, action: #selector(downloadClicked(_:)), for: .touchUpInside)
        }
        viewForTextFeild.imgView.isHidden = true;
        
        viewForTextFeild.fileName.isHidden = true;
        fileData.updateValue("", forKey: fullEditProfileData[IndexWithWholeData].field_to_post)
        imagViewArray.updateValue(viewForTextFeild.imgView ?? UIImageView(), forKey: fullEditProfileData[IndexWithWholeData].field_to_post)
        stackViewHeight.constant = stackViewHeight.constant + 90
        stackView.addArrangedSubview(viewForTextFeild)
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 90)
        viewForTextFeild.addConstraint(heightConstrain)
        viewForTextFeild.tag = IndexWithWholeData
    }
    
    @objc func downloadClicked(_ sender: UIButton)
    {
        print("---tag--")
        print(sender.tag)
        let link = fullEditProfileData[sender.tag - 845].saved_value
        let viewControl = storyboard?.instantiateViewController(withIdentifier: "ced_WebView") as! ced_WebView
        viewControl.pageUrl = link;
        self.navigationController?.pushViewController(viewControl, animated: true)
    }
    
    @objc func makeImageUpload(set labelText:String , textFeildText:String,index IndexWithWholeData :Int){
        let viewForTextFeild=Label_ImageView_Combo_View()
        viewForTextFeild.fileName.isHidden = true;
        viewForTextFeild.entityNameLabel.text = fullEditProfileData[IndexWithWholeData].field_name
        viewForTextFeild.browseButton.addTarget(self, action: #selector(browseImageToUplaod(_:)), for: .touchUpInside)
        viewForTextFeild.browseButton.tag = IndexWithWholeData + 845
        viewForTextFeild.imgView.tag = IndexWithWholeData + 845
        if let url = URL(string: fullEditProfileData[IndexWithWholeData].saved_value.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""){
            viewForTextFeild.imgView.sd_setImage(with: url) { (image, error, _, _) in
            }
        }
        imagViewArray.updateValue(viewForTextFeild.imgView ?? UIImageView(), forKey: fullEditProfileData[IndexWithWholeData].field_to_post)
        stackViewHeight.constant = stackViewHeight.constant + 90
        stackView.addArrangedSubview(viewForTextFeild)
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 90)
        viewForTextFeild.addConstraint(heightConstrain)
        viewForTextFeild.tag = IndexWithWholeData
    }
//    Label_TextView_Combo_View
    
    @objc func maketextView(set labelText:String , textFeildText:String,index IndexWithWholeData :Int, inputType: String){
        let viewForTextFeild=Label_TextView_Combo_View()
        viewForTextFeild.entityNameLabel.text = labelText
        viewForTextFeild.entityValueTxtField.text = fullEditProfileData[IndexWithWholeData].saved_value
        stackViewHeight.constant = stackViewHeight.constant + 140
        stackView.addArrangedSubview(viewForTextFeild)
        if(inputType == "int")
        {
            viewForTextFeild.entityValueTxtField.keyboardType = .numberPad;
        }
        viewForTextFeild.translatesAutoresizingMaskIntoConstraints = false
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 120)
        viewForTextFeild.addConstraint(heightConstrain)
        viewForTextFeild.tag = IndexWithWholeData
    }
    
    
}
/// deopdownFunction
extension EditProfileViewController{
    @objc func showDropDown(_ sender:UIButton){
        let dropDown = DropDown();
        if(sender == self.businessStateButton)
        {
            dropDown.dataSource = Array(businessStateArray.keys).sorted();
        }
        else if(sender == self.bankStateButton)
        {
            dropDown.dataSource = Array(bankStateArray.keys).sorted();
        }
        else if(sender == self.businessCityButton)
        {
            dropDown.dataSource = Array(businessCityArray.keys).sorted();
        }
        else if(sender == self.bankCityButton)
        {
            dropDown.dataSource = Array(bankCityArray.keys).sorted();
        }
        else if(sender == self.addressCountryButton){
            dropDown.dataSource = Array(addressCountryArray.keys).sorted()
        }
        else
        {
            
            if (self.statesArray.count > 0 && sender.tag == self.stateButtonIndex) {
                
                dropDown.dataSource = Array(self.statesArray.keys).sorted()
                
            }else{
                dropDown.dataSource = Array(fullEditProfileData[sender.tag].dropdownOptions.keys).sorted();
            }
        }
        
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            for (i,v) in self.countryArray {
                if i == item {
            self.fetchStatesInsideCountry(item, sender: sender)
                }
            }
            /*if(sender == self.businessStateButton)
            {
                self.businessCityButton.setTitle("", for: .normal)
                self.fetchCitiesInsideCountry(self.businessStateArray[item]!, sender: sender)
            }
            else if(sender == self.bankStateButton)
            {
                self.bankCityButton.setTitle("", for: .normal);
                self.fetchCitiesInsideCountry(self.bankStateArray[item]!, sender: sender)
            }*/
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            _ = dropDown.show()
        } else {
            dropDown.hide();
        }
    }
}

// datePickerView selection
extension EditProfileViewController{
    @objc func datePickerValueChanged(_ sender: UIDatePicker){
        let dateFormatter: DateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "MM/dd/yyyy hh:mm a"
        let selectedDate: String = dateFormatter.string(from: sender.date).replacingOccurrences(of: " ", with: "")
        print("Selected value \(selectedDate)")
    }
}

// MultiSelect View selection
extension EditProfileViewController : UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout {
    @objc func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "MultiSelectCollectionViewCell", for: indexPath) as? MultiSelectCollectionViewCell
        let text = fullEditProfileData[collectionView.tag].options[indexPath.row]
        cell?.checkBoxButtonView.checkboxButton.setTitle(text, for: .normal)
//        checking for selected data
        let keyValue = fullEditProfileData[collectionView.tag].field_to_post
        if let selectedata = multiSelect[keyValue]{
            cell?.setdataView(dataFor: selectedata)
        }
        cell?.checkBoxButtonView.tag = collectionView.tag
        cell?.checkBoxButtonView.checkboxButton.tag = collectionView.tag
        cell?.checkBoxButtonView.checkboxButton.addTarget(self, action: #selector(selectMultiSelectbutton(_:)), for: .touchUpInside)
        
        return cell ?? UICollectionViewCell()
    }
    
    @objc func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    @objc func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return fullEditProfileData[collectionView.tag].options.count
    }
    
    @objc func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: 180, height: 40)
    }
    
    @objc func selectMultiSelectbutton(_ sender:UIButton){
        if sender.currentImage == UIImage(named: "unchecked"){
            sender.setImage(UIImage(named: "checked"), for: .normal)
            let keyValue = fullEditProfileData[sender.tag].field_to_post
            var arry = multiSelect[keyValue]
            if !(arry?.contains(sender.currentTitle ?? "") ?? false){
                if arry == nil{
                    arry = [sender.currentTitle ?? ""]
                }else{
                    arry?.append(sender.currentTitle ?? "")
                }
            }
            multiSelect.updateValue(arry ?? [], forKey: keyValue)
        }else{
            sender.setImage(UIImage(named: "unchecked"), for: .normal)
            let keyValue = fullEditProfileData[sender.tag].field_to_post
            var arry = multiSelect[keyValue]
            if (arry?.contains(sender.currentTitle ?? "") ?? false){
//                arry?.append(sender.currentTitle ?? "")
                arry = arry?.filter { $0 != sender.currentTitle ?? "" }
            }
            multiSelect.updateValue(arry ?? [], forKey: keyValue)
        }
        print(multiSelect)
    }
}
extension EditProfileViewController{
   
   /* @objc func fetchCitiesInsideCountry(_ item:String, cityItem: String = "", sender: UIButton)
    {
        
        if(item != "")
        {
            var baseURL = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
            baseURL += "rest/V1/mobiconnect/module/getcity/\(item)";
            
            print(baseURL);
            
            var request = URLRequest(url: URL(string: baseURL)!);
            
            request.httpMethod = "GET";
            
            let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
            //request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
            
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
            
            let sessionConfig = URLSessionConfiguration.default
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request, completionHandler: {
                // check for fundamental networking error
                data, response, error in
                guard error == nil && data != nil else
                {
                    print("error=\(error)");
                    DispatchQueue.main.async
                        {
                            Alert_File.removeLoadingIndicator(self)
                            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                    }
                    return;
                }
                
                // check for http errors
                if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
                {
                    print("statusCode should be 200, but is \(httpStatus.statusCode)")
                    print("response = \(response)");
                    DispatchQueue.main.async
                        {
                            Alert_File.removeLoadingIndicator(self);
                            if(httpStatus.statusCode == -1009){
                                ced_showError.showNoNetWork(self)
                            }else{
                                ced_showError.showNoModule(self)
                            }
                            return;
                    }
                    return;
                }
                
                // code to fetch values from response :: start
                
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self);
                        if let data = data{
                            guard let jsonData = try? JSON(data: data) else {return}
                            print("jsonData[\"data\"]DDDEEEVVV");
                            print(jsonData);
                            if(sender == self.businessStateButton)
                            {
                                self.businessCityArray = [String:String]()
                                for city in jsonData[0]["cities"].arrayValue
                                {
                                    self.businessCityArray[city["label"].stringValue] = city["city_id"].stringValue;
                                }
                            }
                            else if(sender == self.bankStateButton)
                            {
                                self.bankCityArray = [String:String]()
                                for city in jsonData[0]["cities"].arrayValue
                                {
                                    self.bankCityArray[city["label"].stringValue] = city["city_id"].stringValue;
                                }
                            }
                            else if(sender == self.bankCityButton)
                            {
                                self.bankCityArray = [String:String]()
                                for city in jsonData[0]["cities"].arrayValue
                                {
                                    self.bankCityArray[city["label"].stringValue] = city["city_id"].stringValue;
                                    if(cityItem == city["city_id"].stringValue)
                                    {
                                        sender.setTitle(city["label"].stringValue, for: .normal);
                                    }
                                }
                            }
                            else if(sender == self.businessCityButton)
                            {
                                self.businessCityArray = [String:String]()
                                for city in jsonData[0]["cities"].arrayValue
                                {
                                    self.businessCityArray[city["label"].stringValue] = city["city_id"].stringValue;
                                    if(cityItem == city["city_id"].stringValue)
                                    {
                                        sender.setTitle(city["label"].stringValue, for: .normal);
                                    }
                                }
                            }
                            /*if(jsonData["success"].stringValue == "false")
                             {
                             print("Show TEXTBOX");
                             print(self.stateSelectionViewTag);
                             print(self.view.viewWithTag(self.stateSelectionViewTag));
                             if let newStatePickerView = self.view.viewWithTag(self.stateSelectionViewTag) as? NewStatePickerView
                             {
                             print("jkdfgdjksfgkdjsfg")
                             self.stateDropDownIsVisible = false;
                             newStatePickerView.dropDownButton.isHidden = true;
                             newStatePickerView.textValue.isHidden = false;
                             }
                             }
                             else
                             {
                             print("Show DropDown");
                             if let newStatePickerView = self.view.viewWithTag(self.stateSelectionViewTag) as? NewStatePickerView
                             {
                             self.stateDropDownIsVisible = true;
                             newStatePickerView.dropDownButton.isHidden = false;
                             newStatePickerView.textValue.isHidden = true;
                             }
                             
                             self.statesArray = [String:String]();
                             for (_,val) in jsonData["states"]
                             {
                             self.statesArray[val["name"].stringValue] = val["region_id"].stringValue;
                             }
                             
                             print("self.statesArray");
                             print(self.statesArray);
                             }*/
                        }
                }
            })
            
            task.resume();
        }
        
        
        
    }*/
    
    @objc func fetchStatesInsideCountry(_ item:String, sender: UIButton)
    {
    if(item != "")
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
        var baseURL = settings.baseUrl
        baseURL += "vendorapi/index/getcountry/";
        
        print(baseURL);
        
        var request = URLRequest(url: URL(string: baseURL)!);
        
        request.httpMethod = "POST";
        var postString = "vendor_id="+vendorId+"&hashkey="+hashKey;

       //postString += "&country_code="+"IN";
       
        let country_id = self.countryArray[item]!;
           if country_id != "" {
            print(country_id)
        postString += "&country_code="+country_id;
            }
        
        print(postString);
        
        request.httpBody = postString.data(using: String.Encoding.utf8);
        
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request, completionHandler: {
            // check for fundamental networking error
            data, response, error in
            guard error == nil && data != nil else
            {
                print("error=\(error)");
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self)
                    ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error".localized, showMsg: error!.localizedDescription)
                }
                
                return;
            }
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                print("response = \(response)");
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self);
                        if(httpStatus.statusCode == -1009){
                            ced_showError.showNoNetWork(self)
                        }else{
                            ced_showError.showNoModule(self)
                        }
                        return;
                }
                return;
            }
                // code to fetch values from response :: start
                
                
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self);
                        if let data = data{
                            guard let jsonData = try? JSON(data: data) else {return}
                            print("jsonData[\"data\"]DDDEEEVVV");
                            print(jsonData);
                            if(sender == self.bankStateButton)
                            {
                                for val in jsonData["states"].arrayValue
                                {
                                    self.bankStateArray[val["default_name"].stringValue] = val["region_id"].stringValue;
                                    if(val["region_id"].stringValue == item)
                                    {
                                        sender.setTitle(val["default_name"].stringValue, for: .normal);
                                    }
                                }
                                
                            }
                            else if(sender == self.businessStateButton)
                            {
                                for val in jsonData["states"].arrayValue
                                {
                                    self.businessStateArray[val["default_name"].stringValue] = val["region_id"].stringValue;
                                    if(val["region_id"].stringValue == item)
                                    {
                                        sender.setTitle(val["default_name"].stringValue, for: .normal);
                                    }
                                }
                            }
                            
                           if(jsonData["success"].stringValue == "false")
                             {
                             print("Show TEXTBOX");
                                self.statesArray.removeAll()
//                             print(self.stateSelectionViewTag);
//                             print(self.view.viewWithTag(self.stateSelectionViewTag));
//                             if let newStatePickerView = self.view.viewWithTag(self.stateSelectionViewTag) as? NewStatePickerView
//                             {
//                             print("jkdfgdjksfgkdjsfg")
//                             self.stateDropDownIsVisible = false;
//                             newStatePickerView.dropDownButton.isHidden = true;
//                             newStatePickerView.textValue.isHidden = false;
//                             }
                             }
                             else
                           {
                            print("Show DropDown");
                            /*if let newStatePickerView = self.view.viewWithTag((self.stateButtonIndex)) as? NewStatePickerView
                             {
                             self.stateDropDownIsVisible = true;
                             newStatePickerView.dropDownButton.isHidden = false;
                             newStatePickerView.textValue.isHidden = true;
                             }*/
                            self.stateDropdownButton?.isHidden = false
                            
                            
                            
                            
                            self.statesArray = [String:String]();
                            for (_,val) in jsonData["states"]
                            {
                                self.statesArray[val["name"].stringValue] = val["region_id"].stringValue;
                            }
                            
                            print("self.statesArray");
                            print(self.statesArray);
                            }
                        }
                }
            })
            
            task.resume();
        }
        
        
    }
    

       
}
extension EditProfileViewController:UIImagePickerControllerDelegate,UINavigationControllerDelegate{
    
    
    
    /* Browse image button clicked */
    @objc func browseImageToUplaod( _ sender:UIButton )
    {
        currentTagForImageToUpload = sender.tag
        
        ImagePicker.delegate = self
        ImagePicker.sourceType = UIImagePickerController.SourceType.photoLibrary
        self.present(ImagePicker, animated: true, completion: nil)
    }
    
    /* Selected image and Gathered information of image */
    @objc func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
// Local variable inserted by Swift 4.2 migrator.
let info = convertFromUIImagePickerControllerInfoKeyDictionary(info)

        //Showing image in imageview after image is selected in ImagePickerController
        if let img = self.view.viewWithTag(currentTagForImageToUpload) as? UIImageView
        {
            if let browseView = img.superview?.superview as? Label_ImageView_Combo_View
            {
                browseView.downloadButton.isHidden = true;
                browseView.fileName.isHidden = true;
                browseView.imgView.isHidden = false;
            }
            
            img.image = info[convertFromUIImagePickerControllerInfoKey(UIImagePickerController.InfoKey.originalImage)] as? UIImage
            self.fileData.updateValue("", forKey: fullEditProfileData[currentTagForImageToUpload - 845].field_to_post)
            imagViewArray.updateValue(img, forKey: fullEditProfileData[currentTagForImageToUpload - 845].field_to_post)
        }
        //Removing imagePickerController after selecting image
        self.dismiss(animated: true, completion: nil);
    }
    
}
extension EditProfileViewController{
    @objc func saveButtonPressed(_ : UIButton) {
        var param = ""
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendor_id = userData["vendorId"] as! String
        let hashkey    = userData["hashKey"] as! String
        param += "vendor_id="+vendor_id+"&hashkey="+hashkey
        for items in stackView.subviews{
            if let view = items as? buttonLabel{
                let dateFormatter = DateFormatter()
                dateFormatter.locale = Locale(identifier: "en_US_POSIX")
                dateFormatter.dateFormat = "hh:mm"
                let stringDate = dateFormatter.string(from: view.datePicker.date)
                if fullEditProfileData[view.tag].is_required == "1"{
                    if stringDate == ""{
                        self.view.showToastMsg(fullEditProfileData[view.tag].field_name+" is required".localized)
                        return
                    }
                }
                param = param + "&"+fullEditProfileData[view.tag].field_to_post+"="+stringDate
            }else if let view = items as? Label_TextFieldComboView{
                print(fullEditProfileData[view.tag].field_to_post)
                if fullEditProfileData[view.tag].is_required == "1"{
                    if view.textLabel.text == ""{
                        self.view.showToastMsg(fullEditProfileData[view.tag].field_name+" is required".localized)
                        return
                    }
                }
                if fullEditProfileData[view.tag].field_to_post == "shop_url" && !self.is_shop_url_done && !self.isValidShopUrl(testStr: view.textLabel.text!){
                        self.view.makeToast("Enter a valid Shop Url. Example: my-shop-url".localized, duration: 2.0, position: .center)
                         return
                }
                if view.headingLabel.text == "State"{
                    let stateText = view.textLabel.text ?? ""
                    let stateCode = statesArray[stateText] ?? stateText
                    param = param + "&"+fullEditProfileData[view.tag].field_to_post+"="+stateCode
                }else{
                    param = param + "&"+fullEditProfileData[view.tag].field_to_post+"="+(view.textLabel.text ?? "")
                }
                
                
            }else if let view = items as? Label_DropDown_Combo_View{
                if fullEditProfileData[view.tag].is_required == "1"{
                    if view.dropDownButton.currentTitle == ""{
                        self.view.showToastMsg(fullEditProfileData[view.tag].field_name+" is required".localized)
                        return
                    }
                }
                print("-----")
                print(fullEditProfileData[view.tag])
                if(view.dropDownButton.currentTitle != "")
                {
                    if(fullEditProfileData[view.tag].field_to_post == "bank_city")
                    {
                        param = param + "&"+fullEditProfileData[view.tag].field_to_post+"=" + (bankCityArray[view.dropDownButton.currentTitle!] ?? "")
                    }
                    else if(fullEditProfileData[view.tag].field_to_post == "business_city")
                    {
                        param = param + "&"+fullEditProfileData[view.tag].field_to_post+"=" + (businessCityArray[view.dropDownButton.currentTitle!] ?? "")
                    }
                    else if(fullEditProfileData[view.tag].field_to_post == "bank_state")
                    {
                        param = param + "&"+fullEditProfileData[view.tag].field_to_post+"=" + (bankStateArray[view.dropDownButton.currentTitle!] ?? "")
                    }
                    else if(fullEditProfileData[view.tag].field_to_post == "business_state")
                    {
                        param = param + "&"+fullEditProfileData[view.tag].field_to_post+"=" + (businessStateArray[view.dropDownButton.currentTitle!] ?? "")
                    }
                    else
                    {
                        param = param + "&"+fullEditProfileData[view.tag].field_to_post+"=" + (fullEditProfileData[view.tag].dropdownOptions[view.dropDownButton.currentTitle ?? ""] ?? "")
                    }
                    
                }
                else
                {
                    param = param + "&"+fullEditProfileData[view.tag].field_to_post+"="
                }
                
            }else if let view = items as? collectionView_Label{
                if fullEditProfileData[view.tag].is_required == "1"{
                    if multiSelect[fullEditProfileData[view.tag].field_to_post] == nil || multiSelect[fullEditProfileData[view.tag].field_to_post] == []{
                        self.view.showToastMsg(fullEditProfileData[view.tag].field_name+" is required".localized)
                        return
                    }
                }
                if let data = multiSelect[fullEditProfileData[view.tag].field_to_post]{
                    let string = data.joined(separator: ",")
                    param = param + "&"+fullEditProfileData[view.tag].field_to_post+"=" + (string)
                }
            }
            else if let view = items as? Label_ImageView_Combo_View{
                //                TODo: THIS IS DATA FOR IMAGES
                if(fullEditProfileData[view.tag].type == "image")
                {
                    if fullEditProfileData[view.tag].is_required == "1"{
                        if imagViewArray[fullEditProfileData[view.tag].field_to_post] == nil{
                            self.view.showToastMsg(fullEditProfileData[view.tag].field_name+" is required".localized)
                            return
                        }
                    }
                    if let data = imagViewArray[fullEditProfileData[view.tag].field_to_post]{
                        if let tempData=(data.image ?? UIImage()).jpegData(compressionQuality: 0.5)
                        {
                            print(tempData)
                            let dateFormatter: DateFormatter = DateFormatter()
                            dateFormatter.locale = Locale(identifier: "en_US_POSIX")
                            //dateFormatter.dateFormat = "MM/dd/yyyy hh:mm a"
                            dateFormatter.timeStyle = .medium
                            //formatter.dateStyle = .long
                            var selectedDate: String = dateFormatter.string(from: Date())
                            selectedDate = selectedDate.replacingOccurrences(of: " ", with: "")
                            let chat_file=(tempData.base64EncodedString())
                            let dictionary = ["name":"12\(randomString(length: 4))\(selectedDate).jpg","base64_encoded_data":chat_file,"type":"file"]
                            let convertedstring=convertDicTostring(str: dictionary)
                            param = param + "&"+fullEditProfileData[view.tag].field_to_post+"="+(convertedstring )
                        }
                        
                    }
                }
                else if(fullEditProfileData[view.tag].type == "file")
                {
                    if fullEditProfileData[view.tag].is_required == "1"{
                        if fileData[fullEditProfileData[view.tag].field_to_post] == "" && imagViewArray[fullEditProfileData[view.tag].field_to_post] == nil{
                            self.view.showToastMsg(fullEditProfileData[view.tag].field_name+" is required")
                            return
                        }
                    }
                    if(fileData[fullEditProfileData[view.tag].field_to_post] == ""){
                        if let data = imagViewArray[fullEditProfileData[view.tag].field_to_post]{
                            if let tempData=(data.image ?? UIImage()).jpegData(compressionQuality: 0.5)
                            {
                                print(tempData)
                                let dateFormatter: DateFormatter = DateFormatter()
                                dateFormatter.locale = Locale(identifier: "en_US_POSIX")
                                //dateFormatter.dateFormat = "MM/dd/yyyy hh:mm a"
                                dateFormatter.timeStyle = .medium
                                //formatter.dateStyle = .long
                                var selectedDate: String = dateFormatter.string(from: Date())
                                selectedDate = selectedDate.replacingOccurrences(of: " ", with: "")
                                let chat_file=(tempData.base64EncodedString())
                                let dictionary = ["name":"12\(randomString(length: 4))\(selectedDate).jpg","base64_encoded_data":chat_file,"type":"file"]
                                let convertedstring=convertDicTostring(str: dictionary)
                                param = param + "&"+fullEditProfileData[view.tag].field_to_post+"="+(convertedstring )
                            }
                        }
                        
                    }
                    else
                    {
                        if let data = fileData[fullEditProfileData[view.tag].field_to_post]{
                            let dateFormatter: DateFormatter = DateFormatter()
                            dateFormatter.locale = Locale(identifier: "en_US_POSIX")
                            //dateFormatter.dateFormat = "MM/dd/yyyy hh:mm a"
                            dateFormatter.timeStyle = .medium
                            //formatter.dateStyle = .long
                            var selectedDate: String = dateFormatter.string(from: Date())
                            selectedDate = selectedDate.replacingOccurrences(of: " ", with: "")
                            let chat_file=data;
                            let dictionary = ["name":"12\(randomString(length: 4))\(selectedDate).pdf","base64_encoded_data":chat_file,"type":"file"]
                            let convertedstring=convertDicTostring(str: dictionary)
                            param = param + "&"+fullEditProfileData[view.tag].field_to_post+"="+(convertedstring )
                            
                        }
                    }
                    
                }
                
            }else if let view = items as? Label_TextView_Combo_View{
                if fullEditProfileData[view.tag].is_required == "1"{
                    if view.entityValueTxtField.text == ""{
                        self.view.showToastMsg(fullEditProfileData[view.tag].field_name+" is required".localized)
                        return
                    }
                }
                
                param = param + "&"+fullEditProfileData[view.tag].field_to_post+"="+(view.entityValueTxtField.text ?? "")
            }
            else if let view = items as? Label_TextView_Combo_View{
                if fullEditProfileData[view.tag].is_required == "1"{
                    if view.entityValueTxtField.text == ""{
                        self.view.showToastMsg(fullEditProfileData[view.tag].field_name+" is required".localized)
                        return
                    }
                }
                
                param = param + "&"+fullEditProfileData[view.tag].field_to_post+"="+(view.entityValueTxtField.text ?? "")
            }
        }
        if(passwordCheck)
        {
            
           if(passwordView.currentPasswordField.text == "" || passwordView.newPasswordField.text == "" || passwordView.confirmPasswordField.text == "")
           {
            self.view.makeToast("Please Enter Password".localized, duration: 2.0, position: .center)
                return;
            }
            if(passwordView.newPasswordField.text != passwordView.confirmPasswordField.text)
            {
                self.view.makeToast("Confirm And New Password Is Not Same".localized, duration: 2.0, position: .center)
                return;
            }
            if(passwordView.newPasswordField.text!.count < 8)
            {
                self.view.makeToast("Your Password Must Be Atleast 8 Characters Long".localized, duration: 2.0, position: .center)
                return;
            }
            param += "&currentpassword="+passwordView.currentPasswordField.text!;
            param += "&newpassword="+passwordView.confirmPasswordField.text!;
            
        }
        let newMobileNumber = self.mobileNumber.getPhoneNumber().replacingOccurrences(of: "+", with: "")
        param += "&contact_number="+newMobileNumber
        param += "&shop_url_done=1"
        if(newMobileNumber != oldMobileNumber){
            let phonNumber = self.mobileNumber.getNationalPhoneNumber()
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
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait!!!".localized)
            self.sendOTP(toNumber: newMobileNumber, parameter: param, isResend: false)
        }else{
            self.sendRequest(url: "vendorapi/index/update", parameters: param)
        }
    }
}
extension EditProfileViewController: UIDocumentInteractionControllerDelegate,UIDocumentMenuDelegate,UIDocumentPickerDelegate
{
    func randomString(length: Int) -> String {
              let letters = "abcdefghijklmnopqrstuvwxyz0123456789"
              return String((0..<length).map{ _ in letters.randomElement()! })
          }
    @objc func uploadDocButtonPressed(sender:UIButton)
    {
        currentTagForImageToUpload = sender.tag
        if let browseView=sender.superview?.superview as? Label_ImageView_Combo_View{
            selectedBrowseView=browseView
            let actionSheetControllerIOS8: UIAlertController = UIAlertController(title: "Please select".localized, message: "Option to select".localized, preferredStyle: .actionSheet)
            
            let cancelActionButton = UIAlertAction(title: "Cancel".localized, style: .cancel) { _ in
                print("Cancel")
            }
            actionSheetControllerIOS8.addAction(cancelActionButton)
            
            let saveActionButton = UIAlertAction(title: "upload image".localized, style: .default)
            { _ in
                print("upload image")
                
                self.image_picker()
            }
            let uploadpdf = UIAlertAction(title: "upload pdf".localized, style: .default)
            { _ in
                print("upload pdf")
                self.browseButtonPressed()
            }
            if let popoverController = actionSheetControllerIOS8.popoverPresentationController {
                popoverController.sourceView = self.view
                popoverController.sourceRect = CGRect(x: self.view.bounds.midX, y: self.view.bounds.midY, width: 0, height: 0)
                popoverController.permittedArrowDirections = []
            }
            actionSheetControllerIOS8.addAction(uploadpdf)
            actionSheetControllerIOS8.addAction(saveActionButton)
            self.present(actionSheetControllerIOS8, animated: true, completion: nil)
        }
    }
    @objc func image_picker()
    {
        print("browseButtonPressed")
        ImagePicker = UIImagePickerController()
        ImagePicker.delegate=self
        ImagePicker.allowsEditing = false
        ImagePicker.sourceType = .photoLibrary
        self.present(ImagePicker, animated: true, completion: nil)
    }
    
    @objc func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
    }
    
    
    @objc func browseButtonPressed(){
        
        let doc=UIDocumentMenuViewController(documentTypes: ["public.text","public.content","public.image","public.data"], in: .import)
        doc.delegate=self
        doc.modalPresentationStyle = .popover
        
        doc.popoverPresentationController?.sourceView = self.view //to set the source of your alert
        doc.popoverPresentationController?.sourceRect = CGRect(x: self.view.bounds.midX, y: self.view.bounds.midY, width: 0, height: 0) // you can set this as per your requirement.
        doc.popoverPresentationController?.permittedArrowDirections = []
        self.present(doc, animated: true, completion: nil)
        
    }
    
    
    @objc func documentMenu(_ documentMenu: UIDocumentMenuViewController, didPickDocumentPicker documentPicker: UIDocumentPickerViewController) {
        documentPicker.delegate=self
        documentPicker.modalPresentationStyle = .fullScreen
        self.present(documentPicker, animated: true, completion: nil)
        print("didPickDocumentPicker")
    }
    
    
    @objc func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentAt url: URL) {
        
        let fileName=url.absoluteString.components(separatedBy: "/")
        print("didPickDocumentAt")
        let isSecuredURL = url.startAccessingSecurityScopedResource() == true
        let coordinator = NSFileCoordinator()
        var error: NSError? = nil
        coordinator.coordinate(readingItemAt: url, options: [], error: &error) { (url) -> Void in
            do{
                let temp=try Data(contentsOf: url)
                let temp1=fileName[fileName.count-1].components(separatedBy: ".")
                let check=fileName[fileName.count-1].contains(".pdf")
                print(temp1)
                if !check
                {
                    let msg = "The file you have choosed is not in correct format".localized
                    print(msg)
                    ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error".localized, showMsg: msg)
                    return
                }
                else
                {
                    self.selectedBrowseView.fileName.text = temp1[0]
                    self.selectedBrowseView.fileName.isHidden = false;
                    self.selectedBrowseView.imgView.isHidden = true;
                    self.selectedBrowseView.downloadButton.isHidden = true;
                    self.fileData.updateValue(temp.base64EncodedString(), forKey: self.fullEditProfileData[self.selectedBrowseView.fileName.tag - 845].field_to_post)
                    //self.dataPdf = temp.base64EncodedString()
                    //self.selectedBrowseView.base64EncodedStringData=temp.base64EncodedString()
                    
                }
            }catch let error{
                print("%%^^&*(UGGVGHBJKBFXFGXCF")
                print(error.localizedDescription)
                print(error)
            }
        }
        if (isSecuredURL) {
            url.stopAccessingSecurityScopedResource()
        }
        print(url)
        controller.dismiss(animated: true, completion: nil)
    }
    
    @objc func documentMenuWasCancelled(_ documentMenu: UIDocumentMenuViewController) {
        print("documentMenuWasCancelled")
        documentMenu.dismiss(animated: true, completion: nil)
    }
    
    @objc func documentPickerWasCancelled(_ controller: UIDocumentPickerViewController) {
        print("documentPickerWasCancelled")
        controller.dismiss(animated: true, completion: nil)
    }
    @objc func JSONConvert(data :[String]) -> String {
        var ConvertData = ""
        let x = data as [String]
        do{
            let JSONData = try JSONSerialization.data(
                withJSONObject: x ,
                options: JSONSerialization.WritingOptions(rawValue: 0))
            ConvertData = NSString(data: JSONData,
                                   encoding: String.Encoding.utf8.rawValue)! as String
        }
        catch{
            print("error in data encoding")
        }
        print(ConvertData)
        return ConvertData
    }
}
enum passwordStgth: Int {
    case weak
    case moderate
    case strong
    
    static func checPassWordStrenght(password: String) -> passwordStgth {
        let len: Int = password.count
        var strength: Int = 0
        
        switch len {
        case 0...4:
            strength += 1
        case 5...8:
            strength += 2
        default:
            strength += 3
        }
        
        // Upper case, Lower case, Number & Symbols
        let patterns = ["^(?=.*[A-Z]).*$", "^(?=.*[a-z]).*$", "^(?=.*[0-9]).*$", "^(?=.*[!@#%&-_=:;\"'<>,`~\\*\\?\\+\\[\\]\\(\\)\\{\\}\\^\\$\\|\\\\\\.\\/]).*$"]
        
        for pattern in patterns {
            let passwordTest = NSPredicate(format:"SELF MATCHES %@", pattern);
            if passwordTest.evaluate(with: password) {
                strength += 1
            }
        }
        
        switch strength {
        case 0...3:
            return .weak
        case 4...6:
            return .moderate
        default:
            return .strong
        }
    }
}
//extension EditProfileViewController : GMSAutocompleteFetcherDelegate {
//
////    @objc func getLocation(set labelText:String , textFeildText:String,index IndexWithWholeData :Int, inputType: String){
////        print("FindLocation")
////        let filter = GMSAutocompleteFilter()
////        filter.type = .noFilter
////         fetcher = GMSAutocompleteFetcher()
////        fetcher?.autocompleteFilter = filter;
////        fetcher?.delegate = self
////        viewForTextFeild.textLabel.addTarget(self, action: #selector(locationFetchr(_:)), for: .editingChanged)
////
////    }
//    func callLocationData(_ textFeild: UITextField) {
//        let filter = GMSAutocompleteFilter()
//        filter.type = .noFilter
//         fetcher = GMSAutocompleteFetcher()
//        fetcher?.autocompleteFilter = filter;
//        fetcher?.delegate = self
//        textFeild.addTarget(self, action: #selector(locationFetchr(_:)), for: .editingChanged)
//
//    }
//
//  @objc func locationFetchr(_ textField: UITextField) {
//    fetcher?.sourceTextHasChanged(textField.text!)
//  }
//
//  func didFailAutocompleteWithError(_ error: Error) {
//    print(error.localizedDescription)
//  }
//
//  func didAutocomplete(with predictions: [GMSAutocompletePrediction]) {
//
//    print(predictions)
//
//    var suggestionValues = [String]()
//    for prediction in predictions {
//      let addresses = prediction.attributedFullText.string
//      self.placeIdArray[addresses] = prediction.placeID;
//      suggestionValues.append(addresses)
//    }
//     let textm = self.view.viewWithTag(1111) as! UITextField
//        print(textm)
//
//
//    self.dropDown.dataSource = suggestionValues;
//    self.dropDown.selectionAction = {(index, item) in
//        textm.text = item
//      let placesClient = GMSPlacesClient();
//      let placeId = self.placeIdArray[item];
//      placesClient.lookUpPlaceID(placeId!, callback: { (place, error) -> Void in
//        if let error = error {
//          print("lookup place id query error: \(error.localizedDescription)")
//          return
//        }
//        guard let place = place else {
//          print("No place details for \(String(describing: placeId))")
//          return
//        }
//        for component in place.addressComponents!
//        {
//          if(component.type == "locality")
//          {
//            self.currentLocation.updateValue(component.name, forKey: "city")
//          }
//          else if(component.type == "administrative_area_level_1")
//          {
//            self.currentLocation.updateValue(component.name, forKey: "state")
//          }
//          else if(component.type == "country"){
//            self.currentLocation.updateValue(component.name, forKey: "country")
//          }
//          else if(component.type == "postal_code") {
//            self.currentLocation.updateValue(component.name, forKey: "zipCode")
//            }
//        }
//        self.currentLocation.updateValue(place.formattedAddress ?? "", forKey: "location")
//        self.currentLocation.updateValue(String(place.coordinate.latitude), forKey: "latitude")
//        self.currentLocation.updateValue(String(place.coordinate.longitude), forKey: "longitude")
//        UserDefaults.standard.set(self.currentLocation, forKey: "currentLocation")
//        print(self.currentLocation)
//
//        let lat = self.view.viewWithTag(1122) as? UITextField
//        lat?.text = self.currentLocation["latitude"]
//        let long = self.view.viewWithTag(1123) as? UITextField
//        long?.text = self.currentLocation["longitude"]
//        let cityText = self.view.viewWithTag(1100) as? UITextField
//        cityText?.text = self.currentLocation["city"]
//        let zipPostal = self.view.viewWithTag(1103) as? UITextField
//        zipPostal?.text = self.currentLocation["zipCode"]
//        self.stateDropdownButton?.setTitle(self.currentLocation["state"], for: .normal)
//        self.countryDropdownButton?.setTitle(self.currentLocation["country"], for: .normal)
//
//      })
//    }
//
//      //  if textFld.tag == 1111 {
//    self.dropDown.anchorView = textm
//    self.dropDown.direction = .bottom;
//    self.dropDown.bottomOffset = CGPoint(x: 0, y: textm.bounds.height)
//   // }
//    if self.dropDown.isHidden {
//      let _ = self.dropDown.show();
//    } else {
//      //let _ = self.dropDown.hide();
//    }
//  }
//}
// Helper function inserted by Swift 4.2 migrator.
fileprivate func convertFromUIImagePickerControllerInfoKeyDictionary(_ input: [UIImagePickerController.InfoKey: Any]) -> [String: Any] {
    return Dictionary(uniqueKeysWithValues: input.map {key, value in (key.rawValue, value)})
}

// Helper function inserted by Swift 4.2 migrator.
fileprivate func convertFromUIImagePickerControllerInfoKey(_ input: UIImagePickerController.InfoKey) -> String {
    return input.rawValue
}
