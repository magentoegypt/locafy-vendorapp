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
import RxCocoa
import RxSwift

class ced_viewTransactions: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate,UIGestureRecognizerDelegate {
    
    @IBOutlet weak var showfilter: UIButton!
    @IBOutlet weak var titleContainer: UIView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var topButton: UIButton!
    @IBOutlet weak var viewTransactionTable: UITableView!
    let refreshControl = UIRefreshControl()
    let disposeBag = DisposeBag()
    
    var applyFilter = false
    var filterString = String()
    var vendorTransactionArray = [[String:String]]()
    var otherData = [String:String]()
    var filterPostdata = NSDictionary()
    var loading = true
    var page = 1
    var transactionId=String()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        self.title = "View Transactions".localized
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        // Do any additional setup after loading the view.
        if #available(iOS 15.0, *) {
            viewTransactionTable.sectionHeaderTopPadding = 0
        } else {
            // Fallback on earlier versions
        }
        viewTransactionTable.register(ced_newViewTransactTableViewCell.self, forCellReuseIdentifier: ced_newViewTransactTableViewCell.reuseId)
        viewTransactionTable.delegate = self
        viewTransactionTable.dataSource = self
        viewTransactionTable.estimatedRowHeight = 100
        titleContainer.makeCard(titleContainer, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let bodycolorString = Ced_CommonVendor.getInfoPlist("bodyColor") as! String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        let bodycolor = Ced_CommonVendor.UIColorFromRGB(bodycolorString)
        titleLabel.backgroundColor = color
        titleLabel.text=" TRANSACTIONS".localized
        viewTransactionTable.backgroundColor = DynamicColor.ViewBackgroundColor
        self.showfilter.addTarget(self, action: #selector(ced_viewTransactions.showFilterPopup(_:)), for: UIControl.Event.touchUpInside)
        ced_navigationBarController().addNavButton(self,str:"no".localized)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        //self.loadData(1)
        self.loadData()
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        viewTransactionTable.refreshControl = refreshControl
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func refresh(_ sender: AnyObject) {
       
        self.vendorTransactionArray.removeAll()
        viewTransactionTable.reloadData()
        loadData()
    }
    
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(section == 0){
            if(otherData.count == 0){
                return 0
            }
            if(otherData["orders"] == "NO_ORDER"){
                return 1
            }
            return 1
        }else{
            return vendorTransactionArray.count
        }
        
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if(indexPath.section == 1){
//            let ceell = tableView.dequeueReusableCell(withIdentifier: "abc", for: indexPath)
//            ceell.textLabel?.text = "aushdoi hjuaeisru deifjriow[  eirfjopw[]'aweqiow;dmnr er3juihfwioprn ewiruhweiuon djw ern byhwiu hjfrjkw rui bnyio;uw ernkwandcr89iowj 4rkwnavu8i9r eikloftje'ao iloefruiewsyfro wke fr9io8wsu row ci nufrsw9 frw'wsiodeu wi ejrd IOP9; WI; RI FIOU89 RF rfihwjei9n  nf bnrcio rwob r98w 3rklrun j98w kerjmwo krj;nwhofiyri;  reiuhwybnl;ore h uerh bfowi erjwu bhdw irj ueiwo rcw wiuhr  in v aeis9id8unp retneat vtgreh oeihryow89i4y4apq "
//            ceell.textLabel?.numberOfLines = 0
//            return ceell
            let cell = tableView.dequeueReusableCell(withIdentifier: ced_newViewTransactTableViewCell.reuseId, for: indexPath) as! ced_newViewTransactTableViewCell
            cell.populate(with: vendorTransactionArray[indexPath.row])
            print(vendorTransactionArray[indexPath.row])
            return cell
//            let cell = viewTransactionTable.dequeueReusableCell(withIdentifier: "transctdataCell") as! ced_transactData
//            cell.transactionId.text = vendorTransactionArray[indexPath.row]["transaction_id"]
//            cell.CreatedAt.text = vendorTransactionArray[indexPath.row]["created_at"]
//            cell.amount.text = vendorTransactionArray[indexPath.row]["amount"]!
//            cell.netAmount.text = vendorTransactionArray[indexPath.row]["net_amount"]!
//            cell.adjustmentAmount.text = vendorTransactionArray[indexPath.row]["adjustable_amount"]!
//            if vendorTransactionArray[indexPath.row]["payment_method"]=="0"
//            {
//                cell.paymentMode.text = "Offline".localized
//            }
//            else if vendorTransactionArray[indexPath.row]["payment_method"]=="1"
//            {
//                cell.paymentMode.text = "Online".localized
//            }
//            else
//            {
//                cell.paymentMode.text = vendorTransactionArray[indexPath.row]["payment_method"]
//            }
//
//            cell.actionButton.tag=indexPath.row
//            cell.actionButton.tintColor=UIColor.black
//            cell.actionButton.addTarget(self, action: #selector(actionButtonPressed(_:)), for: .touchUpInside)
//            return cell
        }else{
            if(otherData["orders"] == "NO_ORDER"){
                let cell = UITableViewCell(style: UITableViewCell.CellStyle.default, reuseIdentifier: "default")
                cell.textLabel?.text = "You have no transcations".localized
                cell.textLabel?.contentMode = .center
                return cell
            }else{
                let cell = viewTransactionTable.dequeueReusableCell(withIdentifier: "transactcell")  as! ced_viewtransactpend
                //cell.totalEarnedAmount.text = otherData["earnedAmount"]
                cell.totalEarnedAmount.text = otherData["total_earned_amount"]!
                cell.totalPendingAmount.text = otherData["pendingAmount"]!
                cell.pendingTransfers.text = otherData["pendingTransfer"]
                return cell
            }
            
        }
        
    }
    @objc func actionButtonPressed(_ sender:UIButton)
    {
        let tag = sender.tag
        if  let transaction = vendorTransactionArray[tag]["payment_id"]{
            let view = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "viewTansactView") as! ced_viewTransactionView
            view.transactionId = transaction
            self.navigationController?.pushViewController(view, animated: true)
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.section == 1){
            return UITableView.automaticDimension
        }else{
            if(otherData["orders"] == "NO_ORDER"){
                return 40
            }
            return 180
        }
        
    }
    
