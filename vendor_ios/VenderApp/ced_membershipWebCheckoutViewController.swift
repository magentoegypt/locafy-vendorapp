//
//  ced_membershipWebCheckoutViewController.swift
//  VenderApp
//
//  Created by cedcoss on 15/09/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//https://omaneorder.com/en/vmembershipapi/onepage/index/cart_id/2431/store_id/21/check/true

import UIKit
import WebKit

class ced_membershipWebCheckoutViewController: ced_VendorBaseClass,WKNavigationDelegate, WKScriptMessageHandler {
    
    var count = 0
    var checkOut = WKWebView()
    var currentUrl = String()
    var cartId = ""
    var custID = ""
//    required init?(coder aDecoder: NSCoder) {
//
//    }
    override func viewDidLoad() {
        
        super.viewDidLoad()
        self.checkOut = WKWebView(frame: CGRect.zero)
 //       super.init(coder: aDecoder)
        self.checkOut.navigationDelegate = self
        beforeCheckOutCheck()
        // Do any additional setup after loading the view.
    }
    
    func beforeCheckOutCheck(){
        
        var baseUrl   = "vmembershipapi/onepage/index"
//        if cartId != ""{
//            baseUrl   += "/cart_id/" + "\(cartId)"
//        }
        if custID != ""{
            baseUrl   += "/customer_id/" + "\(custID)"
        }
//        let storeId = defaults.value(forKey: "storeId") as? String ?? ""
       // baseUrl += "/store_id/\(storeId)"
        baseUrl += "/check/true"
        
        print(baseUrl)
        
         self.getRequest(url: baseUrl)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            guard let json = try? JSON(data:data) else{return;}
            print(json)
            if json["success"].stringValue == "false"{
                ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg:json["message"].stringValue)
                Alert_File.removeLoadingIndicator(self)
            }else{
                let url = json["message"].stringValue
                loadData(url:url)
            }
            
        }
    }
    
    
    func loadData(url:String){
        var baseUrl = url
        //        var baseUrl = cedMage.getInfoPlist(fileName:"cedMage",indexString: "cedAppBaseUrl") as! String
        //        baseUrl   += "mobiconnectcheckout/onepage/index"
        //        if(defaults.bool(forKey: "isLogin")){
        //            let currentuser = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //            let userid = currentuser["customerId"]
        //            baseUrl   += "/customer_id/"+"\(userid!)"
        //        }else{
        //            baseUrl   += "/cart_id/"
        //            if(defaults.object(forKey: "cartId") != nil)
        //            {
        //                let cart_id = defaults.object(forKey: "cartId") as? String;
        //                baseUrl += "\(cart_id!)"
        //            }
        //        }
        print(baseUrl)
        
        var request = URLRequest(url: URL(string:baseUrl)!)
//        let requestHeader = Settings.headerKey//cedMage.getInfoPlist(fileName:"cedMage",indexString: "requestheader") as! String
//        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        let webconfgi = WKWebViewConfiguration()
        webconfgi.userContentController.add(self,name: "redir")
        
        let bounds = self.view.bounds
        let toplayoutguide = self.navigationController!.view.frame.origin.y ;
        let frame  = CGRect(x: 0, y: toplayoutguide, width: bounds.width, height: bounds.height)
        checkOut = WKWebView(frame: frame, configuration: webconfgi)
        checkOut.load(request)
        self.view.addSubview(checkOut)
        checkOut.navigationDelegate = self
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        
    }
    
    
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        Alert_File.removeLoadingIndicator(self)
    }
    
    func userContentController(_ userContentController:
        WKUserContentController,
                               didReceive message: WKScriptMessage) {
        print(message.body)
        if let message = message.body as? String {
            if message.contains("orderview"){
                if let orderId = message.components(separatedBy: "-").last {
//                    let viewcontroll = UIStoryboard(name: "cedMageAccounts", bundle: nil).instantiateViewController(withIdentifier: "cedMageSingleOrder") as! cedMageSingleOrder
//                    viewcontroll.orderId = orderId
//                    self.navigationController?.pushViewController(viewcontroll, animated: true)
                }
            }else if message.contains("success"){
                print("Gotg success")
             //   defaults.setValue("0", forKey: "items_count")
             //   self.setCartCount(view: self, items: "0")
            }else{
//                if checkOut.url?.absoluteString.contains("onepage/success") ?? false{
                    if message.lowercased().contains("vredirect"){
                        if count >= 2 {
                            _ =  self.navigationController?.popToRootViewController(animated: true)
                        }else{
                            count = count + 1
                        }
                    }
//                }else{
////                    _ =  self.navigationController?.popToRootViewController(animated: true)
//                }
                
            }
        }
    }
    func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        if let urlStr = navigationAction.request.url?.absoluteString {
            print("urlStr\(urlStr)")
            
            currentUrl = urlStr
        }
        decisionHandler(.allow)
    }
}
