//
//  ced_newManageLabelsOptions.swift
//  VenderApp
//
//  Created by Cedcoss on 06/10/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
import RxSwift
import RxCocoa

class ced_newManageLabelsOptions: ced_VendorBaseClass {

    var finaldata = NSMutableDictionary()
    var storeDropdownArray = [String]()
    var storeDropdownDic = [String:String]()
    var attributeId = String()
    var editAttrData = [String:String]()
    var store_label_Data = [String:String]()
    var optionsData = [JSON]()
    var disposebag1 = DisposeBag()
    var disposebag2 = DisposeBag()
    var disposebag3 = DisposeBag()
    var counter = 0
    
    var deletedItems = [String:String]()
    
    lazy var topLabel:UILabel = {
        let label = UILabel()
        label.text = "Manage Labels/Options".localized
        label.font = .systemFont(ofSize: 16, weight: .semibold)
        label.textAlignment = .center
        label.backgroundColor = DynamicColor.secondaryViewBackground
        label.textColor = DynamicColor.labelColor
        return label
    }()
    
    lazy var mainScroll:UIScrollView = {
        let scroll = UIScrollView()
        scroll.showsVerticalScrollIndicator = false
        return scroll
    }()
    
    lazy var mainStack:UIStackView = {
        let stack = UIStackView()
        stack.axis = .vertical
        stack.distribution = .fill
        stack.spacing = 5.0
        return stack
    }()
    
    lazy var defaultView:storeLabelView = {
        let view = storeLabelView()
        view.textViewForText.backgroundColor = DynamicColor.secondaryViewBackground
        view.value = ""
        view.storeName.text = "Default Label".localized
        return view
    }()
    
    lazy var optionView:optionsView = {
        let view = optionsView()
        view.addButton.rx.tap.bind{
            let item = optionItemView()
            self.storeDropdownArray.forEach{ store in
                let optionField = optionfieldView()
                let val = self.storeDropdownDic[store] ?? ""
                optionField.optionField.placeholder = store
                optionField.key = val
                optionField.optionLabel.text = store
                item.hStack.addArrangedSubview(optionField)
                optionField.anchor(width:150)
            }
            item.anchor(height:105)
            view.optionStack.addArrangedSubview(item)
            
            item.optionID = "optione_\(self.counter)"
            self.counter += 1
            
            item.deleteBtn.rx.tap.bind{
                item.removeFromSuperview()
            }.disposed(by: self.disposebag2)
            
            item.isDefaultBtn.rx.tap.bind{
                if item.isDefault{
                    item.isDefault = false
                    item.isDefaultBtn.setImage(UIImage(named: "unchecked"), for: .normal)
                }else{
                    let catalog = self.finaldata["frontend_input"] as? String
                    if catalog == "select"{
                        self.optionView.optionStack.subviews.forEach{subv in
                            if let subV = subv as? optionItemView{
                                subV.isDefault = false
                                subV.isDefaultBtn.setImage(UIImage(named: "unchecked"), for: .normal)
                            }
                        }
                        item.isDefault = true
                        item.isDefaultBtn.setImage(UIImage(named: "checked"), for: .normal)
                    }else{
                        item.isDefault = true
                        item.isDefaultBtn.setImage(UIImage(named: "checked"), for: .normal)
                    }
                }
            }.disposed(by: self.disposebag3)
            
            
        }.disposed(by: disposebag1)
        return view
    }()
    
