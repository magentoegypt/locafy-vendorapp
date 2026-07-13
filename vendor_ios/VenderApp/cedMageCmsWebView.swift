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
 * @package   MageNative
 * @author    CedCommerce Core Team <connect@cedcommerce.com >
 * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 * @license      http://cedcommerce.com/license-agreement.txt
 */


import UIKit
import SafariServices
import WebKit
class cedMageCmsWebView: ced_VendorBaseClass,WKScriptMessageHandler,SFSafariViewControllerDelegate, WKNavigationDelegate {
    
    var webview = WKWebView()
    var pageUrl = String()
    override func viewDidLoad() {
        super.viewDidLoad()
        let temp=URL(string: pageUrl)
        let myRequest = URLRequest(url: temp!)
        let webconfgi = WKWebViewConfiguration()
        webconfgi.userContentController.add(self,                                                                    name: "redir")
        
        let bounds = self.view.bounds
        let toplayoutguide = self.navigationController!.view.frame.origin.y ;
        let frame  = CGRect(x: 0, y: toplayoutguide, width: bounds.width, height: bounds.height)
        webview = WKWebView(frame: frame, configuration: webconfgi)
        webview.load(myRequest)
        self.view.addSubview(webview)
        webview.navigationDelegate = self
//        cedMageLoaders.addDefaultLoader(me: self)
        loadPage()
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.navigationBar.isHidden = false
    }
    func loadPage(){
        let request = URLRequest(url: URL(string: pageUrl)!)
        webview.load(request)
    }
    
     func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
          //cedMageLoaders.removeLoadingIndicator(me: self)
        webView.evaluateJavaScript("document.getElementsByClassName ('page-footer')[0].style.display='none';", completionHandler: nil)
         webView.evaluateJavaScript("document.getElementsByClassName ('page-header type2 header-newskin')[0].style.display='none';", completionHandler: nil)
      }//page-header type2 header-newskin
      
      func userContentController(_ userContentController:
          WKUserContentController,
                                 didReceive message: WKScriptMessage) {
          print(message.body)
          
      }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
}
