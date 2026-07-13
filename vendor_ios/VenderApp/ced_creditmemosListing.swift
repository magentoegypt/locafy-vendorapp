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

class ced_creditmemosListing: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate {

    @IBOutlet weak var tableView: UITableView!
    let refreshControl = UIRefreshControl()
    var orderId = String()
   
  
    var invoiceData = [[String:String]]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "CreditMemos"
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
        cell?.invoiceId.text = invoiceData[indexPath.row]["increment_id"]
        cell?.invoiceDate.text = invoiceData[indexPath.row]["created_at"]
        cell?.amount.text = invoiceData[indexPath.row]["grand_total"]
        cell?.status.text = invoiceData[indexPath.row]["state"]
        
        return cell!
    }
    
    
    @objc func loadData(_ page:Int){
        var baseUrl = settings.baseUrl
        
        baseUrl += "vorderapi/vorders/viewcreditmemo/page/"+"\(page)"
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        if(orderId != ""){
            postString += "&order_id="+orderId
        }
        var request =  URLRequest(url: URL(string:baseUrl)!)
        request.httpMethod = "POST"
        request.httpBody = postString.data(using: String.Encoding.utf8)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")

        print(baseUrl)
        print(postString)
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request, completionHandler: {
            data,response,error in
            

            DispatchQueue.main.async
                {
                    self.refreshControl.endRefreshing()
               Alert_File.removeLoadingIndicator(self)
            }
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self)
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                }
                print("error=\(error)")
                return;
            }
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                DispatchQueue.main.async
                    {
                        self.tableView.isHidden = true
                        if(httpStatus.statusCode == -1009){
                            ced_showError.showNoNetWork(self)
                        }else{
                            ced_showError.showNoModule(self)
                        }
                }
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                print("response = \(response)")
                return;
            }
            DispatchQueue.main.async
                {
                    if let data = data{
                        guard let json = try? JSON(data: data) else {return}
                        print(json)
                        if(json["data"]["success"].stringValue == "true"){
                            self.parseData(json)
                            self.tableView.restore()
                            self.tableView.reloadData()
                        }else{
                            self.tableView.setEmptyMessage(json["data"]["message"].stringValue)
                           // ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: json["data"]["message"].stringValue)
                        }
                    }
                    
            }
        })
        task.resume()
    }
    
    fileprivate func parseData(_ json:JSON){
        for result in json["data"]["creditmemo"].arrayValue{
            var data = [String:String]()
            data["increment_id"] = result["increment_id"].stringValue
            data["billing_name"]  = result["billing_name"].stringValue
            data["total_qty"]  = result["total_qty"].stringValue
            data["state"] = result["state"].stringValue
            data["grand_total"] = result["vendor_payment"].stringValue
            data["created_at"]  = result["created_at"].stringValue
            self.invoiceData.append(data)
        }
    }
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let data = invoiceData[indexPath.row]
        let view = storyboard?.instantiateViewController(withIdentifier: "creditmemoView") as? ced_creditmemoView
        view?.creditMemoId = data["increment_id"]!
        self.navigationController?.pushViewController(view!, animated: true)
    }

    
  

}
