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

class ced_manageProduct: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UIActionSheetDelegate {

    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var button: UIButton!
    @IBOutlet weak var tableView: UITableView!
  
    var orderId = String()
    var array = ["Information","Invoices","Credit Memos","Shipments"] //,"Comments History"
    var slaButton = [String]()
    var EnabledButtons = [String:String]()
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        tableView.delegate = self
        tableView.dataSource = self
    ced_navigationBarController().addNavButton(self,str:"back")
        initializeData()
    }
    
    func initializeData()
    {
        array = ["Information","Invoices","Credit Memos","Shipments"]
        slaButton = [String]()
        EnabledButtons = [String:String]()
        self.button.addTarget(self, action: #selector(ced_manageProduct.ShowOptions(_:)), for: UIControl.Event.touchUpInside)
        button.setBackgroundImage(UIImage(named: "filtericon")?.withRenderingMode(.alwaysTemplate), for: .normal)
        button.tintColor = UIColor.white
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        containerView.backgroundColor = color
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.loadData()
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    @objc func loadData(){
        var baseUrl = settings.baseUrl
        baseUrl += "vorderapi/vorders/viewinitOrder"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString  += "&order_id="+orderId
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
        let task =  session.dataTask(with: request, completionHandler: {
            data,response,error in

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
                        if(self.EnabledButtons.count == 0){
                            self.button.isHidden = true
                        }
                    }else{
                       // self.view.showToastMsg("Problem in loading Data")
                        self.tableView.setEmptyMessage(json["data"]["message"].stringValue)
                    }
                }
            }
        })
        task.resume()

    }
    
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return array.count
    }

    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "manageProdcut") as? ced_manageProducts
        cell!.titleText!.text = array[indexPath.row].localized
        cell?.titleText.setThemeColor()
        return cell!
    }
  
    func parseData(_ json:JSON){
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let customer_id = userData["customerId"] as! String;
        let vendor_name = userData["vendorName"] as! String;
        let profile_picture = userData["profilePicUrl"] as! String;
        let valerts = json["data"]["valerts"].stringValue
        let profile_complete = userData["profile_complete"] as! String;
        //saving value in NSUserDefault to use later on :: start
        let dict = ["customerId": customer_id, "hashKey": hashKey,"vendorName":vendor_name,"vendorId":vendorId,"profilePicUrl":profile_picture,"valerts":valerts,"profile_complete":profile_complete];
        self.defaults.set(dict, forKey: "userInfoDict");
        for result in json["data"]["ordersData"].arrayValue{
            for buttondata in result["buttons"].arrayValue{
                EnabledButtons[buttondata["label"].stringValue] = buttondata["value"].stringValue
            }
            topLabel.text = result["header_meassage"].stringValue
        }
        if json["data"]["sla_button"].arrayValue.count > 0 {
             for buttondata in json["data"]["sla_button"].arrayValue{
                slaButton.append(buttondata.stringValue)
            }
           // self.slaActions()
        }
    }
    
    @objc func slaActions(){
        if slaButton.count > 0 {
            let slaViewControl = UIAlertController(title: nil, message: "Choose action for new order.", preferredStyle: .alert)
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            //print(userData)
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
            postString  += "&order_id="+orderId
            if slaButton.contains("Confirm") {
            let confirmAction = UIAlertAction(title: "Confirm", style: .default, handler: { Void in
          
                self.sendRequest(url: "vslapi/order/confirm", parameters: postString)
                
            })
            slaViewControl.addAction(confirmAction)
            }
              if slaButton.contains("Cancel") {
            let cancelAction = UIAlertAction(title: "Cancel", style: .cancel, handler: { Void in
                   self.sendRequest(url: "vslapi/order/cancel", parameters: postString)
            })
             slaViewControl.addAction(cancelAction)
            }
            self.present(slaViewControl, animated: true, completion: {
                self.navigationController?.popViewController(animated: true)
            })
        }
    }
    
    
   override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data {
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if requestUrl == "vslapi/order/confirm" {
                if json["data"]["success"].stringValue == "true" {
                     ShowSimpleAlert.showSimpleAlert(self, showTitle: "Success", showMsg: json["data"]["message"].stringValue)
                }else{
                      ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: json["data"]["message"].stringValue)
                }
            }else{
                if json["data"]["success"].stringValue == "true" {
                
                    self.navigationController?.popViewController(animated: true)
                }else{
                    ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: json["data"]["message"].stringValue)
                }
            }
            
        }
        
    }
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let data = array[indexPath.row]
        if(data.lowercased() == "Information".lowercased()){
            let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
            let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "advanceOrderView") as! ced_advanceOrderView
            viewcontrol.orderId = orderId
            self.navigationController?.pushViewController(viewcontrol, animated: true)
            
        }else if(data.lowercased() == "Invoices".lowercased()){
            
            let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
            let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "manageOrderInVoice") as! ced_manageOrderinvoiceListViewController
            viewcontrol.orderId = orderId
            self.navigationController?.pushViewController(viewcontrol, animated: true)
            
            
        }else if(data.lowercased() == "Credit Memos".lowercased()){
            let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
            let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "ced_credtmemosList") as! ced_creditmemosListing
            viewcontrol.orderId = orderId
            self.navigationController?.pushViewController(viewcontrol, animated: true)

            
        }else if(data.lowercased() == "SHIPMENTS".lowercased()){
            let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
            let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "ced_shipmentMange") as! ced_shipmentListingmanage
            viewcontrol.orderId = orderId
            self.navigationController?.pushViewController(viewcontrol, animated: true)

        }else if(data.lowercased() == "COMMENTS HISTORY".lowercased()){
            let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
            let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "ced_commentsListing") as! ced_commentsListing
            viewcontrol.orderId = orderId
            self.navigationController?.pushViewController(viewcontrol, animated: true)
            
        }
    }
    
    
    //SHow CReations options start
    
    @objc func ShowOptions(_ sender:UIButton){
        if #available(iOS 8.0, *) {
            let actionsheet = UIAlertController(title: "Choose Action".localized, message: nil, preferredStyle: UIAlertController.Style.actionSheet)
            for (buttons,key) in self.EnabledButtons {
                let action = UIAlertAction(title: buttons, style: UIAlertAction.Style.default, handler: {
                    void in
                    if(key == "invoice"){
                        let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
                        let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "invoicecreation") as! ced_creationOfinvoice
                        viewcontrol.invoiceId = self.orderId
                        viewcontrol.vc = self;
                        self.navigationController?.pushViewController(viewcontrol, animated: true)
                        
                    }else if(key == "ship"){
                        let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
                        let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "createShipment") as! ced_creationOfshipment
                        viewcontrol.invoiceId = self.orderId
                        viewcontrol.vc = self;
                        self.navigationController?.pushViewController(viewcontrol, animated: true)
                    }else if(key == "creditmemo"){
                        let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
                        let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "creditMemocreation") as! ced_creationOfcreditMemos
                        viewcontrol.invoiceId = self.orderId
                        viewcontrol.vc = self;
                        self.navigationController?.pushViewController(viewcontrol, animated: true)
                    }
                })
                actionsheet.addAction(action)
            }
            let cancelAct = UIAlertAction(title: "Cancel".localized, style: UIAlertAction.Style.destructive, handler: nil)
            actionsheet.addAction(cancelAct)
            if(UIDevice().model.lowercased() == "ipad".lowercased()){
                actionsheet.popoverPresentationController?.sourceView = sender
            }
          //  self.present(actionsheet, animated: true, completion: nil)
        } else {
            // Fallback on earlier versions
        }
        /*let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
        let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "createShipment") as! ced_creationOfshipment
        viewcontrol.invoiceId = self.orderId
        viewcontrol.vc = self;
        self.navigationController?.pushViewController(viewcontrol, animated: true)*/
        /*let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
        let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "creditMemocreation") as! ced_creationOfcreditMemos
        viewcontrol.invoiceId = self.orderId
        viewcontrol.vc = self;
        self.navigationController?.pushViewController(viewcontrol, animated: true)*/
    }
    
    
}
