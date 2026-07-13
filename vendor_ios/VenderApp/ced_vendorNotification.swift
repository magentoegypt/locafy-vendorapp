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

class ced_vendorNotification: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource, UIScrollViewDelegate {

    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var noNotificationView: UIImageView!
    
    var notifdata = [[String:String]]()
    var orderData = [[String:String]]()
    var page = 1;
    var loading = true;
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.delegate = self
        tableView.dataSource = self
        tableView.tableFooterView = UIView(frame: CGRect(x: 0, y: 0, width: 0, height: 0))
        self.title = "Vendor Notification".localized
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        ced_navigationBarController().addNavButton(self,str:"no".localized)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        
        // Do any additional setup after loading the view.
        self.loadNotif()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }

    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(section == 0)
        {
            return notifdata.count
        }
        else
        {
            return orderData.count;
        }
        
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if(indexPath.section == 0)
        {
            let cell = tableView.dequeueReusableCell(withIdentifier: "notifcell") as! ced_notifCell
            cell.imageView?.image = UIImage(named: "ic_action_name")
            cell.imageView?.contentMode = .scaleAspectFit
            cell.textLabel?.font=UIFont(name: "Roboto-Medium",size: 17)
            cell.textLabel?.text = notifdata[indexPath.row]["tag"]
            cell.contentMode = UIView.ContentMode.center
            return cell
        }
        else
        {
            let cell = tableView.dequeueReusableCell(withIdentifier: "orderCell") as! ced_notifCell
            cell.imageView?.image = UIImage(named: "ic_action_name")
            cell.imageView?.contentMode = .scaleAspectFit
            cell.textLabel?.font=UIFont(name: "Roboto-Medium",size: 17)
            cell.textLabel?.text = orderData[indexPath.row]["title"]! + "\n" + orderData[indexPath.row]["created_at"]!;
            cell.contentMode = UIView.ContentMode.center
            return cell
        }
        
    }
    
    @objc func loadNotif(){
        //var baseUrl = Ced_CommonVendor.getInfoPlist("baseUrl") as? String
        //let baseUrl = "vendorapi/index/vendornotification/"
        let baseUrl = "vendorapi/index/notification"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey + "&page=\(page)";
        self.sendRequest(url: baseUrl, parameters: postString)

    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            self.parseData(json)
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            //print(userData)
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            let customer_id = userData["customerId"] as! String;
            let vendor_name = userData["vendorName"] as! String;
            let profile_picture = userData["profilePicUrl"] as! String;
            let profile_complete = json["profile_complete"].stringValue
            let valerts = json["alert_count"].stringValue
            //saving value in NSUserDefault to use later on :: start
            let dict = ["customerId": customer_id, "hashKey": hashKey,"vendorName":vendor_name,"vendorId":vendorId,"profilePicUrl":profile_picture,"valerts":valerts,"profile_complete":profile_complete];
            self.defaults.set(dict, forKey: "userInfoDict");
            
            if(json["alert_count"].stringValue == "0"){
                self.tableView.isHidden = true
                self.noNotificationView.isHidden = false
            }else{
                
                self.tableView.reloadData()
            }
        }
        
    }
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 40
    }
    
    func parseData(_ json:JSON){
        if(page == 1)
        {
            for data in json["vendorAlert"].arrayValue {
                let tag = data["tag"].stringValue
                let link_to = data["link_to"].stringValue
                let object = ["tag":tag,"link_to":link_to]
                self.notifdata.append(object)
            }
            
        }
        for data in json["orderAlert"].arrayValue{
            self.orderData.append(["referenceId":data["reference_id"].stringValue,"title":data["title"].stringValue,"created_at":data["created_at"].stringValue]);
        }
        
    }
   
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if(indexPath.section == 0)
        {
            let link_to = notifdata[indexPath.row]["link_to"]
            let tag = notifdata[indexPath.row]["tag"]
            if(tag?.lowercased() == "Add Profile Picture ".lowercased() || tag?.lowercased() == "إضافة صورة الملف الشخصي".lowercased()){
                self.sideDrawerViewController?.toggleDrawer()
                NotificationCenter.default.post(name: NSNotification.Name("openGallaryNotif"),object: nil);
            }else if(link_to?.lowercased() == "Vendor Profile".lowercased() || link_to?.lowercased() == "الملف الشخصي للبائع".lowercased()){
                let stoy = UIStoryboard(name: "Main", bundle: nil)
                
                let viewControl = stoy.instantiateViewController(withIdentifier: "vendorProfileViewController") as!  VendorProfileViewController
                self.navigationController?.pushViewController(viewControl, animated: true)
            }else if(link_to?.lowercased() == "Payment Setting".lowercased()){
                let stoy = UIStoryboard(name: "Main", bundle: nil)
                
                let viewControl = stoy.instantiateViewController(withIdentifier: "vendorTransactionSettings") as!  VendorTransactionSettings
                self.navigationController?.pushViewController(viewControl, animated: true)
            }else if(link_to?.lowercased() == "Shipping Origin".lowercased()){
                let stoy = UIStoryboard(name: "Main", bundle: nil)
                
                let viewControl = stoy.instantiateViewController(withIdentifier: "shippingSettingController") as!  ShippingSettingController
                self.navigationController?.pushViewController(viewControl, animated: true)
            }else if(link_to?.lowercased() == "Shipping Method".lowercased()){
                let stoy = UIStoryboard(name: "Main", bundle: nil)
                
                let viewControl = stoy.instantiateViewController(withIdentifier: "shippingMethodsController") as!  ShippingMethodsController
                self.navigationController?.pushViewController(viewControl, animated: true)
            }
            else if(link_to?.lowercased() == "Add New Product".lowercased() || link_to?.lowercased() == "إضافة منتج جديد".lowercased()){
                let stoy = UIStoryboard(name: "ProductAddon", bundle: nil)
                
                let viewControl = stoy.instantiateViewController(withIdentifier: "addProductAddonFirstPage") as!  AddProductAddonFirstPage
                self.navigationController?.pushViewController(viewControl, animated: true)
            }
        }
        else
        {
            let item = orderData[indexPath.row]
            let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
            let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "manageOrders") as! ced_manageProduct
            viewcontrol.orderId = item["referenceId"]!
            self.navigationController?.pushViewController(viewcontrol, animated: true)
            /*let view = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "vendorSingleorder") as? ced_vendorSingleorder
            view!.orderId = item["referenceId"]!
            self.navigationController?.pushViewController(view!, animated: true)*/
        }
        
    }
    
    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
                self.page += 1;
                loadNotif()
                //self.mainTableView.reloadSections(NSIndexSet(index: 2) as IndexSet, with: .none)
                //mainTableView.reloadData();
            }
            
        }
        
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