    lazy var saveBtn:UIButton = {
        let btn = UIButton()
        btn.backgroundColor = DynamicColor.themeColor
        btn.setTitle("Save".localized, for: .normal)
        btn.setTitleColor(.white, for: .normal)
        btn.clipsToBounds = true
        btn.layer.cornerRadius = 6.0
        btn.addTarget(self, action: #selector(saveButtonTapped(_:)), for: .touchUpInside)
        return btn
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureViews()
    }
    
    func configureViews(){
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        
        view.addSubview(saveBtn)
        saveBtn.anchor(top: view.safeAreaLayoutGuide.topAnchor, right: view.trailingAnchor, paddingTop: 0, paddingRight: 5, width: 100, height: 40)
        
        view.addSubview(topLabel)
        topLabel.anchor(top: view.safeAreaLayoutGuide.topAnchor, left: view.leadingAnchor, right: saveBtn.leadingAnchor, paddingTop: 0, paddingLeft: 5, paddingRight: 8, height: 40)
        
        view.addSubview(mainScroll)
        mainScroll.anchor(top: topLabel.bottomAnchor, left: view.leadingAnchor, bottom: view.bottomAnchor, right: view.trailingAnchor, paddingTop: 0, paddingLeft: 0, paddingBottom: 0, paddingRight: 0)
        
        mainScroll.addSubview(mainStack)
        mainStack.anchor(top: mainScroll.topAnchor, left: view.leadingAnchor, bottom: mainScroll.bottomAnchor, right: view.trailingAnchor, paddingTop: 0, paddingLeft: 8, paddingBottom: 0, paddingRight: 8)
        
        renderLabelsView()
    }
    
    func renderLabelsView(){
        mainStack.addArrangedSubview(defaultView)
        defaultView.anchor(height:45)
        defaultView.textViewForText.text = editAttrData["frontend_label"]
        
        let storeArrayToBeUsed = self.storeDropdownArray.filter { $0 != "Admin" }
        
        storeArrayToBeUsed.forEach{itm in
            let storeView = storeLabelView()
            let val = storeDropdownDic[itm] ?? ""
            storeView.value = val
            storeView.storeName.text = itm
            storeView.textViewForText.text = store_label_Data[val] ?? ""
            storeView.textViewForText.backgroundColor = DynamicColor.secondaryViewBackground
            mainStack.addArrangedSubview(storeView)
            storeView.anchor(height:45)
        }
        
        
        
        let catalog = finaldata["frontend_input"] as? String
        if catalog == "select" || catalog == "multiselect"{
            print("Dropdown case \(optionsData)")
            
            optionsData.forEach{option in
                let item = optionItemView()
                storeDropdownArray.forEach{ store in
                    let optionField = optionfieldView()
                    let val = storeDropdownDic[store] ?? ""
                    optionField.optionField.placeholder = store
                    optionField.key = val
                    optionField.optionLabel.text = store
                    optionField.optionField.text = option["stores"][val].stringValue
                    item.hStack.addArrangedSubview(optionField)
                    optionField.anchor(width:150)
                }
                item.optionID = option["id"].stringValue
                item.deleteBtn.rx.tap.bind{
                    print("Delte from here")
                    self.deletedItems[item.optionID] = "1"
                    item.removeFromSuperview()
                }.disposed(by: disposebag2)
                
                item.isDefault = option["checked"] == "checked"
                let image: UIImage? = option["checked"] == "checked" ? UIImage(named: "checked") : UIImage(named: "unchecked")
                item.isDefaultBtn.setImage(image, for: .normal)
                item.isDefaultBtn.rx.tap.bind{
                    if item.isDefault{
                        item.isDefault = false
                        item.isDefaultBtn.setImage(UIImage(named: "unchecked"), for: .normal)
                    }else{
                        if catalog == "select"{
                            self.optionView.optionStack.subviews.forEach{subv in
                                if let subV = subv as? optionItemView{
                                    subV.isDefault = false
                                    subV.isDefaultBtn.setImage(UIImage(named: "unchecked"), for: .normal)
                                }
                            }
                            item.isDefault = true
                            item.isDefaultBtn.setImage(UIImage(named: "checked"), for: .normal)
                        }else{
                            item.isDefault = true
                            item.isDefaultBtn.setImage(UIImage(named: "checked"), for: .normal)
                        }
                    }
                }.disposed(by: disposebag3)
                
                
                optionView.optionStack.addArrangedSubview(item)
                item.anchor(height:105)
            }
            mainStack.addArrangedSubview(optionView)
        }
    }
    
    @objc func saveButtonTapped(_ sender:UIButton){
        
        var finaldataToSend = finaldata
        
        if defaultView.textViewForText.text == ""{
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error".localized, showMsg: "Default Label Is Required.".localized)
            return
        }
        
        var store_FrontendLabel = [[String:String]]()
        mainStack.subviews.forEach { view in
            if let view = view as? storeLabelView{
                let key = view.value
                if key != ""{
                    let label = view.textViewForText.text ?? ""
                    print("gotcha \(key)\(label)")
                    store_FrontendLabel.append(["key":key,"label":label])
                }
                
            }
        }
        
        var canWeProceed = true
        var optionsObject = [String:[[String:String]]]()
        var defaultOptionArr = [String]()
        
        let catalog = finaldata["frontend_input"] as? String
        if catalog == "select" || catalog == "multiselect"{
            optionView.optionStack.subviews.forEach{subview in
                
                if let subView = subview as? optionItemView{
                    var tempArr = [[String:String]]()
                    subView.hStack.subviews.forEach{fieldsView in
                        if let fieldsview = fieldsView as? optionfieldView{
                            if fieldsview.optionField.text == ""{
                                canWeProceed = false
                                return
                            }else{
                                tempArr.append([fieldsview.key:fieldsview.optionField.text ?? ""])
                            }
                        }
                    }
                    optionsObject[subView.optionID] = tempArr
                    if subView.isDefault {
                        defaultOptionArr.append(subView.optionID)
                    }
                }
                
            }
        }
        if !canWeProceed{
            print("Cant proceed")
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error".localized, showMsg: "Some fields are missing".localized)
            return
        }
        
        var defaultOptions = [[String:String]]()
        for (index,item) in defaultOptionArr.enumerated(){
            defaultOptions.append(["\(index)":item])
        }
        
        
        finaldataToSend["store_frontend_label"] = store_FrontendLabel
        finaldataToSend["frontend_label"] = [defaultView.textViewForText.text ?? ""]
        if !deletedItems.isEmpty{
            finaldataToSend["delete"] = deletedItems
            deletedItems.forEach{optionsObject[$0.key] = []}
            
        }
        
//        if !optionsObject.isEmpty{
            finaldataToSend["option"] = optionsObject
//        }
       
        //if !defaultOptions.isEmpty{
            finaldataToSend["default"] = defaultOptions
       // }
        
        
        var itemsString = String()
        do{
            let JSONData = try JSONSerialization.data(
                withJSONObject: finaldataToSend ,
                options: JSONSerialization.WritingOptions(rawValue: 0))
            itemsString = NSString(data: JSONData,
                encoding: String.Encoding.utf8.rawValue)! as String
            
        }
        catch{
            print("error in data encoding")
            
        }
        print(itemsString)
        let url = "vproductattributeapi/index/saveAttribute"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString  += "&attribute_data="+itemsString//.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!
        if(editAttrData.count != 0){
           postString  += "&attribute_id=" + attributeId
        }
        print(postString)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: url, parameters: postString)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if(json["data"]["success"].stringValue == "true"){
                
                self.view.makeToast(json["data"]["message"].stringValue)
                
                let storybordOrder = UIStoryboard(name: "ProductAttributes", bundle: nil)
                if let viewControl = storybordOrder.instantiateViewController(withIdentifier: "vendor_attributeListing") as? ced_vendorProductAttributesListing {
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                        self.navigationController?.setViewControllers([viewControl], animated: true)
                    }
                    
                }
            }else{
                ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error".localized, showMsg: json["data"]["message"].stringValue)
            }
        }
    }
    
}
