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
import WebKit
class ced_viewTransactionView: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate, WKNavigationDelegate {
    
    @IBOutlet weak var tableView: UITableView!
    var transactionId = String()
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var containerView: UIView!
    var DataView = [String:String]()
    var orderView = [[String:String]]()
    var orderDetail = [[String:String]]()
    var heightToUse=CGFloat(0)
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.register(ced_newViewTransactTableViewCell.self, forCellReuseIdentifier: ced_newViewTransactTableViewCell.reuseId)

        tableView.estimatedRowHeight = 150
        tableView.delegate = self
        tableView.dataSource = self
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        topLabel.backgroundColor = color
        topLabel.text = "Transaction View".localized
        self.title = "Transaction View".localized
        
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        
        
        ced_navigationBarController().addNavButton(self,str:"back".localized)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        self.loadData()
        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 4
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        /*if(DataView.count == 0){
         return 0
         }
         return 4*/
        if(DataView.count != 0){
            if(section == 1)
            {
                return orderDetail.count;
            }
            if(orderView.count>0)
            {
                
                if(section == 2)
                {
                    return orderView.count;
                }
                
            }
            if(section == 0 || section == 3 || section == 4)
            {
                return 1;
            }
            
        }
        return 0;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if(indexPath.section == 0){
            let cell = tableView.dequeueReusableCell(withIdentifier: "benDetail") as! ced_benficarydetail
            cell.beneficaryDetails.text = "-"//DataView[""]
            let sel=DataView["vendorname"]?.components(separatedBy: ">")
            if(sel?.count ?? 0 > 1){
                let selected=sel![1].components(separatedBy: "<")
                if(selected.count > 0){
                    let selectedName=selected[0]
                    cell.vendorName.text = selectedName
                }
            }else{
                cell.vendorName.text = DataView["vendorname"]
            }
            cell.payMethod.text = DataView["transactionmode"]
            
            
            return cell
        }else if(indexPath.section == 1){
            let cell = tableView.dequeueReusableCell(withIdentifier: ced_newViewTransactTableViewCell.reuseId, for: indexPath) as! ced_newViewTransactTableViewCell
            cell.singlepopulateData(with: orderDetail[indexPath.row])
//            let cell = tableView.dequeueReusableCell(withIdentifier: "OrderD") as! ced_orderDetailscell
//            let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
//            let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
//            cell.myWebView.navigationDelegate = self;
//            let screenWidth = UIScreen.main.bounds.width;
//            let str="<html><head><style>* {font-family : 'Arial';} img{width:\(screenWidth-30)  !important}  body{width:\(screenWidth-30)  !important}</style></head><body>"+DataView["order_detail"]!+"</body></html>"
//            heightToUse=CGFloat(cell.myWebView.frame.height)
//            cell.myWebView.loadHTMLString(str, baseURL: nil);
            //cell.topLabel.backgroundColor = color
            /*heightToUse += cell.topLabel.frame.height+10
             for i in 0..<orderView.count
             {
             let orderDetailView = orderDetailViewLabel()
             orderDetailView.frame = CGRect(x:0, y: heightToUse, width: cell.contentView.frame.width-10, height: 150)
             orderDetailView.autoresizingMask = [UIViewAutoresizing.flexibleLeftMargin,UIViewAutoresizing.flexibleRightMargin];
             orderDetailView.center.x = cell.contentView.center.x;
             orderDetailView.wrapperView.makeCard(orderDetailView.wrapperView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
             orderDetailView.orderIDButton.setTitle(orderView[i]["order_id"], for: .normal)
             
             orderDetailView.orderIDButton.addTarget(self, action: #selector(orderIdPressed(_:)), for: .touchUpInside)
             orderDetailView.orderIDButton.setTitleColor(UIColor.blue,for: .normal)
             
             orderDetailView.grandTotal.text = orderView[i]["grand_total"]
             
             
             orderDetailView.commissionFee.text = orderView[i]["commission_fee"]
             
             orderDetailView.paymentMode.text = orderView[i]["order_paymode"]
             
             orderDetailView.totalPayment.text = orderView[i]["vendor_payment"]
             
             cell.contentView.addSubview(orderDetailView)
             heightToUse += 160
             }*/
            return cell
        }
        else if(indexPath.section == 2)
        {
            let cell = tableView.dequeueReusableCell(withIdentifier: "orderData") as! ced_orderDetailscell
            let order = orderView[indexPath.row]
            cell.totalPaymentTitle.text="Total Payment".localized
            cell.orderIDTitle.text="Order Id".localized
            cell.grandTotalTitle.text="Grand Total".localized
         //   cell.commisionFeeTitle.text="Commission Fee".localized
            cell.paymentModeTitle.text="Single Payment Mode".localized
            cell.orderIdButton.setTitle(order["order_id"], for: .normal);
            cell.orderIdButton.addTarget(self, action: #selector(orderIdPressed(_:)), for: .touchUpInside)
            cell.totalPaymentLabel.text = order["vendor_payment"];
            cell.paymentModeLabel.text = order["order_paymode"];
            cell.commissionFeeLabel.text = order["commission_fee"];
            cell.grandTotalLabel.text = order["grand_total"];
            cell.containerView.cardView();
            return cell;
        }else if(indexPath.section == 3) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "tranData") as! ced_transactiondetailss
            cell.transactionId.text = DataView["transactionId"]
            cell.transactionDate.text = DataView["createdat"]?.dateConvertToCurrentTimezone("yyyy-MM-dd HH:mm:ss")
            cell.type.text = DataView["transactiontype"]
            cell.mode.text = DataView["transactionmode"]
            cell.adjustmentAmount.text=DataView["adjustment_amount"]!
            cell.netAmount.text=DataView["net_amount"]!
            cell.notes.text = DataView["notes"]
            cell.amount.text=DataView["amount"]!
            cell.shippingAmount.text=DataView["total_shipping_amount"] ?? "N/A"
            if((DataView["transaction_doc"] ?? "") != "" ){
                cell.pdfHeight.constant = 30
                let labelTap = UITapGestureRecognizer(target: self, action: #selector(self.labelTapped(_:)))
                cell.viewDocumentlbl.isUserInteractionEnabled = true
                cell.viewDocumentlbl.addGestureRecognizer(labelTap)
            }else{
                cell.pdfHeight.constant = 0
            }
            /*if DataView["total_shipping_amount"]==""||DataView["total_shipping_amount"]=="$"
             {
             cell.shippingAmount.text="$0"
             }
             else
             {
             cell.shippingAmount.text=DataView["total_shipping_amount"]!
             }*/
            return cell
        }
        else
        {
            let cell = tableView.dequeueReusableCell(withIdentifier: "totalData") as! ced_totalTransactioncell
            //cell.totalAmountPay.text = DataView["Total_Amount_Pay"]!
            //cell.totalServiceTax.text = DataView["tax_service"]!
            return cell
        }
    }
    
    @objc func labelTapped(_ sender: UITapGestureRecognizer) {
        guard let url = URL(string: DataView["transaction_doc"] ?? "") else {
          return //be safe
        }
        if #available(iOS 10.0, *) {
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
        } else {
            UIApplication.shared.openURL(url)
        }
    }
    
    @objc func loadData(){
        
        let baseUrl = "vendorapi/vtransaction/viewpayment"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey + "&payment_id=" + transactionId
        print(postString)
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
            self.tableView.reloadData()
        }
        
    }
    func parseData(_ json:JSON){
        if let jsondata = json["data"]["payment_detail"].array
        {
            for item in jsondata
            {
                DataView["transactiontype"]=item["transactiontype"].stringValue
                DataView["transactionId"]=item["transactionId"].stringValue
                DataView["createdat"]=item["createdat"].stringValue
                DataView["transactionmode"]=item["transactionmode"].stringValue
                
                DataView["notes"]=item["notes"].stringValue
                DataView["transaction_doc"]=item["transaction_doc"].stringValue
                DataView["adjustment_amount"]=item["adjustment_amount"].stringValue
                DataView["net_amount"]=item["net_amount"].stringValue
                DataView["amount"]=item["amount"].stringValue
                DataView["vendorname"]=item["vendorname"].stringValue
                DataView["paymentdetail"]=item["paymentdetail"].stringValue
                DataView["order_detail"] = item["order_detail"].stringValue
                
                item["order_detail"].arrayValue.forEach{detail in
                    var tempDict = [String:String]()
                    for (itm,val) in detail.dictionaryValue{
                        tempDict[itm] = val.stringValue
                    }
                    orderDetail.append(tempDict)
                }
                /*let orderDetail=item["order_detail"].array
                var itemdata = [String:String]()
                for item in orderDetail!
                {
                    for (key,value) in item
                    {
                        itemdata[key] = value.stringValue
                    }
                    orderView.append(itemdata)
                }*/
            }
            
        }
       
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.section == 0){
            return 156
        }else if(indexPath.section == 1){
            return UITableView.automaticDimension
        }
        else if(indexPath.section == 2)
        {
            return 220;
        }
        else if(indexPath.section == 3){
            return 340
        }
        else{
            return 90
        }
    }
    @objc func orderIdPressed(_ sender:UIButton)
    {
        print("dfcrcdrs")
        let vc = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "ced_ViewTransactionDetails")as! ced_ViewTransactionDetailsController
        vc.paymentID=transactionId
        vc.orderID=sender.currentTitle!
        navigationController?.pushViewController(vc, animated: true)
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

