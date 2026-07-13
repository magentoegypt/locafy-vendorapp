//
//  ced_storeVC.swift
//  VenderApp
//
//  Created by cedcoss on 09/06/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_storeVC: ced_VendorBaseClass {
    
    var storeList = [stores]()//["English","Arabic"]
    var baseUrl = settings.baseUrl//Ced_CommonVendor.getInfoPlist("baseUrl") as? String
    static var selectLangauge = "ar"
    var isFromDrawer=false
    override func viewDidLoad() {
        super.viewDidLoad()
        print("VC called")
   
        getStoresData()
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.navigationBar.isHidden = true
    }
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        self.navigationController?.navigationBar.isHidden = false
    }
    
    func getStoresData(){
        getRequest(url: "rest/V1/vendorapi/stores/storelist")
    }
   
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        guard let data = data else {return}
        guard let json = try? JSON(data: data) else{return}
        print(json)
        json[0]["store_data"].arrayValue.forEach{storeList.append(stores(json: $0))}
        DispatchQueue.main.async {
            self.showStores()
        }
    }
    
    func showStores(){
        let actionsheet = UIAlertController(title: "Select Store".localized, message: nil, preferredStyle: .actionSheet)
        print("%^%^%^*(**^&%&%*)_*^&^")
        for buttons in self.storeList {
            if(buttons.code == "eg_ar" || buttons.code == "eg_en"){
                actionsheet.addAction(UIAlertAction(title: buttons.name, style: UIAlertAction.Style.default,handler: {
                    action -> Void in
                    print(action.title as Any)
                    UserDefaults.standard.set(true, forKey: "isAppAlreadyLaunchedOnce")
                    self.storeSelected(store:  buttons.code, storeId: buttons.store_id)
                }))
            }
        }
        if isFromDrawer{
            actionsheet.addAction(UIAlertAction(title: "Cancel", style: UIAlertAction.Style.cancel, handler: {
                action -> Void in
                self.navigationController?.popViewController(animated: true)
            }))
        }
        if(UIDevice().model.lowercased() == "ipad".lowercased()){
            if let popoverController = actionsheet.popoverPresentationController {
                popoverController.sourceView = self.view //to set the source of your alert
                popoverController.sourceRect = CGRect(x: self.view.bounds.midX, y: self.view.bounds.midY, width: 0, height: 0) // you can set this as per your requirement.
                popoverController.permittedArrowDirections = [] //to hide the arrow of any particular direction
            }
        }
        self.present(actionsheet, animated: true, completion: nil)
    }
       
    func storeSelected(store:String,storeId:String){
        print(store)
        UserDefaults.standard.set(storeId, forKey: "storeId")
        if (store == "en" || store == "eg_en"){
            UserDefaults.standard.removeObject(forKey: "AppleLanguages")
            UserDefaults.standard.setValue("eg-en/", forKey: "storeCode")
            UserDefaults.standard.set(["en"], forKey: "AppleLanguages")
            UserDefaults.standard.set(["en"], forKey: "SelectLanguages")
            UserDefaults.standard.set("en", forKey: "i18n_language")
            UserDefaults.standard.synchronize()
            Bundle.setLanguage("en")
            ced_storeVC.selectLangauge = "en"
            //self.view.reloadInputViews()
            UIView.appearance().semanticContentAttribute = .forceLeftToRight
            UISearchBar.appearance().semanticContentAttribute = .forceLeftToRight
            UITextField.appearance(whenContainedInInstancesOf: [UISearchBar.self]).textAlignment = .left
            UITextField.appearance().textAlignment = .left
            UITextView.appearance().textAlignment = .left
        }else if (store == "ar" || store == "eg_ar"){
            UserDefaults.standard.removeObject(forKey: "AppleLanguages")
            UserDefaults.standard.setValue("eg/", forKey: "storeCode")
            UserDefaults.standard.set(["ar"], forKey: "AppleLanguages")
            UserDefaults.standard.set(["ar"], forKey: "SelectLanguages")
            UserDefaults.standard.set("ar", forKey: "i18n_language")
            UserDefaults.standard.synchronize()
            Bundle.setLanguage("ar")
            ced_storeVC.selectLangauge = "ar"
           // UIApplicationMain(CommandLine.argc, CommandLine.unsafeArgv, nil, NSStringFromClass(AppDelegate.self))
            //self.view.reloadInputViews()
            UIView.appearance().semanticContentAttribute = .forceRightToLeft
            UISearchBar.appearance().semanticContentAttribute = .forceRightToLeft
            UITextField.appearance(whenContainedInInstancesOf: [UISearchBar.self]).textAlignment = .right
            UITextField.appearance().textAlignment = .right
            UITextView.appearance().textAlignment = .right
        }else if store == "spanish"{
            UserDefaults.standard.removeObject(forKey: "AppleLanguages")
            UserDefaults.standard.setValue("spanish/", forKey: "storeCode")
            UserDefaults.standard.set(["en"], forKey: "AppleLanguages")
            UserDefaults.standard.set(["en"], forKey: "SelectLanguages")
            UserDefaults.standard.set("en", forKey: "i18n_language")
            UserDefaults.standard.synchronize()
            Bundle.setLanguage("en")
            ced_storeVC.selectLangauge = "en"
            //self.view.reloadInputViews()
            UIView.appearance().semanticContentAttribute = .forceLeftToRight
            UISearchBar.appearance().semanticContentAttribute = .forceLeftToRight
            UITextField.appearance(whenContainedInInstancesOf: [UISearchBar.self]).textAlignment = .left
            UITextField.appearance().textAlignment = .left
            UITextView.appearance().textAlignment = .left
        }
        
        load_app()
    }
    func load_app(){
        var window: UIWindow?
        let deafaults = UserDefaults.standard
        if(deafaults.bool(forKey: "isLogin")){
            let story = UIStoryboard(name: "Main", bundle: nil)
            let viewControl = story.instantiateViewController(withIdentifier: "rootafterlogin") as? MKSideDrawerViewController
            window?.rootViewController = viewControl
            window?.makeKeyAndVisible()
             Ced_CommonVendor().loadData()
        }else{
            let storyboard = UIStoryboard(name: "ced_vendorLogin", bundle: nil)
            let viewControl = storyboard.instantiateViewController(withIdentifier: "rootbeforelogin") as? UINavigationController
            window?.rootViewController = viewControl
            window?.makeKeyAndVisible()
            Ced_CommonVendor().loadData()
        }
        Ced_CommonVendor.checkmoduless()
        Ced_CommonVendor().isRegAvail()
    }
   

}

struct stores{
    let name:String
    let code:String
    let store_id:String
    init(json:JSON){
        name = json["name"].stringValue
        code = json["code"].stringValue
        store_id = json["store_id"].stringValue
    }
}
