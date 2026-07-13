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

class ced_viewproductReports: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate
{
    
    var forTheFirstTime = true;
    @IBOutlet weak var productReportTableView: UITableView!
    let refreshControl = UIRefreshControl()
    var baseURL = String();
    var postString = String();
    var productListingdata = [[String:String]]();
    var page = 1;
    var loadMoreData = true;
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        productReportTableView.backgroundColor = DynamicColor.ViewBackgroundColor
        self.title = "Product Reports".localized
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        // Do any additional setup after loading the view.
        self.productReportTableView.dataSource = self;
        self.productReportTableView.delegate = self;
    ced_navigationBarController().addNavButton(self,str:"back")
        self.fetchOrderListingData();
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        productReportTableView.refreshControl = refreshControl
    }

    override func didReceiveMemoryWarning()
    {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func refresh(_ sender: AnyObject) {
        self.page = 1
        self.productListingdata.removeAll()
        productReportTableView.reloadData()
        fetchOrderListingData()
    }
    
    // MARK: - Table view data source
    
    @objc func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return self.productListingdata.count;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = productReportTableView.dequeueReusableCell(withIdentifier: "productReportListingCell", for: indexPath) as! ProductReportListingCell;
        
        cell.productText.text = "Product : ".localized
        cell.productValue.text = self.productListingdata[indexPath.row]["product_name"];
        
        cell.skuLabel.text = "SKU : ".localized
        cell.skuValue.text = self.productListingdata[indexPath.row]["sku"];
        
        cell.saleLabel.text = "Sales Items : ".localized
        cell.saleValue.text = self.productListingdata[indexPath.row]["sales_items"];
        
        cell.totalSaleLabel.text = "Total Sales : ".localized
        cell.totalSaleValue.text = self.productListingdata[indexPath.row]["total_sales"];
        
        cell.containerView.makeCardUsingThemeColor(cell.containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
        
        return cell;
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return 160;
    }

    
    @objc func fetchOrderListingData()
    {
        //var baseURL = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
        let baseURL = "vendorapi/vreport/report";
        
        print(baseURL);
        

        //var request = URLRequest(url: URL(string: baseURL)!);

        
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let postStringToSend = self.postString+"&page="+String(self.page);
        self.sendRequest(url: baseURL, parameters: postStringToSend)

    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        refreshControl.endRefreshing()
        if let data = data{
            guard let jsonData = try? JSON(data: data) else {return}
            print(jsonData);
            Alert_File.removeLoadingIndicator(self)
            //print(jsonData["data"]["success"]);
            
            if(self.forTheFirstTime)
            {
                if( jsonData["data"]["success"].stringValue != "true")
                {
                    let title = "Nothing Found".localized
                    let message = "No Data Available".localized
                   // Alert_File.showValidationError(self,title:title,message:message);
                    self.productReportTableView.setEmptyMessage(message)
                }
            }
            
            self.forTheFirstTime = false;
            
            if( jsonData["data"]["success"].stringValue == "true")
            {
                
                for val in jsonData["data"]["product_report"].arrayValue
                {
                    var tempArray = [String:String]();
                    tempArray["sales_items"] = val["sales_items"].stringValue;
                    tempArray["total_sales"] = val["total_sales"].stringValue;
                    tempArray["product_name"] = val["product_name"].stringValue;
                    tempArray["sku"] = val["sku"].stringValue;
                    
                    self.productListingdata.append(tempArray);
                }
                print(self.productListingdata);
                self.productReportTableView.restore()
                self.productReportTableView.reloadData();
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
