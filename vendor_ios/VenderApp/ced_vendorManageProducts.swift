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

class ced_vendorManageProducts: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate {
    var del_button_tag:Int = 0
    @IBOutlet weak var vendormangeProductTable: UITableView!
    var pages: Int!;
    var productData = [[String:String]]()
    override func viewDidLoad() {
        super.viewDidLoad()
        vendormangeProductTable.delegate = self
        vendormangeProductTable.dataSource = self
        ced_navigationBarController().addNavButton(self,str:"no")
        self.loadProductData(1)
        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func loadProductData(_ page:Int){

        pages=page;
        var baseUrl = "";
        baseUrl += "vendorapi/vproducts/item/page=" + "\(page)"

        //let userData = defaults.objectForKey("userInfoDict") as! NSDictionary
        //let vendorId = userData["vendorId"] as! String
        //let hashKey    = userData["hashKey"] as! String
        
        let vendorId="48";
        let hashKey = "d91f3f8359c82130a2bf48a2cb6c268e";
        
        
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey

        self.sendRequest(url: baseUrl, parameters: postString)
        
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            if(requestUrl=="vendorapi/vproducts/delete")
            {
               guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["success"].stringValue.lowercased() == "false"){
                    self.view.showToastMsg(json["message"].stringValue)
                }else if(json["success"].stringValue.lowercased() == "true"){
                    self.view.showToastMsg(json["message"].stringValue)
                    let indexPath = IndexPath(item: del_button_tag, section: 0)
                    self.productData.remove(at: del_button_tag)
                    self.vendormangeProductTable.deleteRows(at: [indexPath], with: UITableView.RowAnimation.right)
                    self.loadProductData(1)
                }
                
            }
            else
                
            {
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue.lowercased() == "false"){
                   // self.view.showToastMsg(json["data"]["message"].stringValue)
                    self.vendormangeProductTable.setEmptyMessage(json["data"]["message"].stringValue)
                }else if(json["data"]["success"].stringValue.lowercased() == "true"){
                    self.parseProductdata(json)
                }
                
            }
        }
        


    }
    
    func parseProductdata(_ json:JSON){
        for result in json["data"]["products"].arrayValue{
            let productId = result["product_id"].stringValue
            let status    = result["status"].stringValue
            let regular_price = result["vendor_price"].stringValue
            let sku  = result["sku"].stringValue
            let product_image = result["product_image"].stringValue
            let type = result["type"].stringValue
            let qty  = result["qty"].stringValue
            let product_name = result["product_name"].stringValue
            let product = ["productId":productId,"status":status,"vendor_price":regular_price,"sku":sku,"product_image":product_image,"type":type,"qty":qty,"product_name":product_name]
            self.productData.append(product)
            self.vendormangeProductTable.restore()
            self.vendormangeProductTable.reloadData()
        }
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return productData.count
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = vendormangeProductTable.dequeueReusableCell(withIdentifier: "manageProduct") as! ced_manageproductcell
        cell.productId.text = "#" + productData[indexPath.row]["productId"]!
        cell.productName.text = productData[indexPath.row]["product_name"]
        cell.productPrice.text = productData[indexPath.row]["vendor_price"]
        cell.productqty.text = productData[indexPath.row]["qty"]
        cell.productStatus.text = productData[indexPath.row]["status"]
        cell.productType.text = productData[indexPath.row]["type"]
        cell.deleteProduct.addTarget(self, action: #selector(ced_vendorManageProducts.delProduct(_:)), for: UIControl.Event.touchUpInside)
        cell.deleteProduct.tag = indexPath.row
        cell.editProduct.addTarget(self, action: #selector(ced_vendorManageProducts.editProduct(_:)), for: UIControl.Event.touchUpInside)
        cell.editProduct.tag = indexPath.row
        let url = productData[indexPath.row]["product_image"]

        let request = URLRequest(url: URL(string: url!)!)
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
                if let data = data{
                    let image = UIImage(data: data)
                    cell.productImageView.image = image
                }
                    
            }
        })
        task.resume()
        cell.editProduct.setThemeColor()
        return cell
    }
    
    @objc func delProduct(_ sender:UIButton){
        let product_id = productData[sender.tag]["productId"]!
        //var baseUrl = Ced_CommonVendor.getInfoPlist("baseUrl") as? String
        let baseUrl = "vendorapi/vproducts/delete"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "entity_id=" + product_id + "&vendor_id=" + vendorId
        postString   +=  "&hashkey=" + hashKey

        del_button_tag=sender.tag
        self.sendRequest(url: baseUrl, parameters: postString)

        
    }
    
    
    @objc func editProduct(_ sender:UIButton){
        
    }
    
    
}
