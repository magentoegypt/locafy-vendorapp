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
import WebKit
class mageHomeWebController: UIViewController, WKNavigationDelegate {

    var notificationCheck = false;
    @IBOutlet weak var webView: WKWebView!
    var url=""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let temp=URL(string: url)
        let myRequest = URLRequest(url: temp!)
        webView?.navigationDelegate = self
        webView.load(myRequest)
        // Do any additional setup after loading the view.
    }

    /*override func viewDidAppear(_ animated: Bool) {
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        self.navigationController?.navigationBar.barTintColor = color
        self.navigationController?.navigationBar.tintColor = UIColor.white;
        
    }*/
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @objc func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...");
    }
    /*@objc func webViewDidStartLoad(_ webView: UIWebView) {
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...");
    }*/
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        Alert_File.removeLoadingIndicator(self)
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
