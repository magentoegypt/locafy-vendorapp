//
//  ced_manageInventoryViewController.swift
//  VenderApp
//
//  Created by Cedcoss on 28/03/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_manageInventoryViewController: ced_VendorBaseClass {

    //MARK: - UIElements
    
    lazy var topLbl:UILabel = {
        let lbl = UILabel()
        lbl.backgroundColor = DynamicColor.themeColor
        lbl.text = "Inventory Minimum Products".localized
        lbl.textAlignment = .center
        lbl.font = .systemFont(ofSize: 16, weight: .semibold)
        lbl.textColor = DynamicColor.ViewBackgroundColor
        lbl.clipsToBounds = true
        lbl.layer.cornerRadius = 6.0
        return lbl
    }()
    
    lazy var minimumLbl:UILabel = {
        let lbl = UILabel()
        lbl.text = "Enter Minimum Number".localized
        lbl.font = .systemFont(ofSize: 14, weight: .semibold)
        return lbl
    }()
    
    lazy var minimumField:UITextField = {
        let field = UITextField()
        field.borderStyle = .roundedRect
        field.keyboardType = .numberPad
//        field.textContentType = .oneTimeCode
        return field
    }()
    
    lazy var saveBtn:UIButton = {
        let btn = UIButton()
        btn.setTitle("Save".localized, for: .normal)
        btn.setTitleColor(DynamicColor.ViewBackgroundColor, for: .normal)
        btn.backgroundColor = DynamicColor.themeColor
        btn.titleLabel?.font = .systemFont(ofSize: 17, weight: .bold)
        btn.addTarget(self, action: #selector(saveBtnTapped(_:)), for: .touchUpInside)
        return btn
    }()
    
    //MARK: - life cycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        getMinimumQuantity()
    }
    
    //MARK: - UIHelper
    
    func setupUI(){
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        
        view.addSubview(topLbl)
        topLbl.anchor(top: view.safeAreaLayoutGuide.topAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 5, paddingLeft: 8, paddingRight: 8, height: 40)
        
        view.addSubview(minimumLbl)
        minimumLbl.anchor(top: topLbl.bottomAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 8, paddingLeft: 12, paddingRight: 12, height: 30)
        
        view.addSubview(minimumField)
        minimumField.anchor(top: minimumLbl.bottomAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 5, paddingLeft: 12, paddingRight: 12, height: 40)
        
        view.addSubview(saveBtn)
        saveBtn.anchor(top: minimumField.bottomAnchor, left: view.leadingAnchor, right: view.trailingAnchor, paddingTop: 8, paddingLeft: 12, paddingRight: 12, height: 40)
    }
    
    //MARK: - selectors
    
    @objc func saveBtnTapped(_ sender:UIButton){
        if minimumField.text == ""{
            self.view.makeToast("Please enter minimum number")
            return
        }
        
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        
        sendRestFullRequest(url: "vinventory/saveMinimumInventory", params: ["minimum_quantity":minimumField.text ?? "","vendor_id":vendorId], store: false)
    }
    
    //MARK: - API
    
    func getMinimumQuantity(){
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        sendRestFullRequest(url: "vinventory/getMinimumInventory", params: ["vendor_id":vendorId], store: false)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        guard let data = data else {return}
        guard let json = try? JSON(data: data) else {return}
        print(json)
        if requestUrl == "vinventory/getMinimumInventory"{
            if json["vendor_data"]["success"].stringValue == "true"{
                DispatchQueue.main.async {
                    self.minimumField.text = json["vendor_data"]["minimum_quantity"].stringValue
                }
            }
        }
        if requestUrl == "vinventory/saveMinimumInventory"{
            self.view.makeToast(json["message"].stringValue)
        }
    }
}
