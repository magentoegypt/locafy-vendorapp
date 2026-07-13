//
//  ced_addFaqViewController.swift
//  VenderApp
//
//  Created by Cedcoss on 25/03/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_addFaqViewController:ced_VendorBaseClass {
    
    //MARK: - Stored Properties
    
    var faq_id = ""

    //MARK: - UIElements
    
    lazy var topLabel:UILabel = {
        let lbl = UILabel()
        lbl.backgroundColor = DynamicColor.themeColor
        lbl.textColor = DynamicColor.ViewBackgroundColor
        lbl.textAlignment = .center
        lbl.clipsToBounds = true
        lbl.layer.cornerRadius = 6.0
        return lbl
    }()
    
    lazy var mainScroll:UIScrollView = {
        let scroll = UIScrollView()
        scroll.backgroundColor = .clear
        return scroll
    }()
    
    lazy var mainStack:UIStackView = {
        let stack = UIStackView()
        stack.axis = .vertical
        stack.spacing = 5.0
        stack.alignment = .fill
        return stack
    }()
    
    lazy var saveButton:UIButton = {
        let btn = UIButton()
        btn.setTitle("Save".localized, for: .normal)
        btn.setTitleColor(DynamicColor.ViewBackgroundColor, for: .normal)
        btn.backgroundColor = DynamicColor.themeColor
        btn.clipsToBounds = true
        btn.layer.cornerRadius = 6.0
        btn.addTarget(self, action: #selector(saveButtonTapped(_:)), for: .touchUpInside)
        return btn
    }()
    
    //MARK: - LifeCycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureSubViews()
        getRequiredFields()
    }
    
    //MARK: - UIHelpers
    
    func configureSubViews(){
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        view.addSubview(topLabel)
        topLabel.anchor(top: view.safeAreaLayoutGuide.topAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 5, paddingLeft: 8, paddingRight: 8, height: 40)
        
        view.addSubview(mainScroll)
        mainScroll.anchor(top: topLabel.bottomAnchor, left: view.leadingAnchor, bottom: view.safeAreaLayoutGuide.bottomAnchor, right: view.trailingAnchor, paddingTop: 0, paddingLeft: 0, paddingBottom: 0, paddingRight: 0)
        
        mainScroll.addSubview(mainStack)
        mainStack.anchor(top: mainScroll.topAnchor, left: view.leadingAnchor, bottom: mainScroll.bottomAnchor, right: view.trailingAnchor, paddingTop: 5, paddingLeft: 8, paddingBottom: 5, paddingRight: 8)
        
        if faq_id == ""{
            topLabel.text = "Create FAQ".localized
            saveButton.setTitle("Save".localized, for: .normal)
        }else{
            topLabel.text = "Update FAQ".localized
            saveButton.setTitle("Update".localized, for: .normal)
        }
    }
    
    func renderAddFaqView(json:JSON){
        json[0]["data"].arrayValue.forEach{fields in
            
            switch fields["type"]{
            case "select":
                let faqDropdown = faqDropdownView()
                faqDropdown.fieldName = fields["name"].stringValue
                faqDropdown.dropdownLabel.text = fields["label"].stringValue
                var dropData = [String:String]()
                var fieldName = ""
                fields["values"].arrayValue.forEach{itm in
                    dropData[itm["label"].stringValue] = itm["value"].stringValue
                    if fields["value"].stringValue == itm["value"].stringValue{
                        fieldName = itm["label"].stringValue
                    }
                }
                faqDropdown.fieldValue = fields["value"].stringValue
                if fields["value"].stringValue != ""{
                    faqDropdown.dropdownBtn.setTitle(fieldName, for: .normal)
                }
                faqDropdown.dropdownDatasource = dropData
                mainStack.addArrangedSubview(faqDropdown)
                faqDropdown.heightAnchor.constraint(equalToConstant: 70).isActive = true
            case "textarea":
                let faqTextview = faqTextviewView()
                faqTextview.fieldName = fields["name"].stringValue
                faqTextview.textViewLabel.text = fields["label"].stringValue
                faqTextview.textViewValue.text = fields["value"].stringValue
                mainStack.addArrangedSubview(faqTextview)
                faqTextview.heightAnchor.constraint(equalToConstant: 160).isActive = true
            case "textfield":
                let faqTextField = faqTextFieldView()
                faqTextField.fieldName = fields["name"].stringValue
                faqTextField.textFieldLabel.text = fields["label"].stringValue
                faqTextField.textfieldValue.text = fields["value"].stringValue
                if(fields["name"] != "email"){
                    mainStack.addArrangedSubview(faqTextField)
                }
                faqTextField.heightAnchor.constraint(equalToConstant: 70).isActive = true
            default:
                print("def")
            }
        }
        
        mainStack.addArrangedSubview(saveButton)
        saveButton.heightAnchor.constraint(equalToConstant: 40).isActive = true
    }
    
    //MARK: - Selectors
    
    @objc func saveButtonTapped(_ sender:UIButton){
        var postData = [String:String]()
        var paramsToPost = [String:Any]()
        var canProceed = true
        mainStack.subviews.forEach{feilds in
            
            if let field = feilds as? faqDropdownView{
                if field.fieldValue == ""{
                    canProceed = false
                }
                if field.fieldName == "product_id"{
                    paramsToPost["product_id"] = field.fieldValue
                }else{
                    postData[field.fieldName] = field.fieldValue
                }
            }
            if let field = feilds as? faqTextviewView{
                if field.textViewValue.text == ""{
                    canProceed = false
                }
                postData[field.fieldName] = field.textViewValue.text
            }
            if let field = feilds as? faqTextFieldView{
                if field.textfieldValue.text == ""{
                    if field.fieldName != "email"{
                        canProceed = false
                    }
                }
                postData[field.fieldName] = field.textfieldValue.text
            }
        }
        print(postData)
        if !canProceed{
            self.view.makeToast("All fields are required".localized)
            return
        }
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        
        paramsToPost["faq_Data"] = postData
        paramsToPost["vendor_id"] = vendorId
        if faq_id != ""{
            paramsToPost["faq_id"] = faq_id
        }
        sendANyRequest(url: "vfaqapi/savefaq", params: paramsToPost)
//        self.sendRequest(url: "vfaqapi/savefaq", params: postData)
    }
    
    //MARK: - API
    
    func getRequiredFields(){
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        
        var postdata = [String:String]()
        postdata["vendor_id"] = vendorId
        if faq_id != ""{
            postdata["faq_id"] = faq_id
        }
        sendRequest(url: "vfaqapi/getfaqattributes", params: postdata)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        guard let data = data else {return}
        guard let json = try? JSON(data: data) else {return}
        print(json)
        if requestUrl == "vfaqapi/getfaqattributes"{
            if json[0]["success"].stringValue == "true"{
                renderAddFaqView(json:json)
            }
        }
        if requestUrl == "vfaqapi/savefaq"{
            self.view.makeToast(json[0]["msg"].stringValue)
            Ced_CommonVendor.delay(1.5) {
                self.navigationController?.popViewController(animated: true)
            }
        }
    }
    
}
