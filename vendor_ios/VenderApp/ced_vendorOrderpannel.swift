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
import RxSwift
import RxCocoa

class ced_vendorOrderpannel: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate {
    
    //outlets from storyboards
    @IBOutlet weak var topHeading: UILabel!
    @IBOutlet weak var ordersFilter: UIButton!
    @IBOutlet weak var filterIcon: UIImageView!
    @IBOutlet weak var ordertopview: UIView!
    @IBOutlet weak var verdorOrderTable: UITableView!
    //end:
    let refreshControl = UIRefreshControl()
    var disposeBag = DisposeBag()
    
    var timerArray = [Int:Timer]()
    var timerCountArray = [TimeInterval]()
    let tabelcellident = "vendorordercell"
    
    var ordersData = [[String:String]]()
    var paymentDict = [String: String]();
    var paymentList = [String]();
    var vendorpaymentDict = [String: String]();
    var vendorpaymentList = [String]();
    var applyFilter = false
    var filterString = String()
    var filterPostdata = NSDictionary()
    var page = 1
    var loading = true
    override func viewDidLoad() {
        super.viewDidLoad()
        topHeading.text="Order List".localized
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        verdorOrderTable.delegate = self
        verdorOrderTable.dataSource = self
        if(applyFilter){
            filterIcon.isHidden = true
            ordersFilter.isHidden = true
            topHeading.text="List of canceled orders".localized
        }
        self.loaddata()
        ordersFilter.addTarget(self, action: #selector(ced_vendorOrderpannel.showFilterPopup(_:)), for: UIControl.Event.touchUpInside)
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let bodycolorString = Ced_CommonVendor.getInfoPlist("bodyColor") as! String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        let bodycolor = Ced_CommonVendor.UIColorFromRGB(bodycolorString)
        self.title = "Orders".localized
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        ced_navigationBarController().addNavButton(self,str:"no".localized)
        verdorOrderTable.backgroundColor = bodycolor
        ordertopview.backgroundColor = color
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        self.loadVendorOrders(1)
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        verdorOrderTable.refreshControl = refreshControl
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func refresh(_ sender: AnyObject) {
        self.filterPostdata = NSDictionary()
        ordersData = [[String:String]]()
        self.verdorOrderTable.reloadData()
        self.page = 1;
        self.loadVendorOrders(1)
    }
    
    @objc func loadVendorOrders(_ page:Int){
        //var baseUrl = Ced_CommonVendor.getInfoPlist("baseUrl") as? String
        let baseUrl = "vorderapi/vorders/item/page/\(page)"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        if(applyFilter){
            postString += "&filter=" + filterString//.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!
        }
        print(postString)
        self.sendRequest(url: baseUrl, parameters: postString)
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        refreshControl.endRefreshing()
        if let data = data{
            if requestUrl=="vorderapi/vorders/orderstatus"
            {
                
                guard let json = try? JSON(data: data) else {return}
                print("---status---\(json)")
                let paymentStatus = json["data"]["order_payment_status"].arrayValue;
                let vendorpaymentStatus = json["data"]["vendor_payment_status"].arrayValue;
                // print(countryListing);
                //var d=0;
                for index in paymentStatus
                {
                    let key = index["value"].stringValue;
                    let value = index["label"].stringValue;
                    
                    self.paymentDict[value] = key;
                    self.paymentList.append(value);
                }
                for index in vendorpaymentStatus
                {
                    let key = index["value"].stringValue;
                    
                    let value = index["label"].stringValue;
                    
                    
                    self.vendorpaymentDict[value] = key;
                    self.vendorpaymentList.append(value);
                }
                /*let filterbyView = ced_orderFilterpopup();
                print(self.paymentList[0])
                filterbyView.orderPaymentStatusbutton.setTitle(self.paymentList[0], for: UIControlState())
                filterbyView.vendorPaymentStatus.setTitle(self.paymentList[0], for: UIControlState())*/
            }
            else
            {
                
                let msg = NSString(data: data, encoding: String.Encoding.utf8.rawValue)
                
                if(msg?.lowercased == "no_order")
                {
                    self.loading = false;
                    if(page == 1)
                    {
                        self.view.makeToast("NoOrdersToList".localized, duration: 2.0, position: .bottom)
                        self.verdorOrderTable.setEmptyMessage("NoOrdersToList".localized)
                        self.verdorOrderTable.reloadData();
                        return;
                    }
                }
                else
                {
                   guard let json = try? JSON(data: data) else {return}
                    if(json["data"]["success"].stringValue.lowercased() == "false"){
                       // self.view.showToastMsg(json["message"].stringValue)
                        self.verdorOrderTable.setEmptyMessage(json["message"].stringValue)
                        loading = false
                    }else if(json["data"]["success"].stringValue.lowercased() == "true"){
                        //self.view.showToastMsg(json["data"]["message"].stringValue)
                        print(json)
                        self.verdorOrderTable.restore()
                        parseOrders(json)
                    }
                    else
                    {
                        loading = false
                        
                    }
                }
                
            }
        }
        
    }
    func parseOrders(_ json:JSON){
        for result in json["data"]["orderdata"].arrayValue{
            let grandTotal = result["grand_total_earned"].stringValue
            let customerId = result["id"].stringValue
            let base_currency_code = result["base_currency_code"].stringValue
            let created_at = result["created_at"].stringValue
            let order_total = result["order_total"].stringValue
            let payment_state = result["payment_state"].stringValue
            let order_id = result["order_id"].stringValue
            let shipping_name = result["shipping_name"].stringValue
            let billing_name    = result["billing_name"].stringValue
            let net_vendor_earn = result["net_vendor_earn"].stringValue
            let shop_commission_base_fee = result["shop_commission_fee"].stringValue
            let orderpayment = result["order_payment_state"].stringValue
            let shippingTimer = result["shipping_timer"].stringValue
            let shippingNotice = result["shipping_notice"].stringValue
            let timeinterval : TimeInterval = (shippingTimer as NSString).doubleValue
            self.timerCountArray.append(timeinterval);
            let dataObject = ["grandTotal":grandTotal,"customerId":customerId,"base_currency_code":base_currency_code,"order_total":order_total,"payment_state":payment_state,"order_id":order_id,"shipping_name":shipping_name,"net_vendor_earn":net_vendor_earn,"commisonBasefee":shop_commission_base_fee,"billingName":billing_name,"created_at":created_at,"orderpayment":orderpayment,"shippingTimer":shippingTimer,"shippingNotice":shippingNotice]
            
            self.ordersData.append(dataObject)
            
        }
        if(self.ordersData.count != 0){
            if (json["data"]["orderdata"].count == 0) {
                loading = false
            }
        }
        self.verdorOrderTable.reloadData()
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return ordersData.count
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = verdorOrderTable.dequeueReusableCell(withIdentifier: tabelcellident, for: indexPath) as! ced_ordercell
        //if(cell.dataCheck == false)
        //{
            cell.dataCheck = true;
            let order = ordersData[indexPath.row]
            cell.billingName.text = order["billingName"]
            cell.orderIdval.text = order["order_id"]
            cell.grandTotal.text = order["grandTotal"]
            cell.commissionval.text = order["commisonBasefee"]
            cell.puchaseddate.text = order["created_at"]
            cell.orderStatus.text = order["orderpayment"]
            cell.vendorstatus.text = order["payment_state"]
            cell.netearnedval.text = order["net_vendor_earn"]
            cell.shippingNoticeLabel.text = order["shippingNotice"];
            let timeinterval : TimeInterval = (order["shippingTimer"]! as NSString).doubleValue
        if (timerCountArray.count > indexPath.row)
        {
            cell.startTime = timerCountArray[indexPath.row];
            
        }
        
        
        
        cell.parent = self;
        cell.indexPath = indexPath.row;
        cell.reload()
        //}
        
        /*cell.billingName.backgroundColor = .red
        cell.orderIdval.backgroundColor = .green
        cell.grandTotal.backgroundColor = UIColor.lightGray
        cell.commissionval.backgroundColor = .yellow
        cell.puchaseddate.backgroundColor = UIColor.orange
        cell.orderStatus.backgroundColor = UIColor.magenta
        cell.vendorstatus.backgroundColor = .blue
        cell.netearnedval.backgroundColor = UIColor.gray*/
        return cell
    }
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let order = ordersData[indexPath.row]
        if(Ced_CommonVendor().checkModule("Ced_VOrderApi")){
            let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
            let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "manageOrders") as! ced_manageProduct
            viewcontrol.orderId = order["order_id"]!
            self.navigationController?.pushViewController(viewcontrol, animated: true)
        }else{
            let storybordOrder = UIStoryboard(name: "Main", bundle: nil)
            let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "vendorSingleorder") as! ced_vendorSingleorder
            viewcontrol.orderId = order["order_id"]!
            self.navigationController?.pushViewController(viewcontrol, animated: true)
        }
        
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 205//290
    }
    
    @objc func showFilterPopup(_ sender:UIButton){
        
        /* background view */
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let filterbyView = ced_orderFilterpopup();
        self.addgesturesTohideView(self.view)
        if(filterPostdata.count != 0){
            filterbyView.purchasedonTo?.text = (filterPostdata.object(forKey: "created_at") as AnyObject).object(forKey: "to") as? String
            filterbyView.purchaseOnfrom?.text = (filterPostdata.object(forKey: "created_at") as AnyObject).object(forKey: "from") as? String
            filterbyView.billingNamefrom?.text = filterPostdata.object(forKey: "billing_name") as? String
            filterbyView.grandtotalFrom?.text = (filterPostdata.object(forKey: "order_total") as AnyObject).object(forKey: "from") as? String
            filterbyView.grandtotalTo?.text = (filterPostdata.object(forKey: "order_total") as AnyObject).object(forKey: "to") as? String
            
            filterbyView.netEarnedfrom?.text = (filterPostdata.object(forKey: "net_vendor_earn") as AnyObject).object(forKey: "from") as? String
            filterbyView.netEarnedTo?.text = (filterPostdata.object(forKey: "net_vendor_earn") as AnyObject).object(forKey: "to") as? String
            
            filterbyView.orderIdval?.text = filterPostdata.object(forKey: "increment_id") as? String
            
            filterbyView.commisionfeeto?.text = (filterPostdata.object(forKey: "shop_commission_fee") as AnyObject).object(forKey: "to") as? String
            filterbyView.commisionfeefrom?.text = (filterPostdata.object(forKey: "shop_commission_fee") as AnyObject).object(forKey: "from") as? String
            filterbyView.orderIdval.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
            
            filterbyView.netEarnedfrom.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
            filterbyView.netEarnedTo.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
            
            
            filterbyView.commisionfeeto.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
            filterbyView.commisionfeefrom.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
            
            
            filterbyView.grandtotalFrom.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
            filterbyView.grandtotalTo.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
            
            
            let orderStatus = filterPostdata.object(forKey: "order_payment_state") as? String
            filterbyView.orderPaymentStatusbutton.setTitle(orderStatus, for: UIControl.State())
            
            let vendorStatus = filterPostdata.object(forKey: "payment_state") as? String
            filterbyView.vendorPaymentStatus.setTitle(vendorStatus, for: UIControl.State())
            
        }
        filterbyView.filterButton.addTarget(self, action: #selector(ced_vendorOrderpannel.applyFilters(_:)), for: UIControl.Event.touchUpInside)
        filterbyView.ResetFilter.addTarget(self, action: #selector(ced_vendorOrderpannel.resetFilters(_:)), for: UIControl.Event.touchUpInside)
        filterbyView.orderPaymentStatusbutton.addTarget(self, action: #selector(ced_vendorOrderpannel.showorderPaymentstatus(_:)), for: UIControl.Event.touchUpInside)
        filterbyView.vendorPaymentStatus.addTarget(self, action: #selector(ced_vendorOrderpannel.showvendorpaymentstsus(_:)), for: UIControl.Event.touchUpInside)
        
        filterbyView.purchaseOnfrom.tag = 7776
        filterbyView.purchasedonTo.tag = 7777
        filterbyView.purchasedonTo.delegate = self
        filterbyView.purchaseOnfrom.delegate = self
        
        
        filterbyView.tag = 181;
        filterbyView.backgroundColor = UIColor.black;
        filterbyView.frame = CGRect(x: 20, y: 50, width: self.view.frame.width - 40, height: 470)
        filterbyView.center = self.view.center
        bgCView.addSubview(filterbyView)
        self.view.addSubview(bgCView);
        
    }
    
    @objc func applyFilters(_ sender:UIButton){
        self.applyFilter = true
        self.ordersData = [[String:String]]()
        self.verdorOrderTable.reloadData()
        do{
            let view = self.view.viewWithTag(181) as! ced_orderFilterpopup
            var billngNameto = ""
            if(view.billingNamefrom.text != nil){
                billngNameto =  view.billingNamefrom.text!
            }
            var purchaseOnfrom = ""
            if(view.purchaseOnfrom.text != nil){
                purchaseOnfrom  = view.purchaseOnfrom.text!
            }
            var purchasedonTo = ""
            if(view.purchasedonTo.text != nil){
                purchasedonTo  = view.purchasedonTo.text!
            }
            var grandtotalFrom = ""
            if(view.grandtotalFrom.text != nil){
                grandtotalFrom = view.grandtotalFrom.text!
            }
            var grandtotalTo = ""
            if(view.grandtotalTo.text != nil){
                grandtotalTo   = view.grandtotalTo.text!
            }
            var orderpayemnt = ""
            if(view.orderPaymentStatusbutton.titleLabel!.text != "Please Select Option".localized){
                orderpayemnt    = view.orderPaymentStatusbutton.titleLabel!.text!
            }
            var orderdid = ""
            if(view.orderIdval.text != nil){
                orderdid     = view.orderIdval.text!
            }
            var vendorPaymentStatus = ""
            if(view.vendorPaymentStatus.currentTitle != "Please Select Option".localized){
                vendorPaymentStatus = view.vendorPaymentStatus.currentTitle!;
            }
            var netEarnedfrom = ""
            if(view.netEarnedfrom.text != nil){
                netEarnedfrom = view.netEarnedfrom.text!
            }
            
            var netEarnedto = ""
            if(view.netEarnedTo.text != nil){
                netEarnedto   = view.netEarnedTo.text!
            }
            var commisionfeefrom = ""
            if(view.commisionfeefrom.text != nil){
                commisionfeefrom = view.commisionfeefrom.text!
                
            }
            var commisionfeeto = ""
            if(view.commisionfeeto.text != nil){
                commisionfeeto = view.commisionfeeto.text!
            }
            let orderTOtal = ["to":grandtotalTo,"from":grandtotalFrom]
            let shopCommiso = ["to":commisionfeeto,"from":commisionfeefrom]
            let createdAt = ["to":purchasedonTo,"from":purchaseOnfrom]
            let netvendorearn = ["to":netEarnedto,"from":netEarnedfrom]
            var paymentStatus = ""
            if(vendorPaymentStatus != "")
            {
                print("pydict\(paymentDict) \(vendorPaymentStatus)")
                paymentStatus = vendorpaymentDict[vendorPaymentStatus]!;
            }
            else
            {
                vendorPaymentStatus = "Please Select Option".localized
            }
            var orderStatus = ""
            if(orderpayemnt != "")
            {
                orderStatus = paymentDict[orderpayemnt]!
            }
            else
            {
                orderpayemnt = "Please Select Option".localized
            }
            var postdata = ["order_total":orderTOtal,"payment_state":vendorPaymentStatus,"shop_commission_fee":shopCommiso, "billing_name":billngNameto,"order_payment_state":orderpayemnt,"created_at":createdAt,"net_vendor_earn":netvendorearn,"increment_id":orderdid] as [String : Any]
            
            filterPostdata = postdata as NSDictionary
            postdata = ["order_total":orderTOtal,"payment_state":paymentStatus,"shop_commission_fee":shopCommiso, "billing_name":billngNameto,"order_payment_state":orderStatus,"created_at":createdAt,"net_vendor_earn":netvendorearn,"increment_id":orderdid] as [String : Any]
            let JSONData = try JSONSerialization.data(
                withJSONObject: postdata ,
                options: JSONSerialization.WritingOptions(rawValue: 0))
            filterString = NSString(data: JSONData,
                                    encoding: String.Encoding.utf8.rawValue)! as String
            
        }
        catch{
            print("error in data encoding")
        }
        self.view.viewWithTag(151)?.removeFromSuperview();
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        self.page = 1;
        self.loadVendorOrders(self.page)
        
    }
    
    @objc func resetFilters(_ sender:UIButton){
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.filterPostdata = NSDictionary()
        ordersData = [[String:String]]()
        self.verdorOrderTable.reloadData()
        self.applyFilter = false
        self.filterString = String()
        self.page = 1;
        self.loadVendorOrders(self.page)
    }
    
    @objc func showorderPaymentstatus(_ sender:UIButton){
        let dropDown = DropDown();
        dropDown.dataSource = paymentList;
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
           _ = dropDown.show();
        } else {
            dropDown.hide();
        }
        
    }
    
    @objc func showvendorpaymentstsus(_ sender:UIButton){
        let dropDown = DropDown();
        dropDown.dataSource = vendorpaymentList;
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
        
    }
    
    @objc func loaddata(){
        paymentDict = [String: String]();
        paymentList = [String]();
        vendorpaymentDict = [String: String]();
        vendorpaymentList = [String]();
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //var baseUrl = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        let baseUrl = "vorderapi/vorders/orderstatus"
        self.sendRequest(url: baseUrl, parameters: postString)
    }
    
    //@objc function to dismiss the custom popover
    @objc func hideView(_ recognizer: UITapGestureRecognizer)
    {
        self.view.viewWithTag(151)?.removeFromSuperview();
    }
    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(ced_vendorOrderpannel.hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_vendorOrderpannel.hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_vendorOrderpannel.hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_vendorOrderpannel.hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_vendorOrderpannel.hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    //delegate @objc function to handle touch events on the custom popover
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        if(touch.view!.isDescendant(of: verdorOrderTable))
        {
            return false;
        }
        if let innerView = self.view.viewWithTag(181) as? ced_orderFilterpopup {
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
            if loading && !self.refreshControl.isRefreshing{
                self.page += 1;
                Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
                self.loadVendorOrders(page);
                
            }
            
        }
        
    }
}
extension ced_vendorOrderpannel:UITextFieldDelegate{
    @objc func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.resignFirstResponder()
 
        /* background view */
        let bgCView = UIView();
        bgCView.tag=161;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        
        
        let colorGreen = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        let colorRed = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String;
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!);
        
        let tempInrView = UIView();
        tempInrView.tag=131;
        tempInrView.frame = CGRect(x: 0, y: CGFloat(0), width: UIScreen.main.bounds.width-100,height: CGFloat(270));
        tempInrView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        tempInrView.backgroundColor = UIColor.white;
        tempInrView.center = CGPoint(x: bgCView.frame.size.width  / 2,y: bgCView.frame.size.height / 2);
        
        let label = UILabel();
        label.frame = CGRect(x: 0, y: CGFloat(0), width: tempInrView.frame.width,height: CGFloat(30));
        label.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        label.text = "Select Date".localized.uppercased();
        label.textAlignment = NSTextAlignment.center;
        label.textColor = UIColor.white;
        label.backgroundColor = color;
        tempInrView.addSubview(label);
        
        let datePickerView = UIDatePicker()
        datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
        datePickerView.datePickerMode = UIDatePicker.Mode.date;
        datePickerView.maximumDate = Date()
        datePickerView.setValue(UIColor.black, forKeyPath: "textColor");
        datePickerView.backgroundColor = UIColor.white;
        datePickerView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        datePickerView.frame = CGRect(x: 0, y: CGFloat(30), width: tempInrView.frame.width,height: CGFloat(200));
        tempInrView.addSubview(datePickerView);
        
        
        let twoButtonView = TwoButtonView();
        twoButtonView.tag=141;
        twoButtonView.frame = CGRect(x: 0, y: CGFloat(230), width: tempInrView.frame.width,height: CGFloat(40));
        twoButtonView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        tempInrView.addSubview(twoButtonView);
        
        twoButtonView.cancelButton.addTarget(self, action: #selector(cancelDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.cancelButton.backgroundColor = colorRed;
        
        
//        twoButtonView.doneButton.addTarget(self, action: #selector(pickDateFromDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.doneButton.backgroundColor = colorGreen;
        twoButtonView.doneButton.rx.tap.bind{
            let dateFormatter = DateFormatter()
            dateFormatter.locale = Locale(identifier: "en_US_POSIX")
            dateFormatter.dateStyle = DateFormatter.Style.short
            dateFormatter.timeStyle = DateFormatter.Style.none
            dateFormatter.dateFormat = "yyyy-MM-dd";
            let timeFormatter = DateFormatter()
            timeFormatter.locale = Locale(identifier: "en_US_POSIX")
            timeFormatter.dateFormat = "hh:mm:ss"
            let time = timeFormatter.string(from: Date())
            let dateDisplay = dateFormatter.string(from: datePickerView.date);
            textField.text = dateDisplay
            textField.resignFirstResponder();
            self.view.viewWithTag(161)?.removeFromSuperview();
        }.disposed(by: disposeBag)
        
        tempInrView.makeCardUsingThemeColor(tempInrView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
        
        bgCView.addSubview(tempInrView);
        self.view.addSubview(bgCView);
  
    }
  
    @objc func cancelDatePickerView(_ sender:UIButton){
        
        sender.resignFirstResponder();
        
        self.view.viewWithTag(161)?.removeFromSuperview();
    }
}
