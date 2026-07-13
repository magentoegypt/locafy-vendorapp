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

class ced_viewOrdersReports: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate
{
    var forTheFirstTime = true;
    
    @IBOutlet weak var orderReportTableView: UITableView!
    
    let refreshControl = UIRefreshControl()
    var postString = String();
    var payment_state:String = "";
    var orderListingdata = [[String:String]]();
    var page = 1;
    var loadMoreData = true;
    
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.title = "Orders Report".localized
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        self.orderReportTableView.dataSource = self;
        self.orderReportTableView.delegate = self;
        
        ced_navigationBarController().addNavButton(self,str:"back")
        
        self.fetchOrderListingData();
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        orderReportTableView.refreshControl = refreshControl
    }

    override func didReceiveMemoryWarning()
    {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func refresh(_ sender: AnyObject) {
        self.page = 1
        self.orderListingdata.removeAll()
        orderReportTableView.reloadData()
        fetchOrderListingData()
    }
    // MARK: - Table view data source
    
    @objc func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return self.orderListingdata.count;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = orderReportTableView.dequeueReusableCell(withIdentifier: "orderReportListingCell", for: indexPath) as! OrderReportListingCell;
        
        cell.periodText.text = "Period : ".localized;
        cell.periodValue.text = self.orderListingdata[indexPath.row]["period"];
        
        cell.orderLabel.text = "Orders : ".localized;
        cell.orderValue.text = self.orderListingdata[indexPath.row]["orders"];
        cell.saleLabel.text = "Sales Items : ".localized;
        cell.saleValue.text = self.orderListingdata[indexPath.row]["sales_items"];
        
        cell.totalSaleLabel.text = "Subtotal".localized;
        cell.totalSaleValue.text = self.orderListingdata[indexPath.row]["total_sales"];
        
        cell.comissionLabel.text = "Total Commission : ".localized;
        cell.comissionValue.text = self.orderListingdata[indexPath.row]["total_commision"];
        
        cell.netSaleLabel.text = "Net Sales : ".localized;
        cell.netSaleValue.text = self.orderListingdata[indexPath.row]["net_sales"];
        
        cell.containerView.makeCardUsingThemeColor(cell.containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
        
        return cell;
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return 135;
    }

    
    @objc func fetchOrderListingData()
    {
        //var baseURL = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
        let baseURL = "vendorapi/vreport/order";
        
        print(baseURL);
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let postStringToSend = self.postString+"&page="+String(self.page);
        print(postStringToSend)
        self.sendRequest(url: baseURL, parameters: postStringToSend)

    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        refreshControl.endRefreshing()
        if let data = data{
            guard let jsonData = try? JSON(data: data) else {return}
            Alert_File.removeLoadingIndicator(self)
            //print(jsonData);
            
            //print(jsonData["data"]["success"]);
            print(jsonData)
            if(self.forTheFirstTime)
            {
                if( jsonData["data"]["success"].stringValue != "true")
                {
                    let title = "Nothing Found".localized;
                    let message = "No Data Available".localized;
                    Alert_File.showValidationError(self,title:title,message:message)
                    self.navigationController?.popViewController(animated: true)
                }
            }
            
            self.forTheFirstTime = false;
            
            if( jsonData["data"]["success"].stringValue == "true")
            {
                
                for val in jsonData["data"]["order_report"].arrayValue
                {
                    var tempArray = [String:String]();
                    tempArray["sales_items"] = val["sales_items"].stringValue;
                    tempArray["period"] = val["period"].stringValue;
                    tempArray["total_commision"] = val["total_commision"].stringValue;
                    tempArray["total_sales"] = val["net_sales"].stringValue;
                    tempArray["orders"] = val["orders"].stringValue;
                    tempArray["net_sales"] = val["net_sales"].stringValue;
                    
                    self.orderListingdata.append(tempArray);
                }
                
                print(self.orderListingdata);
                
                self.orderReportTableView.reloadData();
            }
            else
            {
                self.loadMoreData = false;
            }
        }
        
    }
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40
        {
            if(self.loadMoreData && !self.refreshControl.isRefreshing)
            {
                print("deve");
                self.page += 1;
                self.fetchOrderListingData();
                
            }
        }
        
    }
    
    
}
