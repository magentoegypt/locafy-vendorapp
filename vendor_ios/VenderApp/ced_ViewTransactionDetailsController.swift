//
//  ced_ViewTransactionDetailsController.swift
//  VenderApp
//
//  Created by Saumya Kashyap on 23/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_ViewTransactionDetailsController: ced_VendorBaseClass {
    
    
    @IBOutlet weak var scrollerView: UIScrollView!
    
    @IBOutlet weak var scrollerViewWidth: NSLayoutConstraint!
    
    //mainview
    @IBOutlet weak var transactionDetailView: UIView!
    
    @IBOutlet weak var topLabel1: UILabel!
    
    @IBOutlet weak var mainViewWidth: NSLayoutConstraint!
    @IBOutlet weak var mainViewHeight: NSLayoutConstraint!
    //orderinfoview
    @IBOutlet weak var oiVendor: UILabel!
    @IBOutlet weak var oiOrderID: UILabel!
    @IBOutlet weak var oiPaymentMode: UILabel!
    @IBOutlet weak var oiGrandTotal: UILabel!
    @IBOutlet weak var oiShippingFee: UILabel!
    
    @IBOutlet weak var topLabel2: UILabel!
    @IBOutlet weak var orderInfoVIew: UIView!
    @IBOutlet weak var vendor_name: UILabel!
    @IBOutlet weak var order_id: UILabel!
    @IBOutlet weak var paymentMode: UILabel!
    @IBOutlet weak var grandTotal: UILabel!
    @IBOutlet weak var shippingFee: UILabel!
    //feedetails
    @IBOutlet weak var fdCommisionFee: UILabel!
    @IBOutlet weak var fdShippingAmount: UILabel!
    @IBOutlet weak var fdFixedFee: UILabel!
    @IBOutlet weak var fdCollectionFee: UILabel!
    @IBOutlet weak var fdServiceTax: UILabel!
    
    
    @IBOutlet weak var topLabel3: UILabel!
    @IBOutlet weak var feeDetailsView: UIView!
    @IBOutlet weak var commissionfee: UILabel!
    @IBOutlet weak var shippingAmount: UILabel!
    @IBOutlet weak var fixedFee: UILabel!
    @IBOutlet weak var collectionFee: UILabel!
    @IBOutlet weak var serviceTax: UILabel!
    //amountview
    @IBOutlet weak var topLabel4: UILabel!
    @IBOutlet weak var amountView: UIView!
    @IBOutlet weak var amount: UILabel!
    
    @IBOutlet weak var amountTitle: UILabel!
    var paymentID=""
    var orderID=""
    var transactionId=""
    var DataView = [String:String]()
    override func viewDidLoad() {
        super.viewDidLoad()
        /*static text setup*/
        amountTitle.text="Amount".localized
        oiVendor.text="Vendor".localized
        oiOrderID.text="Order Id".localized
        oiPaymentMode.text="Payment Mode".localized
        oiGrandTotal.text="GrandTotal".localized
        oiShippingFee.text="Shipping Fee of Order".localized
      //  fdCommisionFee.text="Commission Fee".localized
        fdShippingAmount.text="Shipping Amount".localized
        fdFixedFee.text="Fixed Fee".localized
        fdCollectionFee.text="Collection Fee".localized
        fdServiceTax.text="Service Tax".localized
        
        self.title = "Transaction Details".localized
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        topLabel1.backgroundColor = color
        topLabel2.backgroundColor = color
        topLabel3.backgroundColor = color
        topLabel4.backgroundColor = color
        topLabel1.text="Transaction Details".localized
        topLabel2.text="ORDER INFORMATIONS".localized
        topLabel3.text="FEE DETAILS".localized
        topLabel4.text="AMOUNT".localized
        scrollerViewWidth.constant = self.view.frame.width
        
        mainViewWidth.constant = self.view.frame.width
        mainViewHeight.constant = orderInfoVIew.frame.height + feeDetailsView.frame.height + amountView.frame.height + 100
        transactionDetailView.makeCard(transactionDetailView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        orderInfoVIew.makeCard(orderInfoVIew, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        feeDetailsView.makeCard(feeDetailsView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        amountView.makeCard(amountView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        loadData()
        // Do any additional setup after loading the view.
    }
    @objc func loadData(){
        let baseUrl = "vadvtransactionapi/transaction/details"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey + "&payment_id=" + paymentID + "&order_id=" + orderID
        print(postString)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        self.sendRequest(url: baseUrl, parameters: postString)
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if(json["data"]["success"]==true){
                self.parseData(json)
            }
        }
    }
    func parseData(_ json:JSON){
        if let jsondata = json["data"].dictionary
        {
            //for (key,value) in jsondata
            //{
            
            vendor_name.text=jsondata["vendor"]?.stringValue
            order_id.text=jsondata["order_id"]?.stringValue
            fixedFee.text=jsondata["fixed_fee"]?.stringValue
            collectionFee.text=jsondata["collection_fee"]?.stringValue
            amount.text=jsondata["amount"]?.stringValue
            commissionfee.text=jsondata["commission_fee"]?.stringValue
            shippingFee.text=jsondata["ship_fee"]?.stringValue
            serviceTax.text=jsondata["service_tax"]?.stringValue
            shippingAmount.text=jsondata["shipping_amount"]?.stringValue
            paymentMode.text=jsondata["payment_method"]?.stringValue
            grandTotal.text=jsondata["grand_total"]?.stringValue
            //}
        }
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

