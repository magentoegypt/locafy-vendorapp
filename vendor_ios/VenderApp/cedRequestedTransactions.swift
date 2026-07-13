//
//  cedRequestedTransactions.swift
//  VenderApp
//
//  Created by cedcoss on 18/02/19.
//  Copyright © 2019 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedRequestedTransactions: ced_VendorBaseClass, UITableViewDataSource, UITableViewDelegate {

    @IBOutlet weak var mainTable: UITableView!
    let refreshControl = UIRefreshControl()
    var page = 1;
    var transactionData = [[String:String]]()
    var pendingAmount = "0";
    var cancelledAmount = "0";
    var requestAmount = "0";
    
    var loading = true;
    
    override func viewDidLoad() {
        super.viewDidLoad()
        mainTable.delegate = self;
        mainTable.dataSource = self;
        loadData();
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        mainTable.refreshControl = refreshControl
        // Do any additional setup after loading the view.
    }
    
    @objc func refresh(_ sender: AnyObject) {
        transactionData.removeAll()
        self.mainTable.reloadData()
        self.page = 1;
        self.loadData()
    }
    
    @objc func loadData()
    {
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait!!!")
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey="+hashKey;
        if(Ced_CommonVendor().checkModule("Ced_VAdvTransactionApi")){
            self.sendRequest(url: "vadvtransactionapi/transaction/view/page/\(page)", parameters: postString)
        }else{
             self.sendRequest(url: "vendorapi/vtransaction/paymentrequest/page/\(page)", parameters: postString)
        }
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        refreshControl.endRefreshing()
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            do {
                let json = try JSON(data: data);
                print(json)//if(requestUrl == "vendorapi/vtransaction/paymentrequest/page/\(page)" ||
                if requestUrl == "vadvtransactionapi/transaction/view/page/\(page)"
                {
                    
                    if(json["data"]["success"].stringValue == "true")
                    {
                        for index in json["data"]["transactiondata"].arrayValue
                        {
                            transactionData.append(["request_id":index["request_id"].stringValue,"created_at":index["created_at"].stringValue,"order_increment_id":index["order_increment_id"].stringValue,"pending_amount":index["pending_amount"].stringValue,"cancelled_amount":index["cancelled_amount"].stringValue,"currency_symbol":index["currency_symbol"].stringValue,"action":index["action"].stringValue]);
                        }
                        if(json["data"]["show_refund_notice"].stringValue == "true"){
                            self.view.makeToast("The payment request has been canceled due to returned requests. Please submit a new payment request".localized, duration: 2.0, position: .center)
                        }
                        self.requestAmount = json["data"]["total_earned_amount"].stringValue;
                        self.pendingAmount = json["data"]["total_pending_amount"].stringValue;
                        self.cancelledAmount = json["data"]["total_pending_requested_amount"].stringValue;
                        self.mainTable.restore()
                        self.mainTable.reloadData();
                    }
                    else
                    {
                        self.loading = false;
                       // self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                        self.mainTable.setEmptyMessage(json["data"]["message"].stringValue)
                        return;
                    }
                }
                else if(requestUrl == "vadvtransactionapi/transaction/request/page/\(page)")//(requestUrl == "vendorapi/vtransaction/request" ||
                {
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                    if(json["data"]["success"].stringValue == "true")
                    {
                        Ced_CommonVendor.delay(2.0) {
                            self.page = 1;
                            self.loadData();
                        }
                    }
                }
            } catch let error {
                print(error.localizedDescription)
            }
            
        }
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 2;
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(section == 0)
        {
            return 1;
        }
        else
        {
            return transactionData.count;
        }
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if(indexPath.section == 0)
        {
            if let cell = tableView.dequeueReusableCell(withIdentifier: "headingCell") as? cedRequestedTransactionCell
            {
                cell.pendingAmountLabel.text = cancelledAmount
                cell.cancelledAmountLabel.text = pendingAmount
                cell.requestedAmountLabel.text = requestAmount;
                
                cell.cellView.cardView();
                cell.cellViewLeft.cardView();
                cell.cellViewBottom.cardView();
                cell.requestBtn.setTitleColor(.white, for: .normal)
                cell.requestBtn.backgroundColor = DynamicColor.secondaryColor
                cell.requestBtn.cardView()
                cell.requestBtn.addTarget(self, action: #selector(requestButtonClicked(_:)), for: .touchUpInside);
                let tapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(viewLeftImageTapped(tapGestureRecognizer:)))
                cell.cellViewLeftImage.isUserInteractionEnabled = true
                cell.cellViewLeftImage.addGestureRecognizer(tapGestureRecognizer)
                if(ced_storeVC.selectLangauge == "ar"){
                    cell.cellViewLeftImage.transform = CGAffineTransformMakeScale(-1.0, 1.0)
                    cell.cellViewBottomImage.transform = CGAffineTransformMakeScale(-1.0, 1.0)
                    cell.cellViewImage.transform = CGAffineTransformMakeScale(-1.0, 1.0)
                }
                
                let tapGestureRecognizer1 = UITapGestureRecognizer(target: self, action: #selector(viewBottomImageTapped(tapGestureRecognizer:)))
                cell.cellViewBottomImage.isUserInteractionEnabled = true
                cell.cellViewBottomImage.addGestureRecognizer(tapGestureRecognizer1)
                
                let tapGestureRecognizer2 = UITapGestureRecognizer(target: self, action: #selector(viewImageTapped(tapGestureRecognizer:)))
                cell.cellViewImage.isUserInteractionEnabled = true
                cell.cellViewImage.addGestureRecognizer(tapGestureRecognizer2)
                if let number = Int(pendingAmount.components(separatedBy: CharacterSet.decimalDigits.inverted).joined()) {
                    if(number > 0 && !pendingAmount.contains("-")){
                        cell.cellViewBottom.backgroundColor = Ced_CommonVendor.UIColorFromRGB("#DBE2F1")
                        cell.requestBtn.isUserInteractionEnabled = true
                        cell.requestBtn.alpha = 1
                        cell.requestBtn.setTitleColor(UIColor.init(white: 1, alpha: 1), for: .normal)
                    }else{
                        cell.cellViewBottom.backgroundColor = .white
                        cell.requestBtn.isUserInteractionEnabled = false
                        cell.requestBtn.alpha = 0.5
                        cell.requestBtn.backgroundColor = .lightGray
                        cell.requestBtn.setTitleColor(UIColor.init(white: 1, alpha: 0.5), for: .normal)
                    }
                }
                return cell;
            }
        }
        else if(indexPath.section == 1)
        {
            if let cell = tableView.dequeueReusableCell(withIdentifier: "cedRequestedTransactionCell") as? cedRequestedTransactionCell
            {
                let transact = transactionData[indexPath.row];
                cell.orderIdLabel.text = transact["order_increment_id"];
                cell.orderDateLabel.text = transact["created_at"];
                cell.cancelledAmountLabel.text = transact["cancelled_amount"];
                cell.pendingAmountLabel.text = transact["pending_amount"];
                if(transact["action"] == "Request")
                {
                    cell.requestedButton.isHidden = false;
                    cell.actionLabel.isHidden = true;
                    
                }
                else
                {
                    cell.actionLabel.isHidden = false;
                    cell.requestedButton.isHidden = true;
                }
                cell.requestedButton.tag = indexPath.row;
                cell.requestedButton.addTarget(self, action: #selector(requestButtonClicked(_:)), for: .touchUpInside);
                cell.cellView.cardView();
                return cell;
            }
        }
        return UITableViewCell();
    }

    @objc func requestButtonClicked(_ sender: UIButton)
    {
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait!!!")
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
       
         if(Ced_CommonVendor().checkModule("Ced_VAdvTransactionApi")){
            let postString = "vendor_id=" + vendorId
            self.sendRequest(url: "vadvtransactionapi/transaction/request/page/\(page)", parameters: postString);
         }else{
             let postString = "vendor_id=" + vendorId + "&hashkey="+hashKey+"&payment_id="+transactionData[sender.tag]["request_id"]!;
        self.sendRequest(url: "vendorapi/vtransaction/request", parameters: postString);
        }
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.section == 0)
        {
            if(Ced_CommonVendor().checkModule("Ced_VAdvTransactionApi")){
                return 300;}else{return 220;}
        }
        else
        {
            return 180;
        }
    }
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading && !self.refreshControl.isRefreshing{
                self.page += 1;
                self.loadData();
                mainTable.reloadData();
            }
        }
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

    @objc func viewLeftImageTapped(tapGestureRecognizer: UITapGestureRecognizer)
    {
        self.view.makeToast("The total amount received for sales".localized, duration: 2.0, position: .center)
    }
    
    @objc func viewBottomImageTapped(tapGestureRecognizer: UITapGestureRecognizer)
    {
        self.view.makeToast("The total amount due but not yet paid".localized, duration: 2.0, position: .center)
    }
    
    @objc func viewImageTapped(tapGestureRecognizer: UITapGestureRecognizer)
    {
        self.view.makeToast("The total amount you ordered is still waiting for payment".localized, duration: 2.0, position: .center)
    }
    
}
