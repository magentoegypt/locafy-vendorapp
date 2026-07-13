//
//  ced_WebView.swift
//  VenderApp
//
//  Created by cedcoss on 12/02/19.
//  Copyright © 2019 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
import WebKit
class ced_WebView: ced_VendorBaseClass, WKNavigationDelegate {

    var pageUrl = "";
    @IBOutlet weak var myWebView: WKWebView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        myWebView?.navigationDelegate = self
        //myWebView.delegate = self;
        loadPage();

        // Do any additional setup after loading the view.
    }
    
    @objc func loadPage(){
        var request = URLRequest(url: URL(string: pageUrl)! )
        //let requestHeader = cedMage.getInfoPlist(fileName: "cedMage",indexString:"requestheader" ) as! String
        //request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        myWebView.load(request )
        
    }
    
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        //cedMageLoaders.removeLoadingIndicator(me: self)
    }
    func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        print(error)
        //cedMageLoaders.removeLoadingIndicator(me: self)
    }
 

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
