//
//  listAuctionViewController.swift
//  VenderApp
//
//  Created by MacMini on 22/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class listAuctionViewController: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource {

    
    
    @IBOutlet weak var topLabel: UILabel!
    
    
    
    
    @IBOutlet weak var tableView: UITableView!
    
    
    var auctioned_product=[[String:JSON]]()
    var pageNo=1
    override func viewDidLoad() {
        super.viewDidLoad()
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        topLabel.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        tableView.dataSource=self
        tableView.delegate=self
        getListingData()
        
        
        
    }

    
    @objc func getListingData(){
        if flagForPagination{
            let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&page=\(pageNo)"
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
            self.sendRequest(url: "vauctionapi/auction/item", parameters: postString)
            
        }
        
    }
    
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return auctioned_product.count
    }
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell=tableView.dequeueReusableCell(withIdentifier: "listAuctionCell") as! listAuctionCell
        let data=auctioned_product[indexPath.row]
        /*
         "product_id":"2049",
         "max_price":"10000.0000",
         "status":"BidClosed",
         "id":"3",
         "end_datetime":"2018-01-23 11:54:00",
         "sellproduct":"No",
         "vendor_auction_status":"Approved",
         "starting_price":"54.0000",
         "start_datetime":"2018-01-21 11:45:00",
         "product_name":"test"
         */
        cell.viewButton.tag=Int((data["id"]?.stringValue)!)!
        cell.deleteButton.tag=Int((data["id"]?.stringValue)!)!
        cell.editButton.tag=Int((data["id"]?.stringValue)!)!
        cell.viewButton.addTarget(self, action: #selector(viewButtonPressed(_:)), for: .touchUpInside)
        cell.deleteButton.addTarget(self, action: #selector(deleteButtonPressed(_:)), for: .touchUpInside)
        cell.editButton.addTarget(self, action: #selector(editButtonPressed(_:)), for: .touchUpInside)
        cell.viewForCard.cardView()
        cell.productId.text=data["product_id"]?.stringValue
        cell.productName.text=data["product_name"]?.stringValue
        cell.startPrice.text=data["starting_price"]?.stringValue
        cell.maxPrice.text=data["max_price"]?.stringValue
        cell.startDate.text=data["start_datetime"]?.stringValue
        cell.endDate.text=data["end_datetime"]?.stringValue
        cell.sellProduct.text=data["sellproduct"]?.stringValue
        cell.status.text=data["status"]?.stringValue
        cell.vpaStatus.text=data["vendor_auction_status"]?.stringValue
        
        
        
        return cell
    }
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        
        if flagForPagination{
            
            //Alert_File.addLoadingIndicator(self, msg: "Please Wait....")
            pageNo+=1
            getListingData()
        }
        
    }
    @objc func viewButtonPressed(_ sender: UIButton){
        
        var productId=""
        for item in auctioned_product{
            if item["id"]?.stringValue==String(sender.tag){
                productId=(item["product_id"]?.stringValue)!
            }
        }
        auction_id=String(sender.tag)
        
        let vc=UIStoryboard(name: "auctionStoryboard", bundle: nil).instantiateViewController(withIdentifier: "bidViewController") as! bidViewController
        
        vc.productId=productId
        vc.auction_id=auction_id
        self.navigationController?.pushViewController(vc, animated: true)
        
        
    }
    var auction_id=""
    @objc func editButtonPressed(_ sender: UIButton){
        auction_id=String(sender.tag)
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&auction_id=\(sender.tag)"
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: "vauctionapi/auction/info", parameters: postString)
    }
    @objc func deleteButtonPressed(_ sender: UIButton){
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&id=\(sender.tag)"
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: "vauctionapi/auction/delete", parameters: postString)
    }
    var flagForPagination=true
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            if requestUrl=="vauctionapi/auction/item"{
                
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if json["data"]["success"].stringValue=="true"{
                    if let auctionedProduct=json["data"]["auctioned_product"].array{
                        for item in auctionedProduct{
                            auctioned_product.append(item.dictionary!)
                        }
                        tableView.restore()
                        tableView.reloadData()
                        
                    }
                }
                else
                {
                    flagForPagination=false
                    if(pageNo == 1)
                    {
                       // self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center);
                        self.tableView.setEmptyMessage(json["data"]["message"].stringValue)
                        Ced_CommonVendor.delay(2.0, closure: {
                            self.navigationController?.popViewController(animated: true);
                        })
                    }
                }
            }
            else if requestUrl=="vauctionapi/auction/delete"{
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if json["data"]["success"].stringValue=="true"{
                    //ShowSimpleAlert.showSimpleAlert(self, showTitle: "", showMsg: json["data"]["message"].stringValue)
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                    Ced_CommonVendor.delay(2.0, closure: {
                        self.pageNo=1
                        self.flagForPagination=true
                        self.getListingData()
                    })
                    
                    
                }
                else
                {
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                }
            }
            else if requestUrl=="vauctionapi/auction/info"{
               guard let json = try? JSON(data: data) else {return}
                print(json)
                var info=[String:JSON]()
                if json["data"]["success"].stringValue=="true"{
                    if let auctionInfo=json["data"]["auction_info"].dictionary{
                        info.updateValue(auctionInfo["max_price"]!, forKey: "max_price")
                        info.updateValue(auctionInfo["status"]!, forKey: "status")
                        info.updateValue(auctionInfo["end_datetime"]!, forKey: "end_datetime")
                        info.updateValue(auctionInfo["starting_price"]!, forKey: "starting_price")
                        info.updateValue(auctionInfo["start_datetime"]!, forKey: "start_datetime")
                        info.updateValue(auctionInfo["product_id"]!, forKey: "product_id")
                        info.updateValue(auctionInfo["product_name"]!, forKey: "product_name")
                    }
                    let viewControl=UIStoryboard(name: "auctionStoryboard", bundle: nil).instantiateViewController(withIdentifier: "addAuctionViewController") as! addAuctionViewController
                    viewControl.proId=(info["product_id"]?.stringValue)!
                    viewControl.proName=(info["product_name"]?.stringValue)!
                    viewControl.info=info
                    viewControl.isEdit=true
                    viewControl.auction_id=auction_id
                    self.navigationController?.pushViewController(viewControl, animated: true)
                }
                else
                {
                    if json["data"]["success"].stringValue=="false"
                    {
                        self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center);
                        
                        
                    }
                }
                
            }
        }
        
    }
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 440
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
