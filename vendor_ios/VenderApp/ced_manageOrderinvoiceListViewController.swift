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
* @package   MageNative MultiVendor
* @author    CedCommerce Core Team <connect@cedcommerce.com >
* @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
* @license      http://cedcommerce.com/license-agreement.txt
*/

import UIKit

class ced_manageOrderinvoiceListViewController: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource {

    @IBOutlet weak var tableView: UITableView!
    let refreshControl = UIRefreshControl()
    var invoiceData = [[String:String]]()
    
    var orderId = String()
 
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        self.title = "Invoices"
        tableView.delegate = self
        tableView.dataSource = self
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
    ced_navigationBarController().addNavButton(self,str:"back")
        self.loadData(1)
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        tableView.refreshControl = refreshControl
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func refresh(_ sender: AnyObject) {
        self.invoiceData.removeAll()
        tableView.reloadData()
        self.loadData(1)
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return invoiceData.count
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "manageInvoicedata") as? ced_mangageInvoiceList
        cell?.invoiceId.text = invoiceData[indexPath.row]["invoiceID"]
        cell?.invoiceDate.text = invoiceData[indexPath.row]["invoiceDate"]     
        cell?.billingName.text = ""//invoiceData[indexPath.row]["billing_name"]
        cell?.status.text  = invoiceData[indexPath.row]["status"]
        cell?.amount.text   = invoiceData[indexPath.row]["amount"]
        return cell!
    }
    
    
    @objc func loadData(_ page:Int){
        
        let baseUrl = "vorderapi/vorders/viewinvoice/page/"+"\(page)"
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        if(orderId != ""){
            postString += "&order_id="+orderId
        }
        sendRequest(url: baseUrl, parameters: postString);
    }
    
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        refreshControl.endRefreshing()
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if(json["data"]["success"].stringValue == "true"){
                self.parseData(json)
                self.tableView.reloadData()
                Alert_File.removeLoadingIndicator(self)
            }else{
              //  ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: json["data"]["message"].stringValue)
                self.tableView.setEmptyMessage(json["data"]["message"].stringValue)
            }
        }
        
    }
    
    fileprivate func parseData(_ json:JSON){
        for result in json["data"]["invoice"].arrayValue{
            var data = [String:String]()
            data["status"] = result["state"].stringValue
            data["invoiceID"] = result["increment_id"].stringValue
            data["billing_name"]  = result["billing_name"].stringValue
            data["amount"]       = result["vendor_payment"].stringValue
            data["invoiceDate"]  = result["created_at"].stringValue
            self.invoiceData.append(data)
        }
    }
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let data = invoiceData[indexPath.row]
        let view = storyboard?.instantiateViewController(withIdentifier: "invoiceView") as? ced_invoiceView
        view?.invoiceId = data["invoiceID"]!
        self.navigationController?.pushViewController(view!, animated: true)
    }
    
   
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}

extension UITableView {

    func setEmptyMessage(_ message: String) {
        let messageLabel = UILabel(frame: CGRect(x: 0, y: 0, width: self.bounds.size.width, height: self.bounds.size.height))
        messageLabel.text = message
        messageLabel.textColor = .black
        messageLabel.numberOfLines = 0
        messageLabel.textAlignment = .center
        messageLabel.font = UIFont(name: "Roboto-Regular", size: 15)
        messageLabel.sizeToFit()

        self.backgroundView = messageLabel
        self.separatorStyle = .none
    }

    func restore() {
        self.backgroundView = nil
       // self.separatorStyle = .singleLine
    }
}
