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

class ced_requestTransactions: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate {
    
    @IBOutlet weak var topContainer: UIView!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var RequestTranscationTable: UITableView!
    @IBOutlet weak var topButton: UIButton!
    var otherData = [String:String]()
    var applyFilter = false
    var filterString = String()
    var vendorTransactionArray = [[String:String]]()
    
    var filterPostdata = NSDictionary()
    var page = 1
    var loading = true
    override func viewDidLoad() {
        super.viewDidLoad()
        RequestTranscationTable.delegate = self
        RequestTranscationTable.dataSource = self
        self.title = "Request Transactions"
        
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 21)!]
        // Do any additional setup after loading the view.
        topButton.addTarget(self, action: #selector(ced_requestTransactions.showFilterPopup(_:)), for: UIControl.Event.touchUpInside)
        topContainer.makeCard(topContainer, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        topContainer.backgroundColor = UIColor.white
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        topLabel.backgroundColor = color
        //ced_navigationBarController().addNavButton(self,str:"no")
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.loadData(1)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(section == 0){
            if(otherData.count == 0){
                return 0
            }
            return 1
        }else{
            return vendorTransactionArray.count
        }
        
    }
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if(indexPath.section == 1){
            let cell = RequestTranscationTable.dequeueReusableCell(withIdentifier: "requestTrantData") as! ced_requestTransactionData
            cell.orderDate.text = vendorTransactionArray[indexPath.row]["created_at"]
            cell.orderId.text  = vendorTransactionArray[indexPath.row]["order_id"]
            cell.pendingAmount.text = vendorTransactionArray[indexPath.row]["amount"]
            cell.action.text = vendorTransactionArray[indexPath.row]["adjustment_amount"]
            return cell
        }else{
            
            let cell = RequestTranscationTable.dequeueReusableCell(withIdentifier: "requestData")  as! ced_requesttransactFirst
            cell.totalPendingAmount.text = otherData["pendingAmount"]
            cell.totalRequestedAmount.text = otherData["requestedAmount"]
            cell.massRequestButton.addTarget(self, action: #selector(ced_requestTransactions.vendorMassRequest(_:)), for: UIControl.Event.touchUpInside)
            cell.massRequestButton.tag = indexPath.row
            return cell
        }
        
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.section == 1){
            
            return 170
        }
        return 200
    }
    
    @objc func loadData(_ page:Int){
        //var baseUrl = Ced_CommonVendor.getInfoPlist("baseUrl") as? String
        let baseUrl = "vendorapi/vtransaction/paymentrequest/page/\(page)"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        if(applyFilter){
            postString += "&filter=" + filterString.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!
        }
        self.sendRequest(url: baseUrl, parameters: postString)
        
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if requestUrl=="vendorapi/vtransaction/massRequestPayment"
        {
            guard let json = try? JSON(data: data!) else {return}
            if(json["data"]["success"].stringValue.lowercased() == "false"){
                self.view.showToastMsg(json["data"]["message"].stringValue)
            }
        }
        else
        {
            guard let json = try? JSON(data: data!) else {return}
            print(json)
            self.parseData(json)
            Alert_File.removeLoadingIndicator(self)
            self.RequestTranscationTable.reloadData()
        }
    }
    
    func parseData(_ json:JSON){
        let refundableAmount = json["data"]["refundableAmount"].stringValue
        let earnedAmount = json["data"]["earnedAmount"].stringValue
        let pendingAmount = json["data"]["pendingAmount"].stringValue
        let paidAmount = json["data"]["paidAmount"].stringValue
        let requestedAmount    = json["data"]["requestedAmount"].stringValue
        let canceledAmount = json["data"]["canceledAmount"].stringValue
        let refundedAmount = json["data"]["refundedAmount"].stringValue
        otherData = ["canceledAmount":canceledAmount,"earnedAmount":earnedAmount,"pendingAmount":pendingAmount,"paidAmount":paidAmount,"requestedAmount":requestedAmount,"refundedAmount":refundedAmount,"refundableAmount":refundableAmount]
        
        for viewdata in json["data"]["pending_payment"].arrayValue {
            
            let amount = viewdata["pending_amount"].stringValue
            let payment_id = viewdata["payment_id"].stringValue
            let order_id = viewdata["order_id"].stringValue
            let created_at = viewdata["created_at"].stringValue
            let adjustment_amount = viewdata["action"].stringValue
            
            let transactdata = ["amount":amount,"payment_id":payment_id,"order_id":order_id,"created_at":created_at,"adjustment_amount":adjustment_amount]
            vendorTransactionArray.append(transactdata)
            
        }
        return
    }
    
    //Mass Request data
    @objc func vendorMassRequest(_ sender:UIButton){
        //var baseUrl = Ced_CommonVendor.getInfoPlist("baseUrl") as? String
        let baseUrl = "vendorapi/vtransaction/massRequestPayment"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        self.sendRequest(url: baseUrl, parameters: postString)
    }
    
    
    @objc func showFilterPopup(_ sender:UIButton){
        /* background view */
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let filterbyView = ced_requestTransactFilter();
        self.addgesturesTohideView(self.view)
        if(filterPostdata.count != 0){
            filterbyView.pendingAmountFrom?.text = (filterPostdata.object(forKey: "amount") as AnyObject).object(forKey: "from") as? String
            filterbyView.pendingAmountTo?.text = (filterPostdata.object(forKey: "amount") as AnyObject).object(forKey: "to") as? String
            filterbyView.orderId?.text = filterPostdata.object(forKey: "order_id") as? String
            filterbyView.orderDateFrom.text = (filterPostdata.object(forKey: "created_at") as AnyObject).object(forKey: "from") as? String
            filterbyView.orderDateTo?.text = (filterPostdata.object(forKey: "created_at") as AnyObject).object(forKey: "to") as? String
            
        }
        filterbyView.Filter.addTarget(self, action: #selector(ced_requestTransactions.applyFilters(_:)), for: UIControl.Event.touchUpInside)
        filterbyView.resetFilter.addTarget(self, action: #selector(ced_requestTransactions.resetFilters(_:)), for: UIControl.Event.touchUpInside)
        filterbyView.tag = 181;
        filterbyView.backgroundColor = UIColor.black;
        filterbyView.frame = CGRect(x: 25, y: 25, width: self.view.frame.width - 50, height: 320)
        filterbyView.center = self.view.center
        bgCView.addSubview(filterbyView)
        self.view.addSubview(bgCView);
        
    }
    @objc func applyFilters(_ sender:UIButton){
        self.applyFilter = true
        vendorTransactionArray.removeAll()
        otherData.removeAll()
        let view = self.view.viewWithTag(181) as! ced_requestTransactFilter
        var createdAtfr = ""
        if(view.orderDateFrom.text != nil){
            createdAtfr =  view.orderDateFrom.text!
        }
        var createdAtTo  = ""
        if(view.orderDateTo.text != nil){
            createdAtTo = view.orderDateTo.text!
        }
        
        var orderId = ""
        if(view.orderId.text != nil){
            orderId = view.orderId.text!
        }
        
        var amountFro  = ""
        if(view.pendingAmountFrom.text != nil){
            amountFro = view.pendingAmountFrom.text!
        }
        var amountTo = ""
        if(view.pendingAmountTo.text != nil){
            amountTo = view.pendingAmountTo.text!
        }
        
        let createdAt = ["to":createdAtTo,"from":createdAtfr]
        let amount = ["to":amountTo,"from":amountFro]
        let postdata = ["amount":amount,"created_at":createdAt,"order_id":orderId] as [String : Any]
        do{
            let JSONData = try JSONSerialization.data(
                withJSONObject: postdata ,
                options: JSONSerialization.WritingOptions(rawValue: 0))
            filterString = NSString(data: JSONData,
                                    encoding: String.Encoding.utf8.rawValue)! as String
            
        }
        catch{
            print("error in data encoding")
        }
        self.filterPostdata = postdata as NSDictionary
        self.view.viewWithTag(151)?.removeFromSuperview();
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.loadData(1)
        
    }
    
    @objc func resetFilters(_ sender:UIButton){
        self.filterPostdata = NSDictionary()
        vendorTransactionArray = [[String:String]]()
        otherData = [String:String]()
        self.applyFilter = false
        self.filterString = String()
        self.view.viewWithTag(151)?.removeFromSuperview()
        Alert_File.addLoadingIndicator(self, msg: "Please Wait!Loading...")
        self.loadData(1)
    }
    
    //@objc function to dismiss the custom popover
    @objc func hideView(_ recognizer: UITapGestureRecognizer)
    {
        self.view.viewWithTag(151)?.removeFromSuperview();
    }
    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(ced_requestTransactions.hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_requestTransactions.hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_requestTransactions.hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_requestTransactions.hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_requestTransactions.hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    //delegate @objc function to handle touch events on the custom popover
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        if let innerView = self.view.viewWithTag(181) as? ced_requestTransactFilter {
            if(touch.view!.isDescendant(of: innerView))
            {
                return false;
            }
            return true;
        }
        return true;
    }
    
    
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
                self.page += 1;
                Alert_File.addLoadingIndicator(self, msg: "Loading...")
                self.loadData(page);
                
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