//    func tableView(_ tableView: UITableView, titleForFooterInSection section: Int) -> String? {
//        if section == 0{
//            if(otherData["orders"] == "NO_ORDER"){
//                return "You have no transcations".localized
//            }
//        }
//        return ""
//    }
    
    // @objc func loadData(_ page:Int){
    @objc func loadData(){
        print("view list transaction")
        let baseUrl = "vendorapi/vtransaction/payment"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        if(applyFilter){
            postString += "&filter=" + filterString//.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!
        }
        print("-----")
        print(postString)
        self.sendRequest(url: baseUrl, parameters: postString)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        refreshControl.endRefreshing()
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print("view list json")
            print(json)
//            if json["data"]["success"]==true
//            {
                //            print("wwwweeeeee")
                self.parseData(json)
//            }else{
//
//            }
        }
        
    }
    func parseData(_ json:JSON){
        let pendingTransfer = json["data"]["pendingTransfer"].stringValue
        let earnedAmount = json["data"]["earnedAmount"].stringValue
        let pendingAmount = json["data"]["pendingAmount"].stringValue
        let orders = json["data"]["transactiondata"].stringValue
        otherData=["pendingTransfer":pendingTransfer,"total_earned_amount":earnedAmount,"pendingAmount":pendingAmount,"orders":orders]
        for viewdata in json["data"]["transactiondata"].arrayValue{
//            let amount = viewdata["amount"].stringValue
//            let payment_id = viewdata["payment_id"].stringValue
//            let transaction_id = viewdata["transaction_id"].stringValue
//            let created_at = viewdata["created_at"].stringValue
//            let adjustment_amount = viewdata["adjustment_amount"].stringValue
//            let net_amount = viewdata["net_amount"].stringValue
//            let payment_mode = viewdata["payment_mode"].stringValue
            var transactdata = [String:String]()
            for (itm,val) in viewdata.dictionaryValue{
                transactdata[itm] = val.stringValue
            }
            vendorTransactionArray.append(transactdata)
        }
        DispatchQueue.main.async {
            self.viewTransactionTable.reloadData()
        }
        return
    }
    
    @objc func showFilterPopup(_ sender:UIButton){
        /* background view */
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let filterbyView = ced_viewFilter();
        self.addgesturesTohideView(self.view)
        if(filterPostdata.count != 0){
            filterbyView.createdAtfrom?.text = (filterPostdata.object(forKey: "created_at") as AnyObject).object(forKey: "from") as? String
            filterbyView.createdTo?.text = (filterPostdata.object(forKey: "created_at") as AnyObject).object(forKey: "to") as? String
            filterbyView.paymentMode?.text = filterPostdata.object(forKey: "payment_method") as? String
            filterbyView.amountFrom?.text = (filterPostdata.object(forKey: "amount") as AnyObject).object(forKey: "from") as? String
            filterbyView.amountTo?.text = (filterPostdata.object(forKey: "amount") as AnyObject).object(forKey: "to") as? String
            filterbyView.netamountfrom?.text = (filterPostdata.object(forKey: "net_amount") as AnyObject).object(forKey: "from") as? String
            filterbyView.netAmountTo?.text = (filterPostdata.object(forKey: "net_amount") as AnyObject).object(forKey: "to") as? String
            
            filterbyView.trasactionId?.text = filterPostdata.object(forKey: "transaction_id") as? String
            
            filterbyView.adjustmentAmountTo?.text = (filterPostdata.object(forKey: "fee") as AnyObject).object(forKey: "to") as? String
            filterbyView.adjustmentAmount?.text = (filterPostdata.object(forKey: "fee") as AnyObject).object(forKey: "from") as? String
        }
        
        filterbyView.applyFilters.addTarget(self, action: #selector(ced_viewTransactions.applyFilters(_:)), for: UIControl.Event.touchUpInside)
        filterbyView.resetFilters.addTarget(self, action: #selector(ced_viewTransactions.resetFilters(_:)), for: UIControl.Event.touchUpInside)
        filterbyView.createdAtfrom.delegate = self
        filterbyView.createdTo.delegate = self
        filterbyView.tag = 181;
        filterbyView.backgroundColor = UIColor.black;
        filterbyView.frame = CGRect(x: 25, y: 60, width: self.view.frame.width - 50, height: 440)
        filterbyView.center = self.view.center
        bgCView.addSubview(filterbyView)
        self.view.addSubview(bgCView);
    }
    
    @objc func applyFilters(_ sender:UIButton){
        vendorTransactionArray = [[String:String]]()
        otherData = [String:String]()
        self.applyFilter = true
        let view = self.view.viewWithTag(181) as! ced_viewFilter
        var createdAtfr = ""
        if(view.createdAtfrom.text != nil){
            createdAtfr =  view.createdAtfrom.text!
        }
        var createdAtTo  = ""
        if(view.createdTo.text != nil){
            createdAtTo = view.createdTo.text!
        }
        var paymentMod = ""
        if(view.paymentMode.text != nil){
            paymentMod = view.paymentMode.text!
        }
        var transactionId = ""
        if(view.trasactionId.text != nil){
            transactionId = view.trasactionId.text!
        }
        var amountFro  = ""
        if(view.amountFrom.text != nil){
            amountFro = view.amountFrom.text!
        }
        var amountTo = ""
        if(view.amountTo.text != nil){
            amountTo = view.amountTo.text!
        }
        var adjustmentAmounfom = ""
        if(view.adjustmentAmount.text != nil){
            adjustmentAmounfom = view.adjustmentAmount.text!
        }
        
        var adjustmentAmountT  = ""
        if(view.adjustmentAmountTo.text != nil){
            adjustmentAmountT = view.adjustmentAmountTo.text!
        }
        var netamountfro = ""
        if(view.netamountfrom.text != nil){
            netamountfro = view.netamountfrom.text!
        }
        var netamountto = ""
        if(view.netAmountTo.text != nil){
            netamountto = view.netAmountTo.text!
        }
        let createdAt = ["to":createdAtTo,"from":createdAtfr]
        let amount = ["to":amountTo,"from":amountFro]
        let netAnm = ["to":netamountto,"from":netamountfro]
        let adjustamnt = ["to":adjustmentAmountT,"from":adjustmentAmounfom]
        let postdata = ["fee":adjustamnt,"amount":amount,"net_amount":netAnm,"created_at":createdAt,"payment_method":paymentMod,"transaction_id":transactionId] as [String : Any]
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
        filterPostdata = postdata as NSDictionary
        print(filterPostdata)
        self.view.viewWithTag(151)?.removeFromSuperview();
        //self.loadData(1)
        self.loadData()
    }
    
    @objc func resetFilters(_ sender:UIButton){
        filterPostdata = NSDictionary()
        vendorTransactionArray = [[String:String]]()
        filterString = String()
        applyFilter = false
        self.view.viewWithTag(151)?.removeFromSuperview();
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        //self.loadData(1)
        self.loadData()
    }
    
    //@objc function to dismiss the custom popover
    @objc func hideView(_ recognizer: UITapGestureRecognizer)
    {
        self.view.viewWithTag(151)?.removeFromSuperview();
    }
    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(ced_viewTransactions.hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_viewTransactions.hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_viewTransactions.hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_viewTransactions.hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_viewTransactions.hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    //delegate @objc function to handle touch events on the custom popover
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        if(touch.view!.isDescendant(of: viewTransactionTable))
        {
            return false;
        }
        if let innerView = self.view.viewWithTag(181) as? ced_viewFilter {
            if(touch.view!.isDescendant(of: innerView))
            {
                return false;
            }
            return true;
        }
        return true;
    }
    
    
    /* @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
     let currentOffset = scrollView.contentOffset.y
     let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
     if (maximumOffset - currentOffset) <= 40 {
     if loading{
     self.page += 1;
     Alert_File.addLoadingIndicator(self, msg: "Loading...")
     self.loadData(page);
     
     }
     
     }
     
     }*/
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.section == 1{
            let tag = indexPath.row
            if  let transaction = vendorTransactionArray[tag]["payment_id"]{
                let view = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "viewTansactView") as! ced_viewTransactionView
                view.transactionId = transaction
                self.navigationController?.pushViewController(view, animated: true)
            }
        }
        /* if  let transaction = vendorTransactionArray[indexPath.row]["payment_id"]{
         let view = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "viewTansactView") as! ced_viewTransactionView
         view.transactionId = transaction
         self.navigationController?.pushViewController(view, animated: true)
         }*/
    }
    override func viewWillLayoutSubviews() {
        
    }
    
}

extension ced_viewTransactions:UITextFieldDelegate{
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
