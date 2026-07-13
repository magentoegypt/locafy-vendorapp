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

class ced_shipmentListingmanage: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource {

    @IBOutlet weak var tableView: UITableView!
    let refreshControl = UIRefreshControl()
    var orderId = String()
    var invoiceData = [[String:String]]()
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        self.title = "Shipments"
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
        cell?.invoiceId.text = invoiceData[indexPath.row]["shipmentId"]
        cell?.invoiceDate.text = invoiceData[indexPath.row]["created_at"]
        cell?.billingName.text = invoiceData[indexPath.row]["shipping_name"]
        cell?.amount.text = invoiceData[indexPath.row]["total_qty"]
        return cell!
    }
    
    
    @objc func loadData(_ page:Int){
        var baseUrl = settings.baseUrl
        
        baseUrl += "vorderapi/vorders/viewshipment/"
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        if(orderId != ""){
            postString += "&order_id="+orderId
        }
        postString  += "&page="+"\(page)"
        var request =  URLRequest(url: URL(string:baseUrl)!)
        request.httpMethod = "POST"
        request.httpBody = postString.data(using: String.Encoding.utf8)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
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
                Alert_File.removeLoadingIndicator(self)
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
                Alert_File.removeLoadingIndicator(self)
                    if let data = data{
                        guard let json = try? JSON(data: data) else {return}
                        print(json)
                        if(json["data"]["success"].stringValue == "true"){
                            self.parseData(json)
                            self.tableView.reloadData()
                            
                        }else{
                           // ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: json["data"]["message"].stringValue)
                            self.tableView.setEmptyMessage(json["data"]["message"].stringValue)
                        }
                    }
                    
            }
        })
        task.resume()
    }
    
    fileprivate func parseData(_ json:JSON){
        for result in json["data"]["shipment"].arrayValue{
            var data = [String:String]()
           
            data["shipmentId"] = result["increment_id"].stringValue
            data["shipping_name"]  = result["shipping_name"].stringValue
            data["total_qty"]  = result["total_qty"].stringValue
            data["created_at"] = result["created_at"].stringValue
            self.invoiceData.append(data)
        }
    }
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let data = invoiceData[indexPath.row]
        let view = storyboard?.instantiateViewController(withIdentifier: "shipmentView") as? ced_shipmentView
        view?.shipmentId = data["shipmentId"]!
        self.navigationController?.pushViewController(view!, animated: true)
    }

    


}
